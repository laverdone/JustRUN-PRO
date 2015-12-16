package com.glm.app.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    static String DB_PATH = "/data/data/%PACKAGE%/databases/";
//    static String DB_NAME = "4cbaseUTF8";
//    static String DB_NAME = "4c2.mp3";
    static String DB_NAME_ZIPPED = "4czipped.mp3";
    static String DB_NAME_ASSET = "4c3_201011.mp3";
    static String DB_NAME = "4c_201011.sqlite"; 
    static int DB_VERSION = 2;

    public SQLiteDatabase myDataBase; 
    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DatabaseHelper(Context context) {
	super(context, DB_NAME, null, DB_VERSION);

	DB_PATH = DB_PATH.replace("%PACKAGE%", context.getPackageName());
	this.myContext = context;
	
	//Log.v(getClass().getName(), "new DatabaseHelper: DB_NAME="+DB_NAME+" DB_PATH="+DB_PATH);
    }	

    public Cursor fetchAll (String table) {
    	return myDataBase.query(table, null,null,null,null,null,null);              
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase(boolean bForceCreate) throws IOException{
    	//Log.v(getClass().getCanonicalName(), "createDataBase()");	
	if(checkDataBase()){
	    //Log.v(getClass().getName(), "Database already exists. OK");
	    if(bForceCreate){
	    	 //Log.v(getClass().getName(), "Force Create Database.");
	    	 myDataBase = this.getReadableDatabase();	//accede una prima volta al file (ancora inesistente)
	 	    try {
	 	    	copyDataBase();
	 	    } catch (IOException e) {
	 	    	throw new Error("Error copying database");
	 	    }
	    }
	} 
	else {
		myDataBase = this.getReadableDatabase();	//accede una prima volta al file (ancora inesistente)
	    try {
		copyDataBase();
	    } catch (IOException e) {
		throw new Error("Error copying database");
	    }
	}
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
	SQLiteDatabase checkDB = null;

	try{
	    String myPath = DB_PATH + DB_NAME;
	    checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

	}catch(SQLiteException e){}

	if(checkDB != null){
	    checkDB.close();
	}

	return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

	//Log.v(getClass().getCanonicalName(), "copyDataBase() '"+DB_NAME+"' to "+DB_PATH);
				
	//Open your local db as the input stream
	InputStream myInput;
	
	//try ZIP
	//String sFilePath = ""+myContext.getPackageName()+"";
/*	
	try {
	    //Log.v(getClass().getName(), "step0");
	    ZipFile oZip = new ZipFile("/data/app/"+myContext.getPackageName()+"/"+DB_NAME_ZIPPED);
	    //Log.v(getClass().getName(), "step1");
	    String sContentFileName = oZip.entries().nextElement().getName();
	    //Log.v(getClass().getName(), "step2");
	    myInput = oZip.getInputStream(oZip.getEntry(sContentFileName));
	    //Log.v(getClass().getName(), "Located Zipped Database '"+DB_NAME_ZIPPED+" -internalFileName: "+sContentFileName);
	}
	catch (IOException e) {
	    Log.e(getClass().getName(), "error opening ZipFile stream", e);
	}
	*/
	myInput = myContext.getAssets().open(DB_NAME_ASSET);
	
	// Path to the just created empty db
	String outFileName = DB_PATH + DB_NAME;

	//Open the empty db as the output stream
	OutputStream myOutput = new FileOutputStream(outFileName);

	//transfer bytes from the inputfile to the outputfile
	byte[] buffer = new byte[1024];
	int length;
	while ((length = myInput.read(buffer))>0){
	    myOutput.write(buffer, 0, length);
	}

	//Close the streams
	myOutput.flush();
	myOutput.close();
	myInput.close();

    }

    public void openDataBase() throws SQLException{

	//Open the database
	String myPath = DB_PATH + DB_NAME;
	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

	if(myDataBase != null)
	    myDataBase.close();

	super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.

}