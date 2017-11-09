import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;

// the edit toolbar
public class ToolBarView extends JPanel implements Observer {

    JButton undo = new JButton("Undo");
    JButton redo = new JButton("Redo");
    GameModel model;

    public ToolBarView(GameModel model) {

        setLayout(new FlowLayout(FlowLayout.LEFT));
        this.model = model;
        undo.setEnabled(model.canUndo());
    	redo.setEnabled(model.canRedo());
    	model.addObserver(this);

        // prevent buttons from stealing focus
        undo.setFocusable(false);
        redo.setFocusable(false);

        add(undo);
        add(redo);
        
        undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				model.undo();
			}
		});
        redo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				model.redo();
			}
        });
    }

    @Override
    public void update(Observable o, Object arg) {
    	undo.setEnabled(model.canUndo());
    	redo.setEnabled(model.canRedo());
    }
}
