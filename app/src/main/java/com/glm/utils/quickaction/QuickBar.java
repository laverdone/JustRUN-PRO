package com.glm.utils.quickaction;

import android.content.Context;

import com.glm.bean.ConfigTrainer;
import com.glm.trainer.R;

public class QuickBar{
	private ActionItem mShareItem;		
	private ActionItem mFacebookItem;
	private ActionItem mKMLItem;
	private ActionItem mGPXItem;
	private ActionItem mTCXItem;
	private Context mContext;
	final QuickAction quickAction;
	public QuickBar(Context context,ConfigTrainer oConfigTrainer) {				
		mContext=context;
				
		mShareItem     = new ActionItem(1, mContext.getString(R.string.share_long), mContext.getResources().getDrawable(R.drawable.share));
		//mGraphItem     = new ActionItem(2, mContext.getString(R.string.exercise_graph), mContext.getResources().getDrawable(R.drawable.graph));
	    //mMapItem       = new ActionItem(3, mContext.getString(R.string.maps), mContext.getResources().getDrawable(R.drawable.map));
	    //mEditItem      = new ActionItem(4, mContext.getString(R.string.edit_note), mContext.getResources().getDrawable(R.drawable.edit));
        mFacebookItem  = new ActionItem(2, mContext.getString(R.string.fb), mContext.getResources().getDrawable(R.drawable.share));
        mKMLItem       = new ActionItem(3, "KML", mContext.getResources().getDrawable(R.drawable.export));
        mGPXItem       = new ActionItem(4, "GPX", mContext.getResources().getDrawable(R.drawable.export));
        mTCXItem       = new ActionItem(5, "TCX", mContext.getResources().getDrawable(R.drawable.export));
        
        quickAction = new QuickAction(mContext, QuickAction.VERTICAL);
        
        //add action items into QuickAction
        quickAction.addActionItem(mShareItem);   
        //quickAction.addActionItem(mGraphItem);
        //quickAction.addActionItem(mMapItem);                     
        quickAction.addActionItem(mKMLItem);
        quickAction.addActionItem(mGPXItem);
        quickAction.addActionItem(mTCXItem);
        //quickAction.addActionItem(mEditItem);
        //if(oConfigTrainer.isShareFB()){
        //	quickAction.addActionItem(mFacebookItem);
        //}
	}

	public QuickAction getQuickAction(){
		return quickAction; 
	}
	
	
}
