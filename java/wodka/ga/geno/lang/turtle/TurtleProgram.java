package wodka.ga.geno.lang.turtle;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import wodka.ga.soda.AbstractSodaraceProgram;
import wodka.util.SodaGlobalsRanges;

/**
 * A Program for the turtle language.
 */

public class TurtleProgram extends AbstractSodaraceProgram {

    private int gridWidth = 1;

    private static final long serialVersionUID = 1L;

    public int getGridWidth() {
        return gridWidth;
    }

    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
    }

    public TurtleProgram() {
        super();
    }

    public TurtleProgram(int gridWidth, SodaGlobalsRanges ranges) {
        super(ranges);
        this.gridWidth = gridWidth;
    }

    public wodka.ga.geno.lang.Program createEmptyChildProgram() {
        if (this.globalRanges == null) {
            throw new Error(
                    "You cannot create a child program if the parent does not have ranges defined.");
        }
        TurtleProgram program = new TurtleProgram(gridWidth, this.globalRanges);
        program.setGenotypeDesc(this.getGenotypeDesc());
        return program;
    }

    public wodka.ga.soda.Interpreter getInterpreter() {
        return new TurtleInterpreter(gridWidth);
    }

    public void fromStream(DataInputStream inStream, int version)
            throws IOException {
        super.fromStream(inStream, version);
        this.gridWidth = inStream.readInt();
    }

    public void toStream(DataOutputStream outStream) throws IOException {
        super.toStream(outStream);
        outStream.writeInt(this.gridWidth);
    }

}