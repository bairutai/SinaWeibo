package com.bairutai.sinaweibo;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.style.BulletSpan;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.mining.app.zxing.camera.CameraManager;
import com.mining.app.zxing.decoding.CaptureActivityHandler;
import com.mining.app.zxing.decoding.InactivityTimer;
import com.mining.app.zxing.decoding.RGBLuminanceSource;
import com.mining.app.zxing.view.ViewfinderView;
public class MipcaActivityCapture extends Activity implements Callback , View.OnClickListener{

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;


	private static final int REQUEST_CODE = 100;
	private static final int PARSE_BARCODE_SUC = 300;
	private static final int PARSE_BARCODE_FAIL = 303;
	private static final long VIBRATE_DURATION = 200L;
	private ProgressDialog mProgress;
	private String photo_path;
	private Bitmap scanBitmap;

	private View mView;
	private TextView closeBtn;
	private TextView pictureBtn;
	private Button mycardBtn;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);  
		setContentView(R.layout.mipcapture);
		ActionBar actionBar = getActionBar();
		if (null != actionBar) {
			actionBar.setDisplayShowHomeEnabled(false);//左上角logo
			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
			actionBar.setDisplayShowCustomEnabled(true);
			actionBar.setBackgroundDrawable(new ColorDrawable(R.color.transparent));
			LayoutInflater inflator = LayoutInflater.from(this);
			mView = inflator.inflate(R.layout.title_mipcapture, null);//自定义actionbar视图
			mView.getBackground().setAlpha(0);
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			actionBar.setCustomView(mView, layout);//设置actionbar视图
			closeBtn = (TextView)findViewById(R.id.title_mipcapture_close);
			pictureBtn = (TextView)findViewById(R.id.title_mipcapture_picture);
		}
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.mipcapture_viewfinderView);
		mycardBtn = (Button)findViewById(R.id.mipcapture_mycardBtn);
		closeBtn.setOnClickListener(this);
		pictureBtn.setOnClickListener(this);
		mycardBtn.setOnClickListener(this);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		mProgress = new ProgressDialog(this);
		initBeepSound();
		playBeep = true;
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.title_mipcapture_close:
			this.finish();
			break;
		case R.id.title_mipcapture_picture:
			//打开手机中的相册
			Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); //"android.intent.action.GET_CONTENT"
			innerIntent.setType("image/*");
			Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");
			this.startActivityForResult(wrapperIntent, REQUEST_CODE);
			break;
		case R.id.mipcapture_mycardBtn:
			this.startActivity(new Intent(MipcaActivityCapture.this, MycardActivity.class));
			break;
		default:
			break;
		}
	}


	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(mProgress.isShowing()){
				mProgress.dismiss();
			}
			switch (msg.what) {
			case PARSE_BARCODE_SUC:
				handleDecode((Result)msg.obj, scanBitmap);
//				onResultHandler((String)msg.obj, scanBitmap);
				break;
			case PARSE_BARCODE_FAIL:
				Toast.makeText(MipcaActivityCapture.this, (String)msg.obj, Toast.LENGTH_LONG).show();
				break;

			}
		}

	};


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			switch(requestCode){
			case REQUEST_CODE:
				//获取选中图片的路径
				Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
				if(null == cursor){
					Message m = mHandler.obtainMessage();
					m.what = PARSE_BARCODE_FAIL;
					m.obj = "图片路径出错";
					mHandler.sendMessage(m);
					break;
				}else {
					if (cursor.moveToFirst()) {
						photo_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
						cursor.close();
					}
				}

				mProgress.setMessage("正在扫描...");
				mProgress.setCancelable(false);
				mProgress.show();

				new Thread(new Runnable() {
					@Override
					public void run() {
						Result result = scanningImage(photo_path);
						if (result != null) {
							Message m = mHandler.obtainMessage();
							m.what = PARSE_BARCODE_SUC;
							m.obj = result;
							mHandler.sendMessage(m);
						} else {
							Message m = mHandler.obtainMessage();
							m.what = PARSE_BARCODE_FAIL;
							m.obj = "Scan failed!";
							mHandler.sendMessage(m);
						}
					}
				}).start();

				break;

			}
		}
	}

	/**
	 * 扫描二维码图片的方法
	 * @param path
	 * @return
	 */
	public Result scanningImage(String path) {
		if(TextUtils.isEmpty(path)){
			return null;
		}
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;// 先获取原大小
		scanBitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 获取新的大小
		int sampleSize = (int) (options.outHeight / (float) 200);
		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		scanBitmap = BitmapFactory.decodeFile(path, options);
		RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
			return reader.decode(bitmap1, hints);

		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (ChecksumException e) {
			e.printStackTrace();
		} catch (FormatException e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	protected void onResume() {
		System.out.println("onResume");
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.mipcapture_surfaceview);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

	}

	@Override
	protected void onPause() {
		System.out.println("onPause");
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * 处理扫描结果
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		onResultHandler(resultString, barcode);
	}

	/**
	 * 跳转到上一个页面
	 * @param resultString
	 * @param bitmap
	 */
	private void onResultHandler(String resultString, Bitmap bitmap){
		String  a = "sinaweibo://userinfo?uid=";		
		if(TextUtils.isEmpty(resultString)) {
			Toast.makeText(MipcaActivityCapture.this, "Scan failed!", Toast.LENGTH_SHORT).show();
			return;
		}else{
			String[] array = resultString.split("=");
			if(array[0].equalsIgnoreCase(a)){
				Intent intent = new Intent();       
				intent.setAction("android.intent.action.VIEW");   
				Uri content_url = Uri.parse("http://weibo.com/"+array[1]+"/profile");  
				intent.setData(content_url); 
				startActivity(intent);
			}else{
				Intent intent = new Intent(MipcaActivityCapture.this, InfoOfQRActivity.class);
				intent.putExtra("result",resultString);
				startActivity(intent);
				finish();
				System.out.println("goto next activity");
			}
		}
		//		Intent resultIntent = new Intent();
		//		Bundle bundle = new Bundle();
		//		bundle.putString("result", resultString);
		//		bundle.putParcelable("bitmap", bitmap);
		//		resultIntent.putExtras(bundle);
		//		this.setResult(RESULT_OK, resultIntent);
		//		MipcaActivityCapture.this.finish();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = MediaPlayer.create(MipcaActivityCapture.this, R.raw.beep);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);
			mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
			try {
				mediaPlayer.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//			AssetFileDescriptor file = getResources().openRawResourceFd(
			//					R.raw.beep);
			//			try {
			//				mediaPlayer.setDataSource(file.getFileDescriptor(),
			//						file.getStartOffset(), file.getLength());
			//				file.close();
			//				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
			//				mediaPlayer.prepareAsync();
			//			} catch (IOException e) {
			//				mediaPlayer = null;
			//			}
		}
	}

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};


}