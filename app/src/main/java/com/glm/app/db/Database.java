package com.glm.app.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.glm.app.ConstApp;
import com.glm.utils.CryptoUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * DATABASE STRUCTURE
 * 
-- -----------------------------------------------------
-- Table trainer_users
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS trainer_users (
  id_users INT NOT NULL AUTOINCREMENT ,
  name VARCHAR(45) NOT NULL ,
  nick VARCHAR(45) NULL ,
  enabled TINYINT(1)  NULL DEFAULT 1 ,
  deleted TINYINT(1)  NULL DEFAULT 0 ,
  creation_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (id_users) );


-- -----------------------------------------------------
-- Table trainer_type_exercise
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS trainer_type_exercise (
  id_type_exercise INT NOT NULL AUTOINCREMENT ,
  desc VARCHAR(45) NOT NULL ,
  long_desc VARCHAR(255) NOT NULL ,
  PRIMARY KEY (id_type_exercise) );


-- -----------------------------------------------------
-- Table trainer_exercise
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS trainer_exercise (
  id_exercise INT NOT NULL AUTOINCREMENT ,
  id_users INT NOT NULL ,
  id_type_exercise INT NOT NULL ,
  creation_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  start_date DATETIME NULL ,
  end_date DATETIME NULL ,
  PRIMARY KEY (id_exercise, id_users, id_type_exercise) );


-- -----------------------------------------------------
-- Table trainer_exercise_dett
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS trainer_exercise_dett (
  id_watch_point INT NOT NULL AUTOINCREMENT ,
  id_exercise INT NOT NULL ,
  id_type_exercise INT NOT NULL ,
  id_users INT NOT NULL ,
  long DOUBLE NOT NULL ,
  watch_point_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  lat DOUBLE NOT NULL ,
  PRIMARY KEY (id_watch_point, id_exercise, id_type_exercise, id_users) );


-- -----------------------------------------------------
-- Table trainer_pref
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS trainer_pref (
  id_pref INT NOT NULL ,
  type VARCHAR(1) NOT NULL ,
  name VARCHAR(45) NOT NULL ,
  value VARCHAR(255) NOT NULL ,
  PRIMARY KEY (id_pref) );


-- -----------------------------------------------------
-- Table trainer_social_account
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS trainer_social_account (
  id_social_account INT NOT NULL AUTOINCREMENT ,
  id_users INT NOT NULL ,
  name VARCHAR(45) NULL ,
  desc VARCHAR(255) NULL ,
  login VARCHAR(45) NULL ,
  password VARCHAR(45) NULL ,
  creation_date DATETIME NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (id_social_account, id_users) );

 * 
 * 
 * 
 * **/
public class Database {
    private SQLiteDatabase oDatabase;
    private String sVersionNumber;
    private String sVersionDesc;
    
    private boolean bLicence=false;
    
    private DbHelper oDbHelper;
    private Context oContext;
    private static String DB_PATH = "/data/data/%PACKAGE%/databases/";
    //private static String DB_NAME = "Maps4Strategy1";//"4cbase3";
    private static String DB_NAME = "trainer";
    private static final int DB_VERSION = 804;

    static class MetaData {
    	
    	
		static class TrainerTypeEcercise {
		    static final String tablename = "trainer_type_exercise";
		    
		    static final String key_id = "id_type_exercise";
		    static final String key_desc = "desc";
		    static final String key_long_desc = "long_desc";
		}
		/***
		 *  -- -----------------------------------------------------
			-- Table trainer_exercise
			-- -----------------------------------------------------
			CREATE  TABLE IF NOT EXISTS trainer_exercise (
			  id_exercise INT NOT NULL AUTOINCREMENT ,
			  id_users INT NOT NULL ,
			  id_type_exercise INT NOT NULL ,
			  creation_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ,
			  start_date DATETIME NULL ,
			  end_date DATETIME NULL ,
			  PRIMARY KEY (id_exercise, id_users, id_type_exercise) );
		 * 
		 * */
		static class TrainerExercise{
			 static final String tablename = "trainer_exercise";
			    
