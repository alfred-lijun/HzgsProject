package com.commit.auth;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import cn.pedant.sweetalert.SweetAlertDialog;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.api.FrontiaAuthorization;
import com.commit.R;

public class LoginComponent {

	private Context mContext;
	private SweetAlertDialog loginDialog;
	private FrontiaAuthorization mAuthorization;
	private static LoginComponent mComponent;
	private OnLoginSuccessListener listener;
	
	public LoginComponent( Context context ){
		this.mContext = context;
	}
	
	public static LoginComponent getInstance( Context context ){
		if( mComponent == null ){
			mComponent = new LoginComponent( context );
		}
		return mComponent;
	}
	
	public void startLogin(){
		mAuthorization = Frontia.getAuthorization();
		new AlertDialog.Builder(mContext).setTitle(R.string.choose_login).setItems(new String[]{ mContext.getString(R.string.auto), mContext.getString(R.string.qq) },new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog,int which){
				switch (which){
					case 0:
						final EditText nickNameEdt = new EditText(mContext);
						new AlertDialog.Builder(mContext).setTitle(mContext.getText(R.string.please_input_nickname))
						.setIcon(android.R.drawable.ic_dialog_info)
						.setView(nickNameEdt)
						.setPositiveButton("确定", new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which){
								String nickName = nickNameEdt.getText().toString();
								if( TextUtils.isEmpty( nickName ) ){
									AuthHandler.sendEmptyMessage(SocialPlatform.AUTH_FAILED_NO_NICKANME);
								}else{
									new SocialPlatform(mContext,nickName).setAuthorization(mAuthorization).auth(AuthHandler); 
								}
							}
						}).setNegativeButton("取消", null).show();
						break;
					case 1:
						new SocialPlatform(mContext).setAuthorization(mAuthorization).auth(AuthHandler); //qq
						break;
					default:
						break;
				}
			}
		}).setNegativeButton(R.string.cancel_login, null).show();
	}
	
	@SuppressLint("HandlerLeak") 
	private Handler AuthHandler = new Handler(){
		public void handleMessage(android.os.Message msg){
			switch (msg.what){
				case SocialPlatform.AUTH_SUCCESS:
//					Toast.makeText(mContext, R.string.login_success,Toast.LENGTH_SHORT).show();
					loginDialog.setCancelable(true);
					loginDialog.setCanceledOnTouchOutside(true);
					loginDialog.setTitleText("登录成功!").setConfirmText("好").changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
					if( listener != null ){
						listener.onSuccess();
					}
					closeLoginDialog();
					break;
				case SocialPlatform.AUTH_FAILED:
					Toast.makeText(mContext, R.string.login_failed,Toast.LENGTH_SHORT).show();
					closeLoginDialog();
					break;
				case SocialPlatform.AUTH_START:
					loginDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE).setTitleText("正在登录...");
					loginDialog.setCancelable(false);
					loginDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.blue_btn_bg_color));
					loginDialog.show();
					break;
				case SocialPlatform.AUTH_FAILED_NO_NICKANME:
					Toast.makeText(mContext, R.string.login_failed_no_nickname,Toast.LENGTH_SHORT).show();
					break;
				case SocialPlatform.AUTH_NOWIFI:
					Toast.makeText(mContext, R.string.login_failed_no_wifi,Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
		}
	};
	
	private void closeLoginDialog(){
		if(loginDialog != null && loginDialog.isShowing()) {
			loginDialog.cancel();
			loginDialog.dismiss();
			loginDialog = null;
		}
	};
	
	public LoginComponent setOnLoginSuccessListener(OnLoginSuccessListener listener){
		this.listener = listener;
		return this;
	}
	
	public interface OnLoginSuccessListener{
		public void onSuccess();
	}
}
