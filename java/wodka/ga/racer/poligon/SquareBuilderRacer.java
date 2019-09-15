package wodka.ga.racer.poligon;

import wodka.ga.racer.RaceRunner;

/**
 * Implementation of a soda race.
 */

public class SquareBuilderRacer extends AbstractPoligonBuilderRacer {

    private static final long serialVersionUID = 1L;

    public String getLabel() {
        return "Squarebuilder";
    }

    protected void writeInfo(StringBuffer b) {
        b.append("Builds a Square.");
    }

    public RaceRunner createRaceRunner() {
        return new SquareBuilderRaceRunner();
    }

}