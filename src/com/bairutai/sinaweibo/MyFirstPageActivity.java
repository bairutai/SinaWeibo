package com.bairutai.sinaweibo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

public class MyFirstPageActivity extends Activity implements View.OnClickListener{
	//actionbar
	private ActionBar actionBar;
	private ImageView addBtn;
	private ImageView zxingBtn;
	private View mView;
	
	
	private View view_this;
	private View view_zxing_menu;
	private LayoutInflater inflater;
	private LinearLayout zxingmenu_layout;
	private LinearLayout refreshmenu_layout;

	
	private PopupWindow zxing_share_pop;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myfirstpage);
		view_this  = new View(this);
		inflater = LayoutInflater.from(this);
		view_zxing_menu = inflater.inflate(R.layout.zxing_popwindow, null);
		zxingmenu_layout = (LinearLayout)view_zxing_menu.findViewById(R.id.zxing_popwindow_zxing_layout);
		refreshmenu_layout = (LinearLayout)view_zxing_menu.findViewById(R.id.zxing_popwindow_refresh_layout);
		setActionbar();
		findView();
		initScreen();
		addListener();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		System.out.println("myfirstpage onresume");
		//这里要重写onResume的原因是切换tab的时候不会重新调用onCreate，是从onResume
		//开始调用的，所以需要重绘actionbar.
		setActionbar();
		addListener();
		super.onResume();
	}
	
	private void findView() {
		// TODO Auto-generated method stub
		
	}

	private void initScreen() {
		// TODO Auto-generated method stub
	}

	private void addListener() {
		// TODO Auto-generated method stub
		zxingBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
			}
		});
		
		zxingmenu_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(zxing_share_pop.isShowing()){
					zxing_share_pop.dismiss();
					startActivity(new Intent(MyFirstPageActivity.this, MipcaActivityCapture.class));
				}
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
			LayoutInflater inflator = LayoutInflater.from(this);
			mView = inflator.inflate(R.layout.title_myfirstpage, null);//自定义actionbar视图
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			actionBar.setCustomView(mView, layout);//设置actionbar视图
			addBtn = ( ImageView)mView.findViewById(R.id.title_myfirstpage_add);
			zxingBtn = (ImageView)mView.findViewById(R.id.title_myfirstpage_zxing);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.zxing_popwindow_zxing_layout :
			startActivity(new Intent(MyFirstPageActivity.this, MipcaActivityCapture.class));
		}
	}

}
