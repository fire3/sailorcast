package com.crixmod.sailorcast.siteapi;

import com.crixmod.sailorcast.model.SCAlbums;
import com.crixmod.sailorcast.model.SCChannelFilter;

/**
 * Created by fire3 on 2014/12/26.
 */
public interface OnGetChannelFilterListener {
    public void onGetChannelFilterSuccess(SCChannelFilter filter);
    public void onGetChannelFilterFailed(String failReason);
}
