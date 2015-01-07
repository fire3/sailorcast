package com.crixmod.sailorcast.utils;

import android.util.Log;

import com.crixmod.sailorcast.SailorCast;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;

/**
 * Created by fire3 on 14-12-30.
 */
public class HttpUtils {

    private static final String REQUEST_TAG = "okhttp";
    public static void asyncGet(String url, Callback callback) {
        Request request = new Request.Builder()
                .tag(REQUEST_TAG)
                .url(url)
                .build();

        Log.d("HttpUtils","asyncGet: " + url);
        SailorCast.getHttpClient().newCall(request).enqueue(callback);
    }

    public static void asyncGet(Request request, Callback callback) {
        SailorCast.getHttpClient().newCall(request).enqueue(callback);
    }

    public static void cancelAll() {
        SailorCast.getHttpClient().cancel(REQUEST_TAG);
    }
}
