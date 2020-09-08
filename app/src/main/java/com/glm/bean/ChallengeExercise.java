package com.glm.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Vector;

public class ChallengeExercise extends Exercise implements Parcelable {
	Vector<WatchPoint> mTrackPointExercise = new Vector<WatchPoint>();

	public void addWatchPoint(WatchPoint addWatchPoint){
		mTrackPointExercise.add(addWatchPoint);
	}
	public Vector<WatchPoint> getTrackPointExercise(){
		return mTrackPointExercise;
	}

	public ChallengeExercise(){}
	protected ChallengeExercise(Parcel in) {
		if (in.readByte() == 0x01) {
			mTrackPointExercise = new Vector<WatchPoint>();
			in.readList(mTrackPointExercise, WatchPoint.class.getClassLoader());
		} else {
			mTrackPointExercise = null;
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		if (mTrackPointExercise == null) {
			dest.writeByte((byte) (0x00));
		} else {
			dest.writeByte((byte) (0x01));
			dest.writeList(mTrackPointExercise);
		}
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<ChallengeExercise> CREATOR = new Parcelable.Creator<ChallengeExercise>() {
		@Override
		public ChallengeExercise createFromParcel(Parcel in) {
			return new ChallengeExercise(in);
		}

		@Override
		public ChallengeExercise[] newArray(int size) {
			return new ChallengeExercise[size];
		}
	};
}
