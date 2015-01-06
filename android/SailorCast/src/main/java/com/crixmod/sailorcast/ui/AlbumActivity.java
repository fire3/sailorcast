package com.crixmod.sailorcast.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCVideo;
import com.crixmod.sailorcast.siteapi.OnGetAlbumDescListener;
import com.crixmod.sailorcast.siteapi.SiteApi;
import com.crixmod.sailorcast.uiutils.BaseToolbarActivity;
import com.crixmod.sailorcast.uiutils.SlidingTabLayout;

import java.util.HashMap;
import java.util.Map;

public class AlbumActivity extends BaseToolbarActivity implements OnGetAlbumDescListener,AlbumPlayControlFragment.OnAlbumPlayInteractionListener {

    private SCAlbum mAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlbum = getIntent().getParcelableExtra("album");
        SiteApi.doGetAlbumDesc(mAlbum,this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_album;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_album, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void launch(Activity activity, SCAlbum album) {
        Intent mpdIntent = new Intent(activity, AlbumActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra("album",album);

        activity.startActivity(mpdIntent);
    }

    private void fillAlbumDescView(SCAlbum album) {
        ImageView albumImage = (ImageView) findViewById(R.id.album_image);
        TextView albumTitle = (TextView) findViewById(R.id.album_title);
        TextView albumDirector = (TextView) findViewById(R.id.album_director);
        TextView albumActor = (TextView) findViewById(R.id.album_main_actor);
        TextView albumDesc = (TextView) findViewById(R.id.album_desc);
        LinearLayout albumTopInfo = (LinearLayout) findViewById(R.id.album_topinfo_container);


        albumTitle.setText(album.getTitle());
        if(album.getDirector() != null && !album.getDirector().isEmpty()) {
            albumDirector.setText(getResources().getString(R.string.director) + album.getDirector());
            albumDirector.setVisibility(View.VISIBLE);
        }
        else
            albumDirector.setVisibility(View.GONE);

        if(album.getMainActor() != null && !album.getMainActor().isEmpty()) {
            albumActor.setText(getResources().getString(R.string.actor) + album.getMainActor());
            albumActor.setVisibility(View.VISIBLE);
        }
        else
            albumActor.setVisibility(View.GONE);

        if(album.getDesc() != null && !album.getDesc().isEmpty())
            albumDesc.setText(album.getDesc());

        albumImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlbumDesc(view);
            }
        });

    }

    private void fillAlbumPlayControl(final SCAlbum album) {
        LinearLayout playContainer = (LinearLayout) findViewById(R.id.album_play_control_container);

        if(album.getVideosCount() > 1) {
            //More than one videos, start viewPager
            ViewPager mViewPager = (ViewPager) findViewById(R.id.album_viewpager);
            SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
            AlbumVideoGridPagerAdapter mAdapter =
                    new AlbumVideoGridPagerAdapter(getSupportFragmentManager(), this, album.getVideosCount(),20);

            mViewPager.setAdapter(mAdapter);
            mSlidingTabLayout.setViewPager(mViewPager);
        } else {
            //Just one video, hide viewPager, get video info.
            playContainer.setVisibility(View.GONE);
            openAlbumDesc(null);
        }
    }

    @Override
    public void onGetAlbumDescSuccess(final SCAlbum album) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fillAlbumDescView(album);
                fillAlbumPlayControl(album);
            }
        });
    }

    @Override
    public void onGetAlbumDescFailed(String failReason) {

    }

    public void closeAlbumDesc(View view) {
        RelativeLayout albumDescContainer = (RelativeLayout) findViewById(R.id.album_desc_container);
        albumDescContainer.setVisibility(View.GONE);
    }

    public void openAlbumDesc(View view) {
        RelativeLayout albumDescContainer = (RelativeLayout) findViewById(R.id.album_desc_container);
        albumDescContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onVideoSelected(SCVideo v, int videoNoInAlbum) {

    }

    @Override
    public void onVideosLoadFinished() {

    }


    public class AlbumVideoGridPagerAdapter extends FragmentPagerAdapter {

        private int mVideoCount = 0;
        private Context mContext;
        private Map<Integer, String> mFragmentTags;
        private FragmentManager mFragmentManager;
        private int mTabPageSize;



        public AlbumVideoGridPagerAdapter(FragmentManager fm, Context mContext, int videoCount, int tabPageSize) {
            super(fm);
            this.mContext = mContext;
            this.mVideoCount = videoCount;
            this.mFragmentManager = fm;
            this.mTabPageSize = tabPageSize;
            mFragmentTags = new HashMap<Integer, String>();
        }

        @Override
        public int getCount() {
            if (mVideoCount > 0) {
                return (mVideoCount % mTabPageSize == 0) ? (mVideoCount / mTabPageSize) : (mVideoCount / mTabPageSize + 1);
            } else
                return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == (getCount() - 1))
                return (position * mTabPageSize + 1) + " - " + (position * mTabPageSize +
                        (((mVideoCount % mTabPageSize) == 0) ? mTabPageSize : (mVideoCount % mTabPageSize))
                );
            else
                return (position * mTabPageSize + 1) + " - " + (position * mTabPageSize + mTabPageSize);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if (obj instanceof Fragment) {
                // record the fragment tag here.
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                mFragmentTags.put(position, tag);
            }
            return obj;
        }

        @Override
        public Fragment getItem(int i) {
            int pageNo = i + 1;
            int pageSize = mTabPageSize;
            AlbumPlayControlFragment newFrag =
                    AlbumPlayControlFragment.newInstance(mAlbum, pageNo, pageSize);
            return newFrag;
        }

        public Fragment getFragment(int position) {
            String tag = mFragmentTags.get(position);
            if (tag == null)
                return null;
            return mFragmentManager.findFragmentByTag(tag);
        }
    }


}
