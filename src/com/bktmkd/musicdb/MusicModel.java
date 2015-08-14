package com.bktmkd.musicdb;

public class MusicModel {
	
	private int _id;
	private String TITLE;
	private String DURATION;
	private String ARTIST;
	private String _MusicID;
	private String DISPLAY_NAME;
	private String DATA;
	
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getTITLE() {
		return TITLE;
	}
	public void setTITLE(String tITLE) {
		TITLE = tITLE;
	}
	public String getDURATION() {
		return DURATION;
	}
	public void setDURATION(String dURATION) {
		DURATION = dURATION;
	}
	public String getARTIST() {
		return ARTIST;
	}
	public void setARTIST(String aRTIST) {
		ARTIST = aRTIST;
	}
	public String get_MusicID() {
		return _MusicID;
	}
	public void set_MusicID(String _MusicID) {
		this._MusicID = _MusicID;
	}
	public String getDISPLAY_NAME() {
		return DISPLAY_NAME;
	}
	public void setDISPLAY_NAME(String dISPLAY_NAME) {
		DISPLAY_NAME = dISPLAY_NAME;
	}
	public String getDATA() {
		return DATA;
	}
	public void setDATA(String dATA) {
		DATA = dATA;
	}
}
