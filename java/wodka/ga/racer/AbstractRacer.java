/*
 * Created on 11.11.2003
 *
 */
package wodka.ga.racer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import wodka.ExceptionHandler;
import wodka.WodkaException;
import wodka.ga.GeneticAlgorithm;
import wodka.ga.model.Model;
import wodka.ga.soda.Racer;
import wodka.ga.soda.RacerListener;
import wodka.util.StreamPersistor;
import wwan.commons.param.ListParam;
import wwan.commons.param.ListParamDesc;
import wwan.commons.param.ParamException;

/**
 * Abstract superclass for racers running in the virtual machine as 
 * the GeneticAlgorithm.
 *  
 */
abstract public class AbstractRacer implements Racer, RaceRunnerListener, RaceRunnerErrorHandler {

    protected RacerListener racerListener = null;
    protected Collection runningRaces = new Vector();
    private boolean finishedAdding = false;
    protected ExceptionHandler handler = null;
    
    private static Logger logger = Logger.getLogger(AbstractRacer.class);

    public void checkParameterList(ListParam lp) throws ParamException {
        // Nothing to be checked
    }

    public synchronized void addModel(Model m, int id, GeneticAlgorithm genAlgo, int timeout) {
        RaceRunner runner = createRaceRunner();
        runner.setErrHandler(this);
        runner.setId(id);
        runner.setModel(m);
        runner.setRaceRunnerListener(this);
        Thread thread = new Thread(runner);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
        addId(id);
    }
    private void addId(int id) {
        this.runningRaces.add(new Integer(id));
    }

    public void setRacerListener(RacerListener rl) {
        this.racerListener = rl;
    }

    public synchronized void finishedRunning(int fitness, int id) {
        this.racerListener.finishedRace(fitness, id);
        this.runningRaces.remove(new Integer(id));
        if (this.finishedAdding && this.runningRaces.isEmpty()) {
            this.racerListener.finishedAllRace();
        }
    }

    public abstract RaceRunner createRaceRunner();

    public void finishedAdding() {
        this.finishedAdding = true;
    }
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Class:" + this.getClass().getName());
        sb.append("\nfinishedAdding:" + this.finishedAdding);
        sb.append("\nrunningRaces:" + this.runningRaces);
        sb.append("\nracerListeners" + this.racerListener);
        return sb.toString();
    }

    public void setExceptionHandler(ExceptionHandler handler) {
        this.handler = handler;
    }

    public void reset() {
        // Nothing to be done
    }

    public void stop() {
        // Nothing to be done
    }

	public ListParamDesc getParameterDescription() {
		return new ListParamDesc();
	}

	public void setFieldsFromParameterList(ListParam lp) {
		//Nothing to do as long as no params are defined.
	}

	public void setParameterListFromFields(ListParam lp) {
		//Nothing to do as long as no params are defined.
	}

	public void fromStream(DataInputStream s, int version) throws IOException {
		StreamPersistor.configurableFromStream(this, s);
	}

	public int getVersion() {
		return 0;
	}

	public void toStream(DataOutputStream s) throws IOException {
		StreamPersistor.configurableToStream(this, s);
	}

    public void setTerrainXml(String terrainXml) throws WodkaException {
        // No terrain needed
    }
    public void handleThrowable(Throwable t) {
        logger.error("Error evaluating Racer.", t);
    }
    

}
