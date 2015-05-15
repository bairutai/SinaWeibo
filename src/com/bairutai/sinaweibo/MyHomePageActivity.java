package com.bairutai.sinaweibo;

import com.bairutai.application.WeiboApplication;
import com.bairutai.openwidget.CircleImageView;
import com.bairutai.openwidget.PullScrollView;
import com.bairutai.tools.AsyncBitmapLoader;
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
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MyHomePageActivity extends Activity implements PullScrollView.OnTurnListener{

	//主界面
	private PullScrollView mScrollView;
	private ImageView mHeadImg;
	private TextView mdetailTextView;
	private LinearLayout homepage_jianjie_layout;
	private TextView homepage_username_txt;
	private CircleImageView homepage_circleImageView;
	
	//粉丝数
	private TextView homepage_flower;
	private TextView homepage_guanzhu;
	
	//actionbar
	private ImageView backBtn;
	private ImageView moreBtn;
	private ImageView searchBtn;
	private View mView;
	
	//获取数据的临时方法
	private WeiboApplication app;
	private User user;

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

		findView();
		initScreen();
		addListener();

	}

	private void findView() {
		// TODO Auto-generated method stub
		mScrollView = (PullScrollView) findViewById(R.id.scroll_view);
		mHeadImg = (ImageView) findViewById(R.id.background_img);
		mdetailTextView = (TextView)findViewById(R.id.expandable_text);
		homepage_jianjie_layout = (LinearLayout)findViewById(R.id.homepage_jianjie_layout);
		homepage_username_txt = (TextView)findViewById(R.id.myhomepage_username_txt);
		homepage_circleImageView = (CircleImageView)findViewById(R.id.myhomepage_circleImageView);
		
		homepage_guanzhu = (TextView)findViewById(R.id.homepage_guanzhu);
		homepage_flower = (TextView)findViewById(R.id.homepage_flower);
	}

	protected void initScreen() {
		app = (WeiboApplication)getApplication();
		user = app.getUser();
		mdetailTextView.setText(user.description);
		homepage_flower.append(String.valueOf(user.followers_count));
		homepage_guanzhu.append(String.valueOf(user.friends_count));
		mScrollView.setHeader(mHeadImg);
		mScrollView.setOnTurnListener(this);
		homepage_username_txt.setText(user.screen_name);
		new AsyncBitmapLoader().execute(homepage_circleImageView, user.profile_image_url);
	}
	
	private void addListener() {
		// TODO Auto-generated method stub
		homepage_flower.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MyHomePageActivity.this, MyFansListActivity.class));
			}
		});
		
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
		
		/**
		 * scrollview 拉动时设置actionbar上的more为加载动画
		 * 这里我设置成50秒转36000度，这样卡顿的效果基本看不出来
		 */
		
		moreBtn.setImageDrawable(getResources().getDrawable(R.drawable.navigationbar_icon_refresh_white));
		RotateAnimation rotateAnimation = new RotateAnimation(0f,36000f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f );
		LinearInterpolator lir = new LinearInterpolator();
		rotateAnimation.setInterpolator(lir);//设置匀速转动
		rotateAnimation.setDuration(50000);
		rotateAnimation.setRepeatCount(1);//设置重复次数
		moreBtn.startAnimation(rotateAnimation);
		
		rotateAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				moreBtn.setImageDrawable(getResources().getDrawable(R.drawable.more_become_light));
			}
		});
	}
}
