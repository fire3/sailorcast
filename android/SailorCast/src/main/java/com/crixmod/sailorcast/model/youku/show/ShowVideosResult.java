package com.crixmod.sailorcast.model.youku.show;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShowVideosResult {

    @Expose
    private List<String> streamtypes = new ArrayList<String>();
    @Expose
    private String videoid;
    @SerializedName("show_videoseq")
    @Expose
    private Integer showVideoseq;

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("img")
    private String img;
    /**
     *
     * @return
     * The streamtypes
     */
    public List<String> getStreamtypes() {
        return streamtypes;
    }

    /**
     *
     * @param streamtypes
     * The streamtypes
     */
    public void setStreamtypes(List<String> streamtypes) {
        this.streamtypes = streamtypes;
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
     * The showVideoseq
     */
    public Integer getShowVideoseq() {
        return showVideoseq;
    }

    /**
     *
     * @param showVideoseq
     * The show_videoseq
     */
    public void setShowVideoseq(Integer showVideoseq) {
        this.showVideoseq = showVideoseq;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
