/*
 * ModelBuilder.java
 *
 * Created on March 14, 2003, 11:01 PM
 */

package wodka.ga.racer.mc;

import wodka.ga.model.*;

/**
 *
 * @author  wolfi
 */
public abstract class ModelHolder {
  
  private Model model = createModel();
  
  public Model getModel(){
    return model;
  }
  public abstract Model createModel();
  
}
