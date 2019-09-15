package wodka.ga.geno.lang;

import java.util.Iterator;

import wodka.ga.Genotype;

/**
 * A Program is a spezial kind of genotype. Evaluation of a program by an
 * interpreter should generate a phenotype (soda model).
 * 
 */

public interface Program extends Genotype {

    void add(Command cmd);

    Iterator commands();

    Program createEmptyChildProgram();

    Language getLanguage();

}