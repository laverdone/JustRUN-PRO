package com.glm.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.glm.bean.ChallengeExercise;
import com.glm.bean.ConfigTrainer;
import com.glm.bean.WatchPoint;


import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.List;

import io.ticofab.androidgpxparser.parser.GPXParser;
import io.ticofab.androidgpxparser.parser.domain.Gpx;
import io.ticofab.androidgpxparser.parser.domain.TrackPoint;
import io.ticofab.androidgpxparser.parser.domain.WayPoint;

public class GPXUtils {
	GPXParser mParser = new GPXParser(); // consider injection
	Gpx mParsedGpx = null;
	public GPXUtils(Context context, Uri gpxfile){
		JodaTimeAndroid.init(context);
		try {
			InputStream in = context.getContentResolver().openInputStream(gpxfile);
			mParsedGpx = mParser.parse(in);
		} catch (IOException | XmlPullParserException e) {
			// do something with this exception
			Log.e(this.getClass().getCanonicalName(),"Error on read GPX");
			e.printStackTrace();
		}
	}

	/**
	 * Ritorna l'esercizio popolato con il GPX in modo tale
	 * da utilizzarlo per la sfida virtuale.
	 * Limitata alla prima traccia primo segmento
	 *
	 * @return ChallengeExercise null if no parsable GPX file found
	 * @param oConfigTrainer*/
	public ChallengeExercise getChallenge(ConfigTrainer oConfigTrainer){
		ChallengeExercise mChallengeExercise = new ChallengeExercise();

		List<TrackPoint> lWayPoint=null;
		int iHours=0;
		int iMinute=0;
		int iSeconds=0;
		Double dPrevLat=0d;
		Double dPrevLon=0d;
		Double dDistance=0d;

		DateTime mPrevDateTime=null;
		if (mParsedGpx == null) {
			// error parsing track
			Log.e(this.getClass().getCanonicalName(),"Error on parsing GPX");
			return null;
		} else {
			if(mParsedGpx.getTracks().size()==1
					&& mParsedGpx.getTracks().get(0).getTrackSegments().size()==1) {
				lWayPoint = mParsedGpx.getTracks().get(0).getTrackSegments().get(0).getTrackPoints();
				int iMaxSize = mParsedGpx.getTracks().get(0).getTrackSegments().get(0).getTrackPoints().size();

				/*
				Log.d(this.getClass().getCanonicalName(), "WayPoint Size: " + iMaxSize);
				Log.d(this.getClass().getCanonicalName(), "Track Size: " + mParsedGpx.getTracks().size());
				Log.d(this.getClass().getCanonicalName(), "TrackSegment Size: " + mParsedGpx.getTracks().get(0).getTrackSegments().size());
				Log.d(this.getClass().getCanonicalName(), "TrackPoints Size: " + mParsedGpx.getTracks().get(0).getTrackSegments().get(0).getTrackPoints().size());
				*/
				for (int i = 0; i < iMaxSize; i++) {
					if(mPrevDateTime==null){
						//Prima iterazione
						mPrevDateTime=lWayPoint.get(i).getTime();
						dPrevLat=lWayPoint.get(i).getLatitude();
						dPrevLon=lWayPoint.get(i).getLongitude();

					}
					int iCurrentHours=lWayPoint.get(i).getTime().minusHours(mPrevDateTime.getHourOfDay()).getHourOfDay();
					int iCurrentMinute=lWayPoint.get(i).getTime().minusMinutes(mPrevDateTime.getMinuteOfHour()).getMinuteOfHour();
					int iCurrentSeconds=lWayPoint.get(i).getTime().minusSeconds(mPrevDateTime.getSecondOfMinute()).getSecondOfMinute();

					dDistance+=ExerciseUtils.getPartialDistanceUnFormattated(dPrevLon,dPrevLat,lWayPoint.get(i).getLongitude(),lWayPoint.get(i).getLatitude());


					WatchPoint mCurrentWatchPoint=new WatchPoint();
					mCurrentWatchPoint.setAccurancy(0f);
					mCurrentWatchPoint.setdAlt(lWayPoint.get(i).getElevation());
					mCurrentWatchPoint.setLatLong(lWayPoint.get(i).getLongitude(),lWayPoint.get(i).getLatitude());
					mCurrentWatchPoint.setdLat(lWayPoint.get(i).getLatitude());
					mCurrentWatchPoint.setdLong(lWayPoint.get(i).getLongitude());
					mCurrentWatchPoint.setdDateTime(lWayPoint.get(i).getTime().toDateTime().toDate());
					mCurrentWatchPoint.setdDistance(dDistance);
					mCurrentWatchPoint.setiHoursToPrevPoint(iCurrentHours);
					mCurrentWatchPoint.setiMinuteToPrevPoint(iCurrentMinute);
					mCurrentWatchPoint.setiSecondsToPrevPoint(iCurrentSeconds);
					mCurrentWatchPoint.setiHoursTotal(iHours);
					mCurrentWatchPoint.setiMinuteTotal(iMinute);
					mCurrentWatchPoint.setiSecondsTotal(iSeconds);

					mChallengeExercise.addWatchPoint(mCurrentWatchPoint);
/*
					Log.d(this.getClass().getCanonicalName(), "Hours DateTime: " + iCurrentHours);
					Log.d(this.getClass().getCanonicalName(), "Minute DateTime: " + iCurrentMinute);
					Log.d(this.getClass().getCanonicalName(), "Seconds DateTime: " + iCurrentSeconds);

					Log.d(this.getClass().getCanonicalName(), "Delta Point: " + iCurrentHours+":"+iCurrentMinute+":"+iCurrentSeconds);
					Log.d(this.getClass().getCanonicalName(), "Delta Track: " + iHours+":"+iMinute+":"+iSeconds);
					Log.d(this.getClass().getCanonicalName(), "Distance Unformatted: " +dDistance);
					Log.d(this.getClass().getCanonicalName(), "Distance: " +ExerciseUtils.getTotalDistanceFormattated(dDistance,oConfigTrainer,true));

					Log.d(this.getClass().getCanonicalName(), "Desc: " + lWayPoint.get(i).getDesc());
					Log.d(this.getClass().getCanonicalName(), "Name: " + lWayPoint.get(i).getName());
					Log.d(this.getClass().getCanonicalName(), "Type: " + lWayPoint.get(i).getType());
					Log.d(this.getClass().getCanonicalName(), "Time: " + lWayPoint.get(i).getTime());
					Log.d(this.getClass().getCanonicalName(), "Lat: " + lWayPoint.get(i).getLatitude());
					Log.d(this.getClass().getCanonicalName(), "Lon: " + lWayPoint.get(i).getLongitude());
					Log.d(this.getClass().getCanonicalName(), "Alt: " + lWayPoint.get(i).getElevation());
*/
					mPrevDateTime=lWayPoint.get(i).getTime();
					dPrevLat=lWayPoint.get(i).getLatitude();
					dPrevLon=lWayPoint.get(i).getLongitude();

					if(iCurrentHours==0 && iCurrentMinute==0){
						//Sommo soli i secondi di norma
						iSeconds+=iCurrentSeconds;
						if(iSeconds>59){
							iSeconds=iSeconds-59;
							iMinute++;
							if(iMinute>59){
								iMinute=0;
								iHours++;
							}
						}
					}
				}
			}
		}

		return mChallengeExercise;
	}

}
