package com.crixmod.sailorcast.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.database.BookmarkDbHelper;
import com.crixmod.sailorcast.database.HistoryDbHelper;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCVideo;
import com.crixmod.sailorcast.siteapi.OnGetAlbumDescListener;
import com.crixmod.sailorcast.siteapi.SiteApi;
import com.crixmod.sailorcast.uiutils.BaseToolbarActivity;
import com.crixmod.sailorcast.uiutils.SlidingTabLayout;
import com.crixmod.sailorcast.utils.ImageTools;
import com.crixmod.sailorcast.view.fragments.AlbumPlayGridFragment;

public class AlbumDetailActivity extends BaseToolbarActivity implements
        OnGetAlbumDescListener, AlbumPlayGridFragment.OnAlbumPlayGridListener
{

    private SCAlbum mAlbum;
    private SCVideo mCurrentVideo;
    private int mVideoInAlbum; /* start from 0 */


    private Button mPlaySuperButton;
    private Button mPlayNorButton;
    private Button mPlayHighButton;
    private Button mDlnaSuperButton;
    private Button mDlnaHighButton;
    private Button mDlnaNorButton;

    private int mInitialVideoNoInAlbum = 0;
    private boolean mIsShowTitle = false;
    private BookmarkDbHelper mBookmarkDb;
    private HistoryDbHelper mHistoryDb;
    private boolean mIsFav;
    private Fragment mFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlbum = getIntent().getParcelableExtra("album");
        mInitialVideoNoInAlbum = getIntent().getIntExtra("videoNo",0);
        SiteApi.doGetAlbumDesc(mAlbum, this);
        findViews();
        setTitle(mAlbum.getTitle());
        mBookmarkDb = new BookmarkDbHelper(this);
        mHistoryDb = new HistoryDbHelper(this);
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

    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_album_detail;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem fave = menu.findItem(R.id.action_fav_button);
        MenuItem unfave = menu.findItem(R.id.action_unfav_button);

        MenuItem showTitle = menu.findItem(R.id.action_display_title);
        MenuItem showButton = menu.findItem(R.id.action_display_button);
        fave.setVisible(mIsFav);
        unfave.setVisible(!mIsFav);

        if(mAlbum.getVideosCount() <= 1) {
            showTitle.setVisible(false);
            showButton.setVisible(false);
        } else {
            showTitle.setVisible(true);
            showButton.setVisible(true);
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_album_detail, menu);
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
                mIsShowTitle = false;
            }
            return true;
        }
        if (id == R.id.action_display_title) {
            if ( mIsShowTitle == false ) {
                mIsShowTitle = true;
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
        Intent mpdIntent = new Intent(activity, AlbumDetailActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra("album",album);

        activity.startActivity(mpdIntent);
    }


    public static void launch(Activity activity, SCAlbum album, int videoNo) {
        Intent mpdIntent = new Intent(activity, AlbumDetailActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra("videoNo",videoNo)
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

        if(album.getVerImageUrl() != null) {
            ImageTools.displayImage(albumImage, album.getVerImageUrl());
        } else if(album.getHorImageUrl() != null) {
            ImageTools.displayImage(albumImage,album.getHorImageUrl());
        }
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
        mAlbum = album;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fillAlbumDescView(album);
                invalidateOptionsMenu();

                mFragment = AlbumPlayGridFragment.newInstance(mAlbum,mIsShowTitle);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, mFragment);
                ft.commit();
                getFragmentManager().executePendingTransactions();
            }
        });

    }

    @Override
    public void onGetAlbumDescFailed(String failReason) {

    }

    @Override
    public void onVideoSelected(SCVideo v, int videoNoInAlbum) {

    }
}
