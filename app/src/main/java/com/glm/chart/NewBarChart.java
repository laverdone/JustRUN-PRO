package com.glm.chart;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.DefaultValueFormatter;
import com.github.mikephil.charting.utils.ValueFormatter;
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
public class NewBarChart extends com.github.mikephil.charting.charts.BarChart{
    private static Context oContext;
    private int mType = 0;
    private boolean isInteractive=false;

    private String[] mMonth= {"Jan","Feb", "Mar", "Apr","May", "Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    private Typeface mTf;

    public NewBarChart(Context context) {
        super(context);
        oContext =context;
        mTf = Typeface.createFromAsset(oContext.getAssets(), "fonts/OpenSans-Regular.ttf");
    }

    public void init(Context context, int type){

        oContext =context;
        mType=type;
        switch (mType) {
            case 0:
                Log.i(this.getClass().getCanonicalName(), "Crete Graph Summary Month");

                setSummaryMonthCategory();

                break;
            case 1:
                Log.i(this.getClass().getCanonicalName(), "Crete Graph Summary Week");

                setSummaryWeekCategory();
                break;

            default:
                break;
        }
    }

    private void setSummaryWeekCategory() {
        setDrawBarShadow(false);
        setDrawValueAboveBar(true);

        setDescription("");

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
        xAxis.setSpaceBetweenLabels(2);

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
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
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
            yVals1.add(new BarEntry(nDistance,i));
        }
        BarDataSet set1 = new BarDataSet(yVals1, "Distance");
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);
        BarData data = new BarData(xVals, dataSets);

        setData(data);
    }

    private void setSummaryMonthCategory() {
        setDrawBarShadow(false);
        setDrawValueAboveBar(true);

        setDescription("");

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
        xAxis.setSpaceBetweenLabels(2);

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
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
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

        Vector<DistancePerMonth> table = ExerciseUtils.getDistanceForMonth(ExerciseUtils.loadConfiguration(oContext),
                oContext);
        Calendar oCal = Calendar.getInstance();
        oCal.setTime(new Date());



        for(int i=0;i<12;i++){
            DistancePerMonth oDistance = (DistancePerMonth) table.get(i);
            float nDistance = 0;
            try{
                nDistance = Float.parseFloat(oDistance.getsDistance());
            }catch(NumberFormatException e){
                Log.e(this.getClass().getCanonicalName(),"Error Parse Number"+oDistance.getsDistance());
            }
            //Log.v(this.getClass().getCanonicalName(),"Month: "+oDistance.getiMonth()+" - Distance: "+nDistance);

            xVals.add(mMonth[oDistance.getiMonth()-1]);
            yVals1.add(new BarEntry(nDistance,i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "Distance");
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);
        BarData data = new BarData(xVals, dataSets);

        setData(data);
    }
}