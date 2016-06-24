package com.glm.utils;

import android.content.Context;
import android.webkit.WebView;

import com.glm.bean.DistancePerMonth;
import com.glm.bean.Exercise;
import com.glm.bean.ExerciseManipulate;
import com.glm.bean.WatchPoint;
import com.glm.trainer.R;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Vector;

public class JsHandler {

	private WebView mAppView;
	private Vector<Exercise> oExercise;
	private Context oContext;
	private double dMin=0.0;
	
	public JsHandler  (WebView appView, Vector<Exercise> Exercise, Context context) {
		this.mAppView = appView;
		this.oExercise = Exercise;
		this.oContext  = context;
		//Log.v(this.getClass().getCanonicalName(), "Exercise: "+oExercise.size());
	}

/*
	public void loadGraphWeight() {			
		//return arr.toString();  //This _WILL_ return the data in a good looking JSON string, but if you pass it straight into the Flot Plot method, it will not work!		
		mAppView.loadUrl("javascript: var data = "+getRawDataWeight()+"; var options="+getLineOptionsWeight()+"; loadGraphHTML();");  //this callback works!
	}


	public void loadGraphExercise(){
		mAppView.loadUrl("javascript: var data = "+getRawDataPaceExercise()+"; var options="+getLineOptionsExercise(0.0)+"; loadGraphHTML();");  //this callback works!	
	}
	
	public void loadGraphExerciseAlt(){
		mAppView.loadUrl("javascript: var data = "+getRawDataAltExercise()+"; var options="+getLineOptionsExercise(dMin)+"; loadGraphHTML();");  //this callback works!	
	}
	
	public void loadGraphExerciseBpm(){
		mAppView.loadUrl("javascript: var data = "+getRawDataBpmExercise()+"; var options="+getLineOptionsExercise(dMin)+"; loadGraphHTML();");  //this callback works!	
	}
	public void loadRealTimeExercise(){
		mAppView.loadUrl("javascript: var data = "+getRawDataRealTime()+"; var options="+getLineOptionsRealTime()+"; loadRealTimeHTML();");  //this callback works!							
	}
	
	public void loadGraphMonthExercise(){
		mAppView.loadUrl("javascript: var data = "+getRawDataMonthExercise()+"; var options="+getTotalMonthOptions()+"; loadGraphHTML();");  //this callback works!	
	}
*/
/*
	private String getLineOptionsRealTime() {
		String sOptions = "{  series: { lines: { show: true , fill: true}, points: { show: false }, shadowSize: 0 } ,grid: { hoverable: false, clickable: false } ,xaxis: { show: false } , yaxis: { show: false } };";
		//Log.v(this.getClass().getCanonicalName(), "Option for Jflot: "+sOptions);
		return sOptions;
	}

	private String getRawDataRealTime() {
		ArrayList<WatchPoint> aWatchPoint = ExerciseManipulate.getWatchPoint();
		String sData = "[ { data: ["; 		
		if(aWatchPoint.size()==0) return "";
		int iWPSize=aWatchPoint.size()-2;
		for(int i=0;i<iWPSize;i++){
			sData+="["+Math.round(aWatchPoint.get(i).getdDistance())+", "+aWatchPoint.get(i).getdPace()+"], ";			
		}
		sData+="["+aWatchPoint.get(aWatchPoint.size()-1).getdDistance()+", "+Math.round(aWatchPoint.get(aWatchPoint.size()-1).getdPace())+"] ]} ] ";
		//Log.v(this.getClass().getCanonicalName(), "Data for Jflot: "+sData);
		return sData;
	}
	
	public void updateRealTime(){
		//Log.v("updateRealTime"," Now Update; "+"javascript: var data = "+getRawDataRealTime()+";");
		mAppView.loadUrl("javascript: var data = "+getRawDataRealTime()+"; var options="+getLineOptionsRealTime()+"; loadRealTimeHTML();");  //this callback works!							
	}
	
	private String getLineOptionsExercise(double Min) {
		String sOptions = "";
		if(Min==0.0){
			sOptions = "{  series: { lines: { show: true , fill: true}, points: { show: false }  },grid: { hoverable: true, clickable: true } };";	
		}else{
			sOptions = "{  series: { lines: { show: true , fill: true}, points: { show: false }  },grid: { hoverable: true, clickable: true }, yaxis: {min: "+Math.round(Min)+"} };";	
		}
		
		//Log.v(this.getClass().getCanonicalName(), "Option for Jflot: "+sOptions);
		return sOptions;
	}
	*/
	/**
	 * Preleva i dati per passo e distanza
	 * 
	 * */
	private String getRawDataPaceExercise() {
		ArrayList<WatchPoint> aWatchPoint = ExerciseManipulate.getWatchPoint();
		String sData = "[ { label: \""+oContext.getString(R.string.pace)+"\", data: [";
		if(aWatchPoint.size()==0) return "";
		int iWPSize=aWatchPoint.size()-2;
		for(int i=0;i<iWPSize;i++){
			sData+="["+Math.round(aWatchPoint.get(i).getdDistance())+", "+aWatchPoint.get(i).getdPace()+"], ";			
		}
		sData+="["+aWatchPoint.get(aWatchPoint.size()-1).getdDistance()+", "+Math.round(aWatchPoint.get(aWatchPoint.size()-1).getdPace())+"] ]} ] ";
		//Log.v(this.getClass().getCanonicalName(), "Data for Jflot: "+sData);
		return sData;
	}

