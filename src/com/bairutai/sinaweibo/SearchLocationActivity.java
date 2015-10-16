package com.bairutai.sinaweibo;

import com.bairutai.application.WeiboApplication;
import com.bairutai.data.Constants;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.PlaceAPI;
import com.sina.weibo.sdk.openapi.models.PoiList;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

public class SearchLocationActivity extends Activity {
	
	//actionbar
	private ActionBar my_actionbar;
	private View my_actionbar_view;
	private SearchView search_view;
	private RelativeLayout cancle_layout;
	private Button cancleBtn;
	private Button searchBtn;
	
	//主界面
	private LayoutInflater inflator;
	private PlaceAPI mPlaceAPI;
	private WeiboApplication application;
	private PoiList poilist;
	private PullToRefreshListView listview;
	private String lat;
	private String lon;
	private InputMethodManager imm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location);
		application = (WeiboApplication)getApplication();
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		if (null !=getIntent()){
			lat = getIntent().getStringExtra("lat");
			lon = getIntent().getStringExtra("lon");
		}
		my_actionbar = getActionBar();
		if (null != my_actionbar) {
			my_actionbar.setDisplayShowHomeEnabled(false);//去左上角LOGO
			my_actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
			my_actionbar.setDisplayShowCustomEnabled(true);
			inflator = (LayoutInflater) this.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);//布局加载器
			my_actionbar_view = inflator.inflate(R.layout.title_search_location, null);//自定义actionbar视图
			cancle_layout = (RelativeLayout)my_actionbar_view.findViewById(R.id.title_search_location_cancle_layout);
			search_view = (SearchView)my_actionbar_view.findViewById(R.id.title_search_location_search);			
			cancleBtn = (Button)my_actionbar_view.findViewById(R.id.title_search_location_cancleBtn);
			searchBtn = (Button)my_actionbar_view.findViewById(R.id.title_search_location_searchBtn);
			cancle_layout.setVisibility(View.VISIBLE);
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			my_actionbar.setCustomView(my_actionbar_view, layout);//设置actionbar视图
			int id = search_view.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
			TextView textView = (TextView) search_view.findViewById(id);
			textView.setTextSize(15);
			textView.setTextColor(Color.GRAY);
		}
		
		findView();
		addListener();
	}

	private void findView() {
		// TODO Auto-generated method stub
		listview = (PullToRefreshListView)findViewById(R.id.location_listview);
		listview.getLoadingLayoutProxy().setLoadingDrawable(getResources().getDrawable(R.drawable.navigationbar_icon_refresh_white));
	}

	private void initScreen() {
		// TODO Auto-generated method stub
		LocationAdapter adapter = new LocationAdapter();
		listview.setAdapter(adapter);	
	}

	private class LocationAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null != poilist){
				return poilist.pois.size();
			}else {
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
				ViewHolder viewHolder = null;
				convertView = (LinearLayout)inflator.inflate(R.layout.location_listitem, null);
				viewHolder = new ViewHolder();
				viewHolder.txt1 = (TextView)convertView.findViewById(R.id.location_listitem_txt1);
				viewHolder.txt2 = (TextView)convertView.findViewById(R.id.location_listitem_txt2);
				convertView.setTag(viewHolder);
				if (null != poilist&&null != poilist.pois){
					viewHolder.txt1.setText(poilist.pois.get(position).title);
					viewHolder.txt2.setText(poilist.pois.get(position).checkin_user_num+"人去过-"+poilist.pois.get(position).address);
				}
			return convertView;
		}

	}
	
	private class ViewHolder{
		TextView txt1;
		TextView txt2;
	}
	
	private void addListener() {
		// TODO Auto-generated method stub
		cancleBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.animator.zoom_enter,R.animator.zoom_exit);
			}
		});
		
		search_view.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(newText)){
					cancleBtn.setVisibility(View.VISIBLE);
					searchBtn.setVisibility(View.GONE);
					if (imm.isActive()){
						imm.hideSoftInputFromWindow(search_view.getWindowToken(), 0);
					}
				}else{
					cancleBtn.setVisibility(View.GONE);
					searchBtn.setVisibility(View.VISIBLE);
				}
				return true;
			}
		});
		
		searchBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (imm.isActive()){
					imm.hideSoftInputFromWindow(search_view.getWindowToken(), 0);
				}
				mPlaceAPI = new PlaceAPI(SearchLocationActivity.this, Constants.APP_KEY, application.getmAccessToken());
				mPlaceAPI.nearbyPois(lat,lon,10000,search_view.getQuery().toString(),"",50,1,1,false,mPlaceListener);
			}
		});
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SearchLocationActivity.this, SendActivity.class);
				if (null != poilist) {
					intent.putExtra("loaction", poilist.pois.get(position).title);
				}
				startActivity(intent);
				finish();
				overridePendingTransition(R.animator.zoom_enter,R.animator.zoom_exit);
			}
		});
	}
	
	private RequestListener mPlaceListener = new RequestListener() {

		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onComplete(String response) {
			// TODO Auto-generated method stub振
			System.out.println(response);
			if (!TextUtils.isEmpty(response)&&!response.equalsIgnoreCase("[]")) {
				poilist = PoiList.parse(response);
				if (null != poilist){
					initScreen();
				}
			}else{
				Toast.makeText(SearchLocationActivity.this, "没有搜到该位置", Toast.LENGTH_LONG).show();
				poilist = null;
			}
		}
	};
}
