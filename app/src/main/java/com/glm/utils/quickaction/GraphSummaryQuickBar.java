package com.glm.utils.quickaction;

import android.content.Context;

import com.glm.trainer.R;

public class GraphSummaryQuickBar{
	private ActionItem mGraphMonthItem;		
	private ActionItem mGraphWeekItem;
	
	private Context mContext;
	final QuickAction quickAction;
	public GraphSummaryQuickBar(Context context) {				
		mContext=context;
				
		mGraphMonthItem     = new ActionItem(1, mContext.getString(R.string.month), null);
		mGraphWeekItem	   = new ActionItem(2, mContext.getString(R.string.week), null);
        
        quickAction = new QuickAction(mContext, QuickAction.VERTICAL);
        
        quickAction.addActionItem(mGraphMonthItem);   
        quickAction.addActionItem(mGraphWeekItem);
	}

	public QuickAction getQuickAction(){
		return quickAction; 
	}
	
	
}
