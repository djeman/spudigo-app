package spudigo.ui.map.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointRenderer;

import spudigo.Config;
import spudigo.SpudItem;

public class SpudSelectedRenderer implements WaypointRenderer<SpudItem> {
	private BufferedImage imgSelected = null;
	private double factorX, factorY, midRadius = 0;
	private int arcAngle, lonWidth = 0, radius = 0;
	private Color colorSelOutline;
	private BasicStroke lineDashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
	        BasicStroke.JOIN_MITER, 2.0f, new float[]{ 2.0f }, 0.0f);
	
	private boolean showCompassSelected = true;
	
	public SpudSelectedRenderer(boolean showCompassSelected) {
		this.showCompassSelected = showCompassSelected;
		
		init();
	}
	
	public void setShowCompassSelected(boolean value) {
		this.showCompassSelected = value;
	}
	
	private void init() {
		try {
			initImg();
		} catch (Exception ex) {}
		
		this.factorX = Config.getInstance().getDouble("radius.factor.x");
		this.factorY = Config.getInstance().getDouble("radius.factor.y");
		this.arcAngle = Config.getInstance().getInteger("radius.arc.angle");
		this.colorSelOutline = new Color(Config.getInstance().getIntegerHexa("marker.outline.selected"));
	}
	
	private void initImg() throws Exception {
		String img = Config.getInstance().getString("marker.image.selected");
		try {
			if (img != null && img.length() > 0 && new File(img).exists())
				imgSelected = ImageIO.read(new File(img));
			else
				imgSelected = ImageIO.read(getClass().getResource("/spudigo/images/arrow.png"));
		} catch (Exception ex) {
			imgSelected = ImageIO.read(getClass().getResource("/spudigo/images/arrow.png"));
		}
	}
	
	@Override
	public void paintWaypoint(Graphics2D g, JXMapViewer map, SpudItem waypoint) {}
	
	public void paintWaypoint(Graphics2D g, Point2D point, SpudItem w) {
		Graphics2D g2d = (Graphics2D) g.create();
		Stroke oriStroke = g.getStroke();
		g2d.translate(point.getX(), point.getY());
		
		g2d.drawImage(imgSelected, 
				-imgSelected.getWidth() / 2, 
				-imgSelected.getHeight(), 
				null);
		
		if (lonWidth > 1000) {
			g2d.setColor(colorSelOutline);
			drawRotatedArrow(g2d, createUpArrow((float)(midRadius/3)), w.getDirection()%360, 0, 0);
			
			g2d.setColor(colorSelOutline);
			g2d.setStroke(lineDashed);
			switch (w.getDirType()) {
		        case 0: // ALL
		        	g2d.draw(new Ellipse2D.Double(-midRadius, -midRadius, radius, radius));
		        	break;
		        case 1: // UNI
		        	g2d.draw(new Arc2D.Double(-midRadius, -midRadius, radius, radius, 
		        			360 - ((w.getDirection()+90+(this.arcAngle*0.5))%360), this.arcAngle, Arc2D.PIE));
		        	break;
		        case 2: // BI
		        	g2d.draw(new Arc2D.Double(-midRadius, -midRadius, radius, radius, 
		        			360 - ((w.getDirection()+90+(this.arcAngle*0.5))%360), this.arcAngle, Arc2D.PIE));
		        	g2d.draw(new Arc2D.Double(-midRadius, -midRadius, radius, radius, 
		        			360 - ((w.getDirection()-90+(this.arcAngle*0.5))%360), this.arcAngle, Arc2D.PIE));
		        	break;
	        }
	        
			if (this.showCompassSelected && lonWidth > 100000) {
				AffineTransform rotate = AffineTransform.getRotateInstance(Math.toRadians(30), 0, 0);
				
				g2d.setStroke(oriStroke);
				g2d.setColor(new Color(0xff000000));
				g2d.draw(new Ellipse2D.Double(-midRadius+80, -midRadius+80, radius-160, radius-160));				
				
				g2d.setFont(new Font("Arial", Font.BOLD, Math.min((int)(midRadius/4), 25)));
				
				int numWidth = g2d.getFontMetrics().stringWidth("0");
				g2d.drawString("0", -numWidth / 2, (int)-midRadius+70);
				g2d.transform(rotate);
				g2d.drawString("30", -numWidth, (int)-midRadius+70);
				g2d.transform(rotate);
				g2d.drawString("60", -numWidth, (int)-midRadius+70);
				g2d.transform(rotate);
				g2d.drawString("90", -numWidth, (int)-midRadius+70);
				g2d.transform(rotate);
				
				numWidth = numWidth / 2 + numWidth;
				g2d.drawString("120", -numWidth, (int)-midRadius+70);
				g2d.transform(rotate);
				g2d.drawString("150", -numWidth, (int)-midRadius+70);
				g2d.transform(rotate);
				g2d.drawString("180", -numWidth, (int)-midRadius+70);
				g2d.transform(rotate);
				g2d.drawString("210", -numWidth, (int)-midRadius+70);
				g2d.transform(rotate);
				g2d.drawString("240", -numWidth, (int)-midRadius+70);
				g2d.transform(rotate);
				g2d.drawString("270", -numWidth, (int)-midRadius+70);
				g2d.transform(rotate);
				g2d.drawString("300", -numWidth, (int)-midRadius+70);
				g2d.transform(rotate);
				g2d.drawString("330", -numWidth, (int)-midRadius+70);
			}
		}
		
		g2d.dispose();
	}
	
	public void reload() {
		init();
	}

	public int prepareToPaint(JXMapViewer map, int lw) {
		radius = 0;
		lonWidth = lw;
		
		if (lonWidth > 1000) 
			radius = (int) Math.abs(Math.cbrt(lonWidth) * Math.log(Math.cbrt(lonWidth) * this.factorX) * this.factorY);
		midRadius = (double)radius/2;
		
		return radius;
	}
	
	public static void drawRotatedArrow(final Graphics2D g2, final Shape shape,
            final double angle,
            final float x, final float y) {
		final AffineTransform saved = g2.getTransform();
		final AffineTransform rotate = AffineTransform.getRotateInstance(Math.toRadians(angle), x, y);
		
		g2.transform(rotate);
		g2.draw(shape);
		g2.setColor(new Color(0xfff4f4f4));
		g2.fill(shape);
		g2.setTransform(saved);
	}
	
	public static Shape createUpArrow(final float s) {
		final GeneralPath p0 = new GeneralPath();
		p0.moveTo(-s/80, s/2);
		p0.lineTo(-s/80, -s/2);
		p0.lineTo(-s/8, -(3*s)/7);
		p0.lineTo(0.0f, -(2*s)/3);
		p0.lineTo(s/8, -(3*s)/7);
		p0.lineTo(s/80, -s/2);
		p0.lineTo(s/80, s/2);
		p0.closePath();
	    return p0;
	}	
}
