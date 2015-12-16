package com.glm.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.glm.app.fragment.adapter.WorkoutMusicAdapter;
import com.glm.bean.ConfigTrainer;
import com.glm.bean.Music;
import com.glm.trainer.R;

import java.util.ArrayList;



/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class WorkoutMusicFragment extends Fragment {
	private ConfigTrainer oConfigTrainer;
	
	//private List<Overlay> mapOverlays;
	
	//private MapController oMapController;
	//private GeoPoint TrackPoints [];
	//private TrackOverlay oTrack;
	private Context mContext;
	private View rootView;
	private int mIDWorkout=0;
	private ListView mListMusic=null;
	private WorkoutMusicAdapter mWorkoutMusicAdapter;
	/**
	 * 
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	public WorkoutMusicFragment() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		rootView = inflater.inflate(R.layout.workout_music,
					container, false);
		mListMusic = (ListView) rootView.findViewById(R.id.listWorkoutMusic);	
		
		
		   
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		mContext=getActivity().getApplicationContext();
		
		
	}
	
	public void setContext(Context context,int idWorkout) {
		mContext=context;	
		mIDWorkout=idWorkout;
	}
	
	/**
	 * Aggiorna la lista
	 * */
	public void addMusic(ArrayList<Music> mMusic){
		mWorkoutMusicAdapter = new WorkoutMusicAdapter(mContext, mMusic, oConfigTrainer);
		mListMusic.setAdapter(mWorkoutMusicAdapter);
		mWorkoutMusicAdapter.notifyDataSetChanged();
		mWorkoutMusicAdapter.notifyDataSetInvalidated();
	}
}