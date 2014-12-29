package com.crixmod.sailorcast.siteapi;

import com.crixmod.sailorcast.model.SCVideo;

/**
 * Created by fire3 on 14-12-29.
 */
public interface OnGetVideoPlayUrlListener {
    void onGetVideoPlayUrlSuccess(SCVideo video);
    void onGetVideoPlayUrlFailed(String reason);
}
