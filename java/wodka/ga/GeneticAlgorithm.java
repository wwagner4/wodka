package wodka.ga;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import wodka.ApplicationManager;
import wodka.ExceptionHandler;
import wodka.WodkaException;
import wodka.util.StreamPersistable;
import wodka.util.StreamPersistor;
import wodka.util.Util;
import wodka.view.CategorizedInfoModel;
import wwan.commons.param.Configurable;
import wwan.commons.param.ListParam;
import wwan.commons.param.ListParamDesc;
import wwan.commons.param.ParamDesc;
import wwan.commons.param.ParamException;
import wwan.commons.param.enums.ClassesParamDesc;
import wwan.commons.param.enums.EnumParamDesc;
import wwan.commons.param.enums.StringPopulator;

/**
 * Implementation of the genetic algorithm.
 */

public class GeneticAlgorithm implements StreamPersistable, Configurable,
        CategorizedInfoModel {

    private static final long serialVersionUID = 1L;

    private static final String PARAM_NAME_MUTATIONRATE = "mutationRate";

    protected static final String NEW_LINE = "\n";

    protected Population population = new Population();

    private PopulationHistory popHist = new PopulationHistory();

    private int popCount = 0;

    private SelectionPolicy selPolicy = null;

    private FitnessFunction fitFunc = null;

    private GenotypeDesc genoDesc = null;

    protected transient boolean stopped = false;

    protected transient boolean changed = false;

    private String label = null;

    private transient int prevMaxFit = -1;

    private transient boolean resetTimeoutOptimization = false;

    private double mutationRate = 0.01;

    protected transient ExceptionHandler handler = null;

    private static Logger logger = Logger.getLogger(GeneticAlgorithm.class);

    private boolean calcTimeoutOptimized = false;

    public GeneticAlgorithm() {
        super();
    }

    public boolean isCalcTimeoutOptimized() {
        return this.calcTimeoutOptimized;
    }

    public void setCalcTimeoutOptimized(boolean calcTimeoutOptimized) {
        this.calcTimeoutOptimized = calcTimeoutOptimized;
    }

    public int getPopCount() {
        return this.popCount;
    }

    public int getPrevMaxFit() {
        return this.prevMaxFit;
    }

    public double getPrevMaxFitSoda() {
        return Util.current().convertFitnessWodkaToSoda(this.prevMaxFit);
    }

    public FitnessFunction getFitFunc() {
        return this.fitFunc;
    }

    public void setFitFunc(FitnessFunction fitFunc) {
        this.fitFunc = fitFunc;
    }

    public Population getPopulation() {
        return this.population;
    }

    public PopulationHistory getPopHist() {
        return this.popHist;
    }

    public void setHandler(ExceptionHandler handler) {
        this.handler = handler;
    }

    public GenotypeDesc getGenoDesc() {
        return this.genoDesc;
    }

    public void setGenoDesc(GenotypeDesc gDesc) throws WodkaException {
        if (this.genoDesc == null || !this.genoDesc.hasEqualContents(gDesc)) {
            this.genoDesc = gDesc;
            this.createInitialPopulation();
        }
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public double getMutationRate() {
        return this.mutationRate;
    }

    public SelectionPolicy getSelPolicy() {
        return this.selPolicy;
    }

    public void setSelPolicy(SelectionPolicy policy) {
        this.selPolicy = policy;
    }

    public void createNextPopulation() {
        this.population.sort();
        this.popHist.addPopulation(this.population);
        checkIncreaseOfFitness();
        Population newPop = this.selPolicy.selectNewPopulation(this.population,
                this.genoDesc, this.mutationRate);
        newPop.setNumber(this.popCount++);
        this.population = newPop;
        for (int i = 0; i < this.population.size(); i++) {
            this.population.getIndividual(i).setFitness(0);
        }
    }

    private void checkIncreaseOfFitness() {
        int maxFitness = this.population.getIndividual(0).getFitness();
        if (maxFitness < this.prevMaxFit) {
            if (this.getPopHist().size() >= 1) {
                this.getFitnesses(this.getPopHist().getPopulation(0));
            }
            if (this.getPopHist().size() >= 2) {
                this.getFitnesses(this.getPopHist().getPopulation(1));
            }
            logger
                    .warn("Max fitness of generation "
                            + this.population.getNumber()
                            + " was smaller than the fitness of the current population."
                            + " Actual max fitness: " + maxFitness
                            + " Prev max fitness: " + this.prevMaxFit);
        }
        this.prevMaxFit = maxFitness;
    }

    /*
     * Returns a collection of the fitnesses of a generation. The fitness values
     * are Integer.
     */
    private Collection getFitnesses(Population pop) {
        Collection fitnesses = new ArrayList();
        Iterator iter = pop.getIndividuals().iterator();
        while (iter.hasNext()) {
            Individual indi = (Individual) iter.next();
            fitnesses.add(new Integer(indi.getFitness()));
        }
        return fitnesses;
    }

    public void toStream(DataOutputStream outStream) throws java.io.IOException {
        wodka.util.StreamPersistor.toStream(this.genoDesc, outStream);
        outStream.writeInt(this.popCount);
        wodka.util.StreamPersistor.toStream(this.population, outStream);
        wodka.util.StreamPersistor.toStream(this.popHist, outStream);
        outStream.writeDouble(this.mutationRate);
        wodka.util.StreamPersistor.toStream(this.selPolicy, outStream);
        wodka.util.StreamPersistor.toStream(this.fitFunc, outStream);
        outStream.writeInt(this.prevMaxFit);
        outStream.writeBoolean(this.calcTimeoutOptimized);
    }

    public void fromStream(DataInputStream inStream, int version)
            throws java.io.IOException {
        this.genoDesc = (GenotypeDesc) StreamPersistor.fromStream(inStream);
        this.popCount = inStream.readInt();
        fromStreamPopulationAndHistory(inStream);
        fromStreamMutationRate(inStream, version);
        fromStreamSelPolicy(inStream);
        this.fitFunc = (FitnessFunction) StreamPersistor.fromStream(inStream);
        if (version > 3) {
            this.prevMaxFit = inStream.readInt();
        } else {
            if (this.popHist.size() > 0) {
                Population pop = this.popHist.getPopulation(0);
                if (pop.size() > 0) {
                    Individual indi = pop.getIndividual(0);
                    this.prevMaxFit = indi.getFitness();
                }
            }
        }
        if (version > 5) {
            this.calcTimeoutOptimized = inStream.readBoolean();
        } else {
            this.calcTimeoutOptimized = false;
        }
    }

    public int getVersion() {
        return 6;
    }

    private void fromStreamSelPolicy(DataInputStream inStream)
            throws IOException {
        StreamPersistable streamPers = StreamPersistor.fromStream(inStream);
        setSelPolicy((SelectionPolicy) streamPers);
    }

    private void fromStreamPopulationAndHistory(java.io.DataInputStream inStream)
            throws IOException {
        StreamPersistor.fillFromStream(this.population, inStream);
        StreamPersistor.fillFromStream(this.popHist, inStream);
    }

    private void fromStreamMutationRate(java.io.DataInputStream inStream,
            int version) throws IOException {
        if (version >= 1) {
            this.mutationRate = inStream.readDouble();
        } else {
            this.mutationRate = 0.01;
        }
    }

    private void createInitialPopulation() throws WodkaException {
        if (this.genoDesc == null) {
            throw new WodkaException("No language is defined in GA");
        }
        if (this.selPolicy == null) {
            throw new WodkaException("No selection policy is defined in GA");
        }
        this.population = new Population();
        this.popHist = new PopulationHistory();
        for (int i = 0; i < this.selPolicy.getInitialPopulationSize(); i++) {
            Genotype genotype = this.genoDesc.createRandomGenotype();
            Individual indi = new Individual();
            indi.setGenotype(genotype);
            this.population.addIndividual(indi);
        }
    }

    protected synchronized void pause(int milliSec) {
        try {
            this.wait(milliSec);
        } catch (InterruptedException e) {
            // Continue
        }
    }

    public void stop() {
        if (logger.isDebugEnabled()) {
            logger.debug("received stop");
        }
        this.stopped = true;
    }

    public boolean isChanged() {
        return this.changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public String getLabel() {
        if (this.label == null) {
            initShortName();
        }
        return this.label;
    }

    public void setLabel(String name) {
        this.label = name;
    }

    private void initShortName() {
        String identifier = ApplicationManager.current().nextID() + "";
        this.label = "GA_" + identifier;
    }

    public ListParamDesc getParameterDescription() throws ParamException {
        ListParamDesc lpd = new ListParamDesc();
        lpd.add(getParamDescGenAlgoBase());
        lpd.add(getParamDescGenoTypeDesc());
        lpd.add(getParamDescSelPoly());
        lpd.add(getParamDescRacer());
        lpd.add(getParamDescFitFunc());
        lpd.add(getParamDescTerrainManager());
        return lpd;
    }

    private ParamDesc getParamDescRacer() throws ParamException {
        ClassesParamDesc desc = new ClassesParamDesc();
        desc.setName("racer");
        desc.setLabel("Racer");
        StringPopulator pop = new StringPopulator(
                "wodka.ga.racer.soda.MinsRacer;wodka.ga.racer.soda.EasySodaRacer;"
                        + "wodka.ga.racer.poligon.OctagonBuilderRacer;wodka.ga.racer.poligon.PentagonBuilderRacer");
        desc.populate(pop);
        return desc;
    }

    private ParamDesc getParamDescFitFunc() throws ParamException {
        ClassesParamDesc desc = new ClassesParamDesc();
        desc.setName("ff");
        desc.setLabel("Fitness Function");
        StringPopulator pop = new StringPopulator(
                "wodka.ga.fitfunc.NullFitnessFunction");
        desc.populate(pop);
        return desc;
    }

    private ParamDesc getParamDescTerrainManager() throws ParamException {
        ClassesParamDesc desc = new ClassesParamDesc();
        desc.setName("terrManager");
        desc.setLabel("Terrain Manager");

        StringPopulator pop = new StringPopulator(
                "wodka.ga.terrain.FlatTerrainManager;wodka.ga.terrain.HillyTerrainManager;"
                        + "wodka.ga.terrain.AiTerrainManager;wodka.ga.terrain.AiIncrementalTerrainManager;"
                        + "wodka.ga.terrain.EasyIncTerrainManager");
        desc.populate(pop);
        return desc;
    }

    private ParamDesc getParamDescSelPoly() throws ParamException {
        ClassesParamDesc desc = new ClassesParamDesc();
        desc.setName("sel");
        desc.setLabel("Selection Policy");
        StringPopulator pop = new StringPopulator(
                "wodka.ga.selection.FavourFittest;wodka.ga.selection.Simple20;"
                        + "wodka.ga.selection.Simple40;wodka.ga.selection.AddRandom40");
        desc.populate(pop);
        return desc;
    }

    private ParamDesc getParamDescGenoTypeDesc() throws ParamException {
        ClassesParamDesc desc = new ClassesParamDesc();
        desc.setName("geno");
        desc.setLabel("Genotype");
        StringPopulator pop = new StringPopulator(
                "wodka.ga.geno.lang.assembler.AssemblerLanguage;"
                        + "wodka.ga.geno.lang.turtle.TurtleLanguage");
        desc.populate(pop);
        return desc;
    }

    private ParamDesc getParamDescGenAlgoBase() throws ParamException {
        ListParamDesc grp = new ListParamDesc();
        grp.setLabel("GA Base Parameters");
        grp.add(getParamDescMutRate());
        return grp;
    }

    private ParamDesc getParamDescMutRate() throws ParamException {
        EnumParamDesc desc = new EnumParamDesc();
        desc.setName(PARAM_NAME_MUTATIONRATE);
        desc.setLabel("Mutation Rate");
        desc.setUnit("%");

        StringPopulator pop = new StringPopulator("0.001;0.005;0.01;0.05;0.1");
        desc.populate(pop);

        return desc;
    }

    public void setFieldsFromParameterList(ListParam listParam)
            throws ParamException {
        try {
            this.mutationRate = listParam
                    .getParamValueDouble(PARAM_NAME_MUTATIONRATE);
            this.selPolicy = (SelectionPolicy) listParam.getParamValue("sel");
            this.setGenoDesc((GenotypeDesc) listParam.getParamValue("geno"));
            this.fitFunc = (FitnessFunction) listParam.getParamValue("ff");
        } catch (WodkaException e) {
            throw new ParamException(e);
        }
    }

    public void setParameterListFromFields(ListParam param)
            throws ParamException {
        setParameterListFromFieldMutationRate(param);
        param.setParamValue("geno", this.genoDesc);
        param.setParamValue("ff", this.fitFunc);
    }

    private void setParameterListFromFieldMutationRate(ListParam listParam)
            throws ParamException {
        if (this.mutationRate == 0.001) {
            listParam.setParamValue(PARAM_NAME_MUTATIONRATE, "0.001");
        } else if (this.mutationRate <= 0.001) {
            listParam.setParamValue(PARAM_NAME_MUTATIONRATE, "0.001");
        } else if (this.mutationRate <= 0.005) {
            listParam.setParamValue(PARAM_NAME_MUTATIONRATE, "0.005");
        } else if (this.mutationRate <= 0.01) {
            listParam.setParamValue(PARAM_NAME_MUTATIONRATE, "0.01");
        } else if (this.mutationRate <= 0.05) {
            listParam.setParamValue(PARAM_NAME_MUTATIONRATE, "0.05");
        } else if (this.mutationRate <= 0.1) {
            listParam.setParamValue(PARAM_NAME_MUTATIONRATE, "0.1");
        }
    }

    public String getInfo() {
        return "A genetic algorithm";
    }

    public void checkParameterList(ListParam listParam) throws ParamException {
        // Nothing to be checked
    }

    public void setPopHist(PopulationHistory popHist) {
        this.popHist = popHist;
    }

    public void setPopCount(int popCount) {
        this.popCount = popCount;
    }

    public void setPopulation(Population population) {
        this.population = population;
    }

    protected int calcTimeout() {
        int timeout = 10000;
        if (this.calcTimeoutOptimized) {
            timeout = calcTimeoutOptimized();
        }
        return timeout;
    }

    protected int calcTimeoutOptimized() {
        double timeout;
        if (this.resetTimeoutOptimization) {
            this.resetTimeoutOptimization = false;
            timeout = 10000.0;
        } else {
            timeout = getPrevMaxFitSoda();
            timeout += timeout / 10;
            if (timeout < 10)
                timeout = -1;
            else {
                timeout = Math.max(10, timeout);
                timeout = Math.min(10000, timeout);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("calculated timeout: " + timeout);
        }
        return (int) timeout;
    }

    public void resetTimeoutOptimization() {
        this.resetTimeoutOptimization = true;
    }

    public int categoryCount() {
        return 6;
    }

    public String getCategoryInfo(int index) {
        StringBuffer buffer = new StringBuffer();
        appendCategoryInfo(index, buffer);
        return buffer.toString();
    }

    protected void appendCategoryInfo(int index, StringBuffer buffer) throws Error {
        switch (index) {
        case 0:
            this.appendInfoGeneral(buffer);
            break;
        case 1:
            this.appendInfoGenotype(buffer);
            break;
        case 2:
            this.appendInfoSelectionPolicy(buffer);
            break;
        case 4:
            this.appendInfoFitnessFunction(buffer);
            break;
        }
    }

    public String getCategoryName(int index) {
        switch (index) {
        case 0:
            return "General";
        case 1:
            return "Genotype";
        case 2:
            return "Selection Policy";
        case 4:
            return "Fitness Function";
        default:
            return "";
        }
    }

    private void appendInfoFitnessFunction(StringBuffer buf) {
        FitnessFunction fitFun = getFitFunc();
        if (fitFun != null) {
            buf.append("Fitness Function : " + fitFun.getLabel() + NEW_LINE);
            buf.append(fitFun.getInfo());
        }
    }

    private void appendInfoSelectionPolicy(StringBuffer buf) {
        buf
                .append("Selection Policy : " + getSelPolicy().getLabel()
                        + NEW_LINE);
        buf.append(getSelPolicy().getInfo());
    }

    private void appendInfoGenotype(StringBuffer buf) {
        buf.append("Genotype : " + getGenoDesc().getLabel() + NEW_LINE);
        buf.append(getGenoDesc().getInfo());
    }

    protected void appendInfoGeneral(StringBuffer buf) {
        buf.append("Version : "
                + wodka.ApplicationManager.current().getVersion() + NEW_LINE);
        appendHomeDirectory(buf);
        buf.append("Generations: " + getPopCount() + NEW_LINE);
        buf.append("Mutation rate: " + getMutationRate() + NEW_LINE);
        buf.append("Optimze timeout:"
                + booleanToString(this.calcTimeoutOptimized) + NEW_LINE);
        buf.append("Current population size: " + getPopulation().size()
                + NEW_LINE);
        buf.append("Genotype : " + getGenoDesc().getLabel() + NEW_LINE);
        buf
                .append("Selection Policy : " + getSelPolicy().getLabel()
                        + NEW_LINE);
        FitnessFunction fitFun = getFitFunc();
        if (fitFun == null) {
            buf.append("Fitness Function : " + "null" + NEW_LINE);
        } else {
            buf.append("Fitness Function : " + fitFun.getLabel() + NEW_LINE);
        }
    }

    private String booleanToString(boolean bool) {
        String result = "FALSE";
        if (bool) {
            result = "TRUE";
        }
        return result;
    }

    private void appendHomeDirectory(StringBuffer buf) {
        try {
            buf.append("Home directory:\n"
                    + ApplicationManager.current().homeDirectory()
                            .getCanonicalPath() + NEW_LINE);
        } catch (IOException e) {
            buf.append("Home directory:\n" + e.toString() + NEW_LINE);
        }
    }

    public String toString() {
        return "wodka GeneticAlgorithm.";
    }
}