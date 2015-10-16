package com.bairutai.sinaweibo;

import com.bairutai.application.WeiboApplication;
import com.bairutai.data.Constants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SendActivity extends Activity {
	// actionbar
	private View mView;
	private TextView cancleBtn;
	private TextView sendBtn;
	private TextView title;
	private TextView name;
	
	// 主界面
	private WeiboApplication app;
	private EditText editText;
	private StatusesAPI mStatusesAPI;
	private ProgressBar mProgressBar;
	private ImageButton mlocationImgBtn;
	private Button mlocationBtn;
	private boolean isLocation = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendactivity);
		app = (WeiboApplication)getApplication();
		ActionBar actionBar = getActionBar();
		if (null != actionBar) {
			actionBar.setDisplayShowHomeEnabled(false);//左上角logo
			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
			actionBar.setDisplayShowCustomEnabled(true);
			LayoutInflater inflator = LayoutInflater.from(this);
			mView = inflator.inflate(R.layout.title_send, null);//自定义actionbar视图
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			actionBar.setCustomView(mView, layout);//设置actionbar视图
			cancleBtn = (TextView)findViewById(R.id.title_send_cancle);
			sendBtn = (TextView)findViewById(R.id.title_send_send);
			title = (TextView)findViewById(R.id.title_send_title);
			name = (TextView)findViewById(R.id.title_send_title_name);
		}
		
		findView();
		initScreen();
		addListener();
	}
	
	private void findView() {
		// TODO Auto-generated method stub
		editText = (EditText)findViewById(R.id.sendactivity_editText);
		mProgressBar = (ProgressBar)findViewById(R.id.sendactivity_progressbar);
		mlocationBtn = (Button)findViewById(R.id.sendactivity_location_layout_txtBtn);
		mlocationImgBtn = (ImageButton)findViewById(R.id.sendactivity_location_layout_locationBtn);
	}
	
	private void initScreen() {
		// TODO Auto-generated method stub
		if (null != getIntent()) {
			title.setText(getIntent().getStringExtra("title"));
			if (null != app.getUser()) {
				name.setText(app.getUser().screen_name);
			}
			if (null != getIntent().getStringExtra("location") && !getIntent().getStringExtra("location").isEmpty()) {
				mlocationBtn.setText(getIntent().getStringExtra("location"));
				setLocationBtnStatus(true);
			}
		}
	}
	
	private void addListener() {
		// TODO Auto-generated method stub
		editText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {
					sendBtn.setClickable(true);
					sendBtn.setTextColor(getResources().getColor(R.drawable.txt_from_orange_to_dorange));
				}else {
					sendBtn.setClickable(false);
					sendBtn.setTextColor(getResources().getColor(R.color.black2));
				}
			}
		});
		
		sendBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!editText.getText().toString().isEmpty()) {
					System.out.println(editText.getText().toString());
					mStatusesAPI = new StatusesAPI(SendActivity.this, Constants.APP_KEY, app.getmAccessToken());
					mStatusesAPI.update(editText.getText().toString(), null, null, mStatuslistener);
					mProgressBar.setVisibility(View.VISIBLE);
				}
			}
		});
		
		cancleBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		mlocationBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SendActivity.this, LocationActivity.class);
				intent.putExtra("from", "sendactivity");
				intent.putExtra("isLocation", isLocation);
				startActivityForResult(intent, 0);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		switch (resultCode) {
		case Constants.LOCATION_RESULT_OK:
			if (!intent.getStringExtra("location").isEmpty()) {
				mlocationBtn.setText(intent.getStringExtra("location"));
				setLocationBtnStatus(true);
			}
			break;
		case Constants.LOCATION_RESULT_DELETE:
			setLocationBtnStatus(false);
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, intent);
	}
	
	private void setLocationBtnStatus(boolean is){
		this.isLocation = is;
		if(null != mlocationBtn && null != mlocationImgBtn){
			if (is){
				mlocationBtn.setTextColor(Color.BLUE);
				mlocationImgBtn.setImageDrawable(getResources().getDrawable(R.drawable.compose_locatebutton_succeeded));
			}
			else{
				mlocationBtn.setTextColor(Color.BLACK);
				mlocationBtn.setText("显示位置");
				mlocationImgBtn.setImageDrawable(getResources().getDrawable(R.drawable.compose_locatebutton_ready));
			}
		}
	}
	
	private RequestListener mStatuslistener = new RequestListener() {
		
		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			if (!arg0.isEmpty()) {
				System.out.println(arg0);
				mProgressBar.setVisibility(View.GONE);
				Intent intent = new Intent(SendActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		}
	};
}
