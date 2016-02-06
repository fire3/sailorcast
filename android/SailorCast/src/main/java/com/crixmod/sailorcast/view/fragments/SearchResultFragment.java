package com.crixmod.sailorcast.view.fragments;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
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
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCAlbums;
import com.crixmod.sailorcast.model.SCFailLog;
import com.crixmod.sailorcast.model.SCSite;
import com.crixmod.sailorcast.siteapi.OnGetAlbumsListener;
import com.crixmod.sailorcast.siteapi.SiteApi;
import com.crixmod.sailorcast.utils.ImageTools;
import com.crixmod.sailorcast.view.AlbumDetailActivity;

import java.util.concurrent.atomic.AtomicInteger;

public class SearchResultFragment extends Fragment
implements OnGetAlbumsListener
{
    private static final String ARG_KEYWORD = "key";
    private static final String ARG_SITEID = "siteID";

    private String mKeyword;
    private int    mSiteID = -1;

    private SearchResultAdapter mAdapter;
    private String mFailReason;
    private GridView mGrid;
    private TextView mEmpty;

    AtomicInteger mFailCount;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param keyword Parameter 1.
     * @return A new instance of fragment SearchResultFragment.
     */
    public static SearchResultFragment newInstance(String keyword, int siteID) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_KEYWORD, keyword);
        args.putInt(ARG_SITEID, siteID);
        fragment.setArguments(args);
        return fragment;
    }

    public static SearchResultFragment newInstance(String keyword) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_KEYWORD, keyword);
        fragment.setArguments(args);
        return fragment;
    }


    public SearchResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFailCount = new AtomicInteger(0);
        if (getArguments() != null) {
            mKeyword = getArguments().getString(ARG_KEYWORD);
            mSiteID = getArguments().getInt(ARG_SITEID, -1);
        }
        mAdapter = new SearchResultAdapter(getActivity());
        if(mSiteID != -1)
            SiteApi.doSearch(mSiteID, mKeyword, this);
        else {
            SiteApi.doSearchAll(mKeyword,this);
        }
        mFailReason = SailorCast.getResource().getString(R.string.fail_reason_searching);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_search_result, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mGrid = (GridView) view.findViewById(R.id.result_grid);
        mEmpty = (TextView) view.findViewById(android.R.id.empty);
        mEmpty.setText(mFailReason);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mGrid.setAdapter(mAdapter);
        mGrid.setEmptyView(mEmpty);
    }

    @Override
    public void onGetAlbumsSuccess(SCAlbums albums) {
        for(SCAlbum a : albums) {
            if(mAdapter != null)
                mAdapter.addAlbum(a);
        }
        if(getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mAdapter != null)
                        mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onGetAlbumsFailed(final SCFailLog err) {
        int newCount = mFailCount.incrementAndGet();
        if(newCount == SCSite.count()) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mFailReason = getResources().getString(R.string.fail_reason_no_results);
                        mEmpty.setText(mFailReason);
                    }
                });
            }
        }
    }



    class SearchResultAdapter extends BaseAdapter {

        private Context mContext;
        private SCAlbums mAlbums;

        private class ViewHolder {
            LinearLayout resultContainer;
            ImageView videoImage;
            TextView videoTitle;
            TextView videoTip;
            TextView videoSite;
        }

        SearchResultAdapter(Context mContext, SCAlbums mResults) {
            this.mContext = mContext;
            this.mAlbums = mResults;
        }

        SearchResultAdapter(Context mContext) {
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
            viewHolder.videoSite.setText(getResources().getString(R.string.album_from) + album.getSite().getSiteName());

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
            } else {
                Point point = ImageTools.getGridVerPosterSize(mContext, 3);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.videoImage.getLayoutParams();
                params.width = point.x;
                params.height = point.y;
                viewHolder.videoImage.setImageDrawable(getResources().getDrawable(R.drawable.loading));
            }

            viewHolder.resultContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     AlbumDetailActivity.launch((Activity) mContext, album,0,true);
                }
            });
        }

        private View getOneColumnVideoRowView(ViewGroup viewGroup, ViewHolder viewHolder) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            View itemView = inflater.inflate(R.layout.item_gridview_search_result,viewGroup,false);
            viewHolder.videoImage = (ImageView) itemView.findViewById(R.id.video_image);
            viewHolder.videoTitle = (TextView) itemView.findViewById(R.id.video_title);
            viewHolder.videoTip = (TextView) itemView.findViewById(R.id.video_tip);
            viewHolder.videoSite = (TextView) itemView.findViewById(R.id.video_site);
            viewHolder.resultContainer = (LinearLayout)itemView.findViewById(R.id.search_result);

            itemView.setTag(viewHolder);
            return itemView;
        }
    }

}
