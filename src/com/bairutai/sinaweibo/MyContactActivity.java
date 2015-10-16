package com.bairutai.sinaweibo;

import com.bairutai.application.WeiboApplication;
import com.bairutai.model.User;
import com.bairutai.tools.AsyncBitmapLoader;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyContactActivity extends Activity implements View.OnClickListener{
	//主界面
	private TextView my_name;				//用户名
	private TextView my_description;		//简介
	private TextView my_statuesconut; //微博数
	private TextView my_friendscount;	//关注数
	private TextView my_flowerscount;	//粉丝数
	private ImageView my_iamgeIcon;	//头像
	private LinearLayout my_flower_layout;//粉丝布局
	private LinearLayout my_friends_layout;//关注布局
	private RelativeLayout my_name_layout;//"名字"布局
	private RelativeLayout my_more_layout;//"更多"布局

	//actionbar
	private ActionBar my_actionbar;     //actionbar
	private View my_actionbar_view;    //actionbar view
	private Button settingBtn;

	//获取数据的暂时方法
	private User mUser;
	private WeiboApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my);//设置画面布局
		setActionbar();
		findView();
		initScreen();
		addListener();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		setActionbar();
		super.onResume();
	}

	private void findView() {
		// TODO Auto-generated method stub
		my_name_layout = (RelativeLayout)this.findViewById(R.id.my_layout_name);
		my_more_layout = (RelativeLayout)this.findViewById(R.id.my_layout_more);
		my_flower_layout = (LinearLayout)findViewById(R.id.my_layout_flower);
		my_friends_layout = (LinearLayout)findViewById(R.id.my_layout_friends);
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
		new AsyncBitmapLoader().execute(my_iamgeIcon, mUser.avatar_large);
		my_description.setText(mUser.description);
		my_friendscount.setText(String.valueOf(mUser.friends_count));
		my_statuesconut.setText(String.valueOf(mUser.statuses_count));
		my_flowerscount.setText(String.valueOf(mUser.followers_count));	
	}

	private void addListener() {
		// TODO Auto-generated method stub
		my_flower_layout.setOnClickListener(this);
		my_name_layout.setOnClickListener(this);
		my_more_layout.setOnClickListener(this);
	}

	private void setActionbar() {
		// TODO Auto-generated method stub
		// actionbar属于tab所以获取的时候先获取上层View
		// 这里要注意的是要想使用actionbar必须使用带title的样式
		my_actionbar = getParent().getActionBar();
		if (null != my_actionbar) {
			my_actionbar.setDisplayShowHomeEnabled(false);//去左上角LOGO
			my_actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
			my_actionbar.setDisplayShowCustomEnabled(true);
			LayoutInflater inflator = (LayoutInflater) this.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);//布局加载器
			my_actionbar_view = inflator.inflate(R.layout.title_my, null);//自定义actionbar视图
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			my_actionbar.setCustomView(my_actionbar_view, layout);//设置actionbar视图
			settingBtn = (Button)my_actionbar_view.findViewById(R.id.title_my_setBtn);
			settingBtn.setOnClickListener(this);
			my_actionbar.show();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.my_layout_flower:
			startActivity(new Intent(MyContactActivity.this, MyFansListActivity.class));
			break;
		case R.id.my_layout_more:
			startActivity(new Intent(MyContactActivity.this, My_MoreActivity.class));
			break;
		case R.id.my_layout_name:
			startActivity(new Intent(MyContactActivity.this,MyHomePageActivity.class));
			break;
		case R.id.title_my_setBtn:
			startActivity(new Intent(MyContactActivity.this, MySettingActivity.class));
			break;
		case R.id.my_layout_friends:
			Intent intent = new Intent(MyContactActivity.this, MyFriendsListActivity.class);
			startActivity(intent);
			break;
		default :
			break;
		}
	}
}
