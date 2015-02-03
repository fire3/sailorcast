package com.crixmod.sailorcast.siteapi;

import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCFailLog;
import com.crixmod.sailorcast.model.SCVideos;

/**
 * Created by fire3 on 2014/12/26.
 */
public interface OnGetVideosListener {
    public void onGetVideosSuccess(SCVideos videos);
    public void onGetVideosFailed(SCFailLog failReason);
}
