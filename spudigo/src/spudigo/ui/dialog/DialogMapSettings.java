package spudigo.ui.dialog;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import spudigo.Config;
import spudigo.ui.filefilter.FileFilterImage;

public class DialogMapSettings extends JDialog implements PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	
	final static String SET_ZONE = Config.getLangBundle().getString("diagOptMapConfigSetZone");
    final static String SET_IMAGE = Config.getLangBundle().getString("diagOptMapConfigSetImage");
    final static String SET_SPEED = Config.getLangBundle().getString("diagOptMapConfigSetSpeed");
    final static String SET_ZONEPOI = Config.getLangBundle().getString("diagOptMapConfigSetZonePoi");
	
    private JSlider sliderZoneBeginCount;
	private JTextField textFieldZoneBeginCount;
	private JSlider sliderZoneMinSize;
	private JTextField textFieldZoneMinSize;
	
	private ColorButton buttonColor10;
	private ColorButton buttonColor100;
	private ColorButton buttonColor1000;
	private ColorButton buttonColor10000;
	private ColorButton buttonColor10000m;
	private FontButton buttonZoneFont;
    
	private JSlider sliderRadAngle;
	private JTextField textFieldRadAngle;
	private JSlider sliderFactorX;
	private JTextField textFieldFactorX;
	private JSlider sliderFactorY;
	private JTextField textFieldFactorY;
	private ColorButton buttonColor0;
	private JTextField textFieldColor0;
	private ColorButton buttonColor1;
	private JTextField textFieldColor1;
	private ColorButton buttonColor2;
	private JTextField textFieldColor2;
	
	private JTextField[] textFieldImg;
	private JButton[] buttonImg;
	private JTextField textFieldImgS;
	private JButton buttonImgS;
	private JTextField textFieldImgU;
	private JButton buttonImgU;
	
	private FontButton buttonSpeedFont;
	private JSlider sliderSpeedX;
	private JTextField textFieldSpeedX;
	private JSlider sliderSpeedY;
	private JTextField textFieldSpeedY;
	private JCheckBox checkBoxSpeedCenter;

	private JPanel cards;
    private JOptionPane optionPane;

    private String btnString1 = Config.getLangBundle().getString("apply");
    private String btnString2 = Config.getLangBundle().getString("tbClose");
    
    private ReloadListener reloadListener = null;

    /** Creates the reusable dialog. */
    public DialogMapSettings(Window parent) {
    	super(parent, Dialog.ModalityType.APPLICATION_MODAL);
        setTitle(Config.getLangBundle().getString("mbMapSettings"));
        this.setMinimumSize(new Dimension(500, 320));
        this.setPreferredSize(new Dimension(500, 320));
        
        JPanel comboBoxPane = new JPanel(new BorderLayout());
        comboBoxPane.setBorder(new EmptyBorder(0,0,10,0));
        String comboBoxItems[] = { SET_ZONE, SET_IMAGE, SET_SPEED, SET_ZONEPOI };
        final JComboBox<String> cb = new JComboBox<String>(comboBoxItems);
        ((JLabel)cb.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
        cb.setEditable(false);
        cb.setFocusable(false);
        cb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				CardLayout cl = (CardLayout)(cards.getLayout());
		        cl.show(cards, (String)e.getItem());
			}
        });
        
        comboBoxPane.add(cb, BorderLayout.CENTER);
        
        JPanel panZone = getPanZone();
        JScrollPane panImage = new JScrollPane(getPanImage());
        JPanel panSpeed = getPanSpeed();
        JPanel panPoiZone = getPanPoiZone();
        
        cards = new JPanel(new CardLayout());
        cards.add(panZone, SET_ZONE);
        cards.add(panImage, SET_IMAGE);
        cards.add(panSpeed, SET_SPEED);
        cards.add(panPoiZone, SET_ZONEPOI);
        
        JPanel tmp = new JPanel(new BorderLayout());
        tmp.add(comboBoxPane, BorderLayout.NORTH);
        tmp.add(cards, BorderLayout.CENTER);
        
        //Create an array specifying the number of dialog buttons
        //and their text.
        Object[] options = {btnString1, btnString2};

        //Create the JOptionPane.
        optionPane = new JOptionPane(tmp,
                                    JOptionPane.PLAIN_MESSAGE,
                                    JOptionPane.OK_CANCEL_OPTION,
                                    null,
                                    options,
                                    options[0]);
        
        //Make this dialog display it.
        setContentPane(optionPane);
        
        pack();
        setLocationRelativeTo(parent);

        //Handle window closing correctly.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent we) {
        		clearAndHide();
            }
        });

        //Ensure the text field always gets the first focus.
        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
            	//textFieldLat.requestFocusInWindow();
            }
        });

        //Register an event handler that reacts to option pane state changes.
        optionPane.addPropertyChangeListener(this);
    }
    
    private JPanel getPanPoiZone() {
    	JPanel panPoiZone = new JPanel(new GridBagLayout());
    	panPoiZone.setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.GRAY),
        		new EmptyBorder(10,5,10,5)));
    	
    	GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0;c.weighty = 0;c.gridx = 0;c.gridy = 0;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 0, 0);
		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.NONE;
		panPoiZone.add(new JLabel(Config.getLangBundle().getString("diagOptMapConfigZoneBeginPoi")), c);
		
		sliderZoneBeginCount = new JSlider(JSlider.HORIZONTAL, 100, 50000, Config.getInstance().getInteger("zone.begin.count"));
		sliderZoneBeginCount.setMajorTickSpacing(1000);
		sliderZoneBeginCount.setMinorTickSpacing(500);
		sliderZoneBeginCount.setPaintTicks(true);
		c.weightx = 1;c.gridx = 1;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 5, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		panPoiZone.add(sliderZoneBeginCount, c);
		
		textFieldZoneBeginCount = new JTextField(String.format("%1$d", Config.getInstance().getInteger("zone.begin.count")));
		textFieldZoneBeginCount.setHorizontalAlignment(JTextField.CENTER);
		textFieldZoneBeginCount.setEditable(false);
		textFieldZoneBeginCount.setBackground(Color.WHITE);
		textFieldZoneBeginCount.setColumns(4);
		c.weightx = 0;c.gridx = 2;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 0, 0);
		panPoiZone.add(textFieldZoneBeginCount, c);
		
		sliderZoneBeginCount.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				textFieldZoneBeginCount.setText(String.format("%1$d", source.getValue()));
			}
		});
		
		c.weightx = 0;c.gridx = 0;c.gridwidth = 1;c.gridy = 1;
		c.insets = new Insets(0, 0, 0, 0);
		c.fill = GridBagConstraints.NONE;
		panPoiZone.add(new JLabel(Config.getLangBundle().getString("diagOptMapConfigZoneMinSize")), c);
		
		sliderZoneMinSize = new JSlider(JSlider.HORIZONTAL, 10, 500, Config.getInstance().getInteger("zone.size.mini"));
		sliderZoneMinSize.setMajorTickSpacing(100);
		sliderZoneMinSize.setMinorTickSpacing(10);
		sliderZoneMinSize.setPaintTicks(true);
		c.weightx = 1;c.gridx = 1;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 5, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		panPoiZone.add(sliderZoneMinSize, c);
		
		textFieldZoneMinSize = new JTextField(String.format("%1$d px", Config.getInstance().getInteger("zone.size.mini")));
		textFieldZoneMinSize.setHorizontalAlignment(JTextField.CENTER);
		textFieldZoneMinSize.setEditable(false);
		textFieldZoneMinSize.setBackground(Color.WHITE);
		c.weightx = 0;c.gridx = 2;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 0, 0);
		panPoiZone.add(textFieldZoneMinSize, c);
		
		sliderZoneMinSize.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				textFieldZoneMinSize.setText(String.format("%1$d px", source.getValue()));
			}
		});
		
		JPanel panTemp = new JPanel(new GridBagLayout());
		GridBagConstraints cTemp = new GridBagConstraints();
		
		cTemp.weightx = 0;cTemp.weighty = 0;cTemp.gridx = 0;cTemp.gridy = 0;cTemp.gridwidth = 1;
		cTemp.anchor = GridBagConstraints.CENTER;
		cTemp.fill = GridBagConstraints.BOTH;
		panTemp.add(new JLabel("x < 10", JLabel.CENTER), cTemp);
		cTemp.gridx = 1;
		panTemp.add(new JLabel("10 < x < 100", JLabel.CENTER), cTemp);
		cTemp.gridx = 2;
		panTemp.add(new JLabel("100 < x < 1k", JLabel.CENTER), cTemp);
		cTemp.gridx = 3;
		panTemp.add(new JLabel("1k < x < 10k", JLabel.CENTER), cTemp);
		cTemp.gridx = 4;
		panTemp.add(new JLabel("x > 10k", JLabel.CENTER), cTemp);
		
		buttonColor10 = new ColorButton();
		buttonColor10.setOpaque(false);
		buttonColor10.setBackground(new Color(Config.getInstance().getIntegerHexa("zone.color.argb.10"), true));
		buttonColor10.setPreferredSize(new Dimension(0, (int) textFieldColor0.getPreferredSize().getHeight()));
		buttonColor10.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DialogColorChooser dcc = new DialogColorChooser(DialogMapSettings.this, 
						buttonColor10.getBackground());
				dcc.setVisible(true);
				buttonColor10.setBackground(dcc.getColor());
				dcc.dispose();
			}
		});
		cTemp.weightx = 1;cTemp.gridy = 1;cTemp.gridx = 0;
		panTemp.add(buttonColor10, cTemp);
		
		buttonColor100 = new ColorButton();
		buttonColor100.setOpaque(false);
		buttonColor100.setBackground(new Color(Config.getInstance().getIntegerHexa("zone.color.argb.100"), true));
		buttonColor100.setPreferredSize(new Dimension(0, (int) textFieldColor0.getPreferredSize().getHeight()));
		buttonColor100.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DialogColorChooser dcc = new DialogColorChooser(DialogMapSettings.this, 
						buttonColor100.getBackground());
				dcc.setVisible(true);
				buttonColor100.setBackground(dcc.getColor());
				dcc.dispose();
			}
		});
		cTemp.gridx = 1;
		panTemp.add(buttonColor100, cTemp);
		
		buttonColor1000 = new ColorButton();
		buttonColor1000.setOpaque(false);
		buttonColor1000.setBackground(new Color(Config.getInstance().getIntegerHexa("zone.color.argb.1000"), true));
		buttonColor1000.setPreferredSize(new Dimension(0, (int) textFieldColor0.getPreferredSize().getHeight()));
		buttonColor1000.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DialogColorChooser dcc = new DialogColorChooser(DialogMapSettings.this, 
						buttonColor1000.getBackground());
				dcc.setVisible(true);
				buttonColor1000.setBackground(dcc.getColor());
				dcc.dispose();
			}
		});
		cTemp.gridx = 2;
		panTemp.add(buttonColor1000, cTemp);
		
		buttonColor10000 = new ColorButton();
		buttonColor10000.setOpaque(false);
		buttonColor10000.setBackground(new Color(Config.getInstance().getIntegerHexa("zone.color.argb.10000"), true));
		buttonColor10000.setPreferredSize(new Dimension(0, (int) textFieldColor0.getPreferredSize().getHeight()));
		buttonColor10000.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DialogColorChooser dcc = new DialogColorChooser(DialogMapSettings.this, 
						buttonColor10000.getBackground());
				dcc.setVisible(true);
				buttonColor10000.setBackground(dcc.getColor());
				dcc.dispose();
			}
		});
		cTemp.gridx = 3;
		panTemp.add(buttonColor10000, cTemp);
		
		buttonColor10000m = new ColorButton();
		buttonColor10000m.setOpaque(false);
		buttonColor10000m.setBackground(new Color(Config.getInstance().getIntegerHexa("zone.color.argb.10000+"), true));
		buttonColor10000m.setPreferredSize(new Dimension(0, (int) textFieldColor0.getPreferredSize().getHeight()));
		buttonColor10000m.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DialogColorChooser dcc = new DialogColorChooser(DialogMapSettings.this, 
						buttonColor10000m.getBackground());
				dcc.setVisible(true);
				buttonColor10000m.setBackground(dcc.getColor());
				dcc.dispose();
			}
		});
		cTemp.gridx = 4;
		panTemp.add(buttonColor10000m, cTemp);
		
		c.weightx = 0;c.gridx = 0;c.gridwidth = 3;c.gridy = 2;c.gridheight = 2;
		c.insets = new Insets(0, 0, 0, 0);
		panPoiZone.add(panTemp, c);
		
		c.weightx = 0;c.gridx = 0;c.gridwidth = 1;c.gridy = 4;c.gridheight = 1;
		c.insets = new Insets(10, 0, 0, 0);
		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.NONE;
		panPoiZone.add(new JLabel(Config.getLangBundle().getString("diagOptMapConfigZoneFont")), c);
		
		buttonZoneFont = new FontButton("0 1 2 3 4 5 6 7 8 9", new Color[]{
				buttonColor10.getBackground(),
				buttonColor100.getBackground(),
				buttonColor1000.getBackground()
		});
		buttonZoneFont.setActionCommand(Config.getInstance().getString("zone.font"));
		buttonZoneFont.setFont(new Font(DialogFontChooser.getAttributes(buttonZoneFont.getActionCommand())));
		buttonZoneFont.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DialogFontChooser dfc = new DialogFontChooser(DialogMapSettings.this);
				dfc.setAttributes(buttonZoneFont.getActionCommand());
				dfc.setVisible(true);
				
				if (dfc.getOption() == JOptionPane.OK_OPTION) {
					buttonZoneFont.setActionCommand(dfc.getCfgStr());
					buttonZoneFont.setFont(new Font(dfc.getAttributes()));
				}
				
				dfc.dispose();
			}
		});
		c.weightx = 1;c.gridx = 1;c.gridwidth = 2;
		c.insets = new Insets(10, 5, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTH;
		panPoiZone.add(buttonZoneFont, c);
		
		return panPoiZone;
	}

	private JPanel getPanSpeed() {
    	JPanel panSpeed = new JPanel(new GridBagLayout());
    	panSpeed.setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.GRAY),
        		new EmptyBorder(10,5,10,5)));
		
    	GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0;c.weighty = 0;c.gridx = 0;c.gridy = 0;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 0, 0);
		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.NONE;
		panSpeed.add(new JLabel(Config.getLangBundle().getString("diagOptMapConfigSpeedFont")), c);
    	
		buttonSpeedFont = new FontButton("0 1 2 3 4 5 6 7 8 9");
		buttonSpeedFont.setActionCommand(Config.getInstance().getString("marker.speed.font"));
		buttonSpeedFont.setFont(new Font(DialogFontChooser.getAttributes(buttonSpeedFont.getActionCommand())));
		buttonSpeedFont.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DialogFontChooser dfc = new DialogFontChooser(DialogMapSettings.this);
				dfc.setAttributes(buttonSpeedFont.getActionCommand());
				dfc.setVisible(true);
				
				if (dfc.getOption() == JOptionPane.OK_OPTION) {
					buttonSpeedFont.setActionCommand(dfc.getCfgStr());
					buttonSpeedFont.setFont(new Font(dfc.getAttributes()));
				}
				
				dfc.dispose();
			}
		});
		c.weightx = 1;c.gridx = 1;c.gridwidth = 2;
		c.insets = new Insets(0, 5, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTH;
		panSpeed.add(buttonSpeedFont, c);
    	
		c.weightx = 1;c.weighty = 0;c.gridx = 0;c.gridy = 1;c.gridwidth = 3;
		c.insets = new Insets(10, 0, 10, 0);
		c.fill = GridBagConstraints.CENTER;
		panSpeed.add(new JLabel(Config.getLangBundle().getString("diagOptMapConfigSpeedHelp")), c);
		
		c.weightx = 0;c.weighty = 0;c.gridx = 0;c.gridy = 2;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 0, 0);
		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.NONE;
		panSpeed.add(new JLabel(Config.getLangBundle().getString("diagOptMapConfigSpeedX")), c);
		
		sliderSpeedX = new JSlider(JSlider.HORIZONTAL, -200, 200, Config.getInstance().getInteger("marker.speed.x")*-1);
		sliderSpeedX.setMajorTickSpacing(20);
		sliderSpeedX.setMinorTickSpacing(1);
		sliderSpeedX.setPaintTicks(true);
		c.weightx = 1;c.gridx = 1;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		panSpeed.add(sliderSpeedX, c);
		
		textFieldSpeedX = new JTextField(String.format("%1$d px", Config.getInstance().getInteger("marker.speed.x")*-1));
		textFieldSpeedX.setHorizontalAlignment(JTextField.CENTER);
		textFieldSpeedX.setEditable(false);
		textFieldSpeedX.setBackground(Color.WHITE);
		textFieldSpeedX.setColumns(4);
		c.weightx = 0;c.gridx = 2;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 0, 0);
		panSpeed.add(textFieldSpeedX, c);
		
		sliderSpeedX.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				textFieldSpeedX.setText(String.format("%1$d px", source.getValue()));
			}
		});
		
		checkBoxSpeedCenter = new JCheckBox(Config.getLangBundle().getString("diagOptMapConfigCenterTextX"));
		checkBoxSpeedCenter.setSelected(Config.getInstance().getBoolean("marker.speed.x.center"));
		c.weightx = 1;c.weighty = 0;c.gridx = 0;c.gridy = 3;c.gridwidth = 3;
		c.insets = new Insets(5, 0, 5, 0);
		panSpeed.add(checkBoxSpeedCenter, c);
		
		c.weightx = 0;c.weighty = 0;c.gridx = 0;c.gridy = 4;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 0, 0);
		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.NONE;
		panSpeed.add(new JLabel(Config.getLangBundle().getString("diagOptMapConfigSpeedY")), c);
		
		sliderSpeedY = new JSlider(JSlider.HORIZONTAL, -200, 200, Config.getInstance().getInteger("marker.speed.y")*-1);
		sliderSpeedY.setMajorTickSpacing(20);
		sliderSpeedY.setMinorTickSpacing(1);
		sliderSpeedY.setPaintTicks(true);
		c.weightx = 1;c.gridx = 1;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		panSpeed.add(sliderSpeedY, c);
		
		textFieldSpeedY = new JTextField(String.format("%1$d px", Config.getInstance().getInteger("marker.speed.y")*-1));
		textFieldSpeedY.setHorizontalAlignment(JTextField.CENTER);
		textFieldSpeedY.setEditable(false);
		textFieldSpeedY.setBackground(Color.WHITE);
		textFieldSpeedY.setColumns(4);
		c.weightx = 0;c.gridx = 2;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 0, 0);
		panSpeed.add(textFieldSpeedY, c);
		
		sliderSpeedY.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				textFieldSpeedY.setText(String.format("%1$d px", source.getValue()));
			}
		});
    	
		return panSpeed;
	}

	private JPanel getPanImage() {
    	JPanel panImage = new JPanel(new GridBagLayout());
		panImage.setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.GRAY),
        		new EmptyBorder(10,5,10,5)));
		
		textFieldImg = new JTextField[32];
		buttonImg = new JButton[32];

		GridBagConstraints c = new GridBagConstraints();
		
		c.weightx = 1;c.gridx = 1;c.gridwidth = 1;c.gridy = 0;
		c.insets = new Insets(0, 0, 5, 0);
		c.anchor = GridBagConstraints.CENTER;
		panImage.add(new JLabel(Config.getLangBundle().getString("diagOptMapConfigImgPath")), c);
		
		
		c.weightx = 0;c.weighty = 0;c.gridx = 0;c.gridy = 1;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 2, 5);
		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.NONE;
		panImage.add(new JLabel(Config.getLangBundle().getString("diagOptMapConfigSelected")), c);
		
		textFieldImgS = new JTextField(Config.getInstance().getString("marker.image.selected"));
		c.weightx = 1;c.gridx = 1;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 2, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		panImage.add(textFieldImgS, c);
		
		buttonImgS = new JButton("...");
		buttonImgS.setPreferredSize(new Dimension(20, 20));
		buttonImgS.setActionCommand("100");
		buttonImgS.addActionListener(ButtonImgAction);
		c.weightx = 0;c.gridx = 2;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 2, 0);
		c.fill = GridBagConstraints.NONE;
		panImage.add(buttonImgS, c);
		
		
		c.weightx = 0;c.weighty = 0;c.gridx = 0;c.gridy = 2;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 2, 5);
		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.NONE;
		panImage.add(new JLabel(Config.getLangBundle().getString("diagOptMapConfigUnknown")), c);
		
		textFieldImgU = new JTextField(Config.getInstance().getString("marker.image.unknown"));
		c.weightx = 1;c.gridx = 1;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 2, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		panImage.add(textFieldImgU, c);
		
		buttonImgU = new JButton("...");
		buttonImgU.setPreferredSize(new Dimension(20, 20));
		buttonImgU.setActionCommand("200");
		buttonImgU.addActionListener(ButtonImgAction);
		c.weightx = 0;c.gridx = 2;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 2, 0);
		c.fill = GridBagConstraints.NONE;
		panImage.add(buttonImgU, c);
		
		
		for (int i=0;i<32;i++) {
			c.weightx = 0;c.weighty = 0;c.gridx = 0;c.gridy = i+3;c.gridwidth = 1;
			c.insets = new Insets(0, 0, 2, 5);
			c.anchor = GridBagConstraints.EAST;
			c.fill = GridBagConstraints.NONE;
			panImage.add(new JLabel(String.format("%1$s %2$d:", Config.getLangBundle().getString("diagOptMapConfigType"), i)), c);
			
			textFieldImg[i] = new JTextField(Config.getInstance().getString(String.format("marker.image.bin.type.%1$d", i)));
			c.weightx = 1;c.gridx = 1;c.gridwidth = 1;
			c.insets = new Insets(0, 0, 2, 0);
			c.fill = GridBagConstraints.HORIZONTAL;
			panImage.add(textFieldImg[i], c);
			
			buttonImg[i] = new JButton("...");
			buttonImg[i].setPreferredSize(new Dimension(20, 20));
			buttonImg[i].setActionCommand(String.valueOf(i));
			buttonImg[i].addActionListener(ButtonImgAction);
			c.weightx = 0;c.gridx = 2;c.gridwidth = 1;
			c.insets = new Insets(0, 0, 2, 0);
			c.fill = GridBagConstraints.NONE;
			panImage.add(buttonImg[i], c);
		}
		
		return panImage;
    }
    
    private JPanel getPanZone() {
    	JPanel panZone = new JPanel(new GridBagLayout());
        panZone.setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.GRAY),
        		new EmptyBorder(10,5,10,5)));
        
        GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0;c.weighty = 0;c.gridx = 0;c.gridy = 0;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 0, 0);
		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.NONE;
		panZone.add(new JLabel(Config.getLangBundle().getString("diagOptMapConfigRadAngle")), c);
		
		sliderRadAngle = new JSlider(JSlider.HORIZONTAL, 1, 180, Config.getInstance().getInteger("radius.arc.angle"));
		sliderRadAngle.setMajorTickSpacing(20);
		sliderRadAngle.setMinorTickSpacing(1);
		sliderRadAngle.setPaintTicks(true);
		c.weightx = 1;c.gridx = 1;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 5, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		panZone.add(sliderRadAngle, c);
		
		textFieldRadAngle = new JTextField(String.format("%1$d°", Config.getInstance().getInteger("radius.arc.angle")));
		textFieldRadAngle.setHorizontalAlignment(JTextField.CENTER);
		textFieldRadAngle.setEditable(false);
		textFieldRadAngle.setBackground(Color.WHITE);
		c.weightx = 0;c.gridx = 2;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 0, 0);
		panZone.add(textFieldRadAngle, c);
		
		sliderRadAngle.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
			    textFieldRadAngle.setText(String.format("%1$d°", source.getValue()));
			}
		});
		
        c.weightx = 0;c.gridx = 0;c.gridwidth = 1;c.gridy = 1;
		c.insets = new Insets(0, 0, 0, 0);
		c.fill = GridBagConstraints.NONE;
		panZone.add(new JLabel(Config.getLangBundle().getString("diagOptMapConfigFactorX")), c);
		
		sliderFactorX = new JSlider(JSlider.HORIZONTAL, 1, 1000, (int)(Config.getInstance().getDouble("radius.factor.x")*100));
		sliderFactorX.setMajorTickSpacing(100);
		sliderFactorX.setMinorTickSpacing(1);
		sliderFactorX.setPaintTicks(true);
		c.weightx = 1;c.gridx = 1;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 5, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		panZone.add(sliderFactorX, c);
		
		textFieldFactorX = new JTextField(String.format("%1$.2f", Config.getInstance().getDouble("radius.factor.x")));
		textFieldFactorX.setHorizontalAlignment(JTextField.CENTER);
		textFieldFactorX.setEditable(false);
		textFieldFactorX.setBackground(Color.WHITE);
		c.weightx = 0;c.gridx = 2;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 0, 0);
		panZone.add(textFieldFactorX, c);
		
		sliderFactorX.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				textFieldFactorX.setText(String.format("%1$.2f", (double)source.getValue()/100));
			}
		});
		
        c.weightx = 0;c.gridx = 0;c.gridwidth = 1;c.gridy = 2;
		c.insets = new Insets(0, 0, 0, 0);
		c.fill = GridBagConstraints.NONE;
		panZone.add(new JLabel(Config.getLangBundle().getString("diagOptMapConfigFactorY")), c);
		
		sliderFactorY = new JSlider(JSlider.HORIZONTAL, 1, 50, (int)(Config.getInstance().getDouble("radius.factor.y")*10));
		sliderFactorY.setMajorTickSpacing(5);
		sliderFactorY.setMinorTickSpacing(1);
		sliderFactorY.setPaintTicks(true);
		c.weightx = 1;c.gridx = 1;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 5, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		panZone.add(sliderFactorY, c);
		
		textFieldFactorY = new JTextField(String.format("%1$.1f", Config.getInstance().getDouble("radius.factor.y")));
		textFieldFactorY.setHorizontalAlignment(JTextField.CENTER);
		textFieldFactorY.setEditable(false);
		textFieldFactorY.setBackground(Color.WHITE);
		c.weightx = 0;c.gridx = 2;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 0, 0);
		panZone.add(textFieldFactorY, c);
		
		sliderFactorY.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				textFieldFactorY.setText(String.format("%1$.1f", (double)source.getValue()/10));
			}
		});
		
        c.weightx = 0;c.gridx = 0;c.gridy = 3;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 0, 0);
		c.fill = GridBagConstraints.NONE;
		panZone.add(new JLabel(Config.getLangBundle().getString("diagOptMapConfigColor0")), c);
		
		buttonColor0 = new ColorButton();
		buttonColor0.setOpaque(false);
		buttonColor0.setBackground(new Color(Config.getInstance().getIntegerHexa("radius.color.argb.0"), true));
		
		textFieldColor0 = new JTextField(Config.getInstance().getString("radius.color.argb.0"));
		textFieldColor0.setHorizontalAlignment(JTextField.CENTER);
		textFieldColor0.setEditable(false);
		textFieldColor0.setBackground(Color.WHITE);
		c.weightx = 0;c.gridx = 2;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 5, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		panZone.add(textFieldColor0, c);
		
		buttonColor0.setPreferredSize(new Dimension(0, (int) textFieldColor0.getPreferredSize().getHeight()));
		buttonColor0.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DialogColorChooser dcc = new DialogColorChooser(DialogMapSettings.this, 
						buttonColor0.getBackground());
				dcc.setVisible(true);
				buttonColor0.setBackground(dcc.getColor());
				textFieldColor0.setText(String.format("%08X", dcc.getColor().getRGB()));
				dcc.dispose();
			}
		});
		
		c.weightx = 1;c.gridx = 1;c.gridwidth = 1;
		c.insets = new Insets(0, 5, 5, 5);
		panZone.add(buttonColor0, c);
		
        c.weightx = 0;c.gridx = 0;c.gridy = 4;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 0, 0);
		c.fill = GridBagConstraints.NONE;
		panZone.add(new JLabel(Config.getLangBundle().getString("diagOptMapConfigColor1")), c);
		
		buttonColor1 = new ColorButton();
		buttonColor1.setOpaque(false);
		buttonColor1.setBackground(new Color(Config.getInstance().getIntegerHexa("radius.color.argb.1"), true));
		
		textFieldColor1 = new JTextField(Config.getInstance().getString("radius.color.argb.1"));
		textFieldColor1.setHorizontalAlignment(JTextField.CENTER);
		textFieldColor1.setEditable(false);
		textFieldColor1.setBackground(Color.WHITE);
		c.weightx = 0;c.gridx = 2;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 5, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		panZone.add(textFieldColor1, c);
		
		buttonColor1.setPreferredSize(new Dimension(0, (int) textFieldColor1.getPreferredSize().getHeight()));
		buttonColor1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DialogColorChooser dcc = new DialogColorChooser(DialogMapSettings.this, 
						buttonColor1.getBackground());
				dcc.setVisible(true);
				buttonColor1.setBackground(dcc.getColor());
				textFieldColor1.setText(String.format("%08X", dcc.getColor().getRGB()));
				dcc.dispose();
			}
		});
		
		c.weightx = 1;c.gridx = 1;c.gridwidth = 1;
		c.insets = new Insets(0, 5, 5, 5);
		panZone.add(buttonColor1, c);
		
		c.weightx = 0;c.gridx = 0;c.gridy = 5;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 0, 0);
		c.fill = GridBagConstraints.NONE;
		panZone.add(new JLabel(Config.getLangBundle().getString("diagOptMapConfigColor2")), c);
		
		buttonColor2 = new ColorButton();
		buttonColor2.setOpaque(false);
		buttonColor2.setBackground(new Color(Config.getInstance().getIntegerHexa("marker.outline.selected"), true));
		
		textFieldColor2 = new JTextField(Config.getInstance().getString("marker.outline.selected"));
		textFieldColor2.setHorizontalAlignment(JTextField.CENTER);
		textFieldColor2.setEditable(false);
		textFieldColor2.setBackground(Color.WHITE);
		c.weightx = 0;c.gridx = 2;c.gridwidth = 1;
		c.insets = new Insets(0, 0, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		panZone.add(textFieldColor2, c);
		
		buttonColor2.setPreferredSize(new Dimension(0, (int) textFieldColor2.getPreferredSize().getHeight()));
		buttonColor2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DialogColorChooser dcc = new DialogColorChooser(DialogMapSettings.this, 
						buttonColor2.getBackground());
				dcc.setVisible(true);
				buttonColor2.setBackground(dcc.getColor());
				textFieldColor2.setText(String.format("%08X", dcc.getColor().getRGB()));
				dcc.dispose();
			}
		});
		
		c.weightx = 1;c.gridx = 1;c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 5);
		panZone.add(buttonColor2, c);
		
		return panZone;
    }
    
    /** This method handles events for the text field. */
    public void actionPerformed(ActionEvent e) {
        optionPane.setValue(btnString1);
    }

    /** This method reacts to state changes in the option pane. */
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();

        if (isVisible() && (e.getSource() == optionPane)
         && (JOptionPane.VALUE_PROPERTY.equals(prop) || JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object value = optionPane.getValue();

            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                return;
            }

            optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

            if (btnString1.equals(value)) {
            	saveConfigFile();
            	if (reloadListener != null)
            		reloadListener.reload();
            } else if (btnString2.equals(value)) {
            	clearAndHide();
            }
        }
    }
    
    private void saveConfigFile() {
    	// panPoiZone
    	Config.getInstance().setValue("zone.begin.count", sliderZoneBeginCount.getValue());
    	Config.getInstance().setValue("zone.size.mini", sliderZoneMinSize.getValue());
    	
    	Config.getInstance().setValue("zone.color.argb.10", String.format("%08X", buttonColor10.getBackground().getRGB()));
    	Config.getInstance().setValue("zone.color.argb.100", String.format("%08X", buttonColor100.getBackground().getRGB()));
    	Config.getInstance().setValue("zone.color.argb.1000", String.format("%08X", buttonColor1000.getBackground().getRGB()));
    	Config.getInstance().setValue("zone.color.argb.10000", String.format("%08X", buttonColor10000.getBackground().getRGB()));
    	Config.getInstance().setValue("zone.color.argb.10000+", String.format("%08X", buttonColor10000m.getBackground().getRGB()));
    	
    	Config.getInstance().setValue("zone.font", buttonZoneFont.getActionCommand());
    	
    	// panZone
    	Config.getInstance().setValue("radius.arc.angle", sliderRadAngle.getValue());
    	Config.getInstance().setValue("radius.factor.x", (double)sliderFactorX.getValue()/100);
		Config.getInstance().setValue("radius.factor.y", (double)sliderFactorY.getValue()/10);
		Config.getInstance().setValue("radius.color.argb.0", textFieldColor0.getText());
		Config.getInstance().setValue("radius.color.argb.1", textFieldColor1.getText());
		Config.getInstance().setValue("marker.outline.selected", textFieldColor2.getText());
		
		// panMap
		Config.getInstance().setValue("marker.image.selected", textFieldImgS.getText());
		Config.getInstance().setValue("marker.image.unknown", textFieldImgU.getText());
		
		for (int i=0;i<32;i++) {
			Config.getInstance().setValue(String.format("marker.image.bin.type.%1$d",i), 
					textFieldImg[i].getText());
		}
		
		// panSpeed
		Config.getInstance().setValue("marker.speed.font", buttonSpeedFont.getActionCommand());
		Config.getInstance().setValue("marker.speed.x.center", checkBoxSpeedCenter.isSelected());
		Config.getInstance().setValue("marker.speed.x", sliderSpeedX.getValue()*-1);
		Config.getInstance().setValue("marker.speed.y", sliderSpeedY.getValue()*-1);
		
		Config.getInstance().save();
    }

    /** This method clears the dialog and hides it. */
    public void clearAndHide() {
        setVisible(false);
    }
    
    public void addReloadListener(ReloadListener rl) {
    	reloadListener = rl;
    }
    
    ActionListener ButtonImgAction = new ActionListener() {
    	public void actionPerformed (java.awt.event.ActionEvent e) {
    		int val;
    		try {
    			val = Integer.parseInt(e.getActionCommand());
    		} catch (Exception ex) {
    			return;
    		}
    		
    		JFileChooser fileChooser = new JFileChooser(Config.getInstance().getString("fileChooser.images.path"));
    		fileChooser.setFileFilter(new FileFilterImage());
    		fileChooser.setDialogTitle(Config.getLangBundle().getString("diagOptMapConfigAskImg"));
    		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    		fileChooser.setMultiSelectionEnabled(false);
    		
    		int ret = fileChooser.showOpenDialog(DialogMapSettings.this);

			URI exePath = new File(System.getProperty("user.dir")).toURI();
			
    		if (ret == JFileChooser.APPROVE_OPTION) {
    			URI imgPath = fileChooser.getSelectedFile().toURI();
    			imgPath = exePath.relativize(imgPath);
    			
    			if (val == 100)
    				textFieldImgS.setText(new File(imgPath.getPath()).getPath());
    			else if (val == 200)
    				textFieldImgU.setText(new File(imgPath.getPath()).getPath());
    			else
    				textFieldImg[val].setText(new File(imgPath.getPath()).getPath());
    		} else if (ret == JFileChooser.ERROR_OPTION) {
    			JOptionPane.showMessageDialog(DialogMapSettings.this, 
    					Config.getLangBundle().getString("errorSelectFile"), Config.getLangBundle().getString("error"), 
    					JOptionPane.ERROR_MESSAGE, null);
    		}
    		
    		if (ret != JFileChooser.CANCEL_OPTION)
    		{
    			URI dirPath = fileChooser.getCurrentDirectory().toURI();
    			dirPath = exePath.relativize(dirPath);
    			
    			Config.getInstance().setValue("fileChooser.images.path", new File(dirPath.getPath()).getPath());
    		}
        }
    };
    
    public interface ReloadListener {
    	public void reload();
    }
}
