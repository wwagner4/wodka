package wodka.ga.selection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import wodka.ga.GenotypeDesc;
import wodka.ga.Individual;
import wodka.ga.Population;
import wodka.ga.SelectionPolicy;

public class Simple20 implements SelectionPolicy, wodka.util.StreamPersistable {

    private static final long serialVersionUID = 1L;

    private java.util.Random ran = new java.util.Random();

    public Population selectNewPopulation(Population current,
            GenotypeDesc desc, double mr) {
        Population population = new Population();
        Individual m = current.getIndividual(0).copy();
        m.setFitness(0);
        population.addIndividual(m);
        m = current.getIndividual(1).copy();
        m.setFitness(0);
        population.addIndividual(m);
        m = current.getIndividual(2).copy();
        m.setFitness(0);
        population.addIndividual(m);
        population.addIndividual(current.getIndividual(0).recombine(
                current.getIndividual(1), mr));
        population.addIndividual(current.getIndividual(0).recombine(
                current.getIndividual(2), mr));
        population.addIndividual(current.getIndividual(1).recombine(
                current.getIndividual(2), mr));
        for (int i = 0; i < 3; i++) {
            Individual m1 = current.getIndividual(0);
            Individual m2 = current.getIndividual(ran.nextInt(current.size()));
            population.addIndividual(m1.recombine(m2, mr));
        }
        for (int i = 0; i < 3; i++) {
            Individual m1 = current.getIndividual(1);
            Individual m2 = current.getIndividual(ran.nextInt(current.size()));
            population.addIndividual(m1.recombine(m2, mr));
        }
        for (int i = 0; i < 3; i++) {
            Individual m1 = current.getIndividual(2);
            Individual m2 = current.getIndividual(ran.nextInt(current.size()));
            population.addIndividual(m1.recombine(m2, mr));
        }
        for (int i = 0; i < 5; i++) {
            Individual m1 = current.getIndividual(ran.nextInt(current.size()));
            Individual m2 = current.getIndividual(ran.nextInt(current.size()));
            population.addIndividual(m1.recombine(m2, mr));
        }
        return population;
    }

    public int getInitialPopulationSize() {
        return 20;
    }

    public String getLabel() {
        return "Simple_S_20";
    }

    public String getInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("Population size is 20 according to the rules below.\n");
        sb.append("Creates a new population by the following rules:\n");
        sb.append("-The three fittest are taken.\n");
        sb.append("-The tree fittest are recombined each with each other.\n");
        sb
                .append("-Each of the three fittest is recombined with three random individuals.\n");
        sb
                .append("-Five random individuals are recombined with five other random individuals\n");
        return sb.toString();
    }

    public void fromStream(DataInputStream s, int version) throws IOException {
        // Do nothing
    }

    public void toStream(DataOutputStream s) throws IOException {
        // Do nothing
    }

    public int getVersion() {
        return 0;
    }

}