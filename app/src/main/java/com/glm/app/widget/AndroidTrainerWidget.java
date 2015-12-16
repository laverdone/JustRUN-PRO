package com.glm.app.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.glm.bean.Summary;
import com.glm.trainer.MainActivity;
import com.glm.trainer.R;
import com.glm.utils.ExerciseUtils;

public class AndroidTrainerWidget extends AppWidgetProvider {
    
    public void onUpdate(Context oContext, AppWidgetManager appWidgetManager, int[] appWidgetIds) {    	
    	oContext.startService(new Intent(oContext, WidgetService.class));
    	
    	Summary oSummary = ExerciseUtils.getTotalSummary(oContext);
    	RemoteViews updateViews = new RemoteViews(oContext.getPackageName(), R.layout.trainer_widget);;
        if(oSummary!=null){
        	updateViews.setTextViewText(R.id.txtKalories, oSummary.getsKalories());
        	updateViews.setTextViewText(R.id.txtDistance, oSummary.getsDistance());   
        	updateViews.setTextViewText(R.id.txtSteps, oSummary.getsSteps());  
        }else{
        	updateViews.setTextViewText(R.id.txtKalories, "-");
        	updateViews.setTextViewText(R.id.txtDistance, "-");       	
        	updateViews.setTextViewText(R.id.txtSteps, "-");  
        }
    	
        
    	
    	PendingIntent pendingIntent = PendingIntent.getActivity(oContext, 0, new Intent(oContext,MainActivity.class), 0);
    	
    	updateViews.setOnClickPendingIntent(R.id.imgTrainer, pendingIntent);
		
		ComponentName thisWidget = new ComponentName(oContext,
				AndroidTrainerWidget.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		for (int widgetId : allWidgetIds) {
			appWidgetManager.updateAppWidget(widgetId, updateViews);	
		}
    }
    
    
    public static class WidgetService extends Service {
        @Override
        public void onStart(Intent intent, int startId) {
            // Build the widget update for today
            RemoteViews updateViews = buildUpdate(this);

            // Push update for this widget to the home screen
            ComponentName thisWidget = new ComponentName(this, AndroidTrainerWidget.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            manager.updateAppWidget(thisWidget, updateViews);
        }
       
        /**
         * Build a widget update to show the current Wiktionary
         * "Word of the day." Will block until the online API returns.
         */
        public RemoteViews buildUpdate(Context oContext) {
          
        	Summary oSummary = ExerciseUtils.getTotalSummary(oContext);
        	RemoteViews updateViews = new RemoteViews(oContext.getPackageName(), R.layout.trainer_widget);;
        	if(oSummary!=null){
            	updateViews.setTextViewText(R.id.txtKalories, oSummary.getsKalories());
            	updateViews.setTextViewText(R.id.txtDistance, oSummary.getsDistance());   
            	updateViews.setTextViewText(R.id.txtSteps, oSummary.getsSteps());   
            }else{
            	updateViews.setTextViewText(R.id.txtKalories, "-");
            	updateViews.setTextViewText(R.id.txtDistance, "-");       	
            	updateViews.setTextViewText(R.id.txtSteps, "-");  
            }
        	             	
        	PendingIntent pendingIntent = PendingIntent.getActivity(oContext, 0, new Intent(oContext,MainActivity.class), 0);
        	
        	updateViews.setOnClickPendingIntent(R.id.imgTrainer, pendingIntent);
    		   		
            return updateViews;
        }

        @Override
        public IBinder onBind(Intent intent) {
            // We don't need to bind to this service
            return null;
        }
        @Override
        public void onCreate() {
        	// TODO Auto-generated method stub
        	super.onCreate();
        }
    }
    
}