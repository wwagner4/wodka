/*
 * ApplicationManager.java
 * 
 * Created on 05. Dezember 2003, 14:35
 */

package wodka;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

import wodka.util.IoUtil;
import wodka.util.PropertyManager;
import wodka.util.Util;

/**
 * @author wwagner4
 */
public class ApplicationManager {

    public static final String PROP_WEBSTART = "wodka.webstart";

    public static final String PROP_FONT_SIZE = "wodka.font.size";

    public static final String PROP_FONT = "wodka.font.name";

    public static final String PROP_LOOKFEEL = "wodka.lookandfeel";

    private static final String SODARACE_URL = "http://www.sodaplay.com/constructor/beta/racer.jnlp";

    private static final String SODACONSTRUCTOR_URL = "http://www.sodaplay.com/constructor/beta/constructor.jnlp";

    private static final String ID_FILE_NAME = "id.txt";

    private static ApplicationManager current = null;

    private transient Properties env = null;

    private transient String version = null;

    private transient File wodkaHomeDir = null;

    private transient PropertyManager propManager = null;

    /** Creates a new instance of ApplicationManager */
    private ApplicationManager() {
        initVersion();
        this.env = getEnvVars();
    }

    private void initVersion() {
        try {
            String name = "version.properties";
            java.net.URL url = IoUtil.current().loadResource(name);
            if (url == null)
                this.version = "unknown";
            else {
                java.util.Properties props = new java.util.Properties();
                props.load(url.openStream());
                this.version = props.getProperty("version");
            }
        } catch (java.io.IOException ex) {
            this.version = "unknown";
        }
    }

    public static ApplicationManager current() {
        if (current == null) {
            current = new ApplicationManager();
        }
        return current;
    }

    /**
     * Getter for property version.
     * 
     * @return Value of property version.
     *  
     */
    public java.lang.String getVersion() {
        return this.version;
    }

    public int getPropertyInt(String name) {
        String val = getProperty(name);
        if (val == null) {
            throw new Error("Property " + name + " is not defined.");
        }
        return Integer.parseInt(val);
    }

    public double getPropertyDouble(String name) {
        String val = getProperty(name);
        if (val == null) {
            throw new Error("Property " + name + " is not defined.");
        }
        return Double.parseDouble(val);
    }

    public String getProperty(String name) {
        try {
            if (this.propManager == null) {
                initProperties();
            }
            return this.propManager.getProperty(name);
        } catch (IOException e) {
            throw new Error("Property " + name + " could not be found.");
        }
    }

    private void initProperties() {
        // Create the PropertyManager.
        this.propManager = new PropertyManager();
        this.propManager.setFile(new File(this.adminDirectory(),
                "wodka.properties"));

        // Add property descriptions.
        this.propManager.addDesc(PROP_LOOKFEEL,
                "javax.swing.plaf.metal.MetalLookAndFeel",
                "Defines the look and feel for wodka GUIs");
        this.propManager.addDesc(PROP_FONT, "Tahoma",
                "Defines the font for wodka GUIs");
        this.propManager.addDesc(PROP_FONT_SIZE, "12",
                "Defines the font size for wodka GUIs");
        this.propManager
                .addDesc(
                        PROP_WEBSTART,
                        "default",
                        "Defines the path to your local webstart executable.",
                        "'DEFAULT' uses some program logic to guess where the executable can be found. Works only on windows.");
        initPropertiesLog4J();
    }

    private void initPropertiesLog4J() {
        File logFile = new File(this.adminDirectory(), "wodka.log");
        String logFileName = Util.current().completeBackslashes(
                logFile.getAbsolutePath());

        this.propManager.addDesc("log4j.rootCategory", "WARN, R",
                "Root category",
                "Some examples how to configure the rootCategory.",
                "log4j.rootCategory=WARN, LF5, C, R",
                "log4j.rootCategory=DEBUG, C, R");
        this.propManager
                .addDesc("log4j.appender.LF5",
                        "org.apache.log4j.lf5.LF5Appender",
                        "LF5 is a LF5Appender which outputs to a swing logging console. ");
        this.propManager.addDesc("log4j.appender.LF5.MaxNumberOfRecords",
                "1000");
        this.propManager.addDesc("log4j.appender.R",
                "org.apache.log4j.RollingFileAppender",
                "R is the RollingFileAppender that outputs to a log file.");
        this.propManager.addDesc("log4j.appender.R.File", logFileName);
        this.propManager.addDesc("log4j.appender.R.MaxFileSize", "100KB");
        this.propManager.addDesc("log4j.appender.R.MaxBackupIndex", "5");
        this.propManager.addDesc("log4j.appender.R.layout",
                "org.apache.log4j.PatternLayout");
        this.propManager.addDesc("log4j.appender.R.layout.ConversionPattern",
                "%d{ISO8601} - %5p -(%c:%L) : %m%n");
        this.propManager
                .addDesc("log4j.appender.C",
                        "org.apache.log4j.ConsoleAppender",
                        "C is set to be a ConsoleAppender which outputs to System.out. ");
        this.propManager.addDesc("log4j.appender.C.layout",
                "org.apache.log4j.PatternLayout");
        this.propManager.addDesc("log4j.appender.C.layout.ConversionPattern",
                "%d{ISO8601} - %5p -(%c:%L) : %m%n");
    }

