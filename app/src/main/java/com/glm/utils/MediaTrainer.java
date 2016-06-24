package com.glm.utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

import com.glm.bean.Music;

import java.io.IOException;
import java.util.Vector;

public class MediaTrainer {
	private Context oContext;
	/**
	 * Media Player
	 * */
	private MediaPlayer 	oMediaPlayer=null;
	/**Lista delle canzoni su handset*/
	private static Vector<Music> 	vListOfMusic;
	private static int 		iMusicIndex=0;
	private static String sCurrentSong=""; 
	private boolean mPause=false;

	public static void setsCurrentSong(String sCurrentSong) {
		MediaTrainer.sCurrentSong = sCurrentSong;
	}

	/**identifica se avviare il lettore interno o esterno*/
	private boolean isExternalPlayer=false;
	/**Command for External Player*/
	public static final String SERVICECMD = "com.android.music.musicservicecommand";
	public static final String CMDNAME = "command";
	public static final String CMDTOGGLEPAUSE = "togglepause";
	public static final String CMDSTOP = "stop";
	public static final String CMDPAUSE = "pause";
	public static final String CMDPLAY = "play";
	public static final String CMDPREVIOUS = "previous";
	public static final String CMDNEXT = "next";
	
	/** 
	 * Mute Stream Music
	 * 
	 * AudioManager am = (AudioManager)MainActivity.this.getSystemService(Context.AUDIO_SERVICE);
	 * am.setStreamMute(AudioManager.STREAM_MUSIC, true);
	 **/
	public MediaTrainer(Context context, boolean useExternalPlayer){
		isExternalPlayer=useExternalPlayer;
		oContext=context;
		if(!isExternalPlayer){
			oMediaPlayer= new MediaPlayer();
			//Sync Music
	        vListOfMusic = ExerciseUtils.listRecursiveFile(oContext);	
			iMusicIndex=(int) (Math.random()*vListOfMusic.size());
			
			this.initPlayer();
		}/*else{
			Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			oContext.startActivity(intent);
		}*/
	}
	
