package wodka.ga.geno.lang.turtle;

import wodka.ga.model.*;
import java.util.*;
import java.io.*;

/**
 * Turn command. Turns the model building turtle a certain angle.
 */

public class PenUp extends AbstractTurtleCommand {

    private static final long serialVersionUID = 1L;

    public PenUp() {
        super();
    }

    public String toShortDescription() {
        return "(pu)";
    }

    public wodka.ga.geno.lang.Command mutateCommand(Random ran) {
        switch (ran.nextInt(3)) {
        case 0:
            return getTurtleLanguage().createForwardCommand();
        case 1:
            return getTurtleLanguage().createTurnCommandWithRandomParams();
        case 2:
            return getTurtleLanguage().createPenDownCommand();
        default:
            throw new Error("Invalid random");
        }
    }

    public int parameterCount() {
        return 0;
    }

    public void mutateParameter(int paramInd, Random ran) {
        throw new Error("Should never be called.");
    }

    public wodka.ga.geno.lang.Command createClone() {
        PenUp cmd = new PenUp();
        cmd.setLanguage(getTurtleLanguage());
        return cmd;
    }

    public void eval(Model model, TurtleInterpreter interp) {
        interp.setIsUp(true);
    }

    public void toStream(DataOutputStream outStream) throws IOException {
        // Nothing to do here
    }

    public void fromStream(DataInputStream inStream, int version)
            throws IOException {
        // Nothing to do here
    }

    public int getVersion() {
        return 0;
    }
}