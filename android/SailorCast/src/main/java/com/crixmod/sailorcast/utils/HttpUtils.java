package com.crixmod.sailorcast.utils;

import android.util.Log;

import com.crixmod.sailorcast.SailorCast;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;

/**
 * Created by fire3 on 14-12-30.
 */
public class HttpUtils {

    public static void asyncGet(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Log.d("fire3 asyncGet", url);
        SailorCast.getHttpClient().newCall(request).enqueue(callback);
    }

    public static void asyncGet(Request request, Callback callback) {
        SailorCast.getHttpClient().newCall(request).enqueue(callback);
    }

}
