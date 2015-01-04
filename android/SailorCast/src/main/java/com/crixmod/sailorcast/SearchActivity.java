package com.crixmod.sailorcast;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCAlbums;
import com.crixmod.sailorcast.model.SCVideo;
import com.crixmod.sailorcast.model.SCVideos;
import com.crixmod.sailorcast.siteapi.OnGetAlbumDescListener;
import com.crixmod.sailorcast.siteapi.OnGetVideoPlayUrlListener;
import com.crixmod.sailorcast.siteapi.OnGetVideosListener;
import com.crixmod.sailorcast.siteapi.OnSearchRequestListener;
import com.crixmod.sailorcast.siteapi.SiteApi;
import com.crixmod.sailorcast.ui.SCDrawerActivity;


public class SearchActivity extends SCDrawerActivity
        implements
        OnSearchRequestListener,
        OnGetAlbumDescListener,
        OnGetVideosListener,
        OnGetVideoPlayUrlListener,
        View.OnKeyListener

{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        createMenuDrawer(R.layout.activity_search);
        setSupportActionBar(getToolbar());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(getString(R.string.hello_world));
        }

        fixEditTextPadding();
        setSearchButton();

    }


    private void setSearchButton() {
        Button button = (Button) findViewById(R.id.search_ok);
        final EditText editText = (EditText) findViewById(R.id.search_input);
        final OnSearchRequestListener listener = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = editText.getText().toString();
                SiteApi.doSearchAll(key,listener);
            }
        });
    }

    private void fixEditTextPadding() {
        float density = getResources().getDisplayMetrics().density;
        int paddingH = (int) (16 * density);
        EditText editText = (EditText) findViewById(R.id.search_input);
        editText.setPadding(paddingH,0,paddingH,0);
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
        //TODO：letv存在VideosCount为0的情况。
        if(album.getVideosCount() > 20)
            SiteApi.doGetAlbumVideos(album,1,20,this);
        else
            SiteApi.doGetAlbumVideos(album,1,album.getVideosCount(),this);
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

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        final OnSearchRequestListener listener = this;
        final EditText editText = (EditText) findViewById(R.id.search_input);
        if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                keyCode == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

            if(v.getId() == R.id.search_input) {

                String key = editText.getText().toString();
                SiteApi.doSearchAll(key,listener);
            }
        }

        return false;
    }
}
