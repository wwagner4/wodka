package wodka.util;

/**
 * Performs linear transformations of 2D Koordinates.
 */

public class LinearTransform {

    double t1x, t2x, t1y, t2y;
    int offset = 0;

    /**
     * @param aMax
     *            World koordinates maximal x value.
     * @param bMax
     *            World koordinates maximal y value.
     * @param w
     *            Panel maximal x value.
     * @param h
     *            Panel maximal y value.
     * @param kr
     *            Percentage of border.
     * @param offset
     *            Offset in x and y.
     */
    public LinearTransform(int aMax, int bMax, int w, int h, double kr, int offset) {
        if ((double) aMax / bMax >= (double) w / h) {
            double kw = w * kr / 100;
            double w1 = w - 2 * kw;
            double h1 = w1 * bMax / aMax;
            double kh = (h - h1) / 2;
            t1x = w1 / aMax;
            t2x = kw + 0.5;
            t1y = h1 / bMax;
            t2y = kh + 0.5;
        } else {
            double kh = h * kr / 100;
            double h1 = h - 2 * kh;
            double w1 = h1 * aMax / bMax;
            double kw = (w - w1) / 2;
            t1x = w1 / aMax;
            t2x = kw + 0.5;
            t1y = h1 / bMax;
            t2y = kh + 0.5;
        }
        this.offset = offset;
    }
    public int transX(int a) {
        return (int) (a * t1x + t2x) + offset;
    }
    public int transY(int b) {
        return (int) (b * t1y + t2y) + offset;
    }
}