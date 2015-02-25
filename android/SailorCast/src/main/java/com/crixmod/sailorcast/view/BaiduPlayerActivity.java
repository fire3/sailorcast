package com.crixmod.sailorcast.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.baidu.cyberplayer.core.BVideoView;
import com.baidu.cyberplayer.core.BVideoView.OnCompletionListener;
import com.baidu.cyberplayer.core.BVideoView.OnErrorListener;
import com.baidu.cyberplayer.core.BVideoView.OnInfoListener;
import com.baidu.cyberplayer.core.BVideoView.OnPlayingBufferCacheListener;
import com.baidu.cyberplayer.core.BVideoView.OnPreparedListener;
import com.crixmod.sailorcast.R;
import com.crixmod.sailorcast.model.SCVideo;

import java.lang.ref.WeakReference;

public class BaiduPlayerActivity extends Activity implements OnPreparedListener, OnCompletionListener,
        OnErrorListener, OnInfoListener, OnPlayingBufferCacheListener, VideoControllerView.MediaPlayerControl {
	
	private final String TAG = "VideoViewPlayingActivity";

	/**
	 * 记录播放位置
	 */
	private int mLastPos = 0;
    private VideoControllerView controller;
    private boolean isPlaying = false;

    @Override
    public void start() {
        if(mVV != null) {
            mVV.resume();
            isPlaying = true;
        }
    }

    @Override
    public void pause() {
        if(mVV != null) {
            mVV.pause();
            isPlaying = false;
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
    private static final String VIDEO = "video";
    private static final String HWDECODE = "hwdecode";
    private static final String INITPOS = "initpos";
    private LinearLayout mLoadingView;
    private TextView mLoadingPercentView;
    private RelativeLayout mVideoTitleView;
    private TextView mVideoTitle;
    private SCVideo mVideo;
    private Handler mHandler;
    private static final int    FADE_OUT = 1;
    private boolean mIsHardDecode = false;
    private CheckBox mHDCheckBox;
    private long mInitialPosition = 0;

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
		super.onCreate(savedInstanceState);
				
		setContentView(R.layout.activity_baidu_player);

        controller = new VideoControllerView(this);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, POWER_LOCK);
		

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
        mVideoTitle.setText(mVideo.getVideoTitle());
        mHandler = new MessageHandler(mVideoTitleView);
        final ImageView mVideoClose = (ImageView) findViewById(R.id.video_close);

        mVideoTitleView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoTitle.setPressed(true);
                mVideoClose.setPressed(true);
                finish();
            }
        });
        /*
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoTitleView.setPressed(true);
                finish();
            }
        });

        mVideoTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close.setPressed(true);
                finish();

            }
        });
        */

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
                    mVV.stopPlayback();
                    Log.d("fire3","setDecode: " + mIsHardDecode);
                    mVV.setDecodeMode(mIsHardDecode? BVideoView.DECODE_HW: BVideoView.DECODE_SW);
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
		/**
		 * 设置ak及sk的前16位
		 */
		BVideoView.setAKSK(AK, SK);
		
		/**
		 *获取BVideoView对象
		 */
		mVV = (BVideoView) findViewById(R.id.video_view);
		
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
		super.onPause();
		/**
		 * 在停止播放前 你可以先记录当前播放的位置,以便以后可以续播
		 */
		if (mPlayerStatus == PLAYER_STATUS.PLAYER_PREPARED) {
			mLastPos = mVV.getCurrentPosition();
			mVV.stopPlayback();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v(TAG, "onResume");
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

	
	@Override
	protected void onStop(){
		super.onStop();
	}
	
	@Override
	protected void onDestroy(){
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
	}

	/**
	 * 准备播放就绪
	 */
	@Override
	public void onPrepared() {
		Log.v(TAG, "onPrepared");
		mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARED;

        controller.setEnabled(true);
        controller.setMediaPlayer(this);
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
