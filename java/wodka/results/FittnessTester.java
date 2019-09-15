package wodka.results;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

import racer.RacerException;
import wodka.ApplicationManager;
import wodka.WodkaException;
import wodka.ga.GeneticAlgorithm;
import wodka.ga.Individual;
import wodka.ga.racer.soda.EasySodaRaceRunner;
import wodka.ga.soda.SodaraceGeneticAlgorithm;
import wodka.ga.soda.SodaraceProgram;

/**
 * Test for the right output and calculation of fittnes.
 */
public class FittnessTester implements ResultsHandler {
    
    private static Logger logger = Logger.getLogger("wodka.results");
    private PrintWriter pw;

    public FittnessTester() {
        super();
    }

    public static void main(String[] args) {
        FittnessTester tester = new FittnessTester();
        tester.run();
    }

    private void run() {
        try {
            ApplicationManager man = ApplicationManager.current();
            man.initLogging();
            File wodkaHome = man.homeDirectory();
            File testDir = new File(wodkaHome, "fittest");
            File out = new File(testDir, "out.csv");
            FileWriter fw = new FileWriter(out);
            try {
                pw = new PrintWriter(fw);
                ResultsIterator iter = new ResultsIterator(testDir, this);
                iter.iterate();
            } finally {
                if (pw != null) {
                    pw.close();
                }
            }
            logger.info("finished");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void handle(SodaraceGeneticAlgorithm algo, String fileName)
            throws WodkaException {
        pw.print(fileName);
        printSeperator();
        pw.print(algo.getPrevMaxFitSoda());
        printSeperator();
        String terrainXml = algo.getTerrManager().getTerrainXml(algo);
        pw.print(runSoda(idivOfPop(algo, 0), terrainXml));
        printSeperator();
        pw.print(runSoda(idivOfPop(algo, 1), terrainXml));
        printSeperator();
        pw.print(runSoda(idivOfPop(algo, 2), terrainXml));
        printSeperator();
        pw.print(runSoda(indivOfHist(algo, 0),  terrainXml));
        printSeperator();
        pw.print(runSoda(indivOfHist(algo, 1),  terrainXml));
        printSeperator();
        pw.print(runSoda(indivOfHist(algo, 2),  terrainXml));
        pw.println();
        logger.info("handeled: " + fileName);
    }

    private Individual indivOfHist(GeneticAlgorithm algo, int index) {
        return algo.getPopHist().getPopulation(index).getIndividual(0);
    }

    private Individual idivOfPop(GeneticAlgorithm algo, int index) {
        return algo.getPopulation().getIndividual(index);
    }

    private double runSoda(Individual individual, String terrain) throws WodkaException {
        try {
            EasySodaRaceRunner runner = new EasySodaRaceRunner();
            SodaraceProgram pgm = (SodaraceProgram) individual.getGeno();
            runner.setModel(pgm.eval());
            runner.setTerrainXml(terrain);
            return runner.runRace();
        } catch (IOException e) {
            throw new WodkaException(e);
        } catch (RacerException e) {
            throw new WodkaException(e);
        }
    }

    private void printSeperator() {
        pw.print(";");
    }
}