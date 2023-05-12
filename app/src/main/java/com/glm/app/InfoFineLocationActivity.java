package com.glm.app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;

import com.glm.trainer.R;

import org.twinone.androidwizard.WizardActivity;
import org.twinone.androidwizard.fragments.TextWizardFragment;
import org.twinone.androidwizard.fragments.WelcomeWizardFragment;


public class InfoFineLocationActivity extends WizardActivity {

	private Context mContext;

	private Button btnAccept;
	private Button btnReject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext=this;

		WelcomeWizardFragment.newInstance(
				getString(R.string.app_name_pro),
				getString(R.string.info_fine_location),
				"Tap next to continue",
				R.drawable.icon_pro
		).addTo(this);

		// LOCATION GRANT PERMISSION
		//Remove for new policy Manifest.permission.ACCESS_BACKGROUND_LOCATION

		PermissionsWizardFragment1.newInstance(
				"Access Fine Location to record yours workouts", "This is a very useful string","test",R.drawable.icon_pro,
				new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}
				).addTo(this);

		// WRITE_EXTERNAL_STORAGE
		PermissionsWizardFragment2.newInstance(
				"Write external storage to save yours workout and export data", "This is a very useful string","test",R.drawable.icon_pro,
				new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
		).addTo(this);

		// READ_PHONE_STATE
		PermissionsWizardFragment3.newInstance(
				"Read phone state for autopause workouts on incoming call", "This is a very useful string","test",R.drawable.icon_pro,
				new String[]{Manifest.permission.READ_PHONE_STATE}
		).addTo(this);

		TextWizardFragment.newInstance(
				"Almost done", "Click to Continue"
		).addTo(this);

		Log.i(this.getClass().getCanonicalName(), "getSelectedPage(): "+getSelectedPage());
		if(getSelectedPage()==5){
			Log.i(this.getClass().getCanonicalName(), "Error load Config");
		}
	    /*Bundle extras = getIntent().getExtras();
		setContentView(R.layout.info_fine_location);
		btnAccept		= (Button) findViewById(R.id.btnAccept);
		btnAccept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					if (ActivityCompat.checkSelfPermission(mContext,
							Manifest.permission.ACCESS_FINE_LOCATION)
							!= PackageManager.PERMISSION_GRANTED) {

						ActivityCompat.requestPermissions((Activity) mContext,
								new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE},
								ConstApp.PERMISSION_WRITE_EXTERNAL_STORAGE_CODE);


					}else if (ActivityCompat.checkSelfPermission(mContext,
							Manifest.permission.WRITE_EXTERNAL_STORAGE)
							!= PackageManager.PERMISSION_GRANTED) {

						ActivityCompat.requestPermissions((Activity)mContext,
								new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE},
								ConstApp.PERMISSION_WRITE_EXTERNAL_STORAGE_CODE);

					}else{
						accepted();
					}
				}

			}
		});
		btnReject		= (Button) findViewById(R.id.btnReject);

		btnReject.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});*/
	}

	@Override
	protected void onPause() {
		Log.i(this.getClass().getCanonicalName(), "getSelectedPage(): "+getSelectedPage()+" getCount(); "+getCount());
		if(getSelectedPage()==getCount()-1){
			accepted();
		}
		super.onPause();
	}

	private void accepted(){
		Intent intent=new Intent();
		intent.putExtra("first_launch", true);
		intent.putExtra("pref_type",4);
		intent.setClass(getApplicationContext(), PreferencesActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
	}
	@Override
	public void onBackPressed() {

		finish();	
	}    

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}



}