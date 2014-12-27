package com.crixmod.sailorcast.model;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by fire3 on 2014/12/26.
 */
public class SCVideos {
    private static final String TAG = "SCVideos";
    protected ArrayList<SCVideo> videos;

    public SCVideos() {
        videos = new ArrayList<>();
    }

    public void add(SCVideo album) {
        videos.add(album);
    }

    public int size() {
        return  videos.size();
    }

    public SCVideo get(int i) {
        return videos.get(i);
    }


    public void debugLog() {
        for (SCVideo a : videos) {
            Log.d(TAG, a.toString());
        }
    }
}
