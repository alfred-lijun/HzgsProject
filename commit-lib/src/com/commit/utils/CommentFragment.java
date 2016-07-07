package com.commit.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ocpsoft.prettytime.PrettyTime;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

import com.bmob.server.bean.Comment;
import com.bmob.server.bean.UserBean;
import com.commit.R;
import com.commit.auth.Conf;
import com.commit.auth.LoginComponent;
import com.commit.auth.User;
import com.commit.utils.AsynCommentTask.OnCommentStateListener;
import com.commit.view.FButton;
import com.jsonhelper.JsonTools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressLint("NewApi")
public class CommentFragment extends Fragment implements OnClickListener
{
	private View mainView = null;
	private Context mContext;
	private User mUser;
	private EditText mCommentEditText;
	private LinearLayout mComments;
	private View mLoadMoreComment;
	private String keyObject;
	private String hitStr;
	private int mSkip = 0;
	private int mStep = 5;
	private int commType;
	private PrettyTime mPrettyTime;
	private int mCommentCount;
	private boolean mCommentFinished;
	private boolean mCommentEdtState = true;
	private boolean mSignTxState = true;
	private Button mLoadMoreButton;
	private DisplayImageOptions user_options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	
	public static final String STRKEY = "strkey";
	public static final String LEAVE_COMMENT = "_leave";
	public static final String PUBLISH_COMMENT = "_publish";
	public static final String COMM_TYPE = "comm_type";
	
	public static final int COMM_TYPE_PERSON 	= 1;
	public static final int COMM_TYPE_READ 		= 2;
	public static final int COMM_TYPE_VIDEO 	= 3;
	public static final int COMM_TYPE_GAME 		= 4;
	public static final int COMM_TYPE_TALK 		= 5;
	public static final int COMM_TYPE_PUBLISH 	= 6;
	
