/*
 * Created on Apr 7, 2004
 */
package wodka.batch;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import wodka.ExceptionHandler;

/**
 * Saves the GA in regular intervalls. Helps to monitor the running batch
 * process.
 *  
 */
public class AutoSaveGA implements Runnable {

    private static final int PAUSE_INTERVAL = 300;
    private transient ExceptionHandler handler = null;

    private BatchRunner runner = null;

    private static Logger logger = Logger.getLogger(AutoSaveGA.class);
    private String prefix = "Batch";

    private AutoSaveGA() {
        super();
    }
    public AutoSaveGA(BatchRunner runner, ExceptionHandler handler, String prefix) {
        this();
        this.runner = runner;
        this.handler = handler;
        this.prefix = prefix;
    }

    public void run() {
        try {
            while (true) {
                pauseMinutes(PAUSE_INTERVAL);
                save();
            }
        } catch (Exception e) {
            this.handler.handleThrowable(e);
        }
    }

    private void save() throws IOException {
        String fileName = createFileName(this.runner.getRunId(), this.runner.getAlgoCount());
        this.runner.save(this.runner.getAlgo(), fileName);
        logger.debug("saved GA to " + fileName);
    }

    private String createFileName(long runId, int algoCount) {
        return this.prefix + "_" + runId + "_" + algoCount + "_" + timestamp() + ".wodka";
    }

    private String timestamp() {
        Date data = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(data);
    }

    private synchronized void pauseMinutes(int minutes) {
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