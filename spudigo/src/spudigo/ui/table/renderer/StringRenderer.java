package spudigo.ui.table.renderer;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import spudigo.ui.table.SpudTable;

public class StringRenderer implements TableCellRenderer {
	private final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JComponent component = (JComponent) renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		component.setBorder(BorderFactory.createEmptyBorder(1, 10, 1, 10));
		
		((SpudTable)table).setColorDuplicate(component, isSelected, row);
		
		return component;
	}
}
