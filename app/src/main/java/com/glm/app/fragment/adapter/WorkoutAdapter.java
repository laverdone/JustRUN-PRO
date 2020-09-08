package com.glm.app.fragment.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.glm.app.ConstApp;
import com.glm.app.stopwatch.WorkOutActivity;
import com.glm.bean.ChallengeExercise;
import com.glm.bean.ConfigTrainer;
import com.glm.bean.Exercise;
import com.glm.app.HistoryList;
import com.glm.trainer.R;
import com.glm.app.WorkoutDetail;
import com.glm.utils.ExerciseUtils;

import java.io.File;
import java.util.ArrayList;

public class WorkoutAdapter extends BaseAdapter{
	private ArrayList<Exercise> mWorkouts;
    private HistoryList mContext;
	public ConfigTrainer oConfigTrainer;
	private Exercise mExercise;

	private Animation a;
	private View mRootView;

    public WorkoutAdapter(HistoryList context, ArrayList<Exercise> workouts){
    	mContext=context;
    	mWorkouts=workouts;
		try {
			oConfigTrainer = ExerciseUtils.loadConfiguration(mContext);
		} catch (NullPointerException e) {
			Log.e(this.getClass().getCanonicalName(), "Error load Config");
			return;
		}
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Exercise oExercise = (Exercise) getItem(position);
		mExercise=oExercise;
		//if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =  infalInflater.inflate(R.layout.history_row_cardview, null);
        //}
		mRootView=convertView;
		mRootView.setDrawingCacheEnabled(true);
		mRootView.buildDrawingCache(true);
		Toolbar toolbar = (Toolbar) convertView.findViewById(R.id.card_toolbar);
		toolbar.setLogo(R.drawable.calendar);
		toolbar.setTitle(oExercise.getDateFormatted(mContext));

		if (toolbar != null) {
			// inflate your menu
			toolbar.inflateMenu(R.menu.history_menu);

			toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem menuItem) {
					Log.v(this.getClass().getCanonicalName(),"menu itme is: "+menuItem.getTitle());

					if (menuItem.getTitle().equals( mContext.getString(R.string.erase))) {
						AlertDialog alertDialog;
						alertDialog = new AlertDialog.Builder(mContext).create();
						alertDialog.setTitle(mContext.getString(R.string.titledeleteexercise));
						alertDialog.setMessage(mContext.getString(R.string.messagedeleteexercise));
						alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,mContext.getString(R.string.yes), new android.content.DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								a = AnimationUtils.loadAnimation(mContext, R.anim.disappear);
								a.reset();

								if(ExerciseUtils.deleteExercise(mContext, oExercise.getsIDExerise())){
									mWorkouts.remove(position);
									WorkoutAdapter.this.notifyDataSetChanged();
									WorkoutAdapter.this.notifyDataSetInvalidated();
								}
								mRootView.clearAnimation();
								mRootView.setAnimation(a);
								mRootView.animate();
							}
						});

						alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,mContext.getString(R.string.no), new android.content.DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface arg0, int arg1) {

							}

						});
						alertDialog.show();
					}else if (menuItem.getTitle().equals( mContext.getString(R.string.export_kml))) {
						if(ExerciseUtils.writeKML(-1,mContext,oConfigTrainer)){
							Toast.makeText(mContext, mContext.getString(R.string.exercise_export_ok)+" "+ExerciseUtils.sExportFile, Toast.LENGTH_SHORT)
									.show();
							//Send via mail
							final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
							emailIntent.setType("plain/text");
							//emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"laverdone@gmail.com"});
							emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mContext.getString(R.string.message_subject));
							emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, mContext.getString(R.string.message_text));
							File fileIn = new File(ExerciseUtils.sExportFile);
							Uri u = Uri.fromFile(fileIn);
							emailIntent.putExtra(Intent.EXTRA_STREAM, u);
							mContext.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
						}else{
							Toast.makeText(mContext, mContext.getString(R.string.exercise_export_ko), Toast.LENGTH_SHORT)
									.show();
						}
					}else if (menuItem.getTitle().equals( mContext.getString(R.string.export_gpx))) {
						if(ExerciseUtils.writeGPX(-1,mContext,oConfigTrainer)){
							Toast.makeText(mContext, mContext.getString(R.string.exercise_export_ok)+" "+ExerciseUtils.sExportFile, Toast.LENGTH_SHORT)
									.show();
							//Send via mail
							final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
							emailIntent.setType("plain/text");
							//emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"laverdone@gmail.com"});
							emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mContext.getString(R.string.message_subject));
							emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, mContext.getString(R.string.message_text));
							File fileIn = new File(ExerciseUtils.sExportFile);
							Uri u = Uri.fromFile(fileIn);
							emailIntent.putExtra(Intent.EXTRA_STREAM, u);
							mContext.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
						}else{
							Toast.makeText(mContext, mContext.getString(R.string.exercise_export_ko), Toast.LENGTH_SHORT)
									.show();
						}
					}else if (menuItem.getTitle().equals( mContext.getString(R.string.export_tcx))) {
						if(ExerciseUtils.writeTCX(-1,mContext,oConfigTrainer)){
							Toast.makeText(mContext, mContext.getString(R.string.exercise_export_ok)+" "+ExerciseUtils.sExportFile, Toast.LENGTH_SHORT)
									.show();
							//Send via mail
							final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
							emailIntent.setType("plain/text");
							//emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"laverdone@gmail.com"});
							emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mContext.getString(R.string.message_subject));
							emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, mContext.getString(R.string.message_text));
							File fileIn = new File(ExerciseUtils.sExportFile);
							Uri u = Uri.fromFile(fileIn);
							emailIntent.putExtra(Intent.EXTRA_STREAM, u);
							mContext.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
						}else{
							Toast.makeText(mContext, mContext.getString(R.string.exercise_export_ko), Toast.LENGTH_SHORT)
									.show();
						}
					}else if (menuItem.getTitle().equals( mContext.getString(R.string.use_as_opponent))) {
						if(oExercise.getiTypeExercise()==ConstApp.TYPE_BIKE ||
								oExercise.getiTypeExercise()==ConstApp.TYPE_RUN ||
								oExercise.getiTypeExercise()==ConstApp.TYPE_WALK) {
							//Devo esportare l'esercizio come opponent
							ChallengeExercise mChallengeExercise = ExerciseUtils.getChallengeExrcise(mContext, oConfigTrainer, Integer.parseInt(oExercise.getsIDExerise()));
							Intent intent = new Intent();
							intent.setClass(mContext, WorkOutActivity.class);
							intent.putExtra("type", oExercise.getiTypeExercise());
							intent.putExtra("challengeexercise", mChallengeExercise);
							mContext.startActivity(intent);
							mContext.finish();
						}else{
							Toast.makeText(mContext, mContext.getString(R.string.unable_to_use_as_opponent), Toast.LENGTH_SHORT)
									.show();
						}
					}


					return true;
				}
			});
		}

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
				if(oExercise.getiTypeExercise()==0
						|| oExercise.getiTypeExercise()==1
							|| oExercise.getiTypeExercise()==100) {
					Intent intent = new Intent();
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setClass(mContext, WorkoutDetail.class);
					intent.putExtra("exercise_id", Integer.parseInt(oExercise.getsIDExerise()));

					mContext.startActivity(intent);
					mContext.finish();
				}else{
					Toast.makeText(mContext, mContext.getString(R.string.not_avail_manual), Toast.LENGTH_SHORT)
							.show();
				}
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

		//SimpleDateFormat dnf = new SimpleDateFormat("yyyy-MMM-dd");



		/*Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", ge);
		cal.setTime(sdf.parse("Mon Mar 14 16:02:37 GMT 2011"));// all done
		oExerxise.setdDateExercise(oCursor.get);*/

		txtTime.setText(oExercise.getsTotalTime());
		txtKm.setText(oExercise.getsAVGSpeed());
		txtDistance.setText(oExercise.getsDistanceFormatted());
		txtKal.setText(oExercise.getsTotalKalories());
		return convertView;
	}
}
