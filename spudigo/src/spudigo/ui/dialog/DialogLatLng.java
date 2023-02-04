package spudigo.ui.dialog;

import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import spudigo.Config;

public class DialogLatLng extends JDialog implements ActionListener, PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	private double typedLat = 250.0;
	private double typedLng = 250.0;
	
	private JLabel label = new JLabel(Config.getLangBundle().getString("diagLatLngLabel"));
    private JTextField textFieldLat;
    private JTextField textFieldLng;

    private JOptionPane optionPane;
    private JPopupMenu popup = new JPopupMenu();

    private String btnString1 = Config.getLangBundle().getString("go");
    private String btnString2 = Config.getLangBundle().getString("cancel");

    public Double getLatitude() {
        return typedLat;
    }
    
    public Double getLongitude() {
        return typedLng;
    }

    /** Creates the reusable dialog. */
    public DialogLatLng(Window parent, double defLat, double defLng) {
    	super(parent, Dialog.ModalityType.APPLICATION_MODAL);
    	this.setResizable(false);
        setTitle(Config.getLangBundle().getString("mapGoToPosition"));
        
        JMenuItem menuItemArea = new JMenuItem(Config.getLangBundle().getString("tbCut"));
        menuItemArea.addActionListener(CutActionArea);
        popup.add(menuItemArea);
        menuItemArea = new JMenuItem(Config.getLangBundle().getString("tbCopy"));
        menuItemArea.addActionListener(CopyActionArea);
        popup.add(menuItemArea);
        menuItemArea = new JMenuItem(Config.getLangBundle().getString("tbPaste"));
        menuItemArea.addActionListener(PasteActionArea);
        popup.add(menuItemArea);

        textFieldLat = new JTextField(String.format("%." + Config.getInstance().getInteger("position.number.decimal") + "f", defLat));
        textFieldLat.setHorizontalAlignment(JTextField.RIGHT);
        textFieldLat.addMouseListener(PopupMouseListener);
        
        textFieldLng = new JTextField(String.format("%." + Config.getInstance().getInteger("position.number.decimal") + "f", defLng));
        textFieldLng.setHorizontalAlignment(JTextField.RIGHT);
        textFieldLng.addMouseListener(PopupMouseListener);

        JLabel labLat = new JLabel(Config.getLangBundle().getString("diagLatitude"));
        JLabel labLng = new JLabel(Config.getLangBundle().getString("diagLongitude"));
        JLabel labDeg1 = new JLabel("°");
        JLabel labDeg2 = new JLabel("°");
        
        JPanel pan = new JPanel(new GridBagLayout());   
        GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;c.weighty = 0;
		c.gridx = 0;c.gridy = 0;
		c.gridwidth = 4;
		c.insets = new Insets(0, 0, 10, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		pan.add(label, c);
		
		c.gridy = 1;c.gridwidth = 1;c.weightx = 0;
		c.insets = new Insets(0, 0, 2, 30);
		pan.add(labLng, c);
		
		c.gridx = 1;c.gridwidth = 2;c.weightx = 1;
		c.insets = new Insets(0, 0, 2, 0);
		pan.add(textFieldLng, c);
		
		c.gridx = 3;c.gridwidth = 1;c.weightx = 0;
		pan.add(labDeg1, c);
		
		c.gridy = 2;
		c.gridx = 0;c.gridwidth = 1;c.weightx = 0;
		c.insets = new Insets(0, 0, 2, 30);
		pan.add(labLat, c);
		
		c.gridx = 1;c.gridwidth = 2;c.weightx = 1;
		c.insets = new Insets(0, 0, 2, 0);
		pan.add(textFieldLat, c);
		
		c.gridx = 3;c.gridwidth = 1;c.weightx = 0;
		pan.add(labDeg2, c);
        
        //Create an array specifying the number of dialog buttons
        //and their text.
        Object[] options = {btnString1, btnString2};

        //Create the JOptionPane.
        optionPane = new JOptionPane(pan,
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
            	textFieldLat.requestFocusInWindow();
            }
        });

        //Register an event handler that puts the text into the option pane.
        textFieldLat.addActionListener(this);
        textFieldLng.addActionListener(this);

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
            	boolean checkIsDouble = false;
            	try {
            		typedLat = Double.parseDouble(textFieldLat.getText().replace(",", "."));
            		typedLng = Double.parseDouble(textFieldLng.getText().replace(",", "."));
            		checkIsDouble = true;
            	} catch (Exception ex) {}
            	
            	if (checkIsDouble)
            		clearAndHide();
            	else
            		label.setText(Config.getLangBundle().getString("diagLatLngLabelErr"));
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
