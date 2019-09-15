package wodka.ga.soda;

import wodka.ga.GeneticAlgorithm;

/**
 * A Listener to a GeneticAlgorithm. Why not 
 */

public interface BreederListener {

    void performEvaluationOfStepFinished(GeneticAlgorithm genAlgo);
}