package com.glm.app.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.glm.bean.ConfigTrainer;
import com.glm.bean.Music;
import com.glm.trainer.R;
import com.glm.utils.ExerciseUtils;

import java.util.ArrayList;

public class WorkoutMusicAdapter extends BaseAdapter{
	private ArrayList<Music> mMusic;
    private Context mContext;
    private ConfigTrainer oConfigTrainer;
  
    public WorkoutMusicAdapter(Context context, ArrayList<Music> music, ConfigTrainer configTrainer){
    	mContext=context;
    	mMusic=music;
    	oConfigTrainer=configTrainer;
    }
    
    
    
	@Override
	public int getCount() {
		return mMusic.size();
	}

	@Override
	public Object getItem(int position) {
		return mMusic.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mMusic.get(position).getiID();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Music mMusic = (Music) getItem(position);
		//if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =  infalInflater.inflate(R.layout.music_row, null);
        //}
		TextView txtNumber 	= (TextView) convertView.findViewById(R.id.txtNumber);
		TextView txtFrom 	= (TextView) convertView.findViewById(R.id.txtFrom);
		TextView txtMusic 	= (TextView) convertView.findViewById(R.id.txtMusic);
		
		if(oConfigTrainer==null){
			oConfigTrainer=ExerciseUtils.loadConfiguration(mContext);
		}
		
		txtNumber.setText(mMusic.getiID()+":");
		txtFrom.setText(ExerciseUtils.getTotalDistanceFormattated(mMusic.getfDistance(), oConfigTrainer, true));
		txtMusic.setText(mMusic.getsTITLE().replaceAll(".mp3", ""));
		return convertView;
	}
}
