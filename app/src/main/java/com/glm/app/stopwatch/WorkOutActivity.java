package com.glm.app.stopwatch;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.glm.app.ConstApp;
import com.glm.app.OpenStreetMapActivity;
import com.glm.bean.ChallengeExercise;
import com.glm.bean.ConfigTrainer;
import com.glm.bean.User;
import com.glm.services.ExerciseService;
import com.glm.app.NewMainActivity;
import com.glm.trainer.R;
import com.glm.utils.ExerciseUtils;
import com.glm.utils.GPXUtils;
import com.glm.utils.Logger;
import com.glm.utils.StopwatchUtils;
import com.glm.utils.TrainerServiceConnection;
import com.glm.utils.sensor.BlueToothHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class WorkOutActivity<onActivityResult> extends Activity implements OnClickListener{
	/**indica se è stato premuto il pulsante avvia*/
	private boolean bInStarting=false; 
	private float fWeight=0;	
	protected static final int GUIUPDATEIDENTIFIER = 0x101;
	protected static final int PAUSEEXERCISE = 0x1337;	
	protected static final int AUTOPAUSEEXERCISE= 0x1338;
	protected static final int RESUMEAUTOPAUSEEXERCISE= 0x1339;
	protected static final int RESUMEEXERCISE = 0x1991; 
	protected static final int STARTEXERCISE = 0x1995; 
	protected static final int SAVEEXERCISE = 0x1996;
	protected static final int NOSAVEEXERCISE = 0x1998;
	protected static final int STOPEXERCISE = 0x1997;
	protected static final int GUIUPDATECARDIO = 0x2000;
	private Thread mThreadConnection=null;
	private ConnectToServiceTask mConnectionTask=null;

	protected static final int GPSNOTENABLED  = 0x1999;
	/**Wait for GPS Fix*/
    private AlertDialog oWaitForGPSFix;
	private Button btnStart;
	private Button btnPause;
	private Button btnMaps;
	private Button btnAddGPX;
	private ImageButton btnSkipTrack;
	//private ImageView imgMode;
	private Animation a;

	private TextView lblDistance;
	private TextView lblKalories;
	private TextView lblInclination;
	private TextView lblPace;
	private TextView lblALT;
	private TextView lblHeatRate;
	private TextView txtDistance;
	private TextView txtKalories;
	private TextView txtTimeHHMM;
	private TextView txtTimeSSdd;
	private TextView txtInclination;
	private TextView txtPace;
	private TextView txtALT;
	
	private Typeface oFont;
	
	private User mUser=null;
	/**
	 * variabili per il target
	 * */
	private int iGoalDistance=0;
	private double dGoalHH=0;
	private double dGoalMM=0;
	
	/**ID Dell'esercizio corrente*/
	private int iCurrentExercise=0;
	
	/***
	 * 0=first start
	 * 1=pause/save
	 * 2=resume	 
	 * */
	private int iStartTrainer=0;

	public boolean bAutoPause=false;
	public boolean bPause=false;
	public boolean bGPSEnabled=true;
	long diffTime;
	/***
	 * 0=running
	 * 1=biking
	 * 100=walking
	 * 2=skiing
	 * 
	 * */
	private int I_TYPE_OF_TRAINER=0;
	private Bundle extras=null;

	/**Oggetto principale per disegnare/controllare lo stopwatch**/	
	private StopwatchUtils StopwatchUtils = new StopwatchUtils();
	
	
	/**Thread principale che avvia il nuovo esercizio**/
	private Thread ThreadTrainer;
    
	private LinearLayout oMainLayout;
	
	/**Messaggi del/per server*/
	//private Messenger mMessenger =null;

	volatile boolean isRun=true;

	/**Oggetto connessione al servizio*/
	private TrainerServiceConnection mConnection =null;
	boolean mIsBound=false;
	/**Utiliti per la creazione di un nuovo exercise*/
	private ExerciseUtils oExerciseUtil = new ExerciseUtils();
	
	private ConfigTrainer oConfigTrainer;
	
	/**Gestione Cardio*/
	private BlueToothHelper oBTHelper;
	private boolean bShowAlert=true;
	private String sStatus="";
	private Context mContext=null;

	/**contiene il GPX*/
	private ChallengeExercise mChallengeExercise=null;
	/**
	 * 
	 * Handler che si occupa del redraw dello stopwatch
	 * richiamato ogni secondo dal Thread 
	 * **/
	Handler StopwatchViewUpdateHandler = new Handler(){
		
		/** Gets called on every message that is received */
		// @Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case WorkOutActivity.STARTEXERCISE:									               
                    try {
                    	if(mConnection!=null && mConnection.mIService!=null){
                    		mConnection.mIService.startExercise(I_TYPE_OF_TRAINER,iGoalDistance,dGoalHH,dGoalMM);
                    		if(mConnection.mIService.isCardioConnected()){
                    			Toast.makeText(getApplicationContext(), "Connect to Cardio OK", Toast.LENGTH_SHORT).show();	
                    		}
							if(ConstApp.IS_DEBUG) Logger.log("INFO - Start Workout");
						}
                    	
                    } catch (RemoteException e) {
                    	Log.e(this.getClass().getCanonicalName(), "RemoteException STARTEXERCISE");
                    	WorkOutActivity.this.ThreadTrainer.interrupt();
						if(ConstApp.IS_DEBUG) Logger.log("ERORR - Start Workout Fail RemoteException "+e.getMessage());
                    } catch (NullPointerException e) {
                    	Log.e(this.getClass().getCanonicalName(), "NullPointerException STARTEXERCISE");
						if(ConstApp.IS_DEBUG) Logger.log("ERROR - Start Workout Fail NullPointerException "+e.getMessage());
					} catch (IllegalArgumentException e) {
						Log.e(this.getClass().getCanonicalName(), "IllegalArgumentException STARTEXERCISE");
						if(ConstApp.IS_DEBUG) Logger.log("ERROR - Start Workout Fail IllegalArgumentException "+e.getMessage());
					}
																
					break;
				case WorkOutActivity.PAUSEEXERCISE:					
					//TODO STOP ALL WRITES
					//getBaseContext().stopService(new Intent(getApplication(),ExerciseService.class));					                   
                    try {
                    	if(mConnection!=null && mConnection.mIService!=null) mConnection.mIService.pauseExercise();
                    	
                    } catch (RemoteException e) {
                    	Log.e(this.getClass().getCanonicalName(), "RemoteException PAUSEEXERCISE");
                    } catch (NullPointerException e) {
                    	Log.e(this.getClass().getCanonicalName(), "NullPointerException PAUSEEXERCISE");
                    	android.os.Process.killProcess(android.os.Process.myPid());
					}
					break;
				case WorkOutActivity.AUTOPAUSEEXERCISE:		
					if(!bAutoPause){
						Toast.makeText(getBaseContext(), "AUTOPAUSE", Toast.LENGTH_SHORT)
						.show();
						
						bAutoPause=true;
						btnStart.setEnabled(false);
						btnPause.setEnabled(true);
						btnPause.setText(getApplication().getString(R.string.btnresume));
					}
		                     
					break;
				case WorkOutActivity.RESUMEAUTOPAUSEEXERCISE:						
					bAutoPause=false;
					
				    iStartTrainer=1;
				    break;
				case WorkOutActivity.RESUMEEXERCISE:
					//TODO Tempo effettivo di Corsa						                   
                    try {
                    	if(mConnection!=null && mConnection.mIService!=null) mConnection.mIService.resumeExercise();
                    	
                    } catch (RemoteException e) {
                    	Log.e(this.getClass().getCanonicalName(), "RemoteException RESUMEEXERCISE");
                    } catch (IllegalArgumentException e) {
                    	Log.e(this.getClass().getCanonicalName(), "IllegalArgumentException RESUMEEXERCISE");
					} catch (NullPointerException e) {
                    	Log.e(this.getClass().getCanonicalName(), "NullPointerException RESUMEEXERCISE");
                    	android.os.Process.killProcess(android.os.Process.myPid());
					}
					break;										
				case WorkOutActivity.GUIUPDATEIDENTIFIER:																	
					try {
						if(mConnection!=null && mConnection.mIService!=null){
							if(!mConnection.mIService.isRunning() 
									&& !mConnection.mIService.isRefumeFromAutoPause()){								
								//Chiamato quando entro in autopausa
								iStartTrainer=2;							
								bPause=true;
										
								Message mPause = new Message();
								mPause.what = WorkOutActivity.AUTOPAUSEEXERCISE;
								WorkOutActivity.this.StopwatchViewUpdateHandler.sendMessage(mPause);
								mPause=null;
							}else if(mConnection.mIService.isRunning() 
										&& mConnection.mIService.isRefumeFromAutoPause()){
								Message mPause = new Message();
								mPause.what = WorkOutActivity.RESUMEAUTOPAUSEEXERCISE;
								WorkOutActivity.this.StopwatchViewUpdateHandler.sendMessage(mPause);	
								mConnection.mIService.setRefumeFromAutoPause(false);
							}
							if(mConnection.mIService.isRunning()){								
															
								if(mConnection.mIService.isCardioConnected()){
									if (bShowAlert) {
										Toast.makeText(getBaseContext(), "Connected to Cardio", Toast.LENGTH_SHORT).show();
										bShowAlert=false;
									}
									if(oConfigTrainer.isbUseCardio() 
											&& 
											oConfigTrainer.isbCardioPolarBuyed()){
										
										lblHeatRate.setText(String.valueOf(mConnection.mIService.getHeartRate()));	
										//Log.v(this.getClass().getCanonicalName(), "Heart Rate:"+mConnection.mIService.getHeartRate());
									}
								}
								
								StopwatchUtils.updateTimeHHMM(txtTimeHHMM, mConnection.mIService.getCurrentTime());
								StopwatchUtils.updateTimeSSdd(txtTimeSSdd, mConnection.mIService.getCurrentTime());
								StopwatchUtils.updateCurrentDistance(txtDistance, mConnection.mIService.getsCurrentDistance());
								StopwatchUtils.updateCurrentPaceVm(txtPace, mConnection.mIService.getsPace());
								StopwatchUtils.updateCurrentKalories(txtKalories, mConnection.mIService.getsKaloriesBurn());
								StopwatchUtils.updateCurrentALT(txtALT, mConnection.mIService.getsAltitude());
								StopwatchUtils.updateCurrentInclination(txtInclination, mConnection.mIService.getsInclination());
								
								
								
								btnStart.setEnabled(true);
								btnPause.setEnabled(true);
								btnStart.setText(getApplication().getString(R.string.btnstop));
							}							
						}						
					} catch (RemoteException e1) {						
						Log.e(this.getClass().getCanonicalName(), "RemoteException GUIUPDATEIDENTIFIER");
						WorkOutActivity.this.ThreadTrainer.interrupt();
						System.exit(1);
					} catch (NullPointerException e) {
                    	Log.e(this.getClass().getCanonicalName(), "NullPointerException GUIUPDATEIDENTIFIER");
                    	e.printStackTrace();
					}
					
					
					break;	
				case WorkOutActivity.STOPEXERCISE:	
					try {
	                	if(mConnection!=null && mConnection.mIService!=null) mConnection.mIService.stopExercise();
	                	
	                	StopwatchUtils.updateTimeHHMM(txtTimeHHMM,0);
						StopwatchUtils.updateTimeSSdd(txtTimeSSdd,0);
						StopwatchUtils.updateCurrentDistance(txtDistance,"0");
						StopwatchUtils.updateCurrentPaceVm(txtPace,"0");
						StopwatchUtils.updateCurrentKalories(txtKalories,"0");
						StopwatchUtils.updateCurrentALT(txtALT, "0");
						
	                } catch (RemoteException e) {
	                	Log.e(this.getClass().getCanonicalName(), "RemoteException STOPEXERCISE");
	                	e.printStackTrace();
	                } catch (NullPointerException e) {
                    	Log.e(this.getClass().getCanonicalName(), "NullPointerException STOPEXERCISE");
                    	android.os.Process.killProcess(android.os.Process.myPid());
					}
					break;
				case WorkOutActivity.SAVEEXERCISE:					
	                
	                try {
	                	if(mConnection.mIService!=null) mConnection.mIService.saveExercise();             	
	                	StopwatchUtils.updateTimeHHMM(txtTimeHHMM,0);
						StopwatchUtils.updateTimeSSdd(txtTimeSSdd,0);
						StopwatchUtils.updateCurrentDistance(txtDistance,"0");
						StopwatchUtils.updateCurrentPaceVm(txtPace,"0");
						StopwatchUtils.updateCurrentKalories(txtKalories,"0");
						
						
						Intent intent = new Intent();
						intent.setClass(getApplicationContext(), NewMainActivity.class);
						startActivity(intent);
						finish(); 
	                } catch (RemoteException e) {
	                	Log.e(this.getClass().getCanonicalName(), "RemoteException SAVEEXERCISE");
	                } catch (NullPointerException e) {
                    	Log.e(this.getClass().getCanonicalName(), "NullPointerException SAVEEXERCISE");
                    	android.os.Process.killProcess(android.os.Process.myPid());
					}
					break;
				case WorkOutActivity.NOSAVEEXERCISE:

					try {
						if(mConnection.mIService!=null){
							mConnection.mIService.stopExercise();
							mConnection.mIService.stopGPSFix();

						}
						StopwatchUtils.updateTimeHHMM(txtTimeHHMM,0);
						StopwatchUtils.updateTimeSSdd(txtTimeSSdd,0);
						StopwatchUtils.updateCurrentDistance(txtDistance,"0");
						StopwatchUtils.updateCurrentPaceVm(txtPace,"0");
						StopwatchUtils.updateCurrentKalories(txtKalories,"0");


						Intent intent = new Intent();
						intent.setClass(getApplicationContext(), NewMainActivity.class);
						startActivity(intent);
						finish();
					} catch (RemoteException e) {
						Log.e(this.getClass().getCanonicalName(), "RemoteException SAVEEXERCISE");
					} catch (NullPointerException e) {
						Log.e(this.getClass().getCanonicalName(), "NullPointerException SAVEEXERCISE");
						android.os.Process.killProcess(android.os.Process.myPid());
					}
					break;
				case WorkOutActivity.GPSNOTENABLED:					
					//GPS Disable during exercise
					break;						
			}
			super.handleMessage(msg);
		}		
	};


	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                
    	mContext=this;

		a = AnimationUtils.loadAnimation(this, R.anim.fadein);
		a.reset();

		try{
			oConfigTrainer=ExerciseUtils.loadConfiguration(mContext);
			if(ConstApp.IS_DEBUG) Logger.log("INFO - onCreate WokoutActivity");
		}catch (NullPointerException e) {
			Log.e(this.getClass().getCanonicalName(),"Error load Config");
			if(ConstApp.IS_DEBUG) Logger.log("ERROR - onCreate WokoutActivity Error load Config");
			return;
		}

		Bundle extras = getIntent().getExtras();
		if(extras !=null){
			I_TYPE_OF_TRAINER = extras.getInt("type");
			sStatus = extras.getString("Status");
			mChallengeExercise=extras.getParcelable("challengeexercise");
		}          
        
        //a = AnimationUtils.loadAnimation(this, R.animator.slide_right);
        //a.reset();
        
        initFromResume();

    }

	private void initFromResume() {
		setContentView(R.layout.stopwatch_full);
		oMainLayout = (LinearLayout) findViewById(R.id.MainLayout);

		MobileAds.initialize(this, new OnInitializationCompleteListener() {
			@Override
			public void onInitializationComplete(InitializationStatus initializationStatus) {
			}
		});
		//setContentView(this.StopwatchView);
		//Avvio Automatico del timer
		//lStartTime=System.currentTimeMillis ();
		//this.ThreadTrainer = new Thread(new TrainerRunner());
		//this.ThreadTrainer.start();

//        oTxtStopwatch = (TextView) findViewById(R.id.text_stopwatch);
//        oTxtStopwatchSS = (TextView) findViewById(R.id.text_stopwatchSS);
		btnStart 		= (Button) findViewById(R.id.btnStart);
		btnPause 		= (Button) findViewById(R.id.btnPause);
		btnMaps			= (Button) findViewById(R.id.btnMaps);
		btnAddGPX		= (Button) findViewById(R.id.btnAddGPX);
		btnSkipTrack 	= (ImageButton) findViewById(R.id.btnSkipTrack);

		//imgMode = (ImageView) findViewById(R.id.imgMode);
		oFont = Typeface.createFromAsset(this.getAssets(), "fonts/TRANA___.TTF");
		txtDistance		= (TextView) findViewById(R.id.txtDistance);
		txtKalories		= (TextView) findViewById(R.id.txtKalories);
		txtTimeHHMM		= (TextView) findViewById(R.id.txtTimeHHMM);
		txtTimeSSdd		= (TextView) findViewById(R.id.txtTimeSSdd);
		txtInclination	= (TextView) findViewById(R.id.txtInclination);
		txtPace			= (TextView) findViewById(R.id.txtPace);
		txtALT			= (TextView) findViewById(R.id.txtALT);
		lblDistance		= (TextView) findViewById(R.id.lblDistance);
		lblKalories		= (TextView) findViewById(R.id.lblKalories);
		lblInclination	= (TextView) findViewById(R.id.lblInclination);
		lblPace			= (TextView) findViewById(R.id.lblPace);
		lblALT			= (TextView) findViewById(R.id.lblALT);
		lblHeatRate     = (TextView) findViewById(R.id.lblHeatRate);

		txtDistance.setTypeface(oFont);
		txtKalories.setTypeface(oFont);
		txtTimeHHMM.setTypeface(oFont);
		txtTimeSSdd.setTypeface(oFont);
		txtInclination.setTypeface(oFont);
		txtPace.setTypeface(oFont);
		txtALT.setTypeface(oFont);
		lblDistance.setTypeface(oFont);
		lblKalories.setTypeface(oFont);
		lblInclination.setTypeface(oFont);
		lblPace.setTypeface(oFont);
		lblALT.setTypeface(oFont);
		lblHeatRate.setTypeface(oFont);
//
//        Typeface font1 = Typeface.createFromAsset(getAssets(), "fonts/7LED.ttf");
//        oTxtStopwatch.setTypeface(font1);
//        oTxtStopwatchSS.setTypeface(font1);


		//btnStart.setBackgroundResource(R.drawable.btn_start);

//
		btnStart.setOnClickListener(this);
		btnPause.setOnClickListener(this);
		btnMaps.setOnClickListener(this);
		btnAddGPX.setOnClickListener(this);
		btnSkipTrack.setOnClickListener(this);
		btnPause.setEnabled(false);
		//imgMode.setOnClickListener(this);
		//btnStart.setOnTouchListener(this);
		//btnPause.setOnTouchListener(this);
		//imgMode.setOnTouchListener(this);

		//oMainLayout.clearAnimation();
		//oMainLayout.setAnimation(a);
		if(I_TYPE_OF_TRAINER==1){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}else{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		if(ConstApp.IS_DEBUG) Logger.log("INFO - WorkOut->onCreate service Workout");

		oMainLayout.clearAnimation();
		oMainLayout.setAnimation(a);

		checkGoal(extras);

		if(getPackageName().equals(ConstApp.ADS_APP_PACKAGE_NAME)){
			AdView mAdView = (AdView) findViewById(R.id.adView);
			AdRequest adRequest = new AdRequest.Builder().build();
			mAdView.loadAd(adRequest);
		}else{
			AdView mAdView = (AdView) findViewById(R.id.adView);
			mAdView.setVisibility(View.GONE);
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (ActivityCompat.checkSelfPermission(mContext,
					Manifest.permission.ACCESS_FINE_LOCATION)
					!= PackageManager.PERMISSION_GRANTED) {

				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE},
						ConstApp.PERMISSION_WRITE_EXTERNAL_STORAGE_CODE);


			}


			if (ActivityCompat.checkSelfPermission(mContext,
					Manifest.permission.WRITE_EXTERNAL_STORAGE)
					!= PackageManager.PERMISSION_GRANTED) {

				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE},
						ConstApp.PERMISSION_WRITE_EXTERNAL_STORAGE_CODE);

			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
			Log.v(this.getClass().getCanonicalName(),"Permission: "+permissions[0]+ "was "+grantResults[0]);
			//resume tasks needing this permission
		}
	}

	@Override
	protected void onResume() {

		Bundle extras = getIntent().getExtras();
		if(extras !=null){
			I_TYPE_OF_TRAINER = extras.getInt("type");
			sStatus = extras.getString("Status");
			mChallengeExercise=extras.getParcelable("challengeexercise");
		}

		if(mChallengeExercise==null){
			//Avvio normale
			mConnection= new TrainerServiceConnection(getApplicationContext(),this.getClass().getCanonicalName());
		}else{
			//Avvio con mChallengeExercise
			mConnection= new TrainerServiceConnection(getApplicationContext(),this.getClass().getCanonicalName(),mChallengeExercise);
		}

		if(ConstApp.IS_DEBUG) Logger.log("INFO - Workout->onResume->mConnection: "+mConnection+" Trainer Services ");
		//ActivitySwitcher.animationIn(oMainLayout, getWindowManager());
		bShowAlert=true;
		/*if(I_TYPE_OF_TRAINER==1){
			Toast.makeText(this, this.getString(R.string.bike), Toast.LENGTH_SHORT).show();
		}else if(I_TYPE_OF_TRAINER==0){
			Toast.makeText(this, this.getString(R.string.run), Toast.LENGTH_SHORT).show();
		}else if(I_TYPE_OF_TRAINER==100){
			Toast.makeText(this, this.getString(R.string.walk), Toast.LENGTH_SHORT).show();
		}*/
		initFromResume();

		mConnectionTask=new ConnectToServiceTask();

		mThreadConnection = new Thread(mConnectionTask);
		mThreadConnection.start();

		/*ConnectToServiceTask oService = new ConnectToServiceTask();
		oService.execute();*/

		super.onResume();
	}
	
	@Override
	protected void onPause() {
		if(ConstApp.IS_DEBUG) Logger.log("INFO - Workout->onPause Trainer Services ");
		boolean bDoFinish=false;
		if(mConnection.mIService!=null) {
			try {
				bDoFinish=mConnection.mIService.isRunning();
			}catch (RemoteException ex){
				bDoFinish=false;
			}
		}
		mConnection.destroy();
		if(mThreadConnection!=null ){
			isRun=false;
			mThreadConnection.interrupt();
		}
		if(ThreadTrainer!=null){
			ThreadTrainer.interrupt();
		}
		mConnection=null;
		// animateIn this activity
		//ActivitySwitcher.animationOut(oMainLayout, getWindowManager());
		a = AnimationUtils.loadAnimation(this, R.anim.disappear);
		a.reset();
		oMainLayout.clearAnimation();
		oMainLayout.setAnimation(a);
		/*if(I_TYPE_OF_TRAINER!=1) {
			finish();
		}*/
		if(bDoFinish) finish();
		super.onPause();
	}
	/**
	 * Controlla lo stato quando chiamato dalla main con servizio in pausa
	 * */
	private void changeGUIStatus() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {


					if(ConstApp.IS_DEBUG) Logger.log("INFO - Workout->changeGUIStatus->sStatus: "+sStatus+" Trainer Services ");

					if(sStatus!=null){
						if(sStatus.compareToIgnoreCase("service_under_user_pause")==0){
							if(ConstApp.IS_DEBUG) Logger.log("INFO - Workout->changeGUIStatus->service_under_user_pause: "+sStatus+" Trainer Services ");

							//Servizio in pausa dall'utente
							iStartTrainer=2;
							btnPause.setText(mContext.getString(R.string.btnresume));
							btnStart.setEnabled(false);
						}else if(sStatus.compareToIgnoreCase("running")==0){
							ThreadTrainer = new Thread(new TrainerRunner());
							ThreadTrainer.start();
							Message mToStart = new Message();
							mToStart.what=WorkOutActivity.GUIUPDATEIDENTIFIER;
							WorkOutActivity.this.StopwatchViewUpdateHandler.sendMessage(mToStart);
							iStartTrainer=1;
							btnStart.setText(mContext.getString(R.string.btnstop));
							btnPause.setEnabled(true);
							checkGoal(extras);
							return;
						}
						btnStart.setText(mContext.getString(R.string.btnstop));
						btnPause.setEnabled(true);
					}

				}
		});

        try {                	        	
			if(mConnection!= null && mConnection.mIService!=null && mConnection.mIService.isRunning()){
				//Esercizio in running prelevo il tempo e faccio partire il timer
				if(ConstApp.IS_DEBUG) Logger.log("INFO - Workout->changeGUIStatus->isRunning: "+mConnection.mIService.isRunning()+" Trainer Services ");


				//Log.v(this.getClass().getCanonicalName(),"start time restored: "+NewExercise.getlStartTime());
				ThreadTrainer = new Thread(new TrainerRunner());
			    ThreadTrainer.start();
			    Message mToStart = new Message();
				mToStart.what=WorkOutActivity.GUIUPDATEIDENTIFIER;				
				WorkOutActivity.this.StopwatchViewUpdateHandler.sendMessage(mToStart);
				iStartTrainer=1;	            					            				    				
			}
			if(mConnection!= null && mConnection.mIService!=null && mConnection.mIService.isAutoPause()){
				//Resume in autopause quando rientro dal background
				iStartTrainer=2;

				if(ConstApp.IS_DEBUG) Logger.log("INFO - Workout->changeGUIStatus->isAutoPause: "+mConnection.mIService.isAutoPause()+" Trainer Services ");
				mConnection.mIService.setRefumeFromAutoPause(true);
				mConnection.mIService.resumeExercise();
				bAutoPause=false;
				ThreadTrainer = new Thread(new TrainerRunner());
				ThreadTrainer.start();
				Message mToStart = new Message();
				mToStart.what=WorkOutActivity.GUIUPDATEIDENTIFIER;
				WorkOutActivity.this.StopwatchViewUpdateHandler.sendMessage(mToStart);
				iStartTrainer=1;
			}
			if(ConstApp.IS_DEBUG) Logger.log("INFO - Workout->changeGUIStatus->: mConnection is: "+mConnection+" mConnection.mIService: "+mConnection.mIService
					+" Trainer Services ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(ConstApp.IS_DEBUG) Logger.log("ERROR - Workout->changeGUIStatus->Exception: "+e.getMessage()+" Trainer Services ");

		}
        
	}
	/**
	 * Controlla se la corsa è con un target impostato
	 * */
	private void checkGoal(Bundle extras) {
		if(extras !=null){
			iGoalDistance=(int) extras.getLong("goalDistance");
			dGoalHH=extras.getInt("goalHH");
			dGoalMM=extras.getInt("goalMM");	
			//Log.v(this.getClass().getCanonicalName(),"goalDistance: "+iGoalDistance);
			//Log.v(this.getClass().getCanonicalName(),"goalHH: "+dGoalHH);
			//Log.v(this.getClass().getCanonicalName(),"goalMM: "+dGoalMM);
		}
	}
	
	@Override
	public void onClick(View oObj) {
		if(oObj.getId()==R.id.btnStart){
			LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
	    	if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {	        
		       ShowAlertNoGPS();
		       return;
		    } 
			if(iStartTrainer==0){

				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setCancelable(false); // if you want user to wait for some process to finish,
				builder.setView(R.layout.layout_loading_dialog);
				oWaitForGPSFix = builder.create();

				oWaitForGPSFix.setOnCancelListener(new OnCancelListener() {
					
					@Override
					public void onCancel(DialogInterface dialog) {
						try {
							if(!mConnection.mIService.isGPSFixPosition()){
								oWaitForGPSFix.dismiss();
								mConnection.mIService.stopGPSFix();
								mConnection.mIService.stopExercise();
								iStartTrainer=0;
								Log.v(this.getClass().getCanonicalName(),"Cancel Workout");
							}
						} catch (RemoteException e) {
							Log.v(this.getClass().getCanonicalName(),"Error Cancel Workout");
						}
					}
				});
				oWaitForGPSFix.show();
				NewExerciseTask task = new NewExerciseTask();
				task.execute();
				
			}else if(iStartTrainer==1){
				//TODO add stop/save
				AlertDialog alertDialog;
		    	alertDialog = new AlertDialog.Builder(this).create();
		    	alertDialog.setTitle(this.getString(R.string.titleendapp));
		    	alertDialog.setMessage(this.getString(R.string.messagesaveworkout));
		    	alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,this.getString(R.string.yes), new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						
						Log.i(this.getClass().getCanonicalName()," esercizio save");
						
						Message mSave = new Message();
						mSave.what = WorkOutActivity.SAVEEXERCISE;
						WorkOutActivity.this.StopwatchViewUpdateHandler.sendMessage(mSave);
						mSave=null;
						iStartTrainer=0;
						Log.i(this.getClass().getCanonicalName()," esercizio stop");
						btnStart.setText(WorkOutActivity.this.getString(R.string.btnstart));
						WorkOutActivity.this.ThreadTrainer.interrupt();
						btnAddGPX.setEnabled(true);
						iStartTrainer=0;
						Intent intent = new Intent();
						intent.setClass(getApplicationContext(), NewMainActivity.class);
						startActivity(intent);
						finish();
					}        				
		    		});
		    	
		    	alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,this.getString(R.string.no), new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {					
						
						ExerciseUtils.deleteExercise(getApplicationContext(), String.valueOf(iCurrentExercise));
						Log.i(this.getClass().getCanonicalName()," esercizio stop");
						Message mSave = new Message();
						mSave.what = WorkOutActivity.NOSAVEEXERCISE;
						WorkOutActivity.this.StopwatchViewUpdateHandler.sendMessage(mSave);
						btnStart.setText(WorkOutActivity.this.getString(R.string.btnstart));
						WorkOutActivity.this.ThreadTrainer.interrupt();
						iStartTrainer=0;
						btnAddGPX.setEnabled(true);
						Intent intent = new Intent();
						intent.setClass(getApplicationContext(), NewMainActivity.class);
						startActivity(intent);
						finish();
						//Intent intent = ActivityHelper.createActivityIntent(WorkOutActivity.this,MainTrainerActivity.class);
						//ActivityHelper.startNewActivityAndFinish(WorkOutActivity.this,intent);
					}        		
					
		    		});
		    	//Log.i(this.getClass().getCanonicalName()," esercizio stop");
				//btnStart.setText(this.getString(R.string.btnstart));
				this.ThreadTrainer.interrupt();	
				
				Message mSave = new Message();
				mSave.what = WorkOutActivity.STOPEXERCISE;
				WorkOutActivity.this.StopwatchViewUpdateHandler.sendMessage(mSave);
				mSave=null;
				iStartTrainer=0;
				alertDialog.setCancelable(false);
		    	alertDialog.show();

				
			}
		}if(oObj.getId()==R.id.btnPause){
			if(iStartTrainer==1){
				Log.i(this.getClass().getCanonicalName()," new esercizio pause");
				btnPause.setText(this.getString(R.string.btnresume));
				btnStart.setEnabled(false);
				iStartTrainer=2;
				//imgStart.setBackgroundResource(R.drawable.start_trainer);
				bPause=true;
						
				this.ThreadTrainer.interrupt();		
				Message mPause = new Message();
				mPause.what = WorkOutActivity.PAUSEEXERCISE;
				WorkOutActivity.this.StopwatchViewUpdateHandler.sendMessage(mPause);
				mPause=null;
			}else if(iStartTrainer==2){
				Log.i(this.getClass().getCanonicalName()," esercizio in pausa, restartato");
				btnPause.setText(this.getString(R.string.btnpause));
				btnStart.setEnabled(true);
				iStartTrainer=1;
				//imgStart.setBackgroundResource(R.drawable.pause_trainer);
				bPause=false;							 		        
		        
		        Message mResume = new Message();
		        mResume.what = WorkOutActivity.RESUMEEXERCISE;
				WorkOutActivity.this.StopwatchViewUpdateHandler.sendMessage(mResume);
		        this.ThreadTrainer = new Thread(new TrainerRunner());
		        this.ThreadTrainer.start();
		        mResume=null;
		        bInStarting=false;
			}
		}if(oObj.getId()==R.id.btnSkipTrack){
			if(oConfigTrainer.isbPlayMusic()){
				if(mConnection!=null){
					if(mConnection.mIService!=null){
						try {
							if(mConnection.mIService.isRunning()){
								mConnection.mIService.skipTrack();
							}
						} catch (RemoteException e) {
							Log.e(this.getClass().getCanonicalName(), "Error get state of Service");
						}
							
					}
				}
			}
		}if(oObj.getId()==R.id.btnMaps){			
				if(oConfigTrainer!=null && oConfigTrainer.isbDisplayMap()) {
					Intent intent = new Intent();
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.setClass(getApplicationContext(), OpenStreetMapActivity.class);
					startActivity(intent);
					finish(); 				
				}
				return;		
		}if(oObj.getId()==R.id.btnAddGPX) {
			//TODO Load GPX File
			Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

			// Filter to only show results that can be "opened", such as a
			// file (as opposed to a list of contacts or timezones)
			intent.addCategory(Intent.CATEGORY_OPENABLE);

			// Filter to show only images, using the image MIME data type.
			// If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
			// To search for all documents available via installed storage providers,
			// it would be "*/*".
			intent.setType("application/octet-stream");
			startActivityForResult(intent, ConstApp.READ_REQUEST_CODE);
			return;
		}

	}
	@Override
	public void onActivityResult(int requestCode, int resultCode,
								 Intent resultData) {

		// The ACTION_OPEN_DOCUMENT intent was sent with the request code
		// READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
		// response to some other intent, and the code below shouldn't run at all.

		if (requestCode == ConstApp.READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			// The document selected by the user won't be returned in the intent.
			// Instead, a URI to that document will be contained in the return intent
			// provided to this method as a parameter.
			// Pull that URI using resultData.getData().
			Uri uri = null;
			if (resultData != null) {
				uri = resultData.getData();

				Log.i(this.getClass().getCanonicalName(), "Uri: " + uri.getPath());
				if(!uri.getPath().endsWith("gpx")){
					Toast.makeText(this, getString(R.string.only_gpx), Toast.LENGTH_LONG).show();
				}else {
					GPXUtils mChallenge = new GPXUtils(mContext,uri);
					mChallengeExercise = mChallenge.getChallenge(oConfigTrainer);

				}

			}
		}
	}

	private void startWorkout(){
			sStatus="running";
			//TTS Voice
	    	iStartTrainer=1;
			/**
			 * 
			 * CREAZIONE DI UN NUOVO ESERCIZIO IN DB
			 * 
			 * 
			 */
	    	iCurrentExercise=oExerciseUtil.createNewExercise(getApplicationContext(), oConfigTrainer, I_TYPE_OF_TRAINER, mUser.iWeight);
			;
			
	        WorkOutActivity.this.ThreadTrainer = new Thread(new TrainerRunner());
	        WorkOutActivity.this.ThreadTrainer.start();
	        Message mToStart = new Message();
			mToStart.what=WorkOutActivity.STARTEXERCISE;				
			WorkOutActivity.this.StopwatchViewUpdateHandler.sendMessage(mToStart);
			//if(I_TYPE_OF_TRAINER==ConstApp.TYPE_BIKE && oConfigTrainer.isbPlayMusic())
			//	finish();
			
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();		
		this.setResult(0);			
	}	

	/**Test code UP**/
	@Override
	public void onBackPressed() {	
		Intent intent = new Intent();

		if(mConnection.mIService!=null) {
			try {
				if(mConnection.mIService.isRunning()){
					if(oWaitForGPSFix!=null && oWaitForGPSFix.isShowing()){
						if(ConstApp.IS_DEBUG) Logger.log("INFO - Workout->onBackPressed Back Press on Running and GPS FIX ");
						Log.d(this.getClass().getCanonicalName(),"Back Press on Running and GPS FIX");
						oWaitForGPSFix.dismiss();
						mConnection.mIService.stopGPSFix();
						mConnection.mIService.stopExercise();
						mConnection.destroy();

					}
					Log.d(this.getClass().getCanonicalName(),"Back Press on Running");
					return;
				}else{
					if(ConstApp.IS_DEBUG) Logger.log("INFO - Workout->onBackPressed Back Press on NOT Running ");
					Log.d(this.getClass().getCanonicalName(),"Back Press on NOT Running");
					mConnection.mIService.stopGPSFix();
					mConnection.mIService.shutDown();
					mConnection.destroy();

					intent.setClass(getApplicationContext(), NewMainActivity.class);
					startActivity(intent);
					finish(); 				
				}
			} catch (RemoteException e) {
				if(ConstApp.IS_DEBUG) Logger.log("INFO - Workout->onBackPressed RemoteException "+e.getMessage());
				Log.e(this.getClass().getCanonicalName(),"onBackPressed RemoteException: "+e.getMessage());
				intent.setClass(getApplicationContext(), NewMainActivity.class);
				startActivity(intent);
				finish(); 		
			}
		}

		Log.d(this.getClass().getCanonicalName(),"Back Press on SERVICE NULL Running");
		intent.setClass(getApplicationContext(), NewMainActivity.class);
		startActivity(intent);
		finish(); 		
	}
	
	/**
	 * Thread principale che viene avviato per far partire l'esercizio
	 * all'interno ci saranno tutti i metodi per aggiornare la GUI e 
	 * inserire i record nel DB
	 * */
	class TrainerRunner implements Runnable{
		public void run() {  
			/**
			 * 
			 * Start Service for the GPS
			 * 
			 * AVVIO DEL SERVIZIO PER IL NUOVO ESERCIZIO
			 * 
			 * */
			if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
				getBaseContext().startForegroundService(new Intent(getApplication(),ExerciseService.class));
			}else{
				getBaseContext().startService(new Intent(getApplication(),ExerciseService.class));
			}


			//Log.i("Avvio Servizio New Exercise", "Stopwatch->TrainerRunner");
			if(ConstApp.IS_DEBUG) Logger.log("INFO - TrainerRunner id "+Thread.currentThread().getId()+" is started");
			while(!Thread.currentThread().isInterrupted()){
				Message m = new Message();
				
				m.what = WorkOutActivity.GUIUPDATEIDENTIFIER;
				
				WorkOutActivity.this.StopwatchViewUpdateHandler.sendMessage(m);
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}

			if(ConstApp.IS_DEBUG) Logger.log("INFO - TrainerRunner id "+Thread.currentThread().getId()+" is interrupted!");
	    }
	}
	
	/**
	 * Thread per la gestione del Cardio e aggiornamento della GUI
	 * */
	class CardioThread implements Runnable{
		public void run() {  
			
			Log.i(this.getClass().getCanonicalName(), "Avvio Lettura Cardio");
			
			while(!Thread.currentThread().isInterrupted()){
				Message m = new Message();
				if(!oBTHelper.isbConnect()){
					oBTHelper.disconect();
					oBTHelper.stop();
					
					oBTHelper.connect();										
				}
			
				m.what = WorkOutActivity.GUIUPDATECARDIO;				
				WorkOutActivity.this.StopwatchViewUpdateHandler.sendMessage(m);
				
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}   	   
	    }
	}
	
	/**
	 * Visualizza una alert per il GPS non abilitato
	 *
	 * @author Gianluca Masci aka (GLM)
	 * */
	public void ShowAlertNoGPS() {
		try{
			AlertDialog alertDialog;
	    	alertDialog = new AlertDialog.Builder(this).create();
	    	alertDialog.setTitle(this.getString(R.string.titlegps));
	    	alertDialog.setMessage(this.getString(R.string.messagegpsnoenabled));
	    	alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,this.getString(R.string.yes), new android.content.DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
				    startActivity(myIntent);
				}        				
	    		});
	    	
	    	alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,this.getString(R.string.no), new android.content.DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int arg1) {					
					
				}        		
				
	    		});
	    	alertDialog.show();
		}catch (Exception e) {
			Toast.makeText(this, "ERROR DIALOG:"+e.getMessage(), Toast.LENGTH_SHORT).show();
			Log.e("MEEERR: ",e.getMessage());
		}
	}
	
	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	final Messenger mMessengerIncoming = new Messenger(new IncomingHandler());
	
	class IncomingHandler extends Handler {
	    @Override
	    public void handleMessage(Message msg) {
	       
	        Log.i("Received from service: " , String.valueOf(msg.arg1));
	              
	    }
	}	
	
	private class NewExerciseTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected void onPostExecute(Boolean result) {
			try{
				if(oWaitForGPSFix.isShowing()){
					oWaitForGPSFix.dismiss();
					if(result){
						if(!bInStarting){
							btnStart.setText(getApplicationContext().getString(R.string.btnstop));
							btnPause.setEnabled(true);
							btnAddGPX.setEnabled(false);
						   	//Non Traccio Il Peso
						   	startWorkout();
						    bInStarting=true;
					    }
					}else{
						ShowAlertNoGPS();
					}
				}
			}catch(IllegalArgumentException e){
				Log.e(this.getClass().getCanonicalName(),"Error waoit dismiss");
			}
		}
			
		@Override
		protected Boolean doInBackground(Void... mDB) {
			Log.i(this.getClass().getCanonicalName()," new esercizio start");
			
			//TODO Check User if(!bCheckUser()) return;
			final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
			if(ConstApp.IS_DEBUG) Logger.log("INFO - Workout->NewExerciseTask->doInBackground Create new exercise for Trainer Services");
			if(mConnection==null){
				mConnectionTask=new ConnectToServiceTask();
				mThreadConnection = new Thread(mConnectionTask);
				mThreadConnection.start();
			}
			while(mConnection==null){
				if(ConstApp.IS_DEBUG) Logger.log("INFO - Workout->NewExerciseTask->doInBackground try to reconnect to service wait for service Workout");
				if(mConnection==null || mConnectionTask==null || !mThreadConnection.isAlive()){
					mConnectionTask=new ConnectToServiceTask();
					mThreadConnection = new Thread(mConnectionTask);
					mThreadConnection.start();
				}

			}
			if(mConnection.mIService!=null){
				try {
					if(mConnection!=null) mConnection.mIService.runGPSFix();
					while(mConnection!=null &&
							!mConnection.mIService.isGPSFixPosition()){
						Thread.sleep(1000);
					}
					if(ConstApp.IS_DEBUG) Logger.log("INFO - Workout->NewExerciseTask->doInBackground FIX GPS for Trainer Services");

				} catch (RemoteException e) {
					Log.e(this.getClass().getCanonicalName(), "RemoteException from service");
					if(ConstApp.IS_DEBUG) Logger.log("ERROR - Workout->NewExerciseTask->doInBackground RemoteException FIX GPS for Trainer Services "+e.getMessage());
					finish();
				} catch (InterruptedException e) {
					Log.e(this.getClass().getCanonicalName(), "InterruptedException from service");
					if(ConstApp.IS_DEBUG) Logger.log("ERROR - Workout->NewExerciseTask->doInBackground InterruptedException FIX GPS for Trainer Services "+e.getMessage());

				}
			}else{
				if(ConstApp.IS_DEBUG) Logger.log("WARNING - Workout->NewExerciseTask->doInBackground mConnection.mIService==null FIX GPS for Trainer Services ");

				return false;
			}
		    if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {	        
		        return false;
		    }
		    
			return true;
		}
	}	
	private class ConnectToServiceTask implements Runnable{

		@Override
		public void run() {
			while(isRun){
				Log.v(this.getClass().getCanonicalName(),"wait for Service");
				if(ConstApp.IS_DEBUG) Logger.log("INFO - ConnectToServiceTask wait for service from Workout");
				try {
					Thread.sleep(1000);
					if(mConnection!=null && mConnection.mIService!=null){
						isRun=false;
						break;
					}
					if(Thread.interrupted()){
						break;
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(ConstApp.IS_DEBUG) Logger.log("INFO - ConnectToServiceTask connected to service from Workout");

			try {
				if(mConnection!=null &&
						mConnection.mIService!=null &&
							mConnection.mIService.getiTypeExercise()==1){
                    /** 0=running
                     * 1=biking
                     * 100=walking
                     *
                     * */
                    I_TYPE_OF_TRAINER=mConnection.mIService.getiTypeExercise();
                }
				if(mConnection!=null &&
						mConnection.mIService!=null &&
							mConnection.mIService.isServiceAlive() &&
								mConnection.mIService.isRunning()){
					if(ConstApp.IS_DEBUG) Logger.log("INFO - ConnectToServiceTask->WorkOutActivity->running  Trainer Services ");
					sStatus="running";
				}else if(mConnection!=null &&
							mConnection.mIService!=null &&
								mConnection.mIService.isServiceAlive() &&
									!mConnection.mIService.isRunning()) {
					//Toast.makeText(MainTrainerActivity.this, "Second type: "+mIService.getiTypeExercise(),
					//        Toast.LENGTH_LONG).show();
					if (mConnection.mIService.isAutoPause()) {

						if (ConstApp.IS_DEBUG)
							Logger.log("INFO - ConnectToServiceTask->WorkOutActivity->service_under_autopause  Trainer Services ");
						sStatus = "service_under_autopause";

					} else if (mConnection.mIService.isPause()) {
						if (ConstApp.IS_DEBUG)
							Logger.log("INFO - ConnectToServiceTask->WorkOutActivity->service_under_user_pause  Trainer Services ");
						sStatus = "service_under_user_pause";
					}
				}
			} catch (RemoteException e) {
				if(ConstApp.IS_DEBUG) Logger.log("ERROR - ConnectToServiceTask->WorkOutActivity->RemoteException "+e.getMessage()+" Trainer Services ");
			}

			oConfigTrainer = ExerciseUtils.loadConfiguration(getApplicationContext());
			mUser=ExerciseUtils.loadUserDectails(getApplicationContext());

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					try{
						if(oConfigTrainer.isbUseCardio() && oConfigTrainer.isbCardioPolarBuyed()){
							oBTHelper = new BlueToothHelper();

							if(!oBTHelper.isBluetoothAvail()){
								Toast.makeText(getBaseContext(), "NO Bluetooth", Toast.LENGTH_LONG)
										.show();
							}
							if(!oBTHelper.isBlueToothEnabled()){
								Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
								startActivityForResult(enableBtIntent, BluetoothAdapter.STATE_TURNING_ON);
							}
							//startCardio();
						}else{
							lblHeatRate.setVisibility(View.INVISIBLE);
						}
					}catch (NullPointerException e) {
						Log.e(this.getClass().getCanonicalName(),"Error Cardio Polar");
					}

					if(oConfigTrainer.isbDisplayMap()){
						btnMaps.setVisibility(View.VISIBLE);
					}else{
						btnMaps.setVisibility(View.INVISIBLE);
					}

					if(oConfigTrainer.isbPlayMusic()){
						btnSkipTrack.setVisibility(View.VISIBLE);
					}else{
						btnSkipTrack.setVisibility(View.INVISIBLE);
					}
				}
			});


			changeGUIStatus();
		}
	}
}
