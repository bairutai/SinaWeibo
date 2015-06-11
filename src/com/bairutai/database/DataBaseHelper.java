package com.bairutai.database;

import java.util.ArrayList;

import com.bairutai.model.User;
import com.bairutai.model.Status;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseHelper {
	private Context context;
	private DataBase mDatabae;
	public SQLiteDatabase mSQLiteDatabase;

	public DataBaseHelper(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		mDatabae = new DataBase(context, "data",null, 1);
		mSQLiteDatabase = mDatabae.getWritableDatabase();
	}

	/**
	 * 删除数据库
	 * @param database
	 */
	public void dropDataBase(String database){
		String sql_drop = "drop table if exists "+database;
		mSQLiteDatabase.execSQL(sql_drop);
		String str = String.format("%s has been drop", database);
		System.out.println(str);
	}

	/**
	 * 清空数据库数据
	 * @param database
	 */
	public void cleanDataBase(String database) {
		String sql_clean = "delete from " + database;
		mSQLiteDatabase.execSQL(sql_clean);
	}

	/**
	 * 添加用户个人信息到数据库
	 * 
	 * @param user
	 */
	public void addUser(User user){
		String sql_user = "replace into user("
				+ " userid,screen_name,name," 
				+"location,description,url," 
				+"profile_image_url,gender,followers_count,"
				+ "friends_count,statuses_count,favourites_count," 
				+"verified,verified_type,follow_me,status_text,avatarLarge," 
				+"avatarHd,verifiedReason,online_status," 
				+"created_at)"
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		mSQLiteDatabase.execSQL(sql_user, new Object[] {
				user.id, user.screen_name,user.name, 
				user.location, user.description, user.url,
				user.profile_image_url, user.gender, user.followers_count,
				user.friends_count, user.statuses_count, user.favourites_count,
				user.verified,user.verified_type,user.follow_me==true?1:0, 
				user.status_text,user.avatar_large, user.avatar_hd,
				user.verified_reason, user.online_status, user.created_at });
	}

	/**
	 * 添加status到数据库
	 * 
	 * @param status
	 */
	public void addStatus(Status status) {
		String sql = "replace into status("
				+"status_id,"
				+"created_at," 
				+"user_id,"
				+"source,"
				+ "text," 
				+ "favorited,"
				+"thumbnail,"
				+"bmiddle,"
				+"original,"
				+"retweeted_status_id,"
				+"reposts_count,"
				+"comments_count,"
				+"attitudes_count,"
				+"piclist_id)"
				+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		long retweeted_status_id = 0;
		addUser(status.user);
		if (status.pic_urls != null) {
			addPicUrls(status.pic_urls,status.id);
		}

		if (status.retweeted_status != null) {
			retweeted_status_id = Long.parseLong(status.retweeted_status.id);
			addRetweetedStatus(status.retweeted_status, status.id);
			addUser(status.retweeted_status.user);
		}

		mSQLiteDatabase.execSQL(sql, new Object[] { 
				status.id, status.created_at,status.user.id,
				status.source,status.text,  status.favorited,
				status.thumbnail_pic, status.bmiddle_pic, status.original_pic,
				retweeted_status_id, status.reposts_count, status.comments_count,
				status.attitudes_count, status.id });	
	}


	/**
	 * 添加转发微博信息到数据库
	 * @param status
	 */
	public void addRetweetedStatus(Status status,String mainId) {
		String sql = "replace into retweet_status("
				+"mainId,"
				+"status_id,"
				+"created_at," 
				+ "user_id,"
				+"source,"
				+ "text," 
				+ "favorited,"
				+"thumbnail,"
				+"bmiddle,"
				+"original,"
				+"reposts_count,"
				+"comments_count,"
				+"attitudes_count,"
				+"piclist_id)"
				+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		addUser(status.user);
		if (status.pic_urls != null) {
			addPicUrls(status.pic_urls,status.id);
		}

		mSQLiteDatabase.execSQL(sql, new Object[] { 
				Long.parseLong(mainId),status.id, status.created_at,status.user.id,
				 status.source,status.text, status.favorited,
				status.thumbnail_pic, status.bmiddle_pic, status.original_pic,
				status.reposts_count, status.comments_count,
				status.attitudes_count, status.id });

	}

	/**
	 * 添加微博图片地址到数据库
	 * @param pic_urls
	 * @param status_id
	 */
	public void addPicUrls(ArrayList<String> pic_urls, String status_id ) {

		for (int i = 0; i < pic_urls.size(); i++) {
			String sql_pic = "replace into pic_urls ("
					+ "pic_urls," 
					+ "status_id)"
					+ "values(?,?)";
			mSQLiteDatabase.execSQL(sql_pic, new Object[] { pic_urls.get(i), Long.parseLong(status_id) });
		}
	}
	



	/**
	 * 查询用户信息
	 * @return
	 */
	public User queryUser(String userid) {
		Cursor cursor = mSQLiteDatabase.rawQuery("select * from user where userid = ?",new String[] {userid});
		if (cursor.moveToFirst() == false)
			return null;
		User user = new User();
		user.id = String.valueOf(cursor.getLong(cursor.getColumnIndex("userid")));
		user.screen_name = cursor.getString(cursor.getColumnIndex("screen_name"));
		user.name = cursor.getString(cursor.getColumnIndex("name"));
		user.location = cursor.getString(cursor.getColumnIndex("location"));
		user.description = cursor.getString(cursor.getColumnIndex("description"));
		user.url = cursor.getString(cursor.getColumnIndex("url"));
		user.profile_image_url = cursor.getString(cursor.getColumnIndex("profile_image_url"));
		user.gender = cursor.getString(cursor.getColumnIndex("gender"));
		user.followers_count = cursor.getInt(cursor.getColumnIndex("followers_count"));
		user.friends_count = cursor.getInt(cursor.getColumnIndex("friends_count"));
		user.statuses_count = cursor.getInt(cursor.getColumnIndex("statuses_count"));
		user.favourites_count = cursor.getInt(cursor.getColumnIndex("favourites_count"));
		user.verified = cursor.getInt(cursor.getColumnIndex("verified")) == 1 ? true: false;
		user.verified_type = cursor.getInt(cursor.getColumnIndex("verified_type"));
		user.status_text = cursor.getString(cursor.getColumnIndex("status_text"));
		user.avatar_large = cursor.getString(cursor.getColumnIndex("avatarLarge"));
		user.avatar_hd = cursor.getString(cursor.getColumnIndex("avatarHd"));
		user.verified_reason = cursor.getString(cursor.getColumnIndex("verifiedReason"));
		user.follow_me = cursor.getInt(cursor.getColumnIndex("follow_me")) == 1 ? true: false;
		user.online_status = cursor.getInt(cursor.getColumnIndex("online_status"));
		user.created_at = cursor.getString(cursor.getColumnIndex("created_at"));
		cursor.close();
		return user;
	}

	public ArrayList<User> queryFlower() {
		ArrayList<User> flowerlist = new ArrayList<User>();
		Cursor cursor = mSQLiteDatabase.rawQuery("select * from user where follow_me = ? limit 10", new String[]{"1"});
		while (cursor.moveToNext()) {
			System.out.println("flower cursor is not null");
			User user = new User();
			user.id = String.valueOf(cursor.getLong(cursor.getColumnIndex("userid")));
			user.screen_name = cursor.getString(cursor.getColumnIndex("screen_name"));
			user.name = cursor.getString(cursor.getColumnIndex("name"));
			user.location = cursor.getString(cursor.getColumnIndex("location"));
			user.description = cursor.getString(cursor.getColumnIndex("description"));
			user.url = cursor.getString(cursor.getColumnIndex("url"));
			user.profile_image_url = cursor.getString(cursor.getColumnIndex("profile_image_url"));
			user.gender = cursor.getString(cursor.getColumnIndex("gender"));
			user.followers_count = cursor.getInt(cursor.getColumnIndex("followers_count"));
			user.friends_count = cursor.getInt(cursor.getColumnIndex("friends_count"));
			user.statuses_count = cursor.getInt(cursor.getColumnIndex("statuses_count"));
			user.favourites_count = cursor.getInt(cursor.getColumnIndex("favourites_count"));
			user.verified = cursor.getInt(cursor.getColumnIndex("verified")) == 1 ? true: false;
			user.verified_type = cursor.getInt(cursor.getColumnIndex("verified_type"));
			user.status_text = cursor.getString(cursor.getColumnIndex("status_text"));
			user.avatar_large = cursor.getString(cursor.getColumnIndex("avatarLarge"));
			user.avatar_hd = cursor.getString(cursor.getColumnIndex("avatarHd"));
			user.verified_reason = cursor.getString(cursor.getColumnIndex("verifiedReason"));
			user.follow_me = cursor.getInt(cursor.getColumnIndex("follow_me")) == 1 ? true: false;
			user.online_status = cursor.getInt(cursor.getColumnIndex("online_status"));
			user.created_at = cursor.getString(cursor.getColumnIndex("created_at"));
			flowerlist.add(user);
		}
		cursor.close();
		return flowerlist;
	}

	/**
	 * 从数据库获取statuslist
	 * 
	 * @param cursor
	 * @param Statusid
	 * @return
	 */
	public  ArrayList<Status> queryStatus() {
		ArrayList<Status> Statuslist = new ArrayList<Status>();
		Cursor cursor = mSQLiteDatabase.rawQuery("select * from status order by status_id DESC limit 50",null);
		if (cursor.moveToFirst() == false)
			return null;
		while(cursor.moveToNext()) {
			Status status = new Status();
			status.id = cursor.getString(cursor.getColumnIndex("status_id"));
			status.created_at = cursor.getString(cursor.getColumnIndex("created_at"));
			status.source = cursor.getString(cursor.getColumnIndex("source"));
			status.text = cursor.getString(cursor.getColumnIndex("text"));
			status.favorited = cursor.getInt(cursor.getColumnIndex("favorited")) == 1 ? true : false;
			status.thumbnail_pic = cursor.getString(cursor.getColumnIndex("thumbnail"));
			status.bmiddle_pic = cursor.getString(cursor.getColumnIndex("bmiddle"));
			status.original_pic =  cursor.getString(cursor.getColumnIndex("original"));
			long retweeted_status_id = cursor.getLong(cursor.getColumnIndex("retweeted_status_id"));
			if (retweeted_status_id != 0) {
				status.retweeted_status = queryRetweetedStatus(retweeted_status_id);
			}
			String userId = cursor.getString(cursor.getColumnIndex("user_id"));
			status.user = queryUser(userId);
			status.comments_count = cursor.getInt(cursor.getColumnIndex("comments_count"));
			status.attitudes_count = cursor.getInt(cursor.getColumnIndex("attitudes_count"));
			status.reposts_count = cursor.getInt(cursor.getColumnIndex("reposts_count"));
			String status_id = cursor.getString(cursor.getColumnIndex("piclist_id"));
			status.pic_urls = queryPicList(status_id);
			Statuslist.add(status);
		}
		cursor.close();
		return Statuslist;
	}
	
	/**
	 * 从数据库获取微博配图地址
	 * @param status_id
	 * @return
	 */
	public ArrayList<String> queryPicList(String status_id) {
		// TODO Auto-generated method stub
		String sql = "select * from pic_urls where status_id = "+ status_id;
		Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
		if (cursor.moveToFirst() == false)
			return null;

		ArrayList<String> list = new ArrayList<String>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			list.add(cursor.getString(cursor.getColumnIndex("pic_urls")));
		}
		cursor.close();
		return list;
	}

	/**
	 * 从数据库获取转发微博信息
	 * @param retweeted_status_id
	 * @return
	 */
	public Status queryRetweetedStatus(long retweeted_status_id) {
		// TODO Auto-generated method stub
		String sql = "select * from retweet_status where status_id =" + retweeted_status_id;
		Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
		if (cursor.moveToFirst() == false)
			return null;
		Status status = new Status();
		status.id = cursor.getString(cursor.getColumnIndex("status_id"));
		status.created_at = cursor.getString(cursor.getColumnIndex("created_at"));
		status.source = cursor.getString(cursor.getColumnIndex("source"));
		status.text = cursor.getString(cursor.getColumnIndex("text"));
		status.favorited = cursor.getInt(cursor.getColumnIndex("favorited")) == 1 ? true : false;
		status.thumbnail_pic = cursor.getString(cursor.getColumnIndex("thumbnail"));
		status.bmiddle_pic = cursor.getString(cursor.getColumnIndex("bmiddle"));
		status.original_pic =  cursor.getString(cursor.getColumnIndex("original"));
		String userId = cursor.getString(cursor.getColumnIndex("user_id"));
		status.user = queryUser(userId);
		status.comments_count = cursor.getInt(cursor.getColumnIndex("comments_count"));
		status.attitudes_count = cursor.getInt(cursor.getColumnIndex("attitudes_count"));
		status.reposts_count = cursor.getInt(cursor.getColumnIndex("reposts_count"));
		String status_id = cursor.getString(cursor.getColumnIndex("piclist_id"));
		status.pic_urls = queryPicList(status_id);
		cursor.close();
		return status;
	}
}
