package com.bairutai.tools;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class AsyncBitmapLoader extends AsyncTask<Object, Integer, Bitmap> {
	private static Map<String,SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
	ImageView image = null;
	private ProgressBar progressbar;
	
	public void setProgressBar(ProgressBar progressbar){
		this.progressbar = progressbar;
	}
	@Override
	protected Bitmap doInBackground(Object... params) {
		// TODO Auto-generated method stub
		image = (ImageView) params[0];
		String imageUrl = ((String) params[1]).trim();
		Bitmap bitmap = null;
		if(imageCache.containsKey(imageUrl)){
			SoftReference<Bitmap> softReference = imageCache.get(imageUrl);
			if(softReference.get()!=null){
				bitmap = softReference.get();
			} else {
				bitmap = this.createFromStream(imageUrl);
				imageCache.put(imageUrl, new SoftReference<Bitmap>(bitmap));
			}
		} else {
			bitmap = this.createFromStream(imageUrl);
			imageCache.put(imageUrl, new SoftReference<Bitmap>(bitmap));
		}
		return bitmap;
		//return this.getRoundedCornerBitmap(bitmap, 20);
	}
	
	
	private Bitmap createFromStream(String imageURL){
		InputStream is;
		try {
			URL url = new URL(imageURL);
			HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			is = conn.getInputStream();
			Bitmap bitmap = null;
			bitmap= CompressImage.comp(BitmapFactory.decodeStream(is));

			return bitmap;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static Bitmap createFromSdcard(String imageURL,int width,int height){
		InputStream is;
		try {
			File file=new File(imageURL);
			is=new FileInputStream(file);
			Bitmap bitmap = null;
			int length = (int) file.length();
			if (length != -1){
				byte[] imgData = new byte[length];
				byte[] temp=new byte[512];
				int readLen=0;
				int destPos=0;
				while((readLen=is.read(temp))>0){
					System.arraycopy(temp, 0, imgData, destPos, readLen);
					destPos+=readLen;
				}
				BitmapFactory.Options options=new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				bitmap=BitmapFactory.decodeByteArray(imgData, 0, imgData.length, options);
				options.inJustDecodeBounds = false;
				int be = (int)(options.outHeight / (float)width); 
		        if (be <= 0)   be = 1;
		        options.inSampleSize = be;
		        bitmap=BitmapFactory.decodeByteArray(imgData, 0, imgData.length,options);
		        bitmap=Bitmap.createScaledBitmap(bitmap,width,height,true);
			}
			return bitmap;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		try{
			image.setImageBitmap(result);
			progressbar.setVisibility(View.GONE);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		progressbar.setProgress(values[0]);
	}
}