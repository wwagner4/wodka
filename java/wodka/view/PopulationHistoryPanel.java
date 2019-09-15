package wodka.view;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import wodka.ga.soda.Breeder;

/**
 * A panel that can display a PopulationHistory.
 */

public class PopulationHistoryPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private Breeder breeder = null;

    private int xOffset = 0;

    private int yOffset = 0;

    private PopulationIndividualsPanel[] pps = new PopulationIndividualsPanel[6];

    JTextArea[] xNumTextArea = new JTextArea[7];

    JTextArea[] yNumTextArea = new JTextArea[6];

    GridBagLayout gridBagLayout1 = new GridBagLayout();

    JPanel fillPanel = new JPanel();

    JPanel xNumberPanel = new JPanel();

    JPanel yNumberPanel = new JPanel();

    JPanel popsPanel = new JPanel();

    GridLayout gridLayout1 = new GridLayout();

    JTextArea xNumTextArea0 = new JTextArea();

    JTextArea xNumTextArea6 = new JTextArea();

    JTextArea xNumTextArea5 = new JTextArea();

    JTextArea xNumTextArea4 = new JTextArea();

    JTextArea xNumTextArea3 = new JTextArea();

    JTextArea xNumTextArea2 = new JTextArea();

    JTextArea xNumTextArea1 = new JTextArea();

    GridLayout gridLayout2 = new GridLayout();

    JTextArea yNumTextArea0 = new JTextArea();

    JTextArea yNumTextArea1 = new JTextArea();

    JTextArea yNumTextArea2 = new JTextArea();

    JTextArea yNumTextArea3 = new JTextArea();

    JTextArea yNumTextArea4 = new JTextArea();

    JTextArea yNumTextArea5 = new JTextArea();

    GridLayout gridLayout3 = new GridLayout();

    PopulationIndividualsPanel popPanel0 = new PopulationIndividualsPanel();

    PopulationIndividualsPanel popPanel5 = new PopulationIndividualsPanel();

    PopulationIndividualsPanel popPanel4 = new PopulationIndividualsPanel();

    PopulationIndividualsPanel popPanel3 = new PopulationIndividualsPanel();

    PopulationIndividualsPanel popPanel2 = new PopulationIndividualsPanel();

    PopulationIndividualsPanel popPanel1 = new PopulationIndividualsPanel();

    public PopulationHistoryPanel() {
        try {
            pps[0] = popPanel0;
            pps[1] = popPanel1;
            pps[2] = popPanel2;
            pps[3] = popPanel3;
            pps[4] = popPanel4;
            pps[5] = popPanel5;
            xNumTextArea[0] = xNumTextArea0;
            xNumTextArea[1] = xNumTextArea1;
            xNumTextArea[2] = xNumTextArea2;
            xNumTextArea[3] = xNumTextArea3;
            xNumTextArea[4] = xNumTextArea4;
            xNumTextArea[5] = xNumTextArea5;
            xNumTextArea[6] = xNumTextArea6;
            yNumTextArea[0] = yNumTextArea0;
            yNumTextArea[1] = yNumTextArea1;
            yNumTextArea[2] = yNumTextArea2;
            yNumTextArea[3] = yNumTextArea3;
            yNumTextArea[4] = yNumTextArea4;
            yNumTextArea[5] = yNumTextArea5;
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBreeder(Breeder breeder) {
        this.breeder = breeder;
    }

    public void paint(Graphics g) {
        super.paint(g);
        for (int i = 0; i < 6; i++) {
            pps[i].setBreeder(breeder, yOffset + i);
            pps[i].repaint();
        }
        for (int i = 0; i < 7; i++) {
            xNumTextArea[i].setText("" + (xOffset + i));
        }
        for (int i = 0; i < 6; i++) {
            yNumTextArea[i].setText("" + (yOffset + i));
        }
    }

    void goEast() {
        for (int i = 0; i < 6; i++) {
            pps[i].goEast();
        }
        xOffset = pps[0].getOffset();
    }

    void goEastEnd() {
        for (int i = 0; i < 6; i++) {
            pps[i].goEastEnd();
        }
        xOffset = pps[0].getOffset();
    }

    void goWest() {
        for (int i = 0; i < 6; i++) {
            pps[i].goWest();
        }
        xOffset = pps[0].getOffset();
    }

    void goWestEnd() {
        for (int i = 0; i < 6; i++) {
            pps[i].goWestEnd();
        }
        xOffset = pps[0].getOffset();
    }

    void goNorth(int val) {
        if (yOffset > 0)
            yOffset = Math.max(yOffset - val, 0);
    }

    void goNorthEnd() {
        if (yOffset > 0)
            yOffset = 0;
    }

    void goSouth(int val) {
        if (yOffset < breeder.getGenAlgo().getPopHist().size() - 6)
            yOffset = Math.min(yOffset + val, breeder.getGenAlgo().getPopHist()
                    .size() - 6);
    }

    void goSouthEnd() {
        if (yOffset < breeder.getGenAlgo().getPopHist().size() - 6)
            yOffset = breeder.getGenAlgo().getPopHist().size() - 6;
    }

    private void jbInit() throws Exception {
        this.setLayout(gridBagLayout1);
        xNumberPanel.setLayout(gridLayout1);
        gridLayout1.setColumns(7);
        yNumberPanel.setLayout(gridLayout2);
        gridLayout2.setColumns(1);
        gridLayout2.setRows(6);
        popsPanel.setLayout(gridLayout3);
        gridLayout3.setColumns(1);
        gridLayout3.setRows(6);
        this.add(fillPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        0, 0, 0, 0), 0, 0));
        this.add(xNumberPanel, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        0, 0, 0, 0), 0, 0));
        this.add(yNumberPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        0, 0, 0, 0), 0, 0));
        this.add(popsPanel, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        0, 0, 0, 0), 0, 0));
        xNumberPanel.add(xNumTextArea0, null);
        xNumberPanel.add(xNumTextArea1, null);
        xNumberPanel.add(xNumTextArea2, null);
        xNumberPanel.add(xNumTextArea3, null);
        xNumberPanel.add(xNumTextArea4, null);
        xNumberPanel.add(xNumTextArea5, null);
        xNumberPanel.add(xNumTextArea6, null);
        yNumberPanel.add(yNumTextArea0, null);
        yNumberPanel.add(yNumTextArea1, null);
        yNumberPanel.add(yNumTextArea2, null);
        yNumberPanel.add(yNumTextArea3, null);
        yNumberPanel.add(yNumTextArea4, null);
        yNumberPanel.add(yNumTextArea5, null);
        popsPanel.add(popPanel0, null);
        popsPanel.add(popPanel1, null);
        popsPanel.add(popPanel2, null);
        popsPanel.add(popPanel3, null);
        popsPanel.add(popPanel4, null);
        popsPanel.add(popPanel5, null);
    }
}