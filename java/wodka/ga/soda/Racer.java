package wodka.ga.soda;

import wodka.ExceptionHandler;
import wodka.WodkaException;
import wodka.ga.GeneticAlgorithm;
import wodka.ga.model.Model;
import wodka.util.StreamPersistable;
import wwan.commons.param.Configurable;

/**
 * Reprecents a soda race
 */

public interface Racer extends Configurable, StreamPersistable {

    void addModel(Model model, int identifier, GeneticAlgorithm genAlgo, int timeout)
        throws WodkaException;
    void setTerrainXml(String terrainXml) throws WodkaException;
    void finishedAdding();
    void setRacerListener(RacerListener listener);
    String getInfo();
    String getLabel();
    void setExceptionHandler(ExceptionHandler handler);
    void reset();
}