package com.crixmod.sailorcast.siteapi;

import android.content.Context;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCChannel;
import com.crixmod.sailorcast.model.SCChannelFilter;
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
    public static int SITE_ID_IQIYI = SCSite.IQIYI;

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
        if(siteID == SITE_ID_IQIYI)
            new IqiyiApi().doSearch(key,listener);
    }

    public static void doSearchAll(String key, OnGetAlbumsListener listener) {
        new SohuApi().doSearch(key,listener);
        new YouKuApi().doSearch(key,listener);
        new LetvApi().doSearch(key,listener);
        new IqiyiApi().doSearch(key,listener);
    }

    public static void doGetAlbumVideos(SCAlbum album, int pageNo, int pageSize,  OnGetVideosListener listener) {
        if(album.getSite().getSiteID() == SCSite.YOUKU)
            new YouKuApi().doGetAlbumVideos(album,pageNo,pageSize,listener);
        if(album.getSite().getSiteID() == SCSite.SOHU)
            new SohuApi().doGetAlbumVideos(album,pageNo,pageSize,listener);
        if(album.getSite().getSiteID() == SCSite.LETV)
            new LetvApi().doGetAlbumVideos(album,pageNo,pageSize,listener);
        if(album.getSite().getSiteID() == SCSite.IQIYI)
            new IqiyiApi().doGetAlbumVideos(album,pageNo,pageSize,listener);

    }

    public static void doGetAlbumDesc(SCAlbum album, OnGetAlbumDescListener listener) {
        if(album.getSite().getSiteID() == SCSite.YOUKU)
            new YouKuApi().doGetAlbumDesc(album,listener);
        if(album.getSite().getSiteID() == SCSite.SOHU)
            new SohuApi().doGetAlbumDesc(album,listener);
        if(album.getSite().getSiteID() == SCSite.LETV)
            new LetvApi().doGetAlbumDesc(album,listener);
        if(album.getSite().getSiteID() == SCSite.IQIYI)
            new IqiyiApi().doGetAlbumDesc(album,listener);
    }

    public static void doGetVideoPlayUrl(SCVideo video, OnGetVideoPlayUrlListener listener) {
        if(video.getSite().getSiteID() == SCSite.YOUKU)
            new YouKuApi().doGetVideoPlayUrl(video,listener);
        if(video.getSite().getSiteID() == SCSite.SOHU)
            new SohuApi().doGetVideoPlayUrl(video,listener);
        if(video.getSite().getSiteID() == SCSite.LETV)
            new LetvApi().doGetVideoPlayUrl(video,listener);
        if(video.getSite().getSiteID() == SCSite.IQIYI)
            new IqiyiApi().doGetVideoPlayUrl(video,listener);
    }

    public static int getSupportSiteNumber() {
        return SCSite.count();
    }

    public static String getSiteName(int siteID, Context mContext) {
        if(siteID == SCSite.YOUKU)
            return mContext.getResources().getString(R.string.site_youku);
        if(siteID == SCSite.LETV)
            return mContext.getResources().getString(R.string.site_letv);
        if(siteID == SCSite.SOHU)
            return mContext.getResources().getString(R.string.site_sohu);
        if(siteID == SCSite.IQIYI)
            return mContext.getResources().getString(R.string.site_iqiyi);
        return null;
    }

    public static void doGetChannelAlbumsByFilter(int siteID, int channelID, int pageNo, int pageSize, SCChannelFilter filter, OnGetAlbumsListener listener) {

        if(siteID == SCSite.LETV)
            new LetvApi().doGetChannelAlbumsByFilter(new SCChannel(channelID),pageNo,pageSize,filter,listener);
        if(siteID == SCSite.YOUKU)
            new YouKuApi().doGetChannelAlbumsByFilter(new SCChannel(channelID),pageNo,pageSize,filter,listener);
        if(siteID == SCSite.SOHU)
            new SohuApi().doGetChannelAlbumsByFilter(new SCChannel(channelID),pageNo,pageSize,filter,listener);
        if(siteID == SCSite.IQIYI)
            new IqiyiApi().doGetChannelAlbumsByFilter(new SCChannel(channelID),pageNo,pageSize,filter,listener);

    }
    public static void doGetChannelAlbums(int siteID, int channelID, int pageNo, int pageSize, OnGetAlbumsListener listener) {
        if(siteID == SCSite.LETV)
            new LetvApi().doGetChannelAlbums(new SCChannel(channelID),pageNo,pageSize,listener);
        if(siteID == SCSite.YOUKU)
            new YouKuApi().doGetChannelAlbums(new SCChannel(channelID),pageNo,pageSize,listener);
        if(siteID == SCSite.SOHU)
            new SohuApi().doGetChannelAlbums(new SCChannel(channelID),pageNo,pageSize,listener);
        if(siteID == SCSite.IQIYI)
            new IqiyiApi().doGetChannelAlbums(new SCChannel(channelID),pageNo,pageSize,listener);

    }

    public static void doGetChannelFilter(int siteID, int channelID, OnGetChannelFilterListener listener) {

        if(siteID == SCSite.LETV)
            new LetvApi().doGetChannelFilter(new SCChannel(channelID),listener);
        if(siteID == SCSite.YOUKU)
            new YouKuApi().doGetChannelFilter(new SCChannel(channelID),listener);
        if(siteID == SCSite.SOHU)
            new SohuApi().doGetChannelFilter(new SCChannel(channelID),listener);
        if(siteID == SCSite.IQIYI)
            new IqiyiApi().doGetChannelFilter(new SCChannel(channelID),listener);
    }


}
