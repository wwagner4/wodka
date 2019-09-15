/*
 * Created on Mar 13, 2004
 */
package wodka.results;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import wodka.ApplicationManager;
import wodka.WodkaException;
import wodka.ga.GeneticAlgorithm;
import wodka.ga.soda.SodaraceGeneticAlgorithm;
import wodka.util.Exporter;
import wodka.util.IoUtil;
import wodka.util.PropertyManager;

/**
 * Builds html , race xml and race jnlp files of finished GAs.
 *  
 */
public class ZooBuilder implements ResultsHandler{

    private static Logger logger = Logger.getLogger("wodka.results");
    
    private static final String PROP_INDIR = "zoobuilder.indir";
    private static final String PROP_OUTDIR = "zoobuilder.outdir";
    private static final String PROP_SERVERURL = "zoobuilder.serverurl";
    private static final String RACE_TEMPLATE = "templates/race.jnlp";
    private static final String WEB_XML_TEMPLATE = "templates/web.xml";

    private transient Exporter exporter = Exporter.current();
    private transient IoUtil util = IoUtil.current();
    private transient HtmlHelper helper = null;
    private transient ApplicationManager appMan = ApplicationManager.current();
    private String serverUrl;
    private PrintWriter indexHtml;
    private File outDir;

    public ZooBuilder() {
        super();
        helper = new HtmlHelper(this);
        PropertyManager pMan = appMan.getPropManager();
        pMan.addDesc(PROP_INDIR, "batch_results", "Directory relative to WODKA_HOME, containing result files.");
        pMan.addDesc(PROP_OUTDIR, "d:/pgm/tomcat/webapps/wodkazoo",
                "Directory for output, containing result files. The path has to be absolute.",
                "Typically some directory that can be accessed from a webserver.");
        pMan.addDesc(PROP_SERVERURL, "http://localhost:8080/wodkazoo/",
                "The url where the exported files will be deployed.");

    }
    private void run () throws WodkaException {
        try {
            appMan.initLogging();
            String outDirectory = appMan.getProperty(PROP_OUTDIR);
            serverUrl = appMan.getProperty(PROP_SERVERURL);
            outDir = new File(outDirectory);
            String inDirectory = appMan.getProperty(PROP_INDIR);
            File inDir = IoUtil.current().createDirAtHome(inDirectory);
            logger.info("inDir:" + inDir.getAbsolutePath());
            logger.info("outDir:" + outDir.getAbsolutePath());
            logger.info("serverUrl:" + serverUrl);
            initOutDir(outDir);
            File[] algoFiles = inDir.listFiles();
            if (algoFiles.length == 0) {
                throw new WodkaException("No files in input directory. " + inDir.getAbsolutePath());
            }
            indexHtml = null;
            try {
                indexHtml = initIndexHtml(outDir);
                ResultsIterator iter = new ResultsIterator(inDir, this);
                iter.iterate();
                helper.writeIndexBottom(indexHtml);
            } finally {
                if (indexHtml != null) {
                    indexHtml.close();
                }
            }
            logger.info("finished");
        } catch (IOException e) {
            throw new WodkaException(e);
        } 
    }

    public static void main (String[] args) {
        try {
            ZooBuilder builder = new ZooBuilder();
            builder.run();
        } catch (WodkaException exc) {
            exc.printStackTrace();
        }
    }

    private void initOutDir (File oDir) throws IOException {
        if (!oDir.exists()) {
            oDir.mkdirs();
        } else {
            util.deleteDirContents(oDir);
        }
        File webInfDir = util.createDir(oDir, "WEB-INF");
        util.resourceToDir(WEB_XML_TEMPLATE, webInfDir, "web.xml");
        util.resourceToDir("templates/style.css", oDir, "style.css");
    }

    public void handle(SodaraceGeneticAlgorithm algo, String fileName) throws WodkaException {
        try {
            writeRaceXmls(algo, fileName, outDir);
            writeRaceJnlps(fileName, serverUrl, outDir);
            helper.writeIndexEntry(algo, fileName, fileName, indexHtml);
            writeDetails(algo, fileName, fileName, outDir);
        } catch (IOException e) {
            throw new WodkaException(e);
        } 
    }

    
    private void writeRaceJnlps (String num, String serUrl, File oDir) throws IOException {
        writeJnlp(this.getNameFastXml(num), this.getNameFastJnlp(num), serUrl, oDir);
        writeJnlp(this.getNameHistXml(num), this.getNameHistJnlp(num), serUrl, oDir);
    }

    private void writeRaceXmls (SodaraceGeneticAlgorithm genAlgo, String num, File oDir) throws IOException, WodkaException {
        String raceFastestName = getNameFastXml(num);
        FileWriter fWriter = null;
        try {
            fWriter = new FileWriter(new File(oDir, raceFastestName));
            exporter.exportRaceFastest(genAlgo, fWriter, genAlgo.getTerrManager().getTerrainXml(genAlgo));
            logger.info("wrote race xml: " + raceFastestName);
        } finally {
            if (fWriter != null) {
                fWriter.close();
            }
        }

        String raceNameHist = getNameHistXml(num);
        try {
            fWriter = new FileWriter(new File(oDir, raceNameHist));
            exporter.exportRaceHistory(genAlgo, fWriter, genAlgo.getTerrManager().getTerrainXml(genAlgo));
            logger.info("wrote race xml: " + raceNameHist);
        } finally {
            if (fWriter != null) {
                fWriter.close();
            }
        }
    }

    String getNameHistXml (String num) {
        return "race_hist_" + num + ".xml";
    }

    String getNameFastXml (String num) {
        return "race_fastest_" + num + ".xml";
    }

    String getNameHistJnlp (String num) {
        return "race_hist_" + num + ".jnlp";
    }

    String getNameFastJnlp (String num) {
        return "race_fastest_" + num + ".jnlp";
    }

    String getNameDetailsHtml (String num) {
        return "details_" + num + ".html";
    }

    private PrintWriter initIndexHtml (File pOutDir) throws IOException {
        File file = new File(pOutDir, "index.html");
        FileWriter writer = new FileWriter(file);
        PrintWriter pWriter = new PrintWriter(writer);
        helper.writeIndexHeadLine(pWriter);
        return pWriter;
    }

    private void writeDetails (GeneticAlgorithm genAlgo, String num, String fileName, File pOutDir) throws IOException {
        File file = new File(pOutDir, this.getNameDetailsHtml(num));
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            PrintWriter pWriter = new PrintWriter(writer);
            helper.writeDetails(genAlgo, num, fileName, pWriter);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private void writeJnlp (String xmlFileName, String jnlpFileName, String serverName, File pOutDir) throws IOException {
        String templ = util.resourceToString(RACE_TEMPLATE);
        String strA = StringUtils.replace(templ, "${XML_FILE}", xmlFileName);
        String strB = StringUtils.replace(strA, "${SERVER}", serverName);
        util.stringToFile(strB, new File(pOutDir, jnlpFileName));
        logger.info("wrote jnlp: " + jnlpFileName);
    }

}