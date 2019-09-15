package wodka.ga.geno.lang.assembler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import wodka.ga.geno.lang.Language;
import wodka.ga.model.Mass;
import wodka.ga.model.Model;
import wodka.ga.model.Muscle;
import wodka.util.Util;

/**
 * Implementation of the AddMuscle command.
 */

public class AddMuscle implements AssemblerCommand {

    private static final long serialVersionUID = 1L;

    private double auxFrMassInd;

    private double auxToMassInd;

    private double amplitude;

    private double phase;

    private static DecimalFormat decFormat = new DecimalFormat("#.###");

    private Language language = null;

    private static Util util = Util.current();

    public double getAmplitude() {
        return this.amplitude;
    }

    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }

    public double getPhase() {
        return this.phase;
    }

    public void setPhase(double phase) {
        this.phase = phase;
    }

    public Language getLanguage() {
        return this.language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public AddMuscle() {
        super();
    }

    public AddMuscle(double fromMassInd, double toMassInd, double amplitude,
            double phase) {
        this();
        if (fromMassInd < 0.0 || fromMassInd > 1.0) {
            throw new Error("Invalid from aux index: " + fromMassInd);
        }
        if (toMassInd < 0.0 || toMassInd > 1.0) {
            throw new Error("Invalid to aux index: " + toMassInd);
        }
        this.auxFrMassInd = fromMassInd;
        this.auxToMassInd = toMassInd;
        this.amplitude = amplitude;
        this.phase = phase;
    }

    public double getAuxFrMassInd() {
        return this.auxFrMassInd;
    }

    public void setAuxFrMassInd(double val) {
        if (val < 0.0 || val > 1.0) {
            throw new Error("Invalid aux index: " + val);
        }
        this.auxFrMassInd = val;
    }

    public double getAuxToMassInd() {
        return this.auxToMassInd;
    }

    public void setAuxToMassInd(double val) {
        this.auxToMassInd = val;
    }

    public void eval(Model model) {
        Mass fromMass = getMassFromAuxIndex(model, this.auxFrMassInd);
        Mass toMass = getMassFromAuxIndex(model, this.auxToMassInd);
        model.addMuscle(new Muscle(model, fromMass, toMass, this.amplitude,
                this.phase));
    }

    private Mass getMassFromAuxIndex(Model model, double auxIndex) {
        List ids = model.getMassIdentifiers();
        int index = (int) Math.floor(ids.size() * auxIndex);
        if (auxIndex == 1.0) {
            index--;
        }
        return model.getMassFromId((String) ids.get(index));
    }

    public String toShortDescription() {
        return "(mu " + decFormat.format(this.auxFrMassInd) + " "
                + decFormat.format(this.auxToMassInd) + ")";
    }

    public wodka.ga.geno.lang.Command mutateCommand(Random ran) {
        int posX = util.grid(ran.nextInt(Model.WIDTH), this.getLanguage()
                .getGridWidth(), Model.WIDTH);
        int posY = util.grid(ran.nextInt(Model.WIDTH), this.getLanguage()
                .getGridWidth(), Model.WIDTH);
        return new AddMass(posX, posY);
    }

    public int parameterCount() {
        return 4;
    }

    public void mutateParameter(int parIndex, Random ran) {
        switch (parIndex) {
        case 0:
            this.auxFrMassInd = ran.nextDouble();
            break;
        case 1:
            this.auxToMassInd = ran.nextDouble();
            break;
        case 2:
            this.amplitude = ran.nextDouble();
            break;
        case 3:
            this.phase = ran.nextDouble();
            break;
        default:
            throw new Error("Invalid parameter index. " + parIndex);
        }
    }

    public wodka.ga.geno.lang.Command createClone() {
        AddMuscle cmd = new AddMuscle(this.auxFrMassInd, this.auxToMassInd,
                this.amplitude, this.phase);
        cmd.setLanguage(this.getLanguage());
        return cmd;
    }

    public void toStream(DataOutputStream outStream) throws IOException {
        outStream.writeDouble(this.auxToMassInd);
        outStream.writeDouble(this.auxFrMassInd);
        outStream.writeDouble(this.amplitude);
        outStream.writeDouble(this.phase);
    }

    public void fromStream(DataInputStream inStream, int version)
            throws IOException {
        this.auxToMassInd = inStream.readDouble();
        this.auxFrMassInd = inStream.readDouble();
        this.amplitude = inStream.readDouble();
        this.phase = inStream.readDouble();
    }

    public int getVersion() {
        return 0;
    }

    public boolean isAddMass() {
        return false;
    }

}