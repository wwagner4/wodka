/*
 * Created on 12.12.2003
 *
 */
package wodka.ga;

/**
 * @author wwagner4
 *
 */

public interface Genotype extends wodka.util.StreamPersistable {

    Genotype recombine(Genotype genotype, double mutationRate);
    void setGenotypeDesc(GenotypeDesc geno);
    GenotypeDesc getGenotypeDesc();
    

}
