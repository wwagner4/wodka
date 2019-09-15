package wodka.ga.soda;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import wodka.ga.geno.lang.AbstractProgram;
import wodka.ga.geno.lang.Program;
import wodka.ga.model.Model;
import wodka.util.SodaGlobals;
import wodka.util.SodaGlobalsRanges;
import wodka.util.StreamPersistor;

public abstract class AbstractSodaraceProgram extends AbstractProgram implements
        SodaraceProgram {

    private static final long serialVersionUID = 1L;

    private SodaGlobals globals = null;

    protected SodaGlobalsRanges globalRanges = null;

    public AbstractSodaraceProgram() {
        super();
    }

    public AbstractSodaraceProgram(SodaGlobalsRanges ranges) {
        this.globalRanges = ranges;
        if (this.globals == null) {
            this.globals = SodaGlobals.createNewInstance(ranges);
        }
        this.globals.setRanges(ranges);
    }

    public SodaGlobalsRanges getGlobalRanges() {
        return this.globalRanges;
    }

    public void setGlobalRanges(SodaGlobalsRanges globalRanges) {
        this.globalRanges = globalRanges;
    }

    public SodaGlobals getGlobals() {
        return this.globals;
    }

    public void setGlobals(SodaGlobals globals) {
        this.globals = globals;
    }

    public void createRandomAttributes() {
        this.globals = SodaGlobals.createNewInstance(this.globalRanges);
        this.globals.setRandomValues();
    }

    protected void recombine(double mutationRate, Program parent, Program child) {
        super.recombine(mutationRate, parent, child);
        recombineAttributes((SodaraceProgram) parent, mutationRate,
                (SodaraceProgram) child);
    }

    private void recombineAttributes(SodaraceProgram otherParent,
            double mutationRate, SodaraceProgram child) {
        if (this.globals != null) {
            SodaGlobals sGlobals = (SodaGlobals) this.globals
                    .crossover(otherParent.getGlobals());
            sGlobals.mutate(mutationRate);
            child.setGlobals(sGlobals);
        }
    }

    public void toStream(DataOutputStream outStream) throws IOException {
        super.toStream(outStream);
        StreamPersistor.toStream(this.globalRanges, outStream);
        StreamPersistor.toStream(this.globals, outStream);
    }

    public void fromStream(DataInputStream inStream, int version)
            throws IOException {
        super.fromStream(inStream, version);
        this.globalRanges = (SodaGlobalsRanges) StreamPersistor
                .fromStream(inStream);
        this.globals = (SodaGlobals) StreamPersistor.fromStream(inStream);
        if (this.globals != null) {
            this.globals.setRanges(this.globalRanges);
        }
    }

    protected abstract Interpreter getInterpreter();

    public Model eval() {
        return getInterpreter().eval(this);
    }

    public Model evalNoRewise() {
        return getInterpreter().evalNoRewise(this);
    }

}
