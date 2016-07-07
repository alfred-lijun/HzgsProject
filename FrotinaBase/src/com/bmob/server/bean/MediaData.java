package com.bmob.server.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class MediaData extends BmobObject implements Serializable
{
	private static final long serialVersionUID = 1L;
	public static final int READ = 9; 
	public static final int VIDEO = 10; 
	public static final int TALK = 11; 
	public static final int GAME = 12; 
	
	public String  id;//媒体id
	public String  mediaName;//媒体名字
	public int 	   mediaType;//是阅读还是视频
	public String  mediaStyle;//媒体类型
	public String  mediaStyleId;//媒体类型id
	public long    updateTime;//上传时间
	public String  describe; //描述
	public String  picUrl;//缩略图链接
	public String  contentUrl;//内容或者内容链接
	public String  auther;//作者
	public String  createTime;//作品创作的时间
	public String  freeMode; //收费模式 1收费 2免费
	public Integer playCnt; //播放次数
	public Integer likeCnt; //点赞次数
	public Integer commCnt; //评论次数

	/**
	 * 
	 * @param id
	 * @param mediaName
	 * @param mediaType
	 * @param mediaStyle
	 * @param mediaStyleId
	 * @param updateTime
	 * @param describe
	 * @param picUrl
	 * @param contentUrl
	 * @param auther
	 * @param createTime
	 * @param freeMode
	 * @param playCnt
	 * @param likeCnt
	 * @param commCnt
	 */
	public MediaData(String id, String mediaName, int mediaType,
			String mediaStyle, String mediaStyleId, long updateTime,
			String describe, String picUrl, String contentUrl, String auther,
			String createTime, String freeMode, Integer playCnt,
			Integer likeCnt, Integer commCnt) {
		super();
		this.id = id;
		this.mediaName = mediaName;
		this.mediaType = mediaType;
		this.mediaStyle = mediaStyle;
		this.mediaStyleId = mediaStyleId;
		this.updateTime = updateTime;
		this.describe = describe;
		this.picUrl = picUrl;
		this.contentUrl = contentUrl;
		this.auther = auther;
		this.createTime = createTime;
		this.freeMode = freeMode;
		this.playCnt = playCnt;
		this.likeCnt = likeCnt;
		this.commCnt = commCnt;
	}

	public String getMediaStyle() {
		return mediaStyle;
	}

	public void setMediaStyle(String mediaStyle) {
		this.mediaStyle = mediaStyle;
	}

	public String getMediaStyleId() {
		return mediaStyleId;
	}

	public void setMediaStyleId(String mediaStyleId) {
		this.mediaStyleId = mediaStyleId;
	}

	public MediaData() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public int getMediaType() {
		return mediaType;
	}

	public void setMediaType(int mediaType) {
		this.mediaType = mediaType;
	}
	
	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	public String getAuther() {
		return auther;
	}

	public void setAuther(String auther) {
		this.auther = auther;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getPlayCnt() {
		return playCnt;
	}

	public void setPlayCnt(Integer playCnt) {
		this.playCnt = playCnt;
	}

	public Integer getLikeCnt() {
		return likeCnt;
	}

	public void setLikeCnt(Integer likeCnt) {
		this.likeCnt = likeCnt;
	}

	public Integer getCommCnt() {
		return commCnt;
	}

	public void setCommCnt(Integer commCnt) {
		this.commCnt = commCnt;
	}

	public String getFreeMode() {
		return freeMode;
	}

	public void setFreeMode(String freeMode) {
		this.freeMode = freeMode;
	}
	
}
