package wodka.view;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import wodka.ga.model.Model;

public class ModelResizePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public static final int COLOR_MODE_COLORFUL = 0;

    public static final int COLOR_MODE_ALL_WHITE = 1;

    private List models = new ArrayList();

    private ColorsIterator fgrnd = new ColorsIterator(COLOR_MODE_ALL_WHITE);

    public static final Color BACKGROUND_COLOR = Color.darkGray;

    public void setColorMode(int colorMode) {
        fgrnd = new ColorsIterator(colorMode);
    }

    public ModelResizePanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setModel(wodka.ga.model.Model model) {
        this.models = new ArrayList();
        this.models.add(model);
    }

    public void removeAllModels() {
        this.models = new ArrayList();
    }

    public void addModel(Model m) {
        if (models == null)
            this.models = new ArrayList();
        models.add(m);
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        Iterator iter = models.iterator();
        fgrnd.reset();
        int i = 0;
        while (iter.hasNext()) {
            g.setColor(fgrnd.nextColor());
            paintModel(g, (Model) iter.next(), i);
            i++;
        }
    }

    private void paintModel(Graphics g, Model mod, int i) {
        wodka.util.LinearTransform t = new wodka.util.LinearTransform(
                wodka.ga.model.Model.WIDTH, wodka.ga.model.Model.WIDTH, this
                        .getWidth(), this.getHeight(), 5.0, i * 3);
        if (mod != null) {
            Iterator masses = mod.getMasses().iterator();
            if (!masses.hasNext()) {
                g.drawString("-", 10, 20);
            } else {
                if (this.getWidth() > 200 && this.getHeight() > 200) {
                    paintMasses(g, t, masses);
                }
                paintMuscles(g, mod, t);
            }
        }
    }

    private void paintMuscles(Graphics g, Model mod,
            wodka.util.LinearTransform t) {
        Iterator miter = mod.getMuscles().iterator();
        while (miter.hasNext()) {
            wodka.ga.model.Muscle musc = (wodka.ga.model.Muscle) miter.next();
            wodka.ga.model.Mass from = musc.getFromMass();
            wodka.ga.model.Mass to = musc.getToMass();
            g.drawLine(t.transX(from.getXPos()), t.transY(from.getYPos()), t
                    .transX(to.getXPos()), t.transY(to.getYPos()));
        }
    }

    private void paintMasses(Graphics g, wodka.util.LinearTransform t,
            Iterator masses) {
        while (masses.hasNext()) {
            wodka.ga.model.Mass m = (wodka.ga.model.Mass) masses.next();
            g.drawRect(t.transX(m.getXPos()) - 2, t.transY(m.getYPos()) - 2, 4,
                    4);
            g.drawString(m.getIdentifier(), t.transX(m.getXPos()) + 5, t
                    .transY(m.getYPos()));
        }
    }

    private void jbInit() throws Exception {
        this.setDoubleBuffered(false);
    }

    private class ColorsIterator {

        private static final int MAX_COLORS = 20;

        private Color[] cols = null;

        private int i = 0;

        public ColorsIterator(int colorMode) {
            super();
            cols = initColors(colorMode);
        }

        public Color nextColor() {
            int j = i;
            i++;
            i %= MAX_COLORS;
            return cols[j];
        }

        public void reset() {
            this.i = 0;
        }

        private Color[] initColors(int colorMode) {
            Color[] lCols = new Color[MAX_COLORS];
            for (int index = 0; index < MAX_COLORS; index++) {
                lCols[index] = initColor(colorMode, index);
            }
            return lCols;
        }

        private Color initColor(int colorMode, int index) {
            switch (colorMode) {
            case COLOR_MODE_ALL_WHITE:
                return initColorAllWhite();
            default:
                return initColorColorfull(index);
            }
        }

        private Color initColorColorfull(int index) {
            switch (index % 4) {
            case 0:
                return Color.red;
            case 1:
                return Color.yellow;
            case 2:
                return Color.yellow;
            default:
                return Color.green;
            }
        }

        private Color initColorAllWhite() {
            return Color.white;
        }
    }

}