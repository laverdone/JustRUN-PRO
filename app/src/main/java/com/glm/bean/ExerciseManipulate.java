package com.glm.bean;

import java.util.ArrayList;
import java.util.Date;

/**
 * Bean contenente tutti i dettagli del nuovo Esercizio, 
 * contiene tutte propriet� private cos� possso leggere e 
 * scrivere da tutta l'app.
 * 
 * Tutti i metodi sono sincronizzati, per ottimizzare il codice forse non vanno
 * sincronizzati
 * **/
public final class ExerciseManipulate {
	private static Date dDateTimeStart;
	private static int iTotalTimeInSeconds;
	private static String sStepCount;
	/**Velocita media distanza/tempo*/
	private static String sAVGSpeed;
	/*Passo medio tempo/distanza**/
	private static String sMinutePerDistance;
	/**velocità massima distanza/tempo*/
	private static String sMAXSpeed;
	/**passo massimo tempo/distanza*/
	private static String sMAXMinutePerDistance;
	private static String sNote;
	/**battiti medi*/
	private static int iAVGBpm;
	/**battiti massimi*/
	private static int iMAXBpm;
	/**Velocità media in MT/H*/
	private static double dAVGSpeed=0.0;
	/**Distanza Totale di esercizio*/
	private static double dTotalDistance=0.0;
		
	/**Id dell'esercizio corrente*/
	private static int iIDExercise;
	private static int iTypeExercise=0;
	
	/**Tempo di inizio esercizio*/
	private static long lStartTime=0;
	/**Tempo Corrente di esercizio*/
	private static long lCurrentTime=0;
	/**Current WatchPoint*/
	private static long lCurrentWatchPoint=0;
	/**Current WatchPoint*/
	private static long lPauseTime=0;
	
	/**Latidine di esercizio*/
	private static double dStartLatitude=0;
	/**Longitudine di esercizio*/
	private static double dStartLongitude=0;
	/**Latidine di double*/
	private static double dCurrentLatitude=0;
	/**Longitudine di esercizio*/	
	private static double dCurrentLongitude=0;
	
	/**Latidine di esercizio*/
	private static double dStartAltidute=0;
	/**Latidine di esercizio*/
	private static double dCurrentAltidute=0;
	
	/**Calorie correnti di esercizio*/
	private static String sCurrentCalories="";
	
	/**Distanza Totale di esercizio*/
	private static String sTotalDistance;
	/**Distanza Corrente di esercizio*/
	private static float fCurrentDistance=0;
	/**Velocit� Corrente di esercizio*/
	private static float fCurrentSpeed=0;
	/**Velocit� Totale di esercizio*/
	private static float fTotalSpeed=0;
	/**Stato del GPS*/
	private static boolean bStatusGPS=true;
	
	/**tempo totale di corsa*/
	private static String sTotalTime="";
	private static ArrayList<WatchPoint> oWatchPoint = new ArrayList<WatchPoint>();
	/**
	 * Ritorna il tempo di avvio esercizio
	 * @return the lTotalTime
	 */
	public static synchronized long getlStartTime() {
		return lStartTime;
	}
	/**
	 * Imposta il tempo di avvio esercizio
	 * 
	 * @param lStartTime the lStartTime to set
	 */
	public static synchronized void setlStartTime(long lStartTime) {
		ExerciseManipulate.lStartTime = lStartTime;
	}
	/**
	 * Ritorna il tempo di esercizio aka tempo totale/parziale
	 * 
	 * @return the lCurrentTime
	 */
	public static synchronized long getlCurrentTime() {
		return lCurrentTime;
	}
	/**
	 * Imposta il tempo di esercizio aka tempo totale/parziale
	 * 
	 * @param lCurrentTime the lCurrentTime to set
	 */
	public static synchronized void setlCurrentTime(long lCurrentTime) {
		ExerciseManipulate.lCurrentTime = lCurrentTime;
	}
	
