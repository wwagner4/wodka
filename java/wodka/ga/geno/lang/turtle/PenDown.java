package wodka.ga.geno.lang.turtle;

import wodka.ga.model.*;
import java.util.*;
import java.io.*;

/**
 * Turn command. Turns the model building turtle a certain
 * angle.
 */

public class PenDown extends AbstractTurtleCommand {

    private static final long serialVersionUID = 1L;
    public PenDown() {
        super();
    }
    public String toShortDescription() {
        return "(pd)";
    }
    public wodka.ga.geno.lang.Command mutateCommand(Random ran) {
        switch (ran.nextInt(3)) {
            case 0 :
                return getTurtleLanguage().createForwardCommand();
            case 1 :
                return getTurtleLanguage().createTurnCommandWithRandomParams();
            case 2 :
                return getTurtleLanguage().createPenUpCommand();
            default :
                throw new Error("Invalid random");
        }
    }
    public int parameterCount() {
        return 0;
    }
    public void mutateParameter(int index, Random ran) {
        throw new Error("Should never be called.");
    }
    public wodka.ga.geno.lang.Command createClone() {
        return getTurtleLanguage().createPenDownCommand();
    }
    public void eval(Model model, TurtleInterpreter interp) {
        if (interp.isUp()) {
            interp.setIsUp(false);
            interp.setUpBefore(true);
        }
    }
    public void toStream(DataOutputStream outStream) throws IOException {
        // Nothing to do here
    }
    public void fromStream(DataInputStream inStream, int version) throws IOException {
        // Nothing to do here
   }
    public int getVersion() {
        return 0;
    }
}