	/**
	 * Preleva i dati per altezza e distanza
	 * 
	 * */
	/*private String getRawDataAltExercise() {
		double dTmpAlt=0.0;
		ArrayList<WatchPoint> aWatchPoint = ExerciseManipulate.getWatchPoint();
		String sData = "[ { label: \""+oContext.getString(R.string.alt)+"\", data: ["; 		
		if(aWatchPoint.size()==0) return "";
		int iWPSize=aWatchPoint.size()-2;
		for(int i=0;i<iWPSize;i++){
			if(dMin==0.0){
				dMin=aWatchPoint.get(i).getdAlt();	
			}else if (dMin>aWatchPoint.get(i).getdAlt()){
				dMin=aWatchPoint.get(i).getdAlt();
			}
			sData+="["+Math.round(aWatchPoint.get(i).getdDistance())+", "+aWatchPoint.get(i).getdAlt()+"], ";	
			
		}
		sData+="["+aWatchPoint.get(aWatchPoint.size()-1).getdDistance()+", "+Math.round(aWatchPoint.get(aWatchPoint.size()-1).getdAlt())+"] ]} ] ";
		//Log.v(this.getClass().getCanonicalName(), "Data for Jflot: "+sData);
		return sData;
	}
*/
	/**
	 * Preleva i dati per bpm e distanza
	 * 
	 * */
	/*private String getRawDataBpmExercise() {
		double dTmpAlt=0.0;
		ArrayList<WatchPoint> aWatchPoint = ExerciseManipulate.getWatchPoint();
		String sData = "[ { label: \""+oContext.getString(R.string.heart_rate)+"\", data: ["; 		
		if(aWatchPoint.size()==0) return "";
		int iWPSize=aWatchPoint.size()-2;
		for(int i=0;i<iWPSize;i++){
			if(dMin==0.0){
				dMin=aWatchPoint.get(i).getBpm();	
			}else if (dMin>aWatchPoint.get(i).getBpm()){
				dMin=aWatchPoint.get(i).getBpm();
			}
			sData+="["+Math.round(aWatchPoint.get(i).getdDistance())+", "+aWatchPoint.get(i).getBpm()+"], ";	
			
		}
		sData+="["+aWatchPoint.get(aWatchPoint.size()-1).getdDistance()+", "+Math.round(aWatchPoint.get(aWatchPoint.size()-1).getBpm())+"] ]} ] ";
		//Log.v(this.getClass().getCanonicalName(), "Data for Jflot: "+sData);
		return sData;
	}
	
	private String getLineOptionsWeight() {
		//tickFormatter: {function (val, axis) { var d = new Date(val);return d.getUTCDate() + (d.getUTCMonth() + 1); }}
		String sOptions = "{  series: { lines: { show: true }, points: { show: true }  }, xaxis: {mode: \"time\", timeformat: \"%d/%m/%y\" }," +
				" grid: { hoverable: false, clickable: false }, " +
				" yaxis: {tickFormatter: \"number\", tickDecimals: 2 }, " +
				" selection: { mode: \"x\" }};";
		//Log.v(this.getClass().getCanonicalName(), "Option for Jflot: "+sOptions);
		return sOptions;
	}

	private String getRawDataWeight() {
		NumberFormat oNFormat = NumberFormat.getInstance();
		oNFormat.setMaximumFractionDigits(2);
		String sData = "[ { label: \""+oContext.getString(R.string.weight)+"\", data: ["; 
		if(oExercise.size()==0) return "";
		int iExSize=oExercise.size()-2;
		for(int i=0;i<iExSize;i++){
			sData+="["+oExercise.get(i).getdDateExercise().getTime()+", "+oNFormat.format(oExercise.get(i).getdWeight())+"], ";
		}
		sData+="["+oExercise.get(oExercise.size()-1).getdDateExercise().getTime()+", "+oExercise.get(oExercise.size()-1).getdWeight()+"] ]} ] ";
		//Log.v(this.getClass().getCanonicalName(), "Data for Jflot: "+sData);
		return sData;
	}
	
	private String getRawDataMonthExercise() {
		
		Vector<DistancePerMonth> table = ExerciseUtils.getDistanceForMonth(-1, ExerciseUtils.loadConfiguration(oContext),
				oContext);
		String sData = "[ { data: ["; 		
		
		for(int i=0;i<11;i++){
			DistancePerMonth oDistance = (DistancePerMonth) table.get(i);			
			sData+="["+oDistance.getiMonth()+", "+oDistance.getsDistance()+"], ";	
			oDistance=null;
		}
		sData+="["+((DistancePerMonth) table.get(table.size()-1)).getiMonth()+", "
				+((DistancePerMonth) table.get(table.size()-1)).getsDistance()+"] ]} ] ";
		//Log.v(this.getClass().getCanonicalName(), "Data for Jflot: "+sData);
		return sData;
	}
	
	private String getTotalMonthOptions(){	
		String sOptions = "{" +
						"series: {stack: stack,lines: { show: lines, fill: true, steps: false },bars: { show: true, barWidth: 0.7, align: \"center\"}}," +
						" xaxis: { ticks: [[1,\""+oContext.getString(R.string.month1)+"\"],[2,\""+oContext.getString(R.string.month2)+"\"], " +
								"[3,\""+oContext.getString(R.string.month3)+"\"], [4,\""+oContext.getString(R.string.month4)+"\"]," +
						" [5,\""+oContext.getString(R.string.month5)+"\"], [6,\""+oContext.getString(R.string.month6)+"\"],[7,\""+oContext.getString(R.string.month7)+"\"]," +
								"[8,\""+oContext.getString(R.string.month8)+"\"],[9,\""+oContext.getString(R.string.month9)+"\"]," +
						" [10,\""+oContext.getString(R.string.month10)+"\"],[11,\""+oContext.getString(R.string.month11)+"\"]," +
								"[12,\""+oContext.getString(R.string.month12)+"\"]] }" +
						"};";				
        	//Log.v(this.getClass().getCanonicalName(), "Option for Jflot: "+sOptions);
			return sOptions;
	}
*/
	public void updateMap(double dLong, double dLat) {
		//Log.v("updateMap"," Now Location; "+dLong+" - "+dLat);
		mAppView.loadUrl("javascript: var iLon="+dLong+"; var iLat="+dLat+";");  //this callback works!							
	}
}
