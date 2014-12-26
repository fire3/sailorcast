package com.crixmod.sailorcast;

import android.app.Application;
import android.content.Context;

/**
 * Created by fire3 on 14-12-26.
 */
public class SailorCast extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}
