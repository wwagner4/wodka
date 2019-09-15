/*
 * Created on 10.11.2003
 *
 */
package wodka.ga.soda;

/**
 * 
 */
public interface RacerListener {
	
	void finishedRace(int fitness, int identifier);
	void finishedAllRace();

}
