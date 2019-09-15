package wodka.test;

import junit.framework.*;
import junit.textui.*;
import java.util.*;
import wodka.util.*;

/**
 * Testcases for the wodka.util.Downsizer
 */

public class DownsizerTest extends TestCase {

  public DownsizerTest(String str) {
    super(str);
  }
  public void test01(){
    Downsizer ds = new Downsizer(12, 2, 3);
    List l = new Vector();
    for (int i=0; i<25; i++){
      l.add(0, new Integer(i));
      if (i == 11) {
        assertEquals(12, l.size());
        int vals[] = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        assertValues(l, vals);
      }
      else if (i == 16) {
        assertEquals(12, l.size());
        int vals[] = {16, 15, 14, 13, 12, 11, 10, 8, 5, 2, 1, 0};
        assertValues(l, vals);
      }
      else if (i == 18) {
        assertEquals(12, l.size());
        int vals[] = {18, 17, 16, 15, 12, 11, 10, 8, 5, 2, 1, 0};
        assertValues(l, vals);
      }
      else if (i == 23) {
        assertEquals(12, l.size());
        int vals[] = {23, 22, 21, 20, 19, 18, 17, 15, 10, 2, 1, 0};
        assertValues(l, vals);
      }
      ds.downsize(l);
      if (i == 11) {
        assertEquals(7, l.size());
        int vals[] = {11, 10, 8, 5, 2, 1, 0};
        assertValues(l, vals);
      }
      else if (i == 16) {
        assertEquals(10, l.size());
        int vals[] = {16, 15, 12, 11, 10, 8, 5, 2, 1, 0};
        assertValues(l, vals);
      }
      else if (i == 18) {
        assertEquals(7, l.size());
        int vals[] = {18, 17, 15, 10, 2, 1, 0};
        assertValues(l, vals);
      }
      else if (i == 23) {
        assertEquals(10, l.size());
        int vals[] = {23, 22, 19, 18, 17, 15, 10, 2, 1, 0};
        assertValues(l, vals);
      }
    }
  }
  public void test02(){
    Downsizer ds = new Downsizer(10, 1, 4);
    List l = new Vector();
    for (int i=0; i<25; i++){
      l.add(0, new Integer(i));
      if (i == 9) {
        assertEquals(10, l.size());
        int vals[] = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        assertValues(l, vals);
      }
      else if (i == 15) {
        assertEquals(10, l.size());
        int vals[] = {15, 14, 13, 12, 11, 10, 9, 5, 1, 0};
        assertValues(l, vals);
      }
      else if (i == 18) {
        assertEquals(10, l.size());
        int vals[] = {18, 17, 16, 15, 11, 10, 9, 5, 1, 0};
        assertValues(l, vals);
      }
      else if (i == 24) {
        assertEquals(10, l.size());
        int vals[] = {24, 23, 22, 21, 20, 19,18, 11, 1, 0};
        assertValues(l, vals);
      }
      ds.downsize(l);
      if (i == 9) {
        assertEquals(4, l.size());
        int vals[] = {9, 5, 1, 0};
        assertValues(l, vals);
      }
      else if (i == 15) {
        assertEquals(7, l.size());
        int vals[] = {15, 11, 10, 9, 5, 1, 0};
        assertValues(l, vals);
      }
      else if (i == 18) {
        assertEquals(4, l.size());
        int vals[] = {18, 11, 1, 0};
        assertValues(l, vals);
      }
      else if (i == 24) {
        assertEquals(7, l.size());
        int vals[] = {24, 20, 19,18, 11, 1, 0};
        assertValues(l, vals);
      }
    }
  }
  private void assertValues(List l, int[] vals){
    for (int i=0; i<vals.length; i++) {
      Integer elem = (Integer)l.get(i);
      assertEquals(vals[i], elem.intValue());
    }
  }
  public static void main(String[] args) {
    TestRunner.run(new TestSuite(wodka.test.DownsizerTest.class));
  }
}