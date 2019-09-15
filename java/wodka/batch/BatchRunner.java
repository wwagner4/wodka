/*
 * Created on Apr 6, 2004
 */
package wodka.batch;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Random;

import org.apache.log4j.Logger;

import wodka.ApplicationManager;
import wodka.ExceptionHandler;
import wodka.WodkaException;
import wodka.ga.GeneticAlgorithm;
import wodka.ga.GenotypeDesc;
import wodka.ga.SelectionPolicy;
import wodka.ga.fitfunc.NullFitnessFunction;
import wodka.ga.geno.lang.assembler.AssemblerLanguage;
import wodka.ga.geno.lang.turtle.TurtleLanguage;
import wodka.ga.racer.soda.EasySodaRacer;
import wodka.ga.selection.FavourFittest;
import wodka.ga.soda.SodaraceGeneticAlgorithm;
import wodka.ga.soda.TerrainManager;
import wodka.util.PersistanceHandler;
import wodka.util.PropertyManager;
import wodka.util.SodaGlobals;
import wodka.util.SodaGlobalsRanges;
import wodka.util.StreamPersistable;
import wodka.util.StreamPersistor;
import wodka.util.Util;
import wodka.view.CategorizedStreamOutputter;

/**
 * Runs a serie of genetic algorithms as a batch job.
 *  
 */
public class BatchRunner implements ExceptionHandler, StreamPersistable {

    private static final long serialVersionUID = 1L;

    private static final String PROP_MAXGEN = "batch.maxgen";

    private static final String PROP_MUTRATE_MAX = "batch.mutationrate.max";

    private static final String PROP_MUTRATE_MIN = "batch.mutationrate.min";

    private static final String PROP_POPSIZE = "batch.popsize";

    private static final String PROP_GRAVITY_MAX = "batch.gravity.max";

    private static final String PROP_GRAVITY_MIN = "batch.gravity.min";

    private static final String PROP_SPEED_MAX = "batch.speed.max";

    private static final String PROP_SPEED_MIN = "batch.speed.min";

    private static final String PROP_SPRINGY_MAX = "batch.springyness.max";

    private static final String PROP_SPRINGY_MIN = "batch.springyness.min";

    private static final String PROP_FRIC_MAX = "batch.friction.max";

    private static final String PROP_FRIC_MIN = "batch.friction.min";

    public static final String PROP_ASAVE_FILE = "batch.autosave.file";

    public static final String PROP_ASAVE_INTER = "batch.autosave.interval";

    public static final String PROP_TERRAIN_MANAGER = "batch.terrainmanager";

    public static final String PROP_PREFIX = "batch.prefix";

    private transient Random ran = new Random();

    private transient Util util = Util.current();

    private static Logger logger = Logger.getLogger(BatchRunner.class);

    private transient ApplicationManager manager = ApplicationManager.current();

    private int algoCount;

    private int genCount;

    private long runId;

    private SodaraceGeneticAlgorithm algo;

    public BatchRunner() {
        super();
    }

    private void run() throws WodkaException {
        try {
            init();
            if (logger.isInfoEnabled()) {
                logGaInfo();
            }
            while (true) {
                try {
                    runAlgo();
                } catch (WodkaException e) {
                    logger
                            .error(
                                    "Evaluation of the GA caused an exception. Starting a new GA.",
                                    e);
                }
                this.algoCount++;
                createNewAlgo();
            }
        } catch (IOException e) {
            throw new WodkaException(e);
        }
    }

