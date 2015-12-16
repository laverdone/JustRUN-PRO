package com.glm.chart;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import com.glm.bean.Exercise;
import com.glm.bean.ExerciseManipulate;
import com.glm.bean.WatchPoint;
import com.glm.trainer.R;
import com.glm.utils.ExerciseUtils;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.axis.DateAxis;
import org.afree.chart.axis.NumberAxis;
import org.afree.chart.plot.PlotOrientation;
import org.afree.chart.plot.XYPlot;
import org.afree.data.general.SeriesException;
import org.afree.data.time.Day;
import org.afree.data.time.TimeSeries;
import org.afree.data.time.TimeSeriesCollection;
import org.afree.data.xy.XYDataset;
import org.afree.data.xy.XYSeries;
import org.afree.data.xy.XYSeriesCollection;
import org.afree.graphics.SolidColor;
import org.afree.ui.RectangleInsets;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

public class LineChart extends Chart{
	
	private static Context oContext;
	
	/**
	 * Crea un grafico a linea di tipo Time Series
	 * param int Type=-1 Weight
	 * 			 Type=0 ALT
	 * 			 Type=1 PACE
	 *           Type=2 BPM
	 *           
	 * */
	public LineChart(Context context,int Type) {
		super(context);
		oContext=context;
		
		
		switch (Type) {
		//Grafico Weight
		case -1:
			Log.i(this.getClass().getCanonicalName(), "Crete Graph Weight");		
			final AFreeChart chartWeight = createWeightChart(setWeightCategory());
			setChart(chartWeight);
			break;
		case 0:
			Log.i(this.getClass().getCanonicalName(), "Crete Graph ALT");
			
			final AFreeChart chartALT = createALTChart(setALTCategory());
			setChart(chartALT);
			break;
		case 1:
			Log.i(this.getClass().getCanonicalName(), "Crete Graph Pace");
			
			final AFreeChart chartPace = createPaceChart(setPaceCategory());
			setChart(chartPace);
			break;	
		case 2:
			Log.i(this.getClass().getCanonicalName(), "Crete Graph BPM");
			
			final AFreeChart chartBPM = createBPMChart(setBPMCategory());
			setChart(chartBPM);
			break;	
		default:
			break;
		}
		

       
	}
	
	/**Creo il dataset per il grafico*/
	private XYDataset setWeightCategory(){	
		TimeSeriesCollection oDataSet = new TimeSeriesCollection();
		Vector<Exercise> oExercise=ExerciseUtils.getWeightData(oContext);
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		
		if(oExercise.size()<2) {
			TimeSeries oSerie = new TimeSeries(oContext.getString(R.string.weight));
			oSerie.add(new Day(1,1,9999), 0);				
			oDataSet.addSeries(oSerie);
			return oDataSet;
		}
		int iExSize=oExercise.size()-2;		
		TimeSeries oSerie = new TimeSeries(oContext.getString(R.string.weight));
		 
		for(int i=0;i<iExSize;i++){							
			Log.v(this.getClass().getCanonicalName(),"Date: "+oExercise.get(i).getsDateExercise()+" - "+oExercise.get(i).getdDateExercise().get(Calendar.DAY_OF_MONTH)+"/"+
					oExercise.get(i).getdDateExercise().get(Calendar.MONTH)+"/"+
					(oExercise.get(i).getdDateExercise().get(Calendar.YEAR))+" - Weight: "+oExercise.get(i).getdWeight());
			try{
				if(oExercise.get(i).getdDateExercise().get(Calendar.MONTH)>=1 ||
						oExercise.get(i).getdDateExercise().get(Calendar.MONTH)<=12){
					oSerie.add(new Day(oExercise.get(i).getdDateExercise().get(Calendar.DAY_OF_MONTH),oExercise.get(i).getdDateExercise().get(Calendar.MONTH), 
							oExercise.get(i).getdDateExercise().get(Calendar.YEAR)), Double.valueOf(twoDForm.format(oExercise.get(i).getdWeight())));	
				}
			}catch (SeriesException e) {
				if(oExercise.get(i).getdDateExercise().get(Calendar.MONTH)>=1 ||
						oExercise.get(i).getdDateExercise().get(Calendar.MONTH)<=12){
					oSerie.addOrUpdate(new Day(oExercise.get(i).getdDateExercise().get(Calendar.DAY_OF_MONTH),oExercise.get(i).getdDateExercise().get(Calendar.MONTH), 
						oExercise.get(i).getdDateExercise().get(Calendar.YEAR)), Double.valueOf(twoDForm.format(oExercise.get(i).getdWeight())));
				}
			}catch (RuntimeException e) {
				Log.e(this.getClass().getCanonicalName(),"Error in Month");
			}
			Log.v(this.getClass().getCanonicalName(), "Value to evauate:"+i);										
		}
		try{
			if(oExercise.get(iExSize-1).getdDateExercise().get(Calendar.MONTH)>=1 ||
					oExercise.get(iExSize-1).getdDateExercise().get(Calendar.MONTH)<=12){
				oSerie.add(new Day(oExercise.get(iExSize-1).getdDateExercise().get(Calendar.DAY_OF_MONTH),oExercise.get(iExSize-1).getdDateExercise().get(Calendar.MONTH), 
					oExercise.get(iExSize-1).getdDateExercise().get(Calendar.YEAR)), Double.valueOf(twoDForm.format(oExercise.get(iExSize-1).getdWeight())));
			}
		}catch (SeriesException e) {
			oSerie.addOrUpdate(new Day(oExercise.get(iExSize-1).getdDateExercise().get(Calendar.DAY_OF_MONTH),oExercise.get(iExSize-1).getdDateExercise().get(Calendar.MONTH), 
					oExercise.get(iExSize-1).getdDateExercise().get(Calendar.YEAR)), Double.valueOf(twoDForm.format(oExercise.get(iExSize-1).getdWeight())));
		}catch (RuntimeException e) {
			Log.e(this.getClass().getCanonicalName(),"Error in Month");
		}		
		
		
		
		oDataSet.addSeries(oSerie);
		//Log.v(this.getClass().getCanonicalName(), "Data for Jflot: "+sData);
		return  oDataSet;
	}
	
