package com.glm.app;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.RemoteException;
import android.os.StrictMode;
import android.provider.Settings;
import androidx.annotation.NonNull;
//import android.support.design.widget.TabLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.glm.app.db.Database;
import com.glm.app.fragment.AboutFragment;
import com.glm.app.fragment.SummaryFragment;
import com.glm.app.fragment.WorkoutFragment;
import com.glm.bean.ConfigTrainer;
import com.glm.bean.User;
import com.glm.trainer.R;
import com.glm.utils.ExerciseUtils;
import com.glm.utils.Rate;
import com.glm.utils.TrainerServiceConnection;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.tabs.TabLayout;
//import com.google.android.gcm.GCMRegistrar;
//import com.google.android.gms.drive.Permission;

import java.util.Locale;

//import static com.glm.trainer.R.animator.*;

public class NewMainActivity extends FragmentActivity {
	public ConfigTrainer oConfigTrainer=null;
	public User mUser=null;
	private ProgressDialog oWaitForLoad=null;
	private String sGCMId="";
	private static final String SENDER_ID = "558307532040";
	private Animation a;

	private TelephonyManager oPhone;
	private boolean isLicence=false;
    private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsFrtouMzttS7hakJi3uuswjeGiRvLTKtx37kcg2QIQyamJtFsKtEiLQzhcnR/2p5ng98Jg0BLSvzL0VbZSHyWSoR4/RMuYWYhMsvR6MNjry/ABqFaAlsiFCNNfVGEvKV0Kz8ILNMbZA2XlZBviC0fht9BSSynPWTuvT8uHcl0yJY3GmKgyOzodYuCVtqG9pRew+ZstVEeiYcqYbm8Gd0LJVpXkIN1AB9iPlxsfEQWHYzFhwUNB9UR2VcuTEiKyFKmbCPrYGfHkss+Kbjd/mucZx0sWQITUjKSdK9tmMOql/yDXEjuT+PzgUmr1bmnRJgzYzNkpvWbNiIFrgojYAunwIDAQAB";
    private static final byte[] SALT = new byte[] {
        -46, 65, 30, -128, -103, -57, 74, -64, 51, 88, -95,
        -45, 77, -117, -36, -113, -11, 32, -64, 89
        };
	
    /**
	 * The {@link PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;

	private WorkoutFragment oWorkout=null;
	private SummaryFragment oSummary=null;
	//private StoreFragment oStore=null;
	private AboutFragment oAbout=null;
	public TrainerServiceConnection mConnection=null;
	private LocationManager LocationManager = null;
	private int mSectionFocus=0;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		mSectionFocus=intent.getIntExtra("sectionFocus",0);
		mContext=this;

		MobileAds.initialize(this, new OnInitializationCompleteListener() {
			@Override
			public void onInitializationComplete(InitializationStatus initializationStatus) {
			}
		});

		if(mConnection==null) mConnection = new TrainerServiceConnection(this,this.getClass().getCanonicalName());
        if (false) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects() //or .detectAll() for all detectable problems
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
		setContentView(R.layout.activity_new_main);


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
		/*Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			setActionBar(toolbar);
			toolbar.inflateMenu(R.menu.new_main);
			toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					return true;
				}
			});
		}*/

		oSummary=new SummaryFragment();
		oWorkout= new WorkoutFragment();
		//oStore =new StoreFragment();
		oAbout = new AboutFragment();
		
