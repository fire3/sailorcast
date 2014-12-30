package com.crixmod.sailorcast.siteapi;

import android.util.Log;

import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCVideo;
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

/**
 * Created by fire3 on 2014/12/30.
 */
public class LetvApi extends BaseSiteApi{


    private final static String SEARCH_URL_FORMAT = "http://dynamic.app.m.letv.com/android/dynamic.php" +
            "?mod=mob&ctl=searchmix&act=index&src=1&cg=&wd=%s&anum=&ph=&pt=&ver=&pn=%s&ps=%s&pcode=010410000&version=2.1";
    // Search arg: {keyword,pageNo(start from 0), pageSize}


    @Override
    public void doSearch(String key, OnSearchRequestListener listener) {
        try {
            String pageNo = "0";
            String pageSize = "30";
            String url = String.format(SEARCH_URL_FORMAT,URLEncoder.encode(key,"UTF-8"),pageNo,pageSize);
            HttpUtils.asyncGet(url,new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String ret = response.body().string();
                    try {
                        JSONObject retJson = new JSONObject(ret);
                        JSONObject bodyJson = retJson.optJSONObject("body");
                        if(bodyJson.optInt("album_count") > 0) {
                            JSONArray albumList = bodyJson.optJSONArray("album_list");
                            for (int i = 0; i < albumList.length(); i++) {
                                JSONObject albumJson  = albumList.getJSONObject(i);
                                Log.d("fire3", albumJson.toString());
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

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
}
