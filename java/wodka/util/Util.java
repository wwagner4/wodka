/*
 * Created on 12.12.2003
 * 
 * To change the template for this generated file go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 * Comments
 */
package wodka.util;

import java.io.*;
import java.text.NumberFormat;
import java.util.*;

import org.jdom.JDOMException;

/**
 * @author wwagner4
 *  
 */
public class Util {

    private static final Locale LOCAL = Locale.ENGLISH;

    private static Util current = null;

    private transient Random ran = new Random();

    private transient NumberFormat format3 = createFormat3();

    private transient NumberFormat format5 = createFormat5();

    private Util() {
        super();
    }

    private NumberFormat createFormat3() {
        NumberFormat format = NumberFormat.getInstance(LOCAL);
        format.setMaximumFractionDigits(3);
        return format;
    }

    private NumberFormat createFormat5() {
        NumberFormat format = NumberFormat.getInstance(LOCAL);
        format.setMaximumFractionDigits(5);
        return format;
    }

    public static Util current() {
        if (current == null)
            current = new Util();
        return current;
    }

    public String completeBackslashes(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\')
                sb.append('\\');
            sb.append(c);
        }
        return sb.toString();
    }

    public void validateXml(InputStream in, String schemaUrl)
            throws JDOMException {
        this.validateXml(new InputStreamReader(in), schemaUrl);
    }

    public void validateXml(Reader in, String schemaUrl) throws JDOMException {
        org.jdom.input.SAXBuilder b = new org.jdom.input.SAXBuilder(
                "org.apache.xerces.parsers.SAXParser", true);
        b.setFeature("http://apache.org/xml/features/validation/schema", true);
        b
                .setProperty(
                        "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation",
                        schemaUrl);
        b.build(in);
    }

    public int indexOf(List strings, double val) {
        SortedSet doubleVals = new TreeSet();
        Iterator iter = strings.iterator();
        int i = 0;
        while (iter.hasNext()) {
            String sVal = (String) iter.next();
            doubleVals.add(new DoubleVal(i, sVal));
            i++;
        }
        iter = doubleVals.iterator();
        while (iter.hasNext()) {
            DoubleVal dVal = (DoubleVal) iter.next();
            if (dVal.smallerOrEqual(val))
                return dVal.getIndex();
        }
        DoubleVal lastVal = (DoubleVal) doubleVals.last();
        return lastVal.getIndex();
    }

    public int indexOf(List strings, int val) {
        SortedSet intVals = new TreeSet();
        Iterator iter = strings.iterator();
        int i = 0;
        while (iter.hasNext()) {
            String sVal = (String) iter.next();
            intVals.add(new IntVal(i, sVal));
            i++;
        }
        iter = intVals.iterator();
        while (iter.hasNext()) {
            IntVal iVal = (IntVal) iter.next();
            if (iVal.smallerOrEqual(val))
                return iVal.getIndex();
        }
        IntVal lastVal = (IntVal) intVals.last();
        return lastVal.getIndex();
    }

    private class IntVal implements Comparable {
        private int index = -1;

        private int val = Integer.MIN_VALUE;

        public IntVal(int index, String sval) {
            super();
            this.index = index;
            this.val = Integer.parseInt(sval);
        }

        public boolean smallerOrEqual(int v) {
            return v <= val;
        }

        public int getIndex() {
            return this.index;
        }

        public int compareTo(Object obj) {
            IntVal iVal = (IntVal) obj;
            Integer i1 = new Integer(iVal.val);
            Integer i2 = new Integer(this.val);
            return i2.compareTo(i1);
        }
    }

    private class DoubleVal implements Comparable {
        private int index = -1;

        private double val = Double.MIN_VALUE;

        public DoubleVal(int index, String sval) {
            super();
            this.index = index;
            this.val = Double.parseDouble(sval);
        }

        public boolean smallerOrEqual(double v) {
            return v <= val;
        }

        public int getIndex() {
            return this.index;
        }

        public int compareTo(Object obj) {
            DoubleVal dVal = (DoubleVal) obj;
            Double d1 = new Double(dVal.val);
            Double d2 = new Double(this.val);
            return d2.compareTo(d1);
        }
    }

    public int grid(int x, int grid, int width) {
        int f = (int) Math.round((double) x / grid);
        int g = f * grid;
        return forceToInterval(0, width, g);
    }

    private int forceToInterval(int min, int max, int g) {
        int result = g;
        if (g < min)
            result = min;
        if (g >= max)
            result = max;
        return result;
    }

    public int convertFitnessSodaToWodka(double sodaVal) {
        return (int) (1010000 - Math.round(sodaVal * 100));
    }

    public double convertFitnessWodkaToSoda(int wodkaVal) {
        return (1010000.0 - wodkaVal) / 100;
    }

    public int thin(int len, int n, int i) {
        if (i >= n)
            throw new Error("i may never be greater or equal than n. i:" + i
                    + " n:" + n);
        if (n < 2)
            throw new Error("n may never be smaller than 2. n:" + n);
        if (n > len)
            throw new Error("n may never be greater than len. n:" + n + " len:"
                    + len);
        int result = -1;
        if (i == (n - 1)) {
            result = len - 1;
        } else {
            double d = 1.0 / (n - 1) * i;
            result = (int) Math.ceil(d * (len - 2));
        }
        return result;
    }

    public double randomDouble3(double min, double max) {
        double diff = ran.nextDouble() * (max - min) * 1000.0;
        return Math.round(min * 1000.0 + diff) / 1000.0;
    }

    public int randomInt(int min, int max) {
        double diff = ran.nextDouble() * (max - min + 1);
        return ((int) (min + diff));
    }

    public String formatTime(long milliSec) {
        if (milliSec < 1000)
            return milliSec + "ms";
        else if (milliSec < 60000)
            return Math.round(milliSec / 1000.0) + "s";
        else if (milliSec < 3600000)
            return Math.round(milliSec / 60000.0) + "m";
        else
            return Math.round(milliSec / 3600000.0) + "h";
    }

    public String formatDouble3(double d) {
        return format3.format(d);
    }

    public String formatDouble5(double d) {
        return format5.format(d);
    }
}