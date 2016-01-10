package com.nowui.daning.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nowui.daning.R;

public class HeaderItemView extends RelativeLayout {

    public Button leftButton;
    public TextView titleTextView;
    public Button rightButton;

    public HeaderItemView(Context context) {
        super(context);

        initView(context);
    }

    public HeaderItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView(context);
    }

    public HeaderItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.view_header_item, this);

        leftButton = (Button) findViewById(R.id.leftButton);
        Typeface font = Typeface.createFromAsset(context.getAssets(), getResources().getString(R.string.icon_font));
        leftButton.setTypeface(font);

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        rightButton = (Button) findViewById(R.id.rightButton);
    }

}
