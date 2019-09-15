/*
 * PersistanceHandler.java
 * 
 * Created on 27. helmikuuta 2003, 10:58
 */

package wodka.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import wodka.ga.GeneticAlgorithm;

/**
 * @author wwagner4
 */
public class PersistanceHandler {

    private static PersistanceHandler current = null;

    /** Creates a new instance of PersistanceHandler */
    private PersistanceHandler() {
        super();
    }

    public static PersistanceHandler current() {
        if (current == null)
            current = new PersistanceHandler();
        return current;
    }

    public GeneticAlgorithm load(File gZipFile) throws IOException {
        return (GeneticAlgorithm) loadStreamPersistable(gZipFile);
    }

    public StreamPersistable loadStreamPersistable(File gZipFile)
            throws IOException {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(gZipFile);
            GZIPInputStream in = null;
            in = new GZIPInputStream(fin);
            StreamPersistable object = wodka.util.StreamPersistor
                    .fromStream(new DataInputStream(in));
            in.close();
            return object;
        } finally {
            if (fin != null)
                fin.close();
        }
    }

    public void save(StreamPersistable ga, File gZipFile) throws IOException {
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(gZipFile);
            GZIPOutputStream out = new GZIPOutputStream(fout);
            wodka.util.StreamPersistor.toStream(ga, new DataOutputStream(out));
            out.close();
        } finally {
            if (fout != null)
                fout.close();
        }
    }

    public void saveObject(Object obj, File gZipFile) throws IOException {
        if (obj instanceof StreamPersistable) {
            FileOutputStream fout = null;
            try {
                fout = new FileOutputStream(gZipFile);
                GZIPOutputStream out = new GZIPOutputStream(fout);
                wodka.util.StreamPersistor.toStream((StreamPersistable) obj,
                        new DataOutputStream(out));
                out.close();
            } finally {
                if (fout != null)
                    fout.close();
            }
        } else {
            throw new Error(
                    "Cannot persist: "
                            + obj.getClass().getName()
                            + " because it does not implement wodka.util.StreamPersistable");
        }
    }

}