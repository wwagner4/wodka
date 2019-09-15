/*
 * Created on Feb 17, 2004
 */
package wodka.ga.fitfunc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import wodka.ga.FitnessFunction;
import wodka.ga.model.Model;
import wodka.util.StreamPersistable;
import wwan.commons.param.Informative;

/**
 * A fitness function that concerns only about racer fitness.
 */
public class NullFitnessFunction implements FitnessFunction, Informative, StreamPersistable {

    private static final long serialVersionUID = 1L;

    public NullFitnessFunction() {
        super();
    }

    public int calculate(Model model, int racerFitness) {
        return racerFitness;
    }

    public String getLabel() {
        return "Null Fitness Function";
    }

    public String getInfo() {
        return "A fitness function that returns the fitness from the racer " +
        "without considering the model." +
        "\nIn the future more complex fitness functions can (should) be implemented, that " +
        "do not only consider the racer fitness but any other aspect " +
        "concerning the structur and parameters of the model.";
    }

    public void fromStream(DataInputStream s, int version) throws IOException {
        // Nothing to be done because no attrs.
    }

    public int getVersion() {
        return 0;
    }

    public void toStream(DataOutputStream s) throws IOException {
        // Nothing to be done because no attrs.
    }

}
