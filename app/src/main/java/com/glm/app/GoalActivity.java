package com.glm.app;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.glm.app.stopwatch.WorkOutActivity;
import com.glm.trainer.R;

public class GoalActivity extends Activity implements OnClickListener {
	private Button oRunButton;
	private Spinner oSpinner;
	private ArrayAdapter<CharSequence> adapter;
	private TimePicker oTimeGoal;
	private int mType=0;
	private CheckBox oCheckGoal;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/***    
		NotificationManager notificationManager = (NotificationManager)getSystemService(this.NOTIFICATION_SERVICE);

		int icon = R.drawable.trainer_notification_icon;
		CharSequence text = "Notification Text";
		CharSequence contentTitle = "Notification Title";
		CharSequence contentText = "Sample notification text.";
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon,text,when);						
		notificationManager.notify(APP_NOTIFICATION_ID, notification);
		 */
		setContentView(R.layout.new_goal_run);

		oRunButton = (Button) findViewById(R.id.btnRun);

		oSpinner = ((Spinner) findViewById(R.id.spDistance));       	    				
		adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.goal_distance_km, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);        	       	    	        
		oSpinner.setAdapter(adapter);

		oCheckGoal = ((CheckBox) findViewById(R.id.checkGoal));

		oTimeGoal = ((TimePicker) findViewById(R.id.timeGoal));       	    				
		oTimeGoal.setIs24HourView(true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			oTimeGoal.setHour(0);
			oTimeGoal.setMinute(0);
		}else{
			oTimeGoal.setCurrentHour(0);
			oTimeGoal.setCurrentMinute(0);
		}

		oRunButton.setOnClickListener(this);

		oCheckGoal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				AsyncTask.execute(new Runnable() {
					
					@Override
					public void run() {
						SharedPreferences oPrefs = getApplicationContext().getSharedPreferences("aTrainer",Context.MODE_MULTI_PROCESS);
						SharedPreferences.Editor editPrefs = oPrefs.edit();
					    editPrefs.putBoolean("run_goal", !((CheckBox) v).isChecked());    
					    editPrefs.commit();					    
					}
				});
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnRun) {
			Bundle extras = getIntent().getExtras();
			if(extras !=null){
				mType = extras.getInt("type");
			}
			Intent intent = new Intent();
			intent.setClass(this, WorkOutActivity.class);
			intent.putExtra("type", mType);
			intent.putExtra("goalDistance", oSpinner.getSelectedItemId());
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				intent.putExtra("goalHH", oTimeGoal.getHour());
				intent.putExtra("goalMM", oTimeGoal.getMinute());
			}else{
				intent.putExtra("goalHH", oTimeGoal.getCurrentHour());
				intent.putExtra("goalMM", oTimeGoal.getCurrentMinute());
			}
			startActivity(intent);
			finish();


		}
	}
	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), NewMainActivity.class);
		startActivity(intent);
		finish();    
	}
}
