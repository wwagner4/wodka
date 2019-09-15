/**
 * $Revision: 1.1 $ $Author: wwan $ $Date: 2004/08/17 10:13:22 $ 
 */

package wodka.view;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Writes a CategorizedInfoModel to a stream device.. 
 */
public class CategorizedStreamOutputter {
    
    private static CategorizedStreamOutputter current = null; 

    public CategorizedStreamOutputter() {
        super();
    }
    
    public static CategorizedStreamOutputter current() {
        if (current == null) {
            current = new CategorizedStreamOutputter();
        }
        return current;
    }
    
    public String outputToString (CategorizedInfoModel model) {
        StringBuffer sb = new StringBuffer();
        this.output(model, sb);
        return sb.toString();
    }
    
    public void output (CategorizedInfoModel model, StringBuffer sb) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        output(model, pw);
        sb.append(sw.getBuffer().toString());
    }
    
    public void output(CategorizedInfoModel model, PrintWriter pw) {
        outputSeperator1(pw);
        pw.println("Categorized Info for: " + model.toString());
        for (int i=0; i<model.categoryCount(); i++) {
            if (i == 0) {
                outputSeperator(pw);
            }
            String catInfo = model.getCategoryInfo(i);
            String catName = model.getCategoryName(i);
            pw.println(catName);
            pw.println(catInfo);
            outputSeperator(pw);
        }
    }

    private void outputSeperator1(PrintWriter pw) {
        pw.println("================================================================");
    }
    private void outputSeperator(PrintWriter pw) {
        pw.println("________________________________________________________________");
    }
    
}
