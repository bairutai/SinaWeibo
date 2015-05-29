package com.bairutai.database;

import java.util.ArrayList;

import com.bairutai.model.User;
import com.sina.weibo.sdk.openapi.models.Status;


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
		mDatabae = new DataBase(context, "user",null, 1);
		mDatabae = new DataBase(context, "status",null, 1);
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
				+"verified,follow_me,status_text,avatarLarge," 
				+"avatarHd,verifiedReason,online_status," 
				+"created_at)"
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		mSQLiteDatabase.execSQL(sql_user, new Object[] {
				user.id, user.screen_name,user.name, 
				user.location, user.description, user.url,
				user.profile_image_url, user.gender, user.followers_count,
				user.friends_count, user.statuses_count, user.favourites_count,
				user.verified,user.follow_me==true?1:0, user.status_text,user.avatar_large, 
				user.avatar_hd,user.verified_reason, user.online_status, 
				user.created_at });
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
		user.status_text = cursor.getString(cursor.getColumnIndex("status_text"));
		user.avatar_large = cursor.getString(cursor.getColumnIndex("avatarLarge"));
		user.avatar_hd = cursor.getString(cursor.getColumnIndex("avatarHd"));
		user.verified_reason = cursor.getString(cursor.getColumnIndex("verifiedReason"));
		user.follow_me = cursor.getInt(cursor.getColumnIndex("follow_me")) == 1 ? true: false;
		user.online_status = cursor.getInt(cursor.getColumnIndex("online_status"));
		user.created_at = cursor.getString(cursor.getColumnIndex("created_at"));
		return user;
	}

	public ArrayList<User> queryFlower() {
		ArrayList<User> flowerlist = new ArrayList<User>();
		Cursor cursor = mSQLiteDatabase.rawQuery("select * from user where follow_me = ?", new String[]{"1"});
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
			user.status_text = cursor.getString(cursor.getColumnIndex("status_text"));
			user.avatar_large = cursor.getString(cursor.getColumnIndex("avatarLarge"));
			user.avatar_hd = cursor.getString(cursor.getColumnIndex("avatarHd"));
			user.verified_reason = cursor.getString(cursor.getColumnIndex("verifiedReason"));
			user.follow_me = cursor.getInt(cursor.getColumnIndex("follow_me")) == 1 ? true: false;
			user.online_status = cursor.getInt(cursor.getColumnIndex("online_status"));
			user.created_at = cursor.getString(cursor.getColumnIndex("created_at"));
			flowerlist.add(user);
		}
		return flowerlist;

	}
}
