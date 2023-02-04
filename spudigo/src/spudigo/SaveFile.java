package spudigo;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.Lock;

public class SaveFile implements Runnable {
	private final Set<Integer> idError = new HashSet<Integer>();
	private final SaveFileListener saveFileListener;
	private final boolean toTxt;
	private final boolean primo;
	private final boolean last;
	private final boolean export;
	private final TemplateFile modelIn;
	private final TemplateFile modelOut;
	private final ArrayList<SpudItem> data;
	private final Lock l;
	private final File fileToSave;
	private final int tabIndex;
	
	public SaveFile(File fileToSave, SaveFileListener saveFileListener, boolean toTxt, TemplateFile modelIn, 
			TemplateFile modelOut, ArrayList<SpudItem> data, boolean primo, boolean last, boolean export, int tabIndex, Lock l) {
		this.fileToSave = fileToSave;
		this.saveFileListener = saveFileListener;
		this.toTxt = toTxt;
		this.modelIn = modelIn;
		this.modelOut = modelOut;
		this.data = data;
		this.primo = primo;
		this.last = last;
		this.export = export;
		this.l = l;
		this.tabIndex = tabIndex;
	}
	
	public void run() {
		l.lock();
		
		SpudItem[] finalData;
		if (export) {
			finalData = new SpudItem[data.size()];
			convertData(finalData);
		} else {
			finalData = data.toArray(new SpudItem[data.size()]);
		}
		
		try {
			if (toTxt)
				saveTxt(finalData);
			else
				saveSpud(finalData);
		} catch (IOException e) {
			if (export) {
				if (last)
					saveFileListener.onSaveExportError(String.format(Config.getLangBundle().getString("errorExportFile"), fileToSave.getName()));
				else
					saveFileListener.onFileExported(String.format(Config.getLangBundle().getString("errorExportFile"), fileToSave.getName()), last);
			} else if (last) {
				saveFileListener.onFileSaved(this.tabIndex, fileToSave.getAbsolutePath(), true);
			}
		}
	}
	
	private void convertData(SpudItem[] finalData) {
		for (int i=0;i<finalData.length;i++) {
			finalData[i] = new SpudItem(
					data.get(i).getLat(),
					data.get(i).getLon());
			finalData[i].setDirection(data.get(i).getDirection());
			finalData[i].setDirType(data.get(i).getDirType());
			finalData[i].setSpeed(data.get(i).getSpeed());
			finalData[i].setStatus(data.get(i).getStatus());
			finalData[i].setComment(data.get(i).getComment());
			finalData[i].setExtraComment(data.get(i).getExtraComment());
			
			if (data.get(i).getBinType() > -1) {
				finalData[i].setBinType(modelOut.getBinTypeWithKey(modelIn.getKeyWithBinType(data.get(i).getBinType())));
				finalData[i].setTxtType(modelOut.getTxtTypeWithKey(modelIn.getKeyWithBinType(data.get(i).getBinType())));
				
				if (finalData[i].getBinType() == -1 || finalData[i].getTxtType() == -1)
					idError.add(data.get(i).getBinType());
			} else {
				finalData[i].setBinType(modelOut.getBinTypeWithKey(modelIn.getKeyWithTxtType(data.get(i).getTxtType())));
				finalData[i].setTxtType(modelOut.getTxtTypeWithKey(modelIn.getKeyWithTxtType(data.get(i).getTxtType())));
				
				if (finalData[i].getBinType() == -1 || finalData[i].getTxtType() == -1)
					idError.add(data.get(i).getTxtType());
			}
		}
	}

