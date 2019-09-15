package wodka.test;

import wodka.ga.racer.mc.GridFunction;
import wodka.ga.racer.mc.Log;
import wodka.ga.racer.mc.*;

class ModelCompareRaceRunnerX extends ModelCompareRaceRunner {

    protected ModelCompareRaceRunnerX() {
        super();
    }

    public String getName() {
        throw new UnsupportedOperationException();
    }

    protected GridFunction getGridFunction() {
        return new Log();
    }

    protected ModelHolder getModelHolder() {
        return new XHolder();
    }
}