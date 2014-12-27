package com.crixmod.sailorcast.model.youku.show;

import com.google.gson.annotations.Expose;

public class ShowInfo {

    @Expose
    private String status;
    @Expose
    private Detail detail;

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The detail
     */
    public Detail getDetail() {
        return detail;
    }

    /**
     *
     * @param detail
     * The detail
     */
    public void setDetail(Detail detail) {
        this.detail = detail;
    }

}
