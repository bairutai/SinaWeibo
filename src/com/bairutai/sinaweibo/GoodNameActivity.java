package com.bairutai.sinaweibo;

import java.util.ArrayList;
import java.util.List;

import com.bairutai.sinaweibo.MyrefreshDialog.ClickListenerInterface;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

public class GoodNameActivity extends Activity {

	//actionbar
	private View mView;
	private ImageView backBtn;
	private ImageView moreBtn;
	private TextView titleTxt;

	//主界面
	private WebView goodname_web;
	
	//more dialog
	private MyrefreshDialog myrefreshDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goodname);
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
			titleTxt = (TextView)findViewById(R.id.title_mycard_mycard);
			titleTxt.setText("加载中...");
		}

		findView();
		initScreen();
		addListener();
	}

	private void findView() {
		// TODO Auto-generated method stub
		goodname_web = (WebView)findViewById(R.id.goodname_web);
	}

	private void initScreen() {
		// TODO Auto-generated method stub
		WebSettings settings = goodname_web.getSettings();
		settings.setJavaScriptEnabled(true);
		goodname_web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		goodname_web.loadUrl("http://weibo.com/a/nickname/index");
		goodname_web.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}
		});

		goodname_web.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				if (newProgress == 100) {
					titleTxt.setText("抢占微博好昵称");
				}
				super.onProgressChanged(view, newProgress);
			}	
		});
	}
	
	private void addListener() {
		// TODO Auto-generated method stub
		moreBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				List<String> data = new ArrayList<String>();
				data.add("刷新");
				data.add("返回首页");
				data.add("浏览器打开");
				myrefreshDialog = new MyrefreshDialog(GoodNameActivity.this, data);
				myrefreshDialog.setCanceledOnTouchOutside(true);
				myrefreshDialog.show();
				myrefreshDialog.setClicklistener(new ClickListenerInterface() {
					
					@Override
					public void doyourthing() {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void dorefresh() {
						// TODO Auto-generated method stub
						myrefreshDialog.cancel();
						goodname_web.reload();
					}
					
					@Override
					public void docancle() {
						// TODO Auto-generated method stub
						myrefreshDialog.cancel();
					}
					
					@Override
					public void doback() {
						// TODO Auto-generated method stub
						startActivity(new Intent(GoodNameActivity.this, MainActivity.class));
						finish();
					}
				});
			}
		});
		
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

}
