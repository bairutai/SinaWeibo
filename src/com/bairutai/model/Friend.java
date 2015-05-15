package com.bairutai.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.bairutai.application.WeiboApplication;
import com.bairutai.database.DataBase;
import com.sina.weibo.sdk.openapi.models.Status;

public class Friend {
	/** 用户UID（int64） */
	public String id;
	/** 用户昵称 */
	public String screen_name;
	/** 友好显示名称 */
	public String name;
	/** 用户所在省级ID */
	public int province;
	/** 用户所在城市ID */
	public int city;
	/** 用户所在地 */
	public String location;
	/** 用户个人描述 */
	public String description;
	/** 用户博客地址 */
	public String url;
	/** 用户头像地址，50×50像素 */
	public String profile_image_url;
	/** 用户的微博统一URL地址 */
	public String profile_url;
	/** 用户的个性化域名 */
	public String domain;
	/** 性别，m：男、f：女、n：未知 */
	public String gender;
	/** 粉丝数 */
	public int followers_count;
	/** 关注数 */
	public int friends_count;
	/** 微博数 */
	public int statuses_count;
	/** 收藏数 */
	public int favourites_count;
	/** 用户创建（注册）时间 */
	public String created_at;
	/** 暂未支持 */
	public boolean following;
	/** 是否允许所有人给我发私信，true：是，false：否 */
	public boolean allow_all_act_msg;
	/** 是否允许标识用户的地理位置，true：是，false：否 */
	public boolean geo_enabled;
	/** 是否是微博认证用户，即加V用户，true：是，false：否 */
	public boolean verified;
	/** 暂未支持 */
	public int verified_type;
	/** 用户备注信息，只有在查询用户关系时才返回此字段 */
	public String remark;
	/** 用户的最近一条微博信息字段 */
	public Status status;
	/** 是否允许所有人对我的微博进行评论，true：是，false：否 */
	public boolean allow_all_comment;
	/** 用户大头像地址 */
	public String avatar_large;
	/** 用户高清大头像地址 */
	public String avatar_hd;
	/** 认证原因 */
	public String verified_reason;
	/** 该用户是否关注当前登录用户，true：是，false：否 */
	public boolean follow_me;
	/** 用户的在线状态，0：不在线、1：在线 */
	public int online_status;
	/** 用户的互粉数 */
	public int bi_followers_count;
	/** 用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语 */
	public String lang;
	public String idstr;
	public String weihao;

	public int  next_cursor;
	public int previous_cursor;
	public int total_number;


	
	public  static int parse(String jsonString,WeiboApplication app) {
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			return Friend.parse(jsonObject,app);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static  int parse(JSONObject jsonObject1,WeiboApplication app) throws JSONException{
		if (null == jsonObject1) {
			return 0;
		}

		JSONArray jsonArray=jsonObject1.getJSONArray("users");
		
		for(int i=0;i<jsonArray.length();i++) {
			
			JSONObject jsonObject = (JSONObject)jsonArray.get(i);
			Friend friend = new Friend();
			friend.id                 = jsonObject.optString("id", "");
			friend.idstr              = jsonObject.optString("idstr", "");
			friend.screen_name        = jsonObject.optString("screen_name", "");
			friend.name               = jsonObject.optString("name", "");
			friend.province           = jsonObject.optInt("province", -1);
			friend.city               = jsonObject.optInt("city", -1);
			friend.location           = jsonObject.optString("location", "");
			friend.description        = jsonObject.optString("description", "");
			friend.url                = jsonObject.optString("url", "");
			friend.profile_image_url  = jsonObject.optString("profile_image_url", "");
			friend.profile_url        = jsonObject.optString("profile_url", "");
			friend.domain             = jsonObject.optString("domain", "");
			friend.weihao             = jsonObject.optString("weihao", "");
			friend.gender             = jsonObject.optString("gender", "");
			friend.followers_count    = jsonObject.optInt("followers_count", 0);
			friend.friends_count      = jsonObject.optInt("friends_count", 0);
			friend.statuses_count     = jsonObject.optInt("statuses_count", 0);
			friend.favourites_count   = jsonObject.optInt("favourites_count", 0);
			friend.created_at         = jsonObject.optString("created_at", "");
			friend.following          = jsonObject.optBoolean("following", false);
			friend.allow_all_act_msg  = jsonObject.optBoolean("allow_all_act_msg", false);
			friend.geo_enabled        = jsonObject.optBoolean("geo_enabled", false);
			friend.verified           = jsonObject.optBoolean("verified", false);
			friend.verified_type      = jsonObject.optInt("verified_type", -1);
			friend.remark             = jsonObject.optString("remark", "");
//			friend.status.id             = jsonObject.optJSONObject("status").optString("id", ""); // XXX: NO Need?
//			friend.status.text = jsonObject.optJSONObject("status").optString("text", "");
			friend.allow_all_comment  = jsonObject.optBoolean("allow_all_comment", true);
			friend.avatar_large       = jsonObject.optString("avatar_large", "");
			friend.avatar_hd          = jsonObject.optString("avatar_hd", "");
			friend.verified_reason    = jsonObject.optString("verified_reason", "");
			friend.follow_me          = jsonObject.optBoolean("follow_me", false);
			friend.online_status      = jsonObject.optInt("online_status", 0);
			friend.bi_followers_count = jsonObject.optInt("bi_followers_count", 0);
			friend.lang               = jsonObject.optString("lang", "");       
			friend.previous_cursor = jsonObject.optInt("previous_cursor");
			friend.next_cursor = jsonObject.optInt("next_cursor");
			friend.total_number  = jsonObject.optInt("total_number");
			app.getMfriendDataBase().addFriend(friend);	
		}
		return 1;
	}
}
