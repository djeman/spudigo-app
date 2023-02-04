package org.jxmapviewer.viewer.epsg3395;

import java.awt.geom.Point2D;

import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

public class Epsg3395TileFactory extends DefaultTileFactory {

	public Epsg3395TileFactory(TileFactoryInfo info) {
		super(info);
	}

	@Override
	public GeoPosition pixelToGeo(Point2D pix, int zoom)
	{
		double wx = pix.getX();
		double wy = pix.getY();

		double flon = (wx - getInfo().getMapCenterInPixelsAtZoom(zoom).getX()) / getInfo().getLongitudeDegreeWidthInPixels(zoom);
		double e1 = (wy - getInfo().getMapCenterInPixelsAtZoom(zoom).getY())
				/ (-1 * getInfo().getLongitudeRadianWidthInPixels(zoom));
		double e2 = (2 * Math.atan(Math.exp(e1)) - Math.PI / 2);

		double yy = Math.abs(wy - getInfo().getMapCenterInPixelsAtZoom(zoom).getY());
		int mapsize = getInfo().getMapWidthInTilesAtZoom(zoom) * getInfo().getTileSize(zoom);
		double e3 = Math.exp((2 * yy) / (mapsize / (2 * Math.PI)));
		
		double ex = 0.0818191908426;
		
		double v0 = e2 + 1;
		while ((Math.abs(v0 - e2) > 0.0000001)) {
			v0 = e2;
	        e2 = Math.asin(1 - ((1 + Math.sin(v0)) * Math.pow(1 - ex * Math.sin(v0), ex))
	                       / (e3 * Math.pow(1 + ex * Math.sin(v0), ex)));
	    }

		e2 *= (180 / Math.PI);
		
		double flat = (wy > getInfo().getMapCenterInPixelsAtZoom(zoom).getY()) ? -e2 : e2;
        
		GeoPosition wc = new GeoPosition(flat, flon);
		return wc;
	}

	@Override
	public Point2D geoToPixel(GeoPosition c, int zoom)
	{
		double x = getInfo().getMapCenterInPixelsAtZoom(zoom).getX() + c.getLongitude()
				* getInfo().getLongitudeDegreeWidthInPixels(zoom);
		double e = Math.sin(c.getLatitude() * (Math.PI / 180.0));
		if (e > 0.9999)
		{
			e = 0.9999;
		} 
		else if (e < -0.9999)
		{
			e = -0.9999;
		}
		double ex = 0.0818191908426;
		double y = Math.abs(getInfo().getMapCenterInPixelsAtZoom(zoom).getY() - getInfo().getLongitudeRadianWidthInPixels(zoom) *
				((0.5 * Math.log((1 + e) / (1 - e))) - ex * (0.5 * Math.log((1 + (e * ex)) / (1 - (e * ex))))));
		
		return new Point2D.Double(x, y);
	}
	
}
