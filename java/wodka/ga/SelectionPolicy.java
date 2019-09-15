package wodka.ga;

import wodka.util.StreamPersistable;
import wwan.commons.param.Informative;


public interface SelectionPolicy extends Informative, StreamPersistable {
  
  Population selectNewPopulation(Population pop, GenotypeDesc desc, double mr);
  int getInitialPopulationSize();
  String getLabel();
  String getInfo();
}
