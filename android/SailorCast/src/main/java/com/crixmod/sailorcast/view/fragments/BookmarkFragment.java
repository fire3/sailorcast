package com.crixmod.sailorcast.view.fragments;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.SailorCast;
import com.crixmod.sailorcast.database.BookmarkDbHelper;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCAlbums;
import com.crixmod.sailorcast.utils.ImageTools;
import com.crixmod.sailorcast.view.AlbumDetailActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookmarkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookmarkFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private BookmarkDbHelper mDb;

    private BookmarkAdapter mAdapter;
    private String mFailReason;
    private GridView mGrid;
    private TextView mEmpty;
    private SCAlbums mAlbums;
    private SwipeRefreshLayout mSwipeContainer;


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
        mFailReason = SailorCast.getResource().getString(R.string.fail_reason_no_bookmark);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_bookmark, container, false);
        mGrid = (GridView) view.findViewById(R.id.result_grid);
        mEmpty = (TextView) view.findViewById(android.R.id.empty);
        mGrid.setEmptyView(mEmpty);
        mEmpty.setText(mFailReason);
        mGrid.setAdapter(mAdapter);

        mSwipeContainer = (SwipeRefreshLayout) view;
        mSwipeContainer.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);
        mSwipeContainer.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void deleteSelectedBookmark() {
        mAdapter.deleteCheckedSCAlbums();
        mAdapter.setShowChecker(false);
        mAdapter.notifyDataSetChanged();
    }

    public boolean isShowDeleteChecker() {
        return mAdapter.mShowChecker;
    }

    public void setShowDeleteChecker(boolean isShowChecker) {
        if(isShowChecker == false) {
            mAdapter.uncheckAllAlbums();
        }
        mAdapter.setShowChecker(isShowChecker);
        mAdapter.notifyDataSetInvalidated();
    }

    public void reloadBookmarks() {
        mAlbums = mDb.getAllAlbums();
        mAdapter = new BookmarkAdapter(getActivity(),mAlbums);
        mGrid.setAdapter(mAdapter);
        mSwipeContainer.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        reloadBookmarks();
    }


    class BookmarkAdapter extends BaseAdapter {

        private Context mContext;
        private SCAlbums mAlbums;
        private boolean mShowChecker  = false;
        private ArrayList<BookmarkAlbum> mBookmarkAlbums;

        private class BookmarkAlbum {
            SCAlbum album;
            boolean isChecked;
            public BookmarkAlbum (SCAlbum a) {
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

        private class ViewHolder {
            int position;
            RelativeLayout resultContainer;
            ImageView videoImage;
            TextView videoTitle;
            TextView videoTip;
            CheckBox videoChecker;
        }

        BookmarkAdapter(Context mContext, SCAlbums mResults) {
            this.mContext = mContext;
            this.mAlbums = mResults;
            this.mShowChecker = false;
            this.mBookmarkAlbums = new ArrayList<>();
            for(SCAlbum a: mAlbums) {
                mBookmarkAlbums.add(new BookmarkAlbum(a));
            }
        }

        public void uncheckAllAlbums() {
            for(BookmarkAlbum a :mBookmarkAlbums) {
                a.setUnChecked();
            }
        }

        public void setShowChecker(boolean showChecker) {
            this.mShowChecker = showChecker;
        }


        @Override
        public int getCount() {
            return mAlbums.size();
        }

        @Override
        public BookmarkAlbum getItem(int i) {
            return mBookmarkAlbums.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if(mShowChecker)
                return 1;
            else
                return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            BookmarkAlbum album = getItem(i);
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = getOneColumnVideoRowView(i,viewGroup, viewHolder);
            }
            else {
                viewHolder = (ViewHolder) view.getTag();
            }
            setupViewHolder(view,i,viewHolder,album);

            return view;
        }

        private void setupViewHolder(View view, int i, final ViewHolder viewHolder, final BookmarkAlbum bookmarkAlbum) {
            final SCAlbum album = bookmarkAlbum.getAlbum();
            viewHolder.videoTitle.setText(album.getTitle());
            viewHolder.videoTip.setText(album.getTip());

            Point point = ImageTools.getGridVerPosterSize(mContext,3);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.videoImage.getLayoutParams();
            params.width = point.x;
            params.height = point.y;
            ImageTools.displayImage(viewHolder.videoImage,album.getVerImageUrl(),point.x,point.y);
            viewHolder.videoChecker.setChecked(bookmarkAlbum.getChecked());

            if(mShowChecker == false) {
                viewHolder.resultContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlbumDetailActivity.launch((Activity) mContext, album);
                    }
                });

                viewHolder.resultContainer.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mAdapter.setShowChecker(true);
                        mAdapter.notifyDataSetChanged();
                        return true;
                    }
                });
            }

            if(mShowChecker == true) {
                viewHolder.resultContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean checked = getItem(viewHolder.position).getChecked();
                        viewHolder.videoChecker.setChecked(!checked);
                        getItem(viewHolder.position).setChecked(!checked);
                    }
                });
            }
        }

        public SCAlbums getCheckedSCAlbums(){
            SCAlbums lstItem = new SCAlbums();
            for ( int i = 0; i < getCount(); i++) {
                if (getItem(i).getChecked()){
                    lstItem.add(getItem(i).getAlbum());
                }
            }
            return lstItem;
        }

        public void deleteCheckedSCAlbums() {
            ArrayList<Integer> lstItem = new ArrayList<>();
            for ( int i = 0; i < getCount(); i++) {
                if (getItem(i).getChecked()){
                    lstItem.add(i);
                }
            }
            for(int i : lstItem) {
                SCAlbum a = mBookmarkAlbums.get(i).getAlbum();
                mDb.deleteAlbum(a.getAlbumId(),a.getSite().getSiteID());
            }
            reloadBookmarks();
        }


        private CompoundButton.OnCheckedChangeListener CheckBox_OnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                View view = (View)buttonView.getParent();
                ViewHolder holder = (ViewHolder)view.getTag();
                BookmarkAlbum item = getItem(holder.position);
                item.setChecked(isChecked);
                //Log.d("fire3",String.format("checked changel album %s, checked %s",item.getAlbum().getTitle(), String.valueOf(isChecked)));
            }};


    private View getOneColumnVideoRowView(int position, ViewGroup viewGroup, ViewHolder viewHolder) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            View itemView = inflater.inflate(R.layout.item_gridview_bookmark,viewGroup,false);
            viewHolder.videoImage = (ImageView) itemView.findViewById(R.id.video_image);
            viewHolder.videoTitle = (TextView) itemView.findViewById(R.id.video_title);
            viewHolder.videoTip = (TextView) itemView.findViewById(R.id.video_tip);
            viewHolder.resultContainer = (RelativeLayout)itemView;
            viewHolder.videoChecker = (CheckBox)itemView.findViewById(R.id.video_checkbox);
            viewHolder.position = position;
            if(mShowChecker)
                viewHolder.videoChecker.setVisibility(View.VISIBLE);
            else
                viewHolder.videoChecker.setVisibility(View.GONE);

            viewHolder.videoChecker.setOnCheckedChangeListener(CheckBox_OnCheckedChangeListener);
            itemView.setTag(viewHolder);
            return itemView;
        }
    }


}
