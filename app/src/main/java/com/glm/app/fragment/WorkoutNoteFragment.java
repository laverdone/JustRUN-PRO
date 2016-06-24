package com.glm.app.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.glm.bean.ConfigTrainer;
import com.glm.bean.ExerciseManipulate;
import com.glm.trainer.R;
import com.glm.utils.ExerciseUtils;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class WorkoutNoteFragment extends Fragment {
	private ConfigTrainer oConfigTrainer;
	private View rootView;
	private Context mContext;
	private int mIDWorkout=0;

	private Button btnSaveNote;
	private EditText oNote;
	
	/**
	 * 
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	public WorkoutNoteFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext=getActivity().getApplicationContext();
		if(rootView==null){ 
			rootView = inflater.inflate(R.layout.new_workout_details_note,container, false);
			rootView.setDrawingCacheEnabled(true);
			oNote		  = (EditText) rootView.findViewById(R.id.txtNote);
			oNote.setText(ExerciseManipulate.getsNote());
			btnSaveNote	  = (Button) rootView.findViewById(R.id.btnSaveNote);
			btnSaveNote.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AsyncTask.execute(new Runnable() {
						
						@Override
						public void run() {
							oConfigTrainer = ExerciseUtils.loadConfiguration(mContext);
							ExerciseUtils.saveNote(mContext, ExerciseManipulate.getiIDExercise(), oNote.getText().toString());
							ExerciseUtils.populateExerciseDetails(mContext, oConfigTrainer, ExerciseManipulate.getiIDExercise());			
						}
					});
					
				}
			});
		}
				
		
		return rootView;
	}

	public void setContext(Context context,int idWorkout) {
		mContext=context;	
		mIDWorkout=idWorkout;
	}
}