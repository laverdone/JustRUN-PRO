package com.glm.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

import com.glm.trainer.R;


public class StopwatchView extends View {
	// ===========================================================
	// Fields
	// ===========================================================
	
	protected final int ARCSTROKEWIDTH = 20;
	
	// Stringa Formattata con HH:MM
	protected String TrainerTimeHHMM="0:00";
	// Stringa Formattata con SS.dd
	protected String TrainerTimeSSdd=":00";
	
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
	/**Pulsante di Avvio*/
	Bitmap oImgStart = BitmapFactory.decodeResource(getResources(),
             R.drawable.start_trainer);
	
	//Identifica il tipo di font da utilizzare
	private Typeface oFont;
	private Context oCtx;
	/***
	 * Costruttore dell'oggetto incaricato di disegnare il cronometro
	 * con tutti gli ogetti necessari per la visualizzazione e iterazione durante il
	 * nuovo Trainer
	 * 
	 * @param Context context
	 * 
	 * @author Gianluca Masci aka (glm)
	 * */
	public StopwatchView(Context context) {
		super(context);		
		//this.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_stopwatch));
		Log.d("Impostazione Font", "fonts/DS-DIGIB.ttf");
		oFont = Typeface.createFromAsset(context.getAssets(), "fonts/DS-DIGIB.TTF");
		
		
				
		//TODO inserire il testo per km percorsi e calorie consumante
		oCtx=context; 
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(iTypeExercise==0){
			drawRun(canvas);
		}else if(iTypeExercise==1){
			drawBike(canvas);
		}else if(iTypeExercise==100){
			drawWalk(canvas);
		}
		
		
//		}else{						
//			// Draw the remaining time.
//			canvas.drawText(TrainerTimeHHMM, 
//					this.getWidth() / 2 - (40 * (TrainerTimeHHMM+TrainerTimeSSdd).length()),
//					this.getHeight()/ 2 + 40, 
//					this.TextPaintHHMM);
//		}
	}
	
