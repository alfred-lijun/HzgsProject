package com.hugsproject;

import com.tencent.bugly.crashreport.CrashReport;

import android.app.Application;

public class HugsProjectApp extends Application{
	Application instance;
	
	@Override
	public void onCreate() {
		super.onCreate();
		CrashReport.initCrashReport(getApplicationContext(), "900037384", false); 
	}
}
