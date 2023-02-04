package spudigo.ui.filefilter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FileFilterImage extends FileFilter {
	@Override
	public boolean accept(File f) {
		if (f != null) {
			if (f.isDirectory())
				return true;
			
			String name = f.getName().toLowerCase();
			return name.toLowerCase().endsWith(".jpg") | name.toLowerCase().endsWith(".jpeg") |
					name.toLowerCase().endsWith(".png") | name.toLowerCase().endsWith(".bmp") |
					name.toLowerCase().endsWith(".wbmp") | name.toLowerCase().endsWith(".gif");
		} else {
			return false;
		}
	}
	
	@Override
	public String getDescription() {
		return "Image Files";
	}
}
