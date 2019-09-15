package wodka.view;

import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Controller for the PopulationHistory Panel
 */

public class PopulationHistoryControllerPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private PopulationHistoryPanel histPanel = null;

    GridLayout gridLayout1 = new GridLayout();

    JButton eastEndButton = new JButton();

    JButton eastButton = new JButton();

    JButton westButton = new JButton();

    JButton westEndButtonButton = new JButton();

    JButton northEndButton = new JButton();

    JButton northButton = new JButton();

    JButton southButton = new JButton();

    JButton southEndButton = new JButton();

    JButton southVeryFastButton = new JButton();

    JButton southFastButton = new JButton();

    JButton northVeryFastButton = new JButton();

    JButton northFastButton = new JButton();

    public PopulationHistoryControllerPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        gridLayout1.setColumns(4);
        gridLayout1.setRows(3);
        this.setLayout(gridLayout1);
        eastEndButton.setMargin(new Insets(5, 5, 5, 5));
        eastEndButton.setIcon(new ImageIcon(this.getClass().getClassLoader()
                .getResource("arrow_b1.gif")));
        eastEndButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eastEndButton_actionPerformed();
            }
        });
        eastButton.setMargin(new Insets(5, 5, 5, 5));
        eastButton.setIcon(new ImageIcon(this.getClass().getClassLoader()
                .getResource("arrow_a1.gif")));
        eastButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eastButton_actionPerformed();
            }
        });
        westButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                westButton_actionPerformed();
            }
        });
        westEndButtonButton.setMargin(new Insets(5, 5, 5, 5));
        westEndButtonButton.setIcon(new ImageIcon(this.getClass()
                .getClassLoader().getResource("arrow_b2.gif")));
        westEndButtonButton
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        westEndButtonButton_actionPerformed();
                    }
                });
        northEndButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                northEndButton_actionPerformed();
            }
        });
        northButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                northButton_actionPerformed();
            }
        });
        southButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                southButton_actionPerformed();
            }
        });
        southEndButton.setMargin(new Insets(5, 5, 5, 5));
        southEndButton.setIcon(new ImageIcon(this.getClass().getClassLoader()
                .getResource("arrow_b4.gif")));
        southEndButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                southEndButton_actionPerformed();
            }
        });
        northEndButton.setMargin(new Insets(5, 5, 5, 5));
        northEndButton.setIcon(new ImageIcon(this.getClass().getClassLoader()
                .getResource("arrow_b3.gif")));
        northButton.setMargin(new Insets(5, 5, 5, 5));
        northButton.setIcon(new ImageIcon(this.getClass().getClassLoader()
                .getResource("arrow_a3.gif")));
        southButton.setMargin(new Insets(5, 5, 5, 5));
        southButton.setIcon(new ImageIcon(this.getClass().getClassLoader()
                .getResource("arrow_a4.gif")));
        westButton.setMargin(new Insets(5, 5, 5, 5));
        westButton.setIcon(new ImageIcon(this.getClass().getClassLoader()
                .getResource("arrow_a2.gif")));
        southVeryFastButton.setMargin(new Insets(5, 5, 5, 5));
        southVeryFastButton.setIcon(new ImageIcon(this.getClass()
                .getClassLoader().getResource("arrow_d2.gif")));
        southVeryFastButton
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        southVeryFastButton_actionPerformed();
                    }
                });
        northFastButton.setMargin(new Insets(5, 5, 5, 5));
        northFastButton.setIcon(new ImageIcon(this.getClass().getClassLoader()
                .getResource("arrow_c4.gif")));
        northFastButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                northFastButton_actionPerformed();
            }
        });
        northVeryFastButton.setMargin(new Insets(5, 5, 5, 5));
        northVeryFastButton.setIcon(new ImageIcon(this.getClass()
                .getClassLoader().getResource("arrow_d4.gif")));
        northVeryFastButton
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        northVeryFastButton_actionPerformed();
                    }
                });
        southFastButton.setMargin(new Insets(5, 5, 5, 5));
        southFastButton.setIcon(new ImageIcon(this.getClass().getClassLoader()
                .getResource("arrow_c2.gif")));
        southFastButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                southFastButton_actionPerformed();
            }
        });
        this.add(northButton, null);
        this.add(northFastButton, null);
        this.add(northVeryFastButton, null);
        this.add(northEndButton, null);
        this.add(westEndButtonButton, null);
        this.add(westButton, null);
        this.add(eastButton, null);
        this.add(eastEndButton, null);
        this.add(southButton, null);
        this.add(southFastButton, null);
        this.add(southVeryFastButton, null);
        this.add(southEndButton, null);
    }

    void setPopulationHistoryPanel(PopulationHistoryPanel panel) {
        this.histPanel = panel;
    }

    void northButton_actionPerformed() {
        histPanel.goNorth(1);
        histPanel.repaint();
    }

    void northEndButton_actionPerformed() {
        histPanel.goNorthEnd();
        histPanel.repaint();
    }

    void westEndButtonButton_actionPerformed() {
        histPanel.goWestEnd();
        histPanel.repaint();
    }

    void westButton_actionPerformed() {
        histPanel.goWest();
        histPanel.repaint();
    }

    void eastButton_actionPerformed() {
        histPanel.goEast();
        histPanel.repaint();
    }

    void eastEndButton_actionPerformed() {
        histPanel.goEastEnd();
        histPanel.repaint();
    }

    void southButton_actionPerformed() {
        histPanel.goSouth(1);
        histPanel.repaint();
    }

    void southEndButton_actionPerformed() {
        histPanel.goSouthEnd();
        histPanel.repaint();
    }

    void northFastButton_actionPerformed() {
        histPanel.goNorth(6);
        histPanel.repaint();
    }

    void northVeryFastButton_actionPerformed() {
        histPanel.goNorth(100);
        histPanel.repaint();
    }

    void southFastButton_actionPerformed() {
        histPanel.goSouth(6);
        histPanel.repaint();
    }

    void southVeryFastButton_actionPerformed() {
        histPanel.goSouth(100);
        histPanel.repaint();
    }
}