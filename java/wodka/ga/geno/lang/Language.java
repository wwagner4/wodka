/*
 * Created on Mar 2, 2004
 */
package wodka.ga.geno.lang;

import wodka.ga.GenotypeDesc;

/**
 * Interface for language like Genotypes.
 * 
 */
public interface Language extends GenotypeDesc {
    
    int getGridWidth();
    int getProgramLength();
    
}
