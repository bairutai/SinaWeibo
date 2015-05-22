package com.bairutai.sinaweibo;

import java.io.Serializable;

import android.app.TabActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	private TabHost tabHost;
	private RadioButton firstpageButton;
	private RadioButton messageButton;
	private RadioButton addButton;
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
		addButton = (RadioButton)findViewById(R.id.main_radio_addbutton);
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


		
		firstpageButton.setChecked(true);
		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.main_radio_firstpagebutton:// 首页
					tabHost.setCurrentTabByTag("首页");
					break;
				case R.id.main_radio_messagebutton:// 信息
					tabHost.setCurrentTabByTag("信息");
					break;
				case R.id.main_radio_addbutton:// +
					tabHost.setCurrentTabByTag("添加");
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}
}

