package wodka.ga.geno.lang.turtle;

import wodka.ga.model.*;
import wodka.util.Util;

import java.util.*;
import java.io.*;

/**
 * Forward command. Moves the model building turtle a certain distance.
 */

public class Forward extends AbstractTurtleCommand {

    private static final long serialVersionUID = 1L;

    private int val;

    private double amplitude;

    private double phase;

    private transient Util util = Util.current();

    public double getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }

    public double getPhase() {
        return phase;
    }

    public void setPhase(double phase) {
        this.phase = phase;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public Forward() {
        super();
    }

    public Forward(int val, double amplitude, double phase) {
        this();
        this.val = val;
        this.amplitude = amplitude;
        this.phase = phase;
    }

    public String toShortDescription() {
        return "(f " + decFormat.format(val) + " "
                + decFormat.format(amplitude) + " " + decFormat.format(phase)
                + ")";
    }

    public wodka.ga.geno.lang.Command mutateCommand(Random ran) {
        switch (ran.nextInt(3)) {
        case 0:
            return getTurtleLanguage().createTurnCommandWithRandomParams();
        case 1:
            return getTurtleLanguage().createPenUpCommand();
        case 2:
            return getTurtleLanguage().createPenDownCommand();
        default:
            throw new Error("Invalid random");
        }
    }

    public int parameterCount() {
        return 3;
    }

    public void mutateParameter(int paramInt, Random ran) {
        switch (paramInt) {
        case 0:
            val = getTurtleLanguage().randomForwardValue();
            break;
        case 1:
            amplitude = ran.nextDouble();
            break;
        case 2:
            phase = ran.nextDouble();
            break;
        default:
            throw new Error("Invalid parameter index. " + paramInt);
        }
    }

    public wodka.ga.geno.lang.Command createClone() {
        Forward cmd = new Forward(val, amplitude, phase);
        cmd.setLanguage(getTurtleLanguage());
        return cmd;
    }

    public void eval(Model model, TurtleInterpreter turtInter) {
        int deltaX = (int) Math.round(val * Math.cos(turtInter.getDirection()));
        int deltaY = (int) Math
                .round(-val * Math.sin(turtInter.getDirection()));
        int xPos = util.grid((turtInter.getPosX() + deltaX), turtInter
                .getGridWidth(), Model.WIDTH);
        int yPos = util.grid((turtInter.getPosY() + deltaY), turtInter
                .getGridWidth(), Model.WIDTH);
        if (!turtInter.isUp() && turtInter.isUpBefore()) {
            Mass massA = new Mass(model, turtInter.getPosX(), turtInter
                    .getPosY());
            Mass massB = new Mass(model, xPos, yPos);
            model.addMass(massA);
            model.addMass(massB);
            turtInter.setPrevMass(massB);
            model.addMuscle(new Muscle(model, massA, massB, amplitude, phase));
            turtInter.setUpBefore(false);
        } else if (!turtInter.isUp() && !turtInter.isUpBefore()) {
            Mass prevMass = turtInter.getPrevMass();
            Mass newMass = new Mass(model, xPos, yPos);
            turtInter.setPrevMass(newMass);
            model.addMass(newMass);
            model.addMuscle(new Muscle(model, prevMass, newMass, amplitude,
                    phase));
        }
        turtInter.setPosX(xPos);
        turtInter.setPosY(yPos);
    }

    public void toStream(DataOutputStream outStream) throws IOException {
        outStream.writeInt(val);
        outStream.writeDouble(amplitude);
        outStream.writeDouble(phase);
    }

    public void fromStream(DataInputStream inStream, int version)
            throws IOException {
        val = inStream.readInt();
        amplitude = inStream.readDouble();
        phase = inStream.readDouble();
    }

    public int getVersion() {
        return 0;
    }
}