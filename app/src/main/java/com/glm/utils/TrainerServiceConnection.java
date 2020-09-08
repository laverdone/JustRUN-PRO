package com.glm.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.glm.app.ConstApp;
import com.glm.bean.ChallengeExercise;
import com.glm.bean.ConfigTrainer;
import com.glm.services.ExerciseService;
import com.glm.services.IExerciseService;


/**
 * Classe Connection che stabilisce il bind col servizio
 * 
 * @author gianluca masci aka (GLM)
 * 
 * **/
public class TrainerServiceConnection implements ServiceConnection 
{
	/**Continene tutte le configurazioni del Trainer**/
	private static ConfigTrainer oConfigTrainer;
	public IExerciseService mIService;
	public boolean mIsBound=false;
	public Context mContext;
	private String mBinderAct="";
	private ChallengeExercise mChallengeExercise=null;
    public TrainerServiceConnection(Context context,String binder){
        mContext=context;
		mBinderAct=binder;
		try{
			oConfigTrainer=ExerciseUtils.loadConfiguration(mContext);
		}catch (NullPointerException e) {
			Log.e(this.getClass().getCanonicalName(),"Error load Config");
			return;
		}

		if(ConstApp.IS_DEBUG) Logger.log("INFO - bindService from "+mBinderAct);
        doBindService();
    }

	public TrainerServiceConnection(Context context,String binder,ChallengeExercise challengeExercise){
		mContext=context;
		mBinderAct=binder;
		mChallengeExercise=challengeExercise;
		try{
			oConfigTrainer=ExerciseUtils.loadConfiguration(mContext);
		}catch (NullPointerException e) {
			Log.e(this.getClass().getCanonicalName(),"Error load Config");
			return;
		}

		if(ConstApp.IS_DEBUG) Logger.log("INFO - bindService from "+mBinderAct);
		doBindService();
	}

	/**
     * Disconnetto dal servizio
     * */
    public void destroy(){
		if(ConstApp.IS_DEBUG) Logger.log("INFO - destroy from "+mBinderAct);
        doUnbindService();
    }
    
	@Override 
	public void onServiceConnected(ComponentName name, IBinder service) 
	{ 
		try{
			mIService= IExerciseService.Stub.asInterface(service);
			if(ConstApp.IS_DEBUG) Logger.log("INFO - TrainerServiceConnection->onServiceConnected service Workout");

		}catch (Exception e) {
			Log.e(this.getClass().getCanonicalName(), "onServiceConnected->Remote Exception"+e.getMessage());
			if(ConstApp.IS_DEBUG) Logger.log("ERROR - TrainerServiceConnection->onServiceConnected->Remote Exception"+e.getMessage());

			e.printStackTrace();
		}	                
	} 
	@Override 
	public void onServiceDisconnected(ComponentName name) 
	{ 

		/* Toast.makeText(StopwatchActivity.this, "TrainerServiceConnection->onServiceDisconnected"+R.string.pause_exercise,
    	                Toast.LENGTH_LONG).show();*/
	} 
	
	void doBindService() {
	    // Establish a connection with the service.  We use an explicit
	    // class name because there is no reason to be able to let other
	    // applications replace our component.
		//Intent bindIntent = new Intent(Main.this, MessengerService.class); 
        //bindService(bindIntent, mConnection, Context.BIND_AUTO_CREATE);                 
		if(mIsBound==false){

			Intent serviceIntent = new Intent(mContext,ExerciseService.class);
			if(mChallengeExercise!=null){
				Bundle mBundle = new Bundle();
				mBundle.putParcelable("challengeexercise",mChallengeExercise);
				serviceIntent.putExtras(mBundle);
			}
			mIsBound = mContext.bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
			if(ConstApp.IS_DEBUG) Logger.log("INFO - TrainerServiceConnection->doBindService service Workout");

			Log.i(this.getClass().getCanonicalName(), "Binding from Services");
		}
		
		
	}
	void doUnbindService() {
	    if (mIsBound) {
	        // If we have received the service, and hence registered with
			if(ConstApp.IS_DEBUG) Logger.log("INFO - TrainerServiceConnection->doUnbindService service Workout");

			Log.i(this.getClass().getCanonicalName(), "UnBinding from Services");

	        // Detach our existing connection.
	        mContext.unbindService(this);
	        mIsBound = false;	       
	    }
	}
}     