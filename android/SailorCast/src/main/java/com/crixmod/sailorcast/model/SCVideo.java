package com.crixmod.sailorcast.model;

/**
 * Created by fire3 on 2014/12/26.
 */
public class SCVideo {
    Integer mVideoID = 0;
    String mVideoTitle = null;
    String mM3U8Nor = null;
    String mM3U8High = null;
    String mM3U8Super = null;
    String mActor = null;
    String mDirector = null;
    String mHorPic = null;
    String mVerPic = null;

    public SCVideo() {
    }

    public Integer getVideoID() {
        return mVideoID;
    }

    public void setVideoID(Integer mVideoID) {
        this.mVideoID = mVideoID;
    }

    public String getVideoTitle() {
        return mVideoTitle;
    }

    public void setVideoTitle(String mVideoTitle) {
        this.mVideoTitle = mVideoTitle;
    }

    public String getM3U8Nor() {
        return mM3U8Nor;
    }

    public void setM3U8Nor(String mM3U8Nor) {
        this.mM3U8Nor = mM3U8Nor;
    }

    public String getM3U8High() {
        return mM3U8High;
    }

    public void setM3U8High(String mM3U8High) {
        this.mM3U8High = mM3U8High;
    }

    public String getM3U8Super() {
        return mM3U8Super;
    }

    public void setM3U8Super(String mM3U8Super) {
        this.mM3U8Super = mM3U8Super;
    }

    public String getActor() {
        return mActor;
    }

    public void setActor(String mActor) {
        this.mActor = mActor;
    }

    public String getDirector() {
        return mDirector;
    }

    public void setDirector(String mDirector) {
        this.mDirector = mDirector;
    }

    public String getHorPic() {
        return mHorPic;
    }

    public void setHorPic(String mHorPic) {
        this.mHorPic = mHorPic;
    }

    public String getVerPic() {
        return mVerPic;
    }

    public void setVerPic(String mVerPic) {
        this.mVerPic = mVerPic;
    }

    @Override
    public String toString() {
        return "SCVideo{" +
                "mVideoID=" + mVideoID +
                ", mVideoTitle='" + mVideoTitle + '\'' +
                ", mM3U8Nor='" + mM3U8Nor + '\'' +
                ", mM3U8High='" + mM3U8High + '\'' +
                ", mM3U8Super='" + mM3U8Super + '\'' +
                ", mActor='" + mActor + '\'' +
                ", mDirector='" + mDirector + '\'' +
                ", mHorPic='" + mHorPic + '\'' +
                ", mVerPic='" + mVerPic + '\'' +
                '}';
    }
}
