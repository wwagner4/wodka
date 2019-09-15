/**
 * $Revision: 1.4 $ $Author: wwan $ $Date: 2005/06/20 20:52:21 $ 
 */

package wodka.ga.terrain;

/**
 * A Terrain manager that incremental builds a hill with four masses only.
 */
public class EasyIncTerrainManager extends AbstractIncrementalTerrainManager {

    private static final long serialVersionUID = 1L;

    protected String terrResourceName(int pLevel) {
        switch (pLevel) {
        case 0:
            return "terrain/inc1_0.xml";
        case 1:
            return "terrain/inc1_1.xml";
        case 2:
            return "terrain/inc1_2.xml";
        case 3:
            return "terrain/inc1_3.xml";
        case 4:
            return "terrain/inc1_4.xml";
        default:
            throw new Error("Invalid level: " + pLevel);
        }
    }

    protected int maxLevel() {
        return 4;
    }

    public String getLabel() {
        return "Easy Incremental Terrains";
    }

    public String getInfo() {
        return "Easy incremental terrains with one hill in the middle that is incremental growing.\n"
                + this.getInfoBasic();
    }

}
