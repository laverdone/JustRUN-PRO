package com.glm.utils;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.glm.bean.ConfigTrainer;
import com.glm.services.ExerciseService;
import com.glm.services.IExerciseService;

/*
 * Monitor Stato Batteria
 * 
 * **/
public class BatteryMonitor extends BroadcastReceiver{
	/**Oggetto connessione al servizio*/
	private TrainerServiceConnection mConnection = new TrainerServiceConnection();
	private ConfigTrainer oConfigTrainer;
	private IExerciseService mIService;
	private Context oContext;
	private Intent oIntent;
	@Override
	public void onReceive(Context Context, Intent intent) {
		//Chiamato solo per batteria LOW LEVEL salvo l'esercizio
		oContext=Context;
		oIntent=intent;
		doBindService();
	}

	boolean mIsBound;
	void doBindService() {
	    // Establish a connection with the service.  We use an explicit
	    // class name because there is no reason to be able to let other
	    // applications replace our component.
		//Intent bindIntent = new Intent(Main.this, MessengerService.class); 
        //bindService(bindIntent, mConnection, Context.BIND_AUTO_CREATE);                 
		mIsBound = oContext.bindService(oIntent, mConnection, Context.BIND_AUTO_CREATE);		
	}
	
	/**
	 * Classe Connection che stabilisce il bind col servizio
	 * 
	 * @author gianluca masci aka (GLM)
	 * 
	 * **/
	public class TrainerServiceConnection implements ServiceConnection 
	{ 
		
	        protected ExerciseService oTrainerService = null; 
	        @Override 
	        public void onServiceConnected(ComponentName name, IBinder service) 
	        { 
	                try{
	                	mIService= IExerciseService.Stub.asInterface(service);
	                	
	            		
            			if(mIService.isServiceAlive() && 
            					mIService.isRunning()){
            				mIService.stopExercise();
            				mIService.saveExercise();
            				
            			}else if(mIService.isServiceAlive() && 
            						!mIService.isRunning()){  
            				//Toast.makeText(MainTrainerActivity.this, "Second type: "+mIService.getiTypeExercise(),
        	    	        //        Toast.LENGTH_LONG).show();
            				if(mIService.isAutoPause() || mIService.isPause()){
            					mIService.stopExercise();
            					mIService.saveExercise();
            				}
            			}
	            		
	                }catch (Exception e) {
	                	Log.e(this.getClass().getCanonicalName(), "onServiceConnected->Remote Exception"+e.getMessage());
	                	e.printStackTrace();
					}
	                
	        } 
	        @Override 
	        public void onServiceDisconnected(ComponentName name) 
	        { 
	        	
	               /* Toast.makeText(StopwatchActivity.this, "TrainerServiceConnection->onServiceDisconnected"+R.string.pause_exercise,
	    	                Toast.LENGTH_LONG).show();*/
	        } 
	} 
	
}
