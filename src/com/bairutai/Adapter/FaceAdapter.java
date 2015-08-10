package com.bairutai.Adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bairutai.data.Constants;
import com.bairutai.sinaweibo.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class FaceAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private int currentPage = 0;
	private Map<String, Integer> mFaceMap;
	private List<Integer> faceList = new ArrayList<Integer>();
	private Context context;
	public FaceAdapter(Context context, int currentPage) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.currentPage = currentPage;
		mFaceMap = Constants.mSmileyMap;
		initData();
	}

	private void initData() {
		for(Map.Entry<String, Integer> entry:mFaceMap.entrySet()){
			faceList.add(entry.getValue());
		}
	}

	@Override
	public int getCount() {
		return 21;
	}

	@Override
	public Object getItem(int position) {
		return faceList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.face, null, false);
			viewHolder.faceIV = (ImageView) convertView
					.findViewById(R.id.face_iv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (position == 20) {
			viewHolder.faceIV.setImageDrawable(context.getResources().getDrawable(R.drawable.emotion_delete_selector));
		} else {
			int count = 20 * currentPage + position;
			if (count < 104) {
				viewHolder.faceIV.setImageResource(faceList.get(count));
			} else {
				viewHolder.faceIV.setAlpha(0f);
				viewHolder.faceIV.setClickable(false);
			}
		}
		return convertView;
	}

	public static class ViewHolder {
		ImageView faceIV;
	}
}
