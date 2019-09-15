package wodka.results;

import wodka.WodkaException;
import wodka.ga.soda.SodaraceGeneticAlgorithm;

/**
 * Handles a Result. Is called from a ResultsIterator.
 */
public interface ResultsHandler {
    
    void handle(SodaraceGeneticAlgorithm algo, String fileName) throws WodkaException;

}