	private void drawBike(Canvas canvas) {
		// Impostazione del Testo per HH:MM
		this.TextPaintHHMM.setColor(Color.WHITE);
		this.TextPaintHHMM.setAntiAlias(true);				
		this.TextPaintHHMM.setTypeface(oFont);
		
		this.TextPaintHHMM.setFakeBoldText(true);
		
		// Impostazione del Testo per SS:dd
		this.TextPaintSSdd.setColor(Color.WHITE);
		this.TextPaintSSdd.setAntiAlias(true);				
		this.TextPaintSSdd.setTypeface(oFont);
		
		this.TextPaintSSdd.setFakeBoldText(true);
		
		// Impostazione del Testo Calorie
		this.TextPaintCalories.setColor(Color.WHITE);
		this.TextPaintCalories.setAntiAlias(true);				
		this.TextPaintCalories.setTypeface(oFont);
		
		this.TextPaintCalories.setFakeBoldText(true);
		
		// Impostazione del Testo Distanza
		this.TextPaintDistance.setColor(Color.WHITE);
		this.TextPaintDistance.setAntiAlias(true);				
		this.TextPaintDistance.setTypeface(oFont);
		
		this.TextPaintDistance.setFakeBoldText(true);
		
		// Impostazione del Testo Distanza/Tempo
		this.TextPaintDistanceTime.setColor(Color.WHITE);
		this.TextPaintDistanceTime.setAntiAlias(true);				
		this.TextPaintDistanceTime.setTypeface(oFont);
		
		this.TextPaintDistanceTime.setFakeBoldText(true);
		
		// Impostazione del Testo Distanza/Tempo
		this.TextPaintALT.setColor(Color.WHITE);
		this.TextPaintALT.setAntiAlias(true);				
		this.TextPaintALT.setTypeface(oFont);
		
		this.TextPaintALT.setFakeBoldText(true);
		
		// Impostazione del Testo Pendenza
		this.TextPaintInclination.setColor(Color.WHITE);
		this.TextPaintInclination.setAntiAlias(true);				
		this.TextPaintInclination.setTypeface(oFont);
		
		
		if(this.getHeight()>600){
			this.TextPaintHHMM.setTextSize(110);
			this.TextPaintSSdd.setTextSize(100);
			this.TextPaintCalories.setTextSize(100);
			this.TextPaintDistance.setTextSize(100);
			this.TextPaintDistanceTime.setTextSize(100);
			this.TextPaintALT.setTextSize(100);
			this.TextPaintInclination.setTextSize(100);
			/**Tempo**/
			canvas.drawText(TrainerTimeHHMM, 
					(this.getWidth()/2)-110, (this.getHeight() / 2) + 50, 
								this.TextPaintHHMM);	
			
			canvas.drawText(TrainerTimeSSdd, 
					(this.getWidth()/2)+60, (this.getHeight() / 2) + 50, 
					this.TextPaintSSdd);
			/**Tempo**/						
			
			/**Distanza**/
			canvas.drawText("Distance: "+TrainerDistance, 
					(this.getWidth()/2)-110, (this.getHeight() / 2) - 120, 
					this.TextPaintDistance);
			/**Distanza**/
			
			/**Distanza/Tempo**/
			canvas.drawText("Speed:    "+TrainerPaceVm, 
					(this.getWidth()/2)-110, (this.getHeight() / 2) - 85, 
					this.TextPaintDistanceTime);
			/**Distanza/Tempo**/
			
			/**Calorie**/
			canvas.drawText("Calories: "+TrainerCalories, 
					(this.getWidth()/2)-110, (this.getHeight() / 2) - 50, 
					this.TextPaintCalories);
				
			/**Calorie**/
			
			/**ALT**/
			canvas.drawText("Alt: "+TrainerALT, 
					(this.getWidth()/2)-110, (this.getHeight() / 2) + 110, 
					this.TextPaintALT);
				
			/**ALT**/
			
			/**Inclination*/
			canvas.drawText("Inc: "+TrainerInclination, 
					(this.getWidth()/2)-110, (this.getHeight() / 2) + 150, 
					this.TextPaintInclination);
			/**Inclination*/
		}else{
			this.TextPaintHHMM.setTextSize(60);
			this.TextPaintSSdd.setTextSize(40);
			this.TextPaintCalories.setTextSize(50);
			this.TextPaintDistance.setTextSize(50);
			this.TextPaintDistanceTime.setTextSize(50);
			this.TextPaintALT.setTextSize(50);
			this.TextPaintInclination.setTextSize(50);
			/**Tempo**/
			canvas.drawText(TrainerTimeHHMM, 
					(this.getWidth()/2)-50, (this.getHeight() / 2) + 40, 
								this.TextPaintHHMM);	
			
			canvas.drawText(TrainerTimeSSdd, 
					(this.getWidth()/2)+40, (this.getHeight() / 2) + 40, 
					this.TextPaintSSdd);
			/**Tempo**/						
			
			/**Distanza**/
			canvas.drawText("Distance: "+TrainerDistance, 
					(this.getWidth()/2)-80, (this.getHeight() / 2) - 50, 
					this.TextPaintDistance);
			/**Distanza**/
			
			/**Distanza/Tempo**/
			canvas.drawText("Speed:    "+TrainerPaceVm, 
					(this.getWidth()/2)-80, (this.getHeight() / 2) - 30, 
					this.TextPaintDistanceTime);
			/**Distanza/Tempo**/
			
			/**Calorie**/
			canvas.drawText("Calories: "+TrainerCalories, 
					(this.getWidth()/2)-80, (this.getHeight() / 2) - 10, 
					this.TextPaintCalories);
				
			/**Calorie**/
			
			/**ALT**/
			canvas.drawText("Alt: "+TrainerALT, 
					(this.getWidth()/2)-80, (this.getHeight() / 2) + 60, 
					this.TextPaintALT);
				
			/**ALT**/
			
			/**Inclination*/
			canvas.drawText("Inc: "+TrainerInclination, 
					(this.getWidth()/2)-80, (this.getHeight() / 2) + 50, 
					this.TextPaintInclination);
			/**Inclination*/
		}
	}


