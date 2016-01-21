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

    private RelativeLayout mainRelativeLayout;
    private Button button;
    public TextView textView;
    private int total;
    private int position;

    private OnClickTabItemListener onClickTabItemListener;

    public interface OnClickTabItemListener {
        public void OnClick(int position);
    }

    public void setOnClickTabItemListener(OnClickTabItemListener listener) {
        onClickTabItemListener = listener;
    }

    public TabItemView(Context context, int total, int position) {
        super(context);

        this.total = total;
        this.position = position;

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

        mainRelativeLayout = (RelativeLayout) findViewById(R.id.mainRelativeLayout);

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

        didNormal();
    }

    public void didNormal() {
        if (position == 0) {
            mainRelativeLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_tab_item_left));
        } else if (position + 1 == total) {
            mainRelativeLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_tab_item_right));
        } else {
            //mainRelativeLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_tab_item_center));
        }

        textView.setTextColor(getResources().getColor(R.color.tab_text_unselect_color));
    }

    public void didActive() {
        if (position == 0) {
            mainRelativeLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_tab_item_left_active));
        } else if (position + 1 == total) {
            mainRelativeLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_tab_item_right_active));
        } else {
            //mainRelativeLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_tab_item_center));
        }

        textView.setTextColor(getResources().getColor(R.color.tab_text_select_color));
    }

}
