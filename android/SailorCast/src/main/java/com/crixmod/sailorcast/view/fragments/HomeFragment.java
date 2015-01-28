package com.crixmod.sailorcast.view.fragments;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCAlbums;
import com.crixmod.sailorcast.model.SCBanner;
import com.crixmod.sailorcast.model.SCBanners;
import com.crixmod.sailorcast.siteapi.OnGetBannersListener;
import com.crixmod.sailorcast.siteapi.SohuApi;
import com.crixmod.sailorcast.utils.ImageTools;
import com.crixmod.sailorcast.view.AlbumDetailActivity;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment  implements OnGetBannersListener, BaseSliderView.OnSliderClickListener{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private ListView mListView;
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
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.list);
    }

    @Override
    public void onGetBannersSuccess(final SCBanners banners) {
        /*
        for(SCBanner b: banners) {
           Log.d("fire3", String.format("name %s type %d album %s", b.getTitle(), b.getType(), b.getAlbums().get(0).toString()));
        }
        */
        Activity activity = getActivity();
        final BaseSliderView.OnSliderClickListener listener = this;
        if(activity != null) {
            final HomeAdapter adapter = new HomeAdapter(getActivity(),banners,listener);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  mListView.setAdapter(adapter);
                }
            });
        }

    }

    @Override
    public void onGetBannersFailed(String failReason) {

    }

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {
        String albumJson = (String) baseSliderView.getBundle().get("extra");
        SCAlbum album = SCAlbum.fromJson(albumJson);
        AlbumDetailActivity.launch(getActivity(),album);
//        Toast.makeText(getActivity(),baseSliderView.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }


    class HomeAdapter extends BaseAdapter {
        private Context mContext;
        private SCBanners mBanners;
        private BaseSliderView.OnSliderClickListener listener;

        HomeAdapter(Context mContext, SCBanners mBanners, BaseSliderView.OnSliderClickListener listener) {
            this.mContext = mContext;
            this.mBanners = mBanners;
            this.listener = listener;
        }

        @Override
        public int getCount() {
            return mBanners.size();
        }

        @Override
        public SCBanner getItem(int i) {
            return mBanners.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if(position == 0)
                return 0;
            else
                return 1;
        }

        private View getTopBannerView(int i, View view, ViewGroup viewGroup) {
            if(view == null)
                view = ((Activity)mContext).getLayoutInflater().inflate(R.layout.item_listview_top_banner,viewGroup,false);
            SliderLayout mSlider = (SliderLayout)view.findViewById(R.id.slider);
            mSlider.removeAllSliders();
            SCBanner banner = getItem(i);
            SCAlbums albums = banner.getAlbums();

            for(SCAlbum album : albums) {
                TextSliderView textSliderView = new TextSliderView(mContext);
                // initialize a SliderLayout
                textSliderView
                        .description(album.getTitle())
                        .image(album.getHorImageUrl())
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(listener);

                //add your extra information
                textSliderView.getBundle().putString("extra",album.toJson());
                mSlider.addSlider(textSliderView);
            }
            mSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
            mSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            //mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
            mSlider.setDuration(4000);

            return view;
        }
        class ViewHolder {
            TextView title;
            ArrayList<ImageView> albumImageViews = new ArrayList<>();
            ArrayList<TextView> albumTitles = new ArrayList<>();
            ArrayList<TextView> albumTips = new ArrayList<>();
            ArrayList<LinearLayout> albumContainers = new ArrayList<>();
        }
        private void addRowItem(TableRow row,ViewGroup viewGroup,ViewHolder viewHolder) {
            View view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.item_listview_album_table_item, row, false);
            ImageView albumImage = (ImageView) view.findViewById(R.id.video_image);
            TextView albumTitle = (TextView) view.findViewById(R.id.video_title);
            TextView albumTip = (TextView) view.findViewById(R.id.video_tip);
            LinearLayout resultContainer = (LinearLayout)view.findViewById(R.id.search_result);
            Point point = ImageTools.getGridHorPosterSize(mContext, 2);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(point.x,point.y);
            albumImage.setLayoutParams(params);
            viewHolder.albumImageViews.add(albumImage);
            viewHolder.albumTitles.add(albumTitle);
            viewHolder.albumTips.add(albumTip);
            viewHolder.albumContainers.add(resultContainer);
            row.addView(view);
        }

        private void setupViewHolder(SCBanner banner, ViewHolder viewHolder) {
            int count = viewHolder.albumContainers.size();
            for (int i = 0; i < count; i++) {
                SCAlbum album = banner.getAlbums().get(i);
                ImageView imageView  = viewHolder.albumImageViews.get(i);
                Point point = ImageTools.getGridHorPosterSize(mContext, 2);
                ImageTools.displayImage(imageView,album.getHorImageUrl(),point.x,point.y);

                viewHolder.albumTitles.get(i).setText(album.getTitle());
                viewHolder.albumTips.get(i).setText(album.getTip());
                viewHolder.albumContainers.get(i).setTag(album);

                viewHolder.albumContainers.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SCAlbum album = (SCAlbum) view.getTag();
                        AlbumDetailActivity.launch((Activity)mContext,album);
                    }
                });
            }
            viewHolder.title.setText(banner.getTitle());
        }

        private View getNormalRow(int i, View view, ViewGroup viewGroup) {
            SCBanner banner = getItem(i);
            if(view == null) {
                view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.item_listview_album_table, viewGroup, false);

                ViewHolder viewHolder = new ViewHolder();

                TextView mTitle = (TextView) view.findViewById(R.id.home_list_table_title);
                TableLayout mTable = (TableLayout) view.findViewById(R.id.home_list_table_container);

                viewHolder.title = mTitle;

                for (int j = 0; j < 2; j++) {
                    TableRow row = new TableRow(mContext);
                    TableLayout.LayoutParams lp = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 5, 0, 5);
                    row.setLayoutParams(lp);
                    addRowItem(row, viewGroup, viewHolder);
                    addRowItem(row,viewGroup,viewHolder);
                    mTable.addView(row);
                }

                view.setTag(viewHolder);
                setupViewHolder(banner,viewHolder);
            } else {
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                setupViewHolder(banner,viewHolder);
            }

            return view;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(i == 0) {
                return getTopBannerView(i,view,viewGroup);
            } else {
                return getNormalRow(i,view,viewGroup);
            }
        }
    }
}
