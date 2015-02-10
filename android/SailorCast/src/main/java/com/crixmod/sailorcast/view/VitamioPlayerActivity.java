package com.crixmod.sailorcast.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.SailorCast;
import com.crixmod.sailorcast.model.SCVideo;
import com.crixmod.sailorcast.utils.ImageTools;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;

public class VitamioPlayerActivity extends Activity
        implements OnBufferingUpdateListener,
        MediaPlayer.OnInfoListener,
        OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener, SurfaceHolder.Callback,
        VideoControllerView.MediaPlayerControl {

    private static final String TAG = "MediaPlayerDemo";
    private int mVideoWidth;
    private int mVideoHeight;
    private MediaPlayer mMediaPlayer;
    private SurfaceView mPreview;
    private SurfaceHolder holder;
    private String path;
    private Bundle extras;
    private static final String MEDIA = "media";
    private static final String VIDEO = "video";
    private boolean mIsVideoSizeKnown = false;
    private boolean mIsVideoReadyToBePlayed = false;
    private VideoControllerView controller;
    private boolean needResume;
    private LinearLayout mLoadingView;
    private TextView mLoadingPercentView;
    private RelativeLayout mVideoTitleView;
    private TextView mVideoTitle;
    private SCVideo mVideo;
    private Handler mHandler;
    private static final int    FADE_OUT = 1;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        if (!LibsChecker.checkVitamioLibs(this))
            return;
        setContentView(R.layout.activity_vitamio_player);
        mPreview = (SurfaceView) findViewById(R.id.surface);
        holder = mPreview.getHolder();
        holder.addCallback(this);
        holder.setFormat(PixelFormat.RGBA_8888);
        extras = getIntent().getExtras();
        mVideo = extras.getParcelable(VIDEO);
        mLoadingView = (LinearLayout) findViewById(R.id.loading);
        mLoadingView.setVisibility(View.INVISIBLE);
        mLoadingPercentView = (TextView) findViewById(R.id.loading_percent);
        mVideoTitleView = (RelativeLayout) findViewById(R.id.video_title_container);
        mVideoTitleView.setVisibility(View.GONE);
        mVideoTitle = (TextView) findViewById(R.id.video_title);
        mVideoTitle.setText(mVideo.getVideoTitle());
        mHandler = new MessageHandler(mVideoTitleView);

        ImageView close = (ImageView) findViewById(R.id.video_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private static class MessageHandler extends Handler {
        private final WeakReference<RelativeLayout> mView;

        MessageHandler(RelativeLayout view) {
            mView = new WeakReference<RelativeLayout>(view);
        }
        @Override
        public void handleMessage(Message msg) {
            RelativeLayout view = mView.get();
            if (view == null) {
                return;
            }
            switch (msg.what) {
                case FADE_OUT:
                    view.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private void playVideo() {
        doCleanUp();
        try {
            // Create a new media player and set the listeners
            mMediaPlayer = new MediaPlayer(this);
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.setDisplay(holder);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnInfoListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);
            controller = new VideoControllerView(this);
            setVolumeControlStream(AudioManager.STREAM_MUSIC);

        } catch (Exception e) {
            Log.e(TAG, "error: " + e.getMessage(), e);
        }
    }

    private  void showTitle() {
        if(mVideoTitleView != null) {
            mVideoTitleView.setVisibility(View.VISIBLE);
            int timeout = 3000;
            if(mHandler != null) {
                Message msg = mHandler.obtainMessage(FADE_OUT);
                mHandler.removeMessages(FADE_OUT);
                mHandler.sendMessageDelayed(msg, timeout);
            }
        }
    }

    private void hideTitle() {
        if(mVideoTitleView != null) {
            mVideoTitleView.setVisibility(View.GONE);
        }
    }

    private void toggleControlsVisibility()  {
    if (controller.isShowing()) {
        controller.hide();
        hideTitle();
    } else {
        controller.show();
        showTitle();
    }
  }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            toggleControlsVisibility();
        }
        return true;
    }

    public void onBufferingUpdate(MediaPlayer arg0, int percent) {
        mLoadingPercentView.setText("" + percent + "%");
    }

    public void onCompletion(MediaPlayer arg0) {
        finish();
    }

    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        if (width == 0 || height == 0) {
            Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
            MobclickAgent.reportError(this, "invalid video width(" + width + ") or " +
                    "height(" + height + ")" + " video: " + mVideo.toJson());
            return;
        }
        mIsVideoSizeKnown = true;
        mVideoWidth = width;
        mVideoHeight = height;
        int ScreenWidth = ImageTools.getScreenWidthPixels(this);
        mVideoHeight = Math.round( (float)height/(float)width  * (float)ScreenWidth);
        mVideoWidth = ScreenWidth;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideoPlayback();
        }
    }

    public void onPrepared(MediaPlayer mediaplayer) {
        mIsVideoReadyToBePlayed = true;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideoPlayback();

            controller.setMediaPlayer(this);
            controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        }
    }

    public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {

    }

    public void surfaceDestroyed(SurfaceHolder surfaceholder) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        path = extras.getString(MEDIA);
        playVideo();

    }

    public static void launch(Activity fromActivity,SCVideo video, String url) {
        Intent mpdIntent = new Intent(fromActivity, VitamioPlayerActivity.class)
                .putExtra(VIDEO,video)
                .putExtra(MEDIA, url);
        fromActivity.startActivity(mpdIntent);
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
        releaseMediaPlayer();
        doCleanUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
        doCleanUp();
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void doCleanUp() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        mIsVideoReadyToBePlayed = false;
        mIsVideoSizeKnown = false;
    }

    private void startVideoPlayback() {
        holder.setFixedSize(mVideoWidth, mVideoHeight);
        mMediaPlayer.start();
    }

    @Override
    public void start() {
        if (mMediaPlayer != null) {
            if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
                startVideoPlayback();
            }
        }
        showTitle();
    }

    @Override
    public void pause() {
        if (mMediaPlayer != null) {
            if(mMediaPlayer.isPlaying())
                mMediaPlayer.pause();
        }
        showTitle();

    }

    @Override
    public int getDuration() {
        if (mMediaPlayer != null)
            return (int) mMediaPlayer.getDuration();
        else
            return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (mMediaPlayer != null)
            return (int) mMediaPlayer.getCurrentPosition();
        else
            return 0;
    }

    @Override
    public void seekTo(int pos) {
        if (mMediaPlayer != null)
            mMediaPlayer.seekTo(pos);
        showTitle();
    }

    @Override
    public boolean isPlaying() {
        if (mMediaPlayer != null)
            return mMediaPlayer.isPlaying();
        else
            return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        return true;
    }

    @Override
    public void toggleFullScreen() {

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                //Begin buffer, pause playing
                if (isPlaying()) {
                    stopPlayer();
                    needResume = true;
                }
                mLoadingView.setVisibility(View.VISIBLE);
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                //The buffering is done, resume playing
                if (needResume)
                    startPlayer();
                mLoadingView.setVisibility(View.GONE);
                break;
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                //Display video download speed
                //Log.d("fire3","download rate:" + extra);
                break;
        }
        return true;
    }

    private void startPlayer() {
        if (mMediaPlayer != null)
            mMediaPlayer.start();
    }

    private void stopPlayer() {
        if (mMediaPlayer != null)
            mMediaPlayer.stop();
    }
}
