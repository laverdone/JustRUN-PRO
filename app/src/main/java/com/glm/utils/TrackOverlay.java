/*package com.glm.utils;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import com.glm.trainer.R;

import com.glm.bean.ConfigTrainer;
import com.glm.bean.WatchPoint;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class TrackOverlay extends Overlay {
	
	private GeoPoint locpoint;
    private Paint paint;
    private GeoPoint routePoints [];
    private boolean routeIsActive;	
    private Point pold, pnew, pp;
    private int numberRoutePoints;

    private GeoPoint startPoint;
    private GeoPoint centerPoint;
    private GeoPoint finishPoint;
    private ArrayList<WatchPoint> aWatchPoint;
    private String sTotalTime;
    private String sDistance;
    private Context oContext;
    // Constructor permitting the route array to be passed as an argument.
    public TrackOverlay(Context mContext, GeoPoint[] routePoints, ArrayList<WatchPoint> aWPList, String Pace, String Distance) {
    	aWatchPoint=aWPList;
    	sTotalTime=Pace;
        sDistance=Distance;
        oContext=mContext;
        try{
        	numberRoutePoints  = populateTrackPoints(routePoints,aWPList).length;
        }catch(NegativeArraySizeException e){
        	numberRoutePoints=0;
        }
                    	
    	if(numberRoutePoints==0) return;
    	
    	routeIsActive = true;
        // If first time, set initial location to start of route
        locpoint = this.routePoints[0];
        pold = new Point(0, 0);
        pnew = new Point(0,0);
        pp = new Point(0,0);
        paint = new Paint();
    }
    
    private GeoPoint[] populateTrackPoints(GeoPoint[] routePoints, ArrayList<WatchPoint> aWPList) {
    	try{
	    	if((aWPList.size()-1) <= 0) {
	    		routePoints = new GeoPoint[1];
	    		return routePoints;
	    	}
	    	routePoints = new GeoPoint[aWPList.size()-1];  	 		
			if(aWPList.size()==0) return null;
			int iSize=aWPList.size()-2;
			for(int i=0;i<iSize;i++){
				int latE6 = (int) (aWPList.get(i).getdLat() * 1e6);
			    int lonE6 = (int) (aWPList.get(i).getdLong() * 1e6);
				routePoints[i] = new GeoPoint(latE6,lonE6);										
			}
	    	Log.d(this.getClass().getCanonicalName(),"Track Points size: "+routePoints.length);
	    	this.routePoints=routePoints;
	    	startPoint=this.routePoints[0];
	    	centerPoint = this.routePoints[routePoints.length/2];
	    	finishPoint=this.routePoints[routePoints.length-1];
    	}catch (NegativeArraySizeException e) {
			Log.e(this.getClass().getCanonicalName(), "Error array size is negative");
			routePoints = new GeoPoint[1];
			return routePoints;
		}
    	return routePoints;
	}

	// Method to turn route display on and off
    public void setRouteView(boolean routeIsActive){
            this.routeIsActive = routeIsActive;
    }

    @Override
    public void draw(Canvas canvas, MapView mapview, boolean shadow) {
        super.draw(canvas, mapview, shadow);
        if(! routeIsActive) return;
        
        try{
        	mapview.getProjection().toPixels(locpoint, pp);       // Converts GeoPoint to screen pixels
            
            int xoff = 0;
            int yoff = 0;
            int oldx = pp.x;
            int oldy = pp.y;
            int newx = oldx + xoff;
            int newy = oldy + yoff;
            
            paint.setAntiAlias(true);

            // Draw route segment by segment, setting color and width of segment according to the slope
            // information returned from the server for the route.
            
            for(int i=0; i<numberRoutePoints-2; i++){      	
            	  paint.setARGB(100,52,134,217);
                  paint.setStrokeWidth(6);
            
                // Find endpoints of this segment in pixels
                mapview.getProjection().toPixels(routePoints[i], pold);
                oldx = pold.x;
                oldy = pold.y;
                mapview.getProjection().toPixels(routePoints[i+1], pnew);
                newx = pnew.x;
                newy = pnew.y;
               
                if(i==0){
                	//Start
                	Paint paint = new Paint(); 
                	paint.setARGB(250, 255, 0, 0); 
                	paint.setAntiAlias(true); 
                	paint.setFakeBoldText(true);
                	Bitmap bmp = BitmapFactory.decodeResource(oContext.getResources(), R.drawable.workout_start);             
                	// Create the circle
                	int rad = 10;
                	canvas.drawBitmap(bmp, oldx, oldy-bmp.getHeight(), null);  
                    canvas.drawText(" ", oldx+rad, oldy, paint);	
                }else if(i==numberRoutePoints-3){
                	//End
                	Paint paint = new Paint(); 
                	paint.setARGB(250, 255, 0, 0); 
                	paint.setAntiAlias(true); 
                	paint.setFakeBoldText(true);
                	// Create the circle
                	int rad = 10;
                	Bitmap bmp = BitmapFactory.decodeResource(oContext.getResources(), R.drawable.workout_finish);                       	
                	canvas.drawBitmap(bmp, oldx, oldy-bmp.getHeight(), null);              	
                    canvas.drawText(sTotalTime+"   "+sDistance, oldx+rad, oldy-(bmp.getHeight()+rad), paint);	
                }else if(i==(numberRoutePoints/2)){
                	//Meta'
                	//End
                	Paint paint = new Paint(); 
                	paint.setARGB(250, 255, 0, 0); 
                	paint.setAntiAlias(true); 
                	paint.setFakeBoldText(true);
                	// Create the circle
                	int rad = 10;
                	Bitmap bmp = BitmapFactory.decodeResource(oContext.getResources(), R.drawable.workout);                       	
                	canvas.drawBitmap(bmp, oldx, oldy-bmp.getHeight(), null);              	
                	ConfigTrainer oConfigTrainer=ExerciseUtils.loadConfiguration(oContext);            	       
                    canvas.drawText(ExerciseUtils.getTotalDistanceFormattated((float)aWatchPoint.get(i).getdDistance()/1000, oConfigTrainer, true) , oldx+rad, oldy-(bmp.getHeight()+rad), paint);	
                }
                
                    	
                
                // Draw the segment
                canvas.drawLine(oldx, oldy, newx, newy, paint);
            }    
        }catch (NullPointerException e){
        	Log.e(this.getClass().getCanonicalName(),"Error Diplay tracks!");  
        	return;
        }      
    }
    public GeoPoint getStart(){
    	return startPoint;
    }
    public GeoPoint getCenter(){
    	return centerPoint;
    }
    public GeoPoint getFinish(){
    	return finishPoint;
    }
}
*/