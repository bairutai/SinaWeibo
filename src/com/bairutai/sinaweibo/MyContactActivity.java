package com.bairutai.sinaweibo;

import com.bairutai.application.WeiboApplication;
import com.bairutai.tools.AsyncBitmapLoader;
import com.sina.weibo.sdk.openapi.models.User;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyContactActivity extends Activity {

	private TextView my_name;				//用户名
	private TextView my_description;		//简介
	private TextView my_statuesconut; //微博数
	private TextView my_friendscount;	//关注数
	private TextView my_flowerscount;	//粉丝数
	private ImageView my_iamgeIcon;	//头像
	
	private ActionBar my_actionbar;     //actionbar
	private View my_actionbar_view;    //actionbar view
	
	//获取数据的暂时方法
	private User mUser;
	private WeiboApplication app;
	private RelativeLayout my_name_layout;//"名字"布局
	private RelativeLayout my_more_layout;//"更多"布局


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my);//设置画面布局
		// actionbar属于tab所以获取的时候先获取上层View
		// 这里要注意的是要想使用actionbar必须使用带title的样式
		 my_actionbar = getParent().getActionBar();
		if (null != my_actionbar) {
			my_actionbar.setDisplayShowHomeEnabled(false);//去返回键
			my_actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
			my_actionbar.setDisplayShowCustomEnabled(true);
			LayoutInflater inflator = (LayoutInflater) this.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);//布局加载器
			my_actionbar_view = inflator.inflate(R.layout.title_my, null);//自定义actionbar视图
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			my_actionbar.setCustomView(my_actionbar_view, layout);//设置actionbar视图
		}
		
		findView();
		initScreen();
		addListener();
	}

	private void findView() {
		// TODO Auto-generated method stub
		my_name_layout = (RelativeLayout)this.findViewById(R.id.my_layout_name);
		my_more_layout = (RelativeLayout)this.findViewById(R.id.my_layout_more);
		my_name = (TextView) this.findViewById(R.id.my_txt_name);
		my_iamgeIcon = (ImageView) this.findViewById(R.id.my_img_iamgeIcon);
		my_description = (TextView) this.findViewById(R.id.my_txt_description);
		my_statuesconut = (TextView) this.findViewById(R.id.my_txt_statuescount);
		my_friendscount = (TextView) this.findViewById(R.id.my_txt_friendscount);
		my_flowerscount = (TextView) this.findViewById(R.id.my_txt_flowerscount);
	}



	private void initScreen() {
		app = (WeiboApplication) this.getApplication();
		mUser = app.getUser();
		my_name.setText(mUser.screen_name);
		my_name.setEnabled(true);
		new AsyncBitmapLoader().execute(my_iamgeIcon, mUser.profile_image_url);
		my_description.setText(mUser.description);
		my_friendscount.setText(String.valueOf(mUser.friends_count));
		my_statuesconut.setText(String.valueOf(mUser.statuses_count));
		my_flowerscount.setText(String.valueOf(mUser.followers_count));	
	}
	
	private void addListener() {
		// TODO Auto-generated method stub
		my_name_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MyContactActivity.this,MyHomePageActivity.class));
			}
		});

		my_more_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MyContactActivity.this, My_MoreActivity.class));
			}
		});
	}
}
