package spudigo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Hashtable;

public class LoadTemplates implements Runnable {
	public static final String MODEL_DIRECTORY = "spudigo_mdl";
	
	private final Hashtable<String, TemplateFile> templates = new Hashtable<String, TemplateFile>();
	private final LoadTemplatesListener loadTemplatesListener;
	
	private String endLine = System.lineSeparator();
	
	public LoadTemplates(LoadTemplatesListener loadTemplatesListener) {
		this.loadTemplatesListener = loadTemplatesListener;
		
		Thread tt = new Thread(this);
		tt.start();
	}
	
	public void run() {
		if (!createDirectory() || !createDefModels())
			return;
		
		if (endLine == null || endLine.length() < 1)
			endLine = "\n";
		
		if (loadFiles())
			loadTemplatesListener.onModelsLoaded(templates);
	}
	
	private boolean loadFiles() {
		File dir = new File(MODEL_DIRECTORY);
		File[] files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".txt");
			}
		});
		
		if (files.length < 1) {
			loadTemplatesListener.onError(Config.getLangBundle().getString("errorNoModels"));
			return false;
		}
		
		for (File m:files) {
			TemplateFile templateFile = new TemplateFile();
			String idName = m.getName();
			
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(m), "ISO-8859-1"));
			
				try {
					String line;
					while ((line = br.readLine()) != null) {
						if (line.startsWith(";") || !line.contains("=")) {
							continue;
						}
						
						String[] splitEgal = line.split("="); 
						if (splitEgal[0].toLowerCase().equals("name"))
							idName = splitEgal[1];
						else if (splitEgal[0].toLowerCase().equals("tanuki"))
							templateFile.setTanuki(splitEgal[1]);
						else if (line.contains(":"))
							templateFile.addToHashTable(splitEgal[0], splitEgal[1]);
					}
				} finally {
					br.close();
				}
			} catch (IOException e) {
				loadTemplatesListener.onError(String.format(Config.getLangBundle().getString("errorReadModel"), m.getName()));
			}
			
			if (templateFile.getItemsCount() > 0)
				templates.put(idName, templateFile);
		}
		
		return true;
	}

	private boolean createDirectory() {
		File dir = new File(MODEL_DIRECTORY);
		if (dir.exists() && !dir.isDirectory()) {
			loadTemplatesListener.onError(
					String.format(Config.getLangBundle().getString("errorDirModel"), MODEL_DIRECTORY));
			return false;
		} else if (!dir.exists()) {
			dir.mkdir();
			createDefModels();
		}
		
		return true;
	}
	
	private boolean createDefModels() {
		File dir = new File(MODEL_DIRECTORY);
		File[] files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".txt");
			}
		});
		
		if (files.length < 1) {
			try {
				writeEurPlus();
				writeIgoStd();
			} catch (IOException e) {
				loadTemplatesListener.onError(
						String.format(Config.getLangBundle().getString("errorDefModel"), MODEL_DIRECTORY + File.separator));
				return false;
			}
		}
		
		return true;
	}
	
	private void writeEurPlus() throws IOException {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(MODEL_DIRECTORY + 
				File.separator + "mdl_eur+.txt"), "ISO-8859-1"));
		try {
			bw.write(String.format(Config.getLangBundle().getString("templateEurPlus"), endLine));
		} finally {
			bw.close();
		}
	}
	
	private void writeIgoStd() throws IOException {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(MODEL_DIRECTORY + 
				File.separator + "mdl_std_int.txt"), "ISO-8859-1"));
		try {
			bw.write(String.format(Config.getLangBundle().getString("templateIgoStd"), endLine));
		} finally {
			bw.close();
		}
	}

	public interface LoadTemplatesListener {
		public void onModelsLoaded(Hashtable<String, TemplateFile> templates);
		public void onError(String msg);
	}
}
