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

/**
 * Created by fire3 on 2015/1/22.
 */
public class AlbumPlayGridAdapter extends BaseAdapter {
    private Context mContext;
    private SCVideos mVideos = new SCVideos();

    private class ViewHolder {
            LinearLayout resultContainer;
            Button videoTitle;
    }

    public AlbumPlayGridAdapter(Context mContext) {
        this.mContext = mContext;
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

    private void setupViewHolder(View view, int position, final ViewHolder viewHolder, SCVideo video) {
        viewHolder.videoTitle.setText("" + (position + 1));
        viewHolder.videoTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.videoTitle.setSelected( !viewHolder.videoTitle.isSelected());
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
}
