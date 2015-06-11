package com.bairutai.sinaweibo;

import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.util.Hashtable;
import java.lang.Thread;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import android.graphics.Matrix;
import android.graphics.Rect;

import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;

import android.util.DisplayMetrics;
import android.util.Log;

import android.view.Gravity;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bairutai.application.WeiboApplication;
import com.bairutai.model.User;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class MycardActivity extends Activity {
	/** 生成二维码图片大小 */
	private  int QRCODE_SIZE;
	/** 头像图片大小 */
	private  int PORTRAIT_SIZE;
	/** 头像图片 */
	private Bitmap portrait;
	private URL url;
	private WeiboApplication app;
	private User muser;
	private TextView mname;
	private View view_share;
	private View view_this;
	private View mActionbarView;
	private View weixinshareView;
	private ImageView image2 ;
	private Handler mhandler;
	private Bitmap zxing;
	private ImageView backBtn;
	private ImageView moreBtn;
	private DisplayMetrics dm;
	private PopupWindow pop;
	private TextView titleTxt;
	private Button popwindow_cancel;
	private RelativeLayout mycard_layout;
	private ImageButton imgBtn_save;
	private LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycard);

		//自定义actionBar;
		ActionBar actionBar = getActionBar();
		if (null != actionBar) {
			actionBar.setDisplayShowHomeEnabled(false);//返回键
			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
			actionBar.setDisplayShowCustomEnabled(true);
			inflater = LayoutInflater.from(this);
			mActionbarView = inflater.inflate(R.layout.title_mycard, null);//加载actionbar视图
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			actionBar.setCustomView(mActionbarView, layout);//设置actionbar视图
			backBtn = (ImageView)findViewById(R.id.title_mycard_back);
			moreBtn = (ImageView)findViewById(R.id.title_mycard_more);
			titleTxt = (TextView)findViewById(R.id.title_mycard_mycard);
			titleTxt.setText("我的名片");
		}

		//初始化控件
		mycard_layout = (RelativeLayout)this.findViewById(R.id.mycard_layout);
		mname = (TextView)this.findViewById(R.id.mycard_name);
		image2 = (ImageView)this.findViewById(R.id.mycard_zxing);// 用于显示带头像的二维码的view

		//动态加载微信分享图标
		view_share = inflater.inflate(R.layout.mycard_popwindow, null);
		LinearLayout linearLayout_cardpopwindow_share =
				(LinearLayout) view_share.findViewById(R.id.mycard_popwindow_share);
		weixinshareView = inflater.inflate(R.layout.weixin_share, null);
		popwindow_cancel = (Button)view_share.findViewById(R.id.my_popwindow_cancel);
		imgBtn_save = (ImageButton)view_share.findViewById(R.id.my_popwindow_save);
		linearLayout_cardpopwindow_share.addView(weixinshareView);
		view_this = new View(this);
		mhandler = new Handler();

		//得到User中的头像地址
		app = (WeiboApplication) this.getApplication();
		muser = app.getUser();

		//设置控件的一些属性
		mname.setText(muser.screen_name);
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		QRCODE_SIZE = dm.widthPixels/4*3;//二维码宽度
		PORTRAIT_SIZE = QRCODE_SIZE/6;//二维码高度


		//新线程取到数据之后由主线程更新UI,主要是二维码的绘制
		final Runnable message = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				image2.setImageBitmap(zxing);		
			}
		};

		//新开线程去绘制二维码
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				InputStream input = null;
				try {
					url = new URL(muser.avatar_large);
					input = url.openStream();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 这里采用从asset中加载图片
				portrait = BitmapFactory.decodeStream(input);
				portrait = initProtrait(portrait);
				// 建立二维码
				final Bitmap qr = createQRCodeBitmap();
				createQRCodeBitmapWithPortrait(qr, portrait);
				zxing = qr;
				mhandler.post(message);
			}
		}.start();

		//添加各个监听器
		//actionbar more按钮监听器
		moreBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initPopWindow();
			}
		});

		//actionbar back按钮监听器
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		//popwindow cancel按钮监听器
		popwindow_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (pop.isShowing()) {
					pop.dismiss();
				}
			}
		});

		//layout点击监听器
		mycard_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initPopWindow();
			}
		});

		//popwindow 保存到相册按钮监听器
		imgBtn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (null != zxing && null != pop) {
					MediaStore.Images.Media.insertImage(getContentResolver(),
							zxing, null, null);
					pop.dismiss();
					Toast.makeText(MycardActivity.this, "图片已保存至手机相册", Toast.LENGTH_LONG).show();
					Log.d("loadimg", "load img success");
				}
				else {
					Log.d("loadimg", "load img error");
				}
			}
		});

	}

	private void initPopWindow() 
	{
		pop =new PopupWindow(
				LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		pop.setContentView(view_share);//设置pop视图
		pop.setAnimationStyle(R.style.popupAnimation);//设置pop弹出动画
		pop.setFocusable(true);//必须设置该属性，不然监听不到手机返回按钮点击事件
		pop.showAtLocation(view_this, Gravity.TOP, 0, 0);
		pop.update();
		view_share.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				//这里我用的是Pop 新浪用的是dialog，两者我看到的区别是pop不会把状态栏灰掉
				//而dialog会把状态栏灰掉，而且dialog自带点击除dialog以外的地方隐藏dialog属性
				int height =((RelativeLayout)view_share.findViewById(
						R.id.mycard_popwindow_linearLayout)).getTop();						
				int y=(int) event.getY();
				if (MotionEvent.ACTION_UP==event.getAction()
						&&pop!=null&&pop.isShowing()) {
					if(y<height){
						pop.dismiss();
					}         
				}
				return false;
			}
		});
	}


	/**
	 * 初始化头像图片
	 */
	private Bitmap initProtrait(Bitmap portrait) {
		// 对原有图片压缩显示大小
		Matrix mMatrix = new Matrix();
		float width = portrait.getWidth();
		float height = portrait.getHeight();
		mMatrix.setScale(PORTRAIT_SIZE / width, PORTRAIT_SIZE / height);
		return Bitmap.createBitmap(portrait, 0, 0, (int) width,
				(int) height, mMatrix, true);
	}

	/**
	 * 创建QR二维码图片
	 */
	private Bitmap createQRCodeBitmap() {
		// 用于设置QR二维码参数
		Hashtable<EncodeHintType, Object> qrParam = new Hashtable<EncodeHintType, Object>();
		// 设置QR二维码的纠错级别——这里选择最高H级别
		qrParam.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		// 设置编码方式
		qrParam.put(EncodeHintType.CHARACTER_SET, "UTF-8");

		// 设定二维码里面的内容，这里我采用我微博的地址
		String content = "sinaweibo://userinfo?uid=";
		content += muser.id;

		// 生成QR二维码数据——这里只是得到一个由true和false组成的数组
		// 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
					BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, qrParam);

			// 开始利用二维码数据创建Bitmap图片，分别设为黑白两色
			int w = bitMatrix.getWidth();
			int h = bitMatrix.getHeight();
			int[] data = new int[w * h];

			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					if (bitMatrix.get(x, y))
						data[y * w + x] = 0xff000000;// 黑色
					else
						data[y * w + x] = -1;// -1 相当于0xffffffff 白色
				}
			}

			// 创建一张bitmap图片，采用最高的图片效果ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			// 将上面的二维码颜色数组传入，生成图片颜色
			bitmap.setPixels(data, 0, w, 0, 0, w, h);
			return bitmap;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 在二维码上绘制头像
	 */
	private void createQRCodeBitmapWithPortrait(Bitmap qr, Bitmap portrait) {
		// 头像图片的大小
		int portrait_W = portrait.getWidth();
		int portrait_H = portrait.getHeight();

		// 设置头像要显示的位置，即居中显示
		int left = (QRCODE_SIZE - portrait_W) / 2;
		int top = (QRCODE_SIZE - portrait_H) / 2;
		int right = left + portrait_W;
		int bottom = top + portrait_H;
		Rect rect1 = new Rect(left, top, right, bottom);

		// 取得qr二维码图片上的画笔，即要在二维码图片上绘制我们的头像
		Canvas canvas = new Canvas(qr);

		// 设置我们要绘制的范围大小，也就是头像的大小范围
		Rect rect2 = new Rect(0, 0, portrait_W, portrait_H);
		// 开始绘制
		canvas.drawBitmap(portrait, rect2, rect1, null);
	}
}