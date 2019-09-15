package wodka.test;

import junit.framework.*;
import junit.textui.*;
import java.util.*;
import wodka.ga.model.*;

public class ModelTest extends TestCase {

    public ModelTest(String str) {
        super(str);
    }
	public void testRewiseMultipleMuscles() {
		Model m = new Model();
		Mass m0 = new Mass(m, 10, 10);
        m.addMass(m0);
		Mass m1 = new Mass(m, 12, 12);
        m.addMass(m1);
		m.addMuscle(new Muscle(m, m0, m1, 0.0, 0.0));
		m.addMuscle(new Muscle(m, m0, m1, 0.0, 0.0));
        
		m.rewiseInvalidMassesAndMuscles();

		Iterator si = m.getMuscles().iterator();
		assertTrue(si.hasNext());
		Muscle musc = (Muscle) si.next();
		assertEquals("m0", musc.getFromMass().getIdentifier());
		assertEquals("m1", musc.getToMass().getIdentifier());

		assertTrue(!si.hasNext());
	}
    public void testRewiseLoopMuscles() {
        Model m = new Model();
        Mass m0 = new Mass(m, 10, 10);
        m.addMass(m0);
        Mass m1 = new Mass(m, 12, 12);
        m.addMass(m1);
        Mass m2 = new Mass(m, 15, 16);
        m.addMass(m2);
        m.addMuscle(new Muscle(m, m0, m2, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m2, m2, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m1, m1, 0.0, 0.0));
        
        m.rewiseInvalidMassesAndMuscles();

        Iterator si = m.getMuscles().iterator();
        assertTrue(si.hasNext());
        Muscle musc = (Muscle) si.next();
        assertEquals("m0", musc.getFromMass().getIdentifier());
        assertEquals("m2", musc.getToMass().getIdentifier());

        assertTrue(!si.hasNext());
    }
    public void testRewiseOverlappingMasses() {
        Model m = new Model();
        Mass m0 = new Mass(m, 0, 0);
        m.addMass(m0);
        Mass m1 = new Mass(m, 1, 0);
        m.addMass(m1);
        Mass m2 = new Mass(m, 0, 1);
        m.addMass(m2);
        Mass m3 = new Mass(m, 0, 0);
        m.addMass(m3);
        m.addMuscle(new Muscle(m, m0, m1, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m1, m2, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m2, m3, 0.0, 0.0));
        m.rewiseInvalidMassesAndMuscles();
        Iterator i = m.getMasses().iterator();
        assertTrue(i.hasNext());
        assertMass(0, 0, (Mass) i.next());
        assertTrue(i.hasNext());
        assertMass(1, 0, (Mass) i.next());
        assertTrue(i.hasNext());
        assertMass(0, 1, (Mass) i.next());
        assertTrue(!i.hasNext());
        
        i = m.getMuscles().iterator();
        assertTrue(i.hasNext());
        assertMuscle("m0", "m1", (Muscle) i.next());
        assertTrue(i.hasNext());
        assertMuscle("m1", "m2", (Muscle) i.next());
        assertTrue(i.hasNext());
        assertMuscle("m2", "m0", (Muscle) i.next());
        assertTrue(!i.hasNext());
    }
    private void assertMass(int x, int y, Mass m) {
        assertEquals(x, m.getXPos());
        assertEquals(y, m.getYPos());
    }
    private void assertMuscle(String fromId, String toId, Muscle s) {
        assertEquals(fromId, s.getFromMass().getIdentifier());
        assertEquals(toId, s.getToMass().getIdentifier());
    }

    public static void main(String[] args) {
        TestRunner.run(new TestSuite(wodka.test.ModelTest.class));
    }
}