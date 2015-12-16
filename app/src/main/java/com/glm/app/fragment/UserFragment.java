package com.glm.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.glm.bean.ConfigTrainer;
import com.glm.trainer.NewMainActivity;
import com.glm.trainer.R;
import com.glm.utils.ExerciseUtils;



/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class UserFragment extends Fragment {
	private View rootView;
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
	
	private CheckBox oCkFB;	
	//private CheckBox oCkGooglePlus;
	private CheckBox oCkTwitter;
	
	private Button btnSave;
	private ConfigTrainer oConfigTrainer;
	private boolean isUserExist=false;
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	public UserFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext=getActivity().getApplicationContext();

		rootView = inflater.inflate(R.layout.new_user_details,
				container, false);
		

        oTxtWeight  = (TextView) rootView.findViewById(R.id.txtWeight);
        oTxtAge		= (TextView) rootView.findViewById(R.id.txtAge);
        oTxtHeight	= (TextView) rootView.findViewById(R.id.txtHeight);
        
        olblWeight  = (TextView) rootView.findViewById(R.id.lblWeight);
        olblHeight	= (TextView) rootView.findViewById(R.id.lblHeight);
        
        oEdtNick	= (EditText) rootView.findViewById(R.id.editNick);
        oEdtName	= (EditText) rootView.findViewById(R.id.editName);
        
        RBMale		= (RadioButton) rootView.findViewById(R.id.radioGenderM);
        RBFemale	= (RadioButton) rootView.findViewById(R.id.radioGenderF);
    	
        oCkFB				= (CheckBox) rootView.findViewById(R.id.ckFB);       
    	//oCkGooglePlus		= (CheckBox) findViewById(R.id.ckGoogle);
    	oCkTwitter			= (CheckBox) rootView.findViewById(R.id.ckTwitter);
        btnSave = (Button) rootView.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveUser();
				Intent intent = new Intent();
				intent.setClass(mContext, NewMainActivity.class);
				startActivity(intent);
				getActivity().finish();	
			}
		});
        oCkFB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(final View v) {
				AsyncTask.execute(new Runnable() {
					
					@Override
					public void run() {
						SharedPreferences oPrefs = mContext.getSharedPreferences("aTrainer",Context.MODE_MULTI_PROCESS);
						SharedPreferences.Editor editPrefs = oPrefs.edit();
					    editPrefs.putBoolean("share_fb", ((CheckBox) v).isChecked());    
					    editPrefs.commit();					    
					}
				});				
			}
		});
    	//oCkGooglePlus.setOnClickListener(this);
    	oCkTwitter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(final View v) {
					AsyncTask.execute(new Runnable() {
					
					@Override
					public void run() {
						SharedPreferences oPrefs = mContext.getSharedPreferences("aTrainer",Context.MODE_MULTI_PROCESS);
						SharedPreferences.Editor editPrefs = oPrefs.edit();
					    editPrefs.putBoolean("share_twitter", ((CheckBox) v).isChecked());    
					    editPrefs.commit();					    
					}
				});			
			}
		});  	
    	
		
		PrefAsyncTask oPrefTask = new PrefAsyncTask();
		oPrefTask.execute();
		return rootView;
	}

	public void setContext(Context context) {
		mContext=context;
	}

	@Override
	public void onPause() {
		saveUser();
		Log.v(this.getClass().getCanonicalName(),"on Pause Save user");
		super.onPause();
	}
	
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
			Log.v(this.getClass().getCanonicalName(),"oCkTwitter.isChecked():"+oCkTwitter.isChecked()+" - oCkFB.isChecked():"+oCkFB.isChecked());
			if(ExerciseUtils.saveUserData(mContext, oEdtNick.getText().toString(), oEdtName.getText().toString(),
			        oTxtWeight.getText().toString(), oTxtAge.getText().toString(), oTxtHeight.getText().toString(), oCkFB.isChecked(), 
			    	false, oCkTwitter.isChecked(),"M")){
				  
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
				Integer.parseInt(oTxtWeight.getText().toString());
			}catch (NumberFormatException e) {
				 Toast.makeText(mContext, 
		                    getString(R.string.weight_error), 
		                    Toast.LENGTH_SHORT).show();
				 return false;
			}
			Log.v(this.getClass().getCanonicalName(),"oCkTwitter.isChecked():"+oCkTwitter.isChecked()+" - oCkFB.isChecked():"+oCkFB.isChecked());
			if(ExerciseUtils.saveUserData(mContext, oEdtNick.getText().toString(), oEdtName.getText().toString(),
			        oTxtWeight.getText().toString(), oTxtAge.getText().toString(), oTxtHeight.getText().toString(), oCkFB.isChecked(), 
			    	false, oCkTwitter.isChecked(),"F")){
				  
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
	    		
	    	oTxtWeight.setOnKeyListener(new OnKeyListener() {
				
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
			if(isUserExist){
				btnSave.setVisibility(View.GONE);
			}else{
				btnSave.setVisibility(View.VISIBLE);
			}
		}
	}
}