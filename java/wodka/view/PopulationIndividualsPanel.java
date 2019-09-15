package wodka.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import wodka.ga.Population;
import wodka.ga.soda.Breeder;

/**
 * A panel to display a Population
 */

public class PopulationIndividualsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private Breeder breeder = null;

    private int popIndex = -1;

    private IndividualPanel[] mps = new IndividualPanel[7];

    private int offset = 0;

    JPanel individualPanel = new JPanel();

    IndividualPanel indiPanel01 = new IndividualPanel();

    IndividualPanel indiPanel02 = new IndividualPanel();

    IndividualPanel indiPanel03 = new IndividualPanel();

    IndividualPanel indiPanel04 = new IndividualPanel();

    IndividualPanel indiPanel05 = new IndividualPanel();

    IndividualPanel indiPanel06 = new IndividualPanel();

    IndividualPanel indiPanel07 = new IndividualPanel();

    GridLayout gridLayout2 = new GridLayout();

    BorderLayout borderLayout1 = new BorderLayout();

    JTextArea infoTextArea = new JTextArea();

    public PopulationIndividualsPanel() {
        try {
            mps[0] = indiPanel01;
            mps[1] = indiPanel02;
            mps[2] = indiPanel03;
            mps[3] = indiPanel04;
            mps[4] = indiPanel05;
            mps[5] = indiPanel06;
            mps[6] = indiPanel07;
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBreeder(Breeder runner, int popIndex) {
        this.popIndex = popIndex;
        this.breeder = runner;
        if (population() != null)
            infoTextArea.setText(population().toInfoString());
    }

    private Population population() {
        Population result = null;
        if (popIndex < 0) {
            result = breeder.getGenAlgo().getPopulation();
        } else {
            result = breeder.getGenAlgo().getPopHist().getPopulation(popIndex);
        }
        return result;
    }

    public synchronized void paint(Graphics g) {
        super.paint(g);
        if (breeder != null) {
            if (population() != null) {
                for (int i = 0; i < 7; i++) {
                    mps[i]
                            .setIndividual(population().getIndividual(
                                    offset + i));
                    mps[i].repaint();
                }
            } else {
                for (int i = 0; i < 7; i++) {
                    mps[i].setIndividual(null);
                    mps[i].repaint();
                }
            }
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        this.setDoubleBuffered(false);
        individualPanel.setLayout(gridLayout2);
        infoTextArea.setText("0");
        infoTextArea.setBackground(Color.gray);
        this.add(individualPanel, BorderLayout.CENTER);
        individualPanel.add(indiPanel01, null);
        individualPanel.add(indiPanel02, null);
        individualPanel.add(indiPanel03, null);
        individualPanel.add(indiPanel04, null);
        individualPanel.add(indiPanel05, null);
        individualPanel.add(indiPanel06, null);
        individualPanel.add(indiPanel07, null);
        this.add(infoTextArea, BorderLayout.WEST);
    }

    void goEast() {
        if (population() != null) {
            if (offset + 7 < population().size()) {
                offset++;
            }
        }
    }

    void goEastEnd() {
        if (population() != null) {
            if (offset + 7 < population().size()) {
                offset = population().size() - 7;
            }
        }
    }

    void goWest() {
        if (offset > 0) {
            offset--;
        }
    }

    void goWestEnd() {
        if (offset > 0) {
            offset = 0;
        }
    }

    int getOffset() {
        return offset;
    }
}