/*
 * Created on Jan 29, 2004
 */
package wodka.test;

import wodka.util.DoubleRanges;
import wodka.util.DoubleValues;

/**
 * A list of double values named x, y, z.
 * 
 * @author wwagner4
 */
public class DoubleTestValues extends DoubleValues {

    private static final long serialVersionUID = 1L;

    private DoubleTestValues(DoubleRanges ranges) {
        super(ranges);
    }

    public static DoubleTestValues createNewInstance(DoubleRanges ranges) {
        DoubleTestValues values = new DoubleTestValues(ranges);
        values.initDefaultValues();
        return values;
    }

    protected void initDefaultValues() {
        defineValueWithDefault("x", 1.5);
        defineValueWithDefault("y", 0.5);
        defineValueWithDefault("z", 0.7);
    }

    protected String getRangeKey(String name) {
        if (name.equals("x"))
            return "a";
        else if (name.equals("y"))
            return "b";
        else if (name.equals("z"))
            return "b";
        else
            throw new Error("No range key mapping is defined for -" + name
                    + "-");
    }

    public String toString() {
        return getValue("x") + "|" + getValue("y") + "|" + getValue("z");
    }

    protected DoubleValues createInstance() {
        return new DoubleTestValues(ranges);
    }

}
