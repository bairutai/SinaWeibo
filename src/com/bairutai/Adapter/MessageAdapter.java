package com.bairutai.Adapter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bairutai.application.WeiboApplication;
import com.bairutai.data.Constants;
import com.bairutai.model.MessageItem;
import com.bairutai.model.User;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.bairutai.sinaweibo.R;
import com.bairutai.tools.AsyncBitmapLoader;

public class MessageAdapter extends BaseAdapter {
	public static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");
	private LayoutInflater inflater;
	private Context context;
	private List<MessageItem> list;
	private User user;
	
	public MessageAdapter(Context context, List<MessageItem> list,User user) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
		this.user = user;
		inflater = LayoutInflater.from(context);
	}

	public void removeHeadMsg() {
		if (list.size() - 10 > 10) {
			for (int i = 0; i < 10; i++) {
				list.remove(i);
			}
			notifyDataSetChanged();
		}
	}
	
	public void setMessageList(List<MessageItem> msgList) {
		list = msgList;
		notifyDataSetChanged();
	}

	public void upDateMsg(MessageItem msg) {
		list.add(msg);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(list != null){
			System.out.println("list size is "+list.size());
			return list.size();
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MessageItem item = list.get(position);
		boolean isComMsg = item.isComMeg();
		ViewHolder holder;
		if (convertView == null|| convertView.getTag(R.drawable.ic_launcher + position) == null) {
			holder = new ViewHolder();
			if (isComMsg) {
				convertView = inflater.inflate(R.layout.chat_item_left, null);
			} else {
				convertView = inflater.inflate(R.layout.chat_item_right, null);
			}
			holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
			holder.time = (TextView) convertView.findViewById(R.id.datetime);
			holder.msg = (TextView) convertView.findViewById(R.id.textView2);
			holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.time.setText(item.getDate());
		holder.time.setVisibility(View.VISIBLE);
		holder.msg.setText(convertNormalStringToSpannableString(item.getMessage()),BufferType.SPANNABLE);
		AsyncBitmapLoader bitmapLoader = new AsyncBitmapLoader();
		bitmapLoader.setProgressBar(holder.progressBar);
		if(isComMsg){
			bitmapLoader.execute(holder.imageView, user.avatar_large);
		}else{
			bitmapLoader.execute(holder.imageView,WeiboApplication.getUser().avatar_large);
		}
		return convertView;
	}
	
	/**
	 * 另外一种方法解析表情
	 * 
	 * @param message
	 *            传入的需要处理的String
	 * @return
	 */
	private CharSequence convertNormalStringToSpannableString(String message) {
		// TODO Auto-generated method stub
		String hackTxt;
		if (message.startsWith("[") && message.endsWith("]")) {
			hackTxt = message + " ";
		} else {
			hackTxt = message;
		}
		SpannableString value = SpannableString.valueOf(hackTxt);

		Matcher localMatcher = EMOTION_URL.matcher(value);
		while (localMatcher.find()) {
			String str2 = localMatcher.group(0);
			int k = localMatcher.start();
			int m = localMatcher.end();
			// k = str2.lastIndexOf("[");
			// Log.i("way", "str2.length = "+str2.length()+", k = " + k);
			// str2 = str2.substring(k, m);
			if (m - k < 8) {
				if (Constants.mSmileyMap.containsKey(str2)) {
					int face = Constants.mSmileyMap.get(str2);
					Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), face);
					if (bitmap != null) {
						ImageSpan localImageSpan = new ImageSpan(context,bitmap, ImageSpan.ALIGN_BASELINE);
						value.setSpan(localImageSpan, k, m,
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
			}
		}
		return value;
	}
	private  class ViewHolder {
		TextView time;
		TextView msg;
		ImageView imageView;
		ProgressBar progressBar;

	}
}
