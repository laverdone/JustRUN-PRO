package com.glm.app.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.glm.bean.ConfigTrainer;
import com.glm.trainer.R;
import com.glm.utils.ExerciseUtils;



/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class PreferencesFragment extends Fragment {
	private View rootView;
	private Context mContext;
	private ConfigTrainer oConfigTrainer;
	private int mPreferenceType=0;
	
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
	private CheckBox checkVirtualRace	= null;
	private CheckBox checkInteractive	= null;
	private CheckBox checkMontivator	= null;
	private CheckBox checkDistance 		= null;
	private CheckBox checkTime			= null;
	private CheckBox checkKalories		= null;
	private CheckBox checkPace			= null;
	private CheckBox checkInclination	= null;
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	public PreferencesFragment() {

	}
	@Override
	public void onResume() {
		PrefAsyncTask oPrefTask = new PrefAsyncTask();
		oPrefTask.execute();
		super.onResume();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext=getActivity().getApplicationContext();
		if(mPreferenceType==0){
			rootView = inflater.inflate(R.layout.notification_preferences,
					container, false);
			checkMap			= (CheckBox) rootView.findViewById(R.id.checkMap);
			checkNotification 	= (CheckBox) rootView.findViewById(R.id.checkNotification);
			checkMusic			= (CheckBox) rootView.findViewById(R.id.checkMusic);
			checkExternalPlayer = (CheckBox) rootView.findViewById(R.id.checkExternalPlayer);
			lExternalPlayer 	= (RelativeLayout) rootView.findViewById(R.id.externalplayer);
			
			checkMap.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(final View v) {
					AsyncTask.execute(new Runnable() {
						
						@Override
						public void run() {
							SharedPreferences oPrefs = mContext.getSharedPreferences("aTrainer",Context.MODE_MULTI_PROCESS);
							SharedPreferences.Editor editPrefs = oPrefs.edit();
						    editPrefs.putBoolean("maps", ((CheckBox) v).isChecked());    
						    editPrefs.commit();					    
						}
					});
					
				}
			});
			checkNotification.setOnClickListener(new OnClickListener() {
				
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
			checkMusic.setOnClickListener(new OnClickListener() {
				
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
			checkExternalPlayer.setOnClickListener(new OnClickListener() {
				
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
		}else if(mPreferenceType==1){
			rootView = inflater.inflate(R.layout.general_preferences,
					container, false);
			
			mSpinnerAutoPause = (Spinner) rootView.findViewById(R.id.spinnerAutoPause);
				    				
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
							}
						});
				}

				@Override
				public void onNothingSelected(
						AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});
			
			mSpinnerUnit = (Spinner) rootView.findViewById(R.id.spinnerUnit);
			
			adapter = ArrayAdapter.createFromResource(
					mContext, R.array.units, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);        	       	    	        
			mSpinnerUnit.setAdapter(adapter);
			
			mSpinnerUnit.setOnItemSelectedListener(new OnItemSelectedListener() {

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
			
			checkWeightTrack	= (CheckBox) rootView.findViewById(R.id.checkWeightTrack);
			checkTarget		= (CheckBox) rootView.findViewById(R.id.checkTarget);
			checkCardio		= (CheckBox) rootView.findViewById(R.id.checkCardio);
			checkVirtualRace	= (CheckBox) rootView.findViewById(R.id.checkVirtualRace);
			checkInteractive	= (CheckBox) rootView.findViewById(R.id.checkInteractive);
			checkWeightTrack.setOnClickListener(new OnClickListener() {
				
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
			checkTarget.setOnClickListener(new OnClickListener() {
				
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
			checkCardio.setOnClickListener(new OnClickListener() {
				
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
			checkVirtualRace.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(final View v) {
					AsyncTask.execute(new Runnable() {
						
						@Override
						public void run() {
							SharedPreferences oPrefs = mContext.getSharedPreferences("aTrainer",Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
							SharedPreferences.Editor editPrefs = oPrefs.edit();
						    editPrefs.putBoolean("virtualrace", ((CheckBox) v).isChecked());    
						    editPrefs.commit();					    
						}
					});
					
				}
			});
			
			checkInteractive.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(final View v) {
					AsyncTask.execute(new Runnable() {
						
						@Override
						public void run() {
							SharedPreferences oPrefs = mContext.getSharedPreferences("aTrainer",Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);
							SharedPreferences.Editor editPrefs = oPrefs.edit();
						    editPrefs.putBoolean("interactive", ((CheckBox) v).isChecked());    
						    editPrefs.commit();					    
						}
					});
					
				}
			});

		}else{
			rootView = inflater.inflate(R.layout.trainer_preferences,
					container, false);
			
			mSpinnerTimeNotification = (Spinner) rootView.findViewById(R.id.spinnerTimeNotification);
			
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
										SharedPreferences oPrefs = mContext.getSharedPreferences("aTrainer",Context.MODE_MULTI_PROCESS);
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
		
		checkMontivator		= (CheckBox) rootView.findViewById(R.id.checkMontivator);
		checkDistance 		= (CheckBox) rootView.findViewById(R.id.checkDistance);
		checkTime			= (CheckBox) rootView.findViewById(R.id.checkTime);
        checkKalories		= (CheckBox) rootView.findViewById(R.id.checkKalories);
        checkPace			= (CheckBox) rootView.findViewById(R.id.checkPace);
        checkInclination	= (CheckBox) rootView.findViewById(R.id.checkInclination);
        
        checkMontivator.setOnClickListener(new OnClickListener() {
			
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

        checkDistance.setOnClickListener(new OnClickListener() {
			
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
		checkTime.setOnClickListener(new OnClickListener() {
				
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
		checkKalories.setOnClickListener(new OnClickListener() {
					
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
		checkPace.setOnClickListener(new OnClickListener() {
						
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
		checkInclination.setOnClickListener(new OnClickListener() {
							
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
							
		}
		return rootView;
	}     

	public void setContext(Context context,int preferencesType) {
		mContext=context;
		mPreferenceType=preferencesType;
	}

	class PrefAsyncTask extends AsyncTask{

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
			if(checkVirtualRace!=null)  checkVirtualRace.setChecked(oConfigTrainer.isVirtualRaceSupport());
			if(checkInteractive!=null)  checkInteractive.setChecked(oConfigTrainer.isbInteractiveExercise());
			
			if(checkMontivator!=null) checkMontivator.setChecked(oConfigTrainer.isbMotivator());
			if(checkDistance!=null)  checkDistance.setChecked(oConfigTrainer.isSayDistance());
			if(checkTime!=null)  checkTime.setChecked(oConfigTrainer.isSayTime());
			if(checkKalories!=null)  checkKalories.setChecked(oConfigTrainer.isSayKalories());
			if(checkPace!=null)  checkPace.setChecked(oConfigTrainer.isSayPace());
			if(checkInclination!=null)  checkInclination.setChecked(oConfigTrainer.isSayInclination());
		}

	}
}