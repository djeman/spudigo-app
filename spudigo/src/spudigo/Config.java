package spudigo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Locale.Builder;
import java.util.Properties;
import java.util.ResourceBundle;

public class Config {
	private final Properties userConfig;
	private Locale locale;
	
	private Config() {
		// create and load default properties
		Properties defConfig = new Properties();
		
		try {
			InputStream in = getClass().getResourceAsStream("/default.cfg");
			defConfig.load(in);
			in.close();
		} catch (IOException e) {}
		
		// create application config with default
		userConfig = new Properties(defConfig);

		// load user config from last invocation
		try {
			FileInputStream in = new FileInputStream("spudigo.cfg");
			userConfig.load(in);
			in.close();
		} catch (IOException e) {}
		
		if (getString("spudigo.language") == null ||
				getString("spudigo.country") == null) {
			locale = Locale.getDefault();
			setValue("spudigo.language", locale.getLanguage());
			setValue("spudigo.country", locale.getCountry());
		} else {
			locale = new Builder().setLanguage(getString("spudigo.language")).setRegion(getString("spudigo.country")).build();
			Locale.setDefault(locale);
		}
	}
	
	private static Config INSTANCE = null;
	
	public static synchronized Config getInstance() {
		if (INSTANCE == null)
			INSTANCE = new Config();
		
		return INSTANCE;
	}
	
	public static ResourceBundle getLangBundle() {
		return ResourceBundle.getBundle("spudigo.localization.LangBundle", getInstance().getCurrentLocale());
	}

	public void setCurrentLocale(String lang, String country) {
		this.locale = new Builder().setLanguage(lang).setRegion(country).build();
		setValue("spudigo.language", lang);
		setValue("spudigo.country", country);
		ResourceBundle.clearCache();
	}
	
	public Locale getCurrentLocale() {
		return this.locale;
	}
	
	public void save() {
		FileOutputStream out;
		try {
			out = new FileOutputStream("spudigo.cfg");
			userConfig.store(out, "Spudigo Configuration");
			out.close();
		} catch (IOException e) {}
	}
	
	public String getString(String key) {
		return userConfig.getProperty(key);
	}
	
	public int getInteger(String key) {
		int result = 0;
		try {
			result = Integer.parseInt(userConfig.getProperty(key));
		} catch (NullPointerException | NumberFormatException e) {
			result = 0;
		}
		return result;
	}
	
	public int getIntegerHexa(String key) {
		int result = 0;
		try {
			result = (int)Long.parseLong(userConfig.getProperty(key), 16);
		} catch (NullPointerException | NumberFormatException e) {
			result = 0;
		}
		return result;
	}
	
	public boolean getBoolean(String key) {
		boolean result = false;
		try {
			result = Boolean.parseBoolean(userConfig.getProperty(key));
		} catch (NullPointerException | NumberFormatException e) {
			result = false;
		}
		return result;
	}
	
	public double getDouble(String key) {
		double result = 0;
		try {
			result = Double.parseDouble(userConfig.getProperty(key));
		} catch (NullPointerException | NumberFormatException e) {
			result = 0;
		}
		return result;
	}
	
	public void setValue(String key, String value) {
		userConfig.setProperty(key, value);
	}
	
	public void setValue(String key, int value) {
		userConfig.setProperty(key, String.valueOf(value));
	}
	
	public void setValue(String key, boolean value) {
		userConfig.setProperty(key, String.valueOf(value));
	}

	public void setValue(String key, double value) {
		userConfig.setProperty(key, String.valueOf(value));
	}
}
