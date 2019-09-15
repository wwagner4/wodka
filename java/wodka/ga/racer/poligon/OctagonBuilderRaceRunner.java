package wodka.ga.racer.poligon;


/**
 * Implementation of a soda race.
 */

public class OctagonBuilderRaceRunner extends PoligonBuilderRaceRunner {

    protected int fitnesForNumber(int num) {
        switch (num) {
            case 2 :
                return 10;
            case 3 :
                return 30;
            case 4 :
                return 50;
            case 5 :
                return 70;
            case 6 :
                return 80;
            case 7 :
                return 100;
            case 8 :
                return 200;
            case 9 :
                return 100;
            case 10 :
                return 80;
            case 11 :
                return 70;
            case 12 :
                return 50;
            case 13 :
                return 10;
            default :
                return 0;
        }
    }
    protected int sideLength() {
        return 54;
    }

}