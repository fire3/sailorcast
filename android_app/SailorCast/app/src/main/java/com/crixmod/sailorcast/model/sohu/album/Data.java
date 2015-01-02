package com.crixmod.sailorcast.model.sohu.album;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Data {

    @Expose
    private Long aid;
    @SerializedName("program_id")
    @Expose
    private Long programId;
    @SerializedName("is_download")
    @Expose
    private Long isDownload;
    @SerializedName("ip_limit")
    @Expose
    private Long ipLimit;
    @SerializedName("mobile_limit")
    @Expose
    private Long mobileLimit;
    @Expose
    private Long fee;
    @SerializedName("fee_month")
    @Expose
    private Long feeMonth;
    @SerializedName("album_name")
    @Expose
    private String albumName;
    @SerializedName("album_desc")
    @Expose
    private String albumDesc;
    @SerializedName("is_original_code")
    @Expose
    private Long isOriginalCode;
    @SerializedName("ver_big_pic")
    @Expose
    private String verBigPic;
    @SerializedName("hor_big_pic")
    @Expose
    private String horBigPic;
    @SerializedName("ver_high_pic")
    @Expose
    private String verHighPic;
    @SerializedName("hor_high_pic")
    @Expose
    private String horHighPic;
    @Expose
    private Long cid;
    @SerializedName("cate_code")
    @Expose
    private String cateCode;
    @SerializedName("second_cate_name")
    @Expose
    private String secondCateName;
    @SerializedName("album_publish_time")
    @Expose
    private String albumPublishTime;
    @SerializedName("is_trailer")
    @Expose
    private Long isTrailer;
    @SerializedName("trailer_aid")
    @Expose
    private Long trailerAid;
    @Expose
    private String director;
    @SerializedName("main_actor")
    @Expose
    private String mainActor;
    @Expose
    private String actor;
    @SerializedName("area_id")
    @Expose
    private Long areaId;
    @Expose
    private String area;
    @Expose
    private String year;
    @SerializedName("total_video_count")
    @Expose
    private Integer totalVideoCount;
    @SerializedName("latest_video_count")
    @Expose
    private Integer latestVideoCount;
    @Expose
    private Double score;
    @SerializedName("play_count")
    @Expose
    private Long playCount;
    @Expose
    private String updateNotification;
    @Expose
    private Long effective;
    @SerializedName("original_album_url")
    @Expose
    private String originalAlbumUrl;
    @Expose
    private Long crid;
    @SerializedName("bg_big_pic")
    @Expose
    private String bgBigPic;
    @SerializedName("hor_w16_pic")
    @Expose
    private String horW16Pic;
    @SerializedName("ver_w12_pic")
    @Expose
    private String verW12Pic;
    @Expose
    private Long season;
    @SerializedName("pay_type")
    @Expose
    private List<Long> payType = new ArrayList<Long>();
    @SerializedName("alias_name")
    @Expose
    private String aliasName;
    @Expose
    private String language;

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
     * The programId
     */
    public Long getProgramId() {
        return programId;
    }

    /**
     *
     * @param programId
     * The program_id
     */
    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    /**
     *
     * @return
     * The isDownload
     */
    public Long getIsDownload() {
        return isDownload;
    }

    /**
     *
     * @param isDownload
     * The is_download
     */
    public void setIsDownload(Long isDownload) {
        this.isDownload = isDownload;
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
     * The mobileLimit
     */
    public Long getMobileLimit() {
        return mobileLimit;
    }

    /**
     *
     * @param mobileLimit
     * The mobile_limit
     */
    public void setMobileLimit(Long mobileLimit) {
        this.mobileLimit = mobileLimit;
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
     * The albumDesc
     */
    public String getAlbumDesc() {
        return albumDesc;
    }

    /**
     *
     * @param albumDesc
     * The album_desc
     */
    public void setAlbumDesc(String albumDesc) {
        this.albumDesc = albumDesc;
    }

    /**
     *
     * @return
     * The isOriginalCode
     */
    public Long getIsOriginalCode() {
        return isOriginalCode;
    }

    /**
     *
     * @param isOriginalCode
     * The is_original_code
     */
    public void setIsOriginalCode(Long isOriginalCode) {
        this.isOriginalCode = isOriginalCode;
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
     * The secondCateName
     */
    public String getSecondCateName() {
        return secondCateName;
    }

    /**
     *
     * @param secondCateName
     * The second_cate_name
     */
    public void setSecondCateName(String secondCateName) {
        this.secondCateName = secondCateName;
    }

    /**
     *
     * @return
     * The albumPublishTime
     */
    public String getAlbumPublishTime() {
        return albumPublishTime;
    }

    /**
     *
     * @param albumPublishTime
     * The album_publish_time
     */
    public void setAlbumPublishTime(String albumPublishTime) {
        this.albumPublishTime = albumPublishTime;
    }

    /**
     *
     * @return
     * The isTrailer
     */
    public Long getIsTrailer() {
        return isTrailer;
    }

    /**
     *
     * @param isTrailer
     * The is_trailer
     */
    public void setIsTrailer(Long isTrailer) {
        this.isTrailer = isTrailer;
    }

    /**
     *
     * @return
     * The trailerAid
     */
    public Long getTrailerAid() {
        return trailerAid;
    }

    /**
     *
     * @param trailerAid
     * The trailer_aid
     */
    public void setTrailerAid(Long trailerAid) {
        this.trailerAid = trailerAid;
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
     * The actor
     */
    public String getActor() {
        return actor;
    }

    /**
     *
     * @param actor
     * The actor
     */
    public void setActor(String actor) {
        this.actor = actor;
    }

    /**
     *
     * @return
     * The areaId
     */
    public Long getAreaId() {
        return areaId;
    }

    /**
     *
     * @param areaId
     * The area_id
     */
    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    /**
     *
     * @return
     * The area
     */
    public String getArea() {
        return area;
    }

    /**
     *
     * @param area
     * The area
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     *
     * @return
     * The year
     */
    public String getYear() {
        return year;
    }

    /**
     *
     * @param year
     * The year
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     *
     * @return
     * The totalVideoCount
     */
    public Integer getTotalVideoCount() {
        return totalVideoCount;
    }

    /**
     *
     * @return
     * The latestVideoCount
     */
    public Integer getLatestVideoCount() {
        return latestVideoCount;
    }


    /**
     *
     * @return
     * The score
     */
    public Double getScore() {
        return score;
    }

    /**
     *
     * @param score
     * The score
     */
    public void setScore(Double score) {
        this.score = score;
    }

    /**
     *
     * @return
     * The playCount
     */
    public Long getPlayCount() {
        return playCount;
    }

    /**
     *
     * @param playCount
     * The play_count
     */
    public void setPlayCount(Long playCount) {
        this.playCount = playCount;
    }

    /**
     *
     * @return
     * The updateNotification
     */
    public String getUpdateNotification() {
        return updateNotification;
    }

    /**
     *
     * @param updateNotification
     * The updateNotification
     */
    public void setUpdateNotification(String updateNotification) {
        this.updateNotification = updateNotification;
    }

    /**
     *
     * @return
     * The effective
     */
    public Long getEffective() {
        return effective;
    }

    /**
     *
     * @param effective
     * The effective
     */
    public void setEffective(Long effective) {
        this.effective = effective;
    }

    /**
     *
     * @return
     * The originalAlbumUrl
     */
    public String getOriginalAlbumUrl() {
        return originalAlbumUrl;
    }

    /**
     *
     * @param originalAlbumUrl
     * The original_album_url
     */
    public void setOriginalAlbumUrl(String originalAlbumUrl) {
        this.originalAlbumUrl = originalAlbumUrl;
    }

    /**
     *
     * @return
     * The crid
     */
    public Long getCrid() {
        return crid;
    }

    /**
     *
     * @param crid
     * The crid
     */
    public void setCrid(Long crid) {
        this.crid = crid;
    }

    /**
     *
     * @return
     * The bgBigPic
     */
    public String getBgBigPic() {
        return bgBigPic;
    }

    /**
     *
     * @param bgBigPic
     * The bg_big_pic
     */
    public void setBgBigPic(String bgBigPic) {
        this.bgBigPic = bgBigPic;
    }

    /**
     *
     * @return
     * The horW16Pic
     */
    public String getHorW16Pic() {
        return horW16Pic;
    }

    /**
     *
     * @param horW16Pic
     * The hor_w16_pic
     */
    public void setHorW16Pic(String horW16Pic) {
        this.horW16Pic = horW16Pic;
    }

    /**
     *
     * @return
     * The verW12Pic
     */
    public String getVerW12Pic() {
        return verW12Pic;
    }

    /**
     *
     * @param verW12Pic
     * The ver_w12_pic
     */
    public void setVerW12Pic(String verW12Pic) {
        this.verW12Pic = verW12Pic;
    }

    /**
     *
     * @return
     * The season
     */
    public Long getSeason() {
        return season;
    }

    /**
     *
     * @param season
     * The season
     */
    public void setSeason(Long season) {
        this.season = season;
    }

    /**
     *
     * @return
     * The payType
     */
    public List<Long> getPayType() {
        return payType;
    }

    /**
     *
     * @param payType
     * The pay_type
     */
    public void setPayType(List<Long> payType) {
        this.payType = payType;
    }

    /**
     *
     * @return
     * The aliasName
     */
    public String getAliasName() {
        return aliasName;
    }

    /**
     *
     * @param aliasName
     * The alias_name
     */
    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    /**
     *
     * @return
     * The language
     */
    public String getLanguage() {
        return language;
    }

    /**
     *
     * @param language
     * The language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

}
