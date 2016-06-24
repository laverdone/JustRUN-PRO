package com.glm.app.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.glm.app.ConstApp;
import com.glm.app.GoalActivity;
import com.glm.app.ManualWorkout;
import com.glm.app.db.Database;
import com.glm.app.stopwatch.WorkOutActivity;
import com.glm.bean.User;
import com.glm.app.NewMainActivity;
import com.glm.app.PreferencesActivity;
import com.glm.trainer.R;
import com.glm.utils.ExerciseUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class WorkoutFragment extends Fragment {
	private View rootView;
	private Context mContext;
	private RelativeLayout oStartRunningExercise;
	private RelativeLayout oStartWalkingExercise;
	private RelativeLayout oStartBikingExercise;
	//private RelativeLayout mBtnPreferences;
	private TextView oTxtBMI;
	private Toolbar mToolBar;
	private Toolbar mToolBarRunning;
	private Toolbar mToolBarWalking;
	private Toolbar mToolBarBiking;
	private ImageView mImgRunning;

	private User mUser=null;
	private float fWeight=0;
	private WorkTaskAsync oTask=null;
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	public WorkoutFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext=getActivity().getApplicationContext();
		
		
		
		rootView = inflater.inflate(R.layout.new_new_main_page,
				container, false);

		mToolBar 			  = (Toolbar)  rootView.findViewById(R.id.card_toolbar);
		mToolBarRunning		  = (Toolbar)  rootView.findViewById(R.id.card_toolbar_running);
		mToolBarWalking		  = (Toolbar)  rootView.findViewById(R.id.card_toolbar_walking);
		mToolBarBiking		  = (Toolbar)  rootView.findViewById(R.id.card_toolbar_biking);
		oStartRunningExercise = (RelativeLayout) rootView.findViewById(R.id.btn_start_running);
	    oStartWalkingExercise = (RelativeLayout) rootView.findViewById(R.id.btn_start_walking);
	    oStartBikingExercise  = (RelativeLayout) rootView.findViewById(R.id.btn_start_biking);
		//mBtnPreferences		  = (RelativeLayout) rootView.findViewById(R.id.btn_preferences);
		mImgRunning 		  = (ImageView) rootView.findViewById(R.id.imgRunning);


	    oTxtBMI				  = (TextView) rootView.findViewById(R.id.txtBMI);

		mToolBar.inflateMenu(R.menu.preferences);
		mToolBar.setLogo(R.drawable.toolbar);
		mToolBar.setTitle(R.string.app_name_pro);
		mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent();


				if (item.getTitle().equals( mContext.getString(R.string.pref_title))) {
					intent.setClass(mContext, PreferencesActivity.class);
					intent.putExtra("pref_type",2);
					startActivity(intent);
					getActivity().finish();
				}else if (item.getTitle().equals( mContext.getString(R.string.general_pref))) {
					intent.setClass(mContext, PreferencesActivity.class);
					intent.putExtra("pref_type",3);
					startActivity(intent);
					getActivity().finish();
				}else if (item.getTitle().equals( mContext.getString(R.string.show_motivator))) {
					intent.setClass(mContext, PreferencesActivity.class);
					intent.putExtra("pref_type",1);
					startActivity(intent);
					getActivity().finish();
				}else if (item.getTitle().equals( mContext.getString(R.string.user))) {
					intent.setClass(mContext, PreferencesActivity.class);
					intent.putExtra("pref_type",4);
					startActivity(intent);
					getActivity().finish();
				}else if(item.getTitle().equals( mContext.getString(R.string.backup))) {
					new AlertDialog.Builder(getActivity())
							.setTitle(getString(R.string.app_name_pro))
							.setMessage(getString(R.string.backup_desc))
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog, int whichButton) {
									Database oDB = new Database(mContext);
									oDB.backupDB();
									Toast.makeText(mContext, mContext.getString(R.string.backup_ok), Toast.LENGTH_SHORT)
											.show();
								}})
							.setNegativeButton(android.R.string.no, null).show();

				}else if(item.getTitle().equals( mContext.getString(R.string.restore))) {
					new AlertDialog.Builder(getActivity())
							.setTitle(getString(R.string.app_name_pro))
							.setMessage(getString(R.string.restore_desc))
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog, int whichButton) {
									Database oDB = new Database(mContext);
									oDB.restoreDB();
									Toast.makeText(mContext, mContext.getString(R.string.restore_ok), Toast.LENGTH_SHORT)
											.show();
								}})
							.setNegativeButton(android.R.string.no, null).show();


				}

				return false;
			}
		});

		mToolBarRunning.inflateMenu(R.menu.manual);
		mToolBarRunning.setTitle(R.string.run);
		mToolBarRunning.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if(((NewMainActivity)getActivity()).oConfigTrainer!=null
						&& ((NewMainActivity)getActivity()).oConfigTrainer.isbTrackExercise()){
					trackWeight(ConstApp.TYPE_MANUAL_RUN);
					return false;
				}

				Intent intent = new Intent();
				intent.setClass(mContext, ManualWorkout.class);
				intent.putExtra("type", ConstApp.TYPE_MANUAL_RUN);
				Toast.makeText(mContext, "Manual Bike", Toast.LENGTH_SHORT)
						.show();
				startActivity(intent);
				getActivity().finish();
				return false;
			}
		});

		mToolBarWalking.inflateMenu(R.menu.manual);
		mToolBarWalking.setTitle(R.string.walk);
		mToolBarWalking.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if(((NewMainActivity)getActivity()).oConfigTrainer!=null &&
						((NewMainActivity)getActivity()).oConfigTrainer.isbTrackExercise()){
					trackWeight(ConstApp.TYPE_MANUAL_WALK);
					return false;
				}

				Intent intent = new Intent();
				intent.setClass(mContext, ManualWorkout.class);
				intent.putExtra("type", ConstApp.TYPE_MANUAL_WALK);
				startActivity(intent);
				Toast.makeText(mContext, "Manual Walk", Toast.LENGTH_SHORT)
						.show();
				getActivity().finish();

				return false;
			}
		});

		mToolBarBiking.inflateMenu(R.menu.manual);
		mToolBarBiking.setTitle(R.string.bike);
		mToolBarBiking.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				if(((NewMainActivity)getActivity()).oConfigTrainer!=null &&
						((NewMainActivity)getActivity()).oConfigTrainer.isbTrackExercise()){
					trackWeight(ConstApp.TYPE_MANUAL_BIKE);
					return false;
				}

				Intent intent = new Intent();
				intent.setClass(mContext, ManualWorkout.class);
				intent.putExtra("type", ConstApp.TYPE_MANUAL_BIKE);
				Toast.makeText(mContext, "Manual Bike", Toast.LENGTH_SHORT)
						.show();
				startActivity(intent);
				getActivity().finish();

				return false;
			}
		});

	    oStartRunningExercise.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(((NewMainActivity)getActivity()).oConfigTrainer!=null &&
						((NewMainActivity)getActivity()).oConfigTrainer.isbTrackExercise()){
					trackWeight(ConstApp.TYPE_RUN);
					return;
				}

				Intent intent = new Intent();
				if(((NewMainActivity)getActivity()).oConfigTrainer.isbRunGoal()){						
					intent.setClass(mContext, GoalActivity.class);
					intent.putExtra("type", ConstApp.TYPE_RUN);
					startActivity(intent);
					getActivity().finish();
				}else{
					intent.setClass(mContext, WorkOutActivity.class);
					intent.putExtra("type", ConstApp.TYPE_RUN);
					startActivity(intent);
					getActivity().finish();
				}	
				
			}
		} );
	    oStartWalkingExercise.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(((NewMainActivity)getActivity()).oConfigTrainer!=null &&
						((NewMainActivity)getActivity()).oConfigTrainer.isbTrackExercise()){
					trackWeight(ConstApp.TYPE_WALK);
					return;
				}

				Intent intent = new Intent();
				if(((NewMainActivity)getActivity()).oConfigTrainer.isbRunGoal()){					
					intent.setClass(mContext, GoalActivity.class);
					intent.putExtra("type",  ConstApp.TYPE_WALK);
					startActivity(intent);
					getActivity().finish();		
				}else{				
					intent.setClass(mContext, WorkOutActivity.class);
					intent.putExtra("type", ConstApp.TYPE_WALK);
					startActivity(intent);
					getActivity().finish();			
				}
			}
		} );
	    oStartBikingExercise.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				if(((NewMainActivity)getActivity()).oConfigTrainer!=null &&
						((NewMainActivity)getActivity()).oConfigTrainer.isbTrackExercise()){
					trackWeight(ConstApp.TYPE_BIKE);
					return;
				}

				Intent intent = new Intent();
				if(((NewMainActivity)getActivity()).oConfigTrainer.isbRunGoal()){
					intent.setClass(mContext, GoalActivity.class);
					intent.putExtra("type",  ConstApp.TYPE_BIKE);
					startActivity(intent);
					getActivity().finish();	
				}else{
					intent.setClass(mContext, WorkOutActivity.class);
					intent.putExtra("type", ConstApp.TYPE_BIKE);
					startActivity(intent);						
					getActivity().finish();	
				}
				
			}
		} );
	    
	    /*mBtnPreferences.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, PreferencesActivity.class);
				startActivity(intent);
				getActivity().finish();	
			}
		});*/
	    
	    if(((NewMainActivity)getActivity()).oConfigTrainer!=null){
			oTxtBMI.setText(((NewMainActivity)getActivity()).mUser.getsBMI(((NewMainActivity)getActivity()).oConfigTrainer.getiUnits()));
			if(((NewMainActivity)getActivity()).oConfigTrainer.getsGender().toLowerCase().equals("f")){
				mImgRunning.setImageResource(R.drawable.running_dark_f);
			}else{
				mImgRunning.setImageResource(R.drawable.running_dark);
			}

		}


		if(getActivity().getPackageName().equals(ConstApp.ADS_APP_PACKAGE_NAME)){
			AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
			AdRequest adRequest = new AdRequest.Builder().build();
			mAdView.loadAd(adRequest);
		}else{
			AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
			mAdView.setVisibility(View.GONE);
		}
	    /*ImageButton imgBtnMore = (ImageButton) rootView.findViewById(R.id.imgBtnMore);
	    imgBtnMore.setOnClickListener(new OnClickListener() {
	    	final MainQuickBar oBar = new MainQuickBar(mContext);
			@Override
			public void onClick(View v) {
				oBar.getQuickAction().setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {          
			        @Override
			        public void onItemClick(QuickAction source, int pos, int actionId) {
			                //here we can filter which action item was clicked with pos or actionId parameter
			                ActionItem actionItem = source.getActionItem(pos);
			                Log.v(this.getClass().getCanonicalName(),"Item Click Pos: "+actionItem.getActionId());
			                Intent intent = new Intent();
			                intent.setClass(mContext, ManualWorkout.class);
			                switch (actionItem.getActionId()) {
							
			                case 1:	                	
			                	intent.putExtra("type", "1000");	
								Toast.makeText(mContext, "Manual Bike", Toast.LENGTH_SHORT)
								.show();	
								startActivity(intent);
								getActivity().finish();
								break;
							case 2:	
								intent.putExtra("type", "10000");
			        			startActivity(intent);			
			        			Toast.makeText(mContext, "Manual Walk", Toast.LENGTH_SHORT)
			        			.show();
			        			getActivity().finish();	
								break;
							case 3:
								intent.putExtra("type", "1001");	
								Toast.makeText(mContext, "Manual Bike", Toast.LENGTH_SHORT)
								.show();	
								startActivity(intent);
								getActivity().finish();
								break;
							default:
								break;
							}	                                
			            }
			        });			
					oBar.getQuickAction().show(v);
			}
		});*/
	   
		
		
	    
		return rootView;
	}

	public void setContext(Context context) {
		mContext=context;
	}
	@Override
	public void onResume() {
		//oTask = new WorkTaskAsync();
		//oTask.execute();
		super.onResume();
	}
	class WorkTaskAsync extends AsyncTask{

		@Override
		protected Object doInBackground(Object... params) {
			Log.v(this.getClass().getCanonicalName(),"doInBackground WorkTaskAsync");
			((NewMainActivity)getActivity()).oConfigTrainer=ExerciseUtils.loadConfiguration(mContext);
			((NewMainActivity)getActivity()).mUser = ExerciseUtils.loadUserDectails(mContext);
			return true;
		}
		
		@Override
		protected void onPostExecute(Object result) {
		    
		    oTask=null;
		}
	}

	/**
	 * Traccio il peso mostrando una alert per inserire il peso
	 * ad ogni esercizio
	 *
	 * */
	private void trackWeight(final int typeWorkOut) {
		mUser=ExerciseUtils.loadUserDectails(mContext);

		AlertDialog.Builder builder;
		final AlertDialog alertDialog;

		LayoutInflater inflater = getActivity().getLayoutInflater();
		LinearLayout dialoglayout = (LinearLayout) inflater.inflate(R.layout.track_weight,(ViewGroup) getActivity().findViewById(R.id.objMainLayout));
		Button btmPlus =null;
		Button btmMinus =null;
		EditText txtWeight = null;
		int iChild=dialoglayout.getChildCount();
		for(int i=0;i<iChild;i++){
			if(i==0){
				LinearLayout oInternal = (LinearLayout) dialoglayout.getChildAt(i);
				int jChild=oInternal.getChildCount();
				for(int j=0;j<jChild;j++){
					if(j==0){
						//btnPlus
						btmPlus = (Button) oInternal.getChildAt(j);
					}else if (j==1){
						//txtWeight
						txtWeight = (EditText) oInternal.getChildAt(j);
					}else{
						//btnMinus
						btmMinus = (Button) oInternal.getChildAt(j);
					}
				}
			}
		}

		final EditText oTxtWeight=txtWeight;
		//Log.v(this.getClass().getCanonicalName(),"Peso: "+User.getiWeight());
		//Imposto il peso corrente
		oTxtWeight.setText(String.valueOf(mUser.iWeight));
		btmPlus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				try{
					fWeight=Float.parseFloat(oTxtWeight.getText().toString());
				}catch (Exception e) {
					//Log.v(this.getClass().getCanonicalName(), "parseInt Weight error");
				}
				fWeight+=1;
				oTxtWeight.setText(String.valueOf(fWeight));
			}
		});

		btmMinus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				try{
					fWeight=Float.parseFloat(oTxtWeight.getText().toString());
				}catch (Exception e) {
					//Log.v(this.getClass().getCanonicalName(), "parseInt Weight error");
				}
				fWeight-=1;
				if(fWeight<0) fWeight=0;
				oTxtWeight.setText(String.valueOf(fWeight));

			}
		});

		builder = new AlertDialog.Builder(getActivity());
		builder.setView(dialoglayout);

		alertDialog = builder.create();
		alertDialog.setTitle(this.getString(R.string.track_weight));
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,this.getString(R.string.run), new android.content.DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {


				//Se non premo plus/minus
				try{
					fWeight=Float.parseFloat(oTxtWeight.getText().toString());
				}catch(NumberFormatException e){
					fWeight=((NewMainActivity)getActivity()).oConfigTrainer.getiWeight();
				}

				ExerciseUtils.saveUserData(mContext,((NewMainActivity)getActivity()).oConfigTrainer.getsNick(),((NewMainActivity)getActivity()).oConfigTrainer.getsName(),
						String.valueOf(fWeight),String.valueOf(((NewMainActivity)getActivity()).oConfigTrainer.getiAge()),String.valueOf(((NewMainActivity)getActivity()).oConfigTrainer.getiHeight())
						,false,false,false,((NewMainActivity)getActivity()).oConfigTrainer.getsGender());

				if(typeWorkOut==ConstApp.TYPE_MANUAL_RUN ||
						typeWorkOut==ConstApp.TYPE_MANUAL_WALK ||
							typeWorkOut==ConstApp.TYPE_MANUAL_BIKE){
					Intent intent = new Intent();
					intent.setClass(mContext, ManualWorkout.class);
					intent.putExtra("type", typeWorkOut);
					startActivity(intent);
					getActivity().finish();
				}else if(typeWorkOut==ConstApp.TYPE_RUN ||
							typeWorkOut==ConstApp.TYPE_WALK ||
								typeWorkOut==ConstApp.TYPE_BIKE){
					Intent intent = new Intent();
					if(((NewMainActivity)getActivity()).oConfigTrainer.isbRunGoal()){
						intent.setClass(mContext, GoalActivity.class);
						intent.putExtra("type",  typeWorkOut);
						startActivity(intent);
						getActivity().finish();
					}else{
						intent.setClass(mContext, WorkOutActivity.class);
						intent.putExtra("type", typeWorkOut);
						startActivity(intent);
						getActivity().finish();
					}
				}
			}});
		alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,this.getString(R.string.no),
				new android.content.DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						alertDialog.dismiss();
					}
				});
		alertDialog.show();
	}
}