	/**
	 * @param lCurrentLatitude the lCurrentLatitude to set
	 */
	public static synchronized void setlCurrentLatitude(long lCurrentLatitude) {
		ExerciseManipulate.dCurrentLatitude = lCurrentLatitude;
	}
	/**
	 * @return the lCurrentLongitude
	 */
	public static synchronized double getlCurrentLongitude() {
		return dCurrentLongitude;
	}
	/**
	 * @param lCurrentLongitude the lCurrentLongitude to set
	 */
	public static synchronized void setlCurrentLongitude(long lCurrentLongitude) {
		ExerciseManipulate.dCurrentLongitude = lCurrentLongitude;
	}
	/**
	 * @return the iCurrentCalories
	 */
	public static synchronized String getsCurrentCalories() {
		return sCurrentCalories;
	}
	/**
	 * @param iCurrentCalories the iCurrentCalories to set
	 */
	public static synchronized void setsCurrentCalories(String sCurrentCalories) {
		ExerciseManipulate.sCurrentCalories = sCurrentCalories;
	}
	
	/**
	 * @param iTotalDistance the iTotalDistance to set
	 */
	public static synchronized void setsTotalDistance(String sTotalDistance) {
		ExerciseManipulate.sTotalDistance = sTotalDistance;
	}
	
	
	/**
	 * @param iCurrentSpeed the iCurrentSpeed to set
	 */
	public static synchronized void setiCurrentSpeed(int iCurrentSpeed) {
		ExerciseManipulate.fCurrentSpeed = iCurrentSpeed;
	}
	
	/**
	 * @param iTotalSpeed the iTotalSpeed to set
	 */
	public static synchronized void setiTotalSpeed(int iTotalSpeed) {
		ExerciseManipulate.fTotalSpeed = iTotalSpeed;
	}
	/**
	 * @return the lCurrentWatchPoint
	 */
	public static synchronized long getlCurrentWatchPoint() {
		return lCurrentWatchPoint;
	}
	/**
	 * @param lCurrentWatchPoint the lCurrentWatchPoint to set
	 */
	public static synchronized void setlCurrentWatchPoint(long lCurrentWatchPoint) {
		ExerciseManipulate.lCurrentWatchPoint = lCurrentWatchPoint;
	}
	
	/**
	 * @param lStartLatitude the lStartLatitude to set
	 */
	public static synchronized void setlStartLatitude(long lStartLatitude) {
		ExerciseManipulate.dStartLatitude = lStartLatitude;
	}
	
	/**
	 * @param lStartLongitude the lStartLongitude to set
	 */
	public static synchronized void setlStartLongitude(long lStartLongitude) {
		ExerciseManipulate.dStartLongitude = lStartLongitude;
	}
	/**
	 * preleva il Tempo quando l'utente mette in pausa o autopausa
	 * 
	 * @return the lPauseTime
	 */
	public static synchronized long getlPauseTime() {
		return lPauseTime;
	}
	/**
	 * Imposta il Tempo quando l'utente mette in pausa o autopausa
	 * @param lPauseTime the lPauseTime to set
	 */
	public static synchronized void setlPauseTime(long lPauseTime) {
		ExerciseManipulate.lPauseTime = lPauseTime;
	}
	/**
	 * @return the dStartLatitude
	 */
	public static synchronized double getdStartLatitude() {
		return dStartLatitude;
	}
	/**
	 * @param dStartLatitude the dStartLatitude to set
	 */
	public static synchronized void setdStartLatitude(double dStartLatitude) {
		ExerciseManipulate.dStartLatitude = dStartLatitude;
	}
	/**
	 * @return the dStartLongitude
	 */
	public static synchronized double getdStartLongitude() {
		return dStartLongitude;
	}
	/**
	 * @param dStartLongitude the dStartLongitude to set
	 */
	public static synchronized void setdStartLongitude(double dStartLongitude) {
		ExerciseManipulate.dStartLongitude = dStartLongitude;
	}
	/**
	 * @return the dCurrentLatitude
	 */
	public static synchronized double getdCurrentLatitude() {
		return dCurrentLatitude;
	}
	/**
	 * @param dCurrentLatitude the dCurrentLatitude to set
	 */
	public static synchronized void setdCurrentLatitude(double dCurrentLatitude) {
		ExerciseManipulate.dCurrentLatitude = dCurrentLatitude;
	}
	/**
	 * @return the dCurrentLongitude
	 */
	public static synchronized double getdCurrentLongitude() {
		return dCurrentLongitude;
	}
	/**
	 * @param dCurrentLongitude the dCurrentLongitude to set
	 */
	public static synchronized void setdCurrentLongitude(double dCurrentLongitude) {
		ExerciseManipulate.dCurrentLongitude = dCurrentLongitude;
	}
	/**
	 * @return the dStartAltidute
	 */
	public static synchronized double getdStartAltidute() {
		return dStartAltidute;
	}
	/**
	 * @param dStartAltidute the dStartAltidute to set
	 */
	public static synchronized void setdStartAltidute(double dStartAltidute) {
		ExerciseManipulate.dStartAltidute = dStartAltidute;
	}
	/**
	 * @return the dCurrentAltidute
	 */
	public static synchronized double getdCurrentAltidute() {
		return dCurrentAltidute;
	}
	/**
	 * @param dCurrentAltidute the dCurrentAltidute to set
	 */
	public static synchronized void setdCurrentAltidute(double dCurrentAltidute) {
		ExerciseManipulate.dCurrentAltidute = dCurrentAltidute;
	}
	/**
	 * @return the dTotalDistance
	 */
	public static synchronized String getsTotalDistance() {
		return sTotalDistance;
	}
	
