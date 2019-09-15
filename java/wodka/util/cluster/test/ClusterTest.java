/*
 * Created on Feb 19, 2004
 */
package wodka.util.cluster.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import junit.framework.TestCase;
import wodka.ga.model.Mass;
import wodka.ga.model.Model;
import wodka.ga.model.Muscle;
import wodka.util.cluster.Clusterizer;

/**
 * Test for clusterisation.
 * 
 * @author wwagner4
 */
public class ClusterTest extends TestCase {

    public void testTwoClustersComplete() {
        Collection col = createTwoClustersComplete();
        Collection clusters = Clusterizer.current().clusterize(col);
        assertTwoClusters(clusters);
    }
    public void testTwoClustersFragmentary() {
        Collection col = createTwoClustersFragmentary();
        Collection clusters = Clusterizer.current().clusterize(col);
        assertTwoClusters(clusters);
    }
    public void testTwoClustersInModel() {
        Model m1 = createTwoClusterModel();
        m1.rewiseInvalidMassesAndMuscles();
        assertTwoClusterModel(m1);
    }

    private Model createTwoClusterModel() {
        Model m = new Model();
        Mass m0 = new Mass(m, 0, 0);
        m.addMass(m0);
        Mass m1 = new Mass(m, 0, 1);
        m.addMass(m1);
        Mass m2 = new Mass(m, 1, 0);
        m.addMass(m2);
        Mass m3 = new Mass(m, 20, 10);
        m.addMass(m3);
        Mass m4 = new Mass(m, 10, 20);
        m.addMass(m4);
        m.addMuscle(new Muscle(m, m0, m1, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m0, m2, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m3, m4, 0.0, 0.0));
        m.addMuscle(new Muscle(m, m4, m3, 0.0, 0.0));
        m.rewiseInvalidMassesAndMuscles();
        return m;
    }
    private void assertTwoClusterModel(Model m) {
        assertEquals(3, m.getMasses().size());
        {
            Mass mass = m.getMass(0);
            assertEquals(0, mass.getXPos());
            assertEquals(0, mass.getYPos());
        }
        {
            Mass mass = m.getMass(1);
            assertEquals(0, mass.getXPos());
            assertEquals(1, mass.getYPos());
        }
        {
            Mass mass = m.getMass(2);
            assertEquals(1, mass.getXPos());
            assertEquals(0, mass.getYPos());
        }
        assertEquals(2, m.getMuscleCount());
        {
            Muscle musc =m.getMuscle(0);
            assertEquals("m0", musc.getFromMass().getIdentifier());
            assertEquals("m1", musc.getToMass().getIdentifier());
        }
        {
            Muscle musc =m.getMuscle(1);
            assertEquals("m0", musc.getFromMass().getIdentifier());
            assertEquals("m2", musc.getToMass().getIdentifier());
        }
    }
    private Collection createTwoClustersComplete() {
        TestNode a = new TestNode("A");
        TestNode b = new TestNode("B");
        TestNode c = new TestNode("C");
        TestNode x = new TestNode("X");
        TestNode y = new TestNode("Y");
        TestNode z = new TestNode("Z");
        a.addLink(b);
        a.addLink(c);
        b.addLink(a);
        b.addLink(c);
        c.addLink(a);
        c.addLink(b);
        x.addLink(y);
        x.addLink(z);
        z.addLink(x);
        y.addLink(x);
        Collection col = new ArrayList();
        col.add(a);
        col.add(b);
        col.add(c);
        col.add(x);
        col.add(y);
        col.add(z);
        return col;
    }
    private Collection createTwoClustersFragmentary() {
        TestNode a = new TestNode("A");
        TestNode b = new TestNode("B");
        TestNode c = new TestNode("C");
        TestNode x = new TestNode("X");
        TestNode y = new TestNode("Y");
        TestNode z = new TestNode("Z");
        a.addLink(b);
        a.addLink(c);
        b.addLink(c);
        x.addLink(y);
        x.addLink(z);
        Collection col = new ArrayList();
        col.add(a);
        col.add(b);
        col.add(c);
        col.add(x);
        col.add(y);
        col.add(z);
        return col;
    }

    private void assertTwoClusters(Collection clusters) {
        assertEquals(2, clusters.size());
        Iterator iter = clusters.iterator();
        Set c1 = (Set) iter.next();
        Set c2 = (Set) iter.next();
        String[] ids1 = { "A", "B", "C" };
        String[] ids2 = { "X", "Y", "Z" };
        assertCluster(ids1, c1);
        assertCluster(ids2, c2);
    }

    private void assertCluster(String[] ids, Collection c1) {
        Collection names = names(c1);
        assertEquals(ids.length, c1.size());
        for (int i = 0; i < ids.length; i++) {
            assertTrue(names.contains(ids[i]));
        }
    }

    /**
     * Extracts a collection of ids(String) from a collection of TestNodes.
     */
    private Collection names(Collection c1) {
        Iterator iter = c1.iterator();
        Collection names = new ArrayList();
        while (iter.hasNext()) {
            TestNode n = (TestNode) iter.next();
            names.add(n.getId());
        }
        return names;
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(ClusterTest.class);
    }

}
