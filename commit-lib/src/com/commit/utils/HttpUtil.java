package com.commit.utils;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.commit.auth.User;

public class HttpUtil
{
	public static final int ONE_DAY_TIME = 24 * 60 * 60 * 1000;
	public static final int UPDAT_DELAY_TIME = 12 * 60 * 60 * 1000;
	
	public static final String BAIDU_UNION = "1";
	public static final String BAIDU_BAITONG = "2";
	public static final String GDT_BANNER = "3";
	
	@SuppressWarnings("resource")
	public static String GetLocalMacAddress( Context context ) {  
        String Mac=null;  
        try{     
            String path="sys/class/net/wlan0/address";  
            if((new File(path)).exists()){  
				FileInputStream fis = new FileInputStream(path);  
                byte[] buffer = new byte[8192];  
                int byteCount = fis.read(buffer);  
                if(byteCount>0) {  
                    Mac = new String(buffer, 0, byteCount, "utf-8");  
                }  
            }  
            if(Mac==null||Mac.length()==0) {  
                path="sys/class/net/eth0/address";  
				FileInputStream fis_name = new FileInputStream(path);  
                byte[] buffer_name = new byte[8192];  
                int byteCount_name = fis_name.read(buffer_name);  
                if(byteCount_name>0) {  
                    Mac = new String(buffer_name, 0, byteCount_name, "utf-8");  
                }  
            }  
              
            if(Mac.length()==0||Mac==null){  
                return "";  
            }  
        }catch(Exception io){  
        	io.printStackTrace();
        }  
        String mac = Mac.trim();
        
        if( TextUtils.isEmpty( mac ) ){
        	WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);  
        	WifiInfo info = wifi.getConnectionInfo();  
        	if (info.getMacAddress() != null) {  
        		return info.getMacAddress().toString();  
        	} 
        }
        return mac.replace(":", "");
    }
	
	/** hasInternet(判断设备是否已联网)*/
	public static boolean hasInternet(Context context){ // 判断设备是否已联网
		// 获取当前的网络连接服务
		ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		// 判断
		if (manager == null){
			return false;
		}
		// 获取活动的网络连接信息
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()){
			return false;
		}
		return true;
	}
	
	public static long currentTimeMillis(boolean sync){
		if ( !sync )  return System.currentTimeMillis();
		try{
			return syncCurrentTime();
		}catch (Exception e){
			return System.currentTimeMillis();
		}
	}
	
	public static long syncCurrentTime() throws Exception{
		URL url=new URL("http://www.bjtime.cn");//取得资源对象
		URLConnection uc = url.openConnection();//生成连接对象
		uc.connect(); //发出连接
		return uc.getDate();
	}
	
	public static long getUpdateInfoTime( Context context , int mediaType ){
		return PreferenceManager.getDefaultSharedPreferences(context).getLong(String.valueOf(mediaType)+"_save", 0);
	}
	
	public static void setUpdateInfoTime( Context context , int mediaType , long time ){
		PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(String.valueOf(mediaType)+"_save", time).commit();
	}
	
	public static String getRunningActivityName( Context context ) {  
        String contextString = context.toString();  
        return contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));  
	}
	
	public static void showNotifiDialog( Context context , String msg ){
		android.app.AlertDialog.Builder builder = new Builder( context );
		builder.setMessage( msg );
		builder.setTitle( "温馨提示" );
		builder.setPositiveButton( "确定", new DialogInterface.OnClickListener( ) {
			@Override
			public void onClick(DialogInterface dialog, int which ) {
				dialog.dismiss( );
			}
		});
		builder.setNegativeButton( "取消", new DialogInterface.OnClickListener( ) {
			@Override
			public void onClick( DialogInterface dialog, int which ) {
				dialog.dismiss( );
			}
		});
		android.app.AlertDialog notifDialog = builder.create( );
		notifDialog.setCancelable( false );
		notifDialog.show( );
	}
	
	public static String getVersionName( Context context ) {
	    try {
	        PackageManager manager = context.getPackageManager();
	        PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
	        String version = info.versionName;
	        return version;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "1";
	    }
	}
	
	public static void showNewVersonDialog( Context context ) {
		User user = User.getInstance(context);
		if( !HttpUtil.getVersionName( context ).equals( user.getAppVersion() ) && !TextUtils.isEmpty( user.getAppVersion( ) ) ) {
			HttpUtil.showNotifiDialog( context,"新版本：" + user.getAppVersion() + "已经上线啦！ 快去应用商城下载更新吧！~" );
		}
	}
}
