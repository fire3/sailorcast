package com.crixmod.sailorcast.siteapi;

import com.crixmod.sailorcast.SailorCast;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCAlbums;
import com.crixmod.sailorcast.model.SCChannel;
import com.crixmod.sailorcast.model.SCChannelFilter;
import com.crixmod.sailorcast.model.SCChannelFilterItem;
import com.crixmod.sailorcast.model.SCFailLog;
import com.crixmod.sailorcast.model.SCSite;
import com.crixmod.sailorcast.model.SCVideo;
import com.crixmod.sailorcast.model.SCVideos;
import com.crixmod.sailorcast.model.sohu.album.Album;
import com.crixmod.sailorcast.model.sohu.searchresult.SearchResultAlbum;
import com.crixmod.sailorcast.model.sohu.searchresult.SearchResults;
import com.crixmod.sailorcast.model.sohu.videos.Video;
import com.crixmod.sailorcast.model.sohu.videos.Videos;
import com.crixmod.sailorcast.utils.HttpUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by fire3 on 14-12-26.
 */
public class SohuApi extends BaseSiteApi {
    private final static String API_KEY = "plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=4.5.0&sysver=4.4.2&partner=47";
    private final static String API_ALBUM_INFO = "http://api.tv.sohu.com/v4/album/info/" ;
    private final static String API_ALBUM_VIDOES = "http://api.tv.sohu.com/v4/album/videos/" ;
    private final static String API_SEARCH = "http://api.tv.sohu.com/v4/search/album.json?o=&all=0&ds=&" + API_KEY + "&key=";
    private static final String TAG = "SohuApi";

    private static int ORDER_DESCENDING = 1;
    private static int ORDER_ASCENDING = 0;

    private final static String API_CHANNEL_ALBUM_FORMAT = "http://api.tv.sohu.com/v4/search/channel.json" +
            "?cid=%s&o=1&plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&" +
            "sver=4.5.0&sysver=4.4.2&partner=47&page=%s&page_size=%s";
    private final static String API_CHANNEL_FILTER_FORMAT = "http://api.tv.sohu.com/v4/category/%s.json?" +
            "plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=4.5.0&sysver=4.4.2&partner=47";
    private final static String API_CHANNEL_ALBUM_BY_FILTER_FORMAT = "http://api.tv.sohu.com/v4/search/channel.json" +
            "?cid=%s&%s&plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&" +
            "sver=4.5.0&sysver=4.4.2&partner=47&page=%s&page_size=%s";

    private final static String API_HOME_URL = "http://api.tv.sohu.com/v4/mobile/channelPageData/" +
            "list.json?cate_code=0&plat=6&poid=1&" +
            "api_key=9854b2afa779e1a6bff1962447a09dbd&sver=4.5.0&act=1&sysver=4.4.2&partner=47";
    private final static int CID_SHOW = 2;
    private final static int CID_MOVIE = 1;
    private final static int CID_COMIC = 16;
    private final static int CID_VARIETY = 7;
    private final static int CID_DOCUMENTARY = 8;
    private final static int CID_MUSIC = 24;
    private final static int CID_ENT = 13;
    private final static int CID_SPORT = 9009;

    private int channelToCid(SCChannel channel) {

        if(channel.getChannelID() == SCChannel.MOVIE)
            return CID_MOVIE;
        if(channel.getChannelID() == SCChannel.SHOW)
            return CID_SHOW;
        if(channel.getChannelID() == SCChannel.DOCUMENTARY)
            return CID_DOCUMENTARY;
        /*
        if(channel.getChannelID() == SCChannel.ENT)
            return CID_ENT;
        */
        if(channel.getChannelID() == SCChannel.COMIC)
            return CID_COMIC;
        if(channel.getChannelID() == SCChannel.VARIETY)
            return CID_VARIETY;
        if(channel.getChannelID() == SCChannel.MUSIC)
            return CID_MUSIC;
        if(channel.getChannelID() == SCChannel.SPORT)
            return CID_SPORT;
        if(channel.getChannelID() == SCChannel.UNKNOWN)
            return -1;
        return -1;
    }

