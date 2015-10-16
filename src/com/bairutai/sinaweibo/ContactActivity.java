package com.bairutai.sinaweibo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bairutai.Adapter.ContactAdapter;
import com.bairutai.model.ContactBean;
import com.bairutai.openwidget.BladeView;
import com.bairutai.openwidget.BladeView.OnItemClickListener;
import com.bairutai.openwidget.PinnedHeaderListView;
import com.bairutai.tools.StringHelper;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;

public class ContactActivity extends Activity {
	private static final String FORMAT = "^[a-z,A-Z].*$"; 
    // 根据首字母存放数据  
	private Map<String, List<String>> mMap = new HashMap<String, List<String>>();
   // 首字母对应的位置  
   	private Map<String, Integer> mIndexer = new HashMap<String, Integer>();; 
   	// 首字母位置集  
  	private List<Integer> mPositions = new ArrayList<Integer>();  
  	// 首字母集  
	private List<String> mSections = new ArrayList<String>();
	
	private PinnedHeaderListView mPinnedHeaderListView;
	private BladeView mBladeView;
	private AsyncQueryHandler asyncQueryHandler;
	private List<ContactBean> list;
	private Map<Integer, ContactBean> contactIdMap = null;
	private ContactAdapter mAdapter; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactactivity);
		asyncQueryHandler = new MyAsyncQueryHandler(getContentResolver());
		findView();
		init();
	}
	
	private void findView()
	{
		mPinnedHeaderListView = (PinnedHeaderListView) findViewById(R.id.contactactivity_listview);
		mBladeView = (BladeView)findViewById(R.id.contactactivity_bladeview);
	}
	
    /** 
     * 初始化数据库查询参数 
     */  
    private void init() {  
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人Uri；  
        // 查询的字段  
        String[] projection = { ContactsContract.CommonDataKinds.Phone._ID,  
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,  
                ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",  
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,  
                ContactsContract.CommonDataKinds.Phone.PHOTO_ID,  
                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY };  
        // 按照sort_key升序查詢  
        asyncQueryHandler.startQuery(0, null, uri, projection, null, null,  
                "sort_key COLLATE LOCALIZED asc");  
  
    } 
    
	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
			// TODO Auto-generated constructor stub
		}
		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			// TODO Auto-generated method stub
			if (cursor != null && cursor.getCount() > 0) {  
				contactIdMap = new HashMap<Integer, ContactBean>(); 
                list = new ArrayList<ContactBean>();  
                cursor.moveToFirst(); // 游标移动到第一项  
                for (int i = 0; i < cursor.getCount(); i++) {  
                    cursor.moveToPosition(i);  
                    String name = cursor.getString(1);  
                    String number = cursor.getString(2);  
                    String sortKey = cursor.getString(3);  
                    int contactId = cursor.getInt(4);  
                    Long photoId = cursor.getLong(5);  
                    String lookUpKey = cursor.getString(6);  
                    // 根据contactid筛选重复的联系人
                    if (contactIdMap.containsKey(contactId)) {  
                        // 无操作  
                    } else {  
                        // 创建联系人对象  
                        ContactBean contact = new ContactBean();  
                        contact.setDesplayName(name);  
                        contact.setPhoneNum(number);  
                        contact.setSortKey(sortKey);  
                        contact.setPhotoId(photoId);  
                        contact.setLookUpKey(lookUpKey);  
                        // 中文转拼音
                        contact.setPinyin(StringHelper.getPingYin(name));
                        list.add(contact);  
                        contactIdMap.put(contactId, contact);  
                    }  
                }  
                if (list.size() > 0) {  
                	initData();
                }  
            }  
			super.onQueryComplete(token, cookie, cursor);
		}
	}
	
	private void initData(){
        for (int i = 0; i < list.size(); i++) {  
        	// 取首字母
            String firstName = list.get(i).getPinyin().substring(0, 1);  
            // 如果是A-Z
            if (firstName.matches(FORMAT)) {  
            	// 通过首字母筛选数据，
                if (mSections.contains(firstName.toUpperCase())) {  
                	// 把数据按首字母分类，如果存在，list数据加一条
                    mMap.get(firstName.toUpperCase()).add( list.get(i).getPinyin());  
                } else {  
                	// 如果首字母不存在，则新建索引，并增加一条数据
                    mSections.add(firstName.toUpperCase());  
                    List<String> list_1 = new ArrayList<String>();  
                    list_1.add(list.get(i).getPinyin());  
                    mMap.put(firstName.toUpperCase(), list_1);  
                }  
            } 
            // 如果不是A-Z,全部归为#
            else {  
            	// 如果索引存在# 则直接加一条数据
                if (mSections.contains("#")) {  
                    mMap.get("#").add(list.get(i).getPinyin());  
                } 
                // 如果不存在，则新建索引，并增加一条数据
                else {  
                    mSections.add("#");  
                    List<String> list_2 = new ArrayList<String>();  
                    list_2.add(list.get(i).getPinyin());  
                    mMap.put("#", list_2);  
                }  
            }  
        }  
        // 排序
        Collections.sort(mSections);  
        int position = 0;  
        for (int i = 0; i < mSections.size(); i++) {  
        	// 存入map中，key为首字母字符串，value为首字母在listview中位置  
            mIndexer.put(mSections.get(i), position);
            // 首字母在listview中位置，存入list中
            mPositions.add(position);  
            // 计算下一个首字母在listview的位置  
            position += mMap.get(mSections.get(i)).size();
        } 
        
    	mBladeView.setOnItemClickListener(new OnItemClickListener() {  
  		  
            @Override  
            public void onItemClick(String s) {  
                if (mIndexer.get(s) != null) {  
                    mPinnedHeaderListView.setSelection(mIndexer.get(s));  
                }  
            }  
        });  
    	
		mAdapter = new ContactAdapter(this, list, mSections, mPositions);
		mPinnedHeaderListView.setAdapter(mAdapter);
		mPinnedHeaderListView.setOnScrollListener(mAdapter);
		mPinnedHeaderListView.setPinnedHeaderView(LayoutInflater.from(this).inflate(
				R.layout.listview_head, mPinnedHeaderListView, false));
	}
}
