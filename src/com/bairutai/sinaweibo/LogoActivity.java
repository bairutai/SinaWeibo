package com.bairutai.sinaweibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;


public class LogoActivity extends Activity {
	private ImageView imageview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);		
		
		setContentView(R.layout.logo);
		imageview=(ImageView) this.findViewById(R.id.logo);
		AlphaAnimation animation=new AlphaAnimation(0.1f, 1.0f);
		animation.setDuration(4000);
		imageview.setAnimation(animation);
		
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub

				startActivity(new Intent(LogoActivity.this,MainActivity.class));
//				startActivity(new Intent(LogoActivity.this,AuthorizeActivity.class));
		    	finish();
		}
	});	
}
}
