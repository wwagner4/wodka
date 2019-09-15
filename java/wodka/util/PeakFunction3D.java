/*
 * PeakFunction3D.java
 *
 * Created on 04. Dezember 2002, 13:14
 */

package wodka.util;

/**
 * 
 * @author wwagner4
 */
public class PeakFunction3D {

    private wodka.util.PeakFunction vertical = null;

    private wodka.util.PeakFunction horizontal = null;

    private int dx;

    private int dy;

    /** Creates a new instance of PeakFunction3D */
    public PeakFunction3D(int elevation, int dx, int dy, int height, int base) {
        vertical = new wodka.util.PeakFunction(elevation, dy, height, base);
        horizontal = new wodka.util.PeakFunction(elevation, dx, height, base);
        this.dx = dx;
        this.dy = dy;
    }

    public int calculate(int x, int y) {
        int absX = Math.abs(x - dx);
        int absY = Math.abs(y - dy);
        int result = -1;
        if (absX > absY) {
            result = horizontal.calculate(x);
        } else {
            result = vertical.calculate(y);
        }
        return result;
    }

}