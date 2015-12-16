package com.glm.chart;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import com.glm.bean.DistancePerMonth;
import com.glm.bean.DistancePerWeek;
import com.glm.trainer.R;
import com.glm.utils.ExerciseUtils;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.axis.DateAxis;
import org.afree.chart.axis.NumberAxis;
import org.afree.chart.plot.PlotOrientation;
import org.afree.chart.plot.XYPlot;
import org.afree.chart.renderer.xy.XYItemRenderer;
import org.afree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.afree.data.time.Month;
import org.afree.data.time.TimeSeries;
import org.afree.data.time.TimeSeriesCollection;
import org.afree.data.time.Week;
import org.afree.data.xy.IntervalXYDataset;
import org.afree.graphics.SolidColor;
import org.afree.ui.RectangleInsets;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class BarChart extends Chart{
	private static Context oContext;
	private int mType = 0;
	private boolean isInteractive=false;
	/**
	 * Crea un grafico a linea di tipo Time Series
	 * param 
	 * int Type=0 Summary Month
	 * int Type=1 Summary Week          
	 * */
	public BarChart(Context context,int type, boolean interactive) {

		super(context);
		oContext		=	context;
		mType 			= type;
		isInteractive 	= interactive;
		switch (mType) {		
		case 0:
			Log.i(this.getClass().getCanonicalName(), "Crete Graph Summary Month");
			
			final AFreeChart chartSummaryMonth = createSummaryMonthChart(setSummaryMonthCategory());
			setChart(chartSummaryMonth);
			break;	
		case 1:
			Log.i(this.getClass().getCanonicalName(), "Crete Graph Summary Week");
			
			final AFreeChart chartSummaryWeek = createSummaryWeekChart(setSummaryWeekCategory());
			setChart(chartSummaryWeek);
			break;	
				
		default:
			break;
		} 
	}
	public BarChart(Activity activity, Context context,int Type) {
		super(context);
		oContext=context;
		
		switch (Type) {		
		case 0:
			//Log.i(this.getClass().getCanonicalName(), "Crete Graph Summary Month");
			
			final AFreeChart chartSummaryMonth = createSummaryMonthChart(setSummaryMonthCategory());
			setChart(chartSummaryMonth);
			break;	
		case 1:
			//Log.i(this.getClass().getCanonicalName(), "Crete Graph Summary Week");
			
			final AFreeChart chartSummaryWeek = createSummaryWeekChart(setSummaryWeekCategory());
			setChart(chartSummaryWeek);
			break;	
				
		default:
			break;
		} 
	}
	public void initChart(Context context,int type,boolean interactive){
		oContext		= context;
		mType 			= type;
		isInteractive 	= interactive;
		switch (mType) {
			case 0:
				Log.i(this.getClass().getCanonicalName(), "Crete Graph Summary Month");

				final AFreeChart chartSummaryMonth = createSummaryMonthChart(setSummaryMonthCategory());
				setChart(chartSummaryMonth);
				break;
			case 1:
				Log.i(this.getClass().getCanonicalName(), "Crete Graph Summary Week");

				final AFreeChart chartSummaryWeek = createSummaryWeekChart(setSummaryWeekCategory());
				setChart(chartSummaryWeek);
				break;

			default:
				break;
		}
	}
	/**
	 * Imposta la serie per il grafico diviso per mese (anno corrente)
	 * 
	 * */
	private IntervalXYDataset setSummaryMonthCategory() {
		TimeSeriesCollection oDataSet = new TimeSeriesCollection();		
		TimeSeries oSerie = new TimeSeries(oContext.getString(R.string.distance));
		

		Vector<DistancePerMonth> table = ExerciseUtils.getDistanceForMonth( ExerciseUtils.loadConfiguration(oContext), 
				oContext);
		Calendar oCal = Calendar.getInstance();
		oCal.setTime(new Date());
		
		for(int i=0;i<11;i++){
			DistancePerMonth oDistance = (DistancePerMonth) table.get(i);	
			Number nDistance = 0;
			try{
				nDistance = Integer.parseInt(oDistance.getsDistance());
			}catch(NumberFormatException e){
				Log.e(this.getClass().getCanonicalName(),"Error Parse Number"+oDistance.getsDistance());
			}
			//Log.v(this.getClass().getCanonicalName(),"Month: "+oDistance.getiMonth()+" - Distance: "+nDistance);
			oSerie.add(new Month(oDistance.getiMonth(), oCal.get(Calendar.YEAR)),nDistance);
			oDistance=null;
			nDistance=null;
		}
		
		Number nDistance = Integer.parseInt(((DistancePerMonth) table.get(table.size()-1)).getsDistance());
		oSerie.add(new Month(((DistancePerMonth) table.get(table.size()-1)).getiMonth(), oCal.get(Calendar.YEAR)),
				nDistance);
		
		nDistance=null;
		oDataSet.addSeries(oSerie);
		return oDataSet;
	}

	
	
    /**
     * Creates a chart.
     *
     * @param dataset  a dataset.
     *
     * @return A chart.
     */
    private static AFreeChart createSummaryMonthChart(IntervalXYDataset dataset) {
    	//ChartFactory.createXYBarChart(title, xAxisLabel, dateAxis, yAxisLabel, dataset, orientation, legend, tooltips, urls)
        AFreeChart chart = ChartFactory.createXYBarChart(
        		null,  // title
        		null,    // x-axis label
        		true,
        		null,   // y-axis label
            dataset,            // data
            PlotOrientation.VERTICAL,
            false,               // create legend?
            false,               // generate tooltips?
            false               // generate URLs?
        );

        chart.setBackgroundPaintType(new SolidColor(Color.WHITE));

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaintType(new SolidColor(Color.LTGRAY));
        plot.setDomainGridlinePaintType(new SolidColor(Color.WHITE));
        plot.setRangeGridlinePaintType(new SolidColor(Color.WHITE));
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        chart.setTitle(oContext.getString(R.string.month));
        
        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
            renderer.setDrawSeriesLineAsPath(true);
        }
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("MMM"));       
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        return chart;

    }
    
    /**
	 * Imposta la serie per il grafico diviso per settimana corrente (Mese Corrente)
	 * 
	 * */
	private IntervalXYDataset setSummaryWeekCategory() {
		TimeSeriesCollection oDataSet = new TimeSeriesCollection();		
		TimeSeries oSerie = new TimeSeries(oContext.getString(R.string.distance));
		

		Vector<DistancePerWeek> table = ExerciseUtils.getDistanceForWeeks( ExerciseUtils.loadConfiguration(oContext), 
				oContext);
		Calendar oCal = Calendar.getInstance();
		oCal.setTime(new Date());
		int mSize=table.size();
		for(int i=0;i<mSize-1;i++){
			DistancePerWeek oDistance = (DistancePerWeek) table.get(i);	
			Number nDistance = 0;
			try{
				nDistance = Integer.parseInt(oDistance.getsDistance());
			}catch(NumberFormatException e){
				Log.e(this.getClass().getCanonicalName(),"Error Parse Number"+oDistance.getsDistance());
			}
			//Log.v(this.getClass().getCanonicalName(),"Week: "+oDistance.getiWeek()+" - Distance: "+nDistance);
			
			

			oSerie.add(new Week(oDistance.getiWeek(),oCal.get(Calendar.YEAR)),nDistance);
			oDistance=null;
			nDistance=null;
		}
		
		Number nDistance = Integer.parseInt(((DistancePerWeek) table.get(table.size()-1)).getsDistance());
		oSerie.add(new Week(((DistancePerWeek) table.get(table.size()-1)).getiWeek(), oCal.get(Calendar.YEAR)),
				nDistance);
		
		nDistance=null;
		oDataSet.addSeries(oSerie);
		return oDataSet;
	}

	
	
    /**
     * Creates a chart.
     *
     * @param dataset  a dataset.
     *
     * @return A chart.
     */
    private static AFreeChart createSummaryWeekChart(IntervalXYDataset dataset) {
    	//ChartFactory.createXYBarChart(title, xAxisLabel, dateAxis, yAxisLabel, dataset, orientation, legend, tooltips, urls)
        AFreeChart chart = ChartFactory.createXYBarChart(
        		null,  // title
        		null,    // x-axis label
        		true,
        		null,   // y-axis label
            dataset,            // data
            PlotOrientation.VERTICAL,
            false,               // create legend?
            false,               // generate tooltips?
            false               // generate URLs?
        );

        chart.setBackgroundPaintType(new SolidColor(Color.WHITE));

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaintType(new SolidColor(Color.LTGRAY));
        plot.setDomainGridlinePaintType(new SolidColor(Color.WHITE));
        plot.setRangeGridlinePaintType(new SolidColor(Color.WHITE));
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        chart.setTitle(oContext.getString(R.string.week));
        
        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
            renderer.setDrawSeriesLineAsPath(true);
        }
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        //axis.setDateFormatOverride(new SimpleDateFormat("WWW"));       
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        return chart;

    }
    
    
    /**
     * touch event
     */
    public boolean onTouchEvent(MotionEvent ev) { 
        if(isInteractive){
			super.onTouchEvent(ev);
		}else {
			int action = ev.getAction();
			int count = ev.getPointerCount();

			/*oWaitForSave = ProgressDialog.show(oActivity,oContext.getString(R.string.app_name_buy),oContext.getString(R.string.app_name_buy),true,true,null);

			if(oWaitForSave!=null){
				oWaitForSave.setOnCancelListener(new DialogInterface.OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						oWaitForSave.dismiss();
						Log.v(this.getClass().getCanonicalName(),"Cancel Save");
					}
				});
			}   */
			switch (action & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_POINTER_DOWN:

					touched(ev);

					break;
				case MotionEvent.ACTION_MOVE:
					Log.i("TouchEvent", "ACTION_MOVE");

					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:

			}
		}
        return true;
    }


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		Log.d(this.getClass().getCanonicalName(),"widthMeasureSpec: "+widthMeasureSpec+" heightMeasureSpec: "+heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
