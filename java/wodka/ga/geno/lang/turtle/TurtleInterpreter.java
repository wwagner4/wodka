package wodka.ga.geno.lang.turtle;

import java.util.Iterator;

import wodka.ga.model.Mass;
import wodka.ga.model.Model;
import wodka.ga.soda.AbstractInterpreter;
import wodka.ga.soda.SodaraceProgram;

/**
 * An interpreter for the turtle like build language.
 */

public class TurtleInterpreter extends AbstractInterpreter {

    private int posX = 130;

    private int posY = 130;

    private double direction = Math.PI / 2;

    private transient boolean up = true;

    private transient boolean upBefore = true;

    private int gridWidth = 1;

    private Mass prevMass = null;

    public boolean isUpBefore() {
        return upBefore;
    }

    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
    }

    public void setUp(boolean isUp) {
        this.up = isUp;
    }

    public TurtleInterpreter(int gridWidth) {
        this.gridWidth = gridWidth;
    }

    public Model eval(SodaraceProgram pgm) {
        Model model = evalBasic(pgm);
        model.rewiseInvalidMassesAndMuscles();
        model.calculateRestlength();
        return model;
    }

    public Model evalNoRewise(SodaraceProgram pgm) {
        Model model = evalBasic(pgm);
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
            TurtleCommand cmd = (TurtleCommand) iter.next();
            cmd.eval(model, this);
        }
        Iterator masses = model.getMasses().iterator();
        int count = 0;
        while (masses.hasNext()) {
            Mass mass = (Mass) masses.next();
            mass.setXPos(mass.getXPos());
            mass.setYPos(mass.getYPos());
            count++;
        }
        return model;
    }

    int getPosX() {
        return posX;
    }

    void setPosX(int val) {
        posX = val;
    }

    int getPosY() {
        return posY;
    }

    void setPosY(int val) {
        posY = val;
    }

    double getDirection() {
        return direction;
    }

    void setDirection(double val) {
        direction = val;
    }

    void setIsUp(boolean state) {
        this.up = state;
    }

    void setUpBefore(boolean state) {
        this.upBefore = state;
    }

    boolean isUp() {
        return up;
    }

    public String toString() {
        return "(" + posX + "," + posY + "," + direction + "|"
                + (int) (direction * 180 / Math.PI) + ", up" + up + ")";
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public Mass getPrevMass() {
        return this.prevMass;
    }

    public void setPrevMass(Mass prevMass) {
        this.prevMass = prevMass;
    }

}