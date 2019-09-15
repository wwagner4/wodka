/*
 * Created on Jan 23, 2004
 *  
 */
package wodka.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import wwan.commons.param.Configurable;
import wwan.commons.param.ListParam;
import wwan.commons.param.ListParamDesc;
import wwan.commons.param.ParamException;
import wwan.commons.param.enums.EnumParamDesc;
import wwan.commons.param.enums.StringPopulator;

/**
 * Describes the attribute of a DoubleValues.
 * 
 * @author wwagner4
 */
public abstract class DoubleRanges implements Configurable, StreamPersistable {

    Map ranges = new TreeMap();

    protected DoubleRanges() {
        super();
    }

    protected void init() {
        Iterator iter = this.names().iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            LinkedList ll = new LinkedList(values(name));
            putRange(name, (String) ll.getFirst(), (String) ll.getLast());
        }
    }

    protected abstract Collection names();

    private void putRange(String name, String min, String max) {
        this.ranges.put(name, new DoubleRange(name, Double.parseDouble(min),
                Double.parseDouble(max)));
    }

    private void putRange(String name, DoubleRange range) {
        if (!name.equals(range.getName()))
            throw new Error("name not equal to name in range.\n  name: '"
                    + name + "' name in range: '" + range.getName() + "'");
        this.ranges.put(name, range);
    }

    private class DoubleRange implements Cloneable {

        String name = null;

        double min;

        double max;

        public String toString() {
            return this.name + "[" + min + "|" + max + "]";
        }

        public int getVersion() {
            return 0;
        }

        public DoubleRange() {
            super();
        }

        private DoubleRange(String name, double min, double max) {
            this();
            this.name = name;
            this.min = min;
            this.max = max;
        }

        /**
         * @return Returns the max.
         */
        double getMax() {
            return max;
        }

        /**
         * @param max
         *                   The max to set.
         */
        void setMax(double max) {
            this.max = max;
        }

        /**
         * @return Returns the min.
         */
        double getMin() {
            return min;
        }

        /**
         * @param min
         *                   The min to set.
         */
        void setMin(double min) {
            this.min = min;
        }

        /**
         * @return Returns the name.
         */
        String getName() {
            return name;
        }

        /**
         * @param name
         *                   The name to set.
         */
        void setName(String name) {
            this.name = name;
        }

        DoubleRange createCopy() {
            try {
                return (DoubleRange) this.clone();
            } catch (CloneNotSupportedException e) {
                throw new Error(e.toString());
            }
        }

        void addParameterDescs(ListParamDesc lp) throws ParamException {
            EnumParamDesc desc = new EnumParamDesc();
            desc.setLabel(this.name + " MAX");
            desc.setName(this.name + "_max");
            addValues(desc);
            lp.add(desc);
            desc = new EnumParamDesc();
            desc.setLabel(this.name + " MIN");
            desc.setName(this.name + "_min");
            addValues(desc);
            lp.add(desc);
        }

        private void addValues(EnumParamDesc desc) throws ParamException {
            Iterator iter = values(name).iterator();
            StringBuffer buf = new StringBuffer();
            int count = 0;
            while (iter.hasNext()) {
                String val = (String) iter.next();
                if (count > 0) {
                    buf.append(';');
                }
                buf.append(val);
                count++;
            }
            StringPopulator pop = new StringPopulator(buf.toString());
            desc.populate(pop);
        }

    }

    protected abstract DoubleRanges createNew();

    private DoubleRange getRange(String name) {
        return (DoubleRange) ranges.get(name);
    }

    protected abstract Collection values(String name);

    protected Collection defaultValues() {
        Collection c = new ArrayList();
        c.add("0.0");
        c.add("0.2");
        c.add("0.4");
        c.add("0.6");
        c.add("0.8");
        c.add("1.0");
        return c;
    }

    public void toStream(DataOutputStream s) throws IOException {
        Iterator iter = names().iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            paramToStream(getRange(name), s);
        }
    }

    private void paramToStream(DoubleRange param, DataOutputStream s)
            throws IOException {
        StreamPersistor.stringToStream(param.getName(), s);
        s.writeDouble(param.getMin());
        s.writeDouble(param.getMax());
    }

    public void fromStream(DataInputStream s, int version) throws IOException {
        this.ranges = new TreeMap();
        Iterator iter = names().iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            DoubleRange p = paramFromStream(s);
            if (!p.getName().equals(name))
                throw new Error("name was not properly saved. '" + p.getName()
                        + "' should be '" + name + "'");
            this.putRange(name, p);
        }
    }

    private DoubleRange paramFromStream(DataInputStream s) throws IOException {
        DoubleRange p = new DoubleRange();
        p.setName(StreamPersistor.stringFromStream(s));
        p.setMin(s.readDouble());
        p.setMax(s.readDouble());
        return p;
    }

    public int getVersion() {
        return 0;
    }

    /**
     * @throws ParamException
     * @see wodka.param.Configurable#getParameterDescription()
     */
    public ListParamDesc getParameterDescription() throws ParamException {
        ListParamDesc lp = new ListParamDesc();
        ListParamDesc grp = new ListParamDesc();
        Collection c = this.ranges.values();
        Iterator iter = c.iterator();
        while (iter.hasNext()) {
            DoubleRange p = (DoubleRange) iter.next();
            p.addParameterDescs(grp);
        }
        grp.setLabel(this.getLabel());
        lp.add(grp);
        return lp;
    }

    /**
     * @see wodka.param.Configurable#setFieldsFromParameterList(wodka.param.ListParam)
     */
    public void setFieldsFromParameterList(ListParam lp) throws ParamException {
        this.checkParameterList(lp);
        Iterator iter = names().iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            double min = lp.getParamValueDouble(name + "_min");
            double max = lp.getParamValueDouble(name + "_max");
            DoubleRange p = this.getRange(name);
            p.setMax(max);
            p.setMin(min);
        }
    }

    /**
     * @throws ParamException
     * @see wodka.param.Configurable#setParameterListFromFields(wodka.param.ListParam)
     */
    public void setParameterListFromFields(ListParam lp) throws ParamException {
        Iterator iter = names().iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            DoubleRange p = this.getRange(name);
            lp.setParamValue(p.getName() + "_min", p.getMin());
            lp.setParamValue(p.getName() + "_max", p.getMax());
        }
    }

    /**
     * @see wodka.param.Configurable#checkParameterList(wodka.param.ListParam)
     */
    public void checkParameterList(ListParam lp) throws ParamException {
        Iterator iter = names().iterator();
        Collection errors = new ArrayList();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            double min = lp.getParamValueDouble(name + "_min");
            double max = lp.getParamValueDouble(name + "_max");
            if (min > max) {
                errors.add(name + ": min may never be smaller than max.");
            }
        }
        if (!errors.isEmpty()) {
            StringBuffer sb = new StringBuffer();
            sb.append(this.getLabel());
            sb.append("\n");
            Iterator i = errors.iterator();
            while (i.hasNext()) {
                sb.append(i.next());
                sb.append("\n");
            }
            throw new ParamException(sb.toString());
        }
    }

    public DoubleRanges createCopy() {
        DoubleRanges params = this.createNew();
        Iterator iter = names().iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            DoubleRange p = getRange(name).createCopy();
            params.putRange(name, p);
        }
        return params;
    }

    public double getMin(String name) {
        return getRange(name).getMin();
    }

    public double getMax(String name) {
        return getRange(name).getMax();
    }

    public void setMax(String name, double val) {
        getRange(name).setMax(val);
    }

    public void setMin(String name, double val) {
        getRange(name).setMin(val);
    }

}