/*
 * Created on 18.11.2003
 *  
 */
package wodka.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jdom.JDOMException;

import wodka.WodkaException;
import wodka.ga.GeneticAlgorithm;
import wodka.ga.Individual;
import wodka.ga.Population;
import wodka.ga.PopulationHistory;
import wodka.ga.model.Model;
import wodka.ga.soda.SodaraceGeneticAlgorithm;
import wodka.ga.soda.SodaraceProgram;

/**
 * Exports files to be run on the soda constructor / racer.
 */
public class Exporter {

    private static final String EXT = ".xml";
    private static Exporter current = null;
    private transient Marshaller marshaller = Marshaller.current();

    private Exporter() {
        super();
    }

    public static Exporter current() {
        if (current == null) {
            current = new Exporter();
        }
        return current;
    }

    public void exportRaceFastest(GeneticAlgorithm genAlgo, Writer writer, String terrain) throws WodkaException {
        exportRace(first(currentPopulationModels(genAlgo)), writer, terrain);
    }
    public void exportRaceHistory(GeneticAlgorithm genAlgo, Writer writer, String terrain) throws WodkaException {
        exportRace(thin(historyModels(genAlgo), 6), writer, terrain);
    }

    private void exportRace(List models, Writer writer, String terrain) throws WodkaException {
        try {
            this.marshaller.marshalRace(writer, models, terrain);
        } catch (IOException e) {
            throw new WodkaException(e);
        } catch (JDOMException e) {
            throw new WodkaException(e);
        }
    }

    public void exportForBreeder(SodaraceGeneticAlgorithm genAlgo, File outDir) throws WodkaException {
        List currentPop = currentPopulationModels(genAlgo);
        List history = historyModels(genAlgo);
        String desc = genAlgo.getLabel();
        exportModels(outDir, currentPop, history);
        exportRaces(outDir, currentPop, history, desc, genAlgo.getTerrManager().getTerrainXml(genAlgo));
    }

    private void exportModels(File outDir, Collection currentPop, Collection history) throws WodkaException {
        try {
            Iterator iter;
            File modelsDir = new File(outDir, "models");
            if (!modelsDir.exists()) {
                modelsDir.mkdir();
            }
            iter = currentPop.iterator();
            int index = 0;
            while (iter.hasNext()) {
                Model model = (Model) iter.next();
                File outFile = new File(modelsDir, "wodka_current_" + index + EXT);
                marshalModel(model, outFile);
                index++;
            }
            iter = history.iterator();
            index = 0;
            while (iter.hasNext()) {
                Model model = (Model) iter.next();
                File outFile = new File(modelsDir, "wodka_history_" + index + EXT);
                marshalModel(model, outFile);
                index++;
            }
        } catch (FileNotFoundException e) {
            throw new WodkaException(e);
        } catch (IOException e) {
            throw new WodkaException(e);
        }
    }
    private void exportRaces(File outDir, List currentPop, List history, String algoId, String terrain) throws WodkaException {
        File racesDir = new File(outDir, "races");
        if (!racesDir.exists()) {
            racesDir.mkdir();
        }
        marshalRace(first(history), racesDir, algoId, "fastestOfHistory", terrain);
        marshalRace(first(currentPop), racesDir, algoId, "fastestOfCurrentPopulation", terrain);
        marshalRace(thin(currentPop, 6), racesDir, algoId, "currentPopulation", terrain);
        marshalRace(thin(history, 6), racesDir, algoId, "historySubset", terrain);

    }

    private void marshalRace(Collection models, File outDir, String identifier, String desc, String terrain) throws WodkaException {
        File out = new File(outDir, identifier + "_" + desc + ".xml");
        try {
            FileWriter outStream = null;
            try {
                outStream = new FileWriter(out);
                marshaller.marshalRace(outStream, models, terrain);
            } finally {
                if (outStream != null) {
                    outStream.close();
                }
            }
        } catch (FileNotFoundException e) {
            throw new WodkaException(e);
        } catch (IOException e) {
            throw new WodkaException(e);
        } catch (JDOMException e) {
            throw new WodkaException(e);
        }
    }

    private List first(List inColl) {
        List result = new ArrayList();
        if (!inColl.isEmpty()) {
            result.add(inColl.get(0));
        }
        return result;
    }
    private List thin(List inList, int amount) {
        if (inList.size() <= amount) {
            return inList;
        }
        List outList = new ArrayList();
        for (int index = 0; index < amount; index++) {
            int thinIndex = Util.current().thin(inList.size(), amount, index);
            outList.add(inList.get(thinIndex));
        }
        return outList;
    }

    private List historyModels(GeneticAlgorithm genAlgo) {
        PopulationHistory popHist = genAlgo.getPopHist();
        return extractFastestModels(popHist);
    }

    private List extractFastestModels(PopulationHistory popHist) {
        List models = new ArrayList();
        for (int j = 0; j < popHist.size(); j++) {
            Population pop = popHist.getPopulation(j);
            int gen = pop.getNumber();
            Individual indi = pop.getIndividual(0);
            SodaraceProgram pgm = (SodaraceProgram) indi.getGeno();
            Model model = pgm.eval();
            model.setDescription("wodka gen=" + gen + " pos=1");
            models.add(model);
        }
        return models;
    }

    private List currentPopulationModels(GeneticAlgorithm genAlgo) {
        Population pop = genAlgo.getPopulation();
        pop.sort();
        return extractModels(pop);
    }

    private List extractModels(Population pop) {
        Iterator indiIter = pop.getIndividuals().iterator();
        List currentPop = new ArrayList();
        int gen = pop.getNumber();
        int index = 1;
        while (indiIter.hasNext()) {
            Individual indi = (Individual) indiIter.next();
            SodaraceProgram pgm = (SodaraceProgram) indi.getGeno();
            Model model = pgm.eval();
            model.setDescription("wodka gen=" + gen + " pos=" + index);
            currentPop.add(model);
            index++;
        }
        return currentPop;
    }

    private void marshalModel(Model model, File outFile) throws FileNotFoundException, IOException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(outFile);
            marshaller.marshalModel(writer, model);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

}
