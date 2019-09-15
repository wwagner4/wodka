/*
 * Created on Feb 28, 2004
 */
package wodka.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import wodka.ga.Genotype;
import wodka.ga.geno.lang.turtle.TurtleLanguage;
import wodka.ga.model.Mass;
import wodka.ga.model.Model;
import wodka.ga.model.Muscle;
import wodka.ga.soda.SodaraceProgram;
import wodka.util.StreamPersistor;

/**
 * Tests the rewising of invalid Masses and Muscles from Models.
 *  
 */
public class ModelRewiseTestRunner {

    private static final int SAMPLE_SIZE = 1000;
    
    public static ModelRewiseTestRunner current = null;

    private ModelRewiseTestRunner() {
        super();
    }
    
    public static ModelRewiseTestRunner current() {
        if (current == null) current =new ModelRewiseTestRunner();
        return current;
    }

    public void run() {
        try {
            //        viewInStdout();
            rewiseInTurtleLanguage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void rewiseInTurtleLanguage() throws IOException {
        long id = System.currentTimeMillis();
        TurtleLanguage l = new TurtleLanguage();
        int invalidCount = 0;
        for (int i = 0; i < SAMPLE_SIZE; i++) {
            SodaraceProgram gt = (SodaraceProgram) l.createRandomGenotype();
            Model model = gt.eval();
            if (hasInvalidMuscles(model)) {
                invalidCount++;
                info("Invalid model. \n" + model);
                saveGeno(gt, id, i);
            }
        }
        info(invalidCount + " of " + SAMPLE_SIZE + " models where invalid.");
    }

    private void saveGeno(Genotype gt, long id, int i) throws IOException {
        File out = getOutputFile(id, i);
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(out);
            StreamPersistor.toStream(gt, fout);
            fout.close();
        } finally {
            if (fout != null) fout.close();
        }
    }

    private File getOutputFile(long id, int i) {
        File prjDir = getOutputDir();
        File out = new File(prjDir, "gt_" + id + "_" + i);
        return out;
    }

    public File getOutputDir() {
        File tmpDir = new File("c:/tmp");
        if (!tmpDir.exists()) return null;
        File prjDir = new File(tmpDir, "InvalidTurtle");
        if (!prjDir.exists())
            prjDir.mkdirs();
        return prjDir;
    }

    private boolean hasInvalidMuscles(Model model) {
        Collection massIds = new ArrayList();
        for (int i = 0; i < model.getMassCount(); i++) {
            Mass mass = model.getMass(i);
            massIds.add(mass.getIdentifier());
        }
        for (int i = 0; i < model.getMuscleCount(); i++) {
            Muscle musc = model.getMuscle(i);
            if (!massIds.contains(musc.getFromMass().getIdentifier()))
                return true;
            if (!massIds.contains(musc.getToMass().getIdentifier()))
                return true;
        }
        return false;
    }

    private void info(String msg) {
        System.out.println(this.getClass().getName() + ">>" + msg);
    }

    public static void main(String[] args) {
        ModelRewiseTestRunner tester = ModelRewiseTestRunner.current();
        tester.run();
    }

}
