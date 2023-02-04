package spudigo.ui.dialog;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;

public class FontButton extends JButton {
	private static final long serialVersionUID = 1L;
	private Color[] listColor = new Color[]{Color.GREEN, Color.YELLOW, Color.RED};

	public FontButton() {
		super();
	}
	
	public FontButton(String text) {
		super(text);
	}
	
	public FontButton(String text, Color[] newListColor) {
		super(text);
		listColor = newListColor;
	}

	@Override
    protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setColor(listColor[0]);
		g2d.fill(new Rectangle2D.Double(0, 0, (double)getWidth()/3, getHeight()));
		g2d.setColor(listColor[1]);
		g2d.fill(new Rectangle2D.Double((double)getWidth()/3, 0, (double)getWidth()/3, getHeight()));
		g2d.setColor(listColor[2]);
		g2d.fill(new Rectangle2D.Double(((double)getWidth()/3)*2, 0, (double)getWidth()/3, getHeight()));
		
		g2d.setFont(this.getFont());
		FontMetrics metrics = g.getFontMetrics();
		int tw = metrics.stringWidth(getText());
		int th = metrics.getAscent();
		if (tw > (this.getWidth()/3)) {
			g2d.drawString(getText(), (-tw/2)+(this.getWidth()/2), (th/2)+(this.getHeight()/2));
		} else {
			g2d.drawString(getText(), (-tw/2)+(this.getWidth()/3), (th/2)+(this.getHeight()/2));
			g2d.drawString(getText(), (-tw/2)+((this.getWidth()/3)*2), (th/2)+(this.getHeight()/2));
		}
		
		g2d.dispose();
    }
}