package com.crixmod.sailorcast.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.SailorCast;
import com.crixmod.sailorcast.database.BookmarkDbHelper;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCAlbums;
import com.crixmod.sailorcast.model.SCVideo;
import com.crixmod.sailorcast.model.SCVideos;
import com.crixmod.sailorcast.model.upnp.IRendererCommand;
import com.crixmod.sailorcast.siteapi.OnGetAlbumDescListener;
import com.crixmod.sailorcast.siteapi.OnGetVideoPlayUrlListener;
import com.crixmod.sailorcast.siteapi.OnGetVideosListener;
import com.crixmod.sailorcast.siteapi.SiteApi;
import com.crixmod.sailorcast.uiutils.BaseToolbarActivity;
import com.crixmod.sailorcast.uiutils.SlidingTabLayout;
import com.crixmod.sailorcast.utils.ImageTools;
import com.crixmod.sailorcast.view.fragments.AlbumPlayControlFragment;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

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
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;

    private AlbumVideoGridPagerAdapter mAdapter;
    private int TAB_SIZE_BUTTON = 20;
    private int TAB_SIZE_TITLE = 5;
    private int mTabPageSize = TAB_SIZE_BUTTON;
    private int mInitialVideoNoInAlbum = 0;
    private boolean mIsShowTitle = false;
    private BookmarkDbHelper mBookmarkDb;
    private boolean mIsFav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlbum = getIntent().getParcelableExtra("album");
        SiteApi.doGetAlbumDesc(mAlbum, this);
        findViews();
        setupViewPager();
        setTitle(mAlbum.getTitle());
        mBookmarkDb = new BookmarkDbHelper(this);
        mIsFav =  mBookmarkDb.getAlbumById(mAlbum.getAlbumId(),mAlbum.getSite().getSiteID()) != null;
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
        mViewPager = (ViewPager) findViewById(R.id.album_viewpager);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_album;
    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem fave = menu.findItem(R.id.action_fav_button);
        MenuItem unfave = menu.findItem(R.id.action_unfav_button);

        fave.setVisible(mIsFav);
        unfave.setVisible(!mIsFav);
        return true;
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
        if (id == R.id.action_display_button) {
            if ( mIsShowTitle == true ) {
                mTabPageSize = TAB_SIZE_BUTTON;
                mIsShowTitle = false;
                mAdapter.destroy();
                mViewPager.setAdapter(null);
                mViewPager.post(new Runnable() {
                    @Override
                    public void run() {
                        fillAlbumPlayControl(mAlbum);
                    }
                });
            }
            return true;
        }
        if (id == R.id.action_display_title) {
            if ( mIsShowTitle == false ) {
                mTabPageSize = TAB_SIZE_TITLE;
                mIsShowTitle = true;
                mAdapter.destroy();
                mViewPager.setAdapter(null);
                mViewPager.post(new Runnable() {
                    @Override
                    public void run() {
                        fillAlbumPlayControl(mAlbum);
                    }
                });
            }
            return true;
        }

        if (id == R.id.action_unfav_button) {
            if(mIsFav == false) {
                mBookmarkDb.addAlbum(mAlbum);
                mIsFav = true;
                invalidateOptionsMenu();
            }
        }


        if (id == R.id.action_fav_button) {
            if(mIsFav == true) {
                mBookmarkDb.deleteAlbum(mAlbum.getAlbumId(),mAlbum.getSite().getSiteID());
                mIsFav = false;
                invalidateOptionsMenu();
            }
        }

        if(id == android.R.id.home) {
            finish();
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
                hideAllPlayButton();
            }

            @Override
            public void onPageSelected(int position) {
                hideAllPlayButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
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

        if(album.getVerImageUrl() != null) {
            ImageTools.displayImage(albumImage,album.getVerImageUrl());
        }
    }

    private void fillAlbumPlayControl(final SCAlbum album) {
        LinearLayout playContainer = (LinearLayout) findViewById(R.id.album_play_control_container);

        if(album.getVideosCount() > 1) {
            //More than one videos, start viewPager
            mAdapter =
                    new AlbumVideoGridPagerAdapter(getSupportFragmentManager(), this, album.getVideosCount(),mTabPageSize);
            mViewPager.removeAllViews();
            mViewPager.setAdapter(mAdapter);
            mSlidingTabLayout.setViewPager(mViewPager);
            mAdapter.notifyDataSetChanged();

            int index = mInitialVideoNoInAlbum/mTabPageSize;
            mViewPager.setCurrentItem(index);
        } else {
            //Just one video, hide viewPager, get video info.
            playContainer.setVisibility(View.GONE);
            openAlbumDesc(null);
            hideAlbumCloseButton();
            SiteApi.doGetAlbumVideos(mAlbum,1,1,this);
        }
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
        mCurrentVideo = v;
        mVideoInAlbum = videoNoInAlbum;
        hideAllPlayButton();
        SiteApi.doGetVideoPlayUrl(v, this);
    }

    @Override
    public void onVideosLoadFinished() {

        int index = mInitialVideoNoInAlbum/mTabPageSize;
        int id = mInitialVideoNoInAlbum % mTabPageSize;

        if(index == mViewPager.getCurrentItem()) {
            AlbumPlayControlFragment fragment =  mAdapter.getFragment(index);
            if(fragment != null)
                fragment.selectVideoItem(id);
        }
    }

    @Override
    public void onGetVideosSuccess(SCVideos videos) {
        if(videos.size() == 1) {
            SCVideo v = videos.get(0);
            mCurrentVideo = v;
            mVideoInAlbum = 0;
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

                mPlayNorButton.setTag(R.id.key_video_url,urlNormal);
                mPlayNorButton.setTag(R.id.key_video,v);
                mPlayNorButton.setTag(R.id.key_video_number_in_album,mVideoInAlbum);
                mDlnaNorButton.setTag(R.id.key_video_url,urlNormal);
                mDlnaNorButton.setTag(R.id.key_video,v);
                mDlnaNorButton.setTag(R.id.key_video_number_in_album,mVideoInAlbum);
            }
        });
    }

    @Override
    public void onGetVideoPlayUrlHigh(final SCVideo v, final String urlHigh) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDlnaHighButton.setVisibility(View.VISIBLE);
                mPlayHighButton.setVisibility(View.VISIBLE);

                mPlayHighButton.setTag(R.id.key_video_url,urlHigh);
                mPlayHighButton.setTag(R.id.key_video,v);
                mPlayHighButton.setTag(R.id.key_video_number_in_album,mVideoInAlbum);
                mDlnaHighButton.setTag(R.id.key_video_url,urlHigh);
                mDlnaHighButton.setTag(R.id.key_video,v);
                mDlnaHighButton.setTag(R.id.key_video_number_in_album,mVideoInAlbum);
            }
        });
    }

    @Override
    public void onGetVideoPlayUrlSuper(final SCVideo v, final String urlSuper) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDlnaSuperButton.setVisibility(View.VISIBLE);
                mPlaySuperButton.setVisibility(View.VISIBLE);

                mPlaySuperButton.setTag(R.id.key_video_url, urlSuper);
                mPlaySuperButton.setTag(R.id.key_video, v);
                mPlaySuperButton.setTag(R.id.key_video_number_in_album, mVideoInAlbum);
                mDlnaSuperButton.setTag(R.id.key_video_url,urlSuper);
                mDlnaSuperButton.setTag(R.id.key_video,v);
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
                    AlbumPlayControlFragment.newInstance(mAlbum, pageNo, pageSize, mIsShowTitle);
            return newFrag;
        }

        public AlbumPlayControlFragment getFragment(int position) {
            String tag = mFragmentTags.get(position);
            if (tag == null)
                return null;
            return (AlbumPlayControlFragment) mFragmentManager.findFragmentByTag(tag);
        }

        public void destroy() {
            for (Map.Entry<Integer, String> entry : mFragmentTags.entrySet())
            {
                Fragment f =  mFragmentManager.findFragmentByTag(entry.getValue());
                if(f != null) {
                    FragmentTransaction trans = mFragmentManager.beginTransaction();
                    trans.remove((Fragment) f);
                    trans.commit();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        SailorCast.upnpServiceController.resume(this);
        super.onResume();

    }

	@Override
	public void onPause()
	{
		SailorCast.upnpServiceController.pause();
		SailorCast.upnpServiceController.getServiceListener().getServiceConnexion().onServiceDisconnected(null);
		super.onPause();
	}


    /**
     * 该函数触发播放动作
     * @param button
     */
    public void onPlayButtonClick(View button) {
        String url = (String) button.getTag(R.id.key_video_url);
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
        String url = (String) button.getTag(R.id.key_video_url);
        final SCVideo v = (SCVideo) button.getTag(R.id.key_video);
        if(url != null) {
            //Integer no = (Integer) button.getTag(R.id.key_video_number_in_album);
            //BaiduPlayerActivity.launch(this,url);
            Log.d("fire3","play " + url);
        }
        else
            Toast.makeText(this, "请先选择视频!", Toast.LENGTH_SHORT).show();

        FragmentManager fm = getSupportFragmentManager();
        RendererDialog dialog = new RendererDialog();
        dialog.setCallback(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                launchRenderer(v);
                return null;
            }
        });
        dialog.show(fm,"Render");
    }


	private void launchRenderer(SCVideo video)
	{
		IRendererCommand rendererCommand = SailorCast.factory.createRendererCommand(SailorCast.factory.createRendererState());
		rendererCommand.lauchSCVideoHigh(video);
        //RenderActivity.launch(this,mAlbum,video);
	}

}
