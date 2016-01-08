package com.glm.trainer;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import com.glm.app.db.Database;
import com.glm.app.stopwatch.WorkOutActivity;
import com.glm.bean.ConfigTrainer;
import com.glm.services.ExerciseService;
import com.glm.utils.ExerciseUtils;
import com.glm.utils.TrainerServiceConnection;
import com.google.gson.Gson;


public class MainActivity  extends Activity{
	/**Oggetto connessione al servizio*/
	private TrainerServiceConnection mConnection =null;
	private ConfigTrainer oConfigTrainer;

    /** Preferences to store a logged in users credentials */
    private SharedPreferences mPrefs;

 	private String sVersionPackage="";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);


		if (false) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads()
					.detectDiskWrites()
					.detectNetwork()   // or .detectAll() for all detectable problems
					.penaltyLog()
					.build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects()
					.detectLeakedClosableObjects() //or .detectAll() for all detectable problems
					.penaltyLog()
					.penaltyDeath()
					.build());
		}
		Intent serviceIntent = new Intent(this,ExerciseService.class);
		startService(serviceIntent);
		//startService(new Intent("com.glm.trainer.STARTSERVICE"));

		DBTask task = new DBTask();
		task.execute(new Database(this));

	}
	
	public void onResume(Bundle savedInstanceState) {
		setContentView(R.layout.main);		
	}
	@Override
	protected void onPause() {
		//doUnbindService();
		super.onPause();
	}    

	private void checkServiceStatus(){
		AsyncTask.execute(new Runnable() {
			
			@Override
			public void run() {
				try {
					if(mConnection==null) return;
					if(mConnection.mIService.isServiceAlive() && 
							mConnection.mIService.isRunning()){
						//Toast.makeText(MainTrainerActivity.this, "First type: "+mIService.getiTypeExercise(),
					    //        Toast.LENGTH_LONG).show();
						Intent intent=new Intent();
						intent = intent.setClass(getApplicationContext(), WorkOutActivity.class);
						if(mConnection.mIService.getiTypeExercise()==0){
							//RUN
							//intent = ActivityHelper.createActivityIntent(MainActivity.this,WorkOutActivity.class);	
							intent.putExtra("type", 0);
						}else if (mConnection.mIService.getiTypeExercise()==100){
							//WALK
							//intent = ActivityHelper.createActivityIntent(MainActivity.this,WorkOutActivity.class);
							intent.putExtra("type", 100);
						}else if (mConnection.mIService.getiTypeExercise()==1){
							//BIKE 
							//intent = ActivityHelper.createActivityIntent(MainActivity.this,WorkOutActivity.class);
							intent.putExtra("type", 1);
						}     				
						//startActivity(intent);
						//ActivityHelper.startNewActivityAndFinish(MainActivity.this, intent);
						mConnection.destroy();
						mConnection=null;
						startActivity(intent);
			    		finish();
					}else if(mConnection.mIService.isServiceAlive() && 
								!mConnection.mIService.isRunning()){  
						//Toast.makeText(MainTrainerActivity.this, "Second type: "+mIService.getiTypeExercise(),
					    //        Toast.LENGTH_LONG).show();
						if(mConnection.mIService.isAutoPause()){
							Intent intent=new Intent();
							intent = intent.setClass(getApplicationContext(), WorkOutActivity.class);
							if(mConnection.mIService.getiTypeExercise()==0){
								//RUN            		
								
								//ActivityHelper.createActivityIntent(MainActivity.this,WorkOutActivity.class);	
								intent.putExtra("type", 0);
							}else if (mConnection.mIService.getiTypeExercise()==100){
								//WALK
								//intent = ActivityHelper.createActivityIntent(MainActivity.this,WorkOutActivity.class);
								intent.putExtra("type", 100);
							}else if (mConnection.mIService.getiTypeExercise()==1){
								//BIKE 
								//intent = ActivityHelper.createActivityIntent(MainActivity.this,WorkOutActivity.class);
								intent.putExtra("type", 1);
							}
							//ActivityHelper.startNewActivityAndFinish(MainActivity.this, intent);
							mConnection.destroy();
							mConnection=null;
							startActivity(intent);
				    		finish();
						}else if (mConnection.mIService.isPause()){
							Intent intent=new Intent();
							intent = intent.setClass(getApplicationContext(), WorkOutActivity.class);
							if(mConnection.mIService.getiTypeExercise()==0){
								//RUN
								//intent = ActivityHelper.createActivityIntent(MainActivity.this,WorkOutActivity.class);	
								intent.putExtra("type", 0);
							}else if (mConnection.mIService.getiTypeExercise()==100){
								//WALK
								//intent = ActivityHelper.createActivityIntent(MainActivity.this,WorkOutActivity.class);
								intent.putExtra("type", 100);
							}else if (mConnection.mIService.getiTypeExercise()==1){
								//BIKE 
								//intent = ActivityHelper.createActivityIntent(MainActivity.this,WorkOutActivity.class);
								intent.putExtra("type", 1);
							}
							intent.putExtra("Status", "service_under_user_pause");
							//ActivityHelper.startNewActivityAndFinish(MainActivity.this, intent);
							mConnection.destroy();
							mConnection=null;
							startActivity(intent);
				    		finish();
						}else{				
							if(!ExerciseUtils.isUserExist(getApplicationContext())){	
								Intent intent=new Intent();
								intent.putExtra("first_launch", true);
								intent.setClass(getApplicationContext(), PreferencesActivity.class);
								mConnection.destroy();
								mConnection=null;
								startActivity(intent);
					    		finish();
					    	}else{
					    		Intent intent=new Intent(); //.createActivityIntent(MainActivity.this,MainTrainerActivity.class);      
					    		intent.putExtra("ConfigTrainer", new Gson().toJson(oConfigTrainer));
					    		
					    		intent.setClass(getApplicationContext(), NewMainActivity.class);
					    		mConnection.destroy();
								mConnection=null;
								startActivity(intent);
					    		finish();
					    		Log.i(this.getClass().getCanonicalName(),"start MainTrainerActivity");
					    	}
						}
					}
				} catch (RemoteException e) {
					Log.e(this.getClass().getCanonicalName(),"Error Bind Service");
					e.printStackTrace();
				}
			}
		});
		
	}
	
	private class DBTask extends AsyncTask<Database, Void, Boolean> {
		@Override
		protected void onPostExecute(Boolean result) {					       
		              		        							       
				try{					   				    		       			     	
			    	try {
			    		
			    	    PackageInfo manager=getPackageManager().getPackageInfo(getPackageName(), 0);
			    	    sVersionPackage = manager.versionName;
			    	    Log.i(this.getClass().getCanonicalName(),"Pacchetto Versione"+sVersionPackage);
			    	} catch (NameNotFoundException e) {
			    	   Log.e(this.getClass().getCanonicalName(),"Pacchetto non trovato");
			    	}
			    	
				    /*if(ExerciseUtils.isFirstBoot(getApplicationContext(),oConfigTrainer, sVersionPackage)){
				    	Intent intent = ActivityHelper.createActivityIntent(MainActivity.this,ChangeLogActivity.class);
						//startActivity(intent);
				    	ActivityHelper.startNewActivityAndFinish(MainActivity.this, intent);	
				    }*/
				    checkServiceStatus();			    	
				}catch (Exception e) {
					Log.e(this.getClass().getCanonicalName(), "check user: "+e.getMessage());			
					e.printStackTrace();
				}		
		}
			
		@Override
		protected Boolean doInBackground(Database... mDB) {
			int iRetry=0;
			mConnection = new TrainerServiceConnection(getApplicationContext(),this.getClass().getCanonicalName());
			Database oDB=null;
			for (Database DB : mDB) {
				oDB= DB;
			}   
			if(oDB!=null)  {
				oDB.init();
			}
			oConfigTrainer=ExerciseUtils.loadConfiguration(getApplicationContext());

			while(mConnection.mIService==null){
				try {
					Thread.sleep(1000);
					iRetry++;
					if(iRetry>5) {
						mConnection.destroy();
						break;
					}
					Log.v(this.getClass().getCanonicalName(),"wait for service...");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e(this.getClass().getCanonicalName(),"Error during startUP");
				}
				
			}
			return true;
		}
	}
}

