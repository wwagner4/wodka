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
public class Log implements GridFunction {
  
  /** Creates a new instance of Log */
  public Log() {
      super();
  }
  
  public int gridCount(int level) {
    int count = 2;
    for (int i=0; i<level; i++) count *= 2;
    return count;
  }
  
}
