package com.glm.bean;
/**
 * Bean contenente tutti i dettagli del nuovo Esercizio, 
 * contiene tutte propriet� private cos� possso leggere e 
 * scrivere da tutta l'app.
 * 
 * Tutti i metodi sono sincronizzati, per ottimizzare il codice forse non vanno
 * sincronizzati
 * **/
public final class NewExercise {
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
	private static long lCurrentAltidute=0;
	
	/**Pendenza*/
	private static int iInclication=0;
	
	/**Latidine di esercizio*/
	private static String sCurrentAltidute="";
	
	/**Calorie correnti di esercizio*/
	private static String sCurrentCalories="";
		
	/**Distanza Corrente di esercizio*/
	private static double dCurrentDistance=0;
	/**Velocit� Corrente di esercizio*/
	private static float fCurrentSpeed=0;
	/**Velocit� Totale di esercizio*/
	private static float fTotalSpeed=0;
	/**Stato del GPS*/
	private static boolean bStatusGPS=true;
	
	private static String sPace="";
	
	private static String sVm="";
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
		NewExercise.lStartTime = lStartTime;
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
		NewExercise.lCurrentTime = lCurrentTime;
	}
	
	/**
	 * @param lCurrentLatitude the lCurrentLatitude to set
	 */
	public static synchronized void setlCurrentLatitude(long lCurrentLatitude) {
		NewExercise.dCurrentLatitude = lCurrentLatitude;
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
		NewExercise.dCurrentLongitude = lCurrentLongitude;
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
		NewExercise.sCurrentCalories = sCurrentCalories;
	}
	
	
	
	
	/**
	 * @param iCurrentSpeed the iCurrentSpeed to set
	 */
	public static synchronized void setiCurrentSpeed(int iCurrentSpeed) {
		NewExercise.fCurrentSpeed = iCurrentSpeed;
	}
	
	/**
	 * @param iTotalSpeed the iTotalSpeed to set
	 */
	public static synchronized void setiTotalSpeed(int iTotalSpeed) {
		NewExercise.fTotalSpeed = iTotalSpeed;
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
		NewExercise.lCurrentWatchPoint = lCurrentWatchPoint;
	}
	
	/**
	 * @param lStartLatitude the lStartLatitude to set
	 */
	public static synchronized void setlStartLatitude(long lStartLatitude) {
		NewExercise.dStartLatitude = lStartLatitude;
	}
	
	/**
	 * @param lStartLongitude the lStartLongitude to set
	 */
	public static synchronized void setlStartLongitude(long lStartLongitude) {
		NewExercise.dStartLongitude = lStartLongitude;
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
		NewExercise.lPauseTime = lPauseTime;
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
		NewExercise.dStartLatitude = dStartLatitude;
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
		NewExercise.dStartLongitude = dStartLongitude;
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
		NewExercise.dCurrentLatitude = dCurrentLatitude;
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
		NewExercise.dCurrentLongitude = dCurrentLongitude;
	}
	/**
	 * @return the dCurrentAltidute
	 */
	public static synchronized String getCurrentAltidute() {
		return sCurrentAltidute;
	}
	/**
	 * @param dCurrentAltidute the dCurrentAltidute to set
	 */
	public static synchronized void setCurrentAltidute(String sCurrentAltidute) {
		NewExercise.sCurrentAltidute = sCurrentAltidute;
	}
	
	
	/**
	 * @return the dCurrentDistance
	 */
	public static synchronized double getdCurrentDistance() {
		return dCurrentDistance;
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
	 * @param fCurrentDistance the fCurrentDistance to set
	 */
	public static synchronized void setfCurrentDistance(double dCurrentDistance) {
		NewExercise.dCurrentDistance = dCurrentDistance;
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
		NewExercise.fCurrentSpeed = fCurrentSpeed;
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
		NewExercise.fTotalSpeed = fTotalSpeed;
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
		 sCurrentAltidute="";
		
		/**Calorie correnti di esercizio*/
		sCurrentCalories="";
				
		/**Distanza Corrente di esercizio*/
		dCurrentDistance=0;
		/**Velocit� Corrente di esercizio*/
		fCurrentSpeed=0;
		/**Velocit� Totale di esercizio*/
		fTotalSpeed=0;
		/**Stato del GPS*/
		bStatusGPS=true;
		sPace="";
	}
	public static void setPace(String pace) {
		sPace=pace;
	}
	public static String getPace() {
		return sPace;
	}
	public static synchronized String getsVm() {
		return sVm;
	}
	public static synchronized void setsVm(String sVm) {
		NewExercise.sVm = sVm;
	}
	public static synchronized long getlCurrentAltidute() {
		return lCurrentAltidute;
	}
	public static synchronized void setlCurrentAltidute(long lCurrentAltidute) {
		NewExercise.lCurrentAltidute = lCurrentAltidute;
	}
	public static synchronized int getiInclication() {
		return iInclication;
	}
	public static synchronized void setiInclication(int iInclication) {
		NewExercise.iInclication = iInclication;
	}
}
