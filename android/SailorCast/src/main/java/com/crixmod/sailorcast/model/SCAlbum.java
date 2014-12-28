package com.crixmod.sailorcast.model;

/**
 * Created by fire3 on 2014/12/26.
 */
public class SCAlbum {
    private String mAlbumId = null;
    private Integer mVideosCount = 0;   /* 已经更新的视频数量 */
    private Integer mVideosTotal = 0;   /* 总共的视频数量 */
    private String mTitle = null;
    private String mSubTitle = null;
    private String mDirector = null;
    private String mMainActor = null;
    private String mVerImageUrl = null;
    private String mHorImageUrl = null;
    private String mDesc = null;
    private SCSite mSite = new SCSite(SCSite.UNKNOWN);
    private String mTip = null;
    private Boolean mIsCompleted = false;  /* 是否完结 */

    private SCVideos scVideos = null;

    public SCAlbum() {
    }

    public String getAlbumId() {
        return mAlbumId;
    }

    public void setAlbumId(String mAlbumId) {
        this.mAlbumId = mAlbumId;
    }

    public Integer getVideosCount() {
        return mVideosCount;
    }

    public void setVideosCount(Integer mVideosCount) {
        this.mVideosCount = mVideosCount;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getSubTitle() {
        return mSubTitle;
    }

    public void setSubTitle(String mSubTitle) {
        this.mSubTitle = mSubTitle;
    }

    public String getDirector() {
        return mDirector;
    }

    public void setDirector(String mDirector) {
        this.mDirector = mDirector;
    }

    public String getMainActor() {
        return mMainActor;
    }

    public void setMainActor(String mMainActor) {
        this.mMainActor = mMainActor;
    }

    public String getVerImageUrl() {
        return mVerImageUrl;
    }

    public void setVerImageUrl(String mVerImageUrl) {
        this.mVerImageUrl = mVerImageUrl;
    }

    public String getHorImageUrl() {
        return mHorImageUrl;
    }

    public void setHorImageUrl(String mHorImageUrl) {
        this.mHorImageUrl = mHorImageUrl;
    }

    public SCSite getSite() {
        return mSite;
    }

    public void setSite(SCSite mSite) {
        this.mSite = mSite;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String mDesc) {
        this.mDesc = mDesc;
    }


    public String getTip() {
        return mTip;
    }

    public void setTip(String mTip) {
        this.mTip = mTip;
    }

    public SCVideos getVideos() {
        return scVideos;
    }

    public void setVideos(SCVideos scVideos) {
        this.scVideos = scVideos;
    }

    public Boolean getIsCompleted() {
        return mIsCompleted;
    }

    public void setIsCompleted(Boolean mIsCompleted) {
        this.mIsCompleted = mIsCompleted;
    }

    public Integer getVideosTotal() {
        return mVideosTotal;
    }

    public void setVideosTotal(Integer mVideosTotal) {
        this.mVideosTotal = mVideosTotal;
    }

    @Override
    public String toString() {
        return "SCAlbum{" +
                "mAlbumId=" + mAlbumId +
                ", mVideosCount=" + mVideosCount +
                ", mTitle='" + mTitle + '\'' +
                ", mSubTitle='" + mSubTitle + '\'' +
                ", mDirector='" + mDirector + '\'' +
                ", mMainActor='" + mMainActor + '\'' +
                ", mVerImageUrl='" + mVerImageUrl + '\'' +
                ", mHorImageUrl='" + mHorImageUrl + '\'' +
                ", mDesc='" + mDesc + '\'' +
                ", mTip='" + mTip + '\'' +
                ", mSite=" + mSite +
                '}';
    }
}
