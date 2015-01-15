package com.crixmod.sailorcast.view.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.SailorCast;
import com.crixmod.sailorcast.database.BookmarkDbHelper;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCAlbums;
import com.crixmod.sailorcast.utils.ImageTools;
import com.crixmod.sailorcast.view.AlbumActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookmarkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookmarkFragment extends Fragment {
    private BookmarkDbHelper mDb;

    private BookmarkAdapter mAdapter;
    private String mFailReason;
    private GridView mGrid;
    private TextView mEmpty;
    private SCAlbums mAlbums;

    public static BookmarkFragment newInstance() {
        BookmarkFragment fragment = new BookmarkFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public BookmarkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mDb = new BookmarkDbHelper(getActivity());
        mAlbums = mDb.getAllAlbums();
        mAdapter = new BookmarkAdapter(getActivity(),mAlbums);
        mFailReason = SailorCast.getResource().getString(R.string.fail_reason_no_results);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_bookmark, container, false);
        mGrid = (GridView) view.findViewById(R.id.search_result_grid);
        mEmpty = (TextView) view.findViewById(android.R.id.empty);
        mGrid.setEmptyView(mEmpty);
        mEmpty.setText(mFailReason);
        mGrid.setAdapter(mAdapter);

        return view;
    }




    class BookmarkAdapter extends BaseAdapter {

        private Context mContext;
        private SCAlbums mAlbums;

        private class ViewHolder {
            RelativeLayout resultContainer;
            ImageView videoImage;
            TextView videoTitle;
            TextView videoTip;
        }

        BookmarkAdapter(Context mContext, SCAlbums mResults) {
            this.mContext = mContext;
            this.mAlbums = mResults;
        }

        BookmarkAdapter(Context mContext) {
            this.mContext = mContext;
            mAlbums = new SCAlbums();
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
                ImageTools.fixVerPosterRatio(viewHolder.videoImage);
                ImageTools.displayImage(viewHolder.videoImage,album.getVerImageUrl());
            } else if(album.getHorImageUrl() != null) {
                ImageTools.fixHorPosterRatio(viewHolder.videoImage);
                ImageTools.displayImage(viewHolder.videoImage,album.getHorImageUrl());
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
            View itemView = inflater.inflate(R.layout.item_gridview_bookmark,viewGroup,false);
            viewHolder.videoImage = (ImageView) itemView.findViewById(R.id.video_image);
            viewHolder.videoTitle = (TextView) itemView.findViewById(R.id.video_title);
            viewHolder.videoTip = (TextView) itemView.findViewById(R.id.video_tip);
            viewHolder.resultContainer = (RelativeLayout)itemView.findViewById(R.id.search_result);

            itemView.setTag(viewHolder);
            return itemView;
        }
    }


}
