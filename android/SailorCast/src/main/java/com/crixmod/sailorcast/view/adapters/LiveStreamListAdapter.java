package com.crixmod.sailorcast.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.SCLiveStream;
import com.crixmod.sailorcast.model.SCLiveStreams;
import com.crixmod.sailorcast.uiutils.paginglistview.PagingBaseAdapter;
import com.crixmod.sailorcast.utils.ImageTools;
import com.crixmod.sailorcast.view.LiveStreamDetailActivity;

import java.util.List;

/**
 * Created by fire3 on 15-3-11.
 */
public class LiveStreamListAdapter extends PagingBaseAdapter {
    SCLiveStreams mStreams = new SCLiveStreams();
    Context mContext;


    private class ViewHolder {
        RelativeLayout streamContainer;
        ImageView streamImage;
        TextView streamTitle;
        TextView currentPlayTitle;
        TextView nextPlayTitle;
    }

    public void addLiveStream(SCLiveStream stream) {
        mStreams.add(stream);
    }

    public void addLiveStreams(SCLiveStreams streams) {
        for (SCLiveStream stream: streams )
            mStreams.add(stream);
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
        SCLiveStream stream = getItem(i);
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = getItemView(viewGroup, viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        setupViewHolder(view,i,viewHolder,stream);
        return view;

    }

    private void setupViewHolder(View view, int i, ViewHolder viewHolder, final SCLiveStream stream) {
        viewHolder.streamTitle.setText(stream.getChannelName());
        viewHolder.currentPlayTitle.setText(stream.getCurrentPlayTitle());
        viewHolder.currentPlayTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.xiaobofang_normal,0,0,0);
        try {
            String nextTime = stream.getNextPlayStartTime();
            if(nextTime != null) {
                String nTime = nextTime.substring(stream.getNextPlayStartTime().lastIndexOf(" "));
                viewHolder.nextPlayTitle.setText(nTime + " " + stream.getNexPlayTitle());
            } else {
                viewHolder.nextPlayTitle.setText(stream.getNexPlayTitle());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewHolder.streamImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LiveStreamDetailActivity.launch((Activity)mContext, stream);
            }
        });

        viewHolder.streamContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveStreamDetailActivity.launch((Activity) mContext,stream);
            }
        });

        ImageTools.displayImage(viewHolder.streamImage,stream.getHorPic());
    }

    private View getItemView(ViewGroup viewGroup, ViewHolder viewHolder) {
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        View itemView;
        itemView = inflater.inflate(R.layout.item_listview_livestream,viewGroup,false);
        viewHolder.streamContainer = (RelativeLayout) itemView;
        viewHolder.currentPlayTitle  = (TextView) itemView.findViewById(R.id.itemDescPlayNameTitle);
        viewHolder.nextPlayTitle  = (TextView) itemView.findViewById(R.id.itemDescNextPlayTitle);
        viewHolder.streamTitle  = (TextView) itemView.findViewById(R.id.itemTitle);
        viewHolder.streamImage = (ImageView) itemView.findViewById(R.id.programImg);

        itemView.setTag(viewHolder);
        return itemView;
    }
}
