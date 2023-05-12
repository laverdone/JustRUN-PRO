package com.glm.app;

/**
 * Created by gianluca on 11/01/16.
 */
public class ConstApp {
    public final static boolean IS_DEBUG=false;

    public final static int DAYS_UNTIL_PROMPT = 20;
    public final static int LAUNCHES_UNTIL_PROMPT = 10;
    public final static String APP_TITLE = "Just Run, walk & bike";
    public final static String APP_PNAME = "com.glm.trainer";


    public final static int INTERVAL_TWO_MINUTES = 1000 * 60 * 2;

    public final static float GPS_MIN_ACCURANCY_RUN=10;
    public final static float GPS_MIN_ACCURANCY_WALK=20;
    public final static float GPS_MIN_ACCURANCY_BIKE=70;
    public final static double GPSFIX=1;//0.88;
    public final static double MILES_TO_KM=0.621371192;
    public final static double I_BURN_RUNNING=0.9;
    public final static double I_BURN_WALKING=0.45;
    public final static double I_BURN_BIKING=0.105;
    public final static int STEP_FIX=10;
    public static final int TYPE_BIKE = 1;
    public static final int TYPE_MANUAL_BIKE = 1000;
    public static final int TYPE_RUN = 0;
    public static final int TYPE_MANUAL_RUN =1001;
    public static final int TYPE_WALK = 100;
    public static final int TYPE_MANUAL_WALK =10000;
    public static final String FOLDER_PERSONAL_TRAINER="justRun";
    public static final String CRYPTO_KEY="feacbc02a3a697b0";

    public static final double SHORT_KM_MOTIVATOR = 1;
    public static final double MEDIUM_KM_MOTIVATOR = 2;
    public static final double LONG_KM_MOTIVATOR = 3;
    public static final String DB_BACKUP_NAME = "workouts.db";
    public static final String ADS_APP_PACKAGE_NAME="com.glm.trainerlite";
    public static final int PERMISSION_LOCATION_REQUEST_CODE = 1;

    public static final int PERMISSION_WRITE_EXTERNAL_STORAGE_CODE = 2;
	public static final int READ_REQUEST_CODE = 999;
}
