package wodka.ga.terrain;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import wodka.WodkaException;
import wodka.ga.GeneticAlgorithm;
import wodka.ga.soda.TerrainManager;

/**
 * Superclass for incremental terrain managers.
 */
public abstract class AbstractIncrementalTerrainManager implements TerrainManager {

    protected int level = 0;
    private static Logger logger = Logger.getLogger(AbstractIncrementalTerrainManager.class);

    public String getTerrainXml(GeneticAlgorithm genAlgo) throws WodkaException {
        try {
            double fit = genAlgo.getPrevMaxFitSoda();
            if (this.level < maxLevel() && fit < this.levelIncFitness()) {
                this.level++;
                genAlgo.resetTimeoutOptimization();
                logger.info("Increased level to: " + this.level + ".");
            }
            return racer.Util.current().resourceToString(terrResourceName(this.level));
        } catch (IOException e) {
            throw new WodkaException(e);
        }
    }

    protected abstract String terrResourceName(int pLevel);

    protected abstract int maxLevel();

    public void fromStream(DataInputStream s, int version) throws IOException {
        this.level = s.readInt();
    }

    public int getVersion() {
        return 0;
    }

    public void toStream(DataOutputStream s) throws IOException {
        s.writeInt(this.level);
    }

    protected double levelIncFitness() {
        return 3000.0;
    }

    protected String getInfoBasic() {
        return "Incremental terrain manager that increases the"
                + " difficulty of the terrain if the max fittness is than " + levelIncFitness() + ".\n"
                + "The current level is " + this.level + " of " + this.maxLevel() + " possible levels.";
    }

}