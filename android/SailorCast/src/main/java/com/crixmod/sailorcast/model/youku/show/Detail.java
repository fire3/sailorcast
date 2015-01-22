package com.crixmod.sailorcast.model.youku.show;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detail {

    @SerializedName("img_type")
    @Expose
    private String imgType;
    @Expose
    private String showid;
    @Expose
    private List<String> performer = new ArrayList<String>();
    @SerializedName("episode_total")
    @Expose
    private Integer episodeTotal;
    @SerializedName("show_videotype")
    @Expose
    private String showVideotype;
    @SerializedName("total_comment")
    @Expose
    private String totalComment;
    @Expose
    private String img;
    @Expose
    private List<String> area = new ArrayList<String>();
    @Expose
    private String videoid;
    @Expose
    private Long videosize;
    @Expose
    private String cats;
    @SerializedName("img_default")
    @Expose
    private String imgDefault;
    @SerializedName("showtotal_search")
    @Expose
    private String showtotalSearch;
    @SerializedName("format_flag")
    @Expose
    private Long formatFlag;
    @Expose
    private Long completed;
    @Expose
    private List<String> director = new ArrayList<String>();
    @SerializedName("tag_type")
    @Expose
    private String tagType;
    @SerializedName("total_vv")
    @Expose
    private String totalVv;
    @Expose
    private String desc;
    @SerializedName("pk_odshow")
    @Expose
    private String pkOdshow;
    @SerializedName("stripe_bottom")
    @Expose
    private String stripeBottom;
    @Expose
    private String title;
    @SerializedName("total_fav")
    @Expose
    private String totalFav;
    @Expose
    private String reputation;
    @Expose
    private Long limit;
    @SerializedName("cats_id")
    @Expose
    private Long catsId;
    @SerializedName("douban_rating")
    @Expose
    private String doubanRating;
    @Expose
    private String showdate;

    /**
     *
     * @return
     * The imgType
     */
    public String getImgType() {
        return imgType;
    }

    /**
     *
     * @param imgType
     * The img_type
     */
    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    /**
     *
     * @return
     * The showid
     */
    public String getShowid() {
        return showid;
    }

    /**
     *
     * @param showid
     * The showid
     */
    public void setShowid(String showid) {
        this.showid = showid;
    }

    /**
     *
     * @return
     * The performer
     */
    public List<String> getPerformer() {
        return performer;
    }

    /**
     *
     * @param performer
     * The performer
     */
    public void setPerformer(List<String> performer) {
        this.performer = performer;
    }

    /**
     *
     * @return
     * The episodeTotal
     */
    public Integer getEpisodeTotal() {
        return episodeTotal;
    }

    /**
     *
     * @param episodeTotal
     * The episode_total
     */
    public void setEpisodeTotal(Integer episodeTotal) {
        this.episodeTotal = episodeTotal;
    }

    /**
     *
     * @return
     * The showVideotype
     */
    public String getShowVideotype() {
        return showVideotype;
    }

    /**
     *
     * @param showVideotype
     * The show_videotype
     */
    public void setShowVideotype(String showVideotype) {
        this.showVideotype = showVideotype;
    }

    /**
     *
     * @return
     * The totalComment
     */
    public String getTotalComment() {
        return totalComment;
    }

    /**
     *
     * @param totalComment
     * The total_comment
     */
    public void setTotalComment(String totalComment) {
        this.totalComment = totalComment;
    }

    /**
     *
     * @return
     * The img
     */
    public String getImg() {
        return img;
    }

    /**
     *
     * @param img
     * The img
     */
    public void setImg(String img) {
        this.img = img;
    }

    /**
     *
     * @return
     * The area
     */
    public List<String> getArea() {
        return area;
    }

    /**
     *
     * @param area
     * The area
     */
    public void setArea(List<String> area) {
        this.area = area;
    }

    /**
     *
     * @return
     * The videoid
     */
    public String getVideoid() {
        return videoid;
    }

    /**
     *
     * @param videoid
     * The videoid
     */
    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }

    /**
     *
     * @return
     * The videosize
     */
    public Long getVideosize() {
        return videosize;
    }

    /**
     *
     * @param videosize
     * The videosize
     */
    public void setVideosize(Long videosize) {
        this.videosize = videosize;
    }

    /**
     *
     * @return
     * The cats
     */
    public String getCats() {
        return cats;
    }

    /**
     *
     * @param cats
     * The cats
     */
    public void setCats(String cats) {
        this.cats = cats;
    }

    /**
     *
     * @return
     * The imgDefault
     */
    public String getImgDefault() {
        return imgDefault;
    }

    /**
     *
     * @param imgDefault
     * The img_default
     */
    public void setImgDefault(String imgDefault) {
        this.imgDefault = imgDefault;
    }


    /**
     *
     * @return
     * The showtotalSearch
     */
    public String getShowtotalSearch() {
        return showtotalSearch;
    }

    /**
     *
     * @param showtotalSearch
     * The showtotal_search
     */
    public void setShowtotalSearch(String showtotalSearch) {
        this.showtotalSearch = showtotalSearch;
    }

    /**
     *
     * @return
     * The formatFlag
     */
    public Long getFormatFlag() {
        return formatFlag;
    }

    /**
     *
     * @param formatFlag
     * The format_flag
     */
    public void setFormatFlag(Long formatFlag) {
        this.formatFlag = formatFlag;
    }

    /**
     *
     * @return
     * The completed
     */
    public Long getCompleted() {
        return completed;
    }

    /**
     *
     * @param completed
     * The completed
     */
    public void setCompleted(Long completed) {
        this.completed = completed;
    }

    /**
     *
     * @return
     * The director
     */
    public List<String> getDirector() {
        return director;
    }

    /**
     *
     * @param director
     * The director
     */
    public void setDirector(List<String> director) {
        this.director = director;
    }

    /**
     *
     * @return
     * The tagType
     */
    public String getTagType() {
        return tagType;
    }

    /**
     *
     * @param tagType
     * The tag_type
     */
    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    /**
     *
     * @return
     * The totalVv
     */
    public String getTotalVv() {
        return totalVv;
    }

    /**
     *
     * @param totalVv
     * The total_vv
     */
    public void setTotalVv(String totalVv) {
        this.totalVv = totalVv;
    }

    /**
     *
     * @return
     * The desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     *
     * @param desc
     * The desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     *
     * @return
     * The pkOdshow
     */
    public String getPkOdshow() {
        return pkOdshow;
    }

    /**
     *
     * @param pkOdshow
     * The pk_odshow
     */
    public void setPkOdshow(String pkOdshow) {
        this.pkOdshow = pkOdshow;
    }

    /**
     *
     * @return
     * The stripeBottom
     */
    public String getStripeBottom() {
        return stripeBottom;
    }

    /**
     *
     * @param stripeBottom
     * The stripe_bottom
     */
    public void setStripeBottom(String stripeBottom) {
        this.stripeBottom = stripeBottom;
    }

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The totalFav
     */
    public String getTotalFav() {
        return totalFav;
    }

    /**
     *
     * @param totalFav
     * The total_fav
     */
    public void setTotalFav(String totalFav) {
        this.totalFav = totalFav;
    }

    /**
     *
     * @return
     * The reputation
     */
    public String getReputation() {
        return reputation;
    }

    /**
     *
     * @param reputation
     * The reputation
     */
    public void setReputation(String reputation) {
        this.reputation = reputation;
    }

    /**
     *
     * @return
     * The limit
     */
    public Long getLimit() {
        return limit;
    }

    /**
     *
     * @param limit
     * The limit
     */
    public void setLimit(Long limit) {
        this.limit = limit;
    }

    /**
     *
     * @return
     * The catsId
     */
    public Long getCatsId() {
        return catsId;
    }

    /**
     *
     * @param catsId
     * The cats_id
     */
    public void setCatsId(Long catsId) {
        this.catsId = catsId;
    }

    /**
     *
     * @return
     * The doubanRating
     */
    public String getDoubanRating() {
        return doubanRating;
    }

    /**
     *
     * @param doubanRating
     * The douban_rating
     */
    public void setDoubanRating(String doubanRating) {
        this.doubanRating = doubanRating;
    }

    /**
     *
     * @return
     * The showdate
     */
    public String getShowdate() {
        return showdate;
    }

    /**
     *
     * @param showdate
     * The showdate
     */
    public void setShowdate(String showdate) {
        this.showdate = showdate;
    }

}
