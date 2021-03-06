package com.bairutai.Adapter;

import java.text.ParseException;
import java.util.ArrayList;

import com.bairutai.application.WeiboApplication;
import com.bairutai.data.Constants;
import com.bairutai.sinaweibo.ImageExploreActivity;

import com.bairutai.sinaweibo.R;
import com.bairutai.sinaweibo.StatusActivity;
import com.bairutai.tools.HandleStatus;
import com.bairutai.model.Status;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.ActivityInvokeAPI;
import com.sina.weibo.sdk.openapi.legacy.FavoritesAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.squareup.picasso.Picasso;


import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyFirstPageAdapter extends BaseAdapter {
	//	private StatusList statusList;
	private LayoutInflater mLayoutInflater;
	private Context context;
	private ArrayList<Status> statusList;
	private WeiboApplication app;

	//moreDialog
	private View moreDialog_layout;
	private	Dialog moreDialog;
	private DisplayMetrics dm;
	private TextView shoucangTxt;
	private TextView tuiguangTxt;
	private TextView shanchuTxt;

	private View moreDialog_layout2;
	private Dialog moreDialog2;
	private TextView shoucangTxt2;
	private TextView toutiaoTxt;
	private TextView quxiaoguanzhuTxt;
	private TextView pingbiTxt;
	private TextView jubaoTxt;

	private 	int id[] = { R.id.img1, R.id.img2, R.id.img3, R.id.img4, R.id.img5,
			R.id.img6, R.id.img7, R.id.img8, R.id.img9 };

	private StatusesAPI mStatusesAPI;
	private FavoritesAPI mFavoritesAPI;
	private int index = 0;
	private String deleteStatusId;
	private String quxiaoshoucangId;
	private String shoucangId;

	public MyFirstPageAdapter(Context context, WeiboApplication app) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.app = app;
		mLayoutInflater = LayoutInflater.from(context);
		statusList = app.mDataBaseHelper.queryStatus("order by status_id DESC limit 50");
		if(statusList!=null){
			app.setSince_id(Long.parseLong(statusList.get(0).id));
		}else{
			app.setSince_id((long) 0);
		}
		mStatusesAPI = new StatusesAPI(context, Constants.APP_KEY, app.getmAccessToken());
		mFavoritesAPI = new FavoritesAPI(context, Constants.APP_KEY, app.getmAccessToken());
		prepareDialog();
	}

	private void prepareDialog() {
		// TODO Auto-generated method stub
		moreDialog_layout = mLayoutInflater.inflate(R.layout.follow_status_more_dialog, null);
		shoucangTxt = (TextView) moreDialog_layout.findViewById(R.id.follow_status_more_dialog_shoucang);
		tuiguangTxt = (TextView) moreDialog_layout.findViewById(R.id.follow_status_more_dialog_tuiguang);
		shanchuTxt = (TextView)moreDialog_layout.findViewById(R.id.follow_status_more_dialog_shanchu);
		moreDialog=new Dialog(context,R.style.MyDialog);
		moreDialog.setContentView(moreDialog_layout);
		moreDialog.setCanceledOnTouchOutside(true);
		dm = new DisplayMetrics();
		dm = app.getResources().getDisplayMetrics();
		WindowManager.LayoutParams params = moreDialog.getWindow().getAttributes();
		params.width = dm.widthPixels/3*2;
		params.height = params.height;
		moreDialog.getWindow().setAttributes(params);
		
		
		moreDialog_layout2 = mLayoutInflater.inflate(R.layout.follow_status_more_dialog2, null);
		shoucangTxt2 = (TextView)moreDialog_layout2.findViewById(R.id.follow_status_more_dialog2_shoucang);
		toutiaoTxt = (TextView)moreDialog_layout2.findViewById(R.id.follow_status_more_dialog2_toutiao);
		quxiaoguanzhuTxt = (TextView)moreDialog_layout2.findViewById(R.id.follow_status_more_dialog2_quxiaoguanzhu);
		pingbiTxt = (TextView)moreDialog_layout2.findViewById(R.id.follow_status_more_dialog2_pingbi);
		jubaoTxt = (TextView)moreDialog_layout2.findViewById(R.id.follow_status_more_dialog2_jubao);
		moreDialog2 = new Dialog(context,R.style.MyDialog);
		moreDialog2.setContentView(moreDialog_layout2);
		moreDialog2.setCanceledOnTouchOutside(true);
		WindowManager.LayoutParams params2 = moreDialog2.getWindow().getAttributes();
		params2.width = dm.widthPixels/3*2;
		moreDialog2.getWindow().setAttributes(params2);
	}

	public void update(Status status) {
		statusList.add(status);
		notifyDataSetChanged();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			System.out.println(position);
			convertView = (LinearLayout)mLayoutInflater.inflate(R.layout.myfirstpagelistitem, null);
			viewHolder = new ViewHolder();
			viewHolder.status_layout = (LinearLayout)convertView.findViewById(R.id.myfirstpagelistitem_layout);
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
			viewHolder.time.setText(HandleStatus.DateFormat(statusList.get(position).created_at));
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

		HandleStatus handleStatus = new HandleStatus(context);
		handleStatus.handle(viewHolder.text,statusList.get(position).text);
		//			handleStatus(viewHolder.text,statusList.get(position).text);

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
			HandleStatus handleStatus_ret = new HandleStatus(context);
			handleStatus_ret.handle(viewHolder.repost_text,retpost_text);
			//				handleStatus();
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
							intent.putStringArrayListExtra("image", HandleStatus.changeBmiddle(pic_urls));
							intent.putExtra("nowImage", HandleStatus.judgeNowImage(v.getId()));
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
			viewHolder.repost_layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(context, StatusActivity.class);
					intent.putExtra("ret_statusid", Long.parseLong(statusList.get(position).retweeted_status.id));
					context.startActivity(intent);
				}
			});

			viewHolder.repost_layout.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					return false;
				}
			});
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
						intent.putStringArrayListExtra("image", HandleStatus.changeBmiddle(pic_urls));
						intent.putExtra("nowImage", HandleStatus.judgeNowImage(v.getId()));
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
				if (statusList.get(position).user.id.equalsIgnoreCase(app.getUser().id)) {
					if(app.mFavoritesMap.containsKey(statusList.get(position).id)) {
						shoucangTxt.setText("取消收藏");
						shoucangTxt.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								mFavoritesAPI.destroy(Long.parseLong(statusList.get(position).id), mFavoritesDestorylistener);
							}
						});
					}else{
						shoucangTxt.setText("收藏");
						shoucangTxt.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								mFavoritesAPI.create(Long.parseLong(statusList.get(position).id), mFavoriteslistener);
								shoucangId = statusList.get(position).id;
							}
						});
					}
					
					tuiguangTxt.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							
						}
					});
					
					shanchuTxt.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							mStatusesAPI.destroy(Long.parseLong(statusList.get(position).id), mDestoryListener);
							index =  position;
							deleteStatusId = statusList.get(position).id;
						}
					});
					moreDialog.show();

				}else {
					if(app.mFavoritesMap.containsKey(statusList.get(position).id)) {
						shoucangTxt2.setText("取消收藏");
						shoucangTxt2.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								mFavoritesAPI.destroy(Long.parseLong(statusList.get(position).id), mFavoritesDestorylistener);
								quxiaoshoucangId = statusList.get(position).id;
							}
						});
					}else{
						shoucangTxt2.setText("收藏");
						shoucangTxt2.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								mFavoritesAPI.create(Long.parseLong(statusList.get(position).id), mFavoriteslistener);
								shoucangId = statusList.get(position).id;
							}
						});
					}
					
					toutiaoTxt.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							DismissDialog();
						}
					});
					
					quxiaoguanzhuTxt.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							DismissDialog();
						}
					});
					
					pingbiTxt.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							DismissDialog();
						}
					});
					
					jubaoTxt.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							DismissDialog();
						}
					});
					
					moreDialog2.show();
				}
				
			}
		});

		viewHolder.imgIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ActivityInvokeAPI.openUserInfoByNickName((Activity) context, statusList.get(position).user.screen_name);
			}
		});

		viewHolder.status_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, StatusActivity.class);
				intent.putExtra("statusid", statusList.get(position).id);
				context.startActivity(intent);
			}
		});

		viewHolder.status_layout.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		return convertView;
	}


	private class ViewHolder{
		private LinearLayout status_layout;
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

	private RequestListener mFavoritesDestorylistener = new RequestListener() {
		
		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			if (!arg0.isEmpty()){
				DismissDialog();
				Toast.makeText(context, "已取消收藏", Toast.LENGTH_LONG).show();
				app.mFavoritesMap.put(quxiaoshoucangId, false);
			}
		}
	};
	
	private RequestListener mFavoriteslistener = new RequestListener() {

		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			if (!arg0.isEmpty()) {
				DismissDialog();
				Toast.makeText(context, "已收藏", Toast.LENGTH_LONG).show();
				app.mFavoritesMap.put(shoucangId, true);
			}
		}
	};

	private void DismissDialog()
	{
		if(moreDialog.isShowing()){
			moreDialog.dismiss();
		}
		if (moreDialog2.isShowing()){
			moreDialog2.dismiss();
		}
	}
	
	private RequestListener mDestoryListener = new RequestListener() {

		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			if (!arg0.isEmpty()) {
				statusList.remove(index);
				DismissDialog();
				app.mDataBaseHelper.mSQLiteDatabase.execSQL("delete from status where status_id = "+deleteStatusId);
				notifyDataSetChanged();
			}
		}
	};
}
