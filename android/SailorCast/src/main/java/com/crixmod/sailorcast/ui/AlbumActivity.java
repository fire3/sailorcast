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
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCVideo;
import com.crixmod.sailorcast.model.SCVideos;
import com.crixmod.sailorcast.siteapi.OnGetAlbumDescListener;
import com.crixmod.sailorcast.siteapi.OnGetVideoPlayUrlListener;
import com.crixmod.sailorcast.siteapi.OnGetVideosListener;
import com.crixmod.sailorcast.siteapi.SiteApi;
import com.crixmod.sailorcast.uiutils.BaseToolbarActivity;
import com.crixmod.sailorcast.uiutils.SlidingTabLayout;

import java.util.HashMap;
import java.util.Map;

public class AlbumActivity extends BaseToolbarActivity
        implements OnGetAlbumDescListener,
        OnGetVideosListener, OnGetVideoPlayUrlListener,
        AlbumPlayControlFragment.OnAlbumPlayInteractionListener {

    private SCAlbum mAlbum;
    private SCVideo mCurrentVideo;
    private int mVideoInAlbum; /* start from 0 */


    private Button mPlaySuperButton;
    private Button mPlayNorButton;
    private Button mPlayHighButton;
    private Button mDlnaSuperButton;
    private Button mDlnaHighButton;
    private Button mDlnaNorButton;
    private Switch mToggleOrder;
    private Switch mToggleTitle;
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;

    private AlbumVideoGridPagerAdapter mAdapter;

    private int mTabPageSize = 20;
    private int mInitialVideoNoInAlbum = 0;
    private ViewPager.OnPageChangeListener mPageChangeListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlbum = getIntent().getParcelableExtra("album");
        SiteApi.doGetAlbumDesc(mAlbum,this);
        findViews();
        setupViewPager();
    }

    @Override
    protected void onDestroy() {
        SiteApi.cancel();
        super.onDestroy();
    }

    private void findViews() {
        mPlayHighButton = (Button) findViewById(R.id.btn_phone_high);
        mPlayNorButton = (Button) findViewById(R.id.btn_phone_normal);
        mPlaySuperButton = (Button) findViewById(R.id.btn_phone_super);
        mDlnaHighButton = (Button) findViewById(R.id.btn_dlna_high);
        mDlnaNorButton = (Button) findViewById(R.id.btn_dlna_normal);
        mDlnaSuperButton = (Button) findViewById(R.id.btn_dlna_super);
        mToggleOrder = (Switch) findViewById(R.id.toggle_order);
        mToggleTitle = (Switch) findViewById(R.id.toggle_title);
        mViewPager = (ViewPager) findViewById(R.id.album_viewpager);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

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

    private void setupViewPager() {
       mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("fire3", "onPageScrolled");
                hideAllPlayButton();
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("fire3", "onPageSelected");
                hideAllPlayButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("fire3", "onPageScrollStateChanged");
                hideAllPlayButton();
            }
        });
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
            mAdapter =
                    new AlbumVideoGridPagerAdapter(getSupportFragmentManager(), this, album.getVideosCount(),mTabPageSize);

            mViewPager.setAdapter(mAdapter);
            mSlidingTabLayout.setViewPager(mViewPager);
            showToggleButton();

            int index = mInitialVideoNoInAlbum/mTabPageSize;
            mViewPager.setCurrentItem(index);
        } else {
            //Just one video, hide viewPager, get video info.
            playContainer.setVisibility(View.GONE);
            openAlbumDesc(null);
            hideAlbumCloseButton();
            SiteApi.doGetAlbumVideos(mAlbum,1,1,this);
            hideToggleButton();
        }
    }

    private void showToggleButton() {
        mToggleTitle.setVisibility(View.VISIBLE);
        mToggleOrder.setVisibility(View.VISIBLE);
    }

    private void hideToggleButton() {
        mToggleTitle.setVisibility(View.GONE);
        mToggleOrder.setVisibility(View.GONE);
    }


    private void hideAlbumCloseButton() {
        Button b = (Button) findViewById(R.id.album_desc_close);
        b.setVisibility(View.GONE);
    }

    private void showAlbumCloseButton() {
        Button b = (Button) findViewById(R.id.album_desc_close);
        b.setVisibility(View.VISIBLE);
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

    private void hideAllPlayButton() {
        mDlnaHighButton.setVisibility(View.GONE);
        mDlnaNorButton.setVisibility(View.GONE);
        mDlnaSuperButton.setVisibility(View.GONE);
        mPlayHighButton.setVisibility(View.GONE);
        mPlayNorButton.setVisibility(View.GONE);
        mPlaySuperButton.setVisibility(View.GONE);
    }

    @Override
    public void onVideoSelected(SCVideo v, int videoNoInAlbum) {
        Log.d("fire3","selected: " + v.getVideoTitle() + " count: " + videoNoInAlbum);
        mCurrentVideo = v;
        mVideoInAlbum = videoNoInAlbum;
        hideAllPlayButton();
        SiteApi.doGetVideoPlayUrl(v, this);
    }

    @Override
    public void onVideosLoadFinished() {

        int index = mInitialVideoNoInAlbum/mTabPageSize;
        int id = mInitialVideoNoInAlbum % mTabPageSize;
        Log.d("fire3",String.format("onVideosLoadFinished() index: %d id:%d current: %d",index,id,mViewPager.getCurrentItem()));

        if(index == mViewPager.getCurrentItem()) {
            AlbumPlayControlFragment fragment = (AlbumPlayControlFragment) mAdapter.getFragment(index);
            fragment.selectVideoItem(id);
        }
    }

    @Override
    public void onGetVideosSuccess(SCVideos videos) {
        if(videos.size() == 1) {
            SCVideo v = videos.get(0);
            mCurrentVideo = v;
            mVideoInAlbum = 0;
            Log.d("fire3","only one selected: " + mCurrentVideo.getVideoTitle() + " count: " + 0);
            hideAllPlayButton();
            SiteApi.doGetVideoPlayUrl(v,this);
        }
    }

    @Override
    public void onGetVideosFailed(String failReason) {

    }

    @Override
    public void onGetVideoPlayUrlNormal(final SCVideo v, final String urlNormal) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDlnaNorButton.setVisibility(View.VISIBLE);
                mPlayNorButton.setVisibility(View.VISIBLE);

                mPlayNorButton.setTag(R.id.key_video,urlNormal);
                mPlayNorButton.setTag(R.id.key_video_number_in_album,mVideoInAlbum);
                mDlnaNorButton.setTag(R.id.key_video,urlNormal);
                mDlnaNorButton.setTag(R.id.key_video_number_in_album,mVideoInAlbum);
            }
        });
    }

    @Override
    public void onGetVideoPlayUrlHigh(SCVideo v, final String urlHigh) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDlnaHighButton.setVisibility(View.VISIBLE);
                mPlayHighButton.setVisibility(View.VISIBLE);

                mPlayHighButton.setTag(R.id.key_video,urlHigh);
                mPlayHighButton.setTag(R.id.key_video_number_in_album,mVideoInAlbum);
                mDlnaHighButton.setTag(R.id.key_video,urlHigh);
                mDlnaHighButton.setTag(R.id.key_video_number_in_album,mVideoInAlbum);
            }
        });
    }

    @Override
    public void onGetVideoPlayUrlSuper(SCVideo v, final String urlSuper) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDlnaSuperButton.setVisibility(View.VISIBLE);
                mPlaySuperButton.setVisibility(View.VISIBLE);

                mPlaySuperButton.setTag(R.id.key_video, urlSuper);
                mPlaySuperButton.setTag(R.id.key_video_number_in_album, mVideoInAlbum);
                mDlnaSuperButton.setTag(R.id.key_video,urlSuper);
                mDlnaSuperButton.setTag(R.id.key_video_number_in_album, mVideoInAlbum);
            }
        });
    }

    @Override
    public void onGetVideoPlayUrlFailed(String reason) {

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



    /**
     * 该函数触发播放动作
     * @param button
     */
    public void onPlayButtonClick(View button) {
        String url = (String) button.getTag(R.id.key_video);
        if(url != null) {
            //Integer no = (Integer) button.getTag(R.id.key_video_number_in_album);
            //BaiduPlayerActivity.launch(this,url);
            Log.d("fire3","play " + url);
        }
        else
            Toast.makeText(this, "请先选择视频!", Toast.LENGTH_SHORT).show();
    }

    /**
     * 该函数触发播放动作
     * @param button
     */
    public void onDlnaButtonClick(View button) {
        String url = (String) button.getTag(R.id.key_video);
        if(url != null) {
            //Integer no = (Integer) button.getTag(R.id.key_video_number_in_album);
            //BaiduPlayerActivity.launch(this,url);
            Log.d("fire3","play " + url);
        }
        else
            Toast.makeText(this, "请先选择视频!", Toast.LENGTH_SHORT).show();
    }

}
