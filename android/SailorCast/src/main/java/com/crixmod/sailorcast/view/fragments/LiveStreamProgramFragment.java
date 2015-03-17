package com.crixmod.sailorcast.view.fragments;


import android.app.Activity;
import android.content.Context;
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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.SCFailLog;
import com.crixmod.sailorcast.model.SCLiveStream;
import com.crixmod.sailorcast.model.SCLiveStreamProgram;
import com.crixmod.sailorcast.model.SCLiveStreamPrograms;
import com.crixmod.sailorcast.model.SCWeekDay;
import com.crixmod.sailorcast.siteapi.LetvApi;
import com.crixmod.sailorcast.siteapi.OnGetLiveStreamProgramsListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LiveStreamProgramFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LiveStreamProgramFragment extends Fragment implements OnGetLiveStreamProgramsListener {
    private static final String ARG_WEEKDAY_NAME = "weekDayName";
    private static final String ARG_WEEKDAY_ID = "weekDayID";
    private static final String ARG_STREAM = "stream";

    private SCWeekDay mWeekDay;
    private SCLiveStream mStream;
    private ProgramsAdapter mAdapter;
    private Handler mHandler;
    private ListView mListView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LiveStreamProgramFragment.
     */
    public static LiveStreamProgramFragment newInstance(SCLiveStream stream, SCWeekDay SCWeekDay) {
        LiveStreamProgramFragment fragment = new LiveStreamProgramFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STREAM, stream.toJson());
        args.putString(ARG_WEEKDAY_NAME, SCWeekDay.weekDayName);
        args.putString(ARG_WEEKDAY_ID, SCWeekDay.weekDayId);
        fragment.setArguments(args);
        return fragment;
    }

    public LiveStreamProgramFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mWeekDayName = getArguments().getString(ARG_WEEKDAY_NAME);
            String mWeekDayID = getArguments().getString(ARG_WEEKDAY_ID);
            String stream = getArguments().getString(ARG_STREAM);
            mWeekDay = new SCWeekDay(mWeekDayName,mWeekDayID);
            mStream = SCLiveStream.fromJson(stream);

            new LetvApi().doGetLiveStreamProgramsByWeekDay(mStream,mWeekDay,this);

            mAdapter = new ProgramsAdapter(getActivity());

            mHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if(msg.what == 0) {
                        SCLiveStreamPrograms programs = (SCLiveStreamPrograms) msg.obj;
                        mAdapter.addPrograms(programs);
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
        return inflater.inflate(R.layout.fragment_live_stream_program, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.live_stream_programs_listview);

        if(mListView != null) {
            if(mAdapter != null) {
                mListView.setAdapter(mAdapter);
            }
        }
    }

    @Override
    public void onGetLiveStreamProgramsSuccess(SCLiveStream stream, SCLiveStreamPrograms programs) {
        if(mHandler != null) {
            mHandler.obtainMessage(0,programs).sendToTarget();
        }
    }

    @Override
    public void onGetLiveStreamProgramsFailed(SCFailLog reason) {

    }



    private class ProgramsAdapter extends BaseAdapter {
        SCLiveStreamPrograms mPrograms = new SCLiveStreamPrograms();
        Context mContext;

        public void addProgram(SCLiveStreamProgram program) {
            mPrograms.add(program);
        }

        public void addPrograms(SCLiveStreamPrograms programs) {
            for(SCLiveStreamProgram program : programs) {
                mPrograms.add(program);
            }
        }


        private class ViewHolder {
            RelativeLayout streamContainer;
            TextView programTitle;
        }



        public ProgramsAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mPrograms.size();
        }

        @Override
        public SCLiveStreamProgram getItem(int i) {
            return mPrograms.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            SCLiveStreamProgram program = getItem(i);
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = getItemView(viewGroup, viewHolder);
            }
            else {
                viewHolder = (ViewHolder) view.getTag();
            }
            setupViewHolder(view,i,viewHolder,program);
            return view;

        }

        private void setupViewHolder(View view, int i, ViewHolder viewHolder, final SCLiveStreamProgram program) {
            viewHolder.programTitle.setText(" " + program.getPlayTime() + "   " + program.getTitle());

            if(i == 0 && mWeekDay.weekDayName.equals("今天"))
                viewHolder.programTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.xiaobofang_normal,0,0,0);
            else
                viewHolder.programTitle.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        }

        private View getItemView(ViewGroup viewGroup, ViewHolder viewHolder) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            View itemView;
            itemView = inflater.inflate(R.layout.item_listview_livestream_program,viewGroup,false);
            viewHolder.streamContainer = (RelativeLayout) itemView;
            viewHolder.programTitle = (TextView) itemView.findViewById(R.id.itemTitle);

            itemView.setTag(viewHolder);
            return itemView;
        }
    }
}
