package wodka.ga.racer.poligon;

import wodka.ga.racer.RaceRunner;

/**
 * Implementation of a soda race.
 */

public class OctagonBuilderRacer extends AbstractPoligonBuilderRacer {

    private static final long serialVersionUID = 1L;

    protected int fitnesForNumber(int num) {
        switch (num) {
        case 2:
            return 10;
        case 3:
            return 30;
        case 4:
            return 50;
        case 5:
            return 70;
        case 6:
            return 80;
        case 7:
            return 100;
        case 8:
            return 200;
        case 9:
            return 100;
        case 10:
            return 80;
        case 11:
            return 70;
        case 12:
            return 50;
        case 13:
            return 10;
        default:
            return 0;
        }
    }

    public String getLabel() {
        return "Octagonbuilder";
    }

    protected void writeInfo(StringBuffer b) {
        b.append("Builds an Octagon.");
    }

    protected int sideLength() {
        return 54;
    }

    /**
     * 
     * @see wodka.ga.racer.poligon.AbstractPoligonBuilderRacer#createRaceRunner()
     */
    public RaceRunner createRaceRunner() {
        return new OctagonBuilderRaceRunner();
    }

}