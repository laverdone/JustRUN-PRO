package com.glm.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.glm.bean.CardioDevice;
import com.glm.bean.ConfigTrainer;
import com.glm.services.IExerciseService;
import com.glm.trainer.MainActivity;
import com.glm.trainer.R;
import com.glm.utils.AccelerometerListener;
import com.glm.utils.AccelerometerUtils;
import com.glm.utils.ExerciseUtils;
import com.glm.utils.MediaTrainer;
import com.glm.utils.VoiceToSpeechTrainer;
import com.glm.utils.http.HttpClientHelper;
import com.glm.utils.sensor.BlueToothHelper;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Avvio il nuovo Esercizio come servizio in background e quindi
 * lo user per popolare la GUI se visibile.
 * 
 * TODO
 * **/
public class ExerciseService extends Service implements LocationListener, AccelerometerListener {
   
	
	/**Proprietà che sostituiranno il NewExecise*/
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
	
	private static float dPace=0;
	private static String sPace="";
	
	private static String sVm="";
	/**Proprietà che sostituiranno il NewExecise*/
	
	
	private static int iHeartRate=0;
    private static Context mContext;
	private static MediaTrainer oMediaPlayer;
	private VoiceToSpeechTrainer oVoiceSpeechTrainer ;
	/**stringe del motivatore*/
	private String sDistance;
	private String sUnit;
	/**
	 * identifica il tipo di esercizio
	 * 0=run
	 * 1=bike
	 * */
	private int iTypeExercise;
	/**
	 * Media Player
	 * */
	
	private boolean bStopListener=false;
	private NotificationManager mNM;
	
	//Identifica i Km di goal se 0 il goal e solo tempo
	private int iGoalDistance=0;
	//Ientifica le ore di goal, se ore e minuti sono 0 il goal è solo distance
	private double dGoalHH=0;
	//Ientifica i minuti di goal, se ore e minuti sono 0 il goal è solo distance
	private double dGoalMM=0;
	/**Timer da ripetere per il Goal*/
	private Timer GoalTimer = null;
	//Ritardo di pronuncia goal
	private final int iDELAY_GOAL=20000;
	
	private boolean isServiceAlive=false;
    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private static int NOTIFICATION = R.string.app_name_pro;
    private PendingIntent contentIntent;
    private Notification notification;
    // this is a hack and need to be changed, here the offset is the length of the tag XML "</Document></kml", 
    // we minus this offset from the end of the file and write the next Placemark entry.  
    //private static final int KML_INSERT_OFFSET = 17;
    
    private static final int gpsMinDistance = 5;
   
    /**Avvio ms del Timer per le coordinate e salvare il tutto in DB*/
    private static final int START_TIMER_DELAY = 0;
    /**Intervallo ms Periodico del Timer per le coordinate e salvare il tutto in DB*/
    private static final int PERIOD_TIMER_DELAY = 0;
    /**Periodo di lettura accelerometro*/
    private static final int PERIOD_ACCELEROMETER_TIMER=1000;
    
    private static final int GEOCODER_MAX_RESULTS = 5;
    
    private LocationManager LocationManager = null;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private double pre_latitude = 0.0;
    private double pre_longitude = 0.0;
    private long altitude = 0;
    private long pre_altitude = 0;
    
    /**Conta passi*/
    private int iStep=0;
    /**Timer per il motivatore*/
    private Timer MotivatorTimer = null;
    /**Timer per la condivisione interattiva*/
    private Timer InteractiveTimer=null;
    
    /**Timer per le Virtual Race*/
    private Timer VirtualRaceTimer=null;
    private HttpClientHelper oHttpHelper = null;
    
    /**Timer per la condivisione interattiva*/
    private Timer AutoPauseTimer=null;
    /**Avvio ms del Timer per per l'autopausa*/
    private static final int SHORT_PERIOD_AUTOPAUSE_TIMER_DELAY=60000;
    private static final int MEDIUM_PERIOD_AUTOPAUSE_TIMER_DELAY=180000;
    private static final int LONG_PERIOD_AUTOPAUSE_TIMER_DELAY=300000;
    
    private static int iAutoPauseDelay=SHORT_PERIOD_AUTOPAUSE_TIMER_DELAY;
    
    /**Avvio ms del Timer per le coordinate e salvare il tutto in DB*/
    private static final int SHORT_START_MOTIVATOR_TIMER_DELAY = 60000;
    /**Intervallo ms Periodico del Timer per le coordinate e salvare il tutto in DB ms*/
    private static final int SHORT_PERIOD_MOTIVATOR_TIMER_DELAY = 60000;
    /**Avvio ms del Timer per le coordinate e salvare il tutto in DB*/
    private static final int MEDIUM_START_MOTIVATOR_TIMER_DELAY = 180000;
    /**Intervallo ms Periodico del Timer per le coordinate e salvare il tutto in DB ms*/
    private static final int MEDIUM_PERIOD_MOTIVATOR_TIMER_DELAY = 180000;
    /**Avvio ms del Timer per le coordinate e salvare il tutto in DB*/
    private static final int LONG_START_MOTIVATOR_TIMER_DELAY = 300000;
    /**Intervallo ms Periodico del Timer per le coordinate e salvare il tutto in DB ms*/
    private static final int LONG_PERIOD_MOTIVATOR_TIMER_DELAY = 300000;
    
    private static int iDelayMotivator=0;
    private int iRepeatMotivator=0;
    
    
    
    /**Timer usato per la scrittura nel DB*/
    private Timer monitoringTimer = null;
    
    /**Thread principale per il controllo del tempo di corsa quando il servizio e' attivo**/
    private Thread oRunningThread;
    
    /**Tempo di corsa*/
    private static long diffTime;
    
    /**identifica se il servizio e' up and running*/
    private static boolean isRunning = false;
    /**identifica se il servizio e' in AutoPausa*/
    private static boolean isAutoPause = false;
    /**identifica se il servizio e' in Resume AutoPausa*/
    private static boolean isResumeAutoPause = false;
    /**indica se il servizio e' in pausa*/
    private static boolean isPause=false;
    /**indica se è stata fixata la posizione*/
    private static boolean isFixPosition=false;
    ArrayList<Messenger> mClients = new ArrayList<Messenger>(); // Keeps track of all current registered clients.
    /**messaggi supportati*/
    public static final int PAUSEEXERCISE=1;
    public static final int RESUMEEXERCISE=2;
    public static final int STARTEXERCISE=3;
    public static final int STOPSERVICE=4;
    public static final int SAVEEXERCISE=5;
	
    
    
    //private final IBinder mBinder = new TrainerServiceBinder(); 
    
    /**Continene tutte le configurazioni del Trainer**/
    private static ConfigTrainer oConfigTrainer;
   

    private String sPid="";
    /**
     * Gestione delle chiamate in arrivo
     * */    
    TelephonyManager telephonyManager=null;
    /** Will notify us on changes to the PhoneState*/
	
	/**identifica se c' una chiamata*/
	private boolean isInCalling=false;
	
	/**AudioManger per la gestione del volume*/
	AudioManager oAudioManager = null;
	
