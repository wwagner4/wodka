/*
 * Created on Apr 15, 2004
 */
package wodka.results;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import wodka.ga.GeneticAlgorithm;
import wodka.ga.Individual;
import wodka.ga.Population;
import wodka.ga.model.Model;
import wodka.ga.soda.SodaraceProgram;
import wodka.util.Util;
import wodka.view.CategorizedInfoModel;

/**
 * Some functionality to produce HTML.
 *  
 */
class HtmlHelper {

    private transient ZooBuilder builder = null;

    private transient Util util = Util.current();

    private HtmlHelper() {
        super();
    }

    HtmlHelper(ZooBuilder builder) {
        this();
        this.builder = builder;
    }

    void writeIndexHeadLine(PrintWriter pWriter) {
        writeHead(pWriter, "wodka zoo");
        pWriter.println("<body>");
        pWriter.println("<table width=\"98%\">");
        pWriter.println("<tr>");
        pWriter.println("<td class=\"label\">" + "File" + "</td>");
        pWriter.println("<td class=\"label\">" + "Max fitness" + "</td>");
        pWriter.println("<td class=\"label\">" + "Generations" + "</td>");
        pWriter.println("<td class=\"label\">" + "Mutation rate" + "</td>");
        pWriter.println("<td class=\"label\">" + "Population size" + "</td>");
        pWriter.println("<td class=\"label\">" + "Genotype" + "</td>");
        pWriter.println("<td class=\"label\">" + "Selection Policy" + "</td>");
        pWriter.println("<td>" + "</td>");
        pWriter.println("<td>" + "</td>");
        pWriter.println("<td>" + "</td>");
        pWriter.println("</tr>");
    }

    void writeIndexEntry(GeneticAlgorithm genAlgo, String num, String fileName,
            PrintWriter pWriter) {
        pWriter.println("<tr>");
        pWriter.println("<td>" + fileName + "</td>");
        pWriter.println("<td>" + genAlgo.getPrevMaxFitSoda() + "</td>");
        pWriter.println("<td>" + genAlgo.getPopCount() + "</td>");
        pWriter.println("<td>" + genAlgo.getMutationRate() + "</td>");
        pWriter.println("<td>" + genAlgo.getPopulation().size() + "</td>");
        pWriter.println("<td>" + genAlgo.getGenoDesc().getLabel() + "</td>");
        pWriter.println("<td>" + genAlgo.getSelPolicy().getLabel() + "</td>");
        pWriter.println("<td>" + "<a href=\"" + builder.getNameFastJnlp(num)
                + "\">fittest</a>" + "</td>");
        pWriter.println("<td>" + "<a href=\"" + builder.getNameHistJnlp(num)
                + "\">history</a>" + "</td>");
        pWriter.println("<td>" + "<a href=\"" + builder.getNameDetailsHtml(num)
                + "\">details</a>" + "</td>");
        pWriter.println("</tr>");
    }

    void writeIndexBottom(PrintWriter pWriter) {
        pWriter.println("</table>");
        pWriter.println("</body>");
        pWriter.println("</html>");
    }

