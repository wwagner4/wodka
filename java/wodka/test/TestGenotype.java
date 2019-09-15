package wodka.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import wodka.ga.Genotype;
import wodka.ga.GenotypeDesc;
import wodka.ga.model.Model;

public class TestGenotype implements Genotype {

    private static final long serialVersionUID = 1L;

    private String history = "";

    private GenotypeDesc desc = null;

    public TestGenotype() {
        super();
    }

    public TestGenotype(String hist) {
        this();
        this.history = hist;
    }

    public Genotype recombine(Genotype genotype, double mutationRate) {
        TestGenotype other = (TestGenotype) genotype;
        TestGenotype child = new TestGenotype();
        child.history = "[" + this.history + "|" + other.history + "]";
        return child;
    }

    public Model eval() {
        return null;
    }

    public Model evalNoRewise() {
        return null;
    }

    public void setGenotypeDesc(GenotypeDesc geno) {
        this.desc = geno;
    }

    public GenotypeDesc getGenotypeDesc() {
        return this.desc;
    }

    public void toStream(DataOutputStream s) throws IOException {
    }

    public void fromStream(DataInputStream s, int version) throws IOException {
    }

    public int getVersion() {
        return 0;
    }
    
    public String toString() {
        return this.history;
    }

}
