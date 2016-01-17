package com.nowui.daning.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nowui.daning.R;

public class TabItemView extends RelativeLayout {

    private Button button;
    public TextView textView;

    private OnClickTabItemListener onClickTabItemListener;

    public interface OnClickTabItemListener {
        public void OnClick(int position);
    }

    public void setOnClickTabItemListener(OnClickTabItemListener listener) {
        onClickTabItemListener = listener;
    }

    public TabItemView(Context context) {
        super(context);

        initView(context);
    }

    public TabItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView(context);
    }

    public TabItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.view_tab_item, this);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickTabItemListener != null) {
                    onClickTabItemListener.OnClick((int) getTag());
                }
            }
        });

        textView = (TextView) findViewById(R.id.textView);
    }

    public void didNormal() {
        textView.setTextColor(getResources().getColor(R.color.footer_text_unselect_color));
    }

    public void didActive() {
        textView.setTextColor(getResources().getColor(R.color.footer_text_select_color));
    }

}
