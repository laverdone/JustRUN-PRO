package com.glm.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

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
	public IExerciseService mIService;
	public boolean mIsBound=false;
	protected ExerciseService oTrainerService = null; 
	public Context mContext;

    public TrainerServiceConnection(Context context){
        mContext=context;
        doBindService();
    }
    
    /**
     * Disconnetto dal servizio
     * */
    public void destroy(){
        doUnbindService();
    }
    
	@Override 
	public void onServiceConnected(ComponentName name, IBinder service) 
	{ 
		try{
			mIService= IExerciseService.Stub.asInterface(service);

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
	
	void doBindService() {
	    // Establish a connection with the service.  We use an explicit
	    // class name because there is no reason to be able to let other
	    // applications replace our component.
		//Intent bindIntent = new Intent(Main.this, MessengerService.class); 
        //bindService(bindIntent, mConnection, Context.BIND_AUTO_CREATE);                 
		if(mIsBound==false){
			mIsBound = mContext.bindService(new Intent("com.glm.trainer.STARTSERVICE"), this, Context.BIND_AUTO_CREATE);
			Log.i(this.getClass().getCanonicalName(), "Binding from Services");
		}
		
		
	}
	void doUnbindService() {
	    if (mIsBound) {
	        // If we have received the service, and hence registered with
	       
	        Log.i(this.getClass().getCanonicalName(), "UnBinding from Services");

	        // Detach our existing connection.
	        mContext.unbindService(this);
	        mIsBound = false;	       
	    }
	}
}     