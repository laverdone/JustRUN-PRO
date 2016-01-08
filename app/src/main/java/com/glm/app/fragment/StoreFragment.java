package com.glm.app.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.glm.app.fragment.adapter.StoreAdapter;
import com.glm.bean.ItemStore;
import com.glm.trainer.NewMainActivity;
import com.glm.trainer.R;
import com.glm.utils.http.HttpClientHelper;
import com.glm.utils.vending.PlayBillingServiceConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class StoreFragment extends Fragment {
	private View rootView;
	//private HttpClientHelper oHttpClient = new HttpClientHelper();
	
	private Context mContext;
	private ListView mListStore;
	private StoreAdapter mStoreAdapter;
	
	public PlayBillingServiceConnection mServiceConn;
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	public StoreFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext=getActivity().getApplicationContext();
		rootView = inflater.inflate(R.layout.new_trainer_store,
				container, false);
		mListStore = (ListView) rootView.findViewById(R.id.listStore);
        
		
		
        HttpTask oTask = new HttpTask();
        oTask.execute();
		
		return rootView;
	}

	public void setContext(Context context) {
		mContext=context;
		try{
			mServiceConn = new PlayBillingServiceConnection(mContext);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
   
	@Override
	public void onDestroy() {
		if(mServiceConn!=null) mServiceConn.destroy();
		super.onDestroy();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) { 
	   if (requestCode == 1001) {           
	      int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
	      String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
	      String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
	        
	      if (resultCode == Activity.RESULT_OK) {
	         try {
	            JSONObject jo = new JSONObject(purchaseData);
	            String sku = jo.getString("productId");
	            Log.v(this.getClass().getCanonicalName(), "You have bought the " + sku + ". Excellent choice adventurer!");
	          }
	          catch (JSONException e) {
	        	  Log.e(this.getClass().getCanonicalName(),"Failed to parse purchase data.");
	             e.printStackTrace();
	          }
	      }
	   }
	}	
	
   private class HttpTask extends AsyncTask<Void, Void, Void> {
		Collection<ItemStore> aVirtualRace=null;
		ArrayList<ItemStore> oItemStore = new ArrayList<ItemStore>();
		@Override
		protected void onPostExecute(Void mVoid) {
		
			 if(aVirtualRace!=null){       
			        Iterator<ItemStore> iterator = aVirtualRace.iterator();     
				    while (iterator.hasNext()){
					    ItemStore oVirtualRace = iterator.next();
					    oItemStore.add(oVirtualRace);
					    //populareRow(oVirtualRace);
					    //Log.v(this.getClass().getCanonicalName(),"Store Virtual Race");  
				    }  
		        }      
			 mStoreAdapter = new StoreAdapter(mContext, oItemStore,mServiceConn,StoreFragment.this);
			 mListStore.setAdapter(mStoreAdapter);
			 mStoreAdapter.notifyDataSetChanged();
			 mStoreAdapter.notifyDataSetInvalidated();
		}		

		@Override
		protected Void doInBackground(Void... params) {
			//oConfigTrainer = ExerciseUtils.loadConfiguration(mContext);
			if(!isAdded()) return null;
			/*new Thread(new Runnable() {
				
				@Override
				public void run() {
					aVirtualRace = oHttpClient.getVirtualRace(((NewMainActivity)getActivity()).oConfigTrainer, Locale.getDefault().getCountry());	
				}
			});*/
					
			
			ArrayList skuList = new ArrayList();
			skuList.add("donate2.0");
			skuList.add("polarcardio");
			Bundle querySkus = new Bundle();
			querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
			
			try {
				if(mServiceConn==null || mContext==null || mServiceConn.mService==null) return null;
				Bundle skuDetails = mServiceConn.mService.getSkuDetails(3, mContext.getPackageName(), "inapp", querySkus);
				int response = skuDetails.getInt("RESPONSE_CODE");
				if (response == 0) {
				   ArrayList<String> responseList 
				      = skuDetails.getStringArrayList("DETAILS_LIST");
				   
				   for (String thisResponse : responseList) {
				      JSONObject object;
					try {
						object = new JSONObject(thisResponse);
						String name = object.getString("title");
						String desc = object.getString("description");
						String sku = object.getString("productId");
					    String price = object.getString("price");
					    Log.v(this.getClass().getCanonicalName(), "sku: "+sku+" - price: "+price);
					    ItemStore mTmpItemStore = new ItemStore();
						mTmpItemStore.setfPrice(price);
						mTmpItemStore.setsName(name);
						mTmpItemStore.setsDesc(desc);
						mTmpItemStore.setsMsKu(sku);
						oItemStore.add(mTmpItemStore);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				     
				   }
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
			
	}
}