package com.crixmod.sailorcast.siteapi;

import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCChannel;
import com.crixmod.sailorcast.model.SCChannelFilter;
import com.crixmod.sailorcast.model.SCVideo;

/**
 * Created by fire3 on 15-2-3.
 */
public class IqiyiApi extends BaseSiteApi {

    private final static String TAG = "IqiyiApi";
    private final static String SEARCH_URL_FORMAT = "http://iface.iqiyi.com/api/searchIface?key=" +
            "69842642483add0a63503306d63f0443&did=923743189e70aa60&id=357070056881552&version=5.9.1&" +
            "all_episode=-1&need_video_img=1" +
            "&type=json&usertype=-1&sort=6&udid=96c337088070b9cb&ppid=&" +
            "openudid=96c337088070b9cb&uniqid=9c185b315590159c739e02f77ad4bba9&" +
            "search_type=6&showgame=0&qyid=357070056881552&app_type=1&pps=0&cupid_uid=357070056881552&page_number=%s&page_size=%s&category_id=0&keyword=%s";

    @Override
    public void doSearch(String key, OnGetAlbumsListener listener) {

    }

    @Override
    public void doGetAlbumVideos(SCAlbum album, int pageNo, int pageSize, OnGetVideosListener listener) {

    }

    @Override
    public void doGetAlbumDesc(SCAlbum album, OnGetAlbumDescListener listener) {

    }

    @Override
    public void doGetVideoPlayUrl(SCVideo video, OnGetVideoPlayUrlListener listener) {

    }

    @Override
    public void doGetChannelAlbums(SCChannel channel, int pageNo, int pageSize, OnGetAlbumsListener listener) {

    }

    @Override
    public void doGetChannelAlbumsByFilter(SCChannel channel, int pageNo, int pageSize, SCChannelFilter filter, OnGetAlbumsListener listener) {

    }

    @Override
    public void doGetChannelFilter(SCChannel channel, OnGetChannelFilterListener listener) {

    }
}
