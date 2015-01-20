package com.crixmod.sailorcast.view.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.SCAlbums;
import com.crixmod.sailorcast.model.SCChannel;
import com.crixmod.sailorcast.model.SCChannelFilter;
import com.crixmod.sailorcast.model.SCSite;
import com.crixmod.sailorcast.siteapi.OnGetAlbumsListener;
import com.crixmod.sailorcast.siteapi.OnGetChannelFilterListener;
import com.crixmod.sailorcast.siteapi.SiteApi;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompassFragment extends Fragment implements OnGetAlbumsListener, OnGetChannelFilterListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompassFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompassFragment newInstance(String param1, String param2) {
        CompassFragment fragment = new CompassFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CompassFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        SiteApi.doGetChannelAlbums(SCSite.LETV, SCChannel.DOCUMENTARY,1,10,this);
        SiteApi.doGetChannelAlbums(SCSite.LETV, SCChannel.MOVIE,1,10,this);
        SiteApi.doGetChannelAlbums(SCSite.LETV, SCChannel.SHOW,1,10,this);
        SiteApi.doGetChannelFilter(SCSite.LETV, SCChannel.SHOW,this);
        SiteApi.doGetChannelFilter(SCSite.LETV, SCChannel.MOVIE,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compass, container, false);
    }


    @Override
    public void onGetAlbumsSuccess(SCAlbums albums) {
        albums.debugLog();
    }

    @Override
    public void onGetAlbumsFailed(String failReason) {

    }

    @Override
    public void onGetChannelFilterSuccess(SCChannelFilter filter) {
        Log.d("fire3", filter.toString());
    }

    @Override
    public void onGetChannelFilterFailed(String failReason) {

    }
}
