package spudigo.ui.map.painter;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.AbstractPainter;

import spudigo.Config;
import spudigo.SpudItem;
import spudigo.ui.map.renderer.SpudPoiRenderer;

public class SpudPoiPainter extends AbstractPainter<JXMapViewer> {
	private SpudPoiRenderer renderer;
	private Lock l = new ReentrantLock();
	private ArrayList<SpudItem> waypoints = new ArrayList<SpudItem>();
	private int zoneBeginCount, zoneSizeMini;

	public SpudPoiPainter() {
		setAntialiasing(true);
		setCacheable(false);
		
		init();
	}
	
	public void reload() {
		init();
	}
	
	private void init() {
		this.zoneBeginCount = Config.getInstance().getInteger("zone.begin.count");
		this.zoneSizeMini = Config.getInstance().getInteger("zone.size.mini");
	}
	
	public void setRenderer(SpudPoiRenderer r) {
		this.renderer = r;
	}
	
	public ArrayList<SpudItem> getWaypoints() {
		return waypoints;
	}
	
	public void setWaypoints(ArrayList<SpudItem> waypoints) {
		l.lock();
		this.waypoints.clear();
		this.waypoints.addAll(waypoints);
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
		
		if (temp.size() > this.zoneBeginCount) {
			temp.clear();
			
			int maxSeg = 16, size = tempPoint.size();
			if (size > 400000)
				maxSeg = 8;
			else if (size > 200000)
				maxSeg = 12;
			else if (size > 100000)
				maxSeg = 14;
			
			int[] segLenW = getSegmentWLen(viewportBounds, maxSeg);
			int[] segLenH = getSegmentHLen(viewportBounds, maxSeg);
			
			Map<Point2D, Integer> newTemp = groupPoiInZone(viewportBounds, tempPoint, map, segLenW, segLenH);
			
			g.translate(-viewportBounds.getX(), -viewportBounds.getY());
			
			for(Object key : newTemp.keySet())
			    renderer.paintZone(g, (Point2D)key, (int)newTemp.get(key), 
			    		segLenW[1], segLenH[1]);
		} else {
			g.translate(-viewportBounds.getX(), -viewportBounds.getY());
			
			for (int i=0;i<temp.size();i++)
				renderer.paintWaypoint(g, tempPoint.get(i), temp.get(i));
		}
		
		g.translate(viewportBounds.getX(), viewportBounds.getY());
	}
	
	private int[] getSegmentWLen(Rectangle bounds, int maxSeg) {
		int[] segLenW = new int[]{maxSeg, bounds.width / maxSeg};
		
		while (segLenW[1] < this.zoneSizeMini && segLenW[0] > 1) {
			segLenW[0]--;
			segLenW[1] = bounds.width / segLenW[0];
		}
		
		return segLenW;
	}
	
	private int[] getSegmentHLen(Rectangle bounds, int maxSeg) {
		int[] segLenH = new int[]{maxSeg, bounds.height / maxSeg};
		
		while (segLenH[1] < this.zoneSizeMini && segLenH[0] > 1) {
			segLenH[0]--;
			segLenH[1] = bounds.height / segLenH[0];
		}
		
		return segLenH;
	}

	private Map<Point2D, Integer> groupPoiInZone(Rectangle bounds, ArrayList<Point2D> tempPoint, JXMapViewer map, int[] segLenW, int[] segLenH) {
		Rectangle zone[] = new Rectangle[segLenW[0]*segLenH[0]];
		Point2D posi[] = new Point2D[segLenW[0]*segLenH[0]];
		
		int index = 0;
		for (int a=0;a<segLenW[0];a++) {
			for (int b=0;b<segLenH[0];b++) {
				zone[index] = new Rectangle(bounds.x + segLenW[1]*a, bounds.y + segLenH[1]*b, segLenW[1], segLenH[1]);
				posi[index] = new Point2D.Double(zone[index].getCenterX(), zone[index].getCenterY());
				index++;
			}
		}
		
		Map<Point2D, Integer> res = new HashMap<Point2D, Integer>();
		for (Point2D point : tempPoint) {
			for (int i=0;i<zone.length;i++) {
				if (zone[i].contains(point)) {
					if (res.get(posi[i]) == null)
						res.put(posi[i], 1);
					else
						res.put(posi[i], res.get(posi[i])+1);
					
					break;
				}
			}
		}
		
		return res;
	}
}
