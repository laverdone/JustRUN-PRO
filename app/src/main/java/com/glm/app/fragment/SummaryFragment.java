package com.glm.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.glm.bean.DistancePerExercise;
import com.glm.chart.NewBarChart;
import com.glm.trainer.ChartSummaryActivity;
import com.glm.trainer.HistoryList;
import com.glm.trainer.NewMainActivity;
import com.glm.trainer.R;
import com.glm.utils.quickaction.ActionItem;
import com.glm.utils.quickaction.GraphSummaryQuickBar;
import com.glm.utils.quickaction.QuickAction;

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
	
	public RelativeLayout oGraph;
	private int mTypeGraph=0;

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
	public NewBarChart mChart = null;
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
			 
    	oGraph			= (RelativeLayout) rootView.findViewById(R.id.llGraph);
		mChart 			= new NewBarChart(mContext);
		mChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		mChart.init(mContext,mTypeGraph);


    	oTxtTot		= (TextView) rootView.findViewById(R.id.textDistance_tot); 
    	oTxtRun		= (TextView) rootView.findViewById(R.id.textDistance_run); 
    	oTxtWalk	= (TextView) rootView.findViewById(R.id.textDistance_walk); 
    	oTxtBike	= (TextView) rootView.findViewById(R.id.textDistance_bike); 
    	
    	oTxtKalRun	= (TextView) rootView.findViewById(R.id.textKal_run); 
    	oTxtKalWalk	= (TextView) rootView.findViewById(R.id.textKal_walk); 
    	oTxtKalBike	= (TextView) rootView.findViewById(R.id.textKal_bike);
        
    	oTxtTitle   	=  (TextView) rootView.findViewById(R.id.textHistory);
    	oWaitForLoad 	= (ProgressBar) rootView.findViewById(R.id.waitForLoad);
    	oScroll			= (ScrollView) rootView.findViewById(R.id.scrollViewSummary);


    	
    	ImageButton imgBtnMore = (ImageButton) rootView.findViewById(R.id.imgBtnMore);
	    imgBtnMore.setOnClickListener(new OnClickListener() {
	    	final GraphSummaryQuickBar oBar = new GraphSummaryQuickBar(mContext);
			@Override
			public void onClick(View v) {
				oBar.getQuickAction().setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {          
			        @Override
			        public void onItemClick(QuickAction source, int pos, int actionId) {
			                //here we can filter which action item was clicked with pos or actionId parameter
			                ActionItem actionItem = source.getActionItem(pos);
			                Log.v(this.getClass().getCanonicalName(),"Item Click Pos: "+actionItem.getActionId());
			                GraphTaskAsync oTask = new GraphTaskAsync();
			                switch (actionItem.getActionId()) {
							
			                case 1:	   
			                	mTypeGraph=0;
			                	oTask.execute();
								break;
							case 2:	
								mTypeGraph=1;
								oTask.execute();
								break;
							default:
								break;
							}	                                
			            }
			        });			
					oBar.getQuickAction().show(v);
			}
		});
    	
    	
    	Button imgBtnDett = (Button) rootView.findViewById(R.id.imgBtnDett);
    	imgBtnDett.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, HistoryList.class);
				if(((NewMainActivity)getActivity()).mConnection!=null) ((NewMainActivity)getActivity()).mConnection.destroy();
				startActivity(intent);
				getActivity().finish();
				
			}
		});
    	
    	changeGUI();
    	
    	return rootView;
	}

	public void setContext(Context context) {
		mContext=context;
	}
	@Override
	public void onResume() {
		mContext=getActivity().getApplicationContext();
		GraphTaskAsync oTask = new GraphTaskAsync();
		oTask.execute();
		super.onResume();
	}
	public void changeGUI(){
		
		if(oTable!=null && oGraph!=null && mChart!=null){
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
	        
	       
		    //Log.v(this.getClass().getCanonicalName(),"End Child: "+ oGraph.getChildCount());
		    if(oWaitForLoad!=null) {
		    	oWaitForLoad.setVisibility(View.GONE);
		    	oScroll.setVisibility(View.VISIBLE);
		    }
		}	
	}
	
	class GraphTaskAsync extends AsyncTask{
		
		@Override
		protected Object doInBackground(Object... params) {
			//oChart = new BarChart(mContext,mTypeGraph,false);
			//Looper.prepare();
			if(getActivity()==null) return false;
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mChart.init(mContext,mTypeGraph);
				}
			});


			return true;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			 //Log.v(this.getClass().getCanonicalName(),"Start Child: "+ oGraph.getChildCount());
	    	try{
		        oGraph.removeAllViews();
				mChart.setOnTouchListener(new View.OnTouchListener() {

					public boolean onTouch(View view, MotionEvent motionEvent) {

						Intent intent = new Intent();
						intent.setClass(mContext, ChartSummaryActivity.class);
						intent.putExtra("chartType",mTypeGraph);
						startActivity(intent);
						getActivity().finish();

						//getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
						//oGraph.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT));
						return false;
					}
				});

			    oGraph.addView(mChart);
	    	}catch (IllegalStateException e) {
				Log.e(this.getClass().getCanonicalName(),"Error on GRAPH");
			}
		}
		@Override
		protected void onPreExecute() {
			 //Log.v(this.getClass().getCanonicalName(),"Start Child: "+ oGraph.getChildCount());
	    	try{
		        oGraph.removeAllViews();
		    	
			    oGraph.addView(new ProgressBar(mContext));
	    	}catch (IllegalStateException e) {
				Log.e(this.getClass().getCanonicalName(),"Error on GRAPH");
			}
			super.onPreExecute();
		}
	}
}