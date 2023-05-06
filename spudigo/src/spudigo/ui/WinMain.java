package spudigo.ui;

import java.awt.BorderLayout;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyEventPostProcessor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

import spudigo.Config;
import spudigo.HaversineAlgorithm;
import spudigo.LoadTemplates;
import spudigo.LoadTemplates.LoadTemplatesListener;
import spudigo.OSMRoadThread;
import spudigo.OSMRoadThread.OSMRoadListener;
import spudigo.OSMRoadThread.OSMRoadRes;
import spudigo.ParseFile;
import spudigo.ParseFile.ParseFileListener;
import spudigo.SaveFile;
import spudigo.SaveFile.SaveFileListener;
import spudigo.SpudItem;
import spudigo.TemplateFile;
import spudigo.ui.ToolBarModel.ToolBarModelListener;
import spudigo.ui.dialog.DialogDuplicateSearch;
import spudigo.ui.dialog.DialogExportChoice;
import spudigo.ui.dialog.DialogMapSettings;
import spudigo.ui.dialog.DialogMapSettings.ReloadListener;
import spudigo.ui.dialog.DialogResult;
import spudigo.ui.filefilter.FileFilterSpud;
import spudigo.ui.filefilter.FileFilterTxt;
import spudigo.ui.filefilter.FileFilterTxtSpud;
import spudigo.ui.map.SpudMap;
import spudigo.ui.map.SpudMap.PanelMapListener;
import spudigo.ui.table.SpudScroll;
import spudigo.ui.table.SpudTable;
import spudigo.ui.table.SpudTableModel;
import spudigo.ui.table.SpudTable.SpudTableListener;
import spudigo.ui.table.SpudTableModel.ValueChangedListener;

