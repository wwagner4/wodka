/*
 * Created on 20.04.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package wodka.ga.soda;

import wodka.WodkaException;
import wodka.ga.GeneticAlgorithm;
import wodka.util.StreamPersistable;
import wwan.commons.param.Informative;

/**
 * The manager makes terrains available for GAs
 */
public interface TerrainManager extends Informative, StreamPersistable {
	
    String getTerrainXml(GeneticAlgorithm genAlgo) throws WodkaException;

}
