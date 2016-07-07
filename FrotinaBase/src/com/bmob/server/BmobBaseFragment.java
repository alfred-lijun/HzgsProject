package com.bmob.server;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;

@SuppressLint("NewApi")
public abstract class BmobBaseFragment extends Fragment implements RefeshInterface {
	
	public String title;

	public abstract void dispatchTouchEvent(MotionEvent event);
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
}
