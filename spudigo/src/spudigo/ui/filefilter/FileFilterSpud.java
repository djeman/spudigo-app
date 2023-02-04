package spudigo.ui.filefilter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FileFilterSpud extends FileFilter {
	@Override
	public boolean accept(File f) {
		if (f != null) {
			if (f.isDirectory())
				return true;
			
			String name = f.getName().toLowerCase();
			return name.toLowerCase().endsWith(".spud");
		} else {
			return false;
		}
	}
	
	@Override
	public String getDescription() {
		return "*.spud";
	}
}
