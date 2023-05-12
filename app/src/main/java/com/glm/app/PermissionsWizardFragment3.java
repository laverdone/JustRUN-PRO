package com.glm.app;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.glm.trainer.R;

import org.twinone.androidwizard.WizardFragment;

public class PermissionsWizardFragment3 extends WizardFragment {

	public static final String KEY_TITLE = "title";
	public static final String KEY_TEXT1 = "text";
	public static final String KEY_TEXT2 = "text2";
	public static final String KEY_ICON_RES_ID = "icon";
	private static String[] mPermission;
	private static String mTitle;

	private Button mGrant;
	private Context mContext;
	private TextView txtPermission;

	public static PermissionsWizardFragment3 newInstance(String title, String text1, String text2, int iconResId, String[] permission) {
		PermissionsWizardFragment3 f = new PermissionsWizardFragment3();
		Bundle b = new Bundle();
		b.putString(KEY_TITLE, title);
		b.putString(KEY_TEXT1, text1);
		b.putString(KEY_TEXT2, text2);
		b.putInt(KEY_ICON_RES_ID, iconResId);
		f.setArguments(b);
		mPermission = permission;
		mTitle=title;
		return f;
	}



	@Override
	protected View setup(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
		setTitle(R.string.app_name_pro);
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.grant_permission, null);
		mContext=getContext();

		txtPermission = (TextView) v.findViewById(R.id.txtPermission);
		txtPermission.setText(mTitle);
		mGrant = (Button) v.findViewById(R.id.btnGrant);
		mGrant.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					if (ActivityCompat.checkSelfPermission(mContext,
							mPermission[0])
							!= PackageManager.PERMISSION_GRANTED) {

						ActivityCompat.requestPermissions((Activity) mContext,
								mPermission,
								ConstApp.PERMISSION_WRITE_EXTERNAL_STORAGE_CODE);


					}else if (ActivityCompat.checkSelfPermission(mContext,
							mPermission[0])
							!= PackageManager.PERMISSION_GRANTED) {

						ActivityCompat.requestPermissions((Activity)mContext,
								mPermission,
								ConstApp.PERMISSION_WRITE_EXTERNAL_STORAGE_CODE);

					}else{
						//accepted();
					}
				}
			}
		});


		return v;
	}

	public void onEnterFromBack() {
		if (hasPermissions(getActivity())) next();
	}

	public static boolean hasPermissions(Context c) {
		int i= mPermission.length;
		for (int index=0;index<mPermission.length;index++){
			if(!hasPermission(c, mPermission[index])) return false;
		}
		return true;
	}

	public static boolean hasPermission(Context c, String permission) {
		return ContextCompat.checkSelfPermission(
				c, permission) ==
				PackageManager.PERMISSION_GRANTED;
	}


	@Override
	protected void updateLayout() {
		boolean complete = hasPermissions(getActivity());
		setCanGoNext(complete);
		setComplete(R.id.btnGrant, complete);
	}
}