package com.glm.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.glm.bean.ConfigTrainer;
import com.glm.trainer.R;
import com.glm.utils.ExerciseUtils;
import com.glm.utils.quickaction.ActionItem;
import com.glm.utils.quickaction.QuickAction;
import com.glm.utils.quickaction.QuickBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class WorkoutDetailFragment extends Fragment {
	public ConfigTrainer oConfigTrainer;
	
	private View rootView;
	private Context mContext;
	private int mIDWorkout=0;

	public Toolbar mToolbar;
	public TextView oTxt_Time;
	
	public TextView oTxt_Distance;
	
	public TextView oTxt_Kalories;
	
	public TextView oTxt_AVGSpeed;
	
	public TextView oTxt_AVGPace;
	
	public TextView oTxt_MAXSpeed;
	
	public TextView oTxt_MAXPace;

	public TextView oLbl_Step;

	public TextView oTxt_Step;
	
	public TextView oTxt_MaxBpm;
	
	public TextView oTxt_AvgBpm;
	
	public LinearLayout oLLDectails;
	
	public RelativeLayout oMaxBpm;
	
	public RelativeLayout oAvgBpm;
	
	public RelativeLayout oInfo;
	/**
	 * 
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	public WorkoutDetailFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext=getActivity().getApplicationContext();
		rootView = inflater.inflate(R.layout.new_exercise_details,
				container, false);
		rootView.setDrawingCacheEnabled(true);

		mToolbar = (Toolbar) rootView.findViewById(R.id.card_toolbar);
		mToolbar.setLogo(R.drawable.info);
		if (mToolbar != null) {
			// inflate your menu
			mToolbar.inflateMenu(R.menu.new_main);
			mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem menuItem) {
					return true;
				}
			});
		}
		oTxt_Time	  = (TextView) rootView.findViewById(R.id.textTime);
        oTxt_Distance = (TextView) rootView.findViewById(R.id.textDistance);
        oTxt_Kalories = (TextView) rootView.findViewById(R.id.textKalories);
        oTxt_AVGSpeed = (TextView) rootView.findViewById(R.id.textAVGSpeed);
        oTxt_AVGPace  = (TextView) rootView.findViewById(R.id.textPace);
        oTxt_MAXSpeed = (TextView) rootView.findViewById(R.id.textMAXSpeed);
        oTxt_MAXPace  = (TextView) rootView.findViewById(R.id.textMAXPace);
        oTxt_Step	  = (TextView) rootView.findViewById(R.id.textStep);
		oLbl_Step	  = (TextView) rootView.findViewById(R.id.lblStep);
        oTxt_MaxBpm	  = (TextView) rootView.findViewById(R.id.textMaxBpm);
        oTxt_AvgBpm	  = (TextView) rootView.findViewById(R.id.textAvgBpm);           
        oLLDectails 	= (LinearLayout) rootView.findViewById(R.id.LLDett);  
        oMaxBpm	  		= (RelativeLayout) rootView.findViewById(R.id.RLMaxBpm);
        oAvgBpm	  		= (RelativeLayout) rootView.findViewById(R.id.RLAvgBpm); 
        oInfo			= (RelativeLayout) rootView.findViewById(R.id.btnInfo);
        oInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showOptions(v);
			}
		});
       
		return rootView;
	}

	protected void showOptions(View v) {
		//Manual Sharing
		//manualShare();
		final QuickBar oBar = new QuickBar(mContext,oConfigTrainer);
		
		oBar.getQuickAction().setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {          
        @Override
        public void onItemClick(QuickAction source, int pos, int actionId) {
                //here we can filter which action item was clicked with pos or actionId parameter
                ActionItem actionItem = source.getActionItem(pos);
                Log.v(this.getClass().getCanonicalName(),"Item Click Pos: "+actionItem.getActionId());
                Intent intent = new Intent();
                switch (actionItem.getActionId()) {
				
                case 1:
					manualShare();
					break;
				case 2:
					//OLD Facebook						
					//deleteExercise(ExerciseManipulate.getiIDExercise());  																    
						
					break;
				case 3:
					if(ExerciseUtils.writeKML(-1,mContext,oConfigTrainer)){
						Toast.makeText(mContext, getString(R.string.exercise_export_ok)+" "+ExerciseUtils.sExportFile, Toast.LENGTH_SHORT)
						.show();
						//Send via mail
						final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND); 
			        	emailIntent.setType("plain/text"); 
			        	//emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"laverdone@gmail.com"}); 
			        	emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.message_subject)); 
			        	emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.message_text)); 
			        	File fileIn = new File(ExerciseUtils.sExportFile);
			            Uri u = Uri.fromFile(fileIn);       
			            emailIntent.putExtra(Intent.EXTRA_STREAM, u);
			        	startActivity(Intent.createChooser(emailIntent, "Send mail..."));
					}else{
						Toast.makeText(mContext, getString(R.string.exercise_export_ko), Toast.LENGTH_SHORT)
						.show();
					}					
					break;
										
				case 4:
					if(ExerciseUtils.writeGPX(-1,mContext,oConfigTrainer)){
						Toast.makeText(mContext, getString(R.string.exercise_export_ok)+" "+ExerciseUtils.sExportFile, Toast.LENGTH_SHORT)
						.show();
						//Send via mail
						final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND); 
			        	emailIntent.setType("plain/text"); 
			        	//emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"laverdone@gmail.com"}); 
			        	emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.message_subject)); 
			        	emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.message_text)); 
			        	File fileIn = new File(ExerciseUtils.sExportFile);
			            Uri u = Uri.fromFile(fileIn);       
			            emailIntent.putExtra(Intent.EXTRA_STREAM, u);
			        	startActivity(Intent.createChooser(emailIntent, "Send mail..."));
					}else{
						Toast.makeText(mContext, getString(R.string.exercise_export_ko), Toast.LENGTH_SHORT)
						.show();
					}				
					break;	
				case 5:
					if(ExerciseUtils.writeTCX(-1,mContext,oConfigTrainer)){
						Toast.makeText(mContext, getString(R.string.exercise_export_ok)+" "+ExerciseUtils.sExportFile, Toast.LENGTH_SHORT)
						.show();
						//Send via mail
						final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND); 
			        	emailIntent.setType("plain/text"); 
			        	//emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"laverdone@gmail.com"}); 
			        	emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.message_subject)); 
			        	emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.message_text)); 
			        	File fileIn = new File(ExerciseUtils.sExportFile);
			            Uri u = Uri.fromFile(fileIn);       
			            emailIntent.putExtra(Intent.EXTRA_STREAM, u);
			        	startActivity(Intent.createChooser(emailIntent, "Send mail..."));
					}else{
						Toast.makeText(mContext, getString(R.string.exercise_export_ko), Toast.LENGTH_SHORT)
						.show();
					}	
					break;
				default:
					break;
				}	                                
            }
        });			
		oBar.getQuickAction().show(v);
	}

	public void setContext(Context context,int idWorkout) {
		mContext=context;	
		mIDWorkout=idWorkout;
	}
	
	
	
	
	

	/**
	 * Metodo per la condivisione manuale dell'esercizio
	 * */
	public void manualShare(){
		try{			
			
			new Thread(new Runnable() {
		    	String sPathImage = Environment.getExternalStorageDirectory().getAbsolutePath()+"/personal_trainer";
				   
				@Override
				public void run() {
					File dir = new File(sPathImage);
					File mFile=null;
		            if(!dir.exists()) {
		                dir.mkdirs();
		            }
		            
		            //Task async per salvare l'immagine.
		            Bitmap oBmp =  rootView.getDrawingCache();
		            try {
		                if(oBmp!=null){
		                    mFile = new File(sPathImage+"/"+mIDWorkout+".png");
		                    FileOutputStream out = new FileOutputStream(mFile);

		                    oBmp.compress(Bitmap.CompressFormat.PNG, 90, out);
		                    out.close();
		                    out=null;
		                    MimeTypeMap mime = MimeTypeMap.getSingleton();
				            String type = mime.getMimeTypeFromExtension("png");
						    Intent sharingIntent = new Intent("android.intent.action.SEND");
						    sharingIntent.setType(type);
						    sharingIntent.putExtra("android.intent.extra.STREAM",Uri.fromFile(mFile));
						    sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(Intent.createChooser(sharingIntent,"Share using"));
		                }else{
		                    Log.e(this.getClass().getCanonicalName(),"NULL Page Preview saving image");
		                }
		            } catch (IOException e) {
		                Log.e(this.getClass().getCanonicalName(),"Error saving image");
		            }
		           
				}
				
			    
			}).start();
		    

			/*Intent sharingIntent = new Intent(Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
		
			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.glm.trainer \n\n"+getString(R.string.time)+": "+ExerciseManipulate.getsTotalTime()+
					" " + 
					getString(R.string.distance)+": "+ExerciseManipulate.getsTotalDistance() +" "+
					getString(R.string.speed_avg)+": "+ExerciseManipulate.getsAVGSpeed()+" "+getString(R.string.kalories)+": "+
					ExerciseManipulate.getsCurrentCalories()+" Kcal ");
			getActivity().get
			sharingIntent.putExtra(Intent.EXTRA_STREAM, uris.get(0));
			
			sharingIntent.setType("image/*");
			sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(Intent.createChooser(sharingIntent,"Share using"));*/
		}catch (Exception e) {
			Log.w(this.getClass().getCanonicalName(), "FB Interactive sharing error:"+e.getMessage());
			Toast.makeText(mContext, getString(R.string.share_ko), Toast.LENGTH_SHORT)
			.show();
		}
	}
}