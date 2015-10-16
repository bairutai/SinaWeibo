package com.bairutai.sinaweibo;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.app.Activity;
import android.os.Bundle;

public class MyFriendsListActivity extends Activity {
	
	private 	PullToRefreshListView mPullRefreshListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myfanslist);
	}
}
