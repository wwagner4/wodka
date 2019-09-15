package wodka.test;

import java.io.*;
import java.util.*;

/**
 * A dummy class for the fillCollectionFromStream( ... ).
 */

public class DummyPersistable implements wodka.util.StreamPersistable {

    private static final long serialVersionUID = 1L;

    private Collection coll = new Vector();

    private String nam = null;

    public DummyPersistable() {
        super();
    }

    public void setNam(String val) {
        nam = val;
    }

    public String getNam() {
        return nam;
    }

    public void add(Object o) {
        coll.add(o);
    }

    public Collection getCollection() {
        return coll;
    }

    public void toStream(DataOutputStream s) throws IOException {
        wodka.util.StreamPersistor.stringToStream(nam, s);
        wodka.util.StreamPersistor.collectionToStream(coll, s);
    }

    public void fromStream(DataInputStream s, int version) throws IOException {
        nam = wodka.util.StreamPersistor.stringFromStream(s);
        wodka.util.StreamPersistor.fillCollectionFromStream(coll, s);
    }

    public int getVersion() {
        return 0;
    }
}