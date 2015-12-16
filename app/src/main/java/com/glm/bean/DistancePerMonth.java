package com.glm.bean;

public class DistancePerMonth {
	private int iMonth;
	private String sDistance;
	public synchronized int getiMonth() {
		return iMonth;
	}
	public synchronized void setiMonth(int iMonth) {
		this.iMonth = iMonth;
	}
	public synchronized String getsDistance() {
		return sDistance;
	}
	public synchronized void setsDistance(String sDistance) {
		this.sDistance = sDistance;
	}
}
