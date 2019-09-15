package wodka.ga.selection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import wodka.ga.GenotypeDesc;
import wodka.ga.Individual;
import wodka.ga.Population;
import wodka.ga.SelectionPolicy;

public class AddRandom40 implements SelectionPolicy {

    private static final long serialVersionUID = 1L;

    public Population selectNewPopulation(Population currentPop,
            GenotypeDesc desc, double mr) {
        Population newPop = new Population();
        Individual m = currentPop.getIndividual(0).copy();
        m.setFitness(0);
        newPop.addIndividual(m);
        m = currentPop.getIndividual(1).copy();
        m.setFitness(0);
        newPop.addIndividual(m);
        m = currentPop.getIndividual(2).copy();
        m.setFitness(0);
        newPop.addIndividual(m);
        newPop.addIndividual(currentPop.getIndividual(0).recombine(
                currentPop.getIndividual(3), mr));
        newPop.addIndividual(currentPop.getIndividual(1).recombine(
                currentPop.getIndividual(3), mr));
        newPop.addIndividual(currentPop.getIndividual(2).recombine(
                currentPop.getIndividual(3), mr));
        newPop.addIndividual(currentPop.getIndividual(0).recombine(
                currentPop.getIndividual(5), mr));
        newPop.addIndividual(currentPop.getIndividual(1).recombine(
                currentPop.getIndividual(5), mr));
        newPop.addIndividual(currentPop.getIndividual(2).recombine(
                currentPop.getIndividual(5), mr));
        for (int i = 0; i < 31; i++) {
            Individual mem = currentPop.getIndividual(0)
                    .createRandomIndividual(desc);
            newPop.addIndividual(mem);
        }
        return newPop;
    }

    public int getInitialPopulationSize() {
        return 40;
    }

    public String getLabel() {
        return "AddRandom40";
    }

    public String getInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("Population size is 40 according to the rules below.\n");
        sb.append("Creates a new newPop by the following rules:\n");
        sb.append("- The 3 fittest are taken.\n");
        sb.append("- The 3 fittest are each recombined with the 4th.\n");
        sb.append("- The 3 fittest are each recombined with the 6th.\n");
        sb
                .append("- The 31 random generated are added to fill up the new generation.\n");
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
