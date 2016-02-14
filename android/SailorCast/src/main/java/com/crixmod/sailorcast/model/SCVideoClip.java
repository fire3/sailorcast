package com.crixmod.sailorcast.model;

/**
 * Created by fire3 on 16-2-14.
 */
public class SCVideoClip {
    private String duration;
    private String source;

    public SCVideoClip(String duration, String source) {
        this.duration = duration;
        this.source = source;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
