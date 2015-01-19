package com.crixmod.sailorcast.model;

/**
 * Created by fire3 on 2015/1/19.
 */
public class SCChannel {

    public static final int   SHOW = 1;  //电视剧
    public static final int   MOVIE = 2;  //电影
    public static final int   COMIC = 3;  //动漫
    public static final int   ENT = 4;  //综艺
    public static final int   DOCUMENTARY = 5;  //纪录片

    private String mChannelName = "unknown";

    private int mChannelID = 0;

    public SCChannel(int mChannelID) {
        if(mChannelID >= SHOW && mChannelID <= DOCUMENTARY)
            this.mChannelID = mChannelID;

        if(mChannelID == SHOW)
            mChannelName = "show";
        if(mChannelID == MOVIE)
            mChannelName = "movie";
        if(mChannelID == COMIC)
            mChannelName = "comic";
        if(mChannelID == ENT)
            mChannelName = "entertainment";
        if(mChannelID == DOCUMENTARY)
            mChannelName = "documentary";
        else
            mChannelName = "unknown";

    }

    @Override
    public String toString() {
        return mChannelName;
    }
}
