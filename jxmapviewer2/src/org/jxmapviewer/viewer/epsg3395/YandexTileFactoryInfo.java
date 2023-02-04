package org.jxmapviewer.viewer.epsg3395;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.jxmapviewer.viewer.TileFactoryInfo;

public class YandexTileFactoryInfo extends TileFactoryInfo {

	private static final String copyright = "© Yandex";
	private static final String urlSkl = "https://core-renderer-tiles.maps.yandex.net/tiles?l=skl";
	
	/**
	 * Use road map
	 */
	public final static MVEMode ROAD = new MVEMode("Road", "https://core-renderer-tiles.maps.yandex.net/tiles?l=map", false);

	/**
	 * Use satellite map
	 */
	public final static MVEMode SAT = new MVEMode("Satellite", "https://core-sat.maps.yandex.net/tiles?l=sat", false);

	/**
	 * Use satellite with labels map
	 */
	public final static MVEMode SATWITHLABELS = new MVEMode("Satellite with labels", "https://core-sat.maps.yandex.net/tiles?l=sat", true);

	/**
	 * The map mode
	 */
	public static class MVEMode {
		private String name;
		private String url;
		private boolean enableSkl;

		private MVEMode(final String name, final String url, final boolean enableSkl) {
			this.name = name;
			this.url = url;
			this.enableSkl = enableSkl;
		}
	}
	
	private final static int TOP_ZOOM_LEVEL = 19;
	private final static int MAX_ZOOM_LEVEL = 17;
	private final static int MIN_ZOOM_LEVEL = 0;
	private final static int TILE_SIZE = 256;
	
	private MVEMode mode;
	private String culture;
	
	/**
	 * Default constructor
	 */
	public YandexTileFactoryInfo(MVEMode mode) {
		super("Yandex Maps", 
				MIN_ZOOM_LEVEL, MAX_ZOOM_LEVEL, TOP_ZOOM_LEVEL, 
				TILE_SIZE, true, true, 
				"",	"", "", "");

		this.mode = mode;
		this.culture = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
	}

	/**
	 * @return the name of the selected mode
	 */
	public String getModeName()
	{
		return mode.name;
	}

	@Override
	public String getTileUrl(int x, int y, int zoom)
	{
		zoom = TOP_ZOOM_LEVEL - zoom;
		String url = this.mode.url + "&x=" + x + "&y=" + y + "&z=" + zoom + "&scale=1&lang=" + this.culture;
		return url;
	}

	@Override
	public List<String> getOverlayTiles(int x, int y, int zoom)
	{
		if (this.mode.enableSkl) {
			List<String> tiles = new ArrayList<String>();
			zoom = TOP_ZOOM_LEVEL - zoom;
			String url = urlSkl + "&x=" + x + "&y=" + y + "&z=" + zoom + "&scale=1&lang=" + this.culture;
			tiles.add(url);
			return tiles;
		} else {
			return null;
		}
	}

	@Override
	public int getDefaultZoomLevel() 
	{
		return MAX_ZOOM_LEVEL;
	}
	
	@Override
	public String getCopyright()
	{
		return copyright;
	}
}
