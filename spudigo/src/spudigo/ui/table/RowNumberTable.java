package spudigo.ui.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class RowNumberTable extends JTable implements ChangeListener, PropertyChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JTable main;
	private int lastDigits;

	public RowNumberTable(JTable table) {
		main = table;
		main.addPropertyChangeListener(this);

		setFocusable(false);
		setAutoCreateColumnsFromModel(false);
		setSelectionModel(main.getSelectionModel());
		setRowHeight(main.getRowHeight());
		
		TableColumn column = new TableColumn();
		column.setHeaderValue(" ");
		addColumn(column);
		column.setCellRenderer(new RowNumberRenderer());

		setPreferredWidth();
		setPreferredScrollableViewportSize(getPreferredSize());
	}

	@Override
	public void addNotify() {
		super.addNotify();

		Component c = getParent();

		// Keep scrolling of the row table in sync with the main table.
		if (c instanceof JViewport) {
			JViewport viewport = (JViewport) c;
			viewport.addChangeListener(this);
		}
	}

	/*
	 * Delegate method to main table
	 */
	@Override
	public int getRowCount() {
		return main.getRowCount();
	}

	@Override
	public int getRowHeight(int row) {
		int rowHeight = main.getRowHeight(row);

		if (rowHeight != super.getRowHeight(row)) {
			super.setRowHeight(row, rowHeight);
		}

		return rowHeight;
	}

	/*
	 * No model is being used for this table so just use the row number as the
	 * value of the cell.
	 */
	@Override
	public Object getValueAt(int row, int column) {
		return Integer.toString(row + 1);
	}

	/*
	 * Don't edit data in the main TableModel by mistake
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	/*
	 * Do nothing since the table ignores the model
	 */
	@Override
	public void setValueAt(Object value, int row, int column) {
	}

	//
	// Implement the ChangeListener
	//
	public void stateChanged(ChangeEvent e) {
		// Keep the scrolling of the row table in sync with main table

		JViewport viewport = (JViewport) e.getSource();
		JScrollPane scrollPane = (JScrollPane) viewport.getParent();
		scrollPane.getVerticalScrollBar().setValue(viewport.getViewPosition().y);
	}

	//
	// Implement the PropertyChangeListener
	//
	public void propertyChange(PropertyChangeEvent e) {
		// Keep the row table in sync with the main table

		if ("selectionModel".equals(e.getPropertyName())) {
			setSelectionModel(main.getSelectionModel());
		}

		if ("rowHeight".equals(e.getPropertyName())) {
			repaint();
		}
	}
	
	private void setPreferredWidth() {
		int lines = main.getRowCount();
		int digits = Math.max(String.valueOf(lines).length(), 3);

		//  Update sizes when number of digits in the line number changes
		if (lastDigits != digits) {
			lastDigits = digits;
			FontMetrics fontMetrics = getFontMetrics(getFont());
			int width = fontMetrics.charWidth('0') * digits;
			int preferredWidth = 11 + width;

			getColumnModel().getColumn(0).setPreferredWidth(preferredWidth);
		}
	}

	/*
	 * Attempt to mimic the table header renderer
	 */
	private class RowNumberRenderer extends DefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public RowNumberRenderer() {
			setHorizontalAlignment(JLabel.RIGHT);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if (table != null) {
				JTableHeader header = table.getTableHeader();

				if (header != null) {
					if (isSelected)
						setForeground(Color.RED);
					else
						setForeground(header.getForeground());
					
					setBackground(header.getBackground());
				}
			}

			setText((value == null) ? "" : value.toString());
			setBorder(BorderFactory.createEmptyBorder(1, 5, 1, 5));
			setPreferredWidth();
			
			return this;
		}
	}
}