			 static final String key_id = "id_exercise";
			 static final String key_id_user = "id_users";
			 static final String key_id_type_exercise = "id_type_exercise";
			 static final String key_creation_date = "creation_date";
			 static final String key_start_date = "start_date";
			 static final String key_end_date = "end_date";
			 static final String key_note = "note";
			 static final String key_calorie = "calorie_burn";
			 static final String key_total_distance = "distance";
			 static final String key_avg_speed = "avg_speed";
			 static final String key_total_time = "total_time";
			 static final String key_steps = "steps_count";
			 static final String key_weight = "weight";
		}
		/**
		 * -- -----------------------------------------------------
		-- Table trainer_exercise_dett
		-- -----------------------------------------------------
		CREATE  TABLE IF NOT EXISTS trainer_exercise_dett (
		  id_watch_point INT NOT NULL AUTOINCREMENT ,
		  id_exercise INT NOT NULL ,
		  id_type_exercise INT NOT NULL ,
		  id_users INT NOT NULL ,
		  long DOUBLE NOT NULL ,		  
		  lat DOUBLE NOT NULL ,
		  watch_point_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ,
		  PRIMARY KEY (id_watch_point, id_exercise, id_type_exercise, id_users) );
		*/
		static class TrainerExerciseDett{
			 static final String tablename = "trainer_exercise_dett";
			    
			 static final String key_id = "id_watch_point";			 	
			 static final String key_id_exercise = "id_exercise";
			 static final String key_id_type_exercise = "id_type_exercise";
			 static final String key_id_user = "id_users";			 			 
			 static final String key_long = "long";
			 static final String key_lat = "lat";
			 static final String key_alt = "alt";
			 static final String key_distance = "distance";
			 static final String key_watch_point_name = "watch_point_name";
			 static final String key_watch_point_date = "watch_point_date";
			 static final String key_watch_point_date_prev = "watch_point_date_prev";
		     static final String key_watch_point_song = "watch_point_song";
			
		}
		
		/**
		 * -- -----------------------------------------------------
		
		-- Table trainer_social_account
		-- -----------------------------------------------------
		CREATE  TABLE IF NOT EXISTS trainer_social_account (
		  id_social_account INT NOT NULL AUTOINCREMENT ,
		  id_users INT NOT NULL ,
		  name VARCHAR(45) NULL ,
		  desc VARCHAR(255) NULL ,
		  login VARCHAR(45) NULL ,
		  password VARCHAR(45) NULL ,
		  creation_date DATETIME NULL DEFAULT CURRENT_TIMESTAMP ,
		  PRIMARY KEY (id_social_account, id_users) );
		 */
		static class TrainerSocialAccount{
			 static final String tablename = "trainer_social_account";
			    
			 static final String key_id = "id_social_account";	
			 static final String key_id_user = "id_users";				 
			 static final String key_name = "name";
			 static final String key_desc = "desc";
			 static final String key_login = "login";		
			 static final String key_password = "password";	
			 static final String key_creation_date = "creation_date";	
		}
    }
    
    
    
    public Database(Context ctx){
		oContext=ctx;
		DB_PATH = DB_PATH.replace("%PACKAGE%", ctx.getPackageName());	
		
		oDbHelper=new DbHelper(ctx, DB_NAME, null, DB_VERSION); 		
    }

