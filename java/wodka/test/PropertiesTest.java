/*
 * Created on Apr 19, 2004
 */
package wodka.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.TestCase;
import wodka.util.IoUtil;
import wodka.util.PropertyManager;

/**
 * Testcases for Properties.
 *  
 */
public class PropertiesTest extends TestCase {

    private static final String TMP_DIR = "c:/tmp";

    public static void main(String[] args) {
        junit.textui.TestRunner.run(PropertiesTest.class);
    }

    public void testCreateDeleteTempDir() {
        String path = TMP_DIR + "/proptest";
        File dir = createDir(path);
        assertTrue("File does not exist", dir.exists());
        deleteDir(dir);
        assertDeleted(path);
    }

    public void testGetProp() throws IOException {
        File dir = null;
        try {
            dir = createDir(TMP_DIR + "/proptest");
            testGetProp(dir);
        } finally {
            if (dir != null) {
                deleteDir(dir);
                assertDeleted(TMP_DIR + "/proptest");
            }
        }
    }

    public void testMultiLineComment() throws IOException {
        File dir = null;
        try {
            dir = createDir(TMP_DIR + "/proptest");
            testMultiLineComment(dir);
        } finally {
            if (dir != null) {
                deleteDir(dir);
                assertDeleted(TMP_DIR + "/proptest");
            }
        }
    }

    public void testChangedFile() throws IOException {
        File dir = null;
        try {
            dir = createDir(TMP_DIR + "/proptest");
            testChangedFile(dir);
        } finally {
            if (dir != null) {
                deleteDir(dir);
                assertDeleted(TMP_DIR + "/" + "proptest");
            }
        }
    }

    private void testGetProp(File dir) throws IOException,
            FileNotFoundException {
        PropertyManager m = new PropertyManager();
        File file = new File(dir, "test.properties");
        m.setFile(file);
        this.addDescs(m);
        assertEquals("hallo", m.getProperty("test"));
        assertEquals("hallo Wolfi", m.getProperty("test1"));
        assertEquals("hallo Wolfi Nodesc", m.getProperty("test2"));
        assertFileContent(file);
    }

    private void testMultiLineComment(File dir) throws IOException,
            FileNotFoundException {
        PropertyManager m = new PropertyManager();
        File file = new File(dir, "test.properties");
        m.setFile(file);
        addMultiLineDescs(m);
        assertEquals("hallo", m.getProperty("test"));
        assertEquals("hallo Wolfi", m.getProperty("test1"));
        assertEquals("hallo Wolfi Nodesc", m.getProperty("test2"));
        assertFileContentForMultiline(file);
    }

    private void testChangedFile(File dir) throws IOException,
            FileNotFoundException {
        File file = new File(dir, "test.properties");
        IoUtil.current().stringToFile("test=hallo DJ\n", file);
        PropertyManager m = new PropertyManager();
        m.setFile(file);
        addDescs(m);
        assertEquals("hallo DJ", m.getProperty("test"));
        assertEquals("hallo Wolfi", m.getProperty("test1"));
        assertEquals("hallo Wolfi Nodesc", m.getProperty("test2"));
        assertFileContentForChangedFileTest(file);
    }

    private void assertFileContent(File file) throws FileNotFoundException,
            IOException {
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            BufferedReader bReader = new BufferedReader(reader);
            String line = bReader.readLine();
            assertEquals("", line);
            line = bReader.readLine();
            assertEquals("", line);
            line = bReader.readLine();
            assertEquals("# Beschreibung für test", line);
            line = bReader.readLine();
            assertEquals("test=hallo", line);
            line = bReader.readLine();
            assertEquals("", line);
            line = bReader.readLine();
            assertEquals("", line);
            line = bReader.readLine();
            assertEquals("", line);
            line = bReader.readLine();
            assertEquals("", line);
            line = bReader.readLine();
            assertEquals("# Beschreibung für test1", line);
            line = bReader.readLine();
            assertEquals("test1=hallo Wolfi", line);
            line = bReader.readLine();
            assertEquals("", line);
            line = bReader.readLine();
            assertEquals("test2=hallo Wolfi Nodesc", line);
            line = bReader.readLine();
            assertNull(line);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private void assertFileContentForMultiline(File file)
            throws FileNotFoundException, IOException {
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            BufferedReader bReader = new BufferedReader(reader);
            String line = bReader.readLine();
            assertEquals("", line);
            line = bReader.readLine();
            assertEquals("# Beschreibung für test 1.Zeile", line);
            line = bReader.readLine();
            assertEquals("# Beschreibung für test 2.Zeile", line);
            line = bReader.readLine();
            assertEquals("# Beschreibung für test 3.Zeile", line);
            line = bReader.readLine();
            assertEquals("test=hallo", line);
            line = bReader.readLine();
            assertEquals("", line);
            line = bReader.readLine();
            assertEquals("# Beschreibung für test1", line);
            line = bReader.readLine();
            assertEquals("test1=hallo Wolfi", line);
            line = bReader.readLine();
            assertEquals("", line);
            line = bReader.readLine();
            assertEquals("test2=hallo Wolfi Nodesc", line);
            line = bReader.readLine();
            assertNull(line);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private void assertFileContentForChangedFileTest(File file)
            throws FileNotFoundException, IOException {
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            BufferedReader bReader = new BufferedReader(reader);
            String line = bReader.readLine();
            assertEquals("test=hallo DJ", line);
            line = bReader.readLine();
            assertEquals("", line);
            line = bReader.readLine();
            assertEquals("# Beschreibung für test1", line);
            line = bReader.readLine();
            assertEquals("test1=hallo Wolfi", line);
            line = bReader.readLine();
            assertEquals("", line);
            line = bReader.readLine();
            assertEquals("test2=hallo Wolfi Nodesc", line);
            line = bReader.readLine();
            assertNull(line);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private void addDescs(PropertyManager manager) {
        manager.addDesc("test", "hallo", "Beschreibung für test", 2, 3);
        manager.addDesc("test1", "hallo Wolfi", "Beschreibung für test1");
        manager.addDesc("test2", "hallo Wolfi Nodesc", null);
    }

    private void addMultiLineDescs(PropertyManager manager) {
        manager.addDesc("test", "hallo", "Beschreibung für test 1.Zeile",
                "Beschreibung für test 2.Zeile",
                "Beschreibung für test 3.Zeile");
        manager.addDesc("test1", "hallo Wolfi", "Beschreibung für test1");
        manager.addDesc("test2", "hallo Wolfi Nodesc", null);
    }

    private void assertDeleted(String path) {
        File dir = new File(path);
        assertTrue("File exists. " + dir.getAbsolutePath(), !dir.exists());
    }

    private void deleteDir(File dir) {
        IoUtil.current().deleteDir(dir);
    }

    private File createDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!dir.isDirectory()) {
            throw new Error(path + " defines not a directory.");
        }
        if (dir.list().length > 0) {
            throw new Error(path + " is not empty." + dir.list());
        }
        return dir;
    }

    public PropertiesTest(String name) {
        super(name);
    }

}