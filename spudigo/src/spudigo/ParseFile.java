package spudigo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

public class ParseFile implements Runnable {
	final public static int TYPE_FILE_ERROR = -1;
	final public static int TYPE_FILE_TXT = 0;
	final public static int TYPE_FILE_SPUD_IGO8 = 1;
	final public static int TYPE_FILE_SPUD_PRIMO = 2;
	final public static int TYPE_FILE_TOMTOM = 3;
	final public static int TYPE_FILE_EMPTY = 4;
	
	private final ParseFileListener parseFileListener;
	private final File spudFile;
	
	public ParseFile(File file, ParseFileListener parseFileListener) {
		this.parseFileListener = parseFileListener;
		this.spudFile = file;
	}
	
	public void run() {
		if (spudFile != null && spudFile.exists() && spudFile.canRead()) {
			try {
				switch(checkFile()) {
					case TYPE_FILE_TXT:
						loadTextFile();
						break;
					case TYPE_FILE_SPUD_IGO8:
						loadSpudFile(false);
						break;
					case TYPE_FILE_SPUD_PRIMO:
						loadSpudFile(true);
						break;
					case TYPE_FILE_EMPTY:
						parseFileListener.onFileParsed(new ArrayList<SpudItem>(), spudFile.getPath(), false, 0, 0, null);
						break;
					/*case TYPE_FILE_TOMTOM:
						loadTomtomFile();
						break;*/
					default:
						parseFileListener.onError(String.format(Config.getLangBundle().getString("errorParseNotKnow"), 
								spudFile.getName()));
						break;
				}
			} catch (IOException e) {
				parseFileListener.onError(String.format(Config.getLangBundle().getString("errorParseRead"), 
						spudFile.getName()));
			}
		} else {
			parseFileListener.onError(String.format(Config.getLangBundle().getString("errorParseNotFound"), 
					spudFile.getName()));
		}
	}
	
