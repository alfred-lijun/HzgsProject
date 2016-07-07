package com.bmob.server;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.text.TextUtils;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.bmob.server.bean.AppBean;
import com.commit.auth.Conf;
import com.commit.auth.User;

public class AppUtils {

public static void saveInfo(final Context context){
		
	BmobQuery<AppBean> query = new BmobQuery<AppBean>();
	query.addWhereEqualTo(Conf.PACKAGENAME, context.getPackageName());
	query.findObjects(context,new FindListener<AppBean>(){
		@Override
		public void onError(int errorCode, String errorMsg) {
			if( errorCode == 101 ){
				
				PackageManager manager;
				PackageInfo info = null;
				manager = context.getPackageManager();
				try {
					info = manager.getPackageInfo(context.getPackageName(), 0);
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				
				User user = User.getInstance(context);
				AppBean appBean = new AppBean();
				appBean.setPackName(context.getPackageName());
				appBean.setAppAd(user.getAppAd());
				appBean.setAppBannerAd(user.getAppBannerAd());
				appBean.setAppKind(user.getAppKind());
				appBean.setAppGDT(user.getGdtAppAd());
				appBean.setAppGDTBanner(user.getGdtBannerAppAd());
				appBean.setAppVersion(user.getAppVersion());
				appBean.setAppInterteristalPosId(user.getGdtInterteristalPosId());
				appBean.setAppAppWallId(user.getAppWallId());
				appBean.setAppVersionCode(String.valueOf(info.versionCode));
				appBean.save(context);
			}
		}
		@Override
		public void onSuccess(List<AppBean> list) {
			User user = User.getInstance(context);
			if( list != null && !list.isEmpty() ){
				AppBean appBean = list.get(0);
				
				String appAd = appBean.getAppAd();
				if( !TextUtils.isEmpty( appAd ) ) {
					user.setAppAd( appAd );
				}
				
				String appBannerAd = appBean.getAppBannerAd();
				if( !TextUtils.isEmpty( appBannerAd ) ) {
					user.setAppBannerAd( appBannerAd );
				}
				
				String appKind = appBean.getAppKind();
				if( !TextUtils.isEmpty( appKind ) ) {
					user.setAppKind( appKind );
				}
				
				String appGdt = appBean.getAppGDT();
				if( !TextUtils.isEmpty( appGdt ) ) {
					user.setGdtAppAd( appGdt );
				}
				
				String appGdtBanner = appBean.getAppGDTBanner();
				if( !TextUtils.isEmpty( appGdtBanner ) ) {
					user.setGdtBannerAppAd( appGdtBanner );
				}
				
				String appNewVersion = appBean.getAppVersion();
				if( !TextUtils.isEmpty( appNewVersion ) ) {
					user.setAppVersion( appNewVersion );
				}
				
				String appGdtInterteristalPosId = appBean.getAppInterteristalPosId();
				if( !TextUtils.isEmpty( appGdtInterteristalPosId ) ) {
					user.setGdtInterteristalPosId(appGdtInterteristalPosId);
				}
				
				String appGdtAppWallId = appBean.getAppAppWallId();
				if( !TextUtils.isEmpty( appGdtAppWallId ) ) {
					user.setGdtAppWallId( appGdtAppWallId );
				}
				
				String appVersionCode = appBean.getAppVersionCode();
				
				if( !TextUtils.isEmpty( appVersionCode ) ) {
					user.setAppVersionCode( appVersionCode );
				}
				System.out.println("appAd = " + appAd);
				System.out.println("appKind = " + appKind);
				System.out.println("appBannerAd = " + appBannerAd);
				System.out.println("appGdt = " + appGdt);
				System.out.println("appGdtBanner = " + appGdtBanner);
				System.out.println("appNewVersion = " + appNewVersion);
				System.out.println("appGdtInterteristalPosId = " + appGdtInterteristalPosId);
				System.out.println("appGdtAppWallId = " + appGdtAppWallId);
				System.out.println("appVersionCode = " + appVersionCode);
			}else{
				AppBean appBean = new AppBean();
				appBean.setPackName(context.getPackageName());
				appBean.setAppAd(user.getAppAd());
				appBean.setAppBannerAd(user.getAppBannerAd());
				appBean.setAppKind(user.getAppKind());
				appBean.setAppGDT(user.getGdtAppAd());
				appBean.setAppGDTBanner(user.getGdtBannerAppAd());
				appBean.setAppVersion(user.getAppVersion());
				appBean.setAppVersionCode(user.getAppVersionCode());
				appBean.setAppInterteristalPosId(user.getGdtInterteristalPosId());
				appBean.setAppAppWallId(user.getAppWallId());
				appBean.save(context);
			}
		}});
	}

	public static void deleteApk(File file) {  
		if (file.isFile() && file.getAbsolutePath().endsWith("apk")) {
			file.delete();  
			return;  
		} if(file.isDirectory()){  
			File[] childFiles = file.listFiles();  
			if (childFiles == null || childFiles.length == 0) {  
				file.delete();  
				return;  
		}  
		for (int i = 0; i < childFiles.length; i++) {  
			deleteApk(childFiles[i]);  
		}  
		file.delete();  
		}
	} 
	
	public static String getSdCardPath(){
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	public static void deleteDownLoaded(){
		deleteApk(new File(getSdCardPath() + File.separator + "bdappunionsdk" + File.separator));
		deleteApk(new File(getSdCardPath() + File.separator + "bddownload"    + File.separator));
		deleteApk(new File(getSdCardPath() + File.separator + "GDTDOWNLOAD"   + File.separator));
	}
}
