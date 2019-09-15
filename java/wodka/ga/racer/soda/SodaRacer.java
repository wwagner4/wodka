/*
 * Created on Nov 6, 2003
 *  
 */
package wodka.ga.racer.soda;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;

import wodka.ExceptionHandler;
import wodka.WodkaException;
import wodka.ga.GeneticAlgorithm;
import wodka.ga.model.Model;
import wodka.ga.soda.Racer;
import wodka.ga.soda.RacerListener;
import wodka.util.IoUtil;
import wodka.util.Marshaller;
import wodka.util.StreamPersistor;
import wodka.util.Util;
import wwan.commons.param.ListParam;
import wwan.commons.param.ListParamDesc;
import wwan.commons.param.ParamException;

/**
 * A racer that is testing his models with in real sodaraces.
 */
public class SodaRacer implements Racer {

    private static final long serialVersionUID = 1L;

    transient RacerListener racerListener = null;

    private transient Socket socket = null;

    transient Collection runningRaces = new Vector();

    transient Map idsMapping = new TreeMap();

    SodaRaceRunner raceRunner = null;

    transient boolean finishedAdding = false;

    transient long finishedAddTime = -1;

    private transient ExceptionHandler handler = null;

    static Logger logger = Logger.getLogger(SodaRacer.class);

    public SodaRacer() {
        super();
    }

    public void reset() {
        if (logger.isDebugEnabled()) {
            logger.debug("reset");
        }
        stop();
        this.runningRaces = new Vector();
        this.idsMapping = new TreeMap();
        this.finishedAdding = false;
        this.finishedAddTime = -1;
        try {
            if (this.socket != null)
                this.socket.close();
        } catch (IOException ex) {
            handleThrowable(ex);
        } finally {
            this.socket = null;
        }
    }

    Socket getSocket() throws UnknownHostException, IOException {
        String host = "localhost";
        int port = 7777;
        try {
            return tryConnecting(host, port);
        } catch (IOException e) {
            startRacer();
            try {
                return tryConnecting(host, port);
            } catch (IOException e1) {
                SodaRaceRunner srr = this.raceRunner;
                if (srr != null) {
                    srr.running = false;
                }
                this.raceRunner = null;
                throw new IOException("Could not connect to sodaracer. " + host
                        + ":" + port);
            }
        }
    }

    protected void startRacer() {
        // Do nothing here
    }

    private Socket tryConnecting(String host, int port)
            throws UnknownHostException, IOException, SocketException {
        if (this.socket == null) {
            this.socket = new Socket(host, port);
            this.socket.setSoTimeout(5000);
        }
        return this.socket;
    }

    private SodaRaceRunner getRaceRunner() {
        if (this.raceRunner == null) {
            this.raceRunner = new SodaRaceRunner();
            this.raceRunner.start();
        }
        return this.raceRunner;
    }

    public String getInfo() {
        return "A racer that is testing his models with in real sodaraces."
                + "\nNeeds a sodaracer running on localhost:7777."
                + "\nJNLP (Webstart) url for starting a sodaracer: "
                + "\n http://www.sodaplay.com/constructor/beta/racer.jnlp"
                + "\n";
    }

    public String getLabel() {
        return "Sodaracer";
    }

