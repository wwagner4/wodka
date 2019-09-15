package wodka.test;

import junit.framework.*;
import junit.textui.*;
import wodka.ga.racer.mc.*;
import wodka.ga.model.*;

/**
 * Testcases for the wodka assambler language
 */

public class ModelCompareTest extends TestCase {


  public ModelCompareTest(String str) {
    super(str);
  }
  public void test01() {
    ModelCompareRaceRunner mc = new ModelCompareRaceRunnerX();
    int ca = mc.compare(createA());
    assertEquals(780, ca);
  }
  public void test02() {
    ModelCompareRaceRunner mcr = new ModelCompareRaceRunnerX();
    assertEquals(375, mcr.compare(createB()));
  }
  public void test03() {
	ModelCompareRaceRunner mcr = new ModelCompareRaceRunnerX();
    int cc = mcr.compare(createC());
    assertEquals(188, cc);
  }
  private int transformToMaxX(int val) {
    int mul = Model.WIDTH / 32;
    return val * mul;
  }
  private Model createA() {
    Model m = new Model();
    Mass m0 = new Mass(m, transformToMaxX(7), transformToMaxX(6));
    m.addMass(m0);
    Mass m1 = new Mass(m, transformToMaxX(23), transformToMaxX(5));
    m.addMass(m1);
    Mass m2 = new Mass(m, transformToMaxX(26), transformToMaxX(18));
    m.addMass(m2);
    Mass m3 = new Mass(m, transformToMaxX(19), transformToMaxX(25));
    m.addMass(m3);
    Mass m4 = new Mass(m, transformToMaxX(5), transformToMaxX(17));
    m.addMass(m4);
    m.addMuscle(new Muscle(m, m0, m1, 0.0, 0.0));
    m.addMuscle(new Muscle(m, m1, m2, 0.0, 0.0));
    m.addMuscle(new Muscle(m, m2, m3, 0.0, 0.0));
    m.addMuscle(new Muscle(m, m3, m4, 0.0, 0.0));
    m.addMuscle(new Muscle(m, m4, m0, 0.0, 0.0));
    return m;
  }
  private Model createB() {
    Model m = new Model();
    Mass m0 = new Mass(m, transformToMaxX(18), transformToMaxX(2));
    m.addMass(m0);
    Mass m1 = new Mass(m, transformToMaxX(23), transformToMaxX(27));
    m.addMass(m1);
    Mass m2 = new Mass(m, transformToMaxX(6), transformToMaxX(23));
    m.addMass(m2);
    m.addMuscle(new Muscle(m, m0, m1, 0.0, 0.0));
    m.addMuscle(new Muscle(m, m1, m2, 0.0, 0.0));
    m.addMuscle(new Muscle(m, m2, m0, 0.0, 0.0));
    return m;
  }
  private Model createC() {
    Model m = new Model();
    Mass m0 = new Mass(m, transformToMaxX(5), transformToMaxX(5));
    m.addMass(m0);
    Mass m1 = new Mass(m, transformToMaxX(27), transformToMaxX(7));
    m.addMass(m1);
    Mass m2 = new Mass(m, transformToMaxX(27), transformToMaxX(27));
    m.addMass(m2);
    Mass m3 = new Mass(m, transformToMaxX(9), transformToMaxX(27));
    m.addMass(m3);
    m.addMuscle(new Muscle(m, m0, m1, 0.0, 0.0));
    m.addMuscle(new Muscle(m, m1, m2, 0.0, 0.0));
    m.addMuscle(new Muscle(m, m2, m3, 0.0, 0.0));
    m.addMuscle(new Muscle(m, m3, m0, 0.0, 0.0));
    return m;
  }
  public static void main(String[] args) {
    TestRunner.run(new TestSuite(wodka.test.ModelCompareTest.class));
  }
}