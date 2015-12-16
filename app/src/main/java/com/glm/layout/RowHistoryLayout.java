package com.glm.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glm.trainer.R;

public class RowHistoryLayout extends LinearLayout{
	LinearLayout oMainLayout;
	TextView oTextExerciseTitle;
	TextView oTextExerciseDett;
	LinearLayout oInteralLayout;
	View oProgressBar;
	TextView oTextDistance;
	View oRowSeparator;
	public RowHistoryLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		oMainLayout 		= new LinearLayout(context);		
		oTextExerciseTitle 	= new TextView(context);	
		oTextExerciseDett 	= new TextView(context);	
		oInteralLayout 		= new LinearLayout(context);	
		oProgressBar 		= new View(context);	
		oTextDistance  		= new TextView(context);
		oRowSeparator		= new View(context);	
		
		oMainLayout.setOrientation(VERTICAL);
		
		
		oInteralLayout.setOrientation(HORIZONTAL);
		
		oTextExerciseTitle.setText("Exercise - 10/04/2011");
		oTextExerciseTitle.setText("Running and good job");
		
		oProgressBar.setBackgroundResource(R.drawable.bar);
		oTextDistance.setText("20Km");
		
		oInteralLayout.addView(oProgressBar);
		oInteralLayout.addView(oTextDistance);
		
		oMainLayout.addView(oTextExerciseTitle);
		oMainLayout.addView(oTextExerciseDett);		
		oMainLayout.addView(oInteralLayout);
		
	}

}
