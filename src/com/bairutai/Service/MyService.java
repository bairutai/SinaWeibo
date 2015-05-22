package com.bairutai.Service;

import com.bairutai.application.WeiboApplication;
import com.bairutai.model.Friend;
import com.bairutai.tools.AccessTokenKeeper;
import com.bairutai.data.Constants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.openapi.legacy.FriendshipsAPI;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

public class MyService extends Service {

	private UsersAPI mUsersAPI;
	private FriendshipsAPI mFriendshipsAPI;
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
		System.out.print(mAccessToken);
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
						if(app.getmDataBase() == null) {
							System.out.println("database2 instance is null");
						}else{
							System.out.println("database2 instance is not null");
							app.getmDataBase().addUser(user);
						}
						Intent sendIntent = new Intent();
						sendIntent.setAction("com.bairutai.LogoActivity");
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

	public void getfriends() {
		mAccessToken = AccessTokenKeeper.readAccessToken(this);
		if (null == mAccessToken){
			Log.d("accesstokenl","is null" );
			stopSelf();
		}	
		mFriendshipsAPI = new FriendshipsAPI(this, Constants.APP_KEY, mAccessToken);
		app = (WeiboApplication)getApplication();
		long uid = Long.parseLong(mAccessToken.getUid());
		int cursor = 0;
		mFriendshipsAPI.followers(uid, 44, cursor, false, mFriendslistener);
		Log.d("listener","is value" );
	}

	private RequestListener mFriendslistener = new RequestListener() {

		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
			Log.d("exception", "exception" );
		}

		@Override
		public void onComplete(String reponse) {
			// TODO Auto-generated method stub
			if (!TextUtils.isEmpty(reponse)) {
				Log.d("reponse", reponse);
				// 调用 User#parse 将JSON串解析成User对象
				try {
					int back = Friend.parse(reponse, app);
					if (back != 0) {
						System.out.println("the return back is 1");
						Intent sendIntent = new Intent();
						sendIntent.setAction("com.bairutai.MyFansListActivity");
						sendBroadcast(sendIntent);
					} else {
						stopSelf();
					}
				}catch(WeiboException e){
					e.printStackTrace();
				}
			}
		}
	};

	public class MyBinder extends Binder {
		public MyService getService() {
			return MyService.this;
		}
	}
}
