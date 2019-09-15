package wodka.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import wodka.ga.Individual;
import wodka.ga.soda.SodaraceProgram;

/**
 * A panel to display an Individual
 */

public class IndividualPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    BorderLayout borderLayout1 = new BorderLayout();

    ModelResizePanel modelPanel = new ModelResizePanel();

    JTextArea infoTextArea = new JTextArea();

    private Individual individual = null;

    public IndividualPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setIndividual(Individual ind) {
        individual = ind;
        if (ind != null)
            this.infoTextArea.setText(ind.getInfoString());
        else
            this.infoTextArea.setText("");
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (individual == null)
            this.modelPanel.setModel(null);
        else {
            SodaraceProgram pgm = (SodaraceProgram) individual.getGeno();
            modelPanel.setModel(pgm.eval());
        }
        this.modelPanel.repaint();
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        infoTextArea.setBackground(Color.lightGray);
        infoTextArea.setEditable(false);
        this.add(modelPanel, BorderLayout.CENTER);
        this.add(infoTextArea, BorderLayout.SOUTH);
    }

}