package com.crixmod.sailorcast.utils;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.crixmod.sailorcast.SailorCast;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by fire3 on 14-12-30.
 */
public class HttpUtils {

    private static final String REQUEST_TAG = "okhttp";

    public static Request buildRequest(String url) {
        Request request = new Request.Builder()
                .tag(REQUEST_TAG)
                .url(url)
                .build();
        Log.d("HttpUtils", "request Url: " + url);
        return request;
    }

    public static void asyncGet(String url, Activity activity, Callback callback) {

        Request request = buildRequest(url);
        asyncGet(request,activity,callback);
    }

    public static void asyncGet(String url, Callback callback) {
        Request request = buildRequest(url);
        asyncGet(request, callback);
    }

    public static void asyncGet(Request request, Callback callback) {
        SailorCast.getHttpClient().newCall(request).enqueue(callback);
    }

    public static void asyncGet(Request request, final Activity activity, final Callback callback) {

        SailorCast.getHttpClient().newCall(request).enqueue(new Callback() {
            Handler mainHandler = new Handler(activity.getMainLooper());
            @Override
            public void onFailure(final Request request, final IOException e) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(request,e);
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            callback.onResponse(response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public static void cancelAll() {
        SailorCast.getHttpClient().cancel(REQUEST_TAG);
    }
}
