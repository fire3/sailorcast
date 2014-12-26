package com.crixmod.sailorcast;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

/**
 * Created by fire3 on 14-12-26.
 */
public class SailorCast extends Application {
    private static Context mContext;
    private static OkHttpClient mHttpClient;
    private static Gson mGson;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mHttpClient = new OkHttpClient();
        mGson = new Gson();
    }

    public static Context getContext() {
        return mContext;
    }

    public static OkHttpClient getHttpClient() {
        return mHttpClient;
    }

    public static Gson getGson() {
        return mGson;
    }
}
