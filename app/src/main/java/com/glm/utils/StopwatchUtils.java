package com.glm.utils;

import android.graphics.Paint;
import android.widget.TextView;


public class StopwatchUtils {
	// ===========================================================
	// Fields
	// ===========================================================
	
	protected final int ARCSTROKEWIDTH = 20;
	
	
	// Stringa Formattata con Calorie Consumate
	protected String TrainerCalories="n.a. Kc";
	// Stringa Formattata con Distanza Percorsa
	protected String TrainerDistance="";
	// Stringa Formattata con distanza/tempo
	protected String TrainerPaceVm="";
	// Stringa per l'altezza, solo biking
	protected String TrainerALT="0";
	
	// Stringa per l'altezza, solo biking
	protected String TrainerInclination="0";
	
	// Identifica il testo per Ora Minuti
	protected final Paint TextPaintHHMM = new Paint();
	// Identifica il testo per Secondi e Decimi di Sec.
	protected final Paint TextPaintSSdd = new Paint();
	// Identifica il testo per Calorie
	protected final Paint TextPaintCalories = new Paint();
	// Identifica il testo per Distanza
	protected final Paint TextPaintDistance = new Paint();
	// Identifica il testo per distanza/tempo
	protected final Paint TextPaintDistanceTime = new Paint();
	// Identifica il testo per l'altitudine
	protected final Paint TextPaintALT = new Paint();	
	// Identifica il testo per l'altitudine
	protected final Paint TextPaintInclination = new Paint();
	
	protected int iTypeExercise=0;
	
	
	/**
	 * Imposta il tempo dell'esercizio
	 * 
	 * @param int TimePassed tempo trascorso
	 * */
	public void updateTimeHHMM(TextView txtTimeHHMM, long TimeTotal){
		int minutes = (int) (TimeTotal / 60000 % 60);
        int hours = (int) (TimeTotal / 3600000);
        //Tempo Formattato String.format ("%d:%02d:%02d.%d", hours, minutes, seconds, decSeconds);
        txtTimeHHMM.setText(String.format ("%d:%02d", hours, minutes));
	}
	
	/**
	 * Imposta il tempo totale dell'esercizio
	 * 
	 * @param int TimeTotal tempo totale trascorso
	 * */
	public void updateTimeSSdd(TextView txtTimeSSdd, long TimeTotal){
		 int decSeconds = (int) (TimeTotal % 1000 / 100);
         int seconds = (int) (TimeTotal / 1000 % 60);
         //TrainerTimeSSdd = String.format (":%02d.%d", seconds,decSeconds);
         txtTimeSSdd.setText(String.format (":%02d", seconds));
	}
	
	/**
	 * Imposta il tempo totale dell'esercizio
	 * 
	 * @param int TimeTotal tempo totale trascorso
	 * */
	public void updateCurrentDistance(TextView txtDistance, String sCurrentDistance){
		txtDistance.setText(sCurrentDistance);
	}
	
	/**
	 * Imposta il tempo totale dell'esercizio
	 * 
	 * @param int TimeTotal tempo totale trascorso
	 * */
	public void updateCurrentPaceVm(TextView txtPace, String sCurrentPace){
		txtPace.setText(sCurrentPace);
	}
	
	/**
	 * 
	 * Imposta il tempo totale dell'esercizio
	 * 
	 * @param int TimeTotal tempo totale trascorso
	 * */
	public void updateCurrentKalories(TextView txtKalories, String sCurrentKalories){
		txtKalories.setText(sCurrentKalories);
	}
	/**
	 * 
	 * Imposta l'altitudine
	 * 
	 * @param int TimeTotal tempo totale trascorso
	 * */
	public void updateCurrentALT(TextView txtALT, String sALT){
		txtALT.setText(sALT);
	}
	/**
	 * 
	 * Imposta l'altitudine
	 * 
	 * @param int TimeTotal tempo totale trascorso
	 * */
	public void updateCurrentInclination(TextView txtInclination, String sInclination){
		txtInclination.setText(sInclination);
	}
}