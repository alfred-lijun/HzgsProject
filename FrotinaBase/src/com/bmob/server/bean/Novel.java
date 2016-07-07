package com.bmob.server.bean;

import cn.bmob.v3.BmobObject;

public class Novel extends BmobObject {

	private static final long serialVersionUID = 1L;
	private String fromUrl;
	private String content;

	public Novel() {
		super();
	}

	public Novel(String fromUrl, String content) {
		super();
		this.fromUrl = fromUrl;
		this.content = content;
	}

	public String getFromUrl() {
		return fromUrl;
	}

	public void setFromUrl(String fromUrl) {
		this.fromUrl = fromUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
