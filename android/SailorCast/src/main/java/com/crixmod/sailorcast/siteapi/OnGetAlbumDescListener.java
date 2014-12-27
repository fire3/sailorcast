package com.crixmod.sailorcast.siteapi;

import com.crixmod.sailorcast.model.SCAlbum;

/**
 * Created by fire3 on 2014/12/27.
 */
public interface OnGetAlbumDescListener {
    public void onGetAlbumDescSuccess(SCAlbum album);
    public void onGetAlbumDescFailed(String failReason);
}
