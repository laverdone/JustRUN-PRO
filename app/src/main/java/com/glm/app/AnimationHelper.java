package com.glm.app;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Helper per generazione animazioni di base
 * @author norick
 */
public class AnimationHelper {
    public static long Duration_Default = 350;	//millis

    public static Animation inFromRightAnimation() {
	Animation inFromRight = new TranslateAnimation(
		Animation.RELATIVE_TO_PARENT,  +1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
		Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
	);
	inFromRight.setDuration(Duration_Default);
	inFromRight.setInterpolator(new AccelerateInterpolator());
	return inFromRight;
    }
    public static Animation outToLeftAnimation() {
	Animation outtoLeft = new TranslateAnimation(
		Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  -1.0f,
		Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
	);
	outtoLeft.setDuration(Duration_Default);
	outtoLeft.setInterpolator(new AccelerateInterpolator());
	return outtoLeft;
    }    
    public static Animation inFromLeftAnimation() {
	Animation inFromLeft = new TranslateAnimation(
		Animation.RELATIVE_TO_PARENT,  -1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
		Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
	);
	inFromLeft.setDuration(Duration_Default);
	inFromLeft.setInterpolator(new AccelerateInterpolator());
	return inFromLeft;
    }
    public static Animation outToRightAnimation() {
	Animation outtoRight = new TranslateAnimation(
		Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  +1.0f,
		Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
	);
	outtoRight.setDuration(Duration_Default);
	outtoRight.setInterpolator(new AccelerateInterpolator());
	return outtoRight;
    }
    
    public static Animation zoomInAnimation () {
	Animation anim = new ScaleAnimation(
		0.0f,  +1.0f, 0.0f,  +1.0f, 
		Animation.RELATIVE_TO_PARENT, 0.5f, 
		Animation.RELATIVE_TO_PARENT, 0.5f
	);
	anim.setDuration(Duration_Default);
	anim.setInterpolator(new AccelerateInterpolator());
	return anim;	
    }
    
    public static Animation zoomIn2Animation () {
	Animation anim = new ScaleAnimation(
		4.0f,  +1.0f, 4.0f,  +1.0f, 
		Animation.RELATIVE_TO_PARENT, 0.5f, 
		Animation.RELATIVE_TO_PARENT, 0.5f
	);
	anim.setDuration(Duration_Default);
	anim.setInterpolator(new AccelerateInterpolator());
	return anim;	
    }
    
}