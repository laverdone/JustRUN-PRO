package com.glm.app;


import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
//import android.support.design.widget.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.glm.app.fragment.ListWorkoutFragment;
import com.glm.app.fragment.adapter.WorkoutAdapter;
import com.glm.bean.ConfigTrainer;
import com.glm.trainer.R;
import com.glm.utils.ExerciseUtils;
import com.google.android.material.tabs.TabLayout;
//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Locale;

import static com.glm.trainer.R.animator.*;


@SuppressWarnings("ALL")
public class HistoryList extends AppCompatActivity {

	/**
	 * The {@link PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	public String mINWorkoutToDelete = "-100";
	private ProgressDialog oWaitForLoad = null;
	private Animation a;
	public ListWorkoutFragment oWorkout = null;
	private ConfigTrainer oConfigTrainer = null;
	private WorkoutAdapter mWorkoutAdapterRun = null;
	private WorkoutAdapter mWorkoutAdapterWalk = null;
	private WorkoutAdapter mWorkoutAdapterBike = null;
	private Context mContext;
	private int mTypeWorkout = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_new_main);

	}


	@Override
	protected void onResume() {
		//WotkOutTask oTask = new WotkOutTask();
		//oTask.execute();
		Bundle extras = getIntent().getExtras();
		//Log.v(this.getClass().getCanonicalName(),"On Resume HISTORY LIST "+mTypeWorkout+" extras "+extras);
		if (extras != null) {
			mTypeWorkout = extras.getInt("typeworkout");
		}
		//Log.v(this.getClass().getCanonicalName(),"On Resume HISTORY LIST "+mTypeWorkout+" mViewPager "+mViewPager);
		oWaitForLoad = ProgressDialog.show(HistoryList.this, getString(R.string.app_name_buy), getString(R.string.please_wait));
		new Thread(new WorkOutThread()).start();


		super.onResume();
	}

	@Override
	public void onBackPressed() {
		if (oWaitForLoad != null && oWaitForLoad.isShowing()) {
			oWaitForLoad.dismiss();
			return;
		}
		Intent intent = new Intent();
		intent.putExtra("sectionFocus", 1);
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
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
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
			oWorkout = new ListWorkoutFragment();
			switch (position) {
				case 0:
					//RUN

					oWorkout.setContext(HistoryList.this, mWorkoutAdapterRun);
					return oWorkout;
				case 1:
					//WALK

					oWorkout.setContext(HistoryList.this, mWorkoutAdapterWalk);
					return oWorkout;
				case 2:
					//BIKE

					oWorkout.setContext(HistoryList.this, mWorkoutAdapterBike);
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

	class WorkOutThread implements Runnable {

		@Override
		public void run() {
			Looper.prepare();
			//Log.v(this.getClass().getCanonicalName(),"WorkOutThread is running");
			oConfigTrainer = ExerciseUtils.loadConfiguration(getApplicationContext());
			mWorkoutAdapterRun = new WorkoutAdapter(HistoryList.this, ExerciseUtils.getHistory(getApplicationContext(), 0, oConfigTrainer));
			mWorkoutAdapterWalk = new WorkoutAdapter(HistoryList.this, ExerciseUtils.getHistory(getApplicationContext(), 100, oConfigTrainer));
			mWorkoutAdapterBike = new WorkoutAdapter(HistoryList.this, ExerciseUtils.getHistory(getApplicationContext(), 1, oConfigTrainer));

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					//Log.v(this.getClass().getCanonicalName(),"WorkOutThread->runOnUiThread is running");
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

					a = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
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
					//Log.v(this.getClass().getCanonicalName(),"Current Exercise Type is: "+mTypeWorkout);

					if (mViewPager != null) {
						if (mTypeWorkout == ConstApp.TYPE_RUN) {
							mViewPager.setCurrentItem(0);
						} else if (mTypeWorkout == ConstApp.TYPE_WALK) {
							mViewPager.setCurrentItem(1);
						} else {
							mViewPager.setCurrentItem(2);
						}
					}

					//mListWorkOuts.setAdapter(mWorkoutAdapter);
					mWorkoutAdapterRun.notifyDataSetInvalidated();
					mWorkoutAdapterRun.notifyDataSetChanged();
					mWorkoutAdapterWalk.notifyDataSetInvalidated();
					mWorkoutAdapterWalk.notifyDataSetChanged();
					mWorkoutAdapterBike.notifyDataSetInvalidated();
					mWorkoutAdapterBike.notifyDataSetChanged();

					if (oWaitForLoad != null) oWaitForLoad.dismiss();
				}
			});


		}

	}

	@Override
	protected void onPause() {
		a = AnimationUtils.loadAnimation(this, disappear);
		a.reset();

		if (mViewPager != null) {
			mViewPager.clearAnimation();
			mViewPager.setAnimation(a);
		}

		super.onPause();
	}
}
