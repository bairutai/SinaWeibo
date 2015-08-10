package com.bairutai.Adapter;

import com.bairutai.sinaweibo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyMessageAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private ImageView img;
	private TextView txt;
	
	public MyMessageAdapter(Context context) {
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		this.context = context;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
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
		if(position<3) {
			convertView = inflater.inflate(R.layout.mymessagelist_item, null);
			img = (ImageView) convertView.findViewById(R.id.mymessagelist_item_img);
			txt = (TextView)convertView.findViewById(R.id.mymessagelist_item_txt);
			switch (position) {
			case 0:
				img.setImageDrawable(context.getResources().getDrawable(R.drawable.messagescenter_at));
				txt.setText("@我的");
				break;
			case 1:
				img.setImageDrawable(context.getResources().getDrawable(R.drawable.messagescenter_comments));
				txt.setText("评论");
				break;
			case 2:
				img.setImageDrawable(context.getResources().getDrawable(R.drawable.messagescenter_good));
				txt.setText("赞");
				break;
			default:
				break;
			}
		}
		return convertView;
	}

}
