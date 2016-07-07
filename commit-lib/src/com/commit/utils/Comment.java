package com.commit.utils;

import java.util.Date;

public class Comment {
	
	public String Username;
	public String Avatar;
	public Date Date;
	public String Content;
	public String Sign;
	public String Qiandao;

	public Comment(String username, String avatar, Date date,String content,String sign,String qiandao) {
		Username = username;
		Avatar = avatar;
		Date = date;
		Content = content;
		Sign = sign;
		Qiandao = qiandao;
	}
}
