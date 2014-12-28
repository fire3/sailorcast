package com.crixmod.sailorcast.model.youku.show;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShowVideos {

    @Expose
    private String showcats;
    @Expose
    private String status;
    @SerializedName("show_update")
    @Expose
    private Boolean showUpdate;
    @Expose
    private Integer pz;

    @SerializedName("results")
    @Expose
    private List<ShowVideosResult> results = new ArrayList<ShowVideosResult>();
    @Expose
    private Integer pg;
    @Expose
    private Integer total;
    @Expose
    private String order;

    /**
     *
     * @return
     * The showcats
     */
    public String getShowcats() {
        return showcats;
    }

    /**
     *
     * @param showcats
     * The showcats
     */
    public void setShowcats(String showcats) {
        this.showcats = showcats;
    }

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
     * The showUpdate
     */
    public Boolean getShowUpdate() {
        return showUpdate;
    }

    /**
     *
     * @param showUpdate
     * The show_update
     */
    public void setShowUpdate(Boolean showUpdate) {
        this.showUpdate = showUpdate;
    }

    /**
     *
     * @return
     * The pz
     */
    public Integer getPz() {
        return pz;
    }

    /**
     *
     * @param pz
     * The pz
     */
    public void setPz(Integer pz) {
        this.pz = pz;
    }

    /**
     *
     * @return
     * The results
     */
    public List<ShowVideosResult> getResults() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(List<ShowVideosResult> results) {
        this.results = results;
    }

    /**
     *
     * @return
     * The pg
     */
    public Integer getPg() {
        return pg;
    }

    /**
     *
     * @param pg
     * The pg
     */
    public void setPg(Integer pg) {
        this.pg = pg;
    }

    /**
     *
     * @return
     * The total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     *
     * @param total
     * The total
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     *
     * @return
     * The order
     */
    public String getOrder() {
        return order;
    }

    /**
     *
     * @param order
     * The order
     */
    public void setOrder(String order) {
        this.order = order;
    }

}
