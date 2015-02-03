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
import com.crixmod.sailorcast.utils.HttpUtils;
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
import java.util.Hashtable;
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

    private final static String SEARCH_URL_FORMAT = "http://iface.iqiyi.com/api/searchIface?key=2019620214d1a82fc76d0b4b3c6fa685" +
            "&all_episode=-1&need_video_img=0" +
            "&keyword=%s&category_id=0&" +
            "type=json&page_number=1&page_size=30&sort=6&version=5.9.1";

    private final static String ALBUM_VIDEOS_FORMAT = "http://iface2.iqiyi.com/php/xyz/entry/galaxy.php?key=317e617581c95c3e8a996f8bff69607b&device_id=357070056881552&network=12&ua=GT-I9300&os=4.3&version=5.3.1&category_id=2&f_ps=10&s=1&ps=30&pn=1&pcat=2&hwd=1&v5=1&compat=1&platform=GPad&f=1&udid=3fa69706efe86bbd&openudid=3fa69706efe86bbd&uniqid=9c185b315590159c739e02f77ad4bba9&ppid=";
    private final static String IQIYI_MKEY = "317e617581c95c3e8a996f8bff69607b";

    private  Hashtable<String, String> getSignedHeader(String paramString)
    {
        return getSignedHeader(paramString, 1111111727, "D9g6XYm(B-:o1nu|");
    }

    private  Hashtable<String, String> getSignedHeader(String paramString1, int paramInt, String paramString2)
    {
        long l1 = System.currentTimeMillis() / 1000L;
        //l1 = 1422952262;
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

    private String decompress(String zipText) throws IOException {
        byte[] compressed = zipText.getBytes();
            GZIPInputStream gzipInputStream = new GZIPInputStream(
                    new ByteArrayInputStream(compressed));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int value = 0; value != -1;) {
            value = gzipInputStream.read();
            if (value != -1) {
                baos.write(value);
            }
        }
        gzipInputStream.close();
        baos.close();
        String sReturn = new String(baos.toByteArray(), "UTF-8");
        return sReturn;
    }

    private String unzipString(String zippedText) throws Exception
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(zippedText.getBytes("UTF-8"));
        GZIPInputStream gzis = new GZIPInputStream(bais);
        InputStreamReader reader = new InputStreamReader(gzis);
        BufferedReader in = new BufferedReader(reader);

        String unzipped = "";
        while ((unzipped = in.readLine()) != null)
            unzipped+=unzipped;

        return unzipped;
    }

    @Override
    public void doGetAlbumVideos(SCAlbum album, int pageNo, int pageSize, OnGetVideosListener listener) {

        String url = ALBUM_VIDEOS_FORMAT;
        Hashtable<String,String> head = getSignedHeader(IQIYI_MKEY);
        Request request = new Request.Builder().url(url)
                //.addHeader("Accept-Encoding", "gzip")
                .addHeader("t", head.get("t"))
                .addHeader("sign",head.get("sign"))
                .build();
        Log.d("fire3","doGetAlbumVideos: url" + url);
        Log.d("fire3","doGetAlbumVideos: t: " + head.get("t"));
        Log.d("fire3","doGetAlbumVideos: sign: " + head.get("sign"));

        HttpUtils.asyncGet(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("fire3","onFailure");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String ret = response.body().string();
                Log.d("fire3", ret);
            }
        });

    }

    @Override
    public void doGetAlbumDesc(SCAlbum album, OnGetAlbumDescListener listener) {
        if(listener!=null)
            listener.onGetAlbumDescSuccess(album);
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
