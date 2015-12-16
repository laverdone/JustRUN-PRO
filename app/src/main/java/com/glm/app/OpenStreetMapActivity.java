package com.glm.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.glm.app.stopwatch.WorkOutActivity;
import com.glm.bean.ConfigTrainer;
import com.glm.trainer.R;
import com.glm.utils.ExerciseUtils;
import com.glm.utils.JsHandler;
import com.glm.utils.TrainerServiceConnection;
import com.glm.utils.animation.ActivitySwitcher;

import java.util.Timer;
import java.util.TimerTask;

public class OpenStreetMapActivity  extends Activity implements OnClickListener{
	private ConfigTrainer oConfigTrainer;
	private WebView wv;
	private Button oBtnBack;
	private ImageButton btnSkipTrack;
	
	private Timer updateMap = new Timer();
	private JsHandler jshandler;
	
	private double dLong;
	private double dLat;
	
	/**Oggetto connessione al servizio*/
	private TrainerServiceConnection mConnection =null;
	private RelativeLayout oMainLayout;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stopwatch_map);        
		/**Bind col servizio*/
		mConnection = new TrainerServiceConnection(getApplicationContext());
        oMainLayout = (RelativeLayout) findViewById(R.id.MainLayout);
		wv 				= (WebView) findViewById(R.id.wv1); 
		oBtnBack 		= (Button) findViewById(R.id.btnBack); 
		btnSkipTrack 	= (ImageButton) findViewById(R.id.btnSkipTrack);
        
		oConfigTrainer = ExerciseUtils.loadConfiguration(this);	

		jshandler = new JsHandler (wv,ExerciseUtils.getWeightData(this),getApplicationContext());
		try {	           
			// Load the local file into the webview\
			wv.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
			wv.getSettings().setBuiltInZoomControls(false);		           
			wv.getSettings().setJavaScriptEnabled(true);
			wv.addJavascriptInterface(jshandler, "jshandler");
			//jshandler.loadGraph();
			//wv.loadData(text, "text/html", "UTF-8");
			//Log.v(this.getClass().getCanonicalName(), "LoadURL:"+"file:///android_asset/map/jsmap.html");
			wv.loadUrl("file:///android_asset/map/jsmap.html");
			updateMap();
		} catch (Exception e) {
			// Should never happen!
			throw new RuntimeException(e);
		}	  
		oBtnBack.setOnClickListener(this);
		btnSkipTrack.setOnClickListener(this);
	}
	@Override
	public void onBackPressed() {		
		updateMap.cancel();
		mConnection.destroy();
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), WorkOutActivity.class);
		startActivity(intent);
		finish();
	}
	@Override
	protected void onResume() {
		
		// animateIn this activity
		ActivitySwitcher.animationIn(oMainLayout, getWindowManager());
		
		super.onResume();
		jshandler = new JsHandler (wv,ExerciseUtils.getWeightData(this),getApplicationContext());
		try {	           
			// Load the local file into the webview\
			wv.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
			wv.getSettings().setBuiltInZoomControls(false);		           
			wv.getSettings().setJavaScriptEnabled(true);
			wv.addJavascriptInterface(jshandler, "jshandler");
			//jshandler.loadGraph();
			//wv.loadData(text, "text/html", "UTF-8");
			//Log.v(this.getClass().getCanonicalName(), "LoadURL:"+"file:///android_asset/map/jsmap.html");
			wv.loadUrl("file:///android_asset/map/jsmap.html");
			updateMap();
		} catch (RuntimeException e) {
			Log.e(this.getClass().getCanonicalName(),"Error Runtime");
		}	
		
		if(oConfigTrainer.isbPlayMusic()){
	       	btnSkipTrack.setVisibility(View.VISIBLE);
	    }else{
	      	btnSkipTrack.setVisibility(View.INVISIBLE);
	    }
		
	}

	@Override
	protected void onPause() {
		// animateIn this activity
		ActivitySwitcher.animationOut(oMainLayout, getWindowManager());
		super.onPause();
	}

	@Override
	public void onClick(View oObj) {
		if(oObj.getId()==R.id.btnBack){
			updateMap.cancel();
			mConnection.destroy();
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), WorkOutActivity.class);
			startActivity(intent);
			finish();			
		}if(oObj.getId()==R.id.btnSkipTrack){
			if(oConfigTrainer.isbPlayMusic()){
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
	}
	
	/**
	 * Aggiorna la mappa
	 * 
	 * */
	private void updateMap() {
	   //Log.v(this.getClass().getCanonicalName(),"updateMap");
		  
		 
		updateMap.scheduleAtFixedRate(
    			new TimerTask()
    			{
    				@Override
    				public void run()
    				{    				
    					//Log.v(this.getClass().getCanonicalName(),"updateMap->run");
    					if(mConnection.mIService!=null){ 
    						//Log.v(this.getClass().getCanonicalName(),"updateMap->run->!null");
    						runOnUiThread(new Runnable() {
    							@Override
	                            public void run() {
	    								try {
	    									dLat=mConnection.mIService.getLatidute();
	    									dLong=mConnection.mIService.getLongitude();
	    									if(jshandler!=null){
	    										jshandler.updateMap(dLong,dLat);																	
	    									}
	    									
	    								} catch (RemoteException e) {
	    									Log.e(this.getClass().getCanonicalName(),"updateMap->run->Error getting new Location");
	    									e.printStackTrace();
	    								}
	                            }
    						});
    					}    					
    				}					
    			}, 
    			5000,
    			5000);
    	
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
}
