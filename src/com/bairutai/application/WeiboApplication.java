package com.bairutai.application;


import java.util.ArrayList;
import java.util.List;

import com.bairutai.database.DataBaseHelper;
import com.bairutai.model.User;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

import android.content.pm.PackageInfo;
import android.app.Application;


public class WeiboApplication extends Application {
	private User user;
	private Status status;
	private StatusList statusList;
	private Oauth2AccessToken mAccessToken;
	private ArrayList<AllAppInfo> list;
	public DataBaseHelper mDataBaseHelper;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		list=GetAllAppInfo();
		setList(list);
		mDataBaseHelper = new DataBaseHelper(getApplicationContext());
	}
	
	public StatusList getStatusList() {
		return statusList;
	}

	public void setStatusList(StatusList statusList) {
		this.statusList = statusList;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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
			return user;
	}

	public ArrayList<AllAppInfo> getList() {
		return list;
	}

	public void setList(ArrayList<AllAppInfo> list) {
		this.list = list;
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
