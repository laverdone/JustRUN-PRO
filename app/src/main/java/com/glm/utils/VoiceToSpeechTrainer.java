package com.glm.utils;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.glm.app.ConstApp;
import com.glm.app.NewMainActivity;
import com.glm.bean.ChallengeExercise;
import com.glm.bean.ConfigTrainer;
import com.glm.bean.Exercise;
import com.glm.bean.WatchPoint;
import com.glm.services.ExerciseService;
import com.glm.trainer.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class VoiceToSpeechTrainer implements TextToSpeech.OnInitListener{
	private Context oContext;
	private TextToSpeech mTts;
	private WorkOutTrainer oTask;
	private ConfigTrainer mConfigTrainer;
	private MediaTrainer mMediaPlayer;
	private static final Random RANDOM = new Random();
	private AudioManager mAudioManager = null;
	private int preVolume=0;
	private int preVolumeTTs=0;
	private static final String[] HELLOS = {
      "Welcome!"
    };
    public VoiceToSpeechTrainer(Context Context){
    	oContext=Context;
    	 // success, create the TTS instance
        mTts = new TextToSpeech(oContext, this);
		mAudioManager = (AudioManager) oContext.getSystemService(Context.AUDIO_SERVICE);
    }
	public boolean isTextToSpeechAvaiable(){
		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		//startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
		return true;
	}

	public void release(){
		mTts.stop();
	}
	protected void onActivityResult(
	        int requestCode, int resultCode, Intent data) {
	    /*if (requestCode == MY_DATA_CHECK_CODE) {
	        if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
	            // success, create the TTS instance
	            mTts = new TextToSpeech(oContext, this);
	        } else {
	            // missing data, install it
	            Intent installIntent = new Intent();
	            installIntent.setAction(
	                TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
	            startActivity(installIntent);
	        }
	    }*/
	}
	
	
	public void onStopSpeech() {
		 if (mTts != null) {
	            mTts.stop();
	            mTts.shutdown();
	    } 
	}

	@Override
	public void onInit(int status) {
		if(mTts==null) return;
		if (status == TextToSpeech.SUCCESS) {
            // Set preferred language to US english.
            // Note that a language may not be available, and the result will indicate this.
            int result = mTts.setLanguage(Locale.getDefault());
            // Try this someday for some interesting results.
            // int result mTts.setLanguage(Locale.FRANCE);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
               // Lanuage data is missing or the language is not supported.
                Log.e(this.getClass().getCanonicalName(), "Language is not available.");
            } else {             
                // Greet the user.
                //sayHello();
            }
        } else {
            // Initialization failed.
            Log.e(this.getClass().getCanonicalName(), "Could not initialize TextToSpeech.");
        }
	}

    public void sayHello() {
        // Select a random hello.
        int helloLength = HELLOS.length;
        String hello = HELLOS[RANDOM.nextInt(helloLength)];
        mTts.speak(hello,
            TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
            null);
    }
    /*
     *Ritorna se sta o no parlando
     **/
    public boolean isBusy(){
    	return mTts.isSpeaking();
    }
    
    public void say(String sWord){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			mTts.speak(sWord,
					TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
					null,"1234");
		}else{
			mTts.speak(sWord,
					TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
					null);
		}

    	while(mTts.isSpeaking()){
    		try {
				Thread.sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
            	Log.e(this.getClass().getCanonicalName(),"InterruptedException"+e.getMessage());
            	return;
            }
    	}
    	
    	return;
    }
    
	public void sayDistanza(Context oContext, ConfigTrainer oConfigTrainer,
							String sDistanceToSpeech, MediaTrainer oMediaPlayer,
							String sTimeToSpeech, String sKalories, String sPace, String sPendenza, int iHeartRate, ChallengeExercise mChallengeExercise, long lCurrentTime, double dCurrentDistance) {
		Bundle params = new Bundle();
		params.putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_VOICE_CALL);

		String sSpeech="";
		SimpleDateFormat sdf = new SimpleDateFormat("ssSSS");
		String sID = sdf.format(new Date());

		mConfigTrainer=oConfigTrainer;
		mMediaPlayer =oMediaPlayer;

		preVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,10,0);
		preVolumeTTs= mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
		mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,100,0);
		//Step 1: Distanza
		if(oConfigTrainer.isSayDistance()) {
			sSpeech=oContext.getString(R.string.distance);
			//Step 2: Parte intera + unità di misura
			try{
				DecimalFormat oDF = new DecimalFormat();
				if(sDistanceToSpeech!=null &&
						sDistanceToSpeech.indexOf(oDF.getDecimalFormatSymbols().getDecimalSeparator())>0) sSpeech+=sDistanceToSpeech.substring(0, sDistanceToSpeech.indexOf(oDF.getDecimalFormatSymbols().getDecimalSeparator()));
				else sSpeech+=sDistanceToSpeech;
			}catch(StringIndexOutOfBoundsException e){
				if(ConstApp.IS_DEBUG) Logger.log("ERROR - VoiceToSpeechTrainer->sayDistanza - Error getting distance to speech StringIndexOutOfBoundsException");
				Log.w(this.getClass().getCanonicalName(),"Error getting distance to speech");
			}
			if(oConfigTrainer.getiUnits()==0){
				//Km
				sSpeech+=" "+oContext.getString(R.string.kilometer);
			}else{
				//Miles
				sSpeech+=" "+oContext.getString(R.string.miles);
			}
			//Step 3: Parte decimale
			try{
				DecimalFormat oDF = new DecimalFormat();
				if(sDistanceToSpeech!=null &&
						sDistanceToSpeech.indexOf(oDF.getDecimalFormatSymbols().getDecimalSeparator())>0) sSpeech+=sDistanceToSpeech.substring(sDistanceToSpeech.indexOf(oDF.getDecimalFormatSymbols().getDecimalSeparator())+1);
			}catch(StringIndexOutOfBoundsException e){
				if(ConstApp.IS_DEBUG) Logger.log("ERROR - VoiceToSpeechTrainer->sayDistanza - Error getting distance to speech StringIndexOutOfBoundsException");
				Log.w(this.getClass().getCanonicalName(),"Error getting distance to speech");
			}catch (NumberFormatException e) {
				if(ConstApp.IS_DEBUG) Logger.log("ERROR - VoiceToSpeechTrainer->sayDistanza - Error getting distance to speech NumberFormatException");
				Log.w(this.getClass().getCanonicalName(),"Error getting distance to speech");
			}
		}
		if(oConfigTrainer.isSayTime()) {
			//Step 4: Tempo di percorrenza 
			sSpeech+="  "+sTimeToSpeech;
		}
		if(oConfigTrainer.isSayPace()) {
			//Step 5: Passo di percorrenza 
			sSpeech+="  "+sPace;
		}
		if(oConfigTrainer.isSayKalories()) {
			//Step 6: Calorie Bruciate di percorrenza 
			sSpeech+="  "+sKalories;
		}
		
		if(oConfigTrainer.isSayInclination()) {
			//Step 6: Calorie Bruciate di percorrenza 
			sSpeech+="  "+oContext.getString(R.string.inclination)+sPendenza;
		}
		if(oConfigTrainer.isbCardioPolarBuyed() && oConfigTrainer.isbUseCardio() && iHeartRate>0) {
			//Step 6: Calorie Bruciate di percorrenza 
			sSpeech+="  "+oContext.getString(R.string.heart_rate)+iHeartRate;
		}


		Log.i(this.getClass().getCanonicalName(),"Say: "+sSpeech);
		boolean asSpeek=false;
		String sDistanzaOpponent="";
		DecimalFormat decimalFormat = new DecimalFormat("###.#");
		//if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			//Check the challengeexercise
			if(mChallengeExercise!=null){
				int iMaxSize=mChallengeExercise.getTrackPointExercise().size();
				for (int i = 0; i < iMaxSize; i++) {
					WatchPoint currentWatchPoint = mChallengeExercise.getTrackPointExercise().get(i);

					if(ExerciseUtils.getHours(lCurrentTime)==currentWatchPoint.getiHoursTotal() &&
							(ExerciseUtils.getMinutes(lCurrentTime)<=currentWatchPoint.getiMinuteTotal())){
						if(currentWatchPoint.getdDistance()<dCurrentDistance){
							if(oConfigTrainer.getiUnits()==0){
								//Km to meters 1000
								sDistanzaOpponent=decimalFormat.format(Math.abs(dCurrentDistance-currentWatchPoint.getdDistance()))+oContext.getString(R.string.meters)+oContext.getString(R.string.ahead);
							}else{
								//miles to feet 5280
								sDistanzaOpponent=decimalFormat.format(Math.abs(dCurrentDistance-currentWatchPoint.getdDistance()))+oContext.getString(R.string.feet)+oContext.getString(R.string.ahead);
							}

							//Sono attualemnte più veloce
							mTts.speak(sSpeech+" "+oContext.getString(R.string.faster_then_opponent)+" "+sDistanzaOpponent,TextToSpeech.QUEUE_FLUSH, params,sID);
							asSpeek=true;
							break;
						}else{
							if(oConfigTrainer.getiUnits()==0){
								//Km to meters 1000
								sDistanzaOpponent=decimalFormat.format(Math.abs(dCurrentDistance-currentWatchPoint.getdDistance()))+oContext.getString(R.string.meters)+oContext.getString(R.string.behind);
							}else{
								//miles to feet 5280
								sDistanzaOpponent=decimalFormat.format(Math.abs(dCurrentDistance-currentWatchPoint.getdDistance()))+oContext.getString(R.string.feet)+oContext.getString(R.string.behind);
							}
							//Sono attualemnte più lento
							mTts.speak(sSpeech+" "+oContext.getString(R.string.slower_then_opponent)+" "+sDistanzaOpponent,TextToSpeech.QUEUE_FLUSH, params,sID);
							asSpeek=true;
							break;
						}
					}
				}
				if(!asSpeek){
					//Sono attualemnte più veloce perchè l'esercizio attuale è più lungo
					mTts.speak(sSpeech+" "+oContext.getString(R.string.faster_then_opponent)+" "+sDistanzaOpponent,TextToSpeech.QUEUE_FLUSH, params,sID);
				}
			}else {
				mTts.speak(sSpeech,
						TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
						params, sID);
			}
		if(ConstApp.IS_DEBUG) Logger.log("INFO - VoiceToSpeechTrainer->sayDistanza - sSpeech:"+sSpeech);

		oTask = new WorkOutTrainer();
		oTask.execute();

		/*while(mTts.isSpeaking()){
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				if(ConstApp.IS_DEBUG) Logger.log("ERROR - VoiceToSpeechTrainer->sayDistanza - Error getting distance to speech InterruptedException");
				Log.e(this.getClass().getCanonicalName(),"InterruptedException"+e.getMessage());
			}
		}*/


	}
	public void sayDistanzaToGoal(Context oContext,
			ConfigTrainer oConfigTrainer, String sDistanceToSpeech,
			MediaTrainer oMediaPlayer) {
		String sSpeech="";
			
			//Step 2: Parte intera + unità di misura
			try{
				sSpeech=sDistanceToSpeech.substring(0, sDistanceToSpeech.indexOf(","));
			}catch(StringIndexOutOfBoundsException e){
				Log.w(this.getClass().getCanonicalName(),"Error getting distance to speech");
			}
			if(oConfigTrainer.getiUnits()==0){
				//Km
				sSpeech+=oContext.getString(R.string.kilometer);
			}else{
				//Miles
				sSpeech+=oContext.getString(R.string.miles);
			}
			//Step 3: Parte decimale
			try{
				sSpeech+=Integer.parseInt(sDistanceToSpeech.substring(sDistanceToSpeech.indexOf(",")+1));
				sSpeech+=oContext.getString(R.string.distancetogoal);
				mTts.speak(sSpeech,
		                TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
		                null,"");
				while(mTts.isSpeaking()){
		    		try {
						Thread.sleep(1000);
		            } catch (InterruptedException e) {              
		            	Log.e(this.getClass().getCanonicalName(),"InterruptedException"+e.getMessage());            	
		            }
		    	}
				//Riavvio la musica se necessario
				if(oConfigTrainer.isbPlayMusic()){
					if(oMediaPlayer!=null) oMediaPlayer.play(true);		
				}
			}catch(StringIndexOutOfBoundsException e){
				Log.w(this.getClass().getCanonicalName(),"StringIndexOutOfBoundsException->Error getting distance to speech");
			}catch (NumberFormatException e) {
				Log.w(this.getClass().getCanonicalName(),"NumberFormatExceptionn->Error getting distance to speech");
			}
		
	}
	/**
	 * TODO sbaglia e quando ci sono solo le ore non le dice
	 * */
	public void sayTimeToGoal(Context oContext,
			ConfigTrainer oConfigTrainer, double iGoalHH, double iGoalMM, int timeHH,
			int timeMM, MediaTrainer oMediaPlayer) {
		String sSpeech="";
		double iHourToGoal=0;
		double iMinutesToGoal=0;
		
		iHourToGoal=iGoalHH-timeHH;
		iMinutesToGoal=iGoalMM-timeMM;
		
		while(iMinutesToGoal<0){
			iHourToGoal--;
			iMinutesToGoal=60-Math.abs(iMinutesToGoal);
		}
		if(iHourToGoal<0) {
			//Riavvio la musica se necessario
			if(oConfigTrainer.isbPlayMusic()){
				if(oMediaPlayer!=null) oMediaPlayer.play(true);		
			}
			return;
		}
			
		//TODO OBIETTIVO RAGGIUNTO
			
		if(iHourToGoal!=0){			
			sSpeech+=(int)((iHourToGoal/60)+iMinutesToGoal)+oContext.getString(R.string.minutes)+oContext.getString(R.string.distancetogoal);
		}else{			
			sSpeech+=(int) iMinutesToGoal+oContext.getString(R.string.minutes)+oContext.getString(R.string.distancetogoal);			
		}

		mTts.speak(sSpeech,
                TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
                null);
		while(mTts.isSpeaking()){
    		try {
				Thread.sleep(1000);
            } catch (InterruptedException e) {              
            	Log.e(this.getClass().getCanonicalName(),"InterruptedException"+e.getMessage());            	
            }
    	}
		//Riavvio la musica se necessario
		if(oConfigTrainer.isbPlayMusic()){
			if(oMediaPlayer!=null) oMediaPlayer.play(true);		
		}
	}
	public void sayDistanceAndTimeToGoal(Context applicationContext,
			ConfigTrainer oConfigTrainer, double dVMGoal,
			double dVMExercise, MediaTrainer oMediaPlayer) {
		String sSpeech="";
				
		if(dVMGoal>dVMExercise){
			sSpeech+=oContext.getString(R.string.speedup);
		}else{
			sSpeech+=oContext.getString(R.string.speedok);			
		}

		mTts.speak(sSpeech,
                TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
                null);
		while(mTts.isSpeaking()){
    		try {
				Thread.sleep(1000);
            } catch (InterruptedException e) {              
            	Log.e(this.getClass().getCanonicalName(),"InterruptedException"+e.getMessage());            	
            }
    	}
		//Riavvio la musica se necessario
		if(oConfigTrainer.isbPlayMusic()){
			if(oMediaPlayer!=null) oMediaPlayer.play(true);		
		}
	}

	public void sayOther(ConfigTrainer oConfigTrainer,
										 String textToSpeech,MediaTrainer oMediaPlayer) {
		String sSpeech=textToSpeech;


		mTts.speak(sSpeech,1,null,"");
	/*	mTts.speak(sSpeech,
				TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
				null);*/
		while(mTts.isSpeaking()){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Log.e(this.getClass().getCanonicalName(),"InterruptedException"+e.getMessage());
			}
		}
		//Riavvio la musica se necessario
		if(oConfigTrainer.isbPlayMusic()){
			if(oMediaPlayer!=null) oMediaPlayer.play(true);
		}
	}
	class WorkOutTrainer extends AsyncTask{
		private boolean bTimeout=true;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();


		}

		@Override
		protected Object doInBackground(Object... params) {
			return true;
		}

		@Override
		protected void onPostExecute(Object result) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					//Riavvio la musica se necessario
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,preVolume,0);
					mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,preVolumeTTs,0);
				}
			}, 8000);

		}
	}
}
