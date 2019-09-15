/*
 * Created on Feb 12, 2004
 */
package wodka.test;

import wodka.ApplicationManager;
import wodka.WodkaException;

/**
 * Test for starting the sodaracer.
 * 
 * @author wwagner4
 */
public class StartSodaracer {

    public StartSodaracer() {
        super();
    }

    public static void main(String[] args) {
        try {
            StartSodaracer sr = new StartSodaracer();
            sr.startSodaracer();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void startSodaracer() throws WodkaException {
        ApplicationManager.current().startSodaracer();
    }

}
