package wodka.ga.racer.poligon;

import wodka.ga.racer.AbstractRacer;


/**
 * Implementation of a soda race.
 */

public abstract class AbstractPoligonBuilderRacer extends AbstractRacer {

    abstract protected void writeInfo(StringBuffer buffer);
    
    public AbstractPoligonBuilderRacer() {
        super();
    }

    public String getInfo() {
        StringBuffer buffer = new StringBuffer();
        writeInfo(buffer);
        buffer.append("\nThe following rules should build a poligon.");
        buffer.append("\n-The masses should be placed on a centered circle with the radius of 71px.");
        buffer.append("\n-The number of masses and muscles should have the number of edges of the poligon.");
        buffer.append(
            "\n-The mean lengths of the muscles and the minimum distance between masses should have the appropriate ");
        buffer.append("length (d=142*sin(180/n) according to what poligon has to be build.");
        return buffer.toString();
    }

}