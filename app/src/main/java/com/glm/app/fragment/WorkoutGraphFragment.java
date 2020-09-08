package com.glm.app.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.glm.app.ConstApp;
import com.glm.trainer.R;
import com.glm.bean.ConfigTrainer;
import com.glm.chart.NewLineChart;
import com.glm.utils.ExerciseUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class WorkoutGraphFragment extends Fragment {
	private ConfigTrainer oConfigTrainer;
	private View rootView;
	private Context mContext;
	private int mIDWorkout=0;
	private NewLineChart oChartALT;
	private NewLineChart oChartPACE;
	private NewLineChart oChartHEART;
	private RelativeLayout oGraphContainerALT;
	private RelativeLayout oGraphContainerPACE;
	private RelativeLayout oGraphContainerHeart;
	private ProgressBar oWaitForGraphALT;
	private ProgressBar oWaitForGraphPACE;
	private ProgressBar oWaitForGraphHeart;

	private ViewPager mViewPager;
	/**
	 * 
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	public WorkoutGraphFragment() {
		
	}

	public void setPager(ViewPager ViewPager) {
		mViewPager=ViewPager;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext=getActivity().getApplicationContext();
		if(rootView==null){
			rootView = inflater.inflate(R.layout.workout_graph,
					container, false);
			oGraphContainerALT = (RelativeLayout) rootView.findViewById(R.id.graphLayoutALT);
			oGraphContainerPACE = (RelativeLayout) rootView.findViewById(R.id.graphLayoutPACE);
			oGraphContainerHeart = (RelativeLayout) rootView.findViewById(R.id.graphLayoutHEART);

			oWaitForGraphALT = (ProgressBar) rootView.findViewById(R.id.waitForGraphALT);
			oWaitForGraphALT.setVisibility(View.VISIBLE);
			
			oWaitForGraphPACE = (ProgressBar) rootView.findViewById(R.id.waitForGraphPACE);
			oWaitForGraphPACE.setVisibility(View.VISIBLE);
			
			oWaitForGraphHeart = (ProgressBar) rootView.findViewById(R.id.waitForGraphHEART);
			oWaitForGraphHeart.setVisibility(View.VISIBLE);

			oChartALT = (NewLineChart) rootView.findViewById(R.id.chartALT); //new NewLineChart(mContext);
			oChartPACE = (NewLineChart) rootView.findViewById(R.id.chartPACE); //new NewLineChart(mContext);
			oChartHEART = (NewLineChart) rootView.findViewById(R.id.chartHEART); //new NewLineChart(mContext);

		}
		GraphTask oTask = new GraphTask();
        oTask.execute();

		return rootView;
	}


	public void setContext(Context context, int idWorkout) {
		mContext=context;	
		mIDWorkout=idWorkout;
	}

	public void shareWorkOut(){
		Log.v(this.getClass().getCanonicalName(),"Share WorkOut to social");
		rootView.setDrawingCacheEnabled(true);
		rootView.buildDrawingCache(true);

			new Thread(new Runnable() {
				String sPathImage = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ ConstApp.FOLDER_PERSONAL_TRAINER;

				@Override
				public void run() {

					rootView.post(new Runnable() {
						@Override
						public void run() {
							File dir = new File(sPathImage);
							//File mFile=null;
							File mFileGraph=null;
							if(!dir.exists()) {
								dir.mkdirs();
							}

							Bitmap oBmp = Bitmap.createBitmap( rootView.getWidth(), rootView.getHeight(), Bitmap.Config.ARGB_8888);
							Canvas c = new Canvas(oBmp);
							rootView.draw(c);
							try {
								if(oBmp!=null){
									//mFile = new File(sPathImage+"/dett_"+mIDWorkout+".png");
									mFileGraph = new File(sPathImage+"/graph_"+mIDWorkout+".png");
									FileOutputStream out = new FileOutputStream(mFileGraph);

									oBmp.compress(Bitmap.CompressFormat.PNG, 90, out);
									out.close();
									mViewPager.setCurrentItem(2);


								}else{
									Log.e(this.getClass().getCanonicalName(),"NULL Page Preview saving image");
									mViewPager.setCurrentItem(2);
								}
							} catch (IOException e) {
								Log.e(this.getClass().getCanonicalName(),"Error saving image");
								mViewPager.setCurrentItem(2);
							}
						}
					});
					//Task async per salvare l'immagine.
					//Bitmap oBmp =  rootView.getDrawingCache();
				}


			}).start();
	}

	class GraphTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			oConfigTrainer = ExerciseUtils.loadConfiguration(mContext);		
			//Chiamo il tackAsink
			ExerciseUtils.populateExerciseDetails(mContext, oConfigTrainer, mIDWorkout);
		    Log.v(this.getClass().getCanonicalName(), "IDExercide: " + mIDWorkout);
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					//oChartALT = new NewLineChart(mContext);
					oChartALT.init(mContext, 0);
					//oChartPACE = new NewLineChart(mContext);
					oChartPACE.init(mContext,1);
					if(oConfigTrainer.isbCardioPolarBuyed()){
						//oChartHEART = new NewLineChart(mContext);
						oChartHEART.init(mContext,2);
					}
				}
			});

			
		    
	        
	        return null;
		}
		
		@Override
		protected void onPreExecute() {
			if(oWaitForGraphALT!=null){
				oWaitForGraphALT.setVisibility(View.VISIBLE);
				oChartALT.setVisibility(View.GONE);
			}
			if(oWaitForGraphPACE!=null){
				oWaitForGraphPACE.setVisibility(View.VISIBLE);
				oGraphContainerPACE.setVisibility(View.GONE);
			}
			if(oWaitForGraphHeart!=null){
				oWaitForGraphHeart.setVisibility(View.VISIBLE);
				oGraphContainerHeart.setVisibility(View.GONE);
			}
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(Void result) {		
			if(oWaitForGraphALT!=null) oWaitForGraphALT.setVisibility(View.GONE);
			if(oWaitForGraphPACE!=null) oWaitForGraphPACE.setVisibility(View.GONE);
			if(oWaitForGraphHeart!=null) oWaitForGraphHeart.setVisibility(View.GONE);
			
			if(oGraphContainerALT!=null){
				//oGraphContainerALT.removeAllViews();
				//oGraphContainerALT.addView(oChartALT);
				oChartALT.setVisibility(View.VISIBLE);
			}
			if(oGraphContainerPACE!=null){
				//oGraphContainerPACE.removeAllViews();
				//oGraphContainerPACE.addView(oChartPACE);
				oGraphContainerPACE.setVisibility(View.VISIBLE);
			}
			if(oConfigTrainer.isbCardioPolarBuyed() && 
					oGraphContainerHeart!=null){
				//oGraphContainerHeart.removeAllViews();
				//oGraphContainerHeart.addView(oChartHEART);
				//oGraphContainerHeart.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
				oGraphContainerHeart.setVisibility(View.VISIBLE);
			}else{
				oGraphContainerHeart.setVisibility(View.GONE);
			}

			//oChartALT.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
			//oGraphContainerPACE.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
		}
	}
}