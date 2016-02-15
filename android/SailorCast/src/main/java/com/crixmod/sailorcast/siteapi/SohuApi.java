package com.crixmod.sailorcast.siteapi;

import android.util.Log;

import com.crixmod.sailorcast.SailorCast;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCAlbums;
import com.crixmod.sailorcast.model.SCChannel;
import com.crixmod.sailorcast.model.SCChannelFilter;
import com.crixmod.sailorcast.model.SCChannelFilterItem;
import com.crixmod.sailorcast.model.SCFailLog;
import com.crixmod.sailorcast.model.SCSite;
import com.crixmod.sailorcast.model.SCVideo;
import com.crixmod.sailorcast.model.SCVideoClip;
import com.crixmod.sailorcast.model.SCVideoClips;
import com.crixmod.sailorcast.model.SCVideos;
import com.crixmod.sailorcast.model.sohu.album.Album;
import com.crixmod.sailorcast.model.sohu.searchresult.SearchResultAlbum;
import com.crixmod.sailorcast.model.sohu.searchresult.SearchResults;
import com.crixmod.sailorcast.model.sohu.videos.Video;
import com.crixmod.sailorcast.model.sohu.videos.Videos;
import com.crixmod.sailorcast.utils.HttpUtils;
import com.crixmod.sailorcast.utils.M3UServer;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.protocol.HTTP;
import org.eclipse.jetty.util.ajax.JSON;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UTFDataFormatException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

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

    private final static String API_VIDEO_INFO_FORMAT = "http://api.tv.sohu.com/v4/video/info/%s.json?" +
            "site=1&plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=4.5.1&sysver=4.4.2&partner=47&aid=%s";

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

    /*
    private void doGetRealUrls(final SCVideo video, final String vid, final OnGetVideoPlayUrlListener listener, final int videoType) {
        String url = "http://hot.vrs.sohu.com/vrs_flash.action?vid=" + vid;
        HttpUtils.asyncGet(url, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    JSONObject ret = new JSONObject(response.body().string());

                    String host = ret.optString("allot");
                    String prot = ret.optString("prot");
                    String tvid = ret.optString("tvid");

                    JSONObject retData = ret.optJSONObject("data");
                    JSONArray su = retData.getJSONArray("su");
                    JSONArray ck = retData.getJSONArray("ck");
                    JSONArray clipsURL = retData.getJSONArray("clipsURL");
                    JSONArray clipsDuration = retData.getJSONArray("clipsDuration");


                    final SCVideoClips clips = new SCVideoClips();

                    for (int i = 0; i < su.length(); i++) {

                        String s_su = su.getString(i);
                        String s_ck = ck.getString(i);
                        String s_clipURL = clipsURL.getString(i);
                        URL clipURL = new URL(s_clipURL);
                        s_clipURL = clipURL.getPath();
                        final String s_clipDuration = clipsDuration.getString(i);

                        String url = "http://" +host+ "/?prot=9&prod=flash&pt=1&file=" + s_clipURL+
                                "&new=" +s_su + "&key=" + s_ck+ "&vid=" +vid+ "&uid=" + (new Date().getTime()) +
                                "&t=" + new Random().nextDouble()  + "&rb=1";

                        String s = HttpUtils.syncGet(url);
                        JSONObject rj = new JSONObject(s);
                        String realUrl = rj.optString("url");

                        Log.i("fire3",realUrl);

                        SCVideoClip clip = new SCVideoClip(s_clipDuration,realUrl);
                        clips.add(clip);
                   }


                    if(videoType == SCVideo.QUALITY_HIGH) {
                        SailorCast.m3userver.addHighVideoClips(clips);
                        listener.onGetVideoPlayUrlHigh(video, "http://hot.vrs.sohu.com/ipad2885529_4666830478751_5096840.m3u8?plat=6&uid=42f25fed74b07c7ba5f98c262acd41db&pt=5&prod=app&pg=1");
                        //listener.onGetVideoPlayUrlHigh(video, clips.get(0).getSource());
                    }
                    if(videoType == SCVideo.QUALITY_NORMAL) {
                        SailorCast.m3userver.addNormalVideoClips(clips);
                        listener.onGetVideoPlayUrlHigh(video, "http://localhost:8080/normal");
                    }

                    if(videoType == SCVideo.QUALITY_SUPER) {
                        SailorCast.m3userver.addSuperVideoClips(clips);
                        listener.onGetVideoPlayUrlHigh(video, "http://localhost:8080/super");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    */

    private String genUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-","");
    }

    @Override
    public void doGetVideoPlayUrl(final SCVideo video, final OnGetVideoPlayUrlListener listener) {
        String url = String.format(API_VIDEO_INFO_FORMAT,video.getVideoID(),video.getAlbumID());
        HttpUtils.asyncGet(url, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    JSONObject ret = new JSONObject(response.body().string());
                    JSONObject retData = ret.optJSONObject("data");

                    String url_nor = retData.optString("url_nor");
                    if(!url_nor.isEmpty()) {
                        url_nor += "uid=" + genUUID()  + "&pt=5&prod=app&pg=1";
                        video.setM3U8Nor(url_nor);
                        listener.onGetVideoPlayUrlNormal(video, url_nor);
                    }

                    String url_high = retData.optString("url_high");
                    if(!url_high.isEmpty()) {
                        url_high += "uid=" + genUUID()  + "&pt=5&prod=app&pg=1";
                        video.setM3U8High(url_high);
                        listener.onGetVideoPlayUrlHigh(video, url_high);
                    }

                    String url_super = retData.optString("url_super");
                    if(!url_super.isEmpty()) {
                        url_super += "uid=" + genUUID() + "&pt=5&prod=app&pg=1";
                        video.setM3U8Super(url_super);
                        listener.onGetVideoPlayUrlSuper(video, url_super);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

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

}
