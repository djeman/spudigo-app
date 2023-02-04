package spudigo;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

public class SpudItem extends DefaultWaypoint {
	private int speed = 0;
	private int txtType = -1;
	private int binType = -1;
	private int dirType = 0;
	private int direction = 0;
	private int status = 0; // 0: New Record 1: Deleted Record 2: Edited Record 
	private String comment = "";
	private String extraComment = null;
	
	public SpudItem() {
		super(0,0);
	}
	
	public SpudItem(double lat, double lon) {
		super(lat,lon);
	}

	public void setCoord(double lat, double lon) {
		super.setPosition(new GeoPosition(lat, lon));
	}

	public double getLat() {
		return super.getPosition().getLatitude();
	}
	
	public double getLon() {
		return super.getPosition().getLongitude();
	}
	
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getTxtType() {
		return txtType;
	}

	public void setTxtType(int txtType) {
		this.txtType = txtType;
	}

	public int getBinType() {
		return binType;
	}

	public void setBinType(int binType) {
		this.binType = binType;
	}

	public int getDirType() {
		return dirType;
	}

	public void setDirType(int dirType) {
		this.dirType = dirType;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getExtraComment() {
		return extraComment;
	}

	public void setExtraComment(String extraComment) {
		this.extraComment = extraComment;
	}
}
