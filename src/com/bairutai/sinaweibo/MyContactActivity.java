package com.bairutai.sinaweibo;

import com.bairutai.openwidget.OnWheelScrollListener;
import com.bairutai.openwidget.StrericWheelAdapter;
import com.bairutai.openwidget.WheelView;
import com.bairutai.application.WeiboApplication;
import com.bairutai.data.CityInfo;
import com.bairutai.tools.AsyncBitmapLoader;
import com.sina.weibo.sdk.openapi.models.User;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyContactActivity extends Activity implements IWeiboActivity {

	private ImageView my_iamgeIcon;
	private TextView my_name;
	//	private TextView mycontact_address;
	private TextView my_description;
	private TextView my_statuesconut;
	private TextView my_friendscount;
	private TextView my_flowerscount;
	//	private TextView mycontact_item_topic;
	private WheelView mWheelView_province;
	private WheelView mWheelView_city;
	private StrericWheelAdapter provinceAdapter = null;
	private StrericWheelAdapter cityAdapter = null;
	private LayoutInflater mLayoutInflater;
	private View DialogLayout;
	private Dialog mlocation_dialog;
	private Button mcancleBtn;
	private User mUser;
	private WeiboApplication app;
	private RelativeLayout mycard_layout;
	private RelativeLayout my_layout_name;
	private RelativeLayout my_layout_more;
	private View mView;
	private DisplayMetrics dm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// actionbar属于tab所以获取的时候先获取上层View
		// 这里要注意的是要想使用actionbar必须使用带title的样式
		ActionBar actionBar = getParent().getActionBar();
		if (null != actionBar) {
			actionBar.setDisplayShowHomeEnabled(false);//去返回键
			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
			actionBar.setDisplayShowCustomEnabled(true);
			LayoutInflater inflator = (LayoutInflater) this.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			mView = inflator.inflate(R.layout.title_my, null);//自定义actionbar视图
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			actionBar.setCustomView(mView, layout);//设置actionbar视图
		}
		else {
			Log.d("null","null`````" );
		}
		setContentView(R.layout.my);//设置画面布局
		//		registerBoradcastReceiver();  
		app = (WeiboApplication) this.getApplication();
		mycard_layout = (RelativeLayout)this.findViewById(R.id.mycontact_mycardlayout);
		my_layout_name = (RelativeLayout)this.findViewById(R.id.my_layout_name);
		my_layout_more = (RelativeLayout)this.findViewById(R.id.my_layout_more);
		mLayoutInflater = this.getLayoutInflater();
		DialogLayout =mLayoutInflater.inflate(R.layout.citydialog, null);
		mWheelView_province = (WheelView)DialogLayout.findViewById(R.id.citydialog_province);
		mWheelView_city = (WheelView)DialogLayout.findViewById(R.id.citydialog_city);
		mWheelView_city.setCyclic(true);
		mWheelView_province.setCyclic(true);
		mcancleBtn = (Button)DialogLayout.findViewById(R.id.citydialog_cancle);
		provinceAdapter = new StrericWheelAdapter(CityInfo.province);
		mWheelView_province.setAdapter(provinceAdapter);
		mWheelView_province.setInterpolator(new AnticipateOvershootInterpolator());
		mlocation_dialog=new Dialog(this,R.style.MyDialog);
		mlocation_dialog.setContentView(DialogLayout);
		mlocation_dialog.setCanceledOnTouchOutside(true);
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		WindowManager.LayoutParams params = mlocation_dialog.getWindow().getAttributes();
		params.width = dm.widthPixels/4*3;
		params.height = dm.heightPixels/5*2 ;
		mlocation_dialog.getWindow().setAttributes(params);
		my_name = (TextView) findViewById(R.id.my_txt_name);
		my_iamgeIcon = (ImageView) this
				.findViewById(R.id.my_img_iamgeIcon);
		my_description = (TextView) this
				.findViewById(R.id.my_txt_description);
		my_statuesconut = (TextView) this
				.findViewById(R.id.my_txt_statuescount);
		my_friendscount = (TextView) this
				.findViewById(R.id.my_txt_friendscount);
		my_flowerscount = (TextView) this
				.findViewById(R.id.my_txt_flowerscount);
		init();
	}


	@Override
	public void init() {
		mUser = app.getUser();
		if (null == mUser) {
			Log.d("nulluser","null`````aaaa" );
		}
		my_name.setText(mUser.screen_name);
		my_name.setEnabled(true);
		new AsyncBitmapLoader().execute(my_iamgeIcon, mUser
				.profile_image_url);
		my_description.setText(mUser.description);
		my_friendscount.setText(String.valueOf(mUser
				.friends_count));
		my_statuesconut.setText(String.valueOf(mUser
				.statuses_count));
		my_flowerscount.setText(String.valueOf(mUser
				.followers_count));

		// TODO Auto-generated method stub
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


		my_layout_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MyContactActivity.this,MyHomePageActivity.class));
			}
		});

		my_layout_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MyContactActivity.this, My_MoreActivity.class));
			}
		});

		my_name.setOnClickListener(new OnClickListener() {					
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mlocation_dialog.show();
				initCurrentProvince();
				mcancleBtn.setOnClickListener(new OnClickListener() {							
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mlocation_dialog.cancel();
					}
				});
			}

			private void initCurrentProvince() {
				// TODO Auto-generated method stub
				String [] mprovince = app.getUser().location.split(" ");
				Log.w(mprovince[1],CityInfo.city[5][1]);
				if (mprovince[0].isEmpty()) {
					mWheelView_province.setCurrentItem(0);
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
		});
	}

	@Override
	public void refresh(Object... params) {
		// TODO Auto-generated method stub

	}

}
