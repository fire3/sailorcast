package com.crixmod.sailorcast.model;

/**
 * Created by fire3 on 15-3-11.
 */
public class SCLiveStreamProgram {
    private String title;
    private String playTime;
    private String endTime;

    public SCLiveStreamProgram(String title, String playTime, String endTime) {
        this.title = title;
        this.playTime = playTime;
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlayTime() {
        return playTime;
    }

    public void setPlayTime(String playTime) {
        this.playTime = playTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "SCLiveStreamProgram{" +
                "title='" + title + '\'' +
                ", playTime='" + playTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
