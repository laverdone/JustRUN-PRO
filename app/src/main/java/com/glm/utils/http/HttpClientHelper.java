package com.glm.utils.http;

import android.util.Log;

import com.glm.bean.ConfigTrainer;
import com.glm.bean.ItemStore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/*import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;*/

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

public class HttpClientHelper {
/*	private HttpClient httpclient = null;
	private HttpPost httppost = null;
	private HttpGet httpget = null;
	*//**URL Per registrare il Devices*//*
	private final String sURI_Register="http://androidtrainer.no-ip.org:8080/GCMTrainerWeb/Register";
	*//**URL Per registrare le informazioni durante la Virtual Race*//*
	private final String sURI_VirtualRace="http://androidtrainer.no-ip.org:8080/GCMTrainerWeb/VirtualRace";
	*//**URL Per registrare le Virtual Race disponibili*//*
	private final String sURI_VirtualRaceStore="http://androidtrainer.no-ip.org:8080/GCMTrainerWeb/VirtualRaceStore";
	*//**salva tutte le richieste post andate in errore*//*
	private ArrayList<HttpPost> aRequestPending = new ArrayList<HttpPost>();
	*//**costruttore*//*
	public HttpClientHelper(){
		httpclient = new DefaultHttpClient();
		
	}
	*//**
	 * Registra l'utenza sul server Trainer pre le gare virtuali
	 * 
	 * @param String sGCMId identificativo GCM
	 * @param ConfigTrainer oConfigTrainer oggetto con i dettagli dell'utente.
	 * 
	 * *//*
	public void registerToAndroidTrainerServer(String sGCMId, ConfigTrainer oConfigTrainer) {	   
	    try {
	    	httppost = new HttpPost(sURI_Register);
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("gcmid", sGCMId));
	        //Add Others value
	        nameValuePairs.add(new BasicNameValuePair("age", String.valueOf(oConfigTrainer.getiAge())));
	        nameValuePairs.add(new BasicNameValuePair("weight", String.valueOf(oConfigTrainer.getiWeight())));
	        nameValuePairs.add(new BasicNameValuePair("gender", oConfigTrainer.getsGender()));
	        nameValuePairs.add(new BasicNameValuePair("name",  oConfigTrainer.getsName()));
	        nameValuePairs.add(new BasicNameValuePair("nick", oConfigTrainer.getsNick()));
	        
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        Log.v(this.getClass().getCanonicalName(),"Register to Android Trainer Server");
	        Log.v(this.getClass().getCanonicalName(),"response: "+response.getStatusLine().getStatusCode());
	    } catch (ClientProtocolException e) {
	    	Log.e(this.getClass().getCanonicalName(),"Error ClientProtocolException Register to Android Trainer Server");
	    } catch (IOException e) {
	        Log.e(this.getClass().getCanonicalName(),"Error IOException Register to Android Trainer Server");
	    }
	} 
	
	
	*//**
	 * Spedisce i dettagli durante la Virtual Race server Trainer pre le gare virtuali
	 * 
	 * @param String sGCMId identificativo GCM
	 * @param ConfigTrainer oConfigTrainer oggetto con i dettagli dell'utente.
	 * 
	 * *//*
	public void sendDataForVirtualRace(ConfigTrainer oConfigTrainer, int iVirtualRace, 
			double Latitude, double Longitude, long Alt, float Speed, double Distance,long Time, String sLocale) {	   
	    try {
	    	httppost = new HttpPost(sURI_VirtualRace);
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("gcmid", oConfigTrainer.getsGCMId()));
	        //Add Others value
	        nameValuePairs.add(new BasicNameValuePair("virtualrace", String.valueOf(iVirtualRace)));
	        nameValuePairs.add(new BasicNameValuePair("latidute", String.valueOf(Latitude)));
	        nameValuePairs.add(new BasicNameValuePair("logitude", String.valueOf(Longitude)));
	        nameValuePairs.add(new BasicNameValuePair("alt",  String.valueOf(Alt)));
	        nameValuePairs.add(new BasicNameValuePair("speed", String.valueOf(Speed)));
	        nameValuePairs.add(new BasicNameValuePair("distance", String.valueOf(Distance)));
	        nameValuePairs.add(new BasicNameValuePair("time", String.valueOf(Time)));
	        nameValuePairs.add(new BasicNameValuePair("locale", sLocale));
	        
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        Log.v(this.getClass().getCanonicalName(),"Send Virtual Race Watch Point to Android Trainer Server");
	        Log.v(this.getClass().getCanonicalName(),"Response from Server: "+response.getStatusLine().getStatusCode());
	    } catch (ClientProtocolException e) {
	    	Log.e(this.getClass().getCanonicalName(),"Error ClientProtocolException Send Virtual Race to Android Trainer Server");
	    } catch (IOException e) {
	        Log.e(this.getClass().getCanonicalName(),"Error IOException Send Virtual Race to Android Trainer Server");
	    }
	} 
	*//**
	 * Spedisce i dettagli durante la Virtual Race server Trainer pre le gare virtuali
	 * 
	 * @param String sGCMId identificativo GCM
	 * @param ConfigTrainer oConfigTrainer oggetto con i dettagli dell'utente.
	 * 
	 * *//*
	public Collection<ItemStore> getVirtualRace(ConfigTrainer oConfigTrainer,String sLocale) {	   
	    Collection<ItemStore> aVirtualRace = new ArrayList<ItemStore>();
		try {
	    	httppost = new HttpPost(sURI_VirtualRaceStore);
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("gcmid", oConfigTrainer.getsGCMId()));
	        //Add Others value
	        nameValuePairs.add(new BasicNameValuePair("nick", oConfigTrainer.getsNick()));        
	        nameValuePairs.add(new BasicNameValuePair("locale", sLocale));
	        
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        ResponseHandler<String> responseHandler=new BasicResponseHandler();
			String responseBody=httpclient.execute(httppost, responseHandler);      
	        //Devo ritornare l'arrai list di oggetti VirtualRace
	        //TODO
	        Gson oGson=new Gson();
	        Type collectionType = new TypeToken<Collection<ItemStore>>(){}.getType();
	        aVirtualRace = oGson.fromJson(responseBody, collectionType);
	       
	        *//*for(int i=0;i<aVirtualRaceStore.size();i++){
	        	aVirtualRace.add(oGson.fromJson(aVirtualRaceStore.get(i).toString(), VirtualRace.class));
	        }*//*
	        Log.v(this.getClass().getCanonicalName(),"Send Virtual Race Watch Point to Android Trainer Server");
	        Log.v(this.getClass().getCanonicalName(),"Response from Server: "+responseBody);
	    } catch (ClientProtocolException e) {
	    	Log.e(this.getClass().getCanonicalName(),"Error ClientProtocolException Send Virtual Race to Android Trainer Server");
	    	return null;
	    } catch (IOException e) {
	        Log.e(this.getClass().getCanonicalName(),"Error IOException Send Virtual Race to Android Trainer Server");
	        return null;
	    } catch (IllegalStateException e) {
	    	Log.e(this.getClass().getCanonicalName(),"Error IllegalStateException Send Virtual Race to Android Trainer Server");
	    	return null;
		}
		
		return aVirtualRace;
	}
	
	
	*//**
	 * invia le richieste andate in errore
	 * 
	 * *//*
	public void sendHttpPostPending(){
		int iIndex=aRequestPending.size();
		
		for(int i=0;i<iIndex;i++){	
			try {
				HttpPost oPost = aRequestPending.get(i);
				HttpResponse response = httpclient.execute(oPost);
				Log.v(this.getClass().getCanonicalName(),"Response from Server: "+response.getStatusLine().getStatusCode());
			} catch (ClientProtocolException e) {
		    	Log.e(this.getClass().getCanonicalName(),"Error ClientProtocolException Send Virtual Race to Android Trainer Server");
		    	aRequestPending.add(httppost);
		    } catch (IOException e) {
		        Log.e(this.getClass().getCanonicalName(),"Error IOException Send Virtual Race to Android Trainer Server");
		        aRequestPending.add(httppost);
		    }
		}
	}*/
}
