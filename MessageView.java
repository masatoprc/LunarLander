import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

public class MessageView extends JPanel implements Observer {

    // status messages for game
    JLabel fuel;
    JLabel speed;
    JLabel message;
    GameModel model;

    public MessageView(GameModel model) {

    	this.model = model;
    	model.addObserver(this);
        // want the background to be black
        setBackground(Color.BLACK);

        setLayout(new FlowLayout(FlowLayout.LEFT));

        fuel = new JLabel("Fuel: " + (int) model.ship.getFuel());
        speed = new JLabel("Speed: " + model.ship.getSpeed());
        message = new JLabel("");
        add(fuel);
        add(speed);
        add(message);

        for (Component c: this.getComponents()) {
            c.setForeground(Color.WHITE);
            c.setPreferredSize(new Dimension(100, 20));
        }
    }


    @Override
	protected void paintComponent(Graphics g) {		
		DecimalFormat df = new DecimalFormat("#.00");
	    String speedFormated = df.format(model.ship.getSpeed());
		speed.setText("Speed: " + speedFormated);
	    if (model.ship.getSpeed() <= model.ship.getSafeLandingSpeed()) {
	    	speed.setForeground(Color.green);
	    } else {
	    	speed.setForeground(Color.white);
	    }
		int fuelAmount = (int) model.ship.getFuel();
		fuel.setText("Fuel: " + fuelAmount);
		if (fuelAmount < 10) {
			fuel.setForeground(Color.RED);
		} else {
			fuel.setForeground(Color.WHITE);
		}
		if (model.ship.isCrashed) {
			message.setText("CRASH");
		} else if (model.ship.isLanded) {
			message.setText("LANDED!");
		} else if (model.ship.isPaused()) {
			message.setText("(Paused)");
		} else {
			message.setText("");
		}
		super.paintComponent(g);
	}


	@Override
    public void update(Observable o, Object arg) {
    	repaint();
    }
}