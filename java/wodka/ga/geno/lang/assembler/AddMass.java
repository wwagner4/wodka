package wodka.ga.geno.lang.assembler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import wodka.ga.geno.lang.Language;
import wodka.ga.model.Mass;
import wodka.ga.model.Model;
import wodka.util.Util;

/**
 * Implementation of the AddMass(x, y) command.
 */

public class AddMass implements AssemblerCommand {

    private static final long serialVersionUID = 1L;

    private int posX;

    private int posY;

    private Language language = null;

    private static Util util = Util.current();

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public Language getLanguage() {
        return this.language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public AddMass() {
        super();
    }

    public AddMass(int posX, int posY) {
        this();
        this.posX = posX;
        this.posY = posY;
    }

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }

    public void eval(Model model) {
        model.addMass(new Mass(model, this.posX, this.posY));
    }

    public String toShortDescription() {
        return "(ma " + this.posX + " " + this.posY + ")";
    }

    public wodka.ga.geno.lang.Command mutateCommand(Random ran) {
        double rana = ran.nextDouble();
        double ranb = ran.nextDouble();
        double ranamp = ran.nextDouble();
        double ranp = ran.nextDouble();
        return new AddMuscle(rana, ranb, ranamp, ranp);
    }

    public int parameterCount() {
        return 2;
    }

    public void mutateParameter(int index, Random ran) {
        switch (index) {
        case 0:
            this.posX = util.grid(this.posX + ran.nextInt(100) - 50, this
                    .getLanguage().getGridWidth(), Model.WIDTH);
            break;
        case 1:
            this.posY = util.grid(this.posY + ran.nextInt(100) - 50, this
                    .getLanguage().getGridWidth(), Model.WIDTH);
            break;
        default:
            throw new Error("Invalid parameter index. " + index);
        }
    }

    public wodka.ga.geno.lang.Command createClone() {
        AddMass cmd = new AddMass(this.posX, this.posY);
        cmd.setLanguage(this.getLanguage());
        return cmd;
    }

    public void toStream(DataOutputStream outStream) throws IOException {
        outStream.writeInt(this.posX);
        outStream.writeInt(this.posY);
    }

    public void fromStream(DataInputStream inStream, int version)
            throws IOException {
        this.posX = inStream.readInt();
        this.posY = inStream.readInt();
    }

    public int getVersion() {
        return 0;
    }

    public boolean isAddMass() {
        return true;
    }

}