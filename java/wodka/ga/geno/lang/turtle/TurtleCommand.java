package wodka.ga.geno.lang.turtle;

/**
 * Common methods of turtle commands.
 */

public interface TurtleCommand extends wodka.ga.geno.lang.Command {

    void eval(wodka.ga.model.Model model, TurtleInterpreter interp);
}