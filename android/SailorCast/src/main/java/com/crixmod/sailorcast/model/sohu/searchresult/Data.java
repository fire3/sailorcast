package com.crixmod.sailorcast.model.sohu.searchresult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Data {

    @Expose
    private Long count;
    @Expose
    @SerializedName("albums")
    private List<SearchResultAlbum> searchResultAlbums = new ArrayList<SearchResultAlbum>();

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
    public List<SearchResultAlbum> getSearchResultAlbums() {
        return searchResultAlbums;
    }

    /**
     *
     * @param searchResultAlbums
     * The albums
     */
    public void setSearchResultAlbums(List<SearchResultAlbum> searchResultAlbums) {
        this.searchResultAlbums = searchResultAlbums;
    }

}
