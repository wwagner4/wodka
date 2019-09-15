package wodka.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import wodka.util.cluster.test.ClusterTest;

public class AllWodkaTest extends TestCase {

    public AllWodkaTest(String name) {
        super(name);
    }

    
    public static Test suite() {
      TestSuite suite = new TestSuite();
      suite.addTest(new TestSuite(wodka.test.ModelTest.class));
      suite.addTest(new TestSuite(wodka.test.AssemblerTest.class));
      suite.addTest(new TestSuite(wodka.test.TurtleTest.class));
      suite.addTest(new TestSuite(wodka.test.UtilTest.class));
      suite.addTest(new TestSuite(wodka.test.GaTest.class));
      suite.addTest(new TestSuite(wodka.test.DownsizerTest.class));
      suite.addTest(new TestSuite(wodka.test.PersistanceTest.class));
      suite.addTest(new TestSuite(wodka.test.ModelCompareTest.class));
      suite.addTest(new TestSuite(wodka.test.MarshallerTest.class));
      suite.addTest(new TestSuite(ClusterTest.class));
      suite.addTest(new TestSuite(PropertiesTest.class));
      suite.addTest(new TestSuite(TournamentSelectionTest.class));
      return suite;
    }
    public static void main(String[] args) {
        TestRunner.run(suite());
    }
}