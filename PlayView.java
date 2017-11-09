import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;

// the actual game view
public class PlayView extends JPanel implements Observer {

	Rectangle2D ship = new Rectangle2D.Double(350, 50, 10, 10);
	GameModel model;
    @Override
	protected void paintComponent(Graphics g) {
    	final int scale = 3;
    	super.paintComponent(g);
    	Graphics2D g2 = (Graphics2D) g;  
    	AffineTransform M = g2.getTransform();
    	AffineTransform TTer = (AffineTransform) M.clone();
		TTer.concatenate(AffineTransform.getTranslateInstance(
				this.getWidth()/2 + scale * (model.terrain.getBounds().getCenterX() - model.ship.getPosition().getX() - 5), 
				this.getHeight()/2 + scale * (model.terrain.getBounds().getCenterY() - model.ship.getPosition().getY() - 5)));
    	TTer.concatenate(AffineTransform.getScaleInstance(3, 3));
    	TTer.concatenate(AffineTransform.getTranslateInstance(-model.terrain.getBounds().getCenterX(), -model.terrain.getBounds().getCenterY()));
    	g2.setTransform(TTer);
    	g2.setColor(Color.darkGray);
		g2.fillPolygon(model.terrain);
		AffineTransform TPad = (AffineTransform) M.clone();
		TPad.concatenate(AffineTransform.getTranslateInstance(
				this.getWidth()/2 + scale * (model.pad.getCenterX() - model.ship.getPosition().getX() - 5), 
				this.getHeight()/2 + scale * (model.pad.getCenterY() - model.ship.getPosition().getY() - 5)));
    	TPad.concatenate(AffineTransform.getScaleInstance(3, 3));
    	TPad.concatenate(AffineTransform.getTranslateInstance(-model.pad.getCenterX(), -model.pad.getCenterY()));
    	g2.setTransform(TPad);
		g2.setColor(Color.RED);
		g2.fill(model.pad);
		AffineTransform TShip = (AffineTransform) M.clone();
		TShip.concatenate(AffineTransform.getTranslateInstance(this.getWidth()/2, this.getHeight()/2));
    	TShip.concatenate(AffineTransform.getScaleInstance(3, 3));
    	TShip.concatenate(AffineTransform.getTranslateInstance(-ship.getCenterX(), -ship.getCenterY()));
    	g2.setTransform(TShip);
		g2.setColor(Color.blue);
		g2.fill(ship);
    	// reset the transform to what it was before we drew the shape
        g2.setTransform(M);     	
	}

	public PlayView(GameModel model) {

        // needs to be focusable for keylistener
        setFocusable(true);
        model.addObserver(this);
        this.model = model;
        // want the background to be black
        setBackground(Color.lightGray);
    }

    @Override
    public void update(Observable o, Object arg) {
    	repaint();
    }
}
