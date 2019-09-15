/*
 * Created on Nov 5, 2003
 *  
 */
package wodka.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import wodka.ga.model.Mass;
import wodka.ga.model.Model;
import wodka.ga.model.Muscle;

/**
 * Converts a soda model from and to xml.
 */
public class Marshaller {

    private static final String VAL_0 = "0";
    private static final String ELEM_CONTAINER = "container";
    private static final String ELEM_COMMENT = "comment";
    private static final String ELEM_MASS = "mass";
    private static final String ELEM_NODES = "nodes";
    private static final String ELEM_MODEL = "model";
    private static final String ATTR_RESTLENGTH = "restlength";
    private static final String ATTR_B = "b";
    private static final String ATTR_A = "a";
    private static final String ATTR_PHASE = "phase";
    private static final String ATTR_AMPLITUDE = "amplitude";
    private static final String ELEM_MUSCLE = "muscle";
    private static final String ELEM_SPRING = "spring";
    private static final String ELEM_LINKS = "links";
    private static final String ATTR_VY = "vy";
    private static final String ATTR_VX = "vx";
    private static final String ATTR_Y = "y";
    private static final String ATTR_X = "x";
    private static final String ATTR_ID = "id";
    
    
    private static final String ENCODING = "ISO-8859-1";
    private static Marshaller current = null;
    private static NumberFormat format = createNumberFormat();

    private Marshaller() {
        super();
    }

    private static NumberFormat createNumberFormat() {
        NumberFormat lFomat = NumberFormat.getInstance(Locale.ENGLISH);
        lFomat.setMaximumFractionDigits(6);
        lFomat.setMinimumFractionDigits(0);
        return lFomat;
    }
    
    
    public static Marshaller current() {
        if (current == null){
            current = new Marshaller();
        }
        return current;
    }
    
    /**
     * Writes a model as xml stream on the output stream.
     */
    public void marshalModel(Writer out, Model model) throws IOException {
        Element modelElem = marshalJDom(model);
        outputJDom(out, modelElem);
    }

    public void marshalRace(Writer out, Collection models, String terrain) throws IOException, JDOMException {
        Element raceElem = marshalRaceJDom(models, terrain);
        outputJDom(out, raceElem);
    }

    private Element marshalRaceJDom(Collection models, String terrain) throws JDOMException {
        Element raceElem = loadTerrain(terrain);
        Iterator iter = models.iterator();
        while (iter.hasNext()) {
            mergeModel(raceElem, (Model) iter.next());
        }
        return raceElem;
    }
    private void mergeModel(Element raceElem, Model model) {
        Element modelElem = this.marshalJDom(model);
        Element contendersElem = raceElem.getChild("contenders");
        if (contendersElem == null){
            throw new Error("The race xml must contain a contenders tag.");
        }
        Element contenderElem = new Element("contender");
        contenderElem.addContent(modelElem);
        Element resultElem = new Element("result");
        resultElem.setAttribute("frames", "-1.0");
        contenderElem.addContent(resultElem);
        contendersElem.addContent(contenderElem);
    }

	private Element loadTerrain(String xml) throws JDOMException {
	    StringReader sReader = new StringReader(xml);
		SAXBuilder builder = new SAXBuilder();
		return builder.build(sReader).getRootElement();
	}

    private void outputJDom(Writer out, Element modelElem) throws UnsupportedEncodingException, IOException {
        Document doc = new Document(modelElem);
        //doc.setDocType(new org.jdom.DocType("sodarace"));
        XMLOutputter outputter = new XMLOutputter("    ", true);
        outputter.setEncoding(ENCODING);
        outputter.output(doc, out);
    }

    private Element marshalJDom(Model model) {
        Element modelElem = new Element(ELEM_MODEL);
        marshalJDomAttributes(modelElem, model);
        marshalJDomNodes(modelElem, model);
        marshalJDomLinks(modelElem, model);
        return modelElem;
    }

