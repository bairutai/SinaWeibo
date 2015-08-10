package com.bairutai.model;


public class MessageItem {
	// Text
	public static final int MESSAGE_TYPE_TEXT = 1;
	// image
	public static final int MESSAGE_TYPE_IMG = 2;
	// file
	public static final int MESSAGE_TYPE_FILE = 3;

	private int msgType;
	private String time;// 消息日期
	private String message;// 消息内容
	private boolean isComMeg = true;// 是否为收到的消息

	private int isNew;

	public MessageItem() {
		// TODO Auto-generated constructor stub
	}

	public MessageItem(int msgType,  String date, String message,
			 boolean isComMeg, int isNew) {
		super();
		this.msgType = msgType;
		this.time = date;
		this.message = message;
		this.isComMeg = isComMeg;
		this.isNew = isNew;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public String getDate() {
		return time;
	}

	public void setDate(String date) {
		this.time = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isComMeg() {
		return isComMeg;
	}

	public void setComMeg(boolean isComMeg) {
		this.isComMeg = isComMeg;
	}

	public static int getMessageTypeText() {
		return MESSAGE_TYPE_TEXT;
	}

	public static int getMessageTypeImg() {
		return MESSAGE_TYPE_IMG;
	}

	public static int getMessageTypeFile() {
		return MESSAGE_TYPE_FILE;
	}

	public int getIsNew() {
		return isNew;
	}

	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}

}