        new Thread(new Runnable() {
			@Override
			public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Rate.app_launched(mContext);
						}
				});
			}
		}).start();
        super.onCreate(savedInstanceState);
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

		super.onResume();
		//WorkTaskAsync oTask = new WorkTaskAsync();
		//oTask.execute();
		//oWaitForLoad = ProgressDialog.show(NewMainActivity.this, getString(R.string.app_name_buy), getString(R.string.please_wait));
		new Thread(new WorkTaskThread()).start();
	}
	@Override
    public void onBackPressed() {
		AlertDialog alertDialog;
    	alertDialog = new AlertDialog.Builder(this).create();
    	alertDialog.setTitle(this.getString(R.string.titleendapp));
    	alertDialog.setMessage(this.getString(R.string.messageendapp));
    	alertDialog.setButton(alertDialog.BUTTON_POSITIVE,this.getString(R.string.yes), new android.content.DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(mConnection!=null && mConnection.mIService!=null){
					try {
						mConnection.mIService.stopGPSFix();
						mConnection.mIService.shutDown();
						mConnection.destroy();
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						//Log.e(this.getClass().getCanonicalName(),e.getMessage());
					}
				}
				finish();
			}        				
    		});
    	
    	alertDialog.setButton(alertDialog.BUTTON_NEGATIVE,this.getString(R.string.no), new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {

			}

		});
    	alertDialog.show();
    }    
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.new_main, menu);
		return true;
	}

	/*@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		//mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}*/

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			
			switch(position) {
			case 0:
				//WORKOUT
				oWorkout.setContext(NewMainActivity.this);
				return oWorkout;
			case 1:
				//SUMMARY
				oSummary.setContext(NewMainActivity.this);
				return oSummary;
			/*case 2:
				//STORE
				
				oStore.setContext(NewMainActivity.this);
				return oStore;*/
			case 2:
				//ABOUT
				
				oAbout.setContext(NewMainActivity.this);
				return oAbout;
			}
			
			return null;
		}
		
		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			/*case 2:
				return getString(R.string.title_section4).toUpperCase(l);*/
			case 2:
				return getString(R.string.title_section5).toUpperCase(l);
			}
			return null;
		}
	}
	
	@Override
	protected void onDestroy() {
		if(mHandleMessageReceiver!=null){
			try{
				 unregisterReceiver(mHandleMessageReceiver);   
			}catch (IllegalArgumentException e) {
				 Log.e(this.getClass().getCanonicalName(), "Receiver not registered error");
			}
		}    
		if(mConnection!=null) mConnection.destroy();
		mConnection=null;
		super.onDestroy();
	}
	@Override
	protected void onPause() {
		if(mHandleMessageReceiver!=null){
			try{
				 unregisterReceiver(mHandleMessageReceiver);   
			}catch (IllegalArgumentException e) {
				 Log.e(this.getClass().getCanonicalName(), "Receiver not registered error");
			}
		}    
		if(mConnection!=null) mConnection.destroy();
		mConnection=null;
		a = AnimationUtils.loadAnimation(this, R.anim.disappear);
		a.reset();

		if(mViewPager!=null) {
			mViewPager.clearAnimation();
			mViewPager.setAnimation(a);
		}
		super.onPause();
	}
	
	private final BroadcastReceiver mHandleMessageReceiver =
	         new BroadcastReceiver() {
		     @Override
		     public void onReceive(Context context, Intent intent) {
		         Log.v(this.getClass().getCanonicalName(),"onReceive");
		     }
	};

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
	    	alertDialog.setButton(this.getString(R.string.yes), new android.content.DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
				    startActivity(myIntent);
				}        				
	    		});
	    	
	    	alertDialog.setButton2(this.getString(R.string.no), new android.content.DialogInterface.OnClickListener(){

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
	
	
	class WorkTaskThread implements Runnable{

		@Override
		public void run() {
			Looper.prepare();
		
			Database oDB=new Database(getApplicationContext());
		
			if(oDB!=null)  {
				oDB.init();
			}
			
			//Log.v(this.getClass().getCanonicalName(),"doInBackground WorkTaskAsync...");
			oConfigTrainer=ExerciseUtils.loadConfiguration(getApplicationContext());
			mUser = ExerciseUtils.loadUserDectails(getApplicationContext());
			oSummary.oTable=ExerciseUtils.getDistanceForType(oConfigTrainer, getApplicationContext());

			//oSummary.oChart = new BarChart(getApplicationContext(),0,false);
			/*if(oConfigTrainer.getsGCMId().compareToIgnoreCase("")==0){
				//GCM GOOGLE
				GCMRegistrar.checkDevice(getApplicationContext());
			    GCMRegistrar.checkManifest(getApplicationContext());
			    sGCMId = GCMRegistrar.getRegistrationId(getApplicationContext());
			    registerReceiver(mHandleMessageReceiver,
			                new IntentFilter("com.glm.app.DISPLAY_MESSAGE"));	       
			       
			       if (sGCMId.equals("")) {
			         GCMRegistrar.register(getApplicationContext(), SENDER_ID);
			         Log.v(this.getClass().getCanonicalName(), "Not registered, register now: "+sGCMId);
			         ExerciseUtils.saveGCMId(getApplicationContext(),sGCMId);
			       } else {
			         Log.v(this.getClass().getCanonicalName(), "Already registered: "+sGCMId);	
			         ExerciseUtils.saveGCMId(getApplicationContext(),sGCMId);
			       }
			       
			       if(sGCMId!=null){
			    	   if(sGCMId.length()>0){
			    		   SharedPreferences oPrefs = getApplicationContext().getSharedPreferences("aTrainer",Context.MODE_MULTI_PROCESS);
			    		   SharedPreferences.Editor editPrefs = oPrefs.edit();
			    		   editPrefs.putString("GCMId", sGCMId); 
			    		   editPrefs.commit();
			    		   new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								//Send Id to Android Trainer WEB Server via POST METHOD
							 //      HttpClientHelper oHttpHelper = new HttpClientHelper();
							 //      oHttpHelper.registerToAndroidTrainerServer(sGCMId,oConfigTrainer);
								}
								    		   });
			    		   
			    	   }
			       }
			     //GCM GOOGLE
			}*/

			
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {




					mSectionsPagerAdapter = new SectionsPagerAdapter(
							getSupportFragmentManager());



					TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
					tabLayout.removeAllTabs();
					tabLayout.addTab(tabLayout.newTab().setText(mSectionsPagerAdapter.getPageTitle(0)));
					tabLayout.addTab(tabLayout.newTab().setText(mSectionsPagerAdapter.getPageTitle(1)));
					//tabLayout.addTab(tabLayout.newTab().setText(mSectionsPagerAdapter.getPageTitle(2)));
					tabLayout.addTab(tabLayout.newTab().setText(mSectionsPagerAdapter.getPageTitle(2)));
					tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

					// Set up the ViewPager with the sections adapter.
					mViewPager = (ViewPager) findViewById(R.id.pager);

					a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
					a.reset();

					mViewPager.clearAnimation();
					mViewPager.setAnimation(a);

					mViewPager.setAdapter(mSectionsPagerAdapter);
					mViewPager.setOffscreenPageLimit(3);

					mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
					tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
						@Override
						public void onTabSelected(TabLayout.Tab tab) {

							mViewPager.setCurrentItem(tab.getPosition());
						}

						@Override
						public void onTabUnselected(TabLayout.Tab tab) {

						}

						@Override
						public void onTabReselected(TabLayout.Tab tab) {

						}
					});

					mViewPager.setCurrentItem(mSectionFocus);
					if(oWaitForLoad!=null) oWaitForLoad.dismiss();
					LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
			    	if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {	        
				       ShowAlertNoGPS();
				    }
				}
				
			});
						
					
		}
		
	}
}
