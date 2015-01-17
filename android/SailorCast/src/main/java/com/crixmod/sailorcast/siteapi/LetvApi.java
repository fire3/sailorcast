package com.crixmod.sailorcast.siteapi;

import android.util.Log;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.SailorCast;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCAlbums;
import com.crixmod.sailorcast.model.SCSite;
import com.crixmod.sailorcast.model.SCVideo;
import com.crixmod.sailorcast.model.SCVideos;
import com.crixmod.sailorcast.utils.HttpUtils;
import com.crixmod.sailorcast.utils.MD5;
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

    private final static String ALBUM_DESC_URL_FORMAT = "http://static.meizi.app.m.letv.com/" +
            "android/mod/mob/ctl/album/act/detail/id/%s/pcode/010410000/version/2.1.mindex.html";

    private final static String ALBUM_VIDEOS_URL_FORMAT = "http://static.app.m.letv.com/" +
            "android/mod/mob/ctl/videolist/act/detail/id/" +
            "%s/vid/0/b/%s/s/%s/o/%s/m/%s/pcode/010410000/version/2.1.mindex.html";
    //
    private final static String ALBUM_VIDEOS_ORDER_ASCENDING = "-1";
    private final static String ALBUM_VIDEOS_ORDER_DESCENDING = "1";

    private final static String SERVER_TIME_URL = "http://dynamic.meizi.app.m.letv.com/" +
            "android/dynamic.php?mod=mob&ctl=timestamp&act=timestamp&pcode=010410000&version=2.1";

    private final static String VIDEO_FILE_URL_FORMAT = "http://dynamic.meizi.app.m.letv.com/android/dynamic.php?mmsid=" +
            "%s&playid=0&tss=ios&pcode=010410000&version=2.1&tm=%s&key=%s&vid=" +
            "%s&ctl=videofile&mod=minfo&act=index";
    //arg: mmsid currentServerTime key vid

    private static long tmOffset = Long.MAX_VALUE;

    private final static String  VIDEO_REAL_LINK_APPENDIX = "&format=1&expect=1&termid=2&pay=0&ostype=android&hwtype=iphone";

    private final static int QUALITY_NORMAL = 1;
    private final static int QUALITY_HIGH = 2;
    private final static int QUALITY_SUPER = 3;

    public LetvApi() {
        doUpdateTmOffset();
    }

    private synchronized void updateTmOffset(int serverTime) {
        if(tmOffset == Long.MAX_VALUE) {
            tmOffset = (System.currentTimeMillis() / 1000) - serverTime;
        }
    }

    private String getCurrentServerTime() {
        if(tmOffset != Long.MAX_VALUE)
            return ""+ (System.currentTimeMillis()/1000 - tmOffset);
        else
            return null;
    }

    private synchronized void doUpdateTmOffset() {
        String url = SERVER_TIME_URL;

        if(tmOffset != Long.MAX_VALUE) {
            return;
        }

        HttpUtils.asyncGet(url,new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    String serverTime = json.getString("time");
                    updateTmOffset(Integer.parseInt(serverTime));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String generateVideoFileKey(SCVideo video, String currentServerTime)
    {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(video.getLetvVideoMID());
        localStringBuilder.append(",");
        localStringBuilder.append(currentServerTime);
        localStringBuilder.append(",");
        localStringBuilder.append("bh65OzqYYYmHRQ");
        return MD5.toMd5(localStringBuilder.toString());
    }

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
                    //if(albumJson.optJSONArray("vidEpisode").length() > 0) {

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
                            if (jsonImage.optString("150*200") != null && !jsonImage.optString("150*200").isEmpty()) {
                                a.setVerImageUrl(StringEscapeUtils.unescapeJava(jsonImage.getString("150*200")));
                            } else if (jsonImage.optString("300*400") != null && !jsonImage.optString("300*400").isEmpty()) {
                                a.setVerImageUrl(StringEscapeUtils.unescapeJava(jsonImage.getString("300*400")));
                            } else if (jsonImage.optString("120*160") != null && !jsonImage.optString("120*160").isEmpty()) {
                                a.setVerImageUrl(StringEscapeUtils.unescapeJava(jsonImage.getString("120*160")));
                            }
                        }
                        albums.add(a);
                    //}
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
                    } else
                        listener.onSearchFailed(SailorCast.getResource().getString(R.string.fail_reason_no_results));
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void doGetAlbumVideos(final SCAlbum album, final int pageNo, final int pageSize, final OnGetVideosListener listener) {
        String order = ALBUM_VIDEOS_ORDER_ASCENDING;
        String url;
        if(album.getLetvStyle().equals("2"))
            url = String.format(ALBUM_VIDEOS_URL_FORMAT,album.getAlbumId(),
                    Integer.toString(pageNo),Integer.toString(pageSize),order,"1");
        else
            url = String.format(ALBUM_VIDEOS_URL_FORMAT,album.getAlbumId(),
                    Integer.toString(pageNo),Integer.toString(pageSize),order,"0");

        HttpUtils.asyncGet(url,new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String ret = response.body().string();
                try {
                    JSONObject retJson = new JSONObject(ret);
                    if(retJson.optJSONObject("body") != null) {
                        JSONObject jsonBody = retJson.getJSONObject("body");
                        JSONArray jsonArray = jsonBody.optJSONArray("videoInfo");
                        if(jsonArray != null && jsonArray.length() > 0) {
                            SCVideos videos = new SCVideos();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject j = jsonArray.getJSONObject(i);
                                SCVideo v = new SCVideo();
                                v.setAlbumID(album.getAlbumId());
                                v.setSCSite(album.getSite().getSiteID());
                                if(!j.optString("nameCn").isEmpty())
                                    v.setVideoTitle(j.getString("nameCn")  );
                                if(!j.optString("id").isEmpty())
                                    v.setVideoID(j.getString("id"));
                                JSONObject p = j.optJSONObject("picAll");
                                if(p != null) {
                                    if(!p.optString("200*150").isEmpty())
                                        v.setHorPic(p.getString("200*150"));
                                    else if(!p.optString("320*200").isEmpty())
                                        v.setHorPic(p.getString("320*200"));
                                    else if(!p.optString("120*90").isEmpty())
                                        v.setHorPic(p.getString("120*90"));
                                }

                                v.setSeqInAlbum(pageNo * pageSize + i);
                                //MID设置是Letv解析真实链接必须的。
                                if(!j.optString("mid").isEmpty())
                                    v.setLetvVideoMID(j.optString("mid"));

                                videos.add(v);
                            }
                            listener.onGetVideosSuccess(videos);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
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
                if(!albumJsonBody.optString("style").isEmpty())
                    album.setLetvStyle(albumJsonBody.getString("style"));
                if (!albumJsonBody.optString("platformVideoNum").isEmpty()) {
                    album.setVideosTotal(Integer.parseInt(albumJsonBody.getString("platformVideoNum")));
                }
                if (!albumJsonBody.optString("platformVideoInfo").isEmpty()) {
                    album.setVideosCount(Integer.parseInt(albumJsonBody.getString("platformVideoInfo")));
                }
                if(album.getVideosCount() == 0)
                    album.setVideosCount(album.getVideosTotal());
                if (albumJsonBody.optString("isEnd") != null) {
                    if (albumJsonBody.getString("isEnd").equals("1"))
                        album.setIsCompleted(true);
                    else
                        album.setIsCompleted(false);
                }
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

    private void getRealLink(final SCVideo video, final OnGetVideoPlayUrlListener listener, String jumpLink, final int quality) {

        HttpUtils.asyncGet(jumpLink, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String ret = response.body().string();
                try {
                    JSONObject retJson = new JSONObject(ret);
                    String location = retJson.optString("location");
                    if(quality == QUALITY_SUPER) {
                        video.setM3U8Super(location);
                        listener.onGetVideoPlayUrlSuper(video,location);
                    }
                    if(quality == QUALITY_HIGH) {
                        video.setM3U8High(location);
                        listener.onGetVideoPlayUrlHigh(video,location);
                    }
                    if(quality == QUALITY_NORMAL) {
                        video.setM3U8Nor(location);
                        listener.onGetVideoPlayUrlNormal(video,location);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public void doGetVideoPlayUrl(final SCVideo video, final OnGetVideoPlayUrlListener listener) {
        if(tmOffset != Long.MAX_VALUE) {
            String currentTm = getCurrentServerTime();
            String url = String.format(VIDEO_FILE_URL_FORMAT, video.getLetvVideoMID(),
                    currentTm, generateVideoFileKey(video,currentTm),video.getVideoID());
            HttpUtils.asyncGet(url,new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String ret = response.body().string();
                    try {
                        JSONObject retJson = new JSONObject(ret);
                        JSONObject body = retJson.getJSONObject("body").getJSONObject("videofile").getJSONObject("infos");

                        if(body.optJSONObject("mp4_350") != null) {
                            //Normal
                            JSONObject mp4 = body.optJSONObject("mp4_350");
                            //这里优先选择mainUrl即可
                            String mp4Url = null;
                            if(mp4.optString("mainUrl") != null) {
                                mp4Url = mp4.optString("mainUrl") + VIDEO_REAL_LINK_APPENDIX ;
                            } else if (mp4.optString("backUrl0") != null) {
                                mp4Url = mp4.optString("backUrl0") + VIDEO_REAL_LINK_APPENDIX ;
                            } else if (mp4.optString("backUrl1") != null) {
                                mp4Url = mp4.optString("backUrl1") + VIDEO_REAL_LINK_APPENDIX ;
                            } else if (mp4.optString("backUrl2") != null) {
                                mp4Url = mp4.optString("backUrl2") + VIDEO_REAL_LINK_APPENDIX ;
                            }
                            if(mp4Url != null)
                                getRealLink(video,listener,mp4Url,QUALITY_NORMAL);
                        }
                        if(body.optJSONObject("mp4_1000") != null) {
                            //High
                            JSONObject mp4 = body.optJSONObject("mp4_1000");
                            String mp4Url = null;
                            if(mp4.optString("mainUrl") != null) {
                                mp4Url = mp4.optString("mainUrl") + VIDEO_REAL_LINK_APPENDIX ;
                            } else if (mp4.optString("backUrl0") != null) {
                                mp4Url = mp4.optString("backUrl0") + VIDEO_REAL_LINK_APPENDIX ;
                            } else if (mp4.optString("backUrl1") != null) {
                                mp4Url = mp4.optString("backUrl1") + VIDEO_REAL_LINK_APPENDIX ;
                            } else if (mp4.optString("backUrl2") != null) {
                                mp4Url = mp4.optString("backUrl2") + VIDEO_REAL_LINK_APPENDIX ;
                            }
                            if(mp4Url != null)
                                getRealLink(video,listener,mp4Url,QUALITY_HIGH);

                        }
                        if(body.optJSONObject("mp4_1300") != null) {
                            //Super
                            JSONObject mp4 = body.optJSONObject("mp4_1300");
                            String mp4Url = null;
                            if(mp4.optString("mainUrl") != null) {
                                mp4Url = mp4.optString("mainUrl") + VIDEO_REAL_LINK_APPENDIX ;
                            } else if (mp4.optString("backUrl0") != null) {
                                mp4Url = mp4.optString("backUrl0") + VIDEO_REAL_LINK_APPENDIX ;
                            } else if (mp4.optString("backUrl1") != null) {
                                mp4Url = mp4.optString("backUrl1") + VIDEO_REAL_LINK_APPENDIX ;
                            } else if (mp4.optString("backUrl2") != null) {
                                mp4Url = mp4.optString("backUrl2") + VIDEO_REAL_LINK_APPENDIX ;
                            }
                            if(mp4Url != null)
                                getRealLink(video,listener,mp4Url,QUALITY_SUPER);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
