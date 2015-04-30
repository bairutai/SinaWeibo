package com.bairutai.sinaweibo;

import com.bairutai.application.WeiboApplication;
import com.bairutai.openwidget.PullScrollView;
import com.sina.weibo.sdk.openapi.models.User;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class MyHomePageActivity extends Activity implements PullScrollView.OnTurnListener{
	
    private PullScrollView mScrollView;
    private ImageView mHeadImg;
    private TextView mdetailTextView;
    private TextView m_flower_count;
    private TextView m_guanzhu_count;
    private WeiboApplication app;
    private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.my_homepage);
		app = (WeiboApplication)getApplication();
		user = app.getUser();
        initView();
	}
	
    protected void initView() {
        mScrollView = (PullScrollView) findViewById(R.id.scroll_view);
        mHeadImg = (ImageView) findViewById(R.id.background_img);
        mdetailTextView = (TextView)findViewById(R.id.expandable_text);
        m_flower_count = (TextView)findViewById(R.id.homepage_flower_count);
        m_guanzhu_count = (TextView)findViewById(R.id.homepage_guanzhu_count);
        mdetailTextView.setText(user.description);
        m_flower_count.setText(String.valueOf(user.followers_count));
        m_guanzhu_count.setText(String.valueOf(user.friends_count));
        mScrollView.setHeader(mHeadImg);
        mScrollView.setOnTurnListener(this);
    }

	@Override
	public void onTurn() {
		// TODO Auto-generated method stub
		
	}
}
