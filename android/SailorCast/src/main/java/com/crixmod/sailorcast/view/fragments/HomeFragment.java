package com.crixmod.sailorcast.view.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.SCBanner;
import com.crixmod.sailorcast.model.SCBanners;
import com.crixmod.sailorcast.siteapi.OnGetBannersListener;
import com.crixmod.sailorcast.siteapi.SohuApi;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment  implements OnGetBannersListener{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private ScrollView mScrollView;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchFragment.
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SohuApi().getBanners(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        mScrollView = (ScrollView) view.findViewById(R.id.scroll_container);
        return view;
    }

    @Override
    public void onGetBannersSuccess(SCBanners banners) {
        for(SCBanner b: banners) {
           Log.d("fire3", String.format("name %s type %d album %s", b.getTitle(), b.getType(), b.getAlbums().get(0).toString()));
        }
    }

    @Override
    public void onGetBannersFailed(String failReason) {

    }
}
