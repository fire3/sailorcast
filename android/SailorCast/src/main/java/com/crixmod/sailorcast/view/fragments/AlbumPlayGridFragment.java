package com.crixmod.sailorcast.view.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCVideo;
import com.crixmod.sailorcast.model.SCVideos;
import com.crixmod.sailorcast.siteapi.OnGetVideosListener;
import com.crixmod.sailorcast.siteapi.SiteApi;
import com.crixmod.sailorcast.uiutils.pagingridview.PagingGridView;
import com.crixmod.sailorcast.view.adapters.AlbumPlayGridAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.crixmod.sailorcast.view.fragments.AlbumPlayGridFragment.OnAlbumPlayGridListener} interface
 * to handle interaction events.
 * Use the {@link AlbumPlayGridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumPlayGridFragment extends Fragment implements OnGetVideosListener {

    private static final String ARG_ALBUM = "album";
    private static final String ARG_IS_SHOW_TITLE = "isShowTitle";


    private SCAlbum mAlbum;
    private boolean mIsShowTitle;
    private int mPageNo = 0;
    private int mPageSize = 50;
    private AlbumPlayGridAdapter mAdapter;

    private OnAlbumPlayGridListener mListener;
    private PagingGridView mGridView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AlbumPlayGridFragment.
     */

    public static AlbumPlayGridFragment newInstance(SCAlbum album, boolean isShowTitle) {
        AlbumPlayGridFragment fragment = new AlbumPlayGridFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ALBUM, album);
        args.putBoolean(ARG_IS_SHOW_TITLE, isShowTitle);

        fragment.setArguments(args);
        return fragment;
    }

    public AlbumPlayGridFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAlbum = getArguments().getParcelable(ARG_ALBUM);
            mIsShowTitle = getArguments().getBoolean(ARG_IS_SHOW_TITLE);
            mAdapter = new AlbumPlayGridAdapter(getActivity());
            loadMoreVideos();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_album_play_grid, container, false);
        mGridView = (PagingGridView) view.findViewById(R.id.result_grid);
        mGridView.setAdapter(mAdapter);
        mGridView.setHasMoreItems(true);
        mGridView.setPagingableListener(new PagingGridView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                loadMoreVideos();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnAlbumPlayGridListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void loadMoreVideos() {
        mPageNo ++ ;
        SiteApi.doGetAlbumVideos(mAlbum, mPageNo, mPageSize, this);
    }

    @Override
    public void onGetVideosSuccess(SCVideos videos) {
        if(videos.size() > 0) {
            for (SCVideo v : videos) {
                mAdapter.addVideo(v);
            }

            mGridView.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                    mGridView.setIsLoading(false);
                }
            });
        }
        else {

            mGridView.post(new Runnable() {
                @Override
                public void run() {
                    mGridView.setHasMoreItems(false);
                    mGridView.setIsLoading(false);
                }
            });
        }
    }

    @Override
    public void onGetVideosFailed(String failReason) {
        mGridView.post(new Runnable() {
            @Override
            public void run() {
                mGridView.setIsLoading(false);
                mGridView.setHasMoreItems(false);
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnAlbumPlayGridListener {
        public void onVideoSelected(SCVideo v, int videoNoInAlbum);
    }

}
