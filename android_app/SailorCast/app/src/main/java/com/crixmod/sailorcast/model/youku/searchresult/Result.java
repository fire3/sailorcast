package com.crixmod.sailorcast.model.youku.searchresult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Result {

    @Expose
    private String showid;
    @Expose
    private Long sequence;
    @SerializedName("episode_total")
    @Expose
    private Long episodeTotal;
    @Expose
    private Long completed;
    @Expose
    private String notice;
    @Expose
    private List<String> genre = new ArrayList<String>();
    @SerializedName("stripe_bottom_no_status")
    @Expose
    private String stripeBottomNoStatus;
    @SerializedName("show_thumburl")
    @Expose
    private String showThumburl;
    @SerializedName("is_youku")
    @Expose
    private Long isYouku;
    @Expose
    private String showname;

    @SerializedName("stripe_bottom")
    @Expose
    private String stripeBottom;
    @SerializedName("show_vthumburl")
    @Expose
    private String showVthumburl;
    @Expose
    private String summary;
    @Expose
    private String cats;

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
     * The sequence
     */
    public Long getSequence() {
        return sequence;
    }

    /**
     *
     * @param sequence
     * The sequence
     */
    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    /**
     *
     * @return
     * The episodeTotal
     */
    public Long getEpisodeTotal() {
        return episodeTotal;
    }

    /**
     *
     * @param episodeTotal
     * The episode_total
     */
    public void setEpisodeTotal(Long episodeTotal) {
        this.episodeTotal = episodeTotal;
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
     * The notice
     */
    public String getNotice() {
        return notice;
    }

    /**
     *
     * @param notice
     * The notice
     */
    public void setNotice(String notice) {
        this.notice = notice;
    }

    /**
     *
     * @return
     * The genre
     */
    public List<String> getGenre() {
        return genre;
    }

    /**
     *
     * @param genre
     * The genre
     */
    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    /**
     *
     * @return
     * The stripeBottomNoStatus
     */
    public String getStripeBottomNoStatus() {
        return stripeBottomNoStatus;
    }

    /**
     *
     * @param stripeBottomNoStatus
     * The stripe_bottom_no_status
     */
    public void setStripeBottomNoStatus(String stripeBottomNoStatus) {
        this.stripeBottomNoStatus = stripeBottomNoStatus;
    }

    /**
     *
     * @return
     * The showThumburl
     */
    public String getShowThumburl() {
        return showThumburl;
    }

    /**
     *
     * @param showThumburl
     * The show_thumburl
     */
    public void setShowThumburl(String showThumburl) {
        this.showThumburl = showThumburl;
    }

    /**
     *
     * @return
     * The isYouku
     */
    public Long getIsYouku() {
        return isYouku;
    }

    /**
     *
     * @param isYouku
     * The is_youku
     */
    public void setIsYouku(Long isYouku) {
        this.isYouku = isYouku;
    }

    /**
     *
     * @return
     * The showname
     */
    public String getShowname() {
        return showname;
    }

    /**
     *
     * @param showname
     * The showname
     */
    public void setShowname(String showname) {
        this.showname = showname;
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
     * The showVthumburl
     */
    public String getShowVthumburl() {
        return showVthumburl;
    }

    /**
     *
     * @param showVthumburl
     * The show_vthumburl
     */
    public void setShowVthumburl(String showVthumburl) {
        this.showVthumburl = showVthumburl;
    }

    /**
     *
     * @return
     * The summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     *
     * @param summary
     * The summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
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

}
