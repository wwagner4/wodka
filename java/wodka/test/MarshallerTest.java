package wodka.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Iterator;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.jdom.JDOMException;

import wodka.ga.model.Mass;
import wodka.ga.model.Model;
import wodka.ga.model.Muscle;
import wodka.util.IoUtil;
import wodka.util.Marshaller;
import wodka.util.Util;

/**
 * Testcases for the wodka.util.Downsizer
 */

public class MarshallerTest extends TestCase {

    Marshaller ma = Marshaller.current();

    public MarshallerTest(String str) {
        super(str);
    }
    public void testMarshal() throws IOException, org.jdom.JDOMException {
        URL modelUrl = IoUtil.current().loadResourceStrict("test/m1.xml");
        String schemaUrl = IoUtil.current().loadResourceStrict("test/soda.xsd").toExternalForm();
        InputStream in = modelUrl.openStream();
        try {
            Util.current().validateXml(in, schemaUrl);
        } finally {
            in.close();
        }
        Model m0 = createModelByUnmarshalling(modelUrl);
        assertVxVyNotEqualZero(m0);
        assertRestlengthGreaterZero(m0);
        StringWriter out = new StringWriter();
        ma.marshalModel(out, m0);
        String xml = out.getBuffer().toString();
        StringReader in1 = new StringReader(xml);
        Util.current().validateXml(in1, schemaUrl);
        Model m1 = ma.unmarshal(new StringReader(xml));
        assertVxVyNotEqualZero(m1);
        assertRestlengthGreaterZero(m1);
        assertAttributes(m1);
        assertMasses(m1);
        assertMuscles(m1);
    }
    private void assertRestlengthGreaterZero(Model m) {
        Iterator iterator = m.getMuscles().iterator();
        while (iterator.hasNext()) {
            Muscle musc = (Muscle) iterator.next();
            assertTrue("restlength too small: " + musc.getRestlength(), musc.getRestlength() >= 0.0);
        }
    }
    private void assertVxVyNotEqualZero(Model m) {
        Iterator iterator = m.getMasses().iterator();
        while (iterator.hasNext()) {
            Mass mass = (Mass) iterator.next();
            assertFalse("vx must not be zero. " + mass, mass.getXVelo() == 0.0);
            assertFalse("vy must not be zero. " + mass, mass.getYVelo() == 0.0);
        }
    }
    public void testUnmarshal() throws IOException, org.jdom.JDOMException {
        URL modelUrl = IoUtil.current().loadResource("test/m1.xml");
        Model model = createModelByUnmarshalling(modelUrl);
        assertAttributes(model);
        assertMasses(model);
        assertMuscles(model);
    }
    private Model createModelByUnmarshalling(URL u) throws IOException, JDOMException {
        InputStream in = null;
        Model m = null;
        try {
            in = u.openStream();
            m = ma.unmarshal(new InputStreamReader(u.openStream()));
        } finally {
            in.close();
        }
        return m;
    }
    private void assertAttributes(Model model) {
        assertEquals(0.028299415, model.getEnvFriction(), 0.0001);
        assertEquals(0.38046992, model.getEnvGravity(), 0.0001);
        assertEquals(0.5, model.getEnvSpringyness(), 0.0001);
        assertEquals(0.23404256, model.getWaveAplitude(), 0.0001);
        assertEquals(0.34554338, model.getWavePhase(), 0.0001);
        assertEquals(0.012156863, model.getWaveSpeed(), 0.0001);
    }
    private void assertMasses(Model model) {
        Iterator iter = model.getMasses().iterator();
        assertMass(337.63043, 79.90161, (Mass) iter.next());
        assertMass(392.49963, 73.369934, (Mass) iter.next());
        assertMass(186.78564, 81.27075, (Mass) iter.next());
        assertMass(204.06589, 2.0, (Mass) iter.next());
        assertMass(339.63538, 8.446869, (Mass) iter.next());
        assertTrue(!iter.hasNext());
    }
    private void assertMuscles(Model m) {
        Iterator iter = m.getMuscles().iterator();
        assertMuscle("m0", "m1", 0.0, 0.0, (Muscle) iter.next());
        assertMuscle("m1", "m3", 0.0, 0.0, (Muscle) iter.next());
        assertMuscle("m3", "m4", 0.5, 0.7901786, (Muscle) iter.next());
        assertMuscle("m1", "m6", 0.5, 0.98660713, (Muscle) iter.next());
    }
    private void assertMass(double x, double y, Mass m) {
        assertEquals(Math.round(x), m.getXPos(), 0.0001);
        assertEquals(Math.round(y), m.getYPos(), 0.0001);
    }
    private void assertMuscle(String a, String b, double amplitude, double phase, Muscle m) {
        assertEquals(a, m.getFromMass().getIdentifier());
        assertEquals(b, m.getToMass().getIdentifier());
        assertEquals(phase, m.getPhase(), 0.001);
        assertEquals(amplitude, m.getAmplitude(), 0.001);
    }
    public static void main(String[] args) {
        TestRunner.run(new TestSuite(wodka.test.MarshallerTest.class));
    }
}