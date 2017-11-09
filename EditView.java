import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

// the editable view of the terrain and landing pad
public class EditView extends JPanel implements Observer {

	boolean curPadSel = false;
	GameModel model;
	
    public EditView(GameModel model) {
        this.model = model;
        // want the background to be black
        setBackground(Color.BLACK);
        repaint();
        model.addObserver(this);
        this.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent e) {
        		if (e.getClickCount() == 2) {
        			model.oldPadXValue = model.pad.getX();
					model.oldPadYValue = model.pad.getY();
        			model.updatePad(e.getX(), e.getY(), true);
        			repaint();
        		}
        	}

			@Override
			public void mousePressed(MouseEvent e) {
				if (model.pad.contains(e.getX(), e.getY())) {
					curPadSel = true;
					model.oldPadXValue = model.pad.getX();
					model.oldPadYValue = model.pad.getY();
					return;
				}
				for (int i = 0; i < 20; i++) {
					if (model.circles.get(i).contains(e.getX(), e.getY())) {
						model.curCircleSel = i;
						model.oldPeakValue = (int) model.getPeaks().get(i).getY();
						break;
					}
				}
				
				super.mousePressed(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (curPadSel) {
					model.updatePad(e.getX(), e.getY(), true);
					repaint();
					curPadSel = false;
					return;
				}
				if (model.curCircleSel != -1) {
					model.terrain.ypoints[model.curCircleSel] = e.getY();
					model.updatePeak(model.curCircleSel, e.getY(), true);			
					model.curCircleSel = -1;
				}
				super.mouseReleased(e);
			}
		});
        this.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				if (curPadSel) {
					model.updatePad(e.getX(), e.getY(), false);
					repaint();
					return;
				}
				if (model.curCircleSel != -1) {
					model.updatePeak(model.curCircleSel, e.getY(), false);				
				}
				super.mouseDragged(e);
			}
        	
		});
    }

    @Override
    public void update(Observable o, Object arg) {
    	repaint();
    }

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;  
		g2.setColor(Color.darkGray);
		g2.fillPolygon(model.terrain);
		g2.setColor(Color.gray);
		for (int i = 0; i < 20; i++) {
			g2.draw(model.circles.get(i));
		}
		if (model.curCircleSel != -1) {
			g2.setColor(Color.white);
			g2.setStroke(new BasicStroke(3));
			g2.draw(model.circles.get(model.curCircleSel));
		}
		g2.setColor(Color.RED);
		g2.fill(model.pad);
		if (curPadSel) {
			g2.setColor(Color.white);
			g2.setStroke(new BasicStroke(3));
			g2.draw(model.pad);
		}
	}

}
