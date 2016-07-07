package com.commit.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.text.TextUtils;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaUser;
import com.baidu.frontia.api.FrontiaAuthorization;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;
import com.baidu.frontia.api.FrontiaAuthorizationListener.AuthorizationListener;
import com.baidu.frontia.api.FrontiaAuthorizationListener.UserInfoListener;
import com.bmob.server.bean.UserBean;
import com.commit.utils.HttpUtil;

public class SocialPlatform  
{
	private Context mContext;
	private Handler mHandler;
	private String nickName = null;
	private String account = null;
	private String passWord = null;
	private FrontiaAuthorization mAuthorization;
	
	public static final int AUTH_CANCEL = 0;
	public static final int AUTH_START = 2;
	public static final int AUTH_SUCCESS = 5;
	public static final int AUTH_FAILED = 10;
	public static final int AUTH_NOWIFI = 12;
	public static final int AUTH_FAILED_NO_NICKANME = 11;
	
	public SocialPlatform(Context context){
		mContext = context;
	}
	
	public SocialPlatform( Context context , String nickName ){
		mContext = context;
		this.nickName = nickName;
	}

	public SocialPlatform setAuthorization( FrontiaAuthorization authorization ){
		this.mAuthorization = authorization;
		return this;
	}
	
	public void auth(Handler handler) {
		mHandler = handler;	
		if( TextUtils.isEmpty( nickName ) && !TextUtils.isEmpty( account ) ){
			nickName = account;
		}
		if( !TextUtils.isEmpty( nickName ) ){ //快�?�登�?
			loginWithoutQQ(mContext,nickName,onLoginStateListener);
		}
		else {
			login(mContext,onLoginStateListener);//qq登录
		}
	}

	public SocialPlatform setAccount( String account ) {
		this.account = account;
		return this;
	}
	
	public SocialPlatform setPassword( String passWord ) {
		this.passWord = passWord;
		return this;
	}
	
	OnLoginStateListener onLoginStateListener = new OnLoginStateListener(){
		@Override
		public void onLoginSuccess(){
			System.out.println("---onLoginSuccess---");
			if(mHandler != null) mHandler.sendEmptyMessage(AUTH_SUCCESS);
		}
		@Override
		public void onLoginFailed(){
			System.out.println("---onLoginFailed---");
			if(mHandler != null) mHandler.sendEmptyMessage(AUTH_FAILED);
		}
		@Override
		public void onLoginStart(){
			System.out.println("---onLoginStart---");
			if(mHandler != null) mHandler.sendEmptyMessage(AUTH_START);
		}
		@Override
		public void onLoginNoWifi() {
			System.out.println("---onLoginNoWifi---");
			if(mHandler != null) mHandler.sendEmptyMessage(AUTH_NOWIFI);
		}
	};
	
	private static void saveInformation(final Context context ,final Map<String, Object> info, boolean update,final UserBean toUpdateobject,final OnLoginStateListener onLoginStateListener) 
	{
		if( update ){
			final Map<String, Object> data = new HashMap<String, Object>();
			data.put(Conf.UserName, (String)info.get(Conf.UserName));
			data.put(Conf.UserHead, toUpdateobject.getUserHead());
			data.put(Conf.UserUid, toUpdateobject.getUserId());
			data.put(Conf.UserObjectId, toUpdateobject.getObjectId());
			data.put(Conf.UserAlbum, toUpdateobject.getUserAlbum());
			SaveData(context, data);
			if( onLoginStateListener != null ){
        		onLoginStateListener.onLoginSuccess();
        	}
		}else{
			final UserBean userBean = new UserBean();
			userBean.setUserName((String)info.get(Conf.UserName));
			userBean.setUserHead((String)info.get(Conf.UserHead));
			userBean.setUserId((String)info.get(Conf.UserUid));
			userBean.save(context, new SaveListener(){
				@Override
				public void onFailure(int arg0, String arg1) {
					System.out.println("insertData--onFailure");
					if( onLoginStateListener != null ){
		        		onLoginStateListener.onLoginFailed();
		        	}
				}
				@Override
				public void onSuccess() {
					System.out.println("insertData--onSuccess");
					info.put(Conf.UserObjectId, userBean.getObjectId());
					SaveData(context, info);
					if( onLoginStateListener != null ){
		        		onLoginStateListener.onLoginSuccess();
		        	}
				}
			});
		}
	}
	
