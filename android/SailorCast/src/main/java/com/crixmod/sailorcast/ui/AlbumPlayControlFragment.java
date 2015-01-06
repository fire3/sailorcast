package com.crixmod.sailorcast.ui;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    private static final String ARG_ORDER = "videoOrder";

    public static final int PLAY_CONTROL_TYPE_BUTTON = 1;
    public static final int PLAY_CONTROL_TYPE_DETAIL = 2;

    private int mPlayControlType = PLAY_CONTROL_TYPE_BUTTON;

    private GridView mGrid;
    private LinearLayout mContainer;
    private int mPageNo;
    private int mPageSize;
    private SCAlbum mAlbum;
    private SCVideos mVideos;
    private OnFragmentInteractionListener mListener;


    public static AlbumPlayControlFragment newInstance(SCAlbum album, int pageNo, int pageSize) {
        AlbumPlayControlFragment fragment = new AlbumPlayControlFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ALBUM, album);
        args.putInt(ARG_PAGE_NO, pageNo);
        args.putInt(ARG_PAGE_SIZE, pageSize);

        fragment.setArguments(args);
        return fragment;
    }

    public AlbumPlayControlFragment() {
        // Required empty public constructor
    }

    public void setPlayControlType(int controlType) {
        if(controlType == PLAY_CONTROL_TYPE_BUTTON || controlType == PLAY_CONTROL_TYPE_DETAIL) {
            mPlayControlType = controlType;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAlbum = getArguments().getParcelable(ARG_ALBUM);
            mPageNo = getArguments().getInt(ARG_PAGE_NO);
            mPageSize = getArguments().getInt(ARG_PAGE_SIZE);
            SiteApi.doGetAlbumVideos(mAlbum,mPageNo,mPageSize,this);
        }
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGrid = (GridView) view.findViewById(R.id.album_play_control_grid);
        mContainer = (LinearLayout)view.findViewById(R.id.container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_album_play_control, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void selectVideoItem(int item) {
        final int numVisibleChildren = mGrid.getChildCount();
        final int firstVisiblePosition = mGrid.getFirstVisiblePosition();

        for ( int i = 0; i < numVisibleChildren; i++ ) {
            int positionOfView = firstVisiblePosition + i;

            if (positionOfView == item) {
                View view = mGrid.getChildAt(i);
                view.requestFocus();
            }
        }
    }

    private void onVideosReady(SCVideos videos) {
        int count = videos.size();
        mVideos = videos;
        AlbumVideoGridAdaptor adapter = new AlbumVideoGridAdaptor(getActivity(),(mPageNo-1)*mPageSize + 1,count,mPlayControlType);
        mGrid.setAdapter(adapter);
        if(mPlayControlType == PLAY_CONTROL_TYPE_DETAIL)
            mGrid.setNumColumns(1);

        mGrid.post(new Runnable() {
            @Override
            public void run() {
                mListener.onVideosLoadFinished();
            }
        });
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
        private int mPlayControlType;

        AlbumVideoGridAdaptor(Context mContext, int mInitialNumber, int mCount, int mPlayControlType) {
            this.mContext = mContext;
            this.mInitialNumber = mInitialNumber;
            this.mCount = mCount;
            this.mPlayControlType = mPlayControlType;
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
            if(mPlayControlType == AlbumPlayControlFragment.PLAY_CONTROL_TYPE_BUTTON)
                return getButtonView(i,view,viewGroup);
            else if(mPlayControlType == AlbumPlayControlFragment.PLAY_CONTROL_TYPE_DETAIL)
                return getDetailView(i,view,viewGroup);
            else
                return view;
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
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


    public interface OnFragmentInteractionListener {
        public void onVideoSelected(SCVideo v, int videoNoInAlbum);
        public void onVideosLoadFinished();
    }
}
