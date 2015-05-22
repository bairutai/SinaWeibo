package com.bairutai.application;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bairutai.database.DataBase;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.models.User;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.app.Application;


public class WeiboApplication extends Application {
	private User user;
	private Oauth2AccessToken mAccessToken;
	private ArrayList<AllAppInfo> list;
	private DataBase mDataBase;
	private DataBase mfriendDataBase;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		list=GetAllAppInfo();
		setList(list);
		
		mDataBase = new DataBase(getApplicationContext(), "user", 1);
		mfriendDataBase = new DataBase(getApplicationContext(), "flower", 1);
	}
	
	public DataBase getMfriendDataBase() {
		if (mfriendDataBase == null) {
			System.out.println("mfriendDataBase is null ");
		}
		return mfriendDataBase;
	}

	public void setMfriendDataBase(DataBase mfriendDataBase) {
		this.mfriendDataBase = mfriendDataBase;
	}

	public Oauth2AccessToken getmAccessToken() {
		return mAccessToken;
	}

	public void setmAccessToken(Oauth2AccessToken mAccessToken) {
		this.mAccessToken = mAccessToken;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		if (mDataBase.queryUser() == null){
			return user;
		}else {
			System.out.println("database user is value");
			return mDataBase.queryUser();
		}

	}

	public ArrayList<AllAppInfo> getList() {
		return list;
	}

	public void setList(ArrayList<AllAppInfo> list) {
		this.list = list;
	}
	
	public DataBase getmDataBase() {
		if(null == mDataBase) {
			System.out.println("database instance is null");
			return null;
		}
		System.out.println("database instance is not null");
		return mDataBase;
	}

	public void setmDataBase(DataBase mDataBase) {
		this.mDataBase = mDataBase;
	}

	private ArrayList<AllAppInfo> GetAllAppInfo() {
		// TODO Auto-generated method stub
		ArrayList<AllAppInfo> appList = new ArrayList<AllAppInfo>();  
		List<PackageInfo> packageInfos=getPackageManager().getInstalledPackages(0);
		for (int i = 0; i < packageInfos.size(); i++) {
			PackageInfo pInfo=packageInfos.get(i);
			AllAppInfo allAppInfo=new AllAppInfo();
			allAppInfo.setAppname(pInfo.applicationInfo.loadLabel(getPackageManager()).toString());//应用程序的名称
			allAppInfo.setPackagename(pInfo.packageName);//应用程序的包
			allAppInfo.setVersionCode(pInfo.versionCode);//版本号
			allAppInfo.setLastInstal(pInfo.firstInstallTime);
			//allAppInfo.setProvider(pInfo.providers);
			allAppInfo.setInstalPath(pInfo.applicationInfo.sourceDir);
			allAppInfo.setAppicon(pInfo.applicationInfo.loadIcon(getPackageManager()));
			appList.add(allAppInfo);
		}
		return appList;
	}
}
