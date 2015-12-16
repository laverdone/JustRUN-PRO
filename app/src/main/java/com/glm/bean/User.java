package com.glm.bean;

import android.util.Log;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Dati dell'utente che vengono letti dal DB
 * nel caso non è definito un'utente verrà visualizzata una popup dall'Activity
 * con la creazione dell'utente ed eventualmente connessione ad uno o più social
 * 
 * **/
public class User {
	public int iID;

	public String sName;
	
	public String sNick;
	
	public boolean bEnabled;
	
	public boolean bDeleted;
		
	public String sGender;
	public int iAge; 	
	public Float iWeight; 	  
	public int iHeight;
	public int iFB; 	 
	public int iBuzz; 	 
	public int iTwitter;  
	
	public String sBMI=" - ";
	public ArrayList<SocialAccount> aSocialAccount = new ArrayList<SocialAccount>();

	/**
	 * iUnit 0 KM/Kg 1 Mi/Lbs/
	 * 
	 * 
	 * */
	public synchronized String getsBMI(int iUnit) {
		try{
			if(iUnit==0){
				//Unità metriche: BMI = Peso (kg) / (Altezza (m) x Altezza (m))
				float fBMI=0;	
				float fHeight=((float) this.iHeight)/100;
				fBMI=this.iWeight/(fHeight*fHeight);
				NumberFormat oNFormat = NumberFormat.getNumberInstance();
				oNFormat.setMaximumFractionDigits(2);
				sBMI = oNFormat.format(fBMI);
				//Log.i(User.class.getCanonicalName(),"User BMI (Peso/(Altezza m)*(Altezza m)) "+User.iWeight+"/("+fHeight+"*"+fHeight+")"+": "+sBMI);
			}else{
				//Unità Inglesi: BMI = Peso (lb) / (Altezza (in) x Altezza (in)) x 703
				float fBMI=0;	
				float fHeight=((float) this.iHeight);
				fBMI=(float) (((this.iWeight)/(fHeight*fHeight))*703);
				NumberFormat oNFormat = NumberFormat.getNumberInstance();
				oNFormat.setMaximumFractionDigits(2);
				sBMI = oNFormat.format(fBMI);
				//Log.i(User.class.getCanonicalName(),"User BMI (Peso/(Altezza m)*(Altezza m)) "+(User.iWeight)+"/("+(fHeight)+"*"+(fHeight)+")"+": "+sBMI);
			}
			
			
		}catch (Exception e) {
			Log.e(User.class.getCanonicalName(),"Error Calculate BMI");
			return sBMI;
		}
		
		return sBMI;
	}
}
