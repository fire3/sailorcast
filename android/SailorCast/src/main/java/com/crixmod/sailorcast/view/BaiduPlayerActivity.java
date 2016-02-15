package com.crixmod.sailorcast.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.cyberplayer.core.BVideoView;
import com.baidu.cyberplayer.core.BVideoView.OnCompletionListener;
import com.baidu.cyberplayer.core.BVideoView.OnErrorListener;
import com.baidu.cyberplayer.core.BVideoView.OnInfoListener;
import com.baidu.cyberplayer.core.BVideoView.OnPlayingBufferCacheListener;
import com.baidu.cyberplayer.core.BVideoView.OnPreparedListener;
import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.SCLiveStream;
import com.crixmod.sailorcast.model.SCVideo;
import com.crixmod.sailorcast.utils.BrightControl;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class BaiduPlayerActivity extends Activity implements OnPreparedListener, OnCompletionListener,
        OnErrorListener, OnInfoListener, OnPlayingBufferCacheListener, VideoControllerView.MediaPlayerControl, GestureDetectorController.IGestureListener {
	
	private final String TAG = "BaiduPlayerActivity";

    GestureDetectorController  gestureController;

    AudioManager mAudioManager;
    TextView mDragProgressTextView;
    TextView mDragVerticalTextView;
    ImageView mPauseView;
    long mScrollProgress;
    private int mCurrentLight;
    private int mCurrentVolume;
    private int mMaxVolume;
    boolean manualStop = false;
    /**
	 * 记录播放位置
	 */
	private int mLastPos = 0;
    private VideoControllerView controller;
    private boolean isPlaying = false;
    private boolean isMove;
    private boolean isHorizontalScroll;
    private boolean isVerticalScroll;

    @Override
    public void start() {
        if(mVV != null) {
            mVV.resume();
            isPlaying = true;
            if(mPauseView != null)
                mPauseView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void pause() {
        if(mVV != null) {
            mVV.pause();
            isPlaying = false;
            if(mPauseView != null)
                mPauseView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getDuration() {
        if(mVV != null)
            return mVV.getDuration()*1000;
        else
            return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(mVV != null)
            return mVV.getCurrentPosition()*1000;
        else
            return 0;
    }

    @Override
    public void seekTo(int pos) {
        if(mVV != null)
            mVV.seekTo(pos/1000);

    }

    @Override
    public boolean isPlaying() {
        if(mVV != null)
            return isPlaying;
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
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void toggleFullScreen() {

    }

    @Override
    public void showTitleView() {
        showTitle();
    }

    @Override
    public void hideTitleView() {
        hideTitle();
    }

    private void setTextTopDrawables(TextView paramTextView, int paramInt, Context paramContext)
    {
        Drawable localDrawable = paramContext.getResources().getDrawable(paramInt);
        localDrawable.setBounds(0, 0, localDrawable.getMinimumWidth(), localDrawable.getMinimumHeight());
        paramTextView.setCompoundDrawables(null, localDrawable, null, null);
    }


    private String changeDoubleToPercent(double paramDouble)
    {
        NumberFormat localNumberFormat = NumberFormat.getPercentInstance();
        localNumberFormat.setMaximumFractionDigits(0);
        return localNumberFormat.format(paramDouble);
    }

    private void updateVerticalTextView(int paramInt1, int paramInt2)
    {
        String str = changeDoubleToPercent((double) paramInt1 / (double) paramInt2);
        mDragVerticalTextView.setText(str);
    }

    @Override
    public void onScrollBegin(GestureDetectorController.ScrollViewType paramScrollViewType) {
        isMove = true;
        if (controller.isShowing()) {
            controller.hide();
        }

        if(paramScrollViewType == GestureDetectorController.ScrollViewType.HORIZONTAL) {
            mDragProgressTextView.setVisibility(View.VISIBLE);
            mScrollProgress = -1;
            isHorizontalScroll = true;
        }

        if (paramScrollViewType == GestureDetectorController.ScrollViewType.VERTICAL_LEFT)
        {
            setTextTopDrawables(mDragVerticalTextView, R.drawable.light, this);
            mDragVerticalTextView.setVisibility(View.VISIBLE);
            updateVerticalTextView(mCurrentLight, 255);
            isVerticalScroll = true;
        }

        if (paramScrollViewType == GestureDetectorController.ScrollViewType.VERTICAL_RIGHT)
        {
            if(mCurrentVolume > 0)
                setTextTopDrawables(mDragVerticalTextView, R.drawable.nonmute, this);
            else
                setTextTopDrawables(mDragVerticalTextView, R.drawable.mute, this);
            mDragVerticalTextView.setVisibility(View.VISIBLE);
            updateVerticalTextView(mCurrentVolume, mMaxVolume*10);
            isVerticalScroll = true;
        }
    }

    @Override
    public void onScrollHorizontal(float paramFloat1, float paramFloat2) {
        int width = this.getResources().getDisplayMetrics().widthPixels;
        int SLIDE_DISTANCE = 600000;

        int i = (int)(paramFloat2 / width * (SLIDE_DISTANCE / 2)) + mVV.getCurrentPosition()*1000;
        long j = Math.max(0, Math.min(mVV.getDuration()*1000, i));
        this.mScrollProgress = j;

        updateProgressTextView(j);
    }

    public static String formatTime(long paramInt)
    {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("H:mm:ss", Locale.CHINA);
        localSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        return localSimpleDateFormat.format(new Date(paramInt));
    }


    private void updateProgressTextView(long paramInt)
    {
        String str = formatTime(paramInt) + "/" + formatTime(this.mVV.getDuration() * 1000);
        mDragProgressTextView.setText(str);
    }

    @Override
    public void onScrollLeft(float paramFloat1, float paramFloat2) {
        int height = this.getResources().getDisplayMetrics().heightPixels;
        int i = (int)(255.0F * paramFloat1 / height);
        if (Math.abs(i) > 0) {
            this.mCurrentLight = (i + this.mCurrentLight);
            this.mCurrentLight = Math.max(0, Math.min(255, this.mCurrentLight));
            Context localContext = this;
            int j = this.mCurrentLight;
            BrightControl.setScreenBrightness(localContext, j);
            SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(localContext).edit();
            localEditor.putInt("shared_preferences_light", j);
            localEditor.commit();
            updateVerticalTextView(this.mCurrentLight, 255);
        }
    }

    @Override
    public void onScrollRight(float paramFloat1, float paramFloat2) {

        int height = this.getResources().getDisplayMetrics().heightPixels;
        int i = (int)((float)mMaxVolume*10 * paramFloat1 / height);
        if (Math.abs(i) > 0) {
            mCurrentVolume = (i + mCurrentVolume);
            mCurrentVolume = Math.max(0, Math.min(mMaxVolume*10, mCurrentVolume));
            int j = mCurrentVolume/10;
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,j,0);

            updateVerticalTextView(this.mCurrentVolume, mMaxVolume*10);

            if(mCurrentVolume > 0)
                setTextTopDrawables(mDragVerticalTextView, R.drawable.nonmute, this);
            else
                setTextTopDrawables(mDragVerticalTextView, R.drawable.mute, this);
        }
    }

    /**
	 * 播放状态
	 */
	private  enum PLAYER_STATUS {
		PLAYER_IDLE, PLAYER_PREPARING, PLAYER_PREPARED,
	}
	
	private PLAYER_STATUS mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
	
	private BVideoView mVV = null;
	
	private EventHandler mEventHandler;
	private HandlerThread mHandlerThread;
	
	private final Object SYNC_Playing = new Object();
	
	private WakeLock mWakeLock = null;
	private static final String POWER_LOCK = "VideoViewPlayingActivity";
	

	private final int EVENT_PLAY = 0;

    private String mURL;
    private Bundle extras;
    private static final String MEDIA = "media";
    private static final String STREAM = "stream";
    private static final String VIDEO = "video";
    private static final String HWDECODE = "hwdecode";
    private static final String INITPOS = "initpos";
    private LinearLayout mLoadingView;
    private TextView mLoadingPercentView;
    private RelativeLayout mVideoTitleView;
    private TextView mVideoTitle;
    private SCVideo mVideo;
    private SCLiveStream mStream;
    //private Handler mHandler;
    //private static final int    FADE_OUT = 1;
    private boolean mIsHardDecode = false;
    private CheckBox mHDCheckBox;
    private long mInitialPosition = 0;

    /*
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
    */

	class EventHandler extends Handler {
		public EventHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case EVENT_PLAY:
				/**
				 * 如果已经播放了，等待上一次播放结束
				 */
				if (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE) {
					synchronized (SYNC_Playing) {
						try {
							SYNC_Playing.wait();
							Log.v(TAG, "wait player status to idle");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

				/**
				 * 设置播放url
				 */
				mVV.setVideoPath(mURL);
                Log.i("fire3","playing: " + mURL);
				
				/**
				 * 续播，如果需要如此
				 */
				if (mLastPos > 0) {
					mVV.seekTo(mLastPos);
					mLastPos = 0;
				}


				/**
				 * 开始播放
				 */
				mVV.start();
                isPlaying = true;

				mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARING;
				break;
			default:
				break;
			}
		}
	}

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
		super.onCreate(savedInstanceState);

        gestureController = new GestureDetectorController(this,this);
        gestureController.setGestureListener(this);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
		setContentView(R.layout.activity_baidu_player);

        controller = new VideoControllerView(this);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, POWER_LOCK);
        mCurrentLight = BrightControl.getSharedPreferencesLight(this);
        if(mCurrentLight == -1) {
            mCurrentLight = BrightControl.getBrightness(this);
        }



		initUI();
		
		/**
		 * 开启后台事件处理线程
		 */
		mHandlerThread = new HandlerThread("event handler thread",
				Process.THREAD_PRIORITY_BACKGROUND);
		mHandlerThread.start();
		mEventHandler = new EventHandler(mHandlerThread.getLooper());

        extras = getIntent().getExtras();
        mURL = extras.getString(MEDIA);
        mVideo = extras.getParcelable(VIDEO);
        String mStreamString = extras.getString(STREAM);

        if(mStreamString != null && !mStreamString.isEmpty())
            mStream = SCLiveStream.fromJson(mStreamString);



        mIsHardDecode = extras.getBoolean(HWDECODE, false);
        mInitialPosition = extras.getLong(INITPOS, 0);
        mLoadingView = (LinearLayout) findViewById(R.id.loading);
        mLoadingView.setVisibility(View.INVISIBLE);
        mLoadingPercentView = (TextView) findViewById(R.id.loading_percent);
        mVideoTitleView = (RelativeLayout) findViewById(R.id.video_title_container);
        mHDCheckBox = (CheckBox) findViewById(R.id.open_harddecode_checkbox);
        mHDCheckBox.setChecked(mIsHardDecode);
        mVideoTitleView.setVisibility(View.GONE);
        mVideoTitle = (TextView) findViewById(R.id.video_title);
        if(mVideo != null)
            mVideoTitle.setText(mVideo.getVideoTitle());
        if(mStream != null)
            mVideoTitle.setText(mStream.getChannelName());
        //mHandler = new MessageHandler(mVideoTitleView);
        final ImageView mVideoClose = (ImageView) findViewById(R.id.video_close);
        LinearLayout mClose = (LinearLayout) findViewById(R.id.video_close_container);

        mClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoClose.requestFocus();
                mVideoClose.setPressed(true);
                mVideoTitle.requestFocus();
                mVideoTitle.setPressed(true);
                finish();
            }
        });



        mDragProgressTextView = (TextView) findViewById(R.id.dragProgressTextView);
        mDragVerticalTextView = (TextView) findViewById(R.id.dragVerticalTextView);

        mPauseView = (ImageView) findViewById(R.id.pauseImage);
        mPauseView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPlayerStatus == PLAYER_STATUS.PLAYER_PREPARED) {
                    if(mVV != null)
                        start();
                }
            }
        });
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        final Activity from = this;
        mHDCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIsHardDecode = isChecked;
                if (mPlayerStatus == PLAYER_STATUS.PLAYER_PREPARED) {
                    mLastPos = mVV.getCurrentPosition();
                    controller.hide();
                    controller.setMediaPlayer(null);
                    controller.setEnabled(false);
                    manualStop = true;
                    mVV.stopPlayback();
                    Log.d("fire3","setDecode: " + mIsHardDecode);
                    mVV.setDecodeMode(mIsHardDecode ? BVideoView.DECODE_HW : BVideoView.DECODE_SW);
                    if(mIsHardDecode == true)
                        Toast.makeText(from,R.string.hard_decode_info,Toast.LENGTH_SHORT).show();
                    if(mIsHardDecode == false)
                        Toast.makeText(from,R.string.soft_decode_info,Toast.LENGTH_SHORT).show();
                    mEventHandler.sendEmptyMessage(EVENT_PLAY);
                }
            }
        });

        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
	}
	
	/**
	 * 初始化界面
	 */
	private void initUI() {

        String AK = "r8A8URv8tItyRpW64GEakxcf";
        String SK = "ekzuetBpzaofmh7z";
        //String USER_AGENT = "Python-urllib/3.4";
		/**
		 * 设置ak及sk的前16位
		 */
		BVideoView.setAKSK(AK, SK);
		
		/**
		 *获取BVideoView对象
		 */
		mVV = (BVideoView) findViewById(R.id.video_view);
        //mVV.setUserAgent(USER_AGENT);
		/**
		 * 注册listener
		 */
		mVV.setOnPreparedListener(this);
		mVV.setOnCompletionListener(this);
		mVV.setOnErrorListener(this);
		mVV.setOnInfoListener(this);

        mVV.showCacheInfo(true);
		
		/**
		 * 设置解码模式
		 */
		mVV.setDecodeMode(mIsHardDecode? BVideoView.DECODE_HW: BVideoView.DECODE_SW);
	}

	
	@Override
	protected void onPause() {
        Log.v(TAG, "onPause");
        if(mPauseView != null)
            mPauseView.setVisibility(View.INVISIBLE);
		super.onPause();
        controller.setEnabled(false);
        controller.setMediaPlayer(null);
        mAudioManager.abandonAudioFocus(null);
		/**
		 * 在停止播放前 你可以先记录当前播放的位置,以便以后可以续播
		 */
		if (mPlayerStatus == PLAYER_STATUS.PLAYER_PREPARED) {
			mLastPos = mVV.getCurrentPosition();
			mVV.stopPlayback();
            manualStop = true;
		}
        if (null != mWakeLock && (mWakeLock.isHeld())) {
            mWakeLock.release();
        }
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v(TAG, "onResume");
        mAudioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        mCurrentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)*10;
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		if (null != mWakeLock && (!mWakeLock.isHeld())) {
			mWakeLock.acquire();
		}
		/**
		 * 发起一次播放任务,当然您不一定要在这发起
		 */
		mEventHandler.sendEmptyMessage(EVENT_PLAY);
	}

    private  void showTitle() {
        if(mVideoTitleView != null) {
            mVideoTitleView.setVisibility(View.VISIBLE);
            /*
            int timeout = 3000;
            if(mHandler != null) {
                Message msg = mHandler.obtainMessage(FADE_OUT);
                mHandler.removeMessages(FADE_OUT);
                mHandler.sendMessageDelayed(msg, timeout);
            }
            */
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
    } else {
        controller.show();
    }
  }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if(isMove == false)
                toggleControlsVisibility();

            if(isMove == true) {
                isMove = false;
            }


            if(isHorizontalScroll == true) {
                isHorizontalScroll = false;
                mVV.seekTo(mScrollProgress / 1000);
                mDragProgressTextView.setVisibility(View.INVISIBLE);
            }

            if(isVerticalScroll ==  true) {
                isVerticalScroll = false;
                mDragVerticalTextView.setVisibility(View.INVISIBLE);
            }

        }
        return this.gestureController.onTouchEvent(event);
    }

	
	@Override
	protected void onStop(){
        Log.d(TAG,"onStop");
		super.onStop();
	}
	
	@Override
	protected void onDestroy(){
        Log.d(TAG,"onDestroy");
		super.onDestroy();
		/**
		 * 退出后台事件处理线程
		 */
		mHandlerThread.quit();
	}

	@Override
	public boolean onInfo(int what, int extra) {
		switch(what){
		/**
		 * 开始缓冲
		 */
		case BVideoView.MEDIA_INFO_BUFFERING_START:
			break;
		/**
		 * 结束缓冲
		 */
		case BVideoView.MEDIA_INFO_BUFFERING_END:
			break;
		default:
			break;
		}
		return true;
	}
	
	/**
	 * 当前缓冲的百分比， 可以配合onInfo中的开始缓冲和结束缓冲来显示百分比到界面
	 */
	@Override
	public void onPlayingBufferCache(int percent) {
        mLoadingPercentView.setText("" + percent + "%");
	}
	
	/**
	 * 播放出错
	 */
	@Override
	public boolean onError(int what, int extra) {
		Log.v(TAG, "onError");
		synchronized (SYNC_Playing) {
			SYNC_Playing.notify();
		}
		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
		return true;
	}

	/**
	 * 播放完成
	 */
	@Override
	public void onCompletion() {
		Log.v(TAG, "onCompletion");
		synchronized (SYNC_Playing) {
			SYNC_Playing.notify();
		}
		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;

        if(manualStop == false) {
            Log.d(TAG,"finish");
            finish();
        }

        manualStop = false;

	}

	/**
	 * 准备播放就绪
	 */
	@Override
	public void onPrepared() {
		Log.v(TAG, "onPrepared");
		mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARED;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                controller.setEnabled(true);
                controller.setMediaPlayer(BaiduPlayerActivity.this);
            }
        });
	}



    public static void launch(Activity fromActivity,SCLiveStream stream, String url) {
        Intent mpdIntent = new Intent(fromActivity, BaiduPlayerActivity.class)
                .putExtra(STREAM, stream.toJson())
                .putExtra(MEDIA, url);
        fromActivity.startActivity(mpdIntent);
    }

    public static void launch(Activity fromActivity,SCVideo video, String url) {
        Intent mpdIntent = new Intent(fromActivity, BaiduPlayerActivity.class)
                .putExtra(VIDEO, video)
                .putExtra(MEDIA, url);
        fromActivity.startActivity(mpdIntent);
    }

    public static void launch(Activity fromActivity,SCVideo video, String url, boolean isHWDecode, long initialPos) {
        Intent mpdIntent = new Intent(fromActivity, BaiduPlayerActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION|Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra(INITPOS,initialPos)
                .putExtra(HWDECODE, isHWDecode)
                .putExtra(VIDEO, video)
                .putExtra(MEDIA, url);
        fromActivity.startActivity(mpdIntent);
    }

}
