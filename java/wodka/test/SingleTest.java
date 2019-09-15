package wodka.test;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Class to run a single test if it causes problems.
 */

public class SingleTest extends TestCase {

    public SingleTest(String str) {
        super(str);
    }
    public static void main(String[] args) {
        TestRunner.run(new TestSuite(wodka.test.SingleTest.class));
    }
}