    public void initLogging() {
        try {
            Properties props = new Properties();
            if (this.propManager == null) {
                initProperties();
            }
            FileInputStream fin = null;
            try {
                File confFile = this.propManager.getFile();
                fin = new FileInputStream(confFile);
                props.load(fin);
                if (props.isEmpty()) {
                    throw new Error("No properties defined in: "
                            + confFile.getAbsolutePath());
                }
                PropertyConfigurator.configure(props);
            } finally {
                if (fin != null) {
                    fin.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Using basic configuration because: "
                    + e.toString());
            BasicConfigurator.configure();
        }
    }

    public File homeDirectory() {
        if (this.wodkaHomeDir == null) {
            File home = new File(System.getProperty("user.home"));
            File myDoc = new File(home, "My Documents");
            if (myDoc.exists() && myDoc.isDirectory()) {
                home = myDoc;
            }
            this.wodkaHomeDir = new File(home, "wodka");
            if (!this.wodkaHomeDir.exists()) {
                this.wodkaHomeDir.mkdirs();
            }
        }
        return this.wodkaHomeDir;
    }

    public File testDirectory() {
        File wodkaTest = new File(homeDirectory(), "test");
        if (!wodkaTest.exists()) {
            wodkaTest.mkdirs();
        }
        return wodkaTest;
    }

    public File adminDirectory() {
        File dir = new File(homeDirectory(), "admin");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public File combiDirectory() {
        File dir = new File(homeDirectory(), "combi");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public String currentTimeTimestamp() {
        Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd-HHmmss-SS");
        return sdf.format(now);
    }

    public long nextID() {
        try {
            if (!idFile().exists()) {
                writeIdToFile(0);
            }
            FileInputStream inStream = null;
            try {
                inStream = new FileInputStream(this.idFile());
                DataInputStream din = new DataInputStream(inStream);
                long identifier = din.readLong();
                this.writeIdToFile(identifier + 1);
                return identifier;
            } finally {
                if (inStream != null) {
                    inStream.close();
                }
            }
        } catch (IOException e) {
            return System.currentTimeMillis();
        }
    }

    private void writeIdToFile(long identifier) throws FileNotFoundException,
            IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(idFile());
            DataOutputStream dout = new DataOutputStream(out);
            dout.writeLong(identifier);
            dout.flush();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private File idFile() {
        File idFile = new File(adminDirectory(), ID_FILE_NAME);
        return idFile;
    }

    public Properties getEnvVars() {
        try {
            Process proc = null;
            Properties envVars = new Properties();
            Runtime runtime = Runtime.getRuntime();
            String opSys = System.getProperty("os.name").toLowerCase();
            if (opSys.indexOf("windows 9") <= -1) {
                if ((opSys.indexOf("nt") > -1)
                        || (opSys.indexOf("windows 2000") > -1 || (opSys
                                .indexOf("windows xp") > -1))) {
                    proc = runtime.exec("cmd.exe /c set");
                }
                // our last hope, we assume Unix (thanks to H. Ware for the fix)
                else {
                    proc = runtime.exec("env");
                }
                InputStream inputStream = null;
                try {
                    inputStream = proc.getInputStream();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(inputStream));
                    String line = reader.readLine();
                    while (line != null) {
                        int idx = line.indexOf('=');
                        String key = line.substring(0, idx);
                        String value = line.substring(idx + 1);
                        envVars.setProperty(key, value);
                        line = reader.readLine();
                    }
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
            }
            return envVars;
        } catch (IOException e) {
            throw new Error(e.toString());
        }
    }

    public void startSodaracer() throws WodkaException {
        startWebstart(SODARACE_URL);
    }

    public void startSodaconstructor() throws WodkaException {
        startWebstart(SODACONSTRUCTOR_URL);
    }

    private void startWebstart(String jnlpURL) throws WodkaException {
        try {
            String call = null;
            String arg = " \"" + jnlpURL + "\"";
            String value = this.getProperty(PROP_WEBSTART);
            if (value != null && !value.equalsIgnoreCase("default")) {
                call = value;
            } else {
                if (!isWindows()) {
                    throw new WodkaException(
                            "You must define the wodka.webstart property in the property file on non windows systems.");
                }
                String pgmDir = this.env.getProperty("ProgramFiles");
                if (pgmDir == null || pgmDir.equals("")) {
                    call = "javaws";
                } else {
                    call = pgmDir + "\\Java Web Start\\javaws.exe";
                }
            }
            if (call.indexOf(' ') >= 0) {
                call = " \"" + call + "\"";
            }
            Runtime.getRuntime().exec(call + arg);
        } catch (IOException e) {
            throw new WodkaException(
                    "Could not start java webstart.\nReason: '"
                            + e.getMessage() + "'");
        }
    }

    private boolean isWindows() {
        String osName = System.getProperty("os.name");
        return osName.toUpperCase().indexOf("WIN") >= 0;
    }

    //    private void info(String string) {
    //        System.out.println(this.getClass().getName() + ">>" + string);
    //    }

    public PropertyManager getPropManager() {
        if (this.propManager == null) {
            this.initProperties();
        }
        return this.propManager;
    }

    public Object getPropertyInstance(String name)
            throws InstantiationException {
        String className = getProperty(name);
        try {
            Class clazz = Class.forName(className);
            return clazz.newInstance();
        } catch (ClassNotFoundException e) {
            throw new InstantiationException("could not instantiate "
                    + className + ". Reason: " + e.toString());
        } catch (IllegalAccessException e) {
            throw new InstantiationException("could not instantiate "
                    + className + ". Reason: " + e.toString());
        }
    }

}