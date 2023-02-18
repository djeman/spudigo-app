package spudigo.ui.table;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.RowFilter;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.TableRowSorter;

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
		
		rowTable.getTableHeader().addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					JLabel label = (JLabel) rowTable.getColumnModel().getColumn(0).getHeaderValue();
					if (label.getIcon() != null) {
						getTable().getRowSorter().setSortKeys(null);
						if (getTable().getRowSorter() instanceof TableRowSorter<?>) {
							((TableRowSorter<?>) getTable().getRowSorter()).setRowFilter(null);
						}
					}
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) { }

			@Override
			public void mouseReleased(MouseEvent e) { }

			@Override
			public void mouseEntered(MouseEvent e) { }

			@Override
			public void mouseExited(MouseEvent e) { }						
		});
		
		ImageIcon imgFilter = new ImageIcon(getClass().getResource("/spudigo/images/icon16_filter.png"));
		
		this.getTable().getRowSorter().addRowSorterListener(new RowSorterListener() {
			@Override
			public void sorterChanged(RowSorterEvent e) {
				List<?> sortKeys = null;
				RowFilter<?, ?> rowFilter = null;

				sortKeys = getTable().getRowSorter().getSortKeys();
				if (getTable().getRowSorter() instanceof TableRowSorter<?>) {
					rowFilter = ((TableRowSorter<?>) getTable().getRowSorter()).getRowFilter();
				}

				JLabel label = (JLabel) rowTable.getColumnModel().getColumn(0).getHeaderValue();
				if ((sortKeys == null || sortKeys.size() < 1) && rowFilter == null) {
					label.setIcon(null);
				} else {
					label.setIcon(imgFilter);
				}

				rowTable.getTableHeader().repaint();
			}
		});
    }
	
	public SpudTable getTable() {
		return (SpudTable)this.getViewport().getComponent(0);
	}
}
