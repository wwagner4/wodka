package wodka.ga.geno.lang.turtle;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import wodka.ga.Genotype;
import wodka.ga.geno.lang.AbstractLanguage;
import wodka.ga.geno.lang.Program;
import wodka.ga.model.Model;
import wwan.commons.param.ListParam;
import wwan.commons.param.ListParamDesc;
import wwan.commons.param.ParamException;
import wwan.commons.param.enums.EnumParamDesc;
import wwan.commons.param.enums.StringPopulator;

public class TurtleLanguage extends AbstractLanguage {

    private static final long serialVersionUID = 1L;

    private int maxForward = 50;

    private transient Random ran = new Random();

    public int getMaxForward() {
        return maxForward;
    }

    public void setMaxForward(int maxForward) {
        this.maxForward = maxForward;
    }

    public TurtleLanguage() {
        super();
    }

    public TurtleLanguage(int gridWidth) {
        super(gridWidth);
    }

    protected void fillTurtleProgram(Program pgm) {
        for (int i = 0; i < getProgramLength(); i++) {
            switch (ran.nextInt(4)) {
            case 0:
                pgm.add(createForwardCommand());
                break;
            case 1:
                pgm.add(createTurnCommandWithRandomParams());
                break;
            case 2:
                pgm.add(createPenUpCommand());
                break;
            case 3:
                pgm.add(createPenDownCommand());
                break;
            default:
                throw new Error("Invalid random");
            }
        }
    }

    protected PenDown createPenDownCommand() {
        PenDown cmd = new wodka.ga.geno.lang.turtle.PenDown();
        cmd.setLanguage(this);
        return cmd;
    }

    protected PenUp createPenUpCommand() {
        PenUp cmd = new wodka.ga.geno.lang.turtle.PenUp();
        cmd.setLanguage(this);
        return cmd;
    }

    protected Turn createTurnCommandWithRandomParams() {
        Turn cmd = new wodka.ga.geno.lang.turtle.Turn(randomTurnValue());
        cmd.setLanguage(this);
        return cmd;
    }

    protected int randomTurnValue() {
        return ran.nextInt(180) - 90;
    }

    protected Forward createForwardCommand() {
        Forward cmd = new wodka.ga.geno.lang.turtle.Forward(
                randomForwardValue(), ran.nextDouble(), ran.nextDouble());
        cmd.setLanguage(this);
        return cmd;
    }

    protected int randomForwardValue() {
        return ran.nextInt(maxForward * 2) - maxForward;
    }

    public void toStream(DataOutputStream outStream) throws IOException {
        super.toStream(outStream);
        outStream.writeInt(maxForward);
    }

    public void fromStream(DataInputStream inStream, int version)
            throws IOException {
        super.fromStream(inStream, version);
        maxForward = inStream.readInt();
    }

    public String getInfo() {
        StringBuffer buffer = new StringBuffer();
        buffer
                .append("A language much like the one used for generating turtle graphics.");
        buffer.append("\nIt consists of two commands.");
        buffer
                .append("\nTurn: Turns the turtle for a certain angle at the current position.");
        buffer.append("\nForward: Moves the turtle for a certain amount. "
                + "The max of that amount is " + this.maxForward + ". "
                + "Negative values move it backwards.");
        buffer
                .append("\nThe initial position of the turtle is in the center of the building area.");
        buffer.append("\nThe building area has a size of " + Model.WIDTH + "x"
                + Model.WIDTH + " pixels.");
        buffer
                .append("\nIf the turtle tryes to move out of the building area it is stopped at the border.\n");
        buffer.append(super.getInfo());
        return buffer.toString();
    }

    public String getLabel() {
        return "Turtle";
    }

    public String getShortName() {
        return "TU";
    }

    public ListParamDesc getParameterDescription() throws ParamException {
        ListParamDesc lpd = super.getParameterDescription();
        lpd.add(getParameterDescTurtle());
        return lpd;
    }

    private ListParamDesc getParameterDescTurtle() throws ParamException {
        ListParamDesc grp = new ListParamDesc();
        grp.setLabel("Base Turtle Parameters");
        grp.add(this.getParameterDescMaxForward());
        return grp;
    }

    private EnumParamDesc getParameterDescMaxForward() throws ParamException {
        EnumParamDesc desc = new EnumParamDesc();
        desc.setName("maxForward");
        desc.setLabel("Max for Forward");
        desc.setUnit("pixel");
        StringPopulator pop = new StringPopulator("1;2,5,10;15;20;50;100;150");
        desc.populate(pop);

        desc.setDefaultSelectionIndex(3);
        return desc;
    }

    public void setFieldsFromParameterList(ListParam listParam)
            throws ParamException {
        super.setFieldsFromParameterList(listParam);
        this.maxForward = listParam.getParamValueInt("maxForward");
    }

    public void setParameterListFromFields(ListParam listParam)
            throws ParamException {
        super.setParameterListFromFields(listParam);
        listParam.setParamValue("maxForward", maxForward);
    }

    public Genotype createRandomGenotype() {
        TurtleProgram pgm = new TurtleProgram(this.getGridWidth(),
                this.globalRanges);
        pgm.createRandomAttributes();
        for (int i = 0; i < getProgramLength(); i++) {
            switch (ran.nextInt(4)) {
            case 0:
                pgm.add(createForwardCommand());
                break;
            case 1:
                pgm.add(createTurnCommandWithRandomParams());
                break;
            case 2:
                pgm.add(createPenUpCommand());
                break;
            case 3:
                pgm.add(createPenDownCommand());
                break;
            default:
                throw new Error("Invalid random");
            }
        }
        // info(pgm.toString());
        return pgm;
    }

    protected void defineProgramLengthValues(EnumParamDesc desc)
            throws ParamException {
        StringPopulator pop = new StringPopulator(
                "100;200;300;400;500;600;700;800");
        desc.populate(pop);
        desc.setDefaultSelectionIndex(3);
    }

    protected int defaultProgramLength() {
        return 500;
    }

    protected int defaultGridWidth() {
        return 30;
    }

}