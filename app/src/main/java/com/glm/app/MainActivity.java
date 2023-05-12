package com.glm.app;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;

import com.daimajia.androidanimations.library.Techniques;
import com.glm.app.db.Database;
import com.glm.app.stopwatch.WorkOutActivity;
import com.glm.bean.ConfigTrainer;
import com.glm.trainer.R;
import com.glm.utils.ExerciseUtils;
import com.glm.utils.Logger;
import com.glm.utils.TrainerServiceConnection;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;


public class MainActivity  extends AwesomeSplash {
	/**Oggetto connessione al servizio*/
	private TrainerServiceConnection mConnection =null;
	private ConfigTrainer oConfigTrainer;

    /** Preferences to store a logged in users credentials */
    private SharedPreferences mPrefs;

 	private String sVersionPackage="";

	@Override
	public void initSplash(ConfigSplash configSplash) {
		/* you don't have to override every property */

		MobileAds.initialize(this, new OnInitializationCompleteListener() {
			@Override
			public void onInitializationComplete(InitializationStatus initializationStatus) {
			}
		});

		//Customize Circular Reveal
		configSplash.setBackgroundColor(R.color.main_green); //any color you want form colors.xml
		configSplash.setAnimCircularRevealDuration(1000); //int ms
		configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
		configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

		//Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

		//Customize Logo
		configSplash.setLogoSplash(R.drawable.about); //or any other drawable
		configSplash.setAnimLogoSplashDuration(500); //int ms
		configSplash.setAnimLogoSplashTechnique(Techniques.BounceInDown); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


		//Customize Path
		//configSplash.setPathSplash(Constants.DROID_LOGO); //set path String
		configSplash.setOriginalHeight(400); //in relation to your svg (path) resource
		configSplash.setOriginalWidth(400); //in relation to your svg (path) resource
		configSplash.setAnimPathStrokeDrawingDuration(1500);
		configSplash.setPathSplashStrokeSize(3); //I advise value be <5
		configSplash.setPathSplashStrokeColor(R.color.strokeColor); //any color you want form colors.xml
		configSplash.setAnimPathFillingDuration(1500);
		configSplash.setPathSplashFillColor(R.color.fillColor); //path object filling color


		//Customize Title
		configSplash.setTitleSplash(getString(R.string.app_name_pro));
		configSplash.setTitleTextColor(R.color.common_google_signin_btn_text_dark);
		configSplash.setTitleTextSize(30f); //float value
		configSplash.setAnimTitleDuration(1500);
		configSplash.setAnimTitleTechnique(Techniques.FlipInX);
		configSplash.setTitleFont("fonts/DS-DIGIB.TTF"); //provide string to your font located in assets/fonts/




	}

	@Override
	public void animationsFinished() {
		//setContentView(R.layout.main);

		/*Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
		toolbar.setLogo(R.drawable.icon_pro);
		toolbar.setTitle(R.string.app_name_pro);*/

		if (ConstApp.IS_DEBUG) {
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
		}else{
			StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
			StrictMode.setVmPolicy(builder.build());
			builder.detectFileUriExposure();
		}

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
					if(mConnection==null && mConnection.mIService==null) return;
					if(mConnection.mIService.isServiceAlive() && 
							mConnection.mIService.isRunning()){
						//Toast.makeText(MainTrainerActivity.this, "First type: "+mIService.getiTypeExercise(),
					    //        Toast.LENGTH_LONG).show();
						Intent intent=new Intent();
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent = intent.setClass(getApplicationContext(), WorkOutActivity.class);
						if(mConnection.mIService.getiTypeExercise()==ConstApp.TYPE_RUN){
							//RUN
							//intent = ActivityHelper.createActivityIntent(MainActivity.this,WorkOutActivity.class);	
							intent.putExtra("type", ConstApp.TYPE_RUN);
						}else if (mConnection.mIService.getiTypeExercise()==ConstApp.TYPE_WALK){
							//WALK
							//intent = ActivityHelper.createActivityIntent(MainActivity.this,WorkOutActivity.class);
							intent.putExtra("type", ConstApp.TYPE_WALK);
						}else if (mConnection.mIService.getiTypeExercise()==ConstApp.TYPE_BIKE){
							//BIKE 
							//intent = ActivityHelper.createActivityIntent(MainActivity.this,WorkOutActivity.class);
							intent.putExtra("type", ConstApp.TYPE_BIKE);
						}     				
						//startActivity(intent);
						//ActivityHelper.startNewActivityAndFinish(MainActivity.this, intent);
						if(ConstApp.IS_DEBUG) Logger.log("INFO - NewActivity->DBTask->WorkOutActivity->running  Trainer Services ");
						intent.putExtra("Status", "running");
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
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
							if(ConstApp.IS_DEBUG) Logger.log("INFO - NewActivity->DBTask->WorkOutActivity->service_under_autopause  Trainer Services ");
							intent.putExtra("Status", "service_under_autopause");
							mConnection.destroy();
							mConnection=null;
							startActivity(intent);
				    		finish();
						}else if (mConnection.mIService.isPause()){
							Intent intent=new Intent();
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
							if(ConstApp.IS_DEBUG) Logger.log("INFO - NewActivity->DBTask->WorkOutActivity->service_under_user_pause  Trainer Services ");
							intent.putExtra("Status", "service_under_user_pause");
							//ActivityHelper.startNewActivityAndFinish(MainActivity.this, intent);
							mConnection.destroy();
							mConnection=null;
							startActivity(intent);
				    		finish();
						}else{				
							if(!ExerciseUtils.isUserExist(getApplicationContext())){
								//Richiamo la pagina di info_fine_location

								Intent intent=new Intent();
								intent.putExtra("first_launch", true);
								intent.putExtra("pref_type",4);
								intent.setClass(getApplicationContext(), InfoFineLocationActivity.class);
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
								if(ConstApp.IS_DEBUG) Logger.log("INFO - NewActivity->DBTask->NewMainActivity  Trainer Services ");
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
			ExerciseUtils.checkIncompleteWorkout(getApplicationContext(),oConfigTrainer);
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

