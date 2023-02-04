package spudigo.ui;

public interface MenuListener {
	public void onNew();
	public void onOpenFile();
	public void onCloseFile();
	public void onCloseAll();
	public void onSave();
	public void onSaveAs();
	public void onSaveAll();
	public void onQuit();
	
	public void onUndo();
	public void onRedo();
	public void onCopy();
	public void onCut();
	public void onPaste();
	public void onAddLine();
	public void onDelete();
	public void onDuplicate();
	public void onNextDuplicate();
	public void onClearDuplicate();
	
	public void onExport(boolean txt, boolean all);
	
	public void onToggleMap();
	public void onToggleView();
	
	public void onChangeLang(String lang, String country);
	public void onMapSettings();
	
	public void onAbout();
}
