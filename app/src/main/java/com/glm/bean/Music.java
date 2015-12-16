package com.glm.bean;


public class Music {
	 
	private int iID;
	private String sARTIST;
	private String sTITLE;
	private String sFileDATA;
	private String sDISPLAY_NAME;
	private int iDURATION;
	private Float fDistance;
	 
	public synchronized int getiID() {
		return iID;
	}
	public synchronized void setiID(int iID) {
		this.iID = iID;
	}
	public synchronized String getsARTIST() {
		return sARTIST;
	}
	public synchronized void setsARTIST(String sARTIST) {
		this.sARTIST = sARTIST;
	}
	public synchronized String getsTITLE() {
		return sTITLE;
	}
	public synchronized void setsTITLE(String sTITLE) {
		this.sTITLE = sTITLE;
	}
	public synchronized String getsFileDATA() {
		return sFileDATA;
	}
	public synchronized void setsFileDATA(String sFileDATA) {
		this.sFileDATA = sFileDATA;
	}
	public synchronized String getsDISPLAY_NAME() {
		return sDISPLAY_NAME;
	}
	public synchronized void setsDISPLAY_NAME(String sDISPLAY_NAME) {
		this.sDISPLAY_NAME = sDISPLAY_NAME;
	}
	public synchronized int getiDURATION() {
		return iDURATION;
	}
	public synchronized void setiDURATION(int iDURATION) {
		this.iDURATION = iDURATION;
	}
	/**
	 * @return the fFrom
	 */
	public synchronized Float getfDistance() {
		return fDistance;
	}
	/**
	 * @param fFrom the fFrom to set
	 */
	public synchronized void setfDistance(Float fDistance) {
		this.fDistance = fDistance;
	}
	
}
