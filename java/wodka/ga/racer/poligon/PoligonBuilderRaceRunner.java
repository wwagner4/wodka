package wodka.ga.racer.poligon;

import java.util.*;

import wodka.ga.racer.RaceRunner;
import wodka.ga.racer.RaceRunnerErrorHandler;
import wodka.ga.racer.RaceRunnerListener;
import wodka.ga.model.*;

/**
 * Implementation of a soda race.
 */

public abstract class PoligonBuilderRaceRunner implements RaceRunner {

    private wodka.util.PeakFunction lengthPeak = new wodka.util.PeakFunction(sideLength(), 100, 100);
    private wodka.util.CraterFunction crater = new wodka.util.CraterFunction(0, 125, 125, 71, 300, 30);
    private Model model = null;
    private RaceRunnerListener raceRunnerListener = null;
    private int id = -1;

    public PoligonBuilderRaceRunner() {
        super();
    }

    public void setErrHandler(RaceRunnerErrorHandler errHandler) {
        // Do nothing. Errorhandler not relevant for that racer.
    }

    public void setTimeout(int timeout) {
        // Do nothing. Timeout not relevant for that racer.
    }

    public void run() {
        int fit = 0;
        try {
            fit += lengthFit(model);
            fit += positionFit(model);
            fit += numberMassesFit(model);
            fit += numberMusclesFit(model);
            fit += minDistanceFit(model);
        } catch (Throwable e) {
            e.printStackTrace();
            fit = 0;
        } finally {
            raceRunnerListener.finishedRunning(fit, id);
        }
    }

    private int minDistanceFit(Model pModel) {
        List m = pModel.getMasses();
        int minDist = 100000;
        for (int i = 0; i < m.size(); i++) {
            for (int k = i + 1; k < m.size(); k++) {
                int dist = massDistance((Mass) m.get(i), (Mass) m.get(k));
                if (minDist >= dist)
                    minDist = dist;
            }
        }
        return this.lengthPeak.calculate(minDist);
    }

    private int positionFit(Model pModel) {
        double fit = 0;
        Iterator iter = pModel.getMasses().iterator();
        int count = 0;
        while (iter.hasNext()) {
            Mass m = (Mass) iter.next();
            fit += crater.calculate(m.getXPos(), m.getYPos());
            count++;
        }
        return (int) (fit / count);
    }

    private int lengthFit(Model pModel) {
        double fit = 0.0;
        Iterator iter = pModel.getMuscles().iterator();
        int count = 0;
        while (iter.hasNext()) {
            int len = length((Muscle) iter.next());
            fit += lengthPeak.calculate(len);
            count++;
        }
        return (int) (fit / count);
    }

    private int numberMassesFit(Model pModel) {
        return fitnesForNumber(pModel.getMasses().size());
    }

    private int numberMusclesFit(Model pModel) {
        return fitnesForNumber(pModel.getMuscles().size());
    }

    abstract protected int fitnesForNumber(int num);

    abstract protected int sideLength();

    private int length(Muscle musc) {
        Mass m1 = musc.getFromMass();
        Mass m2 = musc.getToMass();
        return massDistance(m1, m2);
    }

    private int massDistance(Mass m1, Mass m2) {
        double dx = m1.getXPos() - m2.getXPos();
        double dy = m1.getYPos() - m2.getYPos();
        return (int) Math.sqrt(dx * dx + dy * dy);
    }

    public void setId(int i) {
        id = i;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setRaceRunnerListener(RaceRunnerListener rrl) {
        this.raceRunnerListener = rrl;
    }

    public void setTerrainXml(String terrain) {
        // Do nothing
    }

}