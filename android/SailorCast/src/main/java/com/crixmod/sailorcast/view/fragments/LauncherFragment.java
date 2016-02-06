package com.crixmod.sailorcast.view.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.SCChannel;
import com.crixmod.sailorcast.view.AlbumListActivity;
import com.crixmod.sailorcast.view.BookmarkActivity;
import com.crixmod.sailorcast.view.HistoryActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LauncherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LauncherFragment extends Fragment {

    private GridView mGrid;
    private CompassAdapter mAdapter;

    public static LauncherFragment newInstance() {
        LauncherFragment fragment = new LauncherFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public LauncherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new CompassAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_launcher, container, false);
        mGrid = (GridView) view.findViewById(R.id.grid);
        mGrid.setAdapter(mAdapter);

        return view;
    }


    private class CompassAdapter  extends BaseAdapter {
        private Context mContext;

        private CompassAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return SCChannel.getChannelCount();
        }

        @Override
        public SCChannel getItem(int position) {
            return new SCChannel(position + 1);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final SCChannel channel = getItem(position);
            ImageView imageView;
            TextView textView;
            if(convertView == null) {
                convertView =((Activity) mContext).getLayoutInflater().inflate(R.layout.item_gridview_compass,parent,false);
                imageView = (ImageView) convertView.findViewById(R.id.icon_image);
                textView = (TextView) convertView.findViewById(R.id.icon_text);

            } else {
                imageView = (ImageView) convertView.findViewById(R.id.icon_image);
                textView = (TextView) convertView.findViewById(R.id.icon_text);
            }
            switch (channel.getChannelID()) {
                case    SCChannel.SHOW:
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_show));
                    break;
                case    SCChannel.DOCUMENTARY:
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_documentary));
                    break;
                case    SCChannel.COMIC:
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_comic));
                    break;
                case    SCChannel.MOVIE:
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_movie));
                    break;
                case    SCChannel.MUSIC:
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_music));
                    break;
                case    SCChannel.VARIETY:
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_variety));
                    break;
                case    SCChannel.LOCAL_BOOKMARK:
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark));
                    break;
                case    SCChannel.LOCAL_HISTORY:
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_history));
                    break;
            }

            textView.setText(channel.toString());


            if( !channel.isLocalChannel()) {
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlbumListActivity.launch(getActivity(), channel.getChannelID());
                    }
                });
            }

            if(channel.getChannelID() == SCChannel.LOCAL_BOOKMARK) {
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BookmarkActivity.launch(getActivity());
                    }
                });
            }

            if(channel.getChannelID() == SCChannel.LOCAL_HISTORY) {
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HistoryActivity.launch(getActivity());
                    }
                });
            }

            return convertView;
        }
    }
    
}
