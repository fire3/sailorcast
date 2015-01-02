package com.crixmod.sailorcast.siteapi;

import com.crixmod.sailorcast.model.SCAlbums;

/**
 * Created by fire3 on 2014/12/26.
 */
public interface OnSearchRequestListener {
    public void onSearchSuccess(SCAlbums albums);
    public void onSearchFailed(String failReason);
}
