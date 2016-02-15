package com.crixmod.sailorcast.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.SailorCast;
import com.crixmod.sailorcast.database.BookmarkDbHelper;
import com.crixmod.sailorcast.database.HistoryDbHelper;
import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCFailLog;
import com.crixmod.sailorcast.model.SCVideo;
import com.crixmod.sailorcast.model.upnp.CallableRendererFilter;
import com.crixmod.sailorcast.model.upnp.IRendererCommand;
import com.crixmod.sailorcast.model.upnp.IUpnpDevice;
import com.crixmod.sailorcast.siteapi.OnGetAlbumDescListener;
import com.crixmod.sailorcast.siteapi.OnGetVideoPlayUrlListener;
import com.crixmod.sailorcast.siteapi.SiteApi;
import com.crixmod.sailorcast.uiutils.BaseToolbarActivity;
import com.crixmod.sailorcast.utils.ImageTools;
import com.crixmod.sailorcast.view.fragments.AlbumPlayGridFragment;

import java.util.Collection;
import java.util.concurrent.Callable;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.crixmod.sailorcast.R.id.error_message;

public class AlbumDetailActivity extends BaseToolbarActivity implements
        OnGetAlbumDescListener, AlbumPlayGridFragment.OnAlbumPlayGridListener
    , OnGetVideoPlayUrlListener
{

    private SCAlbum mAlbum;
    private SCVideo mCurrentVideo;
    private int mVideoInAlbum; /* start from 0, item position */


    private Button mPlaySuperButton;
    private Button mPlayNorButton;
    private Button mPlayHighButton;
    private Button mDlnaSuperButton;
    private Button mDlnaHighButton;
    private Button mDlnaNorButton;

    private int mInitialVideoNoInAlbum = 0;
    private boolean mIsShowTitle = false;
    private boolean mIsBackward = false;
    private BookmarkDbHelper mBookmarkDb;
    private HistoryDbHelper mHistoryDb;
    private boolean mIsFav;
    private AlbumPlayGridFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlbum = getIntent().getParcelableExtra("album");
        mInitialVideoNoInAlbum = getIntent().getIntExtra("videoNo", 0);
        mIsShowTitle = getIntent().getBooleanExtra("showTitle",false);
        findViews();
        setTitle(mAlbum.getTitle());
        mBookmarkDb = new BookmarkDbHelper(this);
        mHistoryDb = new HistoryDbHelper(this);
        mIsFav =  mBookmarkDb.getAlbumById(mAlbum.getAlbumId(),mAlbum.getSite().getSiteID()) != null;
        SiteApi.doGetAlbumDesc(mAlbum, this);
    }





    @Override
    protected void onDestroy() {
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


        MenuItem backward = menu.findItem(R.id.action_order_backward);
        MenuItem forward = menu.findItem(R.id.action_order_forward);

        fave.setVisible(mIsFav);
        unfave.setVisible(!mIsFav);

        if(mAlbum.getVideosTotal() <= 1) {
            showTitle.setVisible(false);
            showButton.setVisible(false);
            backward.setVisible(false);
            forward.setVisible(false);
        } else {
            showTitle.setVisible(mIsShowTitle);
            showButton.setVisible(!mIsShowTitle);

            forward.setVisible(!mIsBackward);
            backward.setVisible(mIsBackward);
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

        if (id == R.id.action_order_backward) {
            if ( mIsBackward == true) {
                mIsBackward = false;
                mFragment.setBackward(mIsBackward);
                invalidateOptionsMenu();
            }
            return true;
        }

        if (id == R.id.action_order_forward) {
            if (mIsBackward == false) {
                mIsBackward = true;
                mFragment.setBackward(mIsBackward);
                invalidateOptionsMenu();
            }
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_display_button) {
            if ( mIsShowTitle == false ) {
                mIsShowTitle = true;
                mFragment.setShowTitle(mIsShowTitle);
                invalidateOptionsMenu();
            }
            return true;
        }
        if (id == R.id.action_display_title) {
            if ( mIsShowTitle == true ) {
                mIsShowTitle = false;
                mFragment.setShowTitle(mIsShowTitle);
                invalidateOptionsMenu();
            }
            return true;
        }

        if (id == R.id.action_unfav_button) {
            if(mIsFav == false) {
                mBookmarkDb.addAlbum(mAlbum);
                mIsFav = true;
                invalidateOptionsMenu();
                Toast.makeText(this,getResources().getString(R.string.toast_bookmarked),Toast.LENGTH_SHORT).show();
            }
            return true;
        }


        if (id == R.id.action_fav_button) {
            if(mIsFav == true) {
                mBookmarkDb.deleteAlbum(mAlbum.getAlbumId(),mAlbum.getSite().getSiteID());
                Toast.makeText(this,getResources().getString(R.string.toast_unbookmarked),Toast.LENGTH_SHORT).show();
                mIsFav = false;
                invalidateOptionsMenu();
            }
            return true;
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

    public static void launch(Activity activity, SCAlbum album, int videoNo, boolean mIsShowTitle) {
        Intent mpdIntent = new Intent(activity, AlbumDetailActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra("videoNo",videoNo)
                .putExtra("showTitle",mIsShowTitle)
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
                openAlbumDesc();
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

    public void openAlbumDesc() {
        RelativeLayout albumDescContainer = (RelativeLayout) findViewById(R.id.album_desc_container);
        albumDescContainer.setVisibility(View.VISIBLE);
    }

    private void openErrorMsg(String msg) {
        TextView textView = (TextView) findViewById(error_message);
        textView.setText(msg);
        textView.setVisibility(View.VISIBLE);
    }

    private void hideAllPlayButton() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDlnaHighButton.setVisibility(View.GONE);
                mDlnaNorButton.setVisibility(View.GONE);
                mDlnaSuperButton.setVisibility(View.GONE);
                mPlayHighButton.setVisibility(View.GONE);
                mPlayNorButton.setVisibility(View.GONE);
                mPlaySuperButton.setVisibility(View.GONE);
            }
        });

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
                try {
                    mFragment = AlbumPlayGridFragment.newInstance(mAlbum, mIsShowTitle, mIsBackward, mInitialVideoNoInAlbum);
                    mFragment.setShowTitle(mIsShowTitle);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, mFragment);
                    ft.commit();
                    getFragmentManager().executePendingTransactions();
                } catch (Exception e) {
                    e.printStackTrace();
                    finish();
                }

                if(mAlbum.getVideosTotal() == 1) {
                    hideAlbumCloseButton();
                    openAlbumDesc();
                }

                if(mAlbum.getVideosTotal() == 0) {
                    hideAlbumCloseButton();
                    openAlbumDesc();
                    hideAllPlayButton();
                    openErrorMsg(getResources().getString(R.string.album_no_videos));
                }
            }
        });

    }

    @Override
    public void onGetAlbumDescFailed(SCFailLog err) {
    }

    @Override
    public void onAlbumPlayVideoSelected(SCVideo v, int positionInGrid) {
        mCurrentVideo = v;
        mVideoInAlbum = positionInGrid;
        if(mIsBackward == false)
            v.setSeqInAlbum(positionInGrid + 1);
        else
            v.setSeqInAlbum(mAlbum.getVideosTotal() - positionInGrid);
        hideAllPlayButton();
        Log.i("fire3","onAlbumPlayVideoSelected" + v.toJson());
        SiteApi.doGetVideoPlayUrl(v, this);

    }

    @Override
    public void onGetVideoPlayUrlNormal(final SCVideo v, final String urlNormal) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDlnaNorButton.setVisibility(View.VISIBLE);
                mPlayNorButton.setVisibility(View.VISIBLE);


                mPlayNorButton.setTag(R.id.key_video_url, urlNormal);
                mPlayNorButton.setTag(R.id.key_video, v);
                mPlayNorButton.setTag(R.id.key_video_number_in_album, mVideoInAlbum);
                mDlnaNorButton.setTag(R.id.key_video_url, urlNormal);
                mDlnaNorButton.setTag(R.id.key_video, v);
                mDlnaNorButton.setTag(R.id.key_video_number_in_album, mVideoInAlbum);
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
    public void onGetVideoPlayUrlFailed(SCFailLog err) {
        hideAllPlayButton();
    }


    @Override
    protected void onResume() {
        super.onResume();
        SailorCast.upnpServiceController.resume(this);
    }

	@Override
	public void onPause()
	{
		super.onPause();
        SailorCast.upnpServiceController.pause();
        SailorCast.upnpServiceController.getServiceListener().getServiceConnexion().onServiceDisconnected(null);
	}

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void startThirdPartyVideoPlayer(String url) {

        Intent intentVideo = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(url);
        String type="video/*";
        intentVideo.setDataAndType(uri, type);
        startActivity(intentVideo);
    }

    /**
     * 该函数触发播放动作
     * @param button
     */
    public void onPlayButtonClick(View button) {
        final String url = (String) button.getTag(R.id.key_video_url);
        final SCVideo v = (SCVideo) button.getTag(R.id.key_video);
        boolean isWifi = SailorCast.isNetworkWifi();
        final Activity from = this;
        if(url != null) {
            //BaiduPlayerActivity.launch(this,url);
            //VitamioPlayerActivity.launch(this,v,url);
            if(isWifi) {
                mHistoryDb.addHistory(mAlbum, mCurrentVideo, 0);
                BaiduPlayerActivity.launch(from, v, url);
                //startThirdPartyVideoPlayer(url);

            }
            else {
                final SweetAlertDialog pDialog =  new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                pDialog.setTitleText("是否播放");
                pDialog.setContentText("继续播放可能会耗费您的流量");
                pDialog.setConfirmText("继续播放");
                pDialog.setCancelable(true);
                pDialog.setCancelText("取消");
                pDialog.show();
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.hide();
                        mHistoryDb.addHistory(mAlbum, mCurrentVideo, 0);
                        pDialog.hide();
                        BaiduPlayerActivity.launch(from, v, url);
                        //ExoPlayerActivity.launch(from, v, url);
                        //startThirdPartyVideoPlayer(url);
                    }
                });
            }
        }
    }

    /**
     * 该函数触发播放动作
     * @param button
     */
    public void onDlnaButtonClick(View button) {
        final String url = (String) button.getTag(R.id.key_video_url);
        final SCVideo v = (SCVideo) button.getTag(R.id.key_video);

		final Collection<IUpnpDevice> upnpDevices = SailorCast.upnpServiceController.getServiceListener()
				.getFilteredDeviceList(new CallableRendererFilter());
        if(upnpDevices.size() > 0) {

            FragmentManager fm = getSupportFragmentManager();
            RendererDialog dialog = new RendererDialog();
            dialog.setCallback(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    launchRenderer(v, url);
                    return null;
                }
            });
            dialog.show(fm, "Render");
        } else {
            Toast.makeText(this,getResources().getString(R.string.noRenderer),Toast.LENGTH_SHORT).show();
        }
    }


	private void launchRenderer(SCVideo video, String url)
	{
        mHistoryDb.addHistory(mAlbum,video,0);
		IRendererCommand rendererCommand = SailorCast.factory.createRendererCommand(SailorCast.factory.createRendererState());
		rendererCommand.launchSCVideo(video, url);
	}

}
