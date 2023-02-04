package org.jxmapviewer;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.jxmapviewer.viewer.TileFactoryInfo;

public class BingTileFactoryInfo extends TileFactoryInfo {
	
	/**
	 * Use road map
	 */
	public final static String ROAD = "Road";

	/**
	 * Use aerial  map
	 */
	public final static String AERIAL = "Aerial";

	/**
	 * Use aerial with labels map
	 */
	public final static String AERIALWITHLABELS = "AerialWithLabels";
	
	/**
	 * Use birds eye 
	 */
	//public final static String BIRDSEYE = "Birdseye";
	
	/**
	 * Use birds eye with labels map
	 */
	//public final static String BIRDEYESWITHLABELS = "BirdseyeWithLabels";
	
	private int topZoomLevel = 21;
	private int maxZoomLevel = 19;
	private int minZoomLevel = 2;
	private int tileSize = 256;

	private String imageUrl;
	private String[] imageUrlSubDomains;
	
	private String mode;
	private String culture;
	
	private String mapKey;
	
	private String copyright = "© 2019 Microsoft";

	/**
	 * @param mode the mode
	 */
	public BingTileFactoryInfo(String mode, String mapKey)
	{
		super("Bing Maps");
		this.mode = mode;
		this.mapKey = mapKey;
		this.culture = Locale.getDefault().getLanguage();
		
		super.setConfig(minZoomLevel, maxZoomLevel, topZoomLevel, tileSize,
				false, false, "", "", "", "");
	}
	
	@Override
	public String getTileUrl(final int x, final int y, final int zoom) 
	{
		if (imageUrl == null) {
			if (!tryToGetMetaData())
				return "";
		}
		
		final String quad = tileToQuadKey(x, y, topZoomLevel - zoom);
		return imageUrl.replace("{subdomain}", imageUrlSubDomains[0])
				.replace("{quadkey}", quad)
				.replace("{culture}", culture);
	}
	
	@Override
	public int getDefaultZoomLevel() 
	{
		return maxZoomLevel;
	}

	private String tileToQuadKey(int tileX, int tileY, int levelOfDetail) {
		StringBuilder quadKey = new StringBuilder();
        for (int i = levelOfDetail; i > 0; i--)
        {
            char digit = '0';
            int mask = 1 << (i - 1);
            if ((tileX & mask) != 0)
            {
                digit++;
            }
            if ((tileY & mask) != 0)
            {
                digit++;
                digit++;
            }
            quadKey.append(digit);
        }
        return quadKey.toString();
	}
	
	private boolean tryToGetMetaData() {
		boolean result = false;
		
		try {
			URL url = new URL("http://dev.virtualearth.net/REST/V1/Imagery/Metadata/" +
				BingTileFactoryInfo.this.mode + "?output=xml&key=" + BingTileFactoryInfo.this.mapKey);
			
			if (loadMetaData(url.openStream()))
				result = true;
		} catch (Exception ex) {}
		
		return result;
	}
	
	private boolean loadMetaData(InputStream result) throws Exception {
		if (result == null)
			return false;
		
		String tagContent = null;
		
		XMLInputFactory factory = XMLInputFactory.newInstance();
		factory.setProperty("javax.xml.stream.isCoalescing", true);
		XMLStreamReader reader = factory.createXMLStreamReader(result);
		
		while(reader.hasNext()) {
			int event = reader.next();
			switch(event) {
				case XMLStreamConstants.START_ELEMENT:
					if (reader.getLocalName().equals("ImageUrlSubdomains"))
						imageUrlSubDomains = readXmlStringArray(reader);
					break;
				case XMLStreamConstants.CHARACTERS:
					tagContent = reader.getText().trim();
					break;
				case XMLStreamConstants.END_ELEMENT:
					switch(reader.getLocalName()) {
						case "StatusCode":
							if (!tagContent.equals("200")) return false;
							break;
						case "Copyright":
							//copyright = tagContent;
							break;
						case "ImageUrl":
							imageUrl = tagContent;
							break;
						case "ImageWidth":
							tileSize = Integer.valueOf(tagContent);
							break;
						case "ZoomMin":
							minZoomLevel = Integer.valueOf(tagContent) + 1;
							break;
						case "ZoomMax":
							topZoomLevel = Integer.valueOf(tagContent);
							break;
					}
					break;
			}
		}
		
		maxZoomLevel = topZoomLevel - minZoomLevel;
		
		return true;
	}
	
	private String[] readXmlStringArray(XMLStreamReader reader) throws Exception {
		String tagContent = null;
		ArrayList<String> array = new ArrayList<String>();
		
		while(reader.hasNext()) {
			int event = reader.next();
			switch (event) {
				case XMLStreamConstants.CHARACTERS:
					tagContent = reader.getText().trim();
					break;
				case XMLStreamConstants.END_ELEMENT:
					switch(reader.getLocalName()) {
						case "string":
							array.add(tagContent);
							break;
						default:
							return array.toArray(new String[array.size()]);
					}
					break;
			}
		}
		
		return array.toArray(new String[array.size()]);
	}
	
	@Override
	public String getCopyright() {
		return copyright;
	}
}
