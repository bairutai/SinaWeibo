package com.bairutai.sinaweibo;

import com.bairutai.Adapter.MyFirstPageAdapter;
import com.bairutai.Service.MyService;
import com.bairutai.application.WeiboApplication;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MyFirstPageActivity extends Activity implements View.OnClickListener {
	//actionbar
	private ActionBar actionBar;
	private ImageView addBtn;
	private ImageView zxingBtn;
	private View mView;

	//popwindow
	private View view_this;
	private View view_zxing_menu;
	private LayoutInflater inflater;
	private LinearLayout zxingmenu_layout;
	private LinearLayout refreshmenu_layout;
	private PopupWindow zxing_share_pop;
	
	private MyService mMyService;
	
	//主界面
	private PullToRefreshListView mPullToReFreshListView;
	private WeiboApplication app;
	private View m_Empty_view;
	private ImageView empty_img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myfirstpage);
		app = (WeiboApplication)getApplication();
		findView();
		addListener();
		initScreen();
	}

	private void registerBoradcastReceiver() {
		// TODO Auto-generated method stub
		IntentFilter myIntentFilter = new IntentFilter();  
		myIntentFilter.addAction("com.bairutai.MyFirstPageActivity");  
		//注册广播        
		registerReceiver(MyReceiver, myIntentFilter);  
	}
	
	private BroadcastReceiver MyReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			mPullToReFreshListView.onRefreshComplete();
			initScreen();
			getApplicationContext().unbindService(mServiceConnection);
			unregisterReceiver(MyReceiver);
		}
	};
	
	private ServiceConnection mServiceConnection = new ServiceConnection() {  
		//bindService
		public void onServiceConnected(ComponentName name, IBinder service) {  
			// TODO Auto-generated method stub  
			System.out.println("myfirstpage getStatus");
			mMyService = ((MyService.MyBinder)service).getService();  
			mMyService.getStatus(app.getSince_id());
			System.out.println("myfirstpage getStatus");
		}  
		public void onServiceDisconnected(ComponentName name) {  
			// TODO Auto-generated method stub  
			System.out.println("myfirstpage connection failed");
		}  
	};
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		System.out.println("myfirstpage onresume");
		//这里要重写onResume的原因是切换tab的时候不会重新调用onCreate，是从onResume
		//开始调用的，所以需要重绘actionbar.
		setActionbar();
		super.onResume();
	}

	private void findView() {
		// TODO Auto-generated method stub
		inflater = LayoutInflater.from(this);
		view_this  = new View(this);
		view_zxing_menu = inflater.inflate(R.layout.zxing_popwindow, null);
		mView = inflater.inflate(R.layout.title_myfirstpage, null);
		m_Empty_view = inflater.inflate(R.layout.empty_view,null);
		zxingmenu_layout = (LinearLayout)view_zxing_menu.findViewById(R.id.zxing_popwindow_zxing_layout);
		refreshmenu_layout = (LinearLayout)view_zxing_menu.findViewById(R.id.zxing_popwindow_refresh_layout);
		addBtn = ( ImageView)mView.findViewById(R.id.title_myfirstpage_add);
		zxingBtn = (ImageView)mView.findViewById(R.id.title_myfirstpage_zxing);
		empty_img = (ImageView)m_Empty_view.findViewById(R.id.empty_view_img);
		empty_img.setImageDrawable(getResources().getDrawable(R.drawable.empty_default));
		mPullToReFreshListView = (PullToRefreshListView)findViewById(R.id.myfirstpage_pull_refresh_listview);
		mPullToReFreshListView.setLoadingDrawable(getResources().getDrawable(R.drawable.navigationbar_icon_refresh_white));
		mPullToReFreshListView.setEmptyView(m_Empty_view);
	}

	private void initScreen() {
		// TODO Auto-generated method stub
		MyFirstPageAdapter myFirstPageAdapter = new MyFirstPageAdapter(this, app);
		mPullToReFreshListView.setAdapter(myFirstPageAdapter);
	}

	private void addListener() {
		// TODO Auto-generated method stub
		zxingBtn.setOnClickListener(this);
		zxingmenu_layout.setOnClickListener(this);
		refreshmenu_layout.setOnClickListener(this);
		mPullToReFreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				registerBoradcastReceiver();
				Intent  service=new Intent();
				service.setClass(MyFirstPageActivity.this, MyService.class);
				getApplicationContext().bindService(service, mServiceConnection, BIND_AUTO_CREATE);
			}
		});
		

	}

	private void setActionbar() {
		// TODO Auto-generated method stub
		actionBar = getParent().getActionBar();
		if (null != actionBar) {
			System.out.println("myfirstpage actionbar is display");
			actionBar.setDisplayShowHomeEnabled(false);//去左上角LOGO
			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
			actionBar.setDisplayShowCustomEnabled(true);
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			actionBar.setCustomView(mView, layout);//设置actionbar视图
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_myfirstpage_zxing:
			zxing_share_pop = new PopupWindow(view_zxing_menu, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			DisplayMetrics dm = new DisplayMetrics();
			getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
			int x =dm.widthPixels-view_zxing_menu.getWidth();
			Rect frame = new Rect();
			getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
			int statusBarHeight = frame.top;
			//如果设置为actionbar的高度，位置显示错误。这里actionvar的高度加上状态栏的高度，
			int y = mView.getHeight()+statusBarHeight;
			zxing_share_pop.setFocusable(true);
			zxing_share_pop.setBackgroundDrawable(new BitmapDrawable());
			zxing_share_pop.setOutsideTouchable(true);
			zxing_share_pop.showAsDropDown(view_this, x, y);
			zxing_share_pop.update();
			break;
		case R.id.zxing_popwindow_zxing_layout:
			if(zxing_share_pop.isShowing()){
				zxing_share_pop.dismiss();
				Intent intent = new Intent();
				intent.setClass(MyFirstPageActivity.this, MipcaActivityCapture.class);
				startActivity(intent);	
			}
		case R.id.zxing_popwindow_refresh_layout:
			mPullToReFreshListView.setRefreshing();
			zxing_share_pop.dismiss();
			registerBoradcastReceiver();
			Intent  service=new Intent();
			service.setClass(MyFirstPageActivity.this, MyService.class);
			getApplicationContext().bindService(service, mServiceConnection, BIND_AUTO_CREATE);
		default:
			break;
		}
	}
}
