package com.glm.bean;

public class DistancePerWeek {
	private int iWeek;
	private String sDistance;
	public synchronized int getiWeek() {
		return iWeek;
	}
	public synchronized void setiWeek(int iWeek) {
		this.iWeek = iWeek;
	}
	public synchronized String getsDistance() {
		return sDistance;
	}
	public synchronized void setsDistance(String sDistance) {
		this.sDistance = sDistance;
	}
}
