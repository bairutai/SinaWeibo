package com.bairutai.Adapter;

import java.util.ArrayList;

import com.bairutai.application.WeiboApplication;
import com.bairutai.database.DataBase;
import com.bairutai.model.Friend;
import com.bairutai.sinaweibo.R;
import com.bairutai.tools.AsyncBitmapLoader;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyFanslistAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mLayoutInflater;
	private DataBase mDataBase;
	private int length;
	private ArrayList<Friend> friendlist ;

	public MyFanslistAdapter(Context context,WeiboApplication app) {
		// TODO 自动生成的构造函数存根
		this.context = context;
		mLayoutInflater = LayoutInflater.from(context);
		if (app.getMfriendDataBase() == null) {
			System.out.println("friendlist is nullaaaaaaaaaaaaaaaaa");
		}else{
			System.out.println("friendlist is nullbbbbbbbbbbbbbbb");
			friendlist = app.getMfriendDataBase().queryFriend();
		}
		

		length = friendlist.size();
		System.out.println(length);
	}
	
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return length;
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
		ViewHolder viewHolder;
		if (convertView == null) {
			System.out.println(position);
			convertView = (RelativeLayout)mLayoutInflater.inflate(R.layout.myfanslistitem, null);
			viewHolder = new ViewHolder();
			viewHolder.imgGuanzhu = (ImageView)convertView.findViewById(R.id.myfanslistitem_flower_guanzhu_img);
			viewHolder.txtName = (TextView)convertView.findViewById(R.id.myfanslistitem_flower_name);
			viewHolder.txtDescription = (TextView)convertView.findViewById(R.id.myfanslistitem_flower_description);
			viewHolder.txtSource = (TextView)convertView.findViewById(R.id.myfanslistitem_flower_source);
			viewHolder.imgIcon = (ImageView)convertView.findViewById(R.id.myfanslistitem_img);
			convertView.setTag(viewHolder);
		}else {
			System.out.println(position);
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.txtName.setText(friendlist.get(position).screen_name);
		viewHolder.txtDescription.setText(friendlist.get(position).description);
//		viewHolder.txtSource.setText(friendlist.get(position).status.source);
		new AsyncBitmapLoader().execute(viewHolder.imgIcon,friendlist.get(position).profile_image_url);
		if(friendlist.get(position).follow_me){				
			viewHolder.imgGuanzhu.setImageResource(R.drawable.card_icon_arrow);
		}else {
			viewHolder.imgGuanzhu.setImageResource(R.drawable.card_icon_addtogroup);
		}
		return convertView;
	}
	
    private class ViewHolder {
        ImageView imgIcon;
        TextView txtName;
        TextView txtDescription;
        TextView txtSource;
        ImageView imgGuanzhu;
   }

}
