package wodka.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import wodka.util.Combis;
import wodka.util.CraterFunction;
import wodka.util.DoubleRanges;
import wodka.util.DoubleValues;
import wodka.util.GridLayoutAlignment;
import wodka.util.LinearTransform;
import wodka.util.MeanBuffer;
import wodka.util.PairDetector;
import wodka.util.PeakFunction;
import wodka.util.PeakFunction3D;
import wodka.util.Util;

public class UtilTest extends junit.framework.TestCase {

    private Util util = Util.current();

    public UtilTest(String str) {
        super(str);
    }

    public void testPairDetector() {
        PairDetector d = new PairDetector();
        d.addPair(1, 2);
        d.addPair(1, 1);
        d.addPair(1, 4);
        d.addPair(1, 4);
        d.addPair(1, 5);
        assertTrue(d.includesPair(1, 1));
        assertTrue(d.includesPair(1, 2));
        assertTrue(d.includesPair(1, 4));
        assertTrue(d.includesPair(1, 5));
        assertTrue(d.includesPair(5, 1));
        assertTrue(d.includesPair(4, 1));
        assertTrue(d.includesPair(2, 1));
        assertTrue(!d.includesPair(6, 1));
        assertTrue(!d.includesPair(5, 2));
        assertTrue(!d.includesPair(2, 2));
        assertTrue(!d.includesPair(-2, -2));
    }

    public void testLinearTransform01() {
        LinearTransform t = new LinearTransform(60, 40, 8, 8, 12.5, 0);
        assertTransform(t, 10, 10, 2, 3);
        assertTransform(t, 0, 0, 1, 2);
        assertTransform(t, 10, 30, 2, 5);
        assertTransform(t, 35, 25, 5, 5);
        assertTransform(t, 34, 24, 4, 4);
    }

    public void testLinearTransform02() {
        LinearTransform t = new LinearTransform(80, 60, 14, 8, 12.5, 0);
        assertTransform(t, 10, 10, 4, 2);
        assertTransform(t, 20, 30, 5, 4);
        assertTransform(t, 25, 45, 6, 6);
        assertTransform(t, 26, 47, 6, 6);
        assertTransform(t, 24, 44, 5, 5);
    }

    private void assertTransform(LinearTransform t, int a, int b, int x, int y) {
        assertEquals(x, t.transX(a));
        assertEquals(y, t.transY(b));
    }

    public void testPeakFunction() {
        PeakFunction f = new PeakFunction(0, 10, 10, 5);
        assertEquals(0, f.calculate(0));
        assertEquals(0, f.calculate(5));
        assertEquals(0, f.calculate(15));
        assertEquals(0, f.calculate(100));
        assertEquals(2, f.calculate(6));
        assertEquals(4, f.calculate(7));
        assertEquals(6, f.calculate(8));
        assertEquals(8, f.calculate(9));
        assertEquals(10, f.calculate(10));
        assertEquals(8, f.calculate(11));
        assertEquals(6, f.calculate(12));
    }

    public void testPeakFunction0() {
        PeakFunction f = new PeakFunction(5, 10, 10, 5);
        assertEquals(5, f.calculate(0));
        assertEquals(5, f.calculate(5));
        assertEquals(5, f.calculate(15));
        assertEquals(5, f.calculate(100));
        assertEquals(7, f.calculate(6));
        assertEquals(9, f.calculate(7));
        assertEquals(11, f.calculate(8));
        assertEquals(13, f.calculate(9));
        assertEquals(15, f.calculate(10));
        assertEquals(13, f.calculate(11));
        assertEquals(11, f.calculate(12));
    }

    public void testPeakFunction1() {
        PeakFunction f = new PeakFunction(5, 10, -5, 5);
        assertEquals(5, f.calculate(0));
        assertEquals(5, f.calculate(5));
        assertEquals(5, f.calculate(15));
        assertEquals(5, f.calculate(100));
        assertEquals(4, f.calculate(6));
        assertEquals(3, f.calculate(7));
        assertEquals(2, f.calculate(8));
        assertEquals(1, f.calculate(9));
        assertEquals(0, f.calculate(10));
        assertEquals(1, f.calculate(11));
        assertEquals(2, f.calculate(12));
    }

