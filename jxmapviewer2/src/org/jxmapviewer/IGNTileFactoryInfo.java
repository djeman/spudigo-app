
package org.jxmapviewer;

import java.util.ArrayList;
import java.util.List;

import org.jxmapviewer.viewer.TileFactoryInfo;

public class IGNTileFactoryInfo extends TileFactoryInfo
{
	private static final String copyright = "© Institut national de l'information géographique et forestière (IGN)";
	
	private final static int MAPS_TOP_ZOOM_LEVEL = 19;
	private final static int MAPS_MAX_ZOOM_LEVEL = 17;
	private final static int MAPSCLASSIC_TOP_ZOOM_LEVEL = 20;
	private final static int MAPSCLASSIC_MAX_ZOOM_LEVEL = 18;
	private final static int AERIAL_TOP_ZOOM_LEVEL = 20;
	private final static int AERIAL_MAX_ZOOM_LEVEL = 18;
	private final static int MIN_ZOOM_LEVEL = 0;
	private final static int TILE_SIZE = 256;
	
	private final static int MAPSSCANOACI_TOP_ZOOM_LEVEL = 12;
	private final static int MAPSSCANOACI_MAX_ZOOM_LEVEL = 10;
	
	private final static int MAPSSCAN25_TOP_ZOOM_LEVEL = 17;
	private final static int MAPSSCAN25_MAX_ZOOM_LEVEL = 15;
	
	/**
	 * Use road map
	 */
	public final static MVEMode MAPSCLASSIC = new MVEMode("Maps Classic", 
			"GEOGRAPHICALGRIDSYSTEMS.PLANIGNV2", "image/png", false,
			MAPSCLASSIC_MAX_ZOOM_LEVEL, MAPSCLASSIC_TOP_ZOOM_LEVEL);
	
	public final static MVEMode MAPS = new MVEMode("Maps", 
			"GEOGRAPHICALGRIDSYSTEMS.MAPS", "image/jpeg", false,
			MAPS_MAX_ZOOM_LEVEL, MAPS_TOP_ZOOM_LEVEL);

	public final static MVEMode MAPSCADASTRAL = new MVEMode("Maps Cadastral", 
			"GEOGRAPHICALGRIDSYSTEMS.MAPS", "image/jpeg", true,
			MAPS_MAX_ZOOM_LEVEL, MAPS_TOP_ZOOM_LEVEL);

	public final static MVEMode PHOTOSCADASTRAL = new MVEMode("Photos Cadastral", 
			"ORTHOIMAGERY.ORTHOPHOTOS", "image/jpeg", true,
			AERIAL_MAX_ZOOM_LEVEL, AERIAL_TOP_ZOOM_LEVEL);
	
	public final static MVEMode PHOTOS = new MVEMode("Photos", 
			"ORTHOIMAGERY.ORTHOPHOTOS", "image/jpeg", false,
			AERIAL_MAX_ZOOM_LEVEL, AERIAL_TOP_ZOOM_LEVEL);
	
	public final static MVEMode MAPSSCANOACI = new MVEMode("Maps Scan OACI", 
			"GEOGRAPHICALGRIDSYSTEMS.MAPS.SCAN-OACI", "image/jpeg", false,
			MAPSSCANOACI_MAX_ZOOM_LEVEL, MAPSSCANOACI_TOP_ZOOM_LEVEL);
	
	public final static MVEMode MAPSSCAN25 = new MVEMode("Maps Scan 25", 
			"GEOGRAPHICALGRIDSYSTEMS.MAPS.SCAN25TOUR", "image/jpeg", false,
			MAPSSCAN25_MAX_ZOOM_LEVEL, MAPSSCAN25_TOP_ZOOM_LEVEL);
		
	/**
	 * The map mode
	 */
	public static class MVEMode {
		private String name;
		private String layer;
		private String format;
		private boolean enableCadastral;
		private int maxZoom, topZoom;

		private MVEMode(final String name, final String layer, 
				final String format, final boolean enableCadastral,
				int maxZoom, int topZoom) {
			this.name = name;
			this.layer = layer;
			this.format = format;
			this.enableCadastral = enableCadastral;
			this.maxZoom = maxZoom;
			this.topZoom = topZoom;
		}
	}

	private MVEMode mode;
	
	private String parcelBaseUrl;
	
	public IGNTileFactoryInfo(MVEMode mode, String key, String parcelKey) {
		super("IGN", 
				MIN_ZOOM_LEVEL, mode.maxZoom, mode.topZoom, 
				TILE_SIZE, true, true,
				"https://wxs.ign.fr/" + key + 
				"/geoportail/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&TILEMATRIXSET=PM&",
				"", "", "");
		
		this.parcelBaseUrl = "https://wxs.ign.fr/" + parcelKey + 
				"/geoportail/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&TILEMATRIXSET=PM&LAYER=CADASTRALPARCELS.PARCELS&";
		
		this.mode = mode;
	}
	
	public String getModeName() {
		return mode.name;
	}

	@Override
	public String getTileUrl(int x, int y, int zoom)
	{
		String url = this.baseURL + "LAYER=" + mode.layer + "&TILEMATRIX=" + (mode.topZoom - zoom) + 
				"&TILECOL=" + x + "&TILEROW=" + y + "&STYLE=normal&FORMAT=" + mode.format;
		return url;
	}

	@Override
	public List<String> getOverlayTiles(int x, int y, int zoom)
	{
		if (this.mode.enableCadastral) {
			List<String> tiles = new ArrayList<String>();
			String url = this.parcelBaseUrl + "TILEMATRIX=" + (mode.topZoom - zoom) + 
					"&TILECOL=" + x + "&TILEROW=" + y + "&STYLE=bdparcellaire&FORMAT=image/png";
			tiles.add(url);
			return tiles;
		} else {
			return null;
		}
	}

	@Override
	public int getDefaultZoomLevel() 
	{
		return this.getMaximumZoomLevel();
	}
	
	@Override
	public String getCopyright()
	{
		return copyright;
	}
}