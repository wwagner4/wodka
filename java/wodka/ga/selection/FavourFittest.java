package wodka.ga.selection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import wodka.ga.GenotypeDesc;
import wodka.ga.Individual;
import wodka.ga.Population;
import wodka.ga.SelectionPolicy;
import wodka.util.StreamPersistor;
import wwan.commons.param.Configurable;
import wwan.commons.param.ListParam;
import wwan.commons.param.ListParamDesc;
import wwan.commons.param.ParamException;
import wwan.commons.param.enums.EnumParamDesc;
import wwan.commons.param.enums.StringPopulator;

public class FavourFittest implements SelectionPolicy, Configurable {

    private static final long serialVersionUID = 1L;

    private java.util.Random ran = new java.util.Random();

    private int popSize = 30;

    private double reproductionRate = 0.5;

    private double wildcardRate = 0.1;

    private double randomRate = 0.1;

    public Population selectNewPopulation(Population current,
            GenotypeDesc desc, double mr) {
        Population population = new Population();
        int count = 0;
        int max = this.getInitialPopulationSize();
        int i = 0;
        while (count < max && i < this.wildcardAmount()) {
            Individual m1 = current.getIndividual(i);
            population.addIndividual(m1.copy());
            i++;
            count++;
        }
        i = 0;
        while (count < max && i < this.randomAmount()) {
            Individual m1 = current.getIndividual(0);
            population.addIndividual(m1.createRandomIndividual(desc));
            i++;
            count++;
        }
        i = 0;
        while (count < max) {
            if (i >= reproductionAmount())
                i = 0;
            Individual m1 = current.getIndividual(i);
            Individual m2 = current.getIndividual(ran.nextInt(current.size()));
            population.addIndividual(m1.recombine(m2, mr));
            i++;
            count++;
        }
        return population;
    }

    public int getInitialPopulationSize() {
        return this.popSize;
    }

    public String getLabel() {
        return "Favour Fittest";
    }

    public String getInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("A new generation is created following the steps below:\n");
        sb.append("1. The individuals are sorted by fitness.\n");
        sb.append("2. The " + wildcardAmount()
                + " fittest individuals are copied to the next generation."
                + " No mutation is applied.\n");
        sb.append("3. " + randomAmount()
                + " random individuals are added to the next generation.\n");
        sb
                .append("4. The "
                        + this.reproductionAmount()
                        + " fittest individuals reproduce itself with another random "
                        + "individual of the current population until the next generation has "
                        + popSize + " individuals.\n");
        sb
                .append("Whenever the size of the next generation has reached the maximum of "
                        + popSize
                        + ", the process "
                        + "gets interrupted and the subsequent steps are not performed.\n");
        sb.append("Parameters:\n");
        sb.append("- population size: " + this.popSize + "\n");
        sb.append("- wildcard rate: " + (this.wildcardRate * 100)
                + " % absolute: " + wildcardAmount() + "\n");
        sb
                .append("    The percentage of the fittest individuals taken for the next generation.\n");
        sb.append("- random rate: " + (this.randomRate * 100) + " % absolute: "
                + randomAmount() + "\n");
        sb
                .append("    The percentage of newly generated individuals in the next generation.\n");
        sb.append("- reproduction rate: " + (this.reproductionRate * 100)
                + " % absolute: " + reproductionAmount() + "\n");
        sb
                .append("    The percentage of the fittest individuals that are choosen to reproduce itself.\n");

        return sb.toString();
    }

    private int reproductionAmount() {
        int amount = (int) Math.round(popSize * this.reproductionRate);
        return amount;
    }

    private int wildcardAmount() {
        int amount = (int) Math.round(popSize * this.wildcardRate);
        return amount;
    }

    private int randomAmount() {
        int amount = (int) Math.round(popSize * this.randomRate);
        return amount;
    }

    public ListParamDesc getParameterDescription() throws ParamException {
        ListParamDesc list = new ListParamDesc();
        ListParamDesc grp = new ListParamDesc();
        grp.setLabel("");
        {
            EnumParamDesc desc = new EnumParamDesc();
            desc.setName("popSize");
            desc.setLabel("population size");
            StringPopulator pop = new StringPopulator(
                    "10;20;30;40;50;100;200;500;1000");
            desc.populate(pop);

            desc.setDefaultSelectionIndex(2);
            grp.add(desc);
        }
        {
            EnumParamDesc desc = new EnumParamDesc();
            desc.setName("repRate");
            desc.setLabel("reproduction rate");
            desc.setUnit("%");
            StringPopulator pop = createPercentagePop();
            desc.populate(pop);
            desc.setDefaultSelectionIndex(6);
            grp.add(desc);
        }
        {
            EnumParamDesc desc = new EnumParamDesc();
            desc.setName("wildcardRate");
            desc.setLabel("wildcard rate");
            desc.setUnit("%");
            StringPopulator pop = createPercentagePop();
            desc.populate(pop);
            desc.setDefaultSelectionIndex(2);
            grp.add(desc);
        }
        {
            EnumParamDesc desc = new EnumParamDesc();
            desc.setName("randomRate");
            desc.setLabel("random rate");
            desc.setUnit("%");
            StringPopulator pop = createPercentagePop();
            desc.populate(pop);
            desc.setDefaultSelectionIndex(2);
            grp.add(desc);
        }
        list.add(grp);
        return list;
    }

    private StringPopulator createPercentagePop() throws ParamException {
        return new StringPopulator(
                "0.01|1|;0.05|5|;0.1|10|;0.3|30|;0.4|40|;0.5|50|;0.75|75|;0.9|90|;0.95|95|;0.99|90|;1|100|");
    }

    public void setFieldsFromParameterList(ListParam lp) {
        this.popSize = lp.getParamValueInt("popSize");
        this.reproductionRate = lp.getParamValueDouble("repRate");
        this.wildcardRate = lp.getParamValueDouble("wildcardRate");
        this.randomRate = lp.getParamValueDouble("randomRate");
    }

    public void setParameterListFromFields(ListParam lp) throws ParamException {
        lp.setParamValue("popSize", popSize);
        lp.setParamValue("repRate", this.reproductionRate);
        lp.setParamValue("wildcardRate", this.wildcardRate);
        lp.setParamValue("randomRate", this.randomRate);
    }

    public void fromStream(DataInputStream s, int version) throws IOException {
        StreamPersistor.configurableFromStream(this, s);
    }

    public int getVersion() {
        return 0;
    }

    public void toStream(DataOutputStream s) throws IOException {
        StreamPersistor.configurableToStream(this, s);
    }

    public int getPopSize() {
        return popSize;
    }

    public void setPopSize(int popSize) {
        this.popSize = popSize;
    }

    public double getReproductionRate() {
        return reproductionRate;
    }

    public void setReproductionRate(double reproductionRate) {
        this.reproductionRate = reproductionRate;
    }

    public void checkParameterList(ListParam lp) throws ParamException {
        // No checks necessary;
    }

    /**
     * @return Returns the randomRate.
     */
    public double getRandomRate() {
        return randomRate;
    }

    /**
     * @param randomRate
     *            The randomRate to set.
     */
    public void setRandomRate(double randomRate) {
        this.randomRate = randomRate;
    }

    /**
     * @return Returns the wildcardRate.
     */
    public double getWildcardRate() {
        return wildcardRate;
    }

    /**
     * @param wildcardRate
     *            The wildcardRate to set.
     */
    public void setWildcardRate(double wildcardRate) {
        this.wildcardRate = wildcardRate;
    }
}