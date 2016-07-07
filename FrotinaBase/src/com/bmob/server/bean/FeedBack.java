package com.bmob.server.bean;

import cn.bmob.v3.BmobObject;

public class FeedBack extends BmobObject {

	private static final long serialVersionUID = 1L;
	private String os;
	private String phone;
	private String content;

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