    private void marshalJDomNodes(Element modelElem, Model model) {
        Element nodesElem = new Element(ELEM_NODES);
        if (model.getMasses().isEmpty()) {
            Element massElem = new Element(ELEM_MASS);
            massElem.setAttribute(ATTR_ID, VAL_0);
            massElem.setAttribute(ATTR_X, VAL_0);
            massElem.setAttribute(ATTR_Y, VAL_0);
            massElem.setAttribute(ATTR_VX, VAL_0);
            massElem.setAttribute(ATTR_VY, VAL_0);
            nodesElem.addContent(massElem);
        } else {
            Iterator iter = model.getMasses().iterator();
            while (iter.hasNext()) {
                Mass mass = (Mass) iter.next();
                Element massElem = new Element(ELEM_MASS);
                massElem.setAttribute(ATTR_ID, mass.getIdentifier());
                massElem.setAttribute(ATTR_X, format.format(mass.getXPos()));
                massElem.setAttribute(ATTR_Y, format.format(mass.getYPos()));
                massElem.setAttribute(ATTR_VX, format.format(mass.getXVelo()));
                massElem.setAttribute(ATTR_VY, format.format(mass.getYVelo()));
                nodesElem.addContent(massElem);
            }
        }
        modelElem.addContent(nodesElem);
    }

    private void marshalJDomLinks(Element modelElem, Model model) {
        Element linksElem = new Element(ELEM_LINKS);
        Iterator iter = model.getMuscles().iterator();
        while (iter.hasNext()) {
            Muscle muscle = (Muscle) iter.next();
            Element linkElem = null;
            if (muscle.getAmplitude() < 0.1) {
                linkElem = new Element(ELEM_SPRING);
            } else {
                linkElem = new Element(ELEM_MUSCLE);
                linkElem.setAttribute(ATTR_AMPLITUDE, format.format(muscle.getAmplitude()));
                linkElem.setAttribute(ATTR_PHASE, format.format(muscle.getPhase()));
            }
            linkElem.setAttribute(ATTR_A, muscle.getFromMass().getIdentifier());
            linkElem.setAttribute(ATTR_B, muscle.getToMass().getIdentifier());
            if (muscle.getRestlength() < 0.0) {
                throw new Error("The restlength of this muscle must be calculated or set before you marshal your model. "+muscle);
            }
            linkElem.setAttribute(ATTR_RESTLENGTH, format.format(muscle.getRestlength()));
            linksElem.addContent(linkElem);
        }
        modelElem.addContent(linksElem);
    }

    private void marshalJDomAttributes(Element modelElem, Model model) {

        Element commentElem = new Element(ELEM_COMMENT);
        String desc = model.getDescription();
        if (desc == null){
            desc = "wodka model";
        }
        commentElem.addContent(desc);
        modelElem.addContent(commentElem);

        Element containerElem = new Element(ELEM_CONTAINER);
        containerElem.setAttribute("width", "651");
        containerElem.setAttribute("height", "422");
        modelElem.addContent(containerElem);

        Element envElem = new Element("environment");
        envElem.setAttribute("gravity", format.format(model.getEnvGravity()));
        envElem.setAttribute("friction", format.format(model.getEnvFriction()));
        envElem.setAttribute("springyness", format.format(model.getEnvSpringyness()));
        modelElem.addContent(envElem);

        Element collisionsElem = new Element("collisions");
        collisionsElem.setAttribute("surface_friction", "0.1");
        collisionsElem.setAttribute("surface_reflection", "-0.75");
        modelElem.addContent(collisionsElem);

        Element waveElem = new Element("wave");
        waveElem.setAttribute(ATTR_AMPLITUDE, format.format(model.getWaveAplitude()));
        waveElem.setAttribute(ATTR_PHASE, format.format(model.getWavePhase()));
        waveElem.setAttribute("speed", format.format(model.getWaveSpeed()));
        modelElem.addContent(waveElem);

        Element settingsElem = new Element("settings");
        settingsElem.setAttribute("gravitydirection", "down");
        settingsElem.setAttribute("wavedirection", "forward");
        settingsElem.setAttribute("autoreverse", "on");
        modelElem.addContent(settingsElem);
    }

