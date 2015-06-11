package com.bairutai.sinaweibo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.touchgallery.GalleryWidget.BasePagerAdapter.OnItemChangeListener;
import com.touchgallery.GalleryWidget.GalleryViewPager;
import com.touchgallery.GalleryWidget.UrlPagerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ImageExploreActivity extends Activity {
	
	private GalleryViewPager mViewPager;
	private ArrayList<String> url;
	private int length;
	private Button saveBtn;
	private Button yuantuBtn;
	private TextView nowImgTxt;
	private UrlPagerAdapter pagerAdapter;
	private RelativeLayout layout;
	private int now_img;
//	private DisplayMetrics dm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageexplore);
//		dm = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(dm);
		findView();
		initScreen();
		addListener();
	}
	
	private void addListener() {
		// TODO Auto-generated method stub
		pagerAdapter.setOnItemChangeListener(new OnItemChangeListener()
		{
			@Override
			public void onItemChange(int currentPosition)
			{
				nowImgTxt.setText((currentPosition+1)+"/"+length);
			}
		});
		layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void initScreen() {
		// TODO Auto-generated method stub
		url = getIntent().getStringArrayListExtra("image");
		now_img = getIntent().getIntExtra("nowImage",0);
		length = url.size();
		pagerAdapter = new UrlPagerAdapter(this, url);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setAdapter(pagerAdapter);
		mViewPager.setCurrentItem(now_img);
	}

	private void findView() {
		// TODO Auto-generated method stub
		saveBtn = (Button)findViewById(R.id.imageexplore_save);
		yuantuBtn = (Button)findViewById(R.id.imageexplore_clear);
		nowImgTxt = (TextView)findViewById(R.id.imageexplore_txt);
		mViewPager = (GalleryViewPager)findViewById(R.id.imageexplore_img);
		layout = (RelativeLayout)findViewById(R.id.imageexplore_layout);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("imageexplore onresume");
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		System.out.println("imageexplore onpause");
		super.onPause();
	}
}
