package wodka.test;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import wodka.ga.geno.lang.Program;
import wodka.ga.geno.lang.assembler.AddMass;
import wodka.ga.geno.lang.assembler.AddMuscle;
import wodka.ga.geno.lang.assembler.AssemblerLanguage;
import wodka.ga.geno.lang.assembler.AssemblerProgram;
import wodka.ga.geno.lang.turtle.TurtleProgram;
import wodka.ga.selection.FavourFittest;
import wodka.ga.soda.SodaraceProgram;
import wodka.util.SodaGlobalsRanges;
import wodka.util.StreamPersistable;
import wodka.util.StreamPersistor;

/**
 * Testcases for the persistance.
 */

public class PersistanceTest extends TestCase {

    private Random ran = new Random();

    public PersistanceTest(String str) {
        super(str);
    }

    public void testStreamAssemblerAddMass() throws Exception {
        AddMass before = new AddMass(10, 11);
        AddMass after = (AddMass) writeAndReadStream(before);
        assertEquals(10, after.getPosX());
        assertEquals(11, after.getPosY());
    }

    public void testStreamAssemblerAddMuscle() throws Exception {
        AddMuscle before = new AddMuscle(0.1, 0.2, 0.5, 0.5);
        AddMuscle after = (AddMuscle) writeAndReadStream(before);
        assertEquals(0.1, after.getAuxFrMassInd(), 0.001);
        assertEquals(0.2, after.getAuxToMassInd(), 0.001);
    }

    public void testStreamConfigurable() throws Exception {
        FavourFittest before = new FavourFittest();
        before.setPopSize(222);
        before.setReproductionRate(0.123);
        FavourFittest after = (FavourFittest) writeAndReadStream(before);
        assertEquals(500, after.getPopSize());
        assertEquals(0.3, after.getReproductionRate(), 0.001);
    }

    public void testStreamAssemblerProgram() throws Exception {
        AssemblerProgram before = createAssemblerProgram();
        AddMass cmd1Before = new AddMass(1, 2);
        before.add(cmd1Before);
        assertEquals(1, cmd1Before.getPosX());
        assertEquals(2, cmd1Before.getPosY());
        before.add(new AddMuscle(0.3, 0.4, 0.5, 0.5));
        AssemblerProgram after = (AssemblerProgram) writeAndReadStream(before);
        Iterator i = after.commands();
        assertTrue(i.hasNext());
        Object o = i.next();
        assertEquals(AddMass.class, o.getClass());
        AddMass cmd1 = (AddMass) o;
        assertEquals(1, cmd1.getPosX());
        assertEquals(2, cmd1.getPosY());
        assertTrue(i.hasNext());
        AddMuscle cmd2 = (AddMuscle) i.next();
        assertEquals(0.3, cmd2.getAuxFrMassInd(), 0.001);
        assertEquals(0.4, cmd2.getAuxToMassInd(), 0.001);
        assertTrue(!i.hasNext());
    }

    public void testStreamAssemblerProgramGlobals() throws Exception {
        AssemblerProgram before = createAssemblerProgram();
        double a = before.getGlobals().getAmplitude();
        double b = before.getGlobals().getPhase();
        AssemblerProgram after = (AssemblerProgram) writeAndReadStream(before);
        assertEquals(a, after.getGlobals().getAmplitude(), 0.0001);
        assertEquals(b, after.getGlobals().getPhase(), 0.0001);
    }

    public void testStreamTurtleProgram() throws Exception {
        wodka.ga.geno.lang.Program before = createRandomTurteProgramWithGridwidth10();
        Iterator i = before.commands();
        int count = 0;
        while (i.hasNext()) {
            i.next();
            count++;
        }
        assertTrue(
                "Turtle program should have more than 10 commands before serialize/deserialize",
                count > 10);
        TurtleProgram after = (TurtleProgram) writeAndReadStream(before);
        i = after.commands();
        count = 0;
        while (i.hasNext()) {
            i.next();
            count++;
        }
        assertTrue(
                "Turtle program should have more than 10 commands after serialize/deserialize",
                count > 10);
    }

