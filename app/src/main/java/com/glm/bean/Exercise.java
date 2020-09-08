package com.glm.bean;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.glm.trainer.R;

import java.util.Calendar;

/**
 * Contiene tutte le info per l'esercizio 
 * 
 * @see HistoryActivity
 * **/
public class Exercise implements Parcelable {
	/**Id Esercizio*/
	private String sIDExerise;
	/**Titolo Esercizio*/
	private String sTitle;
	/**Note*/
	private String sNote;
	/**Ora Avvio*/
	private String sStart;
	/**Ora di fine*/
	private String sEnd;
	/**Lunghezza della barra*/
	private int iBar=0;
	/**distanza percorsa in unita di misura*/
	private float fDistance=0;
	/**distanza formattata*/
	private String sDistanceFormatted="";
	/**Tipo di esercizio*/
	private int iTypeExercise=0;
	
	
	private String sTotalTime="";
	private String sTotalKalories="";
	private String sAVGSpeed="";
	private String sDateExercise="";
	private Calendar dDateExercise;
	private double dWeight=0.0;
	
	
	public synchronized String getsIDExerise() {
		return sIDExerise;
	}
	public synchronized void setsIDExerise(String sIDExerise) {
		this.sIDExerise = sIDExerise;
	}
	public synchronized String getsTitle() {
		return sTitle;
	}
	public synchronized void setsTitle(String sTitle) {
		this.sTitle = sTitle;
	}
	public synchronized String getsNote() {
		return sNote;
	}
	public synchronized void setsNote(String sNote) {
		this.sNote = sNote;
	}
	public synchronized String getsStart() {
		return sStart;
	}
	public String getDateFormatted(Context context){
		String month="";
		if(sStart.substring(5,7).equals("01")){
			month=context.getString(R.string.month1);
		}else if(sStart.substring(5,7).equals("02")){
			month=context.getString(R.string.month2);
		}else if(sStart.substring(5,7).equals("03")){
			month=context.getString(R.string.month3);
		}else if(sStart.substring(5,7).equals("04")){
			month=context.getString(R.string.month4);
		}else if(sStart.substring(5,7).equals("05")){
			month=context.getString(R.string.month5);
		}else if(sStart.substring(5,7).equals("06")){
			month=context.getString(R.string.month6);
		}else if(sStart.substring(5,7).equals("07")){
			month=context.getString(R.string.month7);
		}else if(sStart.substring(5,7).equals("08")){
			month=context.getString(R.string.month8);
		}else if(sStart.substring(5,7).equals("09")){
			month=context.getString(R.string.month9);
		}else if(sStart.substring(5,7).equals("10")){
			month=context.getString(R.string.month10);
		}else if(sStart.substring(5,7).equals("11")){
			month=context.getString(R.string.month11);
		}else if(sStart.substring(5,7).equals("12")){
			month=context.getString(R.string.month12);
		}
		if(sStart!=null) return sStart.substring(8,10)+" "+month+" "+sStart.substring(0,4);
		else return "N.D.";
	}
	public synchronized void setsStart(String sStart) {
		////Log.v(this.getClass().getCanonicalName(),"start:"+sStart);
		//if(sStart==null) return;
		//this.sStart = sdf.format(new Date(sStart));
		this.sStart=sStart;
	}
	public synchronized String getsEnd() {
		return sEnd;
	}
	public synchronized void setsEnd(String sEnd) {
		////Log.v(this.getClass().getCanonicalName(),"sEnd:"+sEnd);
		//if(sEnd==null) return;
		//this.sEnd = sdf.format(new Date(sEnd));
		this.sEnd = sEnd;
	}
	public synchronized int getiBar() {
		return iBar;
	}
	public synchronized void setiBar(int iBar) {
		this.iBar = iBar;
	}
	public synchronized float getfDistance() {
		return fDistance;
	}
	public synchronized void setfDistance(float fDistance) {
		this.fDistance = fDistance;
	}
	public synchronized String getsDistanceFormatted() {
		return sDistanceFormatted;
	}
	public synchronized void setsDistanceFormatted(String sDistanceFormatted) {
		this.sDistanceFormatted = sDistanceFormatted;
	}
	public synchronized String getsTotalTime() {
		return sTotalTime;
	}
	public synchronized void setsTotalTime(String sTotalTime) {
		this.sTotalTime = sTotalTime;
	}
	public synchronized String getsTotalKalories() {
		return sTotalKalories;
	}
	public synchronized void setsTotalKalories(String sTotalKalories) {
		this.sTotalKalories = sTotalKalories;
	}
	public synchronized String getsAVGSpeed() {
		return sAVGSpeed;
	}
	public synchronized void setsAVGSpeed(String sAVGSpeed) {
		this.sAVGSpeed = sAVGSpeed;
	}
	public synchronized Calendar getdDateExercise() {
		return dDateExercise;
	}
	public synchronized void setdDateExercise(Calendar dDateExercise) {
		this.dDateExercise = dDateExercise;
	}
	public synchronized double getdWeight() {
		return dWeight;
	}
	public synchronized void setdWeight(double dWeight) {
		this.dWeight = dWeight;
	}
	public synchronized String getsDateExercise() {
		return sDateExercise;
	}
	public synchronized void setsDateExercise(String sDateExercise) {
		this.sDateExercise = sDateExercise;
	}
	/**
	 * @return the iTypeExercise
	 */
	public synchronized int getiTypeExercise() {
		return iTypeExercise;
	}
	/**
	 * @param iTypeExercise the iTypeExercise to set
	 */
	public synchronized void setiTypeExercise(int iTypeExercise) {
		this.iTypeExercise = iTypeExercise;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.sIDExerise);
		dest.writeString(this.sTitle);
		dest.writeString(this.sNote);
		dest.writeString(this.sStart);
		dest.writeString(this.sEnd);
		dest.writeInt(this.iBar);
		dest.writeFloat(this.fDistance);
		dest.writeString(this.sDistanceFormatted);
		dest.writeInt(this.iTypeExercise);
		dest.writeString(this.sTotalTime);
		dest.writeString(this.sTotalKalories);
		dest.writeString(this.sAVGSpeed);
		dest.writeString(this.sDateExercise);
		dest.writeSerializable(this.dDateExercise);
		dest.writeDouble(this.dWeight);
	}

	public Exercise() {
	}

	protected Exercise(Parcel in) {
		this.sIDExerise = in.readString();
		this.sTitle = in.readString();
		this.sNote = in.readString();
		this.sStart = in.readString();
		this.sEnd = in.readString();
		this.iBar = in.readInt();
		this.fDistance = in.readFloat();
		this.sDistanceFormatted = in.readString();
		this.iTypeExercise = in.readInt();
		this.sTotalTime = in.readString();
		this.sTotalKalories = in.readString();
		this.sAVGSpeed = in.readString();
		this.sDateExercise = in.readString();
		this.dDateExercise = (Calendar) in.readSerializable();
		this.dWeight = in.readDouble();
	}

	public static final Parcelable.Creator<Exercise> CREATOR = new Parcelable.Creator<Exercise>() {
		@Override
		public Exercise createFromParcel(Parcel source) {
			return new Exercise(source);
		}

		@Override
		public Exercise[] newArray(int size) {
			return new Exercise[size];
		}
	};
}
