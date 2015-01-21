package com.crixmod.sailorcast.view.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCAlbums;
import com.crixmod.sailorcast.siteapi.OnGetAlbumsListener;
import com.crixmod.sailorcast.siteapi.SiteApi;
import com.crixmod.sailorcast.uiutils.pagingridview.PagingGridView;
import com.crixmod.sailorcast.view.adapters.AlbumListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumListFragment extends Fragment implements OnGetAlbumsListener{
    private static final String ARG_CHANNEL_ID = "channelID";
    private static final String ARG_SITE_ID = "siteID";

    private int mChannelID;
    private int mSiteID;
    private PagingGridView mGridView;
    private int mPageNo = 0;
    private int mPageSize = 30;
    private AlbumListAdapter mAdapter;


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
            mSiteID = getArguments().getInt(ARG_SITE_ID);
            mChannelID = getArguments().getInt(ARG_CHANNEL_ID);
            loadMoreAlbums();
            mAdapter = new AlbumListAdapter(getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_album_list, container, false);
        mGridView = (PagingGridView) view.findViewById(R.id.result_grid);
        mGridView.setAdapter(mAdapter);
        return view;
    }

    public void loadMoreAlbums() {
        mPageNo ++ ;
        SiteApi.doGetChannelAlbums(mSiteID,mChannelID,mPageNo,mPageSize,this);
    }


    @Override
    public void onGetAlbumsSuccess(SCAlbums albums) {
        for(SCAlbum a : albums) {
            mAdapter.addAlbum(a);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetAlbumsFailed(String failReason) {

    }
}
