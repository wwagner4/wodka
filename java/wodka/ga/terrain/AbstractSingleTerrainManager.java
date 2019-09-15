/*
 * Superclass for single terrain TerrainManagers.
 */
package wodka.ga.terrain;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import wodka.WodkaException;
import wodka.ga.GeneticAlgorithm;
import wodka.ga.soda.TerrainManager;


public abstract class AbstractSingleTerrainManager implements TerrainManager {

	protected abstract String terrResourceName();

    public String getTerrainXml(GeneticAlgorithm genAlgo) throws WodkaException {
        try {
            return racer.Util.current().resourceToString(
                terrResourceName());
        } catch (IOException e) {
            throw new WodkaException(e);
        }
    }
	
    public abstract void fromStream(DataInputStream s, int version) throws IOException;

    public int getVersion() {
        return 0;
    }

    public abstract void toStream(DataOutputStream s) throws IOException;

}
