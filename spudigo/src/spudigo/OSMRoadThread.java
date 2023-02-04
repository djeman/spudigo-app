package spudigo;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jxmapviewer.viewer.GeoPosition;

public class OSMRoadThread extends Thread {
	
	private GeoPosition[] points;
	private OSMRoadListener osmRoadListener;
	
	public OSMRoadThread(GeoPosition[] points, OSMRoadListener osmRoadListener) {
		this.points = points;
		this.osmRoadListener = osmRoadListener;
	}
	
	@Override
	public void run() {
		try {
			OSMRoadRes goval = getRoad(this.points[0], this.points[1]);
			OSMRoadRes backval = getRoad(this.points[1], this.points[0]);
			
			osmRoadListener.onOSMRoadResult(goval, backval);
		} catch (Exception e) {}
	}
	
	private OSMRoadRes getRoad(GeoPosition posA, GeoPosition posB) throws Exception {
		OSMRoadRes result = new OSMRoadRes();
		
		URL url = new URL("http://router.project-osrm.org/route/v1/driving/" + 
				posA.getLongitude() + "," + posA.getLatitude() + ";" +
				posB.getLongitude() + "," + posB.getLatitude() + "?geometries=geojson&overview=full");
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		
		JSONParser parser = new JSONParser();
		InputStreamReader reader = new InputStreamReader(urlConnection.getInputStream(), "UTF-8");
		try {
			JSONObject jsonObj = (JSONObject)parser.parse(reader);
			
			String code = (String)jsonObj.get("code");
			if (!code.equals("Ok"))
				return result;
				
			JSONArray routes = (JSONArray)jsonObj.get("routes");
			JSONObject route = (JSONObject)routes.get(0);
			result.dist = String.format("%1$.1f", (double)route.get("distance"));
			
			JSONObject geometry = (JSONObject)route.get("geometry");
			JSONArray points = (JSONArray)geometry.get("coordinates");
			
			ArrayList<GeoPosition> road = new ArrayList<GeoPosition>(); 
			Iterator<?> iterator = points.iterator();
            while (iterator.hasNext()) {
            	JSONArray point = (JSONArray)iterator.next();
            	
            	GeoPosition pos = new GeoPosition((double)point.get(1), (double)point.get(0));
            	road.add(pos);
            }

            result.road = road.toArray(new GeoPosition[road.size()]);
		} finally {
			reader.close();
		}
		
		return result;
	}
	
	public class OSMRoadRes {
		public String dist = "-";
		public GeoPosition[] road;
	}

	public interface OSMRoadListener {
		public void onOSMRoadResult(OSMRoadRes toGo, OSMRoadRes toBack);
	}
}
