package com.glm.trainer;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.glm.app.fragment.ListWorkoutFragment;
import com.glm.app.fragment.adapter.WorkoutAdapter;
import com.glm.bean.ConfigTrainer;
import com.glm.utils.ExerciseUtils;

import java.util.Locale;


public class HistoryList extends FragmentActivity implements
		ActionBar.TabListener {

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
	ViewPager mViewPager;
	public String mINWorkoutToDelete="-100";
	private ProgressDialog oWaitForLoad=null;
	private Animation a;
	private ListWorkoutFragment oWorkout=null;
	private ConfigTrainer oConfigTrainer=null;
	private WorkoutAdapter mWorkoutAdapterRun=null;
	private WorkoutAdapter mWorkoutAdapterWalk=null;
	private WorkoutAdapter mWorkoutAdapterBike=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    
		setContentView(R.layout.activity_map_main);
        
	}
	@Override
	protected void onResume() {
		//WotkOutTask oTask = new WotkOutTask();
		//oTask.execute();
		oWaitForLoad = ProgressDialog.show(HistoryList.this, getString(R.string.app_name_buy), getString(R.string.please_wait));
		new Thread(new WorkOutThread()).start();
		
		super.onResume();
	}
	@Override
    public void onBackPressed() {
		Intent intent = new Intent();
		intent.putExtra("sectionFocus",1);
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

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
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
			oWorkout= new ListWorkoutFragment();
			switch(position) {
			case 0:
				//RUN
				
				oWorkout.setContext(HistoryList.this,mWorkoutAdapterRun);
				return oWorkout;
			case 1:
				//WALK
				
				oWorkout.setContext(HistoryList.this,mWorkoutAdapterWalk);
                return oWorkout;
			case 2:
				//BIKE
				
				oWorkout.setContext(HistoryList.this,mWorkoutAdapterBike);
				return oWorkout;
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
				return getString(R.string.run).toUpperCase(l);
			case 1:
				return getString(R.string.walk).toUpperCase(l);
			case 2:
				return getString(R.string.bike).toUpperCase(l);
			}
			return null;
		}
	}
	
	class WorkOutThread implements Runnable{

		@Override
		public void run() {
			Looper.prepare();
			
			oConfigTrainer=ExerciseUtils.loadConfiguration(getApplicationContext());
			mWorkoutAdapterRun = new WorkoutAdapter(HistoryList.this, ExerciseUtils.getHistory(getApplicationContext(),0,oConfigTrainer));
			mWorkoutAdapterWalk = new WorkoutAdapter(HistoryList.this, ExerciseUtils.getHistory(getApplicationContext(),100,oConfigTrainer));
			mWorkoutAdapterBike = new WorkoutAdapter(HistoryList.this, ExerciseUtils.getHistory(getApplicationContext(),1,oConfigTrainer));
			
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// Set up the action bar.
					final ActionBar actionBar = getActionBar();
					actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
					actionBar.setDisplayShowHomeEnabled(false);
					actionBar.setDisplayShowTitleEnabled(false);
			        // Create the adapter that will return a fragment for each of the three
					// primary sections of the app.
					mSectionsPagerAdapter = new SectionsPagerAdapter(
							getSupportFragmentManager());

					// Set up the ViewPager with the sections adapter.
					mViewPager = (ViewPager) findViewById(R.id.pager);
					a = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.fadein);
					a.reset();

					mViewPager.clearAnimation();
					mViewPager.setAnimation(a);

					mViewPager.setAdapter(mSectionsPagerAdapter);
					mViewPager.setOffscreenPageLimit(3);
					// When swiping between different sections, select the corresponding
					// tab. We can also use ActionBar.Tab#select() to do this if we have
					// a reference to the Tab.
					mViewPager
							.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
								@Override
								public void onPageSelected(int position) {
									actionBar.setSelectedNavigationItem(position);
								}
							});
					
					actionBar.removeAllTabs();
					// For each of the sections in the app, add a tab to the action bar.
					for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
						// Create a tab with text corresponding to the page title defined by
						// the adapter. Also specify this Activity object, which implements
						// the TabListener interface, as the callback (listener) for when
						// this tab is selected.
						actionBar.addTab(actionBar.newTab()
								.setText(mSectionsPagerAdapter.getPageTitle(i))
								.setTabListener(HistoryList.this));
					}
					//mListWorkOuts.setAdapter(mWorkoutAdapter);
					mWorkoutAdapterRun.notifyDataSetInvalidated();
					mWorkoutAdapterRun.notifyDataSetChanged();
					mWorkoutAdapterWalk.notifyDataSetInvalidated();
					mWorkoutAdapterWalk.notifyDataSetChanged();
					mWorkoutAdapterBike.notifyDataSetInvalidated();
					mWorkoutAdapterBike.notifyDataSetChanged();
					if(oWaitForLoad!=null) oWaitForLoad.dismiss();	
				}
			});
						
					
		}
		
	}
	
	/*class WotkOutTask extends AsyncTask{

		@Override
		protected Object doInBackground(Object... params) {
			oConfigTrainer=ExerciseUtils.loadConfiguration(getApplicationContext());
			mWorkoutAdapterRun = new WorkoutAdapter(HistoryList.this, ExerciseUtils.getHistory(getApplicationContext(),0,oConfigTrainer));
			mWorkoutAdapterWalk = new WorkoutAdapter(HistoryList.this, ExerciseUtils.getHistory(getApplicationContext(),100,oConfigTrainer));
			mWorkoutAdapterBike = new WorkoutAdapter(HistoryList.this, ExerciseUtils.getHistory(getApplicationContext(),1,oConfigTrainer));
			return true;
		}
		@Override
		protected void onPreExecute() {
			oWaitForLoad = ProgressDialog.show(HistoryList.this, getString(R.string.app_name_buy), getString(R.string.please_wait));
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(Object result) {
			// Set up the action bar.
			final ActionBar actionBar = getActionBar();
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowTitleEnabled(false);
	        // Create the adapter that will return a fragment for each of the three
			// primary sections of the app.
			mSectionsPagerAdapter = new SectionsPagerAdapter(
					getSupportFragmentManager());

			// Set up the ViewPager with the sections adapter.
			mViewPager = (ViewPager) findViewById(R.id.pager);
			mViewPager.setAdapter(mSectionsPagerAdapter);
			mViewPager.setOffscreenPageLimit(3);
			// When swiping between different sections, select the corresponding
			// tab. We can also use ActionBar.Tab#select() to do this if we have
			// a reference to the Tab.
			mViewPager
					.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
						@Override
						public void onPageSelected(int position) {
							actionBar.setSelectedNavigationItem(position);
						}
					});
			
			actionBar.removeAllTabs();
			// For each of the sections in the app, add a tab to the action bar.
			for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
				// Create a tab with text corresponding to the page title defined by
				// the adapter. Also specify this Activity object, which implements
				// the TabListener interface, as the callback (listener) for when
				// this tab is selected.
				actionBar.addTab(actionBar.newTab()
						.setText(mSectionsPagerAdapter.getPageTitle(i))
						.setTabListener(HistoryList.this));
			}
			
			//mListWorkOuts.setAdapter(mWorkoutAdapter);
			mWorkoutAdapterRun.notifyDataSetInvalidated();
			mWorkoutAdapterRun.notifyDataSetChanged();
			mWorkoutAdapterWalk.notifyDataSetInvalidated();
			mWorkoutAdapterWalk.notifyDataSetChanged();
			mWorkoutAdapterBike.notifyDataSetInvalidated();
			mWorkoutAdapterBike.notifyDataSetChanged();
			if(oWaitForLoad!=null) oWaitForLoad.dismiss();
			super.onPostExecute(result);
		}
	}*/
	
	class DeleteWotkOutTask extends AsyncTask{

		@Override
		protected Object doInBackground(Object... params) {
			oConfigTrainer=ExerciseUtils.loadConfiguration(getApplicationContext());
			if(ExerciseUtils.deleteExercise(HistoryList.this, mINWorkoutToDelete)){
				mWorkoutAdapterRun = new WorkoutAdapter(HistoryList.this, ExerciseUtils.getHistory(getApplicationContext(),0,oConfigTrainer));
				mWorkoutAdapterWalk = new WorkoutAdapter(HistoryList.this, ExerciseUtils.getHistory(getApplicationContext(),100,oConfigTrainer));
				mWorkoutAdapterBike = new WorkoutAdapter(HistoryList.this, ExerciseUtils.getHistory(getApplicationContext(),1,oConfigTrainer));
			}
			return true;
		}
		@Override
		protected void onPreExecute() {
			oWaitForLoad = ProgressDialog.show(HistoryList.this, getString(R.string.app_name_buy), getString(R.string.please_wait));
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(Object result) {
			// Set up the action bar.
			final ActionBar actionBar = getActionBar();
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowTitleEnabled(false);
	        // Create the adapter that will return a fragment for each of the three
			// primary sections of the app.
			mSectionsPagerAdapter = new SectionsPagerAdapter(
					getSupportFragmentManager());

			// Set up the ViewPager with the sections adapter.
			mViewPager = (ViewPager) findViewById(R.id.pager);
			mViewPager.setAdapter(mSectionsPagerAdapter);
			mViewPager.setOffscreenPageLimit(3);
			// When swiping between different sections, select the corresponding
			// tab. We can also use ActionBar.Tab#select() to do this if we have
			// a reference to the Tab.
			mViewPager
					.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
						@Override
						public void onPageSelected(int position) {
							actionBar.setSelectedNavigationItem(position);
						}
					});
			
			actionBar.removeAllTabs();
			// For each of the sections in the app, add a tab to the action bar.
			for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
				// Create a tab with text corresponding to the page title defined by
				// the adapter. Also specify this Activity object, which implements
				// the TabListener interface, as the callback (listener) for when
				// this tab is selected.
				actionBar.addTab(actionBar.newTab()
						.setText(mSectionsPagerAdapter.getPageTitle(i))
						.setTabListener(HistoryList.this));
			}
			
			//mListWorkOuts.setAdapter(mWorkoutAdapter);
			mWorkoutAdapterRun.notifyDataSetInvalidated();
			mWorkoutAdapterRun.notifyDataSetChanged();
			mWorkoutAdapterWalk.notifyDataSetInvalidated();
			mWorkoutAdapterWalk.notifyDataSetChanged();
			mWorkoutAdapterBike.notifyDataSetInvalidated();
			mWorkoutAdapterBike.notifyDataSetChanged();
			if(oWaitForLoad!=null) oWaitForLoad.dismiss();
			super.onPostExecute(result);
		}
	}
	
	public void deleteExercise(final String id) {		
		AlertDialog alertDialog;
    	alertDialog = new AlertDialog.Builder(this).create();
    	alertDialog.setTitle(getString(R.string.titledeleteexercise));
    	alertDialog.setMessage(getString(R.string.messagedeleteexercise));
    	alertDialog.setButton(getString(R.string.yes), new android.content.DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				DeleteWotkOutTask deleteTask = new DeleteWotkOutTask();
				deleteTask.execute();
			}        				
    	});
    	
    	alertDialog.setButton2(getString(R.string.no), new android.content.DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {					
				
			}        		
			
    		});
    	alertDialog.show();
	}

	@Override
	protected void onPause() {
		a = AnimationUtils.loadAnimation(this, R.animator.disappear);
		a.reset();

		if(mViewPager!=null){
			mViewPager.clearAnimation();
			mViewPager.setAnimation(a);
		}

		super.onPause();
	}
}
