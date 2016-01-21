package com.nowui.daning.utility;

import android.app.ActivityManager;
import android.content.Context;
import android.view.WindowManager;
import android.webkit.WebResourceResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Helper {

    public static final String WebUrl = "http://run.nowui.com";
    //public static final String WebUrl = "http://192.168.200.2:8088";

    //Key
    public static final String KeyId = "id";
    public static final String KeyUrl = "url";
    public static final String KeyText = "text";
    public static final String KeyParameter = "parameter";
    public static final String KeyIsOpen = "isOpen";
    public static final String KeyMimeType = "mimeType";
    public static final String KeyName = "name";
    public static final String KeyIsLocal = "isLocal";
    public static final String KeyPath = "path";
    public static final String KeyInitData = "initData";
    public static final String KeyType = "type";
    public static final String KeyData = "data";
    public static final String KeyTitle = "title";
    public static final String KeyIcon = "icon";
    public static final String KeyHeader = "header";
    public static final String KeyCenter = "center";
    public static final String KeyLeft = "left";
    public static final String KeyRight = "right";
    public static final String KeyTab = "tab";
    public static final String KeyFooter = "footer";
    public static final String KeyIsRoot = "isRoot";
    public static final String KeyIsSplash = "isSplash";
    public static final String KeyIsDoubleClickBack = "isDoubleClickBack";
    public static final String KeyAppSetting = "appSetting";
    public static final String KeyJpushRegistrationId = "jpushRegistrationId";
    public static final String KeyAppUserId = "appUserId";
    public static final String KeyAppUserName = "appUserName";
    public static final String KeyAppDepartmentId = "departmentId";
    public static final String KeyAppDepartmentName = "departmentName";
    public static final String KeyIsFinish = "isFinish";
    public static final String KeyAction = "action";
    public static final String KeyPosition = "position";
    public static final String KeyList = "list";
    public static final String KeyPushFilter = "pushFilter";

    //Event
    public static final String EventPull = "Pull";
    public static final String EventTitle = "Title";
    public static final String EventGo = "Go";
    public static final String EventBack = "Back";
    public static final String EventBackCallback = "BackCallback";
    public static final String EventNormal = "Normal";
    public static final String EventNormalCallback = "NormalCallback";
    public static final String EventDownload = "Download";
    public static final String EventInit = "Init";
    public static final String EventSaveSetting = "SaveSetting";
    public static final String EventFindSetting = "FindSetting";
    public static final String EventClickHeaderRightButton = "ClickHeaderRightButton";

    public static final String ActionGetInit = "getInit";
    public static final String ActionSetInit = "setInit";
    public static final String ActionSetTitle = "setTitle";
    public static final String ActionSetSetting = "setSetting";
    public static final String ActionGetSetting = "getSetting";
    public static final String ActionSetSwitch = "setSwitch";
    public static final String ActionSetNavite = "setNavite";
    public static final String ActionSetBack = "setBack";
    public static final String ActionSetBackAndRefresh = "setBackAndRefresh";
    public static final String ActionSetClickHeaderRightButton = "setClickHeaderRightButton";
    public static final String ActionSetPreviewImage = "setPreviewImage";
    public static final String ActionGetStart = "getStart";
    public static final String ActionGetPush = "getPush";
    public static final String ActionGetAppear = "getAppear";
    public static final String ActionGetAlert = "getAlert";

    public static final int TabWidth = 240;
    public static final int TabHeight = 44;

    //Code
    public static final int CodeRequest = 0;
    public static final int CodeResult = 10;

    public static final String HttpHeader = "http";
    public static final String WebviewplusHeader = "webviewplus://";

    public static final String BaseCharset = "utf-8";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null)
            return true;

        if (obj instanceof CharSequence)
            return ((CharSequence) obj).length() == 0;

        if (obj instanceof Collection)
            return ((Collection<?>) obj).isEmpty();

        if (obj instanceof Map)
            return ((Map<?, ?>) obj).isEmpty();

        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (int i = 0; i < object.length; i++) {
                if (!isNullOrEmpty(object[i])) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }

    public static int formatPix(Context context, float pixValue) {
        int width = 640;

        return (int) Math.round(getScreenWidth(context) * 1.0 / width * pixValue);
    }

    public static String formatDateTime() {
        return dateFormat.format(new Date());
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    public static WebResourceResponse checkLoaclFile(Context context, String url) {
        if (Helper.isNullOrEmpty(url)) {
            return null;
        }

        if(! url.contains(Helper.WebUrl)) {
            return null;
        }

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        Map<String, String> map = new HashMap<String, String>();
        map.put(Helper.KeyMimeType, "text/html");
        map.put(Helper.KeyName, ".html");
        list.add(map);

        map = new HashMap<String, String>();
        map.put(Helper.KeyMimeType, "text/javascript");
        map.put(Helper.KeyName, ".js");
        list.add(map);

        map = new HashMap<String, String>();
        map.put(Helper.KeyMimeType, "text/css");
        map.put(Helper.KeyName, ".css");
        list.add(map);

        map = new HashMap<String, String>();
        map.put(Helper.KeyMimeType, "image/png");
        map.put(Helper.KeyName, ".png");
        list.add(map);

        map = new HashMap<String, String>();
        map.put(Helper.KeyMimeType, "application/x-font-ttf");
        map.put(Helper.KeyName, ".ttf");
        list.add(map);

        map = new HashMap<String, String>();
        map.put(Helper.KeyMimeType, "application/x-font-woff");
        map.put(Helper.KeyName, ".woff");
        list.add(map);

        map = new HashMap<String, String>();
        map.put(Helper.KeyMimeType, "application/x-font-woff2");
        map.put(Helper.KeyName, ".woff2");
        list.add(map);

        map = new HashMap<String, String>();
        map.put(Helper.KeyMimeType, "application/x-font-eot");
        map.put(Helper.KeyName, ".eot");
        list.add(map);

        map = new HashMap<String, String>();
        map.put(Helper.KeyMimeType, "application/x-font-svg");
        map.put(Helper.KeyName, ".svg");
        list.add(map);

        map = new HashMap<String, String>();
        map.put(Helper.KeyMimeType, "application/x-font-otf");
        map.put(Helper.KeyName, ".otf");
        list.add(map);

        map = new HashMap<String, String>();
        map.put(Helper.KeyMimeType, "image/x-icon");
        map.put(Helper.KeyName, ".ico");
        list.add(map);

        Boolean isNotExit = true;

        for (Map<String, String> item : list) {
            if (url.contains(item.get(Helper.KeyName))) {
                WebResourceResponse response = null;

                int index = url.indexOf("?");
                String tempUrl = url;
                if(index > -1) {
                    tempUrl = url.substring(0, url.indexOf("?"));
                }

                tempUrl = tempUrl.replace(Helper.WebUrl + "/", "");

                tempUrl = tempUrl.replace(Helper.WebUrl, "");

                try {
                    response = new WebResourceResponse(item.get(Helper.KeyMimeType), "utf-8", context.getAssets().open(tempUrl));
                } catch (IOException e) {
                    System.out.println("--------------------------------------------:" + url);
                    e.printStackTrace();
                }

                if (response != null) {
                    isNotExit = false;
                }

                return response;
            }
        }

        if (isNotExit) {
            System.out.println("--------------------------------------------:" + url);
        }

        return null;
    }

    public static boolean isAppOnForeground(Context context, String packageName) {
        // Returns a list of application processes that are running on the device
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    public static String encode(String string) {
        try {
            return java.net.URLEncoder.encode(string, Helper.BaseCharset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return string;
    }

    public static String decode(String string) {
        try {
            return java.net.URLDecoder.decode(string, Helper.BaseCharset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return string;
    }

}
