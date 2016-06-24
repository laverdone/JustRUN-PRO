package com.glm.app.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.glm.app.ConstApp;
import com.glm.bean.ConfigTrainer;
import com.glm.bean.ExerciseManipulate;
import com.glm.trainer.R;
import com.glm.utils.ExerciseUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class WorkoutMapFragment extends Fragment {
	private ConfigTrainer oConfigTrainer;
	private GoogleMap oMapView=null;
	
	//private List<Overlay> mapOverlays;
	
	//private MapController oMapController;
	//private GeoPoint TrackPoints [];
	//private TrackOverlay oTrack;
	private Context mContext;
	private View rootView;
	private int mIDWorkout=0;
	private Toolbar mToolBar;
	/**
	 * 
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	public WorkoutMapFragment() {
		
	}

	@Override
	public void onDestroyView() {
		
		super.onDestroyView();
		try {
	        SupportMapFragment fragment = (SupportMapFragment) getActivity()
	                                          .getSupportFragmentManager().findFragmentById(
	                                        		  R.id.gmapv2);
	        if (fragment != null) getFragmentManager().beginTransaction().remove(fragment).commit();

	    } catch (IllegalStateException e) {
	        //handle this situation because you are necessary will get 
	        //an exception here :-(
	    }

		rootView=null;
		oMapView=null;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		Log.v(this.getClass().getCanonicalName(), "onCreateView MAP Fragment");
		((ViewGroup) container.getParent()).removeView(rootView);
		
		try {

			//getActivity().getFragmentManager().findFragmentById(id)
			if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
				rootView = inflater.inflate(R.layout.maps_v2,
						container, false);
				Log.v(this.getClass().getCanonicalName(), "inflate OK MAP Fragment");
				oMapView = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.gmapv2)).getMap();
			}else{
				rootView = inflater.inflate(R.layout.maps_v2l,
						container, false);
				Log.v(this.getClass().getCanonicalName(), "inflate OK MAP Fragment");
				oMapView = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.gmapv2)).getMap();
			}

			mToolBar 			  = (Toolbar)  rootView.findViewById(R.id.card_toolbar);
			mToolBar.inflateMenu(R.menu.workout_menu);
			mToolBar.setLogo(R.drawable.map);
			mToolBar.setTitle(R.string.maps);
			mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
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

								if(ExerciseUtils.deleteExercise(mContext, String.valueOf(mIDWorkout))){
									getActivity().onBackPressed();
								}

							}
						});

						alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,mContext.getString(R.string.no), new android.content.DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface arg0, int arg1) {

							}

						});
						alertDialog.show();
					}else if (menuItem.getTitle().equals( mContext.getString(R.string.share_long))) {
						manualShare();
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
					}


					return true;
				}
			});
			/*SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
			FragmentTransaction fragmentTransaction =
			             getChildFragmentManager().beginTransaction();
			     fragmentTransaction.add(R.id.gmapv2, mMapFragment);
			     fragmentTransaction.commit(); 
			
			oMapView= mMapFragment.getMap(); */
	    } catch (InflateException e) {
	        /* map is already there, just return view as it is */
	    	Log.e(this.getClass().getCanonicalName(),"InflateException");
	    	e.printStackTrace();
	    }catch (Exception e) {
			// TODO: handle exception
	    	Log.e(this.getClass().getCanonicalName(),"Exception");
	    	e.printStackTrace();
		}
		
		   
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		mContext=getActivity().getApplicationContext();
		//GraphTask oTask = new GraphTask();
	    //oTask.execute();
	}
	
	public void setContext(Context context,int idWorkout) {
		mContext=context;	
		mIDWorkout=idWorkout;
		//new Thread(new MapThread()).start();
		GraphTask oTask = new GraphTask();
		oTask.execute();
		//Toast.makeText(mContext, "SET CONTEXT", Toast.LENGTH_LONG)
		//		.show();
	}


	class GraphTask extends AsyncTask<Void, Void, Void> {
		List<LatLng> aLatLng=null;
		@Override
		protected Void doInBackground(Void... params) {
			oConfigTrainer = ExerciseUtils.loadConfiguration(mContext);		
			ExerciseUtils.populateExerciseDetails(mContext, oConfigTrainer,  mIDWorkout);
			
			aLatLng = new ArrayList<LatLng>();
			
			for(int i=0;i<ExerciseManipulate.getWatchPoint().size();i++){
				aLatLng.add(ExerciseManipulate.getWatchPoint().get(i).getLatLong());
			}		    
			while(oMapView==null) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Log.e(this.getClass().getCanonicalName(), "Error during wait maps");
				}
			}
	        return null;
		}
		
		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(Void result) {

			Polyline route = oMapView.addPolyline(new PolylineOptions()
			  .width(5)
			  .color(Color.BLUE)
			  .geodesic(true));
			route.setPoints(aLatLng);
			
		    oMapView.addMarker(new MarkerOptions()
	        .position(ExerciseManipulate.getWatchPoint().get(0).getLatLong())
	        .title("Start")); 
		    oMapView.addMarker(new MarkerOptions()
	        .position(ExerciseManipulate.getWatchPoint().get(ExerciseManipulate.getWatchPoint().size()-1).getLatLong())
	        .title("End "+ExerciseManipulate.getsTotalDistance())); 
		    
			CameraPosition newCamPos = new CameraPosition(ExerciseManipulate.getWatchPoint().get(ExerciseManipulate.getWatchPoint().size()/2).getLatLong(), 
                    12.5f, 
                    oMapView.getCameraPosition().tilt, //use old tilt 
                    oMapView.getCameraPosition().bearing); //use old bearing
			oMapView.animateCamera(CameraUpdateFactory.newCameraPosition(newCamPos), 4000, null);
			
			//oMapView.addPolyline((new PolylineOptions()).addAll(aLatLng));
			//oMapView.moveCamera(CameraUpdateFactory.newLatLng(ExerciseManipulate.getWatchPoint().get(0).getLatLong()));
		}
	}
	/**
	 * Metodo per la condivisione manuale dell'esercizio
	 * */
	public void manualShare(){
		try{

			new Thread(new Runnable() {
				String sPathImage = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ ConstApp.FOLDER_PERSONAL_TRAINER;

				@Override
				public void run() {

					rootView.post(new Runnable() {
						@Override
						public void run() {


							oMapView.snapshot(new GoogleMap.SnapshotReadyCallback() {
								@Override
								public void onSnapshotReady(Bitmap bitmap) {
									try {
										File dir = new File(sPathImage);
										File mFile=null;
										if(!dir.exists()) {
											dir.mkdirs();
										}
										if(bitmap!=null){
											mFile = new File(sPathImage+"/map_"+mIDWorkout+".png");
											FileOutputStream out = new FileOutputStream(mFile);

											bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
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
							});

						}
					});

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