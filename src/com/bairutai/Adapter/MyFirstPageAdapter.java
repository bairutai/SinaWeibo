package com.bairutai.Adapter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ajra.multiactiontextview.InputObject;
import com.ajra.multiactiontextview.MultiActionTextView;
import com.ajra.multiactiontextview.MultiActionTextviewClickListener;
import com.bairutai.application.WeiboApplication;
import com.bairutai.data.Constants;
import com.bairutai.sinaweibo.ImageExploreActivity;
import com.bairutai.sinaweibo.MyBaseInfoActivity;
import com.bairutai.sinaweibo.R;
import com.bairutai.sinaweibo.WebExploreActivity;
import com.bairutai.model.Status;
import com.sina.weibo.sdk.openapi.legacy.ActivityInvokeAPI;
import com.squareup.picasso.Picasso;

import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.sax.StartElementListener;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MyFirstPageAdapter extends BaseAdapter {
	//	private StatusList statusList;
	private LayoutInflater mLayoutInflater;
	private Context context;
	private ArrayList<Status> statusList;
	private WeiboApplication app;

	//moreDialog
	private View moreDialog_layout;
	private Dialog moreDialog;
	private DisplayMetrics dm;
	private ListView moreDialog_list;
	
	private MyMultiActionClickListener myMultiActionClickListener;
	private final int TOPIC_CLICKED = 1;
	private final int AT_CLICKED = 2;
	private final int WEB_CLICKED = 3;

	private SpannableStringBuilder stringBuilder = null;
	private 	int id[] = { R.id.img1, R.id.img2, R.id.img3, R.id.img4, R.id.img5,
			R.id.img6, R.id.img7, R.id.img8, R.id.img9 };

	public MyFirstPageAdapter(Context context, WeiboApplication app) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.app = app;
		mLayoutInflater = LayoutInflater.from(context);
		myMultiActionClickListener = new MyMultiActionClickListener();
		statusList = app.mDataBaseHelper.queryStatus();
		if(statusList!=null){
			app.setSince_id(Long.parseLong(statusList.get(0).id));
		}else{
			app.setSince_id((long) 0);
		}
		prepareDialog();
	}

	private void prepareDialog() {
		// TODO Auto-generated method stub
		moreDialog_layout = mLayoutInflater.inflate(R.layout.follow_status_more_dialog, null);
		moreDialog_list = (ListView) moreDialog_layout.findViewById(R.id.follow_status_more_dialog_list);
		moreDialog_list.setAdapter(new ArrayAdapter<String>(context, R.layout.list_item_ziti,new String[] {
				"收藏", "帮上头条","取消关注","屏蔽","举报"
		}));
		moreDialog=new Dialog(context,R.style.MyDialog);
		moreDialog.setContentView(moreDialog_layout);
		moreDialog.setCanceledOnTouchOutside(true);
		dm = new DisplayMetrics();
		dm = app.getResources().getDisplayMetrics();
		WindowManager.LayoutParams params = moreDialog.getWindow().getAttributes();
		params.width = dm.widthPixels/3*2;
		params.height = dm.heightPixels/3*1;
		moreDialog.getWindow().setAttributes(params);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(statusList!=null){
			return statusList.size();
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
		ViewHolder viewHolder;
		if (convertView == null) {
			System.out.println(position);
			convertView = (LinearLayout)mLayoutInflater.inflate(R.layout.myfirstpagelistitem, null);
			viewHolder = new ViewHolder();
			viewHolder.imgIcon = (ImageView)convertView.findViewById(R.id.myfirstpagelistitem_user_touxiang);
			viewHolder.imgMore = (ImageView)convertView.findViewById(R.id.myfirstpagelistitem_user_more);
			viewHolder.name = (TextView)convertView.findViewById(R.id.myfirstpagelistitem_user_name);
			viewHolder.time = (TextView)convertView.findViewById(R.id.myfirstpagelistitem_user_time);
			viewHolder.text = (TextView)convertView.findViewById(R.id.myfirstpagelistitem_text);
			viewHolder.reposts_count = (TextView)convertView.findViewById(R.id.myfirstpagelistitem_reposts_count);
			viewHolder.comments_count = (TextView)convertView.findViewById(R.id.myfirstpagelistitem_comment_count);
			viewHolder.attitudes_count = (TextView)convertView.findViewById(R.id.myfirstpagelistitem_attitudes_count);
			viewHolder.repost_layout = (LinearLayout)convertView.findViewById(R.id.myfirstpagelistitem_repost_layout);
			viewHolder.repost_text = (TextView)convertView.findViewById(R.id.myfirstpagelistitem_repost_text);
			viewHolder.source = (TextView)convertView.findViewById(R.id.myfirstpagelistitem_user_source);
			viewHolder.retweet_status_pic_layout = (LinearLayout)convertView.findViewById(R.id.myfirstpagelistitem_retweet_status_pic_layout);
			viewHolder.status_pic_layout = (LinearLayout)convertView.findViewById(R.id.myfirstpagelistitem_status_pic_layout);
			viewHolder.status_pic = new ImageView[9];
			viewHolder.retweet_status_pic = new ImageView[9];
			for (int i =0;i<9;i++) {
				viewHolder.status_pic[i] = (ImageView)viewHolder.status_pic_layout.findViewById(id[i]);
				viewHolder.retweet_status_pic[i] = (ImageView)viewHolder.retweet_status_pic_layout.findViewById(id[i]);
			}
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		//头像
		if(statusList.get(position).user.verified == true) {
			int 	verified_type = statusList.get(position).user.verified_type;
			if (verified_type == 0){
				viewHolder.imgIcon.setBackground(app.getResources().getDrawable(R.drawable.portrait_v_yellow));
			}else{
				viewHolder.imgIcon.setBackground(app.getResources().getDrawable(R.drawable.portrait_v_blue));
			}
			Picasso.with(context).load(statusList.get(position).user.avatar_large).into(viewHolder.imgIcon);
			//			new AsyncBitmapLoader().execute(viewHolder.imgIcon,statusList.get(position).user.avatar_large);
		}else {
			Picasso.with(context).load(statusList.get(position).user.avatar_large).into(viewHolder.imgIcon);
			//			new AsyncBitmapLoader().execute(viewHolder.imgIcon,statusList.get(position).user.avatar_large);
		}
		//名字
		viewHolder.name.setText(statusList.get(position).user.screen_name);
		//时间
		try {
			viewHolder.time.setText(DateFormat(statusList.get(position).created_at));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//来自
		if(statusList.get(position).source.length()!=0) {
			viewHolder.source.setVisibility(View.VISIBLE);
			String str = statusList.get(position).source;
			viewHolder.source.setText("来自 "+str.substring(str.indexOf(">")+1, str.lastIndexOf("<")));
		}else {
			viewHolder.source.setVisibility(View.GONE);
		}
		//微博内容
		try {

			handleStatus(viewHolder.text,statusList.get(position).text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//转发
		if(statusList.get(position).reposts_count >1){
			viewHolder.reposts_count.setText(statusList.get(position).reposts_count+"");
		}else{
			viewHolder.reposts_count.setText("转发");
		}
		//评论
		if(statusList.get(position).comments_count >1){
			viewHolder.comments_count.setText(statusList.get(position).comments_count+"");
		}else{
			viewHolder.comments_count.setText("评论");
		}
		//赞
		if(statusList.get(position).attitudes_count >1) {
			viewHolder.attitudes_count.setText(statusList.get(position).attitudes_count+"");
		}else {
			viewHolder.attitudes_count.setText("赞");
		}

		//转发的微博内容
		if(statusList.get(position).retweeted_status != null){
			viewHolder.repost_layout.setVisibility(View.VISIBLE);
			String name = app.mDataBaseHelper.queryUser(
					statusList.get(position).retweeted_status.user.id).screen_name;
			String name_after = "@"+name+ ":";
			String retpost_text = name_after+
					app.mDataBaseHelper.queryRetweetedStatus(
							Long.parseLong(statusList.get(position).retweeted_status.id)).text;
			try {		
				handleStatus(viewHolder.repost_text,retpost_text);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//转发微博配图
			if(statusList.get(position).retweeted_status.pic_urls != null) {
				viewHolder.retweet_status_pic_layout.setVisibility(View.VISIBLE);
				final ArrayList<String> pic_urls = app.mDataBaseHelper.queryPicList(statusList.get(position).retweeted_status.id);
				for (int i = 0;i<pic_urls.size();i++){
					viewHolder.retweet_status_pic[i].setVisibility(View.VISIBLE);
					Picasso.with(context).load(pic_urls.get(i)).into(viewHolder.retweet_status_pic[i]);
					//					.replaceAll("thumbnail", "bmiddle")
					//					new AsyncBitmapLoader().execute(viewHolder.retweet_status_pic[i],pic_urls.get(i).replaceAll("thumbnail", "bmiddle"));
					viewHolder.retweet_status_pic[i].setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(context, ImageExploreActivity.class);
							intent.putStringArrayListExtra("image", changeBmiddle(pic_urls));
							intent.putExtra("nowImage", judgeNowImage(v.getId()));
							context.startActivity(intent);
						}
					});
				}
				for(int j = pic_urls.size();j<9;j++){
					viewHolder.retweet_status_pic[j].setVisibility(View.GONE);
				}
			}else {
				viewHolder.retweet_status_pic_layout.setVisibility(View.GONE);
			}
		}else {
			viewHolder.repost_layout.setVisibility(View.GONE);
		}

		//配图
		if(statusList.get(position).pic_urls != null) {
			viewHolder.status_pic_layout.setVisibility(View.VISIBLE);
			final ArrayList<String> pic_urls = app.mDataBaseHelper.queryPicList(statusList.get(position).id);

			for (int i = 0;i<pic_urls.size();i++){
				viewHolder.status_pic[i].setVisibility(View.VISIBLE);
				Picasso.with(context).load(pic_urls.get(i)).into(viewHolder.status_pic[i]);
				//				.replaceAll("thumbnail", "bmiddle")
				//弃用的异步加载图片方式
				//				new AsyncBitmapLoader().execute(viewHolder.status_pic[i],pic_urls.get(i).replaceAll("thumbnail", "bmiddle"));
				viewHolder.status_pic[i].setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context, ImageExploreActivity.class);
						intent.putStringArrayListExtra("image", changeBmiddle(pic_urls));
						intent.putExtra("nowImage", judgeNowImage(v.getId()));
						System.out.println("now id is"+judgeNowImage(v.getId()));
						context.startActivity(intent);
					}
				});
			}
			for(int j = pic_urls.size();j<9;j++){
				viewHolder.status_pic[j].setVisibility(View.GONE);
			}
		}else {
			viewHolder.status_pic_layout.setVisibility(View.GONE);
		}

		//moreDialog
		viewHolder.imgMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				moreDialog.show();
			}
		});

		return convertView;
	}


	private class ViewHolder{
		private ImageView imgIcon;
		private ImageView imgMore;
		private TextView name;
		private TextView time;
		private TextView text;
		private TextView reposts_count;
		private TextView comments_count;
		private TextView attitudes_count;
		private LinearLayout repost_layout;
		private TextView repost_text;
		private TextView source;
		private LinearLayout status_pic_layout;
		private LinearLayout retweet_status_pic_layout;
		private ImageView[] status_pic;
		private ImageView[] retweet_status_pic;
	}
	
	private int  judgeNowImage(int ResourceId) {
		int now_id = 0;
		for(int i=0;i<id.length;i++){
			if(id[i]==ResourceId){
				now_id = i;
			}
		}
		return now_id;
	}
	
	private ArrayList<String> changeBmiddle(ArrayList<String> list){
		if(list == null){
			return null;
		}else{
			ArrayList<String> newlist = new ArrayList<String>();
			for(int i = 0;i<list.size();i++){
				newlist.add(list.get(i).replaceAll("thumbnail", "bmiddle"));
			}
			return newlist;
		}
	}

	private void handleStatus(TextView textView, String text) throws IOException{
		/**
		 * 在匹配之前先将字符串处理,  "]" 后面加空格 话题的第二个"#"后面加空格
		 */
		text=text.replaceAll("]", "] ");	
		Pattern topic_pattern = Pattern.compile("#([a-zA-Z0-9-_\u4E00-\u9FA5]*)#");
		ArrayList<String> topic_results_ = new ArrayList<String>();
		Matcher topic_matcher = topic_pattern.matcher(text);
		while(!topic_matcher.hitEnd()&&topic_matcher.find()){
			System.out.println(topic_matcher.group());
			topic_results_.add(topic_matcher.group());
		}
		for(int i = 0;i<topic_results_.size();i++){
			text=text.replaceAll(topic_results_.get(i), topic_results_.get(i)+" ");
		}
		Pattern at_pattern = Pattern.compile("@[a-zA-Z0-9-_\u4E00-\u9FA5]+");
		ArrayList<String> at_results_ = new ArrayList<String>();
		Matcher at_matcher = at_pattern.matcher(text);
		while(!at_matcher.hitEnd()&&at_matcher.find()){
			at_results_.add(at_matcher.group());
		}
		for(int i = 0;i<at_results_.size();i++){
			text=text.replaceAll(at_results_.get(i), at_results_.get(i)+" ");
		}
		Pattern web_pattern = Pattern.compile("http://[a-zA-Z0-9]+\\.[a-zA-Z0-9]+\\.?[\\/a-zA-Z0-9]*");
		ArrayList<String> web_results_ = new ArrayList<String>();
		Matcher web_matcher = web_pattern.matcher(text);
		while(!web_matcher.hitEnd()&&web_matcher.find()){
			System.out.println(web_matcher.group());
			web_results_.add(web_matcher.group());
		}
			
		stringBuilder = new SpannableStringBuilder(text);
		String topic_results[] = text.split("#([a-zA-Z0-9-_\u4E00-\u9FA5]*)#");
		String at_results[] = text.split("@[a-zA-Z0-9-_\u4E00-\u9FA5]+");
		String emotions_results[] = text.split("(\\[([a-zA-Z\u4E00-\u9FA5]*)\\])");
		String web_results[] = text.split("http://[a-zA-Z0-9]+\\.[a-zA-Z0-9]+\\.?[\\/a-zA-Z0-9]*");
		//split如果没匹配到length返回1 如果aaa[b] 匹配[b] length也会返回1所以要区分
		if(topic_results.length==1&&at_results.length ==1
				&&emotions_results.length ==1&&web_results.length ==1
				&&topic_results[0].length()==text.length()
				&&emotions_results[0].length()==text.length()
				&&at_results[0].length()==text.length()
				&&web_results[0].length()==text.length()){			
			textView.setText(text);
		}else{
			
			//解析话题
			if(topic_results.length>=1&&topic_results[0].length()!=text.length()){			
				int topic_length = 0;
				for(int i=0;i<topic_results_.size();i++){
					int start = topic_results[i].length()+topic_length;
					int end = topic_results[i].length()+topic_results_.get(i).length()+topic_length;
					topic_length =end; 
					InputObject topicClick = new InputObject();
					topicClick.setStartSpan(start);
					topicClick.setEndSpan(end);
					topicClick.setString(topic_results_.get(i).substring(1, topic_results_.get(i).length()-1));
					topicClick.setStringBuilder(stringBuilder);
					topicClick.setMultiActionTextviewClickListener(myMultiActionClickListener);
					topicClick.setOperationType(TOPIC_CLICKED);
					MultiActionTextView.addActionOnTextViewWithoutLink(topicClick);
					// final step
					MultiActionTextView.setSpannableText(textView,stringBuilder, 0xff436EEE);
				}
			}

			//解析@的人
			if(at_results.length>=1&&at_results[0].length()!=text.length()){

				int at_length = 0;
				for(int i=0;i<at_results_.size();i++){
					System.out.println(at_results_.get(i));
					int start = at_results[i].length()+at_length;
					int end = at_results[i].length()+at_results_.get(i).length()+at_length;
					at_length = end; 
					InputObject atClick = new InputObject();
					atClick.setStartSpan(start);
					atClick.setEndSpan(end);
					atClick.setStringBuilder(stringBuilder);
					atClick.setString(at_results_.get(i).substring(1,at_results_.get(i).length()));
					atClick.setMultiActionTextviewClickListener(myMultiActionClickListener);
					atClick.setOperationType(AT_CLICKED);
					MultiActionTextView.addActionOnTextViewWithoutLink(atClick);
					// final step
					MultiActionTextView.setSpannableText(textView,stringBuilder, 0xff436EEE);
				}
			}	
			
			//解析web
			if(web_results.length>=1&&web_results[0].length()!=text.length()){

				int web_length = 0;
				for(int i=0;i<web_results_.size();i++){
					System.out.println(web_results_.get(i));
					int start = web_results[i].length()+web_length;
					int end = web_results[i].length()+web_results_.get(i).length()+web_length;
					web_length = end; 
					InputObject webClick = new InputObject();
					webClick.setStartSpan(start);
					webClick.setEndSpan(end);
					webClick.setString(web_results_.get(i));
					webClick.setStringBuilder(stringBuilder);
					webClick.setString(web_results_.get(i).substring(1,web_results_.get(i).length()));
					webClick.setMultiActionTextviewClickListener(myMultiActionClickListener);
					webClick.setOperationType(WEB_CLICKED);
					MultiActionTextView.addActionOnTextViewWithoutLink(webClick);
					// final step
					MultiActionTextView.setSpannableText(textView,stringBuilder, 0xff436EEE);
				}
			}	

			//解析表情
			if(emotions_results.length>=1&&emotions_results[0].length()!=text.length()){  
				Pattern emotions_pattern = Pattern.compile("(\\[([a-zA-Z\u4E00-\u9FA5]*)\\])");
				ArrayList<String> emotions_results_ = new ArrayList<String>();
				Matcher at_matcher1 = emotions_pattern.matcher(text);
				while(!at_matcher1.hitEnd()&&at_matcher1.find()){
					System.out.println(at_matcher1.group());
					emotions_results_.add(at_matcher1.group());
				}
				int emotions_length = 0;
				for(int i =0;i<emotions_results_.size();i++)
				{
					System.out.println("emotions_results length is "+i);
					int start = emotions_results[i].length()+emotions_length;
					int end = emotions_results[i].length()+emotions_results_.get(i).length()+emotions_length;
					emotions_length =end; 
					Integer id =Constants.mSmileyMap.get(emotions_results_.get(i)); 
					if(id==null){
						//本地没有该表情
						System.out.println(emotions_results_.get(i)+" not found");
						continue;
					}else{
						Drawable drawable = app.getResources().getDrawable(id);
						drawable.setBounds(0, 0, drawable.getIntrinsicWidth()-34, drawable.getIntrinsicHeight()-34);
						ImageSpan span = new ImageSpan(drawable,ImageSpan.ALIGN_BASELINE);
						stringBuilder.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						textView.setText(stringBuilder);
					}
				}
			}
		}
	}

	private String DateFormat(String date) throws ParseException {
		Date status_date = new Date(date);
		Date now_date = new Date();
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date status_date_change = dateformat.parse(dateformat.format(status_date));
		Date now_date_change = dateformat.parse(dateformat.format(now_date));
		long l = now_date_change.getTime() - status_date_change.getTime();
		long day=l/(24*60*60*1000);
		long hour=(l/(60*60*1000)-day*24);
		long min=((l/(60*1000))-day*24*60-hour*60);
		if (day >1) {
			return day+"天前";
		}else {
			if(hour<1&&min < 3){
				return "刚刚";
			}else if (min>=3&&hour<1&&min<60){
				return min+"分钟前";
			}else {
				return hour + "小时前";
			}
		}
	}

	class MyMultiActionClickListener implements MultiActionTextviewClickListener {

		@Override
		public void onTextClick(InputObject inputObject) {
			int operation = inputObject.getOperationType();
			switch (operation) {
			case TOPIC_CLICKED:
				System.out.println(inputObject.getString());
				break;
			case AT_CLICKED:
				ActivityInvokeAPI.openUserInfoByNickName((Activity) context, inputObject.getString());
				break;
			case WEB_CLICKED:
				System.out.println(inputObject.getString());
				Intent intent = new Intent();
				intent.setClass(context, WebExploreActivity.class);
				intent.putExtra("url",inputObject.getString());			
				context.startActivity(intent);
				break;
			default :
				break;
			}
		}
	}


}
