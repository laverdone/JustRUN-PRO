package com.glm.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.glm.app.fragment.WorkoutDetailFragment;
import com.glm.app.fragment.WorkoutGraphFragment;
import com.glm.app.fragment.WorkoutMapFragment;
import com.glm.app.fragment.WorkoutMusicFragment;
import com.glm.app.fragment.WorkoutNoteFragment;
import com.glm.bean.ConfigTrainer;
import com.glm.bean.ExerciseManipulate;
import com.glm.bean.Music;
import com.glm.trainer.R;
import com.glm.utils.ExerciseUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.glm.trainer.R.animator.*;

public class WorkoutDetail extends AppCompatActivity {
	private ConfigTrainer oConfigTrainer;
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;
	private Animation a;
	private WorkoutDetailFragment oWorkoutDetail=null;
	private WorkoutGraphFragment  oWorkoutGraph=null;
	private WorkoutMapFragment    oWorkoutMap=null;
	private WorkoutMusicFragment  oWorkoutMusic=null;
	private WorkoutNoteFragment   oWorkoutNote=null;
	private int mIDWorkout=0;
	private ArrayList<Music> mMusic;
	private int mTypeWorkout=0;
	private ProgressDialog oWaitforLoad=null;

	public boolean isShare=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		if(extras !=null){
			mIDWorkout = extras.getInt("exercise_id");
		}        

		setContentView(R.layout.activity_new_main);

		oWaitforLoad=ProgressDialog.show(this,this.getString(R.string.app_name_buy),this.getString(R.string.please_wait),true,true,null);

		oWorkoutDetail= new WorkoutDetailFragment();
		oWorkoutGraph= new WorkoutGraphFragment();
		oWorkoutMap= new WorkoutMapFragment();
		oWorkoutMusic = new WorkoutMusicFragment();
		oWorkoutNote= new WorkoutNoteFragment();
		

        // Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
		tabLayout.removeAllTabs();
		tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.running_light));
		tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.graph_light));
		tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.map_light));
		tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.music_light));
		tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.edit_light));

		tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
		a.reset();

		mViewPager.clearAnimation();
		mViewPager.setAnimation(a);

		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager.setOffscreenPageLimit(4);
		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

		tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				if(tab.getPosition()==0){
					tab.setIcon(R.drawable.running_light);
				}else if(tab.getPosition()==1){
					tab.setIcon(R.drawable.graph_light);
					if(isShare) oWorkoutGraph.shareWorkOut();
				}else if(tab.getPosition()==2){
					tab.setIcon(R.drawable.map_light);
					if(isShare) oWorkoutMap.shareWorkOut();
				}else if(tab.getPosition()==3){
					tab.setIcon(R.drawable.music_light);
				}else{
					tab.setIcon(R.drawable.edit_light);
				}
				mViewPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});
		oWorkoutDetail.setPager(mViewPager);
		oWorkoutGraph.setPager(mViewPager);
		oWorkoutMap.setPager(mViewPager);
	}

	@Override
    public void onBackPressed() {
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), HistoryList.class);
		intent.putExtra("typeworkout",mTypeWorkout);
		startActivity(intent);
		finish();
    }    
	@Override
	public void onResume() {
		Bundle extras = getIntent().getExtras();
		if(extras !=null){
			mIDWorkout = extras.getInt("exercise_id");
		} 
		if(oWorkoutDetail!=null) oWorkoutDetail.setContext(WorkoutDetail.this,mIDWorkout);
		if(oWorkoutGraph!=null) oWorkoutGraph.setContext(WorkoutDetail.this,mIDWorkout);
		if(oWorkoutMusic!=null) oWorkoutMusic.setContext(WorkoutDetail.this,mIDWorkout);
		//ExeriseTask oTask = new ExeriseTask();
	    //oTask.execute();

		new Thread(new ExerciseRunnable()).start();

		super.onResume();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

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
				//DETAIL
				
				oWorkoutDetail.setContext(WorkoutDetail.this,mIDWorkout);
				return oWorkoutDetail;
			case 1:
				//GRAPH

				oWorkoutGraph.setContext(WorkoutDetail.this,mIDWorkout);
				return oWorkoutGraph;
			case 2:
				//Maps
				
				oWorkoutMap.setContext(WorkoutDetail.this,mIDWorkout);
				return oWorkoutMap;
			case 3:
				//Music
				
				oWorkoutMusic.setContext(WorkoutDetail.this,mIDWorkout);
				return oWorkoutMusic;
				
			case 4:
				//Note
				
				oWorkoutNote.setContext(WorkoutDetail.this,mIDWorkout);
				return oWorkoutNote;	
			}
			
			return null;
		}

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.exercise_dett).toUpperCase(l);
			case 1:
				return getString(R.string.exercise_graph).toUpperCase(l);
			case 2:
				return getString(R.string.maps).toUpperCase(l);
			case 3:
				return getString(R.string.listening_song).toUpperCase(l);
			case 4:
				return getString(R.string.note).toUpperCase(l);
			}
			return null;
		}
	}

	class ExerciseRunnable implements Runnable {

		@Override
		public void run() {
			oConfigTrainer = ExerciseUtils.loadConfiguration(getApplicationContext());
			//Chiamo il tackAsink
			ExerciseUtils.populateExerciseDetails(getApplicationContext(), oConfigTrainer, mIDWorkout);

			//Log.v(this.getClass().getCanonicalName(),"IDExercide: " +ExerciseManipulate.getiIDExercise()+" - "+ExerciseManipulate.getsTotalDistance());
			mMusic=ExerciseUtils.getMusicWorkout(getApplicationContext(), oConfigTrainer,  mIDWorkout);
			oWorkoutDetail.oConfigTrainer=oConfigTrainer;
			mTypeWorkout=ExerciseManipulate.getiTypeExercise();
			//Log.v(this.getClass().getCanonicalName(),"workout type is "+mTypeWorkout);
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					SimpleDateFormat dfm = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
					if(oWorkoutDetail==null || oWorkoutDetail.mToolbar==null){ onBackPressed(); return;}
					oWorkoutDetail.mToolbar.setTitle(dfm.format(ExerciseManipulate.getdDateTimeStart()));
					oWorkoutDetail.oTxt_Time.setText(ExerciseManipulate.getsTotalTime());
					oWorkoutDetail.oTxt_Distance.setText(ExerciseManipulate.getsTotalDistance());
					oWorkoutDetail.oTxt_AVGSpeed.setText(ExerciseManipulate.getsAVGSpeed());
					oWorkoutDetail.oTxt_AVGPace.setText(ExerciseManipulate.getsMinutePerDistance());
					oWorkoutDetail.oTxt_MAXSpeed.setText(ExerciseManipulate.getsMAXSpeed());
					oWorkoutDetail.oTxt_MAXPace.setText(ExerciseManipulate.getsMAXMinutePerDistance());
					oWorkoutDetail.oTxt_Step.setText(ExerciseManipulate.getsStepCount());

					if(ExerciseManipulate.getsStepCount().compareToIgnoreCase("0")==0){
						oWorkoutDetail.oTxt_Step.setVisibility(View.GONE);
						oWorkoutDetail.oLbl_Step.setVisibility(View.GONE);
					}


					oWorkoutDetail.oTxt_Kalories.setText(ExerciseManipulate.getsCurrentCalories());
					if(oConfigTrainer!=null){
						if(oConfigTrainer.isbCardioPolarBuyed()){
							if(ExerciseManipulate.getiMAXBpm()==0){
								oWorkoutDetail.oTxt_MaxBpm.setText("n.d.");
							}else{
								Log.i(this.getClass().getCanonicalName(),"MaxBPM"+ExerciseManipulate.getiMAXBpm());
								oWorkoutDetail.oTxt_MaxBpm.setText(String.valueOf(ExerciseManipulate.getiMAXBpm()));
							}
							if(ExerciseManipulate.getiAVGBpm()==0){
								oWorkoutDetail.oTxt_AvgBpm.setText("n.d.");
							}else{
								oWorkoutDetail.oTxt_AvgBpm.setText(String.valueOf(ExerciseManipulate.getiAVGBpm()));
							}
						}else{
							oWorkoutDetail.oLLDectails.removeView(oWorkoutDetail.oMaxBpm);
							oWorkoutDetail.oLLDectails.removeView(oWorkoutDetail.oAvgBpm);
						}
					}
					oWorkoutMusic.addMusic(mMusic);
					oWaitforLoad.dismiss();
				}
			});
		}
	}