public class WinMain extends JFrame implements MenuListener, ParseFileListener, ToolBarModelListener, 
							LoadTemplatesListener, SaveFileListener, SpudTableListener, PanelMapListener, OSMRoadListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final JTabbedPane tabbedPane = new JTabbedPane();
	private final StatusBar statusBar = new StatusBar();
	private final MenuActionListener menuActionListener = new MenuActionListener(this);
	private final ToolBarShortcut toolBarShortcut = new ToolBarShortcut(menuActionListener);
	private final MenuBar menuBar = new MenuBar(menuActionListener);
	private final ToolBarModel toolBarModel = new ToolBarModel(this);
	private final ArrayList<SpudItem> copyRow = new ArrayList<SpudItem>();
	private final JSplitPane splitPane = new JSplitPane(Config.getInstance().getInteger("splitpane.divider.orientation"));
	private final Lock l = new ReentrantLock();
	
	private WatchService modelWatcher = null;
	private Hashtable<String, TemplateFile> templates = null;
	private int tableUID = 0;
	private StringBuilder tmpMesSave = null;
	
	private SpudMap panelMap = null;
	private boolean mapEnabled = Config.getInstance().getBoolean("panelmap.enabled");
		
	private Point mouseOri = null;

	public WinMain() {
		this.setTitle(spudigo.Main.version);
		this.setSize(Config.getInstance().getInteger("winmain.width"), 
				Config.getInstance().getInteger("winmain.height"));
		this.setResizable(true);
		this.setJMenuBar(menuBar);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		KeyEventPostProcessor kepp = new KeyEventPostProcessor() {
		    public boolean postProcessKeyEvent(KeyEvent evt) {
		    	if (evt.getID() == KeyEvent.KEY_RELEASED) {
		    		switch (evt.getKeyCode()) {
				    	case KeyEvent.VK_ALT:
				    	case KeyEvent.VK_SHIFT:
							mouseOri = null;
				    		break;
			    	}
		    	}
		    	
		        return false;
		    }
		};

		DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().
		    addKeyEventPostProcessor(kepp);
		
		if (Config.getInstance().getString("winmain.x") != null || 
				Config.getInstance().getString("winmain.y") != null) {
			this.setLocation(Config.getInstance().getInteger("winmain.x"), 
					Config.getInstance().getInteger("winmain.y"));
		} else {
			this.setLocationRelativeTo(null);
		}
		
		if (Config.getInstance().getBoolean("winmain.fullscreen"))
			this.setExtendedState(Frame.MAXIMIZED_BOTH);
		
		new LoadTemplates(this);
		
		try {
			this.setIconImage(ImageIO.read(getClass().getResourceAsStream("/spudigo/images/ganzoo.gif")));
		} catch (IOException e) {}
		
		this.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	quitClose();
		    }
		});
		
		tabbedPane.setTabLayoutPolicy(Config.getInstance().getInteger("tabbedpane.tablayoutpolicy"));
		splitPane.add(tabbedPane);
		splitPane.setOneTouchExpandable(true);
		
		JPanel pan = new JPanel();
		final BorderLayout panLayout = new BorderLayout();
		pan.setLayout(panLayout);
		pan.add(splitPane, BorderLayout.CENTER);
		
		toolBarModel.setOrientation(Config.getInstance().getInteger("toolbar.template.orientation"));
		toolBarModel.setComboInValue(Config.getInstance().getString("toolbar.template.input"));
		toolBarModel.setComboOutValue(Config.getInstance().getString("toolbar.template.output"));
		pan.add(toolBarModel, Config.getInstance().getString("toolbar.template.position"));
		
		toolBarModel.addHierarchyListener(new HierarchyListener() {
			public void hierarchyChanged(HierarchyEvent e) {
				Window window = SwingUtilities.getWindowAncestor(toolBarModel);
				if (window == WinMain.this) {
					if (toolBarModel == panLayout.getLayoutComponent(BorderLayout.NORTH))
						Config.getInstance().setValue("toolbar.template.position", BorderLayout.NORTH);
					else if (toolBarModel == panLayout.getLayoutComponent(BorderLayout.SOUTH))
						Config.getInstance().setValue("toolbar.template.position", BorderLayout.SOUTH);
					else if (toolBarModel == panLayout.getLayoutComponent(BorderLayout.EAST))
						Config.getInstance().setValue("toolbar.template.position", BorderLayout.EAST);
					else if (toolBarModel == panLayout.getLayoutComponent(BorderLayout.WEST))
						Config.getInstance().setValue("toolbar.template.position", BorderLayout.WEST);
					
					Config.getInstance().setValue("toolbar.template.orientation", toolBarModel.getOrientation());
				} else if (window != null) {
					toolBarModel.setOrientation(JToolBar.HORIZONTAL);
				}
			}
		});
		
		final BorderLayout cpLayout = new BorderLayout();
		this.getContentPane().setLayout(cpLayout);
		
		toolBarShortcut.setOrientation(Config.getInstance().getInteger("toolbar.menu.orientation"));
		this.getContentPane().add(toolBarShortcut, Config.getInstance().getString("toolbar.menu.position"));
		this.getContentPane().add(pan, BorderLayout.CENTER);
		this.getContentPane().add(statusBar, BorderLayout.PAGE_END);
		
		toolBarShortcut.addHierarchyListener(new HierarchyListener() {
			public void hierarchyChanged(HierarchyEvent e) {
				Window window = SwingUtilities.getWindowAncestor(toolBarShortcut);
				if (window == WinMain.this) {
					if (toolBarShortcut == cpLayout.getLayoutComponent(BorderLayout.NORTH))
						Config.getInstance().setValue("toolbar.menu.position", BorderLayout.NORTH);
					else if (toolBarShortcut == cpLayout.getLayoutComponent(BorderLayout.SOUTH))
						Config.getInstance().setValue("toolbar.menu.position", BorderLayout.SOUTH);
					else if (toolBarShortcut == cpLayout.getLayoutComponent(BorderLayout.EAST))
						Config.getInstance().setValue("toolbar.menu.position", BorderLayout.EAST);
					else if (toolBarShortcut == cpLayout.getLayoutComponent(BorderLayout.WEST))
						Config.getInstance().setValue("toolbar.menu.position", BorderLayout.WEST);
					
					Config.getInstance().setValue("toolbar.menu.orientation", toolBarShortcut.getOrientation());
				} else if (window != null) {
					toolBarShortcut.setOrientation(JToolBar.HORIZONTAL);
				}
			}
		});
		
		new FileDrop(this, new FileDrop.Listener() {
			public void  filesDropped(File[] files) {   
				loadNewFiles(files);
			}
		});
		
		tabbedPane.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				SpudTable table = getCurrentTab();
				if (table != null) {
					enableMap(true);
					
					if (panelMap != null) {
						panelMap.setSelected(null, false);
						panelMap.setPoiArray(table.getData());
						panelMap.clearAllRoads();
					}
					
					statusBar.setStatus(String.format(Config.getLangBundle().getString("statusRowNumbers"), table.getRowCount()));
					statusBar.setDistance("");
					toolBarModel.setComboInValue(table.getNameModelIn());
					//toolBarModel.setComboOutValue(table.getNameModelOut());
					
					if (table.getFilePath() == null)
						WinMain.this.setTitle(spudigo.Main.version + " - new" + tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
					else
						WinMain.this.setTitle(spudigo.Main.version + " - " + table.getFilePath());
				} else {
					enableMap(false);
					
					statusBar.setStatus(Config.getLangBundle().getString("noFileOpen"));
					statusBar.setDistance("");
					WinMain.this.setTitle(spudigo.Main.version);
				}
			}
		});
	}
	
	private SpudTable getCurrentTab() {
		if (tabbedPane.getTabCount() > 0 && tabbedPane.getSelectedIndex() != -1) {
			SpudScroll scroll = (SpudScroll)tabbedPane.getSelectedComponent();
			return scroll.getTable();
		}
		
		return null;
	}
	
	private synchronized void enableMap(boolean value) {
		if (mapEnabled && value && panelMap == null) {
			panelMap = new SpudMap(this, templates.get(toolBarModel.getComboInValue()));
			panelMap.getMainMap().addMouseMotionListener(MouseEditMotion);
			splitPane.add(panelMap);
			splitPane.setDividerLocation(Config.getInstance().getInteger("splitpane.divider.location"));
		} else if (!value && panelMap != null) {
			splitPane.remove(2);
			panelMap.dispose();
			panelMap = null;
		}
	}

	private File[] selectFiles() {
		JFileChooser fileChooser = new JFileChooser(Config.getInstance().getString("fileChooser.default.path"));
		fileChooser.setFileFilter(new FileFilterTxt());
		fileChooser.setFileFilter(new FileFilterSpud());
		fileChooser.setFileFilter(new FileFilterTxtSpud());
		
		fileChooser.setDialogTitle(Config.getLangBundle().getString("selectFilesToOpen"));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setSelectedFile(null);
		
		int ret = fileChooser.showOpenDialog(this);
		
		File[] files = null;
		if (ret == JFileChooser.APPROVE_OPTION) {
			files = fileChooser.getSelectedFiles();
			if (files == null || files.length < 1) {
				files = new File[1];
				files[0] = fileChooser.getSelectedFile();
			}
			
			Config.getInstance().setValue("fileChooser.default.path", fileChooser.getCurrentDirectory().getAbsolutePath());
		} else if (ret == JFileChooser.ERROR_OPTION) { 
			JOptionPane.showMessageDialog(WinMain.this, 
					Config.getLangBundle().getString("fileNotFound"), Config.getLangBundle().getString("error"), 
					JOptionPane.ERROR_MESSAGE, null);
		}
		
		return files;
	}
	
	private File selectNewFile(String newFileName, boolean txt, boolean export) {
		JFileChooser fileChooser = new JFileChooser(Config.getInstance().getString("fileChooser.default.path"));
		fileChooser.setFileFilter(new FileFilterTxt());
		fileChooser.setFileFilter(new FileFilterSpud());
		fileChooser.setFileFilter(new FileFilterTxtSpud());
		
		if (export) {
			fileChooser.setDialogTitle(txt?Config.getLangBundle().getString("exportTxt"):Config.getLangBundle().getString("exportSpud"));
		} else {
			fileChooser.setDialogTitle(txt?Config.getLangBundle().getString("saveTxt"):Config.getLangBundle().getString("saveSpud"));
		}
		
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setCurrentDirectory(new File(newFileName));
		
		int indexSeparator = newFileName.lastIndexOf(File.separator)+1;
		String name = newFileName.substring(indexSeparator, 
				(newFileName.lastIndexOf(".") > indexSeparator)?newFileName.lastIndexOf("."):newFileName.length());
		fileChooser.setSelectedFile(new File(name + (txt?".txt":".spud")));
		fileChooser.setMultiSelectionEnabled(false);
		
		int ret = fileChooser.showSaveDialog(this);
		File res = null;
		
		if (ret == JFileChooser.APPROVE_OPTION) {
			res = fileChooser.getSelectedFile();
			Config.getInstance().setValue("fileChooser.default.path", fileChooser.getCurrentDirectory().getAbsolutePath());
		} else if (ret == JFileChooser.ERROR_OPTION) {
			JOptionPane.showMessageDialog(WinMain.this, 
					Config.getLangBundle().getString("errorSelectFile"), Config.getLangBundle().getString("error"), 
					JOptionPane.ERROR_MESSAGE, null);
		}
		
		return res;
	}
	
	private void loadNewFiles(File[] selectedFiles) {
		if (selectedFiles != null && selectedFiles.length > 0) {
			ExecutorService exe = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 8);
			
			for (File f:selectedFiles) {
				if (f != null && f.canRead())
					exe.execute(new ParseFile(f, this));
			}
		}
	}
	
	private void createNewTab(ArrayList<SpudItem> data, String path, boolean isTxt) {
		final SpudTable table = new SpudTable(path, 
				toolBarModel.getComboInValue(),
				toolBarModel.getComboOutValue(),
				this, 
				new SpudTableModel(data, templates.get(toolBarModel.getComboInValue()), isTxt));
		
		SpudScroll scrollPane = new SpudScroll(table);
		tabbedPane.add(String.valueOf(tableUID), scrollPane);
		
		int index = tabbedPane.indexOfComponent(scrollPane);
		
		if (path == null)
			createTitleNewTab(index, String.format("new%1$d.txt", tableUID));
		else
			createTitleNewTab(index, path);
		
		tableUID++;
		
		tabbedPane.setSelectedIndex(index);
		
		table.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {
				if (!e.isControlDown()) {
					int r = table.rowAtPoint(e.getPoint());
					if ((r < 0 || r >= table.getRowCount()))
						table.clearSelection();
					else if (SwingUtilities.isRightMouseButton(e)) {
						boolean selected = false; 
						for (int i : table.getSelectedRows()) {
					        if (i == r) {
					            selected = true;
					            break;
					        }
					    }
						
						if (!selected)
							table.setRowSelectionInterval(r, r);
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {		        
		        if (e.isPopupTrigger() && e.getComponent() instanceof SpudTable ) {
		        	JPopupMenu popup = table.genRowPopupMenu(table.getSelectedRows(), copyRow, mapEnabled);
		        	popup.show(e.getComponent(), e.getX(), e.getY());
		        }
			}
		});
		
		table.addValueChangedListener(new ValueChangedListener() {
			@Override
			public void onValueChanged(int row, int col) {
				SpudTable table = getCurrentTab();
				if (table != null) {
					setTabTitleModified(true);
					setMapSelected(false);
					
					if (panelMap != null)
						panelMap.setPoiArray(table.getData());
				}
			}
		});
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
		    public void valueChanged(ListSelectionEvent lse) {
		        if (!lse.getValueIsAdjusting()) {
		        	setMapSelected(true);
		        	
		        	if (table.getSelectedRowCount() == 2) {
		        		SpudItem[] selected = new SpudItem[2];
		        		GeoPosition[] gSelected = new GeoPosition[2];
		        		int[] selectedIndex = table.getSelectedRows();
		        		for (int i=0;i<2;i++) {
							selected[i] = table.getData().get(table.getRowSorter().convertRowIndexToModel(selectedIndex[i]));
							gSelected[i] = table.getData().get(table.getRowSorter().convertRowIndexToModel(selectedIndex[i])).getPosition();
						}
		        		
		        		statusBar.setDistance(String.format("%1$.1f", HaversineAlgorithm.HaversineInM(
		        				gSelected[0].getLatitude(), gSelected[0].getLongitude(),
		        				gSelected[1].getLatitude(), gSelected[1].getLongitude())));
		        		
		        		if (panelMap != null)
		        			panelMap.setRoadDirect(gSelected);
		        		
		        		new OSMRoadThread(gSelected, WinMain.this).start();
		        	} else {
		        		statusBar.setDistance("");
		        		if (panelMap != null)
		        			panelMap.clearAllRoads();
		        	}
		        }
		    }
		});
		
		table.addMouseMotionListener(MouseEditMotion);
		
		if (panelMap != null)
			panelMap.centerOnPoiArray(table.getData());
	}
	
	@Override
	public void onOSMRoadResult(OSMRoadRes toGo, OSMRoadRes toBack) {
		statusBar.addRoadDistance(toGo.dist, toBack.dist);
		
		if (panelMap != null)
			panelMap.setRoadOSM(toGo.road, toBack.road);
	}
	
	private void setMapSelected(boolean repaint) {
		SpudTable table = getCurrentTab();
    	if (panelMap != null && table != null) {
			int rowIndex = table.getSelectedRow();
			
			if (rowIndex > -1) {
				SpudItem[] selected = new SpudItem[table.getSelectedRows().length];
				GeoPosition[] gSelected = new GeoPosition[table.getSelectedRows().length];
				int[] selectedIndex = table.getSelectedRows();
				for (int i=0;i<selectedIndex.length;i++) {
					selected[i] = table.getData().get(table.getRowSorter().convertRowIndexToModel(selectedIndex[i]));
					gSelected[i] = table.getData().get(table.getRowSorter().convertRowIndexToModel(selectedIndex[i])).getPosition();
				}
				
				panelMap.setSelected(selected, repaint);
				
				if (menuBar.isAutoCenterMap())
					panelMap.centerOnPoiSelected(gSelected);
			} else {
				panelMap.setSelected(null, repaint);
			}
    	}
	}
	
	private void setTabTitleModified(boolean enable) {
		JPanel pan = (JPanel) tabbedPane.getTabComponentAt(tabbedPane.getSelectedIndex());
		JLabel lab = (JLabel) pan.getComponent(0);
		lab.setText((enable)?"*":"");
	}

	private void createTitleNewTab(int index, String title) {
		JPanel pnlTab = new JPanel(new GridBagLayout());
		pnlTab.setOpaque(false);
		
		String filename = title.substring(title.lastIndexOf(File.separator)+1,title.length());
		JLabel lblTitle = new JLabel(filename);
		JLabel lblSave = new JLabel();
		JButton btnClose = new JButton("X");
		btnClose.setActionCommand(String.valueOf(tableUID));
		btnClose.setBorderPainted(false);
		btnClose.setOpaque(false);
		btnClose.setContentAreaFilled(false);;
		btnClose.setMargin(new Insets(0,10,0,0));
		btnClose.addActionListener(TabCloseFile);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;

		pnlTab.add(lblSave, gbc);
		
		gbc.gridx++;
		gbc.weightx = 1;

		pnlTab.add(lblTitle, gbc);

		gbc.gridx++;
		gbc.weightx = 0;
		pnlTab.add(btnClose, gbc);

		tabbedPane.setTabComponentAt(index, pnlTab);
	}
	
	private void quitClose() {
		int posSep = 0;
		if (splitPane.getComponentCount() > 2)
			posSep = splitPane.getDividerLocation();
		
		if (!this.closeAllTab())
			return;
		
		Config cfg = Config.getInstance();
		if (this.getExtendedState() == Frame.MAXIMIZED_BOTH) {
			cfg.setValue("winmain.fullscreen", true);
		} else {
			cfg.setValue("winmain.fullscreen", false);
			cfg.setValue("winmain.width", this.getWidth());
			cfg.setValue("winmain.height", this.getHeight());
			Point loc = this.getLocation();
			cfg.setValue("winmain.x", (int)loc.getX());
			cfg.setValue("winmain.y", (int)loc.getY());
		}
		
		if (posSep > 0)
			cfg.setValue("splitpane.divider.location", posSep);
		
		cfg.setValue("panelmap.enabled", mapEnabled);
		cfg.setValue("tabbedpane.tablayoutpolicy", tabbedPane.getTabLayoutPolicy());
		cfg.setValue("toolbar.template.input", toolBarModel.getComboInValue());
		cfg.setValue("toolbar.template.output", toolBarModel.getComboOutValue());
		cfg.setValue("splitpane.divider.orientation", splitPane.getOrientation());
		cfg.setValue("position.number.decimal", Config.getInstance().getInteger("position.number.decimal"));
		cfg.setValue("spudmap.autocenter.selection", menuBar.isAutoCenterMap());
		cfg.save();
		
		System.exit(0);
	}

	@Override
	public void onOpenFile() {
		loadNewFiles(selectFiles());
	}

	@Override
	public void onCloseFile() {
		if (tabbedPane.getTabCount() > 0 && tabbedPane.getSelectedIndex() > -1) 
			askToSave(tabbedPane.getSelectedIndex());
	}

	@Override
	public void onCloseAll() {
		closeAllTab();
	}
	
	public boolean closeAllTab() {
		if (tabbedPane.getTabCount() > 0) {
			for (int i=tabbedPane.getTabCount()-1;i>-1;i--)
				if (!askToSave(i))
					return false;
		}
		
		return true;
	}
	
	private boolean askToSave(int index) {
		if (index > -1) {
			JPanel pan = (JPanel) tabbedPane.getTabComponentAt(index);
			if (pan == null)
				return true;
			
			if (((JLabel) pan.getComponent(0)).getText().equals("*")) {
				tabbedPane.setSelectedIndex(index);
				
				String[] buttons = new String[]{ Config.getLangBundle().getString("tbSave"), Config.getLangBundle().getString("tbSaveAs"), 
						Config.getLangBundle().getString("ignore"), Config.getLangBundle().getString("cancel") };

				int res = JOptionPane.showOptionDialog(WinMain.this,
						String.format(Config.getLangBundle().getString("askToSave"), ((JLabel) pan.getComponent(1)).getText()), 
						Config.getLangBundle().getString("mbExit"),
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.QUESTION_MESSAGE, 
						null, buttons, buttons[0]);
					
				switch (res) {
					case 0:
						if (saveFile(index, -1, false) == -2)
							return true;
						break;
					case 1:
						if (saveFile(index, -1, true) == -2)
							return true;
						break;
					case 2:
						break;
					case 3:
						return false;
					default:
						return true;
				}
			}
			
			SpudScroll scroll = (SpudScroll)tabbedPane.getSelectedComponent();
			scroll.getTable().saveColConfig();
			
			tabbedPane.remove(index);
		}
		
		return true;
	}
	
	@Override
	public void onQuit() {
		quitClose();
	}
	
	@Override
	public void onChooseModelIn(String id) {
		SpudTable table = getCurrentTab();
		
		if (table != null)
			table.setModelIn(toolBarModel.getComboInValue(), templates.get(toolBarModel.getComboInValue()));
		
		if (panelMap != null)
			panelMap.setModelIn(templates.get(toolBarModel.getComboInValue()));
	}

	@Override
	public void onChooseModelOut(String id) {
		SpudTable table = getCurrentTab();
		
		if (table != null)
			table.setNameModelOut(toolBarModel.getComboOutValue());
	}
	
	@Override
	public void onModelsLoaded(Hashtable<String, TemplateFile> templates) {
		this.templates = templates;
		this.toolBarModel.updateCombo(templates);
		
		if (modelWatcher == null)
			startModelWatcher();
	}
	
	private void startModelWatcher() {
		new Thread() {
			@Override
			public void run() {
				try {
					Path tempDir = Paths.get(LoadTemplates.MODEL_DIRECTORY);
					modelWatcher = tempDir.getFileSystem().newWatchService();
					tempDir.register(modelWatcher, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
					
					while (true) {
						WatchKey watckKey = modelWatcher.take();
								
						List<WatchEvent<?>> events = watckKey.pollEvents();
						for (WatchEvent<?> event : events) {
							if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE)
								new LoadTemplates(WinMain.this);		
							else if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY)
								new LoadTemplates(WinMain.this);
								
							if (!watckKey.reset()) break;
						}
					}
				} catch (Exception e) {}
			}
		}.start();
	}
	
	@Override
	public synchronized void onFileParsed(ArrayList<SpudItem> data, String path, boolean txt, int nbrError, int nbrLinesIgnored, String messageErreur) {
		createNewTab(data, path, txt);
		
		if (messageErreur != null) {
			JOptionPane.showMessageDialog(this, messageErreur,
						Config.getLangBundle().getString("warning"), JOptionPane.WARNING_MESSAGE, null);
		} else if (nbrError > 0 || nbrLinesIgnored > 0) {
			JOptionPane.showMessageDialog(this, 
					(nbrError>1)?String.format(Config.getLangBundle().getString("linesUnreadable"), path, nbrError, nbrLinesIgnored):
						String.format(Config.getLangBundle().getString("lineUnreadable"), 
						path.substring(path.lastIndexOf(File.separator)+1, path.length()), nbrError, nbrLinesIgnored),
						Config.getLangBundle().getString("warning"), JOptionPane.WARNING_MESSAGE, null);
		}
	}

	@Override
	public void onSaveExportError(String msg) {
		l.unlock();
		
		JOptionPane.showMessageDialog(this, msg, Config.getLangBundle().getString("error"), JOptionPane.ERROR_MESSAGE, null);
	}
	
	@Override
	public void onError(String msg) {
		JOptionPane.showMessageDialog(this, msg, Config.getLangBundle().getString("error"), JOptionPane.ERROR_MESSAGE, null);
	}
	
	@Override
	public synchronized void onExport(boolean toTxt, boolean all) {
		if (tabbedPane.getTabCount() > 0) {
			if (toolBarModel.getComboInValue() == null || toolBarModel.getComboInValue().isEmpty() || toolBarModel.isNothing()) {
				JOptionPane.showMessageDialog(this, 
						Config.getLangBundle().getString("errorModelIn"), Config.getLangBundle().getString("error"), 
						JOptionPane.ERROR_MESSAGE, null);
				return;
			} 
			
			if (toolBarModel.getComboOutValue() == null || toolBarModel.getComboOutValue().isEmpty()) {
				JOptionPane.showMessageDialog(this, 
						Config.getLangBundle().getString("errorModelOut"), Config.getLangBundle().getString("error"), 
						JOptionPane.ERROR_MESSAGE, null);
				return;
			}
			
			DialogExportChoice dec = new DialogExportChoice(this, toTxt, all);
			dec.setVisible(true);
			
			int optSelected = dec.getOptionSelected();
			dec.dispose();
			
			if (optSelected == -1)
				return;
			
			SpudTable table = null;
			File pathToSave = null;
			
			if ((all || optSelected == DialogExportChoice.BY_TYPE) && optSelected != DialogExportChoice.ALL_IN_ONE) {
				pathToSave = selectNewFolder(toTxt);
			} else {
				table = getCurrentTab();
				if (table != null) {
					if (table.getFilePath() == null || table.getFileName() == null || table.getFileName().length() < 1)
						pathToSave = selectNewFile(Config.getInstance().getString("fileChooser.default.path") + File.separator +
									"new" + tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()), toTxt, true);
					else
						pathToSave = selectNewFile(Config.getInstance().getString("fileChooser.default.path") + File.separator +
								table.getFileName(), toTxt, true);
				}
			}
			
			if (pathToSave == null)
				return;
			
			if (all) {
				if (optSelected == DialogExportChoice.ALL_IN_ONE) {
					exportAllInOne(pathToSave, toTxt);
				} else if (optSelected == DialogExportChoice.BY_TYPE) {
					exportAllByType(pathToSave, toTxt);
				} else {
					exportAllNormal(pathToSave, toTxt);
				}
			} else {
				if (optSelected == DialogExportChoice.BY_TYPE) {
					table = getCurrentTab();
					if (table != null)
						exportOneByType(pathToSave, toTxt, table.getData());
				} else {
					exportOneNormal(pathToSave, toTxt, table.getData());
				}
			}
		}
	}
	
	private void exportAllByType(File pathToSave, boolean toTxt) {
		ArrayList<SpudItem> arrayToSave = new ArrayList<SpudItem>();
		for (int i=0;i<tabbedPane.getTabCount();i++) {
			SpudScroll scroll = (SpudScroll)tabbedPane.getComponentAt(i);
			SpudTable table = scroll.getTable();
			
			arrayToSave.addAll(table.getData());
		}
		
		exportOneByType(pathToSave, toTxt, arrayToSave);
	}
	
	private void exportOneByType(File pathToSave, boolean toTxt, ArrayList<SpudItem> arrayToSave) {
		HashMap<Integer,ArrayList<SpudItem>> map = new HashMap<Integer,ArrayList<SpudItem>>();
		TemplateFile modelIn = templates.get(toolBarModel.getComboInValue());
		TemplateFile modelOut = templates.get(toolBarModel.getComboOutValue());
		
		for (SpudItem item:arrayToSave) {
			int type = -1;
			if (item.getBinType() > -1)
				type = modelOut.getTxtTypeWithKey(modelIn.getKeyWithBinType(item.getBinType()));
			else
				type = modelOut.getTxtTypeWithKey(modelIn.getKeyWithTxtType(item.getTxtType()));
			
			if (map.containsKey(type)) {
				ArrayList<SpudItem> tempList = (ArrayList<SpudItem>) map.get(type);
				tempList.add(item);
			} else {
				ArrayList<SpudItem> tempList = new ArrayList<SpudItem>();
				tempList.add(item);
				map.put(type, tempList);
			}
		}
		
		ExecutorService exe = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 8);
		
		boolean noask = false;
		int primoVersion = -1;
		Iterator<Entry<Integer, ArrayList<SpudItem>>> it = map.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry<Integer, ArrayList<SpudItem>> pairs = (Map.Entry<Integer, ArrayList<SpudItem>>)it.next();
	    	
	    	String name;
	    	if (pairs.getKey() < 0) {
	    		name = String.format("T%1$d-%2$s_%3$s.%4$s", pairs.getKey(), 
						"Undefined" , toolBarModel.getComboOutValue().replace(' ', '_') ,(toTxt?"txt":"spud"));
	    	} else {
	    		name = String.format("T%1$d-%2$s_%3$s.%4$s", pairs.getKey(), 
						modelOut.getLabelWithTxtType(pairs.getKey()).replace(' ', '_'), toolBarModel.getComboOutValue().replace(' ', '_') ,(toTxt?"txt":"spud"));
	    	}
			
			File fileToSave = new File(pathToSave, name);
				
			if (primoVersion == -1) {
				if (!toTxt) primoVersion = askIgo8Primo();
				if (primoVersion == 2) return;
			}
				
			if (!noask) {
				int rc = askToReplaceFile(fileToSave, true);
				if (rc == 1) {
					noask = true;
				} else if (rc == 3) {
					return;
				}
			}
				
			exe.execute(new SaveFile(fileToSave,this, toTxt, templates.get(toolBarModel.getComboInValue()), 
					templates.get(toolBarModel.getComboOutValue()), (ArrayList<SpudItem>) pairs.getValue(), (primoVersion==0?true:false), 
					!it.hasNext(), true, -1, l));
		}
	}
	
	private void exportAllInOne(File pathToSave, boolean toTxt) {
		ArrayList<SpudItem> arrayToSave = new ArrayList<SpudItem>();
		for (int i=0;i<tabbedPane.getTabCount();i++) {
			SpudScroll scroll = (SpudScroll)tabbedPane.getComponentAt(i);
			SpudTable table = scroll.getTable();
			
			arrayToSave.addAll(table.getData());
		}
		
		exportOneNormal(pathToSave, toTxt, arrayToSave);
	}
	
	private void exportOneNormal(File fileToSave, boolean toTxt, ArrayList<SpudItem> arrayToSave) {
		if (askToReplaceFile(fileToSave, false) == 0) {
			int primoVersion = 0;		
			if (!toTxt)
				primoVersion = askIgo8Primo();
					
			if (primoVersion == 2) return;
					
			new Thread(new SaveFile(fileToSave, this, toTxt, templates.get(toolBarModel.getComboInValue()), 
					templates.get(toolBarModel.getComboOutValue()), arrayToSave, (primoVersion==0?true:false), 
					true, true, -1, l)).start();
		}
	}
	
	private void exportAllNormal(File pathToSave, boolean toTxt) {
		ExecutorService exe = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 8);
		
		boolean noask = false;
		int primoVersion = -1;
		for (int i=0;i<tabbedPane.getTabCount();i++) {
			SpudScroll scroll = (SpudScroll)tabbedPane.getComponentAt(i);
			SpudTable table = scroll.getTable();
				
			String name;
			if (table.getFilePath() == null || table.getFileName() == null || table.getFileName().length() < 1) {
				name = "new" + tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()) + (toTxt?".txt":".spud");
			} else {
				name = table.getFileName();
				name = name.substring(0, (name.lastIndexOf(".")>-1)?name.lastIndexOf("."):name.length()) + (toTxt?".txt":".spud");
			}
				
			File fileToSave = new File(pathToSave, name);
				
			if (primoVersion == -1) {
				if (!toTxt) primoVersion = askIgo8Primo();
				if (primoVersion == 2) return;
			}
				
			if (!noask) {
				int rc = askToReplaceFile(fileToSave, true);
				if (rc == 1) {
					noask = true;
				} else if (rc == 3) {
					return;
				}
			}
				
			exe.execute(new SaveFile(fileToSave,this, toTxt, templates.get(toolBarModel.getComboInValue()), 
					templates.get(toolBarModel.getComboOutValue()), table.getData(), (primoVersion==0?true:false), 
					(i==tabbedPane.getTabCount()-1)?true:false, true, -1, l));
		}
	}
	
	private File selectNewFolder(boolean txt) {
		JFileChooser fileChooser = new JFileChooser(Config.getInstance().getString("fileChooser.default.path"));
		fileChooser.setFileFilter(new FileFilterTxt());
		fileChooser.setFileFilter(new FileFilterSpud());
		fileChooser.setFileFilter(new FileFilterTxtSpud());
		
		fileChooser.setDialogTitle(txt?Config.getLangBundle().getString("mbExportAllTxt"):Config.getLangBundle().getString("mbExportAllSpud"));
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setSelectedFile(null);
		fileChooser.setMultiSelectionEnabled(false);
		
		int ret = fileChooser.showSaveDialog(this);
		File res = null;
		
		if (ret == JFileChooser.APPROVE_OPTION) {
			res = fileChooser.getSelectedFile();
			Config.getInstance().setValue("fileChooser.default.path", fileChooser.getCurrentDirectory().getAbsolutePath());
		} else if (ret == JFileChooser.ERROR_OPTION) {
			JOptionPane.showMessageDialog(WinMain.this, 
					Config.getLangBundle().getString("errorSelectFolder"), Config.getLangBundle().getString("error"), JOptionPane.ERROR_MESSAGE, null);
		} 
			
		return res;
	}
	
	private int askIgo8Primo() {
		String[] buttons = new String[]{ Config.getLangBundle().getString("igoPrimo"),
				Config.getLangBundle().getString("igo8"), Config.getLangBundle().getString("cancel") };
		int rc = JOptionPane.showOptionDialog(WinMain.this,
				Config.getLangBundle().getString("askIgoVers"), 
				Config.getLangBundle().getString("askIgoVersTitle"),
				JOptionPane.YES_NO_CANCEL_OPTION, 
				JOptionPane.QUESTION_MESSAGE, 
				null, buttons, 0);

		return rc;
	}
	
	private int askToReplaceFile(File f, boolean all) {
		if (f != null && f.exists()) {
			String[] buttons;
			
			if (all)
				buttons = new String[]{ Config.getLangBundle().getString("yes"), Config.getLangBundle().getString("yesToAll"), 
					Config.getLangBundle().getString("no"), Config.getLangBundle().getString("cancel") };
			else
				buttons = new String[]{ Config.getLangBundle().getString("yes"), Config.getLangBundle().getString("no") };

			int rc = JOptionPane.showOptionDialog(WinMain.this,
					String.format(Config.getLangBundle().getString("askReplaceFile"), f.getName()), 
					Config.getLangBundle().getString("askReplaceFileTitle"),
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.QUESTION_MESSAGE, 
					null, buttons, buttons[all?2:1]);

			return rc;
		}
		
		return 0;
	}
	
	@Override
	public synchronized void onFileSaved(int tabIndex, String newPath, boolean error) {
		l.unlock();
		
		if (error) {
			JOptionPane.showMessageDialog(this, Config.getLangBundle().getString("errorSaveFile"), 
					String.format(Config.getLangBundle().getString("error"), newPath), JOptionPane.ERROR_MESSAGE, null);
			return;
		}
		
		if (tabIndex > -1 && tabIndex < tabbedPane.getTabCount()) {
			SpudScroll scroll = (SpudScroll)tabbedPane.getComponentAt(tabIndex);
			if (scroll == null)
				return;
			
			SpudTable table = scroll.getTable();
			if (table == null)
				return;
			
			table.setFilePath(newPath);
			
			JPanel pan = (JPanel) tabbedPane.getTabComponentAt(tabIndex);
			JLabel lab = (JLabel) pan.getComponent(0);
			lab.setText("");
			lab = (JLabel) pan.getComponent(1);
			lab.setText(table.getFileName());
			
			if (tabIndex == tabbedPane.getSelectedIndex())
				this.setTitle(spudigo.Main.version + " - " + newPath);
		}
	}
	
	@Override
	public synchronized void onFileExported(String msg, boolean last) {
		l.unlock();
		
		if (tmpMesSave == null)
			tmpMesSave = new StringBuilder("");
		
		if (last) {
			tmpMesSave.append(msg);
			
			DialogResult res = new DialogResult(this, tmpMesSave.toString());
			res.setVisible(true);
			
			tmpMesSave = null;
		} else {
			tmpMesSave.append(msg);
			tmpMesSave.append("<br>");
		}
	}
	
	@Override
	public synchronized void onSave() {
		if (tabbedPane.getTabCount() > 0)
			saveFile(tabbedPane.getSelectedIndex(), -1, false);
	}
	
	@Override
	public synchronized void onSaveAs() {
		if (tabbedPane.getTabCount() > 0)
			saveFile(tabbedPane.getSelectedIndex(), -1, true);
	}

	@Override
	public synchronized void onSaveAll() {
		if (tabbedPane.getTabCount() > 0) {
			int res = -1;
			for (int i=0;i<tabbedPane.getTabCount();i++) {
				res = saveFile(i, res, false);
			}
		}
	}
	
	@Override
	public synchronized void onSaveView() {
		if (tabbedPane.getTabCount() > 0)
			saveFile(tabbedPane.getSelectedIndex(), -1, true, true);
	}
	
	private int saveFile(int index, int askForIgo, boolean saveAs) {
		return saveFile(index, askForIgo, saveAs, false);
	}
	
	private int saveFile(int index, int askForIgo, boolean saveAs, boolean asView) {
		if (index > -1) {
			SpudScroll scroll = (SpudScroll)tabbedPane.getComponentAt(index);
			SpudTable table = scroll.getTable();
			if (table != null) {
				File fileToSave = null;
				if (table.getFilePath() != null)
					fileToSave = new File(table.getFilePath());
				
				if (fileToSave == null || !fileToSave.exists() || saveAs) {
					if (table.getFileName() != null && table.getFileName().length() > 0)
						fileToSave = selectNewFile(Config.getInstance().getString("fileChooser.default.path") + File.separator + 
								table.getFileName(), table.isTxt(), false);
					else
						fileToSave = selectNewFile(Config.getInstance().getString("fileChooser.default.path") + File.separator + 
								"new" + tabbedPane.getTitleAt(index), table.isTxt(), false);
				
					if (fileToSave == null || askToReplaceFile(fileToSave, false) != 0)
						return -2;
				}
				
				if (askForIgo < 0) {
					if (!table.isTxt())
						askForIgo = askIgo8Primo();
					
					if (askForIgo == 2) 
						return askForIgo;
				}
				
				new Thread(new SaveFile(fileToSave, 
						this, 
						table.isTxt(), 
						templates.get(toolBarModel.getComboInValue()), 
						templates.get(toolBarModel.getComboOutValue()), 
						(asView?table.getDataAsView():table.getData()), 
						(askForIgo==0?true:false), 
						true, 
						false, 
						(asView?-1:index), l)).start();
			}
		}
		
		return askForIgo;
	}

	@Override
	public void onChangeLang(String lang, String country) {
		Config.getInstance().setCurrentLocale(lang, country);

		JOptionPane.showMessageDialog(WinMain.this, Config.getLangBundle().getString("langNeedRestartMessage"), 
				Config.getLangBundle().getString("langNeedRestartTitle"), JOptionPane.INFORMATION_MESSAGE);
	}
	
	@Override
	public void onMapSettings() {
		DialogMapSettings dc = new DialogMapSettings(this);
		dc.addReloadListener(new ReloadListener() {
			@Override
			public void reload() {
				if (panelMap != null)
		        	panelMap.reload();
			}
		});
		
		dc.setVisible(true);
		dc.dispose();
	}
	
	@Override
	public void onAbout() {
		JOptionPane.showMessageDialog(WinMain.this, "<html><b>" + spudigo.Main.version + "</b><br>" + 
										spudigo.Main.versionDate + "<br><br>" + 
										Config.getLangBundle().getString("aboutBuildBy") + " <b>djeman</b></html>", 
				Config.getLangBundle().getString("aboutTitle"), JOptionPane.INFORMATION_MESSAGE, 
				new ImageIcon(getClass().getResource("/spudigo/images/ganzoo.gif")));
	}
	
	@Override
	public void onUndo() {
		SpudTable table = getCurrentTab();
		
		if (table != null)
			table.undo();
	}

	@Override
	public void onRedo() {
		SpudTable table = getCurrentTab();
		
		if (table != null)
			table.redo();
	}
	
	@Override
	public void onCopy() {
		SpudTable table = getCurrentTab();
		if (table != null) {
			int rowIndex = table.getSelectedRow();
			
			if (rowIndex > -1) {
				copyRow.clear();
				for (int i:table.getSelectedRows())
					copyRow.add(table.getData().get(table.getRowSorter().convertRowIndexToModel(i)));
			}
		}
	}
	
	@Override
	public void onCut() {
		if (tabbedPane.getTabCount() > 0) {
			SpudScroll scroll = (SpudScroll)tabbedPane.getSelectedComponent();
			SpudTable table = scroll.getTable();
			
			int rowIndex = table.getSelectedRow();
			
			if (rowIndex > -1) {
				copyRow.clear();
				for (int i:table.getSelectedRows())
					copyRow.add(table.getData().get(table.getRowSorter().convertRowIndexToModel(i)));
				
				int[] sortIndex = new int[table.getSelectedRows().length];
				int[] selectedIndex = table.getSelectedRows();
				for (int i=0;i<selectedIndex.length;i++)
					sortIndex[i] = table.getRowSorter().convertRowIndexToModel(selectedIndex[i]);
				table.removeRows(sortIndex);
				
				setTabTitleModified(true);
				
				if (rowIndex > -1 && rowIndex < table.getRowCount())
					table.setRowSelectionInterval(rowIndex, rowIndex);
				else if (table.getRowCount() > 0 && rowIndex-1 < table.getRowCount())
					table.setRowSelectionInterval(rowIndex-1, rowIndex-1);
				else
					table.clearSelection();
				
				((JTable)scroll.getRowHeader().getView()).invalidate();
				statusBar.setStatus(String.format(Config.getLangBundle().getString("statusRowNumbers"), table.getRowCount()));
				
				if (panelMap != null)
					panelMap.setPoiArray(table.getData());
			}
		}
	}

	@Override
	public void onPaste() {
		if (tabbedPane.getTabCount() > 0) {
			SpudScroll scroll = (SpudScroll)tabbedPane.getSelectedComponent();
			SpudTable table = scroll.getTable();
			
			if (copyRow.size() > 0) {
				for (SpudItem item:copyRow)
					table.addRow(item);
				
				setTabTitleModified(true);
				
				int index = table.getRowSorter().convertRowIndexToView(table.getRowCount()-copyRow.size());
				table.setRowSelectionInterval(index, index);
				
				for (int i = table.getRowCount() - copyRow.size() + 1; i < table.getRowCount(); i++)
				{
					index = table.getRowSorter().convertRowIndexToView(i);
					table.addRowSelectionInterval(index, index);
				}
				
				table.scrollRectToVisible(table.getCellRect(index, 0, true));
				
				((JTable)scroll.getRowHeader().getView()).invalidate();
				statusBar.setStatus(String.format(Config.getLangBundle().getString("statusRowNumbers"), table.getRowCount()));
				
				if (panelMap != null)
					panelMap.setPoiArray(table.getData());
			}
		}
	}
	
	@Override
	public void onInsert() {
		if (tabbedPane.getTabCount() > 0) {
			SpudScroll scroll = (SpudScroll)tabbedPane.getSelectedComponent();
			SpudTable table = scroll.getTable();
			
			int rowIndex = table.getSelectedRow();
			
			if (rowIndex > -1 && copyRow.size() > 0) {
				for (SpudItem item:copyRow)
					table.insertRow(rowIndex++, item);
				
				setTabTitleModified(true);
				
				int index = table.getRowSorter().convertRowIndexToView(rowIndex-copyRow.size());
				table.setRowSelectionInterval(index, index);
				
				for (int i = rowIndex - copyRow.size() + 1; i < rowIndex; i++)
				{
					index = table.getRowSorter().convertRowIndexToView(i);
					table.addRowSelectionInterval(index, index);
				}
				
				table.scrollRectToVisible(table.getCellRect(index, 0, true));
				
				((JTable)scroll.getRowHeader().getView()).invalidate();
				statusBar.setStatus(String.format(Config.getLangBundle().getString("statusRowNumbers"), table.getRowCount()));
				
				if (panelMap != null)
					panelMap.setPoiArray(table.getData());
			}
		}
	}

	@Override
	public void onAddLine() {
		addLine(0.00, 0.00);
	}
	
	@Override
	public void onAddLine(double lat, double lon) {
		addLine(lat, lon);
	}
	
	private void addLine(double lat, double lon) {
		if (tabbedPane.getTabCount() > 0) {
			SpudScroll scroll = (SpudScroll)tabbedPane.getSelectedComponent();
			SpudTable table = scroll.getTable();

			table.addRow(lat, lon);
			
			setTabTitleModified(true);			
			
			int index = table.getRowSorter().convertRowIndexToView(table.getRowCount()-1);
			table.setRowSelectionInterval(index, index);
			table.scrollRectToVisible(table.getCellRect(index, 0, true));
			
			((JTable)scroll.getRowHeader().getView()).invalidate();
			statusBar.setStatus(String.format(Config.getLangBundle().getString("statusRowNumbers"), table.getRowCount()));
			
			if (panelMap != null)
				panelMap.setPoiArray(table.getData());
		}
	}
	
	@Override
	public void onInsertLine() {		
		insertLine(0.00, 0.00);
	}
	
	private void insertLine(double lat, double lon) {
		if (tabbedPane.getTabCount() > 0) {
			SpudScroll scroll = (SpudScroll)tabbedPane.getSelectedComponent();
			SpudTable table = scroll.getTable();
			
			int rowIndex = table.getSelectedRow();
			
			if (rowIndex > -1) {
				table.insertRow(rowIndex, lat, lon);
				
				setTabTitleModified(true);
								
				int index = table.getRowSorter().convertRowIndexToView(rowIndex);
				table.setRowSelectionInterval(index, index);
				table.scrollRectToVisible(table.getCellRect(index, 0, true));
				
				((JTable)scroll.getRowHeader().getView()).invalidate();
				statusBar.setStatus(String.format(Config.getLangBundle().getString("statusRowNumbers"), table.getRowCount()));
				
				if (panelMap != null)
					panelMap.setPoiArray(table.getData());
			}
		}
	}

	@Override
	public void onDelete() {
		if (tabbedPane.getTabCount() > 0) {
			SpudScroll scroll = (SpudScroll)tabbedPane.getSelectedComponent();
			SpudTable table = scroll.getTable();
			
			int rowIndex = table.getSelectedRow();
			
			if (rowIndex > -1) {
				int[] sortIndex = new int[table.getSelectedRows().length];
				int[] selectedIndex = table.getSelectedRows();
				for (int i=0;i<selectedIndex.length;i++)
					sortIndex[i] = table.getRowSorter().convertRowIndexToModel(selectedIndex[i]);
				table.removeRows(sortIndex);
				
				setTabTitleModified(true);
				
				if (rowIndex > -1 && rowIndex < table.getRowCount()) {
					table.setRowSelectionInterval(rowIndex, rowIndex);
					
					Rectangle rect = table.getCellRect(rowIndex, 0, true);
					rect.y = rect.y - table.getVisibleRect().height/2 + rect.height/2;
					rect.height = table.getVisibleRect().height;
					table.scrollRectToVisible(rect);
				} else if (table.getRowCount() > 0 && rowIndex-1 < table.getRowCount()) {
					table.setRowSelectionInterval(rowIndex-1, rowIndex-1);
					
					Rectangle rect = table.getCellRect(rowIndex-1, 0, true);
					rect.y = rect.y - table.getVisibleRect().height/2 + rect.height/2;
					rect.height = table.getVisibleRect().height;
					table.scrollRectToVisible(rect);
				} else {
					table.clearSelection();
				}
				
				((JTable)scroll.getRowHeader().getView()).invalidate();
				statusBar.setStatus(String.format(Config.getLangBundle().getString("statusRowNumbers"), table.getRowCount()));
				
				if (panelMap != null)
					panelMap.setPoiArray(table.getData());
			}
		}
	}
	
	@Override
	public void onNew() {
		createNewTab(new ArrayList<SpudItem>(), null, true);
	}
	
	@Override
	public void onDuplicate() {
		SpudTable table = getCurrentTab();
		if (table != null) {
			DialogDuplicateSearch dds = new DialogDuplicateSearch(WinMain.this);
			dds.setVisible(true);
			int distance = dds.getDistance();
			int angle = -1;
			if (dds.isAngleInvIgnored())
				angle = dds.getAngleInvDiff();
			dds.dispose();
			
			if (distance > 0)
				table.searchDuplicate(distance, angle);
		}
	}
	
	@Override
	public void onNextDuplicate() {
		SpudTable table = getCurrentTab();
		
		if (table != null)
			table.nextDuplicate();
	}

	@Override
	public void onClearDuplicate() {
		SpudTable table = getCurrentTab();
		
		if (table != null)
			table.clearDuplicate();
	}
	
	@Override
	public void onToggleMap() {
		if (mapEnabled && splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
			double tmp = (double)splitPane.getDividerLocation()/splitPane.getWidth();
			splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			splitPane.setDividerLocation(tmp);
		} else if (mapEnabled) {
			double tmp = (double)splitPane.getDividerLocation()/splitPane.getHeight();
			splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			splitPane.setDividerLocation(tmp);
			
			mapEnabled = !mapEnabled;
			enableMap(mapEnabled);
		} else {
			mapEnabled = !mapEnabled;
			enableMap(mapEnabled);
			
			SpudTable table = getCurrentTab();
			if (panelMap != null && table != null)
				panelMap.setPoiArray(table.getData());
		}
	}
	
	@Override
	public void onToggleView() {
		if (tabbedPane.getTabLayoutPolicy() == JTabbedPane.SCROLL_TAB_LAYOUT)
			tabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		else
			tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}
	
	@Override
	public void onMarkerClicked(MouseEvent evt) {
		SpudTable table = getCurrentTab();
		Point p = evt.getPoint();
		boolean ctrlPressed = evt.isControlDown();
		
		if (panelMap != null && table != null && p != null) {
			int index = panelMap.getIndex(table.getData(), p);
			if (index > -1 && index < table.getRowCount()) {
				index = table.getRowSorter().convertRowIndexToView(index);				
				if (ctrlPressed) {
					boolean selected = false; 
					for (int i : table.getSelectedRows()) {
				        if (i == index) {
				            selected = true;
				            break;
				        }
				    }
					
					if (selected)
						table.removeRowSelectionInterval(index, index);
					else
						table.addRowSelectionInterval(index, index);
				} else  {
					table.setRowSelectionInterval(index, index);
				}
				
				Rectangle rect = table.getCellRect(index, 0, true);
				rect.y = rect.y - table.getVisibleRect().height/2 + rect.height/2;
				rect.height = table.getVisibleRect().height;
				table.scrollRectToVisible(rect);
			} else if (!ctrlPressed) {
				table.clearSelection();
			}
		}
	}
	
	@Override
	public void onMapCenter() {
		SpudTable table = getCurrentTab();
		if (table != null && panelMap != null) {
			int rowIndex = table.getSelectedRow();
			
			if (rowIndex > -1) {
				rowIndex = table.getRowSorter().convertRowIndexToModel(rowIndex);
				panelMap.centerOnPosition(table.getData().get(rowIndex).getLat(), table.getData().get(rowIndex).getLon());
			}
		}
	}
	
	@Override
	public void onMapCenterAndZoom() {
		SpudTable table = getCurrentTab();
		
		if (table != null)
			onMapCenterAndZoom(table.getSelectedRows());
	}
	
	@Override
	public void onMapCenterAndZoom(int[] rowsIndex) {
		SpudTable table = getCurrentTab();
		if (table != null && panelMap != null && rowsIndex.length > 0) {
			ArrayList<SpudItem> items = new ArrayList<SpudItem>();
			for (int i=0;i<rowsIndex.length;i++)
				items.add(table.getData().get(table.getRowSorter().convertRowIndexToModel(rowsIndex[i])));
			
			if (rowsIndex.length > 1) {				
				panelMap.centerOnPoiArray(items);
			} else {
				panelMap.centerOnPosition(items.get(0).getLat(), items.get(0).getLon());
				TileFactoryInfo info = panelMap.getMainMap().getTileFactory().getInfo();
				panelMap.setZoom(Math.min(info.getMinimumZoomLevel()+2, info.getMaximumZoomLevel()));
			}
		}
	}
	
	ActionListener TabCloseFile = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			JButton btn = (JButton)e.getSource();
			int index = tabbedPane.indexOfTab(btn.getActionCommand());
			
			askToSave(index);
		}
	};
	
	MouseMotionListener MouseEditMotion = new MouseMotionListener() {
		@Override
		public void mouseDragged(MouseEvent evt) {}

		@Override
		public void mouseMoved(MouseEvent evt) {
			if (tabbedPane.getTabCount() > 0) {	 
				if (evt.isAltDown()) {
					if (mouseOri == null) {
						mouseOri = evt.getPoint();
						SwingUtilities.convertPointToScreen(mouseOri, evt.getComponent());
					} else {
						Point p = evt.getPoint();
						SwingUtilities.convertPointToScreen(p, evt.getComponent());
						
						int val = (int)mouseOri.getY() - (int)p.getY();
	
						if (val != 0) {
							SpudTable table = getCurrentTab();
							if (table != null) {
								int rowIndex = table.getSelectedRow();
									
								if (rowIndex > -1) {
									int angle = (int)table.getValueAt(rowIndex, 5);
									angle += val;
									if (angle > 359)
										angle -= 359;
									else if (angle < 0)
										angle += 359;
									table.setValueAt(angle, rowIndex, 5);
								}
								
								mouseOri = p;
							}
						}
					}
				}
				
				if (evt.isShiftDown()) {
					if (mouseOri == null) {
						mouseOri = evt.getPoint();
						SwingUtilities.convertPointToScreen(mouseOri, evt.getComponent());
					} else {
						Point p = evt.getPoint();
						SwingUtilities.convertPointToScreen(p, evt.getComponent());
						
						int valY = (int)mouseOri.getY() - (int)p.getY();
						int valX = (int)p.getX() - (int)mouseOri.getX();
						
						if (valX != 0 || valY != 0) {
							SpudTable table = getCurrentTab();
							if (table != null) {
								int rowIndex = table.getSelectedRow();
								
								if (rowIndex > -1) {
									double lon = (double)table.getValueAt(rowIndex, 0);
									double lat = (double)table.getValueAt(rowIndex, 1);
									lon += valX * 0.00001;
									lat += valY * 0.00001;
									if (lon > 180)
										lon -= 360;
									else if (lon < -180)
										lon += 360;
									if (lat > 90)
										lat -= 180;
									else if (lat < -90)
										lat += 180;
									table.setValueAt(lon, rowIndex, 0);
									table.setValueAt(lat, rowIndex, 1);
								}
								
								mouseOri = p;
							}
						}
					}
				}
			}
		}
	};
}
