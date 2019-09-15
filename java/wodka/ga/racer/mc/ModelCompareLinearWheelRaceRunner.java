/*
 * ModelCompareSquare.java
 *
 * Created on March 10, 2003, 8:19 AM
 */

package wodka.ga.racer.mc;


/**
 *
 * @author  wolfi
 */
public class ModelCompareLinearWheelRaceRunner extends ModelCompareRaceRunner {

  public ModelCompareLinearWheelRaceRunner() {
      super();
  }

  protected GridFunction getGridFunction() {
    return new Linear();
  }  
  protected ModelHolder getModelHolder() {
    return new WheelHolder();
  }  


}
