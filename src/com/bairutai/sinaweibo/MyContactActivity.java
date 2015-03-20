package com.bairutai.sinaweibo;

import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.StrericWheelAdapter;
import kankan.wheel.widget.WheelView;

import com.bairutai.application.WeiboApplication;
import com.bairutai.data.AccessTokenKeeper;
import com.bairutai.data.CityInfo;
import com.bairutai.data.Constants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyContactActivity extends Activity implements IWeiboActivity {
	
	private ImageView mycontact_iamgeIcon;
	private TextView mycontact_textName;
	private Button mycontact_editBtn;
	private TextView mycontact_address;
	private TextView mycontact_loginName;
	private TextView mycontact_item_attention;
	private TextView mycontact_item_weibo;
	private TextView mycontact_item_interest;
	private TextView mycontact_item_topic;
	private ImageView mycontact_triagle_zan;
	private TextView head_userName;
	private ProgressDialog p;
	private TextView head_refresh;
	private Oauth2AccessToken mAccessToken;
    private UsersAPI mUsersAPI;
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycontact);
		app = (WeiboApplication) this.getApplication();
		mycard_layout = (RelativeLayout)this.findViewById(R.id.mycontact_mycardlayout);
		mLayoutInflater = this.getLayoutInflater();
		DialogLayout =mLayoutInflater.inflate(R.layout.citydialog, null);
		mWheelView_province = (WheelView)DialogLayout.findViewById(R.id.citydialog_province);
		mWheelView_city = (WheelView)DialogLayout.findViewById(R.id.citydialog_city);
		mcancleBtn = (Button)DialogLayout.findViewById(R.id.citydialog_cancle);
		provinceAdapter = new StrericWheelAdapter(CityInfo.province);
		mWheelView_province.setAdapter(provinceAdapter);
	    mWheelView_province.setInterpolator(new AnticipateOvershootInterpolator());
	    mlocation_dialog=new Dialog(this,R.style.MyDialog);
		mlocation_dialog.setContentView(DialogLayout);
		head_refresh = (TextView) this.findViewById(R.id.head_refresh);
		head_userName = (TextView) this.findViewById(R.id.head_userName);
		head_userName.setText("我");
		mycontact_textName = (TextView) findViewById(R.id.mycontact_textName);
		mycontact_iamgeIcon = (ImageView) this
				.findViewById(R.id.mycontact_iamgeIcon);
		mycontact_editBtn = (Button) this.findViewById(R.id.mycontact_editBtn);
		mycontact_address = (TextView) this.findViewById(R.id.mycontact_address);
		mycontact_loginName = (TextView) this
				.findViewById(R.id.mycontact_loginName);
		mycontact_item_attention = (TextView) this
				.findViewById(R.id.mycontact_item_attention);
		mycontact_item_weibo = (TextView) this
				.findViewById(R.id.mycontact_item_weibo);
		mycontact_item_interest = (TextView) this
				.findViewById(R.id.mycontact_item_interest);
		mycontact_item_topic = (TextView) this
				.findViewById(R.id.mycontact_item_topic);
		mycontact_triagle_zan = (ImageView) this.findViewById(R.id.mycontact_triagle_zan);
		
		// 新浪API，读取授权成功后保存到本地的配置文件
		mAccessToken = AccessTokenKeeper.readAccessToken(this);
         // 获取用户信息接口,根据用户uid获取用户信息
        mUsersAPI = new UsersAPI(this, Constants.APP_KEY, mAccessToken);
        long uid = Long.parseLong(mAccessToken.getUid());
        mUsersAPI.show(uid, mListener);
        long[] uids = { Long.parseLong(mAccessToken.getUid()) };
        mUsersAPI.counts(uids, mListener);
		init();
	}
	// 异步获取数据后的回调函数
	private RequestListener mListener = new RequestListener() {
		 public void onComplete(String response) {
	            if (!TextUtils.isEmpty(response)) {
	            	mUser = User.parse(response);
	            	if (mUser != null) {
	            		// 保存user信息到全局
	            	app.setUser(mUser);
	         		mycontact_textName.setText(mUser.screen_name);
	        		new AsyncBitmapLoader().execute(mycontact_iamgeIcon, mUser
	        				.profile_image_url);
	        		mycontact_address.setText(mUser.location);
	        		mycontact_address.setEnabled(true);
	        		mycontact_loginName.setText(mUser.description);
	        		mycontact_loginName.setEnabled(true);
	        		mycontact_item_attention.setText(String.valueOf(mUser
	        				.friends_count));
	        		mycontact_item_weibo.setText(String.valueOf(mUser
	        				.statuses_count));
	        		mycontact_item_interest.setText(String.valueOf(mUser
	        				.followers_count));
	        		mycontact_item_topic.setText(String.valueOf(mUser
	        				.favourites_count));
	            	}
	            }
		 }

		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	@Override
	public void init() {
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
        
        mycard_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MyContactActivity.this,MycardActivity.class));
			}
		});
        

		
        mycontact_textName.setOnClickListener(new OnClickListener() {					
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
