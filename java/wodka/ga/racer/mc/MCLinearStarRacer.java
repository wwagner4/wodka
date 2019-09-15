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
public class MCLinearStarRacer extends AbstractMCRacer {

    private static final long serialVersionUID = 1L;

    public MCLinearStarRacer() {
        super();
    }

    public String getLabel() {
        return "MC-Lin-Star";
    }

    protected void writeDescription(java.io.PrintWriter pw) {
        pw
                .print("Tries to build a star like structure by means of model comparison. "
                        + "The gridwidth grows linear.");
        pw.println();
    }

    /**
     * 
     * @see wodka.ga.racer.AbstractRacer#createRaceRunner()
     */
    public RaceRunner createRaceRunner() {
        return new ModelCompareLinearStarRaceRunner();
    }

}
