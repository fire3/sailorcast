package com.crixmod.sailorcast.siteapi;

import android.util.Log;

import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCAlbums;
import com.crixmod.sailorcast.model.SCSite;
import com.crixmod.sailorcast.model.SCVideo;
import com.crixmod.sailorcast.utils.HttpUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.commons.lang.StringEscapeUtils;
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

    private final static String ALBUM_DESC_URL_FORMAT = "http://static.meizi.app.m.letv.com/android/mod/mob/ctl/album/act/detail/id/%s/pcode/010410000/version/2.1.mindex.html";

    private SCAlbums parseSearchResult(String result) {
        try {
            JSONObject retJson = new JSONObject(result);
            JSONObject bodyJson = retJson.optJSONObject("body");
            if(bodyJson.optInt("album_count") > 0) {
                SCAlbums albums = new SCAlbums();
                JSONArray albumList = bodyJson.optJSONArray("album_list");
                for (int i = 0; i < albumList.length(); i++) {
                    JSONObject albumJson  = albumList.getJSONObject(i);

                    //TODO: 这里过滤一下，需要存在 vidEpisode 的album才返回，其它的暂时不返回
                    //没有 vidEpisode 的album，利用 videolist 接口无法返回有效的数据。需要提前生成SCVideos。
                    if(albumJson.optJSONArray("vidEpisode").length() > 0) {

                        SCAlbum a = new SCAlbum();
                        a.setSite(SCSite.LETV);
                        if (!albumJson.optString("directory").isEmpty())
                            a.setDirector(albumJson.getString("directory"));
                        if (!albumJson.optString("starring").isEmpty())
                            a.setMainActor(albumJson.getString("starring"));
                        if (!albumJson.optString("aid").isEmpty())
                            a.setAlbumId(albumJson.getString("aid"));
                        if (!albumJson.optString("name").isEmpty())
                            a.setTitle(albumJson.getString("name"));
                        if (!albumJson.optString("categoryName").isEmpty())
                            a.setTip(albumJson.getString("categoryName"));
                        if (albumJson.optJSONObject("images") != null) {
                            JSONObject jsonImage = albumJson.getJSONObject("images");
                            if (jsonImage.optString("150*200") != null) {
                                a.setVerImageUrl(StringEscapeUtils.unescapeJava(jsonImage.getString("150*200")));
                            } else if (jsonImage.optString("300*400") != null) {
                                a.setVerImageUrl(StringEscapeUtils.unescapeJava(jsonImage.getString("300*400")));
                            } else if (jsonImage.optString("120*160") != null) {
                                a.setVerImageUrl(StringEscapeUtils.unescapeJava(jsonImage.getString("120*160")));
                            }
                        }
                        if (!albumJson.optString("episode").isEmpty()) {
                            a.setVideosTotal(Integer.getInteger(albumJson.getString("episode")));
                        }
                        if (!albumJson.optString("nowEpisodes").isEmpty()) {
                            a.setVideosCount(Integer.getInteger(albumJson.getString("nowEpisodes")));
                        }
                        if (albumJson.optString("isEnd") != null) {
                            if (albumJson.getString("isEnd").equals("1"))
                                a.setIsCompleted(true);
                            else
                                a.setIsCompleted(false);
                        }
                        albums.add(a);
                    }
                }
                return albums;
            }


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public void doSearch(String key, final OnSearchRequestListener listener) {
        try {
            String pageNo = "0";
            String pageSize = "30";
            String url = String.format(SEARCH_URL_FORMAT,URLEncoder.encode(key,"UTF-8"),pageNo,pageSize);
            HttpUtils.asyncGet(url,new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    listener.onSearchFailed("Search Failed");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String ret = response.body().string();
                    SCAlbums albums =  parseSearchResult(ret);
                    if(albums != null) {
                        albums.debugLog();
                        listener.onSearchSuccess(albums);
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

    private void fillAlbumDesc(SCAlbum album, String albumJsonString) {
        try {
            JSONObject albumJson = new JSONObject(albumJsonString);
            if(albumJson.optJSONObject("body") != null ) {
                JSONObject albumJsonBody = albumJson.optJSONObject("body");

                if(!albumJsonBody.optString("description").isEmpty())
                    album.setDesc(albumJsonBody.getString("description"));
                if(!albumJsonBody.optString("subTitle").isEmpty())
                    album.setSubTitle(albumJsonBody.getString("subTitle"));

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return;

    }

    @Override
    public void doGetAlbumDesc(final SCAlbum album, final OnGetAlbumDescListener listener) {
        String url = String.format(ALBUM_DESC_URL_FORMAT,album.getAlbumId());
        HttpUtils.asyncGet(url,new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String ret = response.body().string();
                fillAlbumDesc(album,ret);
                listener.onGetAlbumDescSuccess(album);
            }
        });

    }

    @Override
    public void doGetVideoPlayUrl(SCVideo video, OnGetVideoPlayUrlListener listener) {

    }
}
