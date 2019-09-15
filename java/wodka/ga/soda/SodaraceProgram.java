package wodka.ga.soda;

import wodka.ga.geno.lang.Program;
import wodka.ga.model.Model;
import wodka.util.SodaGlobals;

public interface SodaraceProgram extends Program {
    
    Model eval();

    Model evalNoRewise();

    SodaGlobals getGlobals();

    void setGlobals(SodaGlobals globals);

}
