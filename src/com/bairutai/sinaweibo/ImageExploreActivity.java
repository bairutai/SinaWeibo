package com.bairutai.sinaweibo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.bairutai.tools.AsyncBitmapLoader;
import com.squareup.picasso.Picasso;
import com.touchgallery.GalleryWidget.BasePagerAdapter.OnItemChangeListener;
import com.touchgallery.GalleryWidget.GalleryViewPager;
import com.touchgallery.GalleryWidget.GalleryViewPager.OnItemClickListener;
import com.touchgallery.GalleryWidget.UrlPagerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class ImageExploreActivity extends Activity {

	private GalleryViewPager mViewPager;
	private ArrayList<String> url;
	private int length;
	private Button saveBtn;
	private Button yuantuBtn;
	private TextView nowImgTxt;
	private UrlPagerAdapter pagerAdapter;
	private int now_img;
	private ProgressBar mProgressBar;
	private RelativeLayout layout;
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
				yuantuBtn.setVisibility(View.VISIBLE);
				if(mProgressBar.isActivated()){
					mProgressBar.setVisibility(View.GONE);
				}
				nowImgTxt.setText((currentPosition+1)+"/"+length);
			}
		});
		mViewPager.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClicked(View view, int position) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mViewPager.mCurrentView.getImageBitmap()!=null){
					MediaStore.Images.Media.insertImage(getContentResolver(),
							mViewPager.mCurrentView.getImageBitmap(), null, null);
					Toast.makeText(ImageExploreActivity.this, "图片已保存至手机相册", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(ImageExploreActivity.this, "图片有误", Toast.LENGTH_LONG).show();
				}
			}
		});

		yuantuBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("yuantuBtn is click");
				yuantuBtn.setVisibility(View.INVISIBLE);
				int index=mViewPager.getCurrentItem();
				if(url!=null){
					System.out.println("url is not null");
					mProgressBar.setVisibility(View.VISIBLE);
					AsyncBitmapLoader bitmapLoader = new AsyncBitmapLoader();
					bitmapLoader.setProgressBar(mProgressBar);
					bitmapLoader.execute(mViewPager.mCurrentView,url.get(index).replaceAll("bmiddle", "large"));
				}else{
					System.out.println("url is null");
					Toast.makeText(ImageExploreActivity.this, "加载原有图片出错", Toast.LENGTH_LONG).show();
				}
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

		mProgressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		params.setMargins(30, 0, 30, 0);
		mProgressBar.setLayoutParams(params);
		mProgressBar.setIndeterminate(false);
		mProgressBar.setMax(100);
		mProgressBar.setVisibility(View.GONE);
		layout.addView(mProgressBar);
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
