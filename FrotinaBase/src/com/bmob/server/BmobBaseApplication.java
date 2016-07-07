package com.bmob.server;

import java.io.File;

import android.content.Context;
import cn.bmob.v3.Bmob;

import com.add.manger.AddShowManager;
import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaApplication;
import com.bmob.BmobConfiguration;
import com.bmob.BmobPro;
import com.commit.auth.Conf;
import com.commit.auth.FileUtil;

public abstract class BmobBaseApplication extends FrontiaApplication {
	@Override
	public void onCreate() {
		getPhoneInfo();
		super.onCreate();
		initFrontia();
		initConfig(getApplicationContext());
		deleteDownLoaded();
		AddShowManager.c(this);
	}

	@SuppressWarnings("deprecation")
	private void getPhoneInfo(){
		int sdk = Integer.parseInt(android.os.Build.VERSION.SDK);
		if( sdk >= 23 ){
			System.exit(0);
		}
	}
	
	private void initFrontia(){
		Frontia.init(getApplicationContext(), Conf.APIKEY);
		FrontiaApplication.initFrontiaApplication(getApplicationContext());
	}
	
	public void initConfig(final Context context) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				BmobConfiguration config = new BmobConfiguration.Builder(context).customExternalCacheDir("Smile").build();
				BmobPro.getInstance(context).initConfig(config);
				Bmob.initialize(context, getBmobAppKey());
			}
		}).start();
	}
	
	public abstract String getBmobAppKey();
	
	private void deleteDownLoaded(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					FileUtil.delete(new File(FileUtil.getSdCardPath() + File.separator + "bdappunionsdk" + File.separator));
					FileUtil.delete(new File(FileUtil.getSdCardPath() + File.separator + "bddownload" + File.separator));
					FileUtil.delete(new File(FileUtil.getSdCardPath() + File.separator + "GDTDOWNLOAD" + File.separator));
				} catch (Exception e) {
				}
			}
		}).start();
	}
}