    private String getChannelFilterUrl(SCChannel channel) {
        if(channel.getChannelID() == SCChannel.SHOW)
            return String.format(API_CHANNEL_FILTER_FORMAT,"teleplay");
        if(channel.getChannelID() == SCChannel.MOVIE)
            return String.format(API_CHANNEL_FILTER_FORMAT,"movie");
        if(channel.getChannelID() == SCChannel.VARIETY)
            return String.format(API_CHANNEL_FILTER_FORMAT,"zongyi");
        if(channel.getChannelID() == SCChannel.COMIC)
            return String.format(API_CHANNEL_FILTER_FORMAT,"animation");
        if(channel.getChannelID() == SCChannel.DOCUMENTARY)
            return String.format(API_CHANNEL_FILTER_FORMAT,"documentary");
        if(channel.getChannelID() == SCChannel.MUSIC)
            return String.format(API_CHANNEL_FILTER_FORMAT,"music");


        return null;
    }

    private SCFailLog makeHttpFailLog(String url, String functionName) {
        SCFailLog err = new SCFailLog(SCSite.SOHU,SCFailLog.TYPE_HTTP_FAILURE);
        err.setFunctionName(functionName);
        err.setClassName("SohuApi");
        err.setTag(TAG);
        err.setUrl(url);
        return err;
    }

    private SCFailLog makeHttpFailLog(String url, String functionName, Exception e) {
        SCFailLog err = new SCFailLog(SCSite.SOHU,SCFailLog.TYPE_HTTP_FAILURE);
        err.setException(e);
        err.setFunctionName(functionName);
        err.setClassName("SohuApi");
        err.setTag(TAG);
        err.setUrl(url);
        return err;
    }

    private SCFailLog makeFatalFailLog(String url, String functionName, Exception e) {
        SCFailLog err = new SCFailLog(SCSite.SOHU,SCFailLog.TYPE_FATAL_ERR);
        err.setException(e);
        err.setFunctionName(functionName);
        err.setClassName("SohuApi");
        err.setTag(TAG);
        err.setUrl(url);
        return err;
    }

    private SCFailLog makeFatalFailLog(String url, String functionName) {
        SCFailLog err = new SCFailLog(SCSite.SOHU,SCFailLog.TYPE_FATAL_ERR);
        err.setFunctionName(functionName);
        err.setClassName("SohuApi");
        err.setTag(TAG);
        err.setUrl(url);
        return err;
    }


    private SCFailLog makeNoResultFailLog(String url, String functionName) {
        SCFailLog err = new SCFailLog(SCSite.SOHU,SCFailLog.TYPE_NO_RESULT);
        err.setFunctionName(functionName);
        err.setClassName("SohuApi");
        err.setTag(TAG);
        err.setUrl(url);
        return err;
    }

    private String getChannelAlbumUrl(SCChannel channel, int pageNo, int pageSize) {
        return String.format(API_CHANNEL_ALBUM_FORMAT,channelToCid(channel),pageNo,pageSize);
    }

