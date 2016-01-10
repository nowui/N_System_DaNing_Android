package com.nowui.daning.activity;

import android.content.Intent;
import android.os.Bundle;
import com.nowui.daning.utility.Helper;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        String appUserId = setting.getString(Helper.KeyAppUserId, "");
        if(Helper.isNullOrEmpty(appUserId)) {
            String initDataString = "{\"type\": \"OnePage\", \"data\": {\"url\": \"https://m.baidu.com/?from=844b&vit=fps\", \"header\": {\"center\": {\"data\": \"\"} } } }";
            Intent intent = new Intent();
            intent.putExtra(Helper.KeyInitData, initDataString);
            intent.putExtra(Helper.KeyIsRoot, true);
            intent.putExtra(Helper.KeyIsSplash, false);
            intent.putExtra(Helper.KeyIsDoubleClickBack, true);
            intent.putExtra(Helper.KeyIsFinish, true);
            intent.setClass(MainActivity.this, BrowerActivity.class);
            startActivityForResult(intent, Helper.CodeRequest);
        } else {
            String initDataString = "{'type': 'MultiFooter', 'data': {'footer': [{'header': {'center': {'data': '上海市闸北区大宁国际学校'} }, 'text': '首页', 'icon': '\\ue88a', 'url': '/index.html'}, {'header': {'center': {'data': '应用中心'} }, 'text': '应用', 'icon': '\\ue896', 'url': '/application.html'}, {'header': {'center': {'data': '通讯录'} }, 'text': '通讯录', 'icon': '\\ue551', 'url': '/contact.html'}, {'header': {'center': {'data': '我'} }, 'text': '我', 'icon': '\\ue7fd', 'url': '/mine.html'}] } }";

            Intent intent = new Intent();
            intent.putExtra(Helper.KeyInitData, initDataString);
            intent.putExtra(Helper.KeyIsRoot, true);
            intent.putExtra(Helper.KeyIsSplash, true);
            intent.putExtra(Helper.KeyIsDoubleClickBack, true);
            intent.setClass(MainActivity.this, BrowerActivity.class);
            startActivityForResult(intent, Helper.CodeRequest);
        }

        finish();
    }

}
