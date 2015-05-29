package com.bairutai.sinaweibo;

import com.bairutai.application.WeiboApplication;
import com.zcw.togglebutton.ToggleButton;
import com.zcw.togglebutton.ToggleButton.OnToggleChanged;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MySettingActivity extends Activity implements View.OnClickListener{

	//actionbar
	private ActionBar actionbar;
	private View view;
	private ImageView backBtn;
	private Button finishBtn;
	private TextView titleTxt;

	//主界面
	private RelativeLayout MySetting_exit_layout;
	private RelativeLayout MySetting_clear_layout;
	private TextView cachesize;
	private ToggleButton yejian_toggleBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_setting);
		actionbar = getActionBar();
		if (null != actionbar) {
			actionbar.setDisplayShowHomeEnabled(false);//左上角logo
			actionbar.setDisplayOptions(actionbar.DISPLAY_SHOW_CUSTOM); 
			actionbar.setDisplayShowCustomEnabled(true);
			LayoutInflater inflator = LayoutInflater.from(this);
			view = inflator.inflate(R.layout.title_editdescription, null);//自定义actionbar视图
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			actionbar.setCustomView(view, layout);//设置actionbar视图
			backBtn = ( ImageView)findViewById(R.id.title_editdescription_back);
			finishBtn = (Button)findViewById(R.id.title_editdescription_finishBtn);
			titleTxt = (TextView)findViewById(R.id.title_editdescription_title);
			finishBtn.setVisibility(View.INVISIBLE);
			titleTxt.setText("设置");
		}
		findView();
		addListener();
	}

	private void findView() {
		// TODO Auto-generated method stub
		MySetting_exit_layout = (RelativeLayout)findViewById(R.id.my_setting_exit_layout);
		MySetting_clear_layout = (RelativeLayout)findViewById(R.id.my_setting_clear_layout);
		cachesize = (TextView)findViewById(R.id.my_setting_clear_num);
		yejian_toggleBtn = (ToggleButton)findViewById(R.id.my_setting_yejian_toggleBtn);
	}

	private void addListener() {
		// TODO Auto-generated method stub
		backBtn.setOnClickListener(this);
		MySetting_exit_layout.setOnClickListener(this);
		MySetting_clear_layout.setOnClickListener(this);
		yejian_toggleBtn.setOnToggleChanged(new OnToggleChanged() {

			@Override
			public void onToggle(boolean on) {
				// TODO Auto-generated method stub
				if(on){
					AlertDialog switchdialog = new AlertDialog.Builder(MySettingActivity.this).
							setTitle("您需要下载夜间模式主题包").
							setPositiveButton("去下载", new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							}).setNegativeButton("取消", new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
									yejian_toggleBtn.setToggleOff();
								}
							}).create();
					switchdialog.show();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_editdescription_back:
			finish();
			break;
		case R.id.my_setting_exit_layout:
			AlertDialog dialog = new AlertDialog.Builder(this).
			setTitle("确认退出微博").
			setMultiChoiceItems(new String[] {"接收离线消息"}, null, new OnMultiChoiceClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
					// TODO Auto-generated method stub

				}
			}).setPositiveButton("确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					finish();
					Intent intent = new Intent(Intent.ACTION_MAIN);  
					intent.addCategory(Intent.CATEGORY_HOME);  
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
					startActivity(intent);  
					android.os.Process.killProcess(android.os.Process.myPid());
				}
			}).setNegativeButton("取消", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			}).create();
			dialog.show();
			break;
		case R.id.my_setting_clear_layout:
			AlertDialog cleardialog = new AlertDialog.Builder(this).
			setTitle("确定清除缓存么？").
			setPositiveButton("确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					cachesize.setText("0.0MB");
					dialog.dismiss();
				}
			}).setNegativeButton("取消", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			}).create();
			cleardialog.show();
			break;
		default:
			break;
		}
	}
}
