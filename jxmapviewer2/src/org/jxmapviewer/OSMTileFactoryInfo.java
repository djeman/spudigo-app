
package org.jxmapviewer;

import org.jxmapviewer.viewer.TileFactoryInfo;

/**
 * Uses OpenStreetMap
 * @author Martin Dummer
 */
public class OSMTileFactoryInfo extends TileFactoryInfo
{
	private static final String copyright = "© OpenStreetMap";
	private static final int max = 20;

	/**
	 * Default constructor
	 */
	public OSMTileFactoryInfo()
	{
		super("OpenStreetMap", 
				1, max - 2, max, 
				256, true, true, 					// tile size is 256 and x/y orientation is normal
				"https://tile.openstreetmap.org",
				"", "", "");						// 5/15/10.png
	}

	@Override
	public String getTileUrl(int x, int y, int zoom)
	{
		zoom = max - zoom;
		String url = this.baseURL + "/" + zoom + "/" + x + "/" + y + ".png";
		return url;
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