	/**gestione dei backup sul cloud*/
	//private BackupManager oBackupManager =null;
	
	/**Gestione Cardio*/
	private static BlueToothHelper oBTHelper;
	
	private Timer tCardioTimer;
	
	/**
	 * Gestione delle chiamate in arrivo
	 * */  
    @Override
    public void onCreate() {    	      
    	mContext=getApplicationContext();
    	oHttpHelper = new HttpClientHelper();
    	
        SimpleDateFormat sdf = new SimpleDateFormat("ssSSS");
        sPid=sdf.format(new Date());
        
        Log.i("Start Service ExerciseSevice", "OK");
        //Carico la configurazione
        try{
        	oConfigTrainer=ExerciseUtils.loadConfiguration(mContext);
        }catch (NullPointerException e) {
			Log.e(this.getClass().getCanonicalName(),"Error load Config");
			return;
		}
        
        isServiceAlive=true;
                
        oVoiceSpeechTrainer = new VoiceToSpeechTrainer(mContext);
                
		oAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);          
		AccelerometerUtils.setContext(mContext);	
	
		//Cardio
		if(oConfigTrainer!=null && oConfigTrainer.isbUseCardio() && oConfigTrainer.isbCardioPolarBuyed()){
			oBTHelper = new BlueToothHelper();
		}
				
		//super.onCreate();
		/*oBackupManager.requestRestore(new RestoreObserver() {
            public void restoreFinished(int error) {
                *//** Done with the restore!  Now draw the new state of our data *//*
                //Log.v(this.getClass().getCanonicalName(), "Restore finished, error = " + error);
            }
        });*/
		if(oConfigTrainer.isbUseExternalPlayer()){
			Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
		
    }
    /**
     * Esecuzione di avvio esercizio
     * 
     * **/
    public int onStartCommand(Intent intent, int flags, int startId) {
    	Log.i(this.getClass().getCanonicalName(),"on start Command");
        return Service.START_STICKY;
    }
    
    /**
     * Avvio il FIX del GPS
     * @throws Throwable 
     * */
    private void startGPSFix(){
    	//IMPOSTO QUELLO DEL ON CREATE
    	mContext=getApplicationContext();
    	oHttpHelper = new HttpClientHelper();
    	
        SimpleDateFormat sdf = new SimpleDateFormat("ssSSS");
        sPid=sdf.format(new Date());
        
        Log.i("Start Service ExerciseSevice", "OK");
        //Carico la configurazione
        try{
        	oConfigTrainer=ExerciseUtils.loadConfiguration(mContext);
        }catch (NullPointerException e) {
			Log.e(this.getClass().getCanonicalName(),"Error load Config");
			return;
		}
        
        isServiceAlive=true;
                
        oVoiceSpeechTrainer = new VoiceToSpeechTrainer(mContext);
                
		oAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);          
		AccelerometerUtils.setContext(mContext);	
	
		//Cardio
		if(oConfigTrainer.isbUseCardio() && oConfigTrainer.isbCardioPolarBuyed()){
			oBTHelper = new BlueToothHelper();
		}

    	
    	
    	isFixPosition=false;
    	latitude = 0.0;
        longitude = 0.0;
    	pre_latitude = 0.0;
        pre_longitude = 0.0;
        
