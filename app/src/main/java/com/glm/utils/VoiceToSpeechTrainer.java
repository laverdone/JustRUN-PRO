package com.glm.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.glm.bean.ConfigTrainer;
import com.glm.trainer.R;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Random;

public class VoiceToSpeechTrainer implements TextToSpeech.OnInitListener{
	private Context oContext;
	private TextToSpeech mTts;
	
	private static final Random RANDOM = new Random();
    private static final String[] HELLOS = {
      "Welcome!"
    };
    public VoiceToSpeechTrainer(Context Context){
    	oContext=Context;
    	 // success, create the TTS instance
        mTts = new TextToSpeech(oContext, this);
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
     *Risotna se sta o no parlando
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
			String sTimeToSpeech, String sKalories, String sPace, String sPendenza, int iHeartRate) {
		
		String sSpeech="";

		//Step 1: Distanza
		if(oConfigTrainer.isSayDistance()) {
			sSpeech=oContext.getString(R.string.distance);
			//Step 2: Parte intera + unità di misura
			try{
				sSpeech+=sDistanceToSpeech.substring(0, sDistanceToSpeech.indexOf(","));
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
				DecimalFormat oDF = new DecimalFormat();				
				sSpeech+=sDistanceToSpeech.substring(sDistanceToSpeech.indexOf(oDF.getDecimalFormatSymbols().getDecimalSeparator())+1);
			}catch(StringIndexOutOfBoundsException e){
				Log.w(this.getClass().getCanonicalName(),"Error getting distance to speech");
			}catch (NumberFormatException e) {
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
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			mTts.speak(sSpeech,
                    TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
                    null,"1234");
		}else{
			mTts.speak(sSpeech,
					TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
					null);
		}

		while(mTts.isSpeaking()){
    		try {
				Thread.sleep(2000);
            } catch (InterruptedException e) {              
            	Log.e(this.getClass().getCanonicalName(),"InterruptedException"+e.getMessage());            	
            }
    	}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		 	Log.e(this.getClass().getCanonicalName(),"InterruptedException"+e.getMessage());
		}
		//Riavvio la musica se necessario
		if(oConfigTrainer.isbPlayMusic()){
			if(oMediaPlayer!=null) oMediaPlayer.play(true);
		}
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
    
}
