package wodka.results;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import wodka.WodkaException;
import wodka.ga.GeneticAlgorithm;
import wodka.ga.soda.SodaraceGeneticAlgorithm;
import wodka.util.PersistanceHandler;
import wodka.util.StreamPersistable;

/**
 * Iterates over the results of a Directory.
 */
public class ResultsIterator {

    private static Logger logger = Logger.getLogger("wodka.results");

    private File inDir = null;

    private ResultsHandler handler;

    private ResultsIterator() {
        super();
    }

    public ResultsIterator(File inDir, ResultsHandler handler) {
        this();
        this.inDir = inDir;
        this.handler = handler;
    }

    public void iterate() throws WodkaException {
        if (!inDir.exists()) {
            throw new WodkaException("The directory '" + inDir.getAbsolutePath() + "' does not exist");
        }
        File[] algoFiles = inDir.listFiles();
        if (algoFiles.length == 0) {
            throw new WodkaException("No files in input directory. "
                    + inDir.getAbsolutePath());
        }
        for (int i = 0; i < algoFiles.length; i++) {
            try {
                SodaraceGeneticAlgorithm genAlgo = (SodaraceGeneticAlgorithm) loadGenAlgo(algoFiles[i]);
                this.handler.handle(genAlgo, algoFiles[i].getName());
            } catch (WodkaException e1) {
                logger.info("could not handle: " + algoFiles[i].getName()
                        + " <" + e1.getMessage() + ">");
            }
        }
    }

    private GeneticAlgorithm loadGenAlgo(File file) throws WodkaException {
        try {
            logger.info("loading from file: " + file.getName());
            StreamPersistable persistable = PersistanceHandler.current()
                    .loadStreamPersistable(file);
            if (!(persistable instanceof GeneticAlgorithm)) {
                throw new WodkaException("Invalid Datatype in file. "
                        + persistable.getClass().getName());
            }
            return (GeneticAlgorithm) persistable;
        } catch (IOException e) {
            throw new WodkaException(e);
        }
    }

}