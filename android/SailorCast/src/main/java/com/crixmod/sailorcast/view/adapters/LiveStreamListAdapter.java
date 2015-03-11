package com.crixmod.sailorcast.view.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crixmod.sailorcast.model.SCLiveStream;
import com.crixmod.sailorcast.model.SCLiveStreams;

/**
 * Created by fire3 on 15-3-11.
 */
public class LiveStreamListAdapter extends BaseAdapter {
    SCLiveStreams mStreams = new SCLiveStreams();
    Context mContext;

    private class ViewHolder {
        LinearLayout streamContainer;
        ImageView streamImage;
        TextView currentPlayTitle;
        TextView nextPlayTitle;
        TextView nextPlayTime;
    }

    public LiveStreamListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mStreams.size();
    }

    @Override
    public SCLiveStream getItem(int i) {
        return mStreams.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
