package com.glm.chart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import com.github.mikephil.charting.formatter.ValueFormatter;
import com.glm.bean.ExerciseManipulate;
import com.glm.bean.WatchPoint;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by gianluca on 25/08/15.
 */
public class NewLineChart extends com.github.mikephil.charting.charts.LineChart{
    private static Context oContext;
    private int mType = 0;
    private boolean isInteractive=false;

    private Typeface mTf;

    public NewLineChart(Context context) {
        super(context);
        oContext =context;
        mTf = Typeface.createFromAsset(oContext.getAssets(), "fonts/OpenSans-Regular.ttf");
    }

    public NewLineChart(Context context, AttributeSet attributeSet){
        super(context,attributeSet);

    }
    public void init(Context context, int type){

        oContext =context;
        mType=type;
        setBackgroundColor(Color.WHITE);
        switch (mType) {
            //Grafico Weight
            case -1:
                Log.i(this.getClass().getCanonicalName(), "Create Graph Weight");
                setWeightCategory();
                break;
            case 0:
                Log.i(this.getClass().getCanonicalName(), "Create Graph ALT");
                setALTCategory();
                break;
            case 1:
                Log.i(this.getClass().getCanonicalName(), "Create Graph Pace");
                setPaceCategory();
                break;
            case 2:
                Log.i(this.getClass().getCanonicalName(), "Create Graph BPM");
                setBPMCategory();
                break;
            default:
                break;
        }
    }

