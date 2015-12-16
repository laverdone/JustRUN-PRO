package com.glm.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.glm.chart.NewLineChart;


public class GraphAdapter extends BaseAdapter{
	private Context mContext;
	public GraphAdapter(Context context){
		mContext=context;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		NewLineChart oChart = new NewLineChart(mContext);
		oChart.init(mContext,position);
		return oChart;
	}

}
