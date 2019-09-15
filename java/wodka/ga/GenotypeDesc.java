/*
 * Created on 12.12.2003
 *
 */
package wodka.ga;

import wodka.util.ContentComparable;
import wodka.util.StreamPersistable;
import wwan.commons.param.Informative;

/**
 * @author wwagner4
 *
 */
public interface GenotypeDesc extends StreamPersistable, Informative, ContentComparable {

    Genotype createRandomGenotype();
    String getLabel();
    String getShortName();
    String getInfo();
    
}
