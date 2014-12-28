package com.crixmod.sailorcast.siteapi;

import com.crixmod.sailorcast.model.SCAlbum;

/**
 * Created by fire3 on 14-12-26.
 */
abstract public class BaseSiteApi {
    abstract public void doSearch(String key, OnSearchRequestListener listener);
    abstract public void doGetAlbumVideos(SCAlbum album, int pageNo, int pageSize,  OnGetVideosListener listener);
    abstract public void doGetAlbumDesc(SCAlbum album, OnGetAlbumDescListener listener);
}
