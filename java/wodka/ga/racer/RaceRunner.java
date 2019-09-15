/*
 * Created on 11.11.2003
 *
 */
package wodka.ga.racer;

import wodka.ga.model.Model;

public interface RaceRunner extends Runnable {
	
	public void setModel(Model m);
	public void setId(int id);
    public void setTimeout(int timeout);
    public void setTerrainXml(String terrain);
	public void setRaceRunnerListener(RaceRunnerListener rrl);
    public void setErrHandler(RaceRunnerErrorHandler errHandler);

}
