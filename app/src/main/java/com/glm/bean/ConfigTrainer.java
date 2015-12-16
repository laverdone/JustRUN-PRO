package com.glm.bean;

/**
 * Classe che contiene tutte le configurazioni del Trainer da utilizzare 
 * durante l'esercizio o anche altro
 * 
 * @see PrefActivity
 * **/
public class ConfigTrainer{
	
	private boolean bDisplayMap=false;
	private boolean bDisplayNotification=false;
	private boolean bPlayMusic=false;
	private boolean bUseExternalPlayer=true;
	private boolean bMotivator=false;
	private boolean bAutoPause=false;
	private boolean bInteractiveExercise=false;
	private boolean bTrackExercise=false;
	private boolean bRunGoal=false;
	private boolean bUseCardio=false;
	private boolean bCardioPolarBuyed=false;
	private int iMotivatorTime=0;
	private int iAutoPauseTime=0;
	private boolean isSayDistance=false;
	private boolean isSayTime=false;
	private boolean isSayKalories=false;
	private boolean isSayPace=false;
	private boolean isSayInclination=false;
	private boolean isVirtualRaceSupport=false;
	
	private int iAge=0;
	private Float iWeight=0f;
	private int iHeight=0;
	private int iUserID;
	private String sNick;
	private String sName;
	private String sGender;
	private boolean isShareFB=false;
	private boolean isShareBuzz=false;
	private boolean isShareTwitter=false;
	private boolean bLicence=false;
	private boolean bEndCallOnWorkout=false;
	private String sVersionDesc;
	private int iVersionNumber;
	/**
	 * 0= Sistema Metrico Internazionale Kg/Km/h
	 * 1= Amarica Lb/Miles/h
	 * */
	private int iUnits=0;
	/**GCM Id*/
	private String sGCMId="";
	public synchronized boolean isbDisplayMap() {
		return bDisplayMap;
	}
	public synchronized void setbDisplayMap(boolean bDisplayMap) {
		this.bDisplayMap = bDisplayMap;
	}
	public synchronized boolean isbDisplayNotification() {
		return bDisplayNotification;
	}
	public synchronized void setbDisplayNotification(boolean bDisplayNotification) {
		this.bDisplayNotification = bDisplayNotification;
	}
	public synchronized boolean isbPlayMusic() {
		return bPlayMusic;
	}
	public synchronized void setbPlayMusic(boolean bPlayMusic) {
		this.bPlayMusic = bPlayMusic;
	}
	public synchronized boolean isbMotivator() {
		return bMotivator;
	}
	public synchronized void setbMotivator(boolean bMotivator) {
		this.bMotivator = bMotivator;
	}
	public synchronized boolean isbAutoPause() {
		return bAutoPause;
	}
	public synchronized void setbAutoPause(boolean bAutoPause) {
		this.bAutoPause = bAutoPause;
	}
	public synchronized boolean isbInteractiveExercise() {
		return bInteractiveExercise;
	}
	public synchronized void setbInteractiveExercise(boolean bInteractiveExercise) {
		this.bInteractiveExercise = bInteractiveExercise;
	}
	

