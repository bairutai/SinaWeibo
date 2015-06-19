package com.bairutai.Adapter;

import java.text.ParseException;
import java.util.ArrayList;


import com.bairutai.application.WeiboApplication;
import com.bairutai.data.Constants;
import com.bairutai.model.Status;
import com.bairutai.sinaweibo.ImageExploreActivity;
import com.bairutai.sinaweibo.R;
import com.bairutai.sinaweibo.StatusActivity;
import com.bairutai.tools.HandleStatus;
import com.dd.CircularProgressButton.CircularProgressButton;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.ActivityInvokeAPI;
import com.sina.weibo.sdk.openapi.legacy.FavoritesAPI;
import com.sina.weibo.sdk.openapi.legacy.FriendshipsAPI;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StatusAdapter extends BaseAdapter {
	//主界面
	//position==0
	private RelativeLayout status_layout;
	private ImageView imgIcon;
	private ImageView imgShoucang;
	private CircularProgressButton circularButton;
	private TextView name;
	private TextView time;
	private TextView text;
	private TextView status_reposts_count;
	private TextView status_comments_count;
	private TextView status_attitudes_count;
	private RelativeLayout repost_layout;
	private TextView repost_text;
	private TextView source;
	private LinearLayout status_pic_layout;
	private LinearLayout retweet_status_pic_layout;
	private ImageView[] status_pic;
	private ImageView[] retweet_status_pic;
	private 	int id[] = { R.id.img1, R.id.img2, R.id.img3, R.id.img4, R.id.img5,
			R.id.img6, R.id.img7, R.id.img8, R.id.img9 };

	//position==1
	public RelativeLayout reposts_layout;
	public RelativeLayout comments_layout;
	public RelativeLayout attitudes_layout;
	public TextView  reposts_count;
	public TextView  comments_count;
	public TextView  attitudes_count;
	public TextView  reposts_text;
	public TextView  comments_text;
	public TextView  attitudes_text;
	private int whichIsClick;
	
	private LayoutInflater mLayoutInflater;
	private Context context;
	private Status status;
	private WeiboApplication app;
	private boolean isFavorite;
	private FavoritesAPI mFavoritesAPI;
	private FriendshipsAPI mFriendshipsAPI;
	public StatusAdapter(Context context,Status status,WeiboApplication app) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.status = status;
		this.app = app;
		mLayoutInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
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

	public int getWhichIsClick() {
		return whichIsClick;
	}
	public void setWhichIsClick(int whichIsClick) {
		this.whichIsClick = whichIsClick;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(position == 0){
			convertView = (RelativeLayout)mLayoutInflater.inflate(R.layout.statuslayout, null);
			status_layout = (RelativeLayout)convertView.findViewById(R.id.status_text_layout);
			imgIcon = (ImageView)convertView.findViewById(R.id.status_user_touxiang);
			name = (TextView)convertView.findViewById(R.id.status_user_name);
			time = (TextView)convertView.findViewById(R.id.status_user_time);
			circularButton = (CircularProgressButton)convertView.findViewById(R.id.status_user_guanzhu);
			imgShoucang = (ImageView)convertView.findViewById(R.id.status_user_shoucang);
			text = (TextView)convertView.findViewById(R.id.status_text);
			repost_layout = (RelativeLayout)convertView.findViewById(R.id.retstatus_layout);
			repost_text = (TextView)convertView.findViewById(R.id.status_repost_text);

			status_reposts_count = (TextView)convertView.findViewById(R.id.status_reposts_reposts_count);
			status_attitudes_count = (TextView)convertView.findViewById(R.id.status_reposts_attitudes_count);
			status_comments_count = (TextView)convertView.findViewById(R.id.status_reposts_comment_count);

			source = (TextView)convertView.findViewById(R.id.status_user_source);
			retweet_status_pic_layout = (LinearLayout)convertView.findViewById(R.id.status_retweet_status_pic_layout);
			status_pic_layout = (LinearLayout)convertView.findViewById(R.id.status_status_pic_layout);
			status_pic = new ImageView[9];
			retweet_status_pic = new ImageView[9];
			for (int i =0;i<9;i++) {
				status_pic[i] = (ImageView)status_pic_layout.findViewById(id[i]);
				retweet_status_pic[i] = (ImageView)retweet_status_pic_layout.findViewById(id[i]);
			}

			//头像
			if (status.user != null){
				if(status.user.verified == true) {
					int 	verified_type = status.user.verified_type;
					if (verified_type == 0){
						imgIcon.setBackground(context.getResources().getDrawable(R.drawable.portrait_v_yellow));
					}else{
						imgIcon.setBackground(context.getResources().getDrawable(R.drawable.portrait_v_blue));
					}
					Picasso.with(context).load(status.user.avatar_large).into(imgIcon);
					//			new AsyncBitmapLoader().execute(imgIcon,status.user.avatar_large);
				}else {
					Picasso.with(context).load(status.user.avatar_large).into(imgIcon);
					//			new AsyncBitmapLoader().execute(imgIcon,status.user.avatar_large);
				}
				//名字
				name.setText(status.user.screen_name);

				//收藏或者关注按钮
				if(status.user.following){
					imgShoucang.setVisibility(View.VISIBLE);
					if(status.favorited){
						isFavorite = true;
						imgShoucang.setImageDrawable(context.getResources().getDrawable(R.drawable.card_icon_favorite_highlighted));
					}else{
						isFavorite = false;
						imgShoucang.setImageDrawable(context.getResources().getDrawable(R.drawable.card_icon_favorite));
					}
				}else{
					circularButton.setVisibility(View.VISIBLE);
				}
			}

			//时间
			try {
				time.setText(HandleStatus.DateFormat(status.created_at));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//来自
			if(status.source.length()!=0) {
				source.setVisibility(View.VISIBLE);
				String str = status.source;
				source.setText("来自 "+str.substring(str.indexOf(">")+1, str.lastIndexOf("<")));
			}else {
				source.setVisibility(View.GONE);
			}

			//微博内容
			HandleStatus handlestatus = new HandleStatus(context);
			handlestatus.handle(text, status.text);

			//转发的微博内容
			if(status.retweeted_status != null){
				repost_layout.setVisibility(View.VISIBLE);
				status_reposts_count.setText(String.valueOf(status.retweeted_status.reposts_count));
				status_attitudes_count.setText(String.valueOf(status.retweeted_status.attitudes_count));
				status_comments_count.setText(String.valueOf(status.retweeted_status.comments_count));
				String name = app.mDataBaseHelper.queryUser(
						status.retweeted_status.user.id).screen_name;
				String name_after = "@"+name+ ":";
				String retpost_text = name_after+
						app.mDataBaseHelper.queryRetweetedStatus(
								Long.parseLong(status.retweeted_status.id)).text;
				HandleStatus handlestatus_ret = new HandleStatus(context);
				handlestatus_ret.handle(repost_text, retpost_text);
				//转发微博配图
				if(status.retweeted_status.pic_urls != null) {
					retweet_status_pic_layout.setVisibility(View.VISIBLE);
					final ArrayList<String> pic_urls = app.mDataBaseHelper.queryPicList(status.retweeted_status.id);
					for (int i = 0;i<pic_urls.size();i++){
						retweet_status_pic[i].setVisibility(View.VISIBLE);
						Picasso.with(context).load(pic_urls.get(i)).into(retweet_status_pic[i]);
						//					.replaceAll("thumbnail", "bmiddle")
						//					new AsyncBitmapLoader().execute(retweet_status_pic[i],pic_urls.get(i).replaceAll("thumbnail", "bmiddle"));
						retweet_status_pic[i].setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(context, ImageExploreActivity.class);
								intent.putStringArrayListExtra("image", HandleStatus.changeBmiddle(pic_urls));
								intent.putExtra("nowImage", HandleStatus.judgeNowImage(v.getId()));
								context.startActivity(intent);
							}
						});
					}
					for(int j = pic_urls.size();j<9;j++){
						retweet_status_pic[j].setVisibility(View.GONE);
					}
				}else {
					retweet_status_pic_layout.setVisibility(View.GONE);
				}

				repost_layout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.setClass(context, StatusActivity.class);
						intent.putExtra("ret_statusid", Long.parseLong(status.retweeted_status.id));
						context.startActivity(intent);
					}
				});

				repost_layout.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						return false;
					}
				});
			}else {
				repost_layout.setVisibility(View.GONE);
			}

			//配图
			if(status.pic_urls != null) {
				status_pic_layout.setVisibility(View.VISIBLE);
				final ArrayList<String> pic_urls = app.mDataBaseHelper.queryPicList(status.id);

				for (int i = 0;i<pic_urls.size();i++){
					status_pic[i].setVisibility(View.VISIBLE);
					Picasso.with(context).load(pic_urls.get(i)).into(status_pic[i]);
					//				.replaceAll("thumbnail", "bmiddle")
					//弃用的异步加载图片方式
					//				new AsyncBitmapLoader().execute(status_pic[i],pic_urls.get(i).replaceAll("thumbnail", "bmiddle"));
					status_pic[i].setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(context, ImageExploreActivity.class);
							intent.putStringArrayListExtra("image", HandleStatus.changeBmiddle(pic_urls));
							intent.putExtra("nowImage", HandleStatus.judgeNowImage(v.getId()));
							context.startActivity(intent);
						}
					});
				}
				for(int j = pic_urls.size();j<9;j++){
					status_pic[j].setVisibility(View.GONE);
				}
			}else {
				status_pic_layout.setVisibility(View.GONE);
			}

			imgIcon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ActivityInvokeAPI.openUserInfoByNickName((Activity) context, status.user.screen_name);
				}
			});


			status_layout.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					return false;
				}
			});


			imgShoucang.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mFavoritesAPI = new FavoritesAPI(context, Constants.APP_KEY, app.getmAccessToken());
					long uid = Long.parseLong(status.id);
					if(isFavorite){
						mFavoritesAPI.destroy(uid, mDestoryListener);					
					}else {
						mFavoritesAPI.create(uid, mCreateListener);
					}
				}
			});

			circularButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mFriendshipsAPI = new FriendshipsAPI(context, Constants.APP_KEY, app.getmAccessToken());
					long uid = Long.parseLong(status.user.id);
					String name = status.user.screen_name;
					System.out.println(uid);
					System.out.println(name);
					mFriendshipsAPI.create(uid, name, mGuanzhuListener);
					circularButton.setProgress(10);
					System.out.println("circularButton is click");
				}
			});
		}

		if( position == 1){
			convertView = mLayoutInflater.inflate(R.layout.status_tab, null);
			reposts_layout = (RelativeLayout)convertView.findViewById(R.id.status_tab_respost_layout);
			comments_layout = (RelativeLayout)convertView.findViewById(R.id.status_tab_comment_layout);
			attitudes_layout = (RelativeLayout)convertView.findViewById(R.id.status_tab_zan_layout);
			reposts_count = (TextView)convertView.findViewById(R.id.status_tab_respost_count);
			comments_count = (TextView)convertView.findViewById(R.id.status_tab_comment_count);
			attitudes_count = (TextView)convertView.findViewById(R.id.status_tab_zan_count);
			reposts_text = (TextView)convertView.findViewById(R.id.status_tab_txt_repost);
			comments_text = (TextView)convertView.findViewById(R.id.status_tab_txt_comment);
			attitudes_text = (TextView)convertView.findViewById(R.id.status_tab_txt_zan);
			reposts_count.setText(String.valueOf(status.reposts_count));
			comments_count.setText(String.valueOf(status.comments_count));
			attitudes_count.setText(String.valueOf(status.attitudes_count));
			reposts_layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					reposts_count.setTextColor(context.getResources().getColor(R.color.orange));
					reposts_text.setTextColor(context.getResources().getColor(R.color.orange));
					comments_count.setTextColor(context.getResources().getColor(R.color.black2));
					comments_text.setTextColor(context.getResources().getColor(R.color.black2));
					attitudes_count.setTextColor(context.getResources().getColor(R.color.black2));
					attitudes_text.setTextColor(context.getResources().getColor(R.color.black2));
					setWhichIsClick(1);
				}
			});
			
			comments_layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					comments_count.setTextColor(context.getResources().getColor(R.color.orange));
					comments_text.setTextColor(context.getResources().getColor(R.color.orange));
					attitudes_count.setTextColor(context.getResources().getColor(R.color.black2));
					attitudes_text.setTextColor(context.getResources().getColor(R.color.black2));
					reposts_count.setTextColor(context.getResources().getColor(R.color.black2));
					reposts_text.setTextColor(context.getResources().getColor(R.color.black2));
					setWhichIsClick(2);
					((StatusActivity) context).LoadData(1);
				}
			});
			
			attitudes_layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					attitudes_count.setTextColor(context.getResources().getColor(R.color.orange));
					attitudes_text.setTextColor(context.getResources().getColor(R.color.orange));
					reposts_count.setTextColor(context.getResources().getColor(R.color.black2));
					reposts_text.setTextColor(context.getResources().getColor(R.color.black2));
					comments_count.setTextColor(context.getResources().getColor(R.color.black2));
					comments_text.setTextColor(context.getResources().getColor(R.color.black2));
					setWhichIsClick(3);
				}
			});
		}
		return convertView;
	}
	private void setColor(int index){
		switch (index) {
		case 1:
			
			break;

		default:
			break;
		}
	}
	private RequestListener mCreateListener = new RequestListener() {

		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			imgShoucang.setImageDrawable(context.getResources().getDrawable(R.drawable.card_icon_favorite_highlighted));
			isFavorite = true;
		}
	};

	private RequestListener mDestoryListener = new RequestListener() {

		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onComplete(String arg0) {
			imgShoucang.setImageDrawable(context.getResources().getDrawable(R.drawable.card_icon_favorite));
			isFavorite = false;
		}
	};

	private RequestListener mGuanzhuListener = new RequestListener() {

		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub		
		}

		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			circularButton.setProgress(100);
			System.out.println("关注api回调函数");
			Toast.makeText(context, "关注成功", Toast.LENGTH_SHORT).show();
			circularButton.setClickable(false);
		}
	};
}
