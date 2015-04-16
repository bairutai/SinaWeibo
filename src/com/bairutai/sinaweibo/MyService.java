package com.bairutai.sinaweibo;

import com.bairutai.application.WeiboApplication;
import com.bairutai.data.AccessTokenKeeper;
import com.bairutai.data.Constants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

public class MyService extends Service {

	private UsersAPI mUsersAPI;
	private Oauth2AccessToken mAccessToken;
	private WeiboApplication app;
	private MyBinder mBinder = new MyBinder();
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}


	public void getUser(){
		mAccessToken = AccessTokenKeeper.readAccessToken(this);
		if (null == mAccessToken){
			stopSelf();
		}		
		mUsersAPI = new UsersAPI(this, Constants.APP_KEY, mAccessToken);
		app = (WeiboApplication)getApplication();
		long uid = Long.parseLong(mAccessToken.getUid());
		mUsersAPI.show(uid, mListener);
		long[] uids = { Long.parseLong(mAccessToken.getUid()) };
		mUsersAPI.counts(uids, mListener);
	}

	private RequestListener mListener = new RequestListener() {
		@Override
		public void onComplete(String response) {
			if (!TextUtils.isEmpty(response)) {
				// 调用 User#parse 将JSON串解析成User对象
				try {

					User user = User.parse(response);
					if (user != null) {
						app.setUser(user);
						Intent sendIntent = new Intent();
						sendIntent.setAction("com.bairutai.MyContactActivity");
						sendBroadcast(sendIntent);
					} else {
						stopSelf();
					}
				}catch(WeiboException e){
					e.printStackTrace();
				}
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
		}
	};
	public class MyBinder extends Binder {
		MyService getService() {
			return MyService.this;
		}
	}
}
