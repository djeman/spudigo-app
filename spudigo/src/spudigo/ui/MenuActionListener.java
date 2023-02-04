package spudigo.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuActionListener implements ActionListener {
	public enum ActionName {
		New,
		Open,
		Close,
		CloseAll,
		Save,
		SaveAs,
		SaveAll,
		Quit,
		
		Undo,
		Redo,
		Copy,
		Cut,
		Paste,
		AddLine,
		Delete,
		FindDup,
		NextDup,
		ClearDup,
		
		ExportTxt,
		ExportTxtAll,
		ExportSpud,
		ExportSpudAll,
		
		MapViewToggle,
		TabViewToggle,
		
		Lang_de,
		Lang_en,
		Lang_es,
		Lang_fr,
		Lang_hu,
		Lang_it,
		Lang_ru,
		
		MapSettings,
		
		About
	}
	
	private final MenuListener menuListener;
	
	public MenuActionListener(MenuListener menuListener) {
		super();
		this.menuListener = menuListener;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (menuListener == null)
			return;

		switch (ActionName.valueOf(ae.getActionCommand())) {
			case New:
				menuListener.onNew();
				return;
			case Open:
				menuListener.onOpenFile();
				return;
			case Close:
				menuListener.onCloseFile();
				return;
			case CloseAll:
				menuListener.onCloseAll();
				return;
			case Save:
				menuListener.onSave();
				return;
			case SaveAs:
				menuListener.onSaveAs();
				return;
			case SaveAll:
				menuListener.onSaveAll();
				return;
			case Quit:
				menuListener.onQuit();
				return;
				
			case Undo:
				menuListener.onUndo();
				return;
			case Redo:
				menuListener.onRedo();
				return;
			case Copy:
				menuListener.onCopy();
				return;
			case Cut:
				menuListener.onCut();
				return;
			case Paste:
				menuListener.onPaste();
				return;
			case AddLine:
				menuListener.onAddLine();
				return;
			case Delete:
				menuListener.onDelete();
				return;
			case FindDup:
				menuListener.onDuplicate();
				return;
			case NextDup:
				menuListener.onNextDuplicate();
				return;
			case ClearDup:
				menuListener.onClearDuplicate();
				return;
			
			case ExportTxt:
				menuListener.onExport(true, false);
				return;
			case ExportTxtAll:
				menuListener.onExport(true, true);
				return;
			case ExportSpud:
				menuListener.onExport(false, false);
				return;
			case ExportSpudAll:
				menuListener.onExport(false, true);
				return;
				
			case MapViewToggle:
				menuListener.onToggleMap();
				return;
			case TabViewToggle:
				menuListener.onToggleView();
				return;
				
			case Lang_de:
				menuListener.onChangeLang("de", "DE");
				return;
			case Lang_en:
				menuListener.onChangeLang("en", "US");
				return;
			case Lang_es:
				menuListener.onChangeLang("es", "ES");
				return;
			case Lang_fr:
				menuListener.onChangeLang("fr", "FR");
				return;
			case Lang_hu:
				menuListener.onChangeLang("hu", "HU");
				return;
			case Lang_it:
				menuListener.onChangeLang("it", "IT");
				return;
			case Lang_ru:
				menuListener.onChangeLang("ru", "RU");
				return;
				
			case MapSettings:
				menuListener.onMapSettings();
				return;
				
			case About:
				menuListener.onAbout();
				return;
			default:
				return;
		}
	}
}
