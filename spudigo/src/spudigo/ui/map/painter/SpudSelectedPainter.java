package spudigo.ui.map.painter;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.AbstractPainter;

import spudigo.SpudItem;
import spudigo.ui.map.renderer.SpudSelectedRenderer;

public class SpudSelectedPainter extends AbstractPainter<JXMapViewer> {
	private SpudSelectedRenderer renderer;
	private Lock l = new ReentrantLock();
	private SpudItem[] waypoints;	

	public SpudSelectedPainter() {		
		setAntialiasing(true);
		setCacheable(false);
	}
	
	public void setRenderer(SpudSelectedRenderer r) {
		this.renderer = r;
	}
	
	public SpudItem[] getWaypoints() {
		return waypoints;
	}
	
	public void setWaypoints(SpudItem[] waypoints) {
		l.lock();
		this.waypoints = waypoints;
		l.unlock();
	}
	
	@Override
	protected void doPaint(Graphics2D g, JXMapViewer map, int width, int height) {
		if (renderer == null || waypoints == null || map == null || g == null)
			return;

		Rectangle viewportBounds = map.getViewportBounds();
		
		int lonWidth = (int) map.getTileFactory().getInfo().getLongitudeDegreeWidthInPixels(map.getZoom());
		int radius = renderer.prepareToPaint(map, lonWidth);
		double midRadius = (double)radius/2;
		
		ArrayList<SpudItem> temp = new ArrayList<SpudItem>();
		ArrayList<Point2D> tempPoint = new ArrayList<Point2D>();
		l.lock();
		for (SpudItem item : getWaypoints()) {
			Point2D point = map.getTileFactory().geoToPixel(item.getPosition(), map.getZoom());
			
			if (viewportBounds.getX() > (point.getX() + midRadius) || (viewportBounds.getX() + viewportBounds.getWidth()) < (point.getX() - midRadius) ||
					viewportBounds.getY() > (point.getY() + midRadius) || (viewportBounds.getY() + viewportBounds.getHeight()) < (point.getY() - midRadius))
				continue;
			
			temp.add(item);
			tempPoint.add(point);
		}
		l.unlock();

		g.translate(-viewportBounds.getX(), -viewportBounds.getY());

		for (int i=0;i<temp.size();i++)
			renderer.paintWaypoint(g, tempPoint.get(i), temp.get(i));

		g.translate(viewportBounds.getX(), viewportBounds.getY());
	}
}