    public void testMeanBuffer() {
        MeanBuffer mb = new MeanBuffer(4);
        mb.add(5);
        assertEquals(1, mb.mean());
        mb.add(5);
        assertEquals(2, mb.mean());
        mb.add(5);
        assertEquals(3, mb.mean());
        mb.add(5);
        assertEquals(5, mb.mean());
        mb.add(0);
        assertEquals(3, mb.mean());
        mb.add(0);
        assertEquals(2, mb.mean());
        mb.add(0);
        assertEquals(1, mb.mean());
        mb.add(0);
        assertEquals(0, mb.mean());
        mb.add(0);
        assertEquals(0, mb.mean());
    }

    public void testPeak3D_00() {
        PeakFunction3D f = new PeakFunction3D(0, 200, 200, 100, 100);
        assertEquals(f, 100, 100, 0);
        assertEquals(f, 200, 200, 100);
        assertEquals(f, 300, 300, 0);
        assertEquals(f, 150, 150, 50);
        assertEquals(f, 150, 200, 50);
        assertEquals(f, 150, 222, 50);
        assertBetween(f, 149, 222, 0, 50);
        assertEquals(f, 250, 222, 50);
        assertBetween(f, 200, 222, 50, 100);
        assertBetween(f, 200, 199, 50, 100);
    }

    public void testPeak3D_01() {
        PeakFunction3D f = new PeakFunction3D(0, 200, 200, -100, 100);
        assertEquals(f, 100, 100, 0);
        assertEquals(f, 200, 200, -100);
        assertEquals(f, 300, 300, 0);
        assertEquals(f, 150, 150, -50);
        assertEquals(f, 150, 200, -50);
        assertEquals(f, 150, 222, -50);
        assertBetween(f, 149, 222, -50, 0);
        assertEquals(f, 250, 222, -50);
        assertBetween(f, 200, 222, -100, 50);
        assertBetween(f, 200, 199, -100, -50);
    }

    private void assertEquals(PeakFunction3D f, int x, int y, int z) {
        assertEquals(z, f.calculate(x, y));
    }

    private void assertBetween(PeakFunction3D f, int x, int y, int from, int to) {
        int val = f.calculate(x, y);
        assertTrue("val:" + val + " from:" + from + " ", val > from);
        assertTrue("val:" + val + " to:" + to + " ", val < to);
    }

    private void assertBetween(int from, int to, int val) {
        assertTrue("val:" + val + " from:" + from + " ", val > from);
        assertTrue("val:" + val + " to:" + to + " ", val < to);
    }

    public void testCraterFunction() {
        CraterFunction f = new CraterFunction(0, 50, 50, 30, 100, 10);
        assertEquals(0, f.calculate(10, 10));
        assertEquals(0, f.calculate(10, 50));
        assertEquals(100, f.calculate(20, 50));
        assertEquals(50, f.calculate(25, 50));
        assertEquals(0, f.calculate(40, 60));
        assertBetween(0, 50, f.calculate(40, 70));
        assertBetween(50, 100, f.calculate(40, 80));
        assertEquals(0, f.calculate(40, 90));
        assertEquals(0, f.calculate(50, 90));
        assertEquals(0, f.calculate(50, 50));
        assertBetween(50, 100, f.calculate(60, 20));
        assertEquals(100, f.calculate(80, 50));
        assertBetween(50, 100, f.calculate(80, 60));
        assertEquals(0, f.calculate(100, 100));
        assertEquals(0, f.calculate(90, 90));
    }

