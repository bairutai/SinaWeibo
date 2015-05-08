package com.bairutai.sinaweibo;

import com.bairutai.application.WeiboApplication;
import com.sina.weibo.sdk.openapi.models.User;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EditDescriptionActivity extends Activity {
	//actionbar
	private ActionBar myEditDescription_actionbar; 
	private View myEditDescription_View;
	private ImageView backBtn;
	private Button finishBtn;
	private TextView titleTxt;
	
	//edittext
	private Intent myIntent;
	private EditText myEditDescription_edittext;
	
	//app
	private WeiboApplication app;
	private User mUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editdescription);

		myEditDescription_actionbar = getActionBar();
		if (null != myEditDescription_actionbar) {
			myEditDescription_actionbar.setDisplayShowHomeEnabled(false);//返回键
			myEditDescription_actionbar.setDisplayOptions(myEditDescription_actionbar.DISPLAY_SHOW_CUSTOM); 
			myEditDescription_actionbar.setDisplayShowCustomEnabled(true);
			LayoutInflater inflator = LayoutInflater.from(this);
			myEditDescription_View = inflator.inflate(R.layout.title_editdescription, null);//自定义actionbar视图
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			myEditDescription_actionbar.setCustomView(myEditDescription_View, layout);//设置actionbar视图
			backBtn = ( ImageView)findViewById(R.id.title_editdescription_back);
			finishBtn = (Button)findViewById(R.id.title_editdescription_finishBtn);
			titleTxt = (TextView)findViewById(R.id.title_editdescription_title);
		}

		findView();
		initScreen();
		addLinstener();
	}


	private void findView() {
		// TODO Auto-generated method stub
		myEditDescription_edittext = (EditText)findViewById(R.id.editdescription_edittext);
	}
	
	private void initScreen() {
		// TODO Auto-generated method stub
		app = (WeiboApplication)getApplication();
		mUser = app.getUser();
		myIntent = getIntent();
		String jianjie = myIntent.getExtras().getString("jianjie");
		myEditDescription_edittext.setText(jianjie);
		myEditDescription_edittext.setSelection(jianjie.length());
	}
	
	private void addLinstener() {
		// TODO Auto-generated method stub
		finishBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myIntent.putExtra("jianjie_new", myEditDescription_edittext.getText().toString());
				mUser.description = myEditDescription_edittext.getText().toString();
				setResult(RESULT_OK, myIntent);
				finish();
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
