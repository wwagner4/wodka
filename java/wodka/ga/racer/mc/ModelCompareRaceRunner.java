package wodka.ga.racer.mc;

import wodka.ga.model.*;
import wodka.ga.racer.RaceRunner;
import wodka.ga.racer.RaceRunnerErrorHandler;
import wodka.ga.racer.RaceRunnerListener;

import java.util.*;

public abstract class ModelCompareRaceRunner implements RaceRunner {

    private static final int MAX = 8;
    private List[] originalConnections = new List[MAX];
    private GridFunction gridFunction = getGridFunction();
    private ModelHolder modelHolder = getModelHolder();
    private Model model = null;
    private RaceRunnerListener raceRunnerListener = null;
    private int id = -1;

    public void setTimeout(int timeout) {
        // Do nothing. Timeout not relevant for that racer.
    }

    protected abstract GridFunction getGridFunction();
    protected abstract ModelHolder getModelHolder();

    public void setErrHandler(RaceRunnerErrorHandler errHandler) {
        // Do nothing
    }

    public int compare(Model m) {
        int value = 0;
        int sum = 0;
        for (int i = 0; i < MAX; i++) {
            int currentValue = compare(m, i);
            sum += value + i * 100;
            if (currentValue == 0) {
                return sum;
            }
            value = currentValue;
        }
        return sum;
    }
    private int compare(Model m, int level) {
        List conns = connections(m, level);
        int total = conns.size() + getOriginalConnections(level).size();
        Iterator iter = getOriginalConnections(level).iterator();
        int count = 0;
        while (iter.hasNext()) {
            Connection c = (Connection) iter.next();
            if (conns.contains(c)) {
                count += 2;
                conns.remove(c);
            }
        }
        return count * 100 / total;
    }
    private List connections(Model m, int level) {
        List conns = new ArrayList();
        Map gridIndexes = massesGridIndexes(m, level);
        Iterator miter = m.getMuscles().iterator();
        while (miter.hasNext()) {
            Muscle musc = (Muscle) miter.next();
            Integer fromGridIndex = (Integer) gridIndexes.get(musc.getFromMass().getIdentifier());
            Integer toGridIndex = (Integer) gridIndexes.get(musc.getToMass().getIdentifier());
            Connection conn = new Connection(fromGridIndex.intValue(), toGridIndex.intValue());
            conns.add(conn);
        }
        return conns;
    }
    private List getOriginalConnections(int level) {
        if (originalConnections[level] == null) {
            originalConnections[level] = connections(getOriginalModel(), level);
        }
        return originalConnections[level];
    }

    private Model getOriginalModel() {
        return modelHolder.getModel();
    }
    private Map massesGridIndexes(Model m, int level) {
        Map masses = new HashMap();
        for (int i = 0; i < m.getMassCount(); i++) {
            Mass mass = m.getMass(i);
            int index = gridIndex(mass.getXPos(), mass.getYPos(), level);
            masses.put(mass.getIdentifier(), new Integer(index));
        }
        return masses;
    }
    private int gridIndex(int x, int y, int level) {
        int count = gridFunction.gridCount(level);
        int dx = Model.WIDTH / count;
        int dy = Model.WIDTH / count;
        int indexY = y / dy;
        return x / dx + indexY * count;
    }

    public void run() {
        int fitness = 0;
        try {
            fitness = compare(this.model);
        } finally {
            this.raceRunnerListener.finishedRunning(fitness, id);
        }
    }

    protected void setGridFunction(GridFunction gridFunction) {
        this.gridFunction = gridFunction;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setModel(Model m) {
        this.model = m;
    }

    public void setRaceRunnerListener(RaceRunnerListener rrl) {
        this.raceRunnerListener = rrl;
    }

    public void setTerrainXml(String terrain) {
        // Do nothing
    }

}
