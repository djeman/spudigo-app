package spudigo.ui.dialog;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import spudigo.Config;

public class DialogDuplicateSearch extends JDialog implements ActionListener, PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	private int distance = 0;
	private int angleInvDiff = -1;
	private boolean angleInvIgnored = false;
	
	private JSlider sliderDistance;
    private JTextField textFieldDistance;
    private JCheckBox checkBoxAngle;
    private JSlider sliderAngle;
    private JTextField textFieldAngle;
    private JLabel degLab;
    private JLabel degTextLab;

    private JOptionPane optionPane;
    
    private String btnString1 = Config.getLangBundle().getString("dupSearch");
    private String btnString2 = Config.getLangBundle().getString("cancel");

    public int getDistance() {
        return distance;
    }
    
    public int getAngleInvDiff() {
        return angleInvDiff;
    }
    
    public boolean isAngleInvIgnored() {
    	return angleInvIgnored;
    }

    /** Creates the reusable dialog. */
    public DialogDuplicateSearch(Window parent) {
    	super(parent, Dialog.ModalityType.APPLICATION_MODAL);
    	this.setMinimumSize(new Dimension(350,0));
        setTitle(Config.getLangBundle().getString("askDupDistanceTitle"));

        JPanel pan = new JPanel(new GridBagLayout());   
        GridBagConstraints c = new GridBagConstraints();
        
        JLabel lab = new JLabel(Config.getLangBundle().getString("askDupDistance"));
		c.weightx = 1;c.weighty = 0;
		c.gridx = 0;c.gridy = 0;
		c.gridwidth = 3;
		c.insets = new Insets(0, 0, 10, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		pan.add(lab, c);
		
		sliderDistance = new JSlider(JSlider.HORIZONTAL, 1, 200, Config.getInstance().getInteger("duplicate.distance"));
		sliderDistance.setMajorTickSpacing(20);
		sliderDistance.setMinorTickSpacing(2);
		sliderDistance.setPaintTicks(true);
		c.gridy = 1;c.gridx = 0;c.gridwidth = 1;c.weightx = 1;
		c.insets = new Insets(0, 0, 10, 2);
		pan.add(sliderDistance, c);
		
		textFieldDistance = new JTextField(Config.getInstance().getString("duplicate.distance"));
		textFieldDistance.setColumns(3);
		textFieldDistance.setHorizontalAlignment(JTextField.RIGHT);
		textFieldDistance.setEditable(false);
		textFieldDistance.setBackground(Color.WHITE);
		c.gridx = 1;c.gridwidth = 1;c.weightx = 0;
		c.insets = new Insets(0, 0, 10, 2);
		pan.add(textFieldDistance, c);
		
		sliderDistance.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				textFieldDistance.setText(String.valueOf(source.getValue()));
			}
		});
		
		lab = new JLabel("m");
		c.insets = new Insets(0, 0, 10, 0);
		c.gridx = 2;c.gridwidth = 1;c.weightx = 0;
		pan.add(lab, c);
		
		checkBoxAngle = new JCheckBox(Config.getLangBundle().getString("dupCheckIgnore"), false);
		checkBoxAngle.setSelected(Config.getInstance().getBoolean("duplicate.angle.ignored"));
		c.gridx = 0;c.gridy = 2;c.gridwidth = 3;c.weightx = 1;
		c.insets = new Insets(0, 0, 10, 0);
		pan.add(checkBoxAngle, c);
		
		degTextLab = new JLabel(Config.getLangBundle().getString("dupCheckAngle"));
		c.gridy = 3;c.gridx = 0;c.gridwidth = 3;c.weightx = 1;
		pan.add(degTextLab, c);
		
		sliderAngle = new JSlider(JSlider.HORIZONTAL, 0, 90, Config.getInstance().getInteger("duplicate.angle"));
		sliderAngle.setMajorTickSpacing(10);
		sliderAngle.setMinorTickSpacing(1);
		sliderAngle.setPaintTicks(true);
		c.gridy = 4;c.gridx = 0;c.gridwidth = 1;c.weightx = 1;
		c.insets = new Insets(0, 0, 10, 2);
		pan.add(sliderAngle, c);
		
		textFieldAngle = new JTextField(Config.getInstance().getString("duplicate.angle"));
		textFieldAngle.setColumns(3);
		textFieldAngle.setHorizontalAlignment(JTextField.RIGHT);
		textFieldAngle.setEditable(false);
		textFieldAngle.setBackground(Color.WHITE);
		c.gridx = 1;c.gridwidth = 1;c.weightx = 0;
		c.insets = new Insets(0, 0, 10, 2);
		pan.add(textFieldAngle, c);
		
		sliderAngle.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				textFieldAngle.setText(String.valueOf(source.getValue()));
			}
		});
		
		degLab = new JLabel("°");
		c.insets = new Insets(0, 0, 10, 0);
		c.gridx = 2;c.gridwidth = 1;c.weightx = 0;
		pan.add(degLab, c);
		
		if (!checkBoxAngle.isSelected()) {
			degTextLab.setVisible(false);
			sliderAngle.setVisible(false);
			textFieldAngle.setVisible(false);
			degLab.setVisible(false);
		}
		
		checkBoxAngle.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				degTextLab.setVisible(e.getStateChange()==1);
				sliderAngle.setVisible(e.getStateChange()==1);
				textFieldAngle.setVisible(e.getStateChange()==1);
				degLab.setVisible(e.getStateChange()==1);
				
				DialogDuplicateSearch.this.pack();
			}
		});
        
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
            	this.distance = sliderDistance.getValue();
            	this.angleInvIgnored = checkBoxAngle.isSelected();
            	this.angleInvDiff = sliderAngle.getValue();
            	
            	Config.getInstance().setValue("duplicate.distance", this.distance);
            	Config.getInstance().setValue("duplicate.angle.ignored", this.angleInvIgnored);
        		Config.getInstance().setValue("duplicate.angle", this.angleInvDiff);
        		
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
