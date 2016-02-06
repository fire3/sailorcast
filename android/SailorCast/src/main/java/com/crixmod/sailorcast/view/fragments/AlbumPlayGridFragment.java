package com.crixmod.sailorcast.view.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.SailorCast;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCFailLog;
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
public class AlbumPlayGridFragment extends Fragment implements
        OnGetVideosListener ,AlbumPlayGridAdapter.AlbumPlayGridSelectedListener
{

    private static final String ARG_ALBUM = "album";
    private static final String ARG_IS_SHOW_TITLE = "isShowTitle";
    private static final String ARG_IS_BACKWARD = "isBackward";
    private static final String ARG_INITIAL_POSITION = "initialPosition";


    private SCAlbum mAlbum;
    private boolean mIsShowTitle;
    private boolean mIsBackward;
    private int mPageNo = 0;
    private int mPageSize = 50;//sohu最多一次列出50个剧集
    private int mPageTotal;
    private AlbumPlayGridAdapter mAdapter;
    private int mInitialVideoNoInAlbum = 0;
    private boolean mFirstSelection = true;
    private int COLUMNS_BUTTON = 6;
    private int COLUMNS_TITLE = 1;
    private int mColumns = COLUMNS_BUTTON;

    private OnAlbumPlayGridListener mListener;
    private PagingGridView mGridView;
    private int mCurrentSelected = mInitialVideoNoInAlbum;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AlbumPlayGridFragment.
     */

    public static AlbumPlayGridFragment newInstance(SCAlbum album, boolean isShowTitle, boolean isBackward, int initialVideoPosition) {
        AlbumPlayGridFragment fragment = new AlbumPlayGridFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ALBUM, album);
        args.putBoolean(ARG_IS_SHOW_TITLE, isShowTitle);
        args.putBoolean(ARG_IS_BACKWARD, isBackward);
        args.putInt(ARG_INITIAL_POSITION, initialVideoPosition);

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
            mIsBackward = getArguments().getBoolean(ARG_IS_BACKWARD);
            mAdapter = new AlbumPlayGridAdapter(getActivity(),mAlbum.getVideosTotal(), this);
            mInitialVideoNoInAlbum = getArguments().getInt(ARG_INITIAL_POSITION);
            mCurrentSelected = mInitialVideoNoInAlbum;
            mPageTotal = (mAlbum.getVideosTotal() + mPageSize - 1) / mPageSize;
            loadMoreVideos();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_album_play_grid, container, false);
        mGridView = (PagingGridView) view.findViewById(R.id.result_grid);
        mGridView.setNumColumns(mColumns);
        mAdapter.setShowTitle(mIsShowTitle);
        mGridView.setAdapter(mAdapter);
        if(mAlbum.getVideosTotal() > 0 && mAlbum.getVideosTotal() > mPageSize)
            mGridView.setHasMoreItems(true);
        else
            mGridView.setHasMoreItems(false);

        mGridView.setPagingableListener(new PagingGridView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                loadMoreVideos();
            }
        });
        return view;
    }

    public void setBackward(boolean isBackward) {
        mIsBackward = isBackward;
        mPageNo = isBackward ? mPageTotal : 0;
        mAdapter.clear();
        mAdapter.setBackward(mIsBackward);
        mAdapter.notifyDataSetChanged();
        mGridView.setHasMoreItems(true);
        mFirstSelection = true;
        mInitialVideoNoInAlbum = 0;
        mCurrentSelected = mInitialVideoNoInAlbum;
        loadMoreVideos();
    }

    public void setShowTitle(boolean showTitle) {
        mIsShowTitle = showTitle;
        if(mIsShowTitle) {
            mColumns = COLUMNS_TITLE;
        } else {
            mColumns = COLUMNS_BUTTON;
        }
        if(mAdapter!=null) {
            mAdapter.setShowTitle(mIsShowTitle);
        }
        if(mGridView != null) {
            mGridView.setNumColumns(mColumns);
            mGridView.post(new Runnable() {
                @Override
                public void run() {
                    mGridView.smoothScrollToPosition(mCurrentSelected);
                }
            });
        }
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
    public void onDestroy() {
        super.onDestroy();
        mFirstSelection = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void loadMoreVideos() {
        if(mIsBackward == false) {
            if(mPageNo < mPageTotal)
                mPageNo++;
            if(mPageNo <= mPageTotal) {
                SiteApi.doGetAlbumVideos(mAlbum, mPageNo, mPageSize, this);
                if(mPageNo == mPageTotal) {
                    if(mGridView != null) {
                        mGridView.post(new Runnable() {
                            @Override
                            public void run() {
                                mGridView.setHasMoreItems(false);
                            }
                        });
                    }
                }
            } else {
                if(mGridView != null)
                    mGridView.post(new Runnable() {
                        @Override
                        public void run() {
                            mGridView.setHasMoreItems(false);
                        }
                    });
            }
        } else {
            if(mPageNo > 0)
                SiteApi.doGetAlbumVideos(mAlbum, mPageNo, mPageSize, this);
            if(mPageNo > 0)
                mPageNo--;
            if(mPageNo == 0) {
                if(mGridView != null) {
                    mGridView.post(new Runnable() {
                        @Override
                        public void run() {
                            mGridView.setHasMoreItems(false);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onGetVideosSuccess(final SCVideos videos) {

        if(videos.size() > 0) {
            for (int i = 0; i < videos.size(); i++) {
                int index;
                if(mIsBackward == false)
                    index = i;
                else
                    index = videos.size() - i - 1;
                SCVideo v = videos.get(index);
                if(mAdapter != null)
                    mAdapter.addVideo(v);

            }
            if (mInitialVideoNoInAlbum > mAdapter.getCount())
                loadMoreVideos();
            if(mAdapter != null) {
                if (mAdapter.getCount() > mInitialVideoNoInAlbum && mFirstSelection) {
                    if(mListener != null)
                        mListener.onAlbumPlayVideoSelected(mAdapter.getItem(mInitialVideoNoInAlbum), mInitialVideoNoInAlbum);
                }
            }
            if(getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(mAdapter != null)
                            mAdapter.notifyDataSetChanged();
                        if (mGridView != null) {
                            mGridView.setIsLoading(false);
                        /*
                        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                if (mAdapter.getCount() > mInitialVideoNoInAlbum && mFirstSelection == true) {
                                    mGridView.setSelection(mInitialVideoNoInAlbum);
                                    mGridView.setItemChecked(mInitialVideoNoInAlbum, true);
                                    mFirstSelection = false;
                                    SystemClock.sleep(50);
                                    mGridView.smoothScrollToPosition(mInitialVideoNoInAlbum);
                                }
                            }
                        });
                        */
                            mGridView.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (mAdapter.getCount() > mInitialVideoNoInAlbum && mFirstSelection == true) {
                                        mGridView.setSelection(mInitialVideoNoInAlbum);
                                        mGridView.setItemChecked(mInitialVideoNoInAlbum, true);
                                        mFirstSelection = false;
                                        SystemClock.sleep(100);
                                        mGridView.smoothScrollToPosition(mInitialVideoNoInAlbum);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
        else {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mGridView != null) {
                            mGridView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mGridView.setHasMoreItems(false);
                                    mGridView.setIsLoading(false);
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onGetVideosFailed(final SCFailLog failReason) {
        try {
            if(getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mGridView != null) {
                            if (mGridView.isLoading() == true) {
                                mGridView.setIsLoading(false);
                                mGridView.setHasMoreItems(false);
                            }
                        }

                    }
                });
            }
        } catch (Exception e) {
            //Error
        }
    }

    @Override
    public void onVideoSelected(int position, SCVideo v) {

        if(mGridView != null) {
            mGridView.setSelection(position);
            mGridView.setItemChecked(position, true);
            mCurrentSelected = position;
        }
        if(mListener != null)
            mListener.onAlbumPlayVideoSelected(v, position);
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
        public void onAlbumPlayVideoSelected(SCVideo v, int videoNoInAlbum);
    }

}