	/**Creo il dataset per il grafico*/
	private XYDataset setBPMCategory() {
		XYSeries oSerie = new XYSeries(oContext.getString(R.string.alt));	
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		ArrayList<WatchPoint> aWatchPoint = ExerciseManipulate.getWatchPoint();
		double dMin=0.0;
		if(aWatchPoint.size()==0) return null;
		int iWPSize=aWatchPoint.size()-2;
		for(int i=0;i<iWPSize;i++){
			if(dMin==0.0){
				dMin=aWatchPoint.get(i).getBpm();	
			}else if (dMin>aWatchPoint.get(i).getBpm()){
				dMin=aWatchPoint.get(i).getBpm();
			}				
			//Log.v(this.getClass().getCanonicalName(),"Add to Series Distance: "+Math.round(aWatchPoint.get(i).getdDistance())+" BPM:"+aWatchPoint.get(i).getBpm());
			oSerie.add(Math.round(aWatchPoint.get(i).getdDistance()),aWatchPoint.get(i).getBpm());
		}
		oSerie.add(Math.round(aWatchPoint.get(aWatchPoint.size()-1).getdDistance()),
				aWatchPoint.get(aWatchPoint.size()-1).getBpm());
		dataset.addSeries(oSerie);
		
		return dataset;
	}

	/**Creo il dataset per il grafico*/
	private XYDataset setPaceCategory() {
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries oSerie = new XYSeries(oContext.getString(R.string.pace));		
		ArrayList<WatchPoint> aWatchPoint = ExerciseManipulate.getWatchPoint();
		
		if(aWatchPoint.size()==0) return null;
		int iWPSize=aWatchPoint.size()-2;
		for(int i=0;i<iWPSize;i++){
			//Log.v(this.getClass().getCanonicalName(),"Add to Series Distance: "+Math.round(aWatchPoint.get(i).getdDistance())+" Pace:"+aWatchPoint.get(i).getdPace());
			oSerie.add(Math.round(aWatchPoint.get(i).getdDistance()),aWatchPoint.get(i).getdPace());
		}
		oSerie.add(Math.round(aWatchPoint.get(aWatchPoint.size()-1).getdDistance()),
				aWatchPoint.get(aWatchPoint.size()-1).getdPace());
		dataset.addSeries(oSerie);
		return dataset;
	}

