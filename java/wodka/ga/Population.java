package wodka.ga;

import java.util.*;
import java.io.*;

/**
 * A Population of Individuals. They might be sorted by Fitness.
 */

public class Population implements wodka.util.StreamPersistable,
        java.lang.Cloneable {

    private static final long serialVersionUID = 1L;

    private List individuals = new Vector();

    private int number = 0;

    public List getIndividuals() {
        return individuals;
    }

    public void setIndividuals(List individuals) {
        this.individuals = individuals;
    }

    public Population() {
        super();
    }

    public Individual getIndividual(int index) {
        Individual indi = null;
        if (index < individuals.size()) {
            indi = (Individual) individuals.get(index);
        }
        return indi;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return this.number;
    }

    public void addIndividual(Individual indi) {
        individuals.add(indi);
    }

    public int size() {
        return individuals.size();
    }

    public void sort() {
        SortedSet sorted = new TreeSet();
        Iterator iter = individuals.iterator();
        while (iter.hasNext()) {
            sorted.add(iter.next());
        }
        this.individuals = new Vector();
        iter = sorted.iterator();
        while (iter.hasNext()) {
            this.addIndividual((Individual) iter.next());
        }
    }

    public void removeAllIndividuals() {
        individuals = new Vector();
    }

    public Population createClone() {
        try {
            return (Population) clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public String toInfoString() {
        return "" + number;
    }

    public void toStream(DataOutputStream outStream) throws IOException {
        wodka.util.StreamPersistor.collectionToStream(individuals, outStream);
        outStream.writeInt(this.number);
    }

    public void toStreamLimited(DataOutputStream outStream) throws IOException {
        List limited = new ArrayList();
        if (!this.individuals.isEmpty()) {
            limited.add(individuals.get(0));
        }
        wodka.util.StreamPersistor.collectionToStream(limited, outStream);
        outStream.writeInt(this.number);
    }

    public void fromStream(DataInputStream inStream, int version)
            throws IOException {
        individuals = (Vector) wodka.util.StreamPersistor
                .collectionFromStream(inStream);
        this.number = inStream.readInt();
    }

    public int getVersion() {
        return 0;
    }
}