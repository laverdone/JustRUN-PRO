package com.glm.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.glm.app.ConstApp;
import com.glm.bean.DistancePerExercise;
import com.glm.chart.NewBarChart;
import com.glm.app.ChartSummaryActivity;
import com.glm.app.HistoryList;
import com.glm.app.NewMainActivity;
import com.glm.trainer.R;
import com.glm.utils.ExerciseUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;


/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class SummaryFragment extends Fragment {
	private View rootView;
	private Context mContext;
		
	private TextView oTxtTot;
	private TextView oTxtRun;
	private TextView oTxtWalk;
	private TextView oTxtBike;

	private TextView oTxtKalRun;
	private TextView oTxtKalWalk;
	private TextView oTxtKalBike;
	private TextView oTxtTitle;
	
	public RelativeLayout mTotalGraph;
	public RelativeLayout mRunGraph;
	public RelativeLayout mWalkGraph;
	public RelativeLayout mBikeGraph;
	private int mTypeGraph=0;

	public int mYear=0;
	public SeekBar mSeekBarYears=null;
	public Vector<Integer> mYears = null;

	private ProgressBar oWaitForLoad=null;
	private ScrollView oScroll = null;
	/**
	 * 0=pace
	 * 1=alt
	 * 2=...
	 * */
	private int iTypeGraph=0;
	public Vector<DistancePerExercise> oTable=null;
	private String sUnit="Km";
	public NewBarChart mTotalChart = null;
	public NewBarChart mRunChart = null;
	public NewBarChart mWalkChart = null;
	public NewBarChart mBikeChart = null;

	public ImageButton mZoomRun=null;
	public ImageButton mZoomWalk=null;
	public ImageButton mZoomBike=null;

	private TextView mTitleRun=null;
	private TextView mTitleWalk=null;
	private TextView mTitleBike=null;
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	public SummaryFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext=getActivity().getApplicationContext();

		rootView = inflater.inflate(R.layout.new_summary_history,
				container, false);

		MobileAds.initialize(mContext, new OnInitializationCompleteListener() {
			@Override
			public void onInitializationComplete(InitializationStatus initializationStatus) {
			}
		});



		mTotalGraph		= (RelativeLayout) rootView.findViewById(R.id.totalGraph);
		mRunGraph		= (RelativeLayout) rootView.findViewById(R.id.runGraph);
		mWalkGraph		= (RelativeLayout) rootView.findViewById(R.id.walkGraph);
		mBikeGraph		= (RelativeLayout) rootView.findViewById(R.id.bikeGraph);

		mTotalChart 			= new NewBarChart(mContext);
		mTotalChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		//mTotalChart.init(mContext,mTypeGraph,getActivity());

		mRunChart 			= new NewBarChart(mContext);
		mRunChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		//mRunChart.init(mContext,ConstApp.TYPE_RUN,getActivity());
		Toolbar toolbarRun = (Toolbar) rootView.findViewById(R.id.card_toolbar_running);
		//toolbarRun.setLogo(R.drawable.running_dark);

		mWalkChart 			= new NewBarChart(mContext);
		mWalkChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		//mWalkChart.init(mContext,ConstApp.TYPE_WALK,getActivity());
		Toolbar toolbarWalk = (Toolbar) rootView.findViewById(R.id.card_toolbar_walking);
		//toolbarWalk.setLogo(R.drawable.walking_dark);

		mBikeChart 			= new NewBarChart(mContext);
		mBikeChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		//mBikeChart.init(mContext,ConstApp.TYPE_BIKE,getActivity());
		//Toolbar toolbarBike = (Toolbar) rootView.findViewById(R.id.card_toolbar_biking);
		//toolbarBike.setLogo(R.drawable.biking_dark);

		oTxtTot		= (TextView) rootView.findViewById(R.id.textDistance_tot);
    	oTxtRun		= (TextView) rootView.findViewById(R.id.textDistance_run); 
    	oTxtWalk	= (TextView) rootView.findViewById(R.id.textDistance_walk); 
    	oTxtBike	= (TextView) rootView.findViewById(R.id.textDistance_bike);

		mTitleRun	= (TextView) rootView.findViewById(R.id.titleRun);
		mTitleWalk	= (TextView) rootView.findViewById(R.id.titleWalk);
		mTitleBike	= (TextView) rootView.findViewById(R.id.titleBike);

    	oTxtKalRun	= (TextView) rootView.findViewById(R.id.textKal_run); 
    	oTxtKalWalk	= (TextView) rootView.findViewById(R.id.textKal_walk); 
    	oTxtKalBike	= (TextView) rootView.findViewById(R.id.textKal_bike);

		mZoomRun	= (ImageButton) rootView.findViewById(R.id.zoomRun);
		mZoomWalk	= (ImageButton) rootView.findViewById(R.id.zoomWalk);
		mZoomBike	= (ImageButton) rootView.findViewById(R.id.zoomBike);

		//TODO count years
		mSeekBarYears = (SeekBar)  rootView.findViewById(R.id.seekBarYears);

		//toolbarBike.removeAllViews();
		//toolbarBike.add;

    	//oTxtTitle   	=  (TextView) rootView.findViewById(R.id.textHistory);
    	oWaitForLoad 	= (ProgressBar) rootView.findViewById(R.id.waitForLoad);
    	oScroll			= (ScrollView) rootView.findViewById(R.id.scrollViewSummary);


		LinearLayout imgBtnDettRun = (LinearLayout) rootView.findViewById(R.id.btnDettRun);
		imgBtnDettRun.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, HistoryList.class);
				intent.putExtra("typeworkout",ConstApp.TYPE_RUN);
				if(((NewMainActivity)getActivity()).mConnection!=null) ((NewMainActivity)getActivity()).mConnection.destroy();
				startActivity(intent);
				getActivity().finish();

			}
		});

		LinearLayout imgBtnDettWalk = (LinearLayout) rootView.findViewById(R.id.btnDettWalk);
		imgBtnDettWalk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, HistoryList.class);
				intent.putExtra("typeworkout",ConstApp.TYPE_WALK);
				if(((NewMainActivity)getActivity()).mConnection!=null) ((NewMainActivity)getActivity()).mConnection.destroy();
				startActivity(intent);
				getActivity().finish();

			}
		});

		LinearLayout imgBtnDettBike = (LinearLayout) rootView.findViewById(R.id.btnDettBike);
		imgBtnDettBike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, HistoryList.class);
				intent.putExtra("typeworkout",ConstApp.TYPE_BIKE);
				if(((NewMainActivity)getActivity()).mConnection!=null) ((NewMainActivity)getActivity()).mConnection.destroy();
				startActivity(intent);
				getActivity().finish();

			}
		});
    	changeGUI();

		if(getActivity().getPackageName().equals(ConstApp.ADS_APP_PACKAGE_NAME)){
			AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
			AdRequest adRequest = new AdRequest.Builder().build();
			mAdView.loadAd(adRequest);
		}else{
			AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
			mAdView.setVisibility(View.GONE);
		}

    	return rootView;
	}

	public void setContext(Context context) {
		mContext=context;
	}
	@Override
	public void onResume() {
		mContext=getActivity().getApplicationContext();
		new Thread(new GraphTaskAsync()).start();

		super.onResume();
	}
	public void changeGUI(){
		
		if(oTable!=null && mTotalGraph!=null && mTotalChart!=null){
			int iRun=0,iWalk=0,iBike=0;
	        int iTableSize=oTable.size();
			for(int i=0;i<iTableSize;i++){
	        	
	        	if(oTable.get(i).getiTypeExercise()==0){
	        		//Run
	        		try{
	        			iRun = Integer.parseInt(oTable.get(i).getsDistance().replace(" ", ""));
	        		}catch (NumberFormatException e) {
						iRun=0;
					}
	        		oTxtRun.setText(oTable.get(i).getsDistance()+sUnit);
	        		oTxtKalRun.setText(oTable.get(i).getsCalories()+" "+getString(R.string.kalories));
	        	}else if(oTable.get(i).getiTypeExercise()==1){
	        		//Bike
	        		try{
	        			iBike = Integer.parseInt(oTable.get(i).getsDistance().replace(" ", ""));
	        		}catch (NumberFormatException e) {
	        			iBike=0;
					}
	        		oTxtBike.setText(oTable.get(i).getsDistance()+sUnit);
	        		oTxtKalBike.setText(oTable.get(i).getsCalories()+" "+getString(R.string.kalories));
	        	}else if(oTable.get(i).getiTypeExercise()==100){
	        		//walk
	        		try{
	        			iWalk = Integer.parseInt(oTable.get(i).getsDistance().replace(" ", ""));
	        		}catch (NumberFormatException e) {
	        			iWalk=0;
					}
	        		oTxtWalk.setText(oTable.get(i).getsDistance()+sUnit);
	        		oTxtKalWalk.setText(oTable.get(i).getsCalories()+" "+getString(R.string.kalories));
	        	}
	        }
	        oTxtTot.setText((iRun+iBike+iWalk)+sUnit);
	        
	       

		    if(oWaitForLoad!=null) {
		    	oWaitForLoad.setVisibility(View.GONE);
		    	oScroll.setVisibility(View.VISIBLE);
		    }
		}	
	}
	
	class GraphTaskAsync implements Runnable{
		@Override
		public void run() {
			if(getActivity()==null) return;
			mYears=ExerciseUtils.getYears(mContext);

			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					//mTotalChart.init(mContext,mTypeGraph,getActivity());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

					int iYear=Integer.parseInt(sdf.format(new Date()));
					if(mYears.size()>0){
						iYear=mYears.get(mYears.size()-1);
					}


					mRunChart.init(mContext, ConstApp.TYPE_RUN,getActivity(),iYear);
					mWalkChart.init(mContext,ConstApp.TYPE_WALK,getActivity(),iYear);
					mBikeChart.init(mContext,ConstApp.TYPE_BIKE,getActivity(),iYear);

					mTotalGraph.removeAllViews();
					mTotalChart.setOnTouchListener(new View.OnTouchListener() {

						public boolean onTouch(View view, MotionEvent motionEvent) {

							Intent intent = new Intent();
							intent.setClass(mContext, ChartSummaryActivity.class);
							intent.putExtra("chartType",mTypeGraph);
							intent.putExtra("year",mYear);
							startActivity(intent);
							getActivity().finish();

							//getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
							return false;
						}
					});
					mTotalGraph.addView(mTotalChart);

					mRunGraph.removeAllViews();
					mZoomRun.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent();
							intent.setClass(mContext, ChartSummaryActivity.class);
							intent.putExtra("chartType",ConstApp.TYPE_RUN);
							intent.putExtra("year",mYear);
							startActivity(intent);
							getActivity().finish();

						}
					});
					mRunGraph.addView(mRunChart);

					mWalkGraph.removeAllViews();
					mZoomWalk.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {

							Intent intent = new Intent();
							intent.setClass(mContext, ChartSummaryActivity.class);
							intent.putExtra("chartType",ConstApp.TYPE_WALK);
							intent.putExtra("year",mYear);
							startActivity(intent);
							getActivity().finish();
						}
					});

					mWalkGraph.addView(mWalkChart);

					mBikeGraph.removeAllViews();
					mZoomBike.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent();
							intent.setClass(mContext, ChartSummaryActivity.class);
							intent.putExtra("chartType",ConstApp.TYPE_BIKE);
							intent.putExtra("year",mYear);
							startActivity(intent);
							getActivity().finish();
						}
					});
					mBikeGraph.addView(mBikeChart);



					if(mYears.size()>0){
						mSeekBarYears.setMax(mYears.size()-1);
						mSeekBarYears.setProgress(mYears.size()-1);
						mYear=mYears.get(mYears.size()-1);
					}else{
						mSeekBarYears.setVisibility(View.GONE);
					}


					mTitleRun.setText(mContext.getString(R.string.history_run)+" - "+mYear);
					mTitleWalk.setText(mContext.getString(R.string.history_walk)+" - "+mYear);
					mTitleBike.setText(mContext.getString(R.string.history_bike)+" - "+mYear);

					mSeekBarYears.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
						@Override
						public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
							//TODO
							if(mYears.size()>0) mYear=mYears.get(progress);
							Log.v(this.getClass().getCanonicalName(),"Year selected is: "+mYear+" progress selectd is: "+progress);
							mRunChart.init(mContext, ConstApp.TYPE_RUN,getActivity(),mYear);
							mWalkChart.init(mContext,ConstApp.TYPE_WALK,getActivity(),mYear);
							mBikeChart.init(mContext,ConstApp.TYPE_BIKE,getActivity(),mYear);

							mTitleRun.setText(mContext.getString(R.string.history_run)+" - "+mYear);
							mTitleWalk.setText(mContext.getString(R.string.history_walk)+" - "+mYear);
							mTitleBike.setText(mContext.getString(R.string.history_bike)+" - "+mYear);
						}

						@Override
						public void onStartTrackingTouch(SeekBar seekBar) {

						}

						@Override
						public void onStopTrackingTouch(SeekBar seekBar) {

						}
					});
				}
			});
		}
	}
}
