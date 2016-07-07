package com.commit.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bmob.server.bean.UserBean;
import com.commit.auth.User;

public class InputTools {
	
	public static final String DELETE = "delete_expression";
	public static final String SmileUtilsAll = "com.commit.utils.SmileUtils";
	public static final String TALKTYPE = "_talk";
	
	// 隐藏虚拟键盘
	public static void HideKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
		}
	}

	// 显示虚拟键盘
	public static void ShowKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
	}

	public static void showInputMethod(Context context, View view) {
		InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		im.showSoftInput(view, 0);
	}

	public static String initQiandaoStr( int cnt ){
		return String.valueOf("连续签到" + cnt + "天");
	}
	
	public static UserBean initUserBeadn(User user){
		UserBean userBean = new UserBean();
		userBean.setObjectId(user.getObjectId());
		userBean.setUserAlbum(user.getUserAlbum());
		userBean.setUserHead(user.getUserHead());
		userBean.setUserId(user.getUserId());
		userBean.setUserName(user.getUserName());
		userBean.setUserSign(user.getUserSign());
		return userBean;
	}
	
	public static List<String> getExpressionRes(int getSum) {
		List<String> reslist = new ArrayList<String>();
		for (int x = 1; x <= getSum; x++) {
			String filename = "ee_" + x;
			reslist.add(filename);
		}
		return reslist;
	}
}
