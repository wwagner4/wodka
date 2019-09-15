package wodka.ga.soda;

import wodka.ga.model.Model;

/**
 * Defines how to build Models from Programs.
 */

public interface Interpreter {

    /**
     * Builds a Model from a Program.
     */
    Model eval(SodaraceProgram pgm);

    /**
     * Builds a Model from a Program without rewising invalid 
     * nodes and springs.
     */
    Model evalNoRewise(SodaraceProgram program);

}