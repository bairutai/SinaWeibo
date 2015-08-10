package com.bairutai.sinaweibo;

import java.util.ArrayList;
import java.util.List;

import com.bairutai.Adapter.Status_Com_Res_Atti_PageAdapter;
import com.bairutai.openwidget.MyViewPager;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AddActivity extends Activity {
	private MyViewPager viewPager;
	private LayoutInflater inflator;
	private LinearLayout closeLinearLayout1;
	private ImageView closeBtn1;
	private LinearLayout closeLinearLayout2;
	private ImageView closeBtn2;
	private ImageView returnBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);
		findView();
		initScreen();
		addListener();
	}
	
	private void findView() {
		// TODO Auto-generated method stub
		inflator = LayoutInflater.from(this);
		viewPager = (MyViewPager)findViewById(R.id.add_viewpager);
		closeLinearLayout1 = (LinearLayout)findViewById(R.id.add_closeLayout1);
		closeLinearLayout2 = (LinearLayout)findViewById(R.id.add_closeLayout2);
		closeBtn1 = (ImageView)findViewById(R.id.add_close1);
		closeBtn2 = (ImageView)findViewById(R.id.add_close2);
		returnBtn = (ImageView)findViewById(R.id.add_return);
	}
	
	private void initScreen() {
		// TODO Auto-generated method stub
		List<View> views = new ArrayList<View>();
		for (int i = 0; i <2; i++) {
			views.add(getGridView(i));
		}
		Status_Com_Res_Atti_PageAdapter adapter = new Status_Com_Res_Atti_PageAdapter(views);
		viewPager.setAdapter(adapter);
	}
	
	private View getGridView(int i) {
		// TODO Auto-generated method stub
		GridView gv = new GridView(this);
		gv.setNumColumns(3);
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));// 屏蔽GridView默认点击效果
		gv.setBackgroundColor(Color.TRANSPARENT);
		gv.setCacheColorHint(Color.TRANSPARENT);
		gv.setHorizontalSpacing(1);
		gv.setVerticalSpacing(10);
		gv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		gv.setGravity(Gravity.CENTER);
		gv.setAdapter(new AddAdapter(i));
		gv.setOnTouchListener(forbidenScroll());
		if (0 == i){
			gv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					switch (position) {
					case 0:
						break;
					case 3:
						Intent intent = new Intent(AddActivity.this, LocationActivity.class);
						startActivity(intent);
						finish();
						break;
					case 5:
						setCurrentPage(1);
						break;
					default:
						break;
					}
				}
			});
		}
		return gv;
	}
	
	protected void setCurrentPage(int i) {
		// TODO Auto-generated method stub
		if (0 == i){
			viewPager.setCurrentItem(0);
			closeLinearLayout1.setVisibility(View.VISIBLE);
			closeLinearLayout2.setVisibility(View.GONE);
		}else{
			viewPager.setCurrentItem(1);
			closeLinearLayout1.setVisibility(View.GONE);
			closeLinearLayout2.setVisibility(View.VISIBLE);
		}
	}

	private class AddAdapter extends BaseAdapter {
        private int i;
		int id[] = {R.drawable.tabbar_compose_idea,R.drawable.tabbar_compose_photo,
				R.drawable.tabbar_compose_camera,R.drawable.tabbar_compose_lbs,
				R.drawable.tabbar_compose_review,R.drawable.tabbar_compose_more};
		String ids[]  = {"文字","相册","拍摄","签到","点评","更多"};
		int id1[] = {R.drawable.tabbar_compose_friend,R.drawable.tabbar_compose_shooting,
				R.drawable.tabbar_compose_transfer,R.drawable.tabbar_compose_voice,
				R.drawable.tabbar_compose_weibo};
		String ids1[]  = {"好友圈","微博相机","收款","音乐","长微博"};
		public AddAdapter(int i) {
			// TODO Auto-generated constructor stub
			this.i = i;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (0==i) {
				return 6;
			}else if(1==i){
				return 5;
			}else{
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			if(convertView == null){
				convertView = (RelativeLayout)inflator.inflate(R.layout.message_more_gridview_item, null);
				viewHolder = new ViewHolder();
				viewHolder.img = (ImageView)convertView.findViewById(R.id.message_more_gridview_item_img);
				viewHolder.txt = (TextView)convertView.findViewById(R.id.message_more_gridview_item_txt);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder)convertView.getTag();
			}
			if (0 == i){
				viewHolder.img.setImageDrawable(getResources().getDrawable(id[position]));
				viewHolder.txt.setText(ids[position]);
			}else{
				viewHolder.img.setImageDrawable(getResources().getDrawable(id1[position]));
				viewHolder.txt.setText(ids1[position]);
			}
			return convertView;
		}
		
		private class ViewHolder {
			ImageView img;
			TextView txt;
		}
		
	}
	
	private OnTouchListener forbidenScroll() {
		// TODO Auto-generated method stub
		return new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					return true;
				}
				return false;
			}
		};
	}

	private void addListener() {
		// TODO Auto-generated method stub
		closeBtn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		closeBtn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		returnBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setCurrentPage(0);
			}
		});
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				setCurrentPage(arg0);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
	}
}
