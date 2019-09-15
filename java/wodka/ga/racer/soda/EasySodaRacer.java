/*
 * Created on Mar 27, 2004
 */
package wodka.ga.racer.soda;

import wodka.ga.GeneticAlgorithm;
import wodka.ga.model.Model;
import wodka.ga.racer.AbstractRacer;
import wodka.ga.racer.RaceRunner;

/**
 * Runs a model using EasySoda. No racer GUI is needed.
 * 
 */
public class EasySodaRacer extends AbstractRacer {

    private static final long serialVersionUID = 1L;

    private transient int timeout = -1;

    private boolean optimizeTimeout = true;

    private String terrainXml = null;

    public EasySodaRacer() {
        super();
    }

    public boolean isOptimizeTimeout() {
        return optimizeTimeout;
    }

    public void setOptimizeTimeout(boolean optimizeTimeout) {
        this.optimizeTimeout = optimizeTimeout;
    }

    public synchronized void addModel(Model model, int id,
            GeneticAlgorithm genAlgo, int pTimeout) {
        if (this.optimizeTimeout) {
            this.timeout = pTimeout;
        } else {
            this.timeout = -1;
        }
        super.addModel(model, id, genAlgo, pTimeout);
    }

    public RaceRunner createRaceRunner() {
        EasySodaRaceRunner runner = new EasySodaRaceRunner();
        runner.setTimeout(timeout);
        runner.setTerrainXml(this.terrainXml);
        return runner;
    }

    public String getInfo() {
        return "Runs sodaraces without the original sodarace application."
                + "\nSets the timout value according to the fastest fastest model"
                + "of the previous generation.";
    }

    public String getLabel() {
        return "EasySodaRacer";
    }

    public void setTerrainXml(String terrainXml) {
        this.terrainXml = terrainXml;
    }

    // private void info(String msg) {
    // System.out.println(this.getClass().getName() + ">>" + msg);
    // }

}
