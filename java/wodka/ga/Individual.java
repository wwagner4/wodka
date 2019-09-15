package wodka.ga;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import wodka.ga.geno.lang.Program;
import wodka.util.Util;

/**
 * An individual of a population of the genetic algorithm.
 */

public class Individual implements Comparable, Cloneable,
        wodka.util.StreamPersistable {

    private static final long serialVersionUID = 1L;

    private int fitness = 0;

    private Genotype geno = null;

    public Individual() {
        super();
    }

    public void setGenotype(Genotype geno) {
        this.geno = geno;
    }

    public int compareTo(Object obj) {
        int result = -1;
        Individual indi = (Individual) obj;
        if (indi.fitness > fitness) {
            result = 1;
        }
        return result;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int val) {
        fitness = val;
    }

    public boolean equals(Object obj) {
        return false;
    }

    public String getInfoString() {
        String result = "-";
        if (this.fitness > 0) {
            result = ""
                    + Util.current().convertFitnessWodkaToSoda(this.fitness);
        }
        return result;
    }

    public Individual copy() {
        try {
            return (Individual) clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public Individual recombine(Individual indi, double mutationRate) {
        Genotype newGeno = geno.recombine(indi.geno, mutationRate);
        Individual newIndividual = new Individual();
        newIndividual.setGenotype(newGeno);
        return newIndividual;
    }

    public void toStream(DataOutputStream outStream) throws IOException {
        wodka.util.StreamPersistor.toStream(this.geno, outStream);
        outStream.writeInt(this.fitness);
    }

    public void fromStream(DataInputStream inStream, int version)
            throws IOException {
        this.setGenotype((Program) wodka.util.StreamPersistor
                .fromStream(inStream));
        this.fitness = inStream.readInt();
    }

    public int getVersion() {
        return 0;
    }

    public Individual createRandomIndividual(GenotypeDesc genoDesc) {
        Individual mem = new Individual();
        mem.setGenotype(genoDesc.createRandomGenotype());
        mem.setFitness(0);
        return mem;
    }

    public Genotype getGeno() {
        return geno;
    }

    public void setGeno(Genotype geno) {
        this.geno = geno;
    }


}