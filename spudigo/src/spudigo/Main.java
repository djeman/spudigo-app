package spudigo;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.ColorUIResource;

import spudigo.ui.WinMain;

public class Main {
	public static final String version = "Spudigo v2.10";
	public static final String versionDate = "06 oct. 2024";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UIManager.put("swing.boldMetal", Boolean.FALSE);
        UIManager.put("ComboBox.background", new ColorUIResource(UIManager.getColor("TextField.background")));
		
		try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
		
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                WinMain window = new WinMain();
                window.setVisible(true);
            }
        });
	}

}
