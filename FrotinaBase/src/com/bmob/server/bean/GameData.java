package com.bmob.server.bean;

import cn.bmob.v3.BmobObject;

public class GameData extends BmobObject {

	private static final long serialVersionUID = 1L;
	private String gameName;
	private String headUrl;
	private String playUrl;
	private String briefing;
	private String freeMode;
	private String newMode;
	private String onlineMode;
	private Integer playCount;

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}

	public String getBriefing() {
		return briefing;
	}

	public void setBriefing(String briefing) {
		this.briefing = briefing;
	}

	public String getFreeMode() {
		return freeMode;
	}

	public void setFreeMode(String freeMode) {
		this.freeMode = freeMode;
	}

	public String getNewMode() {
		return newMode;
	}

	public void setNewMode(String newMode) {
		this.newMode = newMode;
	}

	public String getOnlineMode() {
		return onlineMode;
	}

	public void setOnlineMode(String onlineMode) {
		this.onlineMode = onlineMode;
	}

	public Integer getPlayCount() {
		return playCount == null ? 0 : playCount;
	}

	public void setPlayCount(Integer playCount) {
		this.playCount = playCount;
	}

}
