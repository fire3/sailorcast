package com.crixmod.sailorcast.uiutils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCAlbums;
import com.crixmod.sailorcast.utils.ImageTools;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by fire3 on 15-1-19.
 */
public abstract class BaseAlbumsAdapter extends BaseAdapter{

    private ArrayList<AdapterAlbum> mBannerAlbums;
    private ArrayList<AdapterAlbum> mTopBannerAlbums;
    private ArrayList<AdapterAlbum> mAlbums;

    private Context mContext;
    private int mColumns = 3;
    private int mBannerRows = 0;
    private int mTopBannerRows = 0;

    public static final int VIEW_TYPE_BANNER_TOP = 0;
    public static final int VIEW_TYPE_BANNER_NORMAL = 1;
    public static final int VIEW_TYPE_MORE_ONE_COLUMN = 2;
    public static final int VIEW_TYPE_MORE_TWO_COLUMN = 3;
    public static final int VIEW_TYPE_MORE_THREE_COLUMN = 4;

    public static final int VIEW_TYPE_BANNER_TOP_CHECKER = 5;
    public static final int VIEW_TYPE_BANNER_NORMAL_CHECKER = 6;
    public static final int VIEW_TYPE_MORE_ONE_COLUMN_CHECKER = 7;
    public static final int VIEW_TYPE_MORE_TWO_COLUMN_CHECKER = 8;
    public static final int VIEW_TYPE_MORE_THREE_COLUMN_CHECKER = 9;

    private boolean isShowChecker;

    private class AdapterAlbum {
        SCAlbum album;
        boolean isChecked;
        public AdapterAlbum(SCAlbum a) {
            this.album = a;
            isChecked = false;
        }
        public SCAlbum getAlbum() {
            return album;
        }
        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public boolean getChecked() {
            return isChecked;
        }
        public void setUnChecked() {
            isChecked = false;
        }
    }


    public BaseAlbumsAdapter(Context mContext) {
        mBannerAlbums = new ArrayList<>();
        mTopBannerAlbums = new ArrayList<>();
        mAlbums = new ArrayList<>();
        this.mContext = mContext;
    }

    public void addAlbum(SCAlbum album) {
        AdapterAlbum adapterAlbum = new AdapterAlbum(album);
        mAlbums.add(adapterAlbum);
    }

    public void addTopBannerAlbum(SCAlbum album) {
        AdapterAlbum adapterAlbum = new AdapterAlbum(album);
        mTopBannerAlbums.add(adapterAlbum);
        mTopBannerRows = 1;
    }

    public void addBannerAlbum(SCAlbum album) {
        AdapterAlbum adapterAlbum = new AdapterAlbum(album);
        mBannerAlbums.add(adapterAlbum);
        mBannerRows = (mBannerAlbums.size() + 1)/2;
    }

    public void addAlbums(SCAlbums albums) {
        for(SCAlbum album : albums) {
            addAlbum(album);
        }
    }

    public void addTopBannerAlbums(SCAlbums albums) {
        for(SCAlbum album : albums) {
            addTopBannerAlbum(album);
        }
    }

    public void addBannerAlbums(SCAlbums albums) {
        for(SCAlbum album: albums) {
            addBannerAlbum(album);
        }
    }


    public void uncheckAllAlbums() {
            for(AdapterAlbum a : mAlbums) {
                a.setUnChecked();
            }
    }




    public void setShowChecker(boolean showChecker) {
        this.isShowChecker = showChecker;
    }


    @Override
    public int getCount() {
        int count = mAlbums.size();
        count = (count / mColumns) + ((count % mColumns) >0 ? 1 : 0);

        //add banner rows
        int normalBannerRows = (mBannerAlbums.size() + 1) / 2;

        int topBannerRows = mTopBannerAlbums.size() > 0 ? 1 : 0;

        return count + normalBannerRows + topBannerRows;

    }


    public void setColumns(int columns) {
        if(columns <= 3 && columns >= 1)
            this.mColumns = columns;
    }

    public int getColumns(int columns) {
        return this.mColumns;
    }


    private AdapterAlbum getAlbumByPosition(int index) {
        if(index < mAlbums.size()) {
            return mAlbums.get(index);
        } else
            return null;
    }


    static class ViewHolder {
        int position;
        ArrayList<View> container = new ArrayList<>();
        ArrayList<ImageView> videoImage = new ArrayList<>();
        ArrayList<TextView> videoTip = new ArrayList<>();
        ArrayList<ImageView> videoTipOverlay = new ArrayList<>();
        ArrayList<TextView> videoTitle = new ArrayList<>();
        ArrayList<CheckBox> videoChecker = new ArrayList<>();
    }