	private void drawWalk(Canvas canvas) {
		// Impostazione del Testo per HH:MM
		this.TextPaintHHMM.setColor(Color.WHITE);
		this.TextPaintHHMM.setAntiAlias(true);				
		this.TextPaintHHMM.setTypeface(oFont);
		
		this.TextPaintHHMM.setFakeBoldText(true);
		
		// Impostazione del Testo per SS:dd
		this.TextPaintSSdd.setColor(Color.WHITE);
		this.TextPaintSSdd.setAntiAlias(true);				
		this.TextPaintSSdd.setTypeface(oFont);
		
		this.TextPaintSSdd.setFakeBoldText(true);
		
		// Impostazione del Testo Calorie
		this.TextPaintCalories.setColor(Color.WHITE);
		this.TextPaintCalories.setAntiAlias(true);				
		this.TextPaintCalories.setTypeface(oFont);
		
		this.TextPaintCalories.setFakeBoldText(true);
		
		// Impostazione del Testo Distanza
		this.TextPaintDistance.setColor(Color.WHITE);
		this.TextPaintDistance.setAntiAlias(true);				
		this.TextPaintDistance.setTypeface(oFont);
		
		this.TextPaintDistance.setFakeBoldText(true);
		
		// Impostazione del Testo Distanza/Tempo
		this.TextPaintDistanceTime.setColor(Color.WHITE);
		this.TextPaintDistanceTime.setAntiAlias(true);				
		this.TextPaintDistanceTime.setTypeface(oFont);
		
		this.TextPaintDistanceTime.setFakeBoldText(true);
		
		// Impostazione del Testo Distanza/Tempo
		this.TextPaintALT.setColor(Color.WHITE);
		this.TextPaintALT.setAntiAlias(true);				
		this.TextPaintALT.setTypeface(oFont);
		
		this.TextPaintALT.setFakeBoldText(true);
		
		// Impostazione del Testo Pendenza
		this.TextPaintInclination.setColor(Color.WHITE);
		this.TextPaintInclination.setAntiAlias(true);				
		this.TextPaintInclination.setTypeface(oFont);
		if(this.getHeight()>600){
			this.TextPaintHHMM.setTextSize(90);
			this.TextPaintSSdd.setTextSize(80);
			this.TextPaintCalories.setTextSize(20);
			this.TextPaintDistance.setTextSize(25);
			this.TextPaintDistanceTime.setTextSize(25);
			this.TextPaintALT.setTextSize(30);
			this.TextPaintInclination.setTextSize(30);
			/**Tempo**/
			canvas.drawText(TrainerTimeHHMM, 
					(this.getWidth()/2)-110, (this.getHeight() / 2) + 50, 
								this.TextPaintHHMM);	
			
			canvas.drawText(TrainerTimeSSdd, 
					(this.getWidth()/2)+30, (this.getHeight() / 2) + 50, 
					this.TextPaintSSdd);
			/**Tempo**/						
			
			/**Distanza**/
			canvas.drawText("Distance: "+TrainerDistance, 
					(this.getWidth()/2)-120, (this.getHeight() / 2) - 110, 
					this.TextPaintDistance);
			/**Distanza**/
			
			/**Distanza/Tempo**/
			canvas.drawText("Speed:    "+TrainerPaceVm, 
					(this.getWidth()/2)-120, (this.getHeight() / 2) - 75, 
					this.TextPaintDistanceTime);
			/**Distanza/Tempo**/
			
			/**Calorie**/
			canvas.drawText("Calories: "+TrainerCalories, 
					(this.getWidth()/2)-120, (this.getHeight() / 2) - 50, 
					this.TextPaintCalories);
				
			/**Calorie**/
			
			/**ALT**/
			canvas.drawText("Alt: "+TrainerALT, 
					(this.getWidth()/2)-90, (this.getHeight() / 2) + 120, 
					this.TextPaintALT);				
			/**ALT**/
			
			/**Inclination*/
			canvas.drawText("Inc: "+TrainerInclination, 
					(this.getWidth()/2)-60, (this.getHeight() / 2) - 150, 
					this.TextPaintInclination);
			/**Inclination*/
		}else{
			this.TextPaintHHMM.setTextSize(40);
			this.TextPaintSSdd.setTextSize(30);
			this.TextPaintCalories.setTextSize(9);
			this.TextPaintDistance.setTextSize(9);
			this.TextPaintDistanceTime.setTextSize(9);
			this.TextPaintALT.setTextSize(10);
			this.TextPaintInclination.setTextSize(13);
			/**Tempo**/
			canvas.drawText(TrainerTimeHHMM, 
					(this.getWidth()/2)-35, (this.getHeight() / 2) + 28, 
								this.TextPaintHHMM);	
			
			canvas.drawText(TrainerTimeSSdd, 
					(this.getWidth()/2)+25, (this.getHeight() / 2) + 28, 
					this.TextPaintSSdd);
			/**Tempo**/						
			
			/**Distanza**/
			canvas.drawText("Distance: "+TrainerDistance, 
					(this.getWidth()/2)-60, (this.getHeight() / 2) - 45, 
					this.TextPaintDistance);
			/**Distanza**/
			
			/**Distanza/Tempo**/
			canvas.drawText("Speed:    "+TrainerPaceVm, 
					(this.getWidth()/2)-60, (this.getHeight() / 2) - 30, 
					this.TextPaintDistanceTime);
			/**Distanza/Tempo**/
			
			/**Calorie**/
			canvas.drawText("Calories: "+TrainerCalories, 
					(this.getWidth()/2)-60, (this.getHeight() / 2) - 15, 
					this.TextPaintCalories);
				
			/**Calorie**/
			
			/**ALT**/
			canvas.drawText("Alt: "+TrainerALT, 
					(this.getWidth()/2)-40, (this.getHeight() / 2) + 58, 
					this.TextPaintALT);
				
			/**ALT**/
			
			/**Inclination*/
			canvas.drawText("Inc: "+TrainerInclination, 
					(this.getWidth()/2)-20, (this.getHeight() / 2) - 70, 
					this.TextPaintInclination);
			/**Inclination*/
		}
	}
	
	
	private void drawRun(Canvas canvas) {
		// Impostazione del Testo per HH:MM
		this.TextPaintHHMM.setColor(Color.BLACK);
		this.TextPaintHHMM.setAntiAlias(true);				
		this.TextPaintHHMM.setTypeface(oFont);
		
		this.TextPaintHHMM.setFakeBoldText(true);
		
		// Impostazione del Testo per SS:dd
		this.TextPaintSSdd.setColor(Color.BLACK);
		this.TextPaintSSdd.setAntiAlias(true);				
		this.TextPaintSSdd.setTypeface(oFont);
		
		this.TextPaintSSdd.setFakeBoldText(true);
		
		// Impostazione del Testo Calorie
		this.TextPaintCalories.setColor(Color.BLACK);
		this.TextPaintCalories.setAntiAlias(true);				
		this.TextPaintCalories.setTypeface(oFont);
		
		this.TextPaintCalories.setFakeBoldText(true);
		
		// Impostazione del Testo Distanza
		this.TextPaintDistance.setColor(Color.BLACK);
		this.TextPaintDistance.setAntiAlias(true);				
		this.TextPaintDistance.setTypeface(oFont);
		
		this.TextPaintDistance.setFakeBoldText(true);
		
		// Impostazione del Testo Distanza/Tempo
		this.TextPaintDistanceTime.setColor(Color.BLACK);
		this.TextPaintDistanceTime.setAntiAlias(true);				
		this.TextPaintDistanceTime.setTypeface(oFont);
		
		this.TextPaintDistanceTime.setFakeBoldText(true);
		
		// Impostazione del Testo Distanza/Tempo
		this.TextPaintALT.setColor(Color.BLACK);
		this.TextPaintALT.setAntiAlias(true);				
		this.TextPaintALT.setTypeface(oFont);
		
		this.TextPaintALT.setFakeBoldText(true);
		// Impostazione del Testo Pendenza
		this.TextPaintInclination.setColor(Color.BLACK);
		this.TextPaintInclination.setAntiAlias(true);				
		this.TextPaintInclination.setTypeface(oFont);
		if(this.getHeight()>600){
			this.TextPaintHHMM.setTextSize(80);
			this.TextPaintSSdd.setTextSize(70);
			this.TextPaintCalories.setTextSize(18);
			this.TextPaintDistance.setTextSize(18);
			this.TextPaintDistanceTime.setTextSize(18);
			/**Tempo**/
			canvas.drawText(TrainerTimeHHMM, 
					(this.getWidth()/2)-95, (this.getHeight() / 2) + 70, 
								this.TextPaintHHMM);	
			
			canvas.drawText(TrainerTimeSSdd, 
					(this.getWidth()/2)+35, (this.getHeight() / 2) + 70, 
					this.TextPaintSSdd);
			/**Tempo**/						
			
			/**Distanza**/
			canvas.drawText("Distance: "+TrainerDistance, 
					(this.getWidth()/2)-55, (this.getHeight() / 2) - 51, 
					this.TextPaintDistance);
			/**Distanza**/
			
			/**Distanza/Tempo**/
			canvas.drawText("Speed:    "+TrainerPaceVm, 
					(this.getWidth()/2)-55, (this.getHeight() / 2) - 31, 
					this.TextPaintDistanceTime);
			/**Distanza/Tempo**/
			
			/**Calorie**/
			canvas.drawText("Calories: "+TrainerCalories, 
					(this.getWidth()/2)-55, (this.getHeight() / 2) - 11, 
					this.TextPaintCalories);
				
			/**Calorie**/
		}else{
			this.TextPaintHHMM.setTextSize(50);
			this.TextPaintSSdd.setTextSize(40);
			this.TextPaintCalories.setTextSize(14);
			this.TextPaintDistance.setTextSize(14);
			this.TextPaintDistanceTime.setTextSize(14);
			/**Tempo**/
			canvas.drawText(TrainerTimeHHMM, 
					(this.getWidth()/2)-60, (this.getHeight() / 2) + 45, 
								this.TextPaintHHMM);	
			
			canvas.drawText(TrainerTimeSSdd, 
					(this.getWidth()/2)+20, (this.getHeight() / 2) + 45, 
					this.TextPaintSSdd);
			/**Tempo**/						
			
			/**Distanza**/
			canvas.drawText("Distance: "+TrainerDistance, 
					(this.getWidth()/2)-55, (this.getHeight() / 2) - 29, 
					this.TextPaintDistance);
			/**Distanza**/
			
			/**Distanza/Tempo**/
			canvas.drawText("Speed:    "+TrainerPaceVm, 
					(this.getWidth()/2)-55, (this.getHeight() / 2) - 11, 
					this.TextPaintDistanceTime);
			/**Distanza/Tempo**/
			
			/**Calorie**/
			canvas.drawText("Calories: "+TrainerCalories, 
					(this.getWidth()/2)-55, (this.getHeight() / 2) + 5, 
					this.TextPaintCalories);
			/**Calorie**/
		}
	}


