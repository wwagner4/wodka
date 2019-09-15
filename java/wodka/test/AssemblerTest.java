package wodka.test;

import junit.framework.*;
import junit.textui.*;
import wodka.ga.geno.lang.*;
import wodka.ga.geno.lang.assembler.AddMass;
import wodka.ga.geno.lang.assembler.AddMuscle;
import wodka.ga.geno.lang.assembler.AssemblerLanguage;

import java.util.*;
import wodka.ga.model.*;
import wodka.ga.soda.Interpreter;
import wodka.ga.soda.SodaraceProgram;
import wodka.util.SodaGlobalsRanges;

/**
 * Testcases for the wodka assambler language
 */

public class AssemblerTest extends TestCase {

    public AssemblerTest(String str) {
        super(str);
    }
    public void testAddMass() {
        SodaraceProgram pgm = createAssemblerProgram();
        pgm.add(new wodka.ga.geno.lang.assembler.AddMass(100, 120));
        pgm.add(new wodka.ga.geno.lang.assembler.AddMass(130, 140));
        pgm.add(new wodka.ga.geno.lang.assembler.AddMass(150, 160));
        Interpreter i = new wodka.ga.geno.lang.assembler.AssemblerInterpreter();
        Model s = i.eval(pgm);
        s.rewiseInvalidMassesAndMuscles();
        Iterator iter = s.getMasses().iterator();

        assertTrue(iter.hasNext());
        iter.next();
        assertTrue(!iter.hasNext());
    }
    private Program createAssemblerProgram(Language lang) {
        SodaGlobalsRanges ranges = SodaGlobalsRanges.createInstance();
        Program pgm = new wodka.ga.geno.lang.assembler.AssemblerProgram(ranges);
        pgm.setGenotypeDesc(lang);
        return pgm;
    }
    private SodaraceProgram createAssemblerProgram() {
        SodaGlobalsRanges ranges = SodaGlobalsRanges.createInstance();
        SodaraceProgram pgm = new wodka.ga.geno.lang.assembler.AssemblerProgram(ranges);
        pgm.setGenotypeDesc(new AssemblerLanguage());
        return pgm;
    }
    public void testAddMuscle() {
        SodaraceProgram pgm = createAssemblerProgram();
        pgm.add(new wodka.ga.geno.lang.assembler.AddMass(100, 120));
        pgm.add(new wodka.ga.geno.lang.assembler.AddMass(130, 140));
        pgm.add(new wodka.ga.geno.lang.assembler.AddMass(150, 160));
        pgm.add(new wodka.ga.geno.lang.assembler.AddMuscle(0, 1, 0.5, 0.5));
        pgm.add(new wodka.ga.geno.lang.assembler.AddMuscle(0.1, 0.2, 0.5, 0.5));
        pgm.add(new wodka.ga.geno.lang.assembler.AddMuscle(0.0, 0.2, 0.5, 0.5));
        Interpreter i = new wodka.ga.geno.lang.assembler.AssemblerInterpreter();
        Model s = i.eval(pgm);
        Iterator iter = s.getMasses().iterator();

        assertTrue(iter.hasNext());
        Mass m = (Mass) iter.next();
        assertEquals(100, m.getXPos());
        assertEquals(120, m.getYPos());

        assertTrue(iter.hasNext());
        m = (Mass) iter.next();
        assertEquals(150, m.getXPos());
        assertEquals(160, m.getYPos());

        assertTrue(!iter.hasNext());

        iter = s.getMuscles().iterator();

        assertTrue(iter.hasNext());
        Muscle sp = (Muscle) iter.next();
        assertEquals("m0", sp.getFromMass().getIdentifier());
        assertEquals("m2", sp.getToMass().getIdentifier());

        assertTrue(!iter.hasNext());

    }
    public void testRecombineCrossover() {
        Language lang =new AssemblerLanguage();
        Program a = createAssemblerProgram(lang);
        for (int i = 0; i < 100; i++) {
            AddMass am = new wodka.ga.geno.lang.assembler.AddMass(0, 0);
            am.setLanguage(lang);
            a.add(am);
        }
        Program b = createAssemblerProgram();
        for (int i = 0; i < 100; i++) {
            AddMuscle am = new wodka.ga.geno.lang.assembler.AddMuscle(0, 0, 0.0, 0.0);
            am.setLanguage(lang);
            b.add(am);
        }
        Program c = (Program) a.recombine(b, 0.01);
        Iterator cmds = c.commands();
        int addMassCounter = 0;
        int addMuscleCounter = 0;
        int illegalCounter = 0;
        while (cmds.hasNext()) {
            Command cmd = (Command) cmds.next();
            if (cmd instanceof wodka.ga.geno.lang.assembler.AddMass)
                addMassCounter++;
            else if (cmd instanceof wodka.ga.geno.lang.assembler.AddMuscle)
                addMuscleCounter++;
            else
                illegalCounter++;
        }
        assertTrue(addMassCounter > 5);
        assertTrue(addMuscleCounter > 5);
        assertEquals(0, illegalCounter);
    }
    public void testRecombineMutation() {
        Language lang = new AssemblerLanguage();
        Program a = this.createAssemblerProgram(lang);
        for (int i = 0; i < 20; i++) {
            AddMass am = new wodka.ga.geno.lang.assembler.AddMass(50, 50);
            am.setLanguage(lang);
            a.add(am);
        }
        Program b = this.createAssemblerProgram(lang);
        for (int i = 0; i < 100; i++) {
            AddMass am = new wodka.ga.geno.lang.assembler.AddMass(50, 50);
            am.setLanguage(lang);
            b.add(am);
        }
        Program c = (Program) a.recombine(b, 0.8);
        Iterator cmds = c.commands();
        int cmdMutationCounter = 0;
        int xMutationCounter = 0;
        int yMutationCounter = 0;
        while (cmds.hasNext()) {
            Command cmd = (Command) cmds.next();
            if (cmd instanceof wodka.ga.geno.lang.assembler.AddMuscle)
                cmdMutationCounter++;
            else {
                wodka.ga.geno.lang.assembler.AddMass amCmd = (wodka.ga.geno.lang.assembler.AddMass) cmd;
                if (amCmd.getPosX() != 50)
                    xMutationCounter++;
                if (amCmd.getPosY() != 50)
                    yMutationCounter++;
            }
        }
        assertTrue("Mutation counter is zero. Try again that migth happen but is no error.", cmdMutationCounter > 0);
        assertTrue(xMutationCounter > 0);
        assertTrue(yMutationCounter > 0);
    }
    public static void main(String[] args) {
        TestRunner.run(new TestSuite(wodka.test.AssemblerTest.class));
    }
}