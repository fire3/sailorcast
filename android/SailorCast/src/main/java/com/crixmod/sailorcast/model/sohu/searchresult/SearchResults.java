package com.crixmod.sailorcast.model.sohu.searchresult;

import com.google.gson.annotations.Expose;

public class SearchResults {

    @Expose
    private Long status;
    @Expose
    private String statusText;
    @Expose
    private Data data;

    /**
     *
     * @return
     * The status
     */
    public Long getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(Long status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The statusText
     */
    public String getStatusText() {
        return statusText;
    }

    /**
     *
     * @param statusText
     * The statusText
     */
    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    /**
     *
     * @return
     * The data
     */
    public Data getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(Data data) {
        this.data = data;
    }

}
