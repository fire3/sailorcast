package com.crixmod.sailorcast.siteapi;

import com.crixmod.sailorcast.SailorCast;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCAlbums;
import com.crixmod.sailorcast.model.SCSite;
import com.crixmod.sailorcast.model.sohu.searchresult.Album;
import com.crixmod.sailorcast.model.sohu.searchresult.SearchResults;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

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
    public void doSearch(String key, final OnSearchRequestListener listener) throws Exception {

        String searchKey = URLEncoder.encode(key, "UTF-8");
        Request request = new Request.Builder()
                .url(API_SEARCH + searchKey)
                .build();
        SailorCast.getHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                SearchResults results = SailorCast.getGson().fromJson(response.body().charStream(), SearchResults.class);
                listener.onSearchSuccess(toSCAlbums(results));
            }
        });
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
            sa.setAlbumId((int)(long)a.getAid());
            String url = API_ALBUM_INFO + a.getAid() + ".json?" + API_KEY;
            sa.setApiUrl(url);
            albums.add(sa);
        }

        return albums;
    }
}
