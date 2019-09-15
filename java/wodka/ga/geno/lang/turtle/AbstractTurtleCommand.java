/*
 * Created on Feb 16, 2004
 */
package wodka.ga.geno.lang.turtle;

import wodka.ga.geno.lang.Language;

/**
 * Common methods for all turtle commands.
 * 
 * @author wwagner4
 */
public abstract class AbstractTurtleCommand implements TurtleCommand {

    protected static java.text.DecimalFormat decFormat = new java.text.DecimalFormat(
            "#.##");

    protected Language language = null;

    public Language getLanguage() {
        return this.language;
    }

    public TurtleLanguage getTurtleLanguage() {
        return (TurtleLanguage) this.language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public AbstractTurtleCommand() {
        super();
    }

}