package wodka.ga.geno.lang;

import java.util.*;

/**
 * An interface that contains common methods for all commands.
 */

public interface Command extends wodka.util.StreamPersistable {

    String toShortDescription();
    Command mutateCommand(Random ran);
    void mutateParameter(int index, Random ran);
    int parameterCount();
    Command createClone();
    Language getLanguage();
    void setLanguage(Language lang);
}