package spudigo.ui.table.editor;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

import spudigo.Config;

public class DoubleEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 1L;

    public DoubleEditor(final JTextField jft) {
        super(jft);
        
        super.delegate = new EditorDelegate() {
			private static final long serialVersionUID = 1L;
			private String oldValue;

			public void setValue(Object value) {
				oldValue = (value != null)?String.format("%." + Config.getInstance().getInteger("position.number.decimal") + "f", (double) value):"0";
				jft.setText(oldValue);
            }

            public Object getCellEditorValue() {
            	if (oldValue.equals(jft.getText()))
            		return null;
            	
            	try {
            		return Double.parseDouble(jft.getText().replace(",", "."));
            	} catch (NumberFormatException e) {
            		return null;
            	}
            }
        };
    }
}
