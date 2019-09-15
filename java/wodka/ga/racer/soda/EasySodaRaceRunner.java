/*
 * Created on Mar 27, 2004
 */
package wodka.ga.racer.soda;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import racer.EasyRacer;
import racer.RacerException;
import wodka.ga.model.Model;
import wodka.ga.racer.RaceRunnerErrorHandler;
import wodka.ga.racer.RaceRunner;
import wodka.ga.racer.RaceRunnerListener;
import wodka.util.Marshaller;
import wodka.util.Util;

/**
 * Runs a race for one model on the EasySodaRacer.
 *  
 */
public class EasySodaRaceRunner implements RaceRunner {

    private transient int id;
    private transient Model model = null;
    private transient RaceRunnerListener listener = null;
    private transient Marshaller marsh = Marshaller.current();
    private transient int timeout = -1;
    private transient String terrainXml = null;
    private RaceRunnerErrorHandler errHandler = null;

    public EasySodaRaceRunner() {
        super();
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRaceRunnerListener(RaceRunnerListener listener) {
        this.listener = listener;
    }

    public void run() {
        double fitness = -1.0;
        try {
            fitness = runRace();
        } catch (Throwable throwable) {
            handleThrowable(throwable);
        } finally {
            listener.finishedRunning(Util.current().convertFitnessSodaToWodka(fitness), id);
        }
    }

    private void handleThrowable(Throwable e) {
        if (this.errHandler == null) {
            e.printStackTrace();
        } else {
            errHandler.handleThrowable(e);
        }
    }

    public double runRace() throws IOException, RacerException {
        EasyRacer easy = new EasyRacer();
        StringWriter writer = new StringWriter();
        marsh.marshalModel(writer, model);
        StringReader reader = null;
        Reader terrReader = null;
        try {
            reader = new StringReader(writer.getBuffer().toString());
            if (this.terrainXml == null) {
                throw new Error("No terrain is defined.");
            }
            terrReader = new StringReader(this.terrainXml);
            return easy.run(terrReader, reader, timeout);
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (terrReader != null) {
                terrReader.close();
            }
        }
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setTerrainXml(String terrain) {
        this.terrainXml = terrain;
    }

    public void setErrHandler(RaceRunnerErrorHandler errHandler) {
        this.errHandler = errHandler;
    }
}