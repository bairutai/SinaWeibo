package com.bairutai.application;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.models.User;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.app.Application;


public class WeiboApplication extends Application {
	private User user;
	private Oauth2AccessToken mAccessToken;
	private ArrayList<AllAppInfo> list;
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

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		list=GetAllAppInfo();
		setList(list);
//		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
//			AllAppInfo allAppInfo=(AllAppInfo) list.iterator();
//			String appname=allAppInfo.getAppname();
//			if (appname.equalsIgnoreCase("微信")) {
//				
//			}
//		}
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
