package wodka.ga.model;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.log4j.Logger;

import wodka.util.cluster.Clusterizer;
import wodka.util.cluster.Node;

public class Model {

    private List masses = new Vector();
    private List muscles = new Vector();
    private double envGravity;
    private double envFriction;
    private double envSpringyness;
    private double waveAplitude;
    private double wavePhase;
    private double waveSpeed;
    private String description = null;

    public static final int WIDTH = 256;
    private int identifier = -1;
    private static Logger logger = Logger.getLogger(Model.class);

    public Model() {
        super();
    }
    public void addMass(Mass mass) {
        masses.add(mass);
    }
    public void addMuscle(Muscle musc) {
        muscles.add(musc);
    }
    public java.util.List getMasses() {
        return masses;
    }
    public java.util.List getMuscles() {
        return muscles;
    }
    public int getMassCount() {
        return masses.size();
    }
    public int getMuscleCount() {
        return muscles.size();
    }

    public Mass getMass(int index) {
        return (Mass) masses.get(index);
    }
    public Muscle getMuscle(int index) {
        return (Muscle) muscles.get(index);
    }

    /**
     * Removes masses that are overlapping, muscles that reference invalid indexes of masses, muscles that start and
     * end at the same mass, masses that are not referenced by any musclesAddMuscle.
     */
    public void rewiseInvalidMassesAndMuscles() {
        this.correctOutOfRangePositions();
        this.rewiseOverlappingMasses();
        this.rewiseMultipleMuscles();
        this.rewiseLoopMuscles();
        rewiseMinorClusters();
        removeMusclesWithInvalidMasses();
        //        this.reviseInvalidMuscles();
        //        this.reviseMassesWithNoMuscles();
    }

