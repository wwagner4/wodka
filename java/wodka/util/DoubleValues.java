/*
 * Created on Jan 29, 2004
 */
package wodka.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

/**
 * A set of double values that can be easily configured and on which genetic operations can be applied.
 * 
 * @author wwagner4
 */
public abstract class DoubleValues implements StreamPersistable, ContentComparable {

    protected transient Map values = new TreeMap();
    protected transient DoubleRanges ranges = null;
    private static Random ran = new Random();

    public void setRanges(DoubleRanges ranges) {
        this.ranges = ranges;
    }

    public DoubleValues() {
        super();
    }

    public void fromStream(DataInputStream inStream, int version) throws IOException {
        int count = inStream.readInt();
        for (int i = 0; i < count; i++) {
            String key = StreamPersistor.stringFromStream(inStream);
            double val = inStream.readDouble();
            values.put(key, new Double(val));
        }
    }

    public int getVersion() {
        return 0;
    }

    public void toStream(DataOutputStream outStream) throws IOException {
        Set keys = values.keySet();
        outStream.writeInt(keys.size());
        Iterator iter = keys.iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            Double val = (Double) values.get(key);
            StreamPersistor.stringToStream(key, outStream);
            outStream.writeDouble(val.doubleValue());
        }
    }

    public DoubleValues(DoubleRanges ranges) {
        super();
        this.ranges = ranges;
    }

    protected abstract void initDefaultValues();

    protected abstract String getRangeKey(String name);

    public double getValue(String name) {
        Double value = (Double) values.get(name);
        if (value == null){
            throw new Error("Value named -" + name + "- not found.");
        }
        return value.doubleValue();
    }

    public void setValue(String name, double value) {
        double val = value;
        if (!values.containsKey(name)){
            throw new Error("-" + name + "- is not a valid name.");
        }
        String rangeKey = getRangeKey(name);
        val = Math.max(val, this.ranges.getMin(rangeKey));
        val = Math.min(val, this.ranges.getMax(rangeKey));
        values.put(name, new Double(val));
    }
    public void mutate(double mutationRate) {
        double ranVal = ran.nextDouble();
        if (ranVal <= mutationRate) {
            int index = ran.nextInt(this.values.size());
            String name = this.getName(index);
            double val = getValue(name);
            double diff = ran.nextDouble();
            if (ran.nextBoolean()) {
                setValue(name, val + diff);
            } else {
                setValue(name, val - diff);
            }
        }
    }

    protected String getName(int index) {
        LinkedList linkedList = new LinkedList(values.keySet());
        return (String) linkedList.get(index);
    }
    
//    private void info(String msg) {
//        System.out.println(this.getClass().getName() + ">>" + msg);
//    }

    protected abstract DoubleValues createInstance();

    public void setAllValues(double val) {
        for (int i = 0; i < values.size(); i++) {
            String name = getName(i);
            this.setValue(name, val);
        }
    }

    public DoubleValues crossover(DoubleValues father) {
        DoubleValues child = createInstance();
        DoubleValues dominant = null;
        if (ran.nextBoolean()) {
            dominant = this;
        } else {
            dominant = father;
        }
        int ranValue = ran.nextInt(values.size());
        for (int i = 0; i < values.size(); i++) {
            if (i == ranValue) {
                if (dominant == this) {
                    dominant = father;
                } else {
                    dominant = this;
                }
            }
            String name = getName(i);
            child.setValue(name, dominant.getValue(name));
        }
        return child;
    }

    protected void defineValueWithDefault(String name, double val) {
        values.put(name, new Double(val));
    }
    public void setRandomValues() {
        for (int i = 0; i < values.size(); i++) {
            String name = getName(i);
            double min = ranges.getMin(name);
            double max = ranges.getMax(name);
            double val = (ran.nextDouble() * (max - min)) + min;
            this.setValue(name, val);
        }
    }

    public boolean hasEqualContents(Object obj) {
        if (!(obj instanceof DoubleValues)) {
            return false;
        }
        DoubleValues other = (DoubleValues) obj;
        Collection thisKeys = this.values.keySet();
        Collection otherKeys = other.values.keySet();
        if (thisKeys.size() != otherKeys.size()) {
            return false;
        }
        Iterator thisKeysIter = thisKeys.iterator();
        while (thisKeysIter.hasNext()) {
            String thisKey = (String) thisKeysIter.next();
            if (!otherKeys.contains(thisKey)) {
                return false;
            }
            Double thisValue = (Double) this.values.get(thisKey);
            Double otherValue = (Double) other.values.get(thisKey);
            if (!equal(thisValue, otherValue)) {
                return false;
            }
        }
        return true;
    }


    private boolean equal(Double thisValue, Double otherValue) {
        double valueA = thisValue.doubleValue();
        double valueB = otherValue.doubleValue();
        return Math.abs(valueA - valueB) < 0.00001;
    }

}
