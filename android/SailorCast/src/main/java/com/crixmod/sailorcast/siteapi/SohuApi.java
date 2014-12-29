package com.crixmod.sailorcast.siteapi;

import com.crixmod.sailorcast.SailorCast;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCAlbums;
import com.crixmod.sailorcast.model.SCSite;
import com.crixmod.sailorcast.model.SCVideo;
import com.crixmod.sailorcast.model.sohu.searchresult.Album;
import com.crixmod.sailorcast.model.sohu.searchresult.SearchResults;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by fire3 on 14-12-26.
 */
public class SohuApi extends BaseSiteApi {
    private final static String API_KEY = "plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=4.5.0&sysver=4.4.2&partner=47";
    private final static String API_ALBUM_INFO = "http://api.tv.sohu.com/v4/album/info/" ;
    private final static String API_ALBUM_VIDOES = "http://api.tv.sohu.com/v4/album/videos/" ;
    private final static String API_CATEGORY_FILTER = "http://api.tv.sohu.com/v4/search/channel.json?";
    private final static String API_SEARCH = "http://api.tv.sohu.com/v4/search/album.json?o=&all=0&ds=&" + API_KEY + "&key=";

    @Override
    public void doSearch(String key, final OnSearchRequestListener listener) {

        String searchKey = null;
        try {
            searchKey = URLEncoder.encode(key, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            listener.onSearchFailed("Error search key");
            e.printStackTrace();
            return;
        }
        Request request = new Request.Builder()
                .url(API_SEARCH + searchKey)
                .build();
        SailorCast.getHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                listener.onSearchFailed("http failure");
            }

            @Override
            public void onResponse(Response response) {
                if (!response.isSuccessful()) {
                    listener.onSearchFailed("response failed");
                    return;
                }
                SearchResults results = SailorCast.getGson().fromJson(response.body().charStream(), SearchResults.class);
                listener.onSearchSuccess(toSCAlbums(results));
            }
        });
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

    private SCAlbums toSCAlbums(SearchResults results) {

        SCAlbums albums = new SCAlbums();
        for(Album a : results.getData().getAlbums()) {
            SCAlbum sa = new SCAlbum();
            sa.setSite(new SCSite(SCSite.SOHU));
            sa.setDesc(a.getTvDesc());
            sa.setDirector(a.getDirector());
            sa.setHorImageUrl(a.getHorHighPic());
            sa.setVerImageUrl(a.getVerHighPic());
            sa.setMainActor(a.getMainActor());
            sa.setTitle(a.getAlbumName());
            sa.setSubTitle(a.getTip());
            sa.setAlbumId(a.getAid().toString());
            albums.add(sa);
        }

        return albums;
    }
}
