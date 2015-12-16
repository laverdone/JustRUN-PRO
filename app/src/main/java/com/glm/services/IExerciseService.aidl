package com.glm.services;

interface IExerciseService {	
	// Declare any non-default types here with import statements

    /** Request the process ID of this service, to do evil things with it. */
    String getPid();

    boolean startExercise(int iTypeExercise, int goalDistance, double goalHH, double goalMM);
    
    boolean pauseExercise();
    
    boolean resumeExercise();
    
    boolean stopExercise();
    
    boolean saveExercise();
    
    boolean isRunning();
    
    boolean isServiceAlive();
    
    /**Definisce se l'allenamento � in autopausa*/
    boolean isAutoPause();
    
    /**Semaforo usato per il resume dall'auto pausa*/
    boolean isRefumeFromAutoPause();
        
    /**indica se messo in pausa dall'utente*/
    boolean isPause();
    
    void setRefumeFromAutoPause(boolean bResumeAutoPause);
    /**inserire tutti i metodi per prendere i dettagli dell'esercizio*/
    long getExerciseTime();
    
    long getCurrentTime();
    
    String getsCurrentDistance();
    
    String getsPace();
    
    String getsVm();
    
    String getsAltitude();
    
    String getsInclination();
    
    String getsKaloriesBurn();
    
    int getiTypeExercise();
    
    double getLatidute();
    
    double getLongitude();
    
    void stopGPSFix();
    
    void runGPSFix();
      
    void setHeartRate(int iheartRate);
    
    int getHeartRate();
    
    boolean isCardioConnected();
    
    void shutDown();
    
    void skipTrack();
    
    boolean isGPSFixPosition();
    
}
