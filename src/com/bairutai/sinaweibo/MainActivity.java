package com.bairutai.sinaweibo;


import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
		private TabHost tabHost;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			this.getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
					WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
			setContentView(R.layout.main);
			
			tabHost = this.getTabHost();
			TabHost.TabSpec spec;
			Intent intent;
			
			intent = new Intent().setClass(this, MyContactActivity.class);
			spec = tabHost.newTabSpec("资料").setIndicator("资料").setContent(intent);
			tabHost.addTab(spec);
			
//			intent = new Intent().setClass(this, CityDialog.class);
//			spec = tabHost.newTabSpec("信息").setIndicator("信息").setContent(intent);
//			tabHost.addTab(spec);
			
			RadioGroup radioGroup = (RadioGroup) this.findViewById(R.id.main_radio);
			radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// TODO Auto-generated method stub
					switch (checkedId) {
					case R.id.radio_button0:// 首页
						tabHost.setCurrentTabByTag("首页");
						break;
					case R.id.radio_button1:// 信息
						tabHost.setCurrentTabByTag("信息");
						break;
					case R.id.radio_button2:// 资料
						tabHost.setCurrentTabByTag("资料");
						break;
					case R.id.radio_button3:// 搜索
						tabHost.setCurrentTabByTag("搜索");
						break;
					case R.id.radio_button4:// 更多
						tabHost.setCurrentTabByTag("更多");
						break;
					default:
						tabHost.setCurrentTabByTag("首页");
						break;
					}
				}
			});
		}
}
