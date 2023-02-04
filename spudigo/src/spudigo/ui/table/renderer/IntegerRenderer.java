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

public class IntegerRenderer implements TableCellRenderer {
	private final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JComponent component = (JComponent) renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		component.setBorder(BorderFactory.createEmptyBorder(1, 10, 1, 10));
		
		switch (column) {
			case 3:
				((JLabel)component).setToolTipText(Config.getLangBundle().getString("tableTipColSpeed"));
				((JLabel)component).setHorizontalAlignment(JLabel.RIGHT);
				((SpudTable)table).setColorDuplicate(component, isSelected, row);
				break;
			case 5:
				((JLabel)component).setToolTipText(Config.getLangBundle().getString("tableTipColAngle"));
				((JLabel)component).setHorizontalAlignment(JLabel.RIGHT);
				((SpudTable)table).setColorDuplicate(component, isSelected, row);
				break;
		}
		
		return component;
	}
}
