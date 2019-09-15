/*
 * Created on Nov 6, 2003
 *
 */
package wodka.test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import wodka.WodkaException;

/**
 * Tests for the SodaRacer.
 */
public class TestSodaracer {

    private Socket socket = null;
    private static Logger logger = Logger.getLogger(TestSodaracer.class);

    public TestSodaracer() {
        super();
    }

    private void run() throws Exception {
        readFromSocketMultipleTimes();
    }
    private Socket getSocket() throws WodkaException {
        try {
            if (socket == null)
                socket = new Socket("localhost", 7777);
            return socket;
        } catch (UnknownHostException e) {
			throw new WodkaException(e);
        } catch (IOException e) {
			throw new WodkaException(e);
        }
    }
    private void readFromSocketMultipleTimes() throws WodkaException {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
            for (int i = 0; i < 10; i++) {
                String messageLine = in.readLine();
                if (logger.isDebugEnabled())
                    logger.debug("messageLine=" + messageLine);
                if (isModelId(messageLine)) {
                    if (logger.isDebugEnabled())
                        logger.debug("model id=" + getModelId(messageLine));
                }
            }
        } catch (IOException e) {
			throw new WodkaException(e);
        } catch (WodkaException e) {
			throw new WodkaException(e);
        }
    }
    private boolean isModelId(String msgLine) {
        return msgLine.indexOf('?') > 0;
    }
    private int getModelId(String msgLine) {
        int i = msgLine.indexOf("?");
        if (i < 0)
            throw new Error("Not a race id message line '" + msgLine + "'");
        String modelIDString = msgLine.substring(0, i);
        return Integer.parseInt(modelIDString);
    }
    public static void main(String[] args) {
        try {
            TestSodaracer t = new TestSodaracer();
            t.run();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
