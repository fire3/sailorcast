package com.crixmod.sailorcast.view.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.SailorCast;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCAlbums;
import com.crixmod.sailorcast.model.SCChannel;
import com.crixmod.sailorcast.model.SCChannelFilter;
import com.crixmod.sailorcast.model.SCFailLog;
import com.crixmod.sailorcast.model.SCSite;
import com.crixmod.sailorcast.siteapi.OnGetAlbumsListener;
import com.crixmod.sailorcast.siteapi.OnGetChannelFilterListener;
import com.crixmod.sailorcast.siteapi.SiteApi;
import com.crixmod.sailorcast.uiutils.pagingridview.PagingGridView;
import com.crixmod.sailorcast.view.AlbumFilterDialog;
import com.crixmod.sailorcast.view.adapters.AlbumListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumListFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,
        OnGetAlbumsListener, OnGetChannelFilterListener{
    private static final String ARG_CHANNEL_ID = "channelID";
    private static final String ARG_SITE_ID = "siteID";

    private int mChannelID;
    private int mSiteID;
    private PagingGridView mGridView;
    private TextView mEmptyView;
    private int mPageNo = 0;
    private int mPageSize = 30;
    private AlbumListAdapter mAdapter;
    private int mColumns = 3;
    private SCChannelFilter mFilter;
    private boolean inFilterMode;
    private SwipeRefreshLayout mSwipeContainer;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AlbumListFragment.
     */
    public static AlbumListFragment newInstance(int mSiteID, int mChannelID) {
        AlbumListFragment fragment = new AlbumListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CHANNEL_ID, mChannelID);
        args.putInt(ARG_SITE_ID, mSiteID);
        fragment.setArguments(args);
        return fragment;
    }

    public AlbumListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPageNo = 0;
            mSiteID = getArguments().getInt(ARG_SITE_ID);
            mChannelID = getArguments().getInt(ARG_CHANNEL_ID);
            loadMoreAlbums();
            mAdapter = new AlbumListAdapter(getActivity(), new SCChannel(mChannelID));
            if(mSiteID == SCSite.LETV) {
                mColumns = 2;
                mAdapter.setColumns(mColumns);
            }

            if(mChannelID == SCChannel.MUSIC) {
                mColumns = 2;
                mAdapter.setColumns(mColumns);
            }
            SiteApi.doGetChannelFilter(mSiteID,mChannelID,this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_album_list, container, false);
        mSwipeContainer = (SwipeRefreshLayout) view;
        mSwipeContainer.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);
        mSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mGridView = (PagingGridView) view.findViewById(R.id.result_grid);
        mEmptyView = (TextView) view.findViewById(android.R.id.empty);
        mEmptyView.setText(getResources().getString(R.string.loading));

        mGridView.setEmptyView(mEmptyView);
        mGridView.setNumColumns(mColumns);
        mGridView.setAdapter(mAdapter);
        mGridView.setHasMoreItems(true);
        mGridView.setScrollableListener(new PagingGridView.Scrollable() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                boolean enable = false;
                if (mGridView != null && mGridView.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = mGridView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = mGridView.getChildAt(0).getTop() >= 0;
                    // enabling or disabling the refresh layout

                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                mSwipeContainer.setEnabled(enable);
            }
        });
        mGridView.setPagingableListener(new PagingGridView.Pagingable() {

            @Override
            public void onLoadMoreItems() {
                loadMoreAlbums();
            }
        });

        return view;
    }


    public void loadMoreAlbums() {
        mPageNo ++ ;
        if(inFilterMode)
            SiteApi.doGetChannelAlbumsByFilter(mSiteID, mChannelID, mPageNo, mPageSize, mFilter, this);
        else
            SiteApi.doGetChannelAlbums(mSiteID, mChannelID, mPageNo, mPageSize, this);
    }


    @Override
    public synchronized void onGetAlbumsSuccess(SCAlbums albums) {

        if (mColumns == 3) {
            if (albums.size() > 0) {
                if (albums.get(0).getVerImageUrl() == null) {
                    mColumns = 2;

                    if(getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mGridView.setNumColumns(mColumns);
                                mAdapter.setColumns(mColumns);
                            }
                        });
                    }
                }
            }
        }

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
                    if (mGridView != null)
                        mGridView.setIsLoading(false);
                    if (mSwipeContainer != null)
                        mSwipeContainer.setRefreshing(false);
                }
            });
        }
    }

    @Override
    public void onGetAlbumsFailed(SCFailLog err) {

        try {
            if (getActivity() != null) {
                if(err.getType() == SCFailLog.TYPE_FATAL_ERR)
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mGridView != null) {
                            mGridView.setIsLoading(false);
                            mGridView.setHasMoreItems(false);
                            mEmptyView.setText(getResources().getString(R.string.album_no_videos));
                        }

                        if (mSwipeContainer != null)
                            mSwipeContainer.setRefreshing(false);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetChannelFilterSuccess(SCChannelFilter filter) {
        if(filter != null)
            mFilter = filter;
    }

    public void setChannelFilter(SCChannelFilter filter) {
        if(filter != null) {
            mFilter = filter;
            inFilterMode = true;
            onRefresh();
        }
    }


    @Override
    public void onGetChannelFilterFailed(SCFailLog err) {

        if(getActivity() != null) {
        }
    }

    public void showAlbumFilterDialog(FragmentActivity activity) {
        FragmentManager fm = activity.getSupportFragmentManager();
        if(mFilter != null) {
            AlbumFilterDialog dialog = AlbumFilterDialog.newInstance(mSiteID, mChannelID, mFilter);
            dialog.show(fm, "");
        }
    }

    @Override
    public void onRefresh() {
        mAdapter = new AlbumListAdapter(getActivity(), new SCChannel(mChannelID));
        mPageNo = 0;
        mGridView.setHasMoreItems(true);
        if(mSiteID == SCSite.LETV) {
            mColumns = 2;
            mAdapter.setColumns(mColumns);
        }

        if(mChannelID == SCChannel.MUSIC) {
            mColumns = 2;
            mAdapter.setColumns(mColumns);
        }
        mGridView.setAdapter(mAdapter);
        mEmptyView.setText(getResources().getString(R.string.loading));
        loadMoreAlbums();
    }
}
