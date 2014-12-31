package com.crixmod.sailorcast;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCAlbums;
import com.crixmod.sailorcast.model.SCSite;
import com.crixmod.sailorcast.model.SCVideo;
import com.crixmod.sailorcast.model.SCVideos;
import com.crixmod.sailorcast.siteapi.OnGetAlbumDescListener;
import com.crixmod.sailorcast.siteapi.OnGetVideoPlayUrlListener;
import com.crixmod.sailorcast.siteapi.OnGetVideosListener;
import com.crixmod.sailorcast.siteapi.OnSearchRequestListener;
import com.crixmod.sailorcast.siteapi.SiteApi;
import com.crixmod.sailorcast.siteapi.SohuApi;
import com.crixmod.sailorcast.siteapi.YouKuApi;
import com.crixmod.sailorcast.ui.SCDrawerActivity;

import java.util.ArrayList;


public class MainActivity extends SCDrawerActivity
        implements OnSearchRequestListener,OnGetAlbumDescListener,OnGetVideosListener,OnGetVideoPlayUrlListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        createMenuDrawer(R.layout.activity_main);
        setSupportActionBar(getToolbar());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(getString(R.string.hello_world));
        }

        SiteApi.doSearch(SiteApi.SITE_ID_LETV,"敢死队3",this);
      //  SiteApi.doSearch(SiteApi.SITE_ID_YOUKU,"巧虎",this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSearchSuccess(SCAlbums albums) {
        for(SCAlbum album : albums) {
            SiteApi.doGetAlbumDesc(album,this);
        }
    }

    @Override
    public void onSearchFailed(String failReason) {

    }

    @Override
    public void onGetAlbumDescSuccess(SCAlbum album) {
        Log.d("fire3",album.toString());
        SiteApi.doGetAlbumVideos(album,1,20,this);
    }

    @Override
    public void onGetAlbumDescFailed(String failReason) {

    }

    @Override
    public void onGetVideosSuccess(SCVideos videos) {
        for(SCVideo v : videos) {
            Log.d("fire3","video: " + v.toString());
           SiteApi.doGetVideoPlayUrl(v,this);
        }
    }

    @Override
    public void onGetVideosFailed(String failReason) {

    }

    @Override
    public void onGetVideoPlayUrlNormal(SCVideo v, String urlNormal) {

    }

    @Override
    public void onGetVideoPlayUrlHigh(SCVideo v, String urlHigh) {
        Log.d("fire3","video: " + v.getVideoTitle() + " " + urlHigh);
    }

    @Override
    public void onGetVideoPlayUrlSuper(SCVideo v, String urlSuper) {

    }

    @Override
    public void onGetVideoPlayUrlFailed(String reason) {

    }
}
