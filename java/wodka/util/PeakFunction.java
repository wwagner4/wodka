package wodka.util;

/**
 * A class that can calculate the values of a peak function. The peak
 * is defined by its xValue, its height and the width of its base.
 * It may also have a certain elevation that moves the whole peak 
 * along the y-axe. The width is the distance from the xValue to the
 * point where the peak function reaches its lowest value.
 */

public class PeakFunction {

  private int x1, x2, x3, elevation;
  private double a1, a2, k1, k2;

  public PeakFunction(int xValue, int height, int width) {
      this(0, xValue, height, width);
  }
  public PeakFunction(int elevation, int xValue, int height, int width) {
    this.elevation = elevation;
    x1 = xValue-width;
    x2 = xValue;
    x3 = xValue+width;
    a1 = (((double)((width - xValue)*height))/width)+elevation;
    k1 = ((double)height)/width;
    a2 = (((double)((width + xValue)*height))/width)+elevation;
    k2 = ((double)-height)/width;
  }
  public int calculate(int x){
    if      (x < x1) {
      return elevation;
    }
    else if (x < x2){
      return (int)Math.round(k1*x+a1);
    }
    else if (x < x3){
      return (int)Math.round(k2*x+a2);
    }
    else{
      return elevation;
    }
  }
}