    private String getChannelAlbumUrlByFilter(SCChannel channel, SCChannelFilter filter, int pageNo, int pageSize) {
        String filterString = "";
        ArrayList<SCChannelFilterItem> items = filter.getSelectedItems();
        for (int i = 0; i < items.size(); i++) {
            SCChannelFilterItem item = items.get(i);
            filterString = filterString + item.getSearchKey() + "=" + item.getSearchVal();
            if(i < (items.size() - 1))
                filterString = filterString + "&";
        }
        return String.format(API_CHANNEL_ALBUM_BY_FILTER_FORMAT,channelToCid(channel),filterString,pageNo,pageSize);
    }
    @Override
    public void doSearch(String key, final OnGetAlbumsListener listener) {

        String searchKey = null;
        try {
            searchKey = URLEncoder.encode(key, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            if(listener != null) {
                SCFailLog err = new SCFailLog(SCSite.SOHU,SCFailLog.TYPE_URL_ERR);
                err.setFunctionName("doSearch");
                err.setException(e);
                err.setTag(TAG);
                err.setClassName("SohuApi");
                err.setReason(searchKey);
                listener.onGetAlbumsFailed(err);
            }
            e.printStackTrace();
            return;
        }
        final String url = API_SEARCH + searchKey;

        HttpUtils.asyncGet(url, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if(listener != null) {
                    SCFailLog err = makeHttpFailLog(url,"doSearch",e);
                    listener.onGetAlbumsFailed(err);
                }

                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) {
                if (!response.isSuccessful()) {
                    if(listener != null) {
                        SCFailLog err = makeHttpFailLog(url,"doSearch");
                        listener.onGetAlbumsFailed(err);
                    }
                    return;
                }
                SearchResults results = null;
                try {
                    results = SailorCast.getGson().fromJson(response.body().string(), SearchResults.class);

                    SCAlbums albums = toSCAlbums(results);
                    if(albums != null) {
                        if(listener != null)
                            listener.onGetAlbumsSuccess(albums);
                    }
                    else {
                        if(listener != null) {
                            SCFailLog err =  makeNoResultFailLog(url, "doSearch");
                            listener.onGetAlbumsFailed(err);
                        }
                    }

                } catch (IOException e) {
                    if(listener != null) {
                        SCFailLog err =  makeFatalFailLog(url, "doSearch", e);
                        listener.onGetAlbumsFailed(err);
                    }
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void doGetAlbumVideos(final SCAlbum album, final int pageNo, final int pageSize, final OnGetVideosListener listener) {
        final String url;
        url = API_ALBUM_VIDOES + album.getAlbumId() + ".json?" + "page=" + pageNo + "&page_size=" + pageSize +
                "&order=" + ORDER_ASCENDING + "&site=1&with_trailer=1&" + API_KEY;
        HttpUtils.asyncGet(url,new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if(listener != null) {
                    SCFailLog err = makeHttpFailLog(url,"doGetAlbumVideos",e);
                    listener.onGetVideosFailed(err);
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    if(listener != null) {
                        SCFailLog err = makeHttpFailLog(url, "doGetAlbumVideos");
                        listener.onGetVideosFailed(err);
                    }
                    return;
                }
                try {
                    Videos videos = SailorCast.getGson().fromJson(response.body().string(), Videos.class);
                    if (videos.getData() != null) {
                        SCVideos scVideos = new SCVideos();
                        int i = 0;
                        for (Video v : videos.getData().getVideos()) {
                            i++;
                            SCVideo scVideo = new SCVideo();
                            scVideo.setSCSite(SCSite.SOHU);
                            scVideo.setHorPic(v.getHorHighPic());
                            scVideo.setVerPic(v.getVerHighPic());
                            scVideo.setVideoID(v.getVid().toString());
                            scVideo.setVideoTitle(v.getVideoName());
                            scVideo.setM3U8Nor(v.getUrlNor());
                            scVideo.setM3U8High(v.getUrlHigh());
                            scVideo.setM3U8Super(v.getUrlSuper());
                            scVideo.setAlbumID(album.getAlbumId());
                            scVideos.add(scVideo);
                        }
                        listener.onGetVideosSuccess(scVideos);
                    }
                } catch (Exception e) {
                    if(listener != null) {
                        SCFailLog err = makeFatalFailLog(url, "doGetAlbumVideos", e);
                        listener.onGetVideosFailed(err);
                    }
                    e.printStackTrace();
                }

            }
        });
    }


    private void fillAlbumDesc(String url, SCAlbum album, Album sohuAlbum, OnGetAlbumDescListener listener) {
        try {
            if (sohuAlbum.getData() != null) {
                //album.setVideosCount(sohuAlbum.getData().getLatestVideoCount());
                //TotalVideoCount is 0 sometimes, use latestVideoCount instead.
                if (sohuAlbum.getData().getLatestVideoCount() > 0)
                    album.setVideosTotal(sohuAlbum.getData().getLatestVideoCount());
                else
                    album.setVideosTotal(sohuAlbum.getData().getTotalVideoCount());

                album.setDesc(sohuAlbum.getData().getAlbumDesc());
                album.setMainActor(sohuAlbum.getData().getMainActor());
                album.setDirector(sohuAlbum.getData().getDirector());
                if (listener != null)
                    listener.onGetAlbumDescSuccess(album);
            } else {
                if (listener != null) {
                    SCFailLog err = makeNoResultFailLog(url, "fillAlbumDesc");
                    listener.onGetAlbumDescFailed(err);
                }
            }
        } catch (Exception e) {
            if (listener != null) {
                SCFailLog err = makeFatalFailLog(url, "fillAlbumDesc");
                listener.onGetAlbumDescFailed(err);
            }

        }
    }

    @Override
    public void doGetAlbumDesc(final SCAlbum album, final OnGetAlbumDescListener listener) {
        final String url = API_ALBUM_INFO + album.getAlbumId() + ".json?" + API_KEY;
        HttpUtils.asyncGet(url, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if(listener != null) {
                    SCFailLog err = makeHttpFailLog(url, "doGetAlbumDesc", e);
                    listener.onGetAlbumDescFailed(err);
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    if(listener != null) {
                        SCFailLog err = makeHttpFailLog(url, "doGetAlbumDesc");
                        listener.onGetAlbumDescFailed(err);
                    }
                    return;
                }
                Album a = SailorCast.getGson().fromJson(response.body().string(), Album.class);
                fillAlbumDesc(url,album, a, listener);
            }
        });
    }


