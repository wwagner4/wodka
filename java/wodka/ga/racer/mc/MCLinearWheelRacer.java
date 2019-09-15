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
public class MCLinearWheelRacer extends AbstractMCRacer {

    private static final long serialVersionUID = 1L;

    public MCLinearWheelRacer() {
        super();
    }

    public String getLabel() {
        return "MC-Lin-Wheel";
    }

    protected void writeDescription(java.io.PrintWriter pw) {
        pw
                .print("Tries to build a wheel like structure by means of model comparison. "
                        + "The gridwidth grows linear.");
        pw.println();
    }

    protected GridFunction getGridFunction() {
        return new Linear();
    }

    protected ModelHolder getModelHolder() {
        return new WheelHolder();
    }

    public RaceRunner createRaceRunner() {
        return new ModelCompareLinearWheelRaceRunner();
    }

}
