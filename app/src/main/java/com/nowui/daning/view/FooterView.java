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

public class FooterView extends RelativeLayout {

    private Context myContext;
    private int selectInt = -1;

    private OnClickFooterListener onClickFooterListener;

    public interface OnClickFooterListener {
        public void OnClick(int position);
    }

    public void setOnClickFooterListener(OnClickFooterListener listener) {
        onClickFooterListener = listener;
    }

    public FooterView(Context context) {
        super(context);

        myContext = context;

        initView(context);
    }

    public FooterView(Context context, AttributeSet attrs) {
        super(context, attrs);

        myContext = context;

        initView(context);
    }

    public FooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        myContext = context;

        initView(context);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.view_footer, this);
    }

    public void initItem(List<Map<String, Object>> itemList) {
        int width = Helper.getScreenWidth(myContext) / itemList.size();

        for(int i = 0; i < itemList.size(); i++) {
            Map<String, Object> map = itemList.get(i);

            FooterItemView footerItemView = new FooterItemView(myContext);
            footerItemView.setTag(i);
            footerItemView.setOnClickFooterItemListener(new FooterItemView.OnClickFooterItemListener() {
                @Override
                public void OnClick(int position) {
                    checkItem(position);
                }
            });

            if(i == 0) {
                footerItemView.didActive();
            }

            Typeface font = Typeface.createFromAsset(myContext.getAssets(), getResources().getString(R.string.icon_font));
            footerItemView.iconTextView.setTypeface(font);
            footerItemView.iconTextView.setText(Helper.decode(map.get(Helper.KeyIcon).toString()));

            footerItemView.textView.setText(Helper.decode((map.get(Helper.KeyText).toString())));

            RelativeLayout.LayoutParams footerItemViewLayoutParams = new RelativeLayout.LayoutParams(width, RelativeLayout.LayoutParams.WRAP_CONTENT);
            footerItemViewLayoutParams.leftMargin = width * i;

            this.addView(footerItemView, footerItemViewLayoutParams);
        }
    }

    public void checkItem(int position) {
        if(selectInt == position) {
            return;
        }

        selectInt = position;

        for(int i = 0; i < this.getChildCount(); i++) {
            View view = this.getChildAt(i);

            if(view instanceof FooterItemView) {
                if((int) view.getTag() == selectInt) {
                    ((FooterItemView) view).didActive();
                } else {
                    ((FooterItemView) view).didNormal();
                }
            }
        }

        if(onClickFooterListener != null) {
            onClickFooterListener.OnClick(position);
        }
    }

}
