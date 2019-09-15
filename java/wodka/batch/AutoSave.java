/*
 * Created on Apr 7, 2004
 */
package wodka.batch;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import wodka.ApplicationManager;
import wodka.ExceptionHandler;
import wodka.util.PersistanceHandler;

/**
 * Prozess that saves a GA in regular intervals.
 *  
 */
public class AutoSave implements Runnable {

    private transient ExceptionHandler handler = null;

    private BatchRunner runner = null;

    private static Logger logger = Logger.getLogger(AutoSave.class);

    public AutoSave(BatchRunner runner, ExceptionHandler handler) {
        super();
        this.runner = runner;
        this.handler = handler;
    }

    public void run() {
        try {
            while (true) {
                pause(ApplicationManager.current().getPropertyInt(BatchRunner.PROP_ASAVE_INTER));
                save();
            }
        } catch (Exception e) {
            this.handler.handleThrowable(e);
        }
    }
    private void save() throws IOException {
        ApplicationManager manager = ApplicationManager.current();
        String fileName = manager.getProperty(BatchRunner.PROP_ASAVE_FILE);
        File file = new File(manager.homeDirectory(), fileName);
        PersistanceHandler.current().saveObject(this.runner, file);
        logger.debug("saved to " + file.getAbsolutePath());
    }

    private synchronized void pause(int minutes) {
        try {
            this.wait(minutes * 1000 * 60);
        } catch (InterruptedException e) {
            // Continue
        }
    }
    public BatchRunner getRunner() {
        return this.runner;
    }

    public void setRunner(BatchRunner runner) {
        this.runner = runner;
    }

}
