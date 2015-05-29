package com.bairutai.sinaweibo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoOfQRActivity extends Activity {

	//actionbar
	private ActionBar actionbar;
	private View view;
	private ImageView backBtn;
	private TextView titleTxt;
	private Button finishBtn;

	//主界面
	private TextView content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.infoofqr);
		actionbar = getActionBar();
		if (null != actionbar) {
			actionbar.setDisplayShowHomeEnabled(false);//返回键
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
			titleTxt.setText("扫描到的信息");
		}
		findView();
		initScreen();
		addListener();
	}

	private void findView() {
		// TODO Auto-generated method stub
		content = (TextView)findViewById(R.id.infoofqr_txt);
	}
	
	private void initScreen() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		//显示扫描到的内容
		content.setText(intent.getCharSequenceExtra("result"));
	}
	
	private void addListener() {
		// TODO Auto-generated method stub
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

}
