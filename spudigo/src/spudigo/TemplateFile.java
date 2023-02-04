package spudigo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

public class TemplateFile {
	private final Hashtable<String, TemplateItem> modelItems = new Hashtable<String, TemplateItem>();
	private Tanuki tanuki = null;
	
	public TemplateFile() {	}
	
	public void addToHashTable(String id, String value) {
		TemplateItem item = null;
		String[] val = value.split(":");
		
		if (val.length > 2)
			item = new TemplateItem(Integer.valueOf(val[0]), Integer.valueOf(val[1]), val[2]);
		else if (val.length == 2)
			item = new TemplateItem(Integer.valueOf(val[0]), Integer.valueOf(val[1]), null);
		
		modelItems.put(id, item);
	}
	
	public int getItemsCount() {
		return modelItems.size();
	}
	
	public int getBinTypeWithTxtType(int txtType) {
		Enumeration<TemplateItem> e = modelItems.elements();
	    while (e.hasMoreElements()) {
	    	TemplateItem ti = e.nextElement();
	    	if (ti.txtType == txtType)
	    		return ti.binType;
	    }
	    
		return -1;
	}
	
	public int getTxtTypeWithBinType(int binType) {
		Enumeration<TemplateItem> e = modelItems.elements();
	    while (e.hasMoreElements()) {
	    	TemplateItem ti = e.nextElement();
	    	if (ti.binType == binType)
	    		return ti.txtType;
	    }
	    
		return -1;
	}
	
	public int getBinTypeWithLabel(String label, int speed) {
		if (tanuki != null && tanuki.label == label && tanuki.speed == speed)
			return tanuki.binType;
		
		Enumeration<TemplateItem> e = modelItems.elements();
	    while (e.hasMoreElements()) {
	    	TemplateItem ti = e.nextElement();
	    	if (ti.label != null && ti.label.equals(label))
	    		return ti.binType;
	    }
	    
		return -1;
	}
	
	public int getTxtTypeWithLabel(String label, int speed) {
		if (tanuki != null && tanuki.label == label && tanuki.speed == speed)
			return tanuki.txtType;
		
		Enumeration<TemplateItem> e = modelItems.elements();
	    while (e.hasMoreElements()) {
	    	TemplateItem ti = e.nextElement();
	    	if (ti.label != null && ti.label.equals(label))
	    		return ti.txtType;
	    }
	    
		return -1;
	}
	
	public String getLabelWithBinType(int binType, int speed) {
		if (tanuki != null && tanuki.binType == binType && tanuki.speed == speed)
			return tanuki.label;
		
		Enumeration<TemplateItem> e = modelItems.elements();
	    while (e.hasMoreElements()) {
	    	TemplateItem ti = e.nextElement();
	    	if (ti.binType == binType && ti.label != null)
	    		return ti.label;
	    }
	    
		return null;
	}
	
	public String getLabelWithTxtType(int txtType, int speed) {
		if (tanuki != null && tanuki.txtType == txtType && tanuki.speed == speed)
			return tanuki.label;
		
		return getLabelWithTxtType(txtType);
	}
	
	public String getLabelWithTxtType(int txtType) {
		Enumeration<TemplateItem> e = modelItems.elements();
	    while (e.hasMoreElements()) {
	    	TemplateItem ti = e.nextElement();
	    	if (ti.txtType == txtType && ti.label != null)
	    		return ti.label;
	    }
	    
		return null;
	}
	
	public String getKeyWithBinType(int binType) {
		Iterator<Entry<String, TemplateItem>> it = modelItems.entrySet().iterator();
	    while (it.hasNext()) {
	    	Entry<String, TemplateItem> entry = it.next();
	    	if (entry.getValue().binType == binType)
	    		return entry.getKey();
	    }
	    
		return null;
	}
	
	public String getKeyWithTxtType(int txtType) {
		Iterator<Entry<String, TemplateItem>> it = modelItems.entrySet().iterator();
	    while (it.hasNext()) {
	    	Entry<String, TemplateItem> entry = it.next();
	    	if (entry.getValue().txtType == txtType)
	    		return entry.getKey();
	    }
	    
		return null;
	}
	
	public String[] getComboTypeList() {
		ArrayList<String> resList = new ArrayList<String>();
		Enumeration<TemplateItem> e = modelItems.elements();
		
	    while (e.hasMoreElements()) {
	    	String val = e.nextElement().label;
	    	
	    	if (val != null) 
	    		resList.add(val);
	    }
	    
	    String[] res = resList.toArray(new String[resList.size()]);
	    Arrays.sort(res);
	    
		return res;
	}
	
	public int getBinTypeWithKey(String key) {
		try {
			return modelItems.get(key).binType;
		} catch (Exception e) {
			return -1;
		}
	}
	
	public int getTxtTypeWithKey(String key) {
		try {
			return modelItems.get(key).txtType;
		} catch (Exception e) {
			return -1;
		}
	}
	
	public class TemplateItem {
		public int txtType = -1;
		public int binType = -1;
		public String label = null;
		
		public TemplateItem(int txtType, int binType, String label) {
			this.txtType = txtType;
			this.binType = binType;
			this.label = label;
		}
	}

	public void setTanuki(String values) {
		String[] val = values.split(":");
		if (val.length < 4)
			return;
		
		tanuki = new Tanuki(val[0], val[1], val[2], val[3]);
	}
	
	private class Tanuki {
		public int txtType = 0;
		public int binType = 0;
		public int speed = 5;
		public String label = null;
		
		public Tanuki(String txtType, String binType, String speed, String label) {
			this.txtType = Integer.valueOf(txtType);
			this.binType = Integer.valueOf(binType);
			this.speed = Integer.valueOf(speed);
			this.label = label;
		}
	}
}
