/*
 * A terrain manager that creates always hilly terrains.
 */
package wodka.ga.terrain;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class AiTerrainManager extends AbstractSingleTerrainManager {

    private static final long serialVersionUID = 1L;

    protected String terrResourceName() {
        return "terrain/TerrainAi.xml";
    }

    public String getLabel() {
        return "AI Terrain";
    }

    public String getInfo() {
        return "A complex terrain, published in the soda ai forum.";
    }

    public void fromStream(DataInputStream s, int version) throws IOException {
        // Do nothing
    }

    public void toStream(DataOutputStream s) throws IOException {
        // Do nothing
    }

}
