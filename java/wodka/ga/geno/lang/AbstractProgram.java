package wodka.ga.geno.lang;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import wodka.ga.Genotype;
import wodka.ga.GenotypeDesc;
import wodka.util.StreamPersistor;

/**
 * A sequence of commands.
 */
public abstract class AbstractProgram implements Program {

    private Collection commands = new java.util.ArrayList();

    private transient Random ran = new Random();

    protected Language language = null;

    private transient int coMean = 12;

    private transient int coVariance = 5;

    public Collection getCommands() {
        return this.commands;
    }

    public void setCommands(Collection commands) {
        this.commands = commands;
    }

    public Language getLanguage() {
        return (Language) getGenotypeDesc();
    }

    public void setLanguage(Language language) {
        this.setGenotypeDesc(language);
    }

    public AbstractProgram() {
        super();
    }

    public GenotypeDesc getGenotypeDesc() {
        return this.language;
    }

    public void setGenotypeDesc(GenotypeDesc geno) {
        this.language = (Language) geno;
        Iterator iter = this.commands();
        while (iter.hasNext()) {
            Command cmd = (Command) iter.next();
            cmd.setLanguage((Language) geno);
        }
    }

    public void add(wodka.ga.geno.lang.Command cmd) {
        this.commands.add(cmd);
    }

    public Iterator commands() {
        return this.commands.iterator();
    }

    public Genotype recombine(Genotype parent, double mutationRate) {
        Program pgm = (Program) parent;
        Program child = this.createEmptyChildProgram();
        recombine(mutationRate, pgm, child);
        return child;
    }

    protected void recombine(double mutationRate, Program parent, Program child) {
        recombineCommands(parent, child, mutationRate, this.coMean, this.coVariance);
    }

    private void recombineCommands(Program parent, Program child,
            double mutationRate, int mean, int vari) {
        boolean takeThis = this.ran.nextBoolean();
        Iterator thisCmd = commands();
        Iterator aCmd = parent.commands();
        int index = mean + this.ran.nextInt(vari) - vari / 2;
        while (thisCmd.hasNext()) {
            if (index <= 0) {
                index = mean + this.ran.nextInt(vari) - vari / 2;
                takeThis = !takeThis;
            }
            wodka.ga.geno.lang.Command actual = null;
            if (takeThis) {
                actual = ((wodka.ga.geno.lang.Command) thisCmd.next())
                        .createClone();
                aCmd.next();
            } else {
                actual = ((wodka.ga.geno.lang.Command) aCmd.next())
                        .createClone();
                thisCmd.next();
            }
            index--;
            double ranVal = this.ran.nextDouble();
            if (ranVal < mutationRate) {
                int ranIndex = this.ran.nextInt(actual.parameterCount() + 1);
                if (ranIndex == 0)
                    actual = actual.mutateCommand(this.ran);
                else {
                    actual.mutateParameter(ranIndex - 1, this.ran);
                }
            }
            child.add(actual);
        }
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("(");
        Iterator cmdIter = commands();
        while (cmdIter.hasNext()) {
            wodka.ga.geno.lang.Command cmd = (wodka.ga.geno.lang.Command) cmdIter
                    .next();
            buffer.append(cmd.toShortDescription());
        }
        buffer.append(")");
        return buffer.toString();
    }

    public void toStream(DataOutputStream outStream) throws IOException {
        StreamPersistor.collectionToStream(this.commands, outStream);
    }

    public void fromStream(DataInputStream inStream, int version)
            throws IOException {
        this.commands = StreamPersistor.collectionFromStream(inStream);
    }

    public int getVersion() {
        return 0;
    }

}