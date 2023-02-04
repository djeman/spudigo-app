package org.jxmapviewer;

import org.jxmapviewer.viewer.TileFactoryInfo;

public class TomTomTileFactoryInfo extends TileFactoryInfo {
	
	/**
	 * Use full tiles
	 */
	public final static MVEMode MAP = new MVEMode("Standard map tiles", "1");

	/**
	 * Use overlay tiles
	 */
	public final static MVEMode MAPOV = new MVEMode("Standard label overlay", "1-labels");

	/**
	 * Use hybrid overlay tiles
	 */
	public final static MVEMode HYBRIDOV = new MVEMode("Hybrid overlay", "1-hybrid");

	/**
	 * The map mode
	 */
	public static class MVEMode {
		private String type;
		private String name;

		private MVEMode(final String name, final String type) {
			this.type = type;
			this.name = name;
		}
	}

	private final static int TOP_ZOOM_LEVEL = 19;
	private final static int MAX_ZOOM_LEVEL = 17;
	private final static int MIN_ZOOM_LEVEL = 0;
	private final static int TILE_SIZE = 256;

	private MVEMode mode;
	private String mapKey;
	
	private static final String copyright = "Map Data © 1992-2023 TomTom";

	/**
	 * @param mode the mode
	 */
	public TomTomTileFactoryInfo(MVEMode mode, String mapKey)
	{
		super("TomTom Maps", MIN_ZOOM_LEVEL, MAX_ZOOM_LEVEL, TOP_ZOOM_LEVEL, TILE_SIZE, 
				false, false, "https://api.tomtom.com/lbs/map/3/basic", "", "", "");
		
		this.mode = mode;
		this.mapKey = mapKey;
	}

	/**
	 * @return the name of the selected mode
	 */
	public String getModeName()
	{
		return mode.name;
	}
	
	/**
	 * @return the label of the selected mode
	 */
	public String getModeLabel()
	{
		return mode.name;
	}
	
	@Override
	public String getTileUrl(final int x, final int y, final int zoom)
	{
		return this.baseURL + "/" + mode.type + "/" + (TOP_ZOOM_LEVEL - zoom) + "/" + x + "/" + y + ".png?key=" + this.mapKey;
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
