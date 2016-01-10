package com.nowui.daning.category;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

public class BasePagerAdapter extends PagerAdapter {

    private List<View> viewList;

    public BasePagerAdapter(List<View> viewList) {
        this.viewList = viewList;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(View view, int position) {
        ((ViewPager) view).addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public void destroyItem(View view, int position, Object object) {
        ((ViewPager) view).removeView((View) viewList.get(position));
    }
}
