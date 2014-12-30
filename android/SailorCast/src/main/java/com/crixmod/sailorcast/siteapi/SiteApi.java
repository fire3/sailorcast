package com.crixmod.sailorcast.siteapi;

import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCSite;
import com.crixmod.sailorcast.model.SCVideo;

/**
 * Created by fire3 on 14-12-30.
 */
public class SiteApi {
    public static int SITE_ID_YOUKU = SCSite.YOUKU;
    public static int SITE_ID_SOHU = SCSite.SOHU;
    public static int SITE_ID_LETV = SCSite.LETV;

    public static void doSearch(int siteID, String key, OnSearchRequestListener listener) {
        if(siteID == SITE_ID_SOHU)
            new SohuApi().doSearch(key,listener);
        if(siteID == SITE_ID_YOUKU)
            new YouKuApi().doSearch(key,listener);
        if(siteID == SITE_ID_LETV)
            new LetvApi().doSearch(key,listener);

    }

    public static void doGetAlbumVideos(SCAlbum album, int pageNo, int pageSize,  OnGetVideosListener listener) {
        if(album.getSite().getSiteID() == SCSite.YOUKU)
            new YouKuApi().doGetAlbumVideos(album,pageNo,pageSize,listener);
        if(album.getSite().getSiteID() == SCSite.SOHU)
            new SohuApi().doGetAlbumVideos(album,pageNo,pageSize,listener);

    }

    public static void doGetAlbumDesc(SCAlbum album, OnGetAlbumDescListener listener) {
        if(album.getSite().getSiteID() == SCSite.YOUKU)
            new YouKuApi().doGetAlbumDesc(album,listener);
        if(album.getSite().getSiteID() == SCSite.SOHU)
            new SohuApi().doGetAlbumDesc(album,listener);
    }

    public static void doGetVideoPlayUrl(SCVideo video, OnGetVideoPlayUrlListener listener) {
        if(video.getSite().getSiteID() == SCSite.YOUKU)
            new YouKuApi().doGetVideoPlayUrl(video,listener);
        if(video.getSite().getSiteID() == SCSite.SOHU)
            new SohuApi().doGetVideoPlayUrl(video,listener);
    }

}
