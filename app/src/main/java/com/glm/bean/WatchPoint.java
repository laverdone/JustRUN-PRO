package com.glm.bean;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class WatchPoint {
	private double dDistance=0.0;
	private double dPace=0.0;
	private double dLat=0.0;
	private double dLong=0.0;
	private Date dDateTime;
	private String sSong;
	private double dAlt=0.0;
	private int bpm=0;
	private LatLng mLatLong;
	/**
	 * @return the dDistance
	 */
	public synchronized double getdDistance() {
		return dDistance;
	}
	/**
	 * @param dDistance the dDistance to set
	 */
	public synchronized void setdDistance(double dDistance) {
		this.dDistance = dDistance;
	}
	/**
	 * @return the dPace
	 */
	public synchronized double getdPace() {
		return dPace/2;
	}
	/**
	 * @param dPace the dPace to set
	 */
	public synchronized void setdPace(double dPace) {
		this.dPace = dPace;
	}
	/**
	 * @return the dLat
	 */
	public synchronized double getdLat() {
		return dLat;
	}
	/**
	 * @param dLat the dLat to set
	 */
	public synchronized void setdLat(double dLat) {
		this.dLat = dLat;
	}
	/**
	 * @return the dLong
	 */
	public synchronized double getdLong() {
		return dLong;
	}
	/**
	 * @param dLong the dLong to set
	 */
	public synchronized void setdLong(double dLong) {
		this.dLong = dLong;
	}
	/**
	 * @return the dDateTime
	 */
	public synchronized Date getdDateTime() {
		return dDateTime;
	}
	/**
	 * @param dDateTime the dDateTime to set
	 */
	public synchronized void setdDateTime(Date dDateTime) {
		this.dDateTime = dDateTime;
	}
	public synchronized String getsSong() {
		return sSong;
	}
	public synchronized void setsSong(String sSong) {
		this.sSong = sSong;
	}
	public synchronized double getdAlt() {
		return dAlt;
	}
	public synchronized void setdAlt(double dAlt) {
		this.dAlt = dAlt;
	}
	public synchronized int getBpm() {
		return bpm;
	}
	public synchronized void setBpm(int bpm) {
		this.bpm = bpm;
	}
	/**
	 * @return the mLatLong
	 */
	public synchronized LatLng getLatLong() {
		return mLatLong;
	}
	/**
	 * @param mLatLong the mLatLong to set
	 */
	public synchronized void setLatLong(double mLong, double mLat) {
		this.mLatLong = new LatLng(mLat, mLong);
		
	}
}
