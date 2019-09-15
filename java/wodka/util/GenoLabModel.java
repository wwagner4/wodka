/*
 * Created on Feb 26, 2004
 */
package wodka.util;
import wodka.ga.GenotypeDesc;
import wodka.ga.soda.SodaraceProgram;
import wwan.commons.param.Configurable;
import wwan.commons.param.ListParam;
import wwan.commons.param.ListParamDesc;
import wwan.commons.param.ParamDesc;
import wwan.commons.param.ParamException;
import wwan.commons.param.enums.ClassesParamDesc;
import wwan.commons.param.enums.StringPopulator;

/**
 * Model for the genolab.
 *  
 */
public class GenoLabModel implements Configurable {

    public GenoLabModel() {
        super();
    }

    private GenotypeDesc desc = null;

    public ListParamDesc getParameterDescription() throws ParamException {
        ListParamDesc lpd = new ListParamDesc();
        lpd.add(getGenoDesc());
        return lpd;
    }
    private ParamDesc getGenoDesc() throws ParamException {
        ClassesParamDesc lDesc = new ClassesParamDesc();
        lDesc.setName("geno");
        lDesc.setLabel("Genotype");
        StringPopulator pop = new StringPopulator("wodka.ga.geno.lang.assembler.AssemblerLanguage;" +
        		"wodka.ga.geno.lang.turtle.TurtleLanguage");
        lDesc.populate(pop);
        return lDesc;
    }

    public void setFieldsFromParameterList(ListParam lp) throws ParamException {
        desc = (GenotypeDesc) lp.getParamValue("geno");
    }

    public void setParameterListFromFields(ListParam lp) throws ParamException {
        lp.setParamValue("geno", desc);
    }

    public void checkParameterList(ListParam lp) throws ParamException {
        // Do nothing
    }

    public String getLabel() {
        return "Geno Lab";
    }

    public String getInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("GENO LAB MODEL\n\n");
        if (this.desc ==null) {
            sb.append("Currently no genotype is selected.");
        }
        else {
            sb.append("Currently the following genotype is loaded.\n\n");
            sb.append(desc.getInfo());
        }
        return sb.toString();
    }
    public SodaraceProgram getRandomGenotype() {
        if (desc == null) return null;
        return (SodaraceProgram) desc.createRandomGenotype();
    }

}
