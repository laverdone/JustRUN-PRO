package com.glm.app.fragment.adapter;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glm.app.fragment.StoreFragment;
import com.glm.bean.ItemStore;
import com.glm.trainer.R;
import com.glm.utils.vending.PlayBillingServiceConnection;

import java.util.ArrayList;

public class StoreAdapter extends BaseAdapter{
	private ArrayList<ItemStore> mVirtualRace;
    private Context mContext;
    
    private String mPayloadContents = null;
    private PlayBillingServiceConnection mServiceConn=null;
    private StoreFragment mFragment;
    public StoreAdapter(Context context, ArrayList<ItemStore> virtualRace, PlayBillingServiceConnection serviceConn, StoreFragment fragment){
    	mContext=context;
    	mVirtualRace=virtualRace;
    	mServiceConn=serviceConn;
    	mFragment=fragment;
    }
    
    
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mVirtualRace.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mVirtualRace.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return mVirtualRace.get(position).getiVirtualRaceID();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ItemStore oVirtualRace = (ItemStore) getItem(position);
		if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =  infalInflater.inflate(R.layout.new_virtual_race, null);
        }
		RelativeLayout obtnBuy = (RelativeLayout) convertView.findViewById(R.id.btn_buy_item);
		obtnBuy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.v(this.getClass().getCanonicalName()," buy item sku:"+oVirtualRace.getsMsKu());
				//mBillingService.requestPurchase(oVirtualRace.getsMsKu(), mPayloadContents);
				try {
					Bundle buyIntentBundle = mServiceConn.mService.getBuyIntent(3, mContext.getPackageName(),
							oVirtualRace.getsMsKu(), "inapp", "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
					PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
					mFragment.getActivity().startIntentSenderForResult(pendingIntent.getIntentSender(),
								   1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
								   Integer.valueOf(0));
				} catch (RemoteException e) {
					Log.e(this.getClass().getCanonicalName(),"Buy ITEM FAIL");
					e.printStackTrace();
				} catch (SendIntentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		TextView txtName = (TextView) convertView.findViewById(R.id.txtName);
		TextView txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
		TextView txtDesc = (TextView) convertView.findViewById(R.id.txtDesc);
		TextView txtDesc1 = (TextView) convertView.findViewById(R.id.txtDesc1);
		
		txtName.setText(oVirtualRace.getsName());
		txtPrice.setText(String.valueOf(oVirtualRace.getfPrice()));
		txtDesc.setText(oVirtualRace.getsDesc());
		txtDesc1.setText(oVirtualRace.getsDesc1());
		return convertView;
	}
}