	private void initPlayer() {
		//Log.v(this.getClass().getCanonicalName(), "#################### initPlayer Play Music");	   		
   		
   		if(vListOfMusic!=null &&
   				vListOfMusic.size()>0){
   			try {   				
   				Music oMusic=(Music) vListOfMusic.get(iMusicIndex);
   				sCurrentSong=oMusic.getsTITLE();
				oMediaPlayer.setDataSource(oMusic.getsFileDATA());
				
				//Log.v(this.getClass().getCanonicalName(),"Now Play "+iMusicIndex+": "+oMusic.getsFileDATA()+" - Dur: "+oMusic.getiDURATION());
				oMusic=null;
				oMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer oCurrentPlayer) {
						//Rimuovo la canzone dalla lista
						try{
							vListOfMusic.remove(iMusicIndex);
						} catch (ArrayIndexOutOfBoundsException e) {
							Log.w(this.getClass().getCanonicalName(),"initPlayer->ArrayIndexOutOfBoundsException: "+e.getMessage());
							//Canzoni Finite
							return;
						}
						int iCurrentMusicIndex= (int) (Math.random()*vListOfMusic.size());
						
						if(iCurrentMusicIndex<vListOfMusic.size()){
							if(iCurrentMusicIndex!=iMusicIndex){
								iMusicIndex=iCurrentMusicIndex;
							}else{
								iMusicIndex=iCurrentMusicIndex++;
							}								
						}else{
							//Restart
							iMusicIndex=0;
						}
						
						try {
							oCurrentPlayer.reset(); 
							Music oMusic=(Music) vListOfMusic.get(iMusicIndex);
							sCurrentSong=oMusic.getsTITLE();
							//Log.v(this.getClass().getCanonicalName(),"Now Play: "+oMusic.getsFileDATA()+" - Dur: "+oMusic.getiDURATION());
							oMediaPlayer.setDataSource(oMusic.getsFileDATA());
							oMusic=null;								
							oCurrentPlayer.prepare();
							oCurrentPlayer.start();
						} catch (IllegalArgumentException e) {
							Log.e(this.getClass().getCanonicalName(),"initPlayer->IllegalArgumentException: "+e.getMessage());
						} catch (IllegalStateException e) {
							Log.e(this.getClass().getCanonicalName(),"initPlayer->IllegalStateException: "+e.getMessage());
						} catch (IOException e) {
							Log.e(this.getClass().getCanonicalName(),"initPlayer->IOException: "+e.getMessage());
						} catch (ArrayIndexOutOfBoundsException e) {
							Log.e(this.getClass().getCanonicalName(),"initPlayer->ArrayIndexOutOfBoundsException: "+e.getMessage());
						}
					}
				});
			} catch (IllegalArgumentException e) {
				Log.e(this.getClass().getCanonicalName(),"initPlayer->IllegalArgumentException: "+e.getMessage());
				
			} catch (IllegalStateException e) {
				Log.e(this.getClass().getCanonicalName(),"initPlayer->IllegalStateException: "+e.getMessage());
				
			} catch (IOException e) {
				Log.e(this.getClass().getCanonicalName(),"initPlayer->IOException: "+e.getMessage());				
			}
   		}	   		
	}

	public boolean play(boolean isResume){
		if(!isExternalPlayer){
			try{
				if(!isResume) oMediaPlayer.prepare();
				oMediaPlayer.start();
				mPause=false;
				return true;
			}catch (IOException e) {
				Log.e(this.getClass().getCanonicalName(),"IOException Error Playing Media");
				e.printStackTrace();
				return false;
			}catch (IllegalStateException e) {
				Log.e(this.getClass().getCanonicalName(),"IllegalStateException Error Playing Media");
				e.printStackTrace();
				return false;
			}
		}else{
			//Avvio il Player Esterno
			// play
			Intent i = new Intent("com.android.music.musicservicecommand");
			i.putExtra(CMDNAME, CMDPLAY);
			oContext.sendBroadcast(i);
			return true;
		}
	}
	public boolean pause(){
		if(!isExternalPlayer){
			try{
				if(this.isPlaying()){
					oMediaPlayer.pause();
					mPause=true;
				}
				return true;
			}catch (Exception e) {
				mPause=false;
				Log.e(this.getClass().getCanonicalName(),"Error Playing Media");
				return false;
			}
		}else{
			//Avvio il Player Esterno
			// pause
			Intent i = new Intent("com.android.music.musicservicecommand");
			i.putExtra(CMDNAME, CMDPAUSE);
			oContext.sendBroadcast(i);
			return true;
		}
	}
	
	public boolean stop(){
		if(!isExternalPlayer){
			try{
				if(this.isPlaying()){
					oMediaPlayer.stop();
				}
				return true;
			}catch (Exception e) {
				Log.e(this.getClass().getCanonicalName(),"Error Stopping Media");
				return false;
			}
		}else{
			//Avvio il Player Esterno
			// pause
			Intent i = new Intent("com.android.music.musicservicecommand");
			i.putExtra(CMDNAME, CMDSTOP);
			oContext.sendBroadcast(i);
			return true;
		}
	}
	public boolean isPlaying(){
		 if(oMediaPlayer!=null) return oMediaPlayer.isPlaying(); else return false;
	}
	/**
	 * identifica se il MediaTrainer è in pause
	 * 
	 * */
	public boolean isInPause(){
		return mPause;
	}
	/**
	 * Ritorna la traccia corrente
	 * */
	public String getCurrentSong(){
		return sCurrentSong;
	}
	public boolean destroy(){
		try{
			this.stop();
			//oMediaPlayer.release();
			return true;
		}catch (Exception e) {
			Log.e(this.getClass().getCanonicalName(),"destroy MediaPlayer Error: "+e.getMessage());
			return false;
		}
	}
	public void SkipTrack(){
		if(!isExternalPlayer){
			if(this.isPlaying()){
				
				try {
					oMediaPlayer.stop();
					oMediaPlayer.reset();				
					iMusicIndex=(int) (Math.random()*vListOfMusic.size());			
					this.initPlayer();
					this.play(false);
				} catch (IllegalStateException e) {
					Log.e(this.getClass().getCanonicalName(),"IllegalStateException on SkipTrack");				
				}
				
			}
		}else{
				//Avvio il Player Esterno
				// next track
				Intent i = new Intent("com.android.music.musicservicecommand");
				i.putExtra(CMDNAME, CMDNEXT);
				oContext.sendBroadcast(i);
		}
	}
}
