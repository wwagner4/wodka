/**
 * $Revision: 1.2 $ $Author: wwan $ $Date: 2005/06/20 20:52:21 $ 
 */

package wodka.ga.racer.soda;

import mins.racer.Main;

/**
 * A racer that runs with mins.
 */
public class MinsRacer extends SodaRacer {

    private static final long serialVersionUID = 1L;

    public MinsRacer() {
        super();
    }

    public String getInfo() {
        return "A racer that uses mins from Stefan West."
                + "\nThe Racer is automatically started if it is not yet started. Do not forget to click 'Accept All'.";
    }

    public String getLabel() {
        return "Mins";
    }

    protected void startRacer() {
        String[] args = new String[0];
        Main.main(args);
    }
}
