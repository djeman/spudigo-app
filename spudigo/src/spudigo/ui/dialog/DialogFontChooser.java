package spudigo.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ToolTipManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import spudigo.Config;

public class DialogFontChooser extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private static final String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	private static final String[] fontSizes = new String[] { "7", "8", "9", "10", "11", "12", "14", "16",
													"18", "20", "22", "24", "26", "28", "36", "48" };
	private static final String PREVIEW_TEXT = "0 1 2 3 4 5 6 7 8 9";

	private int Closed_Option = JOptionPane.CLOSED_OPTION;

	private InputList fontNameInputList = new InputList(fontNames);
	private InputList fontSizeInputList = new InputList(fontSizes);

	private MutableAttributeSet attributes;

	private JCheckBox boldCheckBox = new JCheckBox(Config.getLangBundle().getString("diagFontChooserFontBold"));
	private JCheckBox italicCheckBox = new JCheckBox(Config.getLangBundle().getString("diagFontChooserFontItalic"));
	private JCheckBox underlineCheckBox = new JCheckBox(Config.getLangBundle().getString("diagFontChooserFontUnderline"));
	private JCheckBox strikethroughCheckBox = new JCheckBox(Config.getLangBundle().getString("diagFontChooserFontStrikethrough"));
	private JCheckBox subscriptCheckBox = new JCheckBox(Config.getLangBundle().getString("diagFontChooserFontSubscript"));
	private JCheckBox superscriptCheckBox = new JCheckBox(Config.getLangBundle().getString("diagFontChooserFontSuperscript"));
	
	private ColorComboBox colorComboBox;

	private FontLabel previewLabel;

	public DialogFontChooser(Window parent) {
		super(parent, Config.getLangBundle().getString("diagFontChooser"), Dialog.ModalityType.APPLICATION_MODAL);
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		JPanel p = new JPanel(new GridLayout(1, 2, 10, 2));
		p.setBorder(new TitledBorder(new EtchedBorder(), Config.getLangBundle().getString("diagFontChooserFont")));
		p.add(fontNameInputList);
		fontNameInputList.setDisplayedMnemonic('n');
		fontNameInputList.setToolTipText(Config.getLangBundle().getString("diagFontChooserFontName"));

		p.add(fontSizeInputList);
		fontSizeInputList.setDisplayedMnemonic('s');
		fontSizeInputList.setToolTipText(Config.getLangBundle().getString("diagFontChooserFontSize"));
		getContentPane().add(p);

		p = new JPanel(new GridLayout(2, 3, 10, 5));
		p.setBorder(new TitledBorder(new EtchedBorder(), Config.getLangBundle().getString("diagFontChooserFontEffects")));
		boldCheckBox.setMnemonic('b');
		p.add(boldCheckBox);

		italicCheckBox.setMnemonic('i');
		p.add(italicCheckBox);

		underlineCheckBox.setMnemonic('u');
		p.add(underlineCheckBox);

		strikethroughCheckBox.setMnemonic('r');
		p.add(strikethroughCheckBox);

		subscriptCheckBox.setMnemonic('t');
		p.add(subscriptCheckBox);

		superscriptCheckBox.setMnemonic('p');
		p.add(superscriptCheckBox);
		getContentPane().add(p);

		getContentPane().add(Box.createVerticalStrut(5));
		p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(Box.createHorizontalStrut(10));
		JLabel lbl = new JLabel(Config.getLangBundle().getString("diagFontChooserFontColor"));
		lbl.setDisplayedMnemonic('c');
		p.add(lbl);
		p.add(Box.createHorizontalStrut(20));
		colorComboBox = new ColorComboBox();
		lbl.setLabelFor(colorComboBox);
		ToolTipManager.sharedInstance().registerComponent(colorComboBox);
		p.add(colorComboBox);
		p.add(Box.createHorizontalStrut(10));
		getContentPane().add(p);

		p = new JPanel(new BorderLayout());
		p.setBorder(new TitledBorder(new EtchedBorder(), Config.getLangBundle().getString("diagFontChooserFontPreview")));
		previewLabel = new FontLabel(PREVIEW_TEXT);

		p.add(previewLabel, BorderLayout.CENTER);
		getContentPane().add(p);

		p = new JPanel(new FlowLayout());
		JPanel p1 = new JPanel(new GridLayout(1, 2, 10, 2));
		JButton btOK = new JButton(Config.getLangBundle().getString("ok"));
		getRootPane().setDefaultButton(btOK);
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Closed_Option = JOptionPane.OK_OPTION;
				setVisible(false);
			}
		};
		btOK.addActionListener(actionListener);
		p1.add(btOK);

		JButton btCancel = new JButton(Config.getLangBundle().getString("cancel"));
		actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Closed_Option = JOptionPane.CANCEL_OPTION;
				setVisible(false);
			}
		};
		btCancel.addActionListener(actionListener);
		p1.add(btCancel);
		p.add(p1);
		getContentPane().add(p);

		pack();
		setLocationRelativeTo(parent);

		ListSelectionListener listSelectListener = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				updatePreview();
			}
		};
		fontNameInputList.addListSelectionListener(listSelectListener);
		fontSizeInputList.addListSelectionListener(listSelectListener);

		actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updatePreview();
			}
		};
		
		boldCheckBox.addActionListener(actionListener);
		italicCheckBox.addActionListener(actionListener);
		colorComboBox.addActionListener(actionListener);
		underlineCheckBox.addActionListener(actionListener);
		strikethroughCheckBox.addActionListener(actionListener);
		subscriptCheckBox.addActionListener(actionListener);
		superscriptCheckBox.addActionListener(actionListener);
	}
	
	public void setAttributes(String configStr) {
		if (configStr == null)
			return;
			
		String[] values = configStr.split(",");
		if (values.length != 9)
			return;
		
		attributes = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attributes, values[0]);
		StyleConstants.setFontSize(attributes, Integer.parseInt(values[1]));
		StyleConstants.setBold(attributes, Boolean.parseBoolean(values[2]));
		StyleConstants.setItalic(attributes, Boolean.parseBoolean(values[3]));
		StyleConstants.setUnderline(attributes, Boolean.parseBoolean(values[4]));
		StyleConstants.setStrikeThrough(attributes, Boolean.parseBoolean(values[5]));
		StyleConstants.setSubscript(attributes, Boolean.parseBoolean(values[6]));
		StyleConstants.setSuperscript(attributes, Boolean.parseBoolean(values[7]));
		StyleConstants.setForeground(attributes, new Color((int)Long.parseLong(values[8], 16)));
		
		String name = StyleConstants.getFontFamily(attributes);
		fontNameInputList.setSelected(name);
		int size = StyleConstants.getFontSize(attributes);
		fontSizeInputList.setSelectedInt(size);
		boldCheckBox.setSelected(StyleConstants.isBold(attributes));
		italicCheckBox.setSelected(StyleConstants.isItalic(attributes));
		underlineCheckBox.setSelected(StyleConstants.isUnderline(attributes));
		strikethroughCheckBox.setSelected(StyleConstants.isStrikeThrough(attributes));
		subscriptCheckBox.setSelected(StyleConstants.isSubscript(attributes));
		superscriptCheckBox.setSelected(StyleConstants.isSuperscript(attributes));
		colorComboBox.setSelectedItem(StyleConstants.getForeground(attributes));
		updatePreview();
	}
	
	public String getCfgStr() {
		String name = fontNameInputList.getSelected();
		int size = fontSizeInputList.getSelectedInt();
		if (size <= 0) size = 10;
		
		StringBuilder sb = new StringBuilder();
		sb.append(name);sb.append(",");
		sb.append(size);sb.append(",");
		sb.append(boldCheckBox.isSelected());sb.append(",");
		sb.append(italicCheckBox.isSelected());sb.append(",");
		sb.append(underlineCheckBox.isSelected());sb.append(",");
		sb.append(strikethroughCheckBox.isSelected());sb.append(",");
		sb.append(subscriptCheckBox.isSelected());sb.append(",");
		sb.append(superscriptCheckBox.isSelected());sb.append(",");
		sb.append(String.format("%08X", ((Color)(colorComboBox.getSelectedItem())).getRGB()));
		
		return sb.toString();
	}
	
	public Map<TextAttribute, Object> getAttributes() {
		String name = fontNameInputList.getSelected();
		int size = fontSizeInputList.getSelectedInt();
		if (size <= 0) size = 10;

		Map<TextAttribute, Object> atb = new HashMap<TextAttribute, Object>();

		atb.put(TextAttribute.FAMILY, name);
		atb.put(TextAttribute.SIZE, (float) size);
		atb.put(TextAttribute.FOREGROUND, (Color) colorComboBox.getSelectedItem());

		if (underlineCheckBox.isSelected())
			atb.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_ONE_PIXEL);
		if (strikethroughCheckBox.isSelected())
			atb.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
		if (boldCheckBox.isSelected())
			atb.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
		if (italicCheckBox.isSelected())
			atb.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
		if (subscriptCheckBox.isSelected()) 
			atb.put(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB);
		if (superscriptCheckBox.isSelected())
			atb.put(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER);

		return atb;
	}

	public int getOption() {
		return Closed_Option;
	}

	protected void updatePreview() {
		StringBuilder previewText = new StringBuilder(PREVIEW_TEXT);
		int size = fontSizeInputList.getSelectedInt();
		if (size <= 0)
			return;

		superscriptCheckBox.setEnabled(!subscriptCheckBox.isSelected());
		subscriptCheckBox.setEnabled(!superscriptCheckBox.isSelected());

		Font fn = new Font(getAttributes());

		previewLabel.setText(previewText.toString());
		previewLabel.setFont(fn);
		previewLabel.repaint();
	}
	
	public static Map<TextAttribute, Object> getAttributes(String configStr) {
		if (configStr == null)
			return null;
			
		String[] values = configStr.split(",");
		if (values.length != 9)
			return null;
		
		Map<TextAttribute, Object> atb = new HashMap<TextAttribute, Object>();
		
		atb.put(TextAttribute.FAMILY, values[0]);
		atb.put(TextAttribute.SIZE, Float.parseFloat(values[1]));
		atb.put(TextAttribute.FOREGROUND, new Color((int)Long.parseLong(values[8], 16)));
		
		if (Boolean.parseBoolean(values[4]))
			atb.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_ONE_PIXEL);
		if (Boolean.parseBoolean(values[5]))
			atb.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
		if (Boolean.parseBoolean(values[2]))
			atb.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
		if (Boolean.parseBoolean(values[3]))
			atb.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
		if (Boolean.parseBoolean(values[6]))
			atb.put(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB);
		if (Boolean.parseBoolean(values[7]))
			atb.put(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER);
		
		return atb;
	}
}

