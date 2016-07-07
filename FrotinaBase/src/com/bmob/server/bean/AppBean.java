package com.bmob.server.bean;

import cn.bmob.v3.BmobObject;

public class AppBean extends BmobObject {

	private static final long serialVersionUID = 1L;

	private String appAd;
	private String appKind;
	private String appName;
	private String appBannerAd;
	private String appGDTBanner;
	private String appGDT;
	private String appVersion;
	private String appInterteristalPosId;
	private String appAppWallId;
	private String packName;
	private String appVersionCode;
	
	public String getAppVersionCode() {
		return appVersionCode;
	}

	public void setAppVersionCode(String appVersionCode) {
		this.appVersionCode = appVersionCode;
	}

	public String getAppBannerAd() {
		return appBannerAd;
	}

	public void setAppBannerAd(String appBannerAd) {
		this.appBannerAd = appBannerAd;
	}
	
	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public String getAppAd() {
		return appAd;
	}
	
	public void setAppAd(String appAd) {
		this.appAd = appAd;
	}
	
	public String getAppKind() {
		return appKind;
	}
	
	public void setAppKind(String appKind) {
		this.appKind = appKind;
	}
	
	public String getAppName() {
		return appName;
	}
	
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public String getAppGDTBanner() {
		return appGDTBanner;
	}
	
	public void setAppGDTBanner(String appGDTBanner) {
		this.appGDTBanner = appGDTBanner;
	}
	
	public String getAppGDT() {
		return appGDT;
	}
	
	public void setAppGDT(String appGDT) {
		this.appGDT = appGDT;
	}
	
	public String getAppVersion() {
		return appVersion;
	}
	
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	
	public String getAppInterteristalPosId() {
		return appInterteristalPosId;
	}
	
	public void setAppInterteristalPosId(String appInterteristalPosId) {
		this.appInterteristalPosId = appInterteristalPosId;
	}
	
	public String getAppAppWallId() {
		return appAppWallId;
	}
	
	public void setAppAppWallId(String appAppWallId) {
		this.appAppWallId = appAppWallId;
	}
	
	
}
