package com.bairutai.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {
	
	public DataBase(Context context, String name, CursorFactory factory,int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public DataBase(Context context, String name, int version){
		this(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql_user = "create table user(_id integer primary key autoincrement,"
				+ "userid integer unique,"
				+ "screen_name text,"
				+ "name text,"
				+ "location text,"
				+ "description text,"
				+ "url text,"
				+ "profile_image_url text,"
				+ "gender text,"
				+ "followers_count integer,"
				+ "friends_count integer,"
				+ "statuses_count interger,"
				+ "favourites_count integer,"
				+ "verified integer,"
				+ "verified_type integer,"
				+ "status_text text,"
				+ "avatarLarge text,"
				+ "avatarHd text,"
				+ "verifiedReason text,"
				+ "follow_me interger,"
				+ "online_status integer,"
				+ "created_at text,"
				+ "following integer,"
				+ "status_from text"
				+ ")";
		db.execSQL(sql_user);
		System.out.println("Create Database user");

		String sql_status = "create table status(_id integer primary key autoincrement,"
				+"status_id text unique,"
				+"created_at text,"
				+"user_id text,"
				+"source text,"
				+ "text text," 
				+ "favorited integer,"
				+"thumbnail text,"
				+"bmiddle text,"
				+"original text,"
				+"retweeted_status_id integer,"
				+"reposts_count integer,"
				+"comments_count integer,"
				+"attitudes_count integer,"
				+"piclist_id text)";
		db.execSQL(sql_status);
		System.out.println("Create Database status");
		
		String sql_retweet_status = "create table retweet_status(	_id integer primary key autoincrement,"
				+"mainId integer,"
				+"status_id integer unique,"
				+"created_at text," 
				+"user_id text,"
				+"source text,"
				+ "text text," 
				+ "favorited integer,"
				+"thumbnail text,"
				+"bmiddle text,"
				+"original text,"
				+"reposts_count integer,"
				+"comments_count integer,"
				+"attitudes_count integer,"
				+"piclist_id text)";
		db.execSQL(sql_retweet_status);
		System.out.println("Create Database sql_retweet_status");
		
		String sql_pic_urls = "create table pic_urls(_id integer primary key autoincrement,"
				+"pic_urls text unique,"
				+"status_id integer)";
		db.execSQL(sql_pic_urls);
		System.out.println("Create Database sql_pic_urls");
		
		String sql_emotions = "create table emotions(_id integer primary key autoincrement,"
				+"phrase text unique,"
				+"url text)";
		db.execSQL(sql_emotions);
		System.out.println("Create Database emotions");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

}
