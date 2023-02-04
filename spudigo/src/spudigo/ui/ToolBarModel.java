package spudigo.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import spudigo.Config;
import spudigo.TemplateFile;

public class ToolBarModel extends JToolBar {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final JComboBox<String> comboIn = new JComboBox<String>();
	private final JComboBox<String> comboOut = new JComboBox<String>();
	private final ToolBarModelListener toolBarListener;
	
	private String strNone;
	
	private enum ActionModel {
		ModelIn,
		ModelOut
	}
	
	public ToolBarModel(ToolBarModelListener toolBarListener) {
		super();
		this.toolBarListener = toolBarListener;
		init();
	}
	
	private void init() {
		this.strNone = Config.getLangBundle().getString("none");
		
		JLabel label;
		Font font;
		// Label -> Model Open
		label = new JLabel(Config.getLangBundle().getString("tbModelIn"));
		font = new Font(label.getFont().getFontName(), Font.BOLD, label.getFont().getSize());
		label.setFont(font);
		this.add(label);
		
		this.addSeparator();
		
		// ComboBox -> Model Open
		comboIn.setEnabled(false);
		comboIn.setFocusable(false);
		comboIn.setMaximumSize(new Dimension(250,24));
		comboIn.setPreferredSize(new Dimension(250,24));
		comboIn.addPopupMenuListener(ComboPopupListener);
		comboIn.setActionCommand(ActionModel.ModelIn.toString());
		comboIn.addActionListener(ComboAction);
		comboIn.setFont(font);
		((JLabel)comboIn.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		this.add(comboIn);
		
		this.addSeparator();
		this.add(Box.createHorizontalGlue());
		this.addSeparator();
		
		// Label -> Model Save
		label = new JLabel(Config.getLangBundle().getString("tbModelOut"));
		label.setFont(font);
		this.add(label);
		
		this.addSeparator();
				
		// ComboBox -> Model Save
		comboOut.setEnabled(false);
		comboOut.setFocusable(false);
		comboOut.setMaximumSize(new Dimension(250,24));
		comboOut.setPreferredSize(new Dimension(250,24));
		comboOut.addPopupMenuListener(ComboPopupListener);
		comboOut.setActionCommand(ActionModel.ModelOut.toString());
		comboOut.addActionListener(ComboAction);
		comboOut.setFont(font);
		((JLabel)comboOut.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		this.add(comboOut);
		
		this.addSeparator();
	}

	public void updateCombo(Hashtable<String, TemplateFile> templates) {
		String currentValIn = (String)comboIn.getSelectedItem();
		String currentValOut = (String)comboOut.getSelectedItem();
		
		comboIn.removeActionListener(ComboAction);
		comboOut.removeActionListener(ComboAction);
		
		String[] valComboIn = new String[templates.size()+1];
		String[] valComboOut = new String[templates.size()];
		
		Enumeration<String> e = templates.keys();
		int i = 0;
	    while (e.hasMoreElements()) {
	    	String key = e.nextElement();
	    	valComboIn[i] = key;
	    	valComboOut[i] = key;
	    	i++;
	    }
	    
	    valComboIn[i] = strNone;
	    
	    comboIn.setModel(new DefaultComboBoxModel<String>(valComboIn));
	    comboOut.setModel(new DefaultComboBoxModel<String>(valComboOut));
	    
	    comboIn.addActionListener(ComboAction);
	    comboOut.removeActionListener(ComboAction);
	    
	    if (!comboIn.isEnabled() && comboIn.getItemCount() > 0) {
	    	comboIn.setEnabled(true);
	    } else if (comboIn.isEnabled() && comboIn.getItemCount() < 1)
	    	comboIn.setEnabled(false);
	    
	    if (currentValIn != null && !currentValIn.isEmpty())
	    	comboIn.setSelectedItem(currentValIn);
	    
	    if (!comboOut.isEnabled() && comboOut.getItemCount() > 0) {
	    	comboOut.setEnabled(true);
	    } else if (comboOut.isEnabled() && comboOut.getItemCount() < 1)
	    	comboOut.setEnabled(false);
	    
	    if (currentValOut != null && !currentValOut.isEmpty())
	    	comboOut.setSelectedItem(currentValOut);
	}
	
	public String getComboInValue() {
		return (String) this.comboIn.getSelectedItem();
	}
	
	public String getComboOutValue() {
		return (String) this.comboOut.getSelectedItem();
	}
	
	public void setComboInValue(String name) {
		this.comboIn.setSelectedItem(name);
	}
	
	public void setComboOutValue(String name) {
		this.comboOut.setSelectedItem(name);
	}
	
	public boolean isNothing() {
		return ((String) this.comboIn.getSelectedItem()).equals(strNone);
	}
	
	PopupMenuListener ComboPopupListener = new PopupMenuListener() {
		@Override
		public void popupMenuCanceled(PopupMenuEvent e) {
			comboIn.setFocusable(false);
		}

		@Override
		public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			comboIn.setFocusable(false);
		}

		@Override
		public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			comboIn.setFocusable(true);
		}
	};
	
	ActionListener ComboAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (toolBarListener == null)
				return;
			
			switch (ActionModel.valueOf(ae.getActionCommand())) {
				case ModelIn:
					toolBarListener.onChooseModelIn((String) comboIn.getSelectedItem());
					return;
				case ModelOut:
					toolBarListener.onChooseModelOut((String) comboOut.getSelectedItem());
					return;
				default:
					return;
			}
		}
	};
	
	public interface ToolBarModelListener {
		public void onChooseModelIn(String id);
		public void onChooseModelOut(String id);
	}
}
