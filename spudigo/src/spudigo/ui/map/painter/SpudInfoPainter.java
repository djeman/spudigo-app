package spudigo.ui.map.painter;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.AbstractPainter;

import spudigo.HaversineAlgorithm;

public class SpudInfoPainter extends AbstractPainter<JXMapViewer> {
	private static final Font font = new Font("Tahoma", Font.PLAIN, 10);
	private static final double oneDegLonMeters = HaversineAlgorithm.HaversineInM(0.0,0.0,0.0,1.0);
	
	private BufferedImage imgCompass = null;
	
	private boolean showCompass = true;
	
	public SpudInfoPainter(boolean showCompass) {
		this.showCompass = showCompass;
		
		try {
			imgCompass = ImageIO.read(getClass().getResource("/spudigo/images/compass.png"));
		} catch (IOException e) {}
	}
	
	public void setShowCompass(boolean value) {
		this.showCompass = value;
	}

	@Override
	protected void doPaint(Graphics2D g, JXMapViewer map, int width, int height) {
		// Compass
		if (this.showCompass && imgCompass != null) {
			g.drawImage(imgCompass, map.getWidth() - imgCompass.getWidth() - 7, 7, null);
		}
		
		// Scale
		double meterPerPixel = oneDegLonMeters / map.getTileFactory().getInfo().getLongitudeDegreeWidthInPixels(map.getZoom());
		long nbrDivision = (long)(100 * meterPerPixel);
		int nbrZero = 0;
		
		while (nbrDivision > 9) {
			nbrDivision /= 10;
			nbrZero++;
		}
		
		long lenMetersScale = (long)(nbrDivision * Math.pow(10, nbrZero));
		int lenPixelScale = (int)(lenMetersScale / meterPerPixel);
		
		g.setColor(Color.GRAY);
		g.drawRect(map.getWidth() - 100, map.getHeight() - 108, lenPixelScale, 5);
		
		for (int i=0;i<nbrDivision;i++) {
			g.setColor((i%2 == 1)?Color.BLACK:Color.WHITE);
			g.fill(new Rectangle2D.Double(map.getWidth() - 99 + (((double)lenPixelScale-1)/nbrDivision*i), 
					map.getHeight() - 107, ((double)lenPixelScale-2)/nbrDivision, 4));
		}
		
		String scaleStr = (lenMetersScale>1000)?String.format("%1$d km", lenMetersScale/1000):
			String.format("%1$d m", lenMetersScale);
		
		g.setFont(font);
		
		FontMetrics metrics = g.getFontMetrics();
		int tw = metrics.stringWidth(scaleStr);
		int th = 1 + metrics.getAscent();
		
		g.setColor(new Color(0x90EEEEEE, true));
		g.fillRoundRect(map.getWidth() - 100, map.getHeight() - 109 - th, tw + 8, th, 10, 10);
		g.setColor(Color.BLACK);
		g.drawString(scaleStr, map.getWidth() - 96, map.getHeight() - 111);
		
		// Copyright
		tw = metrics.stringWidth(map.getTileFactory().getInfo().getCopyright());
		
		g.setColor(new Color(0x90EEEEEE, true));
		g.fillRoundRect(50, map.getHeight() - 6 - th, tw + 8, th, 10, 10);
		g.setColor(Color.BLACK);
		g.drawString(map.getTileFactory().getInfo().getCopyright(), 54, map.getHeight() - 8);
	}
}