	/**
	 * Imposta il tempo dell'esercizio
	 * 
	 * @param int TimePassed tempo trascorso
	 * */
	public void updateTimeHHMM(long TimeTotal){
		int minutes = (int) (TimeTotal / 60000 % 60);
        int hours = (int) (TimeTotal / 3600000);
        //Tempo Formattato String.format ("%d:%02d:%02d.%d", hours, minutes, seconds, decSeconds);
        TrainerTimeHHMM = String.format ("%d:%02d", hours, minutes);
	}
	
	/**
	 * Imposta il tempo totale dell'esercizio
	 * 
	 * @param int TimeTotal tempo totale trascorso
	 * */
	public void updateTimeSSdd(long TimeTotal){
		 int decSeconds = (int) (TimeTotal % 1000 / 100);
         int seconds = (int) (TimeTotal / 1000 % 60);
         //TrainerTimeSSdd = String.format (":%02d.%d", seconds,decSeconds);
         TrainerTimeSSdd = String.format (":%02d", seconds);
	}
	
	/**
	 * Imposta il tempo totale dell'esercizio
	 * 
	 * @param int TimeTotal tempo totale trascorso
	 * */
	public void updateCurrentDistance(String sCurrentDistance){
		TrainerDistance = sCurrentDistance;
	}
	
