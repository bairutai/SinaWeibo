package com.bairutai.Adapter;

import com.bairutai.application.WeiboApplication;
import com.bairutai.sinaweibo.R;
import com.bairutai.tools.AsyncBitmapLoader;
import com.sina.weibo.sdk.openapi.models.StatusList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyFirstPageAdapter extends BaseAdapter {
	private StatusList statusList;
	private LayoutInflater mLayoutInflater;
	
	public MyFirstPageAdapter(Context context, WeiboApplication app) {
		// TODO Auto-generated constructor stub
		mLayoutInflater = LayoutInflater.from(context);
		statusList = app.getStatusList();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return statusList.statusList.size();
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
			viewHolder.imgIcon = (ImageView)convertView.findViewById(R.id.iv_head_portrait);
			viewHolder.name = (TextView)convertView.findViewById(R.id.tv_name);
			viewHolder.time = (TextView)convertView.findViewById(R.id.tv_time_source);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		new AsyncBitmapLoader().execute(viewHolder.imgIcon,statusList.statusList.get(position).user.avatar_large);
		viewHolder.name.setText(statusList.statusList.get(position).user.screen_name);
		viewHolder.time.setText(statusList.statusList.get(position).created_at);
		return convertView;
	}
	
	private class ViewHolder{
		private ImageView imgIcon;
		private TextView name;
		private TextView time;
	}
}
