package com.bairutai.Adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.bairutai.sinaweibo.R;
import com.bairutai.tools.HandleStatus;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StatusCommentsAdapter extends BaseAdapter {
	private CommentList commentlist;
	private LayoutInflater mLayoutInflater;
	private Context context;
	private boolean isLike  = false;
	public StatusCommentsAdapter(Context context,
			CommentList commentlist) {
		// TODO Auto-generated constructor stub
		this.commentlist = commentlist;
		this.context = context;
		mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (commentlist.commentList != null) {
			return commentlist.commentList.size();
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
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = (RelativeLayout)mLayoutInflater.inflate(R.layout.comments_listview_item, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) convertView.findViewById(R.id.commentslistitem_user_name);
			viewHolder.time = (TextView)convertView.findViewById(R.id.commentslistitem_user_time);
			viewHolder.text = (TextView)convertView.findViewById(R.id.commentslistitem_user_text);
			viewHolder.imgIcon = (ImageView)convertView.findViewById(R.id.commentslistitem_user_touxiang);
			viewHolder.imgzan = (ImageView)convertView.findViewById(R.id.commentslistitem_user_zan);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		//头像
		if(commentlist.commentList.get(position).user.avatar_large!=null){
			Picasso.with(context).load(commentlist.commentList.get(position).user.avatar_large).into(viewHolder.imgIcon);
		}else {
			viewHolder.imgIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.head_portrait_default));
		}
		if(commentlist.commentList.get(position).user.screen_name!=null){
			viewHolder.name.setText(commentlist.commentList.get(position).user.screen_name);
		}else {
			viewHolder.name.setText("未知用户");
		}
		try {
			viewHolder.time.setText(DateToString(commentlist.commentList.get(position).created_at));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(commentlist.commentList.get(position).text!=null){
			HandleStatus handleStatus  = new HandleStatus(context);
			handleStatus.handle(viewHolder.text, commentlist.commentList.get(position).text);
		}else {
			viewHolder.text.setText("回复为空");
		}
		
		viewHolder.imgzan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isLike) {
					viewHolder.imgzan.setImageDrawable(context.getResources().getDrawable(R.drawable.timeline_icon_unlike));
					isLike = false;
				}else {
					viewHolder.imgzan.setImageDrawable(context.getResources().getDrawable(R.drawable.timeline_trend_icon_like));
					isLike = true;
				}
			}
		});
		
		return convertView;
	}
	
	private class ViewHolder{
		private TextView name;
		private TextView time;
		private TextView text;
		private ImageView imgIcon;
		private ImageView imgzan;
	}
	
	private String DateToString(String date) throws ParseException {
		Date status_date = new Date(date);
		SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd HH:mm");
		return dateformat.format(status_date);
	}
}
