package com.nowui.daning.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.nowui.daning.R;
import com.nowui.daning.utility.Helper;
import com.nowui.daning.view.HeaderView;

import org.w3c.dom.Text;

public class BaseActivity extends Activity {

    public int screenWidth;
    public int screenHeight;

    public SharedPreferences setting;
    public SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base);

        screenWidth = Helper.getScreenWidth(this);
        screenHeight = Helper.getScreenHeight(this);

        setting = getSharedPreferences(Helper.KeyAppSetting, Activity.MODE_PRIVATE);

        editor = setting.edit();
    }

}
