package spudigo;

public class HaversineAlgorithm {
	private static final double R = 6372.797560856; // In kilometers

    public static double HaversineInM(double lat1, double lon1, double lat2, double lon2) {
        return 1000D * HaversineInKM(lat1, lon1, lat2, lon2);
    }

    public static double HaversineInKM(double lat1, double lon1, double lat2, double lon2) {
    	double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
}