    @Override
    public ArrayList<AdapterAlbum> getItem(int i) {

        int index;
        ArrayList<AdapterAlbum> videos = new ArrayList<>();
        if(i < mBannerRows) {
            if(i == 0) {
                return mTopBannerAlbums;
            } else {
                for (int j = 0; j < 2; j++) {
                    videos.add(mBannerAlbums.get(i * 2 + j - 1));
                }
            }
        } else {
            index = i - mBannerRows;
            AdapterAlbum v;
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


    private View getThreeColumnsRow(int i, View view, ViewGroup viewGroup) {
        ArrayList<AdapterAlbum> albums = getItem(i);
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = getThreeColumnsVideoRowView(i, viewGroup, viewHolder, albums);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        setupViewHolder(view,i,viewHolder,albums);

        return view;
    }

    private void setupViewHolder(View view, int i, ViewHolder viewHolder, ArrayList<AdapterAlbum> albums) {
        Log.d("fire3", "setupViewHolder " + i);
        int columns = viewHolder.videoTitle.size();
        for (int j = 0; j < columns; j++) {
            setupVideoTitleTextView(viewHolder.videoTitle.get(j), albums.get(j).getAlbum());
            setupVideoImageView(viewHolder.videoImage.get(j), albums.get(j).getAlbum());
        }
    }

    private void setupVideoImageView(ImageView imageView, SCAlbum album) {
        Point point = ImageTools.getGridVerPosterSize(mContext, 3);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        params.width = point.x;
        params.height = point.y;
        ImageTools.displayImage(imageView,album.getVerImageUrl(),point.x,point.y);
    }

    private void setupVideoTitleTextView(TextView textView, SCAlbum album) {
        textView.setText(album.getTitle());
    }

    private View getThreeColumnsVideoRowView(int i, ViewGroup viewGroup, ViewHolder viewHolder, ArrayList<AdapterAlbum> albums) {
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        View root = inflater.inflate(R.layout.base_list_row_3_column, viewGroup, false);
        TableRow row = (TableRow) root.findViewById(R.id.base_list_row);

        for (int j = 0; j < albums.size(); j++) {
            View itemView = inflater.inflate(R.layout.item_gridview_bookmark,viewGroup,false);
            viewHolder.videoImage.add((ImageView) itemView.findViewById(R.id.video_image));
            viewHolder.videoTipOverlay.add((ImageView) itemView.findViewById(R.id.video_tip_overlay));
            viewHolder.videoTitle.add((TextView) itemView.findViewById(R.id.video_title));
            viewHolder.videoTip.add((TextView) itemView.findViewById(R.id.video_tip));
            viewHolder.container.add(itemView);
            viewHolder.videoChecker.add((CheckBox) itemView.findViewById(R.id.video_checkbox));
            int position = (i - mTopBannerRows  - mBannerRows)*mColumns + j;
            viewHolder.position = position;
            if(isShowChecker)
                viewHolder.videoChecker.get(j).setVisibility(View.VISIBLE);
            else
                viewHolder.videoChecker.get(j).setVisibility(View.GONE);

            viewHolder.videoChecker.get(j).setOnCheckedChangeListener(CheckBox_OnCheckedChangeListener);

            row.addView(itemView);
        }

        root.setTag(viewHolder);
        return root;
    }


        private CompoundButton.OnCheckedChangeListener CheckBox_OnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                View view = (View)buttonView.getParent();
                ViewHolder holder = (ViewHolder)view.getTag();
                AdapterAlbum item = getAlbumByPosition(holder.position);
                item.setChecked(isChecked);
            }};


    private View getMoreVideosView(int i, View view, ViewGroup viewGroup) {
        if(mColumns == 3) {
            return getThreeColumnsRow(i,view,viewGroup);
        }

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
        if(mBannerAlbums.size() == 0 && mTopBannerAlbums.size() == 0 && mAlbums.size() == 0)
            return true;
        else
            return false;
    }

    @Override
    public int getItemViewType(int position) {
        if(mBannerRows > 0 && position < mBannerRows) {
            if (position == 0) {
                    return isShowChecker ? VIEW_TYPE_BANNER_TOP_CHECKER:VIEW_TYPE_BANNER_TOP;
            }
            else {
                return isShowChecker ? VIEW_TYPE_BANNER_NORMAL_CHECKER : VIEW_TYPE_BANNER_NORMAL;
            }
        }
        else {
           if(mColumns == 1)
               return isShowChecker ? VIEW_TYPE_MORE_ONE_COLUMN_CHECKER : VIEW_TYPE_MORE_ONE_COLUMN;
            if(mColumns == 2)
                return isShowChecker ? VIEW_TYPE_MORE_TWO_COLUMN_CHECKER: VIEW_TYPE_MORE_TWO_COLUMN;
            if(mColumns == 3)
                return isShowChecker ? VIEW_TYPE_MORE_THREE_COLUMN_CHECKER : VIEW_TYPE_MORE_THREE_COLUMN;
        }
        return isShowChecker ? VIEW_TYPE_MORE_THREE_COLUMN_CHECKER : VIEW_TYPE_MORE_THREE_COLUMN;
    }

    @Override
    public int getViewTypeCount() {
        return 10;
    }

}
