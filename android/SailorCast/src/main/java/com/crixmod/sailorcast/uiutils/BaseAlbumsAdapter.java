package com.crixmod.sailorcast.uiutils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCAlbums;

import java.util.ArrayList;

/**
 * Created by fire3 on 15-1-19.
 */
public abstract class BaseAlbumsAdapter extends BaseAdapter{

    SCAlbums mMoreAlbums;
    SCAlbums mNormalBannerAlbums;
    SCAlbums mTopBannerAlbums;

    private Context mContext;
    private int mColumns = 3;
    private int mBannerRows = 0;

    public static final int VIEW_TYPE_BANNER_TOP = 0;
    public static final int VIEW_TYPE_BANNER_NORMAL = 1;
    public static final int VIEW_TYPE_MORE_ONE_COLUMN = 2;
    public static final int VIEW_TYPE_MORE_TWO_COLUMN = 3;
    public static final int VIEW_TYPE_MORE_THREE_COLUMN = 4;


    public BaseAlbumsAdapter(Context mContext) {
        mNormalBannerAlbums = new SCAlbums();
        mTopBannerAlbums = new SCAlbums();
        mMoreAlbums = new SCAlbums();
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        int count = mMoreAlbums.size();
        count = (count / mColumns) + ((count % mColumns) >0 ? 1 : 0);

        //add banner rows
        int normalBannerRows = (mNormalBannerAlbums.size() + 1) / 2;

        int topBannerRows = mTopBannerAlbums.size() > 0 ? 1 : 0;

        return count + normalBannerRows + topBannerRows;

    }


    public void setColumns(int columns) {
        this.mColumns = columns;
    }

    public int getColumns(int columns) {
        return this.mColumns;
    }


    private SCAlbum getAlbumByPosition(int index) {
        if(index < mMoreAlbums.size()) {
            return mMoreAlbums.get(index);
        } else
            return null;
    }


    static class ViewHolder {
        int position;
        ArrayList<RelativeLayout> videoContainer = new ArrayList<>();
        ArrayList<ImageView> videoImageView = new ArrayList<>();
        ArrayList<TextView> videoTip = new ArrayList<>();
        ArrayList<ImageView> videoTipOverlay = new ArrayList<>();
        ArrayList<TextView> videoTitle = new ArrayList<>();
        ArrayList<CheckBox> videoChecker = new ArrayList<>();
    }


    @Override
    public SCAlbums getItem(int i) {

        int index;
        SCAlbums videos = new SCAlbums();
        if(i < mBannerRows) {
            if(i == 0) {
                return mTopBannerAlbums;
            } else {
                for (int j = 0; j < 2; j++) {
                    videos.add(mNormalBannerAlbums.get(i * 2 + j - 1));
                }
            }
        } else {
            index = i - mBannerRows;
            SCAlbum v;
            for (int j = 0; j < mColumns; j++) {
                v = getAlbumByPosition(index * mColumns + j);
                if (v != null) {
                    videos.add(v);
                }
            }
        }

        if(videos.size() > 0)
            return videos;
        else
            return null;
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }

    private View getTopBannerView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    private View getBannerView(int i, View view, ViewGroup viewGroup) {
        return null;
    }


    private View getMoreVideosView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(getItemViewType(i) == VIEW_TYPE_MORE_ONE_COLUMN ||
                getItemViewType(i) == VIEW_TYPE_MORE_TWO_COLUMN ||
                getItemViewType(i) == VIEW_TYPE_MORE_THREE_COLUMN)
            return getMoreVideosView(i,view,viewGroup);
        else if(getItemViewType(i) == VIEW_TYPE_BANNER_NORMAL)
            return getBannerView(i,view,viewGroup);
        else if(getItemViewType(i) == VIEW_TYPE_BANNER_TOP)
            return getTopBannerView(i, view, viewGroup);
        else
            return null;
    }


    @Override
    public boolean isEmpty() {
        if(mNormalBannerAlbums.size() == 0 && mTopBannerAlbums.size() == 0 && mMoreAlbums.size() == 0)
            return true;
        else
            return false;
    }

    @Override
    public int getItemViewType(int position) {
        if(mBannerRows > 0 && position < mBannerRows) {
            if (position == 0) {
                return VIEW_TYPE_BANNER_TOP;
            }
            else
                return VIEW_TYPE_BANNER_NORMAL;
        }
        else {
           if(mColumns == 1)
               return VIEW_TYPE_MORE_ONE_COLUMN;
            if(mColumns == 2)
                return VIEW_TYPE_MORE_TWO_COLUMN;
            if(mColumns == 3)
                return VIEW_TYPE_MORE_THREE_COLUMN;
        }
        return VIEW_TYPE_MORE_THREE_COLUMN;
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }


}
