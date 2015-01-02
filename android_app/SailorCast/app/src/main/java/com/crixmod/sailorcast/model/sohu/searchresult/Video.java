package com.crixmod.sailorcast.model.sohu.searchresult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Video {

    @Expose
    private Long site;
    @SerializedName("url_html5")
    @Expose
    private String urlHtml5;
    @Expose
    private Long vid;

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

}