	private XYDataset setALTCategory() {
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries oSerie = new XYSeries(oContext.getString(R.string.alt));		
		ArrayList<WatchPoint> aWatchPoint = ExerciseManipulate.getWatchPoint();
		double dMin=0.0;
		if(aWatchPoint.size()==0) return null;
		int iWPSize=aWatchPoint.size()-2;
		for(int i=0;i<iWPSize;i++){
			if(dMin==0.0){
				dMin=aWatchPoint.get(i).getdAlt();	
			}else if (dMin>aWatchPoint.get(i).getdAlt()){
				dMin=aWatchPoint.get(i).getdAlt();
			}				
			//Log.v(this.getClass().getCanonicalName(),"Add to Series Distance: "+Math.round(aWatchPoint.get(i).getdDistance())+" Alt:"+aWatchPoint.get(i).getdAlt());
			oSerie.add(Math.round(aWatchPoint.get(i).getdDistance()),aWatchPoint.get(i).getdAlt());
		}
		oSerie.add(Math.round(aWatchPoint.get(aWatchPoint.size()-1).getdDistance()),
				aWatchPoint.get(aWatchPoint.size()-1).getdAlt());
		dataset.addSeries(oSerie);
		return dataset;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
     * Creates a chart.
     *
     * @param dataset  a dataset.
     *
     * @return A chart.
     */
    private static AFreeChart createWeightChart(XYDataset dataset) {

    	
        AFreeChart chart = ChartFactory.createTimeSeriesChart(
        		null,  // title
        		null,    // x-axis label
        		oContext.getString(R.string.weight),   // y-axis label
            dataset,            // data
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
        plot.setDomainCrosshairVisible(false);
        plot.setRangeCrosshairVisible(false);
        
       /* XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(false);
            renderer.setBaseShapesFilled(false);
            renderer.setDrawSeriesLineAsPath(true);
           
        }*/

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("dd-MM-yyyy"));
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        return chart;

    }
	
    /**
     * Creates a chart.
     *
     * @param dataset  a dataset.
     *
     * @return A chart.
     */
    private static AFreeChart createALTChart(XYDataset dataset) {

        AFreeChart chart = ChartFactory.createXYLineChart(
        		null,  // title
        		oContext.getString(R.string.distance),    // x-axis label
        		oContext.getString(R.string.alt),   // y-axis label
            dataset,            // data
            PlotOrientation.VERTICAL,
            false,               // create legend?
            false,               // generate tooltips?
            false               // generate URLs?
        );

        chart.setBackgroundPaintType(new SolidColor(Color.WHITE));

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setOutlineStroke(3);
        plot.setBackgroundPaintType(new SolidColor(Color.LTGRAY));
        plot.setDomainGridlinePaintType(new SolidColor(Color.WHITE));
        plot.setRangeGridlinePaintType(new SolidColor(Color.WHITE));
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(false);
        plot.setRangeCrosshairVisible(false);

        /*XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
            renderer.setDrawSeriesLineAsPath(true);
            renderer.setBaseStroke(5);
            renderer.setSeriesStroke(1, 5f);
        }
*/
        return chart;

    }
    
    /**
     * Creates a chart.
     *
     * @param dataset  a dataset.
     *
     * @return A chart.
     */
    private static AFreeChart createPaceChart(XYDataset dataset) {
    	//ChartFactory.createAreaChart(title, categoryAxisLabel, valueAxisLabel, dataset, orientation, legend, tooltips, urls);
    	//ChartFactory.createXYLineChart(title, xAxisLabel, yAxisLabel, dataset, orientation, legend, tooltips, urls)
        AFreeChart chart = ChartFactory.createXYLineChart(
        		null,  // title
        		oContext.getString(R.string.distance),    // x-axis label
        		oContext.getString(R.string.pace),   // y-axis label
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
        plot.setDomainCrosshairVisible(false);
        plot.setRangeCrosshairVisible(false);

        /*XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
            renderer.setDrawSeriesLineAsPath(true);
        }    */ 
        return chart;

    }
    
    /**
     * Creates a chart.
     *
     * @param dataset  a dataset.
     *
     * @return A chart.
     */
    private static AFreeChart createBPMChart(XYDataset dataset) {

        AFreeChart chart = ChartFactory.createXYLineChart(
        		null,  // title
        		oContext.getString(R.string.distance),    // x-axis label
        		oContext.getString(R.string.heart_rate),   // y-axis label
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
        plot.setDomainCrosshairVisible(false);
        plot.setRangeCrosshairVisible(false);

        /*XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
            renderer.setDrawSeriesLineAsPath(true);
        }   */  
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      
        return chart;

    }	
}
