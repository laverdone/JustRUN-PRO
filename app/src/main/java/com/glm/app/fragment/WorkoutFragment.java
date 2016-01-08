package com.glm.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.glm.app.GoalActivity;
import com.glm.app.ManualWorkout;
import com.glm.app.stopwatch.WorkOutActivity;
import com.glm.trainer.NewMainActivity;
import com.glm.trainer.PreferencesActivity;
import com.glm.trainer.R;
import com.glm.utils.ExerciseUtils;
import com.glm.utils.quickaction.ActionItem;
import com.glm.utils.quickaction.MainQuickBar;
import com.glm.utils.quickaction.QuickAction;


/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class WorkoutFragment extends Fragment {
	private View rootView;
	private Context mContext;
	private RelativeLayout oStartRunningExercise;
	private RelativeLayout oStartWalkingExercise;
	private RelativeLayout oStartBikingExercise;
	//private RelativeLayout mBtnPreferences;
	private TextView oTxtBMI;
	private Toolbar mToolBar;
	private Toolbar mToolBarRunning;
	private Toolbar mToolBarWalking;
	private Toolbar mToolBarBiking;

	private WorkTaskAsync oTask=null;
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	public WorkoutFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext=getActivity().getApplicationContext();
		
		
		
		rootView = inflater.inflate(R.layout.new_new_main_page,
				container, false);

		mToolBar 			  = (Toolbar)  rootView.findViewById(R.id.card_toolbar);
		mToolBarRunning		  = (Toolbar)  rootView.findViewById(R.id.card_toolbar_running);
		mToolBarWalking		  = (Toolbar)  rootView.findViewById(R.id.card_toolbar_walking);
		mToolBarBiking		  = (Toolbar)  rootView.findViewById(R.id.card_toolbar_biking);
		oStartRunningExercise = (RelativeLayout) rootView.findViewById(R.id.btn_start_running);
	    oStartWalkingExercise = (RelativeLayout) rootView.findViewById(R.id.btn_start_walking);
	    oStartBikingExercise  = (RelativeLayout) rootView.findViewById(R.id.btn_start_biking);
		//mBtnPreferences		  = (RelativeLayout) rootView.findViewById(R.id.btn_preferences);
	    
	    oTxtBMI				  = (TextView) rootView.findViewById(R.id.txtBMI);

		mToolBar.inflateMenu(R.menu.preferences);
		mToolBar.setLogo(R.drawable.toolbar);
		mToolBar.setTitle(R.string.app_name_pro);
		mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent();

				if (item.getTitle().equals( mContext.getString(R.string.pref_title))) {
					intent.putExtra("pref_type",1);
					intent.setClass(mContext, PreferencesActivity.class);
					startActivity(intent);
					getActivity().finish();
				}else if (item.getTitle().equals( mContext.getString(R.string.general_pref))) {
					intent.putExtra("pref_type",2);
					intent.setClass(mContext, PreferencesActivity.class);
					startActivity(intent);
					getActivity().finish();
				}else if (item.getTitle().equals( mContext.getString(R.string.show_motivator))) {
					intent.putExtra("pref_type",3);
					intent.setClass(mContext, PreferencesActivity.class);
					startActivity(intent);
					getActivity().finish();
				}else if (item.getTitle().equals( mContext.getString(R.string.user))) {
					intent.putExtra("pref_type",4);
					intent.setClass(mContext, PreferencesActivity.class);
					startActivity(intent);
					getActivity().finish();
				}

				return false;
			}
		});

		mToolBarRunning.inflateMenu(R.menu.manual);
		mToolBarRunning.setTitle(R.string.run);
		mToolBarRunning.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {

				return false;
			}
		});

		mToolBarWalking.inflateMenu(R.menu.manual);
		mToolBarWalking.setTitle(R.string.walk);
		mToolBarWalking.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {

				return false;
			}
		});

		mToolBarBiking.inflateMenu(R.menu.manual);
		mToolBarBiking.setTitle(R.string.bike);
		mToolBarBiking.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {

				return false;
			}
		});

	    oStartRunningExercise.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if(((NewMainActivity)getActivity()).oConfigTrainer.isbRunGoal()){						
					intent.setClass(mContext, GoalActivity.class);
					intent.putExtra("type", "0");
					startActivity(intent);
					getActivity().finish();
				}else{
					intent.setClass(mContext, WorkOutActivity.class);
					intent.putExtra("type", 0);
					startActivity(intent);
					getActivity().finish();
				}	
				
			}
		} );
	    oStartWalkingExercise.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if(((NewMainActivity)getActivity()).oConfigTrainer.isbRunGoal()){					
					intent.setClass(mContext, GoalActivity.class);
					intent.putExtra("type", "100");
					startActivity(intent);
					getActivity().finish();		
				}else{				
					intent.setClass(mContext, WorkOutActivity.class);
					intent.putExtra("type", 100);
					startActivity(intent);
					getActivity().finish();			
				}
			}
		} );
	    oStartBikingExercise.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if(((NewMainActivity)getActivity()).oConfigTrainer.isbRunGoal()){
					intent.setClass(mContext, GoalActivity.class);
					intent.putExtra("type", "1");
					startActivity(intent);
					getActivity().finish();	
				}else{
					intent.setClass(mContext, WorkOutActivity.class);
					intent.putExtra("type", 1);
					startActivity(intent);						
					getActivity().finish();	
				}
				
			}
		} );
	    
	    /*mBtnPreferences.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, PreferencesActivity.class);
				startActivity(intent);
				getActivity().finish();	
			}
		});*/
	    
	    if(((NewMainActivity)getActivity()).oConfigTrainer!=null) oTxtBMI.setText(((NewMainActivity)getActivity()).mUser.getsBMI(((NewMainActivity)getActivity()).oConfigTrainer.getiUnits()));
	    
	    ImageButton imgBtnMore = (ImageButton) rootView.findViewById(R.id.imgBtnMore);
	    imgBtnMore.setOnClickListener(new OnClickListener() {
	    	final MainQuickBar oBar = new MainQuickBar(mContext);
			@Override
			public void onClick(View v) {
				oBar.getQuickAction().setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {          
			        @Override
			        public void onItemClick(QuickAction source, int pos, int actionId) {
			                //here we can filter which action item was clicked with pos or actionId parameter
			                ActionItem actionItem = source.getActionItem(pos);
			                Log.v(this.getClass().getCanonicalName(),"Item Click Pos: "+actionItem.getActionId());
			                Intent intent = new Intent();
			                intent.setClass(mContext, ManualWorkout.class);
			                switch (actionItem.getActionId()) {
							
			                case 1:	                	
			                	intent.putExtra("type", "1000");	
								Toast.makeText(mContext, "Manual Bike", Toast.LENGTH_SHORT)
								.show();	
								startActivity(intent);
								getActivity().finish();
								break;
							case 2:	
								intent.putExtra("type", "10000");
			        			startActivity(intent);			
			        			Toast.makeText(mContext, "Manual Walk", Toast.LENGTH_SHORT)
			        			.show();
			        			getActivity().finish();	
								break;
							case 3:
								intent.putExtra("type", "1001");	
								Toast.makeText(mContext, "Manual Bike", Toast.LENGTH_SHORT)
								.show();	
								startActivity(intent);
								getActivity().finish();
								break;
							default:
								break;
							}	                                
			            }
			        });			
					oBar.getQuickAction().show(v);
			}
		});
	   
		
		
	    
		return rootView;
	}

	public void setContext(Context context) {
		mContext=context;
	}
	@Override
	public void onResume() {
		//oTask = new WorkTaskAsync();
		//oTask.execute();
		super.onResume();
	}
	class WorkTaskAsync extends AsyncTask{

		@Override
		protected Object doInBackground(Object... params) {
			Log.v(this.getClass().getCanonicalName(),"doInBackground WorkTaskAsync");
			((NewMainActivity)getActivity()).oConfigTrainer=ExerciseUtils.loadConfiguration(mContext);
			((NewMainActivity)getActivity()).mUser = ExerciseUtils.loadUserDectails(mContext);
			return true;
		}
		
		@Override
		protected void onPostExecute(Object result) {
		    
		    oTask=null;
		}
	}
}