	/**
	 * @return the dCurrentDistance
	 */
	public static synchronized double getdCurrentDistance() {
		return fCurrentDistance;
	}
	
	/**
	 * @return the dCurrentSpeed
	 */
	public static synchronized double getdCurrentSpeed() {
		return fCurrentSpeed;
	}
	
	/**
	 * @return the dTotalSpeed
	 */
	public static synchronized double getdTotalSpeed() {
		return fTotalSpeed;
	}
	/**
	 * @return the fCurrentDistance
	 */
	public static synchronized float getfCurrentDistance() {
		return fCurrentDistance;
	}
	/**
	 * @param fCurrentDistance the fCurrentDistance to set
	 */
	public static synchronized void setfCurrentDistance(float fCurrentDistance) {
		ExerciseManipulate.fCurrentDistance = fCurrentDistance;
	}
	/**
	 * @return the fCurrentSpeed
	 */
	public static synchronized float getfCurrentSpeed() {
		return fCurrentSpeed;
	}
	/**
	 * @param fCurrentSpeed the fCurrentSpeed to set
	 */
	public static synchronized void setfCurrentSpeed(float fCurrentSpeed) {
		ExerciseManipulate.fCurrentSpeed = fCurrentSpeed;
	}
	/**
	 * @return the fTotalSpeed
	 */
	public static synchronized float getfTotalSpeed() {
		return fTotalSpeed;
	}
	/**
	 * @param fTotalSpeed the fTotalSpeed to set
	 */
	public static synchronized void setfTotalSpeed(float fTotalSpeed) {
		ExerciseManipulate.fTotalSpeed = fTotalSpeed;
	}
	public static boolean isGPSEnabled() {
		// TODO Auto-generated method stub
		return bStatusGPS;
	}
	public static boolean setGPSStatus(boolean bStatus) {
		// TODO Auto-generated method stub
		bStatusGPS=bStatus;
		return bStatusGPS;
	}
	/**Reimposta tutte le proprietà a 0*/
	public static void reset() {
		/**Tempo di inizio esercizio*/
		lStartTime=0;
		/**Tempo Corrente di esercizio*/
		lCurrentTime=0;
		/**Current WatchPoint*/
		lCurrentWatchPoint=0;
		/**Current WatchPoint*/
		lPauseTime=0;
		
		/**Latidine di esercizio*/
		 dStartLatitude=0;
		/**Longitudine di esercizio*/
		 dStartLongitude=0;
		/**Latidine di double*/
		 dCurrentLatitude=0;
		/**Longitudine di esercizio*/	
		 dCurrentLongitude=0;
		
		/**Latidine di esercizio*/
		 dStartAltidute=0;
		/**Latidine di esercizio*/
		 dCurrentAltidute=0;
		
		/**Calorie correnti di esercizio*/
		sCurrentCalories="";
		
		/**Distanza Totale di esercizio*/
		sTotalDistance="";
		/**Distanza Corrente di esercizio*/
		fCurrentDistance=0;
		/**Velocit� Corrente di esercizio*/
		fCurrentSpeed=0;
		/**Velocit� Totale di esercizio*/
		fTotalSpeed=0;
		/**Stato del GPS*/
		bStatusGPS=true;
		
	}
	public static synchronized int getiIDExercise() {
		return iIDExercise;
	}
	public static synchronized void setiIDExercise(int iIDExercise) {
		ExerciseManipulate.iIDExercise = iIDExercise;
	}
	public synchronized static String getsAVGSpeed() {
		return sAVGSpeed;
	}
	public synchronized static void setsAVGSpeed(String sMAXSpeed) {
		ExerciseManipulate.sAVGSpeed = sMAXSpeed;
	}
	public synchronized static String getsMAXSpeed() {
		return sMAXSpeed;
	}
	public synchronized static void setsMAXSpeed(String sMAXSpeed) {
		ExerciseManipulate.sMAXSpeed = sMAXSpeed;
	}
	public static void setsStepCount(String sStepCount) {
		ExerciseManipulate.sStepCount = sStepCount;
	}
	
