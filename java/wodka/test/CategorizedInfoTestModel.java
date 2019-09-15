/*
 * CategorizedInfoTestModel.java
 *
 * Created on 03. Februar 2004, 09:34
 */

package wodka.test;

import java.util.ArrayList;
import java.util.List;
import wodka.view.CategorizedInfoModel;

/**
 *
 * @author  wolfgang wagner
 */
public class CategorizedInfoTestModel implements CategorizedInfoModel {
    
    private List entries = new ArrayList();
    
    /** Creates a new instance of CategorizedInfoTestModel */
    public CategorizedInfoTestModel() {
        entries.add(new Entry("a", "info for a"));
        entries.add(new Entry("b", "info for b"));
        entries.add(new Entry("c", "info for c"));
        entries.add(new Entry("d", "info for d"));
    }
    
    public int categoryCount() {
        return entries.size();
    }
    
    public String getCategoryInfo(int i) {
        Entry e = (Entry)entries.get(i);
        return e.getInfo();
    }
    
    public String getCategoryName(int i) {
        Entry e = (Entry)entries.get(i);
        return e.getName();
    }
    
    private class Entry {
        
        String name = null;
        String info = null;
        
        public Entry (String name, String info) {
            this.name = name;
            this.info = info;
        }
        
        /** Getter for property name.
         * @return Value of property name.
         *
         */
        public java.lang.String getName() {
            return name;
        }
        
        /** Setter for property name.
         * @param name New value of property name.
         *
         */
        public void setName(java.lang.String name) {
            this.name = name;
        }
        
        /** Getter for property info.
         * @return Value of property info.
         *
         */
        public java.lang.String getInfo() {
            return info;
        }
        
        /** Setter for property info.
         * @param info New value of property info.
         *
         */
        public void setInfo(java.lang.String info) {
            this.info = info;
        }
        
    }
    
}
