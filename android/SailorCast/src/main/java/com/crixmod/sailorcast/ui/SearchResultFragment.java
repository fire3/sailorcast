package com.crixmod.sailorcast.ui;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCAlbums;
import com.crixmod.sailorcast.siteapi.OnSearchRequestListener;
import com.crixmod.sailorcast.siteapi.SiteApi;

public class SearchResultFragment extends ListFragment
implements OnSearchRequestListener
{
    private static final String ARG_KEYWORD = "key";
    private static final String ARG_SITEID = "siteID";

    private String mKeyword;
    private int    mSiteID;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param keyword Parameter 1.
     * @return A new instance of fragment SearchResultFragment.
     */
    public static SearchResultFragment newInstance(String keyword, int siteID) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_KEYWORD, keyword);
        args.putInt(ARG_SITEID, siteID);
        fragment.setArguments(args);
        return fragment;
    }

    public SearchResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mKeyword = getArguments().getString(ARG_KEYWORD);
            mSiteID = getArguments().getInt(ARG_SITEID);
            SiteApi.doSearch(mSiteID,mKeyword,this);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEmptyText("没有搜索结果");
        setListShown(true);
    }

    @Override
    public void onSearchSuccess(SCAlbums albums) {
        albums.debugLog();
        final SearchResultAdapter adapter = new SearchResultAdapter(getActivity(),albums);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setListAdapter(adapter);
                setListShown(true);
            }
        });
    }

    @Override
    public void onSearchFailed(String failReason) {

    }



    class SearchResultAdapter extends BaseAdapter {

        private Context mContext;
        private SCAlbums mAlbums;

        private class ViewHolder {
            LinearLayout resultContainer;
            ImageView videoImage;
            TextView videoTitle;
            TextView videoTip;
        }

        SearchResultAdapter(Context mContext, SCAlbums mResults) {
            this.mContext = mContext;
            this.mAlbums = mResults;
        }

        @Override
        public int getCount() {
            return mAlbums.size();
        }

        @Override
        public SCAlbum getItem(int i) {
            return mAlbums.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            SCAlbum album = getItem(i);
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = getOneColumnVideoRowView(viewGroup, viewHolder);
            }
            else {
                viewHolder = (ViewHolder) view.getTag();
            }
            setupViewHolder(view,i,viewHolder,album);

            return view;
        }

        private void setupViewHolder(View view, int i, ViewHolder viewHolder, final SCAlbum album) {
            viewHolder.videoTitle.setText(album.getTitle());
            viewHolder.videoTip.setText(album.getTip());

            viewHolder.resultContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     AlbumActivity.launch((Activity)mContext,album);
                }
            });
        }

        private View getOneColumnVideoRowView(ViewGroup viewGroup, ViewHolder viewHolder) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            View itemView = inflater.inflate(R.layout.item_listview_search_result,viewGroup,false);
            viewHolder.videoImage = (ImageView) itemView.findViewById(R.id.video_image);
            viewHolder.videoTitle = (TextView) itemView.findViewById(R.id.video_title);
            viewHolder.videoTip = (TextView) itemView.findViewById(R.id.video_tip);
            viewHolder.resultContainer = (LinearLayout)itemView.findViewById(R.id.search_result);


            final ImageView imageView = viewHolder.videoImage;
            //AppUtils.fixVerPosterRatio(imageView);
            itemView.setTag(viewHolder);
            return itemView;
        }
    }

}