	/**
	 * Imposta il tempo totale dell'esercizio
	 * 
	 * @param int TimeTotal tempo totale trascorso
	 * */
	public void updateCurrentPaceVm(String sCurrentPace){
		TrainerPaceVm = sCurrentPace;
	}
	
	/**
	 * 
	 * Imposta il tempo totale dell'esercizio
	 * 
	 * @param int TimeTotal tempo totale trascorso
	 * */
	public void updateCurrentKalories(String sCurrentKalories){
		TrainerCalories = sCurrentKalories;
	}
	/**
	 * 
	 * Imposta l'altitudine
	 * 
	 * @param int TimeTotal tempo totale trascorso
	 * */
	public void updateCurrentALT(String sALT){
		TrainerALT = sALT;
	}
	/**
	 * 
	 * Imposta l'altitudine
	 * 
	 * @param int TimeTotal tempo totale trascorso
	 * */
	public void updateCurrentInclination(String sInclination){
		TrainerInclination = sInclination;
	}
	/**
	 * 
	 * Cambia le posizioni del testo per un altro tipo di stopwatch
	 * 0=RUN
	 * 1=BIKE
	 * 100=walk
	 * */
	public void changeType(int TypeOfTrainer) {
		if(TypeOfTrainer==0){
			//RUN
			iTypeExercise=TypeOfTrainer;
		}else if(TypeOfTrainer==1){
			//BIKE
			iTypeExercise=TypeOfTrainer;
		}else if(TypeOfTrainer==100){
			//walk
			iTypeExercise=TypeOfTrainer;
		}
		
	}
}