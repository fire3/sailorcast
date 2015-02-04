package com.crixmod.sailorcast.siteapi;

import android.content.Context;
import android.support.v4.app.ListFragment;
import android.util.Base64;
import android.util.Log;

import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCAlbums;
import com.crixmod.sailorcast.model.SCChannel;
import com.crixmod.sailorcast.model.SCChannelFilter;
import com.crixmod.sailorcast.model.SCFailLog;
import com.crixmod.sailorcast.model.SCSite;
import com.crixmod.sailorcast.model.SCVideo;
import com.crixmod.sailorcast.model.SCVideos;
import com.crixmod.sailorcast.utils.HttpUtils;
import com.crixmod.sailorcast.utils.MD5;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

/**
 * Created by fire3 on 15-2-3.
 */
public class IqiyiApi extends BaseSiteApi {

    private final static String TAG = "IqiyiApi";
    public static final int GALAXY_SECRET_KEY_ONE = 1111111727;
    public static final String GALAXY_SECRET_KEY_TWO = "D9g6XYm(B-:o1nu|";
    public static final int MORE_SECRET_KEY_ONE = 1100016699;
    public static final String MORE_SECRET_KEY_TWO = "m)*ra772e";
    public static final int NEXT_SECRET_KEY_ONE = 1770021100;
    public static final String NEXT_SECRET_KEY_TWO = "m9192nck:_77";
    public static final int PLAY_SECRET_KEY_ONE = 1121111727;
    public static final String PLAY_SECRET_KEY_TWO = ",rI1:?CJczS3AwJ$";


    private final static int CID_MOVIE = 1;
    private final static int CID_SHOW = 2;
    private final static int CID_COMIC = 4;
    private final static int CID_VARIETY = 6;
    private final static int CID_ENT = 7;
    private final static int CID_DOCUMENTARY = 3;
    private final static int CID_MUSIC = 5;
    private final static int CID_SPORT = 17;


    private final static String SEARCH_URL_FORMAT = "http://iface.iqiyi.com/api/searchIface?key=2019620214d1a82fc76d0b4b3c6fa685" +
            "&all_episode=-1&need_video_img=0" +
            "&keyword=%s&category_id=0&" +
            "type=json&page_number=1&page_size=30&sort=6&version=5.9.1";

    private final static String ALBUM_VIDEOS_FORMAT = "http://cache.video.qiyi.com/jp/avlist/%s/%d/%d/?albumId=%s&pageNo=%d&pageNum=%d";
    private final static String IQIYI_MKEY = "317e617581c95c3e8a996f8bff69607b";

    private final static String ALBUM_VIDEOS_NEBULA_FORMAT = "http://iface2.iqiyi.com/php/xyz/entry/nebula.php?key=" +
            "317e617581c95c3e8a996f8bff69607b&version=5.3.1&uniqid=%s&platform=GPad&block=0&w=1&compat=1&other=1&v5=1&ad_str=1&many_id=%s_0_";

