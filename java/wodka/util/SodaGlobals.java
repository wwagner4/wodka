/*
 * Created on Jan 30, 2004
 */
package wodka.util;

/**
 * Holds the set of global variables of a soda model.
 * 
 * @author wwagner4
 */
public class SodaGlobals extends DoubleValues {

    private static final long serialVersionUID = 1L;

    public static final String FRICTION = "friction";

    public static final String GRAVITY = "gravity";

    public static final String SPRINGYNESS = "springyness";

    public static final String AMPLITUDE = "amplitude";

    public static final String PHASE = "phase";

    public static final String SPEED = "speed";

    public SodaGlobals() {
        super();
    }

    private SodaGlobals(DoubleRanges ranges) {
        super(ranges);
    }

    public static SodaGlobals createNewInstance(DoubleRanges ranges) {
        SodaGlobals globals = new SodaGlobals(ranges);
        globals.initDefaultValues();
        return globals;
    }

    public double getAmplitude() {
        return getValue(AMPLITUDE);
    }

    public void setAplitude(double amplitude) {
        this.setValue(AMPLITUDE, amplitude);
    }

    public double getFriction() {
        return this.getValue(FRICTION);
    }

    public void setFriction(double friction) {
        this.setValue(FRICTION, friction);
    }

    public double getGravity() {
        return getValue(GRAVITY);
    }

    public void setGravity(double gravity) {
        this.setValue(GRAVITY, gravity);
    }

    public double getPhase() {
        return getValue(PHASE);
    }

    public void setPhase(double phase) {
        this.setValue(PHASE, phase);
    }

    public double getSpeed() {
        return this.getValue(SPEED);
    }

    public void setSpeed(double speed) {
        this.setValue(SPEED, speed);
    }

    public double getSpringyness() {
        return this.getValue(SPRINGYNESS);
    }

    /**
     * @param springyness
     *            The springyness to set.
     */
    public void setSpringyness(double springyness) {
        this.setValue(SPRINGYNESS, springyness);
    }

    protected void initDefaultValues() {
        defineValueWithDefault(AMPLITUDE, 0.5);
        defineValueWithDefault(FRICTION, 0.5);
        defineValueWithDefault(GRAVITY, 0.5);
        defineValueWithDefault(SPRINGYNESS, 0.5);
        defineValueWithDefault(AMPLITUDE, 0.5);
        defineValueWithDefault(PHASE, 0.5);
        defineValueWithDefault(SPEED, 0.5);
    }

    protected String getRangeKey(String name) {
        return name;
    }

    protected DoubleValues createInstance() {
        SodaGlobals globals = new SodaGlobals(this.ranges);
        globals.initDefaultValues();
        return globals;
    }

}
