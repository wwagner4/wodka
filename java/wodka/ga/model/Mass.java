package wodka.ga.model;

import java.text.DecimalFormat;

public class Mass {

    private transient Model model;

    private String identifier;

    private int xPos;

    private int yPos;

    private double xVelo;

    private double yVelo;

    private static DecimalFormat format = new DecimalFormat("#.##");

    public Mass(Model model, int xPos, int yPos, double xVelo, double yVelo) {
        this.model = model;
        this.identifier = model.nextMassId();
        this.xPos = xPos;
        this.yPos = yPos;
        this.xVelo = xVelo;
        this.yVelo = yVelo;
    }

    public Mass(Model model, String identifier, int xPos, int yPos,
            double xVelo, double yVelo) {
        this.model = model;
        this.identifier = identifier;
        this.xPos = xPos;
        this.yPos = yPos;
        this.xVelo = xVelo;
        this.yVelo = yVelo;
    }

    public Mass(Model model, int xPos, int yPos) {
        this(model, xPos, yPos, 0.0, 0.0);
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public String toString() {
        String result = identifier;
        String vxStr = format.format(xVelo);
        String vyStr = format.format(yVelo);
        if (vxStr.equals("0") && vyStr.equals("0")) {
            result = "(" + identifier + "|" + xPos + "," + yPos + ")";
        } else {
            result = "(" + identifier + "|" + xPos + "," + yPos + "," + vxStr
                    + "," + vxStr + ")";
        }
        return result;
    }

    public void setXPos(int val) {
        this.xPos = val;
    }

    public void setYPos(int val) {
        this.yPos = val;
    }

    /**
     * @return Returns the vx.
     */
    public double getXVelo() {
        return xVelo;
    }

    /**
     * @param xVelo
     *                   The vx to set.
     */
    public void setXVelo(double xVelo) {
        this.xVelo = xVelo;
    }

    /**
     * @return Returns the vy.
     */
    public double getYVelo() {
        return yVelo;
    }

    /**
     * @param yVelo
     *                   The vy to set.
     */
    public void setYVelo(double yVelo) {
        this.yVelo = yVelo;
    }

    public Model getModel() {
        return this.model;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

}