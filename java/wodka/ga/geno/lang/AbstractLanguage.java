/*
 * Created on Feb 9, 2004
 */
package wodka.ga.geno.lang;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import wodka.ga.model.Model;
import wodka.util.SodaGlobalsRanges;
import wodka.util.StreamPersistor;
import wwan.commons.param.Configurable;
import wwan.commons.param.ListParam;
import wwan.commons.param.ListParamDesc;
import wwan.commons.param.ParamException;
import wwan.commons.param.enums.EnumParamDesc;
import wwan.commons.param.enums.StringPopulator;

/**
 * Contains common functionality for all languages.
 * 
 * @author wwagner4
 */
public abstract class AbstractLanguage implements Language, Configurable {

    protected SodaGlobalsRanges globalRanges = SodaGlobalsRanges
            .createInstance();

    private int pgmLength = defaultProgramLength();

    private int gridWidth = defaultGridWidth();

    public AbstractLanguage(int gridWidth) {
        super();
        this.gridWidth = gridWidth;
    }

    public AbstractLanguage() {
        super();
    }

    public SodaGlobalsRanges getGlobalRanges() {
        return this.globalRanges;
    }

    public void setGlobalRanges(SodaGlobalsRanges globalRanges) {
        this.globalRanges = globalRanges;
    }

    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
    }

    public void setPgmLength(int pgmLength) {
        this.pgmLength = pgmLength;
    }

    abstract protected int defaultProgramLength();

    abstract protected int defaultGridWidth();

    public void fromStream(DataInputStream inStream, int version)
            throws IOException {
        this.globalRanges = (SodaGlobalsRanges) StreamPersistor
                .fromStream(inStream);
        this.pgmLength = inStream.readInt();
        this.gridWidth = inStream.readInt();

    }

    public int getVersion() {
        return 0;
    }

    public int getGridWidth() {
        return this.gridWidth;
    }

    public int getProgramLength() {
        return this.pgmLength;
    }

    public void checkParameterList(ListParam listParam) throws ParamException {
        this.globalRanges.checkParameterList(listParam);
    }

    public ListParamDesc getParameterDescription() throws ParamException {
        ListParamDesc lpd = new ListParamDesc();
        ListParamDesc general = new ListParamDesc();
        general.setLabel("General Parameters for Languages");
        general.add(getParamDescPgmLength());
        general.add(getParameterDescGridwidth());
        lpd.add(general);
        lpd.add(this.globalRanges.getParameterDescription());
        return lpd;
    }

    private EnumParamDesc getParamDescPgmLength() throws ParamException {
        EnumParamDesc pld = new EnumParamDesc();
        pld.setLabel("Program length");
        pld.setName("programLength");
        pld.setUnit("Commands");
        defineProgramLengthValues(pld);
        return pld;
    }

    abstract protected void defineProgramLengthValues(EnumParamDesc pld)
            throws ParamException;

    private EnumParamDesc getParameterDescGridwidth() throws ParamException {
        EnumParamDesc desc = new EnumParamDesc();
        desc.setName("gridWidth");
        desc.setLabel("Gridwidth");
        desc.setUnit("pixel. (max " + Model.WIDTH + ")");

        StringPopulator pop = new StringPopulator("1;2;5;10;15;20;40;60;80;100");
        desc.populate(pop);

        desc.setDefaultSelectionIndex(3);
        return desc;
    }

    public void setFieldsFromParameterList(ListParam listParam)
            throws ParamException {
        this.globalRanges.setFieldsFromParameterList(listParam);
        this.pgmLength = listParam.getParamValueInt("programLength");
        this.gridWidth = listParam.getParamValueInt("gridWidth");
    }

    public void setParameterListFromFields(ListParam listParam)
            throws ParamException {
        this.globalRanges.setParameterListFromFields(listParam);
        listParam.setParamValue("programLength", this.pgmLength);
        listParam.setParamValue("gridWidth", this.gridWidth);
    }

    public String getInfo() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Program length: " + this.pgmLength + " Commands\n");
        buffer.append("Gridwidth: " + this.gridWidth + " of " + Model.WIDTH
                + "\n");
        buffer.append("Ranges for global parameters:\n");
        buffer.append(this.globalRanges.getInfo());
        return buffer.toString();
    }

    public boolean hasEqualContents(Object obj) {
        boolean result = false;
        if (obj instanceof AbstractLanguage) {
            AbstractLanguage lang = (AbstractLanguage) obj;
            result = this.pgmLength == lang.pgmLength
                    && this.gridWidth == lang.gridWidth
                    && this.globalRanges.equals(lang.globalRanges);
        }
        return result;
    }

    public void toStream(DataOutputStream outStream) throws IOException {
        StreamPersistor.toStream(this.globalRanges, outStream);
        outStream.writeInt(this.pgmLength);
        outStream.writeInt(this.gridWidth);
    }

    public int getPgmLength() {
        return this.pgmLength;
    }

}