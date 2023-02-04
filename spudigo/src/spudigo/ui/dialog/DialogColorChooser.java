package spudigo.ui.dialog;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import spudigo.Config;

public class DialogColorChooser extends JDialog implements PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	private Color color;
	
	private JColorChooser colorChooser;
    private JOptionPane optionPane;
    
    private String btnString1 = Config.getLangBundle().getString("ok");
    private String btnString2 = Config.getLangBundle().getString("cancel");

    public Color getColor() {
        return color;
    }

    /** Creates the reusable dialog. */
    public DialogColorChooser(Window parent, Color color) {
    	super(parent, Dialog.ModalityType.APPLICATION_MODAL);
    	this.setResizable(false);
        setTitle(Config.getLangBundle().getString("diagColorChooser"));
        
        this.color = color;
        colorChooser = new JColorChooser(color);
        
        //Create an array specifying the number of dialog buttons
        //and their text.
        Object[] options = {btnString1, btnString2};

        //Create the JOptionPane.
        optionPane = new JOptionPane(colorChooser,
                                    JOptionPane.PLAIN_MESSAGE,
                                    JOptionPane.OK_CANCEL_OPTION,
                                    null,
                                    options,
                                    options[0]);
        
        //Make this dialog display it.
        setContentPane(optionPane);
        
        pack();
        setLocationRelativeTo(parent);

        //Handle window closing correctly.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent we) {
        		clearAndHide();
            }
        });

        //Register an event handler that reacts to option pane state changes.
        optionPane.addPropertyChangeListener(this);
    }
    
    /** This method handles events for the text field. */
    public void actionPerformed(ActionEvent e) {
        optionPane.setValue(btnString1);
    }

    /** This method reacts to state changes in the option pane. */
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();

        if (isVisible() && (e.getSource() == optionPane)
         && (JOptionPane.VALUE_PROPERTY.equals(prop) || JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object value = optionPane.getValue();

            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                return;
            }

            optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

            if (btnString1.equals(value)) {
            	color = colorChooser.getColor();
            	clearAndHide();
            } else if (btnString2.equals(value)) {
            	clearAndHide();
            }
        }
    }

    /** This method clears the dialog and hides it. */
    public void clearAndHide() {
        setVisible(false);
    }
}
