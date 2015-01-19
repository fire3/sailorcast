package com.crixmod.sailorcast.view.fragments;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.crixmod.sailorcast.database.History;
import com.crixmod.sailorcast.database.HistoryDbHelper;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCAlbums;
import com.crixmod.sailorcast.model.SCVideo;
import com.crixmod.sailorcast.utils.ImageTools;
import com.crixmod.sailorcast.view.AlbumActivity;

import java.util.ArrayList;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link com.crixmod.sailorcast.view.fragments.HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    private HistoryDbHelper mDb;

    private HistoryAdapter mAdapter;
    private String mFailReason;
    private GridView mGrid;
    private TextView mEmpty;
    private ArrayList<History> mHistories;


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
        mFailReason = SailorCast.getResource().getString(R.string.fail_reason_no_results);
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
        mHistories = mDb.getAllHistories();
        mAdapter = new HistoryAdapter(getActivity(),mHistories);
        mGrid.setAdapter(mAdapter);
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
            ImageView videoTipOverlay;
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
            if(video.getHorPic() != null &&  !video.getHorPic().isEmpty())
                ImageTools.displayImage(viewHolder.videoImage,video.getHorPic(),point.x,point.y);
            else
                ImageTools.displayImage(viewHolder.videoImage,album.getHorImageUrl(),point.x,point.y);

            viewHolder.videoChecker.setChecked(bookmarkHistory.getChecked());

            if(mShowChecker == false) {
                viewHolder.resultContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlbumActivity.launch((Activity) mContext, album,video.getSeqInAlbum() - 1);
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
            reloadBookmarks();
        }


        private CompoundButton.OnCheckedChangeListener CheckBox_OnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                View view = (View)buttonView.getParent();
                ViewHolder holder = (ViewHolder)view.getTag();
                BookmarkHistory item = getItem(holder.position);
                item.setChecked(isChecked);
                //Log.d("fire3",String.format("checked changel album %s, checked %s",item.getAlbum().getTitle(), String.valueOf(isChecked)));
            }};


    private View getOneColumnVideoRowView(int position, ViewGroup viewGroup, ViewHolder viewHolder) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            View itemView = inflater.inflate(R.layout.item_gridview_history,viewGroup,false);
            viewHolder.videoImage = (ImageView) itemView.findViewById(R.id.video_image);
            viewHolder.videoTipOverlay = (ImageView) itemView.findViewById(R.id.video_tip_overlay);
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

            viewHolder.videoChecker.setOnCheckedChangeListener(CheckBox_OnCheckedChangeListener);
            itemView.setTag(viewHolder);
            return itemView;
        }
    }
}
