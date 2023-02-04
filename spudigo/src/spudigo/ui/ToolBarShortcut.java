package spudigo.ui;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import spudigo.Config;
import spudigo.ui.MenuActionListener.ActionName;

public class ToolBarShortcut extends JToolBar {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final MenuActionListener actionListener;
	
	public ToolBarShortcut(MenuActionListener actionListener) {
		super();
		this.actionListener = actionListener;
		initComponent();
	}
	
	private void initComponent() {
		JButton button;
		
		// File -> New
		button = new JButton(new ImageIcon(getClass().getResource("/spudigo/images/icon16_new.png")));
		button.setFocusable(false);
		button.setToolTipText(Config.getLangBundle().getString("tbNew"));
		button.setActionCommand(ActionName.New.toString());
		button.addActionListener(actionListener);
		this.add(button);
		
		// File -> Open
		button = new JButton(new ImageIcon(getClass().getResource("/spudigo/images/icon16_open.png")));
		button.setFocusable(false);
		button.setToolTipText(Config.getLangBundle().getString("tbOpen"));
		button.setActionCommand(ActionName.Open.toString());
		button.addActionListener(actionListener);
		this.add(button);
		
		// File -> Save
		button = new JButton(new ImageIcon(getClass().getResource("/spudigo/images/icon16_save.png")));
		button.setFocusable(false);
		button.setToolTipText(Config.getLangBundle().getString("tbSave"));
		button.setActionCommand(ActionName.Save.toString());
		button.addActionListener(actionListener);
		this.add(button);
				
		// File -> Close
		button = new JButton(new ImageIcon(getClass().getResource("/spudigo/images/icon16_close.png")));
		button.setFocusable(false);
		button.setToolTipText(Config.getLangBundle().getString("tbClose"));
		button.setActionCommand(ActionName.Close.toString());
		button.addActionListener(actionListener);
		this.add(button);
		
		this.addSeparator();
		
		// Edit -> Undo
		button = new JButton(new ImageIcon(getClass().getResource("/spudigo/images/icon16_undo.png")));
		button.setFocusable(false);
		button.setToolTipText(Config.getLangBundle().getString("tbUndo"));
		button.setActionCommand(ActionName.Undo.toString());
		button.addActionListener(actionListener);
		this.add(button);
						
		// Edit -> Redo
		button = new JButton(new ImageIcon(getClass().getResource("/spudigo/images/icon16_redo.png")));
		button.setFocusable(false);
		button.setToolTipText(Config.getLangBundle().getString("tbRedo"));
		button.setActionCommand(ActionName.Redo.toString());
		button.addActionListener(actionListener);
		this.add(button);
				
		this.addSeparator();
								
		// Edit -> Copy
		button = new JButton(new ImageIcon(getClass().getResource("/spudigo/images/icon16_copy.png")));
		button.setFocusable(false);
		button.setToolTipText(Config.getLangBundle().getString("tbCopy"));
		button.setActionCommand(ActionName.Copy.toString());
		button.addActionListener(actionListener);
		this.add(button);
		
		// Edit -> Cut
		button = new JButton(new ImageIcon(getClass().getResource("/spudigo/images/icon16_cut.png")));
		button.setFocusable(false);
		button.setToolTipText(Config.getLangBundle().getString("tbCut"));
		button.setActionCommand(ActionName.Cut.toString());
		button.addActionListener(actionListener);
		this.add(button);
				
		// Edit -> Paste
		button = new JButton(new ImageIcon(getClass().getResource("/spudigo/images/icon16_paste.png")));
		button.setFocusable(false);
		button.setToolTipText(Config.getLangBundle().getString("tbPaste"));
		button.setActionCommand(ActionName.Paste.toString());
		button.addActionListener(actionListener);
		this.add(button);
		
		this.addSeparator();
		
		// Edit -> Addline
		button = new JButton(new ImageIcon(getClass().getResource("/spudigo/images/icon16_add.png")));
		button.setFocusable(false);
		button.setToolTipText(Config.getLangBundle().getString("tbAddLine"));
		button.setActionCommand(ActionName.AddLine.toString());
		button.addActionListener(actionListener);
		this.add(button);
								
		// Edit -> Delete
		button = new JButton(new ImageIcon(getClass().getResource("/spudigo/images/icon16_del.png")));
		button.setFocusable(false);
		button.setToolTipText(Config.getLangBundle().getString("tbDelete"));
		button.setActionCommand(ActionName.Delete.toString());
		button.addActionListener(actionListener);
		this.add(button);
		
		this.addSeparator();
		
		// Export -> Export txt
		button = new JButton(new ImageIcon(getClass().getResource("/spudigo/images/icon16_export_txt.png")));
		button.setFocusable(false);
		button.setToolTipText(Config.getLangBundle().getString("exportTxt"));
		button.setActionCommand(ActionName.ExportTxt.toString());
		button.addActionListener(actionListener);
		this.add(button);
						
		// Export -> Export spud
		button = new JButton(new ImageIcon(getClass().getResource("/spudigo/images/icon16_export_spud.png")));
		button.setFocusable(false);
		button.setToolTipText(Config.getLangBundle().getString("exportSpud"));
		button.setActionCommand(ActionName.ExportSpud.toString());
		button.addActionListener(actionListener);
		this.add(button);
		
		this.add(Box.createHorizontalGlue());
		this.add(Box.createVerticalGlue());
		this.addSeparator();
		
		// Map - Toggle
		button = new JButton(new ImageIcon(getClass().getResource("/spudigo/images/icon16_map.png")));
		button.setFocusable(false);
		button.setToolTipText(Config.getLangBundle().getString("tbMapToggle"));
		button.setActionCommand(ActionName.MapViewToggle.toString());
		button.addActionListener(actionListener);
		this.add(button);
		
		this.addSeparator();
		
		// View - Toggle
		button = new JButton(new ImageIcon(getClass().getResource("/spudigo/images/icon16_view.png")));
		button.setFocusable(false);
		button.setToolTipText(Config.getLangBundle().getString("tbViewToggle"));
		button.setActionCommand(ActionName.TabViewToggle.toString());
		button.addActionListener(actionListener);
		this.add(button);
		
		this.addSeparator();
	}
}
