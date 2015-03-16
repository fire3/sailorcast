package com.crixmod.sailorcast.model;

import com.crixmod.sailorcast.SailorCast;

import java.util.ArrayList;

/**
 * Created by fire3 on 15-3-9.
 */
public class SCLiveStream {
    private String mChannelID;
    private String mChannelName;
    private String mChannelEName; //For Letv
    private String mStreamSuperID;
    private String mStreamSuperURL;
    private String mStreamHighID;
    private String mStreamHighURL;
    private String mStreamNorID;
    private String mStreamNorURL;
    private String mHorPic;
    private String mCurrentPlayTitle;
    private String mNexPlayTitle;
    private String mNextPlayStartTime;
    private SCWeekDays mSCWeekDays = new SCWeekDays();

    public SCWeekDays getWeekDays() {
        return mSCWeekDays;
    }

    public void addWeekDay(String weekDayName, String weekDayId) {
        SCWeekDay SCWeekDay = new SCWeekDay(weekDayName,weekDayId);
        mSCWeekDays.add(SCWeekDay);
    }


    public String getChannelID() {
        return mChannelID;
    }

    public void setChannelID(String mChannelID) {
        this.mChannelID = mChannelID;
    }

    public String getChannelEName() {
        return mChannelEName;
    }

    public void setChannelEName(String mChannelEName) {
        this.mChannelEName = mChannelEName;
    }

    public String getChannelName() {
        return mChannelName;
    }

    public void setChannelName(String mChannelName) {
        this.mChannelName = mChannelName;
    }

    public String getStreamSuperID() {
        return mStreamSuperID;
    }

    public void setStreamSuperID(String mStreamSuperID) {
        this.mStreamSuperID = mStreamSuperID;
    }

    public String getStreamSuperURL() {
        return mStreamSuperURL;
    }

    public void setStreamSuperURL(String mStreamSuperURL) {
        this.mStreamSuperURL = mStreamSuperURL;
    }

    public String getStreamHighID() {
        return mStreamHighID;
    }

    public void setStreamHighID(String mStreamHighID) {
        this.mStreamHighID = mStreamHighID;
    }

    public String getStreamHighURL() {
        return mStreamHighURL;
    }

    public void setStreamHighURL(String mStreamHighURL) {
        this.mStreamHighURL = mStreamHighURL;
    }

    public String getStreamNorID() {
        return mStreamNorID;
    }

    public void setStreamNorID(String mStreamNorID) {
        this.mStreamNorID = mStreamNorID;
    }

    public String getStreamNorURL() {
        return mStreamNorURL;
    }

    public void setStreamNorURL(String mStreamNorURL) {
        this.mStreamNorURL = mStreamNorURL;
    }

    public String getHorPic() {
        return mHorPic;
    }

    public void setHorPic(String mHorPic) {
        this.mHorPic = mHorPic;
    }

    public String getCurrentPlayTitle() {
        return mCurrentPlayTitle;
    }

    public void setCurrentPlayTitle(String mCurrentPlayTitle) {
        this.mCurrentPlayTitle = mCurrentPlayTitle;
    }

    public String getNexPlayTitle() {
        return mNexPlayTitle;
    }

    public void setNexPlayTitle(String mNexPlayTitle) {
        this.mNexPlayTitle = mNexPlayTitle;
    }

    public String getNextPlayStartTime() {
        return mNextPlayStartTime;
    }

    public void setNextPlayStartTime(String mNextPlayStartTime) {
        this.mNextPlayStartTime = mNextPlayStartTime;
    }

    @Override
    public String toString() {
        return "SCLiveStream{" +
                "mChannelID='" + mChannelID + '\'' +
                ", mChannelName='" + mChannelName + '\'' +
                ", mStreamSuperID='" + mStreamSuperID + '\'' +
                ", mStreamSuperURL='" + mStreamSuperURL + '\'' +
                ", mStreamHighID='" + mStreamHighID + '\'' +
                ", mStreamHighURL='" + mStreamHighURL + '\'' +
                ", mStreamNorID='" + mStreamNorID + '\'' +
                ", mStreamNorURL='" + mStreamNorURL + '\'' +
                ", mHorPic='" + mHorPic + '\'' +
                ", mCurrentPlayTitle='" + mCurrentPlayTitle + '\'' +
                ", mNexPlayTitle='" + mNexPlayTitle + '\'' +
                ", mNextPlayStartTime='" + mNextPlayStartTime + '\'' +
                '}';
    }


    public String toJson() {
        String ret = SailorCast.getGson().toJson(this);
        return ret;
    }

    public static SCLiveStream fromJson(String json) {
        SCLiveStream stream  = SailorCast.getGson().fromJson(json,SCLiveStream.class);
        return stream;
    }
}
