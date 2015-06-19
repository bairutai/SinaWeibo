package com.bairutai.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ajra.multiactiontextview.InputObject;
import com.ajra.multiactiontextview.MultiActionTextView;
import com.ajra.multiactiontextview.MultiActionTextviewClickListener;
import com.bairutai.data.Constants;
import com.bairutai.sinaweibo.R;
import com.bairutai.sinaweibo.WebExploreActivity;
import com.sina.weibo.sdk.openapi.legacy.ActivityInvokeAPI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.widget.TextView;

public class HandleStatus {
	private TextView textview;
	private String text;
	private Context context;
	private MyMultiActionClickListener myMultiActionClickListener;
	private final int TOPIC_CLICKED = 1;
	private final int AT_CLICKED = 2;
	private final int WEB_CLICKED = 3;
	public static 	int id[] = { R.id.img1, R.id.img2, R.id.img3, R.id.img4, R.id.img5,
			R.id.img6, R.id.img7, R.id.img8, R.id.img9 };

	public HandleStatus(Context context) {
		// TODO Auto-generated constructor stub
		myMultiActionClickListener = new MyMultiActionClickListener();
		this.context = context;
	}

	public void handle(TextView textview,String text) {
		// TODO Auto-generated method stub
		/**
		 * 在匹配之前先将字符串处理,  "]" 后面加空格 话题的第二个"#"后面加空格
		 */
		text=text.replaceAll("]", "] ");	
		Pattern topic_pattern = Pattern.compile("#([a-zA-Z0-9-_ \u4E00-\u9FA5]*)#");
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

		SpannableStringBuilder stringBuilder = new SpannableStringBuilder(text);
		String topic_results[] = text.split("#([a-zA-Z0-9-_ \u4E00-\u9FA5]*)#");
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
			textview.setText(text);
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
					MultiActionTextView.setSpannableText(textview,stringBuilder, 0xff436EEE);
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
					System.out.println("testtttttttttttttt"+at_results_.get(i));
					atClick.setMultiActionTextviewClickListener(myMultiActionClickListener);
					atClick.setOperationType(AT_CLICKED);
					MultiActionTextView.addActionOnTextViewWithoutLink(atClick);
					// final step
					MultiActionTextView.setSpannableText(textview,stringBuilder, 0xff436EEE);
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
					webClick.setMultiActionTextviewClickListener(myMultiActionClickListener);
					webClick.setOperationType(WEB_CLICKED);
					MultiActionTextView.addActionOnTextViewWithoutLink(webClick);
					// final step
					MultiActionTextView.setSpannableText(textview,stringBuilder, 0xff436EEE);
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
						textview.setText(stringBuilder);
						continue;
					}else{
						Drawable drawable = context.getResources().getDrawable(id);
						drawable.setBounds(0, 0, drawable.getIntrinsicWidth()-34, drawable.getIntrinsicHeight()-34);
						ImageSpan span = new ImageSpan(drawable,ImageSpan.ALIGN_BASELINE);
						stringBuilder.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						textview.setText(stringBuilder);
					}
				}
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

	/**
	 * 返回大图的地址 新浪api中返回的是缩略图的地址
	 * @param list
	 * @return
	 */
	public static ArrayList<String> changeBmiddle(ArrayList<String> list){
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

	/**
	 * 返回点击的是第几张图片
	 * @param ResourceId
	 * @return
	 */
	public static int  judgeNowImage(int ResourceId) {
		int now_id = 0;
		for(int i=0;i<id.length;i++){
			if(id[i]==ResourceId){
				now_id = i;
			}
		}
		return now_id;
	}
	
	public static String DateFormat(String date) throws ParseException {
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
}
