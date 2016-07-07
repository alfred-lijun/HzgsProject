package com.bmob.server.activity;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;

public abstract class SplashJudgeBarActivity extends ActionBarActivity implements SplashJudgeInterface{
	
	GpsManger mGpsManger;
	
	public void checkAppAble( Context context, boolean isAble ) {
		mGpsManger = new GpsManger(this,this,isAble);
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
