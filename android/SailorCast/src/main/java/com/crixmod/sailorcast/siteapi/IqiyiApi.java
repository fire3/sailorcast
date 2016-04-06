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
import com.crixmod.sailorcast.model.SCVideos;
import com.crixmod.sailorcast.utils.HttpUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;
import java.util.UUID;


/**
 * Created by fire3 on 15-2-3.
 */
public class IqiyiApi extends BaseSiteApi {

    private final static String TAG = "IqiyiApi";
    private static final int GALAXY_SECRET_KEY_ONE = 1111111727;
    private static final String GALAXY_SECRET_KEY_TWO = "D9g6XYm(B-:o1nu|";
    private static final int MORE_SECRET_KEY_ONE = 1100016699;
    private static final String MORE_SECRET_KEY_TWO = "m)*ra772e";
    private static final int NEXT_SECRET_KEY_ONE = 1770021100;
    private static final String NEXT_SECRET_KEY_TWO = "m9192nck:_77";
    private static final int PLAY_SECRET_KEY_ONE = 1121111727;
    private static final String PLAY_SECRET_KEY_TWO = ",rI1:?CJczS3AwJ$";


    private final static int CID_MOVIE = 1;
    private final static int CID_SHOW = 2;
    private final static int CID_COMIC = 4;
    private final static int CID_VARIETY = 6;
    private final static int CID_ENT = 7;
    private final static int CID_DOCUMENTARY = 3;
    private final static int CID_MUSIC = 5;
    private final static int CID_SPORT = 17;
    private final static String IQIYI_SALT = "4a1caba4b4465345366f28da7c117d20" ;

    private final static String SEARCH_URL_FORMAT = "http://iface.iqiyi.com/api/searchIface?key=2019620214d1a82fc76d0b4b3c6fa685" +
            "&all_episode=-1&need_video_img=0" +
            "&keyword=%s&category_id=0&" +
            "type=json&page_number=1&page_size=30&sort=6&version=5.9.1";

    private final static String ALBUM_VIDEOS_FORMAT = "http://cache.video.qiyi.com/jp/avlist/%s/%d/%d/?albumId=%s&pageNo=%d&pageNum=%d";
    private final static String IQIYI_MKEY = "317e617581c95c3e8a996f8bff69607b";

    private final static String ALBUM_VIDEOS_NEBULA_FORMAT = "http://iface2.iqiyi.com/php/xyz/entry/nebula.php?key=" +
            "317e617581c95c3e8a996f8bff69607b&version=5.3.1&uniqid=%s&platform=GPad&block=0&w=1&compat=1&other=1&v5=1&ad_str=1&many_id=%s_0_";

    private final static String CHANNEL_ALBUMS_FORMAT = "http://iface2.iqiyi.com/php/xyz/entry/galaxy.php?" +
            "key=317e617581c95c3e8a996f8bff69607b&version=5.3.1&category_id=%s&" +
            "f_ps=10&s=6&pn=%d&ps=%d&pcat=2&hwd=1&v5=1&compat=1&platform=GPad&f=1&uniqid=%s";

    private final static String CHANNEL_FILTER_URL = "http://iface2.iqiyi.com/st/nav/1.2.json";

    private String getDefaultChannelUrl(SCChannel channel, int pageNo, int pageSize) {
        String url = null;
        switch (channel.getChannelID()) {
            case SCChannel.MOVIE:
                url = String.format(CHANNEL_ALBUMS_FORMAT,"1,0~0~0~120002",pageNo,pageSize,genUUID());
                break;
            case SCChannel.SHOW:
                url = String.format(CHANNEL_ALBUMS_FORMAT,"2,0~0~0",pageNo,pageSize,genUUID());
                break;
            case SCChannel.COMIC:
                url = String.format(CHANNEL_ALBUMS_FORMAT,"4,0~0~0",pageNo,pageSize,genUUID());
                break;
            case SCChannel.VARIETY:
                url = String.format(CHANNEL_ALBUMS_FORMAT,"6,0~0",pageNo,pageSize,genUUID());
                break;
            case SCChannel.DOCUMENTARY:
                url = String.format(CHANNEL_ALBUMS_FORMAT,"3,0",pageNo,pageSize,genUUID());
                break;
            case SCChannel.MUSIC:
                url = String.format(CHANNEL_ALBUMS_FORMAT,"5,0~0~0",pageNo,pageSize,genUUID());
                break;
        }
        return url;
    }

