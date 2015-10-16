package com.bairutai.Adapter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.bairutai.application.WeiboApplication;
import com.bairutai.model.User;
import com.bairutai.sinaweibo.R;
import com.bairutai.tools.AsyncBitmapLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyFanslistAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	private ArrayList<User> friendlist ;

	public MyFanslistAdapter(Context context,WeiboApplication app) {
		// TODO 自动生成的构造函数存根
		mLayoutInflater = LayoutInflater.from(context);
		if (app.mDataBaseHelper == null) {
			System.out.println("friendlist is nullaaaaaaaaaaaaaaaaa");
		}else{
			System.out.println("friendlist is nullbbbbbbbbbbbbbbb");
			friendlist = app.mDataBaseHelper.queryFlower();
		}
	}
	
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		if (null != friendlist) {
			return friendlist.size();
		}else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		final ViewHolder viewHolder;
		if (convertView == null) {
			System.out.println(position);
			convertView = (RelativeLayout)mLayoutInflater.inflate(R.layout.myfanslistitem, null);
			viewHolder = new ViewHolder();
			viewHolder.imgGuanzhu = (ImageView)convertView.findViewById(R.id.myfanslistitem_flower_guanzhu_img);
			viewHolder.txtName = (TextView)convertView.findViewById(R.id.myfanslistitem_flower_name);
			viewHolder.txtStatus = (TextView)convertView.findViewById(R.id.myfanslistitem_flower_status_text);
			viewHolder.txtSource = (TextView)convertView.findViewById(R.id.myfanslistitem_flower_status_from);
			viewHolder.imgIcon = (ImageView)convertView.findViewById(R.id.myfanslistitem_img);
			convertView.setTag(viewHolder);
		}else {
			System.out.println(position);
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.txtName.setText(friendlist.get(position).screen_name);
		viewHolder.txtStatus.setText(friendlist.get(position).status_text);
		viewHolder.txtSource.setText("■来自" + friendlist.get(position).status_from);
		new AsyncBitmapLoader().execute(viewHolder.imgIcon,friendlist.get(position).avatar_large);
		if(friendlist.get(position).following){				
			viewHolder.imgGuanzhu.setImageResource(R.drawable.card_icon_arrow);
			viewHolder.imgGuanzhu.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					viewHolder.imgGuanzhu.setImageResource(R.drawable.card_icon_addtogroup);
				}
			});
		}else {
			viewHolder.imgGuanzhu.setImageResource(R.drawable.card_icon_addtogroup);
			viewHolder.imgGuanzhu.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					viewHolder.imgGuanzhu.setImageResource(R.drawable.card_icon_arrow);
				}
			});
		}
		return convertView;
	}
	
    private class ViewHolder {
        ImageView imgIcon;
        TextView txtName;
        TextView txtStatus;
        TextView txtSource;
        ImageView imgGuanzhu;
   }
   
}
