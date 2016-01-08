package com.glm.app.fragment.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glm.bean.Exercise;
import com.glm.trainer.HistoryList;
import com.glm.trainer.R;
import com.glm.trainer.WorkoutDetail;
import com.glm.utils.ExerciseUtils;

import java.util.ArrayList;

public class WorkoutAdapter extends BaseAdapter{
	private ArrayList<Exercise> mWorkouts;
    private HistoryList mContext;

	private Animation a;


    public WorkoutAdapter(HistoryList context, ArrayList<Exercise> workouts){
    	mContext=context;
    	mWorkouts=workouts;

    }
    
    
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mWorkouts.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mWorkouts.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return Integer.parseInt(mWorkouts.get(position).getsIDExerise());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Exercise oExercise = (Exercise) getItem(position);
		//if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =  infalInflater.inflate(R.layout.history_row_cardview, null);
        //}

		Toolbar toolbar = (Toolbar) convertView.findViewById(R.id.card_toolbar);
		toolbar.setTitle(oExercise.getsStart());
		if (toolbar != null) {
			// inflate your menu
			toolbar.inflateMenu(R.menu.new_main);

			toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem menuItem) {
					AlertDialog alertDialog;
					alertDialog = new AlertDialog.Builder(mContext).create();
					alertDialog.setTitle(mContext.getString(R.string.titledeleteexercise));
					alertDialog.setMessage(mContext.getString(R.string.messagedeleteexercise));
					alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,mContext.getString(R.string.yes), new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							a = AnimationUtils.loadAnimation(mContext, R.animator.disappear);
							a.reset();

							if(ExerciseUtils.deleteExercise(mContext, oExercise.getsIDExerise())){
								notifyDataSetChanged();
								notifyDataSetInvalidated();
							}

						}
					});

					alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,mContext.getString(R.string.no), new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface arg0, int arg1) {

						}

					});
					alertDialog.show();

					return true;
				}
			});
		}

		TextView txtDate 			= (TextView) convertView.findViewById(R.id.txtDate);
		TextView txtTime 			= (TextView) convertView.findViewById(R.id.txtTime);
		TextView txtKm 				= (TextView) convertView.findViewById(R.id.txtKm);
		TextView txtDistance		= (TextView) convertView.findViewById(R.id.txtDistance);
		TextView txtKal				= (TextView) convertView.findViewById(R.id.txtKal);
		ImageView imgTypeExercise	= (ImageView) convertView.findViewById(R.id.imgTypeExercise);
		//ImageButton btnDett			= (ImageButton) convertView.findViewById(R.id.btnDett);
		final LinearLayout btnDett			= (LinearLayout) convertView.findViewById(R.id.btnDett);

		btnDett.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setClass(mContext, WorkoutDetail.class);
				intent.putExtra("exercise_id", Integer.parseInt(oExercise.getsIDExerise()));
				
				mContext.startActivity(intent);
				
			}
		});
		if(oExercise.getiTypeExercise()==0){
			//Runnnig
			imgTypeExercise.setBackgroundResource(R.drawable.running_dark);
			
		}else if(oExercise.getiTypeExercise()==1){
			imgTypeExercise.setBackgroundResource(R.drawable.biking_dark);
			
		}else if(oExercise.getiTypeExercise()==100){
			imgTypeExercise.setBackgroundResource(R.drawable.walking_dark);
			
		}
		btnDett.setLongClickable(true);
		btnDett.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Vibrator mVibration = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
				// Vibrate for 500 milliseconds
				mVibration.vibrate(500);
				btnDett.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.lista_gradient_delete));
				//deleteExercise(mRow, oExercise.getsIDExerise());
				mContext.mINWorkoutToDelete+=", "+oExercise.getsIDExerise();
				return false;
			}
		});
		txtDate.setText(oExercise.getsStart());
		txtTime.setText(oExercise.getsTotalTime());
		txtKm.setText(oExercise.getsAVGSpeed());
		txtDistance.setText(oExercise.getsDistanceFormatted());
		txtKal.setText(oExercise.getsTotalKalories());
		return convertView;
	}
}
