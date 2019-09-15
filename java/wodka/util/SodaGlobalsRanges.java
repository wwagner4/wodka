/*
 * Created on Jan 31, 2004
 */
package wodka.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Ranges for soda models.
 * 
 * @author wwagner4
 */
public class SodaGlobalsRanges extends DoubleRanges {

    private static final long serialVersionUID = 1L;

    private static final String NEW_LINE = "\n";

    protected SodaGlobalsRanges() {
        super();
    }

    public static SodaGlobalsRanges createInstance() {
        SodaGlobalsRanges ranges = new SodaGlobalsRanges();
        ranges.init();
        return ranges;
    }

    protected Collection names() {
        Collection coll = new ArrayList();
        coll.add(SodaGlobals.FRICTION);
        coll.add(SodaGlobals.GRAVITY);
        coll.add(SodaGlobals.SPRINGYNESS);
        coll.add(SodaGlobals.AMPLITUDE);
        coll.add(SodaGlobals.PHASE);
        coll.add(SodaGlobals.SPEED);
        return coll;
    }

    protected DoubleRanges createNew() {
        return new SodaGlobalsRanges();
    }

    public String getLabel() {
        return "ranges for soda globals";
    }

    public String getInfo() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Environmenet:");
        buffer.append(NEW_LINE);
        buffer.append("Springyness: ");
        appendMinMax(buffer, SodaGlobals.SPRINGYNESS);
        buffer.append(", Gravity: ");
        appendMinMax(buffer, SodaGlobals.GRAVITY);
        buffer.append(", Friction: ");
        appendMinMax(buffer, SodaGlobals.FRICTION);
        buffer.append(NEW_LINE);
        buffer.append("Wave:");
        buffer.append(NEW_LINE);
        buffer.append("Amplitude: ");
        appendMinMax(buffer, SodaGlobals.AMPLITUDE);
        buffer.append(", Phase: ");
        appendMinMax(buffer, SodaGlobals.PHASE);
        buffer.append(", Speed: ");
        appendMinMax(buffer, SodaGlobals.SPEED);
        buffer.append(NEW_LINE);
        return buffer.toString();
    }

    private void appendMinMax(StringBuffer buffer, String string) {
        buffer.append(this.getMin(string));
        buffer.append(" - ");
        buffer.append(this.getMax(string));
    }

    protected Collection values(String name) {
        if (name.equals(SodaGlobals.SPRINGYNESS)) {
            Collection coll = new ArrayList();
            coll.add("0.0");
            coll.add("0.1");
            coll.add("0.2");
            coll.add("0.3");
            coll.add("0.4");
            coll.add("0.5");
            return coll;
        } else if (name.equals(SodaGlobals.GRAVITY)) {
            Collection coll = new ArrayList();
            coll.add("0.0");
            coll.add("0.5");
            coll.add("1.0");
            coll.add("1.5");
            coll.add("2.0");
            coll.add("2.5");
            coll.add("3.0");
            coll.add("3.5");
            coll.add("4.0");
            return coll;
        } else if (name.equals(SodaGlobals.SPEED)) {
            Collection coll = new ArrayList();
            coll.add("0.0");
            coll.add("0.01");
            coll.add("0.02");
            coll.add("0.03");
            coll.add("0.04");
            coll.add("0.05");
            coll.add("0.06");
            coll.add("0.07");
            coll.add("0.08");
            coll.add("0.09");
            coll.add("0.1");
            return coll;
        } else if (name.equals(SodaGlobals.PHASE)) {
            return defaultValues();
        } else if (name.equals(SodaGlobals.AMPLITUDE)) {
            return defaultValues();
        } else if (name.equals(SodaGlobals.FRICTION)) {
            return defaultValues();
        } else
            throw new Error("Undefined global constant name: " + name);
    }

}