/*	class ExeriseTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			oConfigTrainer = ExerciseUtils.loadConfiguration(getApplicationContext());		
			//Chiamo il tackAsink
			ExerciseUtils.populateExerciseDetails(getApplicationContext(), oConfigTrainer, mIDWorkout);
			//Log.v(this.getClass().getCanonicalName(),"IDExercide: " +ExerciseManipulate.getiIDExercise()+" - "+ExerciseManipulate.getsTotalDistance());
			mMusic=ExerciseUtils.getMusicWorkout(getApplicationContext(), oConfigTrainer,  mIDWorkout);
			oWorkoutDetail.oConfigTrainer=oConfigTrainer;
		    
	        
	        return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {			
			SimpleDateFormat dfm = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			
			oWorkoutDetail.mToolbar.setTitle(dfm.format(ExerciseManipulate.getdDateTimeStart()));
			oWorkoutDetail.oTxt_Time.setText(ExerciseManipulate.getsTotalTime());
			oWorkoutDetail.oTxt_Distance.setText(ExerciseManipulate.getsTotalDistance());
			oWorkoutDetail.oTxt_AVGSpeed.setText(ExerciseManipulate.getsAVGSpeed());
			oWorkoutDetail.oTxt_AVGPace.setText(ExerciseManipulate.getsMinutePerDistance());
			oWorkoutDetail.oTxt_MAXSpeed.setText(ExerciseManipulate.getsMAXSpeed());
			oWorkoutDetail.oTxt_MAXPace.setText(ExerciseManipulate.getsMAXMinutePerDistance());
			oWorkoutDetail.oTxt_Step.setText(ExerciseManipulate.getsStepCount());
			
			if(ExerciseManipulate.getsStepCount().compareToIgnoreCase("0")==0){
				oWorkoutDetail.oTxt_Step.setVisibility(View.GONE);
				oWorkoutDetail.oLbl_Step.setVisibility(View.GONE);
			}
			
			
			oWorkoutDetail.oTxt_Kalories.setText(ExerciseManipulate.getsCurrentCalories());
			    if(oConfigTrainer!=null){
			    	if(oConfigTrainer.isbCardioPolarBuyed()){
			    		if(ExerciseManipulate.getiMAXBpm()==0){
			    			oWorkoutDetail.oTxt_MaxBpm.setText("n.d.");
			    		}else{
			    			Log.i(this.getClass().getCanonicalName(),"MaxBPM"+ExerciseManipulate.getiMAXBpm());
			    			oWorkoutDetail.oTxt_MaxBpm.setText(String.valueOf(ExerciseManipulate.getiMAXBpm()));	
			    		}
			    		if(ExerciseManipulate.getiAVGBpm()==0){
			    			oWorkoutDetail.oTxt_AvgBpm.setText("n.d.");
			    		}else{
			    			oWorkoutDetail.oTxt_AvgBpm.setText(String.valueOf(ExerciseManipulate.getiAVGBpm()));
			    		}	    	    	    		
			    	}else{
			    		oWorkoutDetail.oLLDectails.removeView(oWorkoutDetail.oMaxBpm);
			    		oWorkoutDetail.oLLDectails.removeView(oWorkoutDetail.oAvgBpm); 	    		
			    	}
			    }
			oWorkoutMusic.addMusic(mMusic);
		}
	}*/

	@Override
	protected void onPause() {
		a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.disappear);
		a.reset();

		mViewPager.clearAnimation();
		mViewPager.setAnimation(a);
		super.onPause();
	}
}