	public static String getsStepCount() {
		return sStepCount;
	}
	/**
	 * @return the sMinutePerDistance
	 */
	public static synchronized String getsMinutePerDistance() {
		return sMinutePerDistance;
	}
	/**
	 * @param sMinutePerDistance the sMinutePerDistance to set
	 */
	public static synchronized void setsMinutePerDistance(String sMinutePerDistance) {
		ExerciseManipulate.sMinutePerDistance = sMinutePerDistance;
	}

	public static synchronized String getsMAXMinutePerDistance() {
		return sMAXMinutePerDistance;
	}
	/**
	 * @param sMinutePerDistance the sMinutePerDistance to set
	 */
	public static synchronized void setsMAXMinutePerDistance(String sMAXMinutePerDistance) {
		ExerciseManipulate.sMAXMinutePerDistance = sMAXMinutePerDistance;
	}
	/**
	 * @return the oWatchPoint
	 */
	public static synchronized ArrayList<WatchPoint> getWatchPoint() {
		return oWatchPoint;
	}
	/**
	 * @param oWatchPoint the oWatchPoint to set
	 */
	public static synchronized void setWatchPoint(ArrayList<WatchPoint> oWatchPoint) {
		ExerciseManipulate.oWatchPoint = oWatchPoint;
	}
	public static synchronized String getsTotalTime() {
		return sTotalTime;
	}
	public static synchronized void setsTotalTime(String sTotalTime) {
		ExerciseManipulate.sTotalTime = sTotalTime;
	}
	public static synchronized double getdAVGSpeed() {
		return dAVGSpeed;
	}
	public static synchronized void setdAVGSpeed(double dAVGSpeed) {
		ExerciseManipulate.dAVGSpeed = dAVGSpeed;
	}
	public static synchronized double getdTotalDistance() {
		return dTotalDistance;
	}
	public static synchronized void setdTotalDistance(double dTotalDistance) {
		ExerciseManipulate.dTotalDistance = dTotalDistance;
	}
	public static synchronized int getiTypeExercise() {
		return iTypeExercise;
	}
	public static synchronized void setiTypeExercise(int iTypeExercire) {
		ExerciseManipulate.iTypeExercise = iTypeExercire;
	}
	public static synchronized String getsNote() {
		return sNote;
	}
	public static synchronized void setsNote(String sNote) {
		ExerciseManipulate.sNote = sNote;
	}
	public static synchronized int getiTotalTimeInSeconds() {
		return iTotalTimeInSeconds;
	}
	public static synchronized void setiTotalTimeInSeconds(int iTotalTimeInSeconds) {
		ExerciseManipulate.iTotalTimeInSeconds = iTotalTimeInSeconds;
	}
	public static synchronized Date getdDateTimeStart() {
		return dDateTimeStart;
	}
	public static synchronized void setdDateTimeStart(Date dDateTimeStart) {
		ExerciseManipulate.dDateTimeStart = dDateTimeStart;
	}
	public static synchronized int getiAVGBpm() {
		return iAVGBpm;
	}
	public static synchronized void setiAVGBpm(int iAVGBpm) {
		ExerciseManipulate.iAVGBpm = iAVGBpm;
	}
	public static synchronized int getiMAXBpm() {
		return iMAXBpm;
	}
	public static synchronized void setiMAXBpm(int iMAXBpm) {
		ExerciseManipulate.iMAXBpm = iMAXBpm;
	}
}