    private  Hashtable<String, String> getSignedHeader(String paramString1, int paramInt, String paramString2)
    {
        long l1 = System.currentTimeMillis() / 1000L;
        long l2 = l1 ^ paramInt;
        String str = md5(l1 + paramString2 + paramString1 + "5.3.1");
        Hashtable localHashtable = new Hashtable();
        localHashtable.put("t", "" + l2);
        localHashtable.put("sign", str);
        return localHashtable;
    }


    private String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private SCFailLog makeHttpFailLog(String url, String functionName, Exception e) {
        SCFailLog err = new SCFailLog(SCSite.IQIYI,SCFailLog.TYPE_HTTP_FAILURE);
        err.setException(e);
        err.setFunctionName(functionName);
        err.setClassName("IqiyiApi");
        err.setTag(TAG);
        err.setUrl(url);
        return err;
    }

    private SCFailLog makeFatalFailLog(String url, String functionName, Exception e) {
        SCFailLog err = new SCFailLog(SCSite.IQIYI,SCFailLog.TYPE_FATAL_ERR);
        err.setException(e);
        err.setFunctionName(functionName);
        err.setClassName("IqiyiApi");
        err.setTag(TAG);
        err.setUrl(url);
        return err;
    }

    private SCFailLog makeFatalFailLog(String url, String functionName) {
        SCFailLog err = new SCFailLog(SCSite.IQIYI,SCFailLog.TYPE_FATAL_ERR);
        err.setFunctionName(functionName);
        err.setClassName("IqiyiApi");
        err.setTag(TAG);
        err.setUrl(url);
        return err;
    }

    private SCFailLog makeNoResultFailLog(String url, String functionName) {
        SCFailLog err = new SCFailLog(SCSite.IQIYI,SCFailLog.TYPE_NO_RESULT);
        err.setFunctionName(functionName);
        err.setClassName("IqiyiApi");
        err.setTag(TAG);
        err.setUrl(url);
        return err;
    }


