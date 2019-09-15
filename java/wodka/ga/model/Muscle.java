package wodka.ga.model;

import java.text.DecimalFormat;

public class Muscle {

    private transient Model model = null;
    private Mass fromMass = null;
    private Mass toMass = null;
    private transient String fromMassId = null;
    private transient String toMassId = null;
    private double amplitude;
    private double phase;
    private double restlength;
    private static DecimalFormat format = new DecimalFormat("#.##");

    public Muscle(Model model, String fromMassId, String toMassId, double amplitude, double phase) {
        this(model, fromMassId, toMassId, amplitude, phase, 0.0);
    }
    public Muscle(Model model, String fromMassId, String toMassId, double amplitude, double phase, double restlength) {
        this.model = model;
        this.fromMassId = fromMassId;
        this.toMassId = toMassId;
        this.phase = phase;
        this.amplitude = amplitude;
        this.restlength = restlength;
    }
    public Muscle(Model model, Mass fromMass, Mass toMass, double amplitude, double phase, double restlength) {
        this.model = model;
        this.fromMassId = fromMass.getIdentifier();
        this.fromMass = fromMass;
        this.toMassId = toMass.getIdentifier();
        this.toMass = toMass;
        this.phase = phase;
        this.amplitude = amplitude;
        this.restlength = restlength;
    }
    public Muscle(Model model, Mass fromMass, Mass toMass, double amplitude, double phase) {
        this(model, fromMass, toMass, amplitude, phase, 0.0);
    }
    public Mass getFromMass() {
        if (fromMass == null) {
            fromMass = model.getMassFromId(fromMassId);
        }
        return fromMass;
    }
    public Mass getToMass() {
        if (toMass == null) {
            toMass = model.getMassFromId(toMassId);
        }
        return toMass;
    }
    public String toString() {
        String restLength = format.format(restlength);
        String result = "(" + getFromMass().getIdentifier() + "," + getToMass().getIdentifier();
        if (restLength.equals("-1")) {
            result +=  ")";
        } else {
            result += "," + restLength + ")";
        }
        return result;
    }
    public double getAmplitude() {
        return amplitude;
    }
    public double getPhase() {
        return phase;
    }
    public void setAmplitude(double ampl) {
        this.amplitude = ampl;
    }
    public void setPhase(double phase) {
        this.phase = phase;
    }

    public double getRestlength() {
        return restlength;
    }

    public void setRestlength(double restlength) {
        this.restlength = restlength;
    }
    public Model getModel() {
        return model;
    }
    public void setFromMass(Mass mass) {
        this.fromMassId = mass.getIdentifier();
        this.fromMass = mass;
    }
    public void setToMass(Mass mass) {
        this.toMassId = mass.getIdentifier();
        this.toMass = mass;
    }

    //    private void info(String msg) {
    //        System.out.println(this.getClass().getName() + ">>" + msg);
    //    }
}