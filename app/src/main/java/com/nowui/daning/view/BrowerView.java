package com.nowui.daning.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.nowui.daning.R;
import com.nowui.daning.activity.BaseActivity;
import com.nowui.daning.activity.BrowerActivity;
import com.nowui.daning.utility.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrowerView extends RelativeLayout {

    private Context myContent;
    private PullToRefreshWebView pullRefreshWebView;
    private WebView webView;
    private boolean isStartActivityForResult;
    private String urlString;
    private boolean isFinish;
    private boolean isOnLoadFinished;

    private OnInitTitleListener onInitTitleListener;

    public interface OnInitTitleListener {
        public void OnDid(Map<String, Object> payloadMap);
    }

    public void setOnInitTitleListener(OnInitTitleListener listener) {
        onInitTitleListener = listener;
    }

    private OnBackAndRefreshListener onBackAndRefreshListener;

    public interface OnBackAndRefreshListener {
        public void OnDid(Map<String, Object> payloadMap);
    }

    public void setOnBackAndRefreshListener(OnBackAndRefreshListener listener) {
        onBackAndRefreshListener = listener;
    }

    private OnPreviewImageListener onPreviewImageListener;

    public interface OnPreviewImageListener {
        public void OnDid(Map<String, Object> payloadMap);
    }

    public void setOnPreviewImageListener(OnPreviewImageListener listener) {
        onPreviewImageListener = listener;
    }

    public BrowerView(Context context) {
        super(context);

        myContent = context;

        initView(context);
    }

    public BrowerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        myContent = context;

        initView(context);
    }

    public BrowerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        myContent = context;

        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.view_brower, this);

        pullRefreshWebView = (PullToRefreshWebView) findViewById(R.id.pull_refresh_webview);
        pullRefreshWebView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullRefreshWebView.getLoadingLayoutProxy().setPullLabel(getResources().getString(R.string.pull));
        pullRefreshWebView.getLoadingLayoutProxy().setRefreshingLabel(getResources().getString(R.string.refreshing));
        pullRefreshWebView.getLoadingLayoutProxy().setReleaseLabel(getResources().getString(R.string.release));
        pullRefreshWebView.getLoadingLayoutProxy().setLastUpdatedLabel(getResources().getString(R.string.update) + Helper.formatDateTime());
        pullRefreshWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WebView>() {
            @Override
            public void onRefresh(PullToRefreshBase<WebView> refreshView) {
                loadUrl(urlString);
            }
        });

        webView = pullRefreshWebView.getRefreshableView();
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new BaseWebViewClient());

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setSaveFormData(false);
        webSettings.setSavePassword(false);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
    }

    public void loadUrl(String url) {
        System.out.println("loadUrl:" + url);

        urlString = url;

        if(urlString.contains(Helper.HttpHeader)) {
            webView.loadUrl(url);
        } else {
            webView.loadUrl(Helper.WebUrl + url);
        }
    }

    public void didClickHeaderRightButtonAction() {
        System.out.println("javascript:" + Helper.ActionSetClickHeaderRightButton + "({});");
        webView.loadUrl("javascript:" + Helper.ActionSetClickHeaderRightButton + "({});");
    }

    public void didPushAction() {
        System.out.println("javascript:" + Helper.ActionGetPush + "({});");
        webView.loadUrl("javascript:" + Helper.ActionGetPush + "({});");
    }

    public void didBackAndRefreshAction() {
        System.out.println("javascript:" + Helper.ActionSetBackAndRefresh + "({});");
        webView.loadUrl("javascript:" + Helper.ActionSetBackAndRefresh + "({});");
    }

    public void setIsFinish(boolean result) {
        isFinish = result;
    }

    public  Map<Object, Object> getUserMap() {
        String appUserId = ((BaseActivity)myContent).setting.getString(Helper.KeyAppUserId, "");
        String appUserName = ((BaseActivity)myContent).setting.getString(Helper.KeyAppUserName, "");
        String jpushRegistrationId = ((BaseActivity)myContent).setting.getString(Helper.KeyJpushRegistrationId, "");

        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put(Helper.KeyAppUserId, appUserId);
        map.put(Helper.KeyAppUserName, appUserName);
        map.put(Helper.KeyJpushRegistrationId, jpushRegistrationId);

        return map;
    }

    public void onStart() {
        System.out.println("onStart:" + urlString);

        System.out.println("javascript:" + Helper.ActionGetStart + "({});");
        webView.loadUrl("javascript:try{" + Helper.ActionGetStart + "({});}catch(e){}");
    }

    public void onStop() {
        System.out.println("onStop:" + urlString);
    }

    public void onResume() {
        System.out.println("onResume:" + urlString);

        Map<Object, Object> map = getUserMap();

        System.out.println("javascript:" + Helper.ActionGetAppear + "(" + JSON.toJSONString(map) + ")");
        webView.loadUrl("javascript:" + Helper.ActionGetAppear + "(" + JSON.toJSONString(map) + ")");

        webView.onResume();
    }

    public void onPause() {
        System.out.println("onPause:" + urlString);

        webView.onPause();
    }

    public void onDestroy() {
        System.out.println("onDestroy:" + urlString);

        webView.removeAllViews();
        webView.destroy();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("onActivityResult:" + urlString);

        isStartActivityForResult = false;

        if(requestCode == Helper.CodeRequest) {
            if (resultCode == Helper.CodeResult) {
                Map<String, Object> map = (Map<String, Object>) data.getSerializableExtra(Helper.KeyParameter);

                System.out.println("javascript:" + Helper.ActionSetBackAndRefresh + "({});");
                webView.loadUrl("javascript:" + Helper.ActionSetBackAndRefresh + "({});");
            }
        }
    }

    public void onNewIntent(Intent intent) {
        System.out.println("onNewIntent:" + urlString);

    }

    private class BaseWebViewClient extends WebViewClient {

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return Helper.checkLoaclFile(myContent, request.getUrl().toString());
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            return Helper.checkLoaclFile(myContent, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            System.out.println("shouldOverrideUrlLoading:" + url);

            if(url.startsWith(Helper.WebviewplusHeader)) {
                Map<String, Object> jsonMap = JSON.parseObject(Helper.decode(url.replace(Helper.WebviewplusHeader, "")), new TypeReference<Map<String, Object>>() {

                });

                System.out.println(jsonMap);

                String action = jsonMap.get(Helper.KeyAction).toString();
                Map<String, Object> payloadMap = (Map<String, Object>) jsonMap.get(Helper.KeyData);

                if(action.equals(Helper.ActionSetTitle)) {
                    if (onInitTitleListener != null) {
                        onInitTitleListener.OnDid(payloadMap);
                    }
                } else if(action.equals(Helper.ActionGetSetting)) {
                    Map<Object, Object> map = getUserMap();

                    System.out.println("javascript:" + Helper.ActionGetSetting + "(" + JSON.toJSONString(map) + ")");
                    webView.loadUrl("javascript:" + Helper.ActionGetSetting + "(" + JSON.toJSONString(map) + ")");
                } else if(action.equals(Helper.ActionSetSetting)) {
                    if (! Helper.isNullOrEmpty(payloadMap.get(Helper.KeyAppUserId))) {
                        ((BaseActivity) myContent).editor.putString(Helper.KeyAppUserId, payloadMap.get(Helper.KeyAppUserId).toString());
                    }

                    if (! Helper.isNullOrEmpty(payloadMap.get(Helper.KeyAppUserName))) {
                        ((BaseActivity) myContent).editor.putString(Helper.KeyAppUserName, payloadMap.get(Helper.KeyAppUserName).toString());
                    }

                    ((BaseActivity) myContent).editor.commit();
                } else if(action.equals(Helper.ActionSetSwitch)) {
                    boolean isRoot = false;
                    boolean isSplash = false;
                    boolean isDoubleClickBack = false;

                    if(! Helper.isNullOrEmpty(payloadMap.get(Helper.KeyIsRoot))) {
                        isRoot = (boolean) payloadMap.get(Helper.KeyIsRoot);
                    }

                    if(! Helper.isNullOrEmpty(payloadMap.get(Helper.KeyIsSplash))) {
                        isSplash = (boolean) payloadMap.get(Helper.KeyIsSplash);
                    }

                    if(! Helper.isNullOrEmpty(payloadMap.get(Helper.KeyIsDoubleClickBack))) {
                        isDoubleClickBack = (boolean) payloadMap.get(Helper.KeyIsDoubleClickBack);
                    }

                    if(! Helper.isNullOrEmpty(payloadMap.get(Helper.KeyIsFinish))) {
                        isFinish = (boolean) payloadMap.get(Helper.KeyIsFinish);
                    }

                    Intent intent = new Intent();
                    intent.putExtra(Helper.KeyInitData, payloadMap.get(Helper.KeyData).toString());
                    intent.putExtra(Helper.KeyIsRoot, isRoot);
                    intent.putExtra(Helper.KeyIsSplash, isSplash);
                    intent.putExtra(Helper.KeyIsDoubleClickBack, isDoubleClickBack);
                    intent.putExtra(Helper.KeyIsFinish, isFinish);
                    intent.setClass(myContent, BrowerActivity.class);
                    ((Activity)myContent).startActivityForResult(intent, Helper.CodeRequest);

                    if(! Helper.isNullOrEmpty(payloadMap.get(Helper.KeyIsFinish))) {
                        ((Activity)myContent).finish();
                    }
                } else if(action.equals(Helper.ActionSetBack)) {

                } else if(action.equals(Helper.ActionSetBackAndRefresh)) {
                    if (onBackAndRefreshListener != null) {
                        onBackAndRefreshListener.OnDid(payloadMap);
                    }
                } else if(action.equals(Helper.ActionSetPreviewImage)) {
                    if (onPreviewImageListener != null) {
                        onPreviewImageListener.OnDid(payloadMap);
                    }

                    List<String> list = (List<String>) payloadMap.get(Helper.KeyList);

                    ArrayList<String> array = new ArrayList<String>();

                    for (String s : list) {
                        array.add(s);
                    }

                } else if(action.equals(Helper.ActionSetNavite)) {
                    String initDataString = payloadMap.get(Helper.KeyData).toString();

                    Intent intent = new Intent();
                    intent.putExtra(Helper.KeyInitData, initDataString);
                    intent.setClass(myContent, BrowerActivity.class);
                    ((Activity) myContent).startActivityForResult(intent, Helper.CodeRequest);

                    ((Activity) myContent).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else if(action.equals(Helper.ActionGetAlert)) {
                    final EditText editText = new EditText(myContent);

                    AlertDialog.Builder builder = new  AlertDialog.Builder(myContent);
                    builder.setTitle(payloadMap.get(Helper.KeyTitle).toString());
                    builder.setView(editText);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Map<Object, Object> map = new HashMap<Object, Object>();
                            map.put(Helper.KeyData, editText.getText());

                            System.out.println("javascript:" + Helper.ActionGetAlert + "(" + JSON.toJSONString(map) + ")");
                            webView.loadUrl("javascript:" + Helper.ActionGetAlert + "(" + JSON.toJSONString(map) + ")");
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.show();
                }

                return true;
            }

            if (url.startsWith(Helper.HttpHeader)) {

                if (url.contains(Helper.WebUrl)) {
                    url = url.replace(Helper.WebUrl, "");
                }

                if (url.equals(urlString)) {
                    return false;
                } else {
                    if (isStartActivityForResult) {
                        return true;
                    }
                    isStartActivityForResult = true;

                    String initDataString = "{\"type\": \"OnePage\", \"data\": {\"url\": \"" + url + "\", \"header\": {\"center\": {\"data\": \"\"} } } }";

                    Intent intent = new Intent();
                    intent.putExtra(Helper.KeyInitData, initDataString);
                    intent.setClass(myContent, BrowerActivity.class);
                    ((Activity) myContent).startActivityForResult(intent, Helper.CodeRequest);

                    ((Activity) myContent).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    return true;
                }
            }

            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            System.out.println("onLoadFinished:" + urlString);

            super.onPageFinished(view, url);

            if (isOnLoadFinished) {
                return;
            }

            isOnLoadFinished = true;

            /*SharedPreferences setting = myContent.getSharedPreferences(Helper.KeyAppSetting, Activity.MODE_PRIVATE);
            String appUserId = setting.getString(Helper.KeyAppUserId, "");

            System.out.println("javascript:" + Helper.ActionGetInit + "({'isFinish':" + isFinish + ",'" + Helper.KeyAppUserId + "':'" + appUserId + "'});");
            xWalkView.load("javascript:" + Helper.ActionGetInit + "({'isFinish':" + isFinish + ",'" + Helper.KeyAppUserId + "':'" + appUserId + "'});", null);*/
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

}