    private  Hashtable<String, String> getSignedHeader(String paramString)
    {
        return getSignedHeader(paramString, 1111111727, "D9g6XYm(B-:o1nu|");
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


    private SCFailLog makeHttpFailLog(String url, String functionName) {
        SCFailLog err = new SCFailLog(SCSite.IQIYI,SCFailLog.TYPE_HTTP_FAILURE);
        err.setFunctionName(functionName);
        err.setClassName("IqiyiApi");
        err.setTag(TAG);
        err.setUrl(url);
        return err;
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

    private SCFailLog makeJsonFailLog(String url, String functionName, Exception e) {
        SCFailLog err = new SCFailLog(SCSite.IQIYI,SCFailLog.TYPE_JSON_ERR);
        err.setException(e);
        err.setFunctionName(functionName);
        err.setClassName("IqiyiApi");
        err.setTag(TAG);
        err.setUrl(url);
        return err;
    }

    private SCFailLog makeJsonFailLog(String url, String functionName) {
        SCFailLog err = new SCFailLog(SCSite.IQIYI,SCFailLog.TYPE_JSON_ERR);
        err.setFunctionName(functionName);
        err.setClassName("IqiyiApi");
        err.setTag(TAG);
        err.setUrl(url);
        return err;
    }



    @Override
    public void doSearch(String key, final OnGetAlbumsListener listener) {
        String encodeKey = URLEncoder.encode(key);
        final String url = String.format(SEARCH_URL_FORMAT,encodeKey);
        HttpUtils.asyncGet(url, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                if(listener != null) {
                    SCFailLog err = makeHttpFailLog(url,"doSearch",e);
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
                    if(albumsArray != null && albumsArray.length() > 0) {
                        SCAlbums albums = new SCAlbums();
                        for (int i = 0; i < albumsArray.length(); i++) {
                            JSONObject albumJson = albumsArray.getJSONObject(i);
                            int category_id = albumJson.optInt("category_id");
                            if(category_id < 7) {
                                // 排除一些无聊的频道
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
                        if(listener != null)
                            listener.onGetAlbumsSuccess(albums);
                    } else {
                        if (listener != null) {
                            SCFailLog err = makeJsonFailLog(url, "doSearch");
                            err.setReason(ret);
                            listener.onGetAlbumsFailed(err);
                        }
                    }
                } catch (Exception e) {
                    if(listener != null) {
                        SCFailLog err = makeJsonFailLog(url,"doSearch",e);
                        err.setReason(ret);
                        listener.onGetAlbumsFailed(err);
                    }
                    e.printStackTrace();
                }

            }
        });
    }

    private Hashtable<String, String> getGalaxyHeader() {
        Hashtable<String,String> head = getSignedHeader(IQIYI_MKEY);
        return head;
    }

    private Hashtable<String, String> getNebulaHeader() {
        Hashtable<String,String> head = getSignedHeader(IQIYI_MKEY,0x42d2ceaf,",rI1:?CJczS3AwJ$");
        return head;
    }

    private void doGetAlbumVideosPcMethod(final SCAlbum album, int pageNo, int pageSize, final OnGetVideosListener listener) {
         final String url = String.format(ALBUM_VIDEOS_FORMAT,album.getAlbumId(),pageNo,pageSize,album.getAlbumId(),pageNo,pageSize);

        HttpUtils.asyncGet(url, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("fire3","onFailure");
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
                            SCFailLog err = new SCFailLog(SCSite.IQIYI,SCFailLog.TYPE_JSON_ERR);
                            err.setReason(retJson.toString());
                            err.setFunctionName("doGetAlbumVideos");
                            err.setUrl(url);
                            listener.onGetVideosFailed(err);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void doGetAlbumVideosNebulaMethod(final SCAlbum album, int pageNo, int pageSize, final OnGetVideosListener listener) {
             String url = String.format(ALBUM_VIDEOS_NEBULA_FORMAT,genUUID(),album.getAlbumId());
        Hashtable<String, String> head = getNebulaHeader();

        Request request = new Request.Builder().url(url)
                //.addHeader("Accept-Encoding", "gzip")
                .addHeader("t", head.get("t"))
                .addHeader("sign",head.get("sign"))
                .build();
        HttpUtils.asyncGet(request,new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

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

                    /*
                    JSONArray resJson = tvJson.optJSONArray("res");
                    for (int i = 0; i < resJson.length(); i++) {
                        JSONObject resJ = resJson.getJSONObject(i);
                        if(resJ.optString("t")!=null && resJ.optString("t").equals("MP4_200K")) {
                            v.setM3U8Nor(resJ.optString("vid"));
                        }
                        if(resJ.optString("t")!=null && resJ.optString("t").equals("MP4_400K")) {
                            v.setM3U8High(resJ.optString("vid"));
                        }
                    }
                    */
                    videos.add(v);

                    if(listener != null) {
                        listener.onGetVideosSuccess(videos);
                    }

                } catch (Exception e) {
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
    public void doGetAlbumDesc(SCAlbum album, OnGetAlbumDescListener listener) {
        if(listener!=null)
            listener.onGetAlbumDescSuccess(album);
    }

    private String genUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-","");
    }

    private String getVideoVMSURL(SCVideo video) {
        String uid = genUUID();
        Random r = new Random();
        String tm =String.format("%d",((r.nextInt(1000-100))+100));
        String enc = md5("ts56gh"+tm+video.getVideoID());
        String tn = String.valueOf(r.nextDouble());
        String authkey = md5(""+tm+video.getVideoID());
        String tvid = video.getVideoID();
        String vid = video.getIqiyiVid();
        String vmsreq="http://cache.video.qiyi.com/vms?key=fvip&src=1702633101b340d8917a69cf8a4b8c7" +
                "&tvId="+tvid+"&vid="+vid+"&vinfo=1&tm="+tm+
                "&enc="+enc+
                "&qyid="+uid+"&tn="+tn +"&um=0" +
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
                            Log.d("fire3",String.format("found bid:%d index:%d",bid,i));
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
                    }
                } catch (Exception e) {
                    //错误情况
                    e.printStackTrace();
                }
            }
        });
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
