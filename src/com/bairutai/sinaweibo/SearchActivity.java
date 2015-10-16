package com.bairutai.sinaweibo;

import java.util.ArrayList;

import com.convenientbanner.CBViewHolderCreator;
import com.convenientbanner.ConvenientBanner;
import com.convenientbanner.ConvenientBanner.Transformer;
import com.convenientbanner.LocalImageHolderView;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

public class SearchActivity extends Activity {

	private ActionBar my_actionbar;
	private View my_actionbar_view;
	private SearchView search_view;
	private ImageView voiceBtn;

	private ConvenientBanner convenientBanner;//顶部广告栏控件
	private String[] images = {"http://img2.imgtn.bdimg.com/it/u=3093785514,1341050958&fm=21&gp=0.jpg",
			"http://img2.3lian.com/2014/f2/37/d/40.jpg",
			"http://d.3987.com/sqmy_131219/001.jpg",
			"http://img2.3lian.com/2014/f2/37/d/39.jpg",
			"http://www.8kmm.com/UploadFiles/2012/8/201208140920132659.jpg",
			"http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg",
			"http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"
	};
	private ArrayList<Integer> localImages = new ArrayList<Integer>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		findView();
		initScreen();
	}

	private void initScreen() {
		// TODO Auto-generated method stub
		localImages.add(R.drawable.ic_test_0);
		localImages.add(R.drawable.ic_test_1);
		localImages.add(R.drawable.ic_test_2);
		localImages.add(R.drawable.ic_test_3);
		localImages.add(R.drawable.ic_test_4);
		localImages.add(R.drawable.ic_test_5);
		localImages.add(R.drawable.ic_test_6);
		convenientBanner.setPages(
	                new CBViewHolderCreator<LocalImageHolderView>() {
	                    @Override
	                    public LocalImageHolderView createHolder() {
	                        return new LocalImageHolderView();
	                    }
	                }, localImages)
	                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
	                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
	                //设置翻页的效果，不需要翻页效果可用不设
	                .setPageTransformer(Transformer.DefaultTransformer);
		
	}

	private void findView() {
		// TODO Auto-generated method stub
		convenientBanner = (ConvenientBanner) findViewById(R.id.convenientBanner);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setActionbar();
		convenientBanner.startTurning(5000);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		convenientBanner.stopTurning();
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
