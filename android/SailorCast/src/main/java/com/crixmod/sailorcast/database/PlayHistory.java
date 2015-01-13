package com.crixmod.sailorcast.database;

/**
 * Created by fire3 on 14-12-10.
 */
public class PlayHistory {
    // private variables
    private int _id;


    private Integer _videoInAlbum;
    private Integer _albumID;
    private String _albumName;

    private Integer _videoID;
    private String _videoName;

    private String _albumImage;
    private String _categoryName;
    private Integer _videoSite;

    public final static int VIDEO_SITE_SOHU = 1;
    public final static int VIDEO_SITE_YOUKU = 2;

    public PlayHistory() {}


    public PlayHistory(int _id, Integer _videoInAlbum, Integer _albumID, String _albumName, Integer _videoID, String _videoName, String _albumImage, String _categoryName, int _videoSite) {
        this._id = _id;
        this._videoInAlbum = _videoInAlbum;
        this._albumID = _albumID;
        this._albumName = _albumName;
        this._videoID = _videoID;
        this._videoName = _videoName;
        this._albumImage = _albumImage;
        this._categoryName = _categoryName;
        this._videoSite = _videoSite;
    }

    public PlayHistory(Integer _videoInAlbum, Integer _albumID, String _albumName, Integer _videoID, String _videoName, String _albumImage, String _categoryName, int _videoSite) {
        this._videoInAlbum = _videoInAlbum;
        this._albumID = _albumID;
        this._albumName = _albumName;
        this._videoID = _videoID;
        this._videoName = _videoName;
        this._albumImage = _albumImage;
        this._categoryName = _categoryName;
        this._videoSite = _videoSite;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public Integer get_videoInAlbum() {
        return _videoInAlbum;
    }

    public void set_videoInAlbum(Integer _videoInAlbum) {
        this._videoInAlbum = _videoInAlbum;
    }

    public Integer get_albumID() {
        return _albumID;
    }

    public void set_albumID(Integer _albumID) {
        this._albumID = _albumID;
    }

    public String get_albumName() {
        return _albumName;
    }

    public void set_albumName(String _albumName) {
        this._albumName = _albumName;
    }

    public Integer get_videoID() {
        return _videoID;
    }

    public void set_videoID(Integer _videoID) {
        this._videoID = _videoID;
    }

    public String get_videoName() {
        return _videoName;
    }

    public void set_videoName(String _videoName) {
        this._videoName = _videoName;
    }

    public String get_albumImage() {
        return _albumImage;
    }

    public void set_albumImage(String _albumImage) {
        this._albumImage = _albumImage;
    }

    public String get_categoryName() {
        return _categoryName;
    }

    public void set_categoryName(String _categoryName) {
        this._categoryName = _categoryName;
    }


    public Integer get_videoSite() {
        return _videoSite;
    }

    public void set_videoSite(Integer _videoSite) {
        this._videoSite = _videoSite;
    }
}
