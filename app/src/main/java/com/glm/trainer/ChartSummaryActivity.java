package com.glm.trainer;

import com.glm.chart.NewBarChart;
import com.glm.trainer.util.SystemUiHider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class ChartSummaryActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    private Animation a;
    private int mTypeGraph=0;
    public NewBarChart oChart = null;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Intent intent = getIntent();

        mTypeGraph=intent.getIntExtra("chartType",0);




        oChart = new NewBarChart(mContext);
        oChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        oChart.setBackgroundColor(Color.WHITE);
        oChart.init(mContext,mTypeGraph);
        setContentView(oChart);

        a = AnimationUtils.loadAnimation(this, R.animator.fadein);
        a.reset();

        if(findViewById(R.id.chartfull)!=null) {
            findViewById(R.id.chartfull).clearAnimation();
            findViewById(R.id.chartfull).setAnimation(a);
        }

        GraphTaskAsync oTask = new GraphTaskAsync();
        oTask.execute();

    }










    @Override
    protected void onPause() {
        a = AnimationUtils.loadAnimation(this, R.animator.disappear);
        a.reset();

        if(findViewById(R.id.chartfull)!=null) {
            findViewById(R.id.chartfull).clearAnimation();
            findViewById(R.id.chartfull).setAnimation(a);
        }
        super.onPause();
    }

    class GraphTaskAsync extends AsyncTask {

        @Override
        protected Object doInBackground(Object... params) {
            return true;
        }

        @Override
        protected void onPostExecute(Object result) {
            //Log.v(this.getClass().getCanonicalName(),"Start Child: "+ oGraph.getChildCount());
            try{
                //((FrameLayout) findViewById(R.id.chartfull)).removeAllViews();
                /*oChart.setOnTouchListener(new View.OnTouchListener() {

                    public boolean onTouch(View viewoGraph, MotionEvent motionEvent) {

                        Intent intent = new Intent();
                        intent.setClass(mContext, ChartSummaryActivity.class);
                        intent.putExtra("chartType",mTypeGraph);
                        startActivity(intent);
                        getActivity().finish();

                        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        //oGraph.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT));
                        return false;
                    }
                });*/
                //oChart.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT));
                //((FrameLayout) findViewById(R.id.chartfull)).addView(oChart);
            }catch (IllegalStateException e) {
                Log.e(this.getClass().getCanonicalName(), "Error on GRAPH");
            }
        }
        @Override
        protected void onPreExecute() {
            //Log.v(this.getClass().getCanonicalName(),"Start Child: "+ oGraph.getChildCount());
            try{
                //((FrameLayout) findViewById(R.id.chartfull)).removeAllViews();

                //((FrameLayout) findViewById(R.id.chartfull)).addView(new ProgressBar(mContext));
            }catch (IllegalStateException e) {
                Log.e(this.getClass().getCanonicalName(),"Error on GRAPH");
            }
            super.onPreExecute();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(mContext, NewMainActivity.class);
        intent.putExtra("sectionFocus",1);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
