package com.crixmod.sailorcast.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.siteapi.SiteApi;
import com.crixmod.sailorcast.uiutils.BaseToolbarActivity;
import com.crixmod.sailorcast.uiutils.SlidingTabLayout;

public class SearchResultsActivity extends BaseToolbarActivity {

    private static final String EXTRA_KEYWORD = "SearchResultsActivity:keyword";

    private String mKeyword;
    ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private SitePagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mKeyword = intent.getStringExtra(EXTRA_KEYWORD);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mPagerAdapter = new SitePagerAdapter(getSupportFragmentManager(), this, mKeyword);
        mViewPager.setAdapter(mPagerAdapter);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_search_results;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_results, menu);
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

    public static void launch(Activity activity, String keyword) {
        Intent mpdIntent = new Intent(activity, SearchResultsActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra(SearchResultsActivity.EXTRA_KEYWORD, keyword);

        activity.startActivity(mpdIntent);

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SitePagerAdapter extends FragmentPagerAdapter {

        private Context mContext;
        private String mKeyword;

        public SitePagerAdapter(FragmentManager fm, Context context, String keyword) {
            super(fm);
            mContext = context;
            mKeyword = keyword;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            return SearchResultFragment.newInstance(mKeyword, position);
        }

        @Override
        public int getCount() {
            return SiteApi.getSupportSiteNumber();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return SiteApi.getSiteName(position, mContext);
        }
    }
}

