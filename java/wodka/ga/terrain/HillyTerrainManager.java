/*
 * A terrain manager that creates always hilly terrains.
 */
package wodka.ga.terrain;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class HillyTerrainManager extends AbstractSingleTerrainManager {

    private static final long serialVersionUID = 1L;

    protected String terrResourceName() {
        return "terrain/TerrainHilly.xml";
    }

    public String getLabel() {
        return "Hilly Terrain";
    }

    public String getInfo() {
        return "A terrain manager that creates always hilly terrains.";
    }

    public void fromStream(DataInputStream s, int version) throws IOException {
        // Do nothing
    }

    public void toStream(DataOutputStream s) throws IOException {
        // Do nothing
    }

}