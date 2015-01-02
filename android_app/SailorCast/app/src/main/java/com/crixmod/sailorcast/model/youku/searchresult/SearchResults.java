package com.crixmod.sailorcast.model.youku.searchresult;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class SearchResults {

    @Expose
    private String status;
    @Expose
    private Long pg;
    @Expose
    private Long pz;
    @Expose
    private Long total;
    @Expose
    private List<Result> results = new ArrayList<Result>();
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
     * The pg
     */
    public Long getPg() {
        return pg;
    }

    /**
     *
     * @param pg
     * The pg
     */
    public void setPg(Long pg) {
        this.pg = pg;
    }

    /**
     *
     * @return
     * The pz
     */
    public Long getPz() {
        return pz;
    }

    /**
     *
     * @param pz
     * The pz
     */
    public void setPz(Long pz) {
        this.pz = pz;
    }

    /**
     *
     * @return
     * The total
     */
    public Long getTotal() {
        return total;
    }

    /**
     *
     * @param total
     * The total
     */
    public void setTotal(Long total) {
        this.total = total;
    }

    /**
     *
     * @return
     * The results
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(List<Result> results) {
        this.results = results;
    }


}
