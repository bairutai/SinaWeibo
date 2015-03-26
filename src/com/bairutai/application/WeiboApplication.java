package com.bairutai.application;


import java.util.ArrayList;

import com.sina.weibo.sdk.openapi.models.User;
import android.content.pm.PackageManager;
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
		
		ArrayList<AllAppInfo> list=GetAllAppInfo();
	}

	private ArrayList<AllAppInfo> GetAllAppInfo() {
		// TODO Auto-generated method stub
		return null;
	}
}
