package com.glm.trainer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.glm.utils.VoiceToSpeechTrainer;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;


public class GCMIntentService extends GCMBaseIntentService{
	private static final String SENDER_ID = "558307532040";
	private NotificationManager mNM;
	private VoiceToSpeechTrainer oVoice;
	public GCMIntentService() {
        super(SENDER_ID);
        Log.i(this.getClass().getCanonicalName(), "Device registered: SENDER_ID = " + SENDER_ID);           
    }
	
	@Override
	protected void onError(Context context, String arg1) {
		// TODO Auto-generated method stub
		Log.i(this.getClass().getCanonicalName(), "Device onError: = " + arg1);
	}

	@Override
	protected void onMessage(Context context, Intent arg1) {
		String Message="";
		Log.i(this.getClass().getCanonicalName(), "Device onMessage: = " + arg1);
		
		if(arg1.getStringExtra("speech")!=null){			
			Message=arg1.getStringExtra("speech");			
		}
		Message+=arg1.getStringExtra("message");
		generateNotification(context, Message);
	}

	private void generateNotification(Context context, String stringExtra) {
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE); 	
		

        // The PendingIntent to launch our activity if the user selects this notification Controller
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, NewMainActivity.class), 0);



		Notification notification = new Notification.Builder(this)
				.setContentTitle(getText(R.string.app_name_pro))
				.setContentText(stringExtra)
				.setSmallIcon(R.drawable.biking).setContentIntent(contentIntent)
				.build();
        Log.v(this.getClass().getCanonicalName(), "Show Notification "+stringExtra);
        // Send the notification.
        if(mNM!=null) mNM.notify( R.string.app_name_pro, notification);
	}

	@Override
	protected void onRegistered(Context context, String redID) {
		// TODO Auto-generated method stub
		GCMRegistrar.setRegisteredOnServer(context, true);
		Log.i(this.getClass().getCanonicalName(), "Device onRegistered: = " + redID);
	}

	@Override
	protected void onUnregistered(Context context, String arg1) {
		// TODO Auto-generated method stub
		
	}

}
