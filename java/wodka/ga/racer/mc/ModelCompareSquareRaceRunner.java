/*
 * ModelCompareSquare.java
 *
 * Created on March 10, 2003, 8:19 AM
 */

package wodka.ga.racer.mc;

/**
 *
 * @author  wolfi
 */
public class ModelCompareSquareRaceRunner extends ModelCompareRaceRunner {

    public ModelCompareSquareRaceRunner() {
        super();
    }
    protected GridFunction getGridFunction() {
        return new Log();
    }
    protected ModelHolder getModelHolder() {
        return new SquareHolder();
    }

}
