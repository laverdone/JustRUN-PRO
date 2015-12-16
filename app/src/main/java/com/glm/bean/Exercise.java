package com.glm.bean;

import java.util.Calendar;

/**
 * Contiene tutte le info per l'esercizio 
 * 
 * @see HistoryActivity
 * **/
public class Exercise {
	/**Id Esercizio*/
	private String sIDExerise;
	/**Titolo Esercizio*/
	private String sTitle;
	/**Note*/
	private String sNote;
	/**Ora Avvio*/
	private String sStart;
	/**Ora di fine*/
	private String sEnd;
	/**Lunghezza della barra*/
	private int iBar=0;
	/**distanza percorsa in unita di misura*/
	private float fDistance=0;
	/**distanza formattata*/
	private String sDistanceFormatted="";
	/**Tipo di esercizio*/
	private int iTypeExercise=0;
	
	
	private String sTotalTime="";
	private String sTotalKalories="";
	private String sAVGSpeed="";
	private String sDateExercise="";
	private Calendar dDateExercise;
	private double dWeight=0.0;
	
	
	public synchronized String getsIDExerise() {
		return sIDExerise;
	}
	public synchronized void setsIDExerise(String sIDExerise) {
		this.sIDExerise = sIDExerise;
	}
	public synchronized String getsTitle() {
		return sTitle;
	}
	public synchronized void setsTitle(String sTitle) {
		this.sTitle = sTitle;
	}
	public synchronized String getsNote() {
		return sNote;
	}
	public synchronized void setsNote(String sNote) {
		this.sNote = sNote;
	}
	public synchronized String getsStart() {
		return sStart;
	}
	public synchronized void setsStart(String sStart) {
		////Log.v(this.getClass().getCanonicalName(),"start:"+sStart);
		//if(sStart==null) return;
		//this.sStart = sdf.format(new Date(sStart));
		this.sStart=sStart;
	}
	public synchronized String getsEnd() {
		return sEnd;
	}
	public synchronized void setsEnd(String sEnd) {
		////Log.v(this.getClass().getCanonicalName(),"sEnd:"+sEnd);
		//if(sEnd==null) return;
		//this.sEnd = sdf.format(new Date(sEnd));
		this.sEnd = sEnd;
	}
	public synchronized int getiBar() {
		return iBar;
	}
	public synchronized void setiBar(int iBar) {
		this.iBar = iBar;
	}
	public synchronized float getfDistance() {
		return fDistance;
	}
	public synchronized void setfDistance(float fDistance) {
		this.fDistance = fDistance;
	}
	public synchronized String getsDistanceFormatted() {
		return sDistanceFormatted;
	}
	public synchronized void setsDistanceFormatted(String sDistanceFormatted) {
		this.sDistanceFormatted = sDistanceFormatted;
	}
	public synchronized String getsTotalTime() {
		return sTotalTime;
	}
	public synchronized void setsTotalTime(String sTotalTime) {
		this.sTotalTime = sTotalTime;
	}
	public synchronized String getsTotalKalories() {
		return sTotalKalories;
	}
	public synchronized void setsTotalKalories(String sTotalKalories) {
		this.sTotalKalories = sTotalKalories;
	}
	public synchronized String getsAVGSpeed() {
		return sAVGSpeed;
	}
	public synchronized void setsAVGSpeed(String sAVGSpeed) {
		this.sAVGSpeed = sAVGSpeed;
	}
	public synchronized Calendar getdDateExercise() {
		return dDateExercise;
	}
	public synchronized void setdDateExercise(Calendar dDateExercise) {
		this.dDateExercise = dDateExercise;
	}
	public synchronized double getdWeight() {
		return dWeight;
	}
	public synchronized void setdWeight(double dWeight) {
		this.dWeight = dWeight;
	}
	public synchronized String getsDateExercise() {
		return sDateExercise;
	}
	public synchronized void setsDateExercise(String sDateExercise) {
		this.sDateExercise = sDateExercise;
	}
	/**
	 * @return the iTypeExercise
	 */
	public synchronized int getiTypeExercise() {
		return iTypeExercise;
	}
	/**
	 * @param iTypeExercise the iTypeExercise to set
	 */
	public synchronized void setiTypeExercise(int iTypeExercise) {
		this.iTypeExercise = iTypeExercise;
	}
	
	
}
