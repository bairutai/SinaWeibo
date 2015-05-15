package com.bairutai.sinaweibo;

import com.bairutai.Service.MyService;
import com.bairutai.application.WeiboApplication;
import com.bairutai.tools.AccessTokenKeeper;
import com.bairutai.data.Constants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.legacy.AccountAPI;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;



public class LogoActivity extends Activity {
	private ImageView imageview;//logo画面
	private Oauth2AccessToken mAccessToken;//用户数据文件
	private WeiboApplication app;
	private MyService mMyService;


	//这里需要用到ServiceConnection在Context.bindService和context.unBindService()里用到  
	private ServiceConnection mServiceConnection = new ServiceConnection() {  
		//bindService
		public void onServiceConnected(ComponentName name, IBinder service) {  
			// TODO Auto-generated method stub  
			mMyService = ((MyService.MyBinder)service).getService();  
			mMyService.getUser();
		}  

		public void onServiceDisconnected(ComponentName name) {  
			// TODO Auto-generated method stub  
		}  
	};  

	private BroadcastReceiver MyReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			startActivity(new Intent(LogoActivity.this,MainActivity.class));
			finish();
			unbindService(mServiceConnection);
			unregisterReceiver(MyReceiver);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logo);
		registerBoradcastReceiver();
		imageview=(ImageView) this.findViewById(R.id.logo);
		app = (WeiboApplication) this.getApplication();
		AlphaAnimation animation=new AlphaAnimation(0.1f, 1.0f);
		animation.setDuration(4000);
		imageview.setAnimation(animation);

		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				// 新浪API，读取授权成功后保存到本地的配置文件
				mAccessToken = AccessTokenKeeper.readAccessToken(LogoActivity.this);
				if (null != mAccessToken) {
					if (mAccessToken.isSessionValid()){
						app.setmAccessToken(mAccessToken);
						Intent  service=new Intent();
						service.setClass(LogoActivity.this, MyService.class);
						bindService(service, mServiceConnection, BIND_AUTO_CREATE);
					}
					else {
						startActivity(new Intent(LogoActivity.this, WBAuthActivity.class));
						finish();
					}
				}
				else {
					startActivity(new Intent(LogoActivity.this, WBAuthActivity.class));
					finish();
				}
			}
		});
	}
	private void registerBoradcastReceiver() {
		// TODO Auto-generated method stub
		IntentFilter myIntentFilter = new IntentFilter();  
		myIntentFilter.addAction("com.bairutai.LogoActivity");  
		//注册广播        
		registerReceiver(MyReceiver, myIntentFilter);  
	}

}
