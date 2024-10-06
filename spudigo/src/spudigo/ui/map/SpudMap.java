package spudigo.ui.map;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dialog.ModalityType;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

import org.jxmapviewer.BingTileFactoryInfo;
import org.jxmapviewer.HereTileFactoryInfo;
import org.jxmapviewer.IGNTileFactoryInfo;
import org.jxmapviewer.JXMapKit;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.TomTomTileFactoryInfo;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.LocalResponseCache;
import org.jxmapviewer.viewer.TileFactory;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.epsg3395.Epsg3395TileFactory;
import org.jxmapviewer.viewer.epsg3395.YandexTileFactoryInfo;

import spudigo.Config;
import spudigo.SpudItem;
import spudigo.TemplateFile;
import spudigo.ui.dialog.DialogAddress;
import spudigo.ui.dialog.DialogLatLng;
import spudigo.ui.map.GeoCodeThread.GeoCodeListener;
import spudigo.ui.map.painter.SpudInfoPainter;
import spudigo.ui.map.painter.SpudPoiPainter;
import spudigo.ui.map.painter.SpudRoadPainter;
import spudigo.ui.map.painter.SpudSelectedPainter;
import spudigo.ui.map.renderer.SpudPoiRenderer;
import spudigo.ui.map.renderer.SpudSelectedRenderer;

public class SpudMap extends JXMapKit implements GeoCodeListener {
	private static final long serialVersionUID = 1L;
		
	private final int defaultInitialDelay = ToolTipManager.sharedInstance().getInitialDelay();
	private final int defaultDismissDelay = ToolTipManager.sharedInstance().getDismissDelay();
	
	private final PanelMapListener panelMapListener;
	
	private JDialog loadingDialog = null;
	private Providers mapSelected;
	private GeoPosition cursorPos = null;
		
	private final SpudPoiRenderer poiRenderer;
	private final SpudSelectedRenderer selectedRenderer;

	private final MapApiKey mapApiKey = new MapApiKey();
	private final JCheckBoxMenuItem checkBoxCompass = new JCheckBoxMenuItem(Config.getLangBundle().getString("mapShowCompass"));
	private final JCheckBoxMenuItem checkBoxCompassSelected = new JCheckBoxMenuItem(Config.getLangBundle().getString("mapShowCompassSelection"));
	private final SpudPoiPainter poiPainter = new SpudPoiPainter();
	private final SpudSelectedPainter selectedPainter = new SpudSelectedPainter();
	private final SpudRoadPainter roadPainter = new SpudRoadPainter();
	private final CompoundPainter<JXMapViewer> compoundPainter = new CompoundPainter<JXMapViewer>();
	private final SpudInfoPainter spudInfoPainter;
	
	public static enum Providers {
		OSMRoad, BingRoad, BingHybrid, BingSat, TomTomRoad, HereRoad, 
		HereHybrid, HereSat, HereTerrain, YandexRoad, YandexHybrid, YandexSat,
		IgnMaps, IgnMapsClassic, IgnMapsCadastral, IgnPhotosCadastral, IgnPhotos,
		IgnMapsScanOACI, IgnMapsScan25
	}
	