    private void setALTCategory() {
        DecimalFormat mFormat = new DecimalFormat("###.###");

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
        xAxis.setTextSize(9f);
        xAxis.setDrawGridLines(false);
        //xAxis.setSpaceBetweenLabels(2);

        ValueFormatter custom = new GlmValueFormatter(true);

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
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });


        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        ArrayList<WatchPoint> aWatchPoint = ExerciseManipulate.getWatchPoint();
        double dMin=0.0;
        if(aWatchPoint.size()==0) return;
        int iWPSize=aWatchPoint.size()-2;
        for(int i=0;i<iWPSize;i++){
            if(dMin==0.0){
                dMin=aWatchPoint.get(i).getdAlt();
            }else if (dMin>aWatchPoint.get(i).getdAlt()){
                dMin=aWatchPoint.get(i).getdAlt();
            }
            //Log.v(this.getClass().getCanonicalName(),"Add to Series Distance: "+Math.round(aWatchPoint.get(i).getdDistance())+" Alt:"+aWatchPoint.get(i).getdAlt());
            //oSerie.add(Math.round(aWatchPoint.get(i).getdDistance()),aWatchPoint.get(i).getdAlt());
            xVals.add(mFormat.format(aWatchPoint.get(i).getdDistance()));
            yVals1.add(new Entry(i,(float) aWatchPoint.get(i).getdAlt()));
        }
        //oSerie.add(Math.round(aWatchPoint.get(aWatchPoint.size()-1).getdDistance()),
        //        aWatchPoint.get(aWatchPoint.size()-1).getdAlt());
        //dataset.addSeries(oSerie);
        if(iWPSize<=0) return;
        LineDataSet set1 = new LineDataSet(yVals1, "ALT");

        set1.enableDashedLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(2f);
        set1.setCircleRadius(0f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setFillAlpha(65);

        set1.setFillColor(Color.BLACK);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCubicIntensity(0.2f);
        set1.setDrawFilled(true);
        set1.setDrawCircles(false);
        set1.setLineWidth(1.8f);
        set1.setCircleRadius(4f);
//        set1.setDrawFilled(true);
        // set1.setShader(new LinearGradient(0, 0, 0, mChart.getHeight(),
        // Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
		//LineData data = new LineData(xVals, dataSets);
		LineData data = new LineData(set1);
        setData(data);

        animateX(2500, Easing.EaseInOutQuart);
//        mChart.invalidate();

        // get the legend (only possible after setting data)
        l = getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
    }

    private void setPaceCategory() {
        DecimalFormat mFormat = new DecimalFormat("###.###");
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
        xAxis.setTextSize(9f);
        xAxis.setDrawGridLines(false);
        //xAxis.setSpaceBetweenLabels(2);

        ValueFormatter custom = new GlmValueFormatter(true);

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
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });


        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        ArrayList<WatchPoint> aWatchPoint = ExerciseManipulate.getWatchPoint();

        if(aWatchPoint.size()==0) return;
        int iWPSize=aWatchPoint.size()-2;
        for(int i=0;i<iWPSize;i++){
            //Log.v(this.getClass().getCanonicalName(),"Add to Series Distance: "+Math.round(aWatchPoint.get(i).getdDistance())+" Pace:"+aWatchPoint.get(i).getdPace());
            //oSerie.add(Math.round(aWatchPoint.get(i).getdDistance()),aWatchPoint.get(i).getdPace());
            xVals.add(mFormat.format(aWatchPoint.get(i).getdDistance()));
            yVals1.add(new Entry(i,(float) aWatchPoint.get(i).getdPace()));
        }
        //oSerie.add(Math.round(aWatchPoint.get(aWatchPoint.size()-1).getdDistance()),
        //        aWatchPoint.get(aWatchPoint.size()-1).getdPace());
        //dataset.addSeries(oSerie);
        if(iWPSize<=0) return;
        LineDataSet set1 = new LineDataSet(yVals1, "Pace");

        set1.enableDashedLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(2f);
        set1.setCircleRadius(0f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setFillAlpha(65);

        set1.setFillColor(Color.BLACK);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCubicIntensity(0.2f);
        set1.setDrawFilled(true);
        set1.setDrawCircles(false);
        set1.setLineWidth(1.8f);
        set1.setCircleRadius(4f);
//        set1.setDrawFilled(true);
        // set1.setShader(new LinearGradient(0, 0, 0, mChart.getHeight(),
        // Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
		//LineData data = new LineData(xVals, dataSets);
		LineData data = new LineData(set1);

        setData(data);

        animateX(2500, Easing.EaseInOutQuart);
//        mChart.invalidate();

        // get the legend (only possible after setting data)
        l = getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
    }

    private void setBPMCategory() {
        DecimalFormat mFormat = new DecimalFormat("###.###");
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
        xAxis.setTextSize(9f);
        xAxis.setDrawGridLines(false);
        //xAxis.setSpaceBetweenLabels(2);

        ValueFormatter custom = new GlmValueFormatter(true);

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
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });


        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        ArrayList<WatchPoint> aWatchPoint = ExerciseManipulate.getWatchPoint();

        if(aWatchPoint.size()==0) return;
        int iWPSize=aWatchPoint.size()-2;
        for(int i=0;i<iWPSize;i++){
            //Log.v(this.getClass().getCanonicalName(),"Add to Series Distance: "+Math.round(aWatchPoint.get(i).getdDistance())+" Pace:"+aWatchPoint.get(i).getdPace());
            //oSerie.add(Math.round(aWatchPoint.get(i).getdDistance()),aWatchPoint.get(i).getdPace());
            xVals.add(mFormat.format(aWatchPoint.get(i).getdDistance()));
            yVals1.add(new Entry(i,(float) aWatchPoint.get(i).getBpm()));
        }
        //oSerie.add(Math.round(aWatchPoint.get(aWatchPoint.size()-1).getdDistance()),
        //        aWatchPoint.get(aWatchPoint.size()-1).getdPace());
        //dataset.addSeries(oSerie);
        if(iWPSize<=0) return;
        LineDataSet set1 = new LineDataSet(yVals1, "Bpm");

        set1.enableDashedLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(2f);
        set1.setCircleRadius(0f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setFillColor(Color.BLACK);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCubicIntensity(0.2f);
        set1.setDrawFilled(true);
        set1.setDrawCircles(false);
        set1.setLineWidth(1.8f);
        set1.setCircleRadius(4f);
//        set1.setDrawFilled(true);
        // set1.setShader(new LinearGradient(0, 0, 0, mChart.getHeight(),
        // Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
		//LineData data = new LineData(xVals, dataSets);
		LineData data = new LineData(set1);

        setData(data);

        animateX(2500, Easing.EaseInOutQuart);
//        mChart.invalidate();

        // get the legend (only possible after setting data)
        l = getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
    }

    private void setWeightCategory() {
        DecimalFormat mFormat = new DecimalFormat("###.###");
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
        xAxis.setTextSize(9f);
        xAxis.setDrawGridLines(false);
        //xAxis.setSpaceBetweenLabels(2);

        //ValueFormatter custom = new MyValueFormatter();

        YAxis leftAxis = getAxisLeft();
        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(8, false);
        //leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);

        YAxis rightAxis = getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(8, false);
        //rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);

        Legend l = getLegend();
        //l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });


        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        ArrayList<WatchPoint> aWatchPoint = ExerciseManipulate.getWatchPoint();

        if(aWatchPoint.size()==0) return;
        int iWPSize=aWatchPoint.size()-2;
        for(int i=0;i<iWPSize;i++){
            //Log.v(this.getClass().getCanonicalName(),"Add to Series Distance: "+Math.round(aWatchPoint.get(i).getdDistance())+" Pace:"+aWatchPoint.get(i).getdPace());
            //oSerie.add(Math.round(aWatchPoint.get(i).getdDistance()),aWatchPoint.get(i).getdPace());

            xVals.add(mFormat.format(aWatchPoint.get(i).getdDistance()));
            yVals1.add(new Entry(i,(float) aWatchPoint.get(i).getBpm()));
        }
        //oSerie.add(Math.round(aWatchPoint.get(aWatchPoint.size()-1).getdDistance()),
        //        aWatchPoint.get(aWatchPoint.size()-1).getdPace());
        //dataset.addSeries(oSerie);
        if(iWPSize<=0) return;
        LineDataSet set1 = new LineDataSet(yVals1, "Weight");

        set1.enableDashedLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(2f);
        set1.setCircleRadius(0f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setFillColor(Color.BLACK);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCubicIntensity(0.2f);
        set1.setDrawFilled(true);
        set1.setDrawCircles(false);
        set1.setLineWidth(1.8f);
        set1.setCircleRadius(4f);
//        set1.setDrawFilled(true);
        // set1.setShader(new LinearGradient(0, 0, 0, mChart.getHeight(),
        // Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
		//LineData data = new LineData(xVals, dataSets);
		LineData data = new LineData(set1);
        setData(data);

        animateX(2500, Easing.EaseInOutQuart);
//        mChart.invalidate();

        // get the legend (only possible after setting data)
        l = getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
    }
}
