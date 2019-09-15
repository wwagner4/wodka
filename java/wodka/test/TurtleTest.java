package wodka.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import wodka.ga.Genotype;
import wodka.ga.GenotypeDesc;
import wodka.ga.geno.lang.turtle.TurtleLanguage;
import wodka.ga.geno.lang.turtle.TurtleProgram;
import wodka.ga.model.Mass;
import wodka.ga.model.Model;
import wodka.ga.model.Muscle;
import wodka.ga.soda.SodaraceProgram;
import wodka.util.SodaGlobalsRanges;
import wodka.util.StreamPersistor;

/**
 * Testcases for the turtle language
 */

public class TurtleTest extends TestCase {

    public TurtleTest(String s) {
        super(s);
    }

    public void test00() {
        SodaraceProgram p = createTurtleProgram();
        p.add(new wodka.ga.geno.lang.turtle.PenDown());
        p.add(new wodka.ga.geno.lang.turtle.Forward(10, 0.0, 0.0));
        p.add(new wodka.ga.geno.lang.turtle.Turn(90));
        p.add(new wodka.ga.geno.lang.turtle.Forward(10, 0.0, 0.0));
        Model s = p.eval();
        Iterator i = s.getMasses().iterator();
        assertTrue(i.hasNext());
        assertMass(130, 130, (Mass) i.next());
        assertTrue(i.hasNext());
        assertMass(130, 120, (Mass) i.next());
        assertTrue(i.hasNext());
        assertMass(120, 120, (Mass) i.next());
        assertTrue(!i.hasNext());
        i = s.getMuscles().iterator();
        assertTrue(i.hasNext());
        assertMuscle("m0", "m1", (Muscle) i.next());
        assertTrue(i.hasNext());
        assertMuscle("m1", "m2", (Muscle) i.next());
        assertTrue(!i.hasNext());
    }

    private SodaraceProgram createTurtleProgram() {
        SodaGlobalsRanges ranges = SodaGlobalsRanges.createInstance();
        return new TurtleProgram(1, ranges);
    }

    public void test01() {
        SodaraceProgram p = createTurtleProgram();
        p.add(new wodka.ga.geno.lang.turtle.PenDown());
        p.add(new wodka.ga.geno.lang.turtle.Forward(10, 0.5, 0.5));
        p.add(new wodka.ga.geno.lang.turtle.Turn(-90));
        p.add(new wodka.ga.geno.lang.turtle.Forward(20, 0.5, 0.5));
        Model s = p.eval();
        Iterator i = s.getMasses().iterator();
        assertTrue(i.hasNext());
        assertMass(130, 130, (Mass) i.next());
        assertTrue(i.hasNext());
        assertMass(130, 120, (Mass) i.next());
        assertTrue(i.hasNext());
        assertMass(150, 120, (Mass) i.next());
        assertTrue(!i.hasNext());
        i = s.getMuscles().iterator();
        assertTrue(i.hasNext());
        assertMuscle("m0", "m1", (Muscle) i.next());
        assertTrue(i.hasNext());
        assertMuscle("m1", "m2", (Muscle) i.next());
        assertTrue(!i.hasNext());
    }

    public void test03() {
        SodaraceProgram p = createTurtleProgram();
        p.add(new wodka.ga.geno.lang.turtle.PenDown());
        p.add(new wodka.ga.geno.lang.turtle.Forward(10, 0.5, 0.5));
        p.add(new wodka.ga.geno.lang.turtle.Turn(90));
        p.add(new wodka.ga.geno.lang.turtle.Forward(-30, 0.5, 0.5));
        Model s = p.eval();
        Iterator i = s.getMasses().iterator();
        assertTrue(i.hasNext());
        assertMass(130, 130, (Mass) i.next());
        assertTrue(i.hasNext());
        assertMass(130, 120, (Mass) i.next());
        assertTrue(i.hasNext());
        assertMass(160, 120, (Mass) i.next());
        assertTrue(!i.hasNext());
        i = s.getMuscles().iterator();
        assertTrue(i.hasNext());
        assertMuscle("m0", "m1", (Muscle) i.next());
        assertTrue(i.hasNext());
        assertMuscle("m1", "m2", (Muscle) i.next());
        assertTrue(!i.hasNext());
    }

    public void test04() {
        SodaraceProgram p = createTurtleProgram();
        p.add(new wodka.ga.geno.lang.turtle.PenDown());
        p.add(new wodka.ga.geno.lang.turtle.Turn(45));
        p.add(new wodka.ga.geno.lang.turtle.Forward(141, 0.5, 0.5));
        Model s = p.eval();
        Iterator i = s.getMasses().iterator();
        assertTrue(i.hasNext());
        assertMass(130, 130, (Mass) i.next());
        assertTrue(i.hasNext());
        assertMass(30, 30, (Mass) i.next());
        assertTrue(!i.hasNext());
        i = s.getMuscles().iterator();
        assertTrue(i.hasNext());
        assertMuscle("m0", "m1", (Muscle) i.next());
        assertTrue(!i.hasNext());
    }

    private void assertMass(int x, int y, Mass m) {
        assertEquals(x, m.getXPos());
        assertEquals(y, m.getYPos());
    }

    private void assertMuscle(String fromId, String toId, Muscle musc) {
        assertEquals(fromId, musc.getFromMass().getIdentifier());
        assertEquals(toId, musc.getToMass().getIdentifier());
    }

    public void testCreateRandomPrograms() {
        GenotypeDesc desc = new TurtleLanguage();
        for (int i = 0; i < 100; i++) {
            SodaraceProgram gt = (SodaraceProgram) desc.createRandomGenotype();
            gt.eval();
        }
    }

    public void testInvalidMuscles() throws java.io.IOException {
        ModelRewiseTestRunner runner = ModelRewiseTestRunner.current();
        File outDir = runner.getOutputDir();
        if (outDir == null)
            throw new Error("Out dir of ModelRewiseTestRunner not found.");
        File[] files = outDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            FileInputStream fin = null;
            try {
                fin = new FileInputStream(files[i]);
                testInvalidMuscles(fin);
            } finally {
                if (fin != null)
                    fin.close();
            }

        }
    }

    private void testInvalidMuscles(InputStream in) throws IOException {
        SodaraceProgram gt = (SodaraceProgram) StreamPersistor.fromStream(in);
        Model m = gt.eval();
        assertNoInvalidMuscles(m);
    }

    private void assertNoInvalidMuscles(Model model) {
        Collection massIds = new ArrayList();
        for (int i = 0; i < model.getMassCount(); i++) {
            Mass mass = model.getMass(i);
            massIds.add(mass.getIdentifier());
        }
        for (int i = 0; i < model.getMuscleCount(); i++) {
            Muscle musc = model.getMuscle(i);
            if (!massIds.contains(musc.getFromMass().getIdentifier()))
                fail("Invalid muscle " + i + " in:\n" + model);
            if (!massIds.contains(musc.getToMass().getIdentifier()))
                fail("Invalid muscle " + i + " in:\n" + model);
        }
    }

    public void testRecombine() {
        TurtleLanguage l = new TurtleLanguage();
        Genotype gt1 = l.createRandomGenotype();
        Genotype gt2 = l.createRandomGenotype();
        Genotype gt3 = gt1.recombine(gt2, 0.001);
        assertNotNull(gt3);
    }

    public static void main(String[] args) {
        TestRunner.run(new TestSuite(wodka.test.TurtleTest.class));
    }
}