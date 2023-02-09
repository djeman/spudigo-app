package spudigo.ui.table;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ProgressMonitor;
import javax.swing.SortOrder;
import javax.swing.RowSorter.SortKey;
import javax.swing.SwingWorker;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import javax.swing.undo.UndoManager;

import spudigo.Config;
import spudigo.EnumDirType;
import spudigo.EnumStatus;
import spudigo.HaversineAlgorithm;
import spudigo.SpudItem;
import spudigo.TemplateFile;
import spudigo.ui.table.SpudTableModel.ValueChangedListener;
import spudigo.ui.table.editor.DoubleEditor;
import spudigo.ui.table.renderer.ComboBoxRenderer;
import spudigo.ui.table.renderer.DoubleRenderer;
import spudigo.ui.table.renderer.IntegerRenderer;
import spudigo.ui.table.renderer.StringRenderer;

public class SpudTable extends JTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final JComboBox<String> comboBoxType = new JComboBox<String>();
	private final UndoManager undoManager = new UndoManager();
	private final SpudTableListener spudTableListener;
	
	private final ArrayList<Integer> dupConfirmed = new ArrayList<Integer>();
	private final ArrayList<Integer> dupNotSure = new ArrayList<Integer>();
	private final ArrayList<Integer> notDup = new ArrayList<Integer>();
	
	private String nameModelIn;
	private String nameModelOut;
	private String filePath;
	
	private int dupIndex = -1;
	private int dupAngle = -1;
	private double dupDistance = 10;
	
	public SpudTable(String filePath, String nameModelIn, String nameModelOut, 
			SpudTableListener spudTableListener, SpudTableModel spudTableModel) {
		super(spudTableModel);
		
		((SpudTableModel) this.getModel()).addUndoableEditListener(undoManager);
		
		this.spudTableListener = spudTableListener;
		this.filePath = filePath;
		this.nameModelIn = nameModelIn;
		this.nameModelOut = nameModelOut;
		
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.setFillsViewportHeight(true);
		this.setRowHeight(20);
		this.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
		
		this.setAutoCreateRowSorter(true);
		
		this.setDefaultRenderer(Double.class, new DoubleRenderer());
		this.setDefaultRenderer(Integer.class, new IntegerRenderer());
		this.setDefaultRenderer(String.class, new StringRenderer());
		
		JTextField fmtTxtField = new JTextField();
		fmtTxtField.setHorizontalAlignment(JTextField.RIGHT);
		this.setDefaultEditor(Double.class, new DoubleEditor(fmtTxtField));
		
		TableColumnModel tcm = this.getColumnModel();
		setUpComboDirectionColumn(tcm.getColumn(SpudTableModel.COL_DIRECTION));
		setUpComboTypeColumn(tcm.getColumn(SpudTableModel.COL_TYPE));
		setUpComboStatusColumn(tcm.getColumn(SpudTableModel.COL_STATUS));
		loadColConfig();
	}
	
	public void loadColConfig() {
		TableColumnModel tcm = this.getColumnModel();
		String[] oldIndex = Config.getInstance().getString("table.col.pos").split(",");
		String[] colWidth = Config.getInstance().getString("table.col.width").split(",");
		
		for (int i=0;i<tcm.getColumnCount();i++) {
			tcm.moveColumn(convertColumnIndexToView(Integer.valueOf(oldIndex[i])), i);
			tcm.getColumn(i).setPreferredWidth(Integer.valueOf(colWidth[i]));
		}
	}
	
	public void saveColConfig() {
		TableColumnModel tcm = this.getColumnModel();
		StringBuilder sb = new StringBuilder();
		
		for (int i=0;i<tcm.getColumnCount();i++) {
			sb.append(tcm.getColumn(i).getPreferredWidth());
			
			if (tcm.getColumnCount()-1 != i)
				sb.append(",");
		}
		
		Config.getInstance().setValue("table.col.width", sb.toString());
		
		sb = new StringBuilder();
		Enumeration<TableColumn> e = tcm.getColumns();
		while(e.hasMoreElements()) {
			sb.append(e.nextElement().getModelIndex());
			
			if (e.hasMoreElements())
				sb.append(",");
		}
		
		Config.getInstance().setValue("table.col.pos", sb.toString());
	}

	private void setUpComboDirectionColumn(TableColumn column) {
		JComboBox<String> comboBox = new JComboBox<String>();
		((JLabel)comboBox.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
        comboBox.addItem(EnumDirType.UNI.getLabel());
        comboBox.addItem(EnumDirType.BI.getLabel());
        comboBox.addItem(EnumDirType.ALL.getLabel());
        
        DefaultCellEditor defCellEditor = new DefaultCellEditor(comboBox);
		defCellEditor.setClickCountToStart(2);
		
        column.setCellEditor(defCellEditor);
        column.setCellRenderer(new ComboBoxRenderer());
	}
	
	private void setUpComboTypeColumn(TableColumn column) {
		((JLabel)comboBoxType.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		
		TemplateFile modelIn = ((SpudTableModel) this.getModel()).getModelIn();
		
		if (modelIn == null) {
			comboBoxType.setModel(new DefaultComboBoxModel<String>());
			comboBoxType.setEnabled(false);
		} else {
			comboBoxType.setModel(new DefaultComboBoxModel<String>(modelIn.getComboTypeList()));
			comboBoxType.setEnabled(true);
		}
		
		DefaultCellEditor defCellEditor = new DefaultCellEditor(comboBoxType);
		defCellEditor.setClickCountToStart(2);
		
		column.setCellEditor(defCellEditor);
		column.setCellRenderer(new ComboBoxRenderer());
	}
	
	private void setUpComboStatusColumn(TableColumn column) {
		JComboBox<String> comboBox = new JComboBox<String>();
		((JLabel)comboBox.getRenderer()).setHorizontalAlignment(JLabel.CENTER); 
        comboBox.addItem(EnumStatus.NEW.getLabel());
        comboBox.addItem(EnumStatus.DELETED.getLabel());
        comboBox.addItem(EnumStatus.EDITED.getLabel());
        
        DefaultCellEditor defCellEditor = new DefaultCellEditor(comboBox);
		defCellEditor.setClickCountToStart(2);
		
        column.setCellEditor(defCellEditor);
        column.setCellRenderer(new ComboBoxRenderer());
	}
	
	/*@Override
	public boolean getScrollableTracksViewportWidth() {
        return getPreferredSize().width < getParent().getWidth();
    }*/
	
	public boolean isTxt() {
		return ((SpudTableModel) this.getModel()).isTxt();
	}
	
	public String getFilePath() {
		return this.filePath;
	}
	
	public String getFileName() {
		if (this.filePath != null && this.filePath.length() > 0)
			return this.filePath.substring(this.filePath.lastIndexOf(File.separator)+1, this.filePath.length());
		
		return null;
	}
	
	public String getNameModelIn() {
		return this.nameModelIn;
	}
	
	public String getNameModelOut() {
		return this.nameModelOut;
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public void setNameModelOut(String nameModelOut) {
		this.nameModelOut = nameModelOut;
	}

	public void setModelIn(String nameModelIn, TemplateFile modelIn) {
		this.nameModelIn = nameModelIn;
		((SpudTableModel) this.getModel()).setModelIn(modelIn);
		setUpComboTypeColumn(getColumnModel().getColumn(convertColumnIndexToView(SpudTableModel.COL_TYPE)));
		((SpudTableModel) this.getModel()).fireTableDataChanged();
	}
	
	public ArrayList<SpudItem> getData() {
		return ((SpudTableModel) this.getModel()).getData();
	}
	
	public void undo() {
		if (undoManager.canUndo())
			undoManager.undo();
	}
	
	public void redo() {
		if (undoManager.canRedo())
			undoManager.redo();
	}
	
	public void addRow(SpudItem copyRow) {
		((SpudTableModel) this.getModel()).addRow(copyRow);
	}
	
	public void addRow(double lat, double lon) {
		((SpudTableModel) this.getModel()).addRow(lat, lon);
	}
	
	public void insertRow(int index, SpudItem copyRow) {
		((SpudTableModel) this.getModel()).insertRow(index, copyRow);
	}
	
	public void insertRow(int index, double lat, double lon) {
		((SpudTableModel) this.getModel()).insertRow(index, lat, lon);
	}

	public void removeRows(int[] selectedRows) {
		((SpudTableModel) this.getModel()).removeRows(selectedRows);
		this.getRowSorter().allRowsChanged();
		
		if (this.getRowCount() == 0)
			loadColConfig();
	}
	
	public JPopupMenu genRowPopupMenu(int[] rowsIndex, ArrayList<SpudItem> copyRow, boolean mapEnabled) {
		JPopupMenu popupArea = new JPopupMenu();
		JMenuItem menuItemArea;

		if (rowsIndex.length > 0) {
			if (mapEnabled) {
		        menuItemArea = new JMenuItem(Config.getLangBundle().getString("centerZoomOnMap"));
				//menuItemArea.setIcon(new ImageIcon(getClass().getResource("/spudigo/images/icon16_copy.png")));
		        menuItemArea.addActionListener(MapCenterAndZoomAction);
		        popupArea.add(menuItemArea);

		        if (rowsIndex.length == 1) {
			        menuItemArea = new JMenuItem(Config.getLangBundle().getString("centerOnMap"));
					//menuItemArea.setIcon(new ImageIcon(getClass().getResource("/spudigo/images/icon16_copy.png")));
			        menuItemArea.addActionListener(MapCenterAction);
			        popupArea.add(menuItemArea);
		        }
		        
		        popupArea.addSeparator();
			}
			
	        menuItemArea = new JMenuItem(Config.getLangBundle().getString("tbCopy"));
			menuItemArea.setIcon(new ImageIcon(getClass().getResource("/spudigo/images/icon16_copy.png")));
	        menuItemArea.addActionListener(CopyAction);
	        popupArea.add(menuItemArea);
	        menuItemArea = new JMenuItem(Config.getLangBundle().getString("tbCut"));
			menuItemArea.setIcon(new ImageIcon(getClass().getResource("/spudigo/images/icon16_cut.png")));
	        menuItemArea.addActionListener(CutAction);
	        popupArea.add(menuItemArea);
		}
		
		if (copyRow.size() > 0) {
	        menuItemArea = new JMenuItem(Config.getLangBundle().getString("tbPaste"));
	        menuItemArea.setIcon(new ImageIcon(getClass().getResource("/spudigo/images/icon16_paste.png")));
	        menuItemArea.addActionListener(PasteAction);
	        popupArea.add(menuItemArea);
	        
	        if (rowsIndex.length > 0) {
		        menuItemArea = new JMenuItem(Config.getLangBundle().getString("tbInsert"));
		        menuItemArea.addActionListener(InsertAction);
		        popupArea.add(menuItemArea);
	        }
        }
        
		if (rowsIndex.length > 0 || copyRow.size() > 0)
			popupArea.addSeparator();
		
        menuItemArea = new JMenuItem(Config.getLangBundle().getString("tbAddLine"));
		menuItemArea.setIcon(new ImageIcon(getClass().getResource("/spudigo/images/icon16_add.png")));
        menuItemArea.addActionListener(AddLineAction);
        popupArea.add(menuItemArea);

        if (rowsIndex.length > 0) {
	        menuItemArea = new JMenuItem(Config.getLangBundle().getString("tbInsertLine"));
	        menuItemArea.addActionListener(InsertLineAction);
	        popupArea.add(menuItemArea);
        
	        menuItemArea = new JMenuItem(Config.getLangBundle().getString("tbDelete"));
	        menuItemArea.setIcon(new ImageIcon(getClass().getResource("/spudigo/images/icon16_del.png")));
	        menuItemArea.addActionListener(DelAction);
	        popupArea.add(menuItemArea);
        }
        
        return popupArea;
	}
	
	public void setColorDuplicate(JComponent component, boolean isSelected, int row) {
		int modelRow = SpudTable.this.getRowSorter().convertRowIndexToModel(row);
		if (dupIndex > -1 && (dupConfirmed.size() > 0 || dupNotSure.size() > 0)) {
			if (dupConfirmed.contains(modelRow)) {
				if (isSelected)
					component.setBackground(Color.YELLOW);
				else
					component.setBackground(Color.RED);
			} else if (dupNotSure.contains(modelRow)) {
				if (isSelected)
					component.setBackground(Color.YELLOW);
				else
					component.setBackground(Color.ORANGE);
			} else if (dupIndex == modelRow) {
				if (isSelected)
					component.setBackground(Color.YELLOW);
				else
					component.setBackground(Color.GREEN);
			} else {
				if (isSelected)
					component.setBackground(new Color(184, 207, 229));
				else
					component.setBackground(null);
			}
		} else {
			if (isSelected)
				component.setBackground(new Color(184, 207, 229));
			else
				component.setBackground(null);
		}
	}
	
	public void searchDuplicate(final double distance, final int angle) {
		TableRowSorter<?> rowSorter = (TableRowSorter<?>) this.getRowSorter();
		List<SortKey> keys = new ArrayList<SortKey>();
		keys.add(new SortKey(SpudTableModel.COL_LONGITUDE, SortOrder.ASCENDING));
		rowSorter.setSortKeys(keys);
		rowSorter.sort();
		
		dupIndex = -1;
		dupDistance = distance;
		dupAngle = angle;
		notDup.clear();
		
		nextDuplicate();
	}
	
	public void nextDuplicate() {
		dupIndex++;
		
		new SearchDupWorker().execute();
	}
	
	public void clearDuplicate() {
		notDup.clear();
		dupIndex = -1;
		((SpudTableModel) this.getModel()).fireTableDataChanged();
	}
	
	public void addValueChangedListener(ValueChangedListener listener) {
		((SpudTableModel) this.getModel()).addValueChangedListener(listener);
	}
	
	public void removeValueChangedListener(ValueChangedListener listener) {
		((SpudTableModel) this.getModel()).removeValueChangedListener(listener);
	}
	
	ActionListener AddLineAction = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent ae) {
			if (spudTableListener != null)
				spudTableListener.onAddLine();
		}
	};
	
	ActionListener InsertLineAction = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent ae) {
			if (spudTableListener != null)
				spudTableListener.onInsertLine();
		}
	};
	
	ActionListener DelAction = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (spudTableListener != null)
				spudTableListener.onDelete();
		}
	};
	
	ActionListener CopyAction = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent ae) {
			if (spudTableListener != null)
				spudTableListener.onCopy();
		}
	};
	
	ActionListener CutAction = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent ae) {
			if (spudTableListener != null)
				spudTableListener.onCut();
		}
	};
	
	ActionListener PasteAction = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (spudTableListener != null)
				spudTableListener.onPaste();
		}
	};
	
	ActionListener InsertAction = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (spudTableListener != null)
				spudTableListener.onInsert();
		}
	};
	
	ActionListener MapCenterAction = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (spudTableListener != null) {
				spudTableListener.onMapCenter();
			}
		}
	};
	
	ActionListener MapCenterAndZoomAction = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (spudTableListener != null) {
				spudTableListener.onMapCenterAndZoom();
			}
		}
	};
	
	public interface SpudTableListener {
		public void onCopy();
		public void onCut();
		public void onPaste();
		public void onInsert();
		public void onAddLine();
		public void onInsertLine();
		public void onDelete();
		public void onMapCenter();
		public void onMapCenterAndZoom();
		public void onMapCenterAndZoom(int[] rowsIndex);
	}
	
	private class SearchDupWorker extends SwingWorker<Void, Void> {
        private final ProgressMonitor monitor;

        public SearchDupWorker() {
        	this.monitor = new ProgressMonitor(SpudTable.this.getParent(), 
        			Config.getLangBundle().getString("dupFindProgress"), null, 0, SpudTable.this.getRowCount());
        	monitor.setProgress(dupIndex);
        }

        @Override
        protected void done() {
            if (monitor != null) {
                monitor.close();
            }
        }

        @Override
        protected Void doInBackground() {
        	SpudTableIndexComp indexComp = new SpudTableIndexComp(((SpudTableModel) SpudTable.this.getModel()).getData());
        	Integer[] indexesSorted = indexComp.createIndexArray();
        	Arrays.sort(indexesSorted, indexComp);
        	
        	dupConfirmed.clear();
			dupNotSure.clear();

        	for (int i=dupIndex;i<SpudTable.this.getRowCount();i++) {
        		if (monitor.isCanceled()) return null;
				monitor.setProgress(i);
				
				if (notDup.contains(i)) continue;
				
				SpudItem firstItem = ((SpudTableModel) SpudTable.this.getModel()).getData().get(i);	
				notDup.add(i);
				
				int mitoDupIndex = -1;
				for (int m=0;m<indexesSorted.length;m++) {
					if (indexesSorted[m] == i) {
						mitoDupIndex = m;
						break;
					}
				}
				
				if (mitoDupIndex == -1) continue;
				
				for (int j=(mitoDupIndex - 100<0?0:mitoDupIndex - 100);j<(mitoDupIndex + 100>indexesSorted.length?indexesSorted.length:mitoDupIndex + 100);j++) {
					if (monitor.isCanceled()) return null;
					
					if (notDup.contains(indexesSorted[j])) continue;
					
					SpudItem secondItem = ((SpudTableModel) SpudTable.this.getModel()).getData().get(indexesSorted[j]);
					if (Math.abs(HaversineAlgorithm.HaversineInM(firstItem.getLat(), firstItem.getLon(), secondItem.getLat(), secondItem.getLon())) < dupDistance) {
						notDup.add(indexesSorted[j]);
						
						if (firstItem.getBinType() == secondItem.getBinType() && firstItem.getTxtType() == secondItem.getTxtType()) {
							if (dupAngle > -1) {
								int diff = Math.abs(firstItem.getDirection() - secondItem.getDirection());
								if (diff < (180 - dupAngle) || diff > (180 + dupAngle)) 
									dupConfirmed.add(indexesSorted[j]);
							} else {
								dupConfirmed.add(indexesSorted[j]);
							}
						} else {
							dupNotSure.add(indexesSorted[j]);
						}
					}
				}
				
				if (dupConfirmed.size() > 0 | dupNotSure.size() > 0) {
					dupIndex = i;
					SpudTable.this.spudTableListener.onMapCenterAndZoom(new int[] {SpudTable.this.getRowSorter().convertRowIndexToView(dupIndex)});
					((SpudTableModel) SpudTable.this.getModel()).fireTableDataChanged();
					Rectangle rect = SpudTable.this.getCellRect(SpudTable.this.getRowSorter().convertRowIndexToView(dupIndex), 
							SpudTableModel.COL_LONGITUDE, true);
					rect.y = rect.y - SpudTable.this.getVisibleRect().height/2 + rect.height/2;
					rect.height = SpudTable.this.getVisibleRect().height;
					SpudTable.this.scrollRectToVisible(rect);
					SpudTable.this.getParent().getParent().repaint();
					return null;
				} 
			}
			
        	JOptionPane.showMessageDialog(SpudTable.this.getParent(), Config.getLangBundle().getString("dupEndFile"), 
					Config.getLangBundle().getString("dupEndFileTitle"), JOptionPane.INFORMATION_MESSAGE, null);
			clearDuplicate();
			return null;
        }
    }
}
