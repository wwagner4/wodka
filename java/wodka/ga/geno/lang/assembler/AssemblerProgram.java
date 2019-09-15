package wodka.ga.geno.lang.assembler;

import wodka.ga.soda.AbstractSodaraceProgram;
import wodka.util.SodaGlobalsRanges;

/**
 * A program for the assembler building language.
 */

public class AssemblerProgram extends AbstractSodaraceProgram {

    private static final long serialVersionUID = 1L;

    public AssemblerProgram(SodaGlobalsRanges ranges) {
        super(ranges);
    }

    public AssemblerProgram() {
        super();
    }

    public wodka.ga.geno.lang.Program createEmptyChildProgram() {
        if (this.globalRanges == null) {
            throw new Error(
                    "You cannot create a child program if the parent does not have ranges defined.");
        }
        AssemblerProgram program = new AssemblerProgram(this.globalRanges);
        program.setGenotypeDesc(this.getGenotypeDesc());
        return program;
    }

    public wodka.ga.soda.Interpreter getInterpreter() {
        return new wodka.ga.geno.lang.assembler.AssemblerInterpreter();
    }
}