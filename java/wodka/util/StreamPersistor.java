package wodka.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import wwan.commons.param.Configurable;
import wwan.commons.param.ListParam;
import wwan.commons.param.Param;
import wwan.commons.param.ParamException;

/**
 * Static Methods to be used for StreamPersistable Objects.
 */

public class StreamPersistor {

    private static final String NULL_STRING = ".se-";

    public static void toStream(StreamPersistable p, DataOutputStream s)
            throws IOException {
        if (p == null) {
            stringToStream(NULL_STRING, s);
        } else {
            String className = p.getClass().getName();
            stringToStream(className, s);
            s.writeInt(p.getVersion());
            p.toStream(s);
        }
    }

    public static void toStream(StreamPersistable p, OutputStream s)
            throws IOException {
        StreamPersistor.toStream(p, new DataOutputStream(s));
    }

    public static StreamPersistable fromStream(DataInputStream s)
            throws IOException {
        String className = stringFromStream(s);
        if (className == null)
            return null;
        if (className.length() == 0)
            throw new Error("Classname was empty on loading StreamPersistable."
                    + "\nThe problem was probably caused during save.");
        try {
            if (className.equals(NULL_STRING))
                return null;
            int version = s.readInt();
            Object o = Class.forName(className).newInstance();
            StreamPersistable p = (StreamPersistable) o;
            p.fromStream(s, version);
            return p;
        } catch (InstantiationException ex) {
            throw new IOException("Problem with: " + className + "\n   "
                    + ex.toString());
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Problem with: " + className + "\n   "
                    + ex.toString(), ex);
        } catch (IllegalAccessException ex) {
            throw new IOException("Problem with: " + className
                    + "\n   Class is probably not declared  public. "
                    + ex.getMessage());
        }
    }

    public static StreamPersistable fromStream(InputStream s)
            throws IOException {
        return StreamPersistor.fromStream(new DataInputStream(s));
    }

    public static void fillFromStream(StreamPersistable p, DataInputStream s)
            throws IOException {
        String className = stringFromStream(s);
        if (className.equals("null")) {
            p = null;
            return;
        }
        int version = s.readInt();
        p.fromStream(s, version);
    }

    public static void stringToStream(String str, DataOutputStream s)
            throws IOException {
        if (str == null)
            stringToStream(NULL_STRING, s);
        else {
            s.writeInt(str.length());
            for (int i = 0; i < str.length(); i++) {
                s.writeChar(str.charAt(i));
            }
        }
    }

    public static String stringFromStream(DataInputStream s) throws IOException {
        int length = s.readInt();
        StringBuffer strBuf = new StringBuffer(length);
        for (int i = 0; i < length; i++) {
            strBuf.append(s.readChar());
        }
        String out = strBuf.toString();
        String result = null;
        if (!out.equals(NULL_STRING)){
            result = out;
        }
        return result;
    }

    public static void configurableToStream(Configurable conf,
            DataOutputStream s) throws IOException {
        try {
            ListParam lp = conf.getParameterDescription().createListParam();
            conf.setParameterListFromFields(lp);
            for (int i = 0; i < lp.atomicSize(); i++) {
                Param param = lp.getAtomicParam(i);
                Object obj = param.getValue();
                if (obj instanceof StreamPersistable) {
                    s.writeChar('p');
                    StreamPersistable sp = (StreamPersistable) obj;
                    sp.toStream(s);
                } else if (obj instanceof Serializable) {
                    s.writeChar('s');
                    Serializable ser = (Serializable) obj;
                    ObjectOutputStream oout = new ObjectOutputStream(s);
                    oout.writeObject(ser);
                } else {
                    throw new Error(i + "th parameter of "
                            + conf.getClass().getName()
                            + " cannot be persisted.");
                }
            }
        } catch (ParamException e) {
            throw new Error(e.toString());
        }
    }

    public static void configurableFromStream(Configurable conf,
            DataInputStream s) throws IOException {
        try {
            ListParam lp = conf.getParameterDescription().createListParam();
            for (int i = 0; i < lp.atomicSize(); i++) {
                Param param = lp.getAtomicParam(i);
                char c = s.readChar();
                switch (c) {
                case 'c':
                    param.setValue(StreamPersistor.fromStream(s));
                    break;
                case 's':
                    ObjectInputStream oin = new ObjectInputStream(s);
                    param.setValue(oin.readObject());
                    break;
                default:
                    throw new Error("Invalid code '" + c + "'");
                }
            }
            conf.setFieldsFromParameterList(lp);
        } catch (ParamException e) {
            throw new Error(e.toString());
        } catch (ClassNotFoundException e) {
            throw new Error(e.toString());
        }
    }

    public static void collectionToStream(Collection c, DataOutputStream s)
            throws IOException {
        if (c == null) {
            stringToStream("null", s);
        } else {
            String className = c.getClass().getName();
            stringToStream(className, s);
            s.writeInt(c.size());
            Iterator i = c.iterator();
            while (i.hasNext()) {
                StreamPersistor.toStream((StreamPersistable) i.next(), s);
            }
        }
    }

    public static Collection collectionFromStream(DataInputStream s)
            throws IOException {
        String className = stringFromStream(s);
        if (className.equals("null"))
            return null;
        Collection c = new Vector();
        int size = s.readInt();
        for (int i = 0; i < size; i++) {
            c.add(StreamPersistor.fromStream(s));
        }
        return c;
    }

    public static void fillCollectionFromStream(Collection c, DataInputStream s)
            throws IOException {
        String className = stringFromStream(s);
        if (className.equals("null")) {
            c = null;
            return;
        }
        if (!c.getClass().getName().equals(className))
            throw new IOException("Invalid collection class: "
                    + c.getClass().getName() + ". Expected: " + className);
        c.removeAll(c);
        int size = s.readInt();
        for (int i = 0; i < size; i++) {
            c.add(StreamPersistor.fromStream(s));
        }
    }

}