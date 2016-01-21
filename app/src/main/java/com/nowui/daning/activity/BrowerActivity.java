package com.nowui.daning.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.nowui.daning.R;
import com.nowui.daning.category.BaseViewPager;
import com.nowui.daning.model.PageType;
import com.nowui.daning.category.BasePagerAdapter;
import com.nowui.daning.utility.Helper;
import com.nowui.daning.view.BrowerView;
import com.nowui.daning.view.FooterView;
import com.nowui.daning.view.HeaderView;
import com.nowui.daning.view.TabView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BrowerActivity extends BaseActivity {

    private BaseBroadcastReceiver baseBroadcastReceiver;
    private long exitTime;
    private boolean isRoot;
    private boolean isSplash;
    private boolean isFinish;
    private boolean isDoubleClickBack;
    private String pageTypeString;
    private RelativeLayout mainRelativeLayout;
    private HeaderView headerView;
    private BaseViewPager viewPager;
    private TabView tabView;
    private FooterView footerView;
    private List<View> browerViewList = new ArrayList<View>();
    private List<Boolean> browerViewIsLoadList = new ArrayList<Boolean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        baseBroadcastReceiver = new BaseBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Helper.KeyPushFilter);
        registerReceiver(baseBroadcastReceiver, filter);

        mainRelativeLayout = (RelativeLayout) findViewById(R.id.mainRelativeLayout);

        isRoot = getIntent().getBooleanExtra(Helper.KeyIsRoot, false);
        isSplash = getIntent().getBooleanExtra(Helper.KeyIsSplash, false);
        isFinish = getIntent().getBooleanExtra(Helper.KeyIsFinish, false);
        isDoubleClickBack = getIntent().getBooleanExtra(Helper.KeyIsDoubleClickBack, false);

        initView();

        initSplashView();
    }

    private void initView() {
        String initDataString = getIntent().getStringExtra(Helper.KeyInitData);

        if(! Helper.isNullOrEmpty(initDataString)) {
            Map<String, Object> initDataMap = JSON.parseObject(initDataString);

            pageTypeString = initDataMap.get(Helper.KeyType).toString();

            Map<String, Object> initDataDataMap = (Map<String, Object>) initDataMap.get(Helper.KeyData);

            if(pageTypeString.equals(PageType.OnePage.toString())) {
                String urlString = initDataDataMap.get(Helper.KeyUrl).toString();

                List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
                itemList.add(initDataDataMap);

                initHeaderView(itemList);

                BrowerView browerView = new BrowerView(this);
                browerView.setTag(0);
                browerView.setIsFinish(isFinish);
                browerView.loadUrl(urlString);
                browerView.setOnInitTitleListener(new BrowerView.OnInitTitleListener() {
                    @Override
                    public void OnDid(Map<String, Object> payloadMap) {
                        headerView.initTitle(0, payloadMap);
                    }
                });
                browerView.setOnBackAndRefreshListener(new BrowerView.OnBackAndRefreshListener() {
                    @Override
                    public void OnDid(Map<String, Object> payloadMap) {
                        //((BrowerView) browerViewList.get(position)).didBackAndRefreshAction();

                        Intent intent = new Intent();
                        intent.putExtra(Helper.KeyParameter, (Serializable) payloadMap);
                        setResult(Helper.CodeResult, intent);

                        finishActivity();
                    }
                });

                browerViewList.add(browerView);
                browerViewIsLoadList.add(true);

                RelativeLayout.LayoutParams browerViewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                browerViewLayoutParams.addRule(RelativeLayout.BELOW, headerView.getId());

                mainRelativeLayout.addView(browerView, browerViewLayoutParams);
            } else if(pageTypeString.equals(PageType.MultiTab.toString())) {
                List<Map<String, Object>> headerItemList = new ArrayList<Map<String, Object>>();
                headerItemList.add(initDataDataMap);

                initHeaderView(headerItemList);

                List<Map<String, Object>> tabItemList = (List<Map<String, Object>>) initDataDataMap.get(Helper.KeyTab);

                initTabView(tabItemList);

                initMainView(tabItemList);
            } else if(pageTypeString.equals(PageType.MultiFooter.toString())) {
                List<Map<String, Object>> itemList = (List<Map<String, Object>>) initDataDataMap.get(Helper.KeyFooter);

                initHeaderView(itemList);

                initFooterView(itemList);

                initMainView(itemList);
            } else {

            }
        }
    }

    private void initSplashView() {
        if(! isSplash) {
            return;
        }

        final RelativeLayout splashViewRelativeLayout = new RelativeLayout(this);
        splashViewRelativeLayout.setBackgroundColor(getResources().getColor(R.color.splash_background_color));
        RelativeLayout.LayoutParams splashViewRelativeLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mainRelativeLayout.addView(splashViewRelativeLayout, splashViewRelativeLayoutParams);

        ImageView imageView = new ImageView(this);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.logo));
        RelativeLayout.LayoutParams imageViewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        imageViewLayoutParams.width = Helper.formatPix(this, 400);
        imageViewLayoutParams.height = Helper.formatPix(this, 379);
        imageViewLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        imageViewLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        splashViewRelativeLayout.addView(imageView, imageViewLayoutParams);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                splashViewRelativeLayout.removeAllViews();

                mainRelativeLayout.removeView(splashViewRelativeLayout);
            }

        }, 2500);
    }

    private void initHeaderView(List<Map<String, Object>> itemList) {
        headerView = new HeaderView(this);
        //noinspection ResourceType
        headerView.setId(1);

        RelativeLayout.LayoutParams headerViewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        headerViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        headerView.initItem(itemList);
        headerView.setOnClickHeaderLeftButtonListener(new HeaderView.OnClickHeaderLeftButtonListener() {
            @Override
            public void OnClick(int position) {
                finishActivity();
            }
        });
        headerView.setOnClickHeaderRightButtonListener(new HeaderView.OnClickHeaderRightButtonListener() {
            @Override
            public void OnClick(int position) {
                ((BrowerView) browerViewList.get(position)).didClickHeaderRightButtonAction();
            }
        });

        if (! isRoot) {
            headerView.setLeftButtonVisibility(View.VISIBLE);
        }

        mainRelativeLayout.addView(headerView, headerViewLayoutParams);
    }

    /*private void initHorizontalScrollView(List<Map<String, Object>> itemList) {
        horizontalScrollView = new HorizontalScrollView(this);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);
        horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }

        });

        RelativeLayout horizontalScrollViewRelativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams horizontalScrollViewRelativeLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        horizontalScrollView.addView(horizontalScrollViewRelativeLayout, horizontalScrollViewRelativeLayoutParams);

        for(int i = 0; i < itemList.size(); i++) {
            BrowerView browerView = new BrowerView(this);

            RelativeLayout.LayoutParams browerViewLayoutParams = new RelativeLayout.LayoutParams(screenWidth, RelativeLayout.LayoutParams.MATCH_PARENT);
            browerViewLayoutParams.leftMargin = screenWidth * i;

            horizontalScrollViewRelativeLayout.addView(browerView, browerViewLayoutParams);
        }

        RelativeLayout.LayoutParams horizontalScrollViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        horizontalScrollViewParams.addRule(RelativeLayout.BELOW, headerView.getId());
        horizontalScrollViewParams.addRule(RelativeLayout.ABOVE, footerView.getId());

        mainRelativeLayout.addView(horizontalScrollView, horizontalScrollViewParams);
    }*/

    private void initTabView(List<Map<String, Object>> itemList) {
        tabView = new TabView(this);
        //noinspection ResourceType
        tabView.setId(2);
        tabView.setOnClickTabListener(new TabView.OnClickTabListener() {
            @Override
            public void OnClick(int position) {
                viewPager.setCurrentItem(position, false);

                Boolean isLoad = browerViewIsLoadList.get(position);
                if (!isLoad) {
                    browerViewIsLoadList.set(position, true);

                    System.out.println("setOnClickFooterListener:" + position);

                    String initDataString = getIntent().getStringExtra(Helper.KeyInitData);
                    Map<String, Object> initDataMap = JSON.parseObject(initDataString);
                    Map<String, Object> initDataDataMap = (Map<String, Object>) initDataMap.get(Helper.KeyData);
                    List<Map<String, Object>> itemList = (List<Map<String, Object>>) initDataDataMap.get(Helper.KeyTab);
                    Map<String, Object> map = itemList.get(position);
                    ((BrowerView) browerViewList.get(position)).loadUrl(map.get(Helper.KeyUrl).toString());
                }
            }
        });

        RelativeLayout.LayoutParams tabViewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tabViewLayoutParams.addRule(RelativeLayout.BELOW, headerView.getId());

        tabView.initItem(itemList);

        mainRelativeLayout.addView(tabView, tabViewLayoutParams);
    }

    private void initFooterView(List<Map<String, Object>> itemList) {
        footerView = new FooterView(this);
        footerView.setId(4);
        footerView.setOnClickFooterListener(new FooterView.OnClickFooterListener() {
            @Override
            public void OnClick(int position) {
                headerView.checkItem(position);

                viewPager.setCurrentItem(position, false);

                Boolean isLoad = browerViewIsLoadList.get(position);
                if (!isLoad) {
                    browerViewIsLoadList.set(position, true);

                    System.out.println("setOnClickFooterListener:" + position);

                    String initDataString = getIntent().getStringExtra(Helper.KeyInitData);
                    Map<String, Object> initDataMap = JSON.parseObject(initDataString);
                    Map<String, Object> initDataDataMap = (Map<String, Object>) initDataMap.get(Helper.KeyData);
                    List<Map<String, Object>> itemList = (List<Map<String, Object>>) initDataDataMap.get(Helper.KeyFooter);
                    Map<String, Object> map = itemList.get(position);
                    ((BrowerView) browerViewList.get(position)).loadUrl(map.get(Helper.KeyUrl).toString());
                }
            }
        });

        RelativeLayout.LayoutParams footerViewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        footerViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        footerView.initItem(itemList);

        mainRelativeLayout.addView(footerView, footerViewLayoutParams);
    }

    private void initMainView(List<Map<String, Object>> itemList) {
        for(int i = 0; i < itemList.size(); i++) {
            Map<String, Object> map = itemList.get(i);

            BrowerView browerView = new BrowerView(this);
            browerView.setIsFinish(isFinish);
            if(i == 0) {
                browerView.loadUrl(map.get(Helper.KeyUrl).toString());
                browerViewIsLoadList.add(true);
            } else {
                browerViewIsLoadList.add(false);
            }
            browerViewList.add(browerView);
        }

        BasePagerAdapter viewPagerAdapter = new BasePagerAdapter(browerViewList);

        viewPager = new BaseViewPager(this);
        viewPager.setOffscreenPageLimit(itemList.size() - 1);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0, false);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (Helper.isNullOrEmpty(tabView)) {
                    footerView.checkItem(position);
                } else {
                    tabView.checkItem(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

        if (! Helper.isNullOrEmpty(tabView)) {
            viewPager.setScroll(true);
        }

        RelativeLayout.LayoutParams viewPagerLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        if (Helper.isNullOrEmpty(tabView)) {
            viewPagerLayoutParams.addRule(RelativeLayout.BELOW, headerView.getId());
            viewPagerLayoutParams.addRule(RelativeLayout.ABOVE, footerView.getId());
        } else {
            viewPagerLayoutParams.addRule(RelativeLayout.BELOW, tabView.getId());
        }

        mainRelativeLayout.addView(viewPager, viewPagerLayoutParams);
    }

    private void finishActivity() {
        finish();

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //System.out.println("onActivityResult");

        super.onActivityResult(requestCode, resultCode, data);

        if(pageTypeString.equals(PageType.OnePage.toString())) {
            for (int i = 0; i < mainRelativeLayout.getChildCount(); i++) {
                View view = mainRelativeLayout.getChildAt(i);

                if (view instanceof BrowerView) {
                    ((BrowerView) view).onActivityResult(requestCode, resultCode, data);
                }
            }
        } else if(pageTypeString.equals(PageType.MultiFooter.toString()) || pageTypeString.equals(PageType.MultiTab.toString())) {
            ((BrowerView) browerViewList.get(viewPager.getCurrentItem())).onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //System.out.println("onNewIntent");
        super.onNewIntent(intent);

        if(pageTypeString.equals(PageType.OnePage.toString())) {
            for (int i = 0; i < mainRelativeLayout.getChildCount(); i++) {
                View view = mainRelativeLayout.getChildAt(i);

                if (view instanceof BrowerView) {
                    ((BrowerView) view).onNewIntent(intent);
                }
            }
        } else if(pageTypeString.equals(PageType.MultiFooter.toString())) {
            ((BrowerView) browerViewList.get(viewPager.getCurrentItem())).onNewIntent(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isDoubleClickBack) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    exitTime = System.currentTimeMillis();

                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }
            } else {
                finishActivity();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();

        for(View view : browerViewList) {
            if(view instanceof BrowerView) {
                ((BrowerView) view).onResume();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        for(View view : browerViewList) {
            if(view instanceof BrowerView) {
                ((BrowerView) view).onPause();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(baseBroadcastReceiver);

        for(View view : browerViewList) {
            if(view instanceof BrowerView) {
                ((BrowerView) view).onDestroy();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        for(View view : browerViewList) {
            if(view instanceof BrowerView) {
                ((BrowerView) view).onStart();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        for(View view : browerViewList) {
            if(view instanceof BrowerView) {
                ((BrowerView) view).onStop();
            }
        }
    }

    private class BaseBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            for(View view : browerViewList) {
                if(view instanceof BrowerView) {
                    ((BrowerView) view).didPushAction();
                }
            }
        }
    }
}
