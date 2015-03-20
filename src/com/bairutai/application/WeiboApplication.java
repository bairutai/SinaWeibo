package com.bairutai.application;


import com.sina.weibo.sdk.openapi.models.User;

import android.app.Application;


public class WeiboApplication extends Application {
	 private User user;

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
}
