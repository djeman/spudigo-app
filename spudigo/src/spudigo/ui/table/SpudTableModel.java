package spudigo.ui.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.event.UndoableEditListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.UndoableEditSupport;

import spudigo.Config;
import spudigo.EnumDirType;
import spudigo.EnumStatus;
import spudigo.SpudItem;
import spudigo.TemplateFile;

public class SpudTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String[] columnNames = Config.getLangBundle().getStringArray("tableColumnNames");
	
	private TemplateFile modelIn;
	
	public final static int COL_LONGITUDE = 0;
	public final static int COL_LATITUDE = 1;
	public final static int COL_TYPE = 2;
	public final static int COL_SPEED = 3;
	public final static int COL_DIRECTION = 4;
	public final static int COL_ANGLE = 5;
	public final static int COL_COMMENT = 6;
	public final static int COL_STATUS = 7;
	
	private final boolean isTxt;
	private final ArrayList<SpudItem> data;
	private final UndoableEditSupport support = new UndoableEditSupport();
	private final Collection<ValueChangedListener> valueChangedListener = new ArrayList<ValueChangedListener>();

	public SpudTableModel(ArrayList<SpudItem> data, TemplateFile modelIn, boolean isTxt) {
		this.data = data;
		this.modelIn = modelIn;
		this.isTxt = isTxt;
	}
	
	public boolean isTxt() {
		return this.isTxt;
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}
	
	@Override
	public String getColumnName(int col) {
        return columnNames[col];
    }

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
			case COL_LONGITUDE:
				return data.get(row).getLon();
			case COL_LATITUDE:
				return data.get(row).getLat();
			case COL_TYPE:
				return getValueTypeAt(row, col);
			case COL_SPEED:
				return data.get(row).getSpeed();
			case COL_DIRECTION:
				return EnumDirType.getDirType(data.get(row).getDirType());
			case COL_ANGLE:
				return data.get(row).getDirection();
			case COL_COMMENT:
				return data.get(row).getComment();
			case COL_STATUS:
				return EnumStatus.getStatus(data.get(row).getStatus());
		}
		
		return 0;
	}
	
	private Object getValueTypeAt(int row, int col) {
		if (modelIn == null) {
			if (!this.isTxt) {
				return String.valueOf(data.get(row).getBinType());
			} else { 
				return String.valueOf(data.get(row).getTxtType());
			}
		} else {
			if (!this.isTxt) {
				return modelIn.getLabelWithBinType(data.get(row).getBinType(), data.get(row).getSpeed());
			} else { 
				return modelIn.getLabelWithTxtType(data.get(row).getTxtType(), data.get(row).getSpeed());
			} 
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int col) {
		switch (col) {
	        case COL_LONGITUDE:
	        case COL_LATITUDE:
	            return Double.class;
	        case COL_TYPE:
	        case COL_DIRECTION:
	        case COL_COMMENT:
	        case COL_STATUS:
	            return String.class;
	        case COL_SPEED:
	        case COL_ANGLE:
	            return Integer.class;
	        default:
	            return Object.class;
	    }
    }
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return true;
    }
	
	public void setUndoRedoAt(Object newValue, int row, int col) {
		if (newValue == null) return;
		
		setValue(newValue, row, col);
	}
	
	@Override
	public void setValueAt(Object newValue, int row, int col) {
		if (newValue == null) return;
		
		Object oldValue = getValueAt(row, col);
		setValue(newValue, row, col);
        fireChangeEdit(row, col, oldValue, newValue);
    }
	
	private void setValue(Object newValue, int row, int col) {
		boolean res = true;
		
		switch (col) {
			case COL_LONGITUDE:
				res = setValueLonAt((double) newValue, row);
				break;
			case COL_LATITUDE:
				res = setValueLatAt((double) newValue, row);
				break;
			case COL_TYPE:
				res = setValueTypeAt((String) newValue, row);
				break;
			case COL_SPEED:
				res = setValueSpeedAt((int) newValue, row);
				break;
			case COL_DIRECTION:
				int dirType = EnumDirType.getDirType((String) newValue);
				if (data.get(row).getDirType() != dirType)
					data.get(row).setDirType(dirType);
				else 
					res = false;
				break;
			case COL_ANGLE:
				res = setValueDirectionAt((int) newValue, row);
				break;
			case COL_COMMENT:
				while (((String) newValue).endsWith(","))
					newValue = ((String) newValue).substring(0, ((String) newValue).length()-2).trim();
					
				if (!((String) newValue).endsWith(",") && !data.get(row).getComment().equals((String) newValue))
					data.get(row).setComment((String) newValue);
				else 
					res = false;
				break;
			case COL_STATUS:
				int status = EnumStatus.getStatus((String) newValue);
				if (data.get(row).getStatus() != status)
					data.get(row).setStatus(status);
				else 
					res = false;
				break;
			default:
				return;
		}
        
		if (!res)
			return;
		
		fireTableCellUpdated(row, col);
		
		for (ValueChangedListener listener : valueChangedListener)
			listener.onValueChanged(row, col);
			
	}
	
	private boolean setValueLonAt(double value, int row) {
		if (value > 180 || value < -180 || data.get(row).getLon() == value)
			return false;
		
		data.get(row).setCoord(data.get(row).getLat(), value);
		return true;
	}
	
	private boolean setValueLatAt(double value, int row) {
		if (value > 90 || value < -90 || data.get(row).getLat() == value)
			return false;
		
		data.get(row).setCoord(value, data.get(row).getLon());
		return true;
	}
	
	private boolean setValueTypeAt(String newValue, int row) {
		if (modelIn == null) {
			int value = Integer.parseInt(newValue);
			if (this.isTxt) {
				if (data.get(row).getTxtType() == value) return false;
				data.get(row).setTxtType(value);
			} else {
				if (data.get(row).getBinType() == value) return false;
				data.get(row).setBinType(value);
			}
		} else {
			if (this.isTxt) {
				int value = modelIn.getTxtTypeWithLabel(newValue, data.get(row).getSpeed());
				if (data.get(row).getTxtType() == value) return false;
				data.get(row).setTxtType(value);
			} else {
				int value = modelIn.getBinTypeWithLabel(newValue, data.get(row).getSpeed());
				if (data.get(row).getBinType() == value) return false;
				data.get(row).setBinType(value);
			}
		}
		
		return true;
	}
	
	private boolean setValueSpeedAt(int value, int row) {
		if (value > 255 || value < 0 || data.get(row).getSpeed() == value)
			return false;
		
		data.get(row).setSpeed(value);
		this.fireTableRowsUpdated(row, row);
		return true;
	}
	
	private boolean setValueDirectionAt(int value, int row) {
		if (value > 359 || value < 0 || data.get(row).getDirection() == value)
			return false;
		
		data.get(row).setDirection(value);
		return true;
	}

	public void addUndoableEditListener(UndoableEditListener l) {
		support.addUndoableEditListener(l);
	}

	public void removeUndoableEditListener(UndoableEditListener l) {
		support.removeUndoableEditListener(l);
	}
	
	public TemplateFile getModelIn() {
		return modelIn;
	}

	public void setModelIn(TemplateFile modelIn) {
		this.modelIn = modelIn;
		this.fireTableDataChanged();
	}
	
	public ArrayList<SpudItem> getData() {
		return this.data;
	}
	
	public void addRow() {
		this.data.add(new SpudItem());
		this.fireTableRowsInserted(getRowCount()-1, getRowCount()-1);
	}
	
	public void addRow(double lat, double lon) {
		SpudItem item = new SpudItem(lat, lon);
		this.data.add(item);
		this.fireTableRowsInserted(getRowCount()-1, getRowCount()-1);
	}
	
	public void addRow(SpudItem copyRow) {
		if (copyRow != null) {
			SpudItem item = new SpudItem(
					copyRow.getLat(),
					copyRow.getLon());
			item.setBinType(copyRow.getBinType());
			item.setDirection(copyRow.getDirection());
			item.setDirType(copyRow.getDirType());
			item.setSpeed(copyRow.getSpeed());
			item.setStatus(copyRow.getStatus());
			item.setTxtType(copyRow.getTxtType());
			item.setComment(copyRow.getComment());
			this.data.add(item);
			this.fireTableRowsInserted(getRowCount()-1, getRowCount()-1);
		}
	}
	
	public void insertRow(int index, SpudItem copyRow) {
		if (copyRow != null) {
			SpudItem item = new SpudItem(
					copyRow.getLat(),
					copyRow.getLon());
			item.setBinType(copyRow.getBinType());
			item.setDirection(copyRow.getDirection());
			item.setDirType(copyRow.getDirType());
			item.setSpeed(copyRow.getSpeed());
			item.setStatus(copyRow.getStatus());
			item.setTxtType(copyRow.getTxtType());
			item.setComment(copyRow.getComment());
			this.data.add(index, item);
			this.fireTableRowsInserted(index, index);
		}
	}
	
	public void insertRow(int index, double lat, double lon) {
		SpudItem item = new SpudItem(lat, lon);
		this.data.add(index, item);
		this.fireTableRowsInserted(index, index);
	}
	
	public void removeRows(int[] selectedRows) {
		if (selectedRows != null && selectedRows.length > 0) {
			Arrays.sort(selectedRows);
			for (int i=selectedRows.length-1;i>-1;i--) {
				if (selectedRows[i] > -1 && selectedRows[i] < this.data.size())
					this.data.remove(selectedRows[i]);
			}
			
			this.fireTableRowsDeleted(getRowCount()-selectedRows.length, getRowCount()-1);
		}
	}
	
	protected void fireChangeEdit(int row, int col, Object oldValue, Object newValue) {
		UndoableEdit edit = new TableChangeEdit(row, col, oldValue, newValue);
		support.beginUpdate();
		support.postEdit(edit);
		support.endUpdate();
	}
	
	public void addValueChangedListener(ValueChangedListener listener) {
		valueChangedListener.add(listener);
	}

	public void removeValueChangedListener(ValueChangedListener listener) {
		valueChangedListener.remove(listener);
	}
	
	private class TableChangeEdit extends AbstractUndoableEdit {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int columnIndex;
		private int rowIndex;
		private Object oldValue;
		private Object newValue;

		public TableChangeEdit(int rowIndex, int columnIndex, Object oldValue, Object newValue) {
			this.columnIndex = columnIndex;
			this.rowIndex = rowIndex;
			this.oldValue = oldValue;
			this.newValue = newValue;
		}

		public void undo() {
			super.undo();
			SpudTableModel.this.setUndoRedoAt(oldValue, rowIndex, columnIndex);
		}

		public void redo() {
			super.redo();
			SpudTableModel.this.setUndoRedoAt(newValue, rowIndex, columnIndex);
		}
	}
	
	public interface ValueChangedListener {
		void onValueChanged(int row, int col);
	}
}
