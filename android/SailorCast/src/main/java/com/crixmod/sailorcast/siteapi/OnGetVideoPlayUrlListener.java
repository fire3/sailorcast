package com.crixmod.sailorcast.siteapi;

import com.crixmod.sailorcast.model.SCVideo;

/**
 * Created by fire3 on 14-12-29.
 */
public interface OnGetVideoPlayUrlListener {
    void onGetVideoPlayUrlNormal(String urlNormal);
    void onGetVideoPlayUrlHigh(String urlHigh);
    void onGetVideoPlayUrlSuper(String urlSuper);
    void onGetVideoPlayUrlFailed(String reason);
}
