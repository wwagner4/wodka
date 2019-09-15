/*
 * Created on Apr 19, 2004
 */
package wodka.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Handles application properties in a reusable way.
 *  
 */
public class PropertyManager {

    private File file = null;
    private List descs = new ArrayList();
    private transient Properties props = null;

    public PropertyManager() {
        super();
    }

    public void setFile(File file) {
        this.file = file;
    }
    public File getFile() throws IOException  {
        if (this.props == null) {
            initProps();
        }
        return this.file;
    }
    
    public String getProperty(String name) throws IOException {
        if (this.props == null) {
            initProps();
        }
        return props.getProperty(name);
    }

    private void initProps() throws IOException {
        initPropsFromFile();
        if (!isComplete()) {
            completeFileAndProps();
        }
    }

    private void completeFileAndProps() throws IOException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(this.file, true);
            Iterator missingIter = this.missingKeys().iterator();
            while (missingIter.hasNext()) {
                String key = (String) missingIter.next();
                writeProperty(writer, key);
                addProperty(key);
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private void addProperty(String key) {
        PropertyDesc desc = getDesc(key);
        this.props.setProperty(desc.getKey(), desc.getValue());
    }

    private PropertyDesc getDesc(String key) {
        Iterator descIter = this.descs.iterator();
        while (descIter.hasNext()) {
            PropertyDesc desc = (PropertyDesc) descIter.next();
            if (desc.getKey().equals(key)) {
                return desc;
            }
        }
        throw new Error("No desc with key '" + key + "' found.");
    }

    private void writeProperty(Writer writer, String key) {
        PropertyDesc desc = getDesc(key);
        PrintWriter pWriter = new PrintWriter(writer);
        for (int i = 0; i < desc.getLeadingBlankLines(); i++) {
            pWriter.println();
        }
        List commentLines = desc.getComment();
        if (!commentLines.isEmpty()) {
            Iterator iter = commentLines.iterator();
            while (iter.hasNext()) {
                String comment = (String) iter.next();
                if (comment != null && !comment.equals("")) {
                    pWriter.print("# ");
                    pWriter.println(comment);
                }
            }
        }
        pWriter.print(desc.getKey());
        pWriter.print("=");
        pWriter.println(desc.getValue());
        for (int i = 0; i < desc.getTrailingBlankLines(); i++) {
            pWriter.println();
        }

    }

    private boolean isComplete() {
        return missingKeys().isEmpty();
    }

    private Collection missingKeys() {
        Collection coll = new ArrayList();
        Iterator descKeysIter = descKeys().iterator();
        while (descKeysIter.hasNext()) {
            Object key = descKeysIter.next();
            if (!props.containsKey(key)) {
                coll.add(key);
            }
        }
        return coll;
    }

    private Collection descKeys() {
        Collection keys = new ArrayList();
        Iterator descIter = this.descs.iterator();
        while (descIter.hasNext()) {
            PropertyDesc desc = (PropertyDesc) descIter.next();
            keys.add(desc.getKey());
        }
        return keys;
    }

    private void initPropsFromFile() throws IOException {
        props = new Properties();
        FileInputStream inStream = null;
        try {
            if (file == null) {
                throw new Error("The file must be defined for the Propertymanager.");
            }
            if (file.exists()) {
                inStream = new FileInputStream(file);
                props.load(inStream);
            }
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
    }

    public void addDesc(String key, String value, String comment) {
        PropertyDesc desc = new PropertyDesc();
        desc.setKey(key);
        desc.setValue(value);
        addComment(desc, comment);
        this.descs.add(desc);
    }
    public void addDesc(String key, String value) {
        PropertyDesc desc = new PropertyDesc();
        desc.setKey(key);
        desc.setValue(value);
        desc.setLeadingBlankLines(0);
        this.descs.add(desc);
    }
    public void addDesc(String key, String value, String comment1, String comment2) {
        PropertyDesc desc = new PropertyDesc();
        desc.setKey(key);
        desc.setValue(value);
        addComment(desc, comment1);
        addComment(desc, comment2);
        this.descs.add(desc);
    }
    public void addDesc(String key, String value, String comment, int leading, int trailing) {
        PropertyDesc desc = this.createPropDesc(key, value);
        addComment(desc, comment);
        desc.setLeadingBlankLines(leading);
        desc.setTrailingBlankLines(trailing);
        this.descs.add(desc);
    }
    public void addDesc(
            String key,
            String value,
            String comment0,
            String comment1,
            String comment2) {
        PropertyDesc desc = createPropDesc(key, value);
        addComment(desc, comment0);
        addComment(desc, comment1);
        addComment(desc, comment2);
        this.descs.add(desc);
    }

    public void addDesc(
            String key,
            String value,
            String comment0,
            String comment1,
            String comment2,
            String comment3) {
        PropertyDesc desc = createPropDesc(key, value);
        addComment(desc, comment0);
        addComment(desc, comment1);
        addComment(desc, comment2);
        addComment(desc, comment3);
        this.descs.add(desc);
    }

    private PropertyDesc createPropDesc(String key, String value) {
        PropertyDesc desc = new PropertyDesc();
        desc.setKey(key);
        desc.setValue(value);
        return desc;
    }
    private void addComment(PropertyDesc desc, String comment) {
        if (comment != null) {
            desc.addCommentLine(comment);
        }
    }

    public Collection allKeysStaringWith(String prefix) throws IOException {
        if (this.props == null) {
            initProps();
        }
        Iterator iter = props.keySet().iterator();
        Collection out = new ArrayList();
        while (iter.hasNext()) {
            String key = (String)iter.next();
            if (key.startsWith(prefix)) {
                out.add(key);
            }
        }
        return out;
    }

    
}
