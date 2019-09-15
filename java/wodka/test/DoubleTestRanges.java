/*
 * Created on Jan 28, 2004
 */
package wodka.test;

import java.util.ArrayList;
import java.util.Collection;

import wodka.util.DoubleRanges;

/**
 * Testimplementation of a DoubleRanges.
 * 
 * @author wwagner4
 */
public class DoubleTestRanges extends DoubleRanges {

    private static final long serialVersionUID = 1L;

    private DoubleTestRanges() {
        super();
    }

    public static DoubleTestRanges createInstance() {
        DoubleTestRanges ranges = new DoubleTestRanges();
        ranges.init();
        return ranges;
    }

    protected Collection names() {
        Collection c = new ArrayList();
        c.add("a");
        c.add("b");
        return c;
    }

    /**
     * Creates a new DoubleRange. This new instance is NOT a copy of this
     * DoubleRange.
     * 
     * @see wodka.util.DoubleRanges#createNew()
     */
    protected DoubleRanges createNew() {
        return new DoubleTestRanges();
    }

    /**
     * @see wodka.param.Informative#getInfo()
     */
    public String getInfo() {
        return "Testranges.";
    }

    /**
     * @see wodka.param.Informative#getLabel()
     */
    public String getLabel() {
        return "Testranges";
    }

    protected Collection values(String name) {
        Collection result = null;
        if (name.equals("a")) {
            Collection c = new ArrayList();
            c.add("1.0");
            c.add("1.2");
            c.add("1.4");
            c.add("1.6");
            c.add("1.8");
            c.add("2.0");
            result = c;
        } else {
            result = defaultValues();
        }
        return result;
    }

}