    	try{
	    	LocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	    	LocationManager.requestLocationUpdates(android.location.LocationManager.GPS_PROVIDER, PERIOD_TIMER_DELAY, gpsMinDistance, this, Looper.getMainLooper());
	 		Log.i(this.getClass().getCanonicalName(),"Add GPS Fix");
    	}catch (RuntimeException e) {
			Log.e(this.getClass().getCanonicalName(),"Errore avvio esercizio");
			e.printStackTrace();
			endAllListner();
			try {
				this.finalize();
			} catch (Throwable e1) {
				Log.e(this.getClass().getCanonicalName(),"Errore finalize esercizio");
				System.exit(0);
			}
		}
    }
    
    @Override
    public void onLocationChanged(Location location) {                  	  
       isFixPosition=true;
       String sAlt="";
       if(bStopListener) return;
       if(!isRunning) return;
       //Non catturo piu' le coordinate quando sono in pausa
       /*if(location==null) {  	   
    	   startGPSFix();
    	   return;
       }*/
       latitude = location.getLatitude();
       longitude = location.getLongitude();
       altitude = Math.round(location.getAltitude());        
       
       NumberFormat oNFormat = NumberFormat.getNumberInstance();
	   oNFormat.setMaximumFractionDigits(2);
	   sAlt=oNFormat.format(altitude);
       if(ExerciseService.dStartLatitude==0){
    	   //Catturo le coordinate di avvio
    	   ExerciseService.dStartLatitude=latitude;
    	   ExerciseService.dStartLongitude=longitude;
          
    	   ExerciseService.sCurrentAltidute=sAlt;
       }
       ExerciseService.dCurrentLatitude=latitude;
       ExerciseService.dCurrentLongitude=longitude;
       // imposto l'altitudine
       ExerciseService.lCurrentAltidute=altitude;      
       
       ExerciseService.sCurrentAltidute=sAlt;      		

       if(telephonyManager!=null){
			if(telephonyManager.getCallState()==TelephonyManager.CALL_STATE_RINGING){
				stopMediaDuringCall();
			}else if(telephonyManager.getCallState()==TelephonyManager.CALL_STATE_IDLE){
				resumeMediaDuringCall();
			}
	    }
       
       //Lettuta alla notification dei GPS
       if (longitude != 0.0 && latitude != 0.0){    						
			if(pre_latitude!=latitude && pre_longitude!=longitude){
				//Cancello l'auto pausa e 
				if(isAutoPause){
					removeAutoPauseTimer();
					resumeAutoPauseExercise(); 
					addAutoPauseTimer();
				}else{	
					//Metto l'autopausa solo se running
					if(isRunning){
						removeAutoPauseTimer();
						addAutoPauseTimer();
					}
					
				}
				ExerciseService.fCurrentSpeed=  (float) (ExerciseService.dCurrentDistance/(diffTime*0.001)/60/60);
				
				if(oConfigTrainer.isbPlayMusic() && !isInCalling && !isAutoPause){  
					//Controllo se sono in pausa prima di riavviare
		    		if(oMediaPlayer!=null && !oMediaPlayer.isPlaying() && !oMediaPlayer.isInPause()){
		    			if(!oMediaPlayer.play(false)){
		    				oMediaPlayer=null;
		    				System.gc();
		    				oMediaPlayer = new MediaTrainer(mContext, oConfigTrainer.isbUseExternalPlayer());
		    				oMediaPlayer.play(false);
		    			}
		    		}
				}											
				if(oMediaPlayer!=null){
					ExerciseUtils.saveCoordinates(mContext, pre_latitude,pre_longitude,
							latitude, longitude, altitude, 
							getLocationName(latitude,longitude), oMediaPlayer.getCurrentSong(), (float) ((ExerciseService.dCurrentDistance*1000)/(diffTime*0.001)), location.getAccuracy(), location.getTime(),iHeartRate, oConfigTrainer);
				}else{
					ExerciseUtils.saveCoordinates(mContext, pre_latitude,pre_longitude,
							latitude, longitude, altitude, 
							getLocationName(latitude,longitude), null, (float) ((ExerciseService.dCurrentDistance*1000)/(diffTime*0.001)), location.getAccuracy(), location.getTime(),iHeartRate, oConfigTrainer);
				}
						
					
					double dPartialDistance=0;
					dPartialDistance=ExerciseUtils.getPartialDistanceUnFormattated(pre_longitude,pre_latitude,longitude,latitude);
					
					ExerciseService.dCurrentDistance = dPartialDistance
							+ExerciseService.dCurrentDistance;
									
					//ExerciseUtils.saveCurrentDistance(dPartialDistance,mContext,oConfigTrainer);

					pre_latitude=latitude;
					pre_longitude=longitude;
					pre_altitude=altitude;		
			}
		}
		if(isAutoPause && oMediaPlayer!=null) oMediaPlayer.pause();	
		//Log.i(this.getClass().getCanonicalName(),"run TimeTask");
		//sendMessageToUI(1229);
		 if(oRunningThread!=null){
		     if(!oRunningThread.isAlive()){
		   	   oRunningThread.run();
		     }
		   }
    }

    @Override
    public void onProviderDisabled(String provider) {
    	//Log.i("GPSLoggerService.onProviderDisabled().","onProviderDisabled");
    	ExerciseService.bStatusGPS = false;
    	//Se disabilitano il GPS fermo il trainer
    	stopExerciseAsService();
    }

    @Override
    public void onProviderEnabled(String provider) {
    	//Log.i("GPSLoggerService.onProviderEnabled().","onProviderEnabled");
    	ExerciseService.bStatusGPS = true;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    	//if(status==LocationProvider.AVAILABLE) isFixPosition=true;
    }    
        
    private void startMotivatorTimer(final boolean bSpeech){   	
    	try{
    		if(MotivatorTimer==null){
    		
	    		/**Avvio Scrittura dati nel DB*/
	        	MotivatorTimer = new Timer();
	        	MotivatorTimer.scheduleAtFixedRate(
	        			new TimerTask()
	        			{
	        				@Override
	        				public void run()
	        				{
	        					
	        					
	        					if(!isRunning) return;
	        					/**imposto la distanza corrente*/
	        					ExerciseService.dCurrentDistance = 
	        							ExerciseUtils.getTotalDistanceUnFormattated(mContext, oConfigTrainer, 
	        									ExerciseUtils.getsIDCurrentExercise(mContext), null);
	        					
	        					final String sDistanceToSpeech=String.valueOf(
	        							ExerciseUtils.getTotalDistanceFormattated(ExerciseService.dCurrentDistance,oConfigTrainer,false));
	        					if(!isInCalling){
	        						if(oConfigTrainer.isbPlayMusic()){
	            						//Abbasso il volume della musica 
	            						if(oMediaPlayer!=null) oMediaPlayer.pause();
	            						
	            					}  
	        						
	    								String sTimeToSpeech=mContext.getString(R.string.time)+ExerciseUtils.getTimeHHMMForSpeech(ExerciseService.lCurrentTime,mContext);	
	    								String sKaloriesToSpeech=String.valueOf(ExerciseService.sCurrentCalories)+mContext.getString(R.string.kaloriesspeech);
	    								String sMinutePerUnit=mContext.getString(R.string.speach_measure_min_km);
	    								
	    								if(oConfigTrainer.getiUnits()==1){			
	    									sMinutePerUnit=mContext.getString(R.string.speach_measure_min_miles);
	    								}
	    								////Log.v(this.getClass().getCanonicalName(),"Kalories: "+sKaloriesToSpeech);
	    								ExerciseService.fCurrentSpeed= (float) (ExerciseService.dCurrentDistance/(diffTime*0.001)/60/60);
	    								
	    								ExerciseService.iInclication=ExerciseUtils.getInclination(mContext, ExerciseUtils.getsIDCurrentExercise(mContext));
	    								//ExerciseService.sPace =ExerciseUtils.getPace(mContext, oConfigTrainer);
	    								NumberFormat oNFormat = NumberFormat.getNumberInstance();
	    								oNFormat.setMaximumFractionDigits(0);	
	    								String sSec = oNFormat.format(Math.round((Math.round(60/(((ExerciseService.dCurrentDistance*1000)/(((diffTime*0.001)/60)*60))*3.6)) - 60/(((ExerciseService.dCurrentDistance*1000)/(((diffTime*0.001)/60)*60))*3.6))*60));
	    								    								
	    								//ExerciseService.sPace=oNFormat.format(60/(((ExerciseService.dCurrent(Distance*1000)/(((diffTime*0.001)/60)*60))*3.6));
	    								ExerciseService.sPace=String.valueOf(Math.round(60/(((ExerciseService.dCurrentDistance*1000)/(((diffTime*0.001)/60)*60))*3.6)))+","+sSec.replace("-", "");
	        							
	    								//TTS Speech
	        							Log.i(this.getClass().getCanonicalName(),"Trainer Type: TTS");
	        							
	        							if(bSpeech) oVoiceSpeechTrainer.sayDistanza(mContext, oConfigTrainer, 
	        									sDistanceToSpeech, oMediaPlayer, sTimeToSpeech,sKaloriesToSpeech,
	        									ExerciseService.sPace+sMinutePerUnit, ExerciseService.iInclication+"%", iHeartRate); 
	        							//showNotification(R.drawable.start_trainer, getText(R.string.start_exercise)+" StepCount: "+iStep);
	        							
	        							//Avvio il timer del goal solo in certe condizioni
	        							
	        							if(iGoalDistance!=0 ||
	        									dGoalHH!=0 ||
	        									dGoalMM!=0){
	        								goalTimerStart();
	        							}
	        							//System.gc();
	        					}    							
	        				}
	        			}, 
	        			iDelayMotivator,
	    		   	    iRepeatMotivator);	
    		}
    	}catch (IllegalArgumentException e) {
			Log.e(this.getClass().getCanonicalName(),"Error start Time");
			e.printStackTrace();
		}
    	
    }
    /**
     * metodo richiamato dal motivator se corrro col Goal
     * */
    private void goalTimerStart() {
    	GoalTimer = new Timer();
    	
    	GoalTimer.schedule(new TimerTask()
    			{
    				@Override
    				public void run()
    				{
    					
    					
    					if(!isRunning) return;
    					//Log.v(this.getClass().getCanonicalName(), "GoalHH:"+dGoalHH+" GoalMM"+dGoalMM+" GoalDiance:"+iGoalDistance);
    					//Goal solo distanza
    					if((dGoalHH==0 && dGoalMM==0) && iGoalDistance!=0){
    						
							
							double iDistanceToGoal;
    						/**imposto la distanza corrente*/
							ExerciseService.dCurrentDistance = 
        							ExerciseUtils.getTotalDistanceUnFormattated(mContext, oConfigTrainer, 
        									ExerciseUtils.getsIDCurrentExercise(mContext), null);
        					iDistanceToGoal=(iGoalDistance/1000)-ExerciseService.dCurrentDistance;
        					//TODO OBIETTIVO RAGGIUNTO
        					if(iDistanceToGoal<0) iDistanceToGoal=0;
        					final String sDistanceToSpeech=String.valueOf(
        							ExerciseUtils.getTotalDistanceFormattated(iDistanceToGoal,oConfigTrainer,false));
        					
        					//Esco se ho superato l'obiettivo
							if(iDistanceToGoal<=0)
								return;
        					
        					if(!isInCalling){
        						if(oConfigTrainer.isbPlayMusic()){
            						//Abbasso il volume della musica 
            						oMediaPlayer.pause();
            						
            					}  
        						
    								Log.i(this.getClass().getCanonicalName(),"Trainer Type: TTS");
        							oVoiceSpeechTrainer.sayDistanzaToGoal(mContext, oConfigTrainer, 
        									sDistanceToSpeech, oMediaPlayer); 
        							//showNotification(R.drawable.start_trainer, getText(R.string.start_exercise)+" StepCount: "+iStep);   							  							
        					} 
    					}else if((dGoalHH!=0 || dGoalMM!=0) && iGoalDistance==0){
    						//Goal solo Tempo
    						
    						//Esco se ho superato l'obiettivo
							if(dGoalHH<ExerciseUtils.getTimeHH(ExerciseService.lCurrentTime,mContext)
									&&
							   dGoalMM<ExerciseUtils.getTimeMM(ExerciseService.lCurrentTime,mContext))
								return;
							
    						if(!isInCalling){
        						if(oConfigTrainer.isbPlayMusic()){
            						//Abbasso il volume della musica 
            						oMediaPlayer.pause();
            						
            					}  
        						
    								Log.i(this.getClass().getCanonicalName(),"Trainer Type: TTS");
        							oVoiceSpeechTrainer.sayTimeToGoal(mContext, oConfigTrainer, 
        									dGoalHH,dGoalMM,
        									ExerciseUtils.getTimeHH(ExerciseService.lCurrentTime,mContext),
        									ExerciseUtils.getTimeMM(ExerciseService.lCurrentTime,mContext), oMediaPlayer); 
        							//showNotification(R.drawable.start_trainer, getText(R.string.start_exercise)+" StepCount: "+iStep);   							  							
        					}
    					}else{
    						if(!isInCalling){
    							//Esco se ho superato l'obiettivo
    							if(iGoalDistance<ExerciseUtils.getTotalDistanceUnFormattated(mContext, oConfigTrainer, 
    									ExerciseUtils.getsIDCurrentExercise(mContext), null))
    								return;
    							
    							if(oConfigTrainer.isbPlayMusic()){
            						//Abbasso il volume della musica 
            						oMediaPlayer.pause();
            						
            					} 
        						
	    						//Goal con entrambi Time e Distance
	    						double dVelocitaMediaGoal=0;
	    						//Calcolo la velocità media che devo mantenere in KM/H
	    						try{
	    							dVelocitaMediaGoal=(iGoalDistance/1000)/(dGoalHH+(dGoalMM/60));
	    						}catch (ArithmeticException e) {
									Log.e(this.getClass().getCanonicalName(),"Errore calcolo velocità media!");
									dVelocitaMediaGoal=0;
								}
	    						//oVoiceSpeechTrainer.say("Media Goal è "+dVelocitaMediaGoal+", Media corrente è "+ExerciseUtils.getVelocitaMedia(mContext));
	    						
	    						oVoiceSpeechTrainer.sayDistanceAndTimeToGoal(mContext, oConfigTrainer, 
    								dVelocitaMediaGoal,
    								ExerciseUtils.getVelocitaMedia(mContext), oMediaPlayer);
    						}
    					}   					   							
    				}
    			}, iDELAY_GOAL);
		
	}


	private String getLocationName(double latitude, double longiture){
            String name = "";
//            Geocoder geocoder = new Geocoder(this);
//            
//            try {
//                    List<Address> address = geocoder.getFromLocation(latitude, longiture, ExerciseService.GEOCODER_MAX_RESULTS);
//                    
//                    name = address.get(0).toString();
//            } catch (IOException e) {
//                    e.printStackTrace();
//            }               
            
            return name;
    }
    /**
     * Show a notification while this service is running.
     * 
     * @param String sMessageToShow
     */
    private Notification showNotification(int iIcon, CharSequence charSequence) {
    	//Controllo la configurazione dal DB
    	//Con lo startforeground la notidicha è sempre necessaria
    	//if(!oConfigTrainer.isbDisplayNotification()) return null;
    	// In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.app_name_pro);

        // Set the icon, scrolling text and timestamp
        notification = new Notification(iIcon, text,
                System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification Controller
        contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, charSequence,
                       text, contentIntent);

        // Send the notification.
        //if(mNM!=null) mNM.notify(NOTIFICATION, notification);
        return notification;
    }             
	
	/**
     * Classe che si occupa di monitorare il tempo di corsa istantaneo ogni sec.
     * 
     * **/
    public static class ThRunningTime implements Runnable{
    	public void run() {      		
    		while(!Thread.currentThread().isInterrupted()){
    			
				try {
					diffTime = (System.currentTimeMillis () - ExerciseService.lStartTime+ExerciseService.lPauseTime);
					//Log.i("difftime: ",String.valueOf(diffTime));
					//Log.i("NewExercise.getlStartTime(): ",String.valueOf(NewExercise.getlStartTime()));
					ExerciseService.lCurrentTime=diffTime;
					ExerciseService.lCurrentWatchPoint=diffTime;	
				
					
					if(oConfigTrainer.isbUseCardio() 
							&& 
							oConfigTrainer.isbCardioPolarBuyed() 
							&& oBTHelper!=null){
						
						iHeartRate=oBTHelper.getHeartRate();
					}
					Thread.sleep(1000);					
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}   	   
    	}
    } 
    /**
     * AUTOPAusa l'esercizio disattivando tutti servizi che usano GPS/Accelerometro e salvataggi al DB
     * per un nuovo esercizio
     * 
     * 
     * **/
    private void autopauseExercise(){
    	isAutoPause		= true;
    	bStopListener	= false;
    	isRunning 		= false;  
    	isServiceAlive 	= true;
    	if(oConfigTrainer.isbPlayMusic()){
    		//TODO Play music during exercise STOP MUSIC
    		//Log.v(this.getClass().getCanonicalName(), "Music AUTOPAUSE Pause");
    		if(oMediaPlayer!=null) oMediaPlayer.pause();
    	}
	   	if(MotivatorTimer!=null) MotivatorTimer.cancel();
    	//Log.v(this.getClass().getCanonicalName(), "AUTO-Pause Exercise");
        //mClients.add(msg.replyTo);
        //showNotification(R.drawable.pause_trainer, getText(R.string.pauseexercise));    	
        ExerciseService.lPauseTime=ExerciseService.lCurrentWatchPoint;
    }
    /**
     * RiAvvia l'esercizio messo in autopausa riattivando tutti servizi che usano GPS/Accelerometro e salvataggi al DB
     * per un nuovo esercizio
     * 
     * @see IExerciseService.Stub
     * **/
    private void resumeAutoPauseExercise(){
    	isResumeAutoPause = true;
    	isAutoPause		= false;
    	bStopListener	= false;
    	isRunning 		= true;  
    	isServiceAlive 	= true;
    	//Log.v(this.getClass().getCanonicalName(), "Resume AUTOPAUSE Exercise");
    	if(oConfigTrainer.isbPlayMusic()){
    		//TODO Play music during exercise STOP MUSIC
    		//Log.v(this.getClass().getCanonicalName(), "Music AUTOPAUSE Resume");
    		if(oMediaPlayer!=null) oMediaPlayer.play(true);
    	}
	   	startMotivatorTimer(oConfigTrainer.isbMotivator());
        //mClients.remove(msg.replyTo);
    	//showNotification(R.drawable.start_trainer, getText(R.string.start_exercise));    	
    	ExerciseService.lStartTime=System.currentTimeMillis();
    }
    
    /**
     * PAusa l'esercizio disattivando tutti servizi che usano GPS/Accelerometro e salvataggi al DB
     * per un nuovo esercizio
     * 
     * @see IExerciseService.Stub
     * **/
    private void pauseExerciseAsService(){
    	isPause			  = true;
    	isResumeAutoPause = false;
    	bStopListener	= true;
    	isRunning 		= false;  
    	isServiceAlive 	= true;
    	if(oConfigTrainer.isbPlayMusic()){
    		//TODO Play music during exercise STOP MUSIC
    		//Log.v(this.getClass().getCanonicalName(), "Music Pause");
    		if(oMediaPlayer!=null) oMediaPlayer.pause();
    	}
	   	if(MotivatorTimer!=null) MotivatorTimer.cancel();
    	//Log.v(this.getClass().getCanonicalName(), "Pause Exercise");
        //mClients.add(msg.replyTo);
        //showNotification(R.drawable.pause_trainer, getText(R.string.pauseexercise));    	
        ExerciseService.lPauseTime=ExerciseService.lCurrentWatchPoint;
    	oRunningThread.interrupt();
    	/**Arresto il listener del Conta Passi*/
    	if (AccelerometerUtils.isListening()) {
    		AccelerometerUtils.stopListening();
        }
    }
    
    /**
     * RiAvvia l'esercizio messo in pausa riattivando tutti servizi che usano GPS/Accelerometro e salvataggi al DB
     * per un nuovo esercizio
     * 
     * @see IExerciseService.Stub
     * **/
    private void resumeExerciseAsService(){
    	isPause			= false;
    	bStopListener	= false;
    	isRunning 		= true;  
    	isServiceAlive 	= true;
    	//Log.v(this.getClass().getCanonicalName(), "Resume Exercise");
    	
    	if(oConfigTrainer.isbPlayMusic()){   
    		//Log.v(this.getClass().getCanonicalName(), "Music Resume");
    		if(oMediaPlayer!=null) oMediaPlayer.play(true);
    	}
	   	startMotivatorTimer(oConfigTrainer.isbMotivator());
        //mClients.remove(msg.replyTo);
    	//showNotification(R.drawable.start_trainer, getText(R.string.start_exercise));    	
    	ExerciseService.lStartTime=System.currentTimeMillis();
 		oRunningThread = new Thread(new ThRunningTime());
 		oRunningThread.start();
    }
    /**
     * Avvia tutti i servizi che usano GPS/Accelerometro e salvataggi al DB
     * per un nuovo esercizio
     * 
     * @see IExerciseService.Stub
     * **/
    private void startExerciseAsService(int TypeExercise, int goalDistance, double goalHH, double goalMM){ 
    	Log.v(this.getClass().getCanonicalName(),"startExerciseAsService ");
    	isServiceAlive=true;

    	oVoiceSpeechTrainer = new VoiceToSpeechTrainer(mContext);

    	oAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);          
    	AccelerometerUtils.setContext(mContext);	

    	//Cardio
    	if(oConfigTrainer.isbUseCardio() && oConfigTrainer.isbCardioPolarBuyed()){
    		oBTHelper = new BlueToothHelper();
    	} 		
    	
    	mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE); 
		
		ExerciseService.dStartLatitude=0;
		ExerciseService.dStartLongitude=0;
		isFixPosition=false;
    	
    	
    	isResumeAutoPause = false;
    	isAutoPause		= false;
    	isRunning 		= true;   
    	isServiceAlive 	= true;
    	bStopListener	= false;
    	iTypeExercise   = TypeExercise;    	
    	iStep			= 0;
    	latitude 		= 0.0;
    	longitude 		= 0.0;
    	pre_latitude 	= 0.0;
    	pre_longitude 	= 0.0;
    	iStep			=	0;
    	dGoalHH=goalHH;
    	dGoalMM=goalMM;

    	if(goalDistance==0){

    	}else if(goalDistance==0){
    		iGoalDistance=0;
    	}else if(goalDistance==1){
    		iGoalDistance=5000;
    	}else if(goalDistance==2){
    		iGoalDistance=10000;
    	}else if(goalDistance==3){
    		iGoalDistance=15000;
    	}else if(goalDistance==4){
    		iGoalDistance=21097;
    	}else if(goalDistance==5){
    		iGoalDistance=31000;
    	}else if(goalDistance==6){
    		iGoalDistance=42195;
    	}
    	//Imposto 
    	//Carico la configurazione
    	oConfigTrainer=ExerciseUtils.loadConfiguration(mContext);
    	//Forzo il reset dell'esercizio
    	//NewExercise.reset();
    	if(oConfigTrainer.isbPlayMusic()){
    		//AVVIO DEL PLAYER ESTERNO	
    		oMediaPlayer = new MediaTrainer(mContext, oConfigTrainer.isbUseExternalPlayer());
    		
    		oMediaPlayer.play(false);
    		listenForIncomingCall();
    	}
    	if(oConfigTrainer.isbAutoPause()){	
    		if(oConfigTrainer.getiAutoPauseTime()==0){
    			//- none -
    			iAutoPauseDelay=-1;

    		}else if(oConfigTrainer.getiAutoPauseTime()==0){
				//Short 1 Min
				iAutoPauseDelay=ExerciseService.SHORT_PERIOD_AUTOPAUSE_TIMER_DELAY;

			}else if(oConfigTrainer.getiAutoPauseTime()==1){
    			//Medium 3 Min
    			iAutoPauseDelay=ExerciseService.MEDIUM_PERIOD_AUTOPAUSE_TIMER_DELAY;

    		}else if(oConfigTrainer.getiAutoPauseTime()==2){
    			//Long 5 Min
    			iAutoPauseDelay=ExerciseService.LONG_PERIOD_AUTOPAUSE_TIMER_DELAY;

    		}
    	}


    	//Imposto la frequenza del motivatore
    	if(oConfigTrainer.getiMotivatorTime()==0){
    		//Short 1 Min
    		iDelayMotivator=ExerciseService.SHORT_START_MOTIVATOR_TIMER_DELAY;
    		iRepeatMotivator=ExerciseService.SHORT_PERIOD_MOTIVATOR_TIMER_DELAY;
    	}else if(oConfigTrainer.getiMotivatorTime()==1){
    		//Medium 3 Min
    		iDelayMotivator=ExerciseService.MEDIUM_START_MOTIVATOR_TIMER_DELAY;
    		iRepeatMotivator=ExerciseService.MEDIUM_PERIOD_MOTIVATOR_TIMER_DELAY;
    	}else if(oConfigTrainer.getiMotivatorTime()==2){
    		//Long 5 Min
    		iDelayMotivator=ExerciseService.LONG_START_MOTIVATOR_TIMER_DELAY;
    		iRepeatMotivator=ExerciseService.LONG_PERIOD_MOTIVATOR_TIMER_DELAY;
    	}



    	//Log.v(this.getClass().getCanonicalName(), "Motivator On");
    	sDistance=mContext.getString(R.string.currentdistance);
    	if(oConfigTrainer.getiUnits()==1){
    		sUnit=mContext.getString(R.string.miles);
    	}else{
    		sUnit=mContext.getString(R.string.kilometer);
    	}
    	if(oConfigTrainer.isbMotivator()) oVoiceSpeechTrainer.say(mContext.getString(R.string.startexercise));

    	//Avvio sempre il motivatore ma non esegua la say se disabilitato
    	startMotivatorTimer(oConfigTrainer.isbMotivator());	
    	/**Aggiungo il timer per l'interactive sharing*/
    	if(oConfigTrainer.isbInteractiveExercise()){
    		/**Aggiungo il timer per l'interactive sharing*/
    		
    	}
    	
    	/**Aggiungo il timer per le Virtual race*/
    	
    	if(oConfigTrainer.isVirtualRaceSupport()){
    		addVirtualRaceTimer();
    	}

    	//AutoPause
    	if(oConfigTrainer.isbAutoPause() && iAutoPauseDelay>0) addAutoPauseTimer();

    	//Abilito l'incoming call listener
    	if (AccelerometerUtils.isSupported() 
    			&& iTypeExercise!=1) {
    		AccelerometerUtils.startListening(ExerciseService.this);
    	}	 
    	//startGPSFix();
    	//Log.v(this.getClass().getCanonicalName(), "Start New Exercise");



    	//Cardio
    	if(oConfigTrainer.isbUseCardio() && oConfigTrainer.isbCardioPolarBuyed()){
    		startCardio();
    	}
  		
    	ExerciseService.lStartTime=System.currentTimeMillis();
    	oRunningThread = new Thread(new ThRunningTime());
    	oRunningThread.start(); 

  		

    	
  		startForeground(Integer.parseInt(sPid), showNotification(R.drawable.start_trainer, getText(R.string.app_name_buy)));
    }
    /**
     * Fermo tutti i servizi che usano GPS/Accelerometro e salvataggi al DB
     * 
     * @see IExerciseService.Stub
     * **/
    private void stopExerciseAsService(){  	
    	bStopListener	= true;
    	isRunning 		= false;  
    	isServiceAlive 	= true;
    	isAutoPause     = false;
    	if(oConfigTrainer.isbPlayMusic()){   		
    		if(oMediaPlayer!=null && oMediaPlayer.isPlaying()){
    			oMediaPlayer.destroy();   			    			
        		oMediaPlayer=null;
    		}    			
    	} 	
    	//Cardio
	   	if(oConfigTrainer.isbUseCardio() && oConfigTrainer.isbCardioPolarBuyed()){
			stopCardio();
		}
    	endAllListner();
    }
    /**
     * Salvo l'esercizio corrente
     * 
     * @see IExerciseService.Stub
     * **/
    private void saveExerciseAsService(){
    	if(oConfigTrainer.isbPlayMusic()){
    		if(oMediaPlayer!=null) {
    			if(oMediaPlayer.isPlaying()){   				
        			oMediaPlayer.destroy();
            		oMediaPlayer=null;
    			}else{
	    			if(oConfigTrainer.isbPlayMusic()){
	    				//Tolgo il MediaPlayer ero in pausa ed ho salvato
	    				oMediaPlayer.destroy();
	            		oMediaPlayer=null;
	    			}
    			}
    		}    		
    	}
	   	if(MotivatorTimer!=null) MotivatorTimer.cancel();
    	//SAVE
    	bStopListener=true;
    	mNM.cancel(NOTIFICATION);	    
	    isRunning = false;	    
	    isAutoPause=false;
	    
	    ExerciseUtils.saveExercise(mContext,oConfigTrainer,iStep);	
	    ExerciseUtils.addStep(mContext, iStep);
	    endAllListner();
    }                     
    /**
     * Ritorna il Servizio
     * **/
    public class TrainerServiceBinder extends Binder 
    { 
            public ExerciseService getService() 
            { 
                    return ExerciseService.this; 
            } 
    } 
    
	 /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
    	Log.i(this.getClass().getCanonicalName(), "onBind->Services");   
    	//startGPSFix();
    	return mBinder;
        //return mMessenger.getBinder();
    }
    @Override
    public boolean onUnbind(Intent intent) {
    	
    	return super.onUnbind(intent);
    }
    @Override
    public void onLowMemory() {
    	Log.w(this.getClass().getCanonicalName(), "Running on Low Memory");  
    	super.onLowMemory();
    }
    
    private final IExerciseService.Stub mBinder = new IExerciseService.Stub() {
    	public String getPid(){
    		
            return sPid;
        }       
		@Override
		public boolean startExercise(int TypeExercise, int goalDistance, double goalHH, double goalMM) throws RemoteException {			
			Log.i("Services Message: ", "start exercise via AIDL");
			startExerciseAsService(TypeExercise, goalDistance, goalHH, goalMM);  
			return false;
		}
		@Override
		public boolean isRunning() throws RemoteException {			
			return isRunning;
		}
		@Override
		public boolean stopExercise() throws RemoteException {			
			Log.i("Services Message: ", "stop exercise via AIDL");
			stopExerciseAsService();
			return false;
		}
		@Override
		public boolean pauseExercise() throws RemoteException {
			
			pauseExerciseAsService();
			return false;
		}
		@Override
		public boolean resumeExercise() throws RemoteException {
			
			resumeExerciseAsService();
			return false;
		}
		@Override
		public boolean saveExercise() throws RemoteException {
			
			saveExerciseAsService();
			return false;
		}
		@Override
		public long getExerciseTime() throws RemoteException {
			
			return ExerciseService.lStartTime+ExerciseService.lPauseTime;
		}
		@Override
		public boolean isServiceAlive() throws RemoteException {
			
			return isServiceAlive;
		}
		@Override
		public String getsCurrentDistance() throws RemoteException {
			
			return ExerciseUtils.getTotalDistanceFormattated(ExerciseService.dCurrentDistance,oConfigTrainer,true);
		}
		@Override
		public String getsPace() throws RemoteException {
			String sMinutePerUnit=mContext.getString(R.string.min_km);
			
			if(oConfigTrainer.getiUnits()==1){			
				sMinutePerUnit=mContext.getString(R.string.min_miles);
			}
			return ExerciseService.sPace+" "+sMinutePerUnit;
		}
		@Override
		public String getsKaloriesBurn() throws RemoteException {
			
			ExerciseService.sCurrentCalories=ExerciseUtils.getKaloriesBurn(oConfigTrainer, ExerciseService.dCurrentDistance);
			return String.valueOf(ExerciseService.sCurrentCalories);
		}
		@Override
		public boolean isRefumeFromAutoPause() throws RemoteException {
			return isResumeAutoPause;
		}
		@Override
		public void setRefumeFromAutoPause(boolean bResumeAutoPause)
				throws RemoteException {
			isResumeAutoPause=bResumeAutoPause;
		}
		@Override
		public boolean isAutoPause() throws RemoteException {			
			return isAutoPause;
		}
		@Override
		public String getsAltitude() throws RemoteException {
			return ExerciseService.sCurrentAltidute;
		}
		
		@Override
		public int getiTypeExercise() throws RemoteException {			
			return iTypeExercise;
		}
		@Override
		public String getsVm() throws RemoteException {
			String sMinutePerUnit=mContext.getString(R.string.km_hours);
			
			if(oConfigTrainer.getiUnits()==1){			
				sMinutePerUnit=mContext.getString(R.string.miles_hours);
			}
			return ExerciseService.sVm+" "+sMinutePerUnit;
		}
		@Override
		public double getLatidute() throws RemoteException {
			
			return latitude;
		}
		@Override
		public double getLongitude() throws RemoteException {
			
			return longitude;
		}
		@Override
		public boolean isPause() throws RemoteException {
			
			return isPause;
		}
		@Override
		public String getsInclination() throws RemoteException {			
			return ExerciseService.iInclication+"%";
		}
		@Override
		public void stopGPSFix() throws RemoteException {
			endAllListner();
		}
		@Override
		public void setHeartRate(int iheartRate) throws RemoteException {
			iHeartRate=iheartRate;
		}
		@Override
		public void shutDown() throws RemoteException {
			bStopListener	= true;
	    	isRunning 		= false;  
	    	isServiceAlive 	= true;
	    	isAutoPause     = false;
	    	if(mNM!=null) mNM.cancel(NOTIFICATION);	
			stopSelf();
		}
		@Override
		public void skipTrack() throws RemoteException {
			if(oMediaPlayer!=null) oMediaPlayer.SkipTrack();
		}
		@Override
		public int getHeartRate() throws RemoteException {
			return iHeartRate;
		}
		@Override
		public boolean isCardioConnected() throws RemoteException {
			if(oBTHelper!=null){
				return oBTHelper.isbConnect();
			}else {
				return false;
			}
			
		}
		@Override
		public boolean isGPSFixPosition() throws RemoteException {
			return isFixPosition;
		}
		@Override
		public long getCurrentTime() throws RemoteException {			
			return diffTime;
		}
		@Override
		public void runGPSFix() throws RemoteException {
			startGPSFix();
		}
    };        

    /**
     *  Metodi utilizzati per l'uso dell'accelerometro per il conteggio dei passi*
     *  Se x cambia da 0 ad 1  stato fatto un passo
     * 
     * **/
	@Override
	public void onAccelerationChanged(float x, float y, float z) {
		// TODO Auto-generated method stub
		//Log.v(this.getClass().getCanonicalName(),"Acceleration: x="+x+" y="+y+" z="+z);
		if(iTypeExercise==0){
			if(y>9 ||
					x==5
					|| z==5){
				////Log.v(this.getClass().getCanonicalName(),"Acceleration: x="+x+" y="+y+" z="+z);
				iStep++;
				//Log.v(this.getClass().getCanonicalName(),"Step: "+iStep);
			}
		}else{
			if(y>5 ||
					x==5
					|| z==5){
				////Log.v(this.getClass().getCanonicalName(),"Acceleration: x="+x+" y="+y+" z="+z);
				iStep++;
				//Log.v(this.getClass().getCanonicalName(),"Step: "+iStep);
			}
		}
		
		//ExerciseUtils.addStep(mContext, x);
	}

	@Override
	public void onShake(float force) {
		// TODO Auto-generated method stub
		//Log.v(this.getClass().getCanonicalName(),"onShake: gForce="+force);		
	}
	
	/**
	 * Ferma il GPS e tutti i listner
	 * 
	 * */
	protected void endAllListner() {		

		 if(oConfigTrainer!=null){
	        	if(oConfigTrainer.isbDisplayNotification()){
	            	// Cancel the persistent notification.
	                if(mNM!=null) mNM.cancel(NOTIFICATION);
	            }
	            if(oConfigTrainer.isbPlayMusic()){
	            	if(oMediaPlayer!=null){
	            		oMediaPlayer.stop();
	            		oMediaPlayer.destroy();
	            	}
	            }        
	        }    
		 
		if(oRunningThread!=null) oRunningThread.interrupt();
		if(LocationManager!=null) LocationManager.removeUpdates(this);
		
	    if(monitoringTimer!=null) monitoringTimer.cancel();
	    if(InteractiveTimer!=null) InteractiveTimer.cancel(); 
	    if(AutoPauseTimer!=null) AutoPauseTimer.cancel();
	    if(VirtualRaceTimer!=null) VirtualRaceTimer.cancel();
	    if(oHttpHelper!=null) oHttpHelper.sendHttpPostPending();
	    
	    /**Arresto il listener del Conta Passi*/
	    if (AccelerometerUtils.isListening()) {
    		AccelerometerUtils.stopListening();
    		
        }
	    LocationManager=null;
	    monitoringTimer=null;
	    InteractiveTimer=null; 
	    AutoPauseTimer=null;
	    VirtualRaceTimer=null;
	    bStopListener=true;
    	isRunning = false;
    	
    	isFixPosition=false;
    	latitude = 0.0;
        longitude = 0.0;
    	pre_latitude = 0.0;
        pre_longitude = 0.0;
        oMediaPlayer=null;
        /**Tempo di inizio esercizio*/
    	ExerciseService.lStartTime=0;
    	/**Tempo Corrente di esercizio*/
    	ExerciseService.lCurrentTime=0;
    	/**Current WatchPoint*/
    	ExerciseService.lCurrentWatchPoint=0;
    	/**Current WatchPoint*/
    	ExerciseService.lPauseTime=0;
    	
    	/**Latidine di esercizio*/
    	ExerciseService.dStartLatitude=0;
    	/**Longitudine di esercizio*/
    	ExerciseService.dStartLongitude=0;
    	/**Latidine di double*/
    	ExerciseService.dCurrentLatitude=0;
    	/**Longitudine di esercizio*/	
    	ExerciseService.dCurrentLongitude=0;
    	
    	/**Latidine di esercizio*/
    	ExerciseService.lCurrentAltidute=0;
    	
    	/**Pendenza*/
    	ExerciseService.iInclication=0;
    	
    	/**Latidine di esercizio*/
    	ExerciseService.sCurrentAltidute="";
    	
    	/**Calorie correnti di esercizio*/
    	ExerciseService.sCurrentCalories="";
    		
    	/**Distanza Corrente di esercizio*/
    	ExerciseService.dCurrentDistance=0;
    	/**Velocit� Corrente di esercizio*/
    	ExerciseService.fCurrentSpeed=0;
    	/**Velocit� Totale di esercizio*/
    	ExerciseService.fTotalSpeed=0;
    	/**Stato del GPS*/
    	ExerciseService.bStatusGPS=true;
    	
    	ExerciseService.dPace=0;
    	ExerciseService.sPace="";
    	
    	ExerciseService.sVm="";
    	stopForeground(true);
    	if(LocationManager!=null) LocationManager.removeUpdates(this);
		LocationManager=null;
	}

	@Override
	public void onDestroy() {		
		super.onDestroy();
		try {
			this.finalize();
			Log.i(this.getClass().getCanonicalName(),"Destroy Services, remove GPS Fix");
			isFixPosition=false;
			if(LocationManager!=null) LocationManager.removeUpdates(this);
			LocationManager=null;
			stopForeground(true);
			System.gc();
			System.exit(0);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Ferma tutto l'audio durante una chiamata
	 * */
	private void stopMediaDuringCall(){
		if(!isRunning) return;
		if(oMediaPlayer!=null){
			if(!oMediaPlayer.isPlaying()) return;	
		}
		
		//Log.v(this.getClass().getCanonicalName(), "Stop Media For INCOMING CALL!");
		isInCalling=true;
		if(oConfigTrainer.isbPlayMusic()){
			oMediaPlayer.pause();
		}
		//Finisco la chiamata se abilitata
		if(oConfigTrainer.isbEndCallOnWorkout()){
			//TODO
		}
		
		
	}
	/**
	 * Riavvia l'audio dopo una chiamata
	 * */
	private void resumeMediaDuringCall(){
		if(!isRunning || !isInCalling) return;
		//Log.v(this.getClass().getCanonicalName(), "Resume Media CALL END!");
		isInCalling=false;
		if(oConfigTrainer.isbPlayMusic()){
			oMediaPlayer.play(true);
		}		
	}
	/**
	 * Gestione delle chiamate in Arrivo
	 * 
	 * 
	 * **/
	
	/**
	 * Metodo da avviare col trainer per catturare le chiamate e spegnere i volumi
	 * 
	 * */
	private void listenForIncomingCall(){
		telephonyManager=(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		
	}

	  
    /**
     * Metodi deprecati
     * 
     * **/
    
    /**METODI DI CONDIVISIONE**/
	
	/**
	 * Lancia il TimerTask per l'interactive exercise
	 * @param c 
	 * @param isFaceBookShareActive 
	 * */
	private void addVirtualRaceTimer() {
		VirtualRaceTimer = new Timer();
		VirtualRaceTimer.scheduleAtFixedRate(
    			new TimerTask()
    			{
    				@Override
    				public void run()
    				{    	
    					int iVirtualRace=0;
    					try{
    						iVirtualRace = Integer.valueOf(sPid);
    					}catch (NumberFormatException e) {
							Log.e(ExerciseService.this.getClass().getCanonicalName(),"Error Generating VirtualRaceId");
						} 					
    					//Send data to server 					
    					oHttpHelper.sendDataForVirtualRace(oConfigTrainer, iVirtualRace, 
    							ExerciseService.dCurrentLatitude, ExerciseService.dCurrentLongitude, 
    							ExerciseService.lCurrentAltidute, ExerciseService.dPace, 
    							ExerciseService.dCurrentDistance, new Date().getTime(),Locale.getDefault().getCountry());
    					 					
    				}					
    			}, 
    			SHORT_START_MOTIVATOR_TIMER_DELAY,
    			SHORT_START_MOTIVATOR_TIMER_DELAY);
	}
	/**
	 * Rimuove il timer per l'auto pausa
	 * */
	private void removeAutoPauseTimer(){
		if(AutoPauseTimer!=null) AutoPauseTimer.cancel();
		AutoPauseTimer=null;
	}
	/**
	 * Imposta il timer per l'auto pausa
	 * */
	private void addAutoPauseTimer(){
		if(iAutoPauseDelay<0 ) return;
		if(AutoPauseTimer==null){
			AutoPauseTimer = new Timer();		
			AutoPauseTimer.schedule(
	    			new TimerTask()
	    			{
	    				@Override
	    				public void run()
	    				{    					
	    					isAutoPause=true;
	    					//Log.v(this.getClass().getCanonicalName(), "RUN AUTOPAUSE!!!"); 
	    					autopauseExercise();
	    				}					
	    			}, 
	    			iAutoPauseDelay);
		}else{
			AutoPauseTimer.cancel();
			AutoPauseTimer = new Timer();
			AutoPauseTimer.schedule(
					new TimerTask() {
						@Override
						public void run() {
							isAutoPause = true;
							//Log.v(this.getClass().getCanonicalName(), "RUN AUTOPAUSE!!!");
							autopauseExercise();
						}
					},
					iAutoPauseDelay);
		}
	}

	/**
	 * Avvia la vomunicazione col Cardio
	 * */
	private void startCardio(){
		
		Log.i(this.getClass().getCanonicalName(),"Start Cardio ");
		
		oBTHelper.searchPairedDevice();
		
		ArrayList<CardioDevice> aCardio = oBTHelper.getaCardioName();
		int l=aCardio.size();
		for(int i=0;i<l;i++){
			String sDeviceName=aCardio.get(i).getsDeviceName();	
			if(sDeviceName.startsWith("Polar")){
				Log.i(this.getClass().getCanonicalName(),"Cardio Polar to Connect "+aCardio.get(i).getsDeviceAddress());
				oBTHelper.setDevice(aCardio.get(i).getoDeviceBluetooth());
					
				Timer ConnectTimer = new Timer();		
				ConnectTimer.schedule(
		    			new TimerTask()
		    			{
		    				@Override
		    				public void run()
		    				{    				
		    					Log.v(this.getClass().getCanonicalName(),"Timer Start Connect Received: ");
		    					oBTHelper.connect();
		    				}					
		    			}, 
		    			5000);
				
				final BroadcastReceiver mReceiver = new BroadcastReceiver() {	// BroadcastReceiver for ACTION_FOUND
		            public void onReceive(Context context, Intent intent) {
		                String action = intent.getAction();		                		
		                Log.v(this.getClass().getCanonicalName(),"Action Received: "+action);
		                if( BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
		                	
		                	//oBTHelper.connect();
		                }
		            }
		        };
		        
		        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
		        mContext.registerReceiver( mReceiver, filter);
				//Toast.makeText(this.mContext, aCardio.get(i).getsDeviceName(), Toast.LENGTH_SHORT).show();	
				Log.i(this.getClass().getCanonicalName(),"Connect To "+aCardio.get(i).getsDeviceName()+" Success!");
			}
		}
	}
	/**
	 * chiude la sessione bluetooch
	 * */
	private void stopCardio(){
		if(oBTHelper!=null){
			if(oBTHelper.isbConnect()){
				oBTHelper.disconect();
			}
		}
	}
}

 