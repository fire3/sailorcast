package com.crixmod.sailorcast.model;

/**
 * Created by fire3 on 2015/1/19.
 */
public class SCChannel {

    public static final int   UNKNOWN = -1;  //未知
    public static final int   SHOW = 1;  //电视剧
    public static final int   MOVIE = 2;  //电影
    public static final int   COMIC = 3;  //动漫
    public static final int   ENT = 4;  //娱乐
    public static final int   DOCUMENTARY = 5;  //纪录片
    public static final int   SPORT = 6;  //体育
    public static final int   MUSIC = 7;  //音乐
    public static final int   VARIETY = 8;  //综艺

    private String mChannelName = "unknown";

    private int mChannelID = UNKNOWN;

    public SCChannel(int mChannelID) {
        if(mChannelID >= SHOW && mChannelID <= VARIETY)
            this.mChannelID = mChannelID;

        if(mChannelID == SHOW)
            mChannelName = "show";
        else if(mChannelID == MOVIE)
            mChannelName = "movie";
        else if(mChannelID == COMIC)
            mChannelName = "comic";
        else if(mChannelID == ENT)
            mChannelName = "entertainment";
        else if(mChannelID == DOCUMENTARY)
            mChannelName = "documentary";
        else if(mChannelID == SPORT)
            mChannelName = "sport";
        else if(mChannelID == MUSIC)
            mChannelName = "music";
        else if(mChannelID == VARIETY)
            mChannelName = "variety";
        else
            mChannelName = "unknown";

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
}
