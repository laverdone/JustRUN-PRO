package com.glm.trainer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.glm.app.fragment.PreferencesFragment;
import com.glm.app.fragment.UserFragment;

import java.util.Locale;

public class PreferencesActivity extends FragmentActivity  {

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

	private PreferencesFragment oPreferences=null;
	private UserFragment 		oUser=null;
	private boolean isFirstLaunch = false;
	private int iPrefType=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);   
		Bundle extras = getIntent().getExtras();
		if(extras !=null){
			isFirstLaunch = extras.getBoolean("first_launch");
			iPrefType	  = extras.getInt("pref_type");
		}    
		setContentView(R.layout.activity_new_main);


		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
		if(iPrefType==1){
			tabLayout.addTab(tabLayout.newTab().setText(mSectionsPagerAdapter.getPageTitle(0)));
			tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
		}
		if(iPrefType==2){
			tabLayout.addTab(tabLayout.newTab().setText(mSectionsPagerAdapter.getPageTitle(1)));
			tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
		}
		if(iPrefType==3){
			tabLayout.addTab(tabLayout.newTab().setText(mSectionsPagerAdapter.getPageTitle(2)));
			tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
		}

		if(iPrefType==4){
			tabLayout.addTab(tabLayout.newTab().setText(mSectionsPagerAdapter.getPageTitle(3)));
			tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
		}


		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);

		mViewPager.setAdapter(mSectionsPagerAdapter);
		if(isFirstLaunch || iPrefType>0) mViewPager.setOffscreenPageLimit(1);
		else mViewPager.setOffscreenPageLimit(4);
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
	}
}