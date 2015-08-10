package com.bairutai.sinaweibo;

import android.R.integer;
import android.app.TabActivity;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	private TabHost tabHost;
	private RadioButton firstpageButton;
	private RadioButton messageButton;
	private ImageButton addButton;
	private RadioButton searchButton;
	private RadioButton myButton;
	private RadioGroup radioGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		firstpageButton = (RadioButton)findViewById(R.id.main_radio_firstpagebutton);
		messageButton = (RadioButton)findViewById(R.id.main_radio_messagebutton);
		addButton = (ImageButton)findViewById(R.id.main_radio_addbutton);
		searchButton = (RadioButton)findViewById(R.id.main_radio_searchbutton);
		myButton = (RadioButton)findViewById(R.id.main_radio_mybutton);
		radioGroup = (RadioGroup)findViewById(R.id.main_radio);
		
		tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		
		intent = new Intent().setClass(this, MyFirstPageActivity.class);
		spec = tabHost.newTabSpec("首页").setIndicator("首页").setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, MyContactActivity.class);
		spec = tabHost.newTabSpec("资料").setIndicator("资料").setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, MyMessageActivity.class);
		spec = tabHost.newTabSpec("信息").setIndicator("信息").setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, SearchActivity.class);
		spec = tabHost.newTabSpec("搜索").setIndicator("搜索").setContent(intent);
		tabHost.addTab(spec);
		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.main_radio_firstpagebutton:// 首页
					tabHost.setCurrentTabByTag("首页");
					if(null != MyFirstPageActivity.mPullToReFreshListView){
						MyFirstPageActivity.mPullToReFreshListView.setRefreshing();
					}
					break;
				case R.id.main_radio_messagebutton:// 信息
					tabHost.setCurrentTabByTag("信息");
					if(null != MyMessageActivity.listView){
						MyMessageActivity.listView.setRefreshing();
					}
					break;
				case R.id.main_radio_searchbutton:// 搜索
					tabHost.setCurrentTabByTag("搜索");
					break;
				case R.id.main_radio_mybutton:// 资料
					tabHost.setCurrentTabByTag("资料");
					break;
				default:
					tabHost.setCurrentTabByTag("首页");
					break;
				}
			}
		});
		
		addButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, AddActivity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out); 
			}
		});
		
		if(getIntent() != null){
			setCurrentPage(getIntent().getIntExtra("currentpage", 0));                                                                                          
		}
	}
	
	

	public void setCurrentPage(int index) {
		if(tabHost != null){
			tabHost.setCurrentTab(index);
		}
		switch (index) {
		case 0:
			firstpageButton.setChecked(true);
			break;
		case 1:
			messageButton.setChecked(true);
			break;
		case 3:
			searchButton.setChecked(true);
			break;
		case 4:
			myButton.setChecked(true);
			break;
		default:
			break;
		}
	}

	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		if(intent != null){
			setCurrentPage(intent.getIntExtra("currentpage", 0));
		}
		super.onNewIntent(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

}

