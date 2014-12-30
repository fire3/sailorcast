package com.crixmod.sailorcast.model.sohu.searchresult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SearchResultAlbum {

    @SerializedName("hor_big_pic")
    @Expose
    private String horBigPic;
    @SerializedName("fee_month")
    @Expose
    private Long feeMonth;
    @SerializedName("ip_limit")
    @Expose
    private Long ipLimit;
    @SerializedName("tv_desc")
    @Expose
    private String tvDesc;
    @Expose
    private Long aid;
    @Expose
    private Long mobileLimit;
    @SerializedName("cate_code")
    @Expose
    private String cateCode;
    @Expose
    private String director;
    @Expose
    private Long cid;
    @Expose
    private String tip;
    @Expose
    private Long fee;
    @SerializedName("ver_big_pic")
    @Expose
    private String verBigPic;
    @SerializedName("main_actor")
    @Expose
    private String mainActor;
    @SerializedName("ver_high_pic")
    @Expose
    private String verHighPic;
    @SerializedName("tv_name")
    @Expose
    private String tvName;
    @SerializedName("video_list")
    @Expose
    private List<VideoList> videoList = new ArrayList<VideoList>();
    @SerializedName("is_album")
    @Expose
    private Long isAlbum;
    @SerializedName("album_name")
    @Expose
    private String albumName;
    @Expose
    private Long vid;
    @SerializedName("hor_high_pic")
    @Expose
    private String horHighPic;

    /**
     *
     * @return
     * The horBigPic
     */
    public String getHorBigPic() {
        return horBigPic;
    }

    /**
     *
     * @param horBigPic
     * The hor_big_pic
     */
    public void setHorBigPic(String horBigPic) {
        this.horBigPic = horBigPic;
    }

    /**
     *
     * @return
     * The feeMonth
     */
    public Long getFeeMonth() {
        return feeMonth;
    }

    /**
     *
     * @param feeMonth
     * The fee_month
     */
    public void setFeeMonth(Long feeMonth) {
        this.feeMonth = feeMonth;
    }

    /**
     *
     * @return
     * The ipLimit
     */
    public Long getIpLimit() {
        return ipLimit;
    }

    /**
     *
     * @param ipLimit
     * The ip_limit
     */
    public void setIpLimit(Long ipLimit) {
        this.ipLimit = ipLimit;
    }

    /**
     *
     * @return
     * The tvDesc
     */
    public String getTvDesc() {
        return tvDesc;
    }

    /**
     *
     * @param tvDesc
     * The tv_desc
     */
    public void setTvDesc(String tvDesc) {
        this.tvDesc = tvDesc;
    }

    /**
     *
     * @return
     * The aid
     */
    public Long getAid() {
        return aid;
    }

    /**
     *
     * @param aid
     * The aid
     */
    public void setAid(Long aid) {
        this.aid = aid;
    }

    /**
     *
     * @return
     * The mobileLimit
     */
    public Long getMobileLimit() {
        return mobileLimit;
    }

    /**
     *
     * @param mobileLimit
     * The mobileLimit
     */
    public void setMobileLimit(Long mobileLimit) {
        this.mobileLimit = mobileLimit;
    }

    /**
     *
     * @return
     * The cateCode
     */
    public String getCateCode() {
        return cateCode;
    }

    /**
     *
     * @param cateCode
     * The cate_code
     */
    public void setCateCode(String cateCode) {
        this.cateCode = cateCode;
    }

    /**
     *
     * @return
     * The director
     */
    public String getDirector() {
        return director;
    }

    /**
     *
     * @param director
     * The director
     */
    public void setDirector(String director) {
        this.director = director;
    }

    /**
     *
     * @return
     * The cid
     */
    public Long getCid() {
        return cid;
    }

    /**
     *
     * @param cid
     * The cid
     */
    public void setCid(Long cid) {
        this.cid = cid;
    }

    /**
     *
     * @return
     * The tip
     */
    public String getTip() {
        return tip;
    }

    /**
     *
     * @param tip
     * The tip
     */
    public void setTip(String tip) {
        this.tip = tip;
    }

    /**
     *
     * @return
     * The fee
     */
    public Long getFee() {
        return fee;
    }

    /**
     *
     * @param fee
     * The fee
     */
    public void setFee(Long fee) {
        this.fee = fee;
    }

    /**
     *
     * @return
     * The verBigPic
     */
    public String getVerBigPic() {
        return verBigPic;
    }

    /**
     *
     * @param verBigPic
     * The ver_big_pic
     */
    public void setVerBigPic(String verBigPic) {
        this.verBigPic = verBigPic;
    }

    /**
     *
     * @return
     * The mainActor
     */
    public String getMainActor() {
        return mainActor;
    }

    /**
     *
     * @param mainActor
     * The main_actor
     */
    public void setMainActor(String mainActor) {
        this.mainActor = mainActor;
    }

    /**
     *
     * @return
     * The verHighPic
     */
    public String getVerHighPic() {
        return verHighPic;
    }

    /**
     *
     * @param verHighPic
     * The ver_high_pic
     */
    public void setVerHighPic(String verHighPic) {
        this.verHighPic = verHighPic;
    }

    /**
     *
     * @return
     * The tvName
     */
    public String getTvName() {
        return tvName;
    }

    /**
     *
     * @param tvName
     * The tv_name
     */
    public void setTvName(String tvName) {
        this.tvName = tvName;
    }

    /**
     *
     * @return
     * The videoList
     */
    public List<VideoList> getVideoList() {
        return videoList;
    }

    /**
     *
     * @param videoList
     * The video_list
     */
    public void setVideoList(List<VideoList> videoList) {
        this.videoList = videoList;
    }

    /**
     *
     * @return
     * The isAlbum
     */
    public Long getIsAlbum() {
        return isAlbum;
    }

    /**
     *
     * @param isAlbum
     * The is_album
     */
    public void setIsAlbum(Long isAlbum) {
        this.isAlbum = isAlbum;
    }

    /**
     *
     * @return
     * The albumName
     */
    public String getAlbumName() {
        return albumName;
    }

    /**
     *
     * @param albumName
     * The album_name
     */
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    /**
     *
     * @return
     * The vid
     */
    public Long getVid() {
        return vid;
    }

    /**
     *
     * @param vid
     * The vid
     */
    public void setVid(Long vid) {
        this.vid = vid;
    }

    /**
     *
     * @return
     * The horHighPic
     */
    public String getHorHighPic() {
        return horHighPic;
    }

    /**
     *
     * @param horHighPic
     * The hor_high_pic
     */
    public void setHorHighPic(String horHighPic) {
        this.horHighPic = horHighPic;
    }

}
