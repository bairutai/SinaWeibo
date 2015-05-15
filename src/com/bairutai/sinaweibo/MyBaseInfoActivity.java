package com.bairutai.sinaweibo;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bairutai.application.WeiboApplication;
import com.bairutai.data.CityInfo;
import com.bairutai.openwidget.ClearEditText;
import com.bairutai.openwidget.OnWheelScrollListener;
import com.bairutai.openwidget.StrericWheelAdapter;
import com.bairutai.openwidget.WheelView;
import com.bairutai.sinaweibo.R.id;
import com.sina.weibo.sdk.openapi.models.User;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyBaseInfoActivity extends Activity {

	//actionbar
	private ActionBar myBaseInfo_actionbar; 
	private View myBaseInfo_View;
	private ImageView backBtn;
	private ImageView moreBtn;
	private TextView titleTxt;

	//所在地dialog
	private String[] mprovince; 
	private WheelView mWheelView_province;
	private WheelView mWheelView_city;
	private StrericWheelAdapter provinceAdapter = null;
	private StrericWheelAdapter cityAdapter = null;
	private LayoutInflater mLayoutInflater;
	private View CityDialogLayout;
	private Dialog mlocation_dialog;
	private Button mlocation_dialog_cancleBtn;
	private Button mlocation_dialog_mconfirmBtn;
	private DisplayMetrics dm;

	//获取数据的暂时方法
	private WeiboApplication app;
	private User mUser;

	//界面
	private TextView myBaseInfo_dengluming_txt;
	private TextView myBaseInfo_jianjie_txt;
	private TextView myBaseInfo_suozaidi_txt;
	private TextView myBaseInfo_xingbie_txt;
	private TextView myBaseInfo_nicheng_txt;
	private TextView myBaseInfo_zhuceshijian_txt;
	private RelativeLayout myBaseInfo_location_layout;
	private RelativeLayout myBaseInfo_jianjie_layout;
	private RelativeLayout myBaseInfo_gender_layout;
	private RelativeLayout myBaseInfo_username_layout;

	//gender dialog
	private View gender_dialog_layout;
	private Dialog gender_dialog;
	private TextView male_txt;
	private TextView female_txt;
	
	//username dialog
	private View username_dialog_layout;
	private Dialog username_dialog;
	private Button username_dialog_confirmBtn;
	private Button username_dialog_cancleBtn;
	private ClearEditText username_dialog_username;
	private TextView username_dialog_web;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mybaseinfo);

		myBaseInfo_actionbar = getActionBar();
		if (null != myBaseInfo_actionbar) {
			myBaseInfo_actionbar.setDisplayShowHomeEnabled(false);//返回键
			myBaseInfo_actionbar.setDisplayOptions(myBaseInfo_actionbar.DISPLAY_SHOW_CUSTOM); 
			myBaseInfo_actionbar.setDisplayShowCustomEnabled(true);
			LayoutInflater inflator = LayoutInflater.from(this);
			myBaseInfo_View = inflator.inflate(R.layout.title_mycard, null);//自定义actionbar视图
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			myBaseInfo_actionbar.setCustomView(myBaseInfo_View, layout);//设置actionbar视图
			backBtn = ( ImageView)findViewById(R.id.title_mycard_back);
			moreBtn = (ImageView)findViewById(R.id.title_mycard_more);
			moreBtn.setVisibility(View.INVISIBLE);
			titleTxt = (TextView)findViewById(R.id.title_mycard_mycard);
			titleTxt.setText("基本信息");
		}

		findView();
		initScreen();
		addListener();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
		case RESULT_OK:
			Bundle b=data.getExtras(); //data为B中回传的Intent
			String str=b.getString("jianjie_new");//str即为回传的值
			myBaseInfo_jianjie_txt.setText(str);
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void findView() {
		// TODO Auto-generated method stub
		myBaseInfo_dengluming_txt = (TextView)findViewById(R.id.mybaseinfo_dengluming_txt);
		myBaseInfo_jianjie_txt = (TextView)findViewById(R.id.mybaseinfo_jianjie_txt);
		myBaseInfo_suozaidi_txt = (TextView)findViewById(R.id.mybaseinfo_suozaidi_txt);
		myBaseInfo_xingbie_txt = (TextView)findViewById(R.id.mybaseinfo_xingbie_txt);
		myBaseInfo_nicheng_txt = (TextView)findViewById(R.id.mybaseinfo_nicheng_txt);
		myBaseInfo_zhuceshijian_txt = (TextView)findViewById(R.id.mybaseinfo_zhuceshijian_txt);
		myBaseInfo_location_layout = (RelativeLayout)findViewById(R.id.mybaseinfo_suozaidi_layout);
		myBaseInfo_jianjie_layout = (RelativeLayout)findViewById(R.id.mybaseinfo_jianjie_layout);
		myBaseInfo_gender_layout = (RelativeLayout)findViewById(R.id.mybaseinfo_xingbie_layout);
		myBaseInfo_username_layout = (RelativeLayout)findViewById(R.id.mybaseinfo_nicheng_layout);
		mLayoutInflater = this.getLayoutInflater();

		//city dialog
		CityDialogLayout = mLayoutInflater.inflate(R.layout.citydialog, null);
		mWheelView_province = (WheelView)CityDialogLayout.findViewById(R.id.citydialog_province);
		mWheelView_city = (WheelView)CityDialogLayout.findViewById(R.id.citydialog_city);
		mlocation_dialog_cancleBtn = (Button)CityDialogLayout.findViewById(R.id.citydialog_cancle);
		mlocation_dialog_mconfirmBtn = (Button)CityDialogLayout.findViewById(R.id.citydialog_confirm);
		
		//gender dialog
		gender_dialog_layout = mLayoutInflater.inflate(R.layout.genderdialog, null);
		male_txt = (TextView)gender_dialog_layout.findViewById(R.id.genderdialog_male_txt);
		female_txt = (TextView)gender_dialog_layout.findViewById(R.id.genderdialog_female_txt);
		
		//username dialog
		username_dialog_layout = mLayoutInflater.inflate(R.layout.usernamedialog, null);
		username_dialog_confirmBtn = (Button)username_dialog_layout.findViewById(R.id.usernamedialog_confirmBtn);
		username_dialog_cancleBtn = (Button)username_dialog_layout.findViewById(R.id.usernamedialog_cancleBtn);
		username_dialog_username = (ClearEditText)username_dialog_layout.findViewById(R.id.usernamedialog_username);
		username_dialog_web = (TextView)username_dialog_layout.findViewById(R.id.usernamedialog_lianjie);
	}

	private void initScreen() {
		// TODO Auto-generated method stub

		//citydialog初始化
		mWheelView_city.setCyclic(true);
		mWheelView_province.setCyclic(true);
		provinceAdapter = new StrericWheelAdapter(CityInfo.province);
		mWheelView_province.setAdapter(provinceAdapter);
		mWheelView_province.setInterpolator(new AnticipateOvershootInterpolator());
		mlocation_dialog=new Dialog(this,R.style.MyDialog);
		mlocation_dialog.setContentView(CityDialogLayout);
		mlocation_dialog.setCanceledOnTouchOutside(true);
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		WindowManager.LayoutParams params = mlocation_dialog.getWindow().getAttributes();
		params.width = dm.widthPixels/4*3;
		params.height = dm.heightPixels/5*2 ;
		mlocation_dialog.getWindow().setAttributes(params);

		//genderdialog初始化
		gender_dialog = new Dialog(this, R.style.MyDialog);
		WindowManager.LayoutParams params1 = gender_dialog.getWindow().getAttributes();
		gender_dialog.setContentView(gender_dialog_layout);
		gender_dialog.setCanceledOnTouchOutside(true);
		params1.width = dm.widthPixels/3*2;
		params1.height = dm.heightPixels/7*1 ;
		gender_dialog.getWindow().setAttributes(params1);
		
		//昵称dialog初始化
		username_dialog = new Dialog(this, R.style.MyDialog);
		WindowManager.LayoutParams params2 = username_dialog.getWindow().getAttributes();
		username_dialog.setContentView(username_dialog_layout);
		username_dialog.setCanceledOnTouchOutside(true);
		params2.width = dm.widthPixels/5*4;
		params2.height = dm.heightPixels/15*4;
		username_dialog.getWindow().setAttributes(params2);
		

		//界面数据初始化
		app = (WeiboApplication) this.getApplication();
		mUser = app.getUser();
		myBaseInfo_dengluming_txt.setText(mUser.id);
		myBaseInfo_jianjie_txt.setText(mUser.description);
		myBaseInfo_suozaidi_txt.setText(mUser.location);
		myBaseInfo_nicheng_txt.setText(mUser.screen_name);
		Date date = new Date(mUser.created_at);
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		String date1 = dateformat.format(date);
		myBaseInfo_zhuceshijian_txt.setText(date1);
		if(mUser.gender.equalsIgnoreCase("m")){
			myBaseInfo_xingbie_txt.setText("男");
		}
		else{
			myBaseInfo_xingbie_txt.setText("女");
		}
	}

	private void addListener() {
		// TODO Auto-generated method stub
		username_dialog_web.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MyBaseInfoActivity.this, GoodNameActivity.class));
			}
		});
		
		username_dialog_cancleBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				username_dialog.cancel();
			}
		});
		
		username_dialog_confirmBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				username_dialog.cancel();
				myBaseInfo_nicheng_txt.setText(username_dialog_username.getText());
				ContentValues values = new ContentValues();
				values.put("screen_name", username_dialog_username.getText().toString());
		        app.getmDataBase().mSQLiteDatabase.update("user", values, null, null);
			}
		});
		
		myBaseInfo_username_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				username_dialog_username.setText(myBaseInfo_nicheng_txt.getText());
				username_dialog_username.setSelection(myBaseInfo_nicheng_txt.getText().length());
				username_dialog.show();
			}
		});

		male_txt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gender_dialog.cancel();
				myBaseInfo_xingbie_txt.setText("男");
			}
		});

		female_txt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gender_dialog.cancel();
				myBaseInfo_xingbie_txt.setText("女");
			}
		});

		myBaseInfo_gender_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gender_dialog.show();
			}
		});

		mlocation_dialog_cancleBtn.setOnClickListener(new OnClickListener() {							
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mlocation_dialog.cancel();
			}
		});

		mlocation_dialog_mconfirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mlocation_dialog.cancel();
				String city = mWheelView_city.getCurrentItemValue();
				String province = mWheelView_province.getCurrentItemValue();
				int city_index = mWheelView_city.getCurrentItem();
				if (city_index == 0) {
					myBaseInfo_suozaidi_txt.setText(province);
				}
				else{
					myBaseInfo_suozaidi_txt.setText(province + " " + city);
				}
			}
		});

		myBaseInfo_location_layout.setOnClickListener(new OnClickListener() {					
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initCurrentProvince();
				mlocation_dialog.show();
			}
		});

		myBaseInfo_jianjie_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MyBaseInfoActivity.this, EditDescriptionActivity.class);
				intent.putExtra("jianjie", mUser.description);
				startActivityForResult(intent, 0);
			}
		});

		mWheelView_province.addScrollingListener(new OnWheelScrollListener() {		
			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub			
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				int i=wheel.getCurrentItem();
				cityAdapter.setStrContents(CityInfo.city[i]);
				mWheelView_city.setAdapter(cityAdapter);
				mWheelView_city.setCurrentItem(0);
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

	private void initCurrentProvince() {
		// TODO Auto-generated method stub
		if (myBaseInfo_suozaidi_txt.getText().length() > 0) {
			mprovince = myBaseInfo_suozaidi_txt.getText().toString().split(" ");
		}
		else{
			mprovince = app.getUser().location.split(" ");
		}
		if (mprovince.length == 0) {
			mWheelView_province.setCurrentItem(0);
		}
		else {
			if(mprovince.length == 1) {
				mWheelView_city.setCurrentItem(0);
			}
			else {
				int i,j;
				for (i=0;i<CityInfo.province.length;i++) {
					if (mprovince[0].equals(CityInfo.province[i])) {
						mWheelView_province.setCurrentItem(i);
						cityAdapter= new StrericWheelAdapter(CityInfo.city[i]);
						mWheelView_city.setAdapter(cityAdapter);
						for(j=0;j<CityInfo.city[i].length;j++) {
							if (mprovince[1].equals(CityInfo.city[i][j].trim())) {
								mWheelView_city.setCurrentItem(j);
								break;
							}
							else {
								mWheelView_city.setCurrentItem(0);
							}
						}
						break;
					}
				}
			}

		} 			
	}
}