    private void logGaInfo() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println();
        CategorizedStreamOutputter.current().output(this.algo, pw);
        logger.info(sw.getBuffer().toString());
    }

    private void init() throws IOException, WodkaException {
        initProperties();
        this.manager.initLogging();
        startAutoSaveThread();
        startAutoSaveGAThread();
        if (logger.isInfoEnabled()) {
            logPropertiesInfo();
        }
        BatchRunner runner = load();
        if (runner != null) {
            initFromLoaded(runner);
        } else {
            initNew();
        }
    }

    private void logPropertiesInfo() throws IOException {
        Iterator iter = this.manager.getPropManager().allKeysStaringWith(
                "batch").iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            logger.info(key + "=" + this.manager.getProperty(key));
        }
    }

    private void initProperties() {
        PropertyManager pManager = this.manager.getPropManager();
        pManager.addDesc(PROP_ASAVE_FILE, "AutoSave.BatchRunner",
                "The file where the batch autosaver stores his temp results.");
        pManager.addDesc(PROP_ASAVE_INTER, "15",
                "The interval of saving the current batch runner in minutes.");
        pManager.addDesc(PROP_POPSIZE, "50", "Population size.");
        pManager.addDesc(PROP_MAXGEN, "600",
                "Max amount of evaluated generations.");
        pManager.addDesc(PROP_MUTRATE_MAX, "0.1", "Max mutation rate.");
        pManager.addDesc(PROP_MUTRATE_MIN, "0.001", "Min mutation rate.");

        pManager.addDesc(PROP_GRAVITY_MAX, "4.0", "Max gravity. [0.0 - 4.0]");
        pManager.addDesc(PROP_GRAVITY_MIN, "1.0", "Min gravity. [0.0 - 4.0]");
        pManager.addDesc(PROP_SPEED_MAX, "0.05", "Max speed. [0.0 - 0.1]");
        pManager.addDesc(PROP_SPEED_MIN, "0.0", "Min speed. [0.0 - 0.1]");
        pManager.addDesc(PROP_SPRINGY_MAX, "0.03",
                "Max sprynginess. [0.0 - 0.5]");
        pManager.addDesc(PROP_SPRINGY_MIN, "0.0",
                "Min sprynginess. [0.0 - 0.5]");
        pManager.addDesc(PROP_FRIC_MAX, "1.0", "Max friction. [0.0 - 1.0]");
        pManager.addDesc(PROP_FRIC_MIN, "0.3", "Min friction. [0.0 - 1.0]");
        pManager.addDesc(PROP_TERRAIN_MANAGER,
                "wodka.ga.terrain.FlatTerrainManager",
                "Class implementing a terrain this.manager.");
        pManager.addDesc(PROP_PREFIX, "BATCH", "Prefix for output files.");
    }

    private void runAlgo() throws WodkaException {
        try {
            long begin = System.currentTimeMillis();
            while (this.genCount < this.manager.getPropertyInt(PROP_MAXGEN)) {
                evaluateOneGeneration();
                this.genCount++;
            }
            this.genCount = 0;
            long end = System.currentTimeMillis();
            String time = Util.current().formatTime(end - begin);
            logger.info("finished algorithm: " + this.algoCount + " in run " + this.runId
                    + " elapsed: " + time);
            save(this.algo, this.algoCount, this.runId);
        } catch (IOException e) {
            throw new WodkaException(e);
        }
    }

    private void evaluateOneGeneration() throws WodkaException {
        long begin = System.currentTimeMillis();
        this.algo.evaluateOneGeneration();
        if (logger.isDebugEnabled()) {
            long end = System.currentTimeMillis();
            double sodaFit = this.algo.getPrevMaxFitSoda();
            String time = Util.current().formatTime(end - begin);
            logger.debug("finished generation: " + this.genCount + " of "
                    + this.manager.getPropertyInt(PROP_MAXGEN) + ". fitness: "
                    + sodaFit + " elapsed: " + time);
        }
    }

    private void initNew() throws WodkaException {
        this.runId = this.manager.nextID();
        this.algoCount = 0;
        this.genCount = 0;
        createNewAlgo();
        logger.info("started new. runId: " + this.runId);
    }

    private void initFromLoaded(BatchRunner runner) {
        this.runId = runner.runId;
        this.algoCount = runner.algoCount;
        this.genCount = runner.genCount;
        this.algo = runner.algo;
        logger.info("resumed. runId: " + this.runId + " algoCount:" + this.algoCount
                + " this.genCount:" + this.genCount);
    }

    private void createNewAlgo() throws WodkaException {
        try {
            this.algo = new SodaraceGeneticAlgorithm();
            this.algo.setHandler(this);
            this.algo.setRacer(this.createRacer(this.algo));
            this.algo.setSelPolicy(this.createSelectionPolicy());
            this.algo.setGenoDesc(this.createGenoTypeDesc());
            this.algo.setMutationRate(this.util.randomDouble3(this.manager
                    .getPropertyDouble(PROP_MUTRATE_MIN), this.manager
                    .getPropertyDouble(PROP_MUTRATE_MAX)));
            this.algo.setFitFunc(new NullFitnessFunction());
            this.algo.setTerrManager((TerrainManager) this.manager
                    .getPropertyInstance(PROP_TERRAIN_MANAGER));
        } catch (InstantiationException e) {
            throw new WodkaException(e);
        }
    }

    private void startAutoSaveThread() {
        AutoSave save = new AutoSave(this, this);
        Thread thread = new Thread(save);
        thread.start();
        logger.info("AutoSave started.");
    }

    private void startAutoSaveGAThread() {
        AutoSaveGA save = new AutoSaveGA(this, this, this.manager
                .getProperty(PROP_PREFIX));
        Thread thread = new Thread(save);
        thread.start();
        logger.info("AutoSaveGA started.");
    }

    private BatchRunner load() throws WodkaException{
        if (!canResume()) {
            return null;
        }
        File file = new File(this.manager.homeDirectory(), this.manager
                .getProperty(PROP_ASAVE_FILE));
        try {
            return (BatchRunner) PersistanceHandler.current()
                    .loadStreamPersistable(file);
        } catch (Throwable e) {
           throw new WodkaException("Could not load " + file.getAbsolutePath()
                    + " reason: " + message(e), e);
        }
    }

    private String message(Throwable exc) {
        String msg = exc.getMessage();
        if (msg == null || msg.equals("")) {
            msg = exc.toString();
        }
        return msg;
    }

    private boolean canResume() {
        String[] names = this.manager.homeDirectory().list();
        for (int index = 0; index < names.length; index++) {
            if (names[index].equals(this.manager.getProperty(PROP_ASAVE_FILE))) {
                return true;
            }
        }
        return false;
    }

    private void save(GeneticAlgorithm genAlgo, int pAlgoCount, long pRunId)
            throws IOException {
        String fileName = this.manager.getProperty(PROP_PREFIX) + "_" + pRunId
                + "_" + pAlgoCount + ".wodka";
        save(genAlgo, fileName);
    }

    public void save(GeneticAlgorithm genAlgo, String fileName)
            throws IOException {
        PersistanceHandler handler = PersistanceHandler.current();
        File home = ApplicationManager.current().homeDirectory();
        File batchDir = new File(home, "batch_results");
        if (!batchDir.exists()) {
            batchDir.mkdirs();
        }
        File file = new File(batchDir, fileName);
        handler.save(genAlgo, file);
    }

    private SelectionPolicy createSelectionPolicy() {
        FavourFittest favFit = new FavourFittest();
        favFit.setPopSize(this.manager.getPropertyInt(PROP_POPSIZE));
        favFit.setWildcardRate(0.2);
        return favFit;
    }

    private GenotypeDesc createGenoTypeDesc() {
        GenotypeDesc result = null;
        if (this.ran.nextBoolean()) {
            result = createAssembler();
        } else {
            result = createTurtleLang();
        }
        return result;
    }

    private TurtleLanguage createTurtleLang() {
        TurtleLanguage lang = new TurtleLanguage();
        SodaGlobalsRanges ranges = lang.getGlobalRanges();
        defineRanges(ranges);
        lang.setPgmLength(this.util.randomInt(50, 400));
        lang.setGridWidth(this.util.randomInt(10, 60));
        return lang;
    }

    private AssemblerLanguage createAssembler() {
        AssemblerLanguage lang = new AssemblerLanguage();
        SodaGlobalsRanges ranges = lang.getGlobalRanges();
        defineRanges(ranges);
        lang.setPgmLength(this.util.randomInt(50, 200));
        lang.setGridWidth(this.util.randomInt(10, 60));
        return lang;
    }

    private void defineRanges(SodaGlobalsRanges ranges) {
        ranges.setMax(SodaGlobals.GRAVITY, this.manager
                .getPropertyDouble(PROP_GRAVITY_MAX));
        ranges.setMin(SodaGlobals.GRAVITY, this.manager
                .getPropertyDouble(PROP_GRAVITY_MIN));
        ranges.setMax(SodaGlobals.SPEED, this.manager
                .getPropertyDouble(PROP_SPEED_MAX));
        ranges.setMin(SodaGlobals.SPEED, this.manager
                .getPropertyDouble(PROP_SPEED_MIN));
        ranges.setMax(SodaGlobals.SPRINGYNESS, this.manager
                .getPropertyDouble(PROP_SPRINGY_MAX));
        ranges.setMin(SodaGlobals.SPRINGYNESS, this.manager
                .getPropertyDouble(PROP_SPRINGY_MIN));
        ranges.setMax(SodaGlobals.FRICTION, this.manager
                .getPropertyDouble(PROP_FRIC_MAX));
        ranges.setMin(SodaGlobals.FRICTION, this.manager
                .getPropertyDouble(PROP_FRIC_MIN));
    }

    private EasySodaRacer createRacer(SodaraceGeneticAlgorithm genAlgo) {
        EasySodaRacer racer = new EasySodaRacer();
        racer.setRacerListener(genAlgo);
        return racer;
    }

    public void fromStream(DataInputStream inStream, int version)
            throws IOException {
        this.algoCount = inStream.readInt();
        this.genCount = inStream.readInt();
        this.runId = inStream.readLong();
        this.algo = (SodaraceGeneticAlgorithm) StreamPersistor.fromStream(inStream);
    }

    public int getVersion() {
        return 0;
    }

    public void toStream(DataOutputStream outStream) throws IOException {
        outStream.writeInt(this.algoCount);
        outStream.writeInt(this.genCount);
        outStream.writeLong(this.runId);
        StreamPersistor.toStream(this.algo, outStream);
    }

    public void handleThrowable(Throwable throwable) {
        logger.error("An error occurred -> Execution will be stopped.",
                throwable);
        System.exit(-1);
    }

    public int getAlgoCount() {
        return this.algoCount;
    }

    public void setAlgoCount(int algoCount) {
        this.algoCount = algoCount;
    }

    public int getGenCount() {
        return this.genCount;
    }

    public void setGenCount(int genCount) {
        this.genCount = genCount;
    }

    public GeneticAlgorithm getAlgo() {
        return this.algo;
    }

    public void setAlgo(SodaraceGeneticAlgorithm algo) {
        this.algo = algo;
    }

    public long getRunId() {
        return this.runId;
    }

    public void setRunId(long identifier) {
        this.runId = identifier;
    }

    public static void main(String[] args) {
        try {
            new BatchRunner().run();
        } catch (Exception e) {
            logger.error(e.getClass().getName() + " was thrown.", e);
            System.exit(-1);
        }
    }

}