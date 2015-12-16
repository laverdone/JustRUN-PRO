package com.glm.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
/**
 * @deprecated
 * */
public class PhoneStateTrainer{
	private PhoneStateListener ListenerPhoneState = null;
	private MediaPlayer mediaPlayer;
	public PhoneStateTrainer(MediaPlayer omediaPlayer, Context oContext){
		mediaPlayer=omediaPlayer;
	
		TelephonyManager telephonyManager=(TelephonyManager) oContext.getSystemService(Context.TELEPHONY_SERVICE);
		//Log.v(this.getClass().getCanonicalName(), "Listening Incoming Call!!!!");
		try{
			ListenerPhoneState = new PhoneStateListener() {				 
				 int statoprecedente=TelephonyManager.CALL_STATE_IDLE;			       
				 @Override
			      public void onCallStateChanged(int state, String incomingNumber) {			    	  		       
			        switch (state) {			       
				        case TelephonyManager.CALL_STATE_IDLE:
				        	//Riabilito di tutti i Media Player
				        	//resumeMediaDuringCall();
				          break;
				        case TelephonyManager.CALL_STATE_OFFHOOK:				        	
				        	statoprecedente=TelephonyManager.CALL_STATE_OFFHOOK;
				        	mediaPlayer.start();
				          break;
				        case TelephonyManager.CALL_STATE_RINGING:
				          //Pausa di tutti i Media Player
				        	mediaPlayer.stop();
				          break;
			        }

			        
			      }			      
			   };
			   telephonyManager.listen(ListenerPhoneState, PhoneStateListener.LISTEN_CALL_STATE);
			   
		}catch (Exception e) {
			Log.e(this.getClass().getCanonicalName(), "Error Listening Incoming Call: "+e.getMessage());
		}			
	}

}
