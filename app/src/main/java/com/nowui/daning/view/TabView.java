package com.nowui.daning.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nowui.daning.R;
import com.nowui.daning.utility.Helper;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

public class TabView extends RelativeLayout {

    private Context myContext;
    private int selectInt = -1;

    private OnClickTabListener onClickTabListener;

    public interface OnClickTabListener {
        public void OnClick(int position);
    }

    public void setOnClickTabListener(OnClickTabListener listener) {
        onClickTabListener = listener;
    }

    public TabView(Context context) {
        super(context);

        myContext = context;

        initView(context);
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);

        myContext = context;

        initView(context);
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        myContext = context;

        initView(context);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.view_tab, this);
    }

    public void initItem(List<Map<String, Object>> itemList) {
        int tabWidth = Helper.formatPix(myContext, Helper.TabWidth * 2);
        int width = tabWidth / itemList.size();

        for(int i = 0; i < itemList.size(); i++) {
            Map<String, Object> map = itemList.get(i);

            TabItemView tabItemView = new TabItemView(myContext, itemList.size(), i);
            tabItemView.setTag(i);
            tabItemView.setOnClickTabItemListener(new TabItemView.OnClickTabItemListener() {
                @Override
                public void OnClick(int position) {
                    checkItem(position);
                }
            });

            if(i == 0) {
                tabItemView.didActive();
            }

            tabItemView.textView.setText(Helper.decode((map.get(Helper.KeyText).toString())));

            RelativeLayout.LayoutParams tabItemViewLayoutParams = new RelativeLayout.LayoutParams(width, RelativeLayout.LayoutParams.WRAP_CONTENT);
            tabItemViewLayoutParams.leftMargin = (Helper.getScreenWidth(myContext) - tabWidth) / 2 + width * i;
            tabItemViewLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

            this.addView(tabItemView, tabItemViewLayoutParams);
        }
    }

    public void checkItem(int position) {
        if(selectInt == position) {
            return;
        }

        selectInt = position;

        for(int i = 0; i < this.getChildCount(); i++) {
            View view = this.getChildAt(i);

            if(view instanceof TabItemView) {
                if((int) view.getTag() == selectInt) {
                    ((TabItemView) view).didActive();
                } else {
                    ((TabItemView) view).didNormal();
                }
            }
        }

        if(onClickTabListener != null) {
            onClickTabListener.OnClick(position);
        }
    }

}
