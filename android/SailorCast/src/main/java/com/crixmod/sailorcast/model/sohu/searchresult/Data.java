package com.crixmod.sailorcast.model.sohu.searchresult;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Data {

    @Expose
    private Long count;
    @Expose
    private List<Album> albums = new ArrayList<Album>();

    /**
     *
     * @return
     * The count
     */
    public Long getCount() {
        return count;
    }

    /**
     *
     * @param count
     * The count
     */
    public void setCount(Long count) {
        this.count = count;
    }

    /**
     *
     * @return
     * The albums
     */
    public List<Album> getAlbums() {
        return albums;
    }

    /**
     *
     * @param albums
     * The albums
     */
    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

}
