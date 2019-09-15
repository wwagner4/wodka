package wodka.ga.geno.lang.assembler;

import java.util.Random;

import wodka.ga.Genotype;
import wodka.ga.geno.lang.AbstractLanguage;
import wodka.ga.model.Model;
import wodka.util.Util;
import wwan.commons.param.ParamException;
import wwan.commons.param.enums.EnumParamDesc;
import wwan.commons.param.enums.StringPopulator;

public class AssemblerLanguage extends AbstractLanguage {

    private static final long serialVersionUID = 1L;

    private transient Random ran = new Random();

    public AssemblerLanguage() {
        super();
    }

    public Genotype createRandomGenotype() {
        wodka.ga.geno.lang.assembler.AssemblerProgram pgm = new wodka.ga.geno.lang.assembler.AssemblerProgram(
                this.globalRanges);
        pgm.setLanguage(this);
        pgm.createRandomAttributes();
        for (int i = 0; i < getProgramLength(); i++) {
            if (this.ran.nextBoolean()) {
                int ranx = Util.current().grid(this.ran.nextInt(Model.WIDTH),
                        this.getGridWidth(), Model.WIDTH);
                int rany = Util.current().grid(this.ran.nextInt(Model.WIDTH),
                        this.getGridWidth(), Model.WIDTH);
                pgm.add(new wodka.ga.geno.lang.assembler.AddMass(ranx, rany));
            } else {
                double rana = this.ran.nextDouble();
                double ranb = this.ran.nextDouble();
                double ranamp = this.ran.nextDouble();
                double ranp = this.ran.nextDouble();
                pgm.add(new wodka.ga.geno.lang.assembler.AddMuscle(rana, ranb,
                        ranamp, ranp));
            }
        }
        return pgm;
    }

    public int getVersion() {
        return 0;
    }

    public String getInfo() {
        StringBuffer buffer = new StringBuffer();
        buffer
                .append("An assebler like language that consists of two commands.");
        buffer.append("\nAddMass: Adds a mass at an absolute position.");
        buffer.append("\nAddMuscle: Adds a muscle between two masses.");
        buffer.append("\nA sequence of these commands builds a program. "
                + "Evaluation of this program build a model.\n");
        buffer.append(super.getInfo());
        return buffer.toString();
    }

    public String getLabel() {
        return "Assembler";
    }

    public String getShortName() {
        return "Ass";
    }

    protected void defineProgramLengthValues(EnumParamDesc desc)
            throws ParamException {
        StringPopulator pop = new StringPopulator(
                "30;40;50;60;70;80;90;100;150;200;500");
        desc.populate(pop);

        desc.setDefaultSelectionIndex(3);
    }

    protected int defaultProgramLength() {
        return 70;
    }

    protected int defaultGridWidth() {
        return 40;
    }

}