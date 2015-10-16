package com.bairutai.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibo.sdk.openapi.models.Tag;

public class Favorite {
	/** 我喜欢的微博信息 */
	public String status;
	/** 我喜欢的微博的 Tag 信息 */
	public ArrayList<Tag> tags;
	/** 创建我喜欢的微博信息的时间 */
	public String favorited_time;

	public static Favorite parse(String jsonString) {
		try {
			JSONObject object = new JSONObject(jsonString);
			return Favorite.parse(object);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Favorite parse(JSONObject jsonObject) {
		if (null == jsonObject) {
			return null;
		}

		Favorite favorite = new Favorite();
		favorite.status         = jsonObject.optString("status");
		favorite.favorited_time = jsonObject.optString("favorited_time");

		JSONArray jsonArray    = jsonObject.optJSONArray("tags");
		if (jsonArray != null && jsonArray.length() > 0) {
			int length = jsonArray.length();
			favorite.tags = new ArrayList<Tag>(length);
			for (int ix = 0; ix < length; ix++) {
				favorite.tags.add(Tag.parse(jsonArray.optJSONObject(ix)));
			}
		}

		return favorite;
	}
}