    @Override
    public void doGetVideoPlayUrl(SCVideo video, OnGetVideoPlayUrlListener listener) {
        if(video.getM3U8Nor() != null)
            listener.onGetVideoPlayUrlNormal(video,video.getM3U8Nor());
        if(video.getM3U8High() != null)
            listener.onGetVideoPlayUrlHigh(video,video.getM3U8High());
        if(video.getM3U8Super() != null)
            listener.onGetVideoPlayUrlSuper(video,video.getM3U8Super());
    }

    public void doGetChannelAlbumsByUrl(final String url, final OnGetAlbumsListener listener) {
        HttpUtils.asyncGet(url, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if(listener != null) {
                    SCFailLog err = makeHttpFailLog(url, "doGetChannelAlbumsByUrl",e);
                    listener.onGetAlbumsFailed(err);
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    if(listener != null) {
                        SCFailLog err = makeHttpFailLog(url, "doGetChannelAlbumsByUrl");
                        listener.onGetAlbumsFailed(err);
                    }
                    return;
                }
                SearchResults results = null;
                try {
                    results = SailorCast.getGson().fromJson(response.body().string(), SearchResults.class);

                    SCAlbums albums = toSCAlbums(results);
                    if(albums != null) {
                        if(listener != null)
                            listener.onGetAlbumsSuccess(albums);
                    }
                    else {
                        if(listener != null) {
                            SCFailLog err = makeNoResultFailLog(url, "doGetChannelAlbumsByUrl");
                            listener.onGetAlbumsFailed(err);
                        }
                    }

                } catch (IOException e) {
                    if(listener != null) {
                        SCFailLog err = makeFatalFailLog(url, "doGetChannelAlbumsByUrl", e);
                        listener.onGetAlbumsFailed(err);
                    }
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void doGetChannelAlbums(SCChannel channel, int pageNo, int pageSize, final OnGetAlbumsListener listener) {
        String url = getChannelAlbumUrl(channel, pageNo, pageSize);
        if(url == null) {
            SCFailLog err = new SCFailLog(SCSite.SOHU,SCFailLog.TYPE_URL_ERR);
            err.setFunctionName("doGetChannelAlbums");
            err.setReason(channel.getChannelName());
            if(listener != null)
                listener.onGetAlbumsFailed(err);
            return;
        }
        doGetChannelAlbumsByUrl(url,listener);
    }

    @Override
    public void doGetChannelAlbumsByFilter(SCChannel channel, int pageNo, int pageSize, SCChannelFilter filter, OnGetAlbumsListener listener) {
        String url = getChannelAlbumUrlByFilter(channel, filter, pageNo, pageSize);
        if(url == null) {
            SCFailLog err = new SCFailLog(SCSite.SOHU,SCFailLog.TYPE_URL_ERR);
            err.setFunctionName("doGetChannelAlbums");
            err.setReason(channel.getChannelName());
            if(listener != null)
                listener.onGetAlbumsFailed(err);
            return;
        }
        doGetChannelAlbumsByUrl(url,listener);
    }

    @Override
    public void doGetChannelFilter(final SCChannel channel, final OnGetChannelFilterListener listener) {
        final String url = getChannelFilterUrl(channel);
         if(url == null) {
            SCFailLog err = new SCFailLog(SCSite.SOHU,SCFailLog.TYPE_URL_ERR);
            err.setFunctionName("doGetChannelFilter");
            err.setReason(channel.getChannelName());
            if(listener != null)
                listener.onGetChannelFilterFailed(err);
            return;
        }
        HttpUtils.asyncGet(url, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                SCFailLog err = makeHttpFailLog(url,"doGetChannelFilter",e);
                if(listener != null)
                    listener.onGetChannelFilterFailed(err);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String ret = response.body().string();
                try {
                    JSONObject retJson = new JSONObject(ret);
                    JSONObject resultsJson = retJson.optJSONObject("data");
                    if(resultsJson != null) {
                        JSONArray filterJsonArray = resultsJson.optJSONArray("categorys");
                        if(filterJsonArray != null && filterJsonArray.length() > 0) {
                            SCChannelFilter filter = new SCChannelFilter();
                            for (int i = 0; i < filterJsonArray.length(); i++) {
                                JSONObject filterJson = filterJsonArray.getJSONObject(i);
                                JSONArray filterItemsJson = filterJson.optJSONArray("cates");
                                String key = filterJson.optString("cate_alias");
                                ArrayList<SCChannelFilterItem> filterItems = new ArrayList<SCChannelFilterItem>();
                                for (int j = 0; j < filterItemsJson.length(); j++) {
                                    JSONObject filterItemJson = filterItemsJson.getJSONObject(j);
                                    String searchVal = filterItemJson.optString("search_key");
                                    String displayName = filterItemJson.optString("name");
                                    if(searchVal == null)
                                        searchVal = "";
                                    SCChannelFilterItem filterItem = new SCChannelFilterItem(searchVal,displayName);
                                    filterItem.setParentKey(key);
                                    filterItem.setSearchKey(key);
                                    filterItems.add(filterItem);
                                }
                                filter.addFilter(key,filterItems);
                            }
                            if(listener != null)
                                listener.onGetChannelFilterSuccess(filter);
                        }
                    }
                } catch (JSONException e) {
                    if(listener != null) {
                        SCFailLog err = makeFatalFailLog(url, "doGetChannelFilter", e);
                        listener.onGetChannelFilterFailed(err);
                    }

                    e.printStackTrace();
                }
            }
        });
    }

    private SCAlbums toSCAlbums(SearchResults results) {
        if(results.getData().getSearchResultAlbums() == null)
            return null;
        if(results.getData().getSearchResultAlbums().size() == 0 && results.getData().getSearchResultVideos().size() == 0)
            return null;

        if(results.getData().getSearchResultAlbums().size() > 0) {
            SCAlbums albums = new SCAlbums();
            for (SearchResultAlbum a : results.getData().getSearchResultAlbums()) {
                SCAlbum sa = new SCAlbum(SCSite.SOHU);
                sa.setDesc(a.getTvDesc());
                sa.setDirector(a.getDirector());
                sa.setHorImageUrl(a.getHorHighPic());
                sa.setVerImageUrl(a.getVerHighPic());
                sa.setMainActor(a.getMainActor());
                sa.setTitle(a.getAlbumName());
                sa.setTip(a.getTip());
                sa.setAlbumId(a.getAid().toString());
                albums.add(sa);
            }
            return albums;
        }

        if(results.getData().getSearchResultVideos().size() > 0) {
            SCAlbums albums = new SCAlbums();
            for (SearchResultAlbum a : results.getData().getSearchResultVideos()) {
                SCAlbum sa = new SCAlbum(SCSite.SOHU);
                sa.setDesc(a.getTvDesc());
                sa.setDirector(a.getDirector());
                sa.setHorImageUrl(a.getHorHighPic());
                sa.setVerImageUrl(a.getVerHighPic());
                sa.setMainActor(a.getMainActor());
                sa.setTitle(a.getAlbumName());
                sa.setTip(a.getTip());
                if(a.getAid() != null) {
                    sa.setAlbumId(a.getAid().toString());
                    albums.add(sa);
                }
            }
            if(albums.size() > 0)
                return albums;
        }

        return null;
    }

/*
    public void  getBanners(final OnGetBannersListener listener) {
        String url = API_HOME_URL;
        HttpUtils.asyncGet(url,new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String ret = response.body().string();
                try {
                    JSONObject retJson = new JSONObject(ret);
                    JSONObject dataJson = retJson.optJSONObject("data");
                    if(dataJson != null) {

                        SCBanners banners = new SCBanners();
                        JSONArray columnsJson = dataJson.optJSONArray("columns");

                        for (int i = 0; i < columnsJson.length(); i++) {
                            JSONObject columnJson = columnsJson.getJSONObject(i);
                            int columnID = columnJson.optInt("column_id");
                            int columnType = columnJson.optInt("column_type");
                            String columnName = columnJson.optString("name");
                            JSONArray listJson = columnJson.optJSONArray("video_list");

                            if(columnID == 1 || columnID == 4 || columnID == 34 || columnID == 143 || columnID == 3 || columnID == 29) {
                                if (listJson.length() > 0) {
                                    SCAlbums albums = new SCAlbums();
                                    for (int j = 0; j < listJson.length(); j++) {

                                        JSONObject a = listJson.getJSONObject(j);

                                        String horPic = a.optString("video_big_pic");
                                        String horPic2 = a.optString("hor_common_pic");
                                        String aid = a.optString("aid");
                                        String title = a.optString("video_name");
                                        String tip = a.optString("tip");
                                        String aName = a.optString("album_name");
                                        String subTitle = a.optString("album_sub_name");

                                        int latest_video_count = a.optInt("latest_video_count", 0);
                                        if (aName != null && !aName.equals("广告")) {
                                            SCAlbum album = new SCAlbum(SCSite.SOHU);
                                            if (title != null && !title.isEmpty())
                                                album.setTitle(title);
                                            else
                                                album.setTitle(aName);
                                            //album.setVideosCount(latest_video_count);
                                            album.setVideosTotal(latest_video_count);
                                            album.setTip(tip);
                                            if (horPic != null && !horPic.isEmpty())
                                                album.setHorImageUrl(horPic);
                                            else if (horPic2 != null && !horPic2.isEmpty())
                                                album.setHorImageUrl(horPic2);
                                            album.setAlbumId(aid);
                                            album.setSubTitle(subTitle);
                                            albums.add(album);
                                        }

                                    }

                                    if (columnID == 1 && columnType == 1) {
                                        SCBanner banner = new SCBanner(SCBanner.TYPE_SLIDER, columnName, albums);
                                        banners.add(banner);
                                    } else  {
                                        SCBanner banner = new SCBanner(SCBanner.TYPE_TABLE, columnName, albums);
                                        banners.add(banner);
                                    }
                                }
                            }
                        }
                        if(banners.size()> 0)
                            listener.onGetBannersSuccess(banners);
                        return;
                    } else {
                        listener.onGetBannersFailed("wrong data");
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }
    */
}
