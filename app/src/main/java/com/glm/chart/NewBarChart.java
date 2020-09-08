package com.glm.chart;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.glm.app.ConstApp;
import com.glm.bean.DistancePerMonth;
import com.glm.bean.DistancePerWeek;
import com.glm.utils.ExerciseUtils;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

/**
 * Created by gianluca on 25/08/15.
 */
public class NewBarChart extends BarChart {
    private static Context oContext;
    private int mType = 0;
    private boolean isInteractive=false;

    private String[] mMonth= {"Jan","Feb", "Mar", "Apr","May", "Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    private Typeface mTf;

    private Activity mActivity;

    public NewBarChart(Context context) {
        super(context);
        oContext =context;
        mTf = Typeface.createFromAsset(oContext.getAssets(), "fonts/OpenSans-Regular.ttf");
    }

    public void init(Context context, int type, Activity activity, int iYear){
        mActivity=activity;
        oContext =context;
        mType=type;
        switch (mType) {
            case -1:
                //ALL Workout Type by Month
                Log.i(this.getClass().getCanonicalName(), "Create Graph Total Summary Month");

                setSummaryMonthCategory(-1,iYear);

                break;
            case -2:
                Log.i(this.getClass().getCanonicalName(), "Create Graph Summary Week");

                setSummaryWeekCategory();
                break;

            case ConstApp.TYPE_RUN:
                //RUNNING Workout Type by Month
                Log.i(this.getClass().getCanonicalName(), "Create Graph Running Summary Month");

                setSummaryMonthCategory(ConstApp.TYPE_RUN,iYear);
                //testNewBar(ConstApp.TYPE_RUN,iYear);
                break;

            case ConstApp.TYPE_WALK:
                //WALKING Workout Type by Month
                Log.i(this.getClass().getCanonicalName(), "Create Graph Walking Summary Month");

                setSummaryMonthCategory(ConstApp.TYPE_WALK,iYear);

                break;
            case ConstApp.TYPE_BIKE:
                //BIKING Workout Type by Month
                Log.i(this.getClass().getCanonicalName(), "Create Graph Biking Summary Month");

                setSummaryMonthCategory(ConstApp.TYPE_BIKE,iYear);

                break;
            default:
                break;
        }
    }


    private void setSummaryMonthCategory(final int typeWorkout, final int iYear){
        new Thread(new Runnable() {
            @Override
            public void run() {
                setDrawBarShadow(false);
                setDrawValueAboveBar(true);

                getDescription().setEnabled(false);

                // if more than 60 entries are displayed in the chart, no values will be
                // drawn
                setMaxVisibleValueCount(60);

                // scaling can now only be done on x- and y-axis separately
                setPinchZoom(false);

                setDrawGridBackground(false);
                // chart.setDrawYLabels(false);

                ValueFormatter xAxisFormatter = new MonthAxisValueFormatter();

                XAxis xAxis = getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setTypeface(mTf);
                xAxis.setDrawGridLines(false);
                xAxis.setGranularity(1f); // only intervals of 1 day
                xAxis.setLabelCount(12);
                xAxis.setValueFormatter(xAxisFormatter);

                ValueFormatter custom = new GlmValueFormatter();

                YAxis leftAxis = getAxisLeft();
                leftAxis.setTypeface(mTf);
                leftAxis.setLabelCount(8, false);
                leftAxis.setValueFormatter(custom);
                leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                leftAxis.setSpaceTop(15f);
                leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                YAxis rightAxis = getAxisRight();
                rightAxis.setDrawGridLines(false);
                rightAxis.setTypeface(mTf);
                rightAxis.setLabelCount(8, false);
                rightAxis.setValueFormatter(custom);
                rightAxis.setSpaceTop(15f);
                rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                Legend l = getLegend();
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                l.setDrawInside(false);
                l.setForm(Legend.LegendForm.SQUARE);
                l.setFormSize(9f);
                l.setTextSize(11f);
                l.setXEntrySpace(4f);

                XYMarkerView mv = new XYMarkerView(oContext, xAxisFormatter);
                mv.setChartView(NewBarChart.this); // For bounds control
                setMarker(mv); // Set the marker to the chart



        //        getData().setHighlightEnabled(true);

                ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

                Vector<DistancePerMonth> table = ExerciseUtils.getDistanceForMonth(typeWorkout, ExerciseUtils.loadConfiguration(oContext),
                        iYear ,oContext);
                Calendar oCal = Calendar.getInstance();
                oCal.setTime(new Date());



                for(int i=0;i<12;i++){
                    DistancePerMonth oDistance = table.get(i);
                    float nDistance = 0;
                    try{
                        nDistance = Float.parseFloat(oDistance.getsDistance());
                    }catch(NumberFormatException e){
                        Log.e(this.getClass().getCanonicalName(),"Error Parse Number"+oDistance.getsDistance());
                    }
                    //Log.v(this.getClass().getCanonicalName(),"Month: "+oDistance.getiMonth()+" - Distance: "+nDistance);

                    yVals1.add(new BarEntry(i,nDistance));
                }



                BarDataSet set1 = new BarDataSet(yVals1, "Distance");
                ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);

                BarData data = new BarData(dataSets);
                data.setValueTextSize(10f);
                data.setValueTypeface(mTf);
                data.setBarWidth(0.9f);
                setData(data);
                for (IBarDataSet set : getData().getDataSets())
                    ((BarDataSet) set).setBarBorderWidth(set.getBarBorderWidth() == 1.f ? 0.f : 1.f);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                });
            }
        }).start();
    }


    private void setSummaryWeekCategory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setDrawBarShadow(false);
                setDrawValueAboveBar(true);



                // if more than 60 entries are displayed in the chart, no values will be
                // drawn
                setMaxVisibleValueCount(60);

                // scaling can now only be done on x- and y-axis separately
                setPinchZoom(false);

                // draw shadows for each bar that show the maximum value
                // mChart.setDrawBarShadow(true);

                // mChart.setDrawXLabels(false);

                setDrawGridBackground(false);
                // mChart.setDrawYLabels(false);



                XAxis xAxis = getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setTypeface(mTf);
                xAxis.setTextSize(8f);
                xAxis.setDrawGridLines(false);
                //xAxis.setL .setSpaceBetweenLabels(2);

                ValueFormatter custom = new GlmValueFormatter();


                YAxis leftAxis = getAxisLeft();
                leftAxis.setTypeface(mTf);
                leftAxis.setLabelCount(8, false);
                leftAxis.setValueFormatter(custom);
                leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                leftAxis.setSpaceTop(15f);

                YAxis rightAxis = getAxisRight();
                rightAxis.setDrawGridLines(false);
                rightAxis.setTypeface(mTf);
                rightAxis.setValueFormatter(custom);
                rightAxis.setLabelCount(8, false);
                rightAxis.setValueFormatter(custom);
                rightAxis.setSpaceTop(15f);

                Legend l = getLegend();
                //l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
                l.setForm(Legend.LegendForm.SQUARE);
                l.setFormSize(9f);
                l.setTextSize(9f);
                l.setXEntrySpace(4f);
                // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
                // "def", "ghj", "ikl", "mno" });
                // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
                // "def", "ghj", "ikl", "mno" });


                ArrayList<String> xVals = new ArrayList<String>();
                ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

                Vector<DistancePerWeek> table = ExerciseUtils.getDistanceForWeeks( ExerciseUtils.loadConfiguration(oContext),
                        oContext);
                Calendar oCal = Calendar.getInstance();
                oCal.setTime(new Date());
                int mSize=table.size();
                for(int i=0;i<mSize-1;i++){
                    DistancePerWeek oDistance = table.get(i);
                    float nDistance = 0;
                    try{
                        nDistance = Float.parseFloat(oDistance.getsDistance());
                    }catch(NumberFormatException e){
                        Log.e(this.getClass().getCanonicalName(),"Error Parse Number"+oDistance.getsDistance());
                    }
                    //Log.v(this.getClass().getCanonicalName(),"Week: "+oDistance.getiWeek()+" - Distance: "+nDistance);

                    xVals.add(oDistance.getiWeek() + " " + oCal.get(Calendar.YEAR));
                    yVals1.add(new BarEntry(i,nDistance));
                }
                if(mSize<=0) return;
                BarDataSet set1 = new BarDataSet(yVals1, "Distance");
                ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
                dataSets.add(set1);
                //BarData data = new BarData(xVals, dataSets);
                BarData data = new BarData(set1);

                setData(data);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                });

            }
        }).start();

    }

    /*private void setSummaryMonthCategory(final int typeWorkout, final int iYear) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setDrawBarShadow(false);
                setDrawValueAboveBar(true);

                //setDescription("");

                // if more than 60 entries are displayed in the chart, no values will be
                // drawn
                setMaxVisibleValueCount(60);

                // scaling can now only be done on x- and y-axis separately
                setPinchZoom(false);

                // draw shadows for each bar that show the maximum value
                // mChart.setDrawBarShadow(true);

                // mChart.setDrawXLabels(false);

                setDrawGridBackground(false);
                // mChart.setDrawYLabels(false);

                XAxis xAxis = getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setTypeface(mTf);
                xAxis.setTextSize(8f);
                xAxis.setDrawGridLines(false);


                //xAxis.setSpaceBetweenLabels(2);

                ValueFormatter custom = new GlmValueFormatter();

                YAxis leftAxis = getAxisLeft();
                leftAxis.setTypeface(mTf);
                leftAxis.setLabelCount(8, false);
                leftAxis.setValueFormatter(custom);
                leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                leftAxis.setSpaceTop(15f);



                YAxis rightAxis = getAxisRight();
                rightAxis.setDrawGridLines(false);
                rightAxis.setTypeface(mTf);
                rightAxis.setLabelCount(8, false);
                rightAxis.setValueFormatter(custom);
                rightAxis.setSpaceTop(15f);

                Legend l = getLegend();
                //l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                l.setForm(Legend.LegendForm.SQUARE);
                l.setFormSize(9f);
                l.setTextSize(9f);
                l.setXEntrySpace(4f);
                // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
                // "def", "ghj", "ikl", "mno" });
                // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
                // "def", "ghj", "ikl", "mno" });


                ArrayList<String> xVals = new ArrayList<String>();
                ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

                Vector<DistancePerMonth> table = ExerciseUtils.getDistanceForMonth(typeWorkout, ExerciseUtils.loadConfiguration(oContext),
                        iYear ,oContext);
                Calendar oCal = Calendar.getInstance();
                oCal.setTime(new Date());



                for(int i=0;i<12;i++){
                    DistancePerMonth oDistance = table.get(i);
                    float nDistance = 0;
                    try{
                        nDistance = Float.parseFloat(oDistance.getsDistance());
                    }catch(NumberFormatException e){
                        Log.e(this.getClass().getCanonicalName(),"Error Parse Number"+oDistance.getsDistance());
                    }
                    //Log.v(this.getClass().getCanonicalName(),"Month: "+oDistance.getiMonth()+" - Distance: "+nDistance);

                    xVals.add(mMonth[oDistance.getiMonth()-1]);
                    yVals1.add(new BarEntry(i,nDistance));
                }

                BarDataSet set1 = new BarDataSet(yVals1, "distance");
                set1.setDrawIcons(false);
                set1.setValues(yVals1);

                ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);

                BarData data = new BarData(dataSets);
                setData(data);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                });
            }
        }).start();

    }*/
}
