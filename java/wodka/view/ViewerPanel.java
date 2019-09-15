package wodka.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import wodka.ga.soda.Breeder;

/**
 * A frame to display a GA.
 */

public class ViewerPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    BorderLayout borderLayout1 = new BorderLayout();

    JPanel contentPanel = new JPanel();

    BorderLayout borderLayout2 = new BorderLayout();

    JSplitPane splitPane = new JSplitPane();

    PopulationHistoryPanel historyPanel = new PopulationHistoryPanel();

    PopulationPanel populationPanel = new PopulationPanel();

    public ViewerPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBreeder(Breeder breeder) {
        this.populationPanel.setBreeder(breeder);
        this.historyPanel.setBreeder(breeder);
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        contentPanel.setLayout(borderLayout2);
        historyPanel.setBackground(new Color(173, 199, 182));
        populationPanel.setPreferredSize(new Dimension(110, 110));
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        contentPanel.setDoubleBuffered(false);
        this.add(contentPanel, BorderLayout.CENTER);
        contentPanel.add(splitPane, BorderLayout.CENTER);
        splitPane.add(historyPanel, JSplitPane.BOTTOM);
        splitPane.add(populationPanel, JSplitPane.TOP);
    }

    void update() {
        this.historyPanel.repaint();
        this.populationPanel.repaint();
    }
}
