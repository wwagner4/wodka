/*
 * Log.java
 *
 * Created on March 14, 2003, 10:17 PM
 */

package wodka.ga.racer.mc;

/**
 *
 * @author  wolfi
 */
public class Linear implements GridFunction {
  
  /** Creates a new instance of Log */
  public Linear() {
      super();
  }
  
  public int gridCount(int level) {
    return level+2;
  }
  
}
