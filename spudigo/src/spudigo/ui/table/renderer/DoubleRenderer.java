package spudigo.ui.table.renderer;

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import spudigo.Config;
import spudigo.ui.table.SpudTable;

public class DoubleRenderer implements TableCellRenderer {
	private final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JComponent component = (JComponent) renderer.getTableCellRendererComponent(table, 
				String.format("%." + Config.getInstance().getInteger("position.number.decimal") + "f", value), 
				isSelected, hasFocus, row, column);
		component.setBorder(BorderFactory.createEmptyBorder(1, 10, 1, 10));
		
		switch (column) {
			case 0:
				((JLabel)component).setToolTipText(Config.getLangBundle().getString("tableTipColLon"));
				((JLabel)component).setHorizontalAlignment(JLabel.RIGHT);
				break;
			case 1:
				((JLabel)component).setToolTipText(Config.getLangBundle().getString("tableTipColLat"));
				((JLabel)component).setHorizontalAlignment(JLabel.RIGHT);
				break;
		}
		
		((SpudTable)table).setColorDuplicate(component, isSelected, row);
		
		return component;
	}
}
