package com.crixmod.sailorcast.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.crixmod.sailorcast.SailorCast;

/**
 * Created by fire3 on 2014/12/26.
 */
public class SCAlbum implements Parcelable {
    private String mAlbumId = null;
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
    private String mLetvStyle = null;  /* Letv需要的字段, 其它站点不需要， 界面不显示 */

    public SCAlbum(int siteID) {
        this.mSite = new SCSite(siteID);
    }

    public String getAlbumId() {
        return mAlbumId;
    }

    public void setAlbumId(String mAlbumId) {
        this.mAlbumId = mAlbumId;
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

    public void setSite(int siteID) {
        this.mSite = new SCSite(siteID);
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

    public String getLetvStyle() {
        return mLetvStyle;
    }

    public void setLetvStyle(String mLetvStyle) {
        this.mLetvStyle = mLetvStyle;
    }

    @Override
    public String toString() {
        return "SCAlbum{" +
                "mAlbumId='" + mAlbumId + '\'' +
                ", mVideosTotal=" + mVideosTotal +
                ", mTitle='" + mTitle + '\'' +
                ", mSubTitle='" + mSubTitle + '\'' +
                ", mDirector='" + mDirector + '\'' +
                ", mMainActor='" + mMainActor + '\'' +
                ", mVerImageUrl='" + mVerImageUrl + '\'' +
                ", mHorImageUrl='" + mHorImageUrl + '\'' +
                ", mDesc='" + mDesc + '\'' +
                ", mSite=" + mSite +
                ", mTip='" + mTip + '\'' +
                ", mIsCompleted=" + mIsCompleted +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(mAlbumId);
        parcel.writeInt(mVideosTotal);
        parcel.writeString(mTitle);
        parcel.writeString(mSubTitle);
        parcel.writeString(mDirector);
        parcel.writeString(mMainActor);
        parcel.writeString(mVerImageUrl);
        parcel.writeString(mHorImageUrl);
        parcel.writeString(mDesc);
        parcel.writeInt(mSite.getSiteID());
        parcel.writeString(mTip);
        parcel.writeByte((byte) (mIsCompleted ? 1 : 0)); //myBoolean = in.readByte() != 0;
        parcel.writeString(mLetvStyle);

    }

    private SCAlbum (Parcel in) {
        this.mAlbumId = in.readString();
        this.mVideosTotal = in.readInt();
        this.mTitle = in.readString();
        this.mSubTitle = in.readString();
        this.mDirector = in.readString();
        this.mMainActor = in.readString();
        this.mVerImageUrl = in.readString();
        this.mHorImageUrl = in.readString();
        this.mDesc = in.readString();
        this.mSite = new SCSite(in.readInt());
        this.mTip = in.readString();
        this.mIsCompleted = in.readByte() != 0;
        this.mLetvStyle = in.readString();
    }

    public static final Parcelable.Creator<SCAlbum> CREATOR = new Parcelable.Creator<SCAlbum>() {

        @Override
        public SCAlbum createFromParcel(Parcel source) {
            return new SCAlbum(source);
        }

        @Override
        public SCAlbum[] newArray(int size) {
            return new SCAlbum[size];
        }
    };

    public String toJson() {
        String ret = SailorCast.getGson().toJson(this);
        return ret;
    }

    public static SCAlbum fromJson(String json) {
        SCAlbum album  = SailorCast.getGson().fromJson(json,SCAlbum.class);
        return album;
    }
}
