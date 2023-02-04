package spudigo.ui.dialog;

import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import spudigo.Config;

public class DialogAddress extends JDialog implements ActionListener, PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	private String typedAddress = "";
	
	private JLabel label = new JLabel(Config.getLangBundle().getString("diagIputTextLabel"));
    private JTextField textFieldAdress = new JTextField();

    private JOptionPane optionPane;
    private JPopupMenu popup = new JPopupMenu();

    private String btnString1 = Config.getLangBundle().getString("go");
    private String btnString2 = Config.getLangBundle().getString("cancel");

    public String getTypedAddress() {
        return typedAddress;
    }

    /** Creates the reusable dialog. */
    public DialogAddress(Window parent) {
    	super(parent, Dialog.ModalityType.APPLICATION_MODAL);
    	this.setResizable(false);
        setTitle(Config.getLangBundle().getString("mapGoToAddress"));
        
        JMenuItem menuItemArea = new JMenuItem(Config.getLangBundle().getString("tbCut"));
        menuItemArea.addActionListener(CutActionArea);
        popup.add(menuItemArea);
        menuItemArea = new JMenuItem(Config.getLangBundle().getString("tbCopy"));
        menuItemArea.addActionListener(CopyActionArea);
        popup.add(menuItemArea);
        menuItemArea = new JMenuItem(Config.getLangBundle().getString("tbPaste"));
        menuItemArea.addActionListener(PasteActionArea);
        popup.add(menuItemArea);
        
        textFieldAdress.addMouseListener(PopupMouseListener);
        
        //Create an array of the text and components to be displayed.
        Object[] array = {label, textFieldAdress};
        
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

        //Ensure the text field always gets the first focus.
        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
            	textFieldAdress.requestFocusInWindow();
            }
        });

        //Register an event handler that puts the text into the option pane.
        textFieldAdress.addActionListener(this);

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
            	typedAddress = textFieldAdress.getText();
            	
            	if (typedAddress.length() > 0)
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
    
    ActionListener CopyActionArea = new ActionListener() {
    	public void actionPerformed (java.awt.event.ActionEvent e) {
    		JTextField textField = (JTextField)popup.getInvoker();
    		textField.copy();
        }
    };
    
    ActionListener PasteActionArea = new ActionListener() {
    	public void actionPerformed (java.awt.event.ActionEvent e) {
    		JTextField textField = (JTextField)popup.getInvoker();
    		textField.paste();
        }
    };
    
    ActionListener CutActionArea = new ActionListener() {
    	public void actionPerformed (java.awt.event.ActionEvent e) {
    		JTextField textField = (JTextField)popup.getInvoker();
    		textField.cut();
        }
    };
    
    MouseListener PopupMouseListener = new MouseListener(){
		@Override
		public void mouseClicked(MouseEvent arg0) {}

		@Override
		public void mouseEntered(MouseEvent arg0) {}

		@Override
		public void mouseExited(MouseEvent arg0) {}

		@Override
		public void mousePressed(MouseEvent arg0) {}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			if (arg0.getButton() == MouseEvent.BUTTON3)
				popup.show(arg0.getComponent(), arg0.getX(), arg0.getY());
		}
	};
}
