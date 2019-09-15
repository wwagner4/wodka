/*
 * Created on Feb 19, 2004
 */
package wodka.util.cluster.test;

import java.util.ArrayList;
import java.util.Collection;

import wodka.util.cluster.Node;

/**
 * Implementation of node to test the clusterizing feature.
 */
public class TestNode implements Node {

    private Collection links = new ArrayList();
    private String id = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private TestNode() {
        super();
    }
    public TestNode(String id) {
        this();
        this.id =id;
    }
    public Collection links() {
        return links;
    }
    public void addLink(Node node) {
        links.add(node);
    }
    public String toString() {
        return id;
    }

}
