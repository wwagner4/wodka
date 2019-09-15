/*
 * A single terrain manager prepairing a single flat terrain.
 */
package wodka.ga.terrain;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FlatTerrainManager extends AbstractSingleTerrainManager {

    private static final long serialVersionUID = 1L;

    protected String terrResourceName() {
        return "terrain/TerrainFlat.xml";
    }

    public String getLabel() {
        return "Flat Terrain";
    }

    public String getInfo() {
        return "A terrain manager that creates always flat terrains.";
    }

    public void fromStream(DataInputStream s, int version) throws IOException {
        // Do nothing
    }

    public void toStream(DataOutputStream s) throws IOException {
        // Do nothing
    }

}