import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.undo.*;
import javax.vecmath.*;

public class GameModel extends Observable {

	java.util.List<Point> peaksArr = new ArrayList<Point>();
	Rectangle2D pad = new Rectangle2D.Double(330, 100, 40, 10);
	ArrayList<Ellipse2D> circles = new ArrayList<>();
	Polygon terrain = new Polygon();
	int curCircleSel = -1;
	
    public GameModel(int fps, int width, int height, int peaks) {

        ship = new Ship(60, width/2, 50);
        worldBounds = new Rectangle2D.Double(0, 0, width, height);

        // anonymous class to monitor ship updates
        ship.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                setChangedAndNotify();
            }
        });
        Random rand = new Random();
        for (int i = 0; i < 20; i++) {
        	peaksArr.add(new Point(i*700/19, rand.nextInt(100) + 100));
        }
        for (Point peak : peaksArr) {
        	terrain.addPoint((int) peak.getX(), (int) peak.getY());
        	circles.add(new Ellipse2D.Double(peak.getX() - 15, peak.getY() - 15, 30, 30));
        }
        terrain.addPoint(700, 200);
        terrain.addPoint(0, 200);
    }

    // World
    // - - - - - - - - - - -
    public final Rectangle2D getWorldBounds() {
        return worldBounds;
    }

    Rectangle2D.Double worldBounds;


    // Ship
    // - - - - - - - - - - -

    public Ship ship;

    // Observerable
    // - - - - - - - - - - -

    // helper function to do both
    void setChangedAndNotify() {
        setChanged();
        notifyObservers();
    }
    
    public java.util.List<Point> getPeaks() {
    	return peaksArr;
    }
    
    public void updatePeak(int index, int y) {
    	if (y < 0) {y = 0;}
    	if (y > 200) {y = 200;}
    	terrain.ypoints[curCircleSel] = y;
    	peaksArr.get(index).y = y;
    	setChangedAndNotify();
    }
    
    public void updatePad(int x, int y) {
    	double xPar = x - pad.getWidth()/2;
    	double yPar = y - pad.getHeight()/2;
    	if (xPar < 0) {xPar = 0;}
    	if (xPar > 660) {xPar = 660;}
    	if (yPar < 0) {yPar = 0;}
    	if (yPar > 190) {yPar = 190;}
    	pad.setRect(xPar, yPar, pad.getWidth(), pad.getHeight());
    }
    
    public void updateCircle(int y) {
    	if (y < 0) {y = 0;}
    	if (y > 200) {y = 200;}
    	circles.set(curCircleSel, new Ellipse2D.Double(peaksArr.get(curCircleSel).getX() - 15,
				peaksArr.get(curCircleSel).getY() - 15, 30, 30));
    }
}