    public void testGridLayoutAlignment() {
        GridLayoutAlignment a = new GridLayoutAlignment();
        assertAlignment(a, 1, 1, 1);
        assertAlignment(a, 2, 1, 2);
        assertAlignment(a, 3, 2, 2);
        assertAlignment(a, 4, 2, 2);
        assertAlignment(a, 5, 2, 3);
        assertAlignment(a, 6, 2, 3);
        assertAlignment(a, 7, 3, 3);
        assertAlignment(a, 8, 3, 3);
        assertAlignment(a, 9, 3, 3);
        assertAlignment(a, 10, 3, 4);
        assertAlignment(a, 11, 3, 4);
        assertAlignment(a, 12, 3, 4);
        assertAlignment(a, 13, 4, 4);
        assertAlignment(a, 14, 4, 4);
        assertAlignment(a, 15, 4, 4);
        assertAlignment(a, 16, 4, 4);
        assertAlignment(a, 17, 4, 5);
        assertAlignment(a, 18, 4, 5);
        assertAlignment(a, 19, 4, 5);
        assertAlignment(a, 20, 4, 5);
        assertAlignment(a, 21, 5, 5);
    }

    private void assertAlignment(GridLayoutAlignment a, int count, int rows, int cols) {
        java.awt.Dimension d = a.align(count);
        assertEquals(rows, d.height);
        assertEquals(cols, d.width);
    }

    public void testCompleteBackslashes() {
        assertEquals("12\\\\345\\\\", util.completeBackslashes("12\\345\\"));
        assertEquals("\\\\345\\\\", util.completeBackslashes("\\345\\"));
        assertEquals("333", util.completeBackslashes("333"));
    }

    public void testIndexOfDouble() {
        List vals = new ArrayList();
        vals.add("0.01"); // 0
        vals.add("0.02"); // 1
        vals.add("0.03"); // 2
        vals.add("0.04"); // 3
        vals.add("1.2"); // 4
        vals.add("123E3"); // 5
        vals.add("-100"); // 6
        vals.add("0"); // 7
        vals.add("123E2"); // 8
        assertIndex(6, vals, -200.0);
        assertIndex(6, vals, -100.0);
        assertIndex(7, vals, -99.0);
        assertIndex(0, vals, 0.000001);
        assertIndex(0, vals, 0.01);
        assertIndex(2, vals, 0.02001);
        assertIndex(1, vals, 0.02);
        assertIndex(4, vals, 1.0);
        assertIndex(8, vals, 2.0);
        assertIndex(5, vals, 12400.0);
    }

    private void assertIndex(int index, List strings, double val) {
        assertEquals(index, Util.current().indexOf(strings, val));
    }

    public void testIndexOfInt() {
        List vals = new ArrayList();
        vals.add("-100"); // 0
        vals.add("-10"); // 1
        vals.add("-1"); // 2
        vals.add("0"); // 3
        vals.add("1"); // 4
        vals.add("2"); // 5
        vals.add("3"); // 6
        vals.add("100"); // 7
        vals.add("50"); // 8
        assertIndex(0, vals, -200);
        assertIndex(0, vals, -100);
        assertIndex(1, vals, -99);
        assertIndex(1, vals, -10);
        assertIndex(3, vals, 0);
        assertIndex(4, vals, 1);
        assertIndex(6, vals, 3);
        assertIndex(8, vals, 10);
        assertIndex(8, vals, 50);
        assertIndex(7, vals, 51);
        assertIndex(7, vals, 100);
        assertIndex(7, vals, 1000);
    }

    private void assertIndex(int index, List strings, int val) {
        assertEquals(index, Util.current().indexOf(strings, val));
    }

    public void testCombinations() {
        List sizes = new ArrayList();
        sizes.add(new Integer(2));
        sizes.add(new Integer(3));
        sizes.add(new Integer(2));
        Combis combi = new Combis(sizes);
        assertCombi(combi, 0, 0, 0);
        assertCombi(combi, 0, 0, 1);
        assertCombi(combi, 0, 1, 0);
        assertCombi(combi, 0, 1, 1);
        assertCombi(combi, 0, 2, 0);
        assertCombi(combi, 0, 2, 1);
        assertCombi(combi, 1, 0, 0);
        assertCombi(combi, 1, 0, 1);
        assertCombi(combi, 1, 1, 0);
        assertCombi(combi, 1, 1, 1);
        assertCombi(combi, 1, 2, 0);
        assertCombi(combi, 1, 2, 1);
        assertTrue(!combi.hasNext());
    }

