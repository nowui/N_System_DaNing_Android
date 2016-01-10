package com.nowui.daning.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nowui.daning.R;

public class FooterItemView extends RelativeLayout {

    private Button button;
    public TextView iconTextView;
    public TextView textView;

    private OnClickFooterItemListener onClickFooterItemListener;

    public interface OnClickFooterItemListener {
        public void OnClick(int position);
    }

    public void setOnClickFooterItemListener(OnClickFooterItemListener listener) {
        onClickFooterItemListener = listener;
    }

    public FooterItemView(Context context) {
        super(context);

        initView(context);
    }

    public FooterItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView(context);
    }

    public FooterItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.view_footer_item, this);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickFooterItemListener != null) {
                    onClickFooterItemListener.OnClick((int) getTag());
                }
            }
        });

        iconTextView = (TextView) findViewById(R.id.iconTextView);
        textView = (TextView) findViewById(R.id.textView);
    }

    public void didNormal() {
        iconTextView.setTextColor(getResources().getColor(R.color.footer_text_unselect_color));
        textView.setTextColor(getResources().getColor(R.color.footer_text_unselect_color));
    }

    public void didActive() {
        iconTextView.setTextColor(getResources().getColor(R.color.footer_text_select_color));
        textView.setTextColor(getResources().getColor(R.color.footer_text_select_color));
    }

}
