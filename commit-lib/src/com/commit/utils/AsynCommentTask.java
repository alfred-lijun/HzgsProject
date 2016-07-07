package com.commit.utils;

import android.content.Context;
import android.os.AsyncTask;
import cn.bmob.v3.listener.SaveListener;

import com.bmob.server.bean.Comment;
import com.bmob.server.bean.UserBean;
import com.commit.auth.User;
import com.jsonhelper.JsonTools;

public class AsynCommentTask extends AsyncTask<String, Void, String>{

	Context mContext;
	String commentTx;
	String key;
	User user;
	OnCommentStateListener listener;
	
	public AsynCommentTask( Context context, String comment ,String key ,OnCommentStateListener listener){
		this.mContext = context;
		this.commentTx = comment;
		this.key = key;
		this.listener = listener;
		user = User.getInstance(mContext);
	}
	
	@Override
	protected String doInBackground(String... params) {
		
		UserBean userBean = new UserBean();
		userBean.setObjectId(user.getObjectId());
		userBean.setUserHead(user.getUserHead());
		userBean.setUserId(user.getUserId());
		userBean.setUserName(user.getUserName());
		userBean.setUserSign(user.getUserSign());
		userBean.setUserQiandao(InputTools.initQiandaoStr(user.getQiandaoCnt()));
		userBean.update(mContext);
		
		final Comment comment = new Comment();
		comment.setComm_vid(key);
		comment.setComm_uid(userBean.getUserId());
		comment.setContent(this.commentTx);
		comment.setUserBean(JsonTools.objToGson(userBean));
		comment.setUploadTime(System.currentTimeMillis());
		comment.save(mContext, new SaveListener() {
			@Override
			public void onFailure(int code, String msg) {
				if( listener != null ){
					listener.onFailure();
				}
			}
			@Override
			public void onSuccess() {
				if( listener != null ){
					listener.onSuccess(comment);
				}
			}
		});
		return null;
	}
	
	public interface OnCommentStateListener{
		public void onSuccess(Comment comment);
		public void onFailure();
	}
}