    private void rewiseLoopMuscles() {
        Collection indexes = new ArrayList();
        for (int i = 0; i < this.getMuscleCount(); i++) {
            Muscle musc = getMuscle(i);
            if (musc.getFromMass().getIdentifier().equals(musc.getToMass().getIdentifier())) {
                indexes.add(new Integer(i));
            }
        }
        List list = new ArrayList(indexes);
        Collections.reverse(list);
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Integer inte = (Integer) iter.next();
            muscles.remove(inte.intValue());
        }
    }
    private void rewiseMultipleMuscles() {
        Collection pairs = new ArrayList();
        SortedSet indexes = new TreeSet();
        for (int i = 0; i < this.getMuscleCount(); i++) {
            Muscle musc = getMuscle(i);
            StringPair pair = new StringPair(musc.getFromMass().getIdentifier(), musc.getToMass().getIdentifier());
            if (pairs.contains(pair)) {
                indexes.add(new Integer(i));
            } else {
                pairs.add(pair);
            }
        }
        List list = null;
        try {
            list = new ArrayList(indexes);
        } catch (NoSuchElementException exc) {
            dump(indexes);
            throw exc;
        }
        Collections.reverse(list);
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Integer inte = (Integer) iter.next();
            muscles.remove(inte.intValue());
        }
    }
    /**
     * Only for finding the sporadic error.
     */
    private void dump(SortedSet indexes) {
        info("indexes:" + indexes);
        Iterator iter = indexes.iterator();
        int index = 0;
        while (iter.hasNext()) {
            info("index: " + index);
            Object obj = iter.next();
            if (obj == null) {
                info("Element was null");
            } else {
                info("Element class: " + obj.getClass().getName() + " value: "+obj.toString());
            }
            index++;
        }
    }
    private void info(String string) {
        logger.error(string);
    }
    private class StringPair implements Comparable {

        private transient String aVal = null;
        private transient String bVal = null;

        public StringPair(String aVal, String bVal) {
            this.aVal = aVal;
            this.bVal = bVal;
        }
        public boolean equals(Object obj) {
            return this.compareTo(obj) == 0;
        }
        public int compareTo(Object obj) {
            StringPair pair = (StringPair) obj;
            String xVal = aVal + bVal;
            String yVal = pair.aVal + pair.bVal;
            return xVal.compareTo(yVal);
        }
        public int hashCode() {
            return aVal.hashCode() + bVal.hashCode();
        }
        public String toString() {
            return "<" + aVal + "," + bVal + ">";
        }
    }
    private void correctOutOfRangePositions() {
        Iterator iter = masses.iterator();
        while (iter.hasNext()) {
            Mass mass = (Mass) iter.next();
            if (mass.getXPos() < 0) {
                mass.setXPos(0);
            }
            if (mass.getYPos() < 0) {
                mass.setYPos(0);
            }
            if (mass.getXPos() >= Model.WIDTH) {
                mass.setXPos(Model.WIDTH);
            }
            if (mass.getYPos() >= Model.WIDTH) {
                mass.setYPos(Model.WIDTH);
            }
        }
    }

    private void rewiseOverlappingMasses() {
        for (int i = 0; i < getMassCount(); i++) {
            Mass remaining = (Mass) masses.get(i);
            for (int j = i + 1; j < getMassCount(); j++) {
                Mass obsolete = (Mass) masses.get(j);
                if (isOverlapping(remaining, obsolete)) {
                    correctMuscles(remaining, obsolete);
                    this.removeMass(obsolete.getIdentifier());
                }
            }
        }
    }

    private void removeMusclesWithInvalidMasses() {
        Collection massIds = new ArrayList();
        for (int i = 0; i < getMassCount(); i++) {
            Mass mass = getMass(i);
            massIds.add(mass.getIdentifier());
        }
        List muscIndexes = new ArrayList();
        for (int i = 0; i < getMuscleCount(); i++) {
            Muscle musc = getMuscle(i);
            if (!massIds.contains(musc.getFromMass().getIdentifier())){
                muscIndexes.add(new Integer(i));
            }
            else if (!massIds.contains(musc.getToMass().getIdentifier())){
                muscIndexes.add(new Integer(i));
            }
        }
        Collections.reverse(muscIndexes);
        Iterator iter = muscIndexes.iterator();
        while (iter.hasNext()) {
            Integer index = (Integer) iter.next();
            muscles.remove(index.intValue());
        }
    }
    private boolean isOverlapping(Mass massA, Mass massB) {
        int xDiff = Math.abs(massA.getXPos() - massB.getXPos());
        int yDiff = Math.abs(massA.getYPos() - massB.getYPos());
        return xDiff + yDiff == 0;
    }

    private void correctMuscles(Mass remaining, Mass obsolete) {
        String obsId = obsolete.getIdentifier();
        Iterator iter = muscles.iterator();
        while (iter.hasNext()) {
            Muscle musc = (Muscle) iter.next();
            if (musc.getFromMass().getIdentifier().equals(obsId)) {
                musc.setFromMass(remaining);
            }
            if (musc.getToMass().getIdentifier().equals(obsId)) {
                musc.setToMass(remaining);
            }
        }
    }
    private void removeMass(String id) {
        for (int i = 0; i < this.getMuscleCount(); i++) {
            Muscle musc = getMuscle(i);
            if (musc.getFromMass().getIdentifier().equals(id)
                || musc.getToMass().getIdentifier().equals(id)) {
                this.muscles.remove(musc);
            }
        }
        Mass mass = getMassFromId(id);
        this.masses.remove(mass);
    }
    public void dump(PrintStream stream) {
        stream.println("DUMP model:");
        stream.print("masses:(");
        Iterator muscIter = masses.iterator();
        while (muscIter.hasNext()) {
            Mass mass = (Mass) muscIter.next();
            stream.print(mass);
        }
        stream.println(")");
        muscIter = muscles.iterator();
        stream.print("muscles:(");
        while (muscIter.hasNext()) {
            Muscle musc = (Muscle) muscIter.next();
            stream.print(musc);
        }
        stream.println(")");
    }

    public double getEnvFriction() {
        return envFriction;
    }
    public double getEnvGravity() {
        return envGravity;
    }
    public double getEnvSpringyness() {
        return envSpringyness;
    }
    public double getWaveAplitude() {
        return waveAplitude;
    }
    public double getWavePhase() {
        return wavePhase;
    }
    public double getWaveSpeed() {
        return waveSpeed;
    }
    public void setEnvFriction(double friction) {
        envFriction = friction;
    }
    public void setEnvGravity(double grav) {
        envGravity = grav;
    }
    public void setEnvSpringyness(double springy) {
        envSpringyness = springy;
    }
    public void setWaveAplitude(double ampl) {
        waveAplitude = ampl;
    }
    public void setWavePhase(double phase) {
        wavePhase = phase;
    }
    public void setWaveSpeed(double speed) {
        waveSpeed = speed;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String string) {
        description = string;
    }
    public void calculateRestlength() {
        Iterator iter = this.muscles.iterator();
        while (iter.hasNext()) {
            calculateRestlength((Muscle) iter.next());
        }
    }
    private void calculateRestlength(Muscle musc) {
        double xDiff = musc.getFromMass().getXPos() - musc.getToMass().getXPos();
        double yDiff = musc.getFromMass().getYPos() - musc.getToMass().getYPos();
        musc.setRestlength(Math.sqrt(xDiff * xDiff + yDiff * yDiff));
    }
    private void rewiseMinorClusters() {
        Collection nodes = createNodesFromModel();
        Collection clusters = Clusterizer.current().clusterize(nodes);
        //info("clusters:" + clusters);
        rewiseMinorClusters(clusters);
    }

    private void rewiseMinorClusters(Collection clusters) {
        if (!clusters.isEmpty()) {
            Collection major = majorCluster(clusters);
            //info("major:" + major);
            Collection minorNodes = sortedMassIndexesFromMinorClusters(clusters, major);
            //info("minors:" + minorNodes);
            Iterator iter = minorNodes.iterator();
            while (iter.hasNext()) {
                String id = (String) iter.next();
                removeMass(id);
            }
        }
    }

    private Collection sortedMassIndexesFromMinorClusters(Collection clusters, Collection majorCluster) {
        Collection nodes = new java.util.TreeSet();
        Iterator clustersIter = clusters.iterator();
        while (clustersIter.hasNext()) {
            Collection clust = (Collection) clustersIter.next();
            if (!clust.equals(majorCluster)) {
                Iterator nodesIter = clust.iterator();
                while (nodesIter.hasNext()) {
                    ModelNode node = (ModelNode) nodesIter.next();
                    nodes.add(node.nodeId);
                }
            }
        }
        return reverse(nodes);
    }

    private Collection reverse(Collection col) {
        List rev = new ArrayList(col);
        Collections.reverse(rev);
        return rev;
    }
    private Collection majorCluster(Collection clusters) {
        SortedMap sorted = new TreeMap();
        Iterator iter = clusters.iterator();
        while (iter.hasNext()) {
            Collection coll = (Collection) iter.next();
            sorted.put(new Integer(coll.size()), coll);
        }
        return (Collection) sorted.get(sorted.lastKey());
    }
    private Collection createNodesFromModel() {
        List nodes = new ArrayList();
        for (int i = 0; i < getMassCount(); i++) {
            nodes.add(new ModelNode(getMass(i).getIdentifier()));
        }
        Iterator nodesIter = nodes.iterator();
        while (nodesIter.hasNext()) {
            ModelNode node = (ModelNode) nodesIter.next();
            node.addLinks(this, nodes);
        }
        return nodes;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("MASSES:");
        for (int i = 0; i < this.getMassCount(); i++) {
            Mass mass = this.getMass(i);
            buffer.append(mass.toString());
        }
        buffer.append("\n");
        buffer.append("MUSCLES:");
        for (int i = 0; i < this.getMuscleCount(); i++) {
            Muscle musc = this.getMuscle(i);
            buffer.append(musc.toString());
        }
        return buffer.toString();
    }

    private class ModelNode implements Node, Comparable {

        private Collection links = new HashSet();
        private String nodeId = null;

        public int compareTo(Object obj) {
            ModelNode node = (ModelNode) obj;
            return node.nodeId.compareTo(nodeId);
        }
        public int hashCode() {
            return nodeId.hashCode();
        }

        private ModelNode() {
            super();
        }
        public ModelNode(String identifier) {
            this();
            this.nodeId = identifier;
        }

        public Collection links() {
            return links;
        }
        public void addLinks(Model model, List nodes) {
            for (int i = 0; i < model.getMuscleCount(); i++) {
                Muscle musc = model.getMuscle(i);
                if (musc.getFromMass().getIdentifier().equals(nodeId)) {
                    String toMassId = musc.getToMass().getIdentifier();
                    links.add(findNode(toMassId, nodes));
                }
            }
        }

        private Node findNode(String toMassId, Collection nodes) {
            Iterator nodesIter = nodes.iterator();
            while (nodesIter.hasNext()) {
                ModelNode node = (ModelNode) nodesIter.next();
                if (node.nodeId.equals(toMassId)){
                    return node;
                }
            }
            throw new Error("No node was found with id:'" + toMassId + "' in:" + nodes);
        }
        public String toString() {
            return "N" + this.nodeId + "(" + this.links.size() + ")";
        }

        public Collection getLinks() {
            return links;
        }

        public void setLinks(Collection links) {
            this.links = links;
        }

        public String getNodeId() {
            return nodeId;
        }

        public void setIdentifier(String identifier) {
            this.nodeId = identifier;
        }

    }
    public String nextMassId() {
        return "m" + (++identifier);
    }
    public Mass getMassFromId(String id) {
        for (int i = 0; i < this.getMassCount(); i++) {
            Mass model = getMass(i);
            if (model.getIdentifier().equals(id)){
                return model;
            }
        }
        throw new Error("No mass with id " + id + " found in model.\n" + this);
    }

    public List getMassIdentifiers() {
        List ids = new ArrayList();
        for (int i = 0; i < this.getMassCount(); i++) {
            Mass mass = this.getMass(i);
            ids.add(mass.getIdentifier());
        }
        return ids;
    }

    public void setMuscles(List muscles) {
        this.muscles = muscles;
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public void setMasses(List masses) {
        this.masses = masses;
    }

}