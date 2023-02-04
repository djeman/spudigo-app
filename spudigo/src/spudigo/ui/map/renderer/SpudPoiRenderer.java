package spudigo.ui.map.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.Shape;
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
import spudigo.TemplateFile;
import spudigo.ui.dialog.DialogFontChooser;

public class SpudPoiRenderer implements WaypointRenderer<SpudItem> {
	private BufferedImage[] imgWaypoints = new BufferedImage[32];
	private BufferedImage imgUnknown = null;
	
	private double factorX, factorY, midRadius = 0;
	private Color[] radiusGradient = new Color[2];
	private Color[] zonesColors = new Color[5];
	private int arcAngle, speedX, speedY, lonWidth = 0, radius = 0;
	private boolean speedCenter;
	private BasicStroke lineDashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
	        BasicStroke.JOIN_MITER, 8.0f, new float[]{ 8.0f }, 0.0f);
	
	private Font fontPoi, fontZone;
	private TemplateFile modelIn;

	public SpudPoiRenderer(TemplateFile modelIn) {
		init();
		
		this.modelIn = modelIn;
	}
	
	private void init() {
		try {
			initImg();
		} catch (Exception ex) {}
		
		this.factorX = Config.getInstance().getDouble("radius.factor.x");
		this.factorY = Config.getInstance().getDouble("radius.factor.y");
		this.arcAngle = Config.getInstance().getInteger("radius.arc.angle");
		this.radiusGradient[0] = new Color(Config.getInstance().getIntegerHexa("radius.color.argb.0"), true);
		this.radiusGradient[1] = new Color(Config.getInstance().getIntegerHexa("radius.color.argb.1"), true);
		
		this.fontPoi = new Font(DialogFontChooser.getAttributes(Config.getInstance().getString("marker.speed.font")));
		this.speedCenter = Config.getInstance().getBoolean("marker.speed.x.center");
		this.speedX = Config.getInstance().getInteger("marker.speed.x");
		this.speedY = Config.getInstance().getInteger("marker.speed.y");
		
		this.zonesColors[0] = new Color(Config.getInstance().getIntegerHexa("zone.color.argb.10"), true);
		this.zonesColors[1] = new Color(Config.getInstance().getIntegerHexa("zone.color.argb.100"), true);
		this.zonesColors[2] = new Color(Config.getInstance().getIntegerHexa("zone.color.argb.1000"), true);
		this.zonesColors[3] = new Color(Config.getInstance().getIntegerHexa("zone.color.argb.10000"), true);
		this.zonesColors[4] = new Color(Config.getInstance().getIntegerHexa("zone.color.argb.10000+"), true);
		this.fontZone = new Font(DialogFontChooser.getAttributes(Config.getInstance().getString("zone.font")));
	}
	
	private void initImg() throws Exception {
		String img = null;
		
		try {
			img = Config.getInstance().getString("marker.image.unknown");
			if (img != null && img.length() > 0 && new File(img).exists())
				imgUnknown = ImageIO.read(new File(img));
			else
				imgUnknown = ImageIO.read(getClass().getResource("/spudigo/images/unknown_waypoint.png"));
		} catch (Exception ex) {
			imgUnknown = ImageIO.read(getClass().getResource("/spudigo/images/unknown_waypoint.png"));
		}
		
		for (int i=0;i<32;i++) {
			try {
				img = Config.getInstance().getString(String.format("marker.image.bin.type.%1$d",i));
				if (img != null && img.length() > 0 && new File(img).exists())
					imgWaypoints[i] = ImageIO.read(new File(img));
				else
					imgWaypoints[i] = ImageIO.read(getClass().getResource("/images/standard_waypoint.png"));
			} catch (Exception ex) {
				imgWaypoints[i] = ImageIO.read(getClass().getResource("/images/standard_waypoint.png"));
			}
		}
	}
	
	public int prepareToPaint(JXMapViewer map, int lw) {
		radius = 0;
		lonWidth = lw;
		
		if (lonWidth > 1000) 
			radius = (int) Math.abs(Math.cbrt(lonWidth) * Math.log(Math.cbrt(lonWidth) * this.factorX) * this.factorY);
		midRadius = (double)radius/2;
		
		return radius;
	}
	
	public void paintZone(Graphics2D g, Point2D point, int count, int segLenW, int segLenH) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.translate(point.getX(), point.getY());
		
		if (count < 10)
			g2d.setColor(this.zonesColors[0]);
		else if (count < 100)
			g2d.setColor(this.zonesColors[1]);
		else if (count < 1000)
			g2d.setColor(this.zonesColors[2]);
		else if (count < 10000)
			g2d.setColor(this.zonesColors[3]);
		else
			g2d.setColor(this.zonesColors[4]);
		
		g2d.fillRect(-segLenW/2, -segLenH/2, segLenW, segLenH);
		
		g2d.setColor(Color.black);
	    g2d.setStroke(lineDashed);
	    g2d.drawRect(-segLenW/2, -segLenH/2, segLenW, segLenH);
		
		g2d.setFont(fontZone);
		FontMetrics metrics = g2d.getFontMetrics();
		int tw = metrics.stringWidth(String.valueOf(count));
		int h = metrics.getAscent();
		g2d.drawString(String.valueOf(count), -(tw / 2), h/4);
		
		g2d.dispose();
	}
	
	@Override
	public void paintWaypoint(Graphics2D g, JXMapViewer map, SpudItem waypoint) {}

	public void paintWaypoint(Graphics2D g, Point2D point, SpudItem w) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.translate(point.getX(), point.getY());
		
		if (lonWidth > 1000) {
	        RadialGradientPaint rgp = new RadialGradientPaint(
	        				new Point(),
	        				(float)midRadius,
	                        new float[]{0f, 1f},
	                        new Color[]{this.radiusGradient[0], this.radiusGradient[1]});
	        
	        g2d.setPaint(rgp);
	        
	        switch (w.getDirType()) {
		        case 0: // ALL
		        	g2d.fill(new Ellipse2D.Double(-midRadius, -midRadius, radius, radius));
		        	break;
		        case 1: // UNI
		        	g2d.fill(new Arc2D.Double(-midRadius, -midRadius, radius, radius, 
		        			360 - ((w.getDirection()+90+(this.arcAngle*0.5))%360), this.arcAngle, Arc2D.PIE));
		        	break;
		        case 2: // BI
		        	g2d.fill(new Arc2D.Double(-midRadius, -midRadius, radius, radius, 
		        			360 - ((w.getDirection()+90+(this.arcAngle*0.5))%360), this.arcAngle, Arc2D.PIE));
		        	g2d.fill(new Arc2D.Double(-midRadius, -midRadius, radius, radius, 
		        			360 - ((w.getDirection()-90+(this.arcAngle*0.5))%360), this.arcAngle, Arc2D.PIE));
		        	break;
	        }
	        
	        g2d.setColor(Color.BLACK);
	        
	        drawRotatedArrow(g2d, createUpArrow(midRadius/3), w.getDirection()%360, 0, 0);
		}
		
		int type = -1;
		if (modelIn != null) {
			if (w.getBinType() > -1)
				type = (modelIn.getKeyWithBinType(w.getBinType()) == null)?-1:w.getBinType();
			else
				type = modelIn.getBinTypeWithTxtType(w.getTxtType());
			
			type = (type>31)?-1:type;
		}
		
		BufferedImage img = (type<0)?imgUnknown:imgWaypoints[type];
		
		if (img == null)
			return;
		
		g2d.drawImage(img, -img.getWidth() / 2, -img.getHeight(), null);
		
		g2d.setFont(fontPoi);
		FontMetrics metrics = g2d.getFontMetrics();
		int tw = metrics.stringWidth(String.valueOf(w.getSpeed()));
		g2d.drawString(String.valueOf(w.getSpeed()), this.speedX - (speedCenter?(tw / 2):0), this.speedY);
		
		g2d.dispose();
	}

	public void reload() {
		init();
	}
	
	public void setModelIn(TemplateFile modelIn) {
		this.modelIn = modelIn;
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
	
	public static Shape createUpArrow(final double s) {
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
