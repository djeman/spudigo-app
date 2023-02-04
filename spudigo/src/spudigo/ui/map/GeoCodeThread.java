package spudigo.ui.map;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import spudigo.Config;

public class GeoCodeThread extends Thread {

	private Double latitude = 250.0;
	private Double longitude = 250.0;
	
	private final String apiKey;
	private final String address;
	private final GeoCodeListener geoCodeListener;
	
	public GeoCodeThread(String apiKey, String address, GeoCodeListener geoCodeListener) {
		this.apiKey = apiKey;
		this.address = address;
		this.geoCodeListener = geoCodeListener;
	}
	
	@Override
	public void run() {
		try {
			URL url = new URL("https://geocoder.ls.hereapi.com/6.2/geocode.xml?" + 
								"apiKey=" +	this.apiKey + 
								"&searchtext=" + URLEncoder.encode(address, "UTF-8"));
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			
			XMLInputFactory factory = XMLInputFactory.newInstance();
			factory.setProperty("javax.xml.stream.isCoalescing", true);
			XMLStreamReader reader = factory.createXMLStreamReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
			
			try {
				getCoorFromXml(reader);
			} finally {
				reader.close();
			}
		} catch (Exception e) {
			geoCodeListener.onGeoCodeError(Config.getLangBundle().getString("diagAddressErr"));
			return;
		}
		
		if (latitude < 90.0 && latitude > -90.0 && longitude < 180 && longitude > -180)
			geoCodeListener.onGeoCodeSuccess(latitude, longitude);
		else
			geoCodeListener.onGeoCodeError(Config.getLangBundle().getString("diagAddressErr"));
	}
	
	private void getCoorFromXml(XMLStreamReader reader) throws XMLStreamException {
		while(reader.hasNext()) {
			int event = reader.next();
			switch(event) {
				case XMLStreamConstants.START_ELEMENT:
					if (reader.getLocalName().equals("DisplayPosition")) {
						readXmlLatLng(reader);
						return;
					}
			}
		}
	}
	
	private void readXmlLatLng(XMLStreamReader reader) throws XMLStreamException {
		String tagContent = null;
		
		while(reader.hasNext()) {
			int event = reader.next();
			switch (event) {
				case XMLStreamConstants.CHARACTERS:
					tagContent = reader.getText().trim();
					break;
				case XMLStreamConstants.END_ELEMENT:
					if (reader.getLocalName().equals("Latitude")) {
						try {
							if (tagContent != null && tagContent.length() > 0)
								latitude = Double.parseDouble(tagContent);
						} catch (NumberFormatException e) {}
					} else if (reader.getLocalName().equals("Longitude")) {
						try {
							if (tagContent != null && tagContent.length() > 0)
								longitude = Double.parseDouble(tagContent);
						} catch (NumberFormatException e) {}
					} else if (reader.getLocalName().equals("DisplayPosition")) {
						return;
					}
					
					break;
			}
		}
	}

	public interface GeoCodeListener {
		public void onGeoCodeSuccess(double latitude, double longitude);
		public void onGeoCodeError(String msg);
	}
}
