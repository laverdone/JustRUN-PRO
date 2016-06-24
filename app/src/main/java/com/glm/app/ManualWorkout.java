package com.glm.app;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.glm.bean.ConfigTrainer;
import com.glm.bean.User;
import com.glm.trainer.R;
import com.glm.utils.ExerciseUtils;

import java.text.NumberFormat;

import static com.glm.trainer.R.animator.*;

public class ManualWorkout extends Activity implements OnClickListener, OnFocusChangeListener{
	/**pulsante torna alla lista esercizi*/
	private Button oBtn_SaveShare;
	
	/**pulsante graph*/
	//private Button oBtn_Graph;
	
	private TimePicker oTimeManual;
	
	private EditText oTxt_Weight; 
	
	private EditText oTxt_Distance;
	
	private EditText oTxt_Kalories;
	
	private EditText oTxt_Speed;
	
	private EditText oTxt_Note;
	
	private Button oBtnSAVE;
	
	private Button oBtnCANCEL;
	
	private LinearLayout oMainLinearLayout;
	
	private ConfigTrainer oConfigTrainer;
	
	private User mUser=null;
	
	private Animation a;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.new_manual_workout);
        
        a = AnimationUtils.loadAnimation(this, fadein);
        a.reset();
        
        oMainLinearLayout = (LinearLayout) findViewById(R.id.main_layout); 
        
        oBtnSAVE 	  	= 	(Button) findViewById(R.id.btnSave);
        oBtnCANCEL    	= 	(Button) findViewById(R.id.btnCancel);
        
        oTimeManual = ((TimePicker) findViewById(R.id.timeManual));       	    				
        oTimeManual.setIs24HourView(true);
        oTimeManual.setCurrentHour(0);
        oTimeManual.setCurrentMinute(0);
        
        oTxt_Weight 	=	(EditText) findViewById(R.id.txtWeight);
        oTxt_Distance	=	(EditText) findViewById(R.id.txtDistance);
        oTxt_Kalories	=	(EditText) findViewById(R.id.txtKalories);
        oTxt_Speed		=	(EditText) findViewById(R.id.txtSpeed);
        oTxt_Note		=	(EditText) findViewById(R.id.txtNote);
        
        oBtnSAVE.setOnClickListener(this);
        oBtnCANCEL.setOnClickListener(this);
        
        oTxt_Distance.setOnFocusChangeListener(this);
        
        oConfigTrainer = ExerciseUtils.loadConfiguration(this);
        mUser=ExerciseUtils.loadUserDectails(this);
        oTxt_Weight.setText(String.valueOf(mUser.iWeight));
        oMainLinearLayout.clearAnimation();
        oMainLinearLayout.setAnimation(a);        
	}
	
	@Override
	public void onClick(View oObj) {
		int mType=0;
		if(oObj.getId()==R.id.btnSave){			
			Bundle extras = getIntent().getExtras();
			if(extras !=null){
				mType = extras.getInt("type");
			}
			
			int Distance=0;
			double Kalories=0;
			try{
				Distance=Integer.parseInt(oTxt_Distance.getText().toString());
				Kalories=Double.parseDouble(oTxt_Kalories.getText().toString().replace(",", "."));
			}catch (NumberFormatException e) {
				Log.e(this.getClass().getCanonicalName(),"Error NumberFormatException Manual Workout");
			}
			if(ExerciseUtils.addManualWorkout(oTimeManual.getCurrentHour().toString(),
					oTimeManual.getCurrentMinute().toString(),
					oTxt_Weight.getText().toString(),
					Distance,
					oTxt_Kalories.getText().toString(),Kalories,
					oTxt_Speed.getText().toString(),oTxt_Note.getText().toString(),mType, getApplicationContext(),oConfigTrainer)){
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), NewMainActivity.class);
				startActivity(intent);
				finish();
			}else{
				Toast.makeText(getBaseContext(), getApplicationContext().getString(R.string.generic_error), Toast.LENGTH_SHORT)
				.show();
			}
				
		}else if(oObj.getId()==R.id.btnCancel){
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), NewMainActivity.class);
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

	@Override
	public void onFocusChange(View oObj, boolean hasFocus) {

		if(oObj.getId()==R.id.txtDistance){	
			//
			if(!hasFocus){
				try{
					String sKalories = ExerciseUtils.getKaloriesBurn(oConfigTrainer, Float.valueOf(oTxt_Distance.getText().toString()));
					double dSpeed=0.0;
					dSpeed=Double.valueOf(oTxt_Distance.getText().toString())/
							(Double.valueOf(oTimeManual.getCurrentHour())+(Double.valueOf(oTimeManual.getCurrentMinute())/60));
					Log.d(this.getClass().getCanonicalName(), "CALCULATE Kalories "+sKalories+ " and Speed");
					
					oTxt_Kalories.setText(sKalories);
					NumberFormat oNFormat = NumberFormat.getNumberInstance();
					oNFormat.setMaximumFractionDigits(2);
					oTxt_Speed.setText(oNFormat.format(dSpeed));
				}catch (NumberFormatException ex){
					Log.d(this.getClass().getCanonicalName(), "Error CALCULATE Kalories and Speed "+ex.getMessage());
					
				}
				
				//ExerciseUtils.getPace(oContext, oConfigTrainer)
			}
			
			
		}
		
	}
}
