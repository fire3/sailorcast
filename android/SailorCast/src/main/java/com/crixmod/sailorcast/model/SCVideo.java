package com.crixmod.sailorcast.model;

/**
 * Created by fire3 on 2014/12/26.
 */
public class SCVideo {
    String mVideoID = null;
    String mVideoTitle = null;
    String mM3U8Nor = null;
    String mM3U8High = null;
    String mM3U8Super = null;
    String mHorPic = null;
    String mVerPic = null;
    String mAlbumID = null;
    Integer mSeqInAlbum = 0;
    SCSite mSCSite = new SCSite(SCSite.UNKNOWN);
    public SCVideo() {
    }

    public String getVideoID() {
        return mVideoID;
    }

    public void setVideoID(String mVideoID) {
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

    public Integer getSeqInAlbum() {
        return mSeqInAlbum;
    }

    public void setSeqInAlbum(Integer mSeqInAlbum) {
        this.mSeqInAlbum = mSeqInAlbum;
    }

    public String getAlbumID() {
        return mAlbumID;
    }

    public void setAlbumID(String mAlbumID) {
        this.mAlbumID = mAlbumID;
    }

    public SCSite getSite() {
        return mSCSite;
    }

    public void setSCSite(int siteID) {
        this.mSCSite = new SCSite(siteID);
    }

    @Override
    public String toString() {
        return "SCVideo{" +
                "mVideoID='" + mVideoID + '\'' +
                ", mVideoTitle='" + mVideoTitle + '\'' +
                ", mM3U8Nor='" + mM3U8Nor + '\'' +
                ", mM3U8High='" + mM3U8High + '\'' +
                ", mM3U8Super='" + mM3U8Super + '\'' +
                ", mHorPic='" + mHorPic + '\'' +
                ", mVerPic='" + mVerPic + '\'' +
                ", mAlbumID='" + mAlbumID + '\'' +
                ", mSeqInAlbum=" + mSeqInAlbum +
                ", mSCSite=" + mSCSite +
                '}';
    }
}
