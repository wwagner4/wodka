package wodka.view;

import java.awt.*;
import javax.swing.*;

/**
 * A Frame containing a controller panel
 */

public class ControllerFrame extends JDialog {

    private static final long serialVersionUID = 1L;

    PopulationControllerPanel controllerPanel = new PopulationControllerPanel();

    PopulationHistoryControllerPanel historyControllerPanel = new PopulationHistoryControllerPanel();

    BorderLayout borderLayout1 = new BorderLayout();

    public ControllerFrame(Frame owner) {
        super(owner, false);
        this.setSize(240, 220);
        this.setLocation(710, 140);
        jbInit();
    }

    private void jbInit() {
        this.getContentPane().setLayout(borderLayout1);
        this.setTitle("wodka view controller");
        controllerPanel.setPreferredSize(new Dimension(68, 50));
        this.getContentPane().add(controllerPanel, BorderLayout.NORTH);
        this.getContentPane().add(historyControllerPanel, BorderLayout.CENTER);
    }

    void setPopulationPanel(PopulationPanel p) {
        this.controllerPanel.setPopulationPanel(p);
    }

    void setPopulationHistoryPanel(PopulationHistoryPanel p) {
        historyControllerPanel.setPopulationHistoryPanel(p);
    }
}