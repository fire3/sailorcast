package com.crixmod.sailorcast.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.SCFailLog;
import com.crixmod.sailorcast.model.SCLiveStream;
import com.crixmod.sailorcast.model.SCLiveStreamType;
import com.crixmod.sailorcast.model.SCLiveStreams;
import com.crixmod.sailorcast.siteapi.LetvApi;
import com.crixmod.sailorcast.siteapi.OnGetLiveStreamDescListener;
import com.crixmod.sailorcast.siteapi.OnGetLiveStreamPlayUrlListener;
import com.crixmod.sailorcast.siteapi.OnGetLiveStreamsListener;
import com.crixmod.sailorcast.uiutils.BaseToolbarActivity;
import com.crixmod.sailorcast.uiutils.SlidingTabLayout;
import com.crixmod.sailorcast.view.fragments.AlbumListFragment;
import com.crixmod.sailorcast.view.fragments.LiveStreamListFragment;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

public class LiveStreamActivity extends BaseToolbarActivity
{

    ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private SitePagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mPagerAdapter = new SitePagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mPagerAdapter);
        mSlidingTabLayout.setViewPager(mViewPager);

        setTitle("直播");
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_live_stream;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_live_stream, menu);
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



    public static void launch(Activity activity) {
        Intent mpdIntent = new Intent(activity, LiveStreamActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(mpdIntent);
    }


        private class SitePagerAdapter extends FragmentPagerAdapter {

        private Context mContext;
        private int mChannelID;
        private HashMap<Integer,LiveStreamListFragment> mPageReferenceMap;

        public SitePagerAdapter(FragmentManager fm, Context context, int mChannelID) {
            super(fm);
            this.mContext = context;
            this.mChannelID = mChannelID;
            mPageReferenceMap = new HashMap<>();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if(obj instanceof AlbumListFragment) {
                mPageReferenceMap.put(position, (LiveStreamListFragment) obj);
            }
            return obj;
        }

        @Override
        public LiveStreamListFragment getItem(int position) {
            LiveStreamListFragment fragment  = LiveStreamListFragment.newInstance(position);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mPageReferenceMap.remove(position);
        }

        public LiveStreamListFragment getFragment(int position) {
            return mPageReferenceMap.get(position);
        }

        @Override
        public int getCount() {
            return SCLiveStreamType.getTypeCount();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //return SiteApi.getSiteName(positionToSiteID(position), mContext);
            return "";
        }
    }
}
