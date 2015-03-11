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
import android.widget.ListView;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.SCFailLog;
import com.crixmod.sailorcast.model.SCLiveStream;
import com.crixmod.sailorcast.model.SCLiveStreamType;
import com.crixmod.sailorcast.model.SCLiveStreams;
import com.crixmod.sailorcast.siteapi.LetvApi;
import com.crixmod.sailorcast.siteapi.OnGetLiveStreamDescListener;
import com.crixmod.sailorcast.siteapi.OnGetLiveStreamsListener;
import com.crixmod.sailorcast.view.adapters.LiveStreamListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LiveStreamListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LiveStreamListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LiveStreamListFragment extends Fragment implements OnGetLiveStreamsListener, OnGetLiveStreamDescListener {
    private static final String ARG_LIVE_STREAM_TYPE = "liveStreamType";

    private int mLiveStreamTypeID;

    private OnFragmentInteractionListener mListener;
    private LiveStreamListAdapter mAdapter;
    private Handler mHandler;
    private ListView mListView;

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
            new LetvApi().doGetLiveStreamsByType(this, new SCLiveStreamType(mLiveStreamTypeID));
            mAdapter = new LiveStreamListAdapter(getActivity());

            mHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what == 0) {
                        SCLiveStream stream = (SCLiveStream) msg.obj;
                        Log.d("fire3", "Stream recv: " + stream.toString());
                        mAdapter.addLiveStream(stream);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            };
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
        mListView = (ListView) view.findViewById(R.id.live_stream_listview);
        mListView.setAdapter(mAdapter);
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
    public void onGetLiveStreamsSuccess(SCLiveStreams streams) {
        for(SCLiveStream stream : streams) {
            new LetvApi().doGetLiveStreamDesc(stream,this);
        }
    }

    @Override
    public void onGetLiveStreamsFailed(SCFailLog failReason) {

    }

    @Override
    public void onGetLiveStreamDescSuccess(SCLiveStream stream) {
        if(mHandler != null) {
            mHandler.obtainMessage(0,stream).sendToTarget();
        }
    }

    @Override
    public void onGetLiveStreamDescFailed(SCFailLog failReason) {

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
