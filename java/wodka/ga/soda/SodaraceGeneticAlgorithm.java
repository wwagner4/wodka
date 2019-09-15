package wodka.ga.soda;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import org.apache.log4j.Logger;

import wodka.ExceptionHandler;
import wodka.WodkaException;
import wodka.ga.GeneticAlgorithm;
import wodka.ga.Individual;
import wodka.ga.terrain.FlatTerrainManager;
import wodka.util.StreamPersistor;
import wwan.commons.param.ListParam;
import wwan.commons.param.ParamException;

public class SodaraceGeneticAlgorithm extends GeneticAlgorithm implements
        RacerListener {

    private static Logger logger = Logger
            .getLogger(SodaraceGeneticAlgorithm.class);

    private static final long serialVersionUID = 1L;

    private transient boolean finishedAllRace = false;

    private Racer racer = null;

    private TerrainManager terrManager = null;

    public SodaraceGeneticAlgorithm() {
        super();
    }

    public void evaluateOneGeneration() throws WodkaException {
        this.stopped = false;
        this.finishedAllRace = false;
        this.racer.setTerrainXml(this.terrManager.getTerrainXml(this));
        int timeout = this.calcTimeout();
        for (int i = 0; i < this.population.size() && !this.stopped; i++) {
            Individual indi = this.population.getIndividual(i);
            SodaraceProgram pgm = (SodaraceProgram) indi.getGeno();
            this.racer.addModel(pgm.eval(), i, this, timeout);
        }
        this.racer.finishedAdding();
        int count = 0;
        int pauseTime = 5;
        while (!this.finishedAllRace) {
            if (count >= 200000) {
                throw new WodkaException(
                        "Timeout in evaluation of one generation.\nDid not finish after "
                                + (count * pauseTime) / 10000 + " seconds\n"
                                + this.racer.getClass().getName());
            }
            pause(pauseTime);
            count++;
        }
        logger.info("finished one generation. count=" + this.getPopCount()
                + ". fitness=" + this.getPrevMaxFitSoda());
        createNextPopulation();
        this.changed = true;
    }

    public Racer getRacer() {
        return this.racer;
    }

    public void setRacer(Racer racer) {
        this.racer = racer;
        if (this.handler != null) {
            racer.setExceptionHandler(this.handler);
        }
        this.racer.setRacerListener(this);
    }

    public void setHandler(ExceptionHandler handler) {
        super.setHandler(handler);
        if (this.racer != null) {
            this.racer.setExceptionHandler(handler);
        }
    }

    public void finishedAllRace() {
        this.finishedAllRace = true;
    }

    public synchronized void finishedRace(int racerFitness, int identifier) {
        Individual individual = this.population.getIndividual(identifier);
        if (individual != null) {
            SodaraceProgram pgm = (SodaraceProgram) individual.getGeno();
            individual.setFitness(this.getFitFunc().calculate(
                    pgm.eval(), racerFitness));
        }
    }

    public void toStream(DataOutputStream outStream) throws java.io.IOException {
        super.toStream(outStream);
        wodka.util.StreamPersistor.toStream(this.racer, outStream);
        wodka.util.StreamPersistor.toStream(this.terrManager, outStream);
    }

    public void fromStream(DataInputStream inStream, int version)
            throws java.io.IOException {
        super.fromStream(inStream, version);
        this.racer = (Racer) StreamPersistor.fromStream(inStream);
        if (this.racer != null) {
            this.racer.setRacerListener(this);
        }
        if (version > 4) {
            this.terrManager = (TerrainManager) StreamPersistor
                    .fromStream(inStream);
        } else {
            this.terrManager = new FlatTerrainManager();
        }
    }

    public void setFieldsFromParameterList(ListParam listParam)
            throws ParamException {
        super.setFieldsFromParameterList(listParam);
        this.racer = (Racer) listParam.getParamValue("racer");
        this.racer.setRacerListener(this);
        this.terrManager = (TerrainManager) listParam
                .getParamValue("terrManager");
    }

    public void setParameterListFromFields(ListParam param)
            throws ParamException {
        super.setParameterListFromFields(param);
        param.setParamValue("racer", this.racer);
        param.setParamValue("terrManager", this.terrManager);
    }

    public void setTerrManager(TerrainManager terrManager) {
        this.terrManager = terrManager;
    }

    public TerrainManager getTerrManager() {
        return this.terrManager;
    }

    private void appendInfoTerrainManager(StringBuffer buf) {
        TerrainManager terrMan = getTerrManager();
        if (terrMan != null) {
            buf.append("Terrain Manager : " + terrMan.getLabel() + NEW_LINE);
            buf.append(terrMan.getInfo());
        }
    }

    private void appendInfoRacer(StringBuffer buf) {
        Racer rac = getRacer();
        if (rac != null) {
            buf.append("Racer : " + rac.getLabel() + NEW_LINE);
            buf.append(rac.getInfo());
        }
    }

    protected void appendCategoryInfo(int index, StringBuffer buffer) throws Error {
        super.appendCategoryInfo(index, buffer);
        switch (index) {
        case 3:
            this.appendInfoRacer(buffer);
            break;
        case 5:
            this.appendInfoTerrainManager(buffer);
            break;
        }
    }

    public String getCategoryName(int index) {
        String re = super.getCategoryInfo(index);
        if (re.equals("")) {
            switch (index) {
            case 3:
                re = "Racer";
            case 5:
                re = "Terrain Manager";
            default:
                re = "";
            }
        }
        return re;
    }

    protected void appendInfoGeneral(StringBuffer buf) {
        super.appendInfoGeneral(buf);
        Racer rac = getRacer();
        if (rac == null) {
            buf.append("Racer : " + "null" + NEW_LINE);
        } else {
            buf.append("Racer : " + rac.getLabel() + NEW_LINE);
        }
        TerrainManager terrMan = getTerrManager();
        if (terrMan == null) {
            buf.append("Terrain Manager : " + "null" + NEW_LINE);
        } else {
            buf.append("Terrain Manager : " + terrMan.getLabel() + NEW_LINE);
        }
    }

}
