package com.bairutai.sinaweibo;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.lang.Thread;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bairutai.application.WeiboApplication;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sina.weibo.sdk.openapi.models.User;

public class MycardActivity extends Activity {
	/** 生成二维码图片大小 */
	private static final int QRCODE_SIZE = 300;
	/** 头像图片大小 */
	private static final int PORTRAIT_SIZE = 50;
	/** 头像图片 */
	private Bitmap portrait;
	private URL url;
	private WeiboApplication app;
	private User muser;
	private TextView mname;
	private TextView menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		setContentView(R.layout.mycard);
		app = (WeiboApplication) this.getApplication();
		muser = app.getUser();
		// 用于显示正常二维码的view
//		ImageView image1 = (ImageView) findViewById(R.id.image1);
//		image1.setImageBitmap(createQRCodeBitmap());
		mname = (TextView)findViewById(R.id.mycard_name);
		mname.setText(muser.screen_name);
		menu =(TextView)findViewById(R.id.head_more);

		try {
			url = new URL(muser.profile_image_url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 用于显示带头像的二维码的view
		ImageView image2 = (ImageView) findViewById(R.id.image2);
		 new Thread() {
			 @Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				InputStream input = null;
				try {
					input = url.openStream();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					// 这里采用从asset中加载图片
				portrait = BitmapFactory.decodeStream(input);
				portrait = initProtrait(portrait);
			}
		 }.start();
		// 初始化头像

		// 建立二维码
		final Bitmap qr = createQRCodeBitmap();
		createQRCodeBitmapWithPortrait(qr, portrait);
		image2.setImageBitmap(qr);		
		menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ( qr != null) {
					try {
						ContentResolver cr = getBaseContext().getContentResolver();
						String url = MediaStore.Images.Media.insertImage(cr, qr,"aaa", "");
						Toast.makeText(getBaseContext(), "保存成功", Toast.LENGTH_SHORT).show();
					}catch (Exception e){
						e.printStackTrace();
					}
				}
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