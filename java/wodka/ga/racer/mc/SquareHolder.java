/*
 * SquareHolder.java
 *
 * Created on March 14, 2003, 11:08 PM
 */

package wodka.ga.racer.mc;

import wodka.ga.model.*;

/**
 * 
 * @author wolfi
 */
public class SquareHolder extends ModelHolder {

    public SquareHolder() {
        super();
    }

    public Model createModel() {
        Model m = new Model();
        Mass m0 = new Mass(m, 78, 78);
        Mass m1 = new Mass(m, 178, 78);
        Mass m2 = new Mass(m, 178, 178);
        Mass m3 = new Mass(m, 78, 178);
        m.addMass(m0);
        m.addMass(m1);
        m.addMass(m2);
        m.addMass(m3);
        m.addMuscle(new Muscle(m, m0, m1, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m1, m2, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m2, m3, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m3, m0, 0.0, 0.0));
        return m;
    }

}