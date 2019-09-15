/*
 * Created on Feb 17, 2004
 */
package wodka.ga;

import wodka.ga.model.Model;
import wodka.util.StreamPersistable;
import wwan.commons.param.Informative;

/**
 * Interface that allows to favour other qualities of a model
 * but the racer fitness. 
 */
public interface FitnessFunction extends Informative, StreamPersistable{
    
    int calculate(Model model, int racerFitness);

}
