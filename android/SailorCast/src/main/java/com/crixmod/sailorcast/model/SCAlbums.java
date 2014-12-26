package com.crixmod.sailorcast.model;

import android.util.Log;

import com.crixmod.sailorcast.model.SCAlbum;

import java.util.ArrayList;

/**
 * Created by fire3 on 2014/12/26.
 */
public class SCAlbums {
    private static final String TAG = "SCAlbums";
    protected ArrayList<SCAlbum> albums;

    public SCAlbums() {
        albums = new ArrayList<>();
    }

    public void add(SCAlbum album) {
        albums.add(album);
    }

    public int size() {
        return  albums.size();
    }

    public SCAlbum get(int i) {
        return albums.get(i);
    }


    public void debugLog() {
        for (SCAlbum a : albums) {
            Log.d(TAG, a.toString());
        }
    }
}
