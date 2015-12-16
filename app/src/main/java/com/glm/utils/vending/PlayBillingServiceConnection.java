package com.glm.utils.vending;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.android.vending.billing.IInAppBillingService;


public class PlayBillingServiceConnection implements ServiceConnection {
	public IInAppBillingService mService;
	public boolean mIsBound=false;
	
	public Context mContext;

    public PlayBillingServiceConnection(Context context){
        mContext=context;
        doBindService();
    }
    
    /**
     * Disconnetto dal servizio
     * */
    public void destroy(){
        doUnbindService();
    }
    
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		mService = IInAppBillingService.Stub.asInterface(service);
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		mService = null;
	}
	void doBindService() {
	    // Establish a connection with the service.  We use an explicit
	    // class name because there is no reason to be able to let other
	    // applications replace our component.
		//Intent bindIntent = new Intent(Main.this, MessengerService.class); 
        //bindService(bindIntent, mConnection, Context.BIND_AUTO_CREATE);                 
		if(mIsBound==false){
			mIsBound = mContext.bindService(new 
	    	        Intent("com.android.vending.billing.InAppBillingService.BIND"),
	                this, Context.BIND_AUTO_CREATE);
			Log.i(this.getClass().getCanonicalName(), "Binding from Services");
		}
		
		
	}
	void doUnbindService() {
	    if (mIsBound) {
	        // If we have received the service, and hence registered with
	       
	        Log.i(this.getClass().getCanonicalName(), "UnBinding from Services");

	        // Detach our existing connection.
	        mContext.unbindService(this);
	        mIsBound = false;	       
	    }
	}
}
