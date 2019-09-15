/*
 * Created on 11.11.2003
 *
 */
package wodka.ga.racer.poligon;

/**
 * 
 */
public class PentagonBuilderRaceRunner extends PoligonBuilderRaceRunner {

    protected int fitnesForNumber(int num) {
        switch (num) {
            case 2 :
                return 50;
            case 3 :
                return 70;
            case 4 :
                return 100;
            case 5 :
                return 200;
            case 6 :
                return 100;
            case 7 :
                return 50;
            case 8 :
                return 30;
            case 9 :
                return 20;
            case 10 :
                return 10;
            default :
                return 0;
        }
    }

    protected int sideLength() {
        return 83;
    }

}
