package com.bairutai.sinaweibo;



import java.util.ArrayList;
import java.util.List;

import com.bairutai.Adapter.StatusAdapter;
import com.bairutai.Adapter.StatusCommentsAdapter;
import com.bairutai.Adapter.Status_Com_Res_Atti_PageAdapter;
import com.bairutai.application.WeiboApplication;
import com.bairutai.data.Constants;
import com.bairutai.model.Status;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.models.CommentList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class StatusActivity extends Activity {
	//actionbar
	private View mActionbarView;
	private ImageView backBtn;
	private ImageView moreBtn;
	private TextView titleTxt;
	private LayoutInflater inflater;

	//主界面
	private PullToRefreshListView mPullToRefreshListView;
	private LinearLayout status_reposts_layout;
	private LinearLayout status_comments_layout;
	private LinearLayout status_attitudes_layout;
	private ImageView status_attitudes_img;

	private WeiboApplication app;
	private Status status;
	private boolean isLike = false;
	

	private ViewPager mViewPager;
	private List<View> listViews;
	private CommentsAPI mCommentsAPI;
	private StatusAdapter statusAdapter;
	private StatusCommentsAdapter mStatusCommentsAdapter;
	private ListView commentsListView;
	private View commentsListView_layout = null;
	private ListView repostListView;
	private View repostListView_layout = null;
	private ListView attitudesListView;
	private View attitudesListView_layout = null;
	private Status_Com_Res_Atti_PageAdapter mStatus_Com_Res_Atti_PageAdapter;
	private View empty_viewView;
	private ImageView empty_img;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statuslayout_);
		app = (WeiboApplication) getApplication();
		if(getIntent().getStringExtra("statusid")!=null){
			String value = "where status_id = "+getIntent().getStringExtra("statusid");
			System.out.println(value);
			status = app.mDataBaseHelper.querySingleStatus(value);
		}else{
			Long value = getIntent().getLongExtra("ret_statusid", 0);
			status = app.mDataBaseHelper.queryRetweetedStatus(value);
		}
		
		findView();
		initScreen();
		addListener();
	}

	private void findView() {
		// TODO Auto-generated method stub
		status_reposts_layout = (LinearLayout)findViewById(R.id.status_reposts_layout);
		status_attitudes_layout = (LinearLayout)findViewById(R.id.status_attitudes_layout);
		status_comments_layout = (LinearLayout)findViewById(R.id.status_comment_layout);		
		status_attitudes_img = (ImageView)findViewById(R.id.status_attitudes_img);
		mViewPager = (ViewPager)findViewById(R.id.statuslayout_ViewPager);
		mPullToRefreshListView = (PullToRefreshListView)findViewById(R.id.statuslayout_PullToRefreshListView);
		mPullToRefreshListView.setLoadingDrawable(getResources().getDrawable(R.drawable.navigationbar_icon_refresh_white));
	}

	private void initScreen()  {
		// TODO Auto-generated method stub
		statusAdapter  = new StatusAdapter(this, status, app);
		statusAdapter.whichIsClick = 1;
		mPullToRefreshListView.setAdapter(statusAdapter);
		
		listViews = new ArrayList<View>();
		inflater = LayoutInflater.from(this);
		repostListView_layout = inflater.inflate(R.layout.reposts_listview, null);
		commentsListView_layout = inflater.inflate(R.layout.comments_listview, null);
		attitudesListView_layout = inflater.inflate(R.layout.attitudes_listview, null);
		empty_viewView = inflater.inflate(R.layout.empty_view, null);
		empty_img = (ImageView)empty_viewView.findViewById(R.id.empty_view_img);
		empty_img.setImageDrawable(getResources().getDrawable(R.drawable.empty_default));
		listViews.add(repostListView_layout);
		listViews.add(commentsListView_layout);
		listViews.add(attitudesListView_layout);
		repostListView = (ListView)listViews.get(0).findViewById(R.id.reposts_listview);
		repostListView.setEmptyView(empty_viewView);
		commentsListView = (ListView)listViews.get(1).findViewById(R.id.comments_listview);
		commentsListView.setEmptyView(empty_viewView);
		attitudesListView = (ListView)listViews.get(2).findViewById(R.id.attitudes_listview);
		attitudesListView.setEmptyView(empty_viewView);
		mStatus_Com_Res_Atti_PageAdapter = new Status_Com_Res_Atti_PageAdapter(listViews);
		mViewPager.setAdapter(mStatus_Com_Res_Atti_PageAdapter);
		mViewPager.setCurrentItem(1);
		LoadData(1);
	}

	public void LoadData(int whichIsClick) {
		// TODO Auto-generated method stub
		mViewPager.setCurrentItem(whichIsClick);
		switch (whichIsClick) {
		case 0:
			repostListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[] {"获取不到该数据"}));
			mStatus_Com_Res_Atti_PageAdapter.notifyDataSetChanged();
			break;
		case 1:
			mCommentsAPI = new CommentsAPI(this, Constants.APP_KEY, app.getmAccessToken());
			mCommentsAPI.show(Long.parseLong(status.id), 0, 0, 50, 1, CommentsAPI.AUTHOR_FILTER_ALL, CommentListener);
			break;
		case 2:
			attitudesListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[] {"获取不到该数据"}));
			mStatus_Com_Res_Atti_PageAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

	private void addListener() {
		// TODO Auto-generated method stub

		status_attitudes_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isLike){
					status_attitudes_img.setImageDrawable(getResources().getDrawable(R.drawable.toolbar_icon_unlike));
					isLike = false;
				}else{
					status_attitudes_img.setImageDrawable(getResources().getDrawable(R.drawable.toolbar_icon_like));
					isLike = true;
				}
			}
		});
		
		mPullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

			}
		});
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				LoadData(arg0);
				statusAdapter.whichIsClick = arg0;
				statusAdapter.setColor(arg0);
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		//自定义actionBar;
		super.onResume();
		ActionBar actionBar = getActionBar();
		if (null != actionBar) {
			actionBar.setDisplayShowHomeEnabled(false);//左上角logo
			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
			actionBar.setDisplayShowCustomEnabled(true);
			inflater = LayoutInflater.from(this);
			mActionbarView = inflater.inflate(R.layout.title_mycard, null);//加载actionbar视图
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			actionBar.setCustomView(mActionbarView, layout);//设置actionbar视图
			backBtn = (ImageView)findViewById(R.id.title_mycard_back);
			moreBtn = (ImageView)findViewById(R.id.title_mycard_more);
			titleTxt = (TextView)findViewById(R.id.title_mycard_mycard);
			titleTxt.setText("微博正文");
		}

		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}
	
	private RequestListener CommentListener = new RequestListener() {
		
		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onComplete(String response) {
			// TODO Auto-generated method stub
			if (!TextUtils.isEmpty(response)) {
				System.out.println(response);
				CommentList commentlist = CommentList.parse(response);
				mStatusCommentsAdapter = new StatusCommentsAdapter(StatusActivity.this, commentlist);
				if(commentsListView == null){
					System.out.println("commentsListView == null");
				}
				commentsListView.setAdapter(mStatusCommentsAdapter);
				mStatus_Com_Res_Atti_PageAdapter.notifyDataSetChanged();
			}
			else {
			
			}
		}
	};
}
