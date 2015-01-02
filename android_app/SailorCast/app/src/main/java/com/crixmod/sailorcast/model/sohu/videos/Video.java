package com.crixmod.sailorcast.model.sohu.videos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Video {
    @SerializedName("video_first_name")
    @Expose
    private String videoFirstName;

    @SerializedName("video_sub_name")
    @Expose
    private String videoSubName;

    @SerializedName("period")
    @Expose
    private String period;


    @SerializedName("url_super")
    @Expose
    private String urlSuper;

    @SerializedName("url_nor")
    @Expose
    private String urlNor;

    @SerializedName("start_time")
    @Expose
    private Long startTime;

    @SerializedName("end_time")
    @Expose
    private Long endTime;

    @SerializedName("create_date")
    @Expose
    private Long createDate;

    @SerializedName("crid")
    @Expose
    private Long crid;

    @SerializedName("tv_id")
    @Expose
    private Long tvId;

    @SerializedName("has_ep")
    @Expose
    private Long hasEp;

    @SerializedName("video_order")
    @Expose
    private Long videoOrder;

    @SerializedName("hor_w16_pic")
    @Expose
    private String horW16Pic;
    @SerializedName("fee_month")
    @Expose
    private Long feeMonth;
    @SerializedName("is_original_code")
    @Expose
    private Long isOriginalCode;
    @SerializedName("second_cate_name")
    @Expose
    private String secondCateName;
    @Expose
    private Float score;
    @Expose
    private Long aid;
    @Expose
    private String tip;
    @SerializedName("time_length")
    @Expose
    private Long timeLength;
    @SerializedName("ver_high_pic")
    @Expose
    private String verHighPic;
    @SerializedName("main_actor")
    @Expose
    private String mainActor;
    @Expose
    private String area;
    @SerializedName("total_video_count")
    @Expose
    private Long totalVideoCount;
    @SerializedName("download_url")
    @Expose
    private String downloadUrl;
    @SerializedName("url_high")
    @Expose
    private String urlHigh;
    @Expose
    private String year;
    @SerializedName("program_id")
    @Expose
    private Long programId;

    @SerializedName("total_duration")
    @Expose
    private Long totalDuration;
    @SerializedName("album_name")
    @Expose
    private String albumName;
    @SerializedName("hor_high_pic")
    @Expose
    private String horHighPic;
    @Expose
    private Long vid;
    @SerializedName("video_big_pic")
    @Expose
    private String videoBigPic;
    @Expose
    private Long site;
    @SerializedName("url_html5")
    @Expose
    private String urlHtml5;
    @SerializedName("hor_big_pic")
    @Expose
    private String horBigPic;
    @SerializedName("play_count")
    @Expose
    private Long playCount;
    @Expose
    private Long mobileLimit;
    @Expose
    private String director;
    @SerializedName("cate_code")
    @Expose
    private String cateCode;
    @Expose
    private Long cid;
    @SerializedName("latest_video_count")
    @Expose
    private Long latestVideoCount;
    @SerializedName("album_desc")
    @Expose
    private String albumDesc;
    @Expose
    private Long fee;
    @SerializedName("ver_big_pic")
    @Expose
    private String verBigPic;
    @Expose
    private Long season;
    @SerializedName("video_name")
    @Expose
    private String videoName;
    @SerializedName("publish_time")
    @Expose
    private String publishTime;
    @SerializedName("ver_w12_pic")
    @Expose
    private String verW12Pic;
    @SerializedName("is_album")
    @Expose
    private Long isAlbum;
    @SerializedName("douban_score")
    @Expose
    private Double doubanScore;
    @SerializedName("show_date")
    @Expose
    private String showDate;
    @SerializedName("recommend_tip")
    @Expose
    private String recommendTip;
    @SerializedName("album_sub_name")
    @Expose
    private String albumSubName;


    @SerializedName("guest")
    @Expose
    private String guest;

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
     * The score
     */
    public Float getScore() {
        return score;
    }

    /**
     *
     * @param score
     * The score
     */
    public void setScore(Float score) {
        this.score = score;
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
     * The timeLength
     */
    public Long getTimeLength() {
        return timeLength;
    }

    /**
     *
     * @param timeLength
     * The time_length
     */
    public void setTimeLength(Long timeLength) {
        this.timeLength = timeLength;
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
     * The totalVideoCount
     */
    public Long getTotalVideoCount() {
        return totalVideoCount;
    }

    /**
     *
     * @param totalVideoCount
     * The total_video_count
     */
    public void setTotalVideoCount(Long totalVideoCount) {
        this.totalVideoCount = totalVideoCount;
    }

    /**
     *
     * @return
     * The downloadUrl
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }

    /**
     *
     * @param downloadUrl
     * The download_url
     */
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    /**
     *
     * @return
     * The urlHigh
     */
    public String getUrlHigh() {
        return urlHigh;
    }

    /**
     *
     * @param urlHigh
     * The url_high
     */
    public void setUrlHigh(String urlHigh) {
        this.urlHigh = urlHigh;
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
     * The videoBigPic
     */
    public String getVideoBigPic() {
        return videoBigPic;
    }

    /**
     *
     * @param videoBigPic
     * The video_big_pic
     */
    public void setVideoBigPic(String videoBigPic) {
        this.videoBigPic = videoBigPic;
    }

    /**
     *
     * @return
     * The site
     */
    public Long getSite() {
        return site;
    }

    /**
     *
     * @param site
     * The site
     */
    public void setSite(Long site) {
        this.site = site;
    }

    /**
     *
     * @return
     * The urlHtml5
     */
    public String getUrlHtml5() {
        return urlHtml5;
    }

    /**
     *
     * @param urlHtml5
     * The url_html5
     */
    public void setUrlHtml5(String urlHtml5) {
        this.urlHtml5 = urlHtml5;
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
     * The latestVideoCount
     */
    public Long getLatestVideoCount() {
        return latestVideoCount;
    }

    /**
     *
     * @param latestVideoCount
     * The latest_video_count
     */
    public void setLatestVideoCount(Long latestVideoCount) {
        this.latestVideoCount = latestVideoCount;
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
     * The videoName
     */
    public String getVideoName() {
        return videoName;
    }

    /**
     *
     * @param videoName
     * The video_name
     */
    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    /**
     *
     * @return
     * The publishTime
     */
    public String getPublishTime() {
        return publishTime;
    }

    /**
     *
     * @param publishTime
     * The publish_time
     */
    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
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
     * The doubanScore
     */
    public Double getDoubanScore() {
        return doubanScore;
    }

    /**
     *
     * @param doubanScore
     * The douban_score
     */
    public void setDoubanScore(Double doubanScore) {
        this.doubanScore = doubanScore;
    }

    /**
     *
     * @return
     * The showDate
     */
    public String getShowDate() {
        return showDate;
    }

    /**
     *
     * @param showDate
     * The show_date
     */
    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    /**
     *
     * @return
     * The recommendTip
     */
    public String getRecommendTip() {
        return recommendTip;
    }

    /**
     *
     * @param recommendTip
     * The recommend_tip
     */
    public void setRecommendTip(String recommendTip) {
        this.recommendTip = recommendTip;
    }

    /**
     *
     * @return
     * The albumSubName
     */
    public String getAlbumSubName() {
        return albumSubName;
    }

    /**
     *
     * @param albumSubName
     * The album_sub_name
     */
    public void setAlbumSubName(String albumSubName) {
        this.albumSubName = albumSubName;
    }

    public Long getTotalDuration() {
        return totalDuration;
    }

    public String getVideoFirstName() {
        return videoFirstName;
    }

    public String getUrlSuper() {
        return urlSuper;
    }

    public String getUrlNor() {
        return urlNor;
    }
    public Long getStartTime() {
        return startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public Long getCrid() {
        return crid;
    }

    public Long getTvId() {
        return tvId;
    }

    public Long getVideoOrder() {

        return videoOrder;
    }

    public String getVideoSubName() {
        return videoSubName;
    }

    public String getPeriod() {
        return period;
    }

    public String getGuest() {
        return guest;
    }
}

