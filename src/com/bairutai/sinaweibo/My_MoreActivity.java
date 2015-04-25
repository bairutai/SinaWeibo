package com.bairutai.sinaweibo;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class My_MoreActivity extends Activity {
	private RelativeLayout my_more_layout_mycard;
	private Button backBtn;
	private Button moreBtn;
	private View mView;
	private TextView titleTxt;
	private 	PullToRefreshScrollView mPullRefreshScrollView;
	private ScrollView mScrollView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_more);
		
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
			backBtn = ( Button)findViewById(R.id.title_mycard_back);
			moreBtn = (Button)findViewById(R.id.title_mycard_more);
			titleTxt = (TextView)findViewById(R.id.title_mycard_mycard);
			titleTxt.setText("更多");
		}	
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.my_more_pull_refresh_scrollview);
		mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> arg0) {
				// TODO Auto-generated method stub
				mPullRefreshScrollView.onRefreshComplete();
			}
			
		});
		mScrollView = mPullRefreshScrollView.getRefreshableView();

		my_more_layout_mycard = (RelativeLayout)this.findViewById(R.id.my_more_layout_mycard);
		my_more_layout_mycard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(My_MoreActivity.this,MycardActivity.class));
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