class InputList extends JPanel implements ListSelectionListener, ActionListener {
	private static final long serialVersionUID = 1L;

	protected JLabel label = new JLabel();
	protected JTextField textfield;
	protected JList<Object> list;
	protected JScrollPane scroll;

	public InputList(String[] data) {
		setLayout(null);

		add(label);
		textfield = new OpelListText();
		textfield.addActionListener(this);
		label.setLabelFor(textfield);
		add(textfield);
		list = new OpelListList(data);
		list.setVisibleRowCount(4);
		list.addListSelectionListener(this);
		scroll = new JScrollPane(list);
		add(scroll);
	}

	public InputList(String title, int numCols) {
		setLayout(null);
		label = new OpelListLabel(title, JLabel.LEFT);
		add(label);
		textfield = new OpelListText(numCols);
		textfield.addActionListener(this);
		label.setLabelFor(textfield);
		add(textfield);
		list = new OpelListList();
		list.setVisibleRowCount(4);
		list.addListSelectionListener(this);
		scroll = new JScrollPane(list);
		add(scroll);
	}

	public void setToolTipText(String text) {
		super.setToolTipText(text);
		label.setToolTipText(text);
		textfield.setToolTipText(text);
		list.setToolTipText(text);
	}

	public void setDisplayedMnemonic(char ch) {
		label.setDisplayedMnemonic(ch);
	}

