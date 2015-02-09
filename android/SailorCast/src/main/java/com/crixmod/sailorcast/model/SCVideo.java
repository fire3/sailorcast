package com.crixmod.sailorcast.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.crixmod.sailorcast.SailorCast;

/**
 * Created by fire3 on 2014/12/26.
 */
public class SCVideo implements Parcelable {
    String mVideoID = null;
    String mVideoTitle = null;
    String mM3U8Nor = null;
    String mM3U8High = null;
    String mM3U8Super = null;
    String mHorPic = null;
    String mVerPic = null;
    String mAlbumID = null;
    Integer mSeqInAlbum = 0;  // Start from 1
    String mVideoMID = null; //This is for Letv
    String mIqiyiURL = null; //This is for Iqiyi
    String mIqiyiVid = null; //This is for Iqiyi
    SCSite mSCSite = new SCSite(SCSite.UNKNOWN);

    public final static int QUALITY_SUPER = 1;
    public final static int QUALITY_HIGH = 2;
    public final static int QUALITY_NORMAL = 3;
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


    public String getLetvVideoMID() {
        return mVideoMID;
    }

    public void setLetvVideoMID(String mVideoMID) {
        this.mVideoMID = mVideoMID;
    }

    public String getIqiyiVideoURL() {
        return mIqiyiURL;
    }

    public void setIqiyiVideoURL(String videoURL) {
        this.mIqiyiURL = videoURL;
    }

    public String getIqiyiVid() {
        return mIqiyiVid;
    }

    public void setIqiyiVid(String mIqiyiVid) {
        this.mIqiyiVid = mIqiyiVid;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        /*

    String mVideoID = null;
    String mVideoTitle = null;
    String mM3U8Nor = null;
    String mM3U8High = null;
    String mM3U8Super = null;
    String mHorPic = null;
    String mVerPic = null;
    String mAlbumID = null;
    Integer mSeqInAlbum = 0;
    String mVideoMID = null; //This is for Letv
    SCSite mSCSite = new SCSite(SCSite.UNKNOWN);         */

        parcel.writeString(mVideoID);
        parcel.writeString(mVideoTitle);
        parcel.writeString(mM3U8Nor);
        parcel.writeString(mM3U8High);
        parcel.writeString(mM3U8Super);
        parcel.writeString(mHorPic);
        parcel.writeString(mVerPic);
        parcel.writeString(mAlbumID);
        parcel.writeInt(mSeqInAlbum);
        parcel.writeString(mVideoMID);
        parcel.writeInt(mSCSite.getSiteID());
        parcel.writeString(mIqiyiURL);
        parcel.writeString(mIqiyiVid);

    }
    private SCVideo (Parcel in) {
        this.mVideoID = in.readString();
        this.mVideoTitle = in.readString();
        this.mM3U8Nor = in.readString();
        this.mM3U8High = in.readString();
        this.mM3U8Super = in.readString();
        this.mHorPic = in.readString();
        this.mVerPic = in.readString();
        this.mAlbumID = in.readString();
        this.mSeqInAlbum = in.readInt();
        this.mVideoMID = in.readString();
        this.mSCSite = new SCSite(in.readInt());
        this.mIqiyiURL = in.readString();
        this.mIqiyiVid = in.readString();
    }

    public static final Parcelable.Creator<SCVideo> CREATOR = new Parcelable.Creator<SCVideo>() {

        @Override
        public SCVideo createFromParcel(Parcel source) {
            return new SCVideo(source);
        }

        @Override
        public SCVideo[] newArray(int size) {
            return new SCVideo[size];
        }
    };


    public String toJson() {
        String ret = SailorCast.getGson().toJson(this);
        return ret;
    }

    public static SCVideo fromJson(String json) {
        SCVideo video = SailorCast.getGson().fromJson(json, SCVideo.class);
        return video;
    }


}
