package com.glm.app;

import com.glm.chart.NewBarChart;
import com.glm.trainer.util.SystemUiHider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.LinearLayout;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class ChartSummaryActivity extends AppCompatActivity {
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
    private int mTypeGraph=-1;
    private int iYear=0;
    public NewBarChart oChart = null;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mContext=this;

        Intent intent = getIntent();

        mTypeGraph=intent.getIntExtra("chartType",-1);
        iYear=intent.getIntExtra("year",2015);

        Log.v(this.getClass().getCanonicalName(),"#########  ChartSummaryActivity is start");

        oChart = new NewBarChart(mContext);
        oChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        oChart.setBackgroundColor(Color.WHITE);
        oChart.init(mContext,mTypeGraph,this, iYear);
        setContentView(oChart);
        Log.v(this.getClass().getCanonicalName(),"######### 1 ChartSummaryActivity is start");
//        a = AnimationUtils.loadAnimation(this, R.animator.fadein);
//        a.reset();



        Log.v(this.getClass().getCanonicalName(),"######### 3 ChartSummaryActivity is start");
        //GraphTaskAsync oTask = new GraphTaskAsync();
        //oTask.execute();

    }
    @Override
    protected void onPause() {
  //      a = AnimationUtils.loadAnimation(this, R.animator.disappear);
  //      a.reset();


        finish();
        super.onPause();
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