    @Override
    public void doSearch(String key, final OnGetAlbumsListener listener) {
        String encodeKey = null;
        try {
            encodeKey = URLEncoder.encode(key, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        final String url = String.format(SEARCH_URL_FORMAT,encodeKey);
        HttpUtils.asyncGet(url, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                if (listener != null) {
                    SCFailLog err = makeHttpFailLog(url, "doSearch", e);
                    listener.onGetAlbumsFailed(err);
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String ret = response.body().string();
                try {
                    JSONObject retJson = new JSONObject(ret);

                    JSONArray albumsArray = retJson.optJSONObject("response").
                            optJSONObject("result").
                            optJSONObject("albums").
                            optJSONArray("album");
                    if (albumsArray != null && albumsArray.length() > 0) {
                        SCAlbums albums = new SCAlbums();
                        for (int i = 0; i < albumsArray.length(); i++) {
                            JSONObject albumJson = albumsArray.getJSONObject(i);
                            int category_id = albumJson.optInt("category_id");
                            if (category_id < 7) {
                                // 排除一些无聊的频道
                                int purchase_type = albumJson.optInt("purchase_type");
                                // 排除 purchase_type = 1 的vip视频
                                if (purchase_type == 0) {
                                    SCAlbum album = new SCAlbum(SCSite.IQIYI);
                                    String albumName = albumJson.optString("title");
                                    album.setTitle(albumName);
                                    String albumImage = albumJson.optString("img");
                                    album.setVerImageUrl(albumImage);
                                    String albumDesc = albumJson.optString("desc");
                                    album.setDesc(albumDesc);
                                    String albumDirector = albumJson.optString("directors");
                                    album.setDirector(albumDirector);
                                    String albumActor = albumJson.optString("mainactors");
                                    album.setMainActor(albumActor);
                                    String albumTip = albumJson.optString("tv_focus");
                                    album.setTip(albumTip);
                                    int albumTotal = albumJson.optInt("episode_count");
                                    album.setVideosTotal(albumTotal);
                                    String albumId = String.valueOf(albumJson.optInt("album_id"));
                                    album.setAlbumId(albumId);
                                    albums.add(album);
                                }
                            }

                        }
                        if (listener != null)
                            listener.onGetAlbumsSuccess(albums);
                    } else {
                        if (listener != null) {
                            SCFailLog err = makeNoResultFailLog(url, "doSearch");
                            err.setReason(ret);
                            listener.onGetAlbumsFailed(err);
                        }
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        SCFailLog err = makeFatalFailLog(url, "doSearch", e);
                        err.setReason(ret);
                        listener.onGetAlbumsFailed(err);
                    }
                    e.printStackTrace();
                }

            }
        });
    }

    private Hashtable<String, String> getGalaxyHeader() {
        return getSignedHeader(IQIYI_MKEY, GALAXY_SECRET_KEY_ONE, GALAXY_SECRET_KEY_TWO);
    }

    private Hashtable<String, String> getNebulaHeader() {
        Hashtable<String,String> head = getSignedHeader(IQIYI_MKEY, PLAY_SECRET_KEY_ONE, PLAY_SECRET_KEY_TWO);
        return head;
    }

    private void doGetAlbumVideosPcMethod(final SCAlbum album, int pageNo, int pageSize, final OnGetVideosListener listener) {
         final String url = String.format(ALBUM_VIDEOS_FORMAT, album.getAlbumId(), pageNo, pageSize, album.getAlbumId(), pageNo, pageSize);

        HttpUtils.asyncGet(url, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                SCFailLog err = makeHttpFailLog(url,"doGetAlbumVideosPcMethod",e);
                if(listener != null) {
                    listener.onGetVideosFailed(err);
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String ret = response.body().string().substring(13);
                try {
                    JSONObject retJson = new JSONObject(ret);
                    String code = retJson.optString("code");
                    if(code.equals("A00000")) {
                        JSONObject retData = retJson.optJSONObject("data");
                        JSONArray vList = retData.optJSONArray("vlist");
                        if(vList.length() > 0) {
                            SCVideos videos = new SCVideos();

                            for (int i = 0; i < vList.length(); i++) {
                                JSONObject vJson = vList.getJSONObject(i);

                                SCVideo v = new SCVideo();
                                v.setAlbumID(album.getAlbumId());
                                v.setSCSite(SCSite.IQIYI);
                                String title = vJson.optString("vn");
                                String shortTitle = vJson.optString("shortTitle");
                                if(title != null && !title.isEmpty())
                                    v.setVideoTitle(title);
                                else if (shortTitle != null && !shortTitle.isEmpty()){
                                    v.setVideoTitle(shortTitle);
                                }

                                int pd = vJson.optInt("pd");
                                v.setSeqInAlbum(pd);

                                String vUrl = vJson.optString("vurl");
                                v.setIqiyiVideoURL(vUrl);

                                String id = vJson.optString("id");
                                v.setVideoID(id);

                                String vid = vJson.optString("vid");
                                v.setIqiyiVid(vid);

                                String vpic = vJson.optString("vpic");
                                v.setHorPic(vpic);

                                videos.add(v);
                            }

                            if(listener != null) {
                                listener.onGetVideosSuccess(videos);
                            }
                        }

                    } else {
                        if(listener != null) {
                            SCFailLog err = makeNoResultFailLog(url,"doGetAlbumVideosPcMethod");
                            listener.onGetVideosFailed(err);
                        }
                    }
                } catch (Exception e) {
                    SCFailLog err = makeFatalFailLog(url, "doGetAlbumVideosPcMethod", e);
                    if(listener != null) {
                        listener.onGetVideosFailed(err);
                    }
                    e.printStackTrace();
                }
            }
        });
    }

