package spudigo;

import java.util.HashMap;
import java.util.Map;

public enum EnumDirType {
	ALL(0, Config.getLangBundle().getString("enumDirTypeAll")),
	UNI(1, Config.getLangBundle().getString("enumDirTypeUni")), 
	BI(2, Config.getLangBundle().getString("enumDirTypeBi"));

	private int type;
	private String label;
	
	private static Map<Integer, String> dirToStringMap;
	private static Map<String, Integer> stringToDirMap;

	private EnumDirType(int type, String label) {
		this.type = type;
		this.label = label;
	}
	
	public static String getDirType(int i) {
        if (dirToStringMap == null) 
            initMappingA();
        
        return dirToStringMap.get(i);
    }
 
    private static void initMappingA() {
    	dirToStringMap = new HashMap<Integer, String>();
        for (EnumDirType d : values()) {
        	dirToStringMap.put(d.type, d.label);
        }
    }
    
    public static int getDirType(String i) {
        if (stringToDirMap == null) 
            initMappingB();
        
        return stringToDirMap.get(i);
    }
 
    private static void initMappingB() {
    	stringToDirMap = new HashMap<String, Integer>();
        for (EnumDirType d : values()) {
        	stringToDirMap.put(d.label, d.type);
        }
    }

	public int getType() {
        return type;
    }
 
    public String getLabel() {
        return label;
    }
}