	private void loadSpudFile(boolean primo) throws IOException {
		Map<String, Map<Integer, SpudItem>> map = new LinkedHashMap<String, Map<Integer, SpudItem>>();
		
		DataInputStream din = new DataInputStream(new BufferedInputStream(new FileInputStream(spudFile), (primo?18944:13312)));
		try {
			int index = -1, tmpLon, tmpLat;
			String id;
			byte[] bId = new byte[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
			
			while (din.available() > (primo?36:12)) {
				if (primo) {
					din.read(bId);
					din.skip(4); // tile x/y
					index = readUInt(din); // index
				} else {
					index++;
				}
				
				id = DatatypeConverter.printHexBinary(bId);
				
				tmpLon = readUInt(din);
				tmpLat = readUInt(din);
				
				SpudItem item = new SpudItem(
						(tmpLat * Math.ulp(1.0f))%90.00,
						(tmpLon * Math.ulp(1.0f))%180.00);
    			item.setSpeed(din.read()%256);
    			item.setBinType(din.read() & 0x1f); // 3 premiers bits service ???
    			
    			Short tmpUShort = readUShort(din);
    			item.setDirType(convertDirTypeSpudToApp((tmpUShort & 0xfe00) >> 9)%3);
    			item.setDirection((tmpUShort & 0x01ff)%360);
    			item.setStatus(din.read()%3);
    			
    			if (map.get(id) == null)
    				map.put(id, new LinkedHashMap<Integer, SpudItem>());
    			
    			Map<Integer, SpudItem> temp = map.get(id);
    			temp.put(index, item);
			}
		} finally {
	        din.close();
	    }
		
		ArrayList<SpudItem> data = new ArrayList<SpudItem>();
		for(String key : map.keySet())
		    data.addAll(map.get(key).values());
		
		Iterator<SpudItem> it = data.iterator();
		while (it.hasNext()) {
			SpudItem item = (SpudItem) it.next();
			if (item.getStatus() == EnumStatus.DELETED.getType())
				it.remove();
		}
		
		data.trimToSize();
		parseFileListener.onFileParsed(data, spudFile.getPath(), false, 0, 0, null);
	}
	
	private int convertDirTypeSpudToApp(int dirType) {
		switch (dirType) {
			case 0:
				return 1;
			case 1:
				return 2;
			case 2:
				return 0;
			default:
				return 0;
		}
	}
	
	private short readUShort(DataInputStream din) throws IOException {
		byte[] buffer = new byte[2];
		din.read(buffer);
		return (short) (ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xffff);
	}
	
	private int readUInt(DataInputStream din) throws IOException {
		byte[] buffer = new byte[4];
		din.read(buffer);
		return ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}

	private void loadTextFile() throws IOException {
		ArrayList<SpudItem> data = new ArrayList<SpudItem>();
		int nbrError = 0;
		int nbrIgnored = 0;
		String errorMess = null;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(spudFile), "ISO-8859-1"));
	    try {
	    	int indexLabelsMax = 0;
	    	String[] labels = null;
	    	Map<String, Integer> colIndex = null;
	        String line = null;
	        
	        while ((line = br.readLine()) != null) {
	        	if (line.isEmpty())
	        		continue;
	        	
	        	if (labels == null) {
	        		labels = line.split(",");
	        		colIndex = new HashMap<String,Integer>();
	        		
	        		for (int i=0;i<labels.length;i++) {
	        			switch (labels[i].trim().toLowerCase(Locale.US)) {
		        			case "x":
		        				indexLabelsMax = Math.max(indexLabelsMax, i);
		        				colIndex.put("x", i);
		        				break;
		        			case "y":
		        				indexLabelsMax = Math.max(indexLabelsMax, i);
		        				colIndex.put("y", i);
		        				break;
		        			case "type":
		        				indexLabelsMax = Math.max(indexLabelsMax, i);
		        				colIndex.put("type", i);
		        				break;
		        			case "speed":
		        				indexLabelsMax = Math.max(indexLabelsMax, i);
		        				colIndex.put("speed", i);
		        				break;
		        			case "dirtype":
		        				indexLabelsMax = Math.max(indexLabelsMax, i);
		        				colIndex.put("dirtype", i);
		        				break;
		        			case "direction":
		        				indexLabelsMax = Math.max(indexLabelsMax, i);
		        				colIndex.put("direction", i);
		        				break;
		        			case "comment":
		        				indexLabelsMax = Math.max(indexLabelsMax, i);
		        				colIndex.put("comment", i);
		        				break;
	        			}
	        		}
	        		continue;
	        	}
	        	
	        	if (colIndex.size() > 5) {	        		        	
		        	String[] values = line.split(",");
		        	if (values !=null && values.length > 5 && values.length > indexLabelsMax) {
		        		try {
		        			SpudItem item = new SpudItem(
		        					getDouble(values[colIndex.get("y")])%90.00,
		        					getDouble(values[colIndex.get("x")])%180.00);
		        			item.setTxtType(getInteger(values[colIndex.get("type")]));
		        			item.setSpeed(getInteger(values[colIndex.get("speed")])%256);
		        			item.setDirType(getInteger(values[colIndex.get("dirtype")])%3);
		        			
		        			if (values[colIndex.get("direction")].contains(";")) {
		        				String[] newComments = line.split(";");
		        				String[] newValues = newComments[0].split(",");
		        				
		        				item.setDirection(getInteger(newValues[5])%360);
		        				item.setExtraComment(newComments[1]);
		        			} else {
		        				item.setDirection(getInteger(values[colIndex.get("direction")])%360);
		        			}
		        			
		        			if (colIndex.get("comment") != null && values.length > colIndex.get("comment"))
		        				item.setComment(values[colIndex.get("comment")]);
		        			
		        			if (values.length > labels.length) {
		        				StringBuilder cmt = new StringBuilder();
		        		    	for (int i=0;i<values.length-labels.length;i++) {
		        		    		cmt.append(values[i+labels.length]);
		        		    		if (i!=values.length-labels.length-1)cmt.append(",");
		        		    	}
		        				
		        				item.setComment(item.getComment() + cmt.toString());
		        			}
		        			
		        			data.add(item);
		        		} catch (NumberFormatException | NullPointerException e) {
		        			if (!values[colIndex.get("x")].equals("X"))
		        				nbrError++;
		        		}
		        	} else {
		        		nbrIgnored++;
		        	}
	        	} else {
	        		errorMess = Config.getLangBundle().getString("badheader");
	        		break;
	        	}
	        }
	    } finally {
	        br.close();
	    }
		