    public void open() {
    	////Log.v(getClass().getCanonicalName(), "createDataBase()");
    	String myPath = DB_PATH + DB_NAME;
    	if(checkDataBase()){
    	    ////Log.v(getClass().getName(), "Database already exists. OK");
    	    try{
    	    	oDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.NO_LOCALIZED_COLLATORS);
    	    }catch(SQLiteException e){
    	    	Log.e(this.getClass().getCanonicalName(), "createDataBase() "+e.getMessage());
    	    }
    	} else {
    		
    	    try {
    	    	oDatabase = oDbHelper.getReadableDatabase();	//accede una prima volta al file (ancora inesistente)	//copyDataBase();
    	    	createDataBase();
    	    } catch (Exception e) {
    	    	Log.e(this.getClass().getCanonicalName(), "createDataBase() "+e.getMessage());
    	    }
    	}    	    	    		
		//oDatabase=oDbHelper.getWritableDatabase();
		////Log.v(this.getClass().getCanonicalName(), "open(): "+oDatabase.isOpen());
    	
    }

    public void close() {
    	if(oDatabase!=null) oDatabase.close();
    }


    public Cursor fetchAll (String table,String sSelection, String sOrderBy) {
    	try{
    		return oDatabase.query(table, null,sSelection,null,null,null,sOrderBy);
    	}catch (SQLiteException e) {
			return null;
		}
    	
    	               
    }
    public Cursor rawQuery(String sSQL, String[] sSelArgs){
    	if(oDatabase!=null)
    		return oDatabase.rawQuery(sSQL, sSelArgs);
    	else
    		return null;
    }
    /**
     * Crea il DataBase per l'applicativo
     * 
     * **/
    public boolean createDataBase(){
    	try {        	        		        		
    	
    		oDatabase.execSQL("CREATE TABLE trainer_type_exercise (" +
    				" 	id_type_exercise INTEGER         PRIMARY KEY AUTOINCREMENT NOT NULL, " + 
    				" 	DESC           VARCHAR( 45 )   NOT NULL, " + 
    				" 	long_desc        VARCHAR( 255 )  NOT NULL)");
    		oDatabase.execSQL("INSERT INTO trainer_type_exercise (id_type_exercise, DESC, long_desc) VALUES (0, 'Bicicletta', 'Bicicletta')");
    		oDatabase.execSQL("INSERT INTO trainer_type_exercise (id_type_exercise, DESC, long_desc) VALUES (1, 'sci', 'sci')");
    		oDatabase.execSQL("INSERT INTO trainer_type_exercise (id_type_exercise, DESC, long_desc) VALUES (2, 'Corsa', 'Corsa')");

    		oDatabase.execSQL("CREATE TABLE trainer_exercise (" +
    				" 	id_exercise      INTEGER          PRIMARY KEY AUTOINCREMENT NOT NULL, " + 
    				" 	id_users         INTEGER          NOT NULL, " + 
    				" 	id_type_exercise INTEGER          NOT NULL, " + 
    				" 	creation_date    DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP, " + 
    				" 	start_date       DATETIME         NULL DEFAULT CURRENT_TIMESTAMP, " + 
    				" 	end_date         DATETIME         NULL, " + 
    				" 	note             VARCHAR( 2000 )  NULL, " + 
    				" 	calorie_burn     VARCHAR( 50 )    NULL, " + 
    				" 	distance         DOUBLE           NULL, " + 
    				" 	avg_speed        DOUBLE           NULL, " + 
    				" 	total_time       VARCHAR( 50 )    NULL, " + 
    				" 	steps_count      INTEGER          NOT NULL DEFAULT 0, " + 
    				" 	weight           DOUBLE           NOT NULL DEFAULT 0, " + 
    				" 	bmi              DOUBLE           DEFAULT 0, " + 
    				" 	kalories         DOUBLE           DEFAULT 0)");

    		oDatabase.execSQL("CREATE TABLE trainer_exercise_dett (" +
    				" 	id_watch_point        INTEGER         PRIMARY KEY AUTOINCREMENT NOT NULL, " + 
    				" 	id_exercise           INTEGER         NOT NULL, " + 
    				" 	id_type_exercise      INTEGER         NOT NULL, " + 
    				" 	id_users              INTEGER         NOT NULL, " + 
    				" 	long                  DOUBLE          NOT NULL, " + 
    				" 	lat                   DOUBLE          NOT NULL, " + 
    				" 	alt                   DOUBLE          NOT NULL, " + 
    				" 	distance              DOUBLE          NOT NULL  DEFAULT 0, " + 
    				" 	watch_point_name      VARCHAR( 255 ), " + 
    				" 	watch_point_date      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP, " + 
    				" 	watch_point_date_prev DATETIME        DEFAULT CURRENT_TIMESTAMP, " + 
    				" 	watch_point_song      VARCHAR( 300 ), " + 
    				" 	speed                 DOUBLE          DEFAULT 0, " + 
    				" 	accurancy             DOUBLE          DEFAULT 0, " + 
    				" 	gpstime               DOUBLE          DEFAULT 0, " + 
    				" 	bpm                   INT             DEFAULT 0)");

    		

    		oDatabase.execSQL("CREATE TABLE trainer_social_account (" +
    				" 	id_social_account INTEGER         PRIMARY KEY AUTOINCREMENT NOT NULL, " + 
    				" 	id_users          INTEGER         NOT NULL, " + 
    				" 	name              VARCHAR( 45 )   NULL, " + 
    				" 	DESC            VARCHAR( 255 )  NULL, " + 
    				" 	login             VARCHAR( 45 )   NULL, " + 
    				" 	password          VARCHAR( 45 )   NULL, " + 
    				" 	creation_date     DATETIME        NULL DEFAULT CURRENT_TIMESTAMP)");


    		oDatabase.execSQL("CREATE TABLE STORE_ORDERS (" +
    				" 	store_id     INTEGER         PRIMARY KEY AUTOINCREMENT NOT NULL, " + 
    				" 	state        INTEGER         NOT NULL DEFAULT 0, " + 
    				" 	product_id   VARCHAR( 45 )   NOT NULL, " + 
    				" 	product_desc VARCHAR( 255 )  NOT NULL, " + 
    				" 	order_time   DATETIME        NULL DEFAULT CURRENT_TIMESTAMP)");
    		oDatabase.execSQL("INSERT INTO STORE_ORDERS (store_id, state, product_id, product_desc, order_time) VALUES (0, 0, 'polarcardio', 'polarcardio', '2012-04-19 06:28:31')");


    		oDatabase.execSQL("CREATE TABLE TRAINER_CONFIG (" +
    				" 	cfg_id    INTEGER        PRIMARY KEY AUTOINCREMENT NOT NULL, " + 
    				" 	cfg_desc  VARCHAR( 45 )  NOT NULL, " + 
    				" 	cfg_value INTEGER        NOT NULL DEFAULT 0, " + 
    				" 	cfg_time  DATETIME       NULL DEFAULT CURRENT_TIMESTAMP)");
    		oDatabase.execSQL("INSERT INTO TRAINER_CONFIG (cfg_id, cfg_desc, cfg_value, cfg_time) VALUES (1, 'first_boot', 1, '2012-04-19 06:28:30')");

    		oDatabase.execSQL("CREATE INDEX exercise_index ON trainer_exercise (" +
    				" 	id_exercise)");

    		oDatabase.execSQL("CREATE INDEX exercise_index_dett ON trainer_exercise_dett (" +
    				" 	id_exercise)");

    		oDatabase.execSQL("CREATE INDEX exercise_type ON trainer_exercise (" +
    				" 	id_type_exercise)");
    	
    		oDatabase.close();
    		/**DB Version**/
    		Log.i(this.getClass().getCanonicalName(), "create DB");
    		return true;
    	} catch (SQLException e) {
    		Log.e(this.getClass().getCanonicalName(), "ERROR create DB");
    		return false;
    	}catch (NullPointerException e1){
    		Log.e(this.getClass().getCanonicalName(), "Null ERROR create DB");
    		return false;
    	}
    }
    
    /**
     * Passato il path dove risiede il database nel device, proviamo ad aprirlo in sola lettura.
     * Se riusciamo ad effettuare l'operazione, il datbase esiste e ritorniamo true altrimenti
     * viene lanciata un'eccezione.
     * 
     * Controlla anche la release e aggiorna la struttura se necessario
     */
    private boolean checkDataBase (){
    	
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    		//Conrollo la versione del DB
    		if(getDataBaseVersionAndLicence(checkDB)!=null){
    			//Log.v(this.getClass().getCanonicalName(),"DB Version Number "+sVersionNumber);
  
    			//manualDB();
    		}

    	}catch(SQLiteException e){
 
    		//TODO il database non esiste createDataBase()
    		createDataBase();
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
	/**
	 * backup DB to External sdCard
	 *
	 * */
	public void backupDB(){
		String myPath = DB_PATH + DB_NAME;
		//Copy DB to External sdCard
		File db = new File(myPath);
		if(db.exists()){
			copyToExternalSd(myPath);
		}
	}
	/**
	 * restore backup
	 * */
	public void restoreDB(){
		String myPath = DB_PATH + DB_NAME;
		//Copy DB to External sdCard
		File db = new File(myPath);
		//if(db.exists()){
			copyToInternal(myPath);
		//}
	}
	private void copyToInternal(final String targetFile){
		new Thread(new Runnable() {
			@Override
			public void run() {
				File oRoot = Environment.getExternalStorageDirectory();
				FileInputStream fis = null;
				FileOutputStream fos = null;
				if(oRoot.canRead() && oRoot.canWrite()) {
					//try {
					File oRootPersonalTrainer = new File(oRoot.getAbsolutePath()+"/"+ConstApp.FOLDER_PERSONAL_TRAINER);
					if(!oRootPersonalTrainer.exists() && oRootPersonalTrainer.mkdir()){
						//Log.v(Exercise.class.getCanonicalName(),"Folder PersonalTrainer Create"+oRoot.getAbsolutePath()+"/"+FOLDER_PERSONAL_TRAINER);
					}
					File oInput	= new File(oRoot.getAbsolutePath()+"/"+ConstApp.FOLDER_PERSONAL_TRAINER+"/"+ConstApp.DB_BACKUP_NAME);
					File oOutput 	= new File(targetFile);
					if(oInput.exists())
						CryptoUtils.decrypt(ConstApp.CRYPTO_KEY,oInput,oOutput);
				}
			}
		}).start();
	}

	private void copyToExternalSd(final String sourceFile) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				File oRoot = Environment.getExternalStorageDirectory();
				FileInputStream fis = null;
				FileOutputStream fos = null;
				if(oRoot.canRead() && oRoot.canWrite()) {
					//try {
						File oRootPersonalTrainer = new File(oRoot.getAbsolutePath()+"/"+ConstApp.FOLDER_PERSONAL_TRAINER);
						if(!oRootPersonalTrainer.exists() && oRootPersonalTrainer.mkdir()){
							//Log.v(Exercise.class.getCanonicalName(),"Folder PersonalTrainer Create"+oRoot.getAbsolutePath()+"/"+FOLDER_PERSONAL_TRAINER);
						}
						File oInput 	= new File(sourceFile);
						File oOutput	= new File(oRoot.getAbsolutePath()+"/"+ConstApp.FOLDER_PERSONAL_TRAINER+"/"+ConstApp.DB_BACKUP_NAME);
						if(oInput.exists())
							CryptoUtils.encrypt(ConstApp.CRYPTO_KEY,oInput,oOutput);

						/*fis = new FileInputStream(sourceFile);

						fos = new FileOutputStream(oRoot.getAbsolutePath()+"/"+ConstApp.FOLDER_PERSONAL_TRAINER+"/database.db");
						byte[] buffer = new byte[1024];
						int noOfBytes = 0;
						System.out.println("Copying file using streams");
						// read bytes from source file and write to destination file
						while ((noOfBytes = fis.read(buffer)) != -1) {
							fos.write(buffer, 0, noOfBytes);
						}
					} catch (FileNotFoundException e) {
						Log.e(this.getClass().getCanonicalName(), "File not found" + e);
					} catch (IOException ioe) {
						Log.e(this.getClass().getCanonicalName(), "Exception while copying file " + ioe);
					} finally {
						// close the streams using close method
						try {
							if (fis != null) {
								fis.close();
							}
							if (fos != null) {
								fos.close();
							}
						} catch (IOException ioe) {
							Log.e(this.getClass().getCanonicalName(), "Error while closing stream: " + ioe);
						}
					}*/
				}
			}
		}).start();
	}


	/**
     * Ony for DEVELOPMENT
     * 
     * **/
    private void manualDB(){
    	SQLiteDatabase oDB=SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
    	try{   		
    		
    		oDB.execSQL("INSERT INTO STORE_ORDERS (store_id, state, product_id, product_desc,order_time) VALUES ('100','ok','polarcardio','polar','')");

    			
    		/*oDB.execSQL("insert into TRAINER_PREF ( type, name, desc,  value, ORDER_ID, VISIBLE) " + 
    		    	" values " +
    		    	" ('B','"+oContext.getString(R.string.cardio).replaceAll("'", "''")+"','"+oContext.getString(R.string.cardio_desc).replaceAll("'", "''")+"',1,18,1)");
           */	
    	}catch (SQLException e) {
    		oDB.close();   
        	oDB=null;
        	return;
		}   	
    	oDB.close();   
    	oDB=null;
    }
    
    /**
     * Ritorna la versione del DB edell'applicazione
     * 
     * */
    public String getDataBaseVersionAndLicence(SQLiteDatabase oDBase) {
		/*String sSQL ="SELECT "+TrainerVersion.key_version+", "
				+TrainerVersion.key_version_desc+", "+TrainerVersion.licence+" from "+TrainerVersion.tablename;
		Cursor oCursor;
		SQLiteDatabase oDB=null;
		if(oDBase==null){
			oDB=SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
			oCursor = oDB.rawQuery(sSQL,null);
		}else{
			oCursor = oDBase.rawQuery(sSQL,null);
		}
		
		if(oCursor!=null){      				   		
   			int iTrainerVersionNumber 	= oCursor.getColumnIndex(TrainerVersion.key_version);
   			int iTrainerVersionDesc 	= oCursor.getColumnIndex(TrainerVersion.key_version_desc);
   			int iTrainerLicence			= oCursor.getColumnIndex(TrainerVersion.licence);
   			
	   		while(oCursor.moveToNext()){ 
	   			sVersionNumber=oCursor.getString(iTrainerVersionNumber);
	   			sVersionDesc=oCursor.getString(iTrainerVersionDesc);
	   			if(oCursor.getString(iTrainerLicence).compareToIgnoreCase("S")==0){
	   				bLicence=true;	   				
	   			}else{
	   				bLicence=false;
	   			}
	   		}
	   		oCursor.close();
		}
		if(oDBase==null){
			if(oDB!=null )oDB.close();
		}else{
			oDBase.close();
		}*/
    	String sVersionNumber="5.0";
		return sVersionNumber;
	}

	public void openDataBase() throws SQLException{
    	 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
        oDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
     }
    /**
     * Restituisce l'oggetto DQLiteDataBase
     * */
    public SQLiteDatabase getOpenedDatabase () {
    	return oDatabase;
    }
 
    
    

    /**
     * Helper di upgrade
     * @author norick
     *
     */
    private class DbHelper extends SQLiteOpenHelper {

	public DbHelper(Context context, String name, CursorFactory factory,int version) {
	    super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase _db) {
	    Log.i(this.getClass().getCanonicalName(),"Database.onCreate()");
	    
	    //_db.execSQL(PRODUCTS_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
	}

    }



	public synchronized boolean isbLicence() {
		return bLicence;
	}
	
	/**
	 * METODO PRINCIPALE CHE VERIFICA SE ESISTE IL DB LO CREA E NE AGGIORNA LA 
	 * STRUTTURA.
	 * 
	 * VIENE CHIAMATO AD OGNI AVVIO DELL'APP DALL'ACTIVITY PRICIPALE.
	 * */
	public synchronized boolean init(){
		try{
			File oDBFile = new File(DB_PATH+DB_NAME);
			//Verifico se il DB Esiste
			if(oDBFile.exists()){
				Log.i(this.getClass().getCanonicalName(),"DB Exist UPGRADE PROCESS");
				return checkDataBase();
			}else{
				if(oDbHelper!=null){
					oDbHelper.close();
				}
				oDatabase = oDbHelper.getReadableDatabase();
				if(createDataBase()){
					return checkDataBase();
				}else{
					return false;
				}
			}	
		}catch(SQLException e){
			Log.e(this.getClass().getCanonicalName(),"Error init DB");
			if(oDatabase!=null){
				if(oDatabase.isOpen()) oDatabase.close();
			}
			return false;
		}catch (Exception e) {
			Log.e(this.getClass().getCanonicalName(),"Error init DB");
			if(oDatabase!=null){
				if(oDatabase.isOpen()) oDatabase.close();
			}
			return false;
		}			
	}
}
