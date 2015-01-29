package com.crixmod.sailorcast.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.SCVideo;
import com.crixmod.sailorcast.model.SCVideos;

import java.util.concurrent.Callable;

/**
 * Created by fire3 on 2015/1/22.
 */
public class AlbumPlayGridAdapter extends BaseAdapter {
    private Context mContext;
    private SCVideos mVideos = new SCVideos();
    private AlbumPlayGridSelectedListener mListener;
    private boolean showTitle = false;
    private boolean isBackward = false;
    private int totalVideos;

    private class ViewHolder {
            LinearLayout resultContainer;
            Button videoTitle;
    }


    public AlbumPlayGridAdapter(Context mContext , int totalVideos,  AlbumPlayGridSelectedListener mListener) {
        this.mContext = mContext;
        this.mListener = mListener;
        this.totalVideos = totalVideos;
    }

    public void clear() {
        mVideos.clear();
    }

    public void setBackward(boolean isBackward) {
        this.isBackward = isBackward;
    }

    public void addVideo(SCVideo v) {
        mVideos.add(v);
    }

    @Override
    public int getCount() {
        return mVideos.size();
    }

    @Override
    public SCVideo getItem(int position) {
        return mVideos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        SCVideo video = getItem(position);
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = getGridView(parent, viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        setupViewHolder(view,position,viewHolder,video);
        return view;
    }

    private void setupViewHolder(View view, final int position, final ViewHolder viewHolder, final SCVideo video) {
        if(isShowTitle()) {
            if(video.getVideoTitle()!=null && !video.getVideoTitle().isEmpty())
                viewHolder.videoTitle.setText(video.getVideoTitle());
            else {
                if(isBackward == false)
                    viewHolder.videoTitle.setText("" + (position + 1));
                else
                    viewHolder.videoTitle.setText("" + (totalVideos - position));
            }
        } else {
            if(isBackward == false)
                viewHolder.videoTitle.setText("" + (position + 1));
            else
                viewHolder.videoTitle.setText("" + (totalVideos - position));
        }
        viewHolder.videoTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SCVideo v = getItem(position);
                mListener.onVideoSelected(position,v);
            }
        });
    }

    private View getGridView(ViewGroup viewGroup, ViewHolder viewHolder) {
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        View itemView;
        itemView = inflater.inflate(R.layout.item_gridview_play_control,viewGroup,false);

        viewHolder.videoTitle = (Button) itemView.findViewById(R.id.album_play_grid_text);
        viewHolder.resultContainer = (LinearLayout)itemView;

        itemView.setTag(viewHolder);
        return itemView;
    }

    public boolean isShowTitle() {
        return showTitle;
    }

    public void setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
    }

    @Override
    public int getItemViewType(int position) {
        if(showTitle)
            return 1;
        else
            return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public interface AlbumPlayGridSelectedListener {
        public void onVideoSelected(int position, SCVideo v);
    }


}
