package com.crixmod.sailorcast.siteapi;

import com.crixmod.sailorcast.model.SCFailLog;
import com.crixmod.sailorcast.model.SCLiveStream;
import com.crixmod.sailorcast.model.SCLiveStreamPrograms;

/**
 * Created by fire3 on 15-3-11.
 */
public interface OnGetLiveStreamProgramsListener {
    void onGetLiveStreamProgramsSuccess(SCLiveStream stream, SCLiveStreamPrograms programs);
    void onGetLiveStreamProgramsFailed(SCFailLog reason);
}
