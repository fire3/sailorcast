package com.crixmod.sailorcast.model;

/**
 * Created by fire3 on 2014/12/26.
 */
public class SCSite {
    public static int UNKNOWN = -1;
    public static int SOHU = 0;
    public static int YOUKU = 1;
    public static int LETV = 2;
    public static int IQIYI = 3;
    private String mSiteName;

    private int mSiteID;

    public SCSite(int mSiteID) {
        this.mSiteID = mSiteID;
        if(mSiteID == SOHU)
            mSiteName = "Sohu";
        if(mSiteID == YOUKU)
            mSiteName = "Youku";
        if(mSiteID == IQIYI)
            mSiteName = "IQiyi";
        if(mSiteID == LETV)
            mSiteName = "Letv";
        if(mSiteID == UNKNOWN)
            mSiteName = "Unknown";
    }

    public int getSiteID() {
        return mSiteID;
    }
    @Override
    public String toString() {
        return mSiteName;
    }
}
