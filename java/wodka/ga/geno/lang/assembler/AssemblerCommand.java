package wodka.ga.geno.lang.assembler;

/**
 * An assembler command
 */

public interface AssemblerCommand extends wodka.ga.geno.lang.Command {

    void eval(wodka.ga.model.Model model);
    boolean isAddMass();
}