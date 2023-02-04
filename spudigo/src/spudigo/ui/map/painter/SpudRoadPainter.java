package spudigo.ui.map.painter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.AbstractPainter;
import org.jxmapviewer.viewer.GeoPosition;

public class SpudRoadPainter extends AbstractPainter<JXMapViewer> {

	private GeoPosition[] roadDirect = null;
	private GeoPosition[] roadOSMtoGo = null;
	private GeoPosition[] roadOSMtoBack = null;
	
	public void setRoadDirect(GeoPosition[] road) {
		roadDirect = road;
	}
	
	public void setRoadOSMtoGo(GeoPosition[] road) {
		roadOSMtoGo = road;
	}
	
	public void setRoadOSMtoBack(GeoPosition[] road) {
		roadOSMtoBack = road;
	}
	
	public void clearAllRoads() {
		roadDirect = null;
		roadOSMtoGo = null;
		roadOSMtoBack = null;
	}
	
	@Override
	protected void doPaint(Graphics2D g, JXMapViewer map, int width, int height) {
		Rectangle viewportBounds = map.getViewportBounds();
		g.translate(-viewportBounds.getX(), -viewportBounds.getY());
		
		g.setStroke(new BasicStroke(3.0f,                      // Width
                BasicStroke.CAP_ROUND,    // End cap
                BasicStroke.JOIN_ROUND,    // Join style
                30.0f,                     // Miter limit
                new float[] {15.0f,8.0f,2.0f,8.0f}, // Dash pattern
                0.0f)); // Vertex join style
		
		if (roadDirect != null) {
			g.setColor(Color.BLACK);
			g.draw(getPolyline(roadDirect, map, viewportBounds));
		}
		
		if (roadOSMtoGo != null) {
			g.setColor(Color.BLUE);
			g.draw(getPolyline(roadOSMtoGo, map, viewportBounds));
		}
		
		if (roadOSMtoBack != null) {
			g.setColor(new Color(0x008516));
			g.draw(getPolyline(roadOSMtoBack, map, viewportBounds));
		}
		
		g.translate(viewportBounds.getX(), viewportBounds.getY());
	}

	private GeneralPath getPolyline(GeoPosition[] road, JXMapViewer map, Rectangle viewportBounds) {
		GeneralPath polyline = new GeneralPath(GeneralPath.WIND_EVEN_ODD, road.length);
		Point2D p = map.getTileFactory().geoToPixel(road[0], map.getZoom());
		
		boolean isFirst = true, beforeV = false, nextV = false, currentV = false;
		if (road.length == 2)
			beforeV = true;
		
		for (int i=0;i<road.length;i++) {
			p = map.getTileFactory().geoToPixel(road[i], map.getZoom());
			if (i == (road.length -1))
				nextV = (road.length == 2)?true:false;
			else
				nextV = checkVisible(map.getTileFactory().geoToPixel(road[i+1], map.getZoom()), viewportBounds);
			
			currentV = checkVisible(p, viewportBounds);
			
			if (isFirst && (currentV || beforeV || nextV)) {
				polyline.moveTo(p.getX(), p.getY());
				isFirst = false;
			} else if (currentV || beforeV || nextV) {
				polyline.lineTo(p.getX(), p.getY());
			}
			
			beforeV = currentV;
		}
		
		return polyline;
	}

	private boolean checkVisible(Point2D p, Rectangle viewportBounds) {
		if (viewportBounds.getX() > p.getX() || (viewportBounds.getX() + viewportBounds.getWidth()) < p.getX() ||
				viewportBounds.getY() > p.getY() || (viewportBounds.getY() + viewportBounds.getHeight()) < p.getY())
			return false;
		
		return true;
	}
}
