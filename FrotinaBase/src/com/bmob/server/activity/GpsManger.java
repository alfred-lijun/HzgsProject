package com.bmob.server.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.Utils.UserUtils;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bmob.server.R;
import com.bmob.server.bean.AppBean;
import com.commit.auth.Conf;

public class GpsManger implements BDLocationListener{

	private Context mContext;
	private SplashJudgeInterface listener;
	private boolean isAble;
	private LocationClient mLocClient;
	private String version;
	
	public GpsManger( Context context,SplashJudgeInterface listener ,boolean isAble){
		this.mContext = context;
		this.listener = listener;
		this.isAble = isAble;
		version = com.commit.auth.User.getAppVersionName(mContext);
		mLocClient = new LocationClient(mContext);
		mLocClient.registerLocationListener(this);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("gcj02");
		option.setAddrType("all");
		mLocClient.setLocOption(option);
	}
	
	public void resume(){
//		if (mLocClient != null) {
//			mLocClient.start();
//		}
		BmobQuery<AppBean> query = new BmobQuery<AppBean>();
		query.addWhereEqualTo(Conf.PACKAGENAME, mContext.getPackageName());
		query.findObjects(mContext,new FindListener<AppBean>(){
			@Override
			public void onError(int errorCode, String errorMsg) {
				if ( isAble ) {
				listener.doRefshUi(false);
			} else {
				listener.noRefshUi(false);
			}}
			@Override
			public void onSuccess(List<AppBean> list) {
				boolean result = false;
				if( list != null && !list.isEmpty() ){
					AppBean appBean = list.get(0);
					String versionCode = appBean.getAppVersion();
					if( !TextUtils.isEmpty( versionCode ) && !versionCode.equals(version) ){
						result = true;
					}
				}
				if ( isAble ) {
					listener.doRefshUi(result);
				} else {
					listener.noRefshUi(result);
				}
			}
		});
	}
	
	public void pause(){
//		if (mLocClient != null) {
//			mLocClient.stop();
//		}
	}
	
	@SuppressLint("NewApi") 
	public void start(){
		resume();
	}
	
	public void stop() {
		
	}
	
	public void setListener(SplashJudgeInterface listener) {
		this.listener = listener;
	}

	@Override
	public void onReceiveLocation(BDLocation bdLocation) {
		boolean result = false;
		if( bdLocation != null ){
			String ip = bdLocation.getAddrStr();
			result = !TextUtils.isEmpty(ip) && !ip.contains(mContext.getString(R.string.action_l_1)) && !UserUtils.is360Exist(mContext);
		}
		if ( isAble ) {
			listener.doRefshUi(result);
		} else {
			listener.noRefshUi(result);
		}
	}

	@Override
	public void onReceivePoi(BDLocation bdLocation) {
		
	}
}