    public void addModel(Model m, int wodkaId, GeneticAlgorithm genAlgo,
            int timeout) throws WodkaException {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("addRace wodkaId:" + wodkaId);
            }
            this.runningRaces.add(new Integer(wodkaId));
            long sodaId = getRaceRunner().getNextSodaId();
            if (isRunning()) {
                setId(sodaId, wodkaId);
                PrintWriter out = new PrintWriter(new OutputStreamWriter(
                        getSocket().getOutputStream()));
                String xml = createXml(m);
                if (logger.isDebugEnabled())
                    logger.debug("xml:\n" + xml);
                if (logger.isDebugEnabled()) {
                    validateXml(xml);
                }
                out.println(xml);
                out.flush();
                if (logger.isDebugEnabled()) {
                    logger.debug("added model wodkaId=" + wodkaId + " sodaId="
                            + sodaId);
                }
            }
        } catch (UnknownHostException e) {
            throw new WodkaException(e);
        } catch (IOException e) {
            throw new WodkaException(e);
        }
    }

    private boolean isRunning() {
        return this.raceRunner != null && this.raceRunner.running;
    }

    private void validateXml(String xml) throws IOException {
        try {
            String schemaUrl = IoUtil.current().loadResourceStrict(
                    "test/soda.xsd").toExternalForm();
            InputStream inStream = new ByteArrayInputStream(xml.getBytes());
            try {
                Util.current().validateXml(inStream, schemaUrl);
                if (logger.isDebugEnabled()) {
                    logger.debug("Validation ok");
                }
            } finally {
                inStream.close();
            }
        } catch (JDOMException e) {
            throw new IOException(e.toString());
        }

    }

    private String createXml(Model m) throws IOException {
        StringWriter writer = new StringWriter();
        Marshaller.current().marshalModel(writer, m);
        return writer.getBuffer().toString();
    }

    private void setId(long sodaId, int wodkaId) {
        this.idsMapping.put(new Long(sodaId), new Integer(wodkaId));
    }

    int getWodkaId(long sodaId) {
        Integer wodkaId = (Integer) this.idsMapping.get(new Long(sodaId));
        if (wodkaId == null) {
            throw new Error("Did not find soda id " + sodaId + " in:\n\t"
                    + this.idsMapping);
        }
        return wodkaId.intValue();
    }

    boolean existsWodkaId(int sodaId) {
        Integer wodkaId = (Integer) this.idsMapping.get(new Long(sodaId));
        return wodkaId != null;
    }

    protected int calculateFitness(double val) {
        return Util.current().convertFitnessSodaToWodka(val);
    }

    public void setRacerListener(RacerListener rl) {
        this.racerListener = rl;
    }

    public void finishedAdding() {
        if (logger.isDebugEnabled()) {
            logger.debug("finishedAdding");
        }
        this.finishedAdding = true;
        this.finishedAddTime = System.currentTimeMillis();
    }

    public void setExceptionHandler(ExceptionHandler handler) {
        this.handler = handler;
    }

    public ListParamDesc getParameterDescription() {
        return new ListParamDesc();
    }

    public void setFieldsFromParameterList(ListParam listParam) {
        // Nothing to do as long as no params are defined.
    }

    public void setParameterListFromFields(ListParam listParam) {
        // Nothing to do as long as no params are defined.
    }

    public void fromStream(DataInputStream inStream, int version)
            throws IOException {
        StreamPersistor.configurableFromStream(this, inStream);
    }

    public int getVersion() {
        return 0;
    }

    public void toStream(DataOutputStream outStream) throws IOException {
        StreamPersistor.configurableToStream(this, outStream);
    }

    private void stop() {
        if (logger.isDebugEnabled()) {
            logger.debug("received stop");
        }
        if (this.raceRunner != null) {
            this.raceRunner.running = false;
        }
        if (this.racerListener != null) {
            this.racerListener.finishedAllRace();
        }
    }

    private class SodaRaceRunner extends Thread {

        private static final String TIMEOUT_STRING = "timeout";

        private transient long nextSodaId = -1;

        transient boolean running = true;

        private transient long latestSodaId = -1;

        public SodaRaceRunner() {
            super();
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        getSocket().getInputStream()));
                while (this.running) {
                    takeOneStep(in);
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("stopped");
                }
            } catch (java.net.SocketException e) {
                handleSocketException(e);
            } catch (Throwable e) {
                this.running = false;
                handleThrowable(e);
            } finally {
                if (logger.isDebugEnabled()) {
                    logger.debug("Finished SodaRaceRunner");
                }
                SodaRacer.this.raceRunner = null;
            }
        }

        private void handleSocketException(java.net.SocketException exc) {
            if (this.running) {
                this.running = false;
                handleThrowable(new wodka.WodkaException(
                        "A problem with the connection to the soda race application occured.",
                        exc));
            }
        }

        private String takeOneStep(BufferedReader bufReader) throws IOException {
            String line = null;
            if (isTimeout()) {
                terminateBecauseTimeout();
            }
            try {
                line = bufReader.readLine();
            } catch (InterruptedIOException ex) {
                line = TIMEOUT_STRING;
            }
            if (line == null) {
                line = "null";
            }
            if (logger.isDebugEnabled()) {
                logger.debug("run messageLine:" + line);
            }
            if (isSodaID(line)) {
                this.nextSodaId = getSodaId(line);
            } else if (isResult(line)) {
                if (existsWodkaId(this.getResultId(line))) {
                    terminateBecauseResult(getResultId(line),
                            getResultValue(line));
                }
            } else if (line.equals(TIMEOUT_STRING)) {
                // Do nothing
            } else {
                if (logger.isDebugEnabled())
                    logger
                            .debug("run unknown message line messageLine:"
                                    + line);
            }
            return line;
        }

        private void terminateBecauseResult(long resultId, double resultValue) {
            int wodkaId = getWodkaId(resultId);
            int fitness = calculateFitness(resultValue);
            if (logger.isDebugEnabled()) {
                logger.debug("finished wodkaId=" + wodkaId + " sodaId="
                        + resultId + " fitness=" + fitness
                        + " running races(size)="
                        + SodaRacer.this.runningRaces.size()
                        + " idsMapping(size)"
                        + SodaRacer.this.idsMapping.size());
            }
            SodaRacer.this.runningRaces.remove(new Integer(wodkaId));
            SodaRacer.this.racerListener.finishedRace(fitness, wodkaId);
            if (SodaRacer.this.finishedAdding
                    && SodaRacer.this.runningRaces.isEmpty()) {
                if (logger.isDebugEnabled())
                    logger.debug("finished all in a regular way.");
                SodaRacer.this.racerListener.finishedAllRace();
                SodaRacer.this.runningRaces = new Vector();
                SodaRacer.this.idsMapping = new TreeMap();
                SodaRacer.this.finishedAdding = false;
                SodaRacer.this.finishedAddTime = -1;
            }
        }

        private void terminateBecauseTimeout() {
            if (logger.isDebugEnabled()) {
                logger.debug("finishing because timeout. \n    running races "
                        + SodaRacer.this.runningRaces + "\n    idsMapping"
                        + SodaRacer.this.idsMapping);
            }
            java.util.Iterator iter = SodaRacer.this.runningRaces.iterator();
            while (iter.hasNext()) {
                Integer wodkaId = (Integer) iter.next();
                SodaRacer.this.racerListener
                        .finishedRace(0, wodkaId.intValue());
            }
            SodaRacer.this.racerListener.finishedAllRace();
            SodaRacer.this.runningRaces = new Vector();
            SodaRacer.this.idsMapping = new TreeMap();
            SodaRacer.this.finishedAdding = false;
            SodaRacer.this.finishedAddTime = -1;
        }

        private boolean isTimeout() {
            return SodaRacer.this.finishedAdding
                    && SodaRacer.this.finishedAddTime > 0
                    && System.currentTimeMillis()
                            - SodaRacer.this.finishedAddTime > 500000;
        }

        public long getNextSodaId() {
            int count = 0;
            int pauseTime = 5;
            int maxRequests = 200000;
            while (this.nextSodaId == -1 && this.running) {
                if (count >= maxRequests) {
                    throw new Error(
                            "Timeout requesting soda id.\nNo id was created after "
                                    + (maxRequests * pauseTime)
                                    / 1000
                                    + " seconds\nTry clear contenders and start again");
                }
                pause(pauseTime);
                count++;
            }
            long tmp = this.nextSodaId;
            this.nextSodaId = -1;
            return tmp;
        }

        private boolean isSodaID(String msgLine) {
            boolean result = false;
            int i = msgLine.indexOf('?');
            if (i > 0) {
                long id = extractId(msgLine, i);
                if (id <= this.latestSodaId) {
                    logger.warn("id was sent twice. id:" + id + " latest id:"
                            + this.latestSodaId);
                } else {
                    result = true;
                }
            }
            return result;
        }

        private boolean isResult(String msgLine) {
            return msgLine.indexOf('>') > 0;
        }

        private long getSodaId(String msgLine) {
            int i = msgLine.indexOf("?");
            return extractId(msgLine, i);
        }

        private long extractId(String msgLine, int i) throws Error,
                NumberFormatException {
            if (i < 0) {
                throw new Error("Not a race id message line '" + msgLine + "'");
            }
            String idString = msgLine.substring(0, i);
            long id = Long.parseLong(idString);
            if (id == 0) {
                id = 1;
            }
            return id;
        }

        private int getResultId(String msgLine) {
            int index = msgLine.indexOf('>');
            if (index < 0)
                throw new Error("Not a race result '" + msgLine + "'");
            String idString = msgLine.substring(0, index);
            return Integer.parseInt(idString);
        }

        private double getResultValue(String msgLine) {
            int i = msgLine.indexOf('>');
            if (i < 0)
                throw new Error("Not a race result '" + msgLine + "'");
            String str = msgLine.substring(i + 1);
            return Double.parseDouble(str);
        }

        private synchronized void pause(int milliSec) {
            try {
                this.wait(milliSec);
            } catch (InterruptedException e) {
                // Continue
            }
        }

    }

    /**
     * Nothing to be checked.
     * 
     * @see wodka.param.Configurable#checkParameterList(wodka.param.ListParam)
     */
    public void checkParameterList(ListParam lp) throws ParamException {
        // Nothing to be checked
    }

    void handleThrowable(Throwable throwable) {
        if (this.handler == null) {
            throwable.printStackTrace();
        } else {
            this.handler.handleThrowable(throwable);
        }
    }

    public void setTerrainXml(String terrainXml) throws WodkaException {
        if (terrainXml != null) {
            logger.warn("Added terrain Xml although it cannot be used by:"
                    + this.getClass().getName());
        }
    }

    public void resetTimeoutOptimization() {
        // do nothing
    }

}