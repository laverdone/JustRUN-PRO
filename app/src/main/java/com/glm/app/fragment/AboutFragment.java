package com.glm.app.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;

import com.glm.app.db.Database;
import com.glm.trainer.R;


/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class AboutFragment extends Fragment {
	private View rootView;
	private Context mContext;
	private Button btnChangeLog;
	private WebView owvChangeLog;
	private Database oDBDatabase;
	/**
	 * 
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	public AboutFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext=getActivity().getApplicationContext();
		rootView = inflater.inflate(R.layout.about,
				container, false);
		/*TextView dummyTextView = (TextView) rootView
				.findViewById(R.id.section_label);
		dummyTextView.setText(Integer.toString(getArguments().getInt(
				ARG_SECTION_NUMBER)));*/
		 Button oRateButton = (Button) rootView.findViewById(R.id.btnRate);
	     //   owvChangeLog  = (WebView) rootView.findViewById(R.id.wvChangeLog);
	        
		    btnChangeLog = (Button) rootView.findViewById(R.id.btnChangeLog);
	        PackageInfo manager;
			try {
				manager = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
				if(oDBDatabase!=null) btnChangeLog.setText(mContext.getString(R.string.changelog)+" "+manager.versionName);
				btnChangeLog.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						 final Dialog dialog = new Dialog(getActivity());
					        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					        dialog.setContentView(R.layout.new_changelog_full);
					        Window window = dialog.getWindow();
					        window.setLayout(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
					        owvChangeLog = (WebView) dialog.findViewById(R.id.wvChangeLog);
					        owvChangeLog.loadUrl("file:///android_asset/changelog/us_US.html");
					        dialog.setOnKeyListener(new OnKeyListener() {
								
								@Override
								public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
									Log.v(this.getClass().getCanonicalName(),"dismiss "+keyCode);
									return false;
								}
							});
					        dialog.show();
					}
				});
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	    
	        
	        
	        oRateButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + mContext.getPackageName()));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
					/*PackageInfo manager;
					try {
						
						manager = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
						//startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.giano-solutions.com")));
					    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
					    emailIntent .setType("message/rfc822") ;
					    emailIntent .putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getString(R.string.email)});
					    emailIntent .putExtra(android.content.Intent.EXTRA_SUBJECT, "Issue Android - Trainer for Android Version "+manager.versionName);
					    emailIntent .putExtra(android.content.Intent.EXTRA_TEXT, "");
					    //startActivityForResult(emailIntent,1);
					    startActivity(Intent.createChooser(emailIntent, "Seleziona l'applicazione email."));
					} catch (NameNotFoundException e) {
						Log.e(this.getClass().getCanonicalName(),"Error getting version");
					}*/
				}
			});
	        //TEST BLUETOOCH	      		
	   		   //Log.i("oBTHelper","Cont: "+oBTHelper.getaDeviceAddress().size());
	   		   //Log.i("oBTHelper","Cont: "+oBTHelper.getaDeviceAddress().get(0));
	   		   //Abilitale la connessione al cardio.
	   		//oBTHelper.connect(device)   
	      //  loadChangeLog();            
	        
		return rootView;
	}

	public void setContext(Context context) {
		mContext=context;
		oDBDatabase = new Database(mContext);
		
	}
	
	/**
	 * Finestra di changelog
	 * 
	 * */
	private void loadChangeLog() {
	    owvChangeLog.loadUrl("file:///android_asset/changelog/us_US.html");
	        
	}
    
}