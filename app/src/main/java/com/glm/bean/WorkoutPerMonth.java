package com.glm.bean;

public class WorkoutPerMonth {
	private int iMonth;
	private String sWorkout;
	public synchronized int getiMonth() {
		return iMonth;
	}
	public synchronized void setiMonth(int iMonth) {
		this.iMonth = iMonth;
	}
	public synchronized String getsWorkout() {
		return sWorkout;
	}
	public synchronized void setsWorkout(String sDistance) {
		this.sWorkout = sWorkout;
	}
}
