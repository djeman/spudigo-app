package spudigo.ui.table;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;

public class SpudScroll extends JScrollPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SpudScroll(final Component view) {
        super(view);
        
        JTable rowTable = new RowNumberTable((JTable) view);
		setRowHeaderView(rowTable);
		setCorner(JScrollPane.UPPER_LEFT_CORNER, rowTable.getTableHeader());
		setColumnHeader(new JViewport() {
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredSize() {
				Dimension d = super.getPreferredSize();
				d.height = 25;
				return d;
			}
		});
    }
	
	public SpudTable getTable() {
		return (SpudTable)this.getViewport().getComponent(0);
	}
}
