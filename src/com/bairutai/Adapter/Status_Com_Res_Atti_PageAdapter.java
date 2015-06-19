package com.bairutai.Adapter;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class Status_Com_Res_Atti_PageAdapter extends PagerAdapter {
	public  List<View> mListViews;
	
	public Status_Com_Res_Atti_PageAdapter(List<View> listViews) {
		// TODO Auto-generated constructor stub
		this.mListViews = listViews;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListViews.size();
	}

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView(mListViews.get(arg1));
    }

    @Override
    public void finishUpdate(View arg0) {
       
    }
    
    @Override
    public Object instantiateItem(View arg0, int arg1) {
        ((ViewPager) arg0).addView(mListViews.get(arg1), 0);
        return mListViews.get(arg1);
    }

    
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == (arg1);
	}
	
    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {
    }
}
