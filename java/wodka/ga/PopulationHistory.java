package wodka.ga;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * A Class that can manage the history of populations.
 */

public class PopulationHistory implements wodka.util.StreamPersistable {

    private static final long serialVersionUID = 1L;

    private List populations = new Vector();

    private transient wodka.util.Downsizer downsizer = new wodka.util.Downsizer(
            200, 10, 10);

    private static Logger logger = Logger.getLogger(PopulationHistory.class);

    public PopulationHistory() {
        super();
    }

    public Population getPopulation(int count) {
        return count >= populations.size() ? null : (Population) populations
                .get(count);
    }

    public void addPopulation(Population pop) {
        if (maxFitnessHasChanged(pop)) {
            populations.add(0, pop);
            downsizer.downsize(populations);
        }
    }

    private boolean maxFitnessHasChanged(Population pop) {
        if (populations.size() <= 0) {
            return true;
        }
        Population latestPop = (Population) populations.get(0);
        Individual indi = pop.getIndividual(0);
        Individual latestIndividual = latestPop.getIndividual(0);
        return indi.getFitness() != latestIndividual.getFitness();
    }

    public void removeAllPopulations() {
        logger
                .warn("used the method removeAllPopulations that might cause an error.");
        // changed from removeAllElements of a Vector to removeAll of a List.
        populations.removeAll(populations);
    }

    public int size() {
        return populations.size();
    }

    public void toStream(DataOutputStream outStream) throws IOException {
        if (populations == null) {
            wodka.util.StreamPersistor.stringToStream("null", outStream);
        } else {
            String className = populations.getClass().getName();
            wodka.util.StreamPersistor.stringToStream(className, outStream);
            outStream.writeInt(populations.size());
            Iterator iter = populations.iterator();
            while (iter.hasNext()) {
                Population pop = (Population) iter.next();
                if (pop == null) {
                    wodka.util.StreamPersistor
                            .stringToStream("null", outStream);
                } else {
                    String cName = pop.getClass().getName();
                    wodka.util.StreamPersistor.stringToStream(cName, outStream);
                    outStream.writeInt(pop.getVersion());
                    pop.toStreamLimited(outStream);
                }
            }
        }

    }

    public void fromStream(DataInputStream inStream, int version)
            throws IOException {
        populations = (Vector) wodka.util.StreamPersistor
                .collectionFromStream(inStream);
    }

    public int getVersion() {
        return 0;
    }

    public List getPopulations() {
        return populations;
    }

    public void setPopulations(List populations) {
        this.populations = populations;
    }

}