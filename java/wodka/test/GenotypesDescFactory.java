/*
 * Created on 12.12.2003
 *
 */
package wodka.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import wodka.ga.GenotypeDesc;

/**
 * @author wwagner4
 *
 */
public class GenotypesDescFactory {
    
    private static GenotypesDescFactory current = null;
    private List genos = new ArrayList();

    private GenotypesDescFactory() {
    genos.add(new wodka.ga.geno.lang.assembler.AssemblerLanguage());
    genos.add(new wodka.ga.geno.lang.turtle.TurtleLanguage(1));
    genos.add(new wodka.ga.geno.lang.turtle.TurtleLanguage(5));
    genos.add(new wodka.ga.geno.lang.turtle.TurtleLanguage(10));
    }
    
    
    public static GenotypesDescFactory current() {
        if (current == null) current = new GenotypesDescFactory();
        return current;
    }
    
    public GenotypeDesc getGenotypeDesc(int index) {
        return (GenotypeDesc) genos.get(index);
    }
    public GenotypeDesc getGenotypeDesc(String name) {
        Iterator iter = genos.iterator();
        while (iter.hasNext()) {
            GenotypeDesc l = (GenotypeDesc) iter.next();
            if (l.getLabel().equals(name))
                return l;
        }
        return null;
    }
    public String getLanguageName(int index) {
        return this.getGenotypeDesc(index).getLabel();
    }

    public int getGenotypeDescCount() {
        return genos.size();
    }

}
