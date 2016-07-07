package com.bmob.server.bean;

import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject {

	private static final long serialVersionUID = 1L;
	private String comm_vid;
	private String comm_uid;
	private String userBean;
	private long uploadTime;
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(long uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getComm_vid() {
		return comm_vid;
	}

	public void setComm_vid(String comm_vid) {
		this.comm_vid = comm_vid;
	}

	public String getComm_uid() {
		return comm_uid;
	}

	public void setComm_uid(String comm_uid) {
		this.comm_uid = comm_uid;
	}

	public String getUserBean() {
		return userBean;
	}

	public void setUserBean(String userBean) {
		this.userBean = userBean;
	}

}
