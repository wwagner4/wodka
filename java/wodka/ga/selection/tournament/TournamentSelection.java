package wodka.ga.selection.tournament;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import wodka.ga.GenotypeDesc;
import wodka.ga.Individual;
import wodka.ga.Population;
import wodka.ga.SelectionPolicy;

public class TournamentSelection implements SelectionPolicy {

    private static final long serialVersionUID = 1L;

    private int initialPopulationSize = 100;

    private int tournamentSize = 20;

    private static Random ran = new Random();

    public TournamentSelection() {
        super();
    }

    public TournamentSelection(int popSize, int tournamentSize) {
        this();
        this.initialPopulationSize = popSize;
        this.tournamentSize = tournamentSize;
    }

    public Population selectNewPopulation(Population pop, GenotypeDesc desc,
            double mr) {
        Population re = new Population();
        for (int i = 0; i < getInitialPopulationSize(); i++) {
            re.addIndividual(createNewIndividual(pop, mr));
        }
        return re;
    }

    private Individual createNewIndividual(Population pop, double mr) {
        Individual indi1 = selectIndividual(pop);
        Individual indi2 = selectIndividual(pop);
        return indi1.recombine(indi2, mr);
    }

    private Individual selectIndividual(Population pop) {
        Population tpop = new Population();
        for (int i = 0; i < this.tournamentSize; i++) {
            int index = ran.nextInt(pop.size());
            tpop.addIndividual(pop.getIndividual(index));
        }
        tpop.sort();
        return tpop.getIndividual(0);
    }

    public int getInitialPopulationSize() {
        return this.initialPopulationSize;
    }

    public String getLabel() {
        return "Tournament Selection";
    }

    public String getInfo() {
        return "Tournament Selection. Population size "
                + this.initialPopulationSize + ". Tournament size "
                + this.tournamentSize + ".";
    }

    public void toStream(DataOutputStream s) throws IOException {
        s.writeInt(this.initialPopulationSize);
        s.writeInt(this.tournamentSize);
    }

    public void fromStream(DataInputStream s, int version) throws IOException {
        this.initialPopulationSize = s.readInt();
        this.tournamentSize = s.readInt();
    }

    public int getVersion() {
        return 0;
    }

    public int getTournamentSize() {
        return this.tournamentSize;
    }

}
