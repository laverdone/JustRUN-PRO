package com.glm.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.glm.bean.ConfigTrainer;
import com.glm.trainer.R;
import com.glm.utils.ExerciseUtils;


public class PreferencesActivity extends AppCompatActivity {

	private Context mContext;

	/**Pulsanti +/- Weight*/
	private TextView oTxtWeight;
	/**Pulsanti +/- Weight*/

	/**Pulsanti +/- Age*/
	private TextView oTxtAge;
	/**Pulsanti +/- Age*/

	private TextView oTxtHeight;

	private TextView olblHeight;
	private TextView olblWeight;

	private EditText oEdtNick;
	private EditText oEdtName;

	private RadioButton RBMale;
	private RadioButton RBFemale;

	private Button btnSave;
	private ConfigTrainer oConfigTrainer;
	private boolean isUserExist=false;




	private RelativeLayout lExternalPlayer=null;
	private Spinner mSpinnerUnit=null;
	private Spinner mSpinnerAutoPause=null;
	private Spinner mSpinnerTimeNotification=null;
	private CheckBox checkMap			= null;
	private CheckBox checkNotification 	= null;
	private CheckBox checkMusic			= null;
	private CheckBox checkExternalPlayer= null;
	private CheckBox checkWeightTrack	= null;
	private CheckBox checkTarget		= null;
	private CheckBox checkCardio		= null;
	private CheckBox checkMontivator	= null;
	private CheckBox checkDistance 		= null;
	private CheckBox checkTime			= null;
	private CheckBox checkKalories		= null;
	private CheckBox checkPace			= null;
	private CheckBox checkInclination	= null;
	
	
	private boolean isFirstLaunch = false;
	private int iPrefType=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext=this;
		
		
		Bundle extras = getIntent().getExtras();
		if(extras !=null){
			isFirstLaunch = extras.getBoolean("first_launch");
			iPrefType	  = extras.getInt("pref_type");
		}    
		String title="";

		if(iPrefType==1){
			setContentView(R.layout.trainer_preferences);
			title=getString(R.string.show_motivator);
			loadPref(1);
			AnotherPrefAsyncTask oPrefTask = new AnotherPrefAsyncTask();
			oPrefTask.execute();
		}
		if(iPrefType==2){
			setContentView(R.layout.notification_preferences);
			title=getString(R.string.pref_title);
			loadPref(2);
			AnotherPrefAsyncTask oPrefTask = new AnotherPrefAsyncTask();
			oPrefTask.execute();
		}
		if(iPrefType==3){
			setContentView(R.layout.general_preferences);
			title=getString(R.string.general_pref);
			loadPref(3);
			AnotherPrefAsyncTask oPrefTask = new AnotherPrefAsyncTask();
			oPrefTask.execute();
		}

