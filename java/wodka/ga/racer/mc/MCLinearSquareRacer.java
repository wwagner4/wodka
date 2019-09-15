/*
 * ModelCompareSquare.java
 *
 * Created on March 10, 2003, 8:19 AM
 */

package wodka.ga.racer.mc;

import wodka.ga.racer.RaceRunner;

/**
 * 
 * @author wolfi
 */
public class MCLinearSquareRacer extends AbstractMCRacer {

    private static final long serialVersionUID = 1L;

    public MCLinearSquareRacer() {
        super();
    }

    public String getLabel() {
        return "MC-Lin-Square";
    }

    protected void writeDescription(java.io.PrintWriter pw) {
        pw
                .print("Tries to build a square by means of model comparison. The gridwidth grows linear.");
        pw.println();
    }

    protected GridFunction getGridFunction() {
        return new Linear();
    }

    protected ModelHolder getModelHolder() {
        return new SquareHolder();
    }

    /**
     * 
     * @see wodka.ga.racer.AbstractRacer#createRaceRunner()
     */
    public RaceRunner createRaceRunner() {
        return new ModelCompareLinearSquareRaceRunner();
    }

}