    private void doGetAlbumVideosNebulaMethod(final SCAlbum album, int pageNo, int pageSize, final OnGetVideosListener listener) {
        final String url = String.format(ALBUM_VIDEOS_NEBULA_FORMAT,genUUID(),album.getAlbumId());
        Hashtable<String, String> head = getNebulaHeader();

        Request request = new Request.Builder().url(url)
                //.addHeader("Accept-Encoding", "gzip")
                .addHeader("t", head.get("t"))
                .addHeader("sign",head.get("sign"))
                .build();
        HttpUtils.asyncGet(request,new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                SCFailLog err = makeHttpFailLog(url,"doGetAlbumVideosNebulaMethod",e);
                if(listener != null) {
                    listener.onGetVideosFailed(err);
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String ret = response.body().string();
                try {
                    JSONObject retJson = new JSONObject(ret);

                    JSONObject tvJson = retJson.optJSONObject("tv").optJSONObject("0");
                    SCVideos videos = new SCVideos();

                    SCVideo v = new SCVideo();
                    v.setAlbumID(album.getAlbumId());
                    v.setSCSite(SCSite.IQIYI);
                    v.setSeqInAlbum(1);
                    v.setVideoTitle(tvJson.optString("_n"));
                    v.setHorPic(tvJson.optString("_img"));
                    v.setVideoID(tvJson.optString("_id"));
                    v.setIqiyiVid(tvJson.optString("_v"));

                    videos.add(v);

                    if(listener != null) {
                        listener.onGetVideosSuccess(videos);
                    }

                } catch (Exception e) {

                    SCFailLog err = makeFatalFailLog(url, "doGetAlbumVideosNebulaMethod", e);
                    if(listener != null) {
                        listener.onGetVideosFailed(err);
                    }
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public void doGetAlbumVideos(final SCAlbum album, int pageNo, int pageSize, final OnGetVideosListener listener) {
        if(album.getVideosTotal() == 1)
            doGetAlbumVideosNebulaMethod(album,pageNo,pageSize,listener);
        else
            doGetAlbumVideosPcMethod(album, pageNo, pageSize, listener);
    }

    @Override
    public void doGetAlbumDesc(final SCAlbum album, final OnGetAlbumDescListener listener) {

        final String url = String.format(ALBUM_VIDEOS_NEBULA_FORMAT, genUUID(), album.getAlbumId());
        Hashtable<String, String> head = getNebulaHeader();

        Request request = new Request.Builder().url(url)
                //.addHeader("Accept-Encoding", "gzip")
                .addHeader("t", head.get("t"))
                .addHeader("sign",head.get("sign"))
                .build();


        HttpUtils.asyncGet(request,new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                SCFailLog err = makeHttpFailLog(url, "doGetAlbumDesc", e);
                if(listener != null) {
                    listener.onGetAlbumDescFailed(err);
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String ret = response.body().string();
                try {
                    JSONObject retJson = new JSONObject(ret);

                    album.setDesc(retJson.optString("desc"));
                    album.setDirector(retJson.optString("_da"));
                    album.setMainActor(retJson.optString("_ma"));


                    // 电影频道的很多数据在上一个步骤取回来 getVideosTotal 为0，这里补充处理一下，猜测利用 _tvct 数据作为
                    // album 中videos Total count。
                    // 另外，电影频道需要使用nebula接口获取video信息，不能使用pc方法。
                    int tvct = retJson.optInt("_tvct");
                    if(album.getVideosTotal() == 0 &&  tvct > 0)
                        album.setVideosTotal(tvct);


                    if(listener != null) {
                        listener.onGetAlbumDescSuccess(album);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    SCFailLog err = makeFatalFailLog(url, "doGetAlbumDesc", e);
                    if(listener != null) {
                        listener.onGetAlbumDescFailed(err);
                    }
                }

            }
        });



    }

    private String genUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-","");
    }

    private String getVideoVMSURL(SCVideo video) {
        String uid = genUUID();
        Random r = new Random();
        String tm =String.format("%d",((r.nextInt(4000-2000))+2000));
        String enc = md5(IQIYI_SALT + tm + video.getVideoID());
        String tn = String.valueOf(r.nextDouble());
        String authkey = md5(""+tm+video.getVideoID());
        String tvid = video.getVideoID();
        String vid = video.getIqiyiVid();
        String vmsreq="http://cache.video.qiyi.com/vms?key=fvip&src=1702633101b340d8917a69cf8a4b8c7" +
                "&tvId="+tvid+"&vid="+vid+"&vinfo=1&tm="+tm+
                "&enc="+enc+
                "&qyid="+uid+"&tn="+tn +"&um=1" +
                "&authkey="+authkey;

        return vmsreq;

    }

    private String getRealM3U8(String m3u8Url) {
        String FORMAT = "http://cache.m.iqiyi.com/dc/dt/mobile/%s?qd_src=5be6a2fdfe4f4a1a8c7b08ee46a18887";
        return String.format(FORMAT,m3u8Url);
    }

    @Override
    public void doGetVideoPlayUrl(final SCVideo video, final OnGetVideoPlayUrlListener listener) {

        final String url = getVideoVMSURL(video);

        HttpUtils.asyncGet(url,new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                SCFailLog err = makeHttpFailLog(url, "doGetVideoPlayUrl", e);
                if(listener != null) {
                    listener.onGetVideoPlayUrlFailed(err);
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String ret = response.body().string();
                try {
                    JSONObject retJson = new JSONObject(ret);
                    String code = retJson.optString("code");
                    if(code.equals("A000000")) {
                        JSONArray vs = retJson.optJSONObject("data")
                                .optJSONObject("vp")
                                .optJSONArray("tkl")
                                .getJSONObject(0)
                                .optJSONArray("vs");

                        /*
                            bid 决定清晰度
                            bid == 96 流畅
                            bid == 1 普通
                            bid == 2 高清
                            bid == 3 超清
                            bid == 4 超高清
                            bid == 5 FULL_HD
                            bid == 10 4K
                         */
                        Integer bids[] = new Integer[vs.length()];
                        HashMap<Integer,Integer> bidMap = new HashMap<Integer, Integer>();

                        for (int i = 0; i < vs.length(); i++) {
                            JSONObject v = vs.getJSONObject(i);
                            int bid = v.optInt("bid");
                            bids[i] = bid;
                            bidMap.put(bid, i);
                        }
                        Arrays.sort(bids);
                        /*这里采用倒序，最高的bid设置为Super */
                        int count = 0;
                        for (int i = (bids.length - 1); i >=0; i--) {
                            int bid = bids[i];
                            int index = bidMap.get(bid);
                            JSONObject v = vs.getJSONObject(index);
                            String m3u8 = v.optString("m3u8Url");
                            if(bid > 10)
                                continue;
                            if(count == 4)
                                break;
                            if(count == 0) {
                                if(m3u8!=null && !m3u8.isEmpty()) {
                                    String realM3U8 = getRealM3U8(m3u8);
                                    video.setM3U8Super(realM3U8);
                                    if(listener != null)
                                        listener.onGetVideoPlayUrlSuper(video,realM3U8);
                                }
                            }
                            if(count == 1) {
                                if(m3u8!=null && !m3u8.isEmpty()) {
                                    String realM3U8 = getRealM3U8(m3u8);
                                    video.setM3U8Nor(realM3U8);
                                    if(listener != null)
                                        listener.onGetVideoPlayUrlHigh(video,realM3U8);
                                }
                            }
                            if(count == 2) {
                                if(m3u8!=null && !m3u8.isEmpty()) {
                                    String realM3U8 = getRealM3U8(m3u8);
                                    video.setM3U8Nor(realM3U8);
                                    if(listener != null)
                                        listener.onGetVideoPlayUrlNormal(video,realM3U8);
                                }
                            }
                            count++;
                        }
                    } else {
                        //错误情况
                        SCFailLog err = makeNoResultFailLog(url, "doGetVideoPlayUrl");
                        if(listener != null) {
                            listener.onGetVideoPlayUrlFailed(err);
                        }
                    }
                } catch (Exception e) {
                    //错误情况
                    e.printStackTrace();
                    SCFailLog err = makeFatalFailLog(url, "doGetVideoPlayUrl", e);
                    if(listener != null) {
                        listener.onGetVideoPlayUrlFailed(err);
                    }
                }
            }
        });
    }

    public void getChannelAlbumsByUrl(final String url, final OnGetAlbumsListener listener) {
                Hashtable<String, String> head = getGalaxyHeader();

        Request request = new Request.Builder().url(url)
                //.addHeader("Accept-Encoding", "gzip")
                .addHeader("t", head.get("t"))
                .addHeader("sign",head.get("sign"))
                .build();

        HttpUtils.asyncGet(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                SCFailLog err = makeHttpFailLog(url, "getChannelAlbumsByUrl", e);
                if (listener != null) {
                    listener.onGetAlbumsFailed(err);
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String ret = response.body().string();
                try {
                    JSONObject retJson = new JSONObject(ret);
                    JSONArray albumIdList = retJson.optJSONArray("albumIdList").getJSONObject(0).optJSONArray("idlist");
                    JSONObject albumArray = retJson.optJSONObject("albumArray");
                    SCAlbums albums = new SCAlbums();
                    for (int i = 0; i < albumIdList.length(); i++) {
                        String id = albumIdList.optString(i);
                        JSONObject albumJson = albumArray.optJSONObject(id);
                        //有时会出现"_a"这个对象，剧集信息会放在这个里面，比如动漫栏目
                        if (albumJson.has("_a"))
                            albumJson = albumJson.optJSONObject("_a");
                        if (albumJson != null) {
                            SCAlbum album = new SCAlbum(SCSite.IQIYI);

                            String albumID = id;
                            album.setAlbumId(albumID);

                            String title = albumJson.optString("_t");
                            album.setTitle(title);

                            String h1Image = albumJson.optString("h1_img");
                            String h2Image = albumJson.optString("h2_img");
                            String h3Image = albumJson.optString("h3_img");
                            if (h1Image != null && !h1Image.isEmpty())
                                album.setVerImageUrl(h1Image);
                            else if (h2Image != null && !h2Image.isEmpty())
                                album.setVerImageUrl(h2Image);
                            else if (h3Image != null && !h3Image.isEmpty())
                                album.setVerImageUrl(h3Image);

                            String v1Image = albumJson.optString("v1_img");
                            String v2Image = albumJson.optString("v2_img");
                            String v3Image = albumJson.optString("v3_img");
                            if (v1Image != null && !v1Image.isEmpty())
                                album.setHorImageUrl(v1Image);
                            else if (v2Image != null && !v2Image.isEmpty())
                                album.setHorImageUrl(v2Image);
                            else if (v3Image != null && !v3Image.isEmpty())
                                album.setHorImageUrl(v3Image);

                            String tip = albumJson.optString("tvfcs");
                            album.setTip(tip);

                            int _tvs = albumJson.optInt("_tvs");
                            album.setVideosTotal(_tvs);

                            albums.add(album);
                        }
                    }
                    if (albums.size() > 0) {
                        if (listener != null) {
                            listener.onGetAlbumsSuccess(albums);
                        }
                    } else if (albums.size() == 0) {
                        SCFailLog err = makeNoResultFailLog(url, "getChannelAlbumsByUrl");
                        if (listener != null) {
                            listener.onGetAlbumsFailed(err);
                        }
                    }
                } catch (Exception e) {

                    SCFailLog err = makeFatalFailLog(url, "getChannelAlbumsByUrl", e);
                    if (listener != null) {
                        listener.onGetAlbumsFailed(err);
                    }
                    e.printStackTrace();
                }

            }
        });
    }
    @Override
    public void doGetChannelAlbums(SCChannel channel, int pageNo, int pageSize, final OnGetAlbumsListener listener) {
        String url = getDefaultChannelUrl(channel, pageNo, pageSize);
        if(url == null) {
            return;
        }
        getChannelAlbumsByUrl(url, listener);
    }


    private String getAlbumListUrlByFilter(SCChannel channel, int pageNo, int pageSize, SCChannelFilter filter) {
        String filterString = "" + channelToCateID(channel) + ",";
        ArrayList<SCChannelFilterItem> items = filter.getSelectedItems();
        int[] keys = new int[items.size()];
        HashMap<Integer,Integer> map = new HashMap<>();

        for (int i = 0; i < items.size(); i++) {
            SCChannelFilterItem item = items.get(i);
            int searchKey = Integer.parseInt(item.getSearchKey());
            keys[i] = searchKey;
            map.put(searchKey,i);
        }

        Arrays.sort(keys);
        for (int i = 0; i < keys.length; i++) {
            int pos = map.get(keys[i]);
            SCChannelFilterItem item = items.get(pos);
            if(item.getSearchVal() != null)
                filterString = filterString + item.getSearchVal() + "~";
        }

        // delete last "~"
        if (filterString.length() > 0 && filterString.charAt(filterString.length()-1)=='~') {
            filterString = filterString.substring(0, filterString.length()-1);
        }

        return String.format(CHANNEL_ALBUMS_FORMAT,filterString,pageNo,pageSize,genUUID());
    }

    @Override
    public void doGetChannelAlbumsByFilter(SCChannel channel, int pageNo, int pageSize, SCChannelFilter filter, OnGetAlbumsListener listener) {
        String url = getAlbumListUrlByFilter(channel,pageNo,pageSize,filter);
        getChannelAlbumsByUrl(url,listener);

    }

    private int channelToCateID(SCChannel channel) {
        switch (channel.getChannelID()) {
            case SCChannel.MOVIE:
                return CID_MOVIE;
            case SCChannel.MUSIC:
                return CID_MUSIC;
            case SCChannel.DOCUMENTARY:
                return CID_DOCUMENTARY;
            case SCChannel.VARIETY:
                return CID_VARIETY;
            case SCChannel.COMIC:
                return CID_COMIC;
            case SCChannel.SHOW:
                return CID_SHOW;
        }
        return 0;
    }

    private void doParseChannelFilter(final SCChannel channel, String ret, final OnGetChannelFilterListener listener) {
        try {
            JSONArray retJson =  new JSONArray(ret);
            for (int i = 0; i < retJson.length(); i++) {
                JSONArray catList = retJson.getJSONObject(i).optJSONArray("cateList");
                for (int j = 0; j < catList.length(); j++) {
                    JSONObject cateJson =  catList.getJSONObject(j);
                    String cat = cateJson.optString("catId");
                    int catId =  Integer.parseInt(cat);
                    int cid = channelToCateID(channel);
                    if(catId == cid) {
                        JSONArray subList = cateJson.optJSONArray("subList");
                        SCChannelFilter filter = new SCChannelFilter();

                        for (int k = 0; k < subList.length(); k++) {

                            ArrayList<SCChannelFilterItem> items = new ArrayList<SCChannelFilterItem>();

                            String subID = subList.getJSONObject(k).getString("subId");
                            String subName = subList.getJSONObject(k).getString("subName");

                            JSONArray leafList = subList.getJSONObject(k).getJSONArray("leafList");
                            for (int l = 0; l < leafList.length() ; l++) {
                                if(subID.equals("7") && subName.equals("是否付费")) {
                                    String leafName = leafList.getJSONObject(l).optString("leafName");
                                    String leafKey = leafList.getJSONObject(l).optString("leafId");
                                    if(leafName.equals("免费")) {
                                        SCChannelFilterItem item = new SCChannelFilterItem(leafKey, leafName);
                                        item.setSearchKey(""+k);
                                        item.setParentKey(""+k);
                                        items.add(item);
                                    }
                                } else {
                                    String leafName = leafList.getJSONObject(l).optString("leafName");
                                    String leafKey = leafList.getJSONObject(l).optString("leafId");
                                    SCChannelFilterItem item = new SCChannelFilterItem(leafKey, leafName);
                                    item.setSearchKey(""+k);
                                    item.setParentKey(""+k);
                                    items.add(item);
                                }

                            }

                            filter.addFilter(""+k,items);
                        }

                        if(listener != null) {
                            listener.onGetChannelFilterSuccess(filter);
                        }

                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            SCFailLog err = makeFatalFailLog("doGetChannelFilter", "doGetChannelFilter", e);
            if(listener != null) {
                listener.onGetChannelFilterFailed(err);
            }
        }
    }

    private String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {

        }
        return outputStream.toString();
    }
    @Override
    public void doGetChannelFilter(final SCChannel channel, final OnGetChannelFilterListener listener) {
        final String url = CHANNEL_FILTER_URL;
        InputStream ins = SailorCast.getContext().getResources().openRawResource(
                SailorCast.getContext().getResources().getIdentifier("iqiyi",
                        "raw", SailorCast.getContext().getPackageName()));

        String ret = readTextFile(ins);
        doParseChannelFilter(channel,ret,listener);

        /*
        http://iface2.iqiyi.com/st/nav/1.2.json has a bad HTTP header:   charset=utf-8
        and will cause okhttp internal bug.
         */

        /*
        HttpUtils.asyncGet(url, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                SCFailLog err = makeHttpFailLog(url, "doGetChannelFilter", e);
                if(listener != null) {
                    listener.onGetChannelFilterFailed(err);
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String ret = response.body().string();
                doParseChannelFilter(channel, ret,listener);
            }
        });
        */
    }

}
