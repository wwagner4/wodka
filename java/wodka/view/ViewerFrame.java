package wodka.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import wodka.ga.soda.Breeder;
import wodka.util.IoUtil;

/**
 * A frame to display a GA.
 */

public class ViewerFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    BorderLayout borderLayout1 = new BorderLayout();

    ViewerPanel viewerPanel = new ViewerPanel();

    JPanel buttonsPanel = new JPanel();

    JButton openControllerButton = new JButton();

    JButton updatButton = new JButton();

    FlowLayout flowLayout1 = new FlowLayout();

    Updater updater = null;

    public ViewerFrame() {
        super();
        try {
            this.setSize(700, 600);
            this.setLocation(10, 140);
            initComponents();
            updater = new Updater();
            updater.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        this.setTitle("wodka viewer");
        URL url = IoUtil.current().loadResource("bottle.jpg");
        this.setIconImage(new ImageIcon(url).getImage());
        this.getContentPane().setLayout(borderLayout1);
        openControllerButton.setText("controller");
        openControllerButton
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        openControllerButton_actionPerformed();
                    }
                });
        updatButton.setText("update");
        updatButton.setFont(new java.awt.Font("Dialog", 0, 10));
        updatButton.setPreferredSize(new Dimension(0, 20));
        updatButton.setMinimumSize(new Dimension(0, 20));
        updatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updatButton_actionPerformed();
            }
        });
        buttonsPanel.setLayout(flowLayout1);
        flowLayout1.setAlignment(FlowLayout.LEFT);
        flowLayout1.setHgap(3);
        flowLayout1.setVgap(3);
        this.getContentPane().add(viewerPanel, BorderLayout.CENTER);
        this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
        buttonsPanel.add(updatButton, null);
        buttonsPanel.add(openControllerButton, null);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm();
            }
        });
    }

    protected void exitForm() {
        // Do nothing
    }

    void openControllerButton_actionPerformed() {
        ControllerFrame f = new ControllerFrame(this);
        f.setPopulationPanel(this.viewerPanel.populationPanel);
        f.setPopulationHistoryPanel(this.viewerPanel.historyPanel);
        f.setVisible(true);
    }

    public void setBreeder(Breeder breeder) {
        this.viewerPanel.setBreeder(breeder);
    }

    void updatButton_actionPerformed() {
        this.viewerPanel.update();
    }

    private class Updater extends Thread {
        public void run() {
            while (true) {
                pause(2000);
                viewerPanel.update();
            }
        }

        private synchronized void pause(int time) {
            try {
                this.wait(time);
            } catch (InterruptedException ex) {
                // Continue
            }
        }
    }
}