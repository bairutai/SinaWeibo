package com.bairutai.sinaweibo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.bairutai.sinaweibo.MyrefreshDialog.ClickListenerInterface;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class WebExploreActivity extends Activity {

	//actionbar
	private View mView;
	private ImageView backBtn;
	private ImageView moreBtn;
	private TextView titleTxt;

	//主界面
	private WebView goodname_web;

	//more dialog
	private MyrefreshDialog myrefreshDialog;
	private int progress;
	private String url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webexplore);
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
		url = getIntent().getStringExtra("url");
		WebSettings settings = goodname_web.getSettings();
		settings.setJavaScriptEnabled(true);
		goodname_web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		goodname_web.loadUrl(url);
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
					progress = 100;
				}
				super.onProgressChanged(view, newProgress);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				// TODO Auto-generated method stub
				super.onReceivedTitle(view, title);
				if(progress == 100){
					titleTxt.setText(title);
				}else {
					titleTxt.setText(" ");
				}
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
				myrefreshDialog = new MyrefreshDialog(WebExploreActivity.this, data);
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
						myrefreshDialog.dismiss();
						goodname_web.reload();
					}

					@Override
					public void docancle() {
						// TODO Auto-generated method stub
						myrefreshDialog.dismiss();
					}

					@Override
					public void doback() {
						// TODO Auto-generated method stub
						myrefreshDialog.dismiss();
						Intent intent = new Intent(WebExploreActivity.this, MainActivity.class);
						intent.putExtra("currentpage", 0);
						startActivity(intent);
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

	private void callHiddenWebViewMethod(String name)
	{
		if (goodname_web != null)
		{
			try
			{
				Method method = WebView.class.getMethod(name);
				method.invoke(goodname_web);
			}
			catch (NoSuchMethodException e)
			{
				Log.i("No such method: " + name, e.toString());
			}
			catch (IllegalAccessException e)
			{
				Log.i("Illegal Access: " + name, e.toString());
			}
			catch (InvocationTargetException e)
			{
				Log.d("Invocation Target Exception: " + name, e.toString());
			}
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		goodname_web.pauseTimers();

		if (isFinishing())
		{
			goodname_web.loadUrl("about:blank");
			setContentView(new FrameLayout(this));
		}
		callHiddenWebViewMethod("onPause");
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		goodname_web.resumeTimers();
		callHiddenWebViewMethod("onResume");
	}
}
