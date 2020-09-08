package com.glm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class WatchPoint implements Parcelable {
	private double dDistance=0.0;
	private double dPace=0.0;
	private double dLat=0.0;
	private double dLong=0.0;
	private Date dDateTime;
	private String sSong;
	private double dAlt=0.0;
	private int bpm=0;
	private LatLng mLatLong;
	private Float mAccurancy;

	public int getiHoursToPrevPoint() {
		return iHoursToPrevPoint;
	}

	public void setiHoursToPrevPoint(int iHoursToPrevPoint) {
		this.iHoursToPrevPoint = iHoursToPrevPoint;
	}

	public int getiMinuteToPrevPoint() {
		return iMinuteToPrevPoint;
	}

	public void setiMinuteToPrevPoint(int iMinuteToPrevPoint) {
		this.iMinuteToPrevPoint = iMinuteToPrevPoint;
	}

	public int getiSecondsToPrevPoint() {
		return iSecondsToPrevPoint;
	}

	public void setiSecondsToPrevPoint(int iSecondsToPrevPoint) {
		this.iSecondsToPrevPoint = iSecondsToPrevPoint;
	}

	public int getiHoursTotal() {
		return iHoursTotal;
	}

	public void setiHoursTotal(int iHoursTotal) {
		this.iHoursTotal = iHoursTotal;
	}

	public int getiMinuteTotal() {
		return iMinuteTotal;
	}

	public void setiMinuteTotal(int iMinuteTotal) {
		this.iMinuteTotal = iMinuteTotal;
	}

	public int getiSecondsTotal() {
		return iSecondsTotal;
	}

	public void setiSecondsTotal(int iSecondsTotal) {
		this.iSecondsTotal = iSecondsTotal;
	}

	private int iHoursToPrevPoint=0;
	private int iMinuteToPrevPoint=0;
	private int iSecondsToPrevPoint=0;

	private int iHoursTotal=0;
	private int iMinuteTotal=0;
	private int iSecondsTotal=0;


	public void setAccurancy(Float mAccurancy) {
		this.mAccurancy = mAccurancy;
	}

	public Float getAccurancy() {

		return mAccurancy;
	}

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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(this.dDistance);
		dest.writeDouble(this.dPace);
		dest.writeDouble(this.dLat);
		dest.writeDouble(this.dLong);
		dest.writeLong(this.dDateTime != null ? this.dDateTime.getTime() : -1);
		dest.writeString(this.sSong);
		dest.writeDouble(this.dAlt);
		dest.writeInt(this.bpm);
		dest.writeParcelable(this.mLatLong, flags);
		dest.writeValue(this.mAccurancy);
		dest.writeInt(this.iHoursToPrevPoint);
		dest.writeInt(this.iMinuteToPrevPoint);
		dest.writeInt(this.iSecondsToPrevPoint);
		dest.writeInt(this.iHoursTotal);
		dest.writeInt(this.iMinuteTotal);
		dest.writeInt(this.iSecondsTotal);
	}

	public WatchPoint() {
	}

	protected WatchPoint(Parcel in) {
		this.dDistance = in.readDouble();
		this.dPace = in.readDouble();
		this.dLat = in.readDouble();
		this.dLong = in.readDouble();
		long tmpDDateTime = in.readLong();
		this.dDateTime = tmpDDateTime == -1 ? null : new Date(tmpDDateTime);
		this.sSong = in.readString();
		this.dAlt = in.readDouble();
		this.bpm = in.readInt();
		this.mLatLong = in.readParcelable(LatLng.class.getClassLoader());
		this.mAccurancy = (Float) in.readValue(Float.class.getClassLoader());
		this.iHoursToPrevPoint = in.readInt();
		this.iMinuteToPrevPoint = in.readInt();
		this.iSecondsToPrevPoint = in.readInt();
		this.iHoursTotal = in.readInt();
		this.iMinuteTotal = in.readInt();
		this.iSecondsTotal = in.readInt();
	}

	public static final Parcelable.Creator<WatchPoint> CREATOR = new Parcelable.Creator<WatchPoint>() {
		@Override
		public WatchPoint createFromParcel(Parcel source) {
			return new WatchPoint(source);
		}

		@Override
		public WatchPoint[] newArray(int size) {
			return new WatchPoint[size];
		}
	};
}
