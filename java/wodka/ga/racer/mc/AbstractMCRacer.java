package wodka.ga.racer.mc;

import java.io.PrintWriter;
import java.io.StringWriter;

import wodka.ga.racer.AbstractRacer;
import wodka.ga.racer.RaceRunner;

public abstract class AbstractMCRacer extends AbstractRacer {

    public String getInfo() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        writeDescription(pw);
        pw.print("A ModelCompare racer breeds models that should be ");
        pw.print("structurally equivalent to a pattern model.");
        pw.println();
        pw.print("It does this by putting a grid over the model and the pattern.");
        pw.print("That grid increases its granularity stepwise. ");
        pw.print("For every granularity level ");
        pw.print("the number of equivalent connections of the model and the pattern ");
        pw.print("are counted. A connection is equivalent if it starts and end in ");
        pw.print("the same grid cell. Models with more equivalent connections are ");
        pw.print("more alike the pattern than others.");
        return sw.getBuffer().toString();
    }
    protected abstract void writeDescription(PrintWriter pw);


    public RaceRunner createRaceRunner() {
        throw new Error("Not yet implemented");
    }

}