	public static CommentFragment newInstance(Bundle bundle) {
		CommentFragment newFragment = new CommentFragment();
		newFragment.setArguments(bundle);
		return newFragment;
    }
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup p = (ViewGroup) mainView.getParent(); 
		if (p != null) { 
			p.removeAllViewsInLayout(); 
		} 
        return mainView;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		initData();
		findView();
		initView();
	}
	
	private void initData(){
		mUser = User.getInstance(mContext);
		if( user_options == null ){
			user_options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.default_user)
			.showImageForEmptyUri(R.drawable.default_user)
			.showImageOnFail(R.drawable.default_user)
			.cacheInMemory(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
		}
		mPrettyTime = new PrettyTime();
		keyObject = getArguments().getString(STRKEY);
		commType = getArguments().getInt(COMM_TYPE);
	}
	
	private void findView(){
		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		mainView = inflater.inflate(R.layout.fragment_comment, null, false);
		mCommentEditText = (EditText) mainView.findViewById(R.id.comment_edit_text);
		mComments = (LinearLayout) mainView.findViewById(R.id.comments);
	}
	
	private void initView(){
		mCommentEditText.setOnClickListener(this);
		if( !TextUtils.isEmpty( hitStr ) ){
			mCommentEditText.setHint( hitStr );
		}
		mCommentEditText.setVisibility(mCommentEdtState?View.VISIBLE:View.GONE);
		getComments();
	}

	@Override
	public void onClick(final View v){
		int id = v.getId();
		if( id == R.id.comment_edit_text ){
			comment();
		}else if( id == R.id.deletBtn ){
			android.app.AlertDialog.Builder builder = new Builder(mContext);
			builder.setTitle( "温馨提示" );
			TextView textView = new TextView(mContext);
			textView.setTextSize(24);
			textView.setText( Html.fromHtml("确定要删除此条评论吗?"));
			textView.setGravity(Gravity.CENTER);
			builder.setView(textView);
			builder.setPositiveButton( "删除", new DialogInterface.OnClickListener( ) {
				@Override
				public void onClick(DialogInterface dialog, int which ){
					if( v.getTag() instanceof Comment ){
						Comment comment = (Comment) v.getTag();
						if( null != comment ){
							comment.delete(mContext, new DeleteListener() {
								@Override
								public void onSuccess() {
									View itemView = (View)v.getParent().getParent();
									View topView = mComments.getChildAt(1);
									if( itemView.equals(topView) && commType == COMM_TYPE_PUBLISH ){
										mUser.setUserSign("");
										UserBean userBean = new UserBean();
										userBean.setUserSign("");
										userBean.update(mContext,mUser.getObjectId(),null);
									}
									mComments.removeView(itemView);
								}
								@Override
								public void onFailure(int errorCode, String errorMsg) {
									Toast.makeText(mContext, R.string.delete_fail, Toast.LENGTH_LONG).show();
								}
							});
						}
					}
					dialog.dismiss( );
				}
			});
			builder.setNegativeButton( "取消", new DialogInterface.OnClickListener( ) {
				@Override
				public void onClick( DialogInterface dialog, int which ){
					dialog.dismiss( );
				}
			});
			android.app.AlertDialog deleteNotifDialog = builder.create( );
			deleteNotifDialog.setCancelable( false );
			deleteNotifDialog.show( );
		}
	}
	
	private void comment(){
		if ( mUser.isLogin() == false ){
			LoginComponent.getInstance(mContext).startLogin();
		}else{
			final EditText editText = new EditText(mContext);
			editText.setHeight(100);
			editText.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(250) });
			editText.setGravity(Gravity.LEFT | Gravity.TOP);
			AlertDialog.Builder commentDialog = new AlertDialog.Builder(mContext).setTitle(R.string.publish_comment).setView(editText).setNegativeButton(R.string.cancel_publish, null).setPositiveButton(R.string.publish, null);
			final AlertDialog dialog = commentDialog.create();
			dialog.show();
			dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					final String content = editText.getText().toString();
					if (content.length() == 0){
						Toast.makeText(mContext,R.string.comment_nothing,Toast.LENGTH_SHORT).show();
					}else{
						InputTools.HideKeyboard(editText);
						new AsynCommentTask(mContext,content,keyObject,listener).execute();
						dialog.dismiss();
					}
				}
			});
			dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					InputTools.HideKeyboard(editText);
					dialog.dismiss();
				}
			});
		}
	}

	private OnCommentStateListener listener = new OnCommentStateListener() {
		@Override
		public void onSuccess(Comment comment) {
			Toast.makeText(mContext,R.string.comment_success,Toast.LENGTH_SHORT).show();
			LayoutInflater mLayoutInflater = ((Activity) mContext).getLayoutInflater();
			View commentItem = mLayoutInflater.inflate(R.layout.comment_item, null);
			commentItem.setTag(InputTools.initUserBeadn(mUser));
			commentItem.setOnClickListener(CommentFragment.this);
			
			ImageView avatar = (ImageView) commentItem.findViewById(R.id.avatar);
			imageLoader.displayImage(mUser.getUserHead(), avatar, user_options);
			
			TextView contentTextView = (TextView) commentItem.findViewById(R.id.content);
			Spannable span = SmileUtils.getSmiledText(mContext, comment.getContent());
			contentTextView.setText(span, BufferType.SPANNABLE);
			
			TextView name = (TextView) commentItem.findViewById(R.id.name);
			name.setText(mUser.getUserName());
			
			TextView qiandao = (TextView) commentItem.findViewById(R.id.qiandaoTx);
			qiandao.setText(InputTools.initQiandaoStr(mUser.getQiandaoCnt()));
			
			FButton deleteBtn = (FButton) commentItem.findViewById(R.id.deletBtn);
			if( commType == COMM_TYPE_PERSON || commType == COMM_TYPE_PUBLISH ){
				deleteBtn.setVisibility(View.VISIBLE);
				deleteBtn.setOnClickListener(CommentFragment.this);
				deleteBtn.setTag(comment);
			}else{
				deleteBtn.setVisibility(View.INVISIBLE);
				deleteBtn.setOnClickListener(null);
				deleteBtn.setTag(null);
			}
			
			LinearLayout signLayout = (LinearLayout) commentItem.findViewById(R.id.signLayout);
			if( TextUtils.isEmpty( mUser.getUserSign() ) || !mSignTxState ){
				signLayout.setVisibility(View.INVISIBLE);
			}else{
				signLayout.setVisibility(View.VISIBLE);
				TextView signTx = (TextView) commentItem.findViewById(R.id.signTx);
				Spannable signSpan = SmileUtils.getSmiledText(mContext, mUser.getUserSign());
				signTx.setText(signSpan, BufferType.SPANNABLE);
			}
			mComments.addView(commentItem, 1);
		}
		
		@Override
		public void onFailure() {
			Toast.makeText(mContext,R.string.comment_failed,Toast.LENGTH_SHORT).show();
		}
	};
	
	private void getComments(){
		if (mLoadMoreComment != null){
			mLoadMoreComment.findViewById(R.id.load_progressbar).setVisibility(View.VISIBLE);
			mLoadMoreComment.findViewById(R.id.load_more_comment_btn).setVisibility(View.GONE);
		}
		BmobQuery<com.bmob.server.bean.Comment> query = new BmobQuery<com.bmob.server.bean.Comment>();
		query.addWhereEqualTo(Conf.COMM_VID, keyObject);
		query.setLimit(mStep);
		query.setSkip(mSkip);
		query.order("-createdAt");
		query.findObjects(mContext, new FindListener<com.bmob.server.bean.Comment>( ) {
			@Override
			public void onSuccess(List<com.bmob.server.bean.Comment> commentList) {
				if ( commentList == null || commentList.isEmpty() || commentList.size() < mStep ){
					mCommentFinished = true;
				}
				ArrayList<LinearLayout> commentsLayout = new ArrayList<LinearLayout>();
				for (com.bmob.server.bean.Comment comment : commentList){
					String json = comment.getUserBean();
					UserBean userBean = JsonTools.GsonToObj(json, UserBean.class);
					com.commit.utils.Comment commentInformation = new com.commit.utils.Comment(userBean.getUserName(), 
										userBean.getUserHead(),  
										new Date(comment.getUploadTime()), 
										comment.getContent(),
										userBean.getUserSign() == null ? "" : userBean.getUserSign(),
										userBean.getUserQiandao());
					LayoutInflater mLayoutInflater = ((Activity) mContext).getLayoutInflater();
					
					LinearLayout commentItem = (LinearLayout) mLayoutInflater.inflate(R.layout.comment_item, null);
					commentItem.setTag(userBean);
					commentItem.setOnClickListener(CommentFragment.this);
					
					TextView content = (TextView) commentItem.findViewById(R.id.content);
					Spannable span = SmileUtils.getSmiledText(mContext, commentInformation.Content);
					content.setText(span, BufferType.SPANNABLE);
					
					LinearLayout signLayout = (LinearLayout) commentItem.findViewById(R.id.signLayout);
					if( TextUtils.isEmpty( commentInformation.Sign ) || !mSignTxState ){
						signLayout.setVisibility(View.INVISIBLE);
					}else{
						signLayout.setVisibility(View.VISIBLE);
						TextView signTx = (TextView) commentItem.findViewById(R.id.signTx);
						Spannable signSpan = SmileUtils.getSmiledText(mContext,commentInformation.Sign);
						signTx.setText(signSpan, BufferType.SPANNABLE);
					}
					
					FButton deleteBtn = (FButton) commentItem.findViewById(R.id.deletBtn);
					if( commType == COMM_TYPE_PERSON || commType == COMM_TYPE_PUBLISH ){
						deleteBtn.setVisibility(View.VISIBLE);
						deleteBtn.setOnClickListener(CommentFragment.this);
						deleteBtn.setTag(comment);
					}else{
						deleteBtn.setVisibility(View.INVISIBLE);
						deleteBtn.setOnClickListener(null);
						deleteBtn.setTag(null);
					}
					
					ImageView avatar = (ImageView) commentItem.findViewById(R.id.avatar);
					imageLoader.displayImage(commentInformation.Avatar, avatar ,user_options);
					
					TextView username = (TextView) commentItem.findViewById(R.id.name);
					username.setText(commentInformation.Username);
					
					TextView qiandao = (TextView) commentItem.findViewById(R.id.qiandaoTx);
					qiandao.setText(commentInformation.Qiandao);
					
					TextView date = (TextView) commentItem.findViewById(R.id.time);
					date.setText(mPrettyTime.format(commentInformation.Date));
					
					commentsLayout.add(commentItem);
				}
				mSkip += mStep;
				mCommentCount += commentList.size();
				if ( mLoadMoreComment != null ){
					mComments.removeView(mLoadMoreComment);
				}
				for (LinearLayout commentView : commentsLayout.toArray(new LinearLayout[commentList.size()])){
					mComments.addView(commentView);
				}
				if (mCommentFinished == false){
					LayoutInflater mLayoutInflater = ((Activity) mContext).getLayoutInflater();
					mLoadMoreComment = mLayoutInflater.inflate(R.layout.comment_load_more, null);
					mLoadMoreButton = (Button) mLoadMoreComment.findViewById(R.id.load_more_comment_btn);
					mComments.addView(mLoadMoreComment);
					mLoadMoreButton.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v){
							getComments();
						}
					});
				}else{
					if ( mCommentCount > mStep ){
						mLoadMoreButton.setText(R.string.no_more_comments);
						mLoadMoreButton.setEnabled(false);
					}
				}
			}
			@Override
			public void onError(int code, String message) {
				if (mLoadMoreComment != null){
					mLoadMoreComment.findViewById(R.id.load_progressbar).setVisibility(View.INVISIBLE);
					mLoadMoreComment.findViewById(R.id.load_more_comment_btn).setVisibility(View.VISIBLE);
				}
			}
		});
	}
	
	public interface OnAuthStateChangeListener{
		public void onAuthSuccess();
		public void onAuthFailed();
		public void onAuthStart();
		public void onAuthNoWifi();
		public void onAuthNoNickName();
	}

	public void setEditHit(String str){
		hitStr = str;
	}
	
	public void setStep( int step ){
		this.mStep = step;
	}
	
	public void setSignState( boolean state ){
		this.mSignTxState = state;
	}
	
	public void setCommentEditTextState(boolean state){
		mCommentEdtState = state;
	}
}
