package com.glm.bean;

public class DistancePerExercise {
	/***
	 * 0=running
	 * 1=biking
	 * 100=walking
	 * 2=skiing
	 * 
	 * */
	private int iTypeExercise;
	private String sDistance;
	private String sCalories;
	private String sTotalTime;
	public synchronized int getiTypeExercise() {
		return iTypeExercise;
	}
	public synchronized void setiTypeExercise(int iTypeExercise) {
		this.iTypeExercise = iTypeExercise;
	}
	public synchronized String getsDistance() {
		return sDistance;
	}
	public synchronized void setsDistance(String sDistance) {
		this.sDistance = sDistance;
	}
	/**
	 * @return the sCalories
	 */
	public synchronized String getsCalories() {
		return sCalories;
	}
	/**
	 * @param sCalories the sCalories to set
	 */
	public synchronized void setsCalories(String sCalories) {
		this.sCalories = sCalories;
	}
	/**
	 * @return the sTotalTime
	 */
	public synchronized String getsTotalTime() {
		return sTotalTime;
	}
	/**
	 * @param sTotalTime the sTotalTime to set
	 */
	public synchronized void setsTotalTime(String sTotalTime) {
		this.sTotalTime = sTotalTime;
	}
}
