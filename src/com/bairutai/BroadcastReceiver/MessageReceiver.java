package com.bairutai.BroadcastReceiver;

import com.bairutai.sinaweibo.ChatActivity;
import com.igexin.sdk.PushConsts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

public class MessageReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Bundle bundle = intent.getExtras();
		if(bundle.getInt(PushConsts.CMD_ACTION) ==PushConsts.GET_MSG_DATA){
			byte[] payload = bundle.getByteArray("payload");
            if (payload != null) {
                String data = new String(payload);
                System.out.println(data);
                Message message = ChatActivity.handler.obtainMessage();
                message.obj = data;
                message.what = 1;
                ChatActivity.handler.sendMessage(message);
            }
		}
	}

}