	@SuppressWarnings("unchecked")
	public SpudMap(PanelMapListener panelMapListener, TemplateFile modelIn) {
		super(false);
		
		this.panelMapListener = panelMapListener;
		
		try {
			mapSelected = Providers.valueOf(Config.getInstance().getString("panelmap.provider"));
		} catch (IllegalArgumentException e) {
			mapSelected = Providers.OSMRoad;
		}
		
		if (Config.getInstance().getString("panelmap.filecache.path") != null)
			LocalResponseCache.installResponseCache(null, 
					new File(Config.getInstance().getString("panelmap.filecache.path")), true);
		
		selectNewTileFactory(mapSelected);
		this.setZoom(this.getMainMap().getTileFactory().getInfo().getMaximumZoomLevel());
		this.setCenterPosition(new GeoPosition(35,0));
		this.getMainMap().setDrawTileBorders(false);
		this.getMainMap().setRestrictOutsidePanning(true);
		this.getMainMap().setHorizontalWrapped(false);
		//this.setAddressLocationShown(false);
		
		poiRenderer = new SpudPoiRenderer(modelIn);
		
		checkBoxCompassSelected.setSelected(Config.getInstance().getBoolean("panelmap.compass.selected.show"));
		selectedRenderer = new SpudSelectedRenderer(checkBoxCompassSelected.getState());
		
		poiPainter.setCacheable(false);
		poiPainter.setRenderer(poiRenderer);
		
		selectedPainter.setCacheable(false);
		selectedPainter.setRenderer(selectedRenderer);
		
		checkBoxCompass.setSelected(Config.getInstance().getBoolean("panelmap.compass.show"));
		spudInfoPainter = new SpudInfoPainter(checkBoxCompass.getState());
		checkBoxCompass.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				spudInfoPainter.setShowCompass(e.getStateChange()==1);
				Config.getInstance().setValue("panelmap.compass.show", e.getStateChange()==1);
				SpudMap.this.repaint();
			}
		});		
		
		checkBoxCompassSelected.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				selectedRenderer.setShowCompassSelected(e.getStateChange()==1);
				Config.getInstance().setValue("panelmap.compass.selected.show", e.getStateChange()==1);
				SpudMap.this.repaint();
			}
		});
		
		compoundPainter.setCacheable(false);
		compoundPainter.setPainters(poiPainter, roadPainter, selectedPainter, spudInfoPainter);
		
		this.getMainMap().setOverlayPainter(compoundPainter);

		this.getMainMap().addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent evt) {}

			@Override
			public void mouseMoved(MouseEvent evt) {
				Rectangle rect = SpudMap.this.getMainMap().getViewportBounds();
				Point converted_gp_pt = new Point((int) evt.getPoint().getX() + rect.x,
                        (int) evt.getPoint().getY() + rect.y);
				
				cursorPos = SpudMap.this.getMainMap().getTileFactory().pixelToGeo(converted_gp_pt, SpudMap.this.getMainMap().getZoom());
				SpudMap.this.getMainMap().setToolTipText("<html><center>" + String.format("%.5f", cursorPos.getLongitude()) + "°<br>" + 
						String.format("%.5f", cursorPos.getLatitude()) + "°</center></html>");
			}
		});
		
		this.getMainMap().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent evt) {
            	if (SwingUtilities.isLeftMouseButton(evt)) {
            		if (SpudMap.this.panelMapListener != null) {
	            		if (evt.isShiftDown()) {
	            			Rectangle rect = SpudMap.this.getMainMap().getViewportBounds();
	        				Point converted_gp_pt = new Point((int) evt.getPoint().getX() + rect.x,
	                                (int) evt.getPoint().getY() + rect.y);
	        				
	        				GeoPosition worldPos = SpudMap.this.getMainMap().getTileFactory().pixelToGeo(converted_gp_pt, SpudMap.this.getMainMap().getZoom());
	            			SpudMap.this.panelMapListener.onAddLine(worldPos.getLatitude(), worldPos.getLongitude());
	            		} else {
		            		SpudMap.this.panelMapListener.onMarkerClicked(evt);
	            		}
            		}
            	} 
            }

			@Override
			public void mouseEntered(MouseEvent evt) {
				ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
				ToolTipManager.sharedInstance().setInitialDelay(0);
			}

			@Override
			public void mouseExited(MouseEvent evt) {
				ToolTipManager.sharedInstance().setDismissDelay(defaultDismissDelay);
				ToolTipManager.sharedInstance().setInitialDelay(defaultInitialDelay);
			}

			@Override
			public void mousePressed(MouseEvent evt) {}

			@Override
			public void mouseReleased(MouseEvent evt) {
				if (evt.isPopupTrigger() && evt.getComponent() instanceof JXMapViewer) {
		        	JPopupMenu popup = SpudMap.this.genRowPopupMenu();
		        	popup.show(evt.getComponent(), evt.getX(), evt.getY());
		        }
			}
        });
	}
	
	protected JPopupMenu genRowPopupMenu() {
		JPopupMenu popupArea = new JPopupMenu();
		JRadioButtonMenuItem rbMenuItem;
		
		JMenu providerMenu = new JMenu(Config.getLangBundle().getString("mapChoice"));
		
		rbMenuItem = new JRadioButtonMenuItem("Open Street Map - " + Config.getLangBundle().getString("mapRoad"));
		rbMenuItem.addActionListener(ChangeProviderAction);
		rbMenuItem.setActionCommand(Providers.OSMRoad.toString());
		if (mapSelected == Providers.OSMRoad) rbMenuItem.setSelected(true);
		providerMenu.add(rbMenuItem);

		providerMenu.addSeparator();
		
		rbMenuItem = new JRadioButtonMenuItem("Bing - " + Config.getLangBundle().getString("mapRoad"));
		rbMenuItem.addActionListener(ChangeProviderAction);
		rbMenuItem.setActionCommand(Providers.BingRoad.toString());
		if (mapSelected == Providers.BingRoad) rbMenuItem.setSelected(true);
		providerMenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Bing - " + Config.getLangBundle().getString("mapHybrid"));
		rbMenuItem.addActionListener(ChangeProviderAction);
		rbMenuItem.setActionCommand(Providers.BingHybrid.toString());
		if (mapSelected == Providers.BingHybrid) rbMenuItem.setSelected(true);
		providerMenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Bing - " + Config.getLangBundle().getString("mapSatellite"));
		rbMenuItem.addActionListener(ChangeProviderAction);
		rbMenuItem.setActionCommand(Providers.BingSat.toString());
		if (mapSelected == Providers.BingSat) rbMenuItem.setSelected(true);
		providerMenu.add(rbMenuItem);
		
		providerMenu.addSeparator();
		
		rbMenuItem = new JRadioButtonMenuItem("Tomtom - " + Config.getLangBundle().getString("mapRoad"));
		rbMenuItem.addActionListener(ChangeProviderAction);
		rbMenuItem.setActionCommand(Providers.TomTomRoad.toString());
		if (mapSelected == Providers.TomTomRoad) rbMenuItem.setSelected(true);
		providerMenu.add(rbMenuItem);
		
		providerMenu.addSeparator();
		
		rbMenuItem = new JRadioButtonMenuItem("Here - " + Config.getLangBundle().getString("mapRoad"));
		rbMenuItem.addActionListener(ChangeProviderAction);
		rbMenuItem.setActionCommand(Providers.HereRoad.toString());
		if (mapSelected == Providers.HereRoad) rbMenuItem.setSelected(true);
		providerMenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Here - " + Config.getLangBundle().getString("mapHybrid"));
		rbMenuItem.addActionListener(ChangeProviderAction);
		rbMenuItem.setActionCommand(Providers.HereHybrid.toString());
		if (mapSelected == Providers.HereHybrid) rbMenuItem.setSelected(true);
		providerMenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Here - " + Config.getLangBundle().getString("mapSatellite"));
		rbMenuItem.addActionListener(ChangeProviderAction);
		rbMenuItem.setActionCommand(Providers.HereSat.toString());
		if (mapSelected == Providers.HereSat) rbMenuItem.setSelected(true);
		providerMenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Here - " + Config.getLangBundle().getString("mapTerrain"));
		rbMenuItem.addActionListener(ChangeProviderAction);
		rbMenuItem.setActionCommand(Providers.HereTerrain.toString());
		if (mapSelected == Providers.HereTerrain) rbMenuItem.setSelected(true);
		providerMenu.add(rbMenuItem);
		
		providerMenu.addSeparator();
		
		rbMenuItem = new JRadioButtonMenuItem("Yandex - " + Config.getLangBundle().getString("mapRoad"));
		rbMenuItem.addActionListener(ChangeProviderAction);
		rbMenuItem.setActionCommand(Providers.YandexRoad.toString());
		if (mapSelected == Providers.YandexRoad) rbMenuItem.setSelected(true);
		providerMenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Yandex - " + Config.getLangBundle().getString("mapHybrid"));
		rbMenuItem.addActionListener(ChangeProviderAction);
		rbMenuItem.setActionCommand(Providers.YandexHybrid.toString());
		if (mapSelected == Providers.YandexHybrid) rbMenuItem.setSelected(true);
		providerMenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Yandex - " + Config.getLangBundle().getString("mapSatellite"));
		rbMenuItem.addActionListener(ChangeProviderAction);
		rbMenuItem.setActionCommand(Providers.YandexSat.toString());
		if (mapSelected == Providers.YandexSat) rbMenuItem.setSelected(true);
		providerMenu.add(rbMenuItem);
		
		providerMenu.addSeparator();
		
		JMenu ignMenu = new JMenu("IGN - France");
		
		rbMenuItem = new JRadioButtonMenuItem("Cartes");
		rbMenuItem.addActionListener(ChangeProviderAction);
		rbMenuItem.setActionCommand(Providers.IgnMaps.toString());
		if (mapSelected == Providers.IgnMaps) rbMenuItem.setSelected(true);
		ignMenu.add(rbMenuItem);
		
		rbMenuItem = new JRadioButtonMenuItem("Cartes classiques");
		rbMenuItem.addActionListener(ChangeProviderAction);
		rbMenuItem.setActionCommand(Providers.IgnMapsClassic.toString());
		if (mapSelected == Providers.IgnMapsClassic) rbMenuItem.setSelected(true);
		ignMenu.add(rbMenuItem);
		
		rbMenuItem = new JRadioButtonMenuItem("Photos aériennes");
		rbMenuItem.addActionListener(ChangeProviderAction);
		rbMenuItem.setActionCommand(Providers.IgnPhotos.toString());
		if (mapSelected == Providers.IgnPhotos) rbMenuItem.setSelected(true);
		ignMenu.add(rbMenuItem);
		
		ignMenu.addSeparator();
		
		rbMenuItem = new JRadioButtonMenuItem("Cartes aéronautiques");
		rbMenuItem.addActionListener(ChangeProviderAction);
		rbMenuItem.setActionCommand(Providers.IgnMapsScanOACI.toString());
		if (mapSelected == Providers.IgnMapsScanOACI) rbMenuItem.setSelected(true);
		ignMenu.add(rbMenuItem);
		
		rbMenuItem = new JRadioButtonMenuItem("Cartes Scan 25");
		rbMenuItem.addActionListener(ChangeProviderAction);
		rbMenuItem.setActionCommand(Providers.IgnMapsScan25.toString());
		if (mapSelected == Providers.IgnMapsScan25) rbMenuItem.setSelected(true);
		ignMenu.add(rbMenuItem);
		
		ignMenu.addSeparator();
		
		rbMenuItem = new JRadioButtonMenuItem("Cartes - Cadastre");
		rbMenuItem.addActionListener(ChangeProviderAction);
		rbMenuItem.setActionCommand(Providers.IgnMapsCadastral.toString());
		if (mapSelected == Providers.IgnMapsCadastral) rbMenuItem.setSelected(true);
		ignMenu.add(rbMenuItem);
		
		rbMenuItem = new JRadioButtonMenuItem("Photos - Cadastre");
		rbMenuItem.addActionListener(ChangeProviderAction);
		rbMenuItem.setActionCommand(Providers.IgnPhotosCadastral.toString());
		if (mapSelected == Providers.IgnPhotosCadastral) rbMenuItem.setSelected(true);
		ignMenu.add(rbMenuItem);
				
		providerMenu.add(ignMenu);
		
		popupArea.add(providerMenu);
		
		popupArea.addSeparator();
		
		JMenuItem menuItem = new JMenuItem(Config.getLangBundle().getString("mapGoToAddress"));
		menuItem.addActionListener(GoToAddressAction);
		popupArea.add(menuItem);
		
		menuItem = new JMenuItem(Config.getLangBundle().getString("mapGoToPosition"));
		menuItem.addActionListener(GoToPositionAction);
		popupArea.add(menuItem);
		
		popupArea.addSeparator();
		providerMenu = new JMenu(Config.getLangBundle().getString("mapStreetView"));
		
		menuItem = new JMenuItem(Config.getLangBundle().getString("mapOpenStreetView"));
		menuItem.addActionListener(OpenStreetView);
		providerMenu.add(menuItem);
		popupArea.add(providerMenu);
		
		popupArea.addSeparator();
		popupArea.add(checkBoxCompass);
		popupArea.add(checkBoxCompassSelected);
		popupArea.addSeparator();
		
		menuItem = new JMenuItem(Config.getLangBundle().getString("mbMapSettings"));
		menuItem.addActionListener(OpenSettingsAction);
		popupArea.add(menuItem);
		
		popupArea.addSeparator();
		
		menuItem = new JMenuItem(Config.getLangBundle().getString("mapHelp"));
		menuItem.addActionListener(ShowHelpAction);
		popupArea.add(menuItem);
		
		return popupArea;
	}

	public void setRoadDirect(GeoPosition[] road) {
		roadPainter.setRoadDirect(road);
		
		this.getMainMap().repaint();
	}
	
	public void setRoadOSM(GeoPosition[] roadToGo, GeoPosition[] roadToBack) {
		roadPainter.setRoadOSMtoGo(roadToGo);
		roadPainter.setRoadOSMtoBack(roadToBack);
		
		this.getMainMap().repaint();
	}
	
	public void clearAllRoads() {
		roadPainter.clearAllRoads();
	}
	
	public void setPoiArray(ArrayList<SpudItem> data) {
		poiPainter.setWaypoints(data);
		
		this.getMainMap().repaint();
	}
	
	public void centerOnPoiArray(ArrayList<SpudItem> data) {
		if (data != null)
			this.getMainMap().zoomToBestFitW(new HashSet<SpudItem>(data), 1);
	}
	
	public void centerOnPoiSelected(GeoPosition[] selected) {
		if (selected != null)
			this.getMainMap().zoomToBestFit(new HashSet<GeoPosition>(Arrays.asList(selected)), 1);
	}
	
	public void setSelected(SpudItem[] selected, boolean repaint) {
		selectedPainter.setWaypoints(selected);
		
		if (repaint)
			this.getMainMap().repaint();
	}
	
	public int getIndex(ArrayList<SpudItem> data, Point point) {
		Point2D pointData = null;
		Rectangle rect = this.getMainMap().getViewportBounds();
		int zoom = this.getMainMap().getZoom();
		
		for (int i=0;i<data.size();i++) {
			pointData = this.getMainMap().getTileFactory().geoToPixel(data.get(i).getPosition(), zoom);
            Point converted_gp_pt = new Point((int)pointData.getX() - rect.x,
                    (int)pointData.getY() - rect.y);
            
            if (Math.abs(converted_gp_pt.getX() - point.getX()) < 11 &&
            		(converted_gp_pt.getY() - point.getY()) > 0 &&
            		(converted_gp_pt.getY() - point.getY()) < 35) {
            	return i;
            }
		}
		
		return -1;
	}
	
	public void centerOnPosition(double lat, double lon) {
		this.setCenterPosition(new GeoPosition(lat, lon));
	}
	
	public void dispose() {
		((DefaultTileFactory) this.getMainMap().getTileFactory()).dispose();
		compoundPainter.clearCache();
	}
	
	public void reload() {
		poiPainter.reload();
		poiRenderer.reload();
		selectedRenderer.reload();
		this.getMainMap().repaint();
	}
	
	public void setModelIn(TemplateFile modelIn) {
		poiRenderer.setModelIn(modelIn);
		this.getMainMap().repaint();
	}
	
	private void setNewTileFactory(TileFactoryInfo info, boolean epsg3395) {
		GeoPosition pos = this.getMainMap().getCenterPosition();
		
		int zoom = this.getMainMap().getZoom();
		int oldTotalZoom = this.getMainMap().getTileFactory().getInfo().getTotalMapZoom();
		if (oldTotalZoom != info.getTotalMapZoom())
			zoom += info.getTotalMapZoom() - oldTotalZoom;
		
		zoom = Math.max(zoom, info.getMinimumZoomLevel());
		zoom = Math.min(zoom, info.getMaximumZoomLevel());
		
		TileFactory tf;
		if (epsg3395)
			tf = new Epsg3395TileFactory(info);
		else
			tf = new DefaultTileFactory(info);
		
		this.setTileFactory(tf);
		this.setZoom(zoom);
		this.setCenterPosition(pos);
	}
	
	private void selectNewTileFactory(Providers provider) {
		switch (provider) {
			case BingRoad:
				mapSelected = Providers.BingRoad;
				setNewTileFactory(new BingTileFactoryInfo(BingTileFactoryInfo.ROAD, mapApiKey.bing), false);
				break;
			case BingHybrid:
				mapSelected = Providers.BingHybrid;
				setNewTileFactory(new BingTileFactoryInfo(BingTileFactoryInfo.AERIALWITHLABELS, mapApiKey.bing), false);
				break;
			case BingSat:
				mapSelected = Providers.BingSat;
				setNewTileFactory(new BingTileFactoryInfo(BingTileFactoryInfo.AERIAL, mapApiKey.bing), false);
				break;
			case TomTomRoad:
				mapSelected = Providers.TomTomRoad;
				setNewTileFactory(new TomTomTileFactoryInfo(TomTomTileFactoryInfo.MAP, mapApiKey.tomtom), false);
				break;
			case HereRoad:
				mapSelected = Providers.HereRoad;
				setNewTileFactory(new HereTileFactoryInfo(HereTileFactoryInfo.ROAD, mapApiKey.here), false);
				break;
			case HereHybrid:
				mapSelected = Providers.HereHybrid;
				setNewTileFactory(new HereTileFactoryInfo(HereTileFactoryInfo.SATWITHLABELS, mapApiKey.here), false);
				break;
			case HereSat:
				mapSelected = Providers.HereSat;
				setNewTileFactory(new HereTileFactoryInfo(HereTileFactoryInfo.SAT, mapApiKey.here), false);
				break;
			case HereTerrain:
				mapSelected = Providers.HereTerrain;
				setNewTileFactory(new HereTileFactoryInfo(HereTileFactoryInfo.TERRAINWITHLABELS, mapApiKey.here), false);
				break;
			case YandexRoad:
				mapSelected = Providers.YandexRoad;
				setNewTileFactory(new YandexTileFactoryInfo(YandexTileFactoryInfo.ROAD), true);
				break;
			case YandexHybrid:
				mapSelected = Providers.YandexHybrid;
				setNewTileFactory(new YandexTileFactoryInfo(YandexTileFactoryInfo.SATWITHLABELS), true);
				break;
			case YandexSat:
				mapSelected = Providers.YandexSat;
				setNewTileFactory(new YandexTileFactoryInfo(YandexTileFactoryInfo.SAT), true);
				break;
			case IgnMaps:
				mapSelected = Providers.IgnMaps;
				setNewTileFactory(new IGNTileFactoryInfo(IGNTileFactoryInfo.MAPS, mapApiKey.ign), false);
				break;
			case IgnMapsClassic:
				mapSelected = Providers.IgnMapsClassic;
				setNewTileFactory(new IGNTileFactoryInfo(IGNTileFactoryInfo.MAPSCLASSIC), false);
				break;
			case IgnMapsCadastral:
				mapSelected = Providers.IgnMapsCadastral;
				setNewTileFactory(new IGNTileFactoryInfo(IGNTileFactoryInfo.MAPSCADASTRAL, mapApiKey.ign), false);
				break;
			case IgnPhotosCadastral:
				mapSelected = Providers.IgnPhotosCadastral;
				setNewTileFactory(new IGNTileFactoryInfo(IGNTileFactoryInfo.PHOTOSCADASTRAL), false);
				break;
			case IgnPhotos:
				mapSelected = Providers.IgnPhotos;
				setNewTileFactory(new IGNTileFactoryInfo(IGNTileFactoryInfo.PHOTOS), false);
				break;
			case IgnMapsScanOACI:
				mapSelected = Providers.IgnMapsScanOACI;
				setNewTileFactory(new IGNTileFactoryInfo(IGNTileFactoryInfo.MAPSSCANOACI, mapApiKey.ign), false);
				break;
			case IgnMapsScan25:
				mapSelected = Providers.IgnMapsScan25;
				setNewTileFactory(new IGNTileFactoryInfo(IGNTileFactoryInfo.MAPSSCAN25, mapApiKey.ign), false);
				break;
			default:
				mapSelected = Providers.OSMRoad;
				setNewTileFactory(new OSMTileFactoryInfo(), false);
				break;
		}
		
		Config.getInstance().setValue("panelmap.provider", mapSelected.toString());
	}
	
	@Override
	public void onGeoCodeSuccess(double latitude, double longitude) {
		if (loadingDialog != null) {
			loadingDialog.dispose();
			loadingDialog = null;
		}
		
		SpudMap.this.centerOnPosition(latitude, longitude);
		TileFactoryInfo info = SpudMap.this.getMainMap().getTileFactory().getInfo();
		SpudMap.this.setZoom(Math.min(info.getMinimumZoomLevel()+2, info.getMaximumZoomLevel()));
	}

	@Override
	public void onGeoCodeError(String msg) {
		if (loadingDialog != null) {
			loadingDialog.dispose();
			loadingDialog = null;
		}
		
		JOptionPane.showMessageDialog(this, msg, Config.getLangBundle().getString("mapGoToAddress"), JOptionPane.ERROR_MESSAGE, null);
	}
	
	ActionListener OpenSettingsAction = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent ae) {
			SpudMap.this.panelMapListener.onMapSettings();
		}
	};
	
	ActionListener ChangeProviderAction = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent ae) {
			selectNewTileFactory(Providers.valueOf(ae.getActionCommand()));
		}
	};
	
	ActionListener ShowHelpAction = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent ae) {
			JOptionPane.showMessageDialog(SwingUtilities.windowForComponent(SpudMap.this), 
					new JLabel(Config.getLangBundle().getString("mapHelpMessage")), 
					Config.getLangBundle().getString("mapHelp"), 
			        JOptionPane.INFORMATION_MESSAGE,
			        null);
		}
	};
	
	ActionListener OpenStreetView = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent ae) {
			GeoPosition pos;
			if (cursorPos != null)
				pos = cursorPos;
			else
				pos = SpudMap.this.getCenterPosition();
			
			String url = String.format(Locale.US, "https://www.google.com/maps?q&layer=c&cbll=%f,%f", 
					pos.getLatitude(), pos.getLongitude());

	        if (Desktop.isDesktopSupported()) {
	            Desktop desktop = Desktop.getDesktop();
	            try {
	                desktop.browse(new URI(url));
	            } catch (Exception e) {}
	        }
		}
	};
	
	ActionListener GoToPositionAction = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent ae) {
			GeoPosition pos;
			if (cursorPos != null)
				pos = cursorPos;
			else
				pos = SpudMap.this.getCenterPosition();
			
			DialogLatLng dll = new DialogLatLng(SwingUtilities.windowForComponent(SpudMap.this), 
					pos.getLatitude(), pos.getLongitude());
			dll.setVisible(true);
			
			if (dll.getLatitude() < 90.0 && dll.getLatitude() > -90.0 &&
					dll.getLongitude() < 180.0 && dll.getLongitude() > -180.0) {
				SpudMap.this.centerOnPosition(dll.getLatitude(), dll.getLongitude());
				TileFactoryInfo info = SpudMap.this.getMainMap().getTileFactory().getInfo();
				SpudMap.this.setZoom(Math.min(info.getMinimumZoomLevel()+2, info.getMaximumZoomLevel()));
			}
			
			dll.dispose();
		}
	};
	
	ActionListener GoToAddressAction = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent ae) {
			DialogAddress dit = new DialogAddress(SwingUtilities.windowForComponent(SpudMap.this));
			dit.setVisible(true);
			
			if (dit.getTypedAddress().length() > 0) {
				new GeoCodeThread(mapApiKey.here, dit.getTypedAddress(), SpudMap.this).start();
				
				if (loadingDialog == null) {
					JProgressBar progressBar = new JProgressBar();
					progressBar.setIndeterminate(true);
					progressBar.setVisible(true);
					
					loadingDialog = new JDialog(SwingUtilities.windowForComponent(SpudMap.this), "Loading", ModalityType.APPLICATION_MODAL);
					loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
					loadingDialog.add(BorderLayout.CENTER, progressBar);
					loadingDialog.setSize(300, 75);
					loadingDialog.setLocationRelativeTo(SwingUtilities.windowForComponent(SpudMap.this));
					loadingDialog.setVisible(true);
				}
			}
			
			dit.dispose();
		}
	};
	
	public interface PanelMapListener {
		public void onMarkerClicked(MouseEvent evt);
		public void onAddLine(double lat, double lon);
		public void onMapSettings();
	}
	
	private class MapApiKey
	{				
		public MapApiKey()
		{
			InputStream is = null;
			
			try {
				int i = 0;
				byte[] ww = new byte[16];				
				is = getClass().getResourceAsStream(new String(new byte[] { 0x2F, 0x73, 0x70, 0x75, 0x64, 0x69, 0x67, 0x6F, 0x2F, 0x69, 
						0x6D, 0x61, 0x67, 0x65, 0x73, 0x2F, 0x67, 0x61, 0x6E, 0x7A, 0x6F, 0x6F, 0x2E, 0x67, 0x69, 0x66 }, "US-ASCII"));
				
				for (; i < 16; i++)
				{
					is.skip(80);
					ww[i] = (byte) is.read();
				}
				
				Key aesKey = new SecretKeySpec(ww, "AES");
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
								
				byte[] zz = new byte[0xA0];
				is.skip(424 + 16 * 81);
				is.read(zz);
				
	            cipher.init(Cipher.DECRYPT_MODE, aesKey);
	            String decrypted = new String(cipher.doFinal(zz));
	            String[] values = decrypted.split("\t");

	            i = 0;
	            bing = values[i++];
				tomtom = values[i++];
				here = values[i++];
				ign = values[i++];
			} catch (Exception e) {
				// Nothing to log
			} finally {
				try
				{
					if (is != null)
						is.close();
				} catch (IOException ex) { }
			}
			
			String value;
			value = Config.getInstance().getString("map.provider.bing.key");			
			if (value != null && value.length() > 0)
				bing = value;
			
			value = Config.getInstance().getString("map.provider.tomtom.key");			
			if (value != null && value.length() > 0)
				tomtom = value;
			
			value = Config.getInstance().getString("map.provider.here.key");			
			if (value != null && value.length() > 0)
				here = value;
			
			value = Config.getInstance().getString("map.provider.ign.key");			
			if (value != null && value.length() > 0)
				ign = value;
		}
		
		public String bing;
		public String tomtom;
		public String here;
		public String ign;
	}
}
