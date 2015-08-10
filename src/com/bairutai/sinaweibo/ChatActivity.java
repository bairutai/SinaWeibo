package com.bairutai.sinaweibo;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bairutai.Adapter.FaceAdapter;
import com.bairutai.Adapter.FacePageAdeapter;
import com.bairutai.Adapter.MessageAdapter;
import com.bairutai.application.WeiboApplication;
import com.bairutai.data.Constants;
import com.bairutai.model.MessageItem;
import com.bairutai.model.User;
import com.bairutai.openwidget.CirclePageIndicator;
import com.bairutai.openwidget.JazzyViewPager;
import com.bairutai.openwidget.JazzyViewPager.TransitionEffect;
import com.bairutai.sinaweibo.R;
import com.bairutai.tools.GetuiSdkHttpPost;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.igexin.sdk.PushManager;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ChatActivity extends Activity {

	static String appId = "4CBoHZXCdl9aQQaQ5Ivgu4";
	static String appkey = "PuCP2rAlwr8GTjMBpKQ6D9";
	static String mastersecret = "ITwBbpILpF8PoUlYTjSbW8";
	static String CID = "e08f0d07aefec02cf05a1b2eb1bf5e16";

	//actionbar
	private View mView;
	private ImageView backBtn;
	private Button setBtn;
	private TextView titleTxt;
	private LayoutInflater inflator;

	//输入框及表情
	private TransitionEffect mEffects[] = { TransitionEffect.Standard,
			TransitionEffect.Tablet, TransitionEffect.CubeIn,
			TransitionEffect.CubeOut, TransitionEffect.FlipVertical,
			TransitionEffect.FlipHorizontal, TransitionEffect.Stack,
			TransitionEffect.ZoomIn, TransitionEffect.ZoomOut,
			TransitionEffect.RotateUp, TransitionEffect.RotateDown,
			TransitionEffect.Accordion, };// 表情翻页效果
	private LinearLayout faceLinearLayout;
	private ImageButton faceBtn;
	private ImageButton voiceBtn;
	private Button talkButton;
	private Button sendButton;
	private ImageButton addBtn;
	private EditText msgEt;
	private JazzyViewPager faceViewPager;
	private CirclePageIndicator circlePageIndicator;
	private static User friendUser;
	private User meUser;
	private static WeiboApplication app;
	private InputMethodManager imm;//输入法
	private int currentPage = 0;
	private List<String> keys;
	private boolean isFaceShow = false;
	private boolean isInputShow = false;
	private boolean isTalking = false;
	private 	boolean isAddShow = false;
	private WindowManager.LayoutParams params;

	//添加的功能
	private LinearLayout addLinearLayout;
	private GridView addGridView;

	//chatlist
	private PullToRefreshListView listView;
	private int MsgPagerNum = 0;//消息记录的页数
	private static MessageAdapter adapter;
	private View m_Empty_view;
	private ImageView empty_img;
	private static SimpleDateFormat formatter;
	private static Date curDate;
	private List<MessageItem> list;
	public static Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what ==1){
				String data = (String)msg.obj;
				System.out.println("接收到了消息");
				MessageItem item = new MessageItem(MessageItem.MESSAGE_TYPE_TEXT, formatter.format(curDate), data, true, 0);
				app.mDataBaseHelper.addMessage(friendUser.id, item);
				adapter.upDateMsg(item);
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_main);
		formatter  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		curDate = new Date(System.currentTimeMillis());
		findView();
		initScreen();
		addListener();
		System.out.println("chatactivity   oncreate!");
	}

	private void findView() {
		// TODO Auto-generated method stub
		inflator = LayoutInflater.from(this);
		faceLinearLayout = (LinearLayout) findViewById(R.id.face_ll);
		faceBtn = (ImageButton)findViewById(R.id.face_btn);
		voiceBtn = (ImageButton)findViewById(R.id.voice_btn);
		talkButton = (Button)findViewById(R.id.talk_btn);
		sendButton = (Button)findViewById(R.id.send_btn);
		addBtn = (ImageButton)findViewById(R.id.add_btn);
		msgEt = (EditText)findViewById(R.id.msg_et);
		faceViewPager = (JazzyViewPager)findViewById(R.id.face_pager);
		circlePageIndicator = (CirclePageIndicator)findViewById(R.id.indicator);

		addLinearLayout = (LinearLayout)findViewById(R.id.panelLayout);
		addGridView = (GridView)findViewById(R.id.panel);

		listView = (PullToRefreshListView)findViewById(R.id.chatmain_listView);
		listView.getLoadingLayoutProxy().setLoadingDrawable(getResources().getDrawable(R.drawable.navigationbar_icon_refresh_white));
		m_Empty_view = inflator.inflate(R.layout.empty_view,null);
		empty_img = (ImageView)m_Empty_view.findViewById(R.id.empty_view_img);
		empty_img.setImageDrawable(getResources().getDrawable(R.drawable.empty_messages));
		ViewGroup viewGroup = (ViewGroup) listView.getParent();
		viewGroup.addView(m_Empty_view);
		listView.setEmptyView(m_Empty_view);
	}

	private void initScreen() {
		// TODO Auto-generated method stub
		app = (WeiboApplication)getApplication();
		meUser = app.getUser();

		if(getIntent().getStringExtra("userid")!=null){
			String value = getIntent().getStringExtra("userid");
			System.out.println(value);
			friendUser = app.mDataBaseHelper.queryUser(value);
		}else{
			finish();
		}

		initFacePage();
		initAddGridView();
		initMessageList();
	}

	private void initMessageList(){
		list = app.mDataBaseHelper.getMessage(friendUser.id, MsgPagerNum);
		adapter = new MessageAdapter(this, list,friendUser);
//		adapter.notifyDataSetChanged();
		listView.setAdapter(adapter);
		listView.getRefreshableView().setSelection(adapter.getCount());
	}

	private void initAddGridView() {
		// TODO Auto-generated method stu
		PanelAdapter panelAdapter = new PanelAdapter();
		addGridView.setAdapter(panelAdapter);
	}

	private class PanelAdapter extends BaseAdapter{
		int id[] = {R.drawable.message_more_camera,R.drawable.message_more_pic,
				R.drawable.message_more_poi,R.drawable.message_more_groupcard,
				R.drawable.message_more_groupluckybag,R.drawable.message_more_account};
		String ids[]  = {"拍照","相册","位置","群名片","群红包","收款"};
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 6;
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
			viewHolder.img.setImageDrawable(getResources().getDrawable(id[position]));
			viewHolder.txt.setText(ids[position]);
			return convertView;
		}

		private class ViewHolder {
			ImageView img;
			TextView txt;
		} 
	}

	private void initFacePage() {
		// TODO Auto-generated method stub
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		params = getWindow().getAttributes();

		Set<String> keySet = Constants.mSmileyMap.keySet();
		keys = new ArrayList<String>();
		keys.addAll(keySet);
		List<View> lv = new ArrayList<View>();
		for (int i = 0; i < 6; ++i){
			lv.add(getGridView(i));
		}
		FacePageAdeapter adapter = new FacePageAdeapter(lv, faceViewPager);
		faceViewPager.setAdapter(adapter);
		faceViewPager.setCurrentItem(0);
		faceViewPager.setTransitionEffect(mEffects[0]);
		circlePageIndicator.setViewPager(faceViewPager);
		adapter.notifyDataSetChanged();
		faceLinearLayout.setVisibility(View.GONE);
	}

	private View getGridView(int i) {
		// TODO Auto-generated method stub
		GridView gv = new GridView(this);
		gv.setNumColumns(7);
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));// 屏蔽GridView默认点击效果
		gv.setBackgroundColor(Color.TRANSPARENT);
		gv.setCacheColorHint(Color.TRANSPARENT);
		gv.setHorizontalSpacing(1);
		gv.setVerticalSpacing(1);
		gv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		gv.setGravity(Gravity.CENTER);
		gv.setAdapter(new FaceAdapter(this, i));
		gv.setOnTouchListener(forbidenScroll());
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg2 == 20) {// 删除键的位置
					int selection = msgEt.getSelectionStart();
					String text = msgEt.getText().toString();
					if (selection > 0) {
						String text2 = text.substring(selection - 1);
						if ("]".equals(text2)) {
							int start = text.lastIndexOf("[");
							int end = selection;
							msgEt.getText().delete(start, end);
							return;
						}
						msgEt.getText().delete(selection - 1, selection);
					}
				} else {
					int count = currentPage * 20 + arg2;
					// 注释的部分，在EditText中显示字符串
					// String ori = msgEt.getText().toString();
					// int index = msgEt.getSelectionStart();
					// StringBuilder stringBuilder = new StringBuilder(ori);
					// stringBuilder.insert(index, keys.get(count));
					// msgEt.setText(stringBuilder.toString());
					// msgEt.setSelection(index + keys.get(count).length());

					// 下面这部分，在EditText中显示表情
					Bitmap bitmap = BitmapFactory.decodeResource(getResources(), (Integer) Constants.mSmileyMap.values().toArray()[count]);
					if (bitmap != null) {
						int rawHeigh = bitmap.getHeight();
						int rawWidth = bitmap.getHeight();
						int newHeight = 60;
						int newWidth = 60;
						// 计算缩放因子
						float heightScale = ((float) newHeight) / rawHeigh;
						float widthScale = ((float) newWidth) / rawWidth;
						// 新建立矩阵
						Matrix matrix = new Matrix();
						matrix.postScale(heightScale, widthScale);
						// 设置图片的旋转角度
						// matrix.postRotate(-30);
						// 设置图片的倾斜
						// matrix.postSkew(0.1f, 0.1f);
						// 将图片大小压缩
						// 压缩后图片的宽和高以及kB大小均会变化
						Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0,
								rawWidth, rawHeigh, matrix, true);
						ImageSpan imageSpan = new ImageSpan(ChatActivity.this,
								newBitmap);
						String emojiStr = keys.get(count);
						SpannableString spannableString = new SpannableString(
								emojiStr);
						spannableString.setSpan(imageSpan,
								emojiStr.indexOf('['),
								emojiStr.indexOf(']') + 1,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						msgEt.append(spannableString);
					} else {
						String ori = msgEt.getText().toString();
						int index = msgEt.getSelectionStart();
						StringBuilder stringBuilder = new StringBuilder(ori);
						stringBuilder.insert(index, keys.get(count));
						msgEt.setText(stringBuilder.toString());
						msgEt.setSelection(index + keys.get(count).length());
					}
				}
			}
		});
		return gv;
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
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				imm.hideSoftInputFromWindow(msgEt.getWindowToken(), 0);
				faceLinearLayout.setVisibility(View.GONE);
				addLinearLayout.setVisibility(View.GONE);
				isFaceShow = false;
				isAddShow = false;
				isInputShow = false;
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				imm.hideSoftInputFromWindow(msgEt.getWindowToken(), 0);
//				faceLinearLayout.setVisibility(View.GONE);
//				addLinearLayout.setVisibility(View.GONE);
//				isFaceShow = false;
//				isAddShow = false;
//				isInputShow = false;
//				return false;
//			}
//		});

		msgEt.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				imm.showSoftInput(msgEt, 0);
				faceLinearLayout.setVisibility(View.GONE);
				addLinearLayout.setVisibility(View.GONE);
				faceBtn.setImageDrawable(getResources().getDrawable(R.drawable.pop_btn_face_selector));
				addBtn.setImageDrawable(getResources().getDrawable(R.drawable.pop_btn_add_selector));
				listView.getRefreshableView().setSelection(adapter.getCount());
				isFaceShow = false;
				isAddShow = false;
				isInputShow = true;
				return false;
			}
		});

		msgEt.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (params.softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
							|| isFaceShow) {
						faceLinearLayout.setVisibility(View.GONE);
						isFaceShow = false;
						// imm.showSoftInput(msgEt, 0);
						return true;
					}
				}
				return false;
			}
		});

		msgEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {
					addBtn.setVisibility(View.GONE);
					sendButton.setVisibility(View.VISIBLE);
				} else {
					addBtn.setVisibility(View.VISIBLE);
					sendButton.setVisibility(View.GONE);
				}
			}
		});

		voiceBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				faceBtn.setImageDrawable(getResources().getDrawable(R.drawable.pop_btn_face_selector));
				addBtn.setImageDrawable(getResources().getDrawable(R.drawable.pop_btn_add_selector));
				faceLinearLayout.setVisibility(View.GONE);
				addLinearLayout.setVisibility(View.GONE);
				isFaceShow = false;
				isInputShow = false;
				if(!isTalking){
					voiceBtn.setImageDrawable(getResources().getDrawable(R.drawable.choose_become_light));
					msgEt.setVisibility(View.GONE);
					faceBtn.setVisibility(View.GONE);
					talkButton.setVisibility(View.VISIBLE);
					imm.hideSoftInputFromWindow(msgEt.getWindowToken(), 0);				
					isTalking = true;
				}else {
					voiceBtn.setImageDrawable(getResources().getDrawable(R.drawable.pop_btn_talk_selector));
					talkButton.setVisibility(View.GONE);
					msgEt.setVisibility(View.VISIBLE);
					faceBtn.setVisibility(View.VISIBLE);
					imm.showSoftInput(msgEt, 0);
					isInputShow = true;
					isTalking = false;
				}
			}
		});

		faceBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addLinearLayout.setVisibility(View.GONE);
				addBtn.setImageDrawable(getResources().getDrawable(R.drawable.pop_btn_add_selector));
				isAddShow = false;
				if (!isFaceShow) {
					imm.hideSoftInputFromWindow(msgEt.getWindowToken(), 0);
					try {
						Thread.sleep(80);// 解决此时会黑一下屏幕的问题
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					faceLinearLayout.setVisibility(View.VISIBLE);
					faceBtn.setImageDrawable(getResources().getDrawable(R.drawable.choose_become_light));
					isFaceShow = true;
				} else {
					imm.showSoftInput(msgEt, 0);
					faceLinearLayout.setVisibility(View.GONE);
					faceBtn.setImageDrawable(getResources().getDrawable(R.drawable.pop_btn_face_selector));
					isFaceShow = false;
				}
			}
		});

		addBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				faceLinearLayout.setVisibility(View.GONE);
				faceBtn.setImageDrawable(getResources().getDrawable(R.drawable.pop_btn_face_selector));
				isFaceShow = false;
				if(!isAddShow){
					if(isTalking){
						voiceBtn.setImageDrawable(getResources().getDrawable(R.drawable.pop_btn_talk_selector));
						talkButton.setVisibility(View.GONE);
						msgEt.setVisibility(View.VISIBLE);
						faceBtn.setVisibility(View.VISIBLE);
						isTalking = false;
					}
					imm.hideSoftInputFromWindow(msgEt.getWindowToken(), 0);
					addLinearLayout.setVisibility(View.VISIBLE);
					addBtn.setImageDrawable(getResources().getDrawable(R.drawable.choose_become_light));
					isAddShow = true;
				}else{
					imm.showSoftInput(msgEt, 0);
					addLinearLayout.setVisibility(View.GONE);
					addBtn.setImageDrawable(getResources().getDrawable(R.drawable.pop_btn_add_selector));
					isAddShow = false;
				}
			}
		});

		circlePageIndicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				currentPage = arg0;
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

		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String msg = msgEt.getText().toString();
				MessageItem item = new MessageItem(MessageItem.MESSAGE_TYPE_TEXT, formatter.format(curDate), msg, false, 0);
				if (list == null) {
					list = new LinkedList<MessageItem>();
					list.add(item);
					adapter.setMessageList(list);
				}else{
					adapter.upDateMsg(item);
				}
				app.mDataBaseHelper.addMessage(WeiboApplication.getUser().id, item);
				msgEt.setText("");
				if (isNetworkConnected()) {
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("action", "pushmessage"); // pushmessage为接口名，注意全部小写
					/*---以下代码用于设定接口相应参数---*/
					param.put("appkey", appkey);
					param.put("appid", appId);
					// 注：透传内容后面需用来验证接口调用是否成功，假定填写为hello girl~
					param.put("data", msg);
					param.put("time", formatter.format(curDate)); // 当前请求时间，可选
					param.put("clientid", PushManager.getInstance().getClientid(ChatActivity.this.getApplicationContext())); // 您获取的ClientID
					param.put("expire", 3600); // 消息超时时间，单位为秒，可选

					// 生成Sign值，用于鉴权
					param.put("sign", GetuiSdkHttpPost.makeSign(mastersecret, param));

					GetuiSdkHttpPost.httpPost(param);
				} else {
					Toast.makeText(ChatActivity.this, "对不起，当前网络不可用!", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("chatactivity   onResume!!");
		setActionbar();
	}

	public boolean isNetworkConnected() {
		// 判断网络是否连接
		ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		return mNetworkInfo != null && mNetworkInfo.isAvailable();
	}

	private void setActionbar() {
		// TODO Auto-generated method stub
		ActionBar actionBar = getActionBar();
		if (null != actionBar) {
			actionBar.setDisplayShowHomeEnabled(false);//返回键
			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
			actionBar.setDisplayShowCustomEnabled(true);
			mView = inflator.inflate(R.layout.title_editdescription, null);//自定义actionbar视图
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			actionBar.setCustomView(mView, layout);//设置actionbar视图
			backBtn = ( ImageView)findViewById(R.id.title_editdescription_back);
			setBtn = (Button)findViewById(R.id.title_editdescription_finishBtn);
			titleTxt = (TextView)findViewById(R.id.title_editdescription_title);
			titleTxt.setText(friendUser.screen_name);
			setBtn.setText("设置");
			backBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
		}	
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(isInputShow) {
			imm.hideSoftInputFromWindow(msgEt.getWindowToken(), 0);
		}
	}

}
