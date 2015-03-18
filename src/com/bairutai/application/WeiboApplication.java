package com.bairutai.application;

import com.bairutai.data.AccessTokenKeeper;
import com.bairutai.data.Constants;
import com.bairutai.sinaweibo.LogoActivity;
import com.bairutai.sinaweibo.MainActivity;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

public class WeiboApplication extends Application {
	 private Oauth2AccessToken mAccessToken;
	 private UsersAPI mUsersAPI;
	 private User user;
	 private boolean iscomplete;
	 
	public boolean getIscomplete() {
		return iscomplete;
	}

	public User getUser() {
		return user;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (mAccessToken.isSessionValid() && mAccessToken != null) {
            // 获取用户信息接口
            mUsersAPI = new UsersAPI(this, Constants.APP_KEY, mAccessToken);
            long uid = Long.parseLong(mAccessToken.getUid());
            mUsersAPI.show(uid, mListener);
            long[] uids = { Long.parseLong(mAccessToken.getUid()) };
            mUsersAPI.counts(uids, mListener);
        }
        Log.w("begin","aaaaaaaaaaaaaaa");
	}
	
	private RequestListener mListener = new RequestListener() {
		 public void onComplete(String response) {
	            if (!TextUtils.isEmpty(response)) {
	            	user = User.parse(response);
	            	System.out.println(user);
	            	iscomplete = true;
	            	Intent intent = new Intent(getApplicationContext(),MainActivity.class);
	            	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            	startActivity(intent);
	            }
		 }

		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
			
		}
	};
}
