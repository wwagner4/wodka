/*
 * SquareHolder.java
 * 
 * Created on March 14, 2003, 11:08 PM
 */

package wodka.ga.racer.mc;

import wodka.ga.model.*;

/**
 * Defines a star like model. For testing purpose.
 */
public class StarHolder extends ModelHolder {

    public StarHolder() {
        super();
    }

    public Model createModel() {
        Model m = new Model();
        Mass m0 = new Mass(m, 110, 80);
        m.addMass(m0);
        Mass m1 = new Mass(m, 140, 80);
        m.addMass(m1);
        Mass m2 = new Mass(m, 160, 100);
        m.addMass(m2);
        Mass m3 = new Mass(m, 160, 130);
        m.addMass(m3);
        Mass m4 = new Mass(m, 140, 150);
        m.addMass(m4);
        Mass m5 = new Mass(m, 110, 150);
        m.addMass(m5);
        Mass m6 = new Mass(m, 90, 130);
        m.addMass(m6);
        Mass m7 = new Mass(m, 90, 100);
        m.addMass(m7);
        Mass m8 = new Mass(m, 130, 20);
        m.addMass(m8);
        Mass m9 = new Mass(m, 190, 40);
        m.addMass(m9);
        Mass m10 = new Mass(m, 210, 120);
        m.addMass(m10);
        Mass m11 = new Mass(m, 190, 170);
        m.addMass(m11);
        Mass m12 = new Mass(m, 120, 190);
        m.addMass(m12);
        Mass m13 = new Mass(m, 60, 170);
        m.addMass(m13);
        Mass m14 = new Mass(m, 30, 110);
        m.addMass(m14);
        Mass m15 = new Mass(m, 50, 30);
        m.addMass(m15);
        Mass m16 = new Mass(m, 180, 0);
        m.addMass(m16);
        Mass m17 = new Mass(m, 250, 50);
        m.addMass(m17);
        Mass m18 = new Mass(m, 250, 170);
        m.addMass(m18);
        Mass m19 = new Mass(m, 190, 230);
        m.addMass(m19);
        Mass m20 = new Mass(m, 70, 220);
        m.addMass(m20);
        Mass m21 = new Mass(m, 10, 160);
        m.addMass(m21);
        Mass m22 = new Mass(m, 10, 40);
        m.addMass(m22);
        Mass m23 = new Mass(m, 90, 0);
        m.addMass(m23);
        m.addMuscle(new Muscle(m, m0, m8, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m8, m1, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m1, m9, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m9, m2, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m2, m10, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m10, m3, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m3, m11, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m11, m4, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m4, m12, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m12, m5, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m5, m13, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m13, m6, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m6, m14, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m14, m7, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m7, m15, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m15, m0, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m8, m16, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m16, m9, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m9, m17, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m17, m10, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m10, m18, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m18, m11, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m11, m19, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m19, m12, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m12, m20, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m20, m13, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m13, m21, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m21, m14, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m14, m22, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m22, m15, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m15, m23, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m23, m8, 0.0, 0.0));
        return m;
    }

}
