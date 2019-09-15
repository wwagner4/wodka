/*
 * Created on Feb 1, 2004
 */
package wodka.test;

import wodka.ApplicationManager;
import wodka.WodkaException;
import wodka.ga.model.Model;
import wodka.ga.racer.soda.SodaRacer;
import wodka.ga.soda.Racer;
import wodka.ga.soda.RacerListener;

/**
 * Tests the ranges of global parameters for soda models.
 * 
 * @author wwagner4
 */
public class RangesTester implements RacerListener {

    public void finishedAllRace() {
        info ("finishedAllRace");
    }

    public void finishedRace(int fitness, int id) {
        info ("finishedRace fitness:"+fitness+" id:"+id);
    }

    public RangesTester() {
        super();
    }

    public static void main(String[] args) {
        RangesTester rt = new RangesTester();
        try {
            ApplicationManager.current().initLogging();
            rt.test();
        } catch (WodkaException e) {
            e.printStackTrace();
        }
    }

    private void test() throws WodkaException {
        testAmplitude(100);
    }

    private void testAmplitude(double val) throws WodkaException {
        Model model = new Model();
        setGlobalsToSaveValues(model);
        model.setWaveAplitude(val);
        testModel(model);
        info("testAmplitude started race val:" + val);
        
    }

    private void testModel(Model model) throws WodkaException {
        Racer racer = new SodaRacer();
        racer.setRacerListener(this);
        racer.addModel(model, 0, null, 10000);
        racer.finishedAdding();
    }

    private void setGlobalsToSaveValues(Model model) {
        model.setEnvFriction(0.5);
        model.setEnvGravity(0.5);
        model.setEnvGravity(0.5);
        model.setWaveAplitude(0.5);
        model.setWaveSpeed(0.5);
    }

    private void info(String msg) {
        System.out.println(this.getClass().getName() + ">>" + msg);
    }
}
