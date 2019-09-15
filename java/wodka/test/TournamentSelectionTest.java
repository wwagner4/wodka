package wodka.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import junit.framework.TestCase;
import wodka.ga.GeneticAlgorithm;
import wodka.ga.Individual;
import wodka.ga.Population;
import wodka.ga.selection.tournament.TournamentSelection;
import wodka.util.StreamPersistor;

public class TournamentSelectionTest extends TestCase {

    public TournamentSelectionTest(String name) {
        super(name);
    }

    public void testSerialization() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(bout);
        TournamentSelection sel = new TournamentSelection(111, 22);
        StreamPersistor.toStream(sel, dout);
        dout.close();
        bout.close();
        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        DataInputStream din = new DataInputStream(bin);
        TournamentSelection sel1 = (TournamentSelection) StreamPersistor
                .fromStream(din);
        assertEquals(111, sel1.getInitialPopulationSize());
        assertEquals(22, sel1.getTournamentSize());
    }

    public void testCrossover() {
        int size = 20;
        TournamentSelection sel = new TournamentSelection(size, 5);
        GeneticAlgorithm ga = new GeneticAlgorithm();
        ga.setSelPolicy(sel);
        ga.setPopulation(createTestPopulation(size));
        ga.createNextPopulation();
        Population pop = ga.getPopulation();
        assertEquals(size, pop.size());
    }

    private Population createTestPopulation(int size) {
        Population re = new Population();
        for (int i = 0; i < size; i++) {
            Individual indi = new Individual();
            indi.setFitness(i);
            indi.setGeno(new TestGenotype("" + i));
            re.addIndividual(indi);
        }
        return re;
    }

}
