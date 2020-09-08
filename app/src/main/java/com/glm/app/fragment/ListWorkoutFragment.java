package com.glm.app.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.glm.app.fragment.adapter.WorkoutAdapter;
import com.glm.bean.Exercise;
import com.glm.trainer.R;

import java.util.ArrayList;



/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class ListWorkoutFragment extends Fragment {
	private View rootView;
	private Context mContext;
	public ListView mListWorkOuts;
	
	private ArrayList<Exercise> aExercise = new ArrayList<Exercise>();
	private WorkoutAdapter mWorkoutAdapter=null;
	
	private ProgressDialog oWaitforLoad=null;
	/**
	 * 
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	public ListWorkoutFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(isAdded())	mContext=getActivity().getApplicationContext();
		rootView = inflater.inflate(R.layout.workout_history,
				container, false);

		mListWorkOuts = (ListView) rootView.findViewById(R.id.listWorkouts);
	    mListWorkOuts.setAdapter(mWorkoutAdapter);    
		return rootView;
	}

	public void setContext(Context context, WorkoutAdapter adapterWorkout) {
		mContext=context;
		mWorkoutAdapter=adapterWorkout;
	}    
}