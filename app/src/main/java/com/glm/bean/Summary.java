package com.glm.bean;

public class Summary {
	private double dDistance;
	private String sDistance;
	private double dKalories;
	private String sKalories;
	private double dTime;
	private int iSteps;
	private String sSteps;
	public synchronized double getdDistance() {
		return dDistance;
	}
	public synchronized void setdDistance(double dDistance) {
		this.dDistance = dDistance;
	}
	public synchronized String getsDistance() {
		return sDistance;
	}
	public synchronized void setsDistance(String sDistance) {
		this.sDistance = sDistance;
	}
	public synchronized double getdKalories() {
		return dKalories;
	}
	public synchronized void setdKalories(double dKalories) {
		this.dKalories = dKalories;
	}
	public synchronized String getsKalories() {
		return sKalories;
	}
	public synchronized void setsKalories(String sKalories) {
		this.sKalories = sKalories;
	}
	public synchronized int getiSteps() {
		return iSteps;
	}
	public synchronized void setiSteps(int iSteps) {
		this.iSteps = iSteps;
	}
	public synchronized String getsSteps() {
		return sSteps;
	}
	public synchronized void setsSteps(String sSteps) {
		this.sSteps = sSteps;
	}
	
}
