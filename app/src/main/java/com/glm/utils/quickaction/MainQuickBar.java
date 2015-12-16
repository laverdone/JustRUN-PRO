package com.glm.utils.quickaction;

import android.content.Context;

import com.glm.trainer.R;

public class MainQuickBar{
	private ActionItem mManualRunItem;		
	private ActionItem mManualWalkItem;
	private ActionItem mManualBikeItem;
	
	private Context mContext;
	final QuickAction quickAction;
	public MainQuickBar(Context context) {				
		mContext=context;
				
		mManualRunItem     = new ActionItem(1, mContext.getString(R.string.manual_workout), mContext.getResources().getDrawable(R.drawable.running));
	    mManualWalkItem	   = new ActionItem(2, mContext.getString(R.string.manual_workout), mContext.getResources().getDrawable(R.drawable.walking));
        mManualBikeItem       = new ActionItem(3, mContext.getString(R.string.manual_workout), mContext.getResources().getDrawable(R.drawable.biking));
        
        quickAction = new QuickAction(mContext, QuickAction.VERTICAL);
        
        quickAction.addActionItem(mManualRunItem);   
        quickAction.addActionItem(mManualWalkItem);
        quickAction.addActionItem(mManualBikeItem);
	}

	public QuickAction getQuickAction(){
		return quickAction; 
	}
	
	
}
