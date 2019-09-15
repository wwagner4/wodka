/*
 * GridLayoutAlignment.java
 *
 * Created on February 21, 2003, 9:53 PM
 */

package wodka.util;

import java.awt.*;

/**
 *
 * @author  wolfi
 */
public class GridLayoutAlignment {
    
    /** Creates a new instance of GridLayoutAlignment */
    public GridLayoutAlignment() {
        super();
    }
    
    public Dimension align(int count) {
        return new Dimension(calcRows(count), calcCols(count));
    }
    private int calcRows(int count){
        return (int)Math.ceil(Math.sqrt(count));
    }
    private int calcCols(int count){
        return (int)Math.round(Math.sqrt(count));
    }
        
        
        
    
}
