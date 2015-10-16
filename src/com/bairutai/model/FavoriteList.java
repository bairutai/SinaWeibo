package com.bairutai.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class FavoriteList {
	  /** 微博列表 */
    public ArrayList<Favorite> favoriteList;
    public int total_number;
    
    public static FavoriteList parse(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        
        FavoriteList favorites = new FavoriteList();
        try {
            JSONObject jsonObject  = new JSONObject(jsonString);
            favorites.total_number = jsonObject.optInt("total_number", 0);
            
            JSONArray jsonArray    = jsonObject.optJSONArray("favorites");
            if (jsonArray != null && jsonArray.length() > 0) {
                int length = jsonArray.length();
                favorites.favoriteList = new ArrayList<Favorite>(length);
                for (int ix = 0; ix < length; ix++) {
                    favorites.favoriteList.add(Favorite.parse(jsonArray.optJSONObject(ix)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return favorites;
    }
}
