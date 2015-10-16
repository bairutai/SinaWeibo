package com.bairutai.sinaweibo;

import com.bairutai.application.WeiboApplication;
import com.bairutai.data.Constants;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.PlaceAPI;
import com.sina.weibo.sdk.openapi.models.PoiList;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.BDNotifyListener;//假如用到位置提醒功能，需要import该类
import com.baidu.location.LocationClientOption.LocationMode;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends Activity {

	//actionbar
	private ActionBar actionbar;
	private View view;
	private ImageView backBtn;
	private Button finishBtn;
	private TextView titleTxt;
	private LayoutInflater inflator;

	//主界面
	private PoiList poilist;
	private PullToRefreshListView listView;
	private PlaceAPI mPlaceAPI;
	private WeiboApplication application;
	private LocationManager locationManager;
	private SearchView searchView; 
	private String lat;
	private String lon;
	private String from;

	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location);
		application = (WeiboApplication)getApplication();
		actionbar = getActionBar();
		if (null != actionbar) {
			actionbar.setDisplayShowHomeEnabled(false);//左上角logo
			actionbar.setDisplayOptions(actionbar.DISPLAY_SHOW_CUSTOM); 
			actionbar.setDisplayShowCustomEnabled(true);
			inflator = LayoutInflater.from(this);
			view = inflator.inflate(R.layout.title_editdescription, null);//自定义actionbar视图
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			actionbar.setCustomView(view, layout);//设置actionbar视图
			backBtn = ( ImageView)findViewById(R.id.title_editdescription_back);
			finishBtn = (Button)findViewById(R.id.title_editdescription_finishBtn);
			titleTxt = (TextView)findViewById(R.id.title_editdescription_title);
			finishBtn.setText("删除位置");
			titleTxt.setText("我在这里");
		}
		mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
		mLocationClient.registerLocationListener( myListener );    //注册监听函数
		if (null != getIntent()){
			this.from = getIntent().getStringExtra("from");
			System.out.println("set from value");
			if (getIntent().getBooleanExtra("isLocation", false)){
				finishBtn.setVisibility(View.VISIBLE);
			}
			else {
				finishBtn.setVisibility(View.GONE);
			}
		}
		findView();		
		addListener();
	}

	private void findView() {
		// TODO Auto-generated method stub
		listView = (PullToRefreshListView)findViewById(R.id.location_listview);
		listView.getLoadingLayoutProxy().setLoadingDrawable(getResources().getDrawable(R.drawable.navigationbar_icon_refresh_white));
	}

	private void initScreen() {
		// TODO Auto-generated method stub
		LocationAdapter adapter = new LocationAdapter();
		listView.setAdapter(adapter);	
	}

	private class LocationAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null != poilist){
				return poilist.pois.size()+1;
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

			if (position == 0){
				convertView = (LinearLayout)inflator.inflate(R.layout.searchview, null);
				searchView = (SearchView)convertView.findViewById(R.id.search);
				int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
				TextView textView = (TextView) searchView.findViewById(id);
				textView.setTextColor(Color.GRAY); 
				searchView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(LocationActivity.this,SearchLocationActivity.class);
						if (!TextUtils.isEmpty(lat)&&!TextUtils.isEmpty(lon)){
							intent.putExtra("lat", lat);
							intent.putExtra("lon", lon);
						}
						startActivity(intent);
						overridePendingTransition(R.animator.zoom_enter,R.animator.zoom_exit);
					}
				});
			}else{	
				ViewHolder viewHolder = null;
				convertView = (LinearLayout)inflator.inflate(R.layout.location_listitem, null);
				viewHolder = new ViewHolder();
				viewHolder.txt1 = (TextView)convertView.findViewById(R.id.location_listitem_txt1);
				viewHolder.txt2 = (TextView)convertView.findViewById(R.id.location_listitem_txt2);
				convertView.setTag(viewHolder);
				if (null != poilist&&null != poilist.pois){
					viewHolder.txt1.setText(poilist.pois.get(position-1).title);
					viewHolder.txt2.setText(poilist.pois.get(position-1).checkin_user_num+"人去过-"+poilist.pois.get(position-1).address);
				}
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
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				initLocation();
				mLocationClient.start();
			}
		});

		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});


		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(LocationActivity.this,"position is "+position, Toast.LENGTH_LONG).show();
				// TODO Auto-generated method stub
				if (1 == position){

				}else{
					Intent intent = new Intent(LocationActivity.this, SendActivity.class);
					if (null != poilist && !from.isEmpty()){
						if(from.equalsIgnoreCase("addactivity")) {
							intent.putExtra("loaction", poilist.pois.get(position-2).title);
							intent.putExtra("title", "签到");
							startActivity(intent);
							finish();
							overridePendingTransition(R.animator.zoom_enter,R.animator.zoom_exit);
						}
						else if(from.equalsIgnoreCase("sendactivity")){
							intent.putExtra("location", poilist.pois.get(position-2).title);
							setResult(Constants.LOCATION_RESULT_OK, intent);
							finish();
						}
					}
				}
			}
		});
		
		finishBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(Constants.LOCATION_RESULT_DELETE);
				finish();
			}
		});
	}

	private void initLocation() {
		// TODO Auto-generated method stub
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
		int span=1000;
		option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);//可选，默认false,设置是否使用gps
		option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
		option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		mLocationClient.setLocOption(option);
	}


	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			System.out.println("getLocType() = "+location.getLocType());
			mLocationClient.stop();
			listView.onRefreshComplete();
			if (location.getLocType() == BDLocation.TypeServerError) {
				Toast.makeText(LocationActivity.this, "服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因", Toast.LENGTH_LONG).show();
			} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
				Toast.makeText(LocationActivity.this, "网络不同导致定位失败，请检查网络是否通畅", Toast.LENGTH_LONG).show();
			} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
				Toast.makeText(LocationActivity.this, "无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机", Toast.LENGTH_LONG).show();
			}else {
				mPlaceAPI = new PlaceAPI(LocationActivity.this, Constants.APP_KEY, application.getmAccessToken());
				String lat = String.valueOf(location.getLatitude());
				String lon = String.valueOf(location.getLongitude());
				setLat(lat);
				setLon(lon);
				mPlaceAPI.nearbyPois(lat,lon,5000,"","",50,1,1,false,mPlaceListener);
			}
		}

	}

	private RequestListener mPlaceListener = new RequestListener() {

		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onComplete(String response) {
			// TODO Auto-generated method stub
			System.out.println(response);
			if (!TextUtils.isEmpty(response)) {
				poilist = PoiList.parse(response);
				if (null != poilist){
					initScreen();
				}
			}
		}
	};

	public void setLat(String lat) {
		this.lat = lat;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}
}
