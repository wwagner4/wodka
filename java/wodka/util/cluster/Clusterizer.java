/*
 * Created on Feb 18, 2004
 */
package wodka.util.cluster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Finds clusters in a model. Removes all except the biggest cluster. A cluster is a set of masses that are connected
 * by springs or muscles.
 *  
 */
public class Clusterizer {

    private static Clusterizer current = null;

    private Clusterizer() {
        super();
    }

    public static Clusterizer current() {
        if (current == null)
            current = new Clusterizer();
        return current;
    }

    /**
     * Returns a collection of collections each representing a cluster. The collections contain the nodes that belong
     * to a cluster.
     * 
     */
    public Collection clusterize(Collection nodes) {
        Collection clusters = new ArrayList();
        Iterator iter = nodes.iterator();
        while (iter.hasNext()) {
            Node n = (Node) iter.next();
            categorize(clusters, n);
        }
        return clusters;

    }

    private void categorize(Collection clusters, Node n) {
        //info("categorize "+n);
        Set newCluster = searchContainingCluster(clusters, n);
        if (newCluster == null) {
            //info("categorize new");
            newCluster = new HashSet();
            newCluster.add(n);
            clusters.add(newCluster);
        }
        Iterator iter = n.links().iterator();
        while (iter.hasNext()) {
            Node ln = (Node) iter.next();
            Collection oldCluster = searchContainingCluster(clusters, ln);
            if (oldCluster != null && oldCluster != newCluster) {
                //info("found link node in old cluster "+ln+" clusters:"+clusters);
                mergeClusters(clusters, newCluster, oldCluster);
            }
            newCluster.add(ln);
        }
        //info("categorized "+n+" clusters:"+clusters);
    }

    private void mergeClusters(Collection clusters, Set newCluster, Collection oldCluster) {
        clusters.remove(oldCluster);
        newCluster.addAll(oldCluster);
    }

    private Set searchContainingCluster(Collection clusters, Node n) {
        //info("search node:" + n + "\nclusters:" + clusters);
        Iterator iter = clusters.iterator();
        while (iter.hasNext()) {
            Set c = (Set) iter.next();
            if (c.contains(n))
                return c;
        }
        //info("NOT FOUND");
        return null;
    }

}
