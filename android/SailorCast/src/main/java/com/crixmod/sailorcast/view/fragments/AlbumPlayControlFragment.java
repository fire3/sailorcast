package com.crixmod.sailorcast.view.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCVideo;
import com.crixmod.sailorcast.model.SCVideos;
import com.crixmod.sailorcast.siteapi.OnGetVideosListener;
import com.crixmod.sailorcast.siteapi.SiteApi;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link AlbumPlayControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumPlayControlFragment extends Fragment implements OnGetVideosListener {
    static final String LOG_TAG = "AlbumPlayControlFragment";
    private static final String ARG_ALBUM = "album";
    private static final String ARG_PAGE_NO = "pageNo";
    private static final String ARG_PAGE_SIZE = "pageSize";
    private static final String ARG_IS_SHOW_TITLE = "isShowTitle";

    private GridView mGrid;
    private LinearLayout mContainer;
    private int mPageNo;
    private int mPageSize;
    private SCAlbum mAlbum;
    private SCVideos mVideos;
    private AlbumVideoGridAdaptor mAdapter;
    private OnAlbumPlayInteractionListener mListener;
    private boolean mIsShowTitle;


    public static AlbumPlayControlFragment newInstance(SCAlbum album, int pageNo, int pageSize, boolean isShowTitle) {
        AlbumPlayControlFragment fragment = new AlbumPlayControlFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ALBUM, album);
        args.putInt(ARG_PAGE_NO, pageNo);
        args.putInt(ARG_PAGE_SIZE, pageSize);
        args.putBoolean(ARG_IS_SHOW_TITLE, isShowTitle);

        fragment.setArguments(args);
        return fragment;
    }

    public AlbumPlayControlFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAlbum = getArguments().getParcelable(ARG_ALBUM);
            mPageNo = getArguments().getInt(ARG_PAGE_NO);
            mPageSize = getArguments().getInt(ARG_PAGE_SIZE);
            mIsShowTitle = getArguments().getBoolean(ARG_IS_SHOW_TITLE);
            Log.d("fire3","AlbumPlayControlFragmet: " + mIsShowTitle);
            SiteApi.doGetAlbumVideos(mAlbum, mPageNo, mPageSize, this);
        }
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContainer = (LinearLayout)view.findViewById(R.id.container);
        if(mAdapter != null)
            mGrid.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_album_play_control, container, false);

        inflater.inflate(R.layout.album_play_gridview, (ViewGroup) view,true);
        mGrid = (GridView) view.findViewById(R.id.album_play_control_grid);
        if(mIsShowTitle)
            mGrid.setNumColumns(1);
        else
            mGrid.setNumColumns(5);

        return view;
    }



    @Override
    public void onResume() {
        super.onResume();

    }

    public void selectVideoItem(final int item) {
        Log.d("fire3", "selectVideoItem item: "  + item);


        mGrid.postDelayed(new Runnable() {
            @Override
            public void run() {

                final int numVisibleChildren = mGrid.getChildCount();
                final int firstVisiblePosition = mGrid.getFirstVisiblePosition();
                for ( int i = 0; i < numVisibleChildren; i++ ) {
                    int positionOfView = firstVisiblePosition + i;

                    if (positionOfView == item) {
                        View view = mGrid.getChildAt(i);
                        if(isAdded())
                            view.requestFocus();
                    }
                }
            }
        },100);
    }

    private void onVideosReady(SCVideos videos) {
        int count = videos.size();
        mVideos = videos;
        mAdapter = new AlbumVideoGridAdaptor(getActivity(),(mPageNo-1)*mPageSize + 1,count);

        if(mGrid != null) {
            mGrid.post(new Runnable() {
                @Override
                public void run() {
                    mGrid.setAdapter(mAdapter);
                    if (mIsShowTitle == true)
                        mGrid.setNumColumns(1);
                    mListener.onVideosLoadFinished();
                }
            });
        //这里有些bug，因为Adapter刚刚设置，mGrid的View可能没有创建好，selectVideoItem方法可能无效。
        //暂时采用等待10ms的方式绕过这个问题，可能存在更好的方法。
        /*
        mGrid.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListener.onVideosLoadFinished();
            }
        },100);
        */

            /*
            mGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    mListener.onVideosLoadFinished();
                    mGrid.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
            */
        }
    }

    private void handleFocusView(View view) {
        view.setBackground(getResources().getDrawable(R.drawable.btn_red_pressed));
        final SCVideo v = (SCVideo) view.getTag();
        final Integer vno = (Integer) view.getTag(R.id.key_video_number_in_album);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListener.onVideoSelected(v,vno);
            }
        }, 10);
    }

    @Override
    public void onGetVideosSuccess(SCVideos videos) {
        onVideosReady(videos);
    }

    @Override
    public void onGetVideosFailed(String failReason) {

    }

    class AlbumVideoGridAdaptor extends BaseAdapter {
        private Context mContext;
        private int mInitialNumber;
        private int mCount;

        AlbumVideoGridAdaptor(Context mContext, int mInitialNumber, int mCount) {
            this.mContext = mContext;
            this.mInitialNumber = mInitialNumber;
            this.mCount = mCount;
        }

        @Override
        public int getCount() {
            return mCount;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        private View getButtonView(int i, View view, ViewGroup viewGroup) {
             if(view == null) {

                final Button b = (Button)
                        getActivity().getLayoutInflater().inflate(R.layout.album_play_gridview_button,viewGroup,false);
                b.setFocusable(true);
                b.setFocusableInTouchMode(true);

                Integer index = mInitialNumber + i;
                b.setText("" + index);
                b.setBackground(getResources().getDrawable(R.drawable.btn_red_disabled));
                SCVideo v = mVideos.get(i);
                b.setTag(v);
                b.setTag(R.id.key_video_number_in_album,index - 1);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.requestFocus();
                    }
                });
                b.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (b == true) {
                            handleFocusView(view);
                        } else
                            view.setBackground(getResources().getDrawable(R.drawable.btn_red_disabled));
                    }
                });
                return b;
            }
            return view;
        }

        private View getDetailView(int i, View view, ViewGroup viewGroup) {
            if(view == null) {
                TextView text =
                        (TextView) getActivity().getLayoutInflater().inflate(R.layout.album_play_gridview_text,viewGroup,false);
                text.setFocusable(true);
                text.setFocusableInTouchMode(true);
                SCVideo v = mVideos.get(i);
                text.setTag(v);
                int index = mInitialNumber + i;
                text.setTag(R.id.key_video_number_in_album, index - 1);
                if(v.getVideoTitle() != null)
                    text.setText(v.getVideoTitle());
                text.setBackground(getResources().getDrawable(R.drawable.square_border));
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.requestFocus();
                    }
                });
                text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (b == true) {
                            ((TextView)(view)).setTextColor(getResources().getColor(R.color.orange_dark));
                            final SCVideo v = (SCVideo) view.getTag();
                            final Integer vno = (Integer) view.getTag(R.id.key_video_number_in_album);
                            if(v != null)
                                mListener.onVideoSelected(v,vno);
                        } else {
                            ((TextView)(view)).setTextColor(getResources().getColor(R.color.grey_medium));
                            //view.setBackgroundColor(getResources().getColor(R.color.Wheat));
                        }
                    }
                });
                return text;
            }
            return view;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(mIsShowTitle == false)
                return getButtonView(i, view, viewGroup);
            else
                return getDetailView(i,view,viewGroup);
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnAlbumPlayInteractionListener) activity;
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


    public interface OnAlbumPlayInteractionListener {
        public void onVideoSelected(SCVideo v, int videoNoInAlbum);
        public void onVideosLoadFinished();
    }
}
