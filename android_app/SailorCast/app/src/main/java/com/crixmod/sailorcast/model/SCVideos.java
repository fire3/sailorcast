package com.crixmod.sailorcast.model;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by fire3 on 2014/12/26.
 */
public class SCVideos extends ArrayList<SCVideo> {

    private static final String TAG = "SCVideos";

    public void debugLog() {
        for (SCVideo a : this) {
            Log.d(TAG, a.toString());
        }
    }

}
