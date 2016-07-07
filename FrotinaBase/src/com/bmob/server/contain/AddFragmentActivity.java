package com.bmob.server.contain;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.Utils.UserUtils;
import com.bmob.server.AppUtils;

public class AddFragmentActivity extends FragmentActivity{
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
