package spudigo.ui.dialog;

import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import spudigo.Config;

public class DialogExportChoice extends JDialog implements PropertyChangeListener {

	public static final int NORMAL = 0;
	public static final int BY_TYPE = 1;
	public static final int ALL_IN_ONE = 2;
	
	private static final long serialVersionUID = 1L;
	private int optionSelected = -1;

    private JOptionPane optionPane;
    
    private JRadioButton normalButton = null;
    private JRadioButton typeButton = new JRadioButton(Config.getLangBundle().getString("exportByType"));
    private JRadioButton allButton = null;

    private String btnString1 = Config.getLangBundle().getString("ok");
    private String btnString2 = Config.getLangBundle().getString("cancel");

    public int getOptionSelected() {
        return optionSelected;
    }

    /** Creates the reusable dialog. */
    public DialogExportChoice(Window parent, boolean toTxt, boolean all) {
    	super(parent, Dialog.ModalityType.APPLICATION_MODAL);
    	this.setResizable(false);
        setTitle(all?(toTxt?Config.getLangBundle().getString("mbExportAllTxt"):
        	Config.getLangBundle().getString("mbExportAllSpud")):
        		(toTxt?Config.getLangBundle().getString("exportTxt"):
                	Config.getLangBundle().getString("exportSpud")));
        
        Object[] array;
        if (all) {
        	normalButton = new JRadioButton(Config.getLangBundle().getString("exportNormalAll"));
        	allButton = new JRadioButton(Config.getLangBundle().getString("exportAllInOne"));
        } else {
        	normalButton = new JRadioButton(Config.getLangBundle().getString("exportNormal"));
        }
        
        ButtonGroup group = new ButtonGroup();
        group.add(normalButton);
        group.add(typeButton);
        
        normalButton.setSelected(true);
        
        if (all) {
        	group.add(allButton);
        	array = new Object[]{normalButton, typeButton, allButton};
        } else {
        	array = new Object[]{normalButton, typeButton};
        }
        
        //Create an array specifying the number of dialog buttons
        //and their text.
        Object[] options = {btnString1, btnString2};

        //Create the JOptionPane.
        optionPane = new JOptionPane(array,
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
            	if (normalButton.isSelected())
            		optionSelected = NORMAL;
            	else if (typeButton.isSelected())
            		optionSelected = BY_TYPE;
            	else if (allButton != null && allButton.isSelected())
            		optionSelected = ALL_IN_ONE;
            	
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
