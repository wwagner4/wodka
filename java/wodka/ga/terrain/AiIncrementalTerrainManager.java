/*
 * Incremental Terrain Manager for the compley AI Terrain published in the soda
 * AI forum.
 */
package wodka.ga.terrain;

import org.apache.log4j.Logger;

public class AiIncrementalTerrainManager extends
        AbstractIncrementalTerrainManager {

    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger
            .getLogger(AiIncrementalTerrainManager.class);

    protected String terrResourceName(int lev) {
        if (logger.isDebugEnabled()) {
            logger.debug("Terrain level: " + lev);
        }
        switch (lev) {
        case 0:
            return "terrain/race00.xml";
        case 1:
            return "terrain/race01.xml";
        case 2:
            return "terrain/race02.xml";
        case 3:
            return "terrain/race03.xml";
        case 4:
            return "terrain/race04.xml";
        case 5:
            return "terrain/race05.xml";
        case 6:
            return "terrain/race06.xml";
        case 7:
            return "terrain/race07.xml";
        case 8:
            return "terrain/race08.xml";
        case 9:
            return "terrain/race09.xml";
        case 10:
            return "terrain/race10.xml";
        default:
            throw new Error("Invalid level: " + lev);
        }
    }

    protected int maxLevel() {
        return 10;
    }

    public String getLabel() {
        return "AI Incremantal Terrain";
    }

    public String getInfo() {
        return "The AI Terrain from the sodarace forum.\n"
                + this.getInfoBasic();
    }
}