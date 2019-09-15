/*
 * CraterFunction.java
 *
 * Created on December 8, 2002, 5:29 PM
 */

package wodka.util;

/**
 *
 * @author  wolfi
 */
public class CraterFunction {
    
    private PeakFunction peak = null;
    private int x, y;
    
    public CraterFunction(int elevation, int x, int y, int radius, int height, int width) {
        this.peak = new PeakFunction(elevation, radius, height, width);
        this.x = x;
        this.y = y;
    }
    public int calculate(int px, int py) {
        double dx = px - this.x;
        double dy = py - this.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        return peak.calculate((int)distance);
    }
}
