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
	Rectangle2D bg = new Rectangle2D.Double(0, 0, 700, 200);
	ArrayList<Ellipse2D> circles = new ArrayList<>();
	Polygon terrain = new Polygon();
	int curCircleSel = -1;
	// Undo manager
	private UndoManager undoManager;
	double oldPadXValue;
	double oldPadYValue;
	int oldPeakValue;
    Rectangle2D.Double worldBounds;
    public Ship ship;  
    boolean isEnhanced = false;
    
    public GameModel(int fps, int width, int height, int peaks) {

        ship = new Ship(60, width/2, 50);
        worldBounds = new Rectangle2D.Double(0, 0, width, height);
        undoManager = new UndoManager();

        // anonymous class to monitor ship updates
        ship.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
            	Rectangle2D shipRect = ship.getShape();
            	if (!ship.isCrashed) {
            		if (terrain.intersects(shipRect) || !worldBounds.contains(shipRect)) {
            			ship.timer.stop();
            			ship.isCrashed = true;            			
            		} else if (pad.intersects(shipRect) && ship.getSpeed() < ship.getSafeLandingSpeed()) {
                        ship.timer.stop();
                        ship.isLanded = true;
            		} else if (pad.intersects(shipRect) && 
            				(ship.getSpeed() >= ship.getSafeLandingSpeed()
            				|| ship.getSpeed() >= 1)) {
                        ship.timer.stop();
                        ship.isCrashed = true;
            		}
            		
            	}
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
    
    public void updatePeak(int index, int y, boolean undoAble) {
    	int yVal;
    	if (y < 0) {yVal = 0;}
    	else if (y > 200) {yVal = 200;}
    	else {yVal = y;}
    	if (undoAble) {
    		// create undoable edit
    		UndoableEdit undoableEdit = new AbstractUndoableEdit() {

    			// capture variables for closure
    			final int oldYValue = oldPeakValue;
    			final int newYValue = yVal;
    			final int newIndex = index;

    			// Method that is called when we must redo the undone action
    			public void redo() throws CannotRedoException {
    				super.redo();
    				updatePeak(newIndex, newYValue, false);
    				setChangedAndNotify();
    			}

    			public void undo() throws CannotUndoException {
    				super.undo();
    				updatePeak(newIndex, oldYValue, false);
    				setChangedAndNotify();
    			}
    		};

    		// Add this undoable edit to the undo manager
    		undoManager.addEdit(undoableEdit);
    	}
    	terrain.ypoints[index] = yVal;
    	peaksArr.get(index).y = yVal;
    	circles.set(index, new Ellipse2D.Double(peaksArr.get(index).getX() - 15,
				peaksArr.get(index).getY() - 15, 30, 30));
    	setChangedAndNotify();
    }
    
    public void updatePad(int x, int y, boolean undoAble) {
    	if (undoAble) {
    		// create undoable edit
    		UndoableEdit undoableEdit = new AbstractUndoableEdit() {

    			// capture variables for closure
    			final double oldXValue = oldPadXValue;
    			final int newXValue = x;
    			final double oldYValue = oldPadYValue;
    			final int newYValue = y;

    			// Method that is called when we must redo the undone action
    			public void redo() throws CannotRedoException {
    				super.redo();
    				updatePad(newXValue, newYValue, false);
    				setChangedAndNotify();
    			}

    			public void undo() throws CannotUndoException {
    				super.undo();
    				pad.setRect(oldXValue, oldYValue, pad.getWidth(), pad.getHeight());
    				setChangedAndNotify();
    			}
    		};

    		// Add this undoable edit to the undo manager
    		undoManager.addEdit(undoableEdit);
    	}
    	double xPar = x - pad.getWidth()/2;
    	double yPar = y - pad.getHeight()/2;
    	if (xPar < 0) {xPar = 0;}
    	if (xPar > 660) {xPar = 660;}
    	if (yPar < 0) {yPar = 0;}
    	if (yPar > 190) {yPar = 190;}
    	pad.setRect(xPar, yPar, pad.getWidth(), pad.getHeight());
    	setChangedAndNotify();
    }
    
	// undo and redo methods
	// - - - - - - - - - - - - - -

	public void undo() {
		if (canUndo())
			undoManager.undo();
	}

	public void redo() {
		if (canRedo())
			undoManager.redo();
	}

	public boolean canUndo() {
		return undoManager.canUndo();
	}

	public boolean canRedo() {
		return undoManager.canRedo();
	}

}



