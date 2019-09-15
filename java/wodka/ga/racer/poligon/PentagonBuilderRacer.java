package wodka.ga.racer.poligon;

import wodka.ga.racer.RaceRunner;

/**
 * Implementation of a soda race.
 */

public class PentagonBuilderRacer extends AbstractPoligonBuilderRacer {

    private static final long serialVersionUID = 1L;

    public String getLabel() {
        return "Pentagonbuilder";
    }

    protected void writeInfo(StringBuffer b) {
        b.append("Builds a Pentagon.");
    }

    public RaceRunner createRaceRunner() {
        return new PentagonBuilderRaceRunner();
    }

}