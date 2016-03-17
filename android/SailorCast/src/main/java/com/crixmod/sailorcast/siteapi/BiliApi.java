package com.crixmod.sailorcast.siteapi;

import com.crixmod.sailorcast.SailorCast;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCChannel;
import com.crixmod.sailorcast.model.SCChannelFilter;
import com.crixmod.sailorcast.model.SCVideo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by fire3 on 16-3-14.
 */
public class BiliApi  extends BaseSiteApi {

    private static int SHOW_TIDS[] = {15,34};
    private static int MOVIE_TIDS[] = {145,146,147,83};
    private static int COMIC_TIDS[]  = {24,25,47,27};
    private static int MUSIC_TIDS[]  = {31,30,59,54,28,29,130};

    private static String mAppkey = "c1b107428d337928";
    private static String mAppSecret = "ea85624dfcf12d7cc7b2b3a94fac1f2c";



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



    @Override
    public void doSearch(String key, OnGetAlbumsListener listener) {

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
