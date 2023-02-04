package spudigo.ui.dialog;

import java.awt.Dimension;
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
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import spudigo.Config;

public class DialogResult extends JDialog implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	private static final String btnString = "OK";
	
	private final JTextPane tp = new JTextPane();;
	private final JPopupMenu popup = new JPopupMenu();
    private final JOptionPane optionPane;
    
    public DialogResult(JFrame parent, String text) {
    	super(parent, true);
    	
        setTitle(Config.getLangBundle().getString("result"));
        
        JMenuItem menuItemArea = new JMenuItem(Config.getLangBundle().getString("tbCopy"));
        menuItemArea.addActionListener(CopyActionArea);
        popup.add(menuItemArea);

        tp.setContentType("text/html");
        tp.setText("<html>" + text.replace("\n","<br>") + "</html>");
        tp.setEditable(false);
        tp.setPreferredSize(new Dimension(350, 250));
        tp.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3)
					popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
        
        JScrollPane jsp = new JScrollPane(tp);
        
        //Create an array of the text and components to be displayed.
        Object[] array = {Config.getLangBundle().getString("exportResult"), jsp};
        
        //Create an array specifying the number of dialog buttons
        //and their text.
        Object[] options = {btnString};

        //Create the JOptionPane.
        optionPane = new JOptionPane(array,
                                    JOptionPane.PLAIN_MESSAGE,
                                    JOptionPane.OK_OPTION,
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
        		DialogResult.this.dispose();
            }
        });

        //Ensure the text field always gets the first focus.
        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
                tp.requestFocusInWindow();
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

            if (btnString.equals(value)) {
            	this.dispose();
            }
        }
    }
    
    ActionListener CopyActionArea = new ActionListener() {
    	public void actionPerformed (java.awt.event.ActionEvent e) {
    		JTextPane textPane = (JTextPane)popup.getInvoker();
    		textPane.copy();
        }
    };
}
