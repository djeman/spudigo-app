package spudigo.ui.table.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import spudigo.Config;
import spudigo.ui.table.SpudTable;

public class ComboBoxRenderer extends JLabel implements TableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ComboBoxRenderer() {
		this.setFont(new Font("SansSerif", Font.PLAIN, 12));
		this.setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (value != null)
			this.setText(String.valueOf(value));
		else 
			this.setText("");
		
		if (isSelected)
			setBackground(new Color(184, 207, 229));
		else
			setBackground(null);
		
		this.setToolTipText(Config.getLangBundle().getString("tableTipColCombo"));
		this.setHorizontalAlignment(JLabel.CENTER);
		
		((SpudTable)table).setColorDuplicate(this, isSelected, row);
		
		return this;
	}
}