	    data.trimToSize();
		parseFileListener.onFileParsed(data, spudFile.getPath(), true, nbrError, nbrIgnored, errorMess);
	}
	
	private Double getDouble(String value) throws NumberFormatException, NullPointerException {
		return Double.parseDouble(value.trim());
	}

	private int getInteger(String value) throws NumberFormatException, NullPointerException {
		value = value.trim();
		
		if (value.length() > 0)
			return Integer.parseInt(value);
		else
			return 0;
	}
	
	private int checkFile() throws IOException {
		int res = TYPE_FILE_ERROR;
		
		long len = spudFile.length();
		if (len < 13) 
			return TYPE_FILE_EMPTY;
		
		FileInputStream in = new FileInputStream(spudFile);
		byte[] data = new byte[13];
		
		try {
		    in.read(data);
		} finally {
			in.close();
		}
		
		if (data != null) {
			String s = new String(data, "ISO-8859-1");
			if (s.contains(",")) {
				res = TYPE_FILE_TXT;
				for (int i=0;i<13;i++) {
					if ((data[i] & 0xFF) < 0x20 || ((data[i] & 0xFF) > 0x7E && (data[i] & 0xFF) < 0xFF)) 
						res = TYPE_FILE_ERROR;
				}
			}
			
			if (res == TYPE_FILE_ERROR) {
				if (len%37 == 0)
					res = TYPE_FILE_SPUD_PRIMO;
				else if (len%13 == 0)
					res = TYPE_FILE_SPUD_IGO8;
			}
		}
		
		return res;
	}

	public interface ParseFileListener {
		public void onFileParsed(ArrayList<SpudItem> data, String path, boolean txt, int nbrError, int nbrLinesIgnored, String messageErreur);
		public void onError(String msg);
	}
	
	/* else {
	String metaName = getMeta(spudFile.getName().substring(0, spudFile.getName().contains(".")?spudFile.getName().lastIndexOf("."):spudFile.getName().length()));
	if (metaName != null) {
		try {
			byte[] b = decodeBlowFishTT(data, metaName);
			if (b[0] == 0x64)
				res = TYPE_FILE_TOMTOM;
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	}*/
	
	/*
	private static byte[] decodeBlowFishTT(byte[] input, String meta) throws Exception {
		SecretKeySpec secretKey = new SecretKeySpec(convertToUBytes(meta), "Blowfish");
		Cipher cipher = Cipher.getInstance("Blowfish/CFB/NoPadding");
	    IvParameterSpec ips = new IvParameterSpec(iv);
	    cipher.init(Cipher.DECRYPT_MODE, secretKey, ips);
		
		return cipher.doFinal(input);
	}
	
	public static byte[] convertToUBytes(String s) {
		s = s.replaceAll(" ", "");
		int len = s.length();
	    byte[] data = new byte[len / 2];
	    
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    
	    return data;
	}
	
	private String getMeta(String name) {
		for (String[] fileName:metaTT) {
			if (name.equalsIgnoreCase(fileName[0]))
				return fileName[1];
		}
		
		return null;
	}
	
	private void loadTomtomFile() throws IOException, Exception {
		String meta = getMeta(spudFile.getName().substring(0, spudFile.getName().contains(".")?spudFile.getName().lastIndexOf("."):spudFile.getName().length()));
		
		if (meta != null) {
			SecretKeySpec secretKey = new SecretKeySpec(convertToUBytes(meta), "Blowfish");    
			
		    Cipher cipherOut = Cipher.getInstance("Blowfish/CFB/NoPadding");
		    IvParameterSpec ips = new IvParameterSpec(iv);
		    cipherOut.init(Cipher.DECRYPT_MODE, secretKey, ips);
		    
		    DataInputStream din = new DataInputStream(new BufferedInputStream(new FileInputStream(spudFile)));
		    PipedOutputStream pout = new PipedOutputStream();
		    CipherOutputStream cout = new CipherOutputStream(pout, cipherOut);
			
		    PipedInputStream pin = new PipedInputStream(pout);
			
		    while (pin.available() > 0) {
		    	int tag = pin.read();
		    	
		    	
		    }
		    
		    cout.close();
		    pin.close();
		    din.close();
		}
	}
	
	protected enum Tag {
	    DELETED(0), 
	    EXTENDED_POI(3), 
	    HEADER_TYPE(100), 
	    SIMPLE_POI(2), 
	    SKIPPER(1);
	    
	    private final int mValue;
	    
	    private Tag(final int mValue) {
	        this.mValue = mValue;
	    }
	    
	    static final Tag valueToTag(final int n) {
	        for (final Tag tag : values()) {
	            if (tag.mValue == n) {
	                return tag;
	            }
	        }
	        return null;
	    }
	    
	    final int getValue() {
	        return this.mValue;
	    }
	}*/
}