    /**
     * Reads an xml from the input stream and creates a model.
     * 
     */
    public Model unmarshal(Reader reader) throws org.jdom.JDOMException {
        Model model = new Model();
        SAXBuilder builder = new SAXBuilder();
        Element root1 = builder.build(reader).getRootElement();
        if (!root1.getName().equals(ELEM_MODEL)){
            throw new Error("Root must be model");
        }
        Element root = root1;
        unmarshalAttributes(model, root);
        unmarshalMasses(model, root);
        unmarshalSprings(model, root);
        unmarshalMuscles(model, root);
        return model;
    }

    private void unmarshalMuscles(Model model, Element root) {
        Iterator muscIter = root.getChild(ELEM_LINKS).getChildren(ELEM_MUSCLE).iterator();
        while (muscIter.hasNext()) {
            Element muscleElem = (Element) muscIter.next();
            String valA = muscleElem.getAttributeValue(ATTR_A);
            String valB = muscleElem.getAttributeValue(ATTR_B);
            String amplitude = muscleElem.getAttributeValue(ATTR_AMPLITUDE);
            String phase = muscleElem.getAttributeValue(ATTR_PHASE);
            String restlength = muscleElem.getAttributeValue(ATTR_RESTLENGTH);
            Muscle muscle = new Muscle(model, valA, valB, stringToDouble(amplitude), stringToDouble(phase), stringToDouble(restlength));
            model.addMuscle(muscle);
        }
    }

    private void unmarshalSprings(Model model, Element root) {
        Iterator springIter = root.getChild(ELEM_LINKS).getChildren(ELEM_SPRING).iterator();
        while (springIter.hasNext()) {
            Element springElem = (Element) springIter.next();
            String valA = springElem.getAttributeValue(ATTR_A);
            String valB = springElem.getAttributeValue(ATTR_B);
            String restlength = springElem.getAttributeValue(ATTR_RESTLENGTH);
            Muscle muscle = new Muscle(model, valA, valB, 0.0, 0.0, stringToDouble(restlength));
            model.addMuscle(muscle);
        }
    }

    private void unmarshalMasses(Model model, Element root) {
        Iterator massIter = root.getChild(ELEM_NODES).getChildren(ELEM_MASS).iterator();
        while (massIter.hasNext()) {
            Element massElem = (Element) massIter.next();
            String valID = massElem.getAttributeValue(ATTR_ID);
            String valX = massElem.getAttributeValue(ATTR_X);
            String valY = massElem.getAttributeValue(ATTR_Y);
            String valVx = massElem.getAttributeValue(ATTR_VX);
            String valVy = massElem.getAttributeValue(ATTR_VY);
            Mass mass = new Mass(model, valID, stringToInt(valX), stringToInt(valY), stringToDouble(valVx), stringToDouble(valVy));
            model.addMass(mass);
        }
    }

    private void unmarshalAttributes(Model model, Element root) throws Error {
        Element envElem = root.getChild("environment");
        if (envElem == null){
            throw new Error("model/environment expected");
        }
        model.setEnvFriction(stringToDouble(envElem.getAttributeValue("friction")));
        model.setEnvGravity(stringToDouble(envElem.getAttributeValue("gravity")));
        model.setEnvSpringyness(stringToDouble(envElem.getAttributeValue("springyness")));

        Element waveElem = root.getChild("wave");
        if (waveElem == null){
            throw new Error("model/wave expected");
        }
        model.setWaveAplitude(stringToDouble(waveElem.getAttributeValue(ATTR_AMPLITUDE)));
        model.setWavePhase(stringToDouble(waveElem.getAttributeValue(ATTR_PHASE)));
        model.setWaveSpeed(stringToDouble(waveElem.getAttributeValue("speed")));
    }
    private double stringToDouble(String val) {
        return Double.parseDouble(val);
    }
    private int stringToInt(String val) {
        return (int) Math.round(stringToDouble(val));
    }
}
