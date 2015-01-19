package com.crixmod.sailorcast.siteapi;

import android.content.Context;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCSite;
import com.crixmod.sailorcast.model.SCVideo;
import com.crixmod.sailorcast.utils.HttpUtils;

/**
 * Created by fire3 on 14-12-30.
 */
public class SiteApi {
    public static int SITE_ID_YOUKU = SCSite.YOUKU;
    public static int SITE_ID_SOHU = SCSite.SOHU;
    public static int SITE_ID_LETV = SCSite.LETV;

    public static enum Channel {
        CHANNEL_SHOW,  //电视剧
        CHANNEL_MOVIE, //电影
        CHANNEL_COMIC, //动漫
        CHANNEL_ENT, //综艺
        CHANNEL_DOCUMENTARY  //纪录片
    }


    public static void cancel() {
        HttpUtils.cancelAll();
    }

    public static void doSearch(int siteID, String key, OnGetAlbumsListener listener) {
        if(siteID == SITE_ID_SOHU)
            new SohuApi().doSearch(key,listener);
        if(siteID == SITE_ID_YOUKU)
            new YouKuApi().doSearch(key,listener);
        if(siteID == SITE_ID_LETV)
            new LetvApi().doSearch(key,listener);
    }

    public static void doSearchAll(String key, OnGetAlbumsListener listener) {
        new SohuApi().doSearch(key,listener);
        new YouKuApi().doSearch(key,listener);
        new LetvApi().doSearch(key,listener);
    }

    public static void doGetAlbumVideos(SCAlbum album, int pageNo, int pageSize,  OnGetVideosListener listener) {
        if(album.getSite().getSiteID() == SCSite.YOUKU)
            new YouKuApi().doGetAlbumVideos(album,pageNo,pageSize,listener);
        if(album.getSite().getSiteID() == SCSite.SOHU)
            new SohuApi().doGetAlbumVideos(album,pageNo,pageSize,listener);
        if(album.getSite().getSiteID() == SCSite.LETV)
            new LetvApi().doGetAlbumVideos(album,pageNo,pageSize,listener);

    }

    public static void doGetAlbumDesc(SCAlbum album, OnGetAlbumDescListener listener) {
        if(album.getSite().getSiteID() == SCSite.YOUKU)
            new YouKuApi().doGetAlbumDesc(album,listener);
        if(album.getSite().getSiteID() == SCSite.SOHU)
            new SohuApi().doGetAlbumDesc(album,listener);
        if(album.getSite().getSiteID() == SCSite.LETV)
            new LetvApi().doGetAlbumDesc(album,listener);
    }

    public static void doGetVideoPlayUrl(SCVideo video, OnGetVideoPlayUrlListener listener) {
        if(video.getSite().getSiteID() == SCSite.YOUKU)
            new YouKuApi().doGetVideoPlayUrl(video,listener);
        if(video.getSite().getSiteID() == SCSite.SOHU)
            new SohuApi().doGetVideoPlayUrl(video,listener);
        if(video.getSite().getSiteID() == SCSite.LETV)
            new LetvApi().doGetVideoPlayUrl(video,listener);
    }

    public static int getSupportSiteNumber() {
        return 3;
    }

    public static String getSiteName(int siteID, Context mContext) {
        if(siteID == SCSite.YOUKU)
            return mContext.getResources().getString(R.string.site_youku);
        if(siteID == SCSite.LETV)
            return mContext.getResources().getString(R.string.site_letv);
        if(siteID == SCSite.SOHU)
            return mContext.getResources().getString(R.string.site_sohu);
        return null;
    }

}