		if(iPrefType==4){
			setContentView(R.layout.new_user_details);
			title=getString(R.string.user);
			loadPref(4);

			PrefAsyncTask oPrefTask = new PrefAsyncTask();
			oPrefTask.execute();

		}
		Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
		myToolbar.setTitle(title);
		myToolbar.setLogo(R.drawable.left);
		setSupportActionBar(myToolbar);

	}

	private void loadPref(int prefType) {
		if(prefType==2){

			checkMap			= (CheckBox) findViewById(R.id.checkMap);
			checkNotification 	= (CheckBox) findViewById(R.id.checkNotification);
			checkMusic			= (CheckBox) findViewById(R.id.checkMusic);
			checkExternalPlayer = (CheckBox) findViewById(R.id.checkExternalPlayer);
			lExternalPlayer 	= (RelativeLayout) findViewById(R.id.externalplayer);

			checkMap.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(final View v) {
					AsyncTask.execute(new Runnable() {

						@Override
						public void run() {
							SharedPreferences oPrefs = mContext.getSharedPreferences("aTrainer",Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
							SharedPreferences.Editor editPrefs = oPrefs.edit();
							editPrefs.putBoolean("maps", ((CheckBox) v).isChecked());
							editPrefs.commit();
						}
					});

				}
			});
			checkNotification.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(final View v) {
					AsyncTask.execute(new Runnable() {

						@Override
						public void run() {
							SharedPreferences oPrefs = mContext.getSharedPreferences("aTrainer",Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
							SharedPreferences.Editor editPrefs = oPrefs.edit();
							editPrefs.putBoolean("notification", ((CheckBox) v).isChecked());
							editPrefs.commit();
						}
					});

				}
			});
			checkMusic.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(final View v) {
					AsyncTask.execute(new Runnable() {

						@Override
						public void run() {
							SharedPreferences oPrefs = mContext.getSharedPreferences("aTrainer",Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
							SharedPreferences.Editor editPrefs = oPrefs.edit();
							editPrefs.putBoolean("music", ((CheckBox) v).isChecked());
							editPrefs.commit();
						}
					});
					if(checkMusic.isChecked()){
						lExternalPlayer.setVisibility(View.VISIBLE);
						AsyncTask.execute(new Runnable() {

							@Override
							public void run() {
								SharedPreferences oPrefs = mContext.getSharedPreferences("aTrainer",Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
								SharedPreferences.Editor editPrefs = oPrefs.edit();
								editPrefs.putBoolean("externalPlayer", ((CheckBox) v).isChecked());
								editPrefs.commit();
							}
						});
					}else{
						lExternalPlayer.setVisibility(View.GONE);
						AsyncTask.execute(new Runnable() {

							@Override
							public void run() {
								SharedPreferences oPrefs = mContext.getSharedPreferences("aTrainer",Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
								SharedPreferences.Editor editPrefs = oPrefs.edit();
								editPrefs.putBoolean("externalPlayer", false);
								editPrefs.commit();
							}
						});
					}
				}
			});
			checkExternalPlayer.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(final View v) {
					AsyncTask.execute(new Runnable() {

						@Override
						public void run() {
							SharedPreferences oPrefs = mContext.getSharedPreferences("aTrainer",Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
							SharedPreferences.Editor editPrefs = oPrefs.edit();
							editPrefs.putBoolean("externalPlayer", ((CheckBox) v).isChecked());
							editPrefs.commit();
						}
					});

				}
			});
		}else if(prefType==3){


			mSpinnerAutoPause = (Spinner) findViewById(R.id.spinnerAutoPause);

			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					mContext, R.array.autopausetime, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mSpinnerAutoPause.setAdapter(adapter);
			//Imposto il valore salvato nel DB
			//mSpinnerAutoPause.setSelection(Integer.parseInt(sValue));
			mSpinnerAutoPause.setOnItemSelectedListener(new  AdapterView.OnItemSelectedListener(){
				@Override
				public void onItemSelected(
						AdapterView<?> arg0, View v,
						final int iPos, long arg3) {
					// TODO Auto-generated method stub
					//Log.i(this.getClass().getCanonicalName(),"Selected Item: "+mSpinnerAutoPause.getSelectedItem().toString());
					AsyncTask.execute(new Runnable() {

						@Override
						public void run() {
							SharedPreferences oPrefs = mContext.getSharedPreferences("aTrainer",Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
							SharedPreferences.Editor editPrefs = oPrefs.edit();
							editPrefs.putInt("autopause_time", iPos);
							editPrefs.commit();
							//Log.v(this.getClass().getCanonicalName(),"auto Pause pos is:" +iPos);
						}
					});
				}

				@Override
				public void onNothingSelected(
						AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});

			mSpinnerUnit = (Spinner) findViewById(R.id.spinnerUnit);

			adapter = ArrayAdapter.createFromResource(
					mContext, R.array.units, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mSpinnerUnit.setAdapter(adapter);

			mSpinnerUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
										   final int iPos, long arg3) {
					AsyncTask.execute(new Runnable() {

						@Override
						public void run() {
							SharedPreferences oPrefs = mContext.getSharedPreferences("aTrainer",Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
							SharedPreferences.Editor editPrefs = oPrefs.edit();
							editPrefs.putInt("units", iPos);
							editPrefs.commit();
						}
					});

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});

			checkWeightTrack	= (CheckBox) findViewById(R.id.checkWeightTrack);
			checkTarget		= (CheckBox) findViewById(R.id.checkTarget);
			checkCardio		= (CheckBox) findViewById(R.id.checkCardio);
			checkWeightTrack.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(final View v) {
					AsyncTask.execute(new Runnable() {
						@Override
						public void run() {
							SharedPreferences oPrefs = mContext.getSharedPreferences("aTrainer",Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
							SharedPreferences.Editor editPrefs = oPrefs.edit();
							editPrefs.putBoolean("track_exercise", ((CheckBox) v).isChecked());
							editPrefs.commit();
						}
					});

				}
			});
			checkTarget.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(final View v) {
					AsyncTask.execute(new Runnable() {

						@Override
						public void run() {
							SharedPreferences oPrefs = mContext.getSharedPreferences("aTrainer",Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
							SharedPreferences.Editor editPrefs = oPrefs.edit();
							editPrefs.putBoolean("run_goal", ((CheckBox) v).isChecked());
							editPrefs.commit();
						}
					});

				}
			});
			checkCardio.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(final View v) {
					AsyncTask.execute(new Runnable() {

						@Override
						public void run() {
							SharedPreferences oPrefs = mContext.getSharedPreferences("aTrainer",Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
							SharedPreferences.Editor editPrefs = oPrefs.edit();
							editPrefs.putBoolean("use_cardio", ((CheckBox) v).isChecked());
							editPrefs.commit();
						}
					});

				}
			});


		}else if(prefType==1){
			

			mSpinnerTimeNotification = (Spinner) findViewById(R.id.spinnerTimeNotification);

			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
					mContext, R.array.motivator_time, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mSpinnerTimeNotification.setAdapter(adapter);
			//Imposto il valore salvato nel DB
			//mSpinnerAutoPause.setSelection(Integer.parseInt(sValue));
			mSpinnerTimeNotification.setOnItemSelectedListener(new  AdapterView.OnItemSelectedListener(){

				@Override
				public void onItemSelected(
						AdapterView<?> arg0, View v,
						final int iPos, long arg3) {
					// TODO Auto-generated method stub
					AsyncTask.execute(new Runnable() {

						@Override
						public void run() {
							AsyncTask.execute(new Runnable() {

								@Override
								public void run() {
									SharedPreferences oPrefs = mContext.getSharedPreferences("aTrainer",Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
									SharedPreferences.Editor editPrefs = oPrefs.edit();
									editPrefs.putInt("motivator_time", iPos);
									editPrefs.commit();
								}
							});
						}
					});
				}

				@Override
				public void onNothingSelected(
						AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});

			checkMontivator		= (CheckBox) findViewById(R.id.checkMontivator);
			checkDistance 		= (CheckBox) findViewById(R.id.checkDistance);
			checkTime			= (CheckBox) findViewById(R.id.checkTime);
			checkKalories		= (CheckBox) findViewById(R.id.checkKalories);
			checkPace			= (CheckBox) findViewById(R.id.checkPace);
			checkInclination	= (CheckBox) findViewById(R.id.checkInclination);

			checkMontivator.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(final View v) {
					AsyncTask.execute(new Runnable() {

						@Override
						public void run() {
							SharedPreferences oPrefs = mContext.getSharedPreferences("aTrainer",Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
							SharedPreferences.Editor editPrefs = oPrefs.edit();
							editPrefs.putBoolean("motivator", ((CheckBox) v).isChecked());
							editPrefs.commit();
						}
					});

				}
			});

			checkDistance.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(final View v) {
					AsyncTask.execute(new Runnable() {

						@Override
						public void run() {
							SharedPreferences oPrefs = mContext.getSharedPreferences("aTrainer",Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
							SharedPreferences.Editor editPrefs = oPrefs.edit();
							editPrefs.putBoolean("say_distance", ((CheckBox) v).isChecked());
							editPrefs.commit();
						}
					});

				}
			});
			checkTime.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(final View v) {
					AsyncTask.execute(new Runnable() {

						@Override
						public void run() {
							SharedPreferences oPrefs = mContext.getSharedPreferences("aTrainer",Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
							SharedPreferences.Editor editPrefs = oPrefs.edit();
							editPrefs.putBoolean("say_time", ((CheckBox) v).isChecked());
							editPrefs.commit();
						}
					});

				}
			});
			checkKalories.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(final View v) {
					AsyncTask.execute(new Runnable() {

						@Override
						public void run() {
							SharedPreferences oPrefs = mContext.getSharedPreferences("aTrainer",Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
							SharedPreferences.Editor editPrefs = oPrefs.edit();
							editPrefs.putBoolean("say_kalories", ((CheckBox) v).isChecked());
							editPrefs.commit();
						}
					});
				}
			});
			checkPace.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(final View v) {
					AsyncTask.execute(new Runnable() {

						@Override
						public void run() {
							SharedPreferences oPrefs = mContext.getSharedPreferences("aTrainer",Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
							SharedPreferences.Editor editPrefs = oPrefs.edit();
							editPrefs.putBoolean("say_pace", ((CheckBox) v).isChecked());
							editPrefs.commit();
						}
					});

				}
			});
			checkInclination.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(final View v) {
					AsyncTask.execute(new Runnable() {

						@Override
						public void run() {
							SharedPreferences oPrefs = mContext.getSharedPreferences("aTrainer",Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
							SharedPreferences.Editor editPrefs = oPrefs.edit();
							editPrefs.putBoolean("say_inclination", ((CheckBox) v).isChecked());
							editPrefs.commit();
						}
					});

				}
			});

		}else{
			oTxtWeight  = (TextView) findViewById(R.id.txtWeight);
			oTxtAge		= (TextView) findViewById(R.id.txtAge);
			oTxtHeight	= (TextView) findViewById(R.id.txtHeight);

			olblWeight  = (TextView) findViewById(R.id.lblWeight);
			olblHeight	= (TextView) findViewById(R.id.lblHeight);

			oEdtNick	= (EditText) findViewById(R.id.editNick);
			oEdtName	= (EditText) findViewById(R.id.editName);

			RBMale		= (RadioButton) findViewById(R.id.radioGenderM);
			RBFemale	= (RadioButton) findViewById(R.id.radioGenderF);

			btnSave = (Button) findViewById(R.id.btnSave);
			btnSave.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					saveUser();
					Intent intent = new Intent();
					intent.setClass(mContext, NewMainActivity.class);
					startActivity(intent);
					finish();
				}
			});

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		
	}
	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), NewMainActivity.class);
		startActivity(intent);
		finish();	
	}    

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
/*
	*//**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 *//*
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
				if(isFirstLaunch){
					//USER
					oUser =new UserFragment();
					oUser.setContext(PreferencesActivity.this);
					return oUser;
				}else{
					if(iPrefType==1){
						//Notification PREFERENCES
						oPreferences =new PreferencesFragment();
						oPreferences.setContext(PreferencesActivity.this,0);
						return oPreferences;
					}else if(iPrefType==2){
						//General PREFERENCES
						oPreferences =new PreferencesFragment();
						oPreferences.setContext(PreferencesActivity.this,1);
						return oPreferences;
					}else if(iPrefType==3){
						//Trainer PREFERENCES
						oPreferences =new PreferencesFragment();
						oPreferences.setContext(PreferencesActivity.this,2);
						return oPreferences;
					}else if(iPrefType==4){
						//USER
						oUser =new UserFragment();
						oUser.setContext(PreferencesActivity.this);
						return oUser;
					}else {
						//Notification PREFERENCES
						oPreferences = new PreferencesFragment();
						oPreferences.setContext(PreferencesActivity.this, 0);
						return oPreferences;
					}
				}
			case 1:
				//General PREFERENCES
				oPreferences =new PreferencesFragment();
				oPreferences.setContext(PreferencesActivity.this,position);
				return oPreferences;
			case 2:
				//Trainer PREFERENCES
				oPreferences =new PreferencesFragment();
				oPreferences.setContext(PreferencesActivity.this,position);
				return oPreferences;	
			case 3:
				//USER
				oUser =new UserFragment();
				oUser.setContext(PreferencesActivity.this);
				return oUser;
			}

			return null;
		}

		@Override
		public int getCount() {
			if(isFirstLaunch || iPrefType>0) return 1;
			else return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				if(isFirstLaunch) return getString(R.string.user).toUpperCase(l);
				else return getString(R.string.pre).toUpperCase(l);
			case 1:
				return getString(R.string.general_pref).toUpperCase(l);
			case 2:
				return getString(R.string.show_motivator).toUpperCase(l);
			case 3:
				return getString(R.string.user).toUpperCase(l);
			}
			return null;
		}
	}*/

	private boolean saveUser() {
		if(RBMale.isChecked()){
			try{
				Integer.parseInt(oTxtAge.getText().toString());
			}catch (NumberFormatException e) {
				Toast.makeText(mContext,
						getString(R.string.age_error),
						Toast.LENGTH_SHORT).show();
				return false;
			}
			try{
				Float.parseFloat(oTxtWeight.getText().toString());
			}catch (NumberFormatException e) {
				Toast.makeText(mContext,
						getString(R.string.weight_error),
						Toast.LENGTH_SHORT).show();
				return false;
			}
			if(ExerciseUtils.saveUserData(mContext, oEdtNick.getText().toString(), oEdtName.getText().toString(),
					oTxtWeight.getText().toString(), oTxtAge.getText().toString(), oTxtHeight.getText().toString(), false,
					false, false,"M")){

				return true;
			}

		}else{
			try{
				Integer.parseInt(oTxtAge.getText().toString());
			}catch (NumberFormatException e) {
				Toast.makeText(mContext,
						getString(R.string.age_error),
						Toast.LENGTH_SHORT).show();
				return false;
			}
			try{
				Float.parseFloat(oTxtWeight.getText().toString());
			}catch (NumberFormatException e) {
				Toast.makeText(mContext,
						getString(R.string.weight_error),
						Toast.LENGTH_SHORT).show();
				return false;
			}

			if(ExerciseUtils.saveUserData(mContext, oEdtNick.getText().toString(), oEdtName.getText().toString(),
					oTxtWeight.getText().toString(), oTxtAge.getText().toString(), oTxtHeight.getText().toString(), false,
					false, false,"F")){

				return true;
			}
		}
		return false;
	}

	class PrefAsyncTask extends AsyncTask{

		@Override
		protected Object doInBackground(Object... params) {
			oConfigTrainer = ExerciseUtils.loadConfiguration(mContext);
			isUserExist=ExerciseUtils.isUserExist(mContext);

			return true;
		}

		@Override
		protected void onPostExecute(Object result) {
			if(oConfigTrainer!=null){
				if(oConfigTrainer.getiUnits()==0){
					olblWeight.setText(olblWeight.getText()+" (Kg)");
					olblHeight.setText(olblHeight.getText()+" (cm)");
				}else{
					olblWeight.setText(olblWeight.getText()+" (Lbs)");
					olblHeight.setText(olblHeight.getText()+" (in)");
				}
			}

			oTxtWeight.setOnKeyListener(new View.OnKeyListener() {

				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {

					return false;
				}
			});

			oTxtWeight.setText(String.valueOf(oConfigTrainer.getiWeight()));
			oTxtHeight.setText(String.valueOf(oConfigTrainer.getiHeight()));
			oTxtAge.setText(String.valueOf(oConfigTrainer.getiAge()));

			oEdtNick.setText(oConfigTrainer.getsNick());
			oEdtName.setText(oConfigTrainer.getsName());

			if(oConfigTrainer.getsGender().compareToIgnoreCase("M")==0){
				RBMale.setChecked(true);
			}else{
				RBFemale.setChecked(true);
			}
			/*if(isUserExist){
				btnSave.setVisibility(View.GONE);
			}else{
				btnSave.setVisibility(View.VISIBLE);
			}*/
		}
	}

	class AnotherPrefAsyncTask extends AsyncTask{

		@Override
		protected Object doInBackground(Object... params) {
			oConfigTrainer = ExerciseUtils.loadConfiguration(mContext);

			return true;
		}

		@Override
		protected void onPostExecute(Object result) {
			if(checkMap!=null) checkMap.setChecked(oConfigTrainer.isbDisplayMap());
			if(checkNotification!=null) checkNotification.setChecked(oConfigTrainer.isbDisplayNotification());
			if(checkMusic!=null){
				checkMusic.setChecked(oConfigTrainer.isbPlayMusic());
				if(checkMusic.isChecked())
					lExternalPlayer.setVisibility(View.VISIBLE);
				else
					lExternalPlayer.setVisibility(View.GONE);
			}
			if(checkExternalPlayer!=null) checkExternalPlayer.setChecked(oConfigTrainer.isbUseExternalPlayer());



			if(mSpinnerUnit!=null) mSpinnerUnit.setSelection(oConfigTrainer.getiUnits());
			if(mSpinnerAutoPause!=null) mSpinnerAutoPause.setSelection(oConfigTrainer.getiAutoPauseTime());
			if(mSpinnerTimeNotification!=null) mSpinnerTimeNotification.setSelection(oConfigTrainer.getiMotivatorTime());

			if(checkWeightTrack!=null)  checkWeightTrack.setChecked(oConfigTrainer.isbTrackExercise());
			if(checkTarget!=null)  checkTarget.setChecked(oConfigTrainer.isbRunGoal());
			if(checkCardio!=null)  checkCardio.setChecked(oConfigTrainer.isbUseCardio());


			if(checkMontivator!=null) checkMontivator.setChecked(oConfigTrainer.isbMotivator());
			if(checkDistance!=null)  checkDistance.setChecked(oConfigTrainer.isSayDistance());
			if(checkTime!=null)  checkTime.setChecked(oConfigTrainer.isSayTime());
			if(checkKalories!=null)  checkKalories.setChecked(oConfigTrainer.isSayKalories());
			if(checkPace!=null)  checkPace.setChecked(oConfigTrainer.isSayPace());
			if(checkInclination!=null)  checkInclination.setChecked(oConfigTrainer.isSayInclination());
		}

	}
}