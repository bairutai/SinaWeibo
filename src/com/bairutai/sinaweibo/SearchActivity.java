package com.bairutai.sinaweibo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

public class SearchActivity extends Activity {

	private ActionBar my_actionbar;
	private View my_actionbar_view;
	private SearchView search_view;
	private ImageView voiceBtn;
	
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
			my_actionbar_view = inflator.inflate(R.layout.title_search, null);//自定义actionbar视图
			voiceBtn = (ImageView) my_actionbar_view.findViewById(R.id.title_search_voice);
			search_view = (SearchView)my_actionbar_view.findViewById(R.id.title_search_search);
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			my_actionbar.setCustomView(my_actionbar_view, layout);//设置actionbar视图
			int id = search_view.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
			TextView textView = (TextView) search_view.findViewById(id);
			textView.setTextSize(15);
			my_actionbar.show();
		}
	}
}
