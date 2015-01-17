package com.crixmod.sailorcast.database;

import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCVideo;

/**
 * Created by fire3 on 2015/1/17.
 */
public class History {
    private SCAlbum album;
    private SCVideo video;
    private int videoNo;
    private int playTime;
    private String createTime;

    public History(SCAlbum album, SCVideo video, int videoNo, int playTime, String createTime) {
        this.album = album;
        this.video = video;
        this.videoNo = videoNo;
        this.playTime = playTime;
        this.createTime = createTime;
    }

    public SCAlbum getAlbum() {
        return album;
    }

    public void setAlbum(SCAlbum album) {
        this.album = album;
    }

    public SCVideo getVideo() {
        return video;
    }

    public void setVideo(SCVideo video) {
        this.video = video;
    }

    public int getVideoNo() {
        return videoNo;
    }

    public void setVideoNo(int videoNo) {
        this.videoNo = videoNo;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
