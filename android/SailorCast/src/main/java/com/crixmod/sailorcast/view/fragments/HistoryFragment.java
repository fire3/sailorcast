package com.crixmod.sailorcast.view.fragments;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.SailorCast;
import com.crixmod.sailorcast.database.History;
import com.crixmod.sailorcast.database.HistoryDbHelper;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCAlbums;
import com.crixmod.sailorcast.model.SCVideo;
import com.crixmod.sailorcast.utils.ImageTools;
import com.crixmod.sailorcast.view.AlbumDetailActivity;

import java.util.ArrayList;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link com.crixmod.sailorcast.view.fragments.HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private HistoryDbHelper mDb;

    private HistoryAdapter mAdapter;
    private String mFailReason;
    private GridView mGrid;
    private TextView mEmpty;
    private ArrayList<History> mHistories;
    private SwipeRefreshLayout mSwipeContainer;


    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mDb = new HistoryDbHelper(getActivity());
        mHistories = mDb.getAllHistories();
        mAdapter = new HistoryAdapter(getActivity(),mHistories);
        mFailReason = SailorCast.getResource().getString(R.string.fail_reason_no_history);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_history, container, false);
        mGrid = (GridView) view.findViewById(R.id.result_grid);
        mEmpty = (TextView) view.findViewById(android.R.id.empty);
        mGrid.setEmptyView(mEmpty);
        mEmpty.setText(mFailReason);
        mGrid.setAdapter(mAdapter);
        mSwipeContainer = (SwipeRefreshLayout) view;
        mSwipeContainer.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);
        mSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        mGrid.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (mGrid != null && mGrid.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = mGrid.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = mGrid.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                mSwipeContainer.setEnabled(enable);
            }
        });
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

    public void deleteSelectedHistory() {
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

    public void reloadHistories() {
        mHistories = mDb.getAllHistories();
        mAdapter = new HistoryAdapter(getActivity(),mHistories);
        mGrid.setAdapter(mAdapter);
        mSwipeContainer.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        reloadHistories();
    }


    class HistoryAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<History> mhistories;
        private boolean mShowChecker  = false;
        private ArrayList<BookmarkHistory> mBookmarkHistories;

        private class BookmarkHistory {
            History history;
            boolean isChecked;
            public BookmarkHistory(History a) {
                this.history = a;
                isChecked = false;
            }
            public History getHistory() {
                return history;
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
            TextView videoStatus;
        }

        HistoryAdapter(Context mContext, ArrayList<History> mResults) {
            this.mContext = mContext;
            this.mhistories = mResults;
            this.mShowChecker = false;
            this.mBookmarkHistories = new ArrayList<>();
            for(History a: mhistories) {
                mBookmarkHistories.add(new BookmarkHistory(a));
            }
        }

        public void uncheckAllAlbums() {
            for(BookmarkHistory a : mBookmarkHistories) {
                a.setUnChecked();
            }
        }

        public void setShowChecker(boolean showChecker) {
            this.mShowChecker = showChecker;
        }


        @Override
        public int getCount() {
            return mhistories.size();
        }

        @Override
        public BookmarkHistory getItem(int i) {
            return mBookmarkHistories.get(i);
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
            BookmarkHistory album = getItem(i);
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = getOneColumnVideoRowView(i,viewGroup, viewHolder);
            }
            else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.position = i;
            setupViewHolder(view,i,viewHolder,album);

            return view;
        }

        private void setupViewHolder(View view, int i, final ViewHolder viewHolder, final BookmarkHistory bookmarkHistory) {
            final SCAlbum album = bookmarkHistory.getHistory().getAlbum();
            final SCVideo video = bookmarkHistory.getHistory().getVideo();
            viewHolder.videoTitle.setVisibility(View.GONE);
            viewHolder.videoTip.setText(album.getTitle());
            viewHolder.videoStatus.setText(video.getVideoTitle());

            Point point = ImageTools.getGridHorPosterSize(mContext, 2);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.videoImage.getLayoutParams();
            params.width = point.x;
            params.height = point.y;
            viewHolder.videoImage.setLayoutParams(params);
            if(video.getHorPic() != null &&  !video.getHorPic().isEmpty())
                ImageTools.displayImage(viewHolder.videoImage,video.getHorPic(),point.x,point.y);
            else if(album.getHorImageUrl()!= null && !album.getHorImageUrl().isEmpty())
                ImageTools.displayImage(viewHolder.videoImage,album.getHorImageUrl(),point.x,point.y);
            else if(album.getVerImageUrl()!= null && !album.getVerImageUrl().isEmpty())
                ImageTools.displayImage(viewHolder.videoImage,album.getVerImageUrl(),point.x,point.y);

            viewHolder.videoChecker.setChecked(bookmarkHistory.getChecked());

            if(mShowChecker == false) {
                viewHolder.resultContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlbumDetailActivity.launch((Activity) mContext, album, video.getSeqInAlbum() - 1);
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
                        boolean checked = bookmarkHistory.getChecked();
                        viewHolder.videoChecker.setChecked(!checked);
                        bookmarkHistory.setChecked(!checked);
                    }
                });
            }
        }

        public SCAlbums getCheckedSCAlbums(){
            SCAlbums lstItem = new SCAlbums();
            for ( int i = 0; i < getCount(); i++) {
                if (getItem(i).getChecked()){
                    lstItem.add(getItem(i).getHistory().getAlbum());
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
                SCAlbum a = mBookmarkHistories.get(i).getHistory().getAlbum();
                mDb.deleteAlbum(a.getAlbumId(),a.getSite().getSiteID());
            }
            reloadHistories();
        }



    private View getOneColumnVideoRowView(int position, ViewGroup viewGroup, ViewHolder viewHolder) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            View itemView = inflater.inflate(R.layout.item_gridview_history,viewGroup,false);
            viewHolder.videoImage = (ImageView) itemView.findViewById(R.id.video_image);
            viewHolder.videoTitle = (TextView) itemView.findViewById(R.id.video_title);
            viewHolder.videoTip = (TextView) itemView.findViewById(R.id.video_tip);
            viewHolder.videoStatus = (TextView) itemView.findViewById(R.id.video_play_status);

            viewHolder.resultContainer = (RelativeLayout)itemView;
            viewHolder.videoChecker = (CheckBox)itemView.findViewById(R.id.video_checkbox);
            viewHolder.position = position;
            if(mShowChecker)
                viewHolder.videoChecker.setVisibility(View.VISIBLE);
            else
                viewHolder.videoChecker.setVisibility(View.GONE);

            itemView.setTag(viewHolder);
            return itemView;
        }
    }
}
