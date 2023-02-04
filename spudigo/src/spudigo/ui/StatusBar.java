package spudigo.ui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import spudigo.Config;

public class StatusBar extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JLabel labelDistance = new JLabel();
	private final JLabel labelStatus = new JLabel(Config.getLangBundle().getString("sbWelcome"));

	public StatusBar() {
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(3, 5, 3, 5));
        
		labelDistance.setHorizontalAlignment(JLabel.LEFT);
		labelDistance.setFont(new Font(labelDistance.getFont().getFontName(), 
				Font.BOLD, labelDistance.getFont().getSize()));
		
		labelStatus.setHorizontalAlignment(JLabel.RIGHT);
		labelStatus.setFont(new Font(labelStatus.getFont().getFontName(), 
				Font.BOLD, labelStatus.getFont().getSize()));
        
		this.add(labelDistance, BorderLayout.WEST);
		this.add(labelStatus, BorderLayout.EAST);
	}
	
	public void setDistance(String text) {
		if (text.isEmpty())
			labelDistance.setText("");
		else
			labelDistance.setText("<html>" + Config.getLangBundle().getString("sbDistance") + 
					" <font color='black'>" + text + " m</font></html>");
	}
	
	public void addRoadDistance(String go, String back) {
		labelDistance.setText(labelDistance.getText().substring(0, labelDistance.getText().length() - 7)+ "   \u2632 "
				+ Config.getLangBundle().getString("sbOSMRoad") + " <font color='blue'>" + go + 
				" m</font> \u21c4 <font color='green'>" + back + " m</font></html>");
	}
	
	public void setStatus(String text) {
		labelStatus.setText(text);
	}
}
