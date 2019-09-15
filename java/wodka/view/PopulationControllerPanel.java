package wodka.view;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * A panel with control elements for a single population Panel.
 */

public class PopulationControllerPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private PopulationPanel popPanel = null;

    GridLayout gridLayout1 = new GridLayout();

    JButton goWestEndButton = new JButton();

    JButton goEastEndButton = new JButton();

    JButton goEastButton = new JButton();

    JButton goWestButton = new JButton();

    public PopulationControllerPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setPopulationPanel(PopulationPanel popPanel) {
        this.popPanel = popPanel;
    }

    private void jbInit() throws Exception {
        goWestEndButton.setIcon(new ImageIcon(this.getClass().getClassLoader()
                .getResource("arrow_b2.gif")));
        goWestEndButton.setMargin(new Insets(5, 5, 5, 5));
        goWestEndButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goWestEndButton_actionPerformed();
            }
        });
        this.setLayout(gridLayout1);
        goEastEndButton.setIcon(new ImageIcon(this.getClass().getClassLoader()
                .getResource("arrow_b1.gif")));
        goEastEndButton.setMargin(new Insets(5, 5, 5, 5));
        goEastEndButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goEastEndButton_actionPerformed();
            }
        });
        goEastButton.setIcon(new ImageIcon(this.getClass().getClassLoader()
                .getResource("arrow_a1.gif")));
        goEastButton.setMargin(new Insets(5, 5, 5, 5));
        goEastButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goEastButton_actionPerformed();
            }
        });
        goWestButton.setIcon(new ImageIcon(this.getClass().getClassLoader()
                .getResource("arrow_a2.gif")));
        goWestButton.setMargin(new Insets(5, 5, 5, 5));
        goWestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goWestButton_actionPerformed();
            }
        });
        this.add(goWestEndButton, null);
        this.add(goWestButton, null);
        this.add(goEastButton, null);
        this.add(goEastEndButton, null);
    }

    void goWestEndButton_actionPerformed() {
        if (popPanel != null) {
            popPanel.goWestEnd();
            popPanel.repaint();
        }
    }

    void goWestButton_actionPerformed() {
        if (popPanel != null) {
            popPanel.goWest();
            popPanel.repaint();
        }
    }

    void goEastButton_actionPerformed() {
        if (popPanel != null) {
            popPanel.goEast();
            popPanel.repaint();
        }
    }

    void goEastEndButton_actionPerformed() {
        if (popPanel != null) {
            popPanel.goEastEnd();
            popPanel.repaint();
        }
    }
}