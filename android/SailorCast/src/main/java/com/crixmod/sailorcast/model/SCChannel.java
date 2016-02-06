package com.crixmod.sailorcast.model;

import android.app.Application;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.SailorCast;

/**
 * Created by fire3 on 2015/1/19.
 */
public class SCChannel {

    public static final int   UNKNOWN = -1;  //未知
    private static final int  MIN_CHANNEL_ID = 1;
    public static final int   SHOW = 1;  //电视剧
    public static final int   MOVIE = 2;  //电影
    public static final int   COMIC = 3;  //动漫
    public static final int   DOCUMENTARY = 4;  //纪录片
    public static final int   MUSIC = 5;  //音乐
    public static final int   VARIETY = 6;  //综艺
    public static final int   LOCAL_BOOKMARK = 7;
    public static final int   LOCAL_HISTORY = 8;
    private static final int  MAX_CHANNEL_ID = 8;  //如果增加频道，注意修改该值

    private String mChannelName = "unknown";

    private int mChannelID = UNKNOWN;

    public SCChannel(int mChannelID) {
        if(mChannelID >= SHOW && mChannelID <= MAX_CHANNEL_ID)
            this.mChannelID = mChannelID;

        if(mChannelID == SHOW)
            mChannelName = SailorCast.getResource().getString(R.string.channel_show);
        else if(mChannelID == MOVIE)
            mChannelName = SailorCast.getResource().getString(R.string.channel_movie);
        else if(mChannelID == COMIC)
            mChannelName = SailorCast.getResource().getString(R.string.channel_comic);
        //else if(mChannelID == ENT)
        //    mChannelName = SailorCast.getResource().getString(R.string.channel_ent);
        else if(mChannelID == DOCUMENTARY)
            mChannelName = SailorCast.getResource().getString(R.string.channel_documentary);
        else if(mChannelID == MUSIC)
            mChannelName = SailorCast.getResource().getString(R.string.channel_music);
        else if(mChannelID == VARIETY)
            mChannelName = SailorCast.getResource().getString(R.string.channel_variety);
        else if(mChannelID == LOCAL_BOOKMARK)
            mChannelName = SailorCast.getResource().getString(R.string.channel_bookmark);
        else if(mChannelID == LOCAL_HISTORY)
            mChannelName = SailorCast.getResource().getString(R.string.channel_history);
        else
            mChannelName = "unknown";

    }

    public String getChannelName() {
        return mChannelName;
    }

    public int getChannelID() {
        return mChannelID;
    }

    public void setChannelID(int mChannelID) {
        this.mChannelID = mChannelID;
    }

    @Override
    public String toString() {
        return mChannelName;
    }

    public static int getChannelCount () {
        return MAX_CHANNEL_ID;
    }

    public boolean isLocalChannel() {
        if(mChannelID == LOCAL_BOOKMARK
                || mChannelID == LOCAL_HISTORY
                )
            return true;
        else
            return false;
    }
}
