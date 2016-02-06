package com.crixmod.sailorcast.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.SCChannel;
import com.crixmod.sailorcast.model.SCChannelFilter;
import com.crixmod.sailorcast.model.SCSite;
import com.crixmod.sailorcast.siteapi.SiteApi;
import com.crixmod.sailorcast.uiutils.BaseToolbarActivity;
import com.crixmod.sailorcast.uiutils.SlidingTabLayout;
import com.crixmod.sailorcast.view.fragments.AlbumListFragment;

import java.util.HashMap;

public class AlbumListActivity extends BaseToolbarActivity
implements AlbumFilterDialog.OnAlbumFilterDialogAction
{
    ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private SitePagerAdapter mPagerAdapter;
    private int mChannelID;

    private static final String EXTRA_CHANNEL_ID = "channelID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mChannelID = intent.getIntExtra(EXTRA_CHANNEL_ID,0);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mPagerAdapter = new SitePagerAdapter(getSupportFragmentManager(), this, mChannelID);
        mViewPager.setAdapter(mPagerAdapter);
        mSlidingTabLayout.setViewPager(mViewPager);

        SCChannel channel = new SCChannel(mChannelID);
        setTitle(channel.toString());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_album_list;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_album_list, menu);
        return true;
    }

    private void showAlbumFilterDialog() {
        AlbumListFragment fragment =    mPagerAdapter.getFragment(mViewPager.getCurrentItem());
        if(fragment!= null)
            fragment.showAlbumFilterDialog(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filters) {
            showAlbumFilterDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static void launch(Activity activity, int mChannelID) {
        Intent mpdIntent = new Intent(activity, AlbumListActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra(AlbumListActivity.EXTRA_CHANNEL_ID, mChannelID);
        activity.startActivity(mpdIntent);
    }

    @Override
    public void onAlbumFilterSelected(SCChannelFilter filter) {

        AlbumListFragment fragment =    mPagerAdapter.getFragment(mViewPager.getCurrentItem());
        if(fragment!= null)
            fragment.setChannelFilter(filter);
    }



    private class SitePagerAdapter extends FragmentPagerAdapter {

        private Context mContext;
        private int mChannelID;
        private HashMap<Integer,AlbumListFragment> mPageReferenceMap;

        public SitePagerAdapter(FragmentManager fm, Context context, int mChannelID) {
            super(fm);
            this.mContext = context;
            this.mChannelID = mChannelID;
            mPageReferenceMap = new HashMap<>();
        }

        private int positionToSiteID(int position) {
            //音乐、体育频道里面，Youku的数据处理起来有些麻烦，这里屏蔽一下。
            int mSiteID = position;
            if(mChannelID == SCChannel.MUSIC) {
                if(position == 0)
                    mSiteID = SCSite.LETV;
                if(position == 1)
                    mSiteID = SCSite.IQIYI;
            }
            else
                mSiteID = position;
            return mSiteID;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if(obj instanceof AlbumListFragment) {
                mPageReferenceMap.put(position, (AlbumListFragment) obj);
            }
            return obj;
        }

        @Override
        public AlbumListFragment getItem(int position) {
            AlbumListFragment fragment  = AlbumListFragment.newInstance(positionToSiteID(position),mChannelID);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mPageReferenceMap.remove(position);
        }

        public AlbumListFragment getFragment(int position) {
            return mPageReferenceMap.get(position);
        }

        @Override
        public int getCount() {
            if(mChannelID == SCChannel.MUSIC)
                return (SiteApi.getSupportSiteNumber() - 1);
            else
                return SiteApi.getSupportSiteNumber();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return SiteApi.getSiteName(positionToSiteID(position), mContext);
        }
    }
}
