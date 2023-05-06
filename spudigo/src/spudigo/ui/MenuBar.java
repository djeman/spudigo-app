package spudigo.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import spudigo.Config;
import spudigo.ui.MenuActionListener.ActionName;

public class MenuBar extends JMenuBar {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final MenuActionListener actionListener;
	private JCheckBoxMenuItem autoCenterItem;
	
	public MenuBar(MenuActionListener actionListener) {
		super();
		this.actionListener = actionListener;
		initComponent();
	}
	
	private void initComponent() {
		JMenu menu, submenu;
		JMenuItem menuItem;
		JRadioButtonMenuItem radButMenuItem;
		Font font;
		
		// File menu
		menu = new JMenu(Config.getLangBundle().getString("mbFile"));
		font = new Font(menu.getFont().getFontName(), Font.BOLD, menu.getFont().getSize());
		menu.setFont(font);
		menu.setMnemonic(KeyEvent.VK_F);
		this.add(menu);

		// File -> New
		menuItem = new JMenuItem(Config.getLangBundle().getString("tbNew"));
		menuItem.setIcon(new ImageIcon(getClass().getResource("/spudigo/images/icon16_new.png")));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		menuItem.setActionCommand(ActionName.New.toString());
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
		
		// File -> Open
		menuItem = new JMenuItem(Config.getLangBundle().getString("tbOpen"));
		menuItem.setIcon(new ImageIcon(getClass().getResource("/spudigo/images/icon16_open.png")));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		menuItem.setActionCommand(ActionName.Open.toString());
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
		
		menu.addSeparator();
				
		// File -> Close
		menuItem = new JMenuItem(Config.getLangBundle().getString("tbClose"));
		menuItem.setIcon(new ImageIcon(getClass().getResource("/spudigo/images/icon16_close.png")));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		menuItem.setActionCommand(ActionName.Close.toString());
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
			
		// File -> Close All
		menuItem = new JMenuItem(Config.getLangBundle().getString("mbCloseAll"));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK|ActionEvent.SHIFT_MASK));
		menuItem.setActionCommand(ActionName.CloseAll.toString());
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
		
		menu.addSeparator();
		
		// File -> Save
		menuItem = new JMenuItem(Config.getLangBundle().getString("tbSave"));
		menuItem.setIcon(new ImageIcon(getClass().getResource("/spudigo/images/icon16_save.png")));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		menuItem.setActionCommand(ActionName.Save.toString());
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
		
		// File -> Save As...
		menuItem = new JMenuItem(Config.getLangBundle().getString("tbSaveAs"));
		menuItem.setIcon(new ImageIcon(getClass().getResource("/spudigo/images/icon16_saveas.png")));
		menuItem.setActionCommand(ActionName.SaveAs.toString());
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
					
		// File -> Save All
		menuItem = new JMenuItem(Config.getLangBundle().getString("mbSaveAll"));
		menuItem.setIcon(new ImageIcon(getClass().getResource("/spudigo/images/icon16_saveall.png")));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK|ActionEvent.SHIFT_MASK));
		menuItem.setActionCommand(ActionName.SaveAll.toString());
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
		
		menu.addSeparator();
		
		// File -> Save View
		menuItem = new JMenuItem(Config.getLangBundle().getString("tbSaveView"));
		menuItem.setIcon(new ImageIcon(getClass().getResource("/spudigo/images/icon16_saveas.png")));
		menuItem.setActionCommand(ActionName.SaveView.toString());
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
		
		menu.addSeparator();
		
		// File -> Quit
		menuItem = new JMenuItem(Config.getLangBundle().getString("mbExit"));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
		menuItem.setActionCommand(ActionName.Quit.toString());
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
		
		// Edit menu
		menu = new JMenu(Config.getLangBundle().getString("mbEdit"));
		menu.setFont(font);
		menu.setMnemonic(KeyEvent.VK_E);
		this.add(menu);
				
		// Edit -> Undo
		menuItem = new JMenuItem(Config.getLangBundle().getString("tbUndo"));
		menuItem.setIcon(new ImageIcon(getClass().getResource("/spudigo/images/icon16_undo.png")));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		menuItem.setActionCommand(ActionName.Undo.toString());
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
						
		// Edit -> Redo
		menuItem = new JMenuItem(Config.getLangBundle().getString("tbRedo"));
		menuItem.setIcon(new ImageIcon(getClass().getResource("/spudigo/images/icon16_redo.png")));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
		menuItem.setActionCommand(ActionName.Redo.toString());
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
		
		menu.addSeparator();
		
		// Edit -> Copy
		menuItem = new JMenuItem(Config.getLangBundle().getString("tbCopy"));
		menuItem.setIcon(new ImageIcon(getClass().getResource("/spudigo/images/icon16_copy.png")));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		menuItem.setActionCommand(ActionName.Copy.toString());
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
		
		// Edit -> Cut
		menuItem = new JMenuItem(Config.getLangBundle().getString("tbCut"));
		menuItem.setIcon(new ImageIcon(getClass().getResource("/spudigo/images/icon16_cut.png")));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		menuItem.setActionCommand(ActionName.Cut.toString());
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
		
		// Edit -> Paste
		menuItem = new JMenuItem(Config.getLangBundle().getString("tbPaste"));
		menuItem.setIcon(new ImageIcon(getClass().getResource("/spudigo/images/icon16_paste.png")));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		menuItem.setActionCommand(ActionName.Paste.toString());
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
		
		menu.addSeparator();
		
		// Edit -> Addline
		menuItem = new JMenuItem(Config.getLangBundle().getString("tbAddLine"));
		menuItem.setIcon(new ImageIcon(getClass().getResource("/spudigo/images/icon16_add.png")));
		menuItem.setActionCommand(ActionName.AddLine.toString());
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
		
		// Edit -> Delete
		menuItem = new JMenuItem(Config.getLangBundle().getString("tbDelete"));
		menuItem.setIcon(new ImageIcon(getClass().getResource("/spudigo/images/icon16_del.png")));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		menuItem.setActionCommand(ActionName.Delete.toString());
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
		
		menu.addSeparator();
		
		// Edit -> Find Duplicates
		menuItem = new JMenuItem(Config.getLangBundle().getString("mbDupSearch"));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
		menuItem.setActionCommand(ActionName.FindDup.toString());
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
		
		// Edit -> Search Next Duplicate
		menuItem = new JMenuItem(Config.getLangBundle().getString("mbDupNext"));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
		menuItem.setActionCommand(ActionName.NextDup.toString());
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
		
		// Edit -> Clear Search Duplicate
		menuItem = new JMenuItem(Config.getLangBundle().getString("mbDupClear"));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK|ActionEvent.SHIFT_MASK));
		menuItem.setActionCommand(ActionName.ClearDup.toString());
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
		
		// Export menu
		menu = new JMenu(Config.getLangBundle().getString("mbExport"));
		menu.setFont(font);
		menu.setMnemonic(KeyEvent.VK_P);
		this.add(menu);
		
		// Export -> Export txt
		menuItem = new JMenuItem(Config.getLangBundle().getString("exportTxt"));
		menuItem.setIcon(new ImageIcon(getClass().getResource("/spudigo/images/icon16_export_txt.png")));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
		menuItem.setActionCommand(ActionName.ExportTxt.toString());
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
		
		// Export -> Export all txt
		menuItem = new JMenuItem(Config.getLangBundle().getString("mbExportAllTxt"));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK|ActionEvent.SHIFT_MASK));
		menuItem.setActionCommand(ActionName.ExportTxtAll.toString());
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
		
		menu.addSeparator();
				
		// Export -> Export spud
		menuItem = new JMenuItem(Config.getLangBundle().getString("exportSpud"));
		menuItem.setIcon(new ImageIcon(getClass().getResource("/spudigo/images/icon16_export_spud.png")));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		menuItem.setActionCommand(ActionName.ExportSpud.toString());
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
		
		// Export -> Export all spud
		menuItem = new JMenuItem(Config.getLangBundle().getString("mbExportAllSpud"));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK|ActionEvent.SHIFT_MASK));
		menuItem.setActionCommand(ActionName.ExportSpudAll.toString());
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
		
		// Options menu
		menu = new JMenu(Config.getLangBundle().getString("mbOptions"));
		menu.setFont(font);
		menu.setMnemonic(KeyEvent.VK_O);
		this.add(menu);

		// Options -> Menus Languages
		submenu = new JMenu(Config.getLangBundle().getString("mbLanguageSetting"));
		submenu.setMnemonic(KeyEvent.VK_L);
		
		// LangGroup for radio buttons
	    ButtonGroup langGroup = new ButtonGroup();
	    String loca = Config.getInstance().getCurrentLocale().getLanguage();
	    boolean enSelected = !(loca.equals(new Locale("de").getLanguage()) ||
	    		loca.equals(new Locale("es").getLanguage()) ||
	    		loca.equals(new Locale("fr").getLanguage()) ||
	    		loca.equals(new Locale("hu").getLanguage()) ||
	    		loca.equals(new Locale("it").getLanguage()) ||
	    		loca.equals(new Locale("ru").getLanguage())); 
	    
		// German
		radButMenuItem = new JRadioButtonMenuItem(Config.getLangBundle().getString("german"));
		radButMenuItem.setSelected(loca.equals(new Locale("de").getLanguage()));
		radButMenuItem.setActionCommand(ActionName.Lang_de.toString());
		radButMenuItem.addActionListener(actionListener);
		submenu.add(radButMenuItem);
		langGroup.add(radButMenuItem);
		// English
		radButMenuItem = new JRadioButtonMenuItem(Config.getLangBundle().getString("english"));
		radButMenuItem.setSelected(enSelected);
		radButMenuItem.setActionCommand(ActionName.Lang_en.toString());
		radButMenuItem.addActionListener(actionListener);
		submenu.add(radButMenuItem);
		langGroup.add(radButMenuItem);
		// Spanish
		radButMenuItem = new JRadioButtonMenuItem(Config.getLangBundle().getString("spanish"));
		radButMenuItem.setSelected(loca.equals(new Locale("es").getLanguage()));
		radButMenuItem.setActionCommand(ActionName.Lang_es.toString());
		radButMenuItem.addActionListener(actionListener);
		submenu.add(radButMenuItem);
		langGroup.add(radButMenuItem);
		// French
		radButMenuItem = new JRadioButtonMenuItem(Config.getLangBundle().getString("french"));
		radButMenuItem.setSelected(loca.equals(new Locale("fr").getLanguage()));
		radButMenuItem.setActionCommand(ActionName.Lang_fr.toString());
		radButMenuItem.addActionListener(actionListener);
		submenu.add(radButMenuItem);
		langGroup.add(radButMenuItem);
		// Hungarian
		radButMenuItem = new JRadioButtonMenuItem(Config.getLangBundle().getString("hungarian"));
		radButMenuItem.setSelected(loca.equals(new Locale("hu").getLanguage()));
		radButMenuItem.setActionCommand(ActionName.Lang_hu.toString());
		radButMenuItem.addActionListener(actionListener);
		submenu.add(radButMenuItem);
		langGroup.add(radButMenuItem);
		// Italian
		radButMenuItem = new JRadioButtonMenuItem(Config.getLangBundle().getString("italian"));
		radButMenuItem.setSelected(loca.equals(new Locale("it").getLanguage()));
		radButMenuItem.setActionCommand(ActionName.Lang_it.toString());
		radButMenuItem.addActionListener(actionListener);
		submenu.add(radButMenuItem);
		langGroup.add(radButMenuItem);
		// Russian
		radButMenuItem = new JRadioButtonMenuItem(Config.getLangBundle().getString("russian"));
		radButMenuItem.setSelected(loca.equals(new Locale("ru").getLanguage()));
		radButMenuItem.setActionCommand(ActionName.Lang_ru.toString());
		radButMenuItem.addActionListener(actionListener);
		submenu.add(radButMenuItem);
		langGroup.add(radButMenuItem);
		
		// Language submenu		
		menu.add(submenu);
		
		// Options -> Map Settings
		menuItem = new JMenuItem(Config.getLangBundle().getString("mbMapSettings"));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		menuItem.setActionCommand(ActionName.MapSettings.toString());
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
		
		menu.addSeparator();

		// Options -> Map Select Center
		autoCenterItem = new JCheckBoxMenuItem(Config.getLangBundle().getString("mbMapSelectCenter"));
		autoCenterItem.setSelected(Config.getInstance().getBoolean("spudmap.autocenter.selection"));
		menu.add(autoCenterItem);
		
		// ? menu
		menu = new JMenu("?");
		menu.setFont(font);
		menu.setMnemonic(KeyEvent.VK_COMMA);
		this.add(menu);
				
		// ? -> About
		menuItem = new JMenuItem(Config.getLangBundle().getString("aboutTitle"));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		menuItem.setActionCommand(ActionName.About.toString());
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
	}
	
	public boolean isAutoCenterMap() {
		return autoCenterItem.isSelected();
	}
}
