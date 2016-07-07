package com.bmob.server.contain;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.Utils.UserUtils;
import com.bmob.server.AppUtils;

public class AddActionBarActivity extends ActionBarActivity{
	
	public void onResume(){
		AppUtils.deleteDownLoaded();
		super.onResume();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UserUtils.initAppUnionSDK(this);
	}
	
	@Override
	protected void onDestroy() {
		UserUtils.quitAppUnionSDK(this);
		super.onDestroy();
	}
}
