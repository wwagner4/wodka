/**
 * $Revision: 1.1 $ $Author: wwan $ $Date: 2004/09/03 11:49:35 $ 
 */

package wodka.ga.racer;

/**
 * Handles an error that occures during execution
 */
public interface RaceRunnerErrorHandler {
    
    void handleThrowable(Throwable t);

}
