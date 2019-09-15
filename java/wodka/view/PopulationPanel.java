package wodka.view;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import wodka.ga.soda.Breeder;

/**
 * A panel to display a populatin showing the number of each individual.
 */

public class PopulationPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    JTextArea[] numTextArea = new JTextArea[7];

    BorderLayout borderLayout1 = new BorderLayout();

    JPanel numPanel = new JPanel();

    PopulationIndividualsPanel popPanel = new PopulationIndividualsPanel();

    GridLayout gridLayout1 = new GridLayout();

    JTextArea numTextArea0 = new JTextArea();

    JTextArea numTextArea1 = new JTextArea();

    JTextArea numTextArea2 = new JTextArea();

    JTextArea numTextArea3 = new JTextArea();

    JTextArea numTextArea4 = new JTextArea();

    JTextArea numTextArea5 = new JTextArea();

    JTextArea numTextArea6 = new JTextArea();

    public PopulationPanel() {
        try {
            numTextArea[0] = numTextArea0;
            numTextArea[1] = numTextArea1;
            numTextArea[2] = numTextArea2;
            numTextArea[3] = numTextArea3;
            numTextArea[4] = numTextArea4;
            numTextArea[5] = numTextArea5;
            numTextArea[6] = numTextArea6;
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        numPanel.setLayout(gridLayout1);
        this.setDoubleBuffered(false);
        this.add(numPanel, BorderLayout.SOUTH);
        numPanel.add(numTextArea0, null);
        numPanel.add(numTextArea1, null);
        numPanel.add(numTextArea2, null);
        numPanel.add(numTextArea3, null);
        numPanel.add(numTextArea4, null);
        numPanel.add(numTextArea5, null);
        numPanel.add(numTextArea6, null);
        this.add(popPanel, BorderLayout.CENTER);
    }

    public void paint(Graphics g) {
        super.paint(g);
        for (int i = 0; i < 7; i++) {
            numTextArea[i].setText("" + (popPanel.getOffset() + i));
            popPanel.repaint();
        }
    }

    public void setBreeder(Breeder breeder) {
        this.popPanel.setBreeder(breeder, -1);
    }

    void goEast() {
        popPanel.goEast();
    }

    void goEastEnd() {
        popPanel.goEastEnd();
    }

    void goWest() {
        popPanel.goWest();
    }

    void goWestEnd() {
        popPanel.goWestEnd();
    }

    int getOffset() {
        return popPanel.getOffset();
    }
}