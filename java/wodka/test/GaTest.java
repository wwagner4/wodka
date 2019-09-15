package wodka.test;

import java.util.Iterator;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import wodka.ga.GeneticAlgorithm;
import wodka.ga.Individual;
import wodka.ga.soda.SodaraceProgram;

public class GaTest extends TestCase {

    public GaTest(String str) {
        super(str);
    }

    public void testInit() {
        GeneticAlgorithm ga = new GeneticAlgorithm();
        Iterator indiIter = ga.getPopulation().getIndividuals().iterator();
        while (indiIter.hasNext()) {
            Individual m = (Individual) indiIter.next();
            SodaraceProgram pgm = (SodaraceProgram) m.getGeno();
            
            assertNotNull(pgm.eval());
            assertTrue(m.getInfoString().length() > 0);
        }
    }

    public static void main(String[] args) {
        TestRunner.run(new TestSuite(wodka.test.GaTest.class));
    }
}