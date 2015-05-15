package com.bairutai.sinaweibo;

import com.bairutai.Service.MyService;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class MyFansListActivity extends Activity {
	
	//actionbar
	private View mView;
	private ImageView backBtn;
	private ImageView moreBtn;
	private TextView titleTxt;
	
	//pullRefreshScrollView
	private 	PullToRefreshScrollView mPullRefreshScrollView;
	private ListView myFanslistView;
	private MyService mMyService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myfanslist);
		ActionBar actionBar = getActionBar();
		if (null != actionBar) {
			actionBar.setDisplayShowHomeEnabled(false);//返回键
			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
			actionBar.setDisplayShowCustomEnabled(true);
			LayoutInflater inflator = LayoutInflater.from(this);
			mView = inflator.inflate(R.layout.title_mycard, null);//自定义actionbar视图
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			actionBar.setCustomView(mView, layout);//设置actionbar视图
			backBtn = ( ImageView)findViewById(R.id.title_mycard_back);
			moreBtn = (ImageView)findViewById(R.id.title_mycard_more);
			moreBtn.setVisibility(View.INVISIBLE);
			titleTxt = (TextView)findViewById(R.id.title_mycard_mycard);
			titleTxt.setText("粉丝");
		}
		
		findView();
		initScreen();
		Intent  service=new Intent();
		service.setClass(MyFansListActivity.this, MyService.class);
		bindService(service, mServiceConnection, BIND_AUTO_CREATE);
		addListener();
	}

	private void findView() {
		// TODO Auto-generated method stub
		mPullRefreshScrollView = (PullToRefreshScrollView)findViewById(R.id.myfanslist_pull_refresh_scrollview);
		myFanslistView = (ListView)findViewById(R.id.myfanslist_listview);
	}

	private void initScreen() {
		// TODO Auto-generated method stub
		mPullRefreshScrollView.setLoadingDrawable(getResources().getDrawable(R.drawable.navigationbar_icon_refresh_white));
	}

	private void addListener() {
		// TODO Auto-generated method stub

		mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> arg0) {
				// TODO Auto-generated method stub
				mPullRefreshScrollView.setRefreshing();
			}			
		});
	}
	
	//这里需要用到ServiceConnection在Context.bindService和context.unBindService()里用到  
	private ServiceConnection mServiceConnection = new ServiceConnection() {  
		//bindService
		public void onServiceConnected(ComponentName name, IBinder service) {  
			// TODO Auto-generated method stub  
			mMyService = ((MyService.MyBinder)service).getService();  
			mMyService.getfriends();
		}  

		public void onServiceDisconnected(ComponentName name) {  
			// TODO Auto-generated method stub  
		}  
	};  
}
