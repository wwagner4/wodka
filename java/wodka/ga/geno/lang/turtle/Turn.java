package wodka.ga.geno.lang.turtle;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import wodka.ga.model.Model;

/**
 * Turn command. Turns the model building turtle a certain angle.
 */

public class Turn extends AbstractTurtleCommand {

    private static final long serialVersionUID = 1L;

    private int deg;

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public Turn() {
        super();
    }

    public Turn(int deg) {
        this();
        this.deg = deg % 365;
    }

    public String toShortDescription() {
        return "(t " + decFormat.format(deg) + ")";
    }

    public wodka.ga.geno.lang.Command mutateCommand(Random ran) {
        switch (ran.nextInt(3)) {
        case 0:
            return getTurtleLanguage().createForwardCommand();
        case 1:
            return getTurtleLanguage().createPenUpCommand();
        case 2:
            return getTurtleLanguage().createPenDownCommand();
        default:
            throw new Error("Invalid random");
        }
    }

    public int parameterCount() {
        return 1;
    }

    public void mutateParameter(int parmIndex, Random ran) {
        switch (parmIndex) {
        case 0:
            deg = ran.nextInt() % 356;
            break;
        default:
            throw new Error("Invalid parameter index. " + parmIndex);
        }
    }

    public wodka.ga.geno.lang.Command createClone() {
        Turn turn = new Turn(deg);
        turn.setLanguage(getTurtleLanguage());
        return turn;
    }

    public void eval(Model model, TurtleInterpreter interp) {
        double direction = (interp.getDirection() + deg * Math.PI / 180)
                % (2 * Math.PI);
        interp.setDirection(direction);
    }

    public void toStream(DataOutputStream outStream) throws IOException {
        outStream.writeInt(this.deg);
    }

    public void fromStream(DataInputStream inStream, int version)
            throws IOException {
        this.deg = inStream.readInt();
    }

    public int getVersion() {
        return 0;
    }
}