package com.bairutai.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
				+ "status_text text,"
				+ "avatarLarge text,"
				+ "avatarHd text,"
				+ "verifiedReason text,"
				+ "follow_me interger,"
				+ "online_status integer,"
				+ "created_at text"
				+ ")";
		db.execSQL(sql_user);
		System.out.println("Create Database");

		String sql = "create table status(	_id integer primary key autoincrement,"
				+"statusid integer unique,"
				+"creatat text," 
				+"source text,"
				+ "text text," 
				+ "favorited integer,"
				+"thumbnail text,"
				+"bmiddle text,"
				+"original text,"
				+"userid integer,"
				+"reposts integer,"
				+"comments integer,"
				+"attitudes integer,"
				+"piclist integer)";
		Log.i("ABC", "dbhelper oncreate");
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

}
