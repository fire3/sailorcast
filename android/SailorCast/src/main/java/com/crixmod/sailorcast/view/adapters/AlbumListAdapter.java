package com.crixmod.sailorcast.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCAlbums;
import com.crixmod.sailorcast.utils.ImageTools;
import com.crixmod.sailorcast.view.AlbumActivity;

/**
 * Created by fire3 on 15-1-21.
 */
public class AlbumListAdapter extends BaseAdapter {
    private Context mContext;
    private int mColumns = 3;

    private SCAlbums mAlbums = new SCAlbums();

    private class ViewHolder {
            LinearLayout resultContainer;
            ImageView videoImage;
            TextView videoTitle;
            TextView videoTip;
    }

    public AlbumListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addAlbum(SCAlbum album) {
        mAlbums.add(album);
    }

    @Override
    public int getCount() {
        return mAlbums.size();
    }

    @Override
    public SCAlbum getItem(int i) {
        return mAlbums.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SCAlbum album = getItem(i);
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = getOneColumnVideoRowView(viewGroup, viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        setupViewHolder(view,i,viewHolder,album);

        return view;
    }

    private void setupViewHolder(View view, int i, ViewHolder viewHolder, final SCAlbum album) {
        viewHolder.videoTitle.setText(album.getTitle());
        viewHolder.videoTip.setText(album.getTip());

        if(album.getVerImageUrl() != null) {

            Point point = ImageTools.getGridVerPosterSize(mContext, 3);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.videoImage.getLayoutParams();
            params.width = point.x;
            params.height = point.y;
            ImageTools.displayImage(viewHolder.videoImage,album.getVerImageUrl(),point.x,point.y);
        } else if(album.getHorImageUrl() != null) {

            Point point = ImageTools.getGridHorPosterSize(mContext,3);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.videoImage.getLayoutParams();
            params.width = point.x;
            params.height = point.y;
            ImageTools.displayImage(viewHolder.videoImage,album.getHorImageUrl(),point.x,point.y);
        }

        viewHolder.resultContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlbumActivity.launch((Activity) mContext, album);
            }
        });
    }

    private View getOneColumnVideoRowView(ViewGroup viewGroup, ViewHolder viewHolder) {
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        View itemView = inflater.inflate(R.layout.item_gridview_search_result,viewGroup,false);
        viewHolder.videoImage = (ImageView) itemView.findViewById(R.id.video_image);
        viewHolder.videoTitle = (TextView) itemView.findViewById(R.id.video_title);
        viewHolder.videoTip = (TextView) itemView.findViewById(R.id.video_tip);
        viewHolder.resultContainer = (LinearLayout)itemView.findViewById(R.id.search_result);

        itemView.setTag(viewHolder);
        return itemView;
    }
}
