package com.bairutai.sinaweibo;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class My_MoreActivity extends Activity {
	private RelativeLayout my_more_layout_mycard;
	private ImageView backBtn;
	private ImageView moreBtn;
	private	Button dialog_cancelBtn;
	private Button dialog_refreshBtn;
	private Button dialog_returnBtn;
	private View mView;
	private TextView titleTxt;
	private 	PullToRefreshScrollView mPullRefreshScrollView;
	private LayoutInflater mLayoutInflater;
	private Dialog mRefreshDialog;
	private DisplayMetrics dm;
	private View dialog_view;

	
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
			backBtn = ( ImageView)findViewById(R.id.title_mycard_back);
			moreBtn = (ImageView)findViewById(R.id.title_mycard_more);
			titleTxt = (TextView)findViewById(R.id.title_mycard_mycard);
			titleTxt.setText("更多");
		}	
		mLayoutInflater = this.getLayoutInflater();
		 dialog_view = mLayoutInflater.inflate(R.layout.refreshdialog, null);
		dialog_cancelBtn = (Button)dialog_view.findViewById(R.id.refresh_cancel);
		dialog_refreshBtn = (Button)dialog_view.findViewById(R.id.refresh_refresh);
		dialog_returnBtn = (Button)dialog_view.findViewById(R.id.refresh_returnhome);
		mRefreshDialog = new Dialog(this,R.style.MyDialog);
		mRefreshDialog.setContentView(dialog_view);
		mRefreshDialog.setCanceledOnTouchOutside(true);
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		WindowManager.LayoutParams params = mRefreshDialog.getWindow().getAttributes();
		params.width = dm.widthPixels;
		params.height = dm.heightPixels/5*1 ;
		params.x = 0;
		params.y = dm.heightPixels-params.height;
		mRefreshDialog.getWindow().setAttributes(params);
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.my_more_pull_refresh_scrollview);
		mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> arg0) {
				// TODO Auto-generated method stub
				mPullRefreshScrollView.onRefreshComplete();
			}
			
		});

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
		
		moreBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mRefreshDialog.show();
			}
		});
		
		dialog_cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mRefreshDialog.cancel();
			}
		});
		
		dialog_refreshBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mRefreshDialog.cancel();
				mPullRefreshScrollView.setRefreshing();
			}
		});
		
		dialog_returnBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mRefreshDialog.cancel();
				startActivity(new Intent(My_MoreActivity.this, MainActivity.class));
				finish();
			}
		});
	}
}
