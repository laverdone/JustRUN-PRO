package com.glm.app.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
			mToolBar.inflateMenu(R.menu.new_main);
			mToolBar.setLogo(R.drawable.toolbar);
			mToolBar.setTitle(R.string.maps);
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
}