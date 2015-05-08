package com.bairutai.sinaweibo;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
public class MyrefreshDialog extends Dialog {

	private Context context;
	private LayoutInflater mLayoutInflater;
	private View view;
	private ListView mListView;
	private List<String> list_str;
	private Button cancleBtn;
	private ClickListenerInterface clickListenerInterface;

	public interface ClickListenerInterface {
		public void dorefresh();
		public void doback();
		public void docancle();
		public void doyourthing();
	}

	public MyrefreshDialog(Context context,List<String> string) {
		super(context, R.style.MyDialog);
		// TODO Auto-generated constructor stub
		this.list_str = string;
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myfreshdialog);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		mListView = (ListView)findViewById(R.id.myrefreshdialog_list);
		mListView.setAdapter(new ArrayAdapter<String>(context, R.layout.list_item_ziti,list_str));
		mListView.setOnItemClickListener(new clickListener());
		cancleBtn = (Button)findViewById(R.id.myrefreshdialog_cancle);
		cancleBtn.setOnClickListener(new clickListener());
		DisplayMetrics dm = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = dm.widthPixels;
		if (list_str.size()<3){
			params.height = dm.heightPixels/5*1 ;
		}
		else{
			params.height = dm.heightPixels/4*1;
		}
		params.x = 0;
		params.y = dm.heightPixels-params.height;
		getWindow().setAttributes(params);
	}

	public void setClicklistener(ClickListenerInterface clickListenerInterface) {
		this.clickListenerInterface = clickListenerInterface;
	}

	private class clickListener implements OnItemClickListener,View.OnClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			switch (position) {
			case 0:
				clickListenerInterface.dorefresh();
				break;
			case 1:
				clickListenerInterface.doback();
			default:
				clickListenerInterface.doyourthing();
				break;
			}
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			clickListenerInterface.docancle();
		}

	};		
}
