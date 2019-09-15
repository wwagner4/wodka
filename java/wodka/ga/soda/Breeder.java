package wodka.ga.soda;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import wodka.ExceptionHandler;
import wodka.WodkaException;
import wodka.ga.GeneticAlgorithm;
import wodka.ga.Population;
import wodka.ga.PopulationHistory;
import wodka.util.Util;
import wodka.view.CategorizedInfoModel;

/**
 * Breeds Individuals using a GA
 */

public class Breeder implements Runnable, CategorizedInfoModel {

    private static final String NEW_LINE = "\n";

    private SodaraceGeneticAlgorithm genAlgo = null;

    private transient boolean running = false;

    private transient wodka.util.MeanBuffer meanBuffer = new wodka.util.MeanBuffer(
            40);

    private transient Collection listeners = new Vector();

    private transient ExceptionHandler exHandler = null;

    private static Logger logger = Logger.getLogger(Breeder.class);

    private transient long genCount = 0;

    public Breeder() {
        super();
    }

    public void setExceptionHandler(ExceptionHandler handler) {
        this.exHandler = handler;
    }

    public void addListener(BreederListener listener) {
        this.listeners.add(listener);
    }

    public void run() {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("run");
            }
            this.genCount = 0;
            while (this.running) {
                evalOneGeneration();
                int sleepTime = this.meanBuffer.mean() / 100;
                if (sleepTime <= 0) {
                    pause(1);
                } else {
                    pause(sleepTime);
                }
                this.genCount++;
            }
        } catch (Throwable t) {
            this.running = false;
            this.exHandler.handleThrowable(t);
        }
    }

    private void evalOneGeneration() throws WodkaException {
        long begin = System.currentTimeMillis();
        this.genAlgo.evaluateOneGeneration();
        informListeners();
        this.meanBuffer.add((int) (System.currentTimeMillis() - begin));
    }

    private void informListeners() {
        Iterator iter = this.listeners.iterator();
        while (iter.hasNext()) {
            BreederListener listener = (BreederListener) iter.next();
            listener.performEvaluationOfStepFinished(this.genAlgo);
        }
    }

    private synchronized void pause(int milliSec) {
        try {
            this.wait(milliSec);
        } catch (InterruptedException e) {
            // continue
        }
    }

    public void start() {
        if (!this.running) {
            this.running = true;
            Thread thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        if (this.running) {
            this.running = false;
            if (this.genAlgo != null) {
                this.genAlgo.stop();
                if (this.genAlgo.getRacer() != null) {
                    this.genAlgo.getRacer().reset();
                }
            }
        }
    }

    public GeneticAlgorithm getGenAlgo() {
        return this.genAlgo;
    }

    public void setGenAlgo(SodaraceGeneticAlgorithm genAlgo) {
        this.genAlgo = genAlgo;
    }

    public String toInfoString() {
        StringBuffer buf = new StringBuffer();
        this.appendInfoBreeder(buf);
        return buf.toString();
    }

    private String maxFitness() {
        PopulationHistory popHistory = this.genAlgo.getPopHist();
        String result = "undefined";
        if (popHistory.size() > 0) {
            Population population = popHistory.getPopulation(0);
            //population.sort();
            int fitWodka = population.getIndividual(0).getFitness();
            result = "undefined (<=0)";
            if (fitWodka > 0) {
                result = Util.current().convertFitnessWodkaToSoda(fitWodka)
                        + "";
            }
        }
        return result;
    }

    public boolean isRunning() {
        return this.running;
    }

    public boolean canBeSaved() {
        return this.genAlgo != null;
    }

    public boolean canBeStarted() {
        return this.genAlgo != null && !this.running;
    }

    public boolean canBeStopped() {
        return this.genAlgo != null && this.running;
    }

    public int categoryCount() {
        int result = 1;
        if (this.genAlgo != null) {
            result = 1 + this.getGenAlgo().categoryCount();
        }
        return result;
    }

    public String getCategoryInfo(int index) {
        StringBuffer buffer = new StringBuffer();
        if (this.genAlgo == null) {
            getCategoryInfoNoGA(buffer);
        } else {
            getCategoryInfo(index, buffer);
        }
        return buffer.toString();
    }

    private void getCategoryInfo(int index, StringBuffer buffer) {
        if (index == 0) {
            this.appendInfoBreeder(buffer);
        } else {
            buffer.append(this.genAlgo.getCategoryInfo(index - 1));
        }
    }

    public String getCategoryName(int index) {
        String name = "Breeder";
        if (index != 0) {
            name = getGenAlgo().getCategoryName(index - 1);
        }
        return name;
    }

    private void getCategoryInfoNoGA(StringBuffer buffer) {
        buffer.append("Currently no genetic algorithm (GA) is defined.\n");
        buffer.append("Click file>new or file>load to create a GA.\n");
    }

    private void appendInfoBreeder(StringBuffer buf) {
        buf.append("State: ");
        if (this.running) {
            buf.append("running" + NEW_LINE);
        } else {
            buf.append("stopped" + NEW_LINE);
        }
        buf.append("Max fitness: " + maxFitness() + NEW_LINE);
        buf.append("Evaluation time: " + this.meanBuffer.mean() + " ms / generation"
                + NEW_LINE);
        buf.append("Generations count: " + this.genCount + NEW_LINE);
    }

}