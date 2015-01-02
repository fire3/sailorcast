package com.crixmod.sailorcast.model.sohu.videos;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Data {

    @Expose
    private Integer count;
    @Expose
    private List<Video> videos = new ArrayList<Video>();

    /**
     *
     * @return
     * The count
     */
    public Integer getCount() {
        return count;
    }

    /**
     *
     * @param count
     * The count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     *
     * @return
     * The videos
     */
    public List<Video> getVideos() {
        return videos;
    }

    /**
     *
     * @param videos
     * The videos
     */
    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

}