    private void writeHead(PrintWriter pWriter, String title) {
        pWriter.println("<html>");
        pWriter.println("<head>");
        pWriter.println("<title>" + title + "</title>");
        pWriter
                .println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\"/>");
        pWriter
                .println("<link rel=\"stylesheet\" href=\"style.css\" type=\"text/css\"/>");
        pWriter.println("</head>");
    }

    private String formatParagraphs(String text) throws IOException {
        StringWriter writer = new StringWriter();
        PrintWriter pWriter = new PrintWriter(writer);
        StringReader reader = new StringReader(text);
        BufferedReader bReader = new BufferedReader(reader);
        String line = bReader.readLine();
        while (line != null) {
            pWriter.print(line);
            pWriter.print("<br>");
            line = bReader.readLine();
        }
        pWriter.flush();
        return writer.getBuffer().toString();
    }

    public void writeDetails(GeneticAlgorithm genAlgo, String num,
            String fileName, PrintWriter pWriter) throws IOException {
        writeHead(pWriter, "wodka zoo details");
        pWriter.println("<body>");

        writeDetailsRace(genAlgo, num, fileName, pWriter);
        writeDetailsFastestModel(genAlgo, num, pWriter);
    }

    private void writeDetailsRun(String num, PrintWriter pWriter) {
        pWriter.println("<table>");
        pWriter.println("<tr>");
        pWriter.println("<td>" + "<a href=\"" + builder.getNameFastJnlp(num)
                + "\">fittest</a>" + "</td>");
        pWriter.println("<td>" + "<a href=\"" + builder.getNameHistJnlp(num)
                + "\">history</a>" + "</td>");
        pWriter.println("</tr>");
        pWriter.println("</table>");
    }

    private void writeDetailsRace(CategorizedInfoModel genAlgo, String num,
            String fileName, PrintWriter pWriter) throws IOException {

        pWriter.println("<h2>Race</h2>");

        writeDetailsRun(num, pWriter);

        pWriter.println("<table width=\"98%\">");

        pWriter.println("<tr>");
        pWriter.println("<td class=\"label\">" + "Name" + "</td>");
        pWriter.println("<td class=\"label\">" + "Description" + "</td>");
        pWriter.println("</tr>");

        pWriter.println("<tr>");
        pWriter.println("<td>" + "Number" + "</td>");
        pWriter.println("<td>" + formatParagraphs(num) + "</td>");
        pWriter.println("</tr>");

        pWriter.println("<tr>");
        pWriter.println("<td>" + "Filename" + "</td>");
        pWriter.println("<td>" + formatParagraphs(fileName) + "</td>");
        pWriter.println("</tr>");

        for (int i = 0; i < genAlgo.categoryCount(); i++) {
            pWriter.println("<tr>");
            pWriter.println("<td>" + genAlgo.getCategoryName(i) + "</td>");
            pWriter.println("<td>"
                    + formatParagraphs(genAlgo.getCategoryInfo(i)) + "</td>");
            pWriter.println("</tr>");
        }

        pWriter.println("</table>");
        pWriter.println("</body>");
        pWriter.println("</html>");
    }

    private void writeDetailsFastestModel(GeneticAlgorithm genAlgo, String num,
            PrintWriter pWriter) {

        Model model = fastestModel(genAlgo);

        pWriter.println("<h2>Fastest Model</h2>");

        writeDetailsRun(num, pWriter);

        pWriter.println("<table>");

        pWriter.println("<tr>");
        pWriter.println("<td class=\"label\">" + "Name" + "</td>");
        pWriter.println("<td class=\"label\">" + "Description" + "</td>");
        pWriter.println("<td class=\"label\">" + "Min" + "</td>");
        pWriter.println("<td class=\"label\">" + "Max" + "</td>");
        pWriter.println("</tr>");

        pWriter.println("<tr>");
        pWriter.println("<td>" + "Friction" + "</td>");
        pWriter.println("<td>" + util.formatDouble3(model.getEnvFriction())
                + "</td>");
        pWriter.println("<td>" + "0.0" + "</td>");
        pWriter.println("<td>" + "1.0" + "</td>");
        pWriter.println("</tr>");

        pWriter.println("<tr>");
        pWriter.println("<td>" + "Gravity" + "</td>");
        pWriter.println("<td>" + util.formatDouble3(model.getEnvGravity())
                + "</td>");
        pWriter.println("<td>" + "0.0" + "</td>");
        pWriter.println("<td>" + "4.0" + "</td>");
        pWriter.println("</tr>");

        pWriter.println("<tr>");
        pWriter.println("<td>" + "Sprynginess" + "</td>");
        pWriter.println("<td>" + util.formatDouble3(model.getEnvSpringyness())
                + "</td>");
        pWriter.println("<td>" + "0.0" + "</td>");
        pWriter.println("<td>" + "0.5" + "</td>");
        pWriter.println("</tr>");

        pWriter.println("<tr>");
        pWriter.println("<td>" + "Amplitude" + "</td>");
        pWriter.println("<td>" + util.formatDouble3(model.getWaveAplitude())
                + "</td>");
        pWriter.println("<td>" + "0.0" + "</td>");
        pWriter.println("<td>" + "1.0" + "</td>");
        pWriter.println("</tr>");

        pWriter.println("<tr>");
        pWriter.println("<td>" + "Phase" + "</td>");
        pWriter.println("<td>" + util.formatDouble3(model.getWavePhase())
                + "</td>");
        pWriter.println("<td>" + "0.0" + "</td>");
        pWriter.println("<td>" + "1.0" + "</td>");
        pWriter.println("</tr>");

        pWriter.println("<tr>");
        pWriter.println("<td>" + "Speed" + "</td>");
        pWriter.println("<td>" + util.formatDouble3(model.getWaveSpeed())
                + "</td>");
        pWriter.println("<td>" + "0.0" + "</td>");
        pWriter.println("<td>" + "0.1" + "</td>");
        pWriter.println("</tr>");

        pWriter.println("<tr>");
        pWriter.println("<td>" + "Mass count" + "</td>");
        pWriter.println("<td>" + model.getMassCount() + "</td>");
        pWriter.println("<td>" + "" + "</td>");
        pWriter.println("<td>" + "" + "</td>");
        pWriter.println("</tr>");

        pWriter.println("<tr>");
        pWriter.println("<td>" + "Muscle count" + "</td>");
        pWriter.println("<td>" + model.getMuscleCount() + "</td>");
        pWriter.println("<td>" + "" + "</td>");
        pWriter.println("<td>" + "" + "</td>");
        pWriter.println("</tr>");

        pWriter.println("</table>");
        pWriter.println("</body>");
        pWriter.println("</html>");
    }

    private Model fastestModel(GeneticAlgorithm genAlgo) {
        Population pop = genAlgo.getPopHist().getPopulation(0);
        Individual indi = pop.getIndividual(0);
        SodaraceProgram pgm = (SodaraceProgram) indi.getGeno();
        return pgm.eval();
    }

}