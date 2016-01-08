package com.glm.trainer;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.glm.app.fragment.ListWorkoutFragment;
import com.glm.app.fragment.adapter.WorkoutAdapter;
import com.glm.bean.ConfigTrainer;
import com.glm.utils.ExerciseUtils;

import java.util.Locale;


public class HistoryList extends FragmentActivity  {

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
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext=this;
		setContentView(R.layout.activity_new_main);



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


	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		private int[] imageResId = {
				R.drawable.running,
				R.drawable.walking,
				R.drawable.biking
		};


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
			/*Drawable image = mContext.getDrawable(imageResId[position]);
			image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
			SpannableString sb = new SpannableString(" ");
			ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
			sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			return sb;*/

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

					a = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.fadein);
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

			mViewPager.setAdapter(mSectionsPagerAdapter);
			mViewPager.setOffscreenPageLimit(3);
			// When swiping between different sections, select the corresponding
			// tab. We can also use ActionBar.Tab#select() to do this if we have
			// a reference to the Tab.
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
    	alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,getString(R.string.yes), new android.content.DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				DeleteWotkOutTask deleteTask = new DeleteWotkOutTask();
				deleteTask.execute();
			}        				
    	});
    	
    	alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,getString(R.string.no), new android.content.DialogInterface.OnClickListener(){

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