	private void saveSpud(SpudItem[] finalData) throws IOException {
		int index = 0;
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileToSave)));
		try {
			for (SpudItem item:finalData) {
				if (item.getBinType() == -1) continue;
				
				if (primo) {
					for (int i=0;i<16;i++) dos.write(0); 
					
					dos.write(getLitEndShort(((int) Math.round(item.getLat()/Math.ulp(1.0f)) >> 21) & 0xffff));
					dos.write(getLitEndShort(((int) Math.round(item.getLon()/Math.ulp(1.0f)) >> 21) & 0xffff));
					dos.write(getLitEndInt(index));
				}
				
				dos.write(getLitEndInt((int) Math.round(item.getLon()/Math.ulp(1.0f))));
				dos.write(getLitEndInt((int) Math.round(item.getLat()/Math.ulp(1.0f))));
				dos.write(item.getSpeed());
				if (item.getBinType() == 4) 
					dos.write((2 << 5) | (item.getBinType() & 0x0000001f));
				else if (item.getBinType() == 3)
					dos.write((7 << 5) | (item.getBinType() & 0x0000001f));
				else
					dos.write((6 << 5) | (item.getBinType() & 0x0000001f));
				dos.write(getLitEndShort((convertDirTypeAppToSpud(item.getDirType()) << 9) | item.getDirection()));
				//dos.write(item.getStatus());
				dos.write(0);
				index++;
			}
		} finally {
			dos.close();
		}
		
		sendEventEnd();
	}
	
	private int convertDirTypeAppToSpud(int dirType) {
		switch (dirType) {
			case 0:
				return 2;
			case 1:
				return 0;
			case 2:
				return 1;
			default:
				return 2;
		}
	}
	
	private byte[] getLitEndShort(int value) {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) value);
		return buffer.array();
	}
	
	private byte[] getLitEndInt(int value) {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt(value);
		return buffer.array();
	}

	private void saveTxt(SpudItem[] finalData) throws IOException {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileToSave), "ISO-8859-1"));
		try {
			bw.write("X,Y,TYPE,SPEED,DIRTYPE,DIRECTION");bw.newLine();
			
			for (SpudItem item:finalData) {
				if (item.getTxtType() == -1) continue;
				
				bw.write(String.format("%." + Config.getInstance().getInteger("position.number.decimal") + "f", 
						item.getLon()).replace(',', '.'));bw.write(",");
				bw.write(String.format("%." + Config.getInstance().getInteger("position.number.decimal") + "f", 
						item.getLat()).replace(',', '.'));bw.write(",");
				bw.write(String.valueOf(item.getTxtType()));bw.write(",");
				bw.write(String.valueOf(item.getSpeed()));bw.write(",");
				bw.write(String.valueOf(item.getDirType()));bw.write(",");
				bw.write(String.valueOf(item.getDirection()));
				
				if (item.getComment() != null && item.getComment().length() > 0) {
					bw.write(",");bw.write(item.getComment());
				}
				
				if (item.getExtraComment() != null && item.getExtraComment().length() > 0) {
					bw.write(" ;");bw.write(item.getExtraComment());
				}
				
				bw.newLine();
			}
		} finally {
			bw.close();
		}
		
		sendEventEnd();
	}
	
	private void sendEventEnd() {
		if (export) {
			if (idError.size() > 0) {
				StringBuilder mes = new StringBuilder(Config.getLangBundle().getString("errorExportUnknownType"));
				Iterator<Integer> it = idError.iterator();
				while(it.hasNext()) {
					mes.append(String.valueOf(it.next()));
					if (it.hasNext())
						mes.append(",");
				}
				mes.append("</b></font>");
				
				saveFileListener.onFileExported(String.format(Config.getLangBundle().getString("saveSucessMes"), fileToSave.getName(), mes.toString()), last);
			} else {
				saveFileListener.onFileExported(String.format(Config.getLangBundle().getString("saveSucess"), fileToSave.getName()), last);
			}
		} else {
			saveFileListener.onFileSaved(this.tabIndex, fileToSave.getAbsolutePath(), false);
		}
	}

	public interface SaveFileListener {
		public void onFileSaved(int tabIndex, String newPath, boolean error);
		public void onFileExported(String msg, boolean last);
		public void onSaveExportError(String msg);
	}
}
