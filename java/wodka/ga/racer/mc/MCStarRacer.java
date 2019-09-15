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
public class MCStarRacer extends AbstractMCRacer {

    private static final long serialVersionUID = 1L;

    public MCStarRacer() {
        super();
    }

    public String getLabel() {
        return "MC-Star";
    }

    protected void writeDescription(java.io.PrintWriter pw) {
        pw
                .print("Tries to build a star like structure by means of model comparison.");
        pw.println();
    }

    protected GridFunction getGridFunction() {
        return new Log();
    }

    protected ModelHolder getModelHolder() {
        return new StarHolder();
    }

    /**
     * 
     * @see wodka.ga.racer.AbstractRacer#createRaceRunner()
     */
    public RaceRunner createRaceRunner() {
        return new ModelCompareStarRaceRunner();
    }

}