    public void testCombinations1() {
        List sizes = new ArrayList();
        sizes.add(new Integer(2));
        Combis combi = new Combis(sizes);
        assertCombi(combi, 0);
        assertCombi(combi, 1);
        assertTrue(!combi.hasNext());
    }

    private void assertCombi(Combis combi, int i, int j, int k) {
        assertTrue(combi.hasNext());
        List c = combi.next();
        Integer v = (Integer) c.get(0);
        assertEquals(i, v.intValue());
        v = (Integer) c.get(1);
        assertEquals(j, v.intValue());
        v = (Integer) c.get(2);
        assertEquals(k, v.intValue());
    }

    private void assertCombi(Combis combi, int i) {
        assertTrue(combi.hasNext());
        List c = combi.next();
        Integer v = (Integer) c.get(0);
        assertEquals(i, v.intValue());
    }

    public void testDoubleValuesReadDefaultValues() {
        DoubleValues dv = DoubleTestValues.createNewInstance(DoubleTestRanges.createInstance());
        assertEquals(1.5, dv.getValue("x"), 0.00001);
        assertEquals(0.5, dv.getValue("y"), 0.00001);
        assertEquals(0.7, dv.getValue("z"), 0.00001);
    }

    public void testDoubleValuesSetOutOfRanges() {
        DoubleRanges dr = DoubleTestRanges.createInstance();
        assertEquals(1.0, dr.getMin("a"), 0.000001);
        assertEquals(2.0, dr.getMax("a"), 0.000001);
        assertEquals(0.0, dr.getMin("b"), 0.000001);
        assertEquals(1.0, dr.getMax("b"), 0.000001);
        // Expecting x is linked to a and y,z are linked to b
        DoubleValues dv = DoubleTestValues.createNewInstance(dr);
        assertSetValue(dv, 1.0, "x", -1);
        assertSetValue(dv, 1.0, "x", 0.0);
        assertSetValue(dv, 1.0, "x", 0.5);
        assertSetValue(dv, 1.0, "x", 1.0);
        assertSetValue(dv, 1.1, "x", 1.1);
        assertSetValue(dv, 1.9, "x", 1.9);
        assertSetValue(dv, 2.0, "x", 2.0);
        assertSetValue(dv, 2.0, "x", 2.1);
        assertSetValue(dv, 2.0, "x", 3.0);
        assertSetValue(dv, 2.0, "x", 1234);
        assertSetValueFrom0To1(dv, "y");
        assertSetValueFrom0To1(dv, "z");
    }

    public void testDoubleValuesSetOutOfRangesWithChangedRanges() {
        DoubleRanges dr = DoubleTestRanges.createInstance();
        assertEquals(1.0, dr.getMin("a"), 0.000001);
        assertEquals(2.0, dr.getMax("a"), 0.000001);
        assertEquals(0.0, dr.getMin("b"), 0.000001);
        assertEquals(1.0, dr.getMax("b"), 0.000001);
        dr.setMax("a", 1.0);
        dr.setMin("a", 0.0);
        assertEquals(0.0, dr.getMin("a"), 0.000001);
        assertEquals(1.0, dr.getMax("a"), 0.000001);
        assertEquals(0.0, dr.getMin("b"), 0.000001);
        assertEquals(1.0, dr.getMax("b"), 0.000001);
        // Expecting x is linked to a and y,z are linked to b
        DoubleValues dv = DoubleTestValues.createNewInstance(dr);
        assertSetValueFrom0To1(dv, "x");
        assertSetValueFrom0To1(dv, "y");
        assertSetValueFrom0To1(dv, "z");
    }