    public void testStreamIndividual() throws Exception {
        wodka.ga.Individual m1 = new wodka.ga.Individual();
        m1.setGenotype(createRandomTurteProgramWithGridwidth10());
        m1.setFitness(111);
        wodka.ga.Individual m2 = (wodka.ga.Individual) writeAndReadStream(m1);
        assertEquals(111, m2.getFitness());
        SodaraceProgram pgm = (SodaraceProgram) m2.getGeno();
        wodka.ga.model.Model m = pgm.eval();
        assertTrue("Model should have masses after serialize/deserialize", m
                .getMasses().iterator().hasNext());
        assertTrue("Model should have muscles after serialize/deserialize", m
                .getMuscles().iterator().hasNext());
    }

    private Program createRandomTurteProgramWithGridwidth10() {
        SodaGlobalsRanges ranges = SodaGlobalsRanges.createInstance();
        Program pgm = new wodka.ga.geno.lang.turtle.TurtleProgram(10, ranges);
        pgm.add(new wodka.ga.geno.lang.turtle.PenDown());
        for (int i = 0; i < 50; i++) {
            if (ran.nextBoolean()) {
                pgm.add(new wodka.ga.geno.lang.turtle.Forward(
                        ran.nextInt(100) - 50, ran.nextDouble(), ran
                                .nextDouble()));
            } else {
                pgm.add(new wodka.ga.geno.lang.turtle.Turn(
                        ran.nextInt(180) - 90));
            }
        }
        return pgm;
    }

    public void testFillCollectionFromStream() throws Exception {
        DummyPersistable dummy = new DummyPersistable();
        dummy.add(new AddMass(10, 11));
        dummy.add(new AddMass(5, 6));
        Collection c = dummy.getCollection();
        Iterator i = c.iterator();
        Object o01 = i.next();
        Object o02 = i.next();
        save(dummy, this.testData());
        InputStream in = null;
        try {
            in = new FileInputStream(this.testData());
            wodka.util.StreamPersistor.fillFromStream(dummy,
                    new DataInputStream(in));
        } finally {
            if (in != null)
                in.close();
        }
        assertEquals(dummy.getCollection(), c);
        assertEquals(2, dummy.getCollection().size());
        i = c.iterator();
        Object o11 = i.next();
        Object o12 = i.next();
        assertTrue(o01 != o11);
        assertTrue(o02 != o12);
    }

    private File testData() {
        File home = new File(System.getProperty("user.home"));
        File test = new File(home, "test");
        if (!test.exists())
            test.mkdirs();
        return new File(test, "wodka.data");
    }

    public void testString() throws IOException {
        DummyPersistable dummy = new DummyPersistable();
        dummy.setNam("hallo");
        save(dummy, testData());
        DummyPersistable dummy1 = (DummyPersistable) load(testData());
        assertEquals("hallo", dummy1.getNam());
    }

    private StreamPersistable writeAndReadStream(StreamPersistable a)
            throws java.io.IOException {
        save(a, testData());
        return load(testData());
    }

    private void save(StreamPersistable p, File file) throws IOException {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            StreamPersistor.toStream(p, new DataOutputStream(out));
        } finally {
            if (out != null)
                out.close();
        }
    }

    private StreamPersistable load(File file) throws IOException {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            BufferedInputStream bin = null;
            try {
                bin = new BufferedInputStream(in);
                DataInputStream din = null;
                try {
                    din = new DataInputStream(bin);
                    StreamPersistable p = StreamPersistor.fromStream(din);
                    return p;
                } finally {
                    if (din != null)
                        in.close();
                }
            } finally {
                if (bin != null)
                    in.close();
            }
        } finally {
            if (in != null)
                in.close();
        }
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    private static TestSuite suite() {
        return new TestSuite(wodka.test.PersistanceTest.class);
    }

    private AssemblerProgram createAssemblerProgram() {
        SodaGlobalsRanges ranges = SodaGlobalsRanges.createInstance();
        AssemblerProgram pgm = new wodka.ga.geno.lang.assembler.AssemblerProgram(
                ranges);
        pgm.setLanguage(new AssemblerLanguage());
        return pgm;
    }

}