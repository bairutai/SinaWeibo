package com.bairutai.sinaweibo;

/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.text.SimpleDateFormat;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.bairutai.application.WeiboApplication;
import com.bairutai.data.Constants;
import com.bairutai.data.AccessTokenKeeper;;
/**
 * 该类主要演示如何进行授权、SSO登陆。
 * 
 * @author SINA
 * @since 2013-09-29
 */
public class WBAuthActivity extends Activity {

	private static final String TAG = "weibosdk";
	private WeiboApplication app;
	private MyService mService;

	/** 显示认证后的信息，如 AccessToken */
	private TextView mTokenText;

	private AuthInfo mAuthInfo;

	/** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
	private Oauth2AccessToken mAccessToken;

	/** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
	private SsoHandler mSsoHandler;

	/**
	 * @see {@link Activity#onCreate}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth);

		app = (WeiboApplication)this.getApplication();
		// 获取 Token View，并让提示 View 的内容可滚动（小屏幕可能显示不全）
		mTokenText = (TextView) findViewById(R.id.token_text_view);
		TextView hintView = (TextView) findViewById(R.id.obtain_token_hint);
		hintView.setMovementMethod(new ScrollingMovementMethod());

		// 创建微博实例
		//mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
		// 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
		mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
		mSsoHandler = new SsoHandler(WBAuthActivity.this, mAuthInfo);

		// SSO 授权, 仅客户端
		findViewById(R.id.obtain_token_via_sso).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSsoHandler.authorizeClientSso(new AuthListener());
			}
		});

		// SSO 授权, 仅Web
		findViewById(R.id.obtain_token_via_web).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSsoHandler.authorizeWeb(new AuthListener());
			}
		});

		// SSO 授权, ALL IN ONE
		findViewById(R.id.obtain_token_via_signature).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSsoHandler.authorize(new AuthListener());
			}
		});

		// 用户登出
		findViewById(R.id.logout).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AccessTokenKeeper.clear(getApplicationContext());
				mAccessToken = new Oauth2AccessToken();
				updateTokenView(false);
			}
		});



		// 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息，
		// 第一次启动本应用，AccessToken 不可用
		mAccessToken = AccessTokenKeeper.readAccessToken(this);
		if (mAccessToken.isSessionValid()) {
			updateTokenView(true);
		}

	}

	/**
	 * 当 SSO 授权 Activity 退出时，该函数被调用。
	 * 
	 * @see {@link Activity#onActivityResult}
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// SSO 授权回调
		// 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	/**
	 * 微博认证授权回调类。
	 * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
	 *    该回调才会被执行。
	 * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
	 * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
	 */
	class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			// 从 Bundle 中解析 Token
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				// 显示 Token
				updateTokenView(false);                
				// 保存 Token 到 SharedPreferences
				AccessTokenKeeper.writeAccessToken(WBAuthActivity.this, mAccessToken);
				Toast.makeText(WBAuthActivity.this, 
						R.string.weibosdk_demo_toast_auth_success, Toast.LENGTH_SHORT).show();
				app.setmAccessToken(mAccessToken);
				registerBoradcastReceiver();  
				Intent  service=new Intent();
				service.setClass(WBAuthActivity.this, MyService.class);
				bindService(service, mServiceConnection, BIND_AUTO_CREATE);
			} else {
				// 以下几种情况，您会收到 Code：
				// 1. 当您未在平台上注册的应用程序的包名与签名时；
				// 2. 当您注册的应用程序包名与签名不正确时；
				// 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
				String code = values.getString("code");
				String message = getString(R.string.weibosdk_demo_toast_auth_failed);
				if (!TextUtils.isEmpty(code)) {
					message = message + "\nObtained the code: " + code;
				}
				Toast.makeText(WBAuthActivity.this, message, Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onCancel() {
			Toast.makeText(WBAuthActivity.this, 
					R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(WBAuthActivity.this, 
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 显示当前 Token 信息。
	 * 
	 * @param hasExisted 配置文件中是否已存在 token 信息并且合法
	 */
	private void updateTokenView(boolean hasExisted) {
		String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
				new java.util.Date(mAccessToken.getExpiresTime()));
		String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
		mTokenText.setText(String.format(format, mAccessToken.getToken(), date));

		String message = String.format(format, mAccessToken.getToken(), date);
		if (hasExisted) {
			message = getString(R.string.weibosdk_demo_token_has_existed) + "\n" + message;
		}
		mTokenText.setText(message);
	}

	private void registerBoradcastReceiver() {
		// TODO Auto-generated method stub
		IntentFilter myIntentFilter = new IntentFilter();  
		myIntentFilter.addAction("com.bairutai.MyContactActivity");  
		//注册广播        
		registerReceiver(MyReceiver, myIntentFilter);  
	}

	private BroadcastReceiver MyReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Intent intent_new = new Intent(WBAuthActivity.this,MainActivity.class);
			startActivity(intent_new);
			unbindService(mServiceConnection);
			unregisterReceiver(MyReceiver);
			finish();
		}
	};
	  //这里需要用到ServiceConnection在Context.bindService和context.unBindService()里用到  
	  private ServiceConnection mServiceConnection = new ServiceConnection() {  
	      //bindService
	      public void onServiceConnected(ComponentName name, IBinder service) {  
	          // TODO Auto-generated method stub  
	          mService = ((MyService.MyBinder)service).getService();  
	          mService.getUser();
	      }  
	        
	      public void onServiceDisconnected(ComponentName name) {  
	          // TODO Auto-generated method stub  
	      }  
	  };  
}