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

import com.sigpwned.chardet4j.com.ibm.icu.text.CharsetDetector;
import com.sigpwned.chardet4j.com.ibm.icu.text.CharsetMatch;

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

		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(spudFile));

		CharsetDetector detector = new CharsetDetector();
		detector.setText(bis);
		CharsetMatch match = detector.detect();

		if (match.getName().startsWith("UTF-16")) {
			bis.skip(2);
		} else if (match.getName().startsWith("UTF-8")) {
			byte[] bom = new byte[3];

			bis.mark(3);
			bis.read(bom, 0, 3);
			if ((bom[0] != (byte) 0xEF) || (bom[1] != (byte) 0xBB) || (bom[2] != (byte) 0xBF))
				bis.reset();
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(bis, match.getName()));
		try {
			int lengthLabels = 0;
			int indexLabelsMin = 0;

			Map<String, Integer> colIndex = null;
			String line = null;

			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty())
					continue;

				String[] values = line.split(",");

				if (lengthLabels == 0) {
					lengthLabels = values.length;
					colIndex = new HashMap<String, Integer>();

					indexLabelsMin = parseHeaderTextFile(values, colIndex);
					if (indexLabelsMin < 0) {
						errorMess = Config.getLangBundle().getString("badheader");
						break;
					}

					continue;
				}

				if (values.length < indexLabelsMin) {
					nbrIgnored++;
					continue;
				}

				try {
					SpudItem item = new SpudItem(getDouble(values[colIndex.get("y")]) % 90.00,
							getDouble(values[colIndex.get("x")]) % 180.00);
					item.setTxtType(getInteger(values[colIndex.get("type")]));
					item.setSpeed(getInteger(values[colIndex.get("speed")]) % 256);

					if (colIndex.containsKey("dirtype") && values.length > colIndex.get("dirtype"))
						item.setDirType(getInteger(values[colIndex.get("dirtype")]) % 3);

					if (colIndex.containsKey("direction") && values.length > colIndex.get("direction")) {
						if (values[colIndex.get("direction")].contains(";")) {
							String[] newComments = line.split(";");
							String[] newValues = newComments[0].split(",");

							item.setDirection(getInteger(newValues[5]) % 360);
							item.setExtraComment(newComments[1]);
						} else {
							item.setDirection(getInteger(values[colIndex.get("direction")]) % 360);
						}
					}

					if (colIndex.containsKey("comment") && values.length > colIndex.get("comment"))
						item.setComment(values[colIndex.get("comment")]);

					if (values.length > lengthLabels) {
						StringBuilder cmt = new StringBuilder();
						for (int i = 0; i < values.length - lengthLabels; i++) {
							cmt.append(values[i + lengthLabels]);
							if (i != values.length - lengthLabels - 1)
								cmt.append(",");
						}

						item.setComment(item.getComment() + cmt.toString());
					}

					data.add(item);
				} catch (NumberFormatException | NullPointerException e) {
					if (!values[colIndex.get("x")].trim().toLowerCase().equals("x"))
						nbrError++;
				}
			}
		} finally {
			br.close();
		}

		data.trimToSize();
		parseFileListener.onFileParsed(data, spudFile.getPath(), true, nbrError, nbrIgnored, errorMess);
	}
	
	private int parseHeaderTextFile(String[] labels, Map<String, Integer> colIndex) {
		byte controlValue = 0;
		int indexLabelsMin = 0;

		for (int i = 0; i < labels.length; i++) {
			labels[i] = labels[i].trim().toLowerCase(Locale.US);

			switch (labels[i]) {
				case "x":
					indexLabelsMin = Math.max(indexLabelsMin, i);
					controlValue |= 0b1;
					colIndex.put(labels[i], i);
					break;
				case "y":
					indexLabelsMin = Math.max(indexLabelsMin, i);
					controlValue |= 0b10;
					colIndex.put(labels[i], i);
					break;
				case "type":
					indexLabelsMin = Math.max(indexLabelsMin, i);
					controlValue |= 0b100;
					colIndex.put(labels[i], i);
					break;
				case "speed":
					indexLabelsMin = Math.max(indexLabelsMin, i);
					controlValue |= 0b1000;
					colIndex.put(labels[i], i);
					break;
				case "dirtype":
				case "direction":
				case "comment":
					colIndex.put(labels[i], i);
					break;
			}
		}

		return (controlValue == 0b1111) ? indexLabelsMin : -1;
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

		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(spudFile));
		CharsetMatch match;

		byte[] data = new byte[1024];

		try {
			try {
				CharsetDetector detector = new CharsetDetector();
				detector.setText(bis);
				match = detector.detect();
			} catch (Exception ex) {
				match = null;
			}

			bis.read(data);
		} finally {
			bis.close();
		}

		if (match != null) {
			String str = new String(data, match.getName());
			int pos = -1;
			if ((pos = str.indexOf(',')) > 0 && (pos = str.indexOf(',', pos + 1)) > 0
					&& (pos = str.indexOf(',', pos + 1)) > 0 && (pos = str.indexOf(',', pos + 1)) > 0) {
				res = TYPE_FILE_TXT;
			}
		}

		if (res == TYPE_FILE_ERROR) {
			if (len % 37 == 0)
				res = TYPE_FILE_SPUD_PRIMO;
			else if (len % 13 == 0)
				res = TYPE_FILE_SPUD_IGO8;
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
