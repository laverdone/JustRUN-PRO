package com.glm.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gianluca on 31/12/15.
 */
public class Logger {

    private static String  mDirectory="com.glm.trainer";

    private static String mLogFile="justRun.log";
    /**
     * Write to txt log file for debug
     * */
    public static void log(String message){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm.ss.SSS");
        SimpleDateFormat sdfLog = new SimpleDateFormat("yyyyMMdd");
        mLogFile="justRun-"+sdfLog.format(new Date())+".log";

        File root = Environment.getExternalStorageDirectory();

        File dir = new File(root.getAbsolutePath() + "/" + mDirectory);

        //Create dir to log
        if(!dir.exists()){
            dir.mkdir();
            dir.mkdirs();
        }
        File logFile = new File(dir, mLogFile);

        try {

            FileOutputStream f = new FileOutputStream(logFile,true);


            PrintWriter pw = new PrintWriter(f);

            pw.println(sdf.format(new Date())+" --> "+message);

            pw.flush();

            pw.close();

            f.close();

            // Log.v(TAG, "file written to sd card");


        } catch (FileNotFoundException e) {

            e.printStackTrace();

            // Log.i(TAG, "******* File not found. Did you" +

            // " add a WRITE_EXTERNAL_STORAGE permission to the manifest?");


        } catch (IOException e) {

            e.printStackTrace();



        }
    }
}