	public void setSelected(String sel) {
		list.setSelectedValue(sel, true);
		textfield.setText(sel);
	}

	public String getSelected() {
		return textfield.getText();
	}

	public void setSelectedInt(int value) {
		setSelected(Integer.toString(value));
	}

	public int getSelectedInt() {
		try {
			return Integer.parseInt(getSelected());
		} catch (NumberFormatException ex) {
			return -1;
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		Object obj = list.getSelectedValue();
		if (obj != null)
			textfield.setText(obj.toString());
	}

	public void actionPerformed(ActionEvent e) {
		ListModel<Object> model = list.getModel();
		String key = textfield.getText().toLowerCase();
		for (int k = 0; k < model.getSize(); k++) {
			String data = (String) model.getElementAt(k);
			if (data.toLowerCase().startsWith(key)) {
				list.setSelectedValue(data, true);
				break;
			}
		}
	}

	public void addListSelectionListener(ListSelectionListener lst) {
		list.addListSelectionListener(lst);
	}

	public Dimension getPreferredSize() {
		Insets ins = getInsets();
		Dimension labelSize = label.getPreferredSize();
		Dimension textfieldSize = textfield.getPreferredSize();
		Dimension scrollPaneSize = scroll.getPreferredSize();
		int w = Math.max(Math.max(labelSize.width, textfieldSize.width),
				scrollPaneSize.width);
		int h = labelSize.height + textfieldSize.height + scrollPaneSize.height;
		return new Dimension(w + ins.left + ins.right, h + ins.top + ins.bottom);
	}

	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	public void doLayout() {
		Insets ins = getInsets();
		Dimension size = getSize();
		int x = ins.left;
		int y = ins.top;
		int w = size.width - ins.left - ins.right;
		int h = size.height - ins.top - ins.bottom;

		Dimension labelSize = label.getPreferredSize();
		label.setBounds(x, y, w, labelSize.height);
		y += labelSize.height;
		Dimension textfieldSize = textfield.getPreferredSize();
		textfield.setBounds(x, y, w, textfieldSize.height);
		y += textfieldSize.height;
		scroll.setBounds(x, y, w, h - y);
	}

	public void appendResultSet(ResultSet results, int index, boolean toTitleCase) {
		textfield.setText("");
		DefaultListModel<Object> model = new DefaultListModel<Object>();
		try {
			while (results.next()) {
				String str = results.getString(index);
				if (toTitleCase) {
					str = Character.toUpperCase(str.charAt(0))
							+ str.substring(1);
				}

				model.addElement(str);
			}
		} catch (SQLException ex) {
			System.err.println("appendResultSet: " + ex.toString());
		}
		list.setModel(model);
		if (model.getSize() > 0)
			list.setSelectedIndex(0);
	}

	class OpelListLabel extends JLabel {
		private static final long serialVersionUID = 1L;

		public OpelListLabel(String text, int alignment) {
			super(text, alignment);
		}

		public AccessibleContext getAccessibleContext() {
			return InputList.this.getAccessibleContext();
		}
	}

	class OpelListText extends JTextField {
		private static final long serialVersionUID = 1L;

		public OpelListText() {
		}

		public OpelListText(int numCols) {
			super(numCols);
		}

		public AccessibleContext getAccessibleContext() {
			return InputList.this.getAccessibleContext();
		}
	}

	class OpelListList extends JList<Object> {
		private static final long serialVersionUID = 1L;

		public OpelListList() {
		}

		public OpelListList(String[] data) {
			super(data);
		}

		public AccessibleContext getAccessibleContext() {
			return InputList.this.getAccessibleContext();
		}
	}

	// Accessibility Support

	public AccessibleContext getAccessibleContext() {
		if (accessibleContext == null)
			accessibleContext = new AccessibleOpenList();
		return accessibleContext;
	}

	protected class AccessibleOpenList extends AccessibleJComponent {
		private static final long serialVersionUID = 1L;

		public String getAccessibleName() {
			System.out.println("getAccessibleName: " + accessibleName);
			if (accessibleName != null)
				return accessibleName;
			return label.getText();
		}

		public AccessibleRole getAccessibleRole() {
			return AccessibleRole.LIST;
		}
	}
}

class FontLabel extends JLabel {
	private static final long serialVersionUID = 1L;

	public FontLabel(String text) {
		super(text, JLabel.CENTER);
		setBackground(Color.white);
		setForeground(Color.black);
		setOpaque(true);
		setBorder(new LineBorder(Color.black));
		setPreferredSize(new Dimension(120, 40));
	}
	
	@Override
    protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setColor(Color.BLACK);
		g2d.fill(new Rectangle2D.Double(0, 0, (double)getWidth()/3, getHeight()));
		g2d.setColor(Color.GRAY);
		g2d.fill(new Rectangle2D.Double((double)getWidth()/3, 0, (double)getWidth()/3, getHeight()));
		g2d.setColor(Color.WHITE);
		g2d.fill(new Rectangle2D.Double(((double)getWidth()/3)*2, 0, (double)getWidth()/3, getHeight()));
		
		g2d.setFont(this.getFont());
		FontMetrics metrics = g.getFontMetrics();
		int tw = metrics.stringWidth(getText());
		int th = metrics.getAscent();
		if (tw > (this.getWidth()/3)) {
			g2d.drawString(getText(), (-tw/2)+(this.getWidth()/2), (th/2)+(this.getHeight()/2));
		} else {
			g2d.drawString(getText(), (-tw/2)+(this.getWidth()/3), (th/2)+(this.getHeight()/2));
			g2d.drawString(getText(), (-tw/2)+((this.getWidth()/3)*2), (th/2)+(this.getHeight()/2));
		}
		
		g2d.dispose();
		
		//super.paintComponent(g);
    }
}

class ColorComboBox extends JComboBox<Object> {
	private static final long serialVersionUID = 1L;

	public ColorComboBox() {
		int[] values = new int[] { 0, 128, 192, 255 };
		for (int r = 0; r < values.length; r++)
			for (int g = 0; g < values.length; g++)
				for (int b = 0; b < values.length; b++) {
					Color c = new Color(values[r], values[g], values[b]);
					addItem(c);
				}
		setRenderer(new ColorComboRenderer1());

	}

	class ColorComboRenderer1 extends JPanel implements ListCellRenderer<Object> {
		private static final long serialVersionUID = 1L;
		protected Color m_c = Color.black;

		public ColorComboRenderer1() {
			super();
			setBorder(new CompoundBorder(new MatteBorder(2, 10, 2, 10,
					Color.white), new LineBorder(Color.black)));
		}

		public Component getListCellRendererComponent(JList<?> list, Object obj,
				int row, boolean sel, boolean hasFocus) {
			if (obj instanceof Color)
				m_c = (Color) obj;
			return this;
		}

		public void paint(Graphics g) {
			setBackground(m_c);
			super.paint(g);
		}

	}
}
