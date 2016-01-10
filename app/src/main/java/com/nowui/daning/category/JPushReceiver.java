package com.nowui.daning.category;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.nowui.daning.activity.BrowerActivity;
import com.nowui.daning.utility.Helper;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 *
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "[JPushReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

		Intent broadcastReceiverIntent = new Intent();
		broadcastReceiverIntent.setAction(Helper.KeyPushFilter);
		context.getApplicationContext().sendBroadcast(broadcastReceiverIntent);

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[JPushReceiver] 接收Registration Id : " + regId);

			SharedPreferences setting = context.getSharedPreferences(Helper.KeyAppSetting, Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = setting.edit();
			editor.putString(Helper.KeyJpushRegistrationId, regId);
			editor.commit();
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "[JPushReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
			processCustomMessage(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "[JPushReceiver] 接收到推送下来的通知");
			int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[JPushReceiver] 接收到推送下来的通知的ID: " + notifactionId);

			Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(3000);
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			Log.d(TAG, "[JPushReceiver] 用户点击打开了通知");

			for (String key : bundle.keySet()) {
				if (key.equals("cn.jpush.android.EXTRA")) {
					Map<String, Object> map = JSON.parseObject(bundle.getString(key));

					String url = "";

					if (map.get(Helper.KeyType).equals("NOTICE")) {
						url = "/notice/detail.html?id=" + map.get(Helper.KeyId).toString();
					} else if (map.get(Helper.KeyType).equals("MEETING")) {
						url = "/meeting/detail.html?id=" + map.get(Helper.KeyId).toString();
					}

					if (! Helper.isNullOrEmpty(url)) {
						String initDataString = "{\"type\": \"OnePage\", \"data\": {\"url\": \"" + url + "\", \"header\": {\"center\": {\"data\": \"\"} } } }";

						Intent mainIntent = new Intent();
						mainIntent.putExtra(Helper.KeyInitData, initDataString);
						mainIntent.setClass(context.getApplicationContext(), BrowerActivity.class);
						mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

						context.getApplicationContext().startActivity(mainIntent);

						break;
					}
				}
			}
		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
			Log.d(TAG, "[JPushReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
			//在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

		} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
			boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[JPushReceiver]" + intent.getAction() +" connected state change to "+connected);
		} else {
			Log.d(TAG, "[JPushReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			}
			else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
        /*if (MainActivity.isForeground) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
            if (!ExampleUtil.isEmpty(extras)) {
                try {
                    JSONObject extraJson = new JSONObject(extras);
                    if (null != extraJson && extraJson.length() > 0) {
                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
                    }
                } catch (JSONException e) {

                }

            }
            context.sendBroadcast(msgIntent);
        }*/
	}
}
