package com.bmob.server.bean;

import cn.bmob.v3.BmobObject;

public class UserBean extends BmobObject {

	private static final long serialVersionUID = 1L;
	private String userName;
	private String userHead;
	private String userId;
	private String userSign;
	private String userAlbum;
	private String userQiandao;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserHead() {
		return userHead;
	}

	public void setUserHead(String userHead) {
		this.userHead = userHead;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserSign() {
		return userSign;
	}

	public void setUserSign(String userSign) {
		this.userSign = userSign;
	}

	public String getUserAlbum() {
		return userAlbum;
	}

	public void setUserAlbum(String userAlbum) {
		this.userAlbum = userAlbum;
	}
	
	public String getUserQiandao() {
		return userQiandao;
	}

	public void setUserQiandao(String userQiandao) {
		this.userQiandao = userQiandao;
	}
	
}
