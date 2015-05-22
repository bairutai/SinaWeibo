package com.bairutai.database;

import java.util.ArrayList;
import java.util.List;

import com.bairutai.model.Friend;
import com.sina.weibo.sdk.openapi.models.User;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {
	private Context context;
	public SQLiteDatabase mSQLiteDatabase;


	public DataBase(Context context, String name, CursorFactory factory,int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		this.context = context;
		mSQLiteDatabase = getWritableDatabase();
	}
	
	public DataBase(Context context, String name, int version){
		this(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql_user = "create table user(_id integer primary key autoincrement,"
				+ "userid integer,"
				+ "screen_name text,"
				+ "name text,"
				+ "location text,"
				+ "description text,"
				+ "url text,"
				+ "profileimage text,"
				+ "gender text,"
				+ "followers integer,"
				+ "friends integer,"
				+ "favourites integer,"
				+ "verified integer,"
				+ "statuses integer,"
				+ "avatarLarge text,"
				+ "avatarHd text,"
				+ "verifiedReason text,"
				+ "online integer,"
				+ "created_at text"
				+ ")";
		db.execSQL(sql_user);
		System.out.println("Create Database");
		
		String sql_friend = "create table flower(_id integer primary key autoincrement,"
				+ "userid integer,"
				+ "screen_name text,"
				+ "name text,"
				+ "location text,"
				+ "description text,"
				+ "url text,"
				+ "profileimage text,"
				+ "gender text,"
				+ "followers integer,"
				+ "friends integer,"
				+ "favourites integer,"
				+ "verified integer,"
				+ "statuses integer,"
				+ "avatarLarge text,"
				+ "avatarHd text,"
				+ "verifiedReason text,"
				+ "online integer,"
				+ "created_at text,"
				+"text text"
				+ ")";
		db.execSQL(sql_friend);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql_drop = "drop table if exists user";
		db.execSQL(sql_drop);
		System.out.println("user has been drop");
		sql_drop = "drop table if exists flower";
		db.execSQL(sql_drop);
		System.out.println("flower has been drop");
		onCreate(db);
	}
	
	public void dropDataBase(){
		String sql_drop = "drop table if exists user";
		mSQLiteDatabase.execSQL(sql_drop);
		System.out.println("user has been drop");
		sql_drop = "drop table if exists flower";
		mSQLiteDatabase.execSQL(sql_drop);
		System.out.println("flower has been drop");
	}
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
		


		String sql_user = "insert into user("
				+ " userid,screen_name,name,location,description,url,profileimage,gender,followers,"
				+ "friends,statuses,favourites,verified,avatarLarge,avatarHd,verifiedReason,online,created_at)"
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		mSQLiteDatabase.execSQL(sql_user, new Object[] { user.id, user.screen_name,
				user.name, user.location, user.description, user.url,
				user.profile_image_url, user.gender, user.followers_count,
				user.friends_count, user.statuses_count, user.favourites_count,
				user.verified, user.avatar_large, user.avatar_hd,
				user.verified_reason, user.online_status, user.created_at });
	}
	
	/**
	 * 添加用户粉丝信息到数据库
	 * 
	 * @param user
	 */
	public void addFriend(Friend user){

		String sql_user = "insert into flower("
				+ " userid,screen_name,name,location,description,url,profileimage,gender,followers,"
				+ "friends,statuses,favourites,verified,avatarLarge,avatarHd,verifiedReason,online,created_at)"
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		mSQLiteDatabase.execSQL(sql_user, new Object[] { user.id, user.screen_name,
				user.name, user.location, user.description, user.url,
				user.profile_image_url, user.gender, user.followers_count,
				user.friends_count, user.statuses_count, user.favourites_count,
				user.verified, user.avatar_large, user.avatar_hd,
				user.verified_reason, user.online_status, user.created_at });
	}
	
	/**
	 * 查询个人信息
	 * @return
	 */

	public User queryUser() {
		Cursor cursor = mSQLiteDatabase.rawQuery("select * from user",null);
		if (cursor.moveToFirst() == false)
			return null;
		User user = new User();
		user.id = String.valueOf(cursor.getLong(cursor.getColumnIndex("userid")));
		user.screen_name = cursor.getString(cursor.getColumnIndex("screen_name"));
		user.name = cursor.getString(cursor.getColumnIndex("name"));
		user.location = cursor.getString(cursor.getColumnIndex("location"));
		user.description = cursor.getString(cursor.getColumnIndex("description"));
		user.url = cursor.getString(cursor.getColumnIndex("url"));
		user.profile_image_url = cursor.getString(cursor.getColumnIndex("profileimage"));
		user.gender = cursor.getString(cursor.getColumnIndex("gender"));
		user.followers_count = cursor.getInt(cursor.getColumnIndex("followers"));
		user.friends_count = cursor.getInt(cursor.getColumnIndex("friends"));
		user.statuses_count = cursor.getInt(cursor.getColumnIndex("statuses"));
		user.favourites_count = cursor.getInt(cursor.getColumnIndex("favourites"));
		user.verified = cursor.getInt(cursor.getColumnIndex("verified")) == 1 ? true: false;
		user.avatar_large = cursor.getString(cursor.getColumnIndex("avatarLarge"));
		user.avatar_hd = cursor.getString(cursor.getColumnIndex("avatarHd"));
		user.verified_reason = cursor.getString(cursor.getColumnIndex("verifiedReason"));
		user.online_status = cursor.getInt(cursor.getColumnIndex("online"));
		user.created_at = cursor.getString(cursor.getColumnIndex("created_at"));
		return user;
	}
	
	public ArrayList<Friend> queryFriend() {
		ArrayList<Friend> friendlist = new ArrayList<Friend>();
		Cursor cursor = mSQLiteDatabase.rawQuery("select * from flower", null);
		Friend user = null;
		System.out.println("cursor is not null a a a ");
        while (cursor.moveToNext()) {
        	System.out.println("cursor is not null");
            user = new Friend();
    		user.id = String.valueOf(cursor.getLong(cursor.getColumnIndex("userid")));
    		user.screen_name = cursor.getString(cursor.getColumnIndex("screen_name"));
    		user.name = cursor.getString(cursor.getColumnIndex("name"));
    		user.location = cursor.getString(cursor.getColumnIndex("location"));
    		user.description = cursor.getString(cursor.getColumnIndex("description"));
    		user.url = cursor.getString(cursor.getColumnIndex("url"));
    		user.profile_image_url = cursor.getString(cursor.getColumnIndex("profileimage"));
    		user.gender = cursor.getString(cursor.getColumnIndex("gender"));
    		user.followers_count = cursor.getInt(cursor.getColumnIndex("followers"));
    		user.friends_count = cursor.getInt(cursor.getColumnIndex("friends"));
    		user.statuses_count = cursor.getInt(cursor.getColumnIndex("statuses"));
    		user.favourites_count = cursor.getInt(cursor.getColumnIndex("favourites"));
    		user.verified = cursor.getInt(cursor.getColumnIndex("verified")) == 1 ? true: false;
    		user.avatar_large = cursor.getString(cursor.getColumnIndex("avatarLarge"));
    		user.avatar_hd = cursor.getString(cursor.getColumnIndex("avatarHd"));
    		user.verified_reason = cursor.getString(cursor.getColumnIndex("verifiedReason"));
    		user.online_status = cursor.getInt(cursor.getColumnIndex("online"));
    		user.created_at = cursor.getString(cursor.getColumnIndex("created_at"));
    		friendlist.add(user);
        }  
		return friendlist;
		
	}
	
}
