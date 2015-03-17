package com.crixmod.sailorcast.view.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.SCFailLog;
import com.crixmod.sailorcast.model.SCLiveStream;
import com.crixmod.sailorcast.model.SCLiveStreamType;
import com.crixmod.sailorcast.model.SCLiveStreams;
import com.crixmod.sailorcast.siteapi.LetvApi;
import com.crixmod.sailorcast.siteapi.OnGetLiveStreamsDescListener;
import com.crixmod.sailorcast.siteapi.OnGetLiveStreamsListener;
import com.crixmod.sailorcast.uiutils.paginglistview.PagingListView;
import com.crixmod.sailorcast.uiutils.pagingridview.PagingGridView;
import com.crixmod.sailorcast.view.adapters.LiveStreamListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LiveStreamListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LiveStreamListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LiveStreamListFragment extends Fragment implements OnGetLiveStreamsListener, OnGetLiveStreamsDescListener {
    private static final String ARG_LIVE_STREAM_TYPE = "liveStreamType";

    private int mLiveStreamTypeID;

    private OnFragmentInteractionListener mListener;
    private LiveStreamListAdapter mAdapter;
    private Handler mHandler;
    private int mPageNo = 0;
    private int mPageSize = 8;
    private SCLiveStreams mStreams;

    private PagingListView mListView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LiveStreamListFragment.
     */
    public static LiveStreamListFragment newInstance(int param1) {
        LiveStreamListFragment fragment = new LiveStreamListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIVE_STREAM_TYPE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public LiveStreamListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLiveStreamTypeID = getArguments().getInt(ARG_LIVE_STREAM_TYPE);
            mAdapter = new LiveStreamListAdapter(getActivity());
            new LetvApi().doGetLiveStreamsByType(this, new SCLiveStreamType(mLiveStreamTypeID));

            /*
            mHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what == 0) {
                        SCLiveStreams streams = (SCLiveStreams) msg.obj;
                        if(mStreams.size() > mAdapter.getCount())
                            mListView.setHasMoreItems(true);
                        mAdapter.addLiveStreams(streams);

                        mListView.setIsLoading(false);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            };
            */
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_live_stream_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (PagingListView) view.findViewById(R.id.result_grid);
        mListView.setAdapter(mAdapter);
        mListView.setHasMoreItems(true);
        Log.d("fire3","onViewCreated");
        mListView.setPagingableListener(new PagingListView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                Log.d("fire3", "loading More Items");
                loadMorItems();
            }
        });
    }

    private synchronized  void loadMorItems() {
        Log.d("fire3","loadMoreItems: " + mPageNo);
        if(mStreams == null)
            return;
        int start = mPageNo * mPageSize;
        int end = start + mPageSize;
        if(start >= mStreams.size()) {
            Log.d("fire3","no more items");
            mListView.setHasMoreItems(false);
            return;
        }
        if(end >= mStreams.size()) {
            end = mStreams.size() - 1;
            mListView.setHasMoreItems(false);
            Log.d("fire3","no more items");
        }
        SCLiveStreams streams = new SCLiveStreams();

        for (int i = start; i < end; i++) {
            SCLiveStream stream = mStreams.get(i);
            streams.add(stream);
        }
        new LetvApi().doGetLiveStreamsDesc(streams,this);
        mPageNo ++;
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

    @Override
    public void onGetLiveStreamsSuccess(final SCLiveStreams streams) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStreams = streams;
                Log.d("fire3","trigger streams loadMoreItems");
                loadMorItems();
            }
        });
    }

    @Override
    public void onGetLiveStreamsFailed(SCFailLog failReason) {

    }

    @Override
    public void onGetLiveStreamsDescSuccess(final SCLiveStreams streams) {
//        if(mHandler != null) {
//            mHandler.obtainMessage(0,streams).sendToTarget();
//        }


        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                     if(mStreams.size() > mAdapter.getCount())
//                            mListView.setHasMoreItems(true);
                        mAdapter.addLiveStreams(streams);
                        mListView.setIsLoading(false);
                        mAdapter.notifyDataSetChanged();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetLiveStreamsDescFailed(SCFailLog failReason) {

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
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
