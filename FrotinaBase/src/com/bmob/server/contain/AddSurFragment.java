package com.bmob.server.contain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.Utils.UserUtils;
import com.bmob.server.AppUtils;

public class AddSurFragment extends Fragment {

	public static ExecutorService FULL_TASK_EXECUTOR;

	static {
		FULL_TASK_EXECUTOR = (ExecutorService) Executors.newCachedThreadPool();
	};

	public void onResume() {
		AppUtils.deleteDownLoaded();
		super.onResume();
	}
	
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
}
