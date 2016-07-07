package com.bmob.server.contain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;

import com.Utils.UserUtils;
import com.bmob.server.AppUtils;

@SuppressLint("NewApi") 
public class AddFragment extends Fragment{
	
	public static ExecutorService FULL_TASK_EXECUTOR;

	static {
		FULL_TASK_EXECUTOR = (ExecutorService) Executors.newCachedThreadPool();
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UserUtils.initAppUnionSDK(getActivity());
	}
	
	@Override
	public void onDestroy() {
		UserUtils.quitAppUnionSDK(getActivity());
		super.onDestroy();
	}
	
	public void onResume(){
		AppUtils.deleteDownLoaded();
		super.onResume();
	}
}
