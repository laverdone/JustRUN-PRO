package com.glm.utils;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.media.MediaPlayer;
import android.util.Log;

import com.glm.bean.ConfigTrainer;

public class VoicePlayerTrainer extends MediaPlayer{
	private MediaPlayer mediaPlayer;
	private Context oContext;
	private String sLocale;
	
	public VoicePlayerTrainer(Context objContext,String Locale){
		oContext=objContext;
		mediaPlayer=new MediaPlayer();
		sLocale=Locale.toLowerCase();
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				
			}
		});
	}
	public MediaPlayer getMediaPlayer(){
		return mediaPlayer;
	}
	public void setMediaPlayer(MediaPlayer oMedia){
		mediaPlayer=oMedia;
	}
	public void sayHello(ConfigTrainer oConfigTrainer){
		try {
			String sMaleFemale="f";
			int resID=0;
			
			resID = oContext.getResources().getIdentifier(sLocale+"_"+sMaleFemale+"_welcome", "raw", "com.glm.trainer");
			mediaPlayer = MediaPlayer.create(oContext,resID);								
			mediaPlayer.start();	
			//mediaPlayer.prepareAsync();			
			
		}catch (IllegalArgumentException e) {
			Log.e(this.getClass().getCanonicalName(),"sayHello->IllegalArgumentException"+e.getMessage());
		}catch (SecurityException e) {
			Log.e(this.getClass().getCanonicalName(),"sayHello->SecurityException"+e.getMessage());
		}catch (IllegalStateException e) {
			Log.e(this.getClass().getCanonicalName(),"sayHello->IllegalStateException"+e.getMessage());
		}catch (NotFoundException e) {
			Log.e(this.getClass().getCanonicalName(),"sayHello->NotFoundException"+e.getMessage());
		}  
	}
	
	public void sayStart(ConfigTrainer oConfigTrainer){
		try {
			String sMaleFemale="f";
			int resID=0;
			
			//Log.v("Start Resouce:",sLocale+"_"+sMaleFemale+"_start");
			resID = oContext.getResources().getIdentifier(sLocale+"_"+sMaleFemale+"_start", "raw", "com.glm.trainer");
			mediaPlayer = MediaPlayer.create(oContext,resID);							
			//mediaPlayer.prepareAsync();			
			mediaPlayer.start();
		}catch (IllegalArgumentException e) {
			Log.e(this.getClass().getCanonicalName(),"sayStart->IllegalArgumentException"+e.getMessage());
		}catch (SecurityException e) {
			Log.e(this.getClass().getCanonicalName(),"sayStart->SecurityException"+e.getMessage());
		}catch (IllegalStateException e) {
			Log.e(this.getClass().getCanonicalName(),"sayStart->IllegalStateException"+e.getMessage());
		}catch (NotFoundException e) {
			Log.e(this.getClass().getCanonicalName(),"sayStart->NotFoundException"+e.getMessage());
		}  
	}
	public void saySave(ConfigTrainer oConfigTrainer){
		try {
			String sMaleFemale="f";
			int resID=0;
			
			//Log.v("Save Resouce:",sLocale+"_"+sMaleFemale+"_save");
			resID = oContext.getResources().getIdentifier(sLocale+"_"+sMaleFemale+"_save", "raw", "com.glm.trainer");
			mediaPlayer = MediaPlayer.create(oContext,resID);								
			//mediaPlayer.prepareAsync();			
			mediaPlayer.start();
		}catch (IllegalArgumentException e) {
			Log.e(this.getClass().getCanonicalName(),"saySave->IllegalArgumentException"+e.getMessage());
		}catch (SecurityException e) {
			Log.e(this.getClass().getCanonicalName(),"saySave->SecurityException"+e.getMessage());
		}catch (IllegalStateException e) {
			Log.e(this.getClass().getCanonicalName(),"saySave->IllegalStateException"+e.getMessage());
		}catch (NotFoundException e) {
			Log.e(this.getClass().getCanonicalName(),"saySave->NotFoundException"+e.getMessage());
		}  
	}
	public void sayPause(ConfigTrainer oConfigTrainer){
		try {
			String sMaleFemale="f";
			int resID=0;
			
			//Log.v("Pause Resouce:",sLocale+"_"+sMaleFemale+"_pause");
			resID = oContext.getResources().getIdentifier(sLocale+"_"+sMaleFemale+"_pause", "raw", "com.glm.trainer");
			mediaPlayer = MediaPlayer.create(oContext,resID);							
			//mediaPlayer.prepareAsync();			
			mediaPlayer.start();
		}catch (IllegalArgumentException e) {
			Log.e(this.getClass().getCanonicalName(),"sayPause->IllegalArgumentException"+e.getMessage());
		}catch (SecurityException e) {
			Log.e(this.getClass().getCanonicalName(),"sayPause->SecurityException"+e.getMessage());
		}catch (IllegalStateException e) {
			Log.e(this.getClass().getCanonicalName(),"sayPause->IllegalStateException"+e.getMessage());
		}catch (NotFoundException e) {
			Log.e(this.getClass().getCanonicalName(),"sayPause->NotFoundException"+e.getMessage());
		}  
	}
	public void sayResume(ConfigTrainer oConfigTrainer){
		try {
			String sMaleFemale="f";
			int resID=0;
			
			//Log.v("Resume Resouce:",sLocale+"_"+sMaleFemale+"_resume");
			resID = oContext.getResources().getIdentifier(sLocale+"_"+sMaleFemale+"_resume", "raw", "com.glm.trainer");
			mediaPlayer = MediaPlayer.create(oContext,resID);							
			//mediaPlayer.prepareAsync();			
			mediaPlayer.start();
		}catch (IllegalArgumentException e) {
			Log.e(this.getClass().getCanonicalName(),"sayResume->IllegalArgumentException"+e.getMessage());
		}catch (SecurityException e) {
			Log.e(this.getClass().getCanonicalName(),"sayResume->SecurityException"+e.getMessage());
		}catch (IllegalStateException e) {
			Log.e(this.getClass().getCanonicalName(),"sayResume->IllegalStateException"+e.getMessage());
		}catch (NotFoundException e) {
			Log.e(this.getClass().getCanonicalName(),"sayResume->NotFoundException"+e.getMessage());
		}  
	}
	public void sayDistanza(final VoiceToSpeechTrainer oTrainerSpeech, 
			final ConfigTrainer oConfigTrainer,
			final String sDistanceToSpeech, final MediaTrainer oMediaPlayer, final String sTimeToSpeech){	
		try {
			String sMaleFemale="f";
			int resID=0;
			
			//Log.v("Distance Resouce:",sLocale+"_"+sMaleFemale+"_dist");
			
			resID = oContext.getResources().getIdentifier(sLocale+"_"+sMaleFemale+"_dist", "raw", "com.glm.trainer");
			mediaPlayer = MediaPlayer.create(oContext,resID);								
			mediaPlayer.start();
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {   			
					mp=null;
					sayDistanceFirst(oConfigTrainer,sDistanceToSpeech,oMediaPlayer,oTrainerSpeech, sTimeToSpeech);																				
				}								
			});    		
		}catch (IllegalArgumentException e) {
			Log.e(this.getClass().getCanonicalName(),"sayDistanza->IllegalArgumentException"+e.getMessage());
		}catch (SecurityException e) {
			Log.e(this.getClass().getCanonicalName(),"sayDistanza->SecurityException"+e.getMessage());
		}catch (IllegalStateException e) {
			Log.e(this.getClass().getCanonicalName(),"sayDistanza->IllegalStateException"+e.getMessage());
		}catch (NotFoundException e) {
			Log.e(this.getClass().getCanonicalName(),"sayDistanza->NotFoundException"+e.getMessage());
		}  
	}
	/**
	 * Dice la parte intera della distanza
	 * @param oMediaPlayer 
	 * 
	 * **/
	private void sayDistanceFirst(final ConfigTrainer oConfigTrainer, 
			final String sDistanceToSpeech, final MediaTrainer oMediaPlayer, 
			final VoiceToSpeechTrainer oTrainerSpeech, final String sTimeToSpeech) {
		String sMaleFemale="f";
		int resID=0;
		
		try {
			
			//Log.v("DistanceToSpeech: ", sDistanceToSpeech);
			
			try{
				//Log.v("First 1: ", sLocale+"_"+sMaleFemale+"_"+
				//		sDistanceToSpeech.substring(0, sDistanceToSpeech.indexOf(",")));
				resID = oContext.getResources().getIdentifier(sLocale+"_"+sMaleFemale+"_"+
						sDistanceToSpeech.substring(0, sDistanceToSpeech.indexOf(",")), "raw", "com.glm.trainer");
			}catch(StringIndexOutOfBoundsException e){
				//Log.v("First 2: ", sLocale+"_"+sMaleFemale+"_"+
				//		sDistanceToSpeech);
				resID = oContext.getResources().getIdentifier(sLocale+"_"+sMaleFemale+"_"+
						sDistanceToSpeech, "raw", "com.glm.trainer");
			}

			
			mediaPlayer = MediaPlayer.create(oContext,resID);								
			mediaPlayer.start();
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {   			
					mp=null;
					sayDistanceMeasure(oConfigTrainer,sDistanceToSpeech,oMediaPlayer,oTrainerSpeech, sTimeToSpeech);																				
				}								
			});    		
		}catch (IllegalArgumentException e) {
			Log.e(this.getClass().getCanonicalName(),"sayDistanceFirst->IllegalArgumentException"+e.getMessage());
		}catch (SecurityException e) {
			Log.e(this.getClass().getCanonicalName(),"sayDistanceFirst->SecurityException"+e.getMessage());
		}catch (IllegalStateException e) {
			Log.e(this.getClass().getCanonicalName(),"sayDistanceFirst->IllegalStateException"+e.getMessage());
		}catch (NotFoundException e) {
			Log.e(this.getClass().getCanonicalName(),"sayDistanceFirst->NotFoundException"+e.getMessage());
			//Rispristino il volume della musica
			if(oConfigTrainer.isbPlayMusic()){
				if(oMediaPlayer!=null) oMediaPlayer.play(true);		
			}	
		} 
	}
	/**
	 * Dice la parte intera della distanza
	 * @param oMediaPlayer 
	 * 
	 * **/
	private void sayDistanceMeasure(final ConfigTrainer oConfigTrainer, 
			final String sDistanceToSpeech, final MediaTrainer oMediaPlayer,
			final VoiceToSpeechTrainer oTrainerSpeech, final String sTimeToSpeech) {
		String sMaleFemale="f";
		String sUnit="km";
		int resID=0;
		
		try {
			
			if(oConfigTrainer.getiUnits()==1){
				sUnit="miles";
			}
			resID = oContext.getResources().getIdentifier(sLocale+"_"+sMaleFemale+"_"+sUnit, "raw", "com.glm.trainer");
			mediaPlayer = MediaPlayer.create(oContext,resID);								
			mediaPlayer.start();
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {   			
					mp=null;
					sayDistanceDecimal(oConfigTrainer,sDistanceToSpeech,oMediaPlayer,oTrainerSpeech, sTimeToSpeech);																				
				}								
			});    		
		}catch (IllegalArgumentException e) {
			Log.e(this.getClass().getCanonicalName(),"sayDistanceDot->IllegalArgumentException"+e.getMessage());
		}catch (SecurityException e) {
			Log.e(this.getClass().getCanonicalName(),"sayDistanceDot->SecurityException"+e.getMessage());
		}catch (IllegalStateException e) {
			Log.e(this.getClass().getCanonicalName(),"sayDistanceDot->IllegalStateException"+e.getMessage());
		}catch (NotFoundException e) {
			Log.e(this.getClass().getCanonicalName(),"sayDistanceDot->NotFoundException"+e.getMessage());
			//Rispristino il volume della musica
			if(oConfigTrainer.isbPlayMusic()){
				if(oMediaPlayer!=null) oMediaPlayer.play(true);		
			}	
		} 
	}
	/**
	 * Dice la parte intera della distanza
	 * 
	 * **/
	private void sayDistanceDecimal(final ConfigTrainer oConfigTrainer, 
			final String sDistanceToSpeech, final MediaTrainer oMediaPlayer,
			final VoiceToSpeechTrainer oTrainerSpeech, final String sTimeToSpeech) {
		String sMaleFemale="f";
		int resID=0;
		
		try {
			
			try{
				//Log.v("First 1: ", sLocale+"_"+sMaleFemale+"_"+
				//		Integer.parseInt(sDistanceToSpeech.substring(sDistanceToSpeech.indexOf(",")+1)));
				
				resID = oContext.getResources().getIdentifier(sLocale+"_"+sMaleFemale+"_"+
						Integer.parseInt(sDistanceToSpeech.substring(sDistanceToSpeech.indexOf(",")+1)), "raw", "com.glm.trainer");
			}catch(StringIndexOutOfBoundsException e){
				//Log.v("First 2: ", sLocale+"_"+sMaleFemale+"_"+
				//		sDistanceToSpeech);
				
				resID = oContext.getResources().getIdentifier(sLocale+"_"+sMaleFemale+"_"+
						sDistanceToSpeech, "raw", "com.glm.trainer");
			}
			
			
			mediaPlayer = MediaPlayer.create(oContext,resID);								
			mediaPlayer.start();
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {   			
					mp=null;
					oTrainerSpeech.say(sTimeToSpeech);
					//Log.v("TimeToSpeech:",sTimeToSpeech);
					while(oTrainerSpeech.isBusy()){
						try {
							Thread.sleep(1000);
		                } catch (InterruptedException e) {
		                    // TODO Auto-generated catch block
		                	Log.e(this.getClass().getCanonicalName(),"InterruptedException"+e.getMessage());
		                }
					}
					if(oConfigTrainer.isbPlayMusic()){
						if(oMediaPlayer!=null) oMediaPlayer.play(true);		
					}																
				}								
			});    		
		}catch (IllegalArgumentException e) {
			Log.e(this.getClass().getCanonicalName(),"sayDistanceFinal->IllegalArgumentException"+e.getMessage());
		}catch (SecurityException e) {
			Log.e(this.getClass().getCanonicalName(),"sayDistanceFinal->SecurityException"+e.getMessage());
		}catch (IllegalStateException e) {
			Log.e(this.getClass().getCanonicalName(),"sayDistanceFinal->IllegalStateException"+e.getMessage());
		}catch (NotFoundException e) {
			Log.e(this.getClass().getCanonicalName(),"sayDistanceFinal->NotFoundException"+e.getMessage());
			//Rispristino il volume della musica
			if(oConfigTrainer.isbPlayMusic()){
				if(oMediaPlayer!=null) oMediaPlayer.play(true);		
			}	
		} 
	}
	/**
	 * Dice la parte intera della distanza
	 * 
	 * **/
	private void sayDistanceFinal(final ConfigTrainer oConfigTrainer, 
			final String sDistanceToSpeech, final MediaTrainer oMediaPlayer) {
		String sMaleFemale="f";
		String sUnit="metres";
		int resID=0;
		
		try {
			
			if(oConfigTrainer.getiUnits()==1){
				sUnit="feet";
			}
			//Log.v("First: ", sLocale+"_"+sMaleFemale+"_"+sUnit);
				
			resID = oContext.getResources().getIdentifier(sLocale+"_"+sMaleFemale+"_"+sUnit, "raw", "com.glm.trainer");
			
			
			
			mediaPlayer = MediaPlayer.create(oContext,resID);								
			mediaPlayer.start();
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {   			
					mp=null;
					//Log.v(this.getClass().getCanonicalName(),"Riavvio Music");
					//Rispristino il volume della musica
					if(oConfigTrainer.isbPlayMusic()){
						if(oMediaPlayer!=null) oMediaPlayer.play(true);		
					}																					
				}								
			});    		
		}catch (IllegalArgumentException e) {
			Log.e(this.getClass().getCanonicalName(),"sayDistanceFinal->IllegalArgumentException"+e.getMessage());
		}catch (SecurityException e) {
			Log.e(this.getClass().getCanonicalName(),"sayDistanceFinal->SecurityException"+e.getMessage());
		}catch (IllegalStateException e) {
			Log.e(this.getClass().getCanonicalName(),"sayDistanceFinal->IllegalStateException"+e.getMessage());
		}catch (NotFoundException e) {
			Log.e(this.getClass().getCanonicalName(),"sayDistanceFinal->NotFoundException"+e.getMessage());
			//Rispristino il volume della musica
			if(oConfigTrainer.isbPlayMusic()){
				if(oMediaPlayer!=null) oMediaPlayer.play(true);		
			}	
		} 
	}
}
