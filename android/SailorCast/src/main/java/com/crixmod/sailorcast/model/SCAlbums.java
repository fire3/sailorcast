package com.crixmod.sailorcast.model;

import android.util.Log;

import com.crixmod.sailorcast.model.SCAlbum;

import java.util.ArrayList;

/**
 * Created by fire3 on 2014/12/26.
 */
public class SCAlbums extends ArrayList<SCAlbum> {
    private static final String TAG = "SCAlbums";
    public void debugLog() {
        for (SCAlbum a : this) {
            Log.d(TAG, a.toString());
        }
    }
}
