package spudigo;

import java.util.HashMap;
import java.util.Map;

public enum EnumStatus {
	NEW(0, Config.getLangBundle().getString("enumStatusNew")),
	DELETED(1, Config.getLangBundle().getString("enumStatusDel")),
	EDITED(2, Config.getLangBundle().getString("enumStatudEdit"));
	
	private int type;
	private String label;
	
	private static Map<Integer, String> statusToStringMap;
	private static Map<String, Integer> stringToStatusMap;

	private EnumStatus(int type, String label) {
		this.type = type;
		this.label = label;
	}
	
	public static String getStatus(int i) {
        if (statusToStringMap == null) 
            initMappingA();
        
        return statusToStringMap.get(i);
    }
 
    private static void initMappingA() {
    	statusToStringMap = new HashMap<Integer, String>();
        for (EnumStatus d : values()) {
        	statusToStringMap.put(d.type, d.label);
        }
    }
    
    public static int getStatus(String i) {
        if (stringToStatusMap == null) 
            initMappingB();
        
        return stringToStatusMap.get(i);
    }
 
    private static void initMappingB() {
    	stringToStatusMap = new HashMap<String, Integer>();
        for (EnumStatus d : values()) {
        	stringToStatusMap.put(d.label, d.type);
        }
    }

	public int getType() {
        return type;
    }
 
    public String getLabel() {
        return label;
    }
}