    private void assertSetValueFrom0To1(DoubleValues dv, String name) {
        assertSetValue(dv, 0.0, name, -100);
        assertSetValue(dv, 0.0, name, -0.9999999);
        assertSetValue(dv, 0.0, name, 0.0);
        assertSetValue(dv, 0.001, name, 0.001);
        assertSetValue(dv, 0.5, name, 0.5);
        assertSetValue(dv, 0.99999, name, 0.99999);
        assertSetValue(dv, 1.0, name, 1.0);
        assertSetValue(dv, 1.0, name, 1.001);
        assertSetValue(dv, 1.0, name, 1001);
    }

    private void assertSetValue(DoubleValues dv, double d, String string, double e) {
        dv.setValue(string, e);
        assertEquals(d, dv.getValue(string), 0.0000001);
    }

    public void testDoubleValuesCrossover() {
        for (int i = 0; i < 20; i++) {
            DoubleValues father = DoubleTestValuesArray.createNewInstance(DoubleTestRanges.createInstance());
            father.setAllValues(0);
            DoubleValues mother = DoubleTestValuesArray.createNewInstance(DoubleTestRanges.createInstance());
            mother.setAllValues(1);
            DoubleValues child = mother.crossover(father);
            assertNotNull(child);
        }

    }

    public void testDoubleValuesMutate() {
        DoubleValues mutated = DoubleTestValues.createNewInstance(DoubleTestRanges.createInstance());
        DoubleValues unchanged = DoubleTestValues.createNewInstance(DoubleTestRanges.createInstance());
        for (int i = 0; i < 200; i++) {
            mutated.mutate(0.01);
        }
        for (int i = 0; i < 2000; i++) {
            mutated.mutate(0.01);
        }
        assertNotNull(unchanged);
    }

    public void testGrid() {
        assertGrid(5, 1, -1, 0);
        assertGrid(5, 1, 0, 0);
        assertGrid(5, 1, 1, 1);
        assertGrid(5, 1, 2, 2);
        assertGrid(5, 1, 3, 3);
        assertGrid(5, 1, 4, 4);
        assertGrid(5, 1, 5, 5);
        assertGrid(5, 1, 6, 5);
        assertGrid(5, 1, 10, 5);

        assertGrid(5, 2, -1, 0);
        assertGrid(5, 2, 0, 0);
        assertGrid(5, 2, 1, 2);
        assertGrid(5, 2, 2, 2);
        assertGrid(5, 2, 3, 4);
        assertGrid(5, 2, 4, 4);
        assertGrid(5, 2, 5, 5);
        assertGrid(5, 2, 6, 5);
        assertGrid(5, 2, 10, 5);

        assertGrid(5, 3, -1, 0);
        assertGrid(5, 3, 0, 0);
        assertGrid(5, 3, 1, 0);
        assertGrid(5, 3, 2, 3);
        assertGrid(5, 3, 3, 3);
        assertGrid(5, 3, 4, 3);
        assertGrid(5, 3, 5, 5);
        assertGrid(5, 3, 6, 5);
        assertGrid(5, 3, 10, 5);
    }

    public void testThinning() {
        assertThinning(8, 2, "0,7");
        assertThinning(8, 3, "0,3,7");
        assertThinning(8, 4, "0,2,4,7");
        assertThinning(8, 5, "0,2,3,5,7");
        assertThinning(8, 6, "0,2,3,4,5,7");
        assertThinning(8, 7, "0,1,2,3,4,5,7");
        assertThinning(8, 8, "0,1,2,3,4,5,6,7");
    }

    public void testConvertFitness() {
        assertSodaWodkaSoda(100.0);
        assertSodaWodkaSoda(200.0);
        assertSodaWodkaSoda(100.12);
        assertSodaWodkaSoda(10.0);
        assertSodaWodkaSoda(1.23);
        assertSodaWodkaSoda(10000.0);
        assertSodaWodkaSoda(10000.23);
        assertSodaWodkaSoda(10000.12);
        assertSodaWodkaSoda(10000.62);
        assertWodkaSodaWodka(1);
        assertWodkaSodaWodka(1000);
        assertWodkaSodaWodka(12345);
        assertWodkaSodaWodka(123456);
    }

    private void assertWodkaSodaWodka(int i) {
        double d = util.convertFitnessWodkaToSoda(i);
        assertEquals(i, util.convertFitnessSodaToWodka(d));
    }