	public static void SaveData(final Context context,final Map<String, Object> info){	
		User.getInstance(context).setLogin(true)
		.setUserName((String)info.get(Conf.UserName))
		.setUserHead((String)info.get(Conf.UserHead))
		.setUserId((String)info.get(Conf.UserUid))
		.setObjectId((String)info.get(Conf.UserObjectId))
		.setUserAlbum((String)info.get(Conf.UserAlbum));
	}
	
	public static void SaveLoginData(final Context context,final Map<String, Object> info,final OnLoginStateListener l){
		BmobQuery<UserBean> query = new BmobQuery<UserBean>();
		query.addWhereEqualTo(Conf.UserUid, (String)info.get(Conf.UserUid));
		query.setLimit(1);
		query.findObjects(context, new FindListener<UserBean>() {
			@Override
			public void onSuccess(List<UserBean> list) {
				if ( list != null && !list.isEmpty( ) ){
					System.out.println("objects.size() > 0");
					saveInformation(context,info,true,list.get(0),l);
				} else {
					System.out.println("objects.size() = 0");
					saveInformation(context,info,false,null,l);
				}
			}
			@Override
			public void onError(int code, String message) {
				if ( code == 101 ){
					saveInformation(context,info,false,null,l);
				}
			}
		});
	}
	
	public void loginWithoutQQ(final Context context,final String nickName,final OnLoginStateListener l){
		if( HttpUtil.hasInternet( context ) ){
			if( TextUtils.isEmpty( account ) ){
				startLogin(context,HttpUtil.GetLocalMacAddress(context),l);
			}else{
				startLogin(context,account + "##" +passWord,l);
			}
		}else{
			if( l != null ){
				l.onLoginNoWifi();
			}
		}
	}
	
	private void startLogin( Context context,String openId,OnLoginStateListener l){
		Map<String, Object> info = new HashMap<String, Object>();
		info.put(Conf.UserName, TextUtils.isEmpty(nickName) ? "小白" : nickName);
		info.put(Conf.UserHead, null);
		info.put(Conf.UserUid, openId);
		SaveLoginData(context,info,l);
		if( l != null ){
			l.onLoginStart();
		}
	}
	
	public interface OnLoginStateListener{
		public void onLoginSuccess();
		public void onLoginFailed();
		public void onLoginNoWifi();
		public void onLoginStart();
	}
	
	private void login( Context context ,final OnLoginStateListener onLoginStateListener ) {
		if( HttpUtil.hasInternet( context ) ){
			mAuthorization.authorize((Activity)mContext,FrontiaAuthorization.MediaType.QZONE.toString(),new AuthorizationListener(){
				@Override
				public void onSuccess(FrontiaUser result){
					Frontia.setCurrentAccount(result);
					userinfo(MediaType.QZONE.toString(),onLoginStateListener);
					if( onLoginStateListener != null ){
						onLoginStateListener.onLoginStart();
					}
				}
				@Override
				public void onFailure(int errorCode, String errorMessage){
					System.out.println("errorCode = " + errorCode + "  errorMessage = " + errorMessage);
					if( onLoginStateListener != null ){
		        		onLoginStateListener.onLoginFailed();
		        	}
				}
				@Override
				public void onCancel(){
					System.out.println("onCancel");
					if( onLoginStateListener != null ){
		        		onLoginStateListener.onLoginFailed();
		        	}
				}
			});
		}else{
			if( onLoginStateListener != null ){
				onLoginStateListener.onLoginNoWifi();
			}
		}
    }
	
	private void userinfo(String accessToken ,final OnLoginStateListener onLoginStateListener){
		mAuthorization.getUserInfo(accessToken, new UserInfoListener(){
			@Override
			public void onSuccess(FrontiaUser.FrontiaUserDetail result){	
				final Map<String, Object> info = new HashMap<String, Object>();
				info.put(Conf.UserName, result.getName());
				info.put(Conf.UserHead, result.getHeadUrl());
				info.put(Conf.UserUid, result.getId() + HttpUtil.GetLocalMacAddress(mContext));
				SaveLoginData(mContext,info,onLoginStateListener);
				System.out.println("--userinfo--onSuccess");
			}

			@Override
			public void onFailure(int errCode, String errMsg){
				if( onLoginStateListener != null ){
	        		onLoginStateListener.onLoginFailed();
	        	}
				System.out.println("--userinfo--onFailure");
			}
		});
	}
	
	public static boolean CheckQQExist( Context context ){
		String packageName = "com.tencent.mobileqq";
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        int cnt = pinfo.size();
        for ( int i = 0; i < cnt ; i++ ){
            if(pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }
	
}
