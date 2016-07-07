package com.commit.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import com.commit.R;
import com.commit.utils.NewCommentFragment;

public class TestActivity extends Activity{

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		initData();
		findView();
		initView();
	}
	
	@SuppressLint("NewApi") 
	private void findView()
	{
		Bundle bundle = new Bundle();
		bundle.putString(NewCommentFragment.STRKEY, "123123");
		NewCommentFragment newCommentFragment = NewCommentFragment.newInstance(bundle);
		getFragmentManager().beginTransaction().add(R.id.test, newCommentFragment).commit();
	}
	
	private void initData()
	{
		
	}
	
	private void initView()
	{
		
	}
    
}
