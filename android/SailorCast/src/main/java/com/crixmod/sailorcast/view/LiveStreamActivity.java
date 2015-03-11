package com.crixmod.sailorcast.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.SCFailLog;
import com.crixmod.sailorcast.model.SCLiveStream;
import com.crixmod.sailorcast.model.SCLiveStreams;
import com.crixmod.sailorcast.siteapi.LetvApi;
import com.crixmod.sailorcast.siteapi.OnGetLiveStreamDescListener;
import com.crixmod.sailorcast.siteapi.OnGetLiveStreamPlayUrlListener;
import com.crixmod.sailorcast.siteapi.OnGetLiveStreamsListener;

public class LiveStreamActivity extends ActionBarActivity

    implements OnGetLiveStreamsListener, OnGetLiveStreamDescListener, OnGetLiveStreamPlayUrlListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_stream);

        new LetvApi().doGetCustomLiveStreams(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_live_stream, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public static void launch(Activity activity) {
        Intent mpdIntent = new Intent(activity, LiveStreamActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(mpdIntent);
    }

    @Override
    public void onGetLiveStreamsSuccess(SCLiveStreams streams) {
        new LetvApi().doGetLiveStreamDesc(streams.get(0),this);
        //for(SCLiveStream stream: streams) {
        //    new LetvApi().doGetLiveStreamDesc(stream,this);
        //}

    }

    @Override
    public void onGetLiveStreamsFailed(SCFailLog failReason) {

    }

    @Override
    public void onGetLiveStreamDescSuccess(SCLiveStream stream) {
        new LetvApi().doGetLiveStreamPlayUrl(stream,this);
    }

    @Override
    public void onGetLiveStreamDescFailed(SCFailLog failReason) {

    }

    @Override
    public void onGetLiveStreamPlayUrlNormal(SCLiveStream v, String urlNormal) {
        Log.d("fire3",urlNormal);
    }

    @Override
    public void onGetLiveStreamPlayUrlHigh(SCLiveStream v, String urlHigh) {

        Log.d("fire3", urlHigh);
    }

    @Override
    public void onGetLiveStreamPlayUrlSuper(SCLiveStream v, String urlSuper) {
        Log.d("fire3",urlSuper);
    }

    @Override
    public void onGetLiveStreamPlayUrlFailed(SCFailLog reason) {

    }
}
