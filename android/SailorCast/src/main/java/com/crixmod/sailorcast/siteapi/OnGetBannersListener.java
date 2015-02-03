package com.crixmod.sailorcast.siteapi;

import com.crixmod.sailorcast.model.SCBanners;
import com.crixmod.sailorcast.model.SCFailLog;

/**
 * Created by fire3 on 15-1-28.
 */
public interface OnGetBannersListener {
    public void onGetBannersSuccess(SCBanners banners);
    public void onGetBannersFailed(SCFailLog failReason);
}
