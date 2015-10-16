package com.bairutai.sinaweibo;

import com.bairutai.Adapter.MyFanslistAdapter;
import com.bairutai.Service.MyService;
import com.bairutai.application.WeiboApplication;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MyFansListActivity extends Activity {
	
	//actionbar
	private View mView;
	private ImageView backBtn;
	private ImageView moreBtn;
	private TextView titleTxt;
	
	//pullRefreshScrollView
	private MyFanslistAdapter myFansListAdapter;
	private 	PullToRefreshListView mPullRefreshListView;
	private MyService mMyService;
	private WeiboApplication app;
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
		app = (WeiboApplication)getApplication();
		registerBoradcastReceiver();
		Intent  service=new Intent();
		service.setClass(MyFansListActivity.this, MyService.class);
		bindService(service, mServiceConnection, BIND_AUTO_CREATE);
		addListener();
	}

	private void findView() {
		// TODO Auto-generated method stub
		mPullRefreshListView = (PullToRefreshListView)findViewById(R.id.myfanslist_pull_refresh_listview);
		mPullRefreshListView.getLoadingLayoutProxy().setLoadingDrawable(getResources().getDrawable(R.drawable.navigationbar_icon_refresh_white));
		mPullRefreshListView.setRefreshing();
	}

	private void initScreen() {
		// TODO Auto-generated method stub
		myFansListAdapter = new MyFanslistAdapter(this, app);
		mPullRefreshListView.setAdapter(myFansListAdapter);
		mPullRefreshListView.onRefreshComplete();
	}

	private void addListener() {
		// TODO Auto-generated method stub
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	//这里需要用到ServiceConnection在Context.bindService和context.unBindService()里用到  
	private ServiceConnection mServiceConnection = new ServiceConnection() {  
		//bindService
		public void onServiceConnected(ComponentName name, IBinder service) {  
			// TODO Auto-generated method stub  
			mMyService = ((MyService.MyBinder)service).getService();  
			mMyService.getflowers();
		}  

		public void onServiceDisconnected(ComponentName name) {  
			// TODO Auto-generated method stub  
		}  
	};  
	private void registerBoradcastReceiver() {
		// TODO Auto-generated method stub
		IntentFilter myIntentFilter = new IntentFilter();  
		myIntentFilter.addAction("com.bairutai.MyFansListActivity");  
		//注册广播        
		registerReceiver(MyReceiver, myIntentFilter);  
	}
	
	private BroadcastReceiver MyReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			initScreen();
			unbindService(mServiceConnection);
			unregisterReceiver(MyReceiver);
		}
	};
}