	/**
	 * 0=km/m
	 * 1=Mi/m
	 * */
	public synchronized int getiUnits() {
		return iUnits;
	}
	public synchronized void setiUnits(int iUnits) {
		this.iUnits = iUnits;
	}
	/**
	 * @return the iAge
	 */
	public synchronized int getiAge() {
		return iAge;
	}
	/**
	 * @param iAge the iAge to set
	 */
	public synchronized void setiAge(int iAge) {
		this.iAge = iAge;
	}
	/**
	 * @return the iWeight
	 */
	public synchronized Float getiWeight() {
		return iWeight;
	}
	/**
	 * @param iWeight the iWeight to set
	 */
	public synchronized void setiWeight(Float iWeight) {
		this.iWeight = iWeight;
	}
	/**
	 * @return the sNick
	 */
	public synchronized String getsNick() {
		return sNick;
	}
	/**
	 * @param sNick the sNick to set
	 */
	public synchronized void setsNick(String sNick) {
		this.sNick = sNick;
	}
	/**
	 * @return the sName
	 */
	public synchronized String getsName() {
		return sName;
	}
	/**
	 * @param sName the sName to set
	 */
	public synchronized void setsName(String sName) {
		this.sName = sName;
	}
	/**
	 * @return the sGender
	 */
	public synchronized String getsGender() {
		return sGender;
	}
	/**
	 * @param sGender the sGender to set
	 */
	public synchronized void setsGender(String sGender) {
		this.sGender = sGender;
	}
	/**
	 * @return the isShareFB
	 */
	public synchronized boolean isShareFB() {
		return isShareFB;
	}
	/**
	 * @param isShareFB the isShareFB to set
	 */
	public synchronized void setShareFB(boolean isShareFB) {
		this.isShareFB = isShareFB;
	}
	/**
	 * @return the isShareBuzz
	 */
	public synchronized boolean isShareBuzz() {
		return isShareBuzz;
	}
	/**
	 * @param isShareBuzz the isShareBuzz to set
	 */
	public synchronized void setShareBuzz(boolean isShareBuzz) {
		this.isShareBuzz = isShareBuzz;
	}
	/**
	 * @return the isShareTwitter
	 */
	public synchronized boolean isShareTwitter() {
		return isShareTwitter;
	}
	/**
	 * @param isShareTwitter the isShareTwitter to set
	 */
	public synchronized void setShareTwitter(boolean isShareTwitter) {
		this.isShareTwitter = isShareTwitter;
	}
	/**
	 * @return the iUserID
	 */
	public synchronized int getiUserID() {
		return iUserID;
	}
	/**
	 * @param iUserID the iUserID to set
	 */
	public synchronized void setiUserID(int iUserID) {
		this.iUserID = iUserID;
	}
	/**
	 * @return the bTrackExercise
	 */
	public synchronized boolean isbTrackExercise() {
		return bTrackExercise;
	}
	/**
	 * @param bTrackExercise the bTrackExercise to set
	 */
	public synchronized void setbTrackExercise(boolean bTrackExercise) {
		this.bTrackExercise = bTrackExercise;
	}
	/**
	 * 0=short 		1Min
	 * 1=medium		3Min
	 * 2=long		5Min
	 * 
	 * */
	public synchronized int getiMotivatorTime() {
		return iMotivatorTime;
	}
	public synchronized void setiMotivatorTime(int iMotivatorTime) {
		this.iMotivatorTime = iMotivatorTime;
	}
	public synchronized boolean isSayDistance() {
		return isSayDistance;
	}
	public synchronized void setSayDistance(boolean isSayDistance) {
		this.isSayDistance = isSayDistance;
	}
	public synchronized boolean isSayTime() {
		return isSayTime;
	}
	public synchronized void setSayTime(boolean isSayTime) {
		this.isSayTime = isSayTime;
	}
	public synchronized boolean isSayKalories() {
		return isSayKalories;
	}
	public synchronized void setSayKalories(boolean isSayKalories) {
		this.isSayKalories = isSayKalories;
	}
	public synchronized boolean isSayPace() {
		return isSayPace;
	}
	public synchronized void setSayPace(boolean isSayPace) {
		this.isSayPace = isSayPace;
	}
	public synchronized boolean isbLicence() {
		return bLicence;
	}
	public synchronized void setbLicence(boolean bLicence) {
		this.bLicence = bLicence;
	}
	public synchronized int getiAutoPauseTime() {
		return iAutoPauseTime;
	}
	public synchronized void setiAutoPauseTime(int iAutoPauseTime) {
		this.iAutoPauseTime = iAutoPauseTime;
	}
	public synchronized boolean isbEndCallOnWorkout() {
		return bEndCallOnWorkout;
	}
	public synchronized void setbEndCallOnWorkout(boolean bEndCallOnWorkout) {
		this.bEndCallOnWorkout = bEndCallOnWorkout;
	}
	public synchronized boolean isSayInclination() {
		return isSayInclination;
	}
	public synchronized void setSayInclination(boolean isSayInclination) {
		this.isSayInclination = isSayInclination;
	}
	public synchronized boolean isbRunGoal() {
		return bRunGoal;
	}
	public synchronized void setbRunGoal(boolean bRunGoal) {
		this.bRunGoal = bRunGoal;
	}
	public synchronized String getsVersionDesc() {
		return sVersionDesc;
	}
	public synchronized void setsVersionDesc(String sVersionDesc) {
		this.sVersionDesc = sVersionDesc;
	}
	public synchronized int getiVersionNumber() {
		return iVersionNumber;
	}
	public synchronized void setiVersionNumber(int iVersionNumber) {
		this.iVersionNumber = iVersionNumber;
	}
	public synchronized boolean isbUseCardio() {
		return bUseCardio;
	}
	public synchronized void setbUseCardio(boolean bUseCardio) {
		this.bUseCardio = bUseCardio;
	}
	public synchronized boolean isbCardioPolarBuyed() {
		return bCardioPolarBuyed;
	}
	public synchronized void setbCardioPolarBuyed(boolean bCardioPolarBuyed) {
		this.bCardioPolarBuyed = bCardioPolarBuyed;
	}
	/**
	 * @return the iHeight
	 */
	public synchronized int getiHeight() {
		return iHeight;
	}
	/**
	 * @param iHeight the iHeight to set
	 */
	public synchronized void setiHeight(int iHeight) {
		this.iHeight = iHeight;
	}
	/**
	 * @return the sGCMId
	 */
	public synchronized String getsGCMId() {
		return sGCMId;
	}
	/**
	 * @param sGCMId the sGCMId to set
	 */
	public synchronized void setsGCMId(String sGCMId) {
		this.sGCMId = sGCMId;
	}
	/**
	 * @return the isVirtualRaceSupport
	 */
	public synchronized boolean isVirtualRaceSupport() {
		return isVirtualRaceSupport;
	}
	/**
	 * @param isVirtualRaceSupport the isVirtualRaceSupport to set
	 */
	public synchronized void setVirtualRaceSupport(boolean isVirtualRaceSupport) {
		this.isVirtualRaceSupport = isVirtualRaceSupport;
	}
	/**
	 * @return the bUseExternalPlayer
	 */
	public boolean isbUseExternalPlayer() {
		return bUseExternalPlayer;
	}
	/**
	 * @param bUseExternalPlayer the bUseExternalPlayer to set
	 */
	public void setbUseExternalPlayer(boolean bUseExternalPlayer) {
		this.bUseExternalPlayer = bUseExternalPlayer;
	}
}
