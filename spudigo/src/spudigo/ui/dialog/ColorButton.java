package spudigo.ui.dialog;

import java.awt.Graphics;
import javax.swing.JButton;

public class ColorButton extends JButton {
	private static final long serialVersionUID = 1L;

	public ColorButton() {
		super();
	}
	
	public ColorButton(String text) {
		super(text);
	}

	@Override
    protected void paintComponent(Graphics g) {
        if (!isOpaque() && getBackground().getAlpha() < 256 && getBackground().getAlpha() > 0) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        //super.paintComponent(g);
    }
}
