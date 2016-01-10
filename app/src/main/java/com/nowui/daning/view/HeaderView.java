package com.nowui.daning.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.nowui.daning.R;
import com.nowui.daning.utility.Helper;

import java.util.List;
import java.util.Map;

public class HeaderView extends RelativeLayout {

    private Context myContext;

    private OnClickHeaderLeftButtonListener onClickHeaderLeftButtonListener;

    public interface OnClickHeaderLeftButtonListener {
        public void OnClick(int position);
    }

    public void setOnClickHeaderLeftButtonListener(OnClickHeaderLeftButtonListener listener) {
        onClickHeaderLeftButtonListener = listener;
    }

    private OnClickHeaderRightButtonListener onClickHeaderRightButtonListener;

    public interface OnClickHeaderRightButtonListener {
        public void OnClick(int position);
    }

    public void setOnClickHeaderRightButtonListener(OnClickHeaderRightButtonListener listener) {
        onClickHeaderRightButtonListener = listener;
    }

    public HeaderView(Context context) {
        super(context);

        myContext = context;

        initView(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        myContext = context;

        initView(context);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        myContext = context;

        initView(context);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.view_header, this);
    }

    public void initItem(List<Map<String, Object>> itemList) {
        for(int i = 0; i < itemList.size(); i++) {
            Map<String, Object> map = itemList.get(i);

            HeaderItemView headerItemView = new HeaderItemView(myContext);
            headerItemView.setTag(i);

            if(i == 0) {
                headerItemView.setVisibility(VISIBLE);
            } else {
                headerItemView.setVisibility(INVISIBLE);
            }

            String titleString = getResources().getString(R.string.app_name);
            Map<String, Object> headerMap = (Map<String, Object>) map.get(Helper.KeyHeader);
            Map<String, Object> centerMap = (Map<String, Object>) headerMap.get(Helper.KeyCenter);
            if(Helper.isNullOrEmpty(centerMap.get(Helper.KeyType))) {
                titleString = centerMap.get(Helper.KeyData).toString();
                titleString = Helper.decode(titleString);
            }

            headerItemView.titleTextView.setText(titleString);

            final int position = i;

            headerItemView.leftButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClickHeaderLeftButtonListener != null) {
                        onClickHeaderLeftButtonListener.OnClick(position);
                    }
                }
            });

            RelativeLayout.LayoutParams headerItemViewLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            headerItemViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

            this.addView(headerItemView, headerItemViewLayoutParams);
        }
    }

    public void checkItem(int position) {
        for(int i = 0; i < this.getChildCount(); i++) {
            View view = this.getChildAt(i);

            if(view instanceof HeaderItemView) {
                if((int) view.getTag() == position) {
                    view.setVisibility(VISIBLE);
                } else {
                    view.setVisibility(INVISIBLE);
                }
            }
        }
    }

    public void initTitle(final int position, Map<String, Object> payloadMap) {
        for(int i = 0; i < this.getChildCount(); i++) {
            View view = this.getChildAt(i);

            if(view instanceof HeaderItemView) {
                if((int) view.getTag() == position) {
                    if (! Helper.isNullOrEmpty(payloadMap.get(Helper.KeyText))) {
                        String titleString = payloadMap.get(Helper.KeyText).toString();
                        titleString = Helper.decode(titleString);

                                ((HeaderItemView) view).titleTextView.setText(titleString);
                    }

                    if (! Helper.isNullOrEmpty(payloadMap.get(Helper.KeyRight))) {
                        ((HeaderItemView) view).rightButton.setVisibility(VISIBLE);
                        ((HeaderItemView) view).rightButton.setText(Helper.decode(payloadMap.get(Helper.KeyRight).toString()));
                        ((HeaderItemView) view).rightButton.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(onClickHeaderRightButtonListener != null) {
                                    onClickHeaderRightButtonListener.OnClick(position);
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    public void setLeftButtonVisibility(int visibility) {
        for(int i = 0; i < this.getChildCount(); i++) {
            View view = this.getChildAt(i);

            if(view instanceof HeaderItemView) {
                ((HeaderItemView) view).leftButton.setVisibility(visibility);
            }
        }
    }

}
