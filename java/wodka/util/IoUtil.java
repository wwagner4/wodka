/*
 * Created on 12.12.2003
 * 
 * To change the template for this generated file go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 * Comments
 */
package wodka.util;

import java.io.*;
import java.net.*;

import wodka.ApplicationManager;

/**
 * @author wwagner4
 *  
 */
public class IoUtil {

    private static IoUtil current = null;

    private static ApplicationManager manager = ApplicationManager.current();

    private IoUtil() {
        super();
    }

    public static IoUtil current() {
        if (current == null){
            current = new IoUtil();
        }
        return current;
    }
    public void inputStreamToOutputStream(InputStream inStream, OutputStream outStream) throws IOException {
        BufferedInputStream bin = new BufferedInputStream(inStream);
        int cha = bin.read();
        while (cha != -1) {
            outStream.write(cha);
            cha = bin.read();
        }
    }
    public void stringToFile(String str, File outFile) throws IOException {
        FileWriter fWriter = null;
        try {
            fWriter = new FileWriter(outFile);
            StringReader sReader = new StringReader(str);
            readerToWriter(sReader, fWriter);
        } finally {
            if (fWriter != null) {
                fWriter.close();
            }
        }
    }
    public String resourceToString(String resName) throws IOException {
        URL url = IoUtil.current().loadResourceStrict(resName);
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(url.openStream());
            StringWriter sWriter = new StringWriter();
            readerToWriter(reader, sWriter);
            return sWriter.getBuffer().toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
    public void readerToWriter(Reader reader, Writer writer) throws IOException {
        BufferedReader bReader = new BufferedReader(reader);
        PrintWriter pWriter = new PrintWriter(writer);
        String line = bReader.readLine();
        while (line != null && bReader.ready()) {
            pWriter.print(line);
            pWriter.println();
            line = bReader.readLine();
        }
        writer.flush();
    }

    public void deleteDirContents(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (int index = 0; index < files.length; index++) {
                deleteDir(files[index]);
            }
        } else {
            throw new Error(dir.getAbsolutePath() + " is no directory.");
        }
    }
    public void deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (int index = 0; index < files.length; index++) {
                deleteDir(files[index]);
            }
        }
        deleteFile(dir);
    }

    private void deleteFile(File file) throws Error {
        if (file.exists()) {
            boolean result = file.delete();
            if (!result) {
                throw new Error("Could not delete " + file.getAbsolutePath());
            }
        }
    }
    public File createDirAtHome(String name) {
        return createDir(manager.homeDirectory(), name);
    }

    public File createDir(File baseDir, String name) {
        File dir = new File(baseDir, name);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public void resourceToDir(String resourceName, File dir, String fileName) throws IOException {
        if (!dir.isDirectory()) {
            throw new Error(dir.getAbsolutePath() + " is no directory.");
        }
        InputStream inStream = null;
        try {
            inStream = this.openResource(resourceName);
            inputStreamToFile(inStream, new File(dir, fileName));
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
    }
    public void inputStreamToFile(InputStream inStream, File file) throws IOException {
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(file);
            inputStreamToOutputStream(inStream, outStream);
        } finally {
            if (outStream != null) {
                outStream.close();
            }
        }
    }

    public java.net.URL loadResource(String name) {
        return this.getClass().getClassLoader().getResource(name);
    }

    public java.net.URL loadResourceStrict(String name) {
        java.net.URL url = loadResource(name);
        if (url == null)
            throw new Error("Resource not found: " + name);
        return url;
    }
    
    public InputStream openResource(String name) throws IOException {
        java.net.URL url = loadResource(name);
        if (url == null) {
            throw new Error("Resource " + name + " could not be opened.");
        }
        return url.openStream();
    }
}
