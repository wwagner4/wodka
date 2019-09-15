package wodka.ga.geno.lang.assembler;

import java.util.Iterator;

import wodka.ga.model.Model;
import wodka.ga.soda.AbstractInterpreter;
import wodka.ga.soda.SodaraceProgram;

/**
 * An interpreter for programs that builds a sodahub model.
 */

public class AssemblerInterpreter extends AbstractInterpreter {

    public AssemblerInterpreter() {
        super();
    }
    
    /**
     * Converts a Program to a Model.
     */
    public Model eval(SodaraceProgram pgm) {
        Model model = evalBasic(pgm);
        model.rewiseInvalidMassesAndMuscles();
        model.calculateRestlength();
        return model;
    }
    public Model evalNoRewise(SodaraceProgram pgm) {
        Model model = evalBasic(pgm);
        model.calculateRestlength();
        return model;
    }
    
    private Model evalBasic(SodaraceProgram pgm) {
        Model model = new Model();
        model.setEnvFriction(pgm.getGlobals().getFriction());
        model.setEnvGravity(pgm.getGlobals().getGravity());
        model.setEnvSpringyness(pgm.getGlobals().getSpringyness());
        model.setWaveAplitude(pgm.getGlobals().getAmplitude());
        model.setWavePhase(pgm.getGlobals().getPhase());
        model.setWaveSpeed(pgm.getGlobals().getSpeed());
        Iterator iter = pgm.commands();
        while (iter.hasNext()) {
            AssemblerCommand aCmd = (AssemblerCommand) iter.next();
            if (aCmd.isAddMass()) {
                aCmd.eval(model);
            }
        }
        iter = pgm.commands();
        while (iter.hasNext()) {
            AssemblerCommand aCmd = (AssemblerCommand) iter.next();
            if (!aCmd.isAddMass()) {
                aCmd.eval(model);
            }
        }
        return model;
    }
}