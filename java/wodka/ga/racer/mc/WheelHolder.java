/*
 * SquareHolder.java
 *
 * Created on March 14, 2003, 11:08 PM
 */

package wodka.ga.racer.mc;

import wodka.ga.model.*;

/**
 *
 * @author  wolfi
 */
public class WheelHolder extends ModelHolder {

    public WheelHolder() {
        super();
    }

    public Model createModel() {
        Model m = new Model();
        Mass m0 = new Mass(m, 50, 30);
        m.addMass(m0);
        Mass m1 = new Mass(m, 20, 70);
        m.addMass(m1);
        Mass m2 = new Mass(m, 20, 150);
        m.addMass(m2);
        Mass m3 = new Mass(m, 50, 210);
        m.addMass(m3);
        Mass m4 = new Mass(m, 110, 230);
        m.addMass(m4);
        Mass m5 = new Mass(m, 180, 230);
        m.addMass(m5);
        Mass m6 = new Mass(m, 230, 180);
        m.addMass(m6);
        Mass m7 = new Mass(m, 230, 110);
        m.addMass(m7);
        Mass m8 = new Mass(m, 190, 40);
        m.addMass(m8);
        Mass m9 = new Mass(m, 130, 30);
        m.addMass(m9);
        Mass m10 = new Mass(m, 40, 90);
        m.addMass(m10);
        Mass m11 = new Mass(m, 40, 150);
        m.addMass(m11);
        Mass m12 = new Mass(m, 70, 190);
        m.addMass(m12);
        Mass m13 = new Mass(m, 110, 210);
        m.addMass(m13);
        Mass m14 = new Mass(m, 170, 210);
        m.addMass(m14);
        Mass m15 = new Mass(m, 200, 180);
        m.addMass(m15);
        Mass m16 = new Mass(m, 210, 110);
        m.addMass(m16);
        Mass m17 = new Mass(m, 190, 70);
        m.addMass(m17);
        Mass m18 = new Mass(m, 130, 60);
        m.addMass(m18);
        Mass m19 = new Mass(m, 70, 50);
        m.addMass(m19);
        m.addMuscle(new Muscle(m, m0, m1, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m1, m2, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m2, m3, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m3, m4, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m4, m5, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m5, m6, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m6, m7, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m7, m8, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m8, m9, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m9, m0, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m10, m11, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m11, m12, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m12, m13, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m13, m14, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m14, m15, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m15, m16, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m16, m17, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m17, m18, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m18, m19, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m19, m10, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m0, m10, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m1, m11, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m2, m12, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m3, m13, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m4, m14, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m5, m15, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m6, m16, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m7, m17, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m8, m18, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m9, m19, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m0, m13, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m1, m14, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m2, m15, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m3, m16, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m4, m17, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m5, m18, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m6, m19, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m7, m10, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m8, m11, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m9, m12, 0.0, 0.0));
        return m;
    }

}
