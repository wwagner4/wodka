/*
 * ModelCompareSquare.java
 * 
 * Created on March 10, 2003, 8:19 AM
 */

package wodka.ga.racer.mc;

/**
 * @author wolfi
 */
public class ModelCompareLinearSquareRaceRunner
    extends ModelCompareRaceRunner {

    public ModelCompareLinearSquareRaceRunner() {
        super();
    }

    public String getName() {
        return "MC-Lin-Square";
    }
    protected void writeDescription(java.io.PrintWriter pw) {
        pw.print(
            "Tries to build a square by means of model comparison. The gridwidth grows linear.");
        pw.println();
    }

    protected GridFunction getGridFunction() {
        return new Linear();
    }

    protected ModelHolder getModelHolder() {
        return new SquareHolder();
    }

}
