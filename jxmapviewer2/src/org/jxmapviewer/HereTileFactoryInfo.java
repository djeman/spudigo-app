package org.jxmapviewer;

import java.util.Locale;

import org.jxmapviewer.viewer.TileFactoryInfo;

public class HereTileFactoryInfo extends TileFactoryInfo {
	
	/**
	 * Use road map
	 */
	public final static MVEMode ROAD = new MVEMode("Road", "explore.day");

	/**
	 * Use satellite map
	 */
	public final static MVEMode SAT = new MVEMode("Satellite", "satellite.day");

	/**
	 * Use satellite with labels map
	 */
	public final static MVEMode SATWITHLABELS = new MVEMode("Satellite with labels", "explore.satellite.day");

	/**
	 * Use terrain with labels map
	 */
	public final static MVEMode TERRAINWITHLABELS = new MVEMode("Terrain with labels", "topo.day");
	
	/**
	 * The map mode
	 */
	public static class MVEMode {
		private String name;
		private String parameter;

		private MVEMode(final String name, final String parameter) {
			this.name = name;
			this.parameter = parameter;
		}
	}

	private final static int TOP_ZOOM_LEVEL = 20;
	private final static int MAX_ZOOM_LEVEL = 18;
	private final static int MIN_ZOOM_LEVEL = 0;
	private final static int TILE_SIZE = 256;

	private MVEMode mode;
	private String culture;
	
	private String apiKey;
	
	private static final String copyright = "© 2024 HERE";

	/**
	 * @param mode the mode
	 */
	public HereTileFactoryInfo(MVEMode mode, String apiKey)
	{
		super("Here Maps", MIN_ZOOM_LEVEL, MAX_ZOOM_LEVEL, TOP_ZOOM_LEVEL, TILE_SIZE, 
				false, false, "", "", "", "");
		
		this.mode = mode;
		this.apiKey = apiKey;
		this.culture = Locale.getDefault().getLanguage();
	}
	
	public String getModeName() {
		return mode.name;
	}
	
	@Override
	public String getTileUrl(final int x, final int y, final int zoom)
	{
		return "https://maps.hereapi.com/v3/base/mc/" + (TOP_ZOOM_LEVEL - zoom) + "/" + x + "/" + y + "/jpeg?" + 
				"style=" + this.mode.parameter + "&size=" + TILE_SIZE + "&lang=" + this.culture + "&apiKey=" + this.apiKey;
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
