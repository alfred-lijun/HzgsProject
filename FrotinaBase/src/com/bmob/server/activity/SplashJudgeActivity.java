package com.bmob.server.activity;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

public abstract class SplashJudgeActivity extends FragmentActivity implements SplashJudgeInterface {

	GpsManger mGpsManger;

	public void checkAppAble(Context context, boolean isAble) {
		mGpsManger = new GpsManger(this, this,isAble);
		mGpsManger.start();
	}
	
	@Override
	public void onPause(){
		if( mGpsManger != null ){
			mGpsManger.pause();
		}
		super.onPause();
	}
	
	@Override
	public void onDestroy(){
		if( mGpsManger != null ){
			mGpsManger.pause();
		}
		super.onDestroy();
	}
}
