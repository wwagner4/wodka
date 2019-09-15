/*
 * Created on Jan 29, 2004
 */
package wodka.test;

import wodka.util.DoubleRanges;
import wodka.util.DoubleValues;

/**
 * An array of double values for testing crossover.
 * 
 * @author wwagner4
 */
public class DoubleTestValuesArray extends DoubleValues {

    private static final long serialVersionUID = 1L;

    private DoubleTestValuesArray(DoubleRanges ranges) {
        super(ranges);
    }

    protected void initDefaultValues() {
        for (int i = 0; i < 30; i++) {
            defineValueWithDefault(i + "", 0.5);
        }
    }

    protected String getRangeKey(String name) {
        return "b";
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < values.size(); i++) {
            String name = this.getName(i);
            if (i > 0)
                sb.append('|');
            sb.append(getValue(name));
        }
        return sb.toString();
    }

    protected DoubleValues createInstance() {
        DoubleTestValuesArray inst = new DoubleTestValuesArray(this.ranges);
        inst.initDefaultValues();
        return inst;
    }

    public static DoubleValues createNewInstance(DoubleRanges ranges) {
        DoubleTestValuesArray inst = new DoubleTestValuesArray(ranges);
        inst.initDefaultValues();
        return inst;
    }

}
