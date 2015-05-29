package com.bairutai.sinaweibo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyMessageActivity extends Activity {
	private ActionBar my_actionbar;
	private View my_actionbar_view;
	private TextView title;
	private Button chatBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setActionbar();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setActionbar();
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
			title = (TextView) my_actionbar_view.findViewById(R.id.title_my_title);
			chatBtn = (Button) my_actionbar_view.findViewById(R.id.title_my_setBtn);
			title.setText("消息");
			chatBtn.setText("发起聊天");
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			my_actionbar.setCustomView(my_actionbar_view, layout);//设置actionbar视图
			my_actionbar.show();
		}
	}

}
