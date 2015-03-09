package com.crixmod.sailorcast.siteapi;

import com.crixmod.sailorcast.model.SCFailLog;
import com.crixmod.sailorcast.model.SCLiveStreams;

/**
 * Created by fire3 on 15-3-9.
 */
public interface OnGetLiveStreamsListener {
    public void onGetLiveStreamsSuccess(SCLiveStreams streams);
    public void onGetLiveStreamsFailed(SCFailLog failReason);
}
