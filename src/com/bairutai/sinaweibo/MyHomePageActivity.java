package com.bairutai.sinaweibo;

import com.bairutai.application.WeiboApplication;
import com.bairutai.openwidget.PullScrollView;
import com.sina.weibo.sdk.openapi.models.User;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MyHomePageActivity extends Activity implements PullScrollView.OnTurnListener{

	private PullScrollView mScrollView;
	private ImageView mHeadImg;
	private TextView mdetailTextView;
	private TextView m_flower_count;
	private TextView m_guanzhu_count;
	private WeiboApplication app;
	private User user;
	private RelativeLayout homepage_jianjie_layout;
	private ImageView backBtn;
	private ImageView moreBtn;
	private ImageView searchBtn;
	private View mView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);  
		setContentView(R.layout.my_homepage);

		ActionBar actionBar = getActionBar();
		if (null != actionBar) {
			actionBar.setDisplayShowHomeEnabled(false);//返回键
			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
			actionBar.setDisplayShowCustomEnabled(true);
			actionBar.setBackgroundDrawable(new ColorDrawable(R.color.transparent));
			LayoutInflater inflator = LayoutInflater.from(this);
			mView = inflator.inflate(R.layout.title_myhomepage, null);//自定义actionbar视图
			mView.getBackground().setAlpha(0);
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			actionBar.setCustomView(mView, layout);//设置actionbar视图
			backBtn = ( ImageView)findViewById(R.id.title_myhomepage_back);
			moreBtn = (ImageView)findViewById(R.id.title_myhomepage_more);
			searchBtn = (ImageView)findViewById(R.id.title_myhomepage_search);
		}

		app = (WeiboApplication)getApplication();
		user = app.getUser();
		initView();
	}

	protected void initView() {
		mScrollView = (PullScrollView) findViewById(R.id.scroll_view);
		mHeadImg = (ImageView) findViewById(R.id.background_img);
		mdetailTextView = (TextView)findViewById(R.id.expandable_text);
		m_flower_count = (TextView)findViewById(R.id.homepage_flower_count);
		m_guanzhu_count = (TextView)findViewById(R.id.homepage_guanzhu_count);
		homepage_jianjie_layout = (RelativeLayout)findViewById(R.id.homepage_jianjie_layout);
		mdetailTextView.setText(user.description);
		m_flower_count.setText(String.valueOf(user.followers_count));
		m_guanzhu_count.setText(String.valueOf(user.friends_count));
		mScrollView.setHeader(mHeadImg);
		mScrollView.setOnTurnListener(this);
		homepage_jianjie_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MyHomePageActivity.this, MyBaseInfoActivity.class));
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

	@Override
	public void onTurn() {
		// TODO Auto-generated method stub
		moreBtn.setBackground(getResources().getDrawable(R.drawable.searchbar_textfield_search_icon));
	}
}