    private void assertSodaWodkaSoda(double d) {
        int i = util.convertFitnessSodaToWodka(d);
        assertEquals(d, util.convertFitnessWodkaToSoda(i), 0.00001);
    }

    private void assertThinning(int len, int n, String values) {
        StringTokenizer st = new StringTokenizer(values, ",");
        int i = 0;
        while (st.hasMoreElements()) {
            String val = (String) st.nextElement();
            int ival = Integer.parseInt(val);
            assertEquals(ival, util.thin(len, n, i));
            i++;
        }
    }

    private void assertGrid(int width, int grid, int x, int y) {
        assertEquals(y, util.grid(x, grid, width));
    }

    public void testTimeFormat() {
        assertEquals("100ms", util.formatTime(100));
        assertEquals("333ms", util.formatTime(333));
        assertEquals("1s", util.formatTime(1000));
        assertEquals("1s", util.formatTime(1100));
        assertEquals("1s", util.formatTime(1111));
        assertEquals("1s", util.formatTime(1499));
        assertEquals("2s", util.formatTime(1500));
        assertEquals("2s", util.formatTime(1919));
        assertEquals("3m", util.formatTime((3 * 60 * 1000)));
        assertEquals("3m", util.formatTime((long) (3.4999 * 60 * 1000)));
        assertEquals("4m", util.formatTime((long) (3.5 * 60 * 1000)));
        assertEquals("4m", util.formatTime((long) (3.9 * 60 * 1000)));
        assertEquals("12m", util.formatTime((12 * 60 * 1000)));
        assertEquals("12m", util.formatTime((long) (12.11 * 60 * 1000)));
        assertEquals("12m", util.formatTime((long) (12.499 * 60 * 1000)));
        assertEquals("13m", util.formatTime((long) (12.5 * 60 * 1000)));
        assertEquals("13m", util.formatTime((long) (12.99 * 60 * 1000)));
        assertEquals("1m", util.formatTime(60 * 1000));
        assertEquals("1h", util.formatTime(60 * 60 * 1000));
        assertEquals("2h", util.formatTime((long) (2.34 * 60 * 60 * 1000)));
        assertEquals("2h", util.formatTime((long) (2.4999 * 60 * 60 * 1000)));
        assertEquals("3h", util.formatTime((long) (2.5 * 60 * 60 * 1000)));
        assertEquals("3h", util.formatTime((long) (2.9 * 60 * 60 * 1000)));
    }

    public void testRandomValues() {
        for (int i = 0; i < 130; i++) {
            //			info("" + util.randomDouble3(0.001, 0.1));
            //			info("" + util.randomInt(5, 10));
        }
    }

    private void info(String msg) {
        System.out.println(this.getClass().getName() + ">>" + msg);
    }

    public void testFormat() {
        assertEquals("0", util.formatDouble3(0.0));
        assertEquals("0.1", util.formatDouble3(0.1));
        assertEquals("0.111", util.formatDouble3(0.111111));
        assertEquals("123.111", util.formatDouble3(123.11111));
        assertEquals("0", util.formatDouble5(0.0));
        assertEquals("0.1", util.formatDouble5(0.1));
        assertEquals("0.11111", util.formatDouble5(0.111111));
        assertEquals("123.11111", util.formatDouble5(123.11111));
    }

    public void xtestReverse() {
        assertCollection("A", "B", "C", "D");
        assertCollection("A", null, "C", "D");
        assertCollection(null, "B", "C", "D");
        assertCollection("A", "B", "C", null);
        assertCollection(null, null, null, null);
    }

    private void assertCollection(String string1, String string2, String string3, String string4) {
        Collection collection = new ArrayList();
        collection.add(string1);
        collection.add(string2);
        collection.add(string3);
        collection.add(string4);
        List rev = new ArrayList(collection);
        info ("normal" + rev);
        Collections.reverse(rev);
        info ("reverse" + rev);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(new junit.framework.TestSuite(wodka.test.UtilTest.class));
    }
}