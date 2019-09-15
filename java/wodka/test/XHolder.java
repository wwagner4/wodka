/*
 * SquareHolder.java
 * 
 * Created on March 14, 2003, 11:08 PM
 */

package wodka.test;

import wodka.ga.model.*;

/**
 * @author wolfi
 */
public class XHolder extends wodka.ga.racer.mc.ModelHolder {

    public XHolder() {
        super();
    }

    public Model createModel() {
        Model m = new Model();
        Mass m0 = new Mass(m, transform(11), transform(6));
        m.addMass(m0);
        Mass m1 = new Mass(m, transform(23), transform(9));
        m.addMass(m1);
        Mass m2 = new Mass(m, transform(27), transform(19));
        m.addMass(m2);
        Mass m3 = new Mass(m, transform(18), transform(27));
        m.addMass(m3);
        Mass m4 = new Mass(m, transform(6), transform(18));
        m.addMass(m4);
        m.addMuscle(new Muscle(m, m0, m1, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m1, m2, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m2, m3, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m3, m4, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m4, m0, 0.0, 0.0));
        return m;
    }
    private int transform(int val) {
        int mul = Model.WIDTH / 32;
        return val * mul;
    }

}
