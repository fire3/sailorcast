package com.crixmod.sailorcast.siteapi;

import com.crixmod.sailorcast.model.SCAlbum;

/**
 * Created by fire3 on 2014/12/26.
 */
public interface OnGetAlbumVideosListener {
    public void onGetAlbumVideosSuccess(SCAlbum album);
    public void onGetAlbumVideosFailed(String failReason);
}
