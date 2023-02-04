package org.jxmapviewer;

import java.util.Locale;

import org.jxmapviewer.viewer.TileFactoryInfo;

public class HereTileFactoryInfo extends TileFactoryInfo {
	
	/**
	 * Use road map
	 */
	public final static MVEMode ROAD = new MVEMode("Road", "base", "normal.day");

	/**
	 * Use satellite map
	 */
	public final static MVEMode SAT = new MVEMode("Satellite", "aerial" ,"satellite.day");

	/**
	 * Use satellite with labels map
	 */
	public final static MVEMode SATWITHLABELS = new MVEMode("Satellite with labels", "aerial", "hybrid.day");

	/**
	 * Use terrain with labels map
	 */
	public final static MVEMode TERRAINWITHLABELS = new MVEMode("Terrain with labels", "aerial", "terrain.day");
	
	/**
	 * The map mode
	 */
	public static class MVEMode {
		private String name;
		private String type;
		private String scheme;

		private MVEMode(final String name, final String type, final String scheme) {
			this.name = name;
			this.type = type;
			this.scheme = scheme;
		}
	}

	private final static int TOP_ZOOM_LEVEL = 20;
	private final static int MAX_ZOOM_LEVEL = 18;
	private final static int MIN_ZOOM_LEVEL = 0;
	private final static int TILE_SIZE = 256;

	private MVEMode mode;
	private String culture;
	
	private String apiKey;
	
	private static final String copyright = "© 2023 HERE";

	/**
	 * @param mode the mode
	 */
	public HereTileFactoryInfo(MVEMode mode, String apiKey)
	{
		super("Here Maps", MIN_ZOOM_LEVEL, MAX_ZOOM_LEVEL, TOP_ZOOM_LEVEL, TILE_SIZE, 
				false, false, "", "", "", "");
		
		this.mode = mode;
		this.apiKey = apiKey;
		this.culture = Locale.getDefault().getISO3Language();
	}
	
	public String getModeName() {
		return mode.name;
	}
	
	@Override
	public String getTileUrl(final int x, final int y, final int zoom)
	{
		return "https://1." + mode.type + ".maps.ls.hereapi.com/maptile/2.1/maptile/newest/" +
				mode.scheme + "/" + (TOP_ZOOM_LEVEL - zoom) + "/" + x + "/" + y + "/" + TILE_SIZE + 
				"/jpg?apiKey=" + this.apiKey + "&lg=" + this.culture;
	}
	
	@Override
	public int getDefaultZoomLevel() 
	{
		return MAX_ZOOM_LEVEL;
	}
	
	@Override
	public String getCopyright() {
		return copyright;
